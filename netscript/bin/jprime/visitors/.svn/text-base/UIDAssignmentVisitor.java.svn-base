package jprime.visitors;

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

import jprime.Experiment;
import jprime.ModelNode;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Link.ILinkAlias;
import jprime.Net.INet;
import jprime.Net.INetAlias;
import jprime.Net.Net;
import jprime.NicQueue.INicQueue;
import jprime.ProtocolSession.IProtocolSession;
import jprime.Traffic.ITraffic;
import jprime.util.IPAddressUtil;

/**
 * @author Nathanael Van Vorst
 * Assign the UIDs
 */
public class UIDAssignmentVisitor {
	private long prev;
	private long proto_count;
	private long queue_count;
	private final long protos_per_host;
	private final long traffics_per_net;
	private final Experiment exp;
	
	/**
	 * @param topnet
	 */
	public UIDAssignmentVisitor(Experiment exp, Net topnet, Map<String,String> params) {
		this.exp=exp;
		if(params.containsKey("protos_per_host")) {
			protos_per_host=Long.parseLong(params.remove("protos_per_host"));
		}
		else {
			protos_per_host = 20; 
		}
		if(params.containsKey("traffics_per_net")) {
			traffics_per_net=Long.parseLong(params.remove("traffics_per_net"));
		}
		else {
			traffics_per_net = 20; 
		}
		
		//no params yet....
		prev=1;
		topnet.setOffset(0);
		topnet.accept(this);
		topnet.setSize(prev-1);
	}
	
	public void visit(ModelNode node) {
		long ip_size=0;
		long base=prev;
		long offset = 0;
		boolean is_host = node instanceof IHost && !node.isAlias();
		boolean is_iface = !is_host && node instanceof IInterface && !node.isAlias();
		boolean is_proto = !is_host && !is_iface && node instanceof IProtocolSession && !node.isAlias();
		boolean is_queue = !is_host && !is_iface && !is_proto && node instanceof INicQueue && !node.isAlias();
		boolean is_traffic = !is_host && !is_iface && !is_proto && !is_queue && node instanceof ITraffic && !node.isAlias();
		if(is_host) {
			proto_count=0;
		}
		else if(is_iface) {
			queue_count=0;
		}
		else if(is_proto) {
			proto_count++;
		}
		else if(is_queue) {
			queue_count++;
		}
		else if (exp != null && node instanceof IEmulationProtocol) {
			exp.addEmuProtocol((IEmulationProtocol)node);
		}
			
		for(ModelNode c : node.getAllChildren()) {
			c.accept(this);
			c.setOffset(offset);
			offset+=c.getSize();
			if(c instanceof ILink && !(c instanceof ILinkAlias)) {
				//jprime.Console.out.println("getIpPrefixLen dbid:"+c.getDBID()+",uid= "+c.getUID()+", len="+((ILink)c).getIpPrefixLen());
				ip_size+=(long)Math.pow(2,((ILink)c).getIpPrefixLen().getValue());
			}
			else if(c instanceof INet && !(c instanceof INetAlias)) {
				//jprime.Console.out.println("getIpPrefixLen dbid:"+c.getDBID()+",uid= "+c.getUID()+", len="+((ILink)c).getIpPrefixLen());
				ip_size+=(long)Math.pow(2,((INet)c).getIpPrefixLen().getValue());
			}
		}
		if(is_host) {
			if(proto_count<protos_per_host) {
				prev+=protos_per_host-proto_count; //so we have some extra room to dynamically add protocols
			}
		}
		else if(is_iface) {
			if(queue_count==0) {
				prev++; //we are going to need a queue so lets leave a uid for it
			}
		}
		else if(is_traffic) {
			prev+=traffics_per_net;
		}
		node.setUID(prev++);
		node.setSize(prev-base);
		
		node.enforceMinimumChildConstraints();
		if(node instanceof ILink && !(node instanceof ILinkAlias)) {
			((ILink)node).setIpPrefixLen((long)Math.ceil(Math.log(((ILink)node).getAttachments().size()+2)/IPAddressUtil.log2));
			//jprime.Console.out.println("setIpPrefixLen dbid:"+node.getDBID()+",uid= "+node.getUID()+", len="+((long)Math.ceil(Math.log(((ILink)node).getAttachments().size()+2)/IPAddressUtil.log2)));
		}
		else if(node instanceof INet && !(node instanceof INetAlias)) {
			((INet)node).setIpPrefixLen((long)Math.ceil(Math.log(ip_size+2)/IPAddressUtil.log2));
			//jprime.Console.out.println("setIpPrefixLen dbid:"+node.getDBID()+",uid= "+node.getUID()+", len="+((long)Math.ceil(Math.log(ip_size+2)/IPAddressUtil.log2)));
		}
	}	
}
