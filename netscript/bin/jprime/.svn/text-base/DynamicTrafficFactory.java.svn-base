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

import java.util.List;

import jprime.Host.IHost;
import jprime.Net.INet;
import jprime.PingTraffic.IPingTraffic;
import jprime.TCPTraffic.ITCPTraffic;
import jprime.Traffic.ITraffic;
import jprime.TrafficType.ITrafficType;
import jprime.UDPTraffic.IUDPTraffic;
import jprime.partitioning.Alignment;
import jprime.partitioning.Partitioning;
import jprime.util.IExperimentController;
import jprime.variable.ResourceIdentifierVariable;

/**
 * @author Nathanael Van Vorst

Basic Usage:
		INet topnet = null; //this would be the real topnet
        //create the model
        ....
        //compile the model
        ....
        IExperimentController controller; // the controller for the exp
        //deploy the model....
        ..
		TrafficFactory tFactory = new TrafficFactory(topnet, "my_dynamic_traffic", controller);
		
		//have srcHost send 10 pings to dstHost at time 10.0
		tFactory.createSimulatedPing(10.0, 10, srcHost,dstHost);
		
		//have all hosts in srcNet contact dstHost and download 10 megs
		//this is dynamic traffic and the name would be 'my_traffic_XX' where XX is an integer
		//every 10 second 1 host from srcNet will be chosen to start after the traffic is started 
		tFactory.createSimulatedTCP(2.0, ArrivalProcess.CONSTANT, 10, 1, 1024*1024*10, srcNet, dstHost);
		
		//have all hosts in srcNet contact the REAL host 131.14.1.3 and download 10 megs
		//this is dynamic traffic and the name would be 'my_traffic_XX' where XX is an integer
		//hosts will be hosts to start using an exponential distribution with an off_time of 0.1
		tFactory.createHybridTCP(3.0, ArrivalProcess.EXPONENTIAL, 0.1, 1, 1024*1024*10, srcNet, "131.14.1.3");
		
 */
public class DynamicTrafficFactory extends TrafficFactory {
	private final IExperimentController controller;
	private final INet topnet;
	private final Partitioning partitioning;
	
	private static INet getTopnet(INet n) {
		while(n.getParent() != null)
			n=(INet)n.getParent();		
		return n;
	}
	private static ITraffic getDynTraffic(INet n) {
		if(n.getMetadata().getState().lt(State.COMPILED)) {
			throw new RuntimeException("You can only create a "+DynamicTrafficFactory.class.getSimpleName()+" after the experiment has been compiled.");
		}
		else {
			final ITraffic rv = (ITraffic)n.getChildByName(ITraffic.DYN_NAME);
			if(rv == null) {
				throw new RuntimeException("How did this happen? the VerifyVistor should have created this....");
			}
			return rv;
		}
	}
	public DynamicTrafficFactory(INet owner, String base_name, IExperimentController controller, Partitioning partitioning) {
		super(owner, getDynTraffic(owner),base_name);
		this.controller=controller;
		this.topnet = getTopnet(owner);
		this.partitioning=partitioning;
	}

