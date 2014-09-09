package jprime.RoutingSphere;

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

import jprime.ModelNodeRecord;
import jprime.RouteTable.IRouteTable;
import jprime.RouteTable.RouteTable;
import jprime.util.ChildList;
import jprime.variable.ModelNodeVariable;
import jprime.variable.StringVariable;

import org.python.core.PyObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public class RoutingSphere extends jprime.gen.RoutingSphere implements jprime.RoutingSphere.IRoutingSphere {
	private EdgeInterfacePairList edges = null;
	
	public RoutingSphere(PyObject[] v, String[] n){
		super(v,n);
		this.createRouteTable();
	}
	
	/**
	 * 
	 */
	public RoutingSphere(ModelNodeRecord rec){
		super(rec);
	}
	
	/**
	 * @param parent
	 */
	public RoutingSphere(jprime.IModelNode parent){
		super(parent);
		this.createRouteTable();
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RoutingSphere;}
	
	/* (non-Javadoc)
	 * @see jprime.gen.RoutingSphere#accept(jprime.visitors.IGenericVisitor)
	 */
	public void accept(jprime.visitors.IGenericVisitor visitor){
		visitor.visit(this);
	}
	
	/* (non-Javadoc)
	 * @see jprime.RoutingSphere.IRoutingSphere#getRouteTable()
	 */
	public IRouteTable getRouteTable() {
		ChildList<IRouteTable> rv = getDefault_route_table();
		if(rv.size()!=1) {
			this.addRouteTable(new RouteTable(this));
		}
		return rv.enumerate().next();
	}
	
	/* (non-Javadoc)
	 * @see jprime.RoutingSphere.IRoutingSphere#getedgeInterfaces()
	 */
	public EdgeInterfacePairList getEdgeInterfaces() {
		if(edges==null) {
			StringVariable str = (StringVariable)getAttributeByName(ModelNodeVariable.rs_edge_interfaces());
			if(str == null) {
				str = new StringVariable(ModelNodeVariable.rs_edge_interfaces(), EdgeInterfacePair.toBytes(new LinkedList<EdgeInterfacePair>()));
				str.setOwner(this);
				attrs.put(ModelNodeVariable.rs_edge_interfaces(), str);
				modified(Modified.ATTRS);
				edges=new EdgeInterfacePairList(str, new LinkedList<EdgeInterfacePair>());
			}
			else {
				edges= new EdgeInterfacePairList(str, EdgeInterfacePair.fromBytes(str.getValue()));
			}
		}
		return edges;
	}
	
	/* (non-Javadoc)
	 * @see jprime.RoutingSphere.IRoutingSphere#makeReal()
	 */
	public void makeReal() {
		//no op
	}

}
