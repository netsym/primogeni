package jprime.database;

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

/* $if DEBUG $
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import jprime.EntityFactory;
import jprime.util.GlobalProperties;
$endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
/* $if DEBUG $
public class AccessLogger {
	final BufferedWriter accessLog;
	
	public AccessLogger(String log) {
		accessLog=open(log);
	}
	private static BufferedWriter open(String log) {
		BufferedWriter rv=null;
		if(GlobalProperties.DB_LOG_ACCESS) {
			jprime.Console.out.println("Logging access pattern to "+log);
			try {
				rv = new BufferedWriter(new FileWriter(log));
				rv.write("#C=create, A=access, L=load, F=flush, P=phase change, T=list the types as strings\n");
				rv.write("#<access type>, <time>, <node type>, <dbid>, <pid>, <size>\n");
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		return rv;
	}
	
	public void logAccess(int type, long dbid, long pid) {
		try {
			if(accessLog!=null)
				accessLog.write("A, "+System.currentTimeMillis()+", "+type+", "+dbid+", "+pid+"\n");
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
	}
	
	public void logCreate(int type, long dbid, long pid) {
		try {
			if(accessLog!=null)
				accessLog.write("C, "+System.currentTimeMillis()+", "+type+", "+dbid+", "+pid+"\n");
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
	}
	
	public void logEvict(int type, long dbid) {
		try {
			if(accessLog!=null)
				accessLog.write("E, "+System.currentTimeMillis()+", "+type+", "+dbid+", -1\n");
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
	}
	
	public void logLoad(int type, long dbid, long pid) {
		try {
			if(accessLog!=null)
				accessLog.write("L, "+System.currentTimeMillis()+", "+type+", "+dbid+", "+pid+"\n");
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
	}
	
	public void logFlush(int type, long dbid, long pid) {
		try {
			if(accessLog!=null)
				accessLog.write("F, "+System.currentTimeMillis()+", "+type+", "+dbid+", "+pid+"\n");
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
	}
	
	public void logPhaseChange(String phase) {
		try {
			if(accessLog!=null)
				accessLog.write("P, "+System.currentTimeMillis()+", "+phase+"\n");
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
	}
	
	public void logRawString(String msg) {
		try {
			if(accessLog!=null)
				accessLog.write("S, "+System.currentTimeMillis()+", "+msg+"\n");
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
	}	
	
	public void close() {
		if(accessLog!=null) {
	    	for(int i=0;i<EntityFactory.MAX_TYPE_ID;i++) {
	    		String s = EntityFactory.getString(i);
	    		if(s!=null) {
	    			try {
	    				accessLog.write("T, "+i+", "+s+"\n");
	    			} catch (IOException e) {
	    				jprime.Console.err.printStackTrace(e);
	    				jprime.Console.halt(100);
	    			}
	    		}
	    			
	    	}
	    	try {
	        	accessLog.close();
			} catch (IOException e) {
				jprime.Console.err.printStackTrace(e);
			}
		}
	}
}
$else$ */
public class AccessLogger {
}
/* $endif$ */

