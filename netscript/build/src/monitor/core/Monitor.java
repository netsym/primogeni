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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import monitor.commands.AbstractCmd;
import monitor.commands.BlockingCmdResult;
import monitor.commands.CodecFactory;
import monitor.commands.ComPartAdvertCmd;
import monitor.commands.NonBlockingCmdResult;
import monitor.commands.PrimeAreaOfInterestUpdate;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.StateExchangeCmd;
import monitor.ssl.PrimoSslContextFactory;
import monitor.util.Utils;
import monitor.util.Utils.CmdRv;
//import monitor.util.Utils.;

import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/* $if TEST_TP $
import java.io.BufferedWriter;
import java.io.FileWriter;
$endif$ */

/**
 * @author Nathanael Van Vorst
 *
 */
public class Monitor implements IPrimeStateListener {
	public static class MetaCmd {
		public final int serialNumber;
		public final ArrayList<Integer> outstandingCmds;
		public int returnCodeSum;
		public final ArrayList<String> msgs;
		public MetaCmd(int serialNumber) {
			super();
			System.out.println("Creating a meta cmd with serial=" + serialNumber);
			if(Utils.DEBUG)System.out.println("Creating a meta cmd with serial=" + serialNumber);
			this.serialNumber = serialNumber;
			this.outstandingCmds = new ArrayList<Integer>();
			this.msgs = new ArrayList<String>();
			this.returnCodeSum = 0;
		}

		void wait(int serialNum) {
			outstandingCmds.add(serialNum);
		}

		int finished(int serialNum, int rc, String msg) {
			if(serialNum>=0)
				throw new RuntimeException("Should  never happen!");
			if(outstandingCmds.remove(new Integer(serialNum))) {
				rc+=Math.abs(rc);
				if(rc!=0)
					msgs.add(msg);
			}
			else {
				throw new RuntimeException("Should  never happen!");
			}
			return outstandingCmds.size();
		}
		String getMsgs() {
			String rv =null;
			for(String s:msgs) {
				if(rv == null)
					rv="{ '";
				else
					rv +=", '";
				rv+=s+"'";
			}
			rv+=" }";
			return rv;
		}
	}
	public static class AggData {
		final Set<Integer> target;
		IncompleteAggValue agg;
		public AggData(Set<Integer> target) {
			this.target=target;
			this.agg=null;
		}
	}
	private final HashMap<Long,AggData> agg2coms = new HashMap<Long, AggData>();
	

	private final HashMap<Integer,TreeMap<Integer,IoSession>> session_map= new HashMap<Integer, TreeMap<Integer,IoSession>>();
	private final HashMap<Integer, MetaCmd> metaCmds = new HashMap<Integer, MetaCmd>();
	private final int master_port, slave_port, prime_port;
	private final long timeout;
	private final MasterSessionHandler master_handler;
	private final SlaveSessionHandler slave_handler;
	private final PrimeSessionHandler prime_handler;
	private final Map<Integer, NodeConfig> slaveMachineId2config;
	private final Map<Integer, NodeConfig> slavePartitionId2config;
	private final String keystore;
	private final BackgroundCmdExecutor backgrounder;
	private NioSocketAcceptor master_acceptor, slave_acceptor, prime_acceptor;
	private NioSocketConnector connector;
	private boolean isSlave, isMaster;
	private IoSession ownerSession;
	private NodeConfig masterConfig;
	private byte[] ssh_publicKey;
	private byte[] ssh_privateKey;
	private boolean shuttingDown;
    private DataStorageThread db;
    private long[] initialAOI;
    
	/* $if TEST_TP $
	private static BufferedWriter timming_debug_=null;
	private static Long real_start=null,sim_start=null;
	private static long objs=0, states=0;
	public static synchronized void timming_debug_write(long sim_time, int state_count) {
		if(sim_start==null) {
			//first
			sim_start=sim_time;
			real_start=System.currentTimeMillis();
			objs=1;
			states=state_count;
		}
		else if(sim_start < sim_time) {
			//start new batch....
			//#real_time_start, real_time_end, sim_time_start, sim_time_end, obj_count, state_count\n
			objs++;
			states+=state_count;
			try {
				timming_debug_.write(real_start+","+System.currentTimeMillis()+","+sim_start+","+sim_time+","+objs+","+states+"\n");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			sim_start=sim_time;
			real_start=System.currentTimeMillis();
			objs=1;
			states=state_count;
		}
		else {
			objs++;
			states+=state_count;
		}
	}
	$endif$ */
    
