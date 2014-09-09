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


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import com.mysql.jdbc.Util;
import com.mysql.jdbc.profiler.ProfilerEvent;

/**
 * @author Nathanael Van Vorst
 *
 */
public class MysqlDebugLogger implements com.mysql.jdbc.log.Log{
	private static final int FATAL = 0;

	private static final int ERROR = 1;

	private static final int WARN = 2;

	private static final int INFO = 3;

	private static final int DEBUG = 4;

	private static final int TRACE = 5;

	//public static StringBuffer bufferedLog = null;

	//private boolean logLocationInfo = true;

	private final BufferedWriter log;
	
	/**
	 * Creates a new StandardLogger object.
	 * 
	 * @param name
	 *            the name of the configuration to use -- ignored
	 */
	public MysqlDebugLogger(String name) {
		this(name, false);
	}

	public MysqlDebugLogger(String name, boolean logLocationInfo) {
		//this.logLocationInfo = logLocationInfo;
		BufferedWriter t= null;
		try {
			t = new BufferedWriter(new FileWriter("/tmp/sql.log"));
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}
		this.log=t;
	}

	
	@Override
	protected void finalize() throws Throwable {
		System.out.println("in db debug finalize ACK!"); System.out.flush();
		log.flush();
		log.close();
		super.finalize();

	}

	//public static void saveLogsToBuffer() {
	//	if (bufferedLog == null) {
	//		bufferedLog = new StringBuffer();
	//	}
	//}

	/**
	 * @see com.mysql.jdbc.log.Log#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		return true;
	}

	/**
	 * @see com.mysql.jdbc.log.Log#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		return true;
	}

	/**
	 * @see com.mysql.jdbc.log.Log#isFatalEnabled()
	 */
	public boolean isFatalEnabled() {
		return true;
	}

	/**
	 * @see com.mysql.jdbc.log.Log#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		return true;
	}

	/**
	 * @see com.mysql.jdbc.log.Log#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		return true;
	}

	/**
	 * @see com.mysql.jdbc.log.Log#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		return true;
	}

	/**
	 * Logs the given message instance using the 'debug' level
	 * 
	 * @param message
	 *            the message to log
	 */
	public void logDebug(Object message) {
		logInternal(DEBUG, message, null);
	}

	/**
	 * Logs the given message and Throwable at the 'debug' level.
	 * 
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the throwable to log (may be null)
	 */
	public void logDebug(Object message, Throwable exception) {
		logInternal(DEBUG, message, exception);
	}

	/**
	 * Logs the given message instance using the 'error' level
	 * 
	 * @param message
	 *            the message to log
	 */
	public void logError(Object message) {
		logInternal(ERROR, message, null);
	}

	/**
	 * Logs the given message and Throwable at the 'error' level.
	 * 
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the throwable to log (may be null)
	 */
	public void logError(Object message, Throwable exception) {
		logInternal(ERROR, message, exception);
	}

	/**
	 * Logs the given message instance using the 'fatal' level
	 * 
	 * @param message
	 *            the message to log
	 */
	public void logFatal(Object message) {
		logInternal(FATAL, message, null);
	}

	/**
	 * Logs the given message and Throwable at the 'fatal' level.
	 * 
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the throwable to log (may be null)
	 */
	public void logFatal(Object message, Throwable exception) {
		logInternal(FATAL, message, exception);
	}

	/**
	 * Logs the given message instance using the 'info' level
	 * 
	 * @param message
	 *            the message to log
	 */
	public void logInfo(Object message) {
		logInternal(INFO, message, null);
	}

	/**
	 * Logs the given message and Throwable at the 'info' level.
	 * 
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the throwable to log (may be null)
	 */
	public void logInfo(Object message, Throwable exception) {
		logInternal(INFO, message, exception);
	}

	/**
	 * Logs the given message instance using the 'trace' level
	 * 
	 * @param message
	 *            the message to log
	 */
	public void logTrace(Object message) {
		logInternal(TRACE, message, null);
	}

	/**
	 * Logs the given message and Throwable at the 'trace' level.
	 * 
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the throwable to log (may be null)
	 */
	public void logTrace(Object message, Throwable exception) {
		logInternal(TRACE, message, exception);
	}

	/**
	 * Logs the given message instance using the 'warn' level
	 * 
	 * @param message
	 *            the message to log
	 */
	public void logWarn(Object message) {
		logInternal(WARN, message, null);
	}

	/**
	 * Logs the given message and Throwable at the 'warn' level.
	 * 
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the throwable to log (may be null)
	 */
	public void logWarn(Object message, Throwable exception) {
		logInternal(WARN, message, exception);
	}

	private void logInternal(int level, Object msg, Throwable exception) {
		StringBuffer msgBuf = new StringBuffer();
		msgBuf.append(new Date().toString());
		msgBuf.append(" ");

		switch (level) {
		case FATAL:
			msgBuf.append("FATAL: ");

			break;

		case ERROR:
			msgBuf.append("ERROR: ");

			break;

		case WARN:
			msgBuf.append("WARN: ");

			break;

		case INFO:
			msgBuf.append("INFO: ");

			break;

		case DEBUG:
			msgBuf.append("DEBUG: ");

			break;

		case TRACE:
			msgBuf.append("TRACE: ");

			break;
		}

		if (msg instanceof ProfilerEvent) {
			ProfilerEvent pe = (ProfilerEvent)msg;
			String m = pe.getMessage();
			if(m!=null) {
				int e =m.indexOf("VALUES");
				if(e>0) {
					m=m.substring(0,e);
				}
				else {
					e =m.indexOf("SET");
					if(e>0) {
						m=m.substring(0,e);
					}
				}
				m+="[len="+m.length()+"]";
			}
			else {
				m="";
			}
			msgBuf.append("duration:"+pe.getEventDuration()+" "+pe.getDurationUnits()+" "+m);
			//msgBuf.append(LogUtils.expandProfilerEventIfNecessary(msg));
		} else {
			/*
			if (this.logLocationInfo && level != TRACE) {
				Throwable locationException = new Throwable();
				msgBuf.append(LogUtils
						.findCallingClassAndMethod(locationException));
				msgBuf.append(" ");
			}*/

			if (msg != null) {
				msgBuf.append(String.valueOf(msg));
			}
		}

		if (exception != null) {
			msgBuf.append("\n");
			msgBuf.append("\n");
			msgBuf.append("EXCEPTION STACK TRACE:");
			msgBuf.append("\n");
			msgBuf.append("\n");
			msgBuf.append(Util.stackTraceToString(exception));
		}
		msgBuf.append("\n");

		String messageAsString = msgBuf.toString();

		try {
			log.write(messageAsString);
			log.flush();
		} catch (IOException e) {
			jprime.Console.err.printStackTrace(e);
			jprime.Console.halt(100);
		}

		//if (bufferedLog != null) {
		//	bufferedLog.append(messageAsString);
		//}
	}
	
}
