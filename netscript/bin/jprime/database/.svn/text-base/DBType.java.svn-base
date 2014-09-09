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

import java.util.Map;
import java.util.Map.Entry;

import jprime.util.GlobalProperties;

/**
 * @author Nathanael Van Vorst
 *
 */
public enum DBType {
	MYSQL("com.mysql.jdbc.Driver","jdbc:mysql:","gatherPerfMetrics=true&explainSlowQueries=true&logSlowQueries=true&slowQueryThresholdMillis=100&logger=jprime.database.MysqlDebugLogger"),
	DERBY("org.apache.derby.jdbc.EmbeddedDriver","jdbc:derby:","")
	;
	public final String driver;
	public final String conType;
	public final String debugParams;
	/* $if false == USE_FLAT_FILE_DB $ */
	private String url=null;
	/* $endif$ */
	DBType(String driver,String conType,String debugParams){
		this.driver=driver;
		this.conType=conType;
		this.debugParams=debugParams;
	}
	public void setup(Map<String,String> params) {
		if(params.containsKey("DB_NAME"))
			GlobalProperties.DB_NAME=params.remove("DB_NAME");
		if(params.containsKey("DB_PATH")) {
			if(this==DERBY) {
				GlobalProperties.DB_PATH=params.remove("DB_PATH");
			}
			else {
				jprime.Console.err.println("WARNING: Did not set the DB_PATH because we are using a MySQL database!");
			}
		}
		if(params.containsKey("DB_SERVER")) {
			if(this==MYSQL)
				GlobalProperties.DB_SERVER=params.remove("DB_SERVER");
			else {
				jprime.Console.err.println("WARNING: Did not set the DB_SERVER because we are using a DERBY database!");
			}
		}
		if(params.containsKey("DB_PORT")) {
			if(this==MYSQL)
				GlobalProperties.DB_PORT=params.remove("DB_PORT");
			else {
				jprime.Console.err.println("WARNING: Did not set the DB_PORT because we are using a DERBY database!");
			}
		}
		if(params.containsKey("DB_USER"))
			GlobalProperties.DB_USER=params.remove("DB_USER");
		if(params.containsKey("DB_PASS"))
			GlobalProperties.DB_PASS=params.remove("DB_PASS");
		if(params.containsKey("DB_DEBUG"))
			GlobalProperties.DB_DEBUG = Boolean.parseBoolean(params.get("DB_DEBUG"));
		if(params.size()>0) {
			jprime.Console.err.println("WARNING: Did not understand the following database params:");
			for(Entry<String, String> e : params.entrySet()) {
    			jprime.Console.err.println("\t"+e.getKey()+" : "+e.getValue());
			}
		}
		/* $if false == USE_FLAT_FILE_DB $ */
		url=null;
		/* $endif$ */
	}
	/* $if USE_FLAT_FILE_DB $
	public String getURL() {
		return "<using nate's custom DB! path="+GlobalProperties.DB_PATH+"/"+GlobalProperties.DB_NAME+">";
	}
	$else$ */
	public String getURL() {
		if(url == null) {
    		url = conType;
    		if(this==MYSQL) {
    			if(GlobalProperties.DB_SERVER !=null && GlobalProperties.DB_SERVER.length()>0)
    				url+="//"+GlobalProperties.DB_SERVER;
    			if(GlobalProperties.DB_PORT!=null && GlobalProperties.DB_PORT.length()>0)
    				url+=":"+GlobalProperties.DB_PORT;
    		}
    		else {
    			if(GlobalProperties.DB_PATH !=null && GlobalProperties.DB_PATH.length()>0)
    				url+=GlobalProperties.DB_PATH;
    		}
    		url+="/"+GlobalProperties.DB_NAME;
    		if(this==DERBY)
    			url+=";create=true";
    		boolean first=true;
    		if(GlobalProperties.DB_DEBUG && debugParams!=null &&debugParams.length()>0) {
    			if(first) {
    				if(this==MYSQL)
    					url+="?";
    				else
    					url+=";";
    				first=false;
    			}
    			else {
    				if(this==MYSQL)
    					url+="&";
    				else
    					url+=";";
    			}
    			url+=debugParams;
    		}
    		if(GlobalProperties.DB_USER!=null && GlobalProperties.DB_USER.length()>0) {
    			if(first) {
    				if(this==MYSQL)
    					url+="?";
    				else
    					url+=";";
    				first=false;
    			}
    			else {
    				if(this==MYSQL)
    					url+="&";
    				else
    					url+=";";
    			}
    			url+="user="+GlobalProperties.DB_USER;
    		}
    		if(GlobalProperties.DB_PASS!=null && GlobalProperties.DB_PASS.length()>0) {
    			if(first) {
    				if(this==MYSQL)
    					url+="?";
    				else
    					url+=";";
    				first=false;
    			}
    			else {
    				if(this==MYSQL)
    					url+="&";
    				else
    					url+=";";
    			}
    			url+="password="+GlobalProperties.DB_PASS;
    		}
    		try {
				Class.forName(driver).newInstance();
			} catch (InstantiationException e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			} catch (IllegalAccessException e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			} catch (ClassNotFoundException e) {
				jprime.Console.err.printStackTrace(e);
				jprime.Console.halt(100);
			}
		}
		return url;
	}
	/* $endif$ */
	
	public static DBType fromString(String str) {
		if("mysql".compareToIgnoreCase(str)==0)
			return MYSQL;
		return DERBY;
	}
}

