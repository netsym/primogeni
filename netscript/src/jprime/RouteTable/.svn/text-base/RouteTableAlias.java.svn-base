package jprime.RouteTable;

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

import java.util.LinkedList;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.routing.RouteEntry;
import jprime.routing.TempRouteEntry;
import jprime.util.PersistentRouteEntryList;

import org.python.core.PyObject;
/**
 * @author Nathanael Van Vorst
 *
 */
public class RouteTableAlias extends jprime.gen.RouteTableAlias implements jprime.RouteTable.IRouteTableAlias {
	/**
	 * 
	 */
	public RouteTableAlias(ModelNodeRecord rec){ super(rec); }
	
	public RouteTableAlias(IModelNode parent){ super(parent); }
	public RouteTableAlias(PyObject[] v, String[] n){
		super(v,n);
	}
	
	/**
	 * @param parent
	 * @param referencedNode
	 */
	public RouteTableAlias(IModelNode parent, jprime.RouteTable.IRouteTable referencedNode) {
		super(parent,(jprime.RouteTable.IRouteTable)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RouteTableAlias;}
	
	/* (non-Javadoc)
	 * @see jprime.gen.RouteTableAlias#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#getPermRouteEntries()
	 */
	public PersistentRouteEntryList getPermRouteEntries() {
		throw new RuntimeException("Aliases dont have route entries!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#getTempRouteEntries()
	 */
	public LinkedList<TempRouteEntry> getTempRouteEntries() {
		throw new RuntimeException("Aliases dont have route entries!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#lookupRoute(long, long, long, long)
	 */
	public RouteEntry lookupPermRoute(long srcMin, long srcMax, long dstMin, long dstMax){
		throw new RuntimeException("Cant lookup route entries to an alias!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#clearTempRoutes()
	 */
	public void clearTempRoutes() {
		throw new RuntimeException("Cant lookup route entries to an alias!");
	}
	
	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#makeReal()
	 */
	public void makeReal() {
		//no op
	}

}
