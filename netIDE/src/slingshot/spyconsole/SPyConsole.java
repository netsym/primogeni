package slingshot.spyconsole;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Keymap;
import javax.swing.text.Position;
import javax.swing.text.Style;
import javax.swing.text.StyledEditorKit;

import org.python.core.Options;
import org.python.core.Py;
import org.python.core.PyException;
import org.python.core.PyModule;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.imp;
import org.python.util.InteractiveConsole;

import javax.swing.text.Highlighter;

import slingshot.model.PythonConsoleHandler;

/**
 * SPyConsole Application Developed by Tom Maxwell, maxwell@cbl.umces.edu
 * University of Maryland Institute for Ecological Economics
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * @author Tom Maxwell <maxwell@cbl.umces.edu>
 * @version 1.0
 */
public class SPyConsole extends InteractiveConsole implements Runnable, KeyListener, MouseListener, CaretListener {
        private static boolean firstConsole = true;
        private String name;

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        Styles _styles = new Styles();
        StyledEditorKit.ForegroundAction _inputAction;
        DefaultStyledDocument _document;

        JTextPane _textpane;
        Vector<String> _history = new Vector<String>();
        ArrayList<String> _initCommands = new ArrayList<String>();
        Vector<String> _killRing = new Vector<String>();
        int _oldHistoryLength = 0;
        int _historyPosition = 0;
        int _completionIndex = 0;
        int _killRingPosition = 0;
        int _localNameSpaceCompletionIndex = -1;
        int _completionOffset = -1;
        int _completionCursorOffset;
        String _completionBuffer;
        StringBuffer _buff = new StringBuffer();
        boolean _waitingForInput = false;
        boolean _debug = false;
        Position _startInput;
        Keymap _keymap;
        
        // ming add global variable here to remember current CaretPosition
        int _curPosion = 0; 
        
        // this is used to highlight the selected text
        Highlighter highlight;
        
        private OutputBuffer out = new OutputBuffer(this, "output");
        private OutputBuffer err = new OutputBuffer(this, "error");

        public final Lock lock = new ReentrantLock();
        public final Condition ready = lock.newCondition();

        public SPyConsole() {
                super();
                addStyles();

                _inputAction = new StyledEditorKit.ForegroundAction("start input", Color.black);
                _document = new DefaultStyledDocument(_styles);
                _document.setLogicalStyle(0, _styles.getStyle("normal"));

                _textpane = new JTextPane(_document);

                _textpane.addKeyListener(this);
                // ming add mouse listener here for caret position
                _textpane.addMouseListener(this);
                _textpane.addCaretListener(this);
                
                highlight = _textpane.getHighlighter();
        }

        public void requestFocus(){
                _textpane.requestFocus();
                capturePythonOutput();
        }

        public void setTextPaneSize(int w, int h) {
                _textpane.setSize(w, h);
        }

        public void isReady() {
                lock.lock();
                try {
                        ready.await();
                } catch (InterruptedException e) {
                        e.printStackTrace();
                }
                lock.unlock();
        }

        void addStyles() {
                Style basic = _styles.addBase("normal", 3, 12, "Courier");
                _styles.addDerived("error", basic, Color.red);
                _styles.addDerived("output", basic, Color.blue);
                _styles.addDerived("input", basic, Color.black);
                _styles.addDerived("prompt", basic, Color.magenta);
        }

        public void processKeyPress() {
                int line_start = _startInput.getOffset() + 1;
                int caret_pos = _textpane.getCaretPosition();
                //ming modify below (change "<" to "==" ) to fix SLINGSHOT-13
                //if (caret_pos == line_start) {
                if (caret_pos < line_start) {
                    //_textpane.setCaretPosition(line_start);
                	setCaretPosition(line_start);
                }
        }

        public String raw_input(PyObject prompt) {
                String p = ((PyString) prompt).toString();
                startUserInput(p);
                waitForInput();
                //System.out.println("sl.spyconsole.spyconsole.raw_input():"+getInput());
                return getInput();
        }

