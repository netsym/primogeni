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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import jprime.EmulationCommand;
import jprime.Experiment;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.Host.IHost;
import jprime.StaticTrafficType.IStaticTrafficType;
import jprime.partitioning.Alignment;
import jprime.partitioning.Partition;
import jprime.partitioning.Partitioning;
import jprime.util.ComputeNode;
import jprime.visitors.TLVVisitor.TLVType;
import monitor.commands.AbstractCmd;
import monitor.commands.AreaOfInterestUpdate;
import monitor.commands.CodecFactory;
import monitor.commands.CommandType;
import monitor.commands.ConnectToSlavesCmd;
import monitor.commands.CreateDynamicModelNode;
import monitor.commands.HostCmd;
import monitor.commands.MonitorCmd;
import monitor.commands.SetupExperimentCmd;
import monitor.commands.ShutdownCmd;
import monitor.commands.StartExperimentCmd;
import monitor.commands.StateExchangeCmd;
import monitor.commands.UpdateVizExportRate;
import monitor.commands.VarUpdate;
import monitor.commands.VarUpdate.StringUpdate;
import monitor.ssl.PrimoSslContextFactory;
import monitor.util.Utils;
import monitor.util.Utils.CmdRv;
import monitor.util.JschPortForwardingL;
//import monitor.util.Utils.CmdRv;
//import monitor.util.MasterConnectionThread;
//import org.python.modules.thread.thread;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;


/**
 * @author Nathanael Van Vorst, Mohammad Obaida
 *
 */
public class RemoteController implements IController {

	

	

	private static class CommandSummary {
		public final CommandType ct;
		public final Integer id;
		public final String summary;
		public CommandSummary(AbstractCmd cmd) {
			super();
			this.ct = cmd.getCommandType();
			this.id = cmd.getSerialNumber();
			this.summary = cmd.toString();
		}
		public String toString() {
			return "["+ct.toString()+", id="+id+": "+summary+"]";
		}
	}
	private final ControllerSessionHandler handler;
	private final long timeout;
	private final String expName;
	private final Partitioning partitioning;
	private final HashMap<Integer, ArrayList<IHost>> emulatedHost2machineMap;
	private final HashMap<Integer,CommandSummary> outstandingBlockingCommands;
	private final HashMap<Integer,CommandSummary> outstandingNonBlockingCommands;
	private final HashMap<String,String> cleanupCommads = new HashMap<String, String>();
	private final IExpListenter listener;
	private final String vpnserver;
	private NioSocketConnector connector;
	private IoSession masterSession;
	private Integer master_id;
	private int failures;
	private boolean shuttingDown;
	private boolean expBeenSetup;
	private Integer runtime=null;
	private LinkedList<AbstractCmd> waiting = new LinkedList<AbstractCmd>();
	
