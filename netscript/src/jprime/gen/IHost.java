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
public interface IHost extends jprime.ProtocolGraph.IProtocolGraph {

	/**
	 * @return host's seed
	 */
	public jprime.variable.IntegerVariable getHostSeed();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHostSeed(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHostSeed(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setHostSeed(jprime.variable.SymbolVariable value);

	/**
	 * @return host's time skew (ratio of host time over absolute time)
	 */
	public jprime.variable.FloatingPointNumberVariable getTimeSkew();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeSkew(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeSkew(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTimeSkew(jprime.variable.SymbolVariable value);

	/**
	 * @return host's time offset (relative to absolute time zero)
	 */
	public jprime.variable.OpaqueVariable getTimeOffset();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTimeOffset(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTimeOffset(jprime.variable.SymbolVariable value);

	/**
	 * @return max  [ for each nic: (bits_out/bit_rate) ]
	 */
	public jprime.variable.FloatingPointNumberVariable getTrafficIntensity();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficIntensity(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficIntensity(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTrafficIntensity(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();

	/**
	  * Create a new child of type jprime.Interface.Interface and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface();

	/**
	  * jython method to create a a new child of type jprime.Interface.Interface, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Interface.Interface and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterface(String name);

	 /**
	  * Add a new child of type jprime.Interface.Interface.
	  */
	public void addInterface(jprime.Interface.Interface kid);

	 /**
	  * Create a new child of type jprime.Interface.InterfaceReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterfaceReplica(jprime.Interface.IInterface to_replicate);

	/**
	  * jython method to create replica new child of type jprime.Interface.InterfaceReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.Interface.IInterface replicateInterface(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.Interface.InterfaceReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.Interface.IInterface createInterfaceReplica(String name, jprime.Interface.IInterface to_replicate);

	/**
	  * return network interfaces on this host
	  */
	public jprime.util.ChildList<jprime.Interface.IInterface> getNics();
}