        public JTextPane getTextPane() {
                return _textpane;
        }

        /**
         * Writes the given text, in the given style, to the console window.
         *
         * @param text
         *            The text to be displayed on the console
         * @param stylename
         *            The name of the style to use. This should be one of the
         *            following values:
         *
         *            <pre>
         * "error", "input", "output" or "prompt"
         * </pre>
         */
        public void write(String text, String stylename) {
                Style style = _styles.getStyle(stylename);
                try {
                        _document.insertString(_document.getLength(), text, style);
                } catch (BadLocationException err) {
                        if (stylename.equals("error")) {
                                System.out.println("\n" + err.getMessage());
                        } else {
                                error("Text write error", err);
                        }
                }
        }
       
        /**
         * Writes the given message to the console using the "error" style
         *
         * @param label
         *            The basic error mesage
         * @param err
         *            The exception that was raised. The message associated with
         *            this exception will be appended to the
         *
         *            <pre>
         * label
         * </pre>
         *
         *            parameter.
         */
        public void error(String label, Exception err) {
                if (_debug) {
                        err.printStackTrace();
                }
                String msg = label + ": " + err.getMessage();
                try {
                        write(msg, "error");
                } catch (Exception err1) {
                        System.out.println("\n" + msg);
                }
        }

        public void error(String message) {
                write(message, "error");
        }

        public void output(String message) {
        	
                write(message, "output");
        }

        void waitForInput() {
                _waitingForInput = true;
                while (_waitingForInput) {
                        lock.lock();
                        ready.signalAll();
                        lock.unlock();
                        try {
                                Thread.sleep(100L);
                        } catch (InterruptedException e0) {
                                System.out.println("Dentro del catch");
                                break;
                        }
                }
        }

        public void beep() {
                _textpane.getToolkit().beep();
        }

        /**
         * Delete a single character
         *
         * @param dir
         */
        public void deleteChar(int dir) {
                int line_end = _document.getLength();
                int line_start = _startInput.getOffset() + 1;
                int caret_pos = _textpane.getCaretPosition();
                try {
                        switch (dir) {
                        case 1:
                                if (caret_pos < line_end) {
                                        _document.remove(caret_pos, 1);
                                } else
                                        beep();
                                break;
                        case -1:
                                if(caret_pos == line_end && caret_pos != line_start){
                                        _document.remove(caret_pos - 1, 1);
                                } else if (caret_pos > line_start) {
                                        _document.remove(caret_pos - 1, 1);
                                        //ming remove moveCaret(-1); and add code below to fix SLINGSHOT-12
                                        //_textpane.setCaretPosition(caret_pos - 1);
                                        setCaretPosition(caret_pos - 1);
                                        //moveCaret(-1);
                                } else
                                        beep();
                                break;
                        }
                } catch (BadLocationException err) {
                        error("Text write error", err);
                }
        }

        public void moveCaret(int moveType) {
                //ming remove statement below to fix SLINGSHOT-14
                //String line = getInput();
               
                int line_end = _document.getLength();
                int line_start = _startInput.getOffset() + 1;
                int caret_pos = _textpane.getCaretPosition();
               
                switch (moveType) {
                case 1:
                        if (caret_pos < line_end) {
                                //_textpane.setCaretPosition(caret_pos + 1);
                        		setCaretPosition(caret_pos + 1);
                        }
                        break;
                       
                //ming modify case 2 below to fix SLINGSHOT-14 
                case 2:
                        System.err.println("moveCaret(type=2) shouldn't be called!!!");
                        break;
               
                case 3:
                        //_textpane.setCaretPosition(line_end);
                		setCaretPosition(line_end);
                        break;
                       
                //ming modify case -1 below to fix SLINGSHOT-14
                case -1:
                        if (caret_pos >= 1) {
                                //_textpane.setCaretPosition(caret_pos - 1);
                        	setCaretPosition(caret_pos - 1);
                        }
                        break;
                       
                //ming modify case -2 below to fix SLINGSHOT-14
                case -2:
                        System.err.println("moveCaret(type=-2) shouldn't be called!!!");
                        break;
                case -3:
                        //_textpane.setCaretPosition(line_start);
                		setCaretPosition(line_start);
                        break;
                }
        }
       
