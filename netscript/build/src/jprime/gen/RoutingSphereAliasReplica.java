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
public abstract class RoutingSphereAliasReplica extends jprime.ModelNodeAliasReplica implements jprime.gen.IRoutingSphereAlias {
	public RoutingSphereAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name,parent,referencedNode);
	}
	public RoutingSphereAliasReplica(ModelNodeRecord rec){ super(rec); }
	public RoutingSphereAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.RoutingSphere.IRoutingSphere.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.RoutingSphere.RoutingSphereAliasReplica c = new jprime.RoutingSphere.RoutingSphereAliasReplica(this.getName(), (IModelNode)parent,(jprime.RoutingSphere.RoutingSphereAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1203: //RoutingSphereAliasReplica
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
	 * @return The maximum number of nix vector cache entries for this sphere.
	 */
	public jprime.variable.IntegerVariable getNixVecCacheSize() {
		return (jprime.variable.IntegerVariable)((IRoutingSphere)deference()).getNixVecCacheSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNixVecCacheSize(String value) {
		((IRoutingSphere)deference()).setNixVecCacheSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNixVecCacheSize(long value) {
		((IRoutingSphere)deference()).setNixVecCacheSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNixVecCacheSize(jprime.variable.SymbolVariable value) {
		((IRoutingSphere)deference()).setNixVecCacheSize(value);
	}

	/**
	 * @return The maximum number of local dst cache entries for this sphere.
	 */
	public jprime.variable.IntegerVariable getLocalDstCacheSize() {
		return (jprime.variable.IntegerVariable)((IRoutingSphere)deference()).getLocalDstCacheSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(String value) {
		((IRoutingSphere)deference()).setLocalDstCacheSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(long value) {
		((IRoutingSphere)deference()).setLocalDstCacheSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(jprime.variable.SymbolVariable value) {
		((IRoutingSphere)deference()).setLocalDstCacheSize(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.RoutingSphere.attrIds;
	}

	/**
	  * Create a new child of type jprime.RouteTable.RouteTable and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.RouteTable.RouteTable, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTable and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.RouteTable.RouteTable.
	  */
	public void addRouteTable(jprime.RouteTable.RouteTable kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTableReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTableReplica(jprime.RouteTable.IRouteTable to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.RouteTable.RouteTableReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable replicateRouteTable(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTableReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTableReplica(String name, jprime.RouteTable.IRouteTable to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return default routing table
	  */
	public jprime.util.ChildList<jprime.RouteTable.IRouteTable> getDefault_route_table() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
