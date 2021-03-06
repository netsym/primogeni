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

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import jprime.util.ComputeNode;
import jprime.util.Portal;

import monitor.util.Utils;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

public class NodeConfig implements Comparable<NodeConfig> {
	public static int COMPUTE_NODE=2;
	public static int SERVICE_NODE=3;
	private static final CharsetDecoder csd = Charset.forName("UTF-8").newDecoder();
	private static final CharsetEncoder cse = Charset.forName("UTF-8").newEncoder();
	public final int nodeType;
	public final int machineId;
	public final int partition_id;
	public final String control_ip, data_ip;
	public final List<Portal> portals;
	public IoSession session;
	
	public NodeConfig(IoBuffer in) throws CharacterCodingException {
		this.nodeType=in.getInt();
		this.machineId=in.getInt();
		this.partition_id=in.getInt();
		this.control_ip=in.getString(in.getInt(),csd);
		this.data_ip=in.getString(in.getInt(),csd);
		int num_portals = in.getInt();
		this.portals = new ArrayList<Portal>();
		for(int i =0;i<num_portals;i++) {
			portals.add(new Portal(in.getString(in.getInt(),csd), in.getString(in.getInt(),csd), in.getLong()));
		}
		if(Utils.DEBUG)System.out.println("decoded "+this+", remaining="+in.remaining());
		System.out.println("decoded "+this+", remaining="+in.remaining()+" x-x");
		if(nodeType!=COMPUTE_NODE && nodeType!=SERVICE_NODE)
			throw new RuntimeException("Invalid node type!");
	}

	public NodeConfig(int nodeType, int machineId, ComputeNode n) {
		super();
		if(nodeType!=COMPUTE_NODE && nodeType!=SERVICE_NODE)
			throw new RuntimeException("Invalid node type!");
		if(n.getPartitionId()==null) {
			if(n.getPortals().size()>0) {
				throw new RuntimeException("The master node cannot have portals.");
			}
		}
		else if(n.getPartitionId() == -1) {
			throw new RuntimeException("The compute node's partition was not set.");
		}
		this.nodeType = nodeType;
		this.machineId = machineId;
		this.partition_id=n.getPartitionId()==null?-1:n.getPartitionId();
		this.control_ip = n.getControl_ip();
		this.data_ip=n.getData_ip();
		this.portals=n.getPortals();
	}
	public int size() {
		int portal_size = 0;
		for(Portal p : portals) {
			portal_size+=(Integer.SIZE*2+Long.SIZE)/8+p.getName().length()+p.getIP().length();
		}
		return (Integer.SIZE*6)/8+control_ip.length()+data_ip.length()+portal_size;
	}
	public String toString() {
		return "["+(nodeType==COMPUTE_NODE?"ComputeNode":(nodeType==SERVICE_NODE?"ServiceNode":"ERROR"))
		+", machineid="+machineId
		+",control_ip="+control_ip
		+", data_ip"+data_ip
		+", partition_id="+partition_id+"]";
	}
	
	public void encode(IoBuffer out) throws CharacterCodingException {
		int ack=out.position();
		out.putInt(nodeType);
		out.putInt(machineId);
		out.putInt(partition_id);
		out.putInt(control_ip.length());
		out.putString(control_ip, cse);
		out.putInt(data_ip.length());
		out.putString(data_ip, cse);
		out.putInt(portals.size());
		for(Portal p : portals) {
			out.putInt(p.getName().length());
			out.putString(p.getName(), cse);
			out.putInt(p.getIP().length());
			out.putString(p.getIP(), cse);
			out.putLong(p.getLinkedInterfaceUID());
		}
		System.out.println("encoded "+this+", size="+(out.position()-ack+" x-x"));
		if(Utils.DEBUG)System.out.println("encoded "+this+", size="+(out.position()-ack));
	}

	public int compareTo(NodeConfig o) {
		if(machineId == o.machineId)
			return 0;
		else if(machineId < o.machineId)
			return -1;
		return 1;
	}
	
}