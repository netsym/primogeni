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
public abstract class RoutingProtocolAlias extends jprime.ProtocolSession.ProtocolSessionAlias implements jprime.gen.IRoutingProtocolAlias {
	public RoutingProtocolAlias(IModelNode parent, jprime.RoutingProtocol.IRoutingProtocol referencedNode) {
		super(parent,(jprime.RoutingProtocol.IRoutingProtocol)referencedNode);
	}
	public RoutingProtocolAlias(ModelNodeRecord rec){ super(rec); }
	public RoutingProtocolAlias(PyObject[] v, String[] s){super(v,s);}
	public RoutingProtocolAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.RoutingProtocol.IRoutingProtocol.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.RoutingProtocol.RoutingProtocolAliasReplica c = new jprime.RoutingProtocol.RoutingProtocolAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1111: //RoutingProtocolAlias
			case 1223: //RoutingProtocolAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.RoutingProtocol.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
