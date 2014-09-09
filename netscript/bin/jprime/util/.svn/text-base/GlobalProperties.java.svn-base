package jprime.util;

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

import jprime.database.DBType;
import jprime.database.IPersistableCache.CacheType;

/**
 * @author Nathanael Van Vorst
 *
 */
public class GlobalProperties {
	public static final String DB_TYPE_DEFAULT         = "DERBY";   
	public static final String DB_CACHE_SIZE_DEFAULT   = "500000";
	public static final String DB_CACHE_TYPE_DEFAULT   = "FIFO";   
	public static final String DB_BATCH_SIZE_DEFAULT   = "10000";
	public static final String DB_FECTCH_SIZE_DEFAULT   = "1";
	public static final String LOG_DIR_DEFAULT         = "/tmp";
	public static final String LOG_PREFIX_DEFAULT      = "";
	public static final String DB_NAME_DEFAULT         = "primex";
	public static final String DB_PATH_DEFAULT         = "/tmp";
	public static final String DB_SERVER_DEFAULT       = "localhost";
	public static final String DB_PORT_DEFAULT         = "";
	public static final String DB_USER_DEFAULT         = "primex";
	public static final String DB_PASS_DEFAULT         = "passwd";
	public static final String DB_DEBUG_DEFAULT        = "false";
	public static final String DB_LOG_ACCESS_DEFAULT   = "false";	
	public static final String CREATE_XML_DEFAULT      = "true";
	public static final String PART_STR_DEFAULT        = "1::1:1";
	public static final String OUT_DIR_DEFAULT         = ".";
	public static final DBType DB_TYPE= DBType.fromString(System.getProperty("DB_TYPE",DB_TYPE_DEFAULT));   
	public static final CacheType DB_CACHE_TYPE= CacheType.fromString(System.getProperty("DB_CACHE_TYPE",DB_CACHE_TYPE_DEFAULT));   
	public static int BATCH_SIZE=Integer.parseInt(System.getProperty("DB_BATCH_SIZE", DB_BATCH_SIZE_DEFAULT));
	public static int CACHE_SIZE=Integer.parseInt(System.getProperty("DB_CACHE_SIZE", DB_CACHE_SIZE_DEFAULT));
	public static int DB_FLUSH_Q_SIZE=Integer.parseInt(System.getProperty("DB_FLUSH_Q_SIZE", "-1"));
	public static int FETCH_SIZE=Integer.parseInt(System.getProperty("DB_FECTCH_SIZE", DB_FECTCH_SIZE_DEFAULT));
	public static String LOG_DIR=System.getProperty("LOG_DIR",LOG_DIR_DEFAULT);
	public static String LOG_PREFIX="".equals(System.getProperty("LOG_PREFIX",LOG_PREFIX_DEFAULT))?"":System.getProperty("LOG_PREFIX",LOG_PREFIX_DEFAULT)+".";
	public static String DB_NAME=System.getProperty("DB_NAME",DB_NAME_DEFAULT);
	public static String DB_PATH=System.getProperty("DB_PATH",DB_PATH_DEFAULT);
	public static String DB_SERVER=System.getProperty("DB_SERVER",DB_SERVER_DEFAULT);
	public static String DB_PORT=System.getProperty("DB_PORT",DB_PORT_DEFAULT);
	public static String DB_USER = System.getProperty("DB_USER",DB_USER_DEFAULT);
	public static String DB_PASS = System.getProperty("DB_PASS",DB_PASS_DEFAULT);
	public static boolean DB_DEBUG = Boolean.parseBoolean(System.getProperty("DB_DEBUG",DB_DEBUG_DEFAULT));
	public static boolean DB_LOG_ACCESS = Boolean.parseBoolean(System.getProperty("DB_LOG_ACCESS",DB_LOG_ACCESS_DEFAULT));
	public static final Boolean CREATE_XML=Boolean.parseBoolean(System.getProperty("CREATE_XML",CREATE_XML_DEFAULT));
	public static final String PART_STR=System.getProperty("PART_STR",PART_STR_DEFAULT);
	public static final String OUT_DIR=System.getProperty("OUT_DIR",OUT_DIR_DEFAULT);
	

	public static void info(String prefix, PrintStream out) {
		out.println(prefix+"DB_TYPE            ; which type of db to use (options are DERBY or MYSQL),\n"+
				prefix+"                     defaults to '"+GlobalProperties.DB_TYPE_DEFAULT+"'");
		out.println(prefix+"DB_NAME            ; the name of the database, defaults to '"+GlobalProperties.DB_NAME_DEFAULT+"'.");
		out.println(prefix+"DB_PATH            ; derby: where to store the db, defaults to '"+GlobalProperties.DB_PATH_DEFAULT+"'.");
		out.println(prefix+"DB_SERVER          ; mysql: the mysql server ip, defaults to '"+GlobalProperties.DB_SERVER_DEFAULT+"'.");
		out.println(prefix+"DB_PORT            ; mysql: the mysql server port, defaults to '"+GlobalProperties.DB_PORT_DEFAULT+"'.");
		out.println(prefix+"DB_USER            ; the name of the database, defaults to '"+GlobalProperties.DB_USER_DEFAULT+"'.");
		out.println(prefix+"DB_PASS            ; the name of the database, defaults to '"+GlobalProperties.DB_PASS_DEFAULT+"'.");
		out.println(prefix+"DB_DEBUG           ; whether to debug the db, defaults to '"+GlobalProperties.DB_DEBUG_DEFAULT+"'.");
		out.println(prefix+"DB_LOG_ACCESS      ; log access patterns, defaults to '"+GlobalProperties.DB_LOG_ACCESS_DEFAULT+"'.");
		out.println("");
		out.println(prefix+"DB_CACHE_TYPE      ; the type of cache to use, defaults to '"+GlobalProperties.DB_CACHE_TYPE_DEFAULT+"',\n"+
				prefix+"                     other options are 'LRU' or 'COMPLETE'.");
		out.println(prefix+"DB_CACHE_SIZE      ; the number of objects to keep in mempry, defaults to '"+GlobalProperties.DB_CACHE_SIZE_DEFAULT+"',\n"+
				prefix+"                     other options are 'LRU'. 'COMPLETE', or 'FREQ'.");
		
		out.println(prefix+"DB_FLUSH_Q_SIZE    ; the # objects to queue for flushing to the DB, defaults to 50 batch sizes.");
		out.println(prefix+"DB_BATCH_SIZE      ; the # of objects per transaction, defaults to '"+GlobalProperties.DB_BATCH_SIZE_DEFAULT+"'.");
		out.println(prefix+"DB_FECTCH_SIZE     ; the # of objects to fetch at once, defaults to '"+GlobalProperties.DB_FECTCH_SIZE_DEFAULT+"'.");
		out.println("");
		out.println(prefix+"LOG_DIR            ; the directory to store logs at, defaults to '"+GlobalProperties.LOG_DIR_DEFAULT+"'.");
		out.println(prefix+"LOG_PREFIX         ; prefix for logs, defaults to '"+GlobalProperties.LOG_PREFIX_DEFAULT+"'.");
		out.println("");
		out.println(prefix+"CREATE_XML         ; create a xml's during compilation, defaults to '"+GlobalProperties.CREATE_XML_DEFAULT+"'.");
		out.println(prefix+"PART_STR           ; how to partition the model, defaults to '"+GlobalProperties.PART_STR_DEFAULT+"'.");
		out.println(prefix+"OUT_DIR            ; how to partition the model, defaults to '"+GlobalProperties.OUT_DIR_DEFAULT+"'.");
	}

}
