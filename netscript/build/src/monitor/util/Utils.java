package monitor.util;

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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @author Nathanael Van Vorst
 *
 */
public class Utils{
	public static class CmdRv {
		public final int status;
		public String msg;
		public CmdRv(int status, String msg) {
			super();
			this.status = status;
			this.msg = msg;
		}

	};
	/*
	public static final boolean DEBUG=Boolean.parseBoolean(System.getProperty("DEBUG", "false"));
	public static final boolean GEN_SSH_KEYS=Boolean.parseBoolean(System.getProperty("GEN_SSH_KEYS", "false"));
	public static final int MAX_OUTSTANDING_CMDS = Integer.parseInt(System.getProperty("maxOutStandingCommands", "255"));
	public static final int MASTER_PORT = Integer.parseInt(System.getProperty("masterPort", "9990"));
	public static final int SLAVE_PORT = Integer.parseInt(System.getProperty("slavePort", "9991"));
	public static final int PRIME_PORT = Integer.parseInt(System.getProperty("primePort", "9992"));
	public static final int SYMBIO_ACTUATOR_PORT = Integer.parseInt(System.getProperty("symbioPort", "9993"));
	public static final boolean enableSSL=Boolean.parseBoolean(System.getProperty("enableSSL", "false"));
	public static final String NETSIM_DIR=System.getProperty("netsimDir", "/cluster/prime/primex/netsim");
	public static final String PRIMEX=System.getProperty("netsimDir", "/cluster/prime/primex/netsim")+"/primex";
	public static final String BASE_EXP_DIR=System.getProperty("expDir", "/cluster/prime/primex/exps");
	public static final String PRIMOGENI_FOLDER=System.getProperty("protogeniDir", "/cluster/prime/primex/netscript/src/monitor_scripts");

	public static final int VEI=Integer.parseInt(System.getProperty("VEI", "-1"));

	public static final int VPN_PORT=Integer.parseInt(System.getProperty("VPN_PORT", "36685"));
	
	public static final String LOCAL_MONITOR_OUTPUT="/tmp";
	
	public static final int STORAGE_QUEUE_SIZE=Integer.parseInt(System.getProperty("storageQueueSize", "10240"));
	public static final String RUNTIME_VAR_NAME="runtime_symbols.txt";
	public static final String TMP_DIR="/tmp";
	
	public static final String MACHINE_FILE=System.getProperty("MACHINE_FILE","/tmp/machineFile");
	public static final String MPIEXEC=System.getProperty("MPIEXEC","/usr/bin/mpiexec");
	
	public static final String PVS_CONFIG=System.getProperty("PVS_CONFIG","/tmp/pvsconfig");
	public static final String ROOT_SSH = "/tmp/.ssh/";
	*/
	
	public static final boolean DEBUG=Boolean.parseBoolean(System.getProperty("DEBUG", "false"));
	public static final boolean GEN_SSH_KEYS=Boolean.parseBoolean(System.getProperty("GEN_SSH_KEYS", "true"));
	public static final int MAX_OUTSTANDING_CMDS = Integer.parseInt(System.getProperty("maxOutStandingCommands", "255"));
	public static final int MASTER_PORT = Integer.parseInt(System.getProperty("masterPort", "9990"));
	public static final int SLAVE_PORT = Integer.parseInt(System.getProperty("slavePort", "9991"));
	public static final int PRIME_PORT = Integer.parseInt(System.getProperty("primePort", "9992"));
	public static final int SYMBIO_ACTUATOR_PORT = Integer.parseInt(System.getProperty("symbioPort", "9993"));
	public static final boolean enableSSL=Boolean.parseBoolean(System.getProperty("enableSSL", "false"));
	public static final String NETSIM_DIR=System.getProperty("netsimDir", "/primex/primogeni/netsim");
	public static final String PRIMEX=System.getProperty("netsimDir", "/primex/primogeni/netsim")+"/primex";
	public static final String BASE_EXP_DIR=System.getProperty("expDir", "/primex/exps");
	public static final String PRIMOGENI_FOLDER=System.getProperty("protogeniDir", "/primex/scripts");

	public static final int VEI=Integer.parseInt(System.getProperty("VEI", "-1"));

	public static final int VPN_PORT=Integer.parseInt(System.getProperty("VPN_PORT", "36685"));
	
	public static final String LOCAL_MONITOR_OUTPUT="/usr/local/primo/vz/tmp";
	
