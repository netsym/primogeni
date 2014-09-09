package jprime.util;

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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nathanael Van Vorst
 *
 */
public class ComputeNode {
	private Integer partition_id=-1;
	private String control_ip, data_ip;
	private final List<Portal> portals;
	
	public ComputeNode(String control_ip, String data_ip, List<Portal> portals) {
		this.control_ip = control_ip;
		this.data_ip = data_ip;
		this.portals=portals;
		if(control_ip == null || control_ip.length() == 0) {
			throw new RuntimeException("Invalid control ip!");
		}
		if(data_ip == null || data_ip.length() == 0) {
			throw new RuntimeException("Invalid data ip!");
		}
		for(Portal p : portals) {
			p.setNode(this);
		}
	}

	/**
	 * @return the partition_id
	 */
	public Integer getPartitionId() {
		return partition_id;
	}

	/**
	 * @param partition_id the partition_id to set
	 */
	public void setPartitionId(Integer partition_id) {
		this.partition_id = partition_id;
	}

	public String toString() {
		return "["+toString(this)+"]";
	}
	
	public static String toString(ComputeNode n) {
		String ps="";
		for(Portal p : n.portals) {
			ps+=", "+p.getName()+","+p.getIP();
		}
		return n.control_ip+","+n.data_ip+ps;
	}
	
	public static String toString(List<ComputeNode> l) {
		String rv = null;
		for(ComputeNode c : l) {
			if(rv == null) rv="";
			else rv+=",";
			rv+="["+toString(c)+"]";
		}
		return rv;
	}
	
	private static ComputeNode parseNodeString(String[] l) {
		switch(l.length) {
		case 0:
			throw new RuntimeException("Must have 1 or more elements!");
		case 1:
			return new ComputeNode(l[0].trim(), l[0].trim(), new ArrayList<Portal>());
		case 2:
			return new ComputeNode(l[0].trim(), l[1].trim(), new ArrayList<Portal>());
		default:
			ArrayList<Portal> ps = new ArrayList<Portal>();
			for(int i=2;i<l.length-1;) {
				ps.add(new Portal(l[i].trim(), l[i+1].trim()));
				i+=2;
			}
			return new ComputeNode(l[0].trim(), l[1].trim(), ps);
		}
	}
	
	public static LinkedList<ComputeNode> fromString(String str) {
		LinkedList<ComputeNode> rv = new LinkedList<ComputeNode>();
		if(str.contains("[") || str.contains("(")) {
			//its a list of compute nodes
			str=str.replace("(","[");
			str=str.replace("]","");
			str=str.replace(")","");
			String[] l = str.split("\\[");
			for(String s : l)  {
				s=s.trim();
				if(s.length()>0)
					rv.add(parseNodeString(s.split(",")));
			}
		}
		else {
			//its a single node
			rv.add(parseNodeString(str.trim().split(",")));
		}
		return rv;
	}
	
	/**
	 * @return the control_ip
	 */
	public String getControl_ip() {
		return control_ip;
	}
	
	/**
	 * @param control_ip the control_ip to set
	 */
	public void setControl_ip(String control_ip) {
		this.control_ip = control_ip;
	}
	
	/**
	 * @return the data_ip
	 */
	public String getData_ip() {
		return data_ip;
	}
	
	/**
	 * @param data_ip the data_ip to set
	 */
	public void setData_ip(String data_ip) {
		this.data_ip = data_ip;
	}
	
	/**
	 * @return the portals
	 */
	public List<Portal> getPortals() {
		return portals;
	}
	
	public void addPortal(Portal p) {
		portals.add(p);
		p.setNode(this);
	}
}