	public Monitor(int master_port, int slave_port, int prime_port, String keystore) {
		this(master_port, slave_port, prime_port, 30*1000L, keystore);
	}

	public Monitor(int master_port, int slave_port, int prime_port, long timeout, String keystore) {
		super();
		this.master_port = master_port;
		this.slave_port = slave_port;
		this.prime_port = prime_port;
		this.timeout = timeout;
		this.keystore = keystore;
		this.slave_handler = new SlaveSessionHandler(this);
		this.master_handler = new MasterSessionHandler(this);
		this.prime_handler=new PrimeSessionHandler(this);
		this.slaveMachineId2config = new HashMap<Integer, NodeConfig>();
		this.slavePartitionId2config = new HashMap<Integer, NodeConfig>();
		this.backgrounder = new BackgroundCmdExecutor(this);
		this.connector = null;
		this.master_acceptor = null;
		this.slave_acceptor = null;
		this.prime_acceptor = null;
		this.isSlave=false;
		this.isMaster=false;
		this.ownerSession=null;
		this.ssh_publicKey=null;
		this.ssh_privateKey=null;
		this.shuttingDown=false;
		this.initialAOI=null;
		this.db=null;
		//System.out.println("Monitor Setting Variables");
		/* $if TEST_TP $
		try {
			if(timming_debug_ != null)
				throw new RuntimeException("wtf?");
			timming_debug_=new BufferedWriter(new FileWriter(new File("/tmp/"+System.getProperty("CSV","ACK")+".csv")));
			timming_debug_.write("#real_time_start, real_time_end, sim_time_start, sim_time_end, obj_count, state_count\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		$endif$ */
	}


