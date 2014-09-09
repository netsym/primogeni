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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import jprime.Host.Host;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Net.INet;
import jprime.PingTraffic.IPingTraffic;
import jprime.TCPTraffic.ITCPTraffic;
import jprime.Traffic.ITraffic;
import jprime.UDPTraffic.IUDPTraffic;
import jprime.gen.IRouter;
import jprime.util.ChildList;

/**
 * @author Nathanael Van Vorst

Basic Usage:
		INet topnet = null; //this would be the real topnet
		IHost srcHost=null, dstHost=null; //just place holder -- should use real hosts/nets
		INet srcNet=null, dstNet=null;//just place holder -- should use real hosts/nets
		
		TrafficFactory tFactory = new TrafficFactory(topnet, "my_traffic");
		
		//have srcHost send 10 pings to dstHost at time 1.0
		tFactory.createSimulatedPing(1.0, 10, srcHost,dstHost);
		
		//have all hosts in srcNet contact dstHost and download 10 megs
		//this is dynamic traffic and the name would be 'my_traffic_XX' where XX is an integer
		//every 10 second 1 host from srcNet will be chosen to start after the traffic is started 
		tFactory.createSimulatedTCP(2.0, ArrivalProcess.CONSTANT, 10, 1, 1024*1024*10, srcNet, dstHost);
		
		//have all hosts in srcNet contact the REAL host 131.14.1.3 and download 10 megs
		//this is dynamic traffic and the name would be 'my_traffic_XX' where XX is an integer
		//hosts will be hosts to start using an exponential distribution with an off_time of 0.1
		tFactory.createHybridTCP(3.0, ArrivalProcess.EXPONENTIAL, 0.1, 1, 1024*1024*10, srcNet, "131.14.1.3");
		
 */
public class TrafficFactory {
	protected static final int default_arrival_rate=1;
	protected static final int default_number_of_flows=1;
	protected static class pair {
		final IHost src;
		final IHost dst;
		final String dst_name;
		public pair(IHost src, IHost dst, String dst_name) {
			super();
			this.src = src;
			this.dst=dst;
			this.dst_name = dst_name;
		}
	};
	
	public static enum ArrivalProcess {
		CONSTANT("false"),
		EXPONENTIAL("true");
		public final String str;
		private ArrivalProcess(String str) { this.str=str;}
		public String toString() {return str;}
	}
	public static enum Mapping {
		ONE2ONE("one2one"),
		ONE2MANY("one2many"),
		MANY2ONE("many2one"),
		ALL2ALL("all2all");
		
		public final String str;
		private Mapping(String str) { this.str=str;}
		public String toString() {return str;}
	}
	protected final INet owner;
	protected final ITraffic tm;
	protected final String base_name;
	protected final Random rand;
	protected int name_count;
	
	
	
	protected TrafficFactory(INet owner, ITraffic tm, String base_name) {
		super();
		this.owner = owner;
		this.tm = tm;
		this.base_name = base_name;
		this.name_count = 0;
		this.rand = new Random();
	}

	public TrafficFactory(INet owner) {
		this.owner=owner;
		this.base_name=null;
		this.name_count = 0;
		this.rand = new Random();
		ChildList<ITraffic> tms = owner.getTraffics();
		if(tms.size()==0)
			tm = owner.createTraffic();
		else if(tms.size()==1)
			tm = tms.get(0);
		else throw new RuntimeException("how did this happen?");
	}
	
	public TrafficFactory(INet owner, String base_name) {
		this.owner=owner;
		this.base_name=base_name;
		this.name_count = 0;
		this.rand = new Random();
		ChildList<ITraffic> tms = owner.getTraffics();
		if(tms.size()==0)
			tm = owner.createTraffic();
		else if(tms.size()==1)
			tm = tms.get(0);
		else throw new RuntimeException("how did this happen?");
	}
	
	protected String nextName() {
		if(base_name==null)
			return null;
		return base_name+(++name_count);
	}

