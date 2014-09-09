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
public abstract class RoutingProtocolReplica extends jprime.ProtocolSession.ProtocolSessionReplica implements jprime.gen.IRoutingProtocol {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public RoutingProtocolReplica(String name, IModelNode parent, jprime.RoutingProtocol.IRoutingProtocol referencedNode) {
		super(name,parent,referencedNode);
	}
	public RoutingProtocolReplica(ModelNodeRecord rec){ super(rec); }
	public RoutingProtocolReplica(PyObject[] v, String[] s){super(v,s);}
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
		doing_deep_copy=true;
		jprime.RoutingProtocol.RoutingProtocolReplica c = new jprime.RoutingProtocol.RoutingProtocolReplica(this.getName(), (IModelNode)parent,(jprime.RoutingProtocol.IRoutingProtocol)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1167: //RoutingProtocolReplica
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
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
