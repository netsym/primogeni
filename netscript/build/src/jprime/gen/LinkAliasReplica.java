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
public abstract class LinkAliasReplica extends jprime.ModelNodeAliasReplica implements jprime.gen.ILinkAlias {
	public LinkAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name,parent,referencedNode);
	}
	public LinkAliasReplica(ModelNodeRecord rec){ super(rec); }
	public LinkAliasReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Link.ILink.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.Link.LinkAliasReplica c = new jprime.Link.LinkAliasReplica(this.getName(), (IModelNode)parent,(jprime.Link.LinkAlias)this.getReplicatedNode());
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1173: //LinkAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return link delay
	 */
	public jprime.variable.FloatingPointNumberVariable getDelay() {
		return (jprime.variable.FloatingPointNumberVariable)((ILink)deference()).getDelay();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDelay(String value) {
		((ILink)deference()).setDelay(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDelay(float value) {
		((ILink)deference()).setDelay(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDelay(jprime.variable.SymbolVariable value) {
		((ILink)deference()).setDelay(value);
	}

	/**
	 * @return link bandwidth
	 */
	public jprime.variable.FloatingPointNumberVariable getBandwidth() {
		return (jprime.variable.FloatingPointNumberVariable)((ILink)deference()).getBandwidth();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBandwidth(String value) {
		((ILink)deference()).setBandwidth(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBandwidth(float value) {
		((ILink)deference()).setBandwidth(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBandwidth(jprime.variable.SymbolVariable value) {
		((ILink)deference()).setBandwidth(value);
	}

	/**
	 * @return The n in W.X.Y.Z/n
	 */
	public jprime.variable.IntegerVariable getIpPrefixLen() {
		return (jprime.variable.IntegerVariable)((ILink)deference()).getIpPrefixLen();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(String value) {
		((ILink)deference()).setIpPrefixLen(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefixLen(long value) {
		((ILink)deference()).setIpPrefixLen(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefixLen(jprime.variable.SymbolVariable value) {
		((ILink)deference()).setIpPrefixLen(value);
	}

	/**
	 * @return ip prefix of this Link
	 */
	public jprime.variable.OpaqueVariable getIpPrefix() {
		return (jprime.variable.OpaqueVariable)((ILink)deference()).getIpPrefix();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpPrefix(String value) {
		((ILink)deference()).setIpPrefix(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpPrefix(jprime.variable.SymbolVariable value) {
		((ILink)deference()).setIpPrefix(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Link.attrIds;
	}

	/**
	  * Create a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(jprime.BaseInterface.IBaseInterface to_alias) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.BaseInterface.BaseInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.BaseInterface.IBaseInterface createBaseInterface(String name, jprime.BaseInterface.IBaseInterface to_alias) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Add a new child of type jprime.BaseInterface.BaseInterfaceAlias
	  */
	public void addBaseInterfaceAlias(jprime.BaseInterface.BaseInterfaceAlias kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return network interfaces attached to this link
	  */
	public jprime.util.ChildList<jprime.BaseInterface.IBaseInterfaceAlias> getAttachments() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(jprime.Interface.IInterface to_alias) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.Interface.InterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(String name, jprime.Interface.IInterface to_alias) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Add a new child of type jprime.Interface.InterfaceAlias
	  */
	public void addInterfaceAlias(jprime.Interface.InterfaceAlias kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(jprime.GhostInterface.IGhostInterface to_alias) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.GhostInterface.GhostInterfaceAlias, which points to to_alias, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.GhostInterface.IGhostInterface createGhostInterface(String name, jprime.GhostInterface.IGhostInterface to_alias) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Add a new child of type jprime.GhostInterface.GhostInterfaceAlias
	  */
	public void addGhostInterfaceAlias(jprime.GhostInterface.GhostInterfaceAlias kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
