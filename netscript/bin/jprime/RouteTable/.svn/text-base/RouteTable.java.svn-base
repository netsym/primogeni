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

import jprime.EntityFactory;
import jprime.ModelNodeRecord;
import jprime.database.ChildId;
import jprime.database.ChildIdList;
import jprime.routing.RouteEntry;
import jprime.routing.TempRouteEntry;
import jprime.util.PersistentRouteEntryList;

import org.python.core.PyObject;
/**
 * @author Nathanael Van Vorst
 *
 */
public class RouteTable extends jprime.gen.RouteTable implements jprime.RouteTable.IRouteTable {
	private PersistentRouteEntryList permRouteEntries;
	private LinkedList<TempRouteEntry> tempRouteEntries;
	
	/**
	 * 
	 */
	public RouteTable(ModelNodeRecord rec){
		super(rec);
		this.permRouteEntries=new PersistentRouteEntryList(getMetadata(),this.getDBID());
		this.tempRouteEntries=new LinkedList<TempRouteEntry>();
		for(ChildId c : rec.kids) {
			if(c.type==EntityFactory.RouteEntry) {
				this.permRouteEntries.addKey(c.child_id,c.order);
			}
		}
	}
	
	public RouteTable(PyObject[] v, String[] n){
		super(v,n);
		this.permRouteEntries=new PersistentRouteEntryList(getMetadata(),this.getDBID());
		this.tempRouteEntries=new LinkedList<TempRouteEntry>();
	}
	
	/**
	 * @param parent
	 */
	public RouteTable(jprime.IModelNode parent){
		super(parent);
		this.permRouteEntries=new PersistentRouteEntryList(getMetadata(),this.getDBID());
		this.tempRouteEntries=new LinkedList<TempRouteEntry>();
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RouteTable;}
	
	/* (non-Javadoc)
	 * @see jprime.gen.RouteTable#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/**
	 * @return
	 */
	public PersistentRouteEntryList getPermRouteEntries() {
		return permRouteEntries;
	}
	
	/**
	 * @return
	 */
	public LinkedList<TempRouteEntry> getTempRouteEntries() {
		return tempRouteEntries;
	}
	
	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#lookupPermRoute(long, long, long, long)
	 */
	public RouteEntry lookupPermRoute(long srcMin, long srcMax, long dstMin, long dstMax){
		RouteEntry rv = permRouteEntries.lookupRoute(srcMin, srcMax, dstMin, dstMax);
		
		if(rv == null) {
	        jprime.Console.out.println("["+getUniqueName()+"]Cannot find route! srcMin="+srcMin+", srcMax="+srcMax+", dstMin="+dstMin+", dstMax="+dstMax);
	        //for(int i=0;i<routeEntries.size();i++) {
	        //	jprime.Console.out.println("\t"+routeEntries.get(i));
	        //}
		}
		
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#makeReal()
	 */
	public void makeReal() {
		//no op
	}
	
	/* (non-Javadoc)
	 * @see jprime.RouteTable.IRouteTable#clearTempRoutes()
	 */
	public void clearTempRoutes() {
		if(tempRouteEntries!=null)
			tempRouteEntries.clear();
	}

	@Override
	public synchronized ChildIdList getChildIds() {
		ChildIdList rv = super.getChildIds();
		if(permRouteEntries!=null)
			rv.addAll(permRouteEntries.getChildIds());
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelNode#delete_extra()
	 */
	@Override
	protected void delete_extra() {
		if(tempRouteEntries!=null)
			tempRouteEntries.clear();
		for(int i=permRouteEntries.size()-1;i>=0;i--)
			permRouteEntries.remove(i);
		permRouteEntries.clear();
	}
}