        public void selectText(int moveType) {
                int line_end = _document.getLength();
                int line_start = _startInput.getOffset() + 1;
                int caret_pos = _textpane.getCaretPosition();
               
                switch (moveType) {
                case 1:
                        if (caret_pos < line_end)
                                _textpane.moveCaretPosition(caret_pos + 1);
                        break;
                case 2:
                        _textpane.moveCaretPosition(line_end);
                        break;
                case -1:
                        if (caret_pos > line_start)
                                _textpane.moveCaretPosition(caret_pos - 1);
                        break;
                case -2:
                        _textpane.moveCaretPosition(line_start);
                        break;
                }
        }

        String trimEnd(String s) {
                int end = s.length() - 1;
                boolean cut = false;
                while ((end >= 0) && Character.isWhitespace(s.charAt(end))) {
                        cut = true;
                        end--;
                }
                return (cut) ? ((end < 0) ? "" : s.substring(0, end + 1)) : s;
        }

        public void replaceInput(String text) {
                int offset = (_completionOffset < 0) ? _startInput.getOffset() + 1
                                : _completionOffset;
                int length = _document.getLength() - offset;
                try {
                        _document.remove(offset, length);
                } catch (BadLocationException err) {
                        error("Text write error", err);
                }
                write(text, "input");
        }

        /**
         * Tell the interpreter to execute the given command.
         *
         * @param cmd
         *            The JPython command to execute.
         */
        public boolean executeCommand(String cmd) {
        		//System.out.println("sl.spyconsole.spyconsole.executeCommand():"+cmd);
        		return executeCommand(cmd, true);
        }

        /**
         * Tell the interpreter to execute the given command.
         *
         * @param cmd
         *            The Python command to execute.
         * @param wait_until_ready
         *            Sedt this to
         *
         *            <pre>
         * true
         * </pre>
         *
         *            if you want this method to block until a response is received
         *            from the inperpreter.
         * @return boolean Returns
         *
         *         <pre>
         * true
         * </pre>
         *
         *         if successful.
         */
        public boolean executeCommand(String cmd, boolean wait_until_ready) {
    		//System.out.println("sl.spyconsole.spyconsole.executeCommand2():"+cmd);

                if (wait_until_ready) {
                        while (!_waitingForInput) {
                                try {
                                        Thread.sleep(100L);
                                } catch (InterruptedException e0) {
                                        break;
                                }
                        }
                }
                if (_waitingForInput) {
                        replaceInput(cmd);
                        write("\n", "input");
                        _waitingForInput = false;
                        return true;
                } else {
                        beep();
                        return false;
                }
        }

