/* ------------------------- */
/* ------------------------- */
/*         WARNING: */
/*  THIS FILE IS GENERATED! */
/*        DO NOT EDIT! */
/* ------------------------- */
/* ------------------------- */


package jprime.gen;

import jprime.*;
import jprime.variable.*;
import jprime.ModelNodeRecord;
import org.python.core.PyObject;
import org.python.core.Py;
public abstract class RouteTable extends jprime.ModelNode implements jprime.gen.IRouteTable {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public RouteTable(PyObject[] v, String[] s){super(v,s);}
	public RouteTable(ModelNodeRecord rec){ super(rec); }
	public RouteTable(IModelNode parent){ super(parent); }
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
		jprime.RouteTable.RouteTableReplica c = new jprime.RouteTable.RouteTableReplica(this.getName(),(IModelNode)parent,(jprime.RouteTable.IRouteTable)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1034: //RouteTable
			case 1090: //RouteTableAlias
			case 1146: //RouteTableReplica
			case 1202: //RouteTableAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();
	public final static java.util.ArrayList<Integer> attrIds=new java.util.ArrayList<Integer>();
	static {
		attrIds.add(36);
	}

	/**
	 * @return A comma separated list of edge ifaces
	 */
	public jprime.variable.OpaqueVariable getEdgeIfaces() {
		return (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.edge_ifaces());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setEdgeIfaces(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.edge_ifaces());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.edge_ifaces(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.edge_ifaces(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setEdgeIfaces(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.edge_ifaces());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
