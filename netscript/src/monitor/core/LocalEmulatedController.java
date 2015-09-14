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
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import jprime.Console;
import jprime.Experiment;
import jprime.IModelNode;
import jprime.ModelNode;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.StaticTrafficType.IStaticTrafficType;
import jprime.TrafficPortal.ITrafficPortal;
import jprime.partitioning.Alignment;
import jprime.util.ComputeNode;
import jprime.util.NetAggPair;
import jprime.util.PartTlvPair;
import jprime.variable.ModelNodeVariable;
import jprime.visitors.TLVVisitor.TLVType;
import monitor.commands.AbstractCmd;
import monitor.commands.CodecFactory;
import monitor.commands.ComPartAdvertCmd;
import monitor.commands.CreateDynamicModelNode;
import monitor.commands.PrimeAreaOfInterestUpdate;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.PrimeUpdateVizExportRate;
import monitor.commands.StateExchangeCmd;
import monitor.commands.VarUpdate;
import monitor.commands.SetupContainerCmd.NIC;
import monitor.commands.VarUpdate.StringUpdate;
import monitor.core.Monitor.AggData;
import monitor.util.Utils;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * @author M Abu Obaida
 *
 */
public class LocalEmulatedController  extends Thread implements IController, IPrimeStateListener{
	private static int base_port = Utils.PRIME_PORT;
	private final PartTlvPair parting;
	private final IExpListenter listener;
	private  String cmd;//zzz removed final because will modify in two steps
	private boolean isEmulated=false;
	private final Experiment exp;
	private final Map<String,String> runtimeSymbols;
	private boolean running=false,stop=false,failure=false;

	private final PrimeSessionHandler prime_handler;
	private final NioSocketAcceptor prime_acceptor;
	private Process p;
	private final int port;
	private final HashMap<Integer,HashMap<Integer,IoSession>> session_map= new HashMap<Integer, HashMap<Integer,IoSession>>();
	private Integer runtime=null;
	private String primexDirectory = null; //zzz
	private final HashMap<Long,AggData> aggMap;
    private DataStorageThread db = null;
    private static class waitingdata {
    	final Set<Integer> aligns;
    	final AbstractCmd cmd;
		public waitingdata(Set<Integer> aligns, AbstractCmd cmd) {
			super();
			this.aligns = aligns;
			this.cmd = cmd;
		}
    }
    private LinkedList<waitingdata> waiting = new LinkedList<waitingdata>();

