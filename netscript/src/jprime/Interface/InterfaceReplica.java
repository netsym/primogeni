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

import java.lang.ref.SoftReference;
import java.util.LinkedList;
import java.util.List;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.Link.ILink;
import jprime.TrafficPortal.ITrafficPortal;
import jprime.TrafficPortal.TrafficPortal;
import jprime.gen.ModelNodeVariable;
import jprime.variable.OpaqueVariable;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class InterfaceReplica extends jprime.gen.InterfaceReplica implements jprime.Interface.IInterface {
	private SoftReference<ILink> __attachedLink;
	private List<String> reachable_nets = null;
	
	public InterfaceReplica(PyObject[] v, String[] n){
		super(v,n);
		this.__attachedLink=null;
		}
	/**
	 * 
	 */
	public InterfaceReplica(ModelNodeRecord rec){
		super(rec);
		this.__attachedLink=null;
	}
	
	/**
	 * @param parent
	 * @param referencedNode
	 */
	public InterfaceReplica(String name, IModelNode parent, jprime.Interface.IInterface referencedNode) {
		super(name, parent,(jprime.Interface.IInterface)referencedNode);
		this.__attachedLink=null;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.InterfaceReplica; }
	
	/* (non-Javadoc)
	 * @see jprime.gen.InterfaceReplica#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/* (non-Javadoc)
	 * @see jprime.Interface.IInterface#setAttachedLink(jprime.Link.ILink)
	 */
	public void setAttachedLink(ILink l) {
		if(getAttached_link_id()==0) {
			setAttached_link_id(l.getDBID());
			__attachedLink=new SoftReference<ILink>(l);
		}
		else if(l.getDBID()!=getAttached_link_id()){
			throw new RuntimeException("This interface is already attached to the link "+getAttachedLink().getUniqueName());
		}
	}
	
	/* (non-Javadoc)
	 * @see jprime.Interface.IInterface#getAttachedLink()
	 */
	public ILink getAttachedLink(){
		ILink rv = __attachedLink==null?null:__attachedLink.get();
		if(rv==null) {
			if(getAttached_link_id()!=0)
				rv=(ILink)getMetadata().loadModelNode(getAttached_link_id());
				__attachedLink=new SoftReference<ILink>(rv);
		}
    	/* $if DEBUG $
		if(rv!=null) {
			getMetadata().logAccess(rv);
		}
		$endif$ */
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.gen.InterfaceReplica#createTrafficPortal()
	 */
	@Override
	public ITrafficPortal createTrafficPortal() {
		this.convertToReal();
		return super.createTrafficPortal();
	}
	
	/* (non-Javadoc)
	 * @see jprime.gen.InterfaceReplica#createTrafficPortal(java.lang.String)
	 */
	@Override
	public ITrafficPortal createTrafficPortal(String name) {
		this.convertToReal();
		return super.createTrafficPortal(name);
	}
	
	/* (non-Javadoc)
	 * @see jprime.gen.InterfaceReplica#addTrafficPortal(jprime.TrafficPortal.TrafficPortal)
	 */
	@Override
	public void addTrafficPortal(TrafficPortal kid) {
		this.convertToReal();
		super.addTrafficPortal(kid);
	}
	
	/* (non-Javadoc)
	 * @see jprime.gen.InterfaceReplica#createTrafficPortalReplica(jprime.TrafficPortal.ITrafficPortal)
	 */
	@Override
	public ITrafficPortal createTrafficPortalReplica(ITrafficPortal to_replicate) {
		throw new RuntimeException("dont make replicas of portals!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.gen.InterfaceReplica#createTrafficPortalReplica(java.lang.String, jprime.TrafficPortal.ITrafficPortal)
	 */
	@Override
	public ITrafficPortal createTrafficPortalReplica(String name,
			ITrafficPortal to_replicate) {
		throw new RuntimeException("dont make replicas of portals!");
	}
	
	public boolean isTrafficPortal() {
		if(isReplica())
			throw new RuntimeException("how did this happen?");
		for(IModelNode c : getAllChildren()) {
			if(c instanceof ITrafficPortal) {
				return true;
			}
		}
		return false;
	}

	public void addReachableNetwork(String ip_prefix) {
		if(isReplica())
			throw new RuntimeException("dont call on replicated intefaces!");
		for(IModelNode c : getAllChildren()) {
			if(c instanceof ITrafficPortal) {
				ITrafficPortal tp = (ITrafficPortal)c;
				OpaqueVariable ov =tp.getNetworks();
				if(ov != null) {
					if(reachable_nets == null)
						reachable_nets=Interface.decodeIPPrefixes(ov.toString());
					reachable_nets.add(ip_prefix);
					ov.setValueAsString(Interface.encodeIPPrefixes(reachable_nets));
					return;
				}
				else {
					if(reachable_nets == null)
						reachable_nets=new LinkedList<String>();
					reachable_nets.add(ip_prefix);
					try {
						tp.setAttribute(ModelNodeVariable.networks(), Interface.encodeIPPrefixes(reachable_nets));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					return;
				}
			}
		}
		throw new RuntimeException("This is not a traffic portal!");
	}
	
	public List<String> getReachableNetworks() {
		if(isReplica())
			return ((Interface)getReplicatedNode()).getReachableNetworks();
		if(reachable_nets == null) {
			for(IModelNode c : getAllChildren()) {
				if(c instanceof ITrafficPortal) {
					ITrafficPortal tp = (ITrafficPortal)c;
					OpaqueVariable ov =tp.getNetworks();
					if(ov != null) {
						if(reachable_nets == null)
							reachable_nets=Interface.decodeIPPrefixes(ov.toString());
					}
					else {
						if(reachable_nets == null)
							reachable_nets=new LinkedList<String>();
					}
				}
			}
		}
		return reachable_nets;
	}

	
	public List<String> help() {
		List<String> rv=super.help();
		Interface.help(rv);
		return rv;
	}
}
