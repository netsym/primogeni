package monitor.core;

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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import monitor.commands.AbstractCmd;
import monitor.commands.HostCmd;
import monitor.commands.MonitorCmd;
import monitor.commands.SetupExperimentCmd.TLVFile;
import monitor.commands.StartExperimentCmd;
import monitor.commands.StartGatewayCmd;
import monitor.util.Utils;

/**
 * @author Nathanael Van Vorst
 * 
 */
public class BackgroundTask implements Runnable {
	private BackgroundCmdExecutor executor;
	private AbstractCmd cmd;
	private Process p;
	private static int killing_command = 0;

	public BackgroundTask(BackgroundCmdExecutor executor, MonitorCmd cmd) {
		this.executor = executor;
		this.cmd=cmd;
		this.p=null;
	}

	public BackgroundTask(BackgroundCmdExecutor executor, HostCmd cmd) {
		this.executor = executor;
		this.cmd=cmd;
		this.p=null;
	}
	
	public BackgroundTask(BackgroundCmdExecutor executor, StartExperimentCmd cmd) {
		this.executor = executor;
		this.cmd=cmd;
		this.p=null;
	}
	
	public BackgroundTask(BackgroundCmdExecutor executor, StartGatewayCmd cmd) {
		this.executor = executor;
		this.cmd=cmd;
		this.p=null;
	}

