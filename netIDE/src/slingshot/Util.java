package slingshot;

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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * @author Nathanael Van Vorst
 *
 */
public class Util {
	public static enum Type {
		ERROR,
		WARNING,
		NOTICE
	}
	static class ScrollableDialog extends TitleAreaDialog {
	    private final String msg;
	    private final Type type;
	    private final String scrollableText;

	    public ScrollableDialog(Shell parentShell, Type type, String msg, String scrollableText) {
	        super(parentShell);
	        this.type=type;
	        this.msg=msg;
	        this.scrollableText = scrollableText;
	    }

	    @Override
	    protected Control createDialogArea(Composite parent) {
	        Composite composite = (Composite) super.createDialogArea (parent); // Let the dialog create the parent composite

	        GridData gridData = new GridData();
	        gridData.grabExcessHorizontalSpace = true;
	        gridData.horizontalAlignment = GridData.FILL;
	        gridData.grabExcessVerticalSpace = true; // Layout vertically, too! 
	        gridData.verticalAlignment = GridData.FILL;

	        Text scrollable = new Text(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	        scrollable.setLayoutData(gridData);
	        scrollable.setText(scrollableText);

	        return composite;
	    }

	    @Override
	    public void create() {
	        super.create();

	        // This is not necessary; the dialog will become bigger as the text grows but at the same time,
	        // the user will be able to see all (or at least more) of the error message at once
	        //getShell ().setSize (300, 300);
	        setTitle(type.toString());
	        switch(type) {
	        case ERROR:
		        setMessage(msg, IMessageProvider.ERROR);
		        break;
	        case NOTICE:
		        setMessage(msg, IMessageProvider.INFORMATION);
		        break;
	        case WARNING:
		        setMessage(msg, IMessageProvider.WARNING);
		        break;
	        }
	    }

	    @Override
	    protected void createButtonsForButtonBar(Composite parent) {
	        Button okButton = createButton(parent, OK, "OK", true);
	        okButton.addSelectionListener(new SelectionAdapter() {

	            @Override
	            public void widgetSelected(SelectionEvent e) {
	                close();
	            }
	        });
	    }

	    @Override
	    protected boolean isResizable() {
	        return true; // Allow the user to change the dialog size!
	    }
	}
	
	
	
	public static final int max_trace_length=64;
	public static String getStackTraceAsString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		e.printStackTrace(pw);
		pw.flush();
		sw.flush();
		String rv = sw.toString();
		String[] s = rv.split("\\n", 65);
		if(s.length<65)
			return rv;
		rv="";
		for(int i=0;i<64;i++)
			rv+=s[i]+"\n";
		return rv;
	}
	public static boolean question(final Shell parent, final String title, final String message){
		return MessageDialog.openQuestion(parent, title, message);
	}
	public static boolean question(final String title, final String message){
		return MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, message);
	}
	public static boolean confirm(final Shell parent, final String title, final String message){
		return MessageDialog.openConfirm(parent, title, message);
	}
	public static boolean confirm(final String title, final String message){
		return MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, message);
	}
	public static void dialog(final Type t, final String desc, final String message) {
		if(t == null || desc==null || message == null) {
			try {
				throw new RuntimeException("one or more args was null!");
			} catch(Exception e) {
				dialog(Type.ERROR,"",getStackTraceAsString(e));
			}
		}
		else {
			 PlatformUI.getWorkbench().getDisplay().syncExec( new Runnable() {
				 @Override
				 public void run() {
						if(desc.length()+message.length() < 256) {
							switch(t) {
							case ERROR:
							case WARNING:
								MessageDialog.openError(Display.getDefault().getActiveShell(), desc, message);
								break;
							case NOTICE:
								MessageDialog.openInformation(Display.getDefault().getActiveShell(), desc, message);
								break;
							}
						} else {
							ScrollableDialog dialog = new ScrollableDialog(Display.getDefault().getActiveShell(), t, desc, message);
							dialog.open();
						}
				 }
			 });
		}
	}
}