	public RemoteController(Partitioning partitioning, HashMap<Integer, ArrayList<IHost>> emulatedHost2machineMap, String vpnserver, long timeout, String expName, IExpListenter l) {
		super();
		this.timeout = timeout;
		this.handler = new ControllerSessionHandler(this);
		this.connector = null;
		this.masterSession=null;
		this.outstandingBlockingCommands=new HashMap<Integer, CommandSummary>();
		this.outstandingNonBlockingCommands=new HashMap<Integer, CommandSummary>();
		this.master_id=null;
		this.failures=0;
		this.partitioning=partitioning;
		this.emulatedHost2machineMap = emulatedHost2machineMap;
		this.vpnserver=vpnserver;
		this.shuttingDown=false;
		this.expName=expName;
		this.expBeenSetup=false;
		this.listener=l;
		if(listener==null)
			throw new RuntimeException("the listener cannot be null!");
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getEmulatedHost2machineMap()
	 */
	public HashMap<Integer, ArrayList<IHost>> getEmulatedHost2machineMap() {
		return emulatedHost2machineMap;
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#getFailures()
	 */
	public int getFailures() {
		return failures;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getExpName()
	 */
	public String getExpName() {
		return expName;
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#getListener()
	 */
	public IExpListenter getListener() {
		return listener;
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#getNumOutstandingCommands()
	 */
	public synchronized int getNumOutstandingCommands() {
		return outstandingNonBlockingCommands.size() + outstandingBlockingCommands.size();
	}
	/* (non-Javadoc)
	 * @see monitor.core.IController#getNumOutstandingNonBlockingCommands()
	 */
	public synchronized int getNumOutstandingNonBlockingCommands() {
		return outstandingNonBlockingCommands.size();
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#getNumOutstandingBlockingCommands()
	 */
	public synchronized int getNumOutstandingBlockingCommands() {
		return outstandingBlockingCommands.size();
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#connect(monitor.core.Provisioner.ComputeNode, java.util.List, java.lang.String)
	 */
	public synchronized void connect(final ComputeNode master, final List<ComputeNode> slaves, final String keystore) throws GeneralSecurityException, IOException {
		if(this.masterSession == null) {
			this.connector = null;
			this.masterSession=null;
			this.master_id=null;
			this.shuttingDown=false;
			if(master == null) {
				throw new RuntimeException("Invalid master node!");
			}
			// Socket for sending to other nodes
			this.connector = new NioSocketConnector();

			// Configure the service
			connector.setConnectTimeoutMillis(this.timeout);
			if(Utils.enableSSL) {
				//XXX the private key needs to be sent over...
				//for now we assume the keystore already has it.....
				connector.getFilterChain().addLast("sslFilter",PrimoSslContextFactory.getClientFilter(keystore));
			}
			connector.getFilterChain().addLast("codec",new ProtocolCodecFilter(new CodecFactory()));

			// Set handler for the connector socket
			connector.setHandler(this.handler);
			// Connect the slave
			//OBAIDA note- Setup ssh tunnel here so that there is successful connection for mina (We can do the tweaking on the ComputeNode.java too)
			
			String master_control_ip = master.getControl_ip();
			listener.println("Retrieved from RSPEC: Master = "+master);
			
			
			//String scmd ="";
			ConnectFuture future=null;
			if (!master_control_ip.contains(":"))
			{
				listener.println(" Connecting to master at "+master+":"+Utils.MASTER_PORT+" ( Case: Master has global IP/ pingable web address)");
				future = connector.connect(new InetSocketAddress( master.getControl_ip(), Utils.MASTER_PORT)); //normal execution
			}
				
			else //setting ssh tunnel, since having a ':' in master name means that its a KVM/XEN VM, not a physical machine 
			{   

				String[] master_cip_parts = master_control_ip.split(":");
				listener.println("Master Ip Before setting up tunnel:"+master.getControl_ip());
			    String tunnel_command = "ssh -o \"StrictHostKeyChecking no\"  -f -N -L 9990:localhost:9990 -p "+master_cip_parts[1]+" "+master_cip_parts[2]+"@"+master_cip_parts[0];
//yyy
				//MasterConnectionThread justAThread = new MasterConnectionThread(tunnel_command);
				//justAThread.start();
				//listener.println("State of the thread: "+justAThread.getState());

			    JschPortForwardingL ssh_tunnel=new JschPortForwardingL();
				String tunnel_username=null,tunnel_remotehost=null;
				int tunnel_masterport,tunnel_localport,tunnel_ssh_port=0;//if ssh port is not :22
				//String[ ]master_cip_parts= pc243.instageni.clemson.edu:ssh_port:username
				tunnel_username=master_cip_parts[2];
				tunnel_remotehost=master_cip_parts[0];
				tunnel_ssh_port=Integer.parseInt(master_cip_parts[1]);
				tunnel_localport=tunnel_masterport=Utils.MASTER_PORT;
				if ((null!=tunnel_username && null!=tunnel_remotehost)&& tunnel_ssh_port>0)
				 {
					 if ( ssh_tunnel.SetTunnel(tunnel_username,tunnel_remotehost, tunnel_ssh_port, tunnel_localport, tunnel_masterport))
					 {
						 System.out.println("Tunnel setup properly.");
						 listener.println("Tunnel setup properly.");
					 }
					 else 
						System.out.println("Problem setting up ssh tunnel, Program might not run properly, unless you create the tunnel manually.");
					 	listener.println("Tunnel setup failed, you can try setting tunnel manually using sh and try to run the model again.");
					 	listener.println("Use this :"+tunnel_command);
				 }
				
				
				
				
//			     scmd = "ssh -f -N -L 9990:localhost:9990 -p "+master_cip_parts[1]+" "+master_cip_parts[2]+"@"+master_cip_parts[0];
//			     listener.println("Tunneling command:"+scmd);
//				CmdRv rv = Utils.executeCommand(scmd);
//				if(rv.status != 0){
//					listener.println("\n     !!!!RemoteController- failed to create local ssh tunnel. Experiment should be shutdown after this.\n");	
//					//sh
//					
//				}
				
				listener.println("Tunnel setup peroperly.");
				master_control_ip="localhost";
				master.setControl_ip(master_control_ip);
				
//				try {
//					listener.println("Thread will sleep for 5 sec for ssh tunnel to setup");
//				    //Program sleeping for 5 second
//					Thread.sleep(5000);                 //1000 milliseconds is one second.
//					listener.println("Thread awake.");
//				} catch(InterruptedException ex) {
//				    Thread.currentThread().interrupt();
//				}
				listener.println("Connecting to master at "+master+":"+Utils.MASTER_PORT + " Case: Master is a vm and a ssh port is available(other than 22) to connect");
				//yyy
//				int max_wait_time=75, unit_wait=3;
//				//future=connector.connect(new InetSocketAddress(master_control_ip, Utils.MASTER_PORT)); //normal execution
//				//future.awaitUninterruptibly();
//				//this.masterSession=future.getSession();
//				future=null;
//				
//				for (int wait_i=0;wait_i<=max_wait_time;wait_i+=unit_wait)
//				{	future=connector.connect(new InetSocketAddress(master_control_ip, Utils.MASTER_PORT)); //normal execution
//					listener.println("Master Session:"+future.getSession());
//					if (null !=future.getSession()) 
//						{
//						this.masterSession=future.getSession();
//						break;
//						}
//					try
//					{
//						listener.println("Waiting: "+unit_wait+"s");
//						Thread.sleep(unit_wait*1000);                 //1000 milliseconds is one second.
//					}catch(InterruptedException ex) {
//						Thread.currentThread().interrupt();
//					}					//if (!future.getSession().isConnected())//null ==future.getSession())
//				}
				future=connector.connect(new InetSocketAddress(master_control_ip, Utils.MASTER_PORT)); //normal execution
			}
			listener.println("Connection setup peroperly.");

			//ConnectFuture future = connector.connect(new InetSocketAddress( master.getControl_ip(), Utils.MASTER_PORT)); --Old connect
			
			future.awaitUninterruptibly();
			this.masterSession=future.getSession();
			listener.println("Connected to master! connected="+this.masterSession.isConnected() +"masterSession="+masterSession);
			List<Partition> partitions = new ArrayList<Partition>();
			partitions.addAll(partitioning.getProcessingNodes().values());
			
			if(partitions.size() != slaves.size()) {
				throw new RuntimeException("The number of partitions does not match the numers of slaves! [partitions.size()="+partitions.size()+", slaves.size()="+slaves.size()+"]");
			}
			//send slave list
			ConnectToSlavesCmd cmd = new ConnectToSlavesCmd();
			this.master_id=0;
			for(ComputeNode slave : slaves) {
				//add a slave with: (nodetype, machineid, ip)
				listener.println("Adding slave("+slave+") partid="+slave.getPartitionId());
				cmd.addSlaveConfig(NodeConfig.COMPUTE_NODE, slave.getPartitionId(), slave);
				if(slave.getPartitionId() > this.master_id)
					this.master_id=slave.getPartitionId();
			}
			this.master_id++;
			cmd.setMasterConfig(NodeConfig.SERVICE_NODE, master_id, master);
			listener.println("Sending "+cmd);
			this.sendCommand(cmd);
		}
		else {
			throw new RuntimeException("Already connected!");
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public class MasterConnectionThread extends Thread {
		//@Override
		public String scmd;
		MasterConnectionThread(String command)
		{
			scmd=command;
		}
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			connectMaster();
		}
		private void connectMaster() {
		    //String scmd = "ssh -f -N -L 9990:localhost:9990 -p "+master_cip_parts[1]+" "+master_cip_parts[2]+"@"+master_cip_parts[0];
//		    listener.println("Tunneling command:"+scmd);
//			CmdRv rv = Utils.executeCommand(scmd);
//			if(rv.status != 0){
//				listener.println("\n     !!!!RemoteController- failed to create local ssh tunnel. Experiment should be shutdown after this.\n");	
//				//sh
//				
//			}
			//String[] command = {"/bin/sh", "/tmp/masterConnect.sh"};

			System.out.println("Connecting Command:"+scmd.toString());
			
			File filesh = new File("/tmp/masterConnect.sh");
	    	try{ 
	    		if(filesh.delete()){
	    			listener.println("Previous connect file deleted");
	    			//System.out.println(filesh.getName() + " is deleted!");
	    		}else{
	    			System.out.println("There is no previous connect file to delete.");
	    		}
	    	}catch(Exception e){e.printStackTrace();}
			
			//listener.println("Master connecting script will be at: /tmp/masterConnect.sh");
	        FileWriter fileWritersh = null;
			try {
				fileWritersh = new FileWriter(filesh,true);
				BufferedWriter bufferFileWritersh  = new BufferedWriter(fileWritersh);
				fileWritersh.append("#!/bin/sh");fileWritersh.append("\n");
				//fileWritersh.append("kill `ps -eo pid,args --cols=10000 | awk '/9990:localhost:9990/ && $1 != PROCINFO[\"pid\"] { print $1 }'`");
				// kill -9 `ps -ef | grep -i 9990:localhost:9990 | awk '{print $2}'`
				//fileWritersh.append("sleep 5");fileWritersh.append("\n");//wait for the kill command to 
				fileWritersh.append(scmd);
				bufferFileWritersh.close();
		        fileWritersh.close();
				listener.println("Updated Tunnel Script at: /tmp/masterConnect.sh");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			Utils.appendMsgToFile("/tmp/pgc_debug_msg","cmd to execute:"+scmd);
			
			System.out.println("Remote Controller, cmd to execute:" + scmd); //xxxxxxxxxx
			
			
			String[] command = {"sh", "/tmp/masterConnect.sh"};//> /dev/null 2>&1
		    System.out.println("Command:"+command.toString());
		    ProcessBuilder p = new ProcessBuilder(command);
		    Process p2=null;
			try {
				p2 = p.start();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    
//		    try {
//				Thread.sleep(3600000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		    BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
		    String line;
		    System.out.println("Output of running " + command.toString() + " is: ");
		    try {
				while ((line = br.readLine()) != null) {
				    System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		}

	}	
	
	
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#shutdown(org.apache.mina.core.session.IoSession)
	 */
	public synchronized void shutdown(IoSession session) {
		//shutdown(session)
		if(this.masterSession == session) {
			listener.println("The connection to the master was lost!"); //OBAIDA
			this.masterSession = null;
		}
		else {
			listener.println("An unknown session was lost! How did this happen?");
		}
		CloseFuture closeFuture = session.getCloseFuture();
		closeFuture.getSession().close(false);
		closeFuture.awaitUninterruptibly(5, TimeUnit.SECONDS);
		shutdown(false,true);
		this.failures++;
	}
	
	
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#shutdown(org.apache.mina.core.session.IoSession, java.lang.Throwable)
	 */
	public synchronized void shutdown(IoSession session, Throwable cause) {
		if(this.masterSession == session) {
			listener.println("An uncaught exception was enountered on the master sesison!\n"+cause.toString());
			//Error here
			listener.println("this.masterSession="+this.masterSession +" session="+session);
			this.masterSession = null;
		}
		else {
			listener.println("An uncaught exception was enountered on an unknown session! How did this happen?\n"+cause.toString());
		}
		CloseFuture closeFuture = session.getCloseFuture();
		closeFuture.getSession().close(false);
		closeFuture.awaitUninterruptibly(5, TimeUnit.SECONDS);
		shutdown(false,true);
		this.failures++;
	}
	
	
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#shutdown(boolean, boolean)
	 */
	public synchronized void shutdown(boolean sendShutdownCommand, boolean failed) {
		if(shuttingDown) return;
		shuttingDown=true;
		listener.println("Shutting down!");
		if(masterSession != null) {
			if(sendShutdownCommand) {
				listener.println("\tSending shutdown to master!");
				this.sendCommand(new ShutdownCmd());
			}
			CloseFuture closeFuture = masterSession.getCloseFuture();
			closeFuture.getSession().close(false);
			try {
				closeFuture.await(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			listener.println("\tMaster connection was alreay down!");
		}
		if(this.connector != null) {
			this.connector.dispose(false);
			int i=500;
			while(i>0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				i-=100;
			}
		}
		this.connector=null;
		this.listener.finishedExperiment(failed);
	}


	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, jprime.Host.IHost, java.util.Date)
	 */
	public synchronized boolean sendHostCommand(String cmd, IHost h, Date whenToRun, boolean checkReturnCode) {
		Set<Integer> is = h.getAlignments(partitioning);
		if(is.size()==1) {
			int part_id = is.iterator().next();
			part_id = partitioning.findAlignment(part_id).getPartId();
			return sendCommand(new HostCmd(cmd, h.getUID(), part_id,whenToRun,checkReturnCode));
		}
		throw new RuntimeException("Invalid host alignment!");
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, jprime.Host.IHost, long)
	 */
	public synchronized boolean sendHostCommand(String cmd, IHost h, long delay, boolean checkReturnCode) {
		Set<Integer> is = h.getAlignments(partitioning);
		if(is.size()==1) {
			int part_id = is.iterator().next();
			part_id = partitioning.findAlignment(part_id).getPartId();
			return sendCommand(new HostCmd(cmd, h.getUID(), part_id,delay,checkReturnCode));
		}
		throw new RuntimeException("Invalid host alignment!");
	}


	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, java.util.Date, long)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid, Date whenToRun, boolean checkReturnCode, long runtime) {
		return sendCommand(new HostCmd(cmd, hid, machineid,whenToRun,checkReturnCode,runtime));
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, java.util.Date)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid, Date whenToRun, boolean checkReturnCode) {
		return sendCommand(new HostCmd(cmd, hid, machineid,whenToRun,checkReturnCode));
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, long, long)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid, long delay, boolean checkReturnCode, long runtime) {
		return sendCommand(new HostCmd(cmd, hid, machineid,delay,checkReturnCode,runtime));
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, long)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid, boolean checkReturnCode, long delay) {
		return sendCommand(new HostCmd(cmd, hid, machineid,delay,checkReturnCode));
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#sendCommand(monitor.commands.AbstractCmd)
	 */
	public synchronized boolean sendCommand(AbstractCmd cmd) {
		cmd.setSerialNumber(Utils.nextSerial());
		final CommandSummary cs = new CommandSummary(cmd);
		outstandingBlockingCommands.put(cs.id,cs);
		//if(Utils.DEBUG)
			jprime.Console.out.println("Sending Command to machine/part "+cmd.getMachineId()+" cmd=" + cmd);
		masterSession.write(cmd);
		if(cmd instanceof SetupExperimentCmd) {
			this.expBeenSetup=true;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getMasterId()
	 */
	public Integer getMasterId() {
		return master_id;
	}

	protected synchronized void commandFinished(CommandType ct, Integer commandId, int status, String msg) {
		listener.println("finished a command,  commandId='"+commandId);
		if(ct == CommandType.NON_BLOCKING_CMD_RESULT) {
			if(outstandingNonBlockingCommands.containsKey(commandId)) {
				CommandSummary cmd = outstandingNonBlockingCommands.remove(commandId);
				if(0!=status || (msg!=null && msg.contains("Failed"))) {
					//XXX what to do if the status is not 0 (i.e. it failed)?
					listener.println("The command '"+cmd+"' failed! Msg:\n"+msg);
					failures++;
				}
				else {
					listener.println("Finished command "+cmd+"; Msg:\n"+msg);
				}
			}
			else {
				listener.println("Got a command response that was not expected(commandId="+commandId+"), Msg:\n"+msg+"\n");
			}
			listener.println("\tOutstanding Blocking    commands:"+outstandingBlockingCommands.size());
			listener.println("\tOutstanding NonBlocking commands:"+outstandingNonBlockingCommands.size());
			//for(CommandSummary cs : outstandingNonBlockingCommands.values()) {
			//	listener.println("\t\t"+cs);
			//}
		}
		else {
			if(outstandingBlockingCommands.containsKey(commandId)) {
				CommandSummary cmd = outstandingBlockingCommands.remove(commandId);
				//removed it....
				//hack for demo!
				//System.out.println("'"+cmd.summary+"'.contains(\"route\")="+cmd.summary.contains("route"));
				if(!cmd.summary.contains("route") && (0!=status || (msg!=null && msg.contains("Failed")))) {
					//if(0!=status|| (msg!=null && msg.contains("Failed"))) {
					//XXX what to do if the status is not 0 (i.e. it failed)?
					listener.println("The command '"+cmd+"' failed! Msg:\n"+msg);
					failures++;
				}
				else {
					if(cmd.ct == CommandType.HOST_CMD || cmd.ct == CommandType.MONITOR_CMD || cmd.ct == CommandType.START_EXPERIMENT) {
						//these commands are backgrounded!
						outstandingNonBlockingCommands.put(cmd.id, cmd);
						listener.println("Started command "+cmd+" in the background");
					}
					else {
						listener.println("Finished command "+cmd+"; Msg:\n"+msg);
					}
				}
			}
			else {
				listener.println("Got a command response that was not issued! commandId="+commandId+", Msg=\n"+msg+"\n\tOutstanding="+outstandingBlockingCommands.keySet()+"");
			}
			listener.println("\tOutstanding Blocking    commands:"+outstandingBlockingCommands.size());
			listener.println("\tOutstanding NonBlocking commands:"+outstandingNonBlockingCommands.size());
			//for(CommandSummary cs : outstandingBlockingCommands.values()) {
			//	listener.println("\t\t"+cs);
			//}
		}
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#startExperiment(int)
	 */
	public boolean startExperiment(int runtime) {
		listener.println("RemoteController.java->startExperiment");
		if(expBeenSetup) {
			for(AbstractCmd a : waiting) {
				if(Utils.DEBUG)System.out.println("sending intial AOI, "+a.toString());
				this.sendCommand(a);
			}
			waiting.clear();
			if(!this.sendCommand(new StartExperimentCmd(runtime,expName, vpnserver))) {
				return false;
			}
			//wait for the exp to be started in the background
			while(getNumOutstandingBlockingCommands()>0 && this.getFailures()==0) {
				//wait for it to connect...
				try {
					Thread.sleep(100);
					listener.println("waiting for exp to start...getNumOutstandingBlockingCommands()="+getNumOutstandingBlockingCommands());
				} catch (InterruptedException e) {
				}
			}
			return this.getFailures()==0;
		}
		throw new RuntimeException("You must first deploy AND setup the experiment!");
	}
	/* (non-Javadoc)
	 * @see monitor.core.IController#runExperiment(jprime.Experiment, int, monitor.core.ExpRunner)
	 */
	public ExpRunner runExperiment(final Experiment exp, final int runtime, ExpRunner runner) {
		//Writing experiment status to storage-Obaida
		
		listener.println("RemoteController.java: runExperiment");
		if(exp == null) {
			throw new RuntimeException("exp was null!");
		}
		
		this.runtime=runtime;
		String outdir = Utils.BASE_EXP_DIR+"/"+exp.getName().replace(" ", "_")+"_"+Utils.getDateTime();
		listener.println("this.runtime="+runtime+"; outdir="+outdir);
		
		if(listener == null) {
			throw new RuntimeException("listener was null!");
		}
		
		//listener.println("Saving output to "+outdir);
		//this.sendCommand(new MonitorCmd("/bin/mkdir -p "+outdir,-1,0,0,true));

		
		
		
		//yyy
		this.startExperiment(runtime);
		for(EmulationCommand e : exp.getEmulationCommands()) {
			e.evaluateCmd(this.partitioning.getTopnet(), runtime);
			for(Integer pid : this.getEmulatedHost2machineMap().keySet()) {
				String s = null;
				if(e.getOutputSuffix() != null && e.getOutputSuffix().length()>0)
					s = e.getCmd()+" > "+outdir+"/"+pid+"_"+e.getOutputSuffix();
				else
					s = e.getCmd();
				this.sendCommand(new MonitorCmd(s, pid, e.getDelay(), e.getMaxRuntime(), e.isBlocking()));
			}
		}
		
		for(Entry<Integer, ArrayList<IHost>> e : this.getEmulatedHost2machineMap().entrySet()) {
			for(IHost h : e.getValue()) {
				for(EmulationCommand cmd : h.getEmulationCommands()) {
					cmd.evaluateCmd(this.partitioning.getTopnet(), runtime);
					
					//XXX final String outfile = "/tmp/"+h.getUID()+"_"+cmd.getOutputSuffix();
					String s = null;
					if(cmd.getOutputSuffix() != null && cmd.getOutputSuffix().length()>0) {
						//XXX for the tutorial we are abandoning saving output
						//s="rm -f "+outfile+" && "+cmd.getCmd()+" > "+outfile;
						s=cmd.getCmd();
						if(!cleanupCommads.containsKey(cmd.getOutputSuffix())) {
							cleanupCommads.put(cmd.getOutputSuffix(), "/bin/mv /usr/local/primo/vz/private-rw/*/tmp/*"+cmd.getOutputSuffix()+" "+outdir+"/.");
						}
					}
					else
						s=cmd.getCmd();
					this.sendHostCommand(s, h.getUID(), e.getKey(), cmd.getDelay(), cmd.shouldCheckReturnCode(), cmd.getMaxRuntime());
				}
			}
		}
		listener.setController(this);
		listener.setEmuHosts(this.getEmulatedHost2machineMap());
		if(runner ==null) {
			throw new RuntimeException("wtf?");
		}
		return runner;
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#cleanupExperiment(jprime.Experiment, monitor.core.IExpListenter)
	 */
	public void cleanupExperiment(final Experiment exp, IExpListenter listener) {
		//XXX for the geni tutorial we dont do this.....
		cleanupCommads.clear();//XXX
		for(Integer i : this.getEmulatedHost2machineMap().keySet()) {
			listener.println("Getting output from machine "+i);
			for(String cmd : cleanupCommads.values()) {
				this.sendCommand(new MonitorCmd(cmd, i,0,0,true));
			}
		}
		int max=2*1000;
		while(this.getNumOutstandingCommands()>0 && max>0) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
			}
			max-=100;
		}
		cleanupCommads.clear();
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#handleStateUpdate(monitor.commands.StateUpdateCmd)
	 */
	public synchronized void handleStateUpdate(StateExchangeCmd update) {
		listener.handleStateUpdate(update);
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#setRuntimeAttribute(jprime.IModelNode, int, java.lang.String)
	 */
	public void setRuntimeAttribute(IModelNode node, int varId, String value) {
		Set<Integer> aligns = node.getAlignments(partitioning);
		Set<Integer> coms = new HashSet<Integer>();
		for(int align : aligns) {
			Alignment a = partitioning.findAlignment(align);
			if(coms.contains(a.getPartId())) continue;
			coms.add(a.getPartId());
			setRuntimeAttribute(node, varId, value, a.getAlignId(), a.getPartId());
		}
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IController#setRuntimeAttribute(jprime.IModelNode, int, java.lang.String, int, int)
	 */
	public void setRuntimeAttribute(IModelNode node, int varId, String value, int com_id, int part_id) {
		ArrayList<VarUpdate> updates = new ArrayList<VarUpdate>(1);
		updates.add(new StringUpdate(varId,TLVType.STRING,value));
		StateExchangeCmd cmd = new StateExchangeCmd(0,node.getUID(),false,false,StateExchangeCmd.SET,com_id,updates);
		cmd.setMachineId(part_id);
		this.sendCommand(cmd);
	}
	
	public int getExperimentRuntime() {
		if(runtime == null)
			throw new RuntimeException("The experiment has not been started!");
		return runtime;
	}

	public void startDynamicTraffic(IStaticTrafficType dynamicTraffic) {
		this.sendCommand(new CreateDynamicModelNode((ModelNode)dynamicTraffic));
	}
	
	public void addToAreaOfInterest(IModelNode m) {
		if(this.expBeenSetup) {
			this.sendCommand(new AreaOfInterestUpdate(m.getUID(), true));
			if(Utils.DEBUG)System.out.println("adding stuff to AOI.");
		}
		else {
			waiting.add(new AreaOfInterestUpdate(m.getUID(), true));
		}
	}
	public void addToAreaOfInterest(Collection<IModelNode> ms) {
		long[] uids = new long[ms.size()];
		int i=0;
		for(IModelNode  m: ms) {
			uids[i]=m.getUID();
			i++;
		}
		if(this.expBeenSetup) {
			this.sendCommand(new AreaOfInterestUpdate(uids, true));
			if(Utils.DEBUG)System.out.println("adding stuff to AOI.");
		}
		else {
			waiting.add(new AreaOfInterestUpdate(uids, true));
		}
	}
	public void addToAreaOfInterest(long[] uids) {
		if(this.expBeenSetup) {
			this.sendCommand(new AreaOfInterestUpdate(uids, true));
			if(Utils.DEBUG)System.out.println("adding stuff to AOI.");
		}
		else {
			waiting.add(new AreaOfInterestUpdate(uids, true));
		}
	}
	
	public void adjustVizExportRate(long newRate) {
		this.sendCommand(new UpdateVizExportRate(newRate));
	}

	public void removeFromAreaOfInterest(IModelNode m) {
		if(this.expBeenSetup) {
			this.sendCommand(new AreaOfInterestUpdate(m.getUID(), false));
			if(Utils.DEBUG)System.out.println("removing stuff from AOI.");
		}		
		else {
			waiting.add(new AreaOfInterestUpdate(m.getUID(), false));
		}
	}
	public void removeFromAreaOfInterest(Collection<IModelNode> ms) {
		long[] uids = new long[ms.size()];
		int i=0;
		for(IModelNode  m: ms) {
			uids[i]=m.getUID();
			i++;
		}
		if(this.expBeenSetup) {
			this.sendCommand(new AreaOfInterestUpdate(uids, false));
			if(Utils.DEBUG)System.out.println("removing stuff from AOI.");
		}
		else {
			waiting.add(new AreaOfInterestUpdate(uids, false));
		}
	}	
	public void removeFromAreaOfInterest(long[] uids) {
		if(this.expBeenSetup) {
			this.sendCommand(new AreaOfInterestUpdate(uids, false));
			if(Utils.DEBUG)System.out.println("removing stuff from AOI.");
		}
		else {
			waiting.add(new AreaOfInterestUpdate(uids, false));
		}
	}	
	

	
	
	
	
}