	protected void findHosts(INet node, List<IHost> hosts, boolean emulated) {
		for(IModelNode n : node.getAllChildren()) {
			if(n instanceof INet)
				findHosts((INet)n, hosts, emulated);
			else if(n instanceof IHost && !(n instanceof IRouter)) {
				if(!emulated)
					hosts.add((IHost)n);
				else if(emulated && ((IHost)n).hasEmulationProtocol())
					hosts.add((IHost)n);
			}
		}
	}
	protected void checkArgs(
			List<IHost> srcs,
			List<IHost> dsts,
			List<String> emu_dsts,
			List<String> real_dsts) {
		if(srcs == null)
			throw new RuntimeException("Sources cannot be null.");
		if(dsts == null &&  emu_dsts == null && real_dsts == null) {
			throw new RuntimeException("No destinations found. Must set one type of dest(sim/real/emu)!");	
		}
		else if(dsts != null &&  (emu_dsts != null || real_dsts != null)) {
			throw new RuntimeException("Found simulated and real/emulated destinations. Must set only one type of dest(sim/real/emu)!");	
		}
		else if(emu_dsts != null &&  (dsts != null || real_dsts != null)) {
			throw new RuntimeException("Found emulated and sim/emulated destinations. Must set only one type of dest(sim/real/emu)!");	
		}
		else if(real_dsts != null &&  (emu_dsts != null || dsts != null)) {
			throw new RuntimeException("Found real and sim/emulated destinations. Must set only one type of dest(sim/real/emu)!");	
		}
	}
	
	protected String compileSimulatedHosts(List<IHost> nodes, boolean allowEmu) {
		StringBuffer rv = new StringBuffer();
		for(IHost h : nodes) {
			if(!allowEmu && h.hasEmulationProtocol()) {
				throw new RuntimeException(h.getUniqueName()+" is emulated. Expecting simulated hosts.");
				//continue;");
				//jprime.Console.err.println("Not using "+h.getUniqueName()+" as a traffic source/sink since it is emulated!");
				//continue;
			}
			if(h instanceof IRouter) {
				throw new RuntimeException(h.getUniqueName()+" is a router. Expecting hosts.");
			}
			if(rv.length() == 0) rv.append("{");
			else rv.append(",");
			rv.append(".:"+h.getUniqueName().toString().substring(owner.getUniqueName().toString().length()+1));
		}
		if(rv.length() == 0) rv.append("{}");
		else rv.append("}");
		return rv.toString();
	}
	
	protected String compileIps(List<String> ips) {
		StringBuffer rv = new StringBuffer();
		for(String ip : ips) {
			if(rv.length() > 0) rv.append(",");
			rv.append(ip);
		}
		return rv.toString();
	}

	protected double exponential(double rate) {
		if(rate <= 0) {
		  throw new RuntimeException("the rate must be >0.");
		}
		return -1.0/(rate*Math.log(1.0-rand.nextDouble()));
	}
	
