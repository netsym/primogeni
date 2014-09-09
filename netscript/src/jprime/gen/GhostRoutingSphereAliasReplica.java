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
public abstract class GhostRoutingSphereAliasReplica extends jprime.RoutingSphere.RoutingSphereAliasReplica implements jprime.gen.IGhostRoutingSphereAlias {
	public GhostRoutingSphereAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}
	public GhostRoutingSphereAliasReplica(ModelNodeRecord rec){ super(rec); }
	public GhostRoutingSphereAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.GhostRoutingSphere.IGhostRoutingSphere.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.GhostRoutingSphere.GhostRoutingSphereAliasReplica c = new jprime.GhostRoutingSphere.GhostRoutingSphereAliasReplica(this.getName(), (IModelNode)parent,(jprime.GhostRoutingSphere.GhostRoutingSphereAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1204: //GhostRoutingSphereAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return the id of the community where the real sphere is
	 */
	public jprime.variable.IntegerVariable getCommunityId() {
		return (jprime.variable.IntegerVariable)((IGhostRoutingSphere)deference()).getCommunityId();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCommunityId(String value) {
		((IGhostRoutingSphere)deference()).setCommunityId(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCommunityId(long value) {
		((IGhostRoutingSphere)deference()).setCommunityId(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCommunityId(jprime.variable.SymbolVariable value) {
		((IGhostRoutingSphere)deference()).setCommunityId(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.GhostRoutingSphere.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
