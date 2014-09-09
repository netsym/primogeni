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
public abstract class HostAlias extends jprime.ProtocolGraph.ProtocolGraphAlias implements jprime.gen.IHostAlias {
	public HostAlias(IModelNode parent, jprime.Host.IHost referencedNode) {
		super(parent,(jprime.Host.IHost)referencedNode);
	}
	public HostAlias(ModelNodeRecord rec){ super(rec); }
	public HostAlias(PyObject[] v, String[] s){super(v,s);}
	public HostAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Host.IHost.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.Host.HostAliasReplica c = new jprime.Host.HostAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1088: //HostAlias
			case 1089: //RouterAlias
			case 1200: //HostAliasReplica
			case 1201: //RouterAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return host's seed
	 */
	public jprime.variable.IntegerVariable getHostSeed() {
		return (jprime.variable.IntegerVariable)((IHost)deference()).getHostSeed();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHostSeed(String value) {
		((IHost)deference()).setHostSeed(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHostSeed(long value) {
		((IHost)deference()).setHostSeed(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setHostSeed(jprime.variable.SymbolVariable value) {
		((IHost)deference()).setHostSeed(value);
	}

	/**
	 * @return host's time skew (ratio of host time over absolute time)
	 */
	public jprime.variable.FloatingPointNumberVariable getTimeSkew() {
		return (jprime.variable.FloatingPointNumberVariable)((IHost)deference()).getTimeSkew();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeSkew(String value) {
		((IHost)deference()).setTimeSkew(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeSkew(float value) {
		((IHost)deference()).setTimeSkew(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTimeSkew(jprime.variable.SymbolVariable value) {
		((IHost)deference()).setTimeSkew(value);
	}

	/**
	 * @return host's time offset (relative to absolute time zero)
	 */
	public jprime.variable.OpaqueVariable getTimeOffset() {
		return (jprime.variable.OpaqueVariable)((IHost)deference()).getTimeOffset();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeOffset(String value) {
		((IHost)deference()).setTimeOffset(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTimeOffset(jprime.variable.SymbolVariable value) {
		((IHost)deference()).setTimeOffset(value);
	}

	/**
	 * @return max  [ for each nic: (bits_out/bit_rate) ]
	 */
	public jprime.variable.FloatingPointNumberVariable getTrafficIntensity() {
		return (jprime.variable.FloatingPointNumberVariable)((IHost)deference()).getTrafficIntensity();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficIntensity(String value) {
		((IHost)deference()).setTrafficIntensity(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficIntensity(float value) {
		((IHost)deference()).setTrafficIntensity(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTrafficIntensity(jprime.variable.SymbolVariable value) {
		((IHost)deference()).setTrafficIntensity(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Host.attrIds;
	}

	/**
	  * Create a new child of type jprime.Interface.Interface and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.Interface.Interface, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Interface.Interface and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.Interface.Interface.
	  */
	public void addInterface(jprime.Interface.Interface kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Interface.InterfaceReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterfaceReplica(jprime.Interface.IInterface to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.Interface.InterfaceReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface replicateInterface(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.Interface.InterfaceReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterfaceReplica(String name, jprime.Interface.IInterface to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return network interfaces on this host
	  */
	public jprime.util.ChildList<jprime.Interface.IInterface> getNics() {
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