	protected List<pair> getEmuMapping(Mapping mapping, 
			final List<IHost> srcs,
			final List<IHost> dsts,
			final List<String> real_dsts) {
		final List<pair> pairs = new LinkedList<TrafficFactory.pair>();
		switch(mapping) {
		case ALL2ALL:
			if(real_dsts != null && real_dsts.size()>0) {
				for(IHost src : srcs) {
					if(!src.hasEmulationProtocol()) {
						throw new RuntimeException(src.getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
						//jprime.Console.err.println("Not using "+src.getUniqueName()+" as a traffic source since it is not emulated!");
						//continue;
					}
					for(String dst : real_dsts) {
						pairs.add(new pair(src,null,dst));
					}
				}
			}
			else {
				for(IHost src : srcs) {
					if(!src.hasEmulationProtocol()) {
						throw new RuntimeException(src.getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
						//jprime.Console.err.println("Not using "+src.getUniqueName()+" as a traffic source since it is not emulated!");
						//continue;
					}
					for(IHost dst : dsts) {
						if(!dst.hasEmulationProtocol()) {
							throw new RuntimeException(dst.getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
							//jprime.Console.err.println("Not using "+dst.getUniqueName()+" as a traffic sink since it is not emulated!");
							//continue;
						}
						final IInterface iface=Host.getDefaultInterface(dst);
						pairs.add(new pair(src,dst, "$$"+iface.getUniqueName().toString()));
					}
				}
			}
			break;
		case MANY2ONE://only use each src is used ONCE
		case ONE2MANY://only use each dst is used ONCE
		{
			
			int dst_idx=0;
			if(real_dsts != null && real_dsts.size()>0) {
				Collections.shuffle(real_dsts);
				for(IHost src : srcs) {
					if(!src.hasEmulationProtocol()) {
						throw new RuntimeException(src.getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
						//jprime.Console.err.println("Not using "+src.getUniqueName()+" as a traffic source since it is not emulated!");
						//continue;
					}
					pairs.add(new pair(src,null,real_dsts.get(dst_idx%real_dsts.size())));
					dst_idx++;
					if(dst_idx>=real_dsts.size() && mapping == Mapping.ONE2MANY)
						break;
				}
			}
			else {
				Collections.shuffle(dsts);
				for(IHost src : srcs) {
					if(!src.hasEmulationProtocol()) {
						throw new RuntimeException(src.getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
						//jprime.Console.err.println("Not using "+src.getUniqueName()+" as a traffic source since it is not emulated!");
						//continue;
					}
					final IHost dst = dsts.get(dst_idx%real_dsts.size());
					if(!dst.hasEmulationProtocol()) {
						throw new RuntimeException(dst.getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
						//jprime.Console.err.println("Not using "+dst.getUniqueName()+" as a traffic sink since it is not emulated!");
						//continue;
					}
					else {
						final IInterface iface=Host.getDefaultInterface(dst);
						pairs.add(new pair(src,dst, "$$"+iface.getUniqueName().toString()));
					}
					dst_idx++;
					if(dst_idx>=real_dsts.size() && mapping == Mapping.ONE2MANY)
						break;

				}
			}
		}
			break;
		case ONE2ONE:
			if(real_dsts != null && real_dsts.size()>0) {
				if(real_dsts.size() != srcs.size())
					throw new RuntimeException("for a one2one mapping the src and dsts sets must be of equal size!");
				for(int i=0; i<srcs.size();i++) {
					pairs.add(new pair(srcs.get(i),null,real_dsts.get(i)));
				}
			}
			else {
				if(dsts.size() != srcs.size())
					throw new RuntimeException("for a one2one mapping the src and dsts sets must be of equal size!");
				for(int i=0; i<srcs.size();i++) {
					if(!srcs.get(i).hasEmulationProtocol()) {
						throw new RuntimeException(srcs.get(i).getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
						//jprime.Console.err.println("Not using "+srcs.get(i).getUniqueName()+" as a traffic source since it is not emulated!");
						//continue;
					}
					final IHost dst = dsts.get(i);
					if(!dst.hasEmulationProtocol()) {
						throw new RuntimeException(dst.getUniqueName()+" is not emulated. Expecting emulated hosts or routers.");
						//jprime.Console.err.println("Not using "+dst.getUniqueName()+" as a traffic sink since it is not emulated!");
					}
					else {
						final IInterface iface=Host.getDefaultInterface(dst);
						pairs.add(new pair(srcs.get(i),dst, "$$"+iface.getUniqueName().toString()));
					}
				}
			}
			break;
		default:
			throw new RuntimeException("unexpected mapping type");
		}
		return pairs;
	}
	
	protected IPingTraffic createSimulatedHybridPing(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			List<IHost> srcs,
			List<IHost> dsts,
			List<String> emu_dsts) {
		checkArgs(srcs,dsts,emu_dsts,null);
		IPingTraffic ping = tm.createPingTraffic(nextName());
		ping.setCount(numberOfPings);
		ping.setStartTime(""+startTime);
		ping.setInterval((float)(1.0/(arrivalRate<0.000001?0.000001:arrivalRate)));
		ping.setIntervalExponential(arrivalProcess.str);
		ping.setMapping(mapping.str);
		ping.setSrcs(compileSimulatedHosts(srcs,false));
		if(dsts != null) {
			//sim to sim
			ping.setDsts(compileSimulatedHosts(dsts,true));
		}
		else {
			//sim to real/emu
			ping.setDstIps(compileIps(emu_dsts));
		}
		return ping;
	}
	
	protected IUDPTraffic createSimulatedHybridUDP(
			Mapping mapping,
			double startTime, //in sec
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			List<IHost> srcs,
			List<IHost> dsts,
			List<String> emu_dsts) {
		checkArgs(srcs,dsts,emu_dsts,null);
		IUDPTraffic udp = tm.createUDPTraffic(nextName());
		udp.setBytesToSendEachInterval((long)(((double)sendRate)/20.0));
		udp.setInterval((float)(1.0/20.0));
		udp.setStartTime(""+startTime);
		udp.setInterval((float)(1.0/(arrivalRate<0.000001?0.000001:arrivalRate)));
		udp.setIntervalExponential(arrivalProcess.str);
		udp.setCount((long)(sendTime*20.0));
		udp.setMapping(mapping.str);
		udp.setSrcs(compileSimulatedHosts(srcs,false));
		if(dsts != null) {
			//sim to sim
			udp.setDsts(compileSimulatedHosts(dsts,true));
		}
		else {
			//sim to real/emu
			udp.setDstIps(compileIps(emu_dsts));
		}
		return udp;
	}
	
	protected ITCPTraffic createSimulatedHybridTCP(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows,
			long bytesToTransfer,
			List<IHost> srcs,
			List<IHost> dsts,
			List<String> emu_dsts) {
		checkArgs(srcs,dsts,emu_dsts,null);
		ITCPTraffic tcp = tm.createTCPTraffic(nextName());
		tcp.setFileSize(bytesToTransfer);
		tcp.setStartTime(""+startTime);
		tcp.setNumberOfSessions(numberOfFlows);
		tcp.setInterval((float)(1.0/(arrivalRate<0.000001?0.000001:arrivalRate)));
		tcp.setIntervalExponential(arrivalProcess.str);
		tcp.setMapping(mapping.str);
		tcp.setSrcs(compileSimulatedHosts(srcs,false));
		if(dsts != null) {
			//sim to sim
			tcp.setDsts(compileSimulatedHosts(dsts,true));
		}
		else {
			//sim to real/emu
			tcp.setToEmulated(true);
			tcp.setDstIps(compileIps(emu_dsts));
		}
		return tcp;
	}

	protected List<EmulationCommand> createEmulatedPing(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			List<IHost> srcs,
			List<IHost> dsts,
			List<String> real_dsts) {
		checkArgs(srcs,dsts,null,real_dsts);
		List<pair> pairs = getEmuMapping(mapping,srcs,dsts,real_dsts);
		
		List<EmulationCommand> cmds = new LinkedList<EmulationCommand>();
		
		final double wait=1000.0/(arrivalRate<0.000001?0.000001:arrivalRate);
		final long run_time = (long)(wait*numberOfPings+5000);

		long base_start = (long)(startTime*1000);
		for(pair p : pairs) {
			cmds.add(new EmulationCommand(
					p.src,
					"ping -i "+wait+" -c "+numberOfPings+" "+p.dst_name,
					"ping.out",
					base_start,
					run_time,
					false,
					TimeUnit.MILLISECONDS,
					true));
			switch(arrivalProcess) {
			case CONSTANT:
				base_start+=wait;
				break;
			case EXPONENTIAL:
				base_start+= (long)(1000*exponential(arrivalRate));
				break;
			default:
				throw new RuntimeException("unexpected mapping type");
			}
		}
		return cmds;
	}

	protected List<EmulationCommand> createEmulatedUDP(
			Mapping mapping,
			double startTime, //in sec
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			List<IHost> srcs,
			List<IHost> dsts,
			List<String> real_dsts) {
		checkArgs(srcs,dsts,null,real_dsts);
		List<pair> pairs = getEmuMapping(mapping,srcs,dsts,real_dsts);
		List<EmulationCommand> cmds = new LinkedList<EmulationCommand>();

		final double wait=1000.0/(arrivalRate<0.000001?0.000001:arrivalRate);
		final long run_time = (long)(wait*sendTime+5000);


		long base_start = (long)(startTime*1000);
		for(pair p : pairs) {
			if(p.dst != null) {
				//we want the server to start before the client
				long s = ((long)(1000*base_start))-5000;
				if(s<0)s=10; 
				//the server command
				cmds.add(new EmulationCommand(
						p.dst,
						"iperf -s -u", "iperf.udp.s.out",
						s,
						run_time,
						false,
						TimeUnit.MILLISECONDS,
						true));
			}
			//else it must be a real host -- we will assume iperf is started!
			
			{// the client command
				cmds.add(new EmulationCommand(
						p.src,
						"iperf -u -i 1 -c "+p.dst_name+" -b "+sendRate+" -t "+((long)sendTime),
						"iperf.udp.c.out",
						base_start,
						run_time,
						false,
						TimeUnit.MILLISECONDS,
						true));
			}

			switch(arrivalProcess) {
			case CONSTANT:
				base_start+=wait;
				break;
			case EXPONENTIAL:
				base_start+= (long)(1000*exponential(arrivalRate));
				break;
			default:
				throw new RuntimeException("unexpected mapping type");
			}
		}
		return cmds;
	}
	

	protected List<EmulationCommand> createEmulatedTCP(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows,
			long bytesToTransfer,
			List<IHost> srcs,
			List<IHost> dsts,
			List<String> real_dsts) {
		checkArgs(srcs,dsts,null,real_dsts);
		List<pair> pairs = getEmuMapping(mapping,srcs,dsts,real_dsts);
		List<EmulationCommand> cmds = new LinkedList<EmulationCommand>();

		final double wait=1000.0/(arrivalRate<0.000001?0.000001:arrivalRate);


		long base_start = (long)(startTime*1000);
		
		for (int i=0; i<numberOfFlows; i++){
			for(pair p : pairs) {
				if(p.dst != null) {
					//we want the server to start before the client
					long s = ((long)(1000*base_start))-5000;
					if(s<0)s=10; 
					//the server command
					cmds.add(new EmulationCommand(
							p.dst,
							"iperf -s ", "iperf.tcp.s.out",
							s,
							EmulationCommand.USE_RUNTIME,
							false,
							TimeUnit.MILLISECONDS,
							true));
				}
				//else it must be a real host -- we will assume iperf is started!
				
				{// the client command
					cmds.add(new EmulationCommand(
							p.src,
							"iperf -i 1 -c "+p.dst_name+" -n "+bytesToTransfer+" -t $${RUNTIME-20}",
							"iperf.tcp.c.out",
							base_start,
							EmulationCommand.USE_RUNTIME,
							false,
							TimeUnit.MILLISECONDS,
							true));
				}
	
				switch(arrivalProcess) {
				case CONSTANT:
					base_start+=wait;
					break;
				case EXPONENTIAL:
					base_start+= (long)(1000*exponential(arrivalRate));
					break;
				default:
					throw new RuntimeException("unexpected mapping type");
				}
			}
		}
		return cmds;
	}
	
	/*
	 * simulated traffic 
	 */
	public void createSimulatedUDP(
			Mapping mapping,
			double startTime, //in sec
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			List<IHost> srcs,
			List<IHost> dsts) {
		createSimulatedHybridUDP(mapping, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}
	public void createSimulatedTCP(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows,
			long bytesToTransfer,
			List<IHost> srcs,
			List<IHost> dsts) {
		createSimulatedHybridTCP(mapping, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}

	public void createSimulatedPing(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			List<IHost> srcs,
			List<IHost> dsts) {
		createSimulatedHybridPing(mapping, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}

	

	public void createSimulatedUDP(
			double startTime,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridUDP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, sendRate, sendTime, srcs, dsts, null);
	}

	public void createSimulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		findHosts(dst,dsts,false);
		createSimulatedHybridUDP(Mapping.ONE2MANY, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}
	
	public void createSimulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		findHosts(dst,dsts,false);
		createSimulatedHybridUDP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}

	public void createSimulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		dsts.add(dst);
		createSimulatedHybridUDP(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}
	
	public void createSimulatedTCP(
			double startTime,
			long bytesToTransfer,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, default_number_of_flows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createSimulatedTCP(
			double startTime,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}

	public void createSimulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		findHosts(dst,dsts,false);
		createSimulatedHybridTCP(Mapping.ONE2MANY, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs,dsts,null);
	}
	
	public void createSimulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		findHosts(dst,dsts,false);
		createSimulatedHybridTCP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}

	public void createSimulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		dsts.add(dst);
		createSimulatedHybridTCP(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createSimulatedPing(
			double startTime,
			long numberOfPings,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridPing(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT,default_arrival_rate, numberOfPings, srcs, dsts, null);
	}

	public void createSimulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			IHost src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		findHosts(dst,dsts,false);
		createSimulatedHybridPing(Mapping.ONE2MANY, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}
	
	public void createSimulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		findHosts(dst,dsts,false);
		createSimulatedHybridPing(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}

	
	public void createSimulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		dsts.add(dst);
		createSimulatedHybridPing(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}
	
	//*****************************************
	//*****************************************
	/*
	 * Hybrid traffic -- i.e. sim to real or sim to emu
	 */
	
	public void createHybridUDP(
			Mapping mapping,
			double startTime, //in sec
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			List<IHost> srcs,
			List<String> emu_dsts) {
		createSimulatedHybridUDP(mapping, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, null, emu_dsts);
	}
	public void createHybridTCP(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			List<IHost> srcs,
			List<String> emu_dsts) {
		createSimulatedHybridTCP(mapping, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, null, emu_dsts);
	}

	public void createHybridPing(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			List<IHost> srcs,
			List<String> emu_dsts) {
		createSimulatedHybridPing(mapping, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, null, emu_dsts);
	}

	
	public void createHybridUDP(
			double startTime,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dstIp);
		createSimulatedHybridUDP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, sendRate, sendTime, srcs, null, dsts);
	}
	
	public void createHybridUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		findHosts(src,srcs,false);
		dsts.add(dstIp);
		createSimulatedHybridUDP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, null, dsts);
	}
	
	public void createHybridUDP(
			double startTime,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createSimulatedHybridUDP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, sendRate, sendTime, srcs, null, dstIps);
	}
	
	public void createHybridUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		createSimulatedHybridUDP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, null, dstIps);
	}
	
	public void createHybridTCP(
			double startTime,
			long bytesToTransfer,
			IHost src,
			String dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, default_number_of_flows, bytesToTransfer, srcs, null, dsts);
	}