	/**
	 * @param tlv
	 */
	public LocalEmulatedController(Experiment exp, PartTlvPair parting, int runtime, int procs, double emuRatio, IExpListenter l, String primexDir, boolean visualize, Map<String,String> runtimeSymbols, String directory) {
		this.exp = exp;
		primexDirectory =primexDir;
		this.runtimeSymbols=runtimeSymbols;
		this.parting=parting;
		this.db = new DataStorageThread(directory+"/data.csv");
		this.db.start();
		if(parting.partitioning.getProcessingNodes().size()>=1) {
			this.aggMap = new HashMap<Long,AggData>();
			for( Entry<NetAggPair, Set<Integer>> e : NetAggPair.getAggMap(parting.partitioning).entrySet()) {
				for(long aid : e.getKey().agg_uids) {
					aggMap.put(aid, new AggData(e.getValue()));
				}
			}
		}
		else {
			this.aggMap=null;
		}
		this.listener=l;
		this.p=null;
		this.port=base_port++;
		String t = null;
		if(parting.tlvs.size()!=1) {
			throw new RuntimeException("found "+parting.tlvs.size()+" partitions but expected 1!");
		}
		if(visualize) {
			this.prime_handler=new PrimeSessionHandler(this);
			prime_acceptor = new NioSocketAcceptor();
			prime_acceptor.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(
							new CodecFactory(true)));
			prime_acceptor.setHandler(this.prime_handler);
			try {
				prime_acceptor.bind(new InetSocketAddress(port));
			} catch (IOException e) {
				e.printStackTrace();
				Runtime.getRuntime().halt(100);
			}
			listener.println("Listening for updates at port "+port);
		}
		else {
			prime_handler=null;
			prime_acceptor=null;
		}
		parting.partitioning.getTotalNumberOfAlignments();
		for(String tlv : parting.tlvs.values()) {
			t = primexDir+"/netsim/primex -n "+procs+(emuRatio>0?(" -e "+emuRatio):"")+" "+runtime+" "+tlv+writeRuntimeSymbols(tlv);
			if(visualize) {
				t=t+" -enable_state_stream -stream_server 127.0.0.1:"+port;
			}
		}
		this.cmd = t;
		listener.println("cmd: "+cmd);
		System.out.println("Running:"+cmd);
	}

	private String writeRuntimeSymbols(final String tlv) {
		String rv ="";
		if(runtimeSymbols != null  && runtimeSymbols.size()>0) {
			File tmp = new File(tlv);
			rv=tmp.getParentFile().getAbsoluteFile()+"/run_time_syms.txt";
			tmp = new File(rv);
			try {
				tmp.createNewFile();
				final BufferedWriter out = new BufferedWriter(new FileWriter(tmp));
				boolean first=true;
				for(Entry<String, String> e : runtimeSymbols.entrySet()) {
					if(e.getKey().contains("::")) {
						if(first) {
							out.write(".defaults\n");
							first=false;
						}
						out.write(e.getKey()+"="+e.getValue()+"\n");
					}
				}
				first=true;
				for(Entry<String, String> e : runtimeSymbols.entrySet()) {
					if(!e.getKey().contains("::")) {
						if(first) {
							out.write(".symbols\n");
							first=false;
						}
						out.write(e.getKey()+"="+e.getValue()+"\n");
					}
				}
				out.close();
				rv=" -v "+rv;
			} catch (IOException e) {
				rv="";
				jprime.Console.errorDialog("Unable to create runtime variable file. Ignoring them for this run!", Console.getStackTraceAsString(e));
			}
		}
		return rv;
	}

	
	public int createEmulatedHosts(){
		/**
		 * @author Mohammad Abu Obaida
		 * This method finds the emulated host in the model of an experiment and 
		 * create respective linux network namespaces(lightweight vms) representative of 
		 * the emulated hosts present in the model 
		 *    
		 * @noparam
		 */
		
		//find the emulated hosts...
		HashMap<Long, IHost> vms = new HashMap<Long,IHost>();
		for(IEmulationProtocol p : exp.getEmuProtocols()) {
			isEmulated=true;
			if(p instanceof ITrafficPortal) {
				IHost h = (IHost)p.getParent().getParent();
				for(IModelNode c : h.getAllChildren()) {
					try {
						if(c instanceof IInterface) {
							//portalRoutes.addAll(((IInterface)c).getReachableNetworks());
						}
					} catch(Exception e) {}
				}
			}
			//else if(p instanceof IOpenVPNEmulation) {
			//	IHost h = (IHost)p.getParent().getParent();
			//	vpns.put(h.getUID(),h);
			//}
			else {
				IHost h = (IHost)p.getParent().getParent();
				System.out.println("jprime/monitor.core/LocalEmulatedController:  \n\tHost uid = "+h.getUID()+" total interfaces = "+h.countInterfaces());
				//, h = "+h);
				//Map<String, String> attrs = h.getAttributes();
				//for (Map.Entry<String,String> entry : attrs.entrySet()) {
				//	  String key = entry.getKey();
				//	  String value = entry.getValue();
				//	  System.out.println("attr key = "+key+ ", val = "+value);
				//	  // do stuff
				//	}
				
				for(ModelNode k : h.getAllChildren()) { //iterating among all the children of ModelNode
					if(k instanceof IInterface) {
						long veth =k.getUID();
						String ipAddr = ((IInterface)k).getIpAddress().toString();
						String macAddr = Integer.toHexString((int) k.getUID());
						System.out.println("\tInterface uid= "+veth+ ", Ip addr = "+ipAddr+", mac addr="+macAddr);
						//nics.add(new NIC(k.getUID(),((IInterface)k).getIpAddress().toString()));
						
						//System.out.println("System working Directory = " + System.getProperty("user.dir"));

						//System.out.println("primexDirectory:"+primexDirectory);
						
						System.out.println("copying script from "+primexDirectory+"/netscript/src/monitor_scripts/script.sh to /tmp");
						String command1 = "cp -a "+primexDirectory+"/netscript/src/monitor_scripts/script.sh /tmp/";
						//String command = "gksudo sh script.sh 237 veth216 192.1.0.41 00:00:00:00:00:d8 66:00:00:00:00:d8";
						String output1 = executeCommand(command1);
						System.out.println(output1);
						
						System.out.println("Before running script");
						String command3 = "gksudo sh /tmp/script.sh "+h.getUID()+" veth"+veth+" "+ipAddr+" 00:00:00:00:00:"+macAddr+" 66:00:00:00:00:"+macAddr;
						//String command = "gksudo sh script.sh 237 veth216 192.1.0.41 00:00:00:00:00:d8 66:00:00:00:00:d8";
						String output3 = executeCommand(command3);
						System.out.println(output3);
						System.out.println("After running script");
						
					}	
				}
				//emulCount++;
				vms.put(h.getUID(),h);
			}
		}
		//setup the machine map
//		for(IHost h : vms.values()) {
//			Set<Integer> pid = h.getAlignments(parting.partitioning);
//			if (pid.size() != 1)
//				throw new RuntimeException("Should never happen!");
//			for(Integer i : pid) {
//				Alignment a = parting.partitioning.findAlignment(i);
//				if(a == null)
//					throw new RuntimeException("Should never happen!");
//				if(!vmMap.containsKey(a.getPartId())) {
//					vmMap.put(a.getPartId(), new ArrayList<IHost>());
//				}
//				//if(Utils.DEBUG)System.out.println("\tAdding "+node.getUniqueName()+" to part "+a.getPartId());
//				vmMap.get(a.getPartId()).add(h);
//			}
//		}
		return vms.size();
	}
	
	
	public static String executeCommand(String command) {
		System.out.println("Ececuting Command:"+command);
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}
	
	
	
	
	public void run() {
		listener.setController(this);
		try {
			running = true;
			listener.println("Starting exp by LocalEmulatedController!");
			exp.setState(jprime.State.STARTING);
			// A Runtime object has methods for dealing with the OS
			Runtime r = Runtime.getRuntime();
			BufferedReader stdout,stderr;  // reader for output of process
			String line1=null,line2=null,m=null;
			
			
			//zzz_start emulated hosts creation method invoked
			int vmCount= createEmulatedHosts();
			if (vmCount!=0){
				listener.println(vmCount+" emulated host created");
			}
			else 
				listener.println("No emulated host created ");
			//zzz_end emulated hosts created
			
			
			//if (isEmulated==true)
			//	this.cmd = "gksudo "+cmd;
			
			p = r.exec(cmd);
			exp.setState(jprime.State.RUNNING);

			// getInputStream gives an Input stream connected to
			// the process p's standard output. Just use it to make
			// a BufferedReader to readLine() what the program writes out.
			stdout = new BufferedReader(new InputStreamReader(p.getInputStream()));
			stderr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			//System.out.println("stdout:"+stdout);
			//System.out.println("stderr:"+stdout);
			while(running && !stop) {
				//System.out.println("LOOP>");//zzz
				try {
					int rv = p.exitValue();
					if(rv != 0)
						failure=true;
					break;
				} catch(IllegalThreadStateException e) {
				}
				m=null;
				while ((stdout.ready() && (line1 = stdout.readLine()) != null) || 
						(stderr.ready() && (line2 = stderr.readLine()) != null)) {
					if(line1!=null && line1.length() >0) {
						if(m==null)
							m=line1.trim();
						else {
							m+="\n"+line1.trim();
						}
					}
					if(line2!=null && line2.length() >0) {
						if(m==null)
							m=line2.trim();
						else {
							m+="\n"+line2.trim();
						}
					}
				}
				if(m!=null)
					System.out.println(m);
					listener.println(m);
				Thread.sleep(1000);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			failure=true;
		}
		finally {
			System.out.println("CLOSING simulator...");
			running=false;
			close();
		}
	}

	private void close() {
		listener.println("closing connection to sim.");
		try {
			try {
				int rv = p.exitValue();
				if(rv != 0)
					failure=true;
			} catch(IllegalThreadStateException e) {
				p.destroy();
			}
			int rv=-1;
			for(int i=10;i>0;i--) {
				try {
					rv = p.waitFor();
					break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			listener.println("Process done, exit status was " + rv);
			if(rv != 0)
				failure=true;
			running=false;
			if(prime_acceptor != null) {
				prime_acceptor.dispose();
			}
			listener.finishedExperiment(failure);
		} catch (IllegalThreadStateException e) {
			listener.println("error stopping sim!");
		}
		finally {
			db.stopThread();
		}
	}

	/* (non-Javadoc)
	 * @see monitor.core.IPrimeStateListener#handleAdvert(monitor.commands.ComPartAdvertCmd)
	 */
	public synchronized void handleAdvert(ComPartAdvertCmd advert, IoSession session) {
		HashMap<Integer, IoSession> h=session_map.get(advert.getPart_id());
		if(h==null) {
			h = new HashMap<Integer, IoSession>();
			session_map.put(advert.getPart_id(),h);
		}
		if(h.containsKey(advert.getCom_id()) && null != h.get(advert.getCom_id())) {
			jprime.Console.out.println("Got advert "+advert+" in session "+session.getId()+"["+session+"].\nWARNING: Already had session "+h.get(advert.getCom_id()).getId()+"!");
		}
		else {
			jprime.Console.out.println("Got advert "+advert+" in session "+session.getId()+"["+session+"].!");
		}
		if(advert.isReader()) {
			h.put(advert.getCom_id(), session);
			if(h.size() == this.parting.partitioning.getTotalNumberOfAlignments()) {
				for(waitingdata c : waiting) {
					send(c.aligns,c.cmd);
				}
				waiting=null;
			}
		}

	}

	/* (non-Javadoc)
	 * @see monitor.core.IPrimeStateListener#handleStateUpdate(monitor.commands.PrimeStateUpdateCmd)
	 */
	public synchronized void handleStateUpdate(PrimeStateExchangeCmd update, int com_id) {
		
		try {
			if(update.forViz()) {
				if(aggMap != null && update.isAggregate()) {
					//System.out.println("got agg update for "+update.getUid()+", com_id="+com_id+", time="+update.getTime());
					AggData ad = aggMap.get(update.getUid());
					if(ad == null) {
						throw new RuntimeException("wtf");
					}
					if(ad.agg == null) {
						ad.agg = new IncompleteAggValue();
					}
					//System.out.println("\thave "+ad.agg.have.size()+" coms of "+ad.target.size());
					StateExchangeCmd rv = ad.agg.processUpdate(update, com_id, ad.target);
					if(rv != null) {
						//System.out.println("\tcompleted an agg update");
						listener.handleStateUpdate(rv);
					}
				}
				else {
					listener.handleStateUpdate(new StateExchangeCmd(update,com_id));
				}
			}
			else {
				db.addWork(update);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}		
		
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#shutdown(boolean, boolean)
	 */
	public void shutdown(boolean sendShutdownCommand, boolean failed) {
		stop=true;
		listener.println("Stopping simulator.");
		if(!running) {
			close();
		}
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#handleStateUpdate(monitor.commands.StateExchangeCmd)
	 */
	public void handleStateUpdate(StateExchangeCmd update) {
		throw new RuntimeException("wtf");
		//listener.handleStateUpdate(update);
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getEmulatedHost2machineMap()
	 */
	public HashMap<Integer, ArrayList<IHost>> getEmulatedHost2machineMap() {
		return new HashMap<Integer, ArrayList<IHost>>();
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getFailures()
	 */
	public int getFailures() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getExpName()
	 */
	public String getExpName() {
		return parting.expName;
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
	public int getNumOutstandingCommands() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getNumOutstandingNonBlockingCommands()
	 */
	public int getNumOutstandingNonBlockingCommands() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getNumOutstandingBlockingCommands()
	 */
	public int getNumOutstandingBlockingCommands() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#connect(monitor.core.Provisioner.ComputeNode, java.util.List, java.lang.String)
	 */
	public void connect(ComputeNode master, List<ComputeNode> slaves,
			String keystore) throws GeneralSecurityException, IOException {
		//no op
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#shutdown(org.apache.mina.core.session.IoSession)
	 */
	public void shutdown(IoSession session) {
		stop=true;
		listener.println("Stopping simulator.");
		if(!running) {
			close();
		}
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#shutdown(org.apache.mina.core.session.IoSession, java.lang.Throwable)
	 */
	public void shutdown(IoSession session, Throwable cause) {
		stop=true;
		listener.println("Stopping simulator.");
		if(!running) {
			close();
		}
	}


	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, jprime.Host.IHost, java.util.Date, boolean)
	 */
	public synchronized boolean sendHostCommand(String cmd, IHost h, Date whenToRun,
			boolean checkReturnCode) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, jprime.Host.IHost, long, boolean)
	 */
	public synchronized boolean sendHostCommand(String cmd, IHost h, long delay,
			boolean checkReturnCode) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, java.util.Date, boolean, long)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid,
			Date whenToRun, boolean checkReturnCode, long runtime) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, java.util.Date, boolean)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid,
			Date whenToRun, boolean checkReturnCode) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, long, boolean, long)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid,
			long delay, boolean checkReturnCode, long runtime) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#sendHostCommand(java.lang.String, long, int, boolean, long)
	 */
	public synchronized boolean sendHostCommand(String cmd, long hid, int machineid,
			boolean checkReturnCode, long delay) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#sendCommand(monitor.commands.AbstractCmd)
	 */
	public synchronized boolean sendCommand(AbstractCmd cmd) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#getMasterId()
	 */
	public Integer getMasterId() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#startExperiment(int)
	 */
	public boolean startExperiment(int runtime) {
		return false;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#runExperiment(jprime.Experiment, int, monitor.core.ExpRunner)
	 */
	public ExpRunner runExperiment(Experiment exp, int runtime, ExpRunner runner) {
		this.runtime=runtime;
		return null;
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#cleanupExperiment(jprime.Experiment, monitor.core.IExpListenter)
	 */
	public void cleanupExperiment(Experiment exp, IExpListenter listener) {
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#setRuntimeAttribute(jprime.IModelNode, int, java.lang.String)
	 */
	public synchronized void setRuntimeAttribute(IModelNode node, int varId, String value) {
		Set<Integer> aligns = node.getAlignments(parting.partitioning);
		Set<Integer> coms = new HashSet<Integer>();
		for(int align : aligns) {
			Alignment a = parting.partitioning.findAlignment(align);
			if(coms.contains(a.getPartId())) continue;
			coms.add(a.getPartId());
			setRuntimeAttribute(node, varId, value, a.getAlignId(), a.getPartId());
		}		
	}

	/* (non-Javadoc)
	 * @see monitor.core.IController#setRuntimeAttribute(jprime.IModelNode, int, java.lang.String, int, int)
	 */
	public synchronized void setRuntimeAttribute(IModelNode node, int varId, String value, int com_id, int part_id) {
		HashMap<Integer, IoSession> h = session_map.get(part_id);
		if(h == null) {
			jprime.Console.err.println("Unable to find io sesison for machine id "+part_id);
			return;
		}
		IoSession s = h.get(com_id);
		if(s==null) {
			jprime.Console.err.println("Unable to find io sesison for com id "+com_id);
			return;
		}
		ArrayList<VarUpdate> updates = new ArrayList<VarUpdate>(1);
		updates.add(new StringUpdate(varId,TLVType.STRING,value));
		PrimeStateExchangeCmd cmd = new PrimeStateExchangeCmd(0,node.getUID(),false,false,StateExchangeCmd.SET,updates);
		s.write(cmd);

		updates = new ArrayList<VarUpdate>(1);
		updates.add(new StringUpdate(varId,TLVType.STRING,value));
		cmd = new PrimeStateExchangeCmd(0,node.getUID(),false,false,StateExchangeCmd.FETCH,updates);
		s.write(cmd);
	}

	public int getExperimentRuntime() {
		if(runtime == null)
			throw new RuntimeException("The experiment has not been started!");
		return runtime;
	}

	public synchronized void startDynamicTraffic(IStaticTrafficType dynamicTraffic) {
		final CreateDynamicModelNode cmd = new CreateDynamicModelNode((ModelNode)dynamicTraffic);
		for(HashMap<Integer, IoSession> h : session_map.values()) {
			for(IoSession s : h.values()) {
				s.write(cmd);				
			}
		}
	}
		
	public synchronized void addToAreaOfInterest(IModelNode m) {
		final PrimeAreaOfInterestUpdate cmd = new PrimeAreaOfInterestUpdate(m.getUID(), true);
		Set<Integer> aligns = m.getAlignments(parting.partitioning);
		if(waiting == null) {
			send(aligns,cmd);
		}
		else {
			waiting.add(new waitingdata(aligns, cmd));
		}
	}
	public synchronized void addToAreaOfInterest(Collection<IModelNode> ms) {
		HashSet<Integer> aligns = new HashSet<Integer>();
		long[] uids = new long[ms.size()];
		int i=0;
		for(IModelNode  m: ms) {
			uids[i]=m.getUID();
			aligns.addAll(m.getAlignments(parting.partitioning));
			i++;
		}
		final PrimeAreaOfInterestUpdate cmd = new PrimeAreaOfInterestUpdate(uids, true);
		if(waiting == null) {
			send(aligns,cmd);
		}
		else {
			waiting.add(new waitingdata(aligns, cmd));
		}
	}
	
	public void adjustVizExportRate(long newRate) {
		for(HashMap<Integer, IoSession> e : session_map.values()) {
			for(IoSession s : e.values()) {
				s.write(new PrimeUpdateVizExportRate(newRate));
			}
		}
	}


	public synchronized void removeFromAreaOfInterest(IModelNode m) {
		final PrimeAreaOfInterestUpdate cmd = new PrimeAreaOfInterestUpdate(m.getUID(), false);
		Set<Integer> aligns = m.getAlignments(parting.partitioning);
		if(waiting == null) {
			send(aligns,cmd);
		}
		else {
			waiting.add(new waitingdata(aligns, cmd));
		}
	}
	public synchronized void removeFromAreaOfInterest(Collection<IModelNode> ms) {
		HashSet<Integer> aligns = new HashSet<Integer>();
		long[] uids = new long[ms.size()];
		int i=0;
		for(IModelNode  m: ms) {
			uids[i]=m.getUID();
			aligns.addAll(m.getAlignments(parting.partitioning));
			i++;
		}
		final PrimeAreaOfInterestUpdate cmd = new PrimeAreaOfInterestUpdate(uids, false);
		if(waiting == null) {
			send(aligns,cmd);
		}
		else {
			waiting.add(new waitingdata(aligns, cmd));
		}
	}	
	private void send(Set<Integer> alings, AbstractCmd cmd) {
		jprime.Console.out.println("sending cmd "+cmd);
		System.out.println("Command Sent: "+cmd);
		
		for(Integer com : alings) {
			for(HashMap<Integer, IoSession> h : session_map.values()) {
				if(h.containsKey(com)) {
					h.get(com).write(cmd);
					jprime.Console.out.println("\tsent to com "+h.get(com).getAttribute(PrimeSessionHandler.COM_ID));
				}
			}
		}
	}
}
