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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import slingshot.Util;

/**
 * The class handles the log window and writing/appending lines to the log as they are generated.
 * 
 * @author Nathanael Van Vorst
 * @author Eduardo Pena
 * 
 */
public class LogHandler{
	/**
	 * The actual log window
	 * @author Nathanael Van Vorst
	 * @author Eduardo Pena
	 */
	public class Console extends jprime.Console {
		public Console() {
			super();
		}

		/* (non-Javadoc)
		 * @see jprime.Console#print(java.lang.String)
		 */
		@Override
		public void print(String str) {
			try {
				printToLog(str);
			} catch(Exception e) {
				System.out.print(str);
			}
		}

		/* (non-Javadoc)
		 * @see jprime.Console#println(java.lang.String)
		 */
		@Override
		public void println(String str) {
			try {
				printLineToLog(str);
			} catch(Exception e) {
				System.out.println(str);
			}
		}

		/* (non-Javadoc)
		 * @see jprime.Console#print(java.lang.Object)
		 */
		@Override
		public void print(Object o) {
			try {
				printToLog(o.toString());
			} catch(Exception e) {
				System.out.print(o.toString());
			}
		}

		/* (non-Javadoc)
		 * @see jprime.Console#println(java.lang.Object)
		 */
		@Override
		public void println(Object o) {
			try {
				printLineToLog(o.toString());
			} catch(Exception e) {
				System.out.println(o.toString());
			}
		}

		/* (non-Javadoc)
		 * @see jprime.Console#flush()
		 */
		@Override
		public void flush() {
			//no op
		}

		/* (non-Javadoc)
		 * @see jprime.Console#printStackTrace(java.lang.Exception)
		 */
		@Override
		public void printStackTrace(Exception e) {
			printLineToLog(Util.getStackTraceAsString(e));
		}
		
	}
	private Text log;
	private Composite logComposite;
	private final CTabFolder tabFolder;
	private final CTabItem tab;
	public final jprime.Console console = new Console();


	LogHandler(Composite parent, GridData data, CTabItem tab) {
		this.tabFolder=(CTabFolder)parent;
		this.tab=tab;
		this.logComposite = new Composite(parent, SWT.BORDER);
		this.logComposite.setLayout(new GridLayout());
		this.logComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.logComposite.setFont(parent.getFont());
		this.log = new Text(logComposite, SWT.READ_ONLY | SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		final GridData textData = new GridData(GridData.FILL_BOTH);
		log.setLayoutData(textData);
	}
	
	public void showLogTab() {
		tabFolder.setSelection(tab);
	}

	public void focus() {
		jprime.Console.out=console;
		jprime.Console.err=console;
	}
	
	public Text getLog() {
		return log;
	}


	public void setLog(Text log) {
		this.log = log;
	}


	public Composite getLogComposite() {
		return logComposite;
	}


	public void setLogComposite(Composite logComposite) {
		this.logComposite = logComposite;
	}


	public void printToLog(final String text){
		try {
			logComposite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					log.append(text);
				}
			});
		}
		catch (Exception e) {
			System.out.print(text);
		}
	}
	
	public void printLineToLog(final String text){
		try {
			logComposite.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					log.append(text+"\n");
				}
			});
		}
		catch (Exception e) {
			System.out.println(text);
		}
	}
	
	
	
	
}
