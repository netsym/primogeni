package jprime.util;

import jprime.Interface.IInterface;

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


/**
 * @author Nathanael Van Vorst
 *
 */
public class Portal {
	private String name;
	private String ip;
	private ComputeNode node;
	private IInterface linkedInterface;
	private long linkedInterfaceUID;
	public Portal(String name, String ip, ComputeNode node) {
		super();
		this.node=node;
		this.linkedInterface=null;
		this.linkedInterfaceUID=0;
		this.name = name;
		this.ip = ip;
	}
	public Portal(String name, String ip) {
		super();
		this.node=null;
		this.linkedInterface=null;
		this.linkedInterfaceUID=0;
		this.name = name;
		this.ip = ip;
	}
	public Portal(String name, String ip, long uid) {
		super();
		this.node=null;
		this.linkedInterface=null;
		this.linkedInterfaceUID=uid;
		this.name = name;
		this.ip = ip;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the ip
	 */
	public String getIP() {
		return ip;
	}
	/**
	 * @param mac the ip to set
	 */
	public void setIP(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the node
	 */
	public ComputeNode getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(ComputeNode node) {
		this.node = node;
	}
	/**
	 * @return the linkedInterface
	 */
	public IInterface getLinkedInterface() {
		return linkedInterface;
	}
	/**
	 * @param linkedInterface the linkedInterface to set
	 */
	public void setLinkedInterface(IInterface linkedInterface) {
		this.linkedInterface = linkedInterface;
		if(linkedInterface != null)
			this.linkedInterfaceUID = linkedInterface.getUID();
	}
	/**
	 * @return the linkedInterfaceUID
	 */
	public long getLinkedInterfaceUID() {
		return linkedInterfaceUID;
	}
	/**
	 * @param linkedInterfaceUID the linkedInterfaceUID to set
	 */
	public void setLinkedInterfaceUID(long linkedInterfaceUID) {
		this.linkedInterfaceUID = linkedInterfaceUID;
		this.linkedInterface = null;
	}
}