	public synchronized void listen() {
		if(this.master_acceptor != null) {
			throw new RuntimeException("Already listening!");
		}
		try {
			// Socket for receiving messages
			master_acceptor = new NioSocketAcceptor();
			slave_acceptor = new NioSocketAcceptor();
			prime_acceptor = new NioSocketAcceptor();
			master_acceptor.setReuseAddress(true);
			slave_acceptor.setReuseAddress(true);
			prime_acceptor.setReuseAddress(true);


			if(Utils.enableSSL) {
				master_acceptor.getFilterChain().addLast("sslFilter", PrimoSslContextFactory.getServerFilter(keystore));
				slave_acceptor.getFilterChain().addLast("sslFilter", PrimoSslContextFactory.getServerFilter(keystore));
			}

			// Add codecs and a filter to the the Filter chain
			master_acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new CodecFactory()));
			slave_acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new CodecFactory()));
			prime_acceptor.getFilterChain().addLast("codec",new ProtocolCodecFilter(new CodecFactory(true)));


			// Set the handler
			master_acceptor.setHandler(this.master_handler);
			slave_acceptor.setHandler(this.slave_handler);
			prime_acceptor.setHandler(this.prime_handler);

			// Bind the socket to a port
			master_acceptor.bind(new InetSocketAddress(this.master_port));
			slave_acceptor.bind(new InetSocketAddress(this.slave_port));
			prime_acceptor.bind(new InetSocketAddress(this.prime_port));
		} catch(Exception e) {
			e.printStackTrace();
			Runtime.getRuntime().halt(1);
		}
	}

	public synchronized void connect(final NodeConfig slave, final int port) throws GeneralSecurityException, IOException {
		if(Utils.DEBUG)System.out.println("Start connect(slave="+slave+", port="+port);
		if(connector == null) {
			// Socket for sending to other nodes
			connector = new NioSocketConnector();        
			if(Utils.enableSSL) {
				//XXX the private key needs to be sent over...
				//for now we assume the keystore already has it.....
				connector.getFilterChain().addLast("sslFilter", PrimoSslContextFactory.getClientFilter(keystore));
			}

			// Configure the service
			connector.setConnectTimeoutMillis(this.timeout);
			connector.getFilterChain().addLast("codec",	new ProtocolCodecFilter(new CodecFactory()));

			// Set handler for the connector socket
			connector.setHandler(this.master_handler);
		}
		if(Utils.DEBUG)System.out.println("\tconnecting to ip="+slave.control_ip+", port="+port);
		Utils.appendMsgToFile("/tmp/pgc_debug_msg","\tconnecting to ip="+slave.control_ip+", port="+port);
		//System.out.println("\tconnecting to ip="+slave.control_ip+", port="+port); //obaida xxxxxxxxxx
		// Connect the slave
		ConnectFuture future = connector.connect(new InetSocketAddress(slave.control_ip, port));
		future.awaitUninterruptibly();
		if(Utils.DEBUG)System.out.println("\tconnected to ip="+slave.control_ip+", port="+port);
		Utils.appendMsgToFile("/tmp/pgc_debug_msg","\tconnected to ip="+slave.control_ip+", port="+port);
		
		slave.session=future.getSession();
		this.slaveMachineId2config.put(slave.machineId, slave);
		this.slavePartitionId2config.put(slave.partition_id, slave);
		if(Utils.DEBUG)System.out.println("finish connect(slave="+slave+", port="+port);
		Utils.appendMsgToFile("/tmp/pgc_debug_msg","finish connect(slave="+slave+", port="+port);
	}

	public synchronized void shutdown() {
		if(shuttingDown)
			return;
		/* $if TEST_TP $
		try {
			timming_debug_.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		timming_debug_=null;
		$endif$ */
		shuttingDown=true;
		Thread shutdown = new Thread() {
			public void run() {
				if(Utils.DEBUG)System.out.println("Start shutdown");
				for(NodeConfig nc : slaveMachineId2config.values()) {
					if(Utils.DEBUG)System.out.println("Disconnecting from slave: ip="+nc.control_ip+", "+nc.machineId);
					if(nc.session != null && nc.session.isConnected()) {
						CloseFuture closeFuture = nc.session.getCloseFuture();
						closeFuture.getSession().close(false);
						closeFuture.awaitUninterruptibly();
					}
					else {
						if(Utils.DEBUG)System.out.println("\tSlave was already disconnected...");
					}
				}
				if(ownerSession != null) {
					if(Utils.DEBUG)System.out.println("Disconnecting from owner");
					CloseFuture closeFuture = ownerSession.getCloseFuture();
					closeFuture.getSession().close(false);
					closeFuture.awaitUninterruptibly();
				}
				else {
					if(Utils.DEBUG)System.out.println("\tOwner was already disconnected...");
				}
			}
		};
		try {
			shutdown.join(60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		shutdown = new Thread() {
			public void run() {
				if(Utils.DEBUG)System.out.println("Cleaning up");
				CmdRv rv = Utils.executeCommand("perl "+Utils.PRIMOGENI_FOLDER+"/shutdown.pl");
				if(Utils.DEBUG)System.out.println("\t"+rv.msg);
				if(Utils.DEBUG)System.out.println("Exiting");
			}
		};
		try {
			shutdown.join(60*1000*10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Runtime.getRuntime().halt(0);
	}

	public synchronized void sessionClosed(IoSession session) {
		if(!shuttingDown) {
			//something went wrong!
			boolean found=false;
			for(NodeConfig nc : slaveMachineId2config.values()) {
				if(nc.session == session) {
					if(Utils.DEBUG)System.out.println("The sesession to slave ip="+nc.control_ip+", id="+nc.machineId+" closed.");
					found = true;
				}
			}
			if(!found && ownerSession != null && session == ownerSession) {
				if(Utils.DEBUG)System.out.println("Our owning session disconnected!");
				found=true;
				ownerSession=null;
			}
			if(!found){
				if(Utils.DEBUG)System.out.println("An unknown session disconnected!");
			}
			shutdown();
		}
	}

	protected synchronized void askedToBeSlave(IoSession masterSession) {
		if(Utils.DEBUG)System.out.println("askedToBeSlave");
		if(this.isMaster) {
			System.err.println("I was a master but I was asked to become a slave.");
			Runtime.getRuntime().halt(1);
		}
		if(this.ownerSession==null) {
			if(Utils.DEBUG)System.out.println("I am the slave");
			this.isSlave=true;
			this.ownerSession=masterSession;
		}
	}

	protected synchronized void askedToBeMaster(IoSession slingshotSession) {
		if(Utils.DEBUG)System.out.println("askedToBeMaster");
		if(this.isSlave) {
			System.err.println("I was a slave but I was asked to become a master.");
			Runtime.getRuntime().halt(1);
		}
		if(this.ownerSession==null) {
			if(Utils.DEBUG)System.out.println("I am the master");
			this.isMaster=true;
			this.ownerSession=slingshotSession;
		}
	}

	public synchronized int sendCommand(int id, AbstractCmd cmd, MetaCmd mc) {
		if(cmd.getSerialNumber()==0) {
			cmd.setSerialNumber(Utils.nextSerial()*-1);
		}
		final IoSession s = slaveMachineId2config.get(id).session;//obaida
		cmd.setMachineId(id);
		if(s == null) {
			if(Utils.DEBUG)System.out.println("No slave with id "+id);
			throw new RuntimeException("No slave with id "+id);
		}
		if(Utils.DEBUG)System.out.println("Added sub cmd " + cmd.getSerialNumber() + " --> " + mc.serialNumber);
		mc.wait(cmd.getSerialNumber());
		metaCmds.put(cmd.getSerialNumber(), mc);

		if(Utils.DEBUG)System.out.println("Sending Command to machine/part "+cmd.getMachineId()+" cmd=" + cmd);
		s.write(cmd);
		return cmd.getSerialNumber();
	}
	protected synchronized void commandFinished(boolean wasBlocking,int commandId) {
		commandFinished(wasBlocking,commandId,0,"");
	}
	protected synchronized void sendToController(AbstractCmd cmd) {
		if(ownerSession != null) {
			ownerSession.write(cmd);
		}
	}	
	protected synchronized void commandFinished(boolean wasBlocking, int commandId, int status, String msg) {
		if(isSlave) {
			//always send result to owner...
			if(wasBlocking)
				ownerSession.write(new BlockingCmdResult(commandId, status, msg));
			else
				ownerSession.write(new NonBlockingCmdResult(commandId, status, msg));
		}
		else if(isMaster){
			//if the commandId<0 the controller doesn't need to know about it....
			if(commandId<0) {
				throw new RuntimeException("Should not happen!");
			}
			else {
				if(wasBlocking)
					ownerSession.write(new BlockingCmdResult(commandId, status, msg));
				else
					ownerSession.write(new NonBlockingCmdResult(commandId, status, msg));
			}
		}
		else {
			throw new RuntimeException("Should never see this!");
		}
	}

	protected synchronized BackgroundCmdExecutor getBackgroundCmdExecutor() {
		return backgrounder;
	}

	protected synchronized MetaCmd getMetaCmd(Integer subCmdSerial) {
		return metaCmds.get(subCmdSerial);
	}

	protected synchronized Collection<NodeConfig> getSlaveConfigs() {
		return slaveMachineId2config.values();
	}
	
	protected synchronized Set<Integer> getSlaveIds() {
		return slaveMachineId2config.keySet();
	}

	protected synchronized NodeConfig getSlaveConfigFromMachineID(int id) {
		return slaveMachineId2config.get(id);
	}

	protected synchronized NodeConfig getSlaveConfigFromPartitionID(int id) {
		return slavePartitionId2config.get(id);
	}
	
	protected synchronized NodeConfig getMasterConfig() {
		return masterConfig;
	}

	protected synchronized void setMasterConfig(NodeConfig masterConfig) {
		this.masterConfig = masterConfig;
	}

	protected synchronized  String generateMpiMachineFile(List<NodeConfig> slave_list) {
		String machine_file = "";
		if(Utils.DEBUG)System.out.println("generating MPIMachineFile");
		Collections.sort(slave_list, new Comparator<NodeConfig>() {
			public int compare(NodeConfig l, NodeConfig r) {
				if(l.partition_id == r.partition_id)
					return 0;
				if(l.partition_id < r.partition_id)
					return -1;
				return 1;
			}
		});
		for(NodeConfig n : slave_list){
			if(Utils.DEBUG)System.out.println("\t Adding node (" + n.control_ip+")[ip="+n.data_ip+", id="+n.machineId+"] to machinefile");
			machine_file += n.data_ip + ":1\n";
		}
		File file = new File(Utils.MACHINE_FILE);
		if(Utils.DEBUG)System.out.println("Creating file "+file.getPath());
		if(file.getParentFile() !=null)
			file.getParentFile().mkdirs();
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(machine_file.getBytes());
			fos.close();
		} catch (IOException e) {
			if(Utils.DEBUG)System.out.println("Error creating file:");
			e.printStackTrace();
		}
		return machine_file;
	}

	protected synchronized String generatePVFSConfig() {
		//XXX
		if(Utils.DEBUG)System.out.println("finish generatePVFSConfig");
		return "XXX generatePVFSConfig NOT DONE";
	}

	public synchronized CmdRv generateSSHKeys() {
		File f = new File(Utils.ROOT_SSH);
		if(!f.exists()) {
			f.mkdirs();
		}
		CmdRv rv = Utils.executeCommand("perl "+Utils.PRIMOGENI_FOLDER+"/genKeys.pl "+Utils.ROOT_SSH); //obaida
		System.out.println("GeneratePVFSConfig: perl "+Utils.PRIMOGENI_FOLDER+"/genKeys.pl "+Utils.ROOT_SSH);//xxxxxx
		if( 0 != rv.status) {
			return rv;
		}
		try {
			FileInputStream fis = new FileInputStream(new File(Utils.ROOT_SSH+"id_dsa"));
			byte[] temp = new byte[5000];
			int read = fis.read(temp, 0, 5000);
			if(read==5000) {
				if(Utils.DEBUG)System.out.println("The private key is huge(>5000)...what happened?");
				return new CmdRv(100, "The private key is huge(>5000)...what happened?");
			}
			this.ssh_privateKey = new byte[read];
			for(int i=0;i<read;i++)this.ssh_privateKey[i]=temp[i];
			fis.close();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			if(Utils.DEBUG)System.out.println("failed to read the private key, st:\n"+sw.toString());
			return new CmdRv(100, "failed to read the private key, st:\n"+sw.toString());
		}

		try {
			FileInputStream fis = new FileInputStream(new File(Utils.ROOT_SSH+"id_dsa.pub"));
			byte[] temp = new byte[5000];
			int read = fis.read(temp, 0, 5000);
			if(read==5000) {
				if(Utils.DEBUG)System.out.println("The public key is huge(>5000)...what happened?");
				return new CmdRv(100, "The public key is huge(>5000)...what happened?");
			}
			this.ssh_publicKey = new byte[read];
			for(int i=0;i<read;i++)this.ssh_publicKey[i]=temp[i];
			fis.close();			
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			if(Utils.DEBUG)System.out.println("failed to read the public key, st:\n"+sw.toString());
			return new CmdRv(100, "failed to read the public key, st:\n"+sw.toString());
		}
		return new CmdRv(0, "");
	}

	public byte[] getSsh_publicKey() {
		return ssh_publicKey;
	}

	public byte[] getSsh_privateKey() {
		return ssh_privateKey;
	}
	
	protected IoSession getPrimeSession(int part_id, int com_id) {
		synchronized(session_map) {
			final TreeMap<Integer,IoSession> h = session_map.get(part_id);
			if(h==null)
				return null;
			return h.get(com_id);
		}
	}
	
	protected String debug_get_session_info() {
		synchronized(session_map) {
			String rv="[";
			for(Entry<Integer, TreeMap<Integer, IoSession>> e: session_map.entrySet()) {
				rv+="{part="+e.getKey()+", coms=(";
				for(int i : e.getValue().keySet()) {
					rv+=", "+i;
				}
				rv+=")}";
			}
			rv+="]";
			return rv;
		}
	}
	
	protected IoSession getPrimeSessionWithMinCommunityId(int part_id) {
		synchronized(session_map) {
			final TreeMap<Integer,IoSession> h = session_map.get(part_id);
			if(h==null)
				return null;
			return h.firstEntry().getValue();
		}
	}

	protected void broadcastCommand(AbstractCmd cmd) {
		synchronized(session_map) {
			for(TreeMap<Integer, IoSession> e : session_map.values()) {
				for(IoSession s : e.values()) {
					s.write(cmd);
				}
			}
		}
	}
	public void handleStateUpdate(StateExchangeCmd update) {
		//System.out.println("got state update destined for slingshot: "+update);
		if(update.aggregate()) {
			//System.out.println("\tgot agg update");
			AggData ad = agg2coms.get(update.getUid());
			if(ad == null) {
				throw new RuntimeException("wtf");
			}
			if(ad.agg == null) {
				ad.agg = new IncompleteAggValue();
			}
			//System.out.println("\thave "+ad.agg.have.size()+" coms of "+ad.target.size());
			StateExchangeCmd rv = ad.agg.processUpdate(update, ad.target);
			if(rv != null) {
				//System.out.println("\tcompleted an agg update");
				// forward to controller
				/* $if TEST_TP $
				timming_debug_write(rv.getTime(),rv.getUpdates().size());
				$endif$ */
				this.sendToController(rv);
			}
			/* $if TEST_TP $
			else {
				timming_debug_write(update.getTime(),update.getUpdates().size());
			}
			$endif$ */
		}
		else {
			//System.out.println("\tgot simple update");
			// forward to controller
			/* $if TEST_TP $
			timming_debug_write(update.getTime(),update.getUpdates().size());
			$endif$ */
			this.sendToController(update);
		}
	}

	public void handleStateUpdate(PrimeStateExchangeCmd update, int com_id) {
		try {
			//System.out.println("got state update:"+update);
			if(isSlave) {
				if(update.forViz()) {
					//System.out.println("\t it is destined for slingshot, com_id="+com_id);
					/* $if TEST_TP $
					timming_debug_write(update.getTime(),update.getUpdates().size());
					$endif$ */
					this.sendToController(new StateExchangeCmd(update,com_id));
				}
				else {
					//System.out.println("\t we are saving it locally");
					/* $if TEST_TP $
					timming_debug_write(update.getTime(),update.getUpdates().size());
					$endif$ */
					db.addWork(update);
				}
			}
			else if(isMaster){
				throw new RuntimeException("wtf? only slaves should see prime state updates!");
			}
			else {
				throw new RuntimeException("wtf? i am not a slave or master!");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public final HashMap<Long,AggData> getAgg2coms() {
		return agg2coms;
	}

	public long[] getInitialAOI() {
		return initialAOI;
	}
	
	public void setInitialAOI(long[] intialAOI) {
		this.initialAOI=intialAOI;
	}
	
	/* (non-Javadoc)
	 * @see monitor.core.IPrimeStateListener#handleAdvert(monitor.commands.ComPartAdvertCmd)
	 */
	public void handleAdvert(ComPartAdvertCmd advert, IoSession session) {
		synchronized(session_map) {
			TreeMap<Integer, IoSession> h=session_map.get(advert.getPart_id());
			if(h==null) {
				h = new TreeMap<Integer, IoSession>();
				session_map.put(advert.getPart_id(),h);
			}
			if(advert.isReader()) {
				if(h.containsKey(advert.getCom_id()) && null != h.get(advert.getCom_id())) {
					jprime.Console.out.println("Got advert "+advert+" in session "+session.getId()+"["+session+"].\nWARNING: Already had session "+h.get(advert.getCom_id()).getId()+"!");
				}
				else {
					jprime.Console.out.println("Got advert "+advert+" in session "+session.getId()+"["+session+"].!");
				}
				h.put(advert.getCom_id(), session);
				if(initialAOI == null) {
					System.out.println("How did it connect before the initial AOI was set?");
				}
				else {
					PrimeAreaOfInterestUpdate aoi = new PrimeAreaOfInterestUpdate(initialAOI, true);
					System.out.println("sending intial AOI to prime: "+aoi);
					session.write(aoi);
				}
			}
		}
	}
	
	protected void setupDataDB(String expdir, int machine_id) {
		File d = new File(expdir);
		if(!d.exists()) {
			d.mkdir();
		}
		else if(!d.isDirectory()) {
			throw new RuntimeException("wtf?");
		}
		this.db = new DataStorageThread(expdir+"/prime_data_"+machine_id+".csv");
		this.db.start();
	}
	public static void main(String[] args) throws IOException, GeneralSecurityException {

		String keystore=null;
		if(Utils.enableSSL) {
			if(args.length ==1) {
				keystore=args[0];
			}
			else {
				throw new RuntimeException("Must specify a keystore!");
			}
			while(true) {
				if(new File(keystore).exists())
					break;
				if(Utils.DEBUG)System.out.println("Waiting for "+keystore+" to exist");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}
		}

		Monitor m = new Monitor(Utils.MASTER_PORT, Utils.SLAVE_PORT,Utils.PRIME_PORT,keystore);
		m.listen();
		if(Utils.DEBUG)System.out.println("Listening on ports: master="+Utils.MASTER_PORT+", slave="+Utils.SLAVE_PORT+", prime="+Utils.PRIME_PORT+"!");
	}
}