	public void run() {
		if(executor==null || cmd==null || p != null) return;
		
		String f = null;
		BufferedReader stdout = null;  // reader for output of process
		String line=null;
		String exec = null;
		String ps_string=null;
		long maxRuntime=0;
		boolean checkrv=false;
		
		if(cmd instanceof MonitorCmd) {
			f=Utils.TMP_DIR+"/monitor_cmd_"+cmd.getSerialNumber()+".sh";
			exec=((MonitorCmd)cmd).getCmd();
			if(!Utils.writeToFile(f, exec.getBytes(), null)) {
				executor.commandFinished(cmd.getSerialNumber(),100, "Failed to create "+f);
				return;
			}
			maxRuntime=((MonitorCmd)cmd).getMaxRuntime();
		}
		else if(cmd instanceof StartGatewayCmd) {
			f=Utils.TMP_DIR+"/monitor_cmd_"+cmd.getSerialNumber()+".sh";
			//run_vpn_server.sh <netsim> <exp dir> <expname> <server name>
			exec="sh "+Utils.PRIMOGENI_FOLDER+"/run_vpn_server.sh "+
			Utils.NETSIM_DIR+" "+
			Utils.BASE_EXP_DIR+" "+
			((StartGatewayCmd)cmd).getExpName()+" "+
			((StartGatewayCmd)cmd).getServerName();
			if(!Utils.writeToFile(f, exec.getBytes(), null)) {
				executor.commandFinished(cmd.getSerialNumber(),100, "Failed to create "+f);
				return;
			}
			maxRuntime=0;
		}
		else if(cmd instanceof StartExperimentCmd) {
			
			StartExperimentCmd scmd = ((StartExperimentCmd)cmd);
			String vpngateway="";
			if(scmd.getVPNGateway().length()>0) {
				vpngateway=" -gateway "+scmd.getVPNGateway()+":"+Utils.VPN_PORT;
			}
			for(int i = 0;i <scmd.getTlvs().size();i++) {
				TLVFile t = scmd.getTlvs().get(i);
				String p = "";
				if(scmd.getPortalArgs().size()>0) {
					p=scmd.getPortalArgs().get(i);
				}
				if(exec == null) {
					exec =Utils.MPIEXEC+" -f "+Utils.MACHINE_FILE+" -n 1 ";
				}
				else {
					exec+=" : -n 1 ";
				}
				exec += Utils.PRIMEX +" "+p+" -enable_state_stream "+ vpngateway+" "+scmd.getRuntime()+" "+t.path;
			}
			if(Utils.VEI>0) {
				exec+=" -VEI "+Utils.VEI+" ";
			}
			String exp_dir = Utils.BASE_EXP_DIR+"/"+((StartExperimentCmd)cmd).getName();
			ps_string=exec;
			exec+=" -v "+exp_dir+"/"+Utils.RUNTIME_VAR_NAME;

			exec+=" > "+Utils.TMP_DIR+"/sim_"+((StartExperimentCmd)cmd).getName().replace(" ","_")+Utils.getDateTime()+".out 2>&1 ";
			Utils.appendMsgToFile("/tmp/pgc_debug_msg","\n EXP CMD:"+exec);
			System.out.println("EXP CMD:"+exec);
			f=Utils.TMP_DIR+"/run_exp_"+cmd.getSerialNumber()+".sh";
			if(!Utils.writeToFile(f,  exec.getBytes(), null)) {
				executor.commandFinished(cmd.getSerialNumber(),100, "Failed to create "+f);
				return;
			}
			maxRuntime=((StartExperimentCmd)cmd).getRuntime()*1000;
		}
		else {
			f=Utils.TMP_DIR+"/host_cmd_"+((HostCmd)cmd).getHostId()+"_"+cmd.getSerialNumber()+".sh";
			exec = "vzctl exec "+((HostCmd)cmd).getHostId()+" \""+((HostCmd)cmd).getCmd().replace("\"", "\\\"")+"\"";
			if(!Utils.writeToFile(f,  exec.getBytes(), null)) {
				executor.commandFinished(cmd.getSerialNumber(),100, "Failed to create "+f);
				return;
			}
			maxRuntime=((HostCmd)cmd).getMaxRuntime();
			ps_string=((HostCmd)cmd).getCmd();
			checkrv=((HostCmd)cmd).shouldCheckReturnCode();
		}
		executor.commandStarted(cmd.getSerialNumber(), "Started \""+exec+"\"");
		if(Utils.DEBUG)System.out.println("["+Utils.getDateTime()+"] Started '"+exec+"' -- "+f);
		try {
			// A Runtime object has methods for dealing with the OS
			p = createProcess(f);
			stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			String msg = "Error executing command '"+cmd+"+':\n"+sw.toString();
			if(Utils.DEBUG)System.out.println(msg);
			executor.commandFinished(cmd.getSerialNumber(),100, msg);
			return;
		}
		if(checkrv && ps_string!=null) {
			int tries=20;
			do {
				try {
					Thread.sleep(75+(20-tries)*50);
				} catch (InterruptedException e) {
				}
				String ps_output = doPS();
				if(ps_output.contains(ps_string)) {
					if(Utils.DEBUG)System.out.println("The command,"+ps_string+" succeded on try "+(20-tries));
					break;
				}
				else {
					if(Utils.DEBUG)System.out.println("The command,"+ps_string+" did not start, on trial "+(20-tries));
					//execute command again
					try {
						if(p!=null) {
							try{
								p.destroy();
							}catch(Exception e) { }
						}
						try {
							p = createProcess(f);
							stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
						} catch (Exception e) {
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw, true);
							e.printStackTrace(pw);
							pw.flush();
							sw.flush();
							String msg = "Error executing command '"+cmd+"+':\n"+sw.toString();
							if(Utils.DEBUG)System.out.println(msg);
							executor.commandFinished(cmd.getSerialNumber(),100, msg);
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				tries--;
			} while(tries>0);			
		}
		
		if(maxRuntime<=0) {
			//we just let the command run its course....
			boolean done=false;
			while(!done) {
				try {
					p.waitFor();  // wait for process to complete
					done=true;
				}
				catch(InterruptedException e) {
				}
			}
			String msg = "";
			try {
				while ((line = stdout.readLine()) != null) {
					msg+=line;
				}
			} catch (Exception e1) {
			}
			int rv = p.exitValue();
			if(!checkrv) rv =0;
			if(Utils.DEBUG)System.out.println("Process done, exit status was " + rv);
			executor.commandFinished(cmd.getSerialNumber(),rv,msg);
			try {
				p.destroy();
			} catch (Exception e) {}
		}
		else {
			//we let the command run for a specific amount of time...
			long runtime=maxRuntime;
			while(runtime>0) {
				try {
					Thread.sleep(500);
					runtime-=500;
				}
				catch(InterruptedException e) {
				}
			}
			try {
				p.destroy();
			} catch (Exception e) {}
			try {
				if(ps_string!=null)
					killAllProcesses(ps_string);
			} catch (Exception e) {}
			String msg = "";
			try {
				while ((line = stdout.readLine()) != null) {
					msg+=line;
				}
			} catch (Exception e1) {
			}
			//since we  forcibly killed it, lets ignore the return code...
			if(Utils.DEBUG)System.out.println("Process was forcibly killed after "+((double)maxRuntime/1000.0)+" seconds!");
			executor.commandFinished(cmd.getSerialNumber(),0,msg);
		}
		p=null;
		cmd=null;
		executor=null;
	}
	
	private static boolean killAllProcesses(String command)
	{
		//This string holds the command
		String exec = null;
		String f = null;
		String line=null;
		String killExec = null;
		
		exec ="/bin/ps aux";
		f = Utils.TMP_DIR + "/ps_output" + ".sh";
		if(!Utils.writeToFile(f,  exec.getBytes(), null)) {
			return false;
		}
		//Execute command
		Process ps_p = null;
		try {
			ps_p = createProcess(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader ps_output = new BufferedReader(new InputStreamReader(ps_p.getInputStream()));
		try {
			while ((line = ps_output.readLine()) != null) {
				//Kill the process
				if(line.contains(exec)){
					//Parse the line to get PID (2nd column)
					String[] tokens = line.trim().split("\\s+");
					killExec = "/usr/bin/kill -9 " + tokens[1];
					
					String content = "killing:" + killExec + "---line:" + line;
					Utils.writeToFile(Utils.TMP_DIR + "/kill_cmd_" + killing_command, content.getBytes(), null);
					killing_command++;
					
					f = Utils.TMP_DIR + "/kill_cmd" + ".sh";
					if(!Utils.writeToFile(f,  killExec.getBytes(), null)) {
						return false;
					}
					//Execute command
					try {
						createProcess(f);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	private static String doPS()
	{
		//This string holds the command
		String exec = null;
		String f = null;
		String msg = null;
		String line=null;
		
		exec ="/bin/ps aux";
		f = Utils.TMP_DIR + "/ps_output" + ".sh";
		if(!Utils.writeToFile(f,  exec.getBytes(), null)) {
			return "Could not write to file";
		}
		//Execute command
		Process ps_p = null;
		try {
			ps_p = createProcess(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader ps_output = new BufferedReader(new InputStreamReader(ps_p.getInputStream()));
		
		try {
			while ((line = ps_output.readLine()) != null) {
				msg += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return msg;
	}
	
	private static Process createProcess(String cmd) throws IOException {
		ProcessBuilder pb = new ProcessBuilder("sh", cmd);
		pb.directory(new File(Utils.TMP_DIR));
		pb.redirectErrorStream(true);
		return pb.start();
	}

}
