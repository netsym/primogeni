/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */


package jprime.gen;

import jprime.*;
import jprime.ModelNodeRecord;
import jprime.variable.*;
import org.python.core.PyObject;
import org.python.core.Py;
public abstract class RouteTableAlias extends jprime.ModelNodeAlias implements jprime.gen.IRouteTableAlias {
	public RouteTableAlias(IModelNode parent, jprime.RouteTable.IRouteTable referencedNode) {
		super(parent,(IModelNode)referencedNode);
	}
	public RouteTableAlias(ModelNodeRecord rec){ super(rec); }
	public RouteTableAlias(PyObject[] v, String[] s){super(v,s);}
	public RouteTableAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.RouteTable.IRouteTable.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.RouteTable.RouteTableAliasReplica c = new jprime.RouteTable.RouteTableAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1090: //RouteTableAlias
			case 1202: //RouteTableAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return A comma separated list of edge ifaces
	 */
	public jprime.variable.OpaqueVariable getEdgeIfaces() {
		return (jprime.variable.OpaqueVariable)((IRouteTable)deference()).getEdgeIfaces();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setEdgeIfaces(String value) {
		((IRouteTable)deference()).setEdgeIfaces(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setEdgeIfaces(jprime.variable.SymbolVariable value) {
		((IRouteTable)deference()).setEdgeIfaces(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.RouteTable.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
