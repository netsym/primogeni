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
import org.python.core.PyObject;
public interface IRoutingSphere extends jprime.IModelNode {

	/**
	 * @return The maximum number of nix vector cache entries for this sphere.
	 */
	public jprime.variable.IntegerVariable getNixVecCacheSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNixVecCacheSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNixVecCacheSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNixVecCacheSize(jprime.variable.SymbolVariable value);

	/**
	 * @return The maximum number of local dst cache entries for this sphere.
	 */
	public jprime.variable.IntegerVariable getLocalDstCacheSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setLocalDstCacheSize(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();

	/**
	  * Create a new child of type jprime.RouteTable.RouteTable and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable();

	/**
	  * jython method to create a a new child of type jprime.RouteTable.RouteTable, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTable and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTable(String name);

	 /**
	  * Add a new child of type jprime.RouteTable.RouteTable.
	  */
	public void addRouteTable(jprime.RouteTable.RouteTable kid);

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTableReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTableReplica(jprime.RouteTable.IRouteTable to_replicate);

	/**
	  * jython method to create replica new child of type jprime.RouteTable.RouteTableReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable replicateRouteTable(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RouteTable.RouteTableReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RouteTable.IRouteTable createRouteTableReplica(String name, jprime.RouteTable.IRouteTable to_replicate);

	/**
	  * return default routing table
	  */
	public jprime.util.ChildList<jprime.RouteTable.IRouteTable> getDefault_route_table();
}
