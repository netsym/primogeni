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
public interface ILink extends jprime.IModelNode {

	/**
	 * @return link delay
	 */
	public jprime.variable.FloatingPointNumberVariable getDelay();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDelay(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDelay(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDelay(jprime.variable.SymbolVariable value);

	/**
	 * @return link bandwidth
	 */
	public jprime.variable.FloatingPointNumberVariable getBandwidth();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBandwidth(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBandwidth(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBandwidth(jprime.variable.SymbolVariable value);

	/**
	 * @return The n in W.X.Y.Z/n
	 */
	public jprime.variable.IntegerVariable getIpPrefixLen();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefixLen(jprime.variable.SymbolVariable value);

	/**
	 * @return ip prefix of this Link
	 */
	public jprime.variable.OpaqueVariable getIpPrefix();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefix(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefix(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();

	/**
	  * Create a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(jprime.BaseInterface.IBaseInterface to_alias);

	/**
	  * jython method to create a a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(PyObject[] v, String[] n);

	/**
	  * Create a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(String name, jprime.BaseInterface.IBaseInterface to_alias);

	/**
	  * Add a new child of type jprime.BaseInterface.BaseInterfaceAlias
	  */
	public void addBaseInterfaceAlias(jprime.BaseInterface.BaseInterfaceAlias kid);

	/**
	  * return network interfaces attached to this link
	  */
	public jprime.util.ChildList<jprime.BaseInterface.IBaseInterfaceAlias> getAttachments();

	/**
	  * Create a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(jprime.Interface.IInterface to_alias);

	/**
	  * jython method to create a a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(PyObject[] v, String[] n);

	/**
	  * Create a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(String name, jprime.Interface.IInterface to_alias);

	/**
	  * Add a new child of type jprime.Interface.InterfaceAlias
	  */
	public void addInterfaceAlias(jprime.Interface.InterfaceAlias kid);

	/**
	  * Create a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(jprime.GhostInterface.IGhostInterface to_alias);

	/**
	  * jython method to create a a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(PyObject[] v, String[] n);

	/**
	  * Create a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(String name, jprime.GhostInterface.IGhostInterface to_alias);

	/**
	  * Add a new child of type jprime.GhostInterface.GhostInterfaceAlias
	  */
	public void addGhostInterfaceAlias(jprime.GhostInterface.GhostInterfaceAlias kid);
}