	public DynamicTrafficFactory(INet owner, IExperimentController controller, Partitioning partitioning) {
		super(owner, getDynTraffic(owner),null);
		this.controller=controller;
		this.topnet = getTopnet(owner);
		this.partitioning=partitioning;
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
		final boolean p = ModelNode.__check_state__;
		ModelNode.__check_state__ = false;
		IPingTraffic ping = super.createSimulatedHybridPing(mapping, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, emu_dsts);
		ModelNode.__check_state__ = p;
		assignUID((ModelNode)ping);
		{ //evaluate src/dst list
			final ResourceIdentifierVariable s = ping.getSrcs();
			final ResourceIdentifierVariable d = ping.getDsts();
			if(null != s)
				s.evaluate((ModelNode)ping.getParent().getParent(), ModelNode.relativePath(ping,ping.getParent().getParent()));
			if(null != d)
				d.evaluate((ModelNode)ping.getParent().getParent(), ModelNode.relativePath(ping,ping.getParent().getParent()));
		}
		controller.startDynamicTraffic(ping);
		ping.delete();
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
		final boolean p = ModelNode.__check_state__;
		ModelNode.__check_state__ = false;
		IUDPTraffic udp = super.createSimulatedHybridUDP(mapping, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, emu_dsts);
		ModelNode.__check_state__ = p;
		assignUID((ModelNode)udp);
		{ //evaluate src/dst list
			final ResourceIdentifierVariable s = udp.getSrcs();
			final ResourceIdentifierVariable d = udp.getDsts();
			if(null != s)
				s.evaluate((ModelNode)udp.getParent().getParent(), ModelNode.relativePath(udp,udp.getParent().getParent()));
			if(null != d)
				d.evaluate((ModelNode)udp.getParent().getParent(), ModelNode.relativePath(udp,udp.getParent().getParent()));
		}
		controller.startDynamicTraffic(udp);
		udp.delete();
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
		final boolean p = ModelNode.__check_state__;
		ModelNode.__check_state__ = false;
		ITCPTraffic tcp = super.createSimulatedHybridTCP(mapping, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, emu_dsts);
		ModelNode.__check_state__ = p;
		assignUID((ModelNode)tcp);
		{ //evaluate src/dst list
			final ResourceIdentifierVariable s = tcp.getSrcs();
			final ResourceIdentifierVariable d = tcp.getDsts();
			if(null != s) {
				s.evaluate((ModelNode)tcp.getParent().getParent(), ModelNode.relativePath(tcp,tcp.getParent().getParent()));
			}
			if(null != d) {
				d.evaluate((ModelNode)tcp.getParent().getParent(), ModelNode.relativePath(tcp,tcp.getParent().getParent()));
			}
		}
		controller.startDynamicTraffic(tcp);
		tcp.delete();
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
		final boolean p = ModelNode.__check_state__;
		ModelNode.__check_state__ = false;
		List<EmulationCommand> cmds = super.createEmulatedPing(mapping, startTime, arrivalProcess, arrivalRate, numberOfPings, srcs, dsts, real_dsts);
		ModelNode.__check_state__ = p;
		for(EmulationCommand cmd : cmds) {
			final IHost h = cmd.getParent();
			cmd.evaluateCmd(topnet, controller.getExperimentRuntime());
			controller.sendHostCommand(cmd.getCmd(), h, cmd.getDelay(), false);
			cmd.delete();
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
		final boolean p = ModelNode.__check_state__;
		ModelNode.__check_state__ = false;
		List<EmulationCommand> cmds = super.createEmulatedUDP(mapping, startTime, arrivalProcess, arrivalRate, sendRate, sendTime, srcs, dsts, real_dsts);
		ModelNode.__check_state__ = p;
		for(EmulationCommand cmd : cmds) {
			final IHost h = cmd.getParent();
			cmd.evaluateCmd(topnet, controller.getExperimentRuntime());
			controller.sendHostCommand(cmd.getCmd(), h, cmd.getDelay(), false);
			cmd.delete();
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
		final boolean p = ModelNode.__check_state__;
		ModelNode.__check_state__ = false;
		List<EmulationCommand> cmds = super.createEmulatedTCP(mapping, startTime, arrivalProcess, arrivalRate, numberOfFlows, bytesToTransfer, srcs, dsts, real_dsts);
		ModelNode.__check_state__ = p;
		for(EmulationCommand cmd : cmds) {
			final IHost h = cmd.getParent();
			cmd.evaluateCmd(topnet, controller.getExperimentRuntime());
			controller.sendHostCommand(cmd.getCmd(), h, cmd.getDelay(), false);
			cmd.delete();
		}
		return cmds;
	}
	
	private void assignUID(ModelNode node) {
		node.setUID(tm.getNextDynamicUID());
		node.setSize(1);
		node.setOffset(node.getUID()-1);
		if(node instanceof ITrafficType) {
			((ITrafficType)node).setCommunityIds(getAlignIds(((ITrafficType)node)));
		}
	}
	
	private String getAlignIds(ITrafficType n) {
		if(partitioning!=null) {
			StringBuffer temp=new StringBuffer();
			for(int ai : n.getParent().getAlignments(partitioning)) {
				Alignment a= partitioning.findAlignment(ai);
				if(temp.length()==0) temp.append("[");
				else temp.append(",");
				temp.append(a.getAlignId());
			}
			if(temp.length()>0) { 
				temp.append("]");
			}
			else temp.append("[]");
			return temp.toString();
		}
		return "[]";
	}
}