        /**
         * This method is used to execute multiple commands in a single String
         * object. This method is usually caqlled from PasteAction and
         * LoadScriptAction.
         *
         * @param cmds
         *            A String of command lines. Each line is trerminated with a
         *            "\n" character.
         * @return boolean Returns
         *
         *         <pre>
         * true
         * </pre>
         *
         *         if all commands executed successfully. Otherwise it returns
         *
         *         <pre>
         * false
         * </pre>
         */
        public boolean executeCommandSet(String cmds) {
    		//System.out.println("sl.spyconsole.spyconsole.executeCommand3():"+cmds);

                boolean result = true; // Assume success
                exec(PythonConsoleHandler.__setexp__);
                try {
                        // read each line of the data
                        StringReader sr = new StringReader(cmds);
                        BufferedReader br = new BufferedReader(sr);
                        String line;
                        while ((line = br.readLine()) != null) {
                                if(line.trim().equals(""))
                                        continue;
                                if (!executeCommand(line)) {
                                        result = false;
                                }
                        }
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
                return result;
        }

        public PyObject compile(java.io.InputStream s, String name) {
        		//System.out.println("sl.spyconsole.spyconsole.compile():"+s+name);
                return Py.compile(s, name, org.python.core.CompileMode.exec);
        }

        public void addInitCommand(String cmd) {
                _initCommands.add(cmd);
        }



        public void capturePythonOutput() {
                this.setOut(out);
                this.setErr(err);
        }

        public void runShell() {
        		System.out.println("SpyConsole.runShell()");
                if (firstConsole) {
                        firstConsole = false;
                        capturePythonOutput();
                }
                getTextPane().requestFocus();

                PyModule mod = imp.addModule("__main__");
                setLocals(mod.__dict__);

                if (Options.importSite) {
                        try {
                                imp.load("site");
                        } catch (PyException pye) {
                                if (!pye.match(Py.ImportError)){
                                        System.err.println("error importing site");
                                        Py.printException(pye);
                                }
                        }
                }


                for(String cmd : _initCommands) {
                        if (_debug) {
                                System.out.println("Processing init cmd: " + cmd);
                        }
                        try {
                                exec(cmd);
                        } catch (Throwable t) {
                                Py.printException(t);
                        }
                }

                this.interactOnThread();
        }

        public String getText() {
                _buff.setLength(0);
                String sep = System.getProperty("line.separator");
                Iterator<String> iter = _history.iterator();
                while (iter.hasNext()) {
                        String line = (String) iter.next();
                        if (line.length() > 0) {
                                _buff.append(line);
                                _buff.append(sep);
                        }
                }
                return _buff.toString();
        }

        public String getInput() {
                int offset = _startInput.getOffset();
                try {
                        String line = trimEnd(_document.getText(offset + 1,
                                        _document.getLength() - offset));
                        registerNewInput(line);
                        return line;
                } catch (BadLocationException err) {
                        error("Text write error", err);
                        return null;
                }
        }

        void registerNewInput(String line) {
                if(!line.trim().equals(""))_history.add(line);
                _historyPosition = _history.size();
                _completionIndex = 0;
                _localNameSpaceCompletionIndex = -1;
                _completionOffset = -1;
                _completionBuffer = null;
        }

        public void startUserInput(String prompt) {
                write(prompt, "prompt");
                try {
                        _startInput = _document.createPosition(_document.getLength() - 1);
                } catch (BadLocationException err) {
                        error("Text write error", err);
                }
                _document.setCharacterAttributes(_document.getLength()-1, 1, _styles.getStyle("input"), true);
                //_textpane.setCaretPosition(_document.getLength());
                setCaretPosition(_document.getLength());
                ActionEvent ae = new ActionEvent(_textpane,
                                ActionEvent.ACTION_PERFORMED, "start input");
                _inputAction.actionPerformed(ae);
        }

        public void interactOnThread() {
                Thread thread = new Thread(this);
                thread.start();
        }

        public void run() {
                this.interact();
        }


        public void getHistory(int direction) {
                int historyLength = _history.size();
                int pos = _historyPosition + direction;
                //ming add definition below to fix SLINGSHOT-14
                int caret_pos = _textpane.getCaretPosition();
                int line_start = _startInput.getOffset() + 1;
               
                //ming add if/else statement here to fix SLINGSHOT-14
		        if (caret_pos >= line_start){
		                  if ((0 <= pos) && (pos < historyLength)) {
		                          _historyPosition = pos;
		                          replaceInput((String) _history.get(pos));
		                  }
		                  else if((direction==1) && (pos == historyLength)){
		                          _historyPosition = pos;
		                          replaceInput("");
		                  }
		                  else {
		                          beep();
		                  }
		        }
		        else {
		                beep();
		        }
		        
		        // modify here to fix history caret bug
		        int line_end = _document.getLength();
		        setCaretPosition(line_end);
        }
       
        //ming implements copy function here and this function can copy the highlight selected string
        public void copyFromClipboard() { 
        	   
        	 Highlighter.Highlight[] high_array = highlight.getHighlights();
        	 Highlighter.Highlight h;
        	 if (high_array == null) 
        		 return;
        	 else if (high_array.length != 1)
        		 return;
        	 else
        		 h = high_array[0];
        	 String s = "";
			try {
				s = _document.getText(h.getStartOffset(), h.getEndOffset() - h.getStartOffset());
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			StringSelection stringSelection = new StringSelection( s );
    	    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	    clipboard.setContents(stringSelection, null);
    	    
    	    highlight.removeAllHighlights();
        }
              
        public void pasteFromClipboard(){
               
                String data = "";

                Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();

                Transferable contents = clip.getContents(null);

                boolean hasTransferableText = (contents != null)
                                && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
               
                if (hasTransferableText) {
                        try {
                                data = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        } catch (UnsupportedFlavorException ex) {
                                // highly unlikely since we are using a standard DataFlavor
                                System.out.println(ex);
                                ex.printStackTrace();
                        } catch (IOException ex) {
                                System.out.println(ex);
                                ex.printStackTrace();
                        }
                }else {
                        System.err.println("Content is not a unicode string? DataFlavor.stringFlavor");
                }

                if (data.indexOf('\n') == -1)
                        _textpane.paste();
                else
                        executeCommandSet(data);
                
                //ming remove all highlight when copy/paste is done
                highlight.removeAllHighlights();
                
        }
       
        @Override
        public void keyPressed(KeyEvent e) {
                if (_debug) System.out.println("DEBUG: keyPressed handled in SPyConsole");
                 e.consume();
                 //ming remove below statement this.processKeyPress(); to fix SLINGSHOT-13
                 //this.processKeyPress();
                 
                 int kc = e.getKeyCode();
                 
                 int ctrl_mask = InputEvent.CTRL_DOWN_MASK | InputEvent.META_DOWN_MASK;
                 int shift_mask = InputEvent.SHIFT_DOWN_MASK;
                 int kmod = (e.getModifiersEx() & (ctrl_mask | shift_mask));
                 
                 if(kc == KeyEvent.VK_ENTER){
                         _waitingForInput = false;
                         //we execute this to make sure topnet/exp/etc are correctly setup.
                         //in some cases they are not correctly populated if prefuse takes too long
                         exec(PythonConsoleHandler.__resetexp__);
                         this.write("\n", "input");
                 }else if(kc == KeyEvent.VK_TAB){
		     //System.out.println("XXX - tab");
                 }else if(kc == KeyEvent.VK_X && (kmod & ctrl_mask) != 0){
                         _textpane.cut();
                 
                 }else if(kc == KeyEvent.VK_C && (kmod & ctrl_mask) != 0){
                         _textpane.copy();
                         copyFromClipboard();
                 
                 }else if(kc == KeyEvent.VK_V && (kmod & ctrl_mask) != 0){
                         pasteFromClipboard();
                 
                 }else if(kc == KeyEvent.VK_HOME && kmod == shift_mask){
                         selectText(-2);
                 
                 }else if(kc == KeyEvent.VK_END && kmod == shift_mask){
                         selectText(2);
                 
                 }else if(kc == KeyEvent.VK_HOME){
                         moveCaret(-3);
                 
                 }else if(kc == KeyEvent.VK_END){
                         moveCaret(3);
                 
                 }else if(kc == KeyEvent.VK_LEFT && kmod == ctrl_mask){
                         System.out.println("TODO - implement ctrl left");
                 
                 }else if(kc == KeyEvent.VK_RIGHT && kmod == ctrl_mask){
                         System.out.println("TODO - implement ctrl right");
                 
                 }else if(kc == KeyEvent.VK_LEFT && kmod == shift_mask){
                         selectText(-1);
                         
                 }else if(kc == KeyEvent.VK_RIGHT && kmod == shift_mask){
                         selectText(1);
                 
                 }else if(kc == KeyEvent.VK_LEFT && kmod == (ctrl_mask | shift_mask)){
                         System.out.println("TODO - implement ctrl-shift left");
                 
                 }else if(kc == KeyEvent.VK_RIGHT && kmod == (ctrl_mask | shift_mask)){
                         System.out.println("TODO - implement ctrl-shift right");
                 
                 }else if(kc == KeyEvent.VK_KP_DOWN || kc == KeyEvent.VK_DOWN){
                         getHistory(1);
                 
                 }else if(kc == KeyEvent.VK_KP_UP || kc == KeyEvent.VK_UP){
                         getHistory(-1);
                 
                 }else if(kc == KeyEvent.VK_KP_LEFT || kc == KeyEvent.VK_LEFT){
                         moveCaret(-1);
                 
                 }else if(kc == KeyEvent.VK_KP_RIGHT || kc == KeyEvent.VK_RIGHT){
                         moveCaret(1);
                         _curPosion =_curPosion + 1;
                 
                 }else if(kc == KeyEvent.VK_DELETE){
                         deleteChar(1);
                 }else if(kc == KeyEvent.VK_BACK_SPACE){
                         deleteChar(-1);
                 }
        }

        @Override
        public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
        }

        @Override
        public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
        	    // ming add statement here for the first time click the previous line when just finish key typed in current line.
        	    _curPosion =_curPosion + 1;
        }