	public static final int STORAGE_QUEUE_SIZE=Integer.parseInt(System.getProperty("storageQueueSize", "10240"));
	public static final String RUNTIME_VAR_NAME="runtime_symbols.txt";
	public static final String TMP_DIR="/tmp";
	
	public static final String MACHINE_FILE=System.getProperty("MACHINE_FILE","/primex/machineFile");
	public static final String MPIEXEC=System.getProperty("MPIEXEC","/usr/mpich/bin/mpiexec");

	public static final String PVS_CONFIG=System.getProperty("PVS_CONFIG","/primex/pvsconfig");
	public static final String ROOT_SSH = "/root/.ssh/";
	
	private static int serial_num = (int)Math.random()*10000;
	
	public static synchronized int nextSerial() {
		if(serial_num<0)
			serial_num=0;
		return ++serial_num;
	}
	
	public static CmdRv executeCommand(String cmd)
	{
		if(Utils.DEBUG)System.out.println("cmd to execute:" + cmd);
		appendMsgToFile("/tmp/pgc_debug_msg","cmd to execute:"+cmd);
		
		System.out.println("cmd to execute:" + cmd); //xxxxxxxxxx
		try {
			// A Runtime object has methods for dealing with the OS
			Runtime r = Runtime.getRuntime();
			Process p;     // Process tracks one external native process
			BufferedReader is;  // reader for output of process
			String line;

			p = r.exec(cmd);

			
			System.out.println("MonitorUtilsExecutingCommand ='"+cmd+"+':\n"); //xxxxxx

			
			// getInputStream gives an Input stream connected to
			// the process p's standard output. Just use it to make
			// a BufferedReader to readLine() what the program writes out.
			is = new BufferedReader(new InputStreamReader(p.getInputStream()));

			p.waitFor();  // wait for process to complete
			String msg = "";
			while ((line = is.readLine()) != null) {
				msg+=line;
			}
			int rv = p.exitValue();
			if(Utils.DEBUG)System.out.println("Process done, exit status was " + rv);
			appendMsgToFile("/tmp/pgc_debug_msg","Process done, exis status was:"+cmd);
			
			try {
				p.destroy();
			}
			catch(Exception e) {
			}
			p=null;
			return new CmdRv(rv,msg);
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			if(Utils.DEBUG)System.out.println("Error executing command '"+cmd+"+':\n"+sw.toString());
			appendMsgToFile("/tmp/pgc_debug_msg","Error executing command '"+cmd+"+': "+sw.toString());
			
			//System.out.println("OBAIDAError executing command '"+cmd+"+':\n"+sw.toString());
			return new CmdRv(100, "Error executing command '"+cmd+"+':\n"+sw.toString());
		}

	}

	
	public static boolean writeToFile(String file_name, byte[] content, String perms)
    {
		if(Utils.DEBUG)System.out.println("Creating file:"+file_name+" with "+content.length+" bytes!");
		try{
		    // Create file 
			File f = new File(file_name);
			if(!f.exists()) {
				if(f.getParentFile() != null && !f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(content);
			fos.close();
	    }catch (Exception e){//Catch exception if any
	    	if(Utils.DEBUG)System.out.println("Failed!");
	    	e.printStackTrace();
	    	return false;
	    }
	    if(perms != null) {
	    	return 0 == Utils.executeCommand("chmod "+perms+" "+file_name).status;	    	
	    }
	    return true;
    }
	
	public static boolean appendToFile(String file_name, byte[] content)
	{
		System.out.println("bytes to append " + content);
		try{
		    // Create file 
			File f = new File(file_name);
			if(!f.exists()) {
				if(f.getParentFile() != null && !f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				f.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(f, true);
			fos.write(content);
			fos.write("\n".getBytes());
			fos.close();
	    } catch (Exception e){//Catch exception if any
	    	if(Utils.DEBUG)System.out.println("Failed!");
	    	e.printStackTrace();
	    	return false;
	    }
	    
		return true;
	}

	public static boolean appendMsgToFile(String file_name, String append_this)
	{
		
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file_name, true)));
		    out.println(append_this);
		    out.close();
		} catch (IOException e) {
	    	if(Utils.DEBUG)System.out.println("Failed!");
	    	e.printStackTrace();			
	    	return false;
		}
		return true;
	}
	
	
	
    public static String getDateTime() {
    	DateFormat dateFormat = new SimpleDateFormat("dd_HH_mm_ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
