package slingshot.model;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */


import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import slingshot.Application;
import slingshot.experiment.ProjectLoader;
import slingshot.experiment.ProjectLoader.ProjectState;
import slingshot.spyconsole.SPyConsole;

/**
 * 
 * This class birdges the jython console with a GUI console where users can type and see results.
 * 
 * @author Nathanael Van Vorst
 * @author Eduardo Tibau
 *
 */
public class PythonConsoleHandler{
	public final static String __setexp__ = "exp, topnet, startViz, pauseViz, resumeViz, refresh, focus, selectNode, sel = __resetexp__()";
	public final static String __resetexp__ = "try:\n\tif topnet is None or exp is None: "+__setexp__+"\nexcept NameError:\n\t"+__setexp__;
	    
	/** 
	 * The Python console handler
	 * @author Nathanael Van Vorst
	 * @author Eduardo Tibau
	 */
	private class ConsoleComposite extends Composite {

		/**
		 * @param parent
		 * @param style
		 */
		public ConsoleComposite(Composite parent, int style) {
			super(parent, style);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.widgets.Composite#setFocus()
		 */
		@Override
		public boolean setFocus() {
			if(console!=null) {
				console.requestFocus();
			}
			if(ps!=null && ps.view != null && ps.view.logHandler != null) {
				ps.view.logHandler.focus();
			}
			return super.setFocus();
		}

	}
	private ConsoleComposite consoleComposite=null;
	private SPyConsole console=null;
	private ProjectState ps=null;

	/**
	 * @param parent
	 * @param data
	 * @param experiment
	 */
	public PythonConsoleHandler(Composite parent, GridData data, String experiment) {
		this.ps = ProjectLoader.getProjectState(experiment);
		createPythonConsoleComposite(parent,data);
	}

	/**
	 * @return
	 */
	public Composite getConsoleComposite() {
		return consoleComposite;
	}

	/**
	 * @return
	 */
	public String getExpName(){
		return this.ps.exp.getName();
	}

	/**
	 * @return
	 */
	public SPyConsole getConsole(){
		return this.console;
	}

	/**
	 * 
	 */
	public void close() {
		console.cleanup();
		consoleComposite.dispose();

	}

	/**
	 * @param parent
	 * @param data
	 */
	private void createPythonConsoleComposite(Composite parent, GridData data){
		consoleComposite = new ConsoleComposite(parent, SWT.BORDER);

		consoleComposite.setLayout(new GridLayout());
		consoleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		consoleComposite.setFont(parent.getFont());

		createConsoleComposite(consoleComposite);
	}

	/**
	 * @param parent
	 */
	private void createConsoleComposite(Composite parent){
		try {
			Class.forName("org.python.util.InteractiveConsole");
			Class.forName(Application.db.getDbType().driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		console = new SPyConsole();
		console.setName(getExpName());

		console.addInitCommand("from slingshot.experiment import ProjectLoader");
		console.addInitCommand("from slingshot.experiment import PyExperiment");
		console.addInitCommand("import jprime");
		console.addInitCommand("def __refresh__(exp):\n"
				+"\ttry:\n"
				+"\t\texp.showAttrs()\n"
				+"\t\tpass\n"
				+"\texcept:\n"
				+"\t\tpass\n");
		console.addInitCommand("def __resetexp__():\n"
				+"\texp=ProjectLoader.getProjectState(\""+getExpName()+"\").exp\n"
				+"\ttopnet = exp.getRootNode()\n"
				+"\tstartViz = exp.startVisualization\n"
				+"\tpauseViz = exp.pauseViz\n"
				+"\tresumeViz = exp.resumeViz\n"
				+"\trefresh = lambda : __refresh__(exp)\n"
				+"\tfocus = exp.focus\n"
				+"\tselectNode = lambda x: exp.selectNode(x)\n"
				+"\tsel = exp.selected\n"
				+"\treturn (exp, topnet, startViz, pauseViz, resumeViz, refresh, focus, selectNode, sel)\n");
		console.addInitCommand(__setexp__);
		console.addInitCommand("def getDynamicTraffics():\n"
				+"\treturn exp.getDynamicTraffics()\n");
		console.addInitCommand("def startDynamicTraffic(t) :\n"
				+"\treturn exp.startDynamicTraffic(t)\n"
				+"\tpass\n");
		String dir="";
		dir+="__base_dir=dir()\n";
		dir+="old_dir=dir\n";
		dir+="def dir(obj=None):\n";
		dir+="\tif obj is None: rv = __base_dir\n";
		dir+="\telse: rv = old_dir(obj)\n";
		dir+="\ts='[ '\n";
		dir+="\tfor i in rv:\n";
		dir+="\t\tif not i.startswith('__'):\n";
		dir+="\t\t\tif not s == '[ ': s+=', '\n";
		dir+="\t\t\tif len(s)>80:\n";
		dir+="\t\t\t\tprint s\n";
		dir+="\t\t\t\ts='  '\n";
		dir+="\t\t\ts+=\"'\"+i+\"'\"\n";
		dir+="\t\t\tpass\n";
		dir+="\t\tpass\n";
		dir+="\ts+=' ]'\n";
		dir+="\tprint s\n";
		dir+="\tpass\n";
		console.addInitCommand(dir);

		console.addInitCommand("def help(node):\n"+
				"\tfor s in jprime.ModelNode.getHelpHeader():\n"+
				"\t\tprint s\n"+
				"\tfor s in node.help():\n"+
		"\t\tprint s");

		Composite consoleComposite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
		Frame frame = SWT_AWT.new_Frame(consoleComposite);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(console.getTextPane());
		JScrollPane scroll = new JScrollPane(panel,ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(scroll);

		consoleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		consoleComposite.setFont(parent.getFont());

		console.runShell();
		ps.exp.setConsole(console);
	}

}
