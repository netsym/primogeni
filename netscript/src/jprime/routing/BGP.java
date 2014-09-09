package jprime.routing;

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

import jprime.EntityFactory;
import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.Link.Link;
import jprime.Net.INet;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.util.PersistentBGPLinkList;

/**
 * @author Nathanael Van Vorst
 * @author Ting Li
 */
public class BGP extends StaticRoutingProtocol {
	private PersistentBGPLinkList linkTypes;
	
	
	
	/**
	 * Used to load from db
	 */
	public BGP(ModelNodeRecord rec) {
		super(rec.meta,rec.dbid,EntityFactory.BGP,rec.db_parent_id);
		this.linkTypes=new PersistentBGPLinkList(rec.meta);
		for(ChildId c : rec.kids ) {
			if(c.type == EntityFactory.BGPLinkType) {
				linkTypes.addKey(c.child_id);
			}
			else throw new RuntimeException("Invalid child type!");
		}
	}

	/**
	 * @param parent
	 */
	public BGP(INet parent) {
		super(parent,EntityFactory.BGP);
		linkTypes=new PersistentBGPLinkList(getParent().getMetadata());
	}
	
	/* (non-Javadoc)
	 * @see jprime.IPeristable#getChildIds()
	 */
	public synchronized ChildIdList getChildIds() {
		ChildIdList rv = super.getChildIds();
		if(linkTypes != null)
			rv.addAll(linkTypes.getChildIds());
		return rv;
	}

	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#isASLevelProtocol()
	 */
	public boolean isASLevelProtocol() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#isAlgorithmicProtocol()
	 */
	public boolean isAlgorithmicProtocol() {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#getRouteVisitor(jprime.Net.Net)
	 */
	public IRouteVisitor getRouteVisitor(INet net) {
		return new BGPVisitor(net);
	}
		
	/**
	 * @return
	 */
	public List<BGPLinkType> getLinkTypes() {
		return linkTypes;
	}
	
	/**
	 * @param lt
	 */
	public synchronized void addBGPLinkType(BGPLinkType lt) {
		linkTypes.add(lt);
		modified(Modified.CHILDREN);
	}
	
	/**
	 * @param link
	 * @param link_type
	 * @return
	 */
	public synchronized BGPLinkType createLinkType(Link link, BGPRelationShipType link_type, boolean first_child_is_src) {
		BGPLinkType rv = new BGPLinkType(this, link, link_type, first_child_is_src);
		linkTypes.add(rv);
		modified(Modified.CHILDREN);
		return rv;
	}
	
	
	/* (non-Javadoc)
	 * @see jprime.routing.StaticRoutingProtocol#deepCopy(jprime.Net.INet)
	 */
	public StaticRoutingProtocol deepCopy(INet parent) {
		throw new RuntimeException("Not finished!");
	}
	
	public IModelNode __copy(String name, IModelNode toCopy, IModelNode parent) {
		throw new RuntimeException("Not finished!");
	}
	
	public int getTypeId() {
		return EntityFactory.BGP;
	}
}