	public void createHybridTCP(
			double startTime,
			long bytesToTransfer,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, default_number_of_flows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createHybridTCP(
			double startTime,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			String dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfFlows, bytesToTransfer, srcs, null, dsts);
	}
	
	public void createHybridTCP(
			double startTime,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createHybridTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		findHosts(src,srcs,false);
		dsts.add(dstIp);
		createSimulatedHybridTCP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, null, dsts);
	}
	
	public void createHybridTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			IHost dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		dsts.add(dstIp);
		createSimulatedHybridTCP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createHybridTCP(
			double startTime,
			long bytesToTransfer,
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createSimulatedHybridTCP(Mapping.MANY2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, default_number_of_flows, bytesToTransfer, srcs, null, dstIps);
	}

	public void createHybridTCP(
			double startTime,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createSimulatedHybridTCP(Mapping.MANY2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfFlows, bytesToTransfer, srcs, null, dstIps);
	}
	
	public void createHybridTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		createSimulatedHybridTCP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, null, dstIps);
	}

	public void createHybridPing(
			double startTime,
			long numberOfPings,
			IHost src,
			String dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dst);
		createSimulatedHybridPing(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, numberOfPings, default_arrival_rate, srcs, null, dsts);
	}
	
	public void createHybridPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		findHosts(src,srcs,false);
		dsts.add(dstIp);
		createSimulatedHybridPing(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, null, dsts);
	}
	
	public void createHybridPing(
			double startTime,
			long numberOfPings,
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createSimulatedHybridPing(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, numberOfPings, default_arrival_rate, srcs, null, dstIps);
	}
	
	public void createHybridPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		findHosts(src,srcs,false);
		createSimulatedHybridPing(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, null, dstIps);
	}
	
	

	//*****************************************
	//*****************************************
	/*
	 * emulated to emulated or emulated to real
	 * 
	 */
	
	public void createEmulatedUDP(
			Mapping mapping,
			double startTime, //in sec
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			List<IHost> srcs,
			List<IHost> dsts) {
		createEmulatedUDP(mapping, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}
	
	public void createEmulatedTCP(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			List<IHost> srcs,
			List<IHost> dsts) {
		createEmulatedTCP(mapping, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}
	
	
	public void createEmulatedPing(
			Mapping mapping,
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			List<IHost> srcs,
			List<IHost> dsts) {
		createEmulatedPing(mapping, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}
	
	public void createEmulatedUDP(
			double startTime,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createEmulatedUDP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, sendRate, sendTime, srcs, dsts, null);
	}

	public void createEmulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		findHosts(dst,dsts,true);
		createEmulatedUDP(Mapping.ONE2MANY, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}
	
	public void createEmulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		findHosts(dst,dsts,true);
		createEmulatedUDP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}

	public void createEmulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		dsts.add(dst);
		createEmulatedUDP(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, null);
	}
	
	public void createEmulatedTCP(
			double startTime,
			long bytesToTransfer,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createEmulatedTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, default_number_of_flows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createEmulatedTCP(
			double startTime,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createEmulatedTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}

	public void createEmulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		findHosts(dst,dsts,true);
		createEmulatedTCP(Mapping.ONE2MANY, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createEmulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		findHosts(dst,dsts,true);
		createEmulatedTCP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}

	public void createEmulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		dsts.add(dst);
		createEmulatedTCP(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, null);
	}
	
	public void createEmulatedPing(
			double startTime,
			long numberOfPings,
			IHost src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		dsts.add(dst);
		createEmulatedPing(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfPings, srcs, dsts, null);
	}

	public void createEmulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			IHost src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		srcs.add(src);
		findHosts(dst,dsts,true);
		createEmulatedPing(Mapping.ONE2MANY, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}
	
	public void createEmulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			INet dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		findHosts(dst,dsts,true);
		createEmulatedPing(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}

	
	public void createEmulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			IHost dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<IHost> dsts = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		dsts.add(dst);
		createEmulatedPing(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, null);
	}
	
	public void createEmulatedUDP(
			double startTime,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dstIp);
		createEmulatedUDP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, sendRate, sendTime, srcs, null, dsts);
	}
	
	public void createEmulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		findHosts(src,srcs,true);
		dsts.add(dstIp);
		createEmulatedUDP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, null, dsts);
	}
	
	public void createEmulatedUDP(
			double startTime,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createEmulatedUDP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, sendRate, sendTime, srcs, null, dstIps);
	}
	
	public void createEmulatedUDP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long sendRate,//in bytes per sec
			double sendTime, //in sec
			INet src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		createEmulatedUDP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, null, dstIps);
	}
	
	public void createEmulatedTCP(
			double startTime,
			long bytesToTransfer,
			IHost src,
			String dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dst);
		createEmulatedTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, default_number_of_flows, bytesToTransfer, srcs, null, dsts);
	}

	public void createEmulatedTCP(
			double startTime,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			String dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dst);
		createEmulatedTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfFlows, bytesToTransfer, srcs, null, dsts);
	}
	
	public void createEmulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		findHosts(src,srcs,true);
		dsts.add(dstIp);
		createEmulatedTCP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, null, dsts);
	}
	
	public void createEmulatedTCP(
			double startTime, 
			long bytesToTransfer,
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createEmulatedTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, default_number_of_flows, bytesToTransfer, srcs, null, dstIps);
	}

	public void createEmulatedTCP(
			double startTime,
			int numberOfFlows, 
			long bytesToTransfer,
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createEmulatedTCP(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfFlows, bytesToTransfer, srcs, null, dstIps);
	}
	
	public void createEmulatedTCP(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			int numberOfFlows, 
			long bytesToTransfer,
			INet src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		createEmulatedTCP(Mapping.ALL2ALL, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, null, dstIps);
	}

	public void createEmulatedPing(
			double startTime,
			long numberOfPings,
			IHost src,
			String dst) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		srcs.add(src);
		dsts.add(dst);
		createEmulatedPing(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfPings, srcs, null, dsts);
	}
	
	public void createEmulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			String dstIp) {
		List<IHost> srcs = new LinkedList<IHost>();
		List<String> dsts = new LinkedList<String>();
		findHosts(src,srcs,true);
		dsts.add(dstIp);
		createEmulatedPing(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, null, dsts);
	}
	
	public void createEmulatedPing(
			double startTime,
			long numberOfPings,
			IHost src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		srcs.add(src);
		createEmulatedPing(Mapping.ONE2ONE, startTime, ArrivalProcess.CONSTANT, default_arrival_rate, numberOfPings, srcs, null, dstIps);
	}
	
	public void createEmulatedPing(
			double startTime,
			ArrivalProcess arrivalProcess,
			double arrivalRate,
			long numberOfPings,
			INet src,
			List<String> dstIps) {
		List<IHost> srcs = new LinkedList<IHost>();
		findHosts(src,srcs,true);
		createEmulatedPing(Mapping.MANY2ONE, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, null, dstIps);
	}
}