        public boolean isWaitingForInput() {
                return _waitingForInput;
        }

       
        public void setWaitingForInput(boolean _waitingForInput) {
                this._waitingForInput = _waitingForInput;
        }

        @Override
        public boolean push(String line) {
                final boolean rv = super.push(line);
                if(line.contains("set") && !(line.contains("showAttrs")  || line.contains("__refresh__") || line.contains("__resetexp__")))
                         exec("__refresh__(exp)");
                return rv;
        }

        
        // ming implements this function to highlight the selected string when the caret position updated
        // @Override
        public void caretUpdate(CaretEvent e) {
                // TODO Auto-generated method stub

	    //System.out.println();
               
                // highlight the string when do selection
                if (e.getDot() != e.getMark()) {
            		highlight.removeAllHighlights();
            		try {
            			int start, end;
            			if (e.getDot() > e.getMark()) {
            				start = e.getMark();
            				end = e.getDot();
            			}
            			else {
            				start = e.getDot();
            				end = e.getMark();
            			}
            			highlight.addHighlight(start, end, DefaultHighlighter.DefaultPainter);
            		}
            		catch (BadLocationException ble) {
                    }	
                }              
        }
       
      
        /*// ming add new event-dispatching thread method here for caret position
         // this will call the below method
         // displaySelectionInfo(e.getDot(), e.getMark()); 

        protected void displaySelectionInfo(final int dot, final int mark) {
                final int line_end = _document.getLength();
                final int line_start = _startInput.getOffset() + 1;
                final int caret_pos = _textpane.getCaretPosition();
               
                SwingUtilities.invokeLater(new Runnable() {
                        public void run(){
                        	 	//no selection
                                if (dot == mark){
                                        if (caret_pos < line_start){
                                                //_textpane.setCaretPosition(line_end);
                                        	 setCaretPosition(_curPosion);
                                        } 
                                        else if (caret_pos >= line_start && caret_pos <= line_end){
                                        	 setCaretPosition(dot);
                                        }
                                }
                                //do selection
                                else {
                                        if (caret_pos < line_start){
                                            setCaretPosition(_curPosion);
                                        }
                                }
                        }
                });
        }
        */
        
        // ming adds this function to setCaretPosition and record it _curPosion  
        public void setCaretPosition (int pos) {
        	_curPosion = pos;
        	_textpane.setCaretPosition(pos);
        }

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
			//ming add statement here to remove the highlighted text when mouse clicked
			highlight.removeAllHighlights();
		    
			int line_end = _document.getLength();
            int line_start = _startInput.getOffset() + 1;
            int caret_pos = _textpane.getCaretPosition();
            
            if (caret_pos >=line_start && caret_pos <= line_end) { 	
            	setCaretPosition(caret_pos);
            }
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			int line_end = _document.getLength();
            int line_start = _startInput.getOffset() + 1;
            int caret_pos = _textpane.getCaretPosition();
            
            if (caret_pos < line_start || caret_pos > line_end) {
            	setCaretPosition(_curPosion);
            }
		}
}
 
