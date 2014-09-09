package jprime;

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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Nathanael Van Vorst
 *
 */
public class Console {
	public static interface ErrorDialogMaker {
		public void errorDialog(String title, String message);
		public String getStackTraceAsString(Exception e);
	};
	public static Console out = new Console(System.out);
	public static Console err = new Console(System.err);
	private static ErrorDialogMaker dialogMaker = null;
	
	private final PrintStream s;
	protected Console() {
		s=null;
	}
	public Console(PrintStream s) {
		if(s == null) {
			try {
				throw new RuntimeException("ACK!");
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(100);
			}
		}
		this.s=s;
	}
	public static void setErrorDialogMaker(ErrorDialogMaker m) {
		dialogMaker=m;
	}
	public static void errorDialog(String title, String message) {
		if(dialogMaker != null) {
			dialogMaker.errorDialog(title, message);
		}
		else {
			err.println(title);
			err.println(message);
		}
	}
	
	public void print(String str) {
		s.print(str);
	}
	public void println(String str) {
		s.println(str);
	}
	public void print(Object o) {
		s.print(o.toString());
	}
	public void println(Object o) {
		s.println(o.toString());
	}
	public void flush() {
		s.flush();
	}
	public void errorDialog() {
		
	}
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

	public final static void halt(int status) {
		halt(status,null);
	}
	public final static void halt(int status, String reason) {
		String st=null;
		try{
			throw new RuntimeException("stack trace");
		} catch(Exception e) {
			st = getStackTraceAsString(e);
		}
		if(reason==null || reason.length()==0) {
			reason="<UNKNOWN>";
		}
		try {
			if(dialogMaker != null) {
				dialogMaker.errorDialog("Fatal Exception", "Reason:\n\t"+reason+"\nWhere:\n"+st);
			}
		}catch(Exception ee) {
			System.err.println("Fatal Exception:\nReason:\n\t"+reason+"\nWhere:\n"+st);
		}
		Runtime.getRuntime().halt(status);
	}
	public void printStackTrace(Exception e) {
		e.printStackTrace(s);
	}
}
