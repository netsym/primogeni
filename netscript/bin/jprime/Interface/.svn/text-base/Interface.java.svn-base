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
import jprime.gen.ModelNodeVariable;
import jprime.variable.OpaqueVariable;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class Interface extends jprime.gen.Interface implements jprime.Interface.IInterface {
	private SoftReference<ILink> __attachedLink=null;
	private List<String> reachable_nets = null;
	
	public Interface(PyObject[] v, String[] n){
		super(v,n);
		__attachedLink=null;
	}

	/**
	 * 
	 */
	public Interface(ModelNodeRecord rec){
		super(rec);
		__attachedLink=null;
	}

	/**
	 * @param parent
	 */
	public Interface(jprime.IModelNode parent){
		super(parent);
		__attachedLink=null;
	}

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.Interface; }

	/* (non-Javadoc)
	 * @see jprime.gen.Interface#accept(jprime.visitors.IGenericVisitor)
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
	
	public void addReachableNetwork(String ip_prefix) {
		for(IModelNode c : getAllChildren()) {
			if(c instanceof ITrafficPortal) {
				ITrafficPortal tp = (ITrafficPortal)c;
				OpaqueVariable ov =tp.getNetworks();
				if(ov != null) {
					if(reachable_nets == null)
						reachable_nets=decodeIPPrefixes(ov.toString());
					reachable_nets.add(ip_prefix);
					ov.setValueAsString(encodeIPPrefixes(reachable_nets));
					return;
				}
				else {
					if(reachable_nets == null)
						reachable_nets=new LinkedList<String>();
					reachable_nets.add(ip_prefix);
					try {
						tp.setAttribute(ModelNodeVariable.networks(), encodeIPPrefixes(reachable_nets));
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
		if(reachable_nets == null) {
			for(IModelNode c : getAllChildren()) {
				if(c instanceof ITrafficPortal) {
					ITrafficPortal tp = (ITrafficPortal)c;
					OpaqueVariable ov =tp.getNetworks();
					if(ov != null) {
						if(reachable_nets == null)
							reachable_nets=decodeIPPrefixes(ov.toString());
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


	public boolean isTrafficPortal() {
		for(IModelNode c : getAllChildren()) {
			if(c instanceof ITrafficPortal) {
				return true;
			}
		}
		return false;
	}

	
	protected static void help(List<String> rv) {
		rv.add("Interface:");
		rv.add("   Drop Tail Queue Creation Functions:");		
		rv.add("      createDropTailQueue()");
		rv.add("      createDropTailQueue(String n)");
		rv.add("      createDropTailQueueReplica(IDropTailQueue)");
		rv.add("      createDropTailQueueReplica(String, IDropTailQueue)");
		rv.add("      replicateDropTailQueue(PyObject[], String[])");
		rv.add("      newDropTailQueue(PyObject[], String[])");
		rv.add("   Red Tail Queue Creation Functions:");		
		rv.add("      createRedQueue()");
		rv.add("      createRedQueue(String)");
		rv.add("      createRedQueueReplica(IRedQueue)");
		rv.add("      createRedQueueReplica(String, IRedQueue)");
		rv.add("      replicateRedQueue(PyObject[], String[])");
		rv.add("      newRedQueue(PyObject[], String[])");
		rv.add("   Fluid Queue Creation Functions:");		
		rv.add("      createFluidQueue()");
		rv.add("      createFluidQueue(String)");
		rv.add("      createFluidQueueReplica(IFluidQueue)");
		rv.add("      createFluidQueueReplica(String, IFluidQueue)");
		rv.add("      replicateFluidQueue(PyObject[], String[])");
		rv.add("      newFluidQueue(PyObject[], String[])");
		rv.add("   Property Setters/Getters:");		
		rv.add("      setBitRate(float)");
		rv.add("      getBitRate()");
		rv.add("      setBufferSize(long)");
		rv.add("      getBufferSize()");
		rv.add("      setCollectData(boolean)");
		rv.add("      getCollectData()");
		rv.add("      setLatency(long)");
		rv.add("      getLatency()");
		rv.add("      setMtu(long)");
		rv.add("      getMtu()");
		rv.add("   State Getters:");		
		rv.add("      getNumInBytes()");
		rv.add("      getNumInPackets()");
		rv.add("      getNumInUcastBytes()");
		rv.add("      getNumInUcastPackets()");
		rv.add("      getNumOutBytes()");
		rv.add("      getNumOutPackets()");
		rv.add("      getNumOutUcastBytes()");
		rv.add("      getNumOutUcastPackets()");
		rv.add("      getQueueSize()");
	}

	public List<String> help() {
		List<String> rv=super.help();
		Interface.help(rv);
		return rv;
	}
	
	
	/**
	 * Transform a string into a list of ip prefixes
	 * 
	 * @param s
	 * @return
	 */
	public static List<String> decodeIPPrefixes(String s) {
		LinkedList<String> rv = new LinkedList<String>();
		String mydelimiter = "\\[|,|\\]";
		String[] temp=s.replaceFirst("^"+mydelimiter,"").split(mydelimiter);
		for(int i=0;i<temp.length;i++) {
			rv.add(temp[i]);
		}
		return rv;
	}

	/**
	 * Transform a list of ip prefixes into a string
	 * @param c
	 * @return
	 */
	public static String encodeIPPrefixes(List<String> l) {
		String rv =null;
		for(String s : l) {
			if(rv == null) rv ="[";
			else rv+=",";
			rv+=s;
		}
		return rv+"]";
	}

}
