package jprime.Interface;

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

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.Link.ILink;

import org.python.core.PyObject;
/**
 * @author Nathanael Van Vorst
 *
 */
public class InterfaceAliasReplica extends jprime.gen.InterfaceAliasReplica implements jprime.Interface.IInterfaceAlias {
	private boolean setAttachedLink;

	public InterfaceAliasReplica(PyObject[] v, String[] n){
		super(v,n);
		setAttachedLink=false;
	}
	
	/**
	 * 
	 */
	public InterfaceAliasReplica(ModelNodeRecord rec){ 
		super(rec); 
		setAttachedLink=false;
	}
	
	/**
	 * @param parent
	 * @param referencedNode
	 */
	public InterfaceAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
		setAttachedLink=false;
	}
	
	public boolean isTrafficPortal() {
		return ((IInterface)deference()).isTrafficPortal();
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.InterfaceAliasReplica; }
	
	
	/* (non-Javadoc)
	 * @see jprime.gen.InterfaceAliasReplica#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/* (non-Javadoc)
	 * @see jprime.Interface.IInterface#setAttachedLink(jprime.Link.ILink)
	 */
	public void setAttachedLink(ILink l) {
		throw new RuntimeException("cant attach to an interface alias!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.Interface.IInterface#getAttachedLink()
	 */
	public ILink getAttachedLink(){
		if(!setAttachedLink) {
			setAttachedLink=true;
			if(getParent() instanceof ILink) {
				((IInterface)deference()).setAttachedLink((ILink)getParent());
			}
		}
		return ((IInterface)deference()).getAttachedLink();
	}	
	
	/* (non-Javadoc)
	 * @see jprime.Interface.IInterface#addReachableNetwork(java.lang.String)
	 */
	public void addReachableNetwork(String ip_prefix) {
		((IInterface)deference()).addReachableNetwork(ip_prefix);
	}
	
	/* (non-Javadoc)
	 * @see jprime.Interface.IInterface#getReachableNetworks()
	 */
	public List<String> getReachableNetworks() {
		return ((IInterface)deference()).getReachableNetworks();
	}
	
	public List<String> help() {
		List<String> rv=super.help();
		Interface.help(rv);
		return rv;
	}
}
