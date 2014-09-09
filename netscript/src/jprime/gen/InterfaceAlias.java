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
public abstract class InterfaceAlias extends jprime.BaseInterface.BaseInterfaceAlias implements jprime.gen.IInterfaceAlias {
	public InterfaceAlias(IModelNode parent, jprime.Interface.IInterface referencedNode) {
		super(parent,(jprime.Interface.IInterface)referencedNode);
	}
	public InterfaceAlias(ModelNodeRecord rec){ super(rec); }
	public InterfaceAlias(PyObject[] v, String[] s){super(v,s);}
	public InterfaceAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.Interface.IInterface.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.Interface.InterfaceAliasReplica c = new jprime.Interface.InterfaceAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1063: //InterfaceAlias
			case 1175: //InterfaceAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return transmit speed
	 */
	public jprime.variable.FloatingPointNumberVariable getBitRate() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getBitRate();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBitRate(String value) {
		((IInterface)deference()).setBitRate(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBitRate(float value) {
		((IInterface)deference()).setBitRate(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBitRate(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setBitRate(value);
	}

	/**
	 * @return transmit latency
	 */
	public jprime.variable.FloatingPointNumberVariable getLatency() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getLatency();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLatency(String value) {
		((IInterface)deference()).setLatency(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLatency(float value) {
		((IInterface)deference()).setLatency(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setLatency(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setLatency(value);
	}

	/**
	 * @return jitter range
	 */
	public jprime.variable.FloatingPointNumberVariable getJitterRange() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getJitterRange();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setJitterRange(String value) {
		((IInterface)deference()).setJitterRange(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setJitterRange(float value) {
		((IInterface)deference()).setJitterRange(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setJitterRange(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setJitterRange(value);
	}

	/**
	 * @return drop probability
	 */
	public jprime.variable.FloatingPointNumberVariable getDropProbability() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getDropProbability();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDropProbability(String value) {
		((IInterface)deference()).setDropProbability(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDropProbability(float value) {
		((IInterface)deference()).setDropProbability(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDropProbability(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setDropProbability(value);
	}

	/**
	 * @return send buffer size
	 */
	public jprime.variable.IntegerVariable getBufferSize() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getBufferSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBufferSize(String value) {
		((IInterface)deference()).setBufferSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBufferSize(long value) {
		((IInterface)deference()).setBufferSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBufferSize(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setBufferSize(value);
	}

	/**
	 * @return maximum transmission unit
	 */
	public jprime.variable.IntegerVariable getMtu() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getMtu();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMtu(String value) {
		((IInterface)deference()).setMtu(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMtu(long value) {
		((IInterface)deference()).setMtu(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMtu(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setMtu(value);
	}

	/**
	 * @return The class to use for the queue.
	 */
	public jprime.variable.StringVariable getQueueType() {
		return (jprime.variable.StringVariable)((IInterface)deference()).getQueueType();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueType(String value) {
		((IInterface)deference()).setQueueType(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQueueType(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setQueueType(value);
	}

	/**
	 * @return is the interface on?
	 */
	public jprime.variable.BooleanVariable getIsOn() {
		return (jprime.variable.BooleanVariable)((IInterface)deference()).getIsOn();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsOn(String value) {
		((IInterface)deference()).setIsOn(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsOn(boolean value) {
		((IInterface)deference()).setIsOn(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIsOn(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setIsOn(value);
	}

	/**
	 * @return number of packets received
	 */
	public jprime.variable.IntegerVariable getNumInPackets() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumInPackets();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInPackets(String value) {
		((IInterface)deference()).setNumInPackets(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInPackets(long value) {
		((IInterface)deference()).setNumInPackets(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInPackets(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumInPackets(value);
	}

	/**
	 * @return number of bytes received
	 */
	public jprime.variable.IntegerVariable getNumInBytes() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumInBytes();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInBytes(String value) {
		((IInterface)deference()).setNumInBytes(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInBytes(long value) {
		((IInterface)deference()).setNumInBytes(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInBytes(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumInBytes(value);
	}

	/**
	 * @return number of packets per second
	 */
	public jprime.variable.FloatingPointNumberVariable getPacketsInPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getPacketsInPerSec();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsInPerSec(String value) {
		((IInterface)deference()).setPacketsInPerSec(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsInPerSec(float value) {
		((IInterface)deference()).setPacketsInPerSec(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPacketsInPerSec(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setPacketsInPerSec(value);
	}

	/**
	 * @return number of bytes per second
	 */
	public jprime.variable.FloatingPointNumberVariable getBytesInPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getBytesInPerSec();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesInPerSec(String value) {
		((IInterface)deference()).setBytesInPerSec(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesInPerSec(float value) {
		((IInterface)deference()).setBytesInPerSec(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesInPerSec(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setBytesInPerSec(value);
	}

	/**
	 * @return number of unicast packets received
	 */
	public jprime.variable.IntegerVariable getNumInUcastPackets() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumInUcastPackets();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastPackets(String value) {
		((IInterface)deference()).setNumInUcastPackets(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastPackets(long value) {
		((IInterface)deference()).setNumInUcastPackets(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInUcastPackets(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumInUcastPackets(value);
	}

	/**
	 * @return number of unicast bytes received
	 */
	public jprime.variable.IntegerVariable getNumInUcastBytes() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumInUcastBytes();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastBytes(String value) {
		((IInterface)deference()).setNumInUcastBytes(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastBytes(long value) {
		((IInterface)deference()).setNumInUcastBytes(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInUcastBytes(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumInUcastBytes(value);
	}

	/**
	 * @return number of packets sent
	 */
	public jprime.variable.IntegerVariable getNumOutPackets() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumOutPackets();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutPackets(String value) {
		((IInterface)deference()).setNumOutPackets(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutPackets(long value) {
		((IInterface)deference()).setNumOutPackets(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutPackets(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumOutPackets(value);
	}

	/**
	 * @return number of bytes sent
	 */
	public jprime.variable.IntegerVariable getNumOutBytes() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumOutBytes();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutBytes(String value) {
		((IInterface)deference()).setNumOutBytes(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutBytes(long value) {
		((IInterface)deference()).setNumOutBytes(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutBytes(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumOutBytes(value);
	}

	/**
	 * @return number of unicast packets sent
	 */
	public jprime.variable.IntegerVariable getNumOutUcastPackets() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumOutUcastPackets();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(String value) {
		((IInterface)deference()).setNumOutUcastPackets(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(long value) {
		((IInterface)deference()).setNumOutUcastPackets(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumOutUcastPackets(value);
	}

	/**
	 * @return number of unicast bytes sent
	 */
	public jprime.variable.IntegerVariable getNumOutUcastBytes() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getNumOutUcastBytes();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(String value) {
		((IInterface)deference()).setNumOutUcastBytes(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(long value) {
		((IInterface)deference()).setNumOutUcastBytes(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setNumOutUcastBytes(value);
	}

	/**
	 * @return number of packets per second
	 */
	public jprime.variable.FloatingPointNumberVariable getPacketsOutPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getPacketsOutPerSec();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(String value) {
		((IInterface)deference()).setPacketsOutPerSec(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(float value) {
		((IInterface)deference()).setPacketsOutPerSec(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setPacketsOutPerSec(value);
	}

	/**
	 * @return number of bytes per second
	 */
	public jprime.variable.FloatingPointNumberVariable getBytesOutPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)((IInterface)deference()).getBytesOutPerSec();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesOutPerSec(String value) {
		((IInterface)deference()).setBytesOutPerSec(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesOutPerSec(float value) {
		((IInterface)deference()).setBytesOutPerSec(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesOutPerSec(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setBytesOutPerSec(value);
	}

	/**
	 * @return quesize
	 */
	public jprime.variable.IntegerVariable getQueueSize() {
		return (jprime.variable.IntegerVariable)((IInterface)deference()).getQueueSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueSize(String value) {
		((IInterface)deference()).setQueueSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueSize(long value) {
		((IInterface)deference()).setQueueSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQueueSize(jprime.variable.SymbolVariable value) {
		((IInterface)deference()).setQueueSize(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.Interface.attrIds;
	}

	/**
	  * Create a new child of type jprime.NicQueue.NicQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.NicQueue.NicQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.NicQueue.NicQueue.
	  */
	public void addNicQueue(jprime.NicQueue.NicQueue kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueueReplica(jprime.NicQueue.INicQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.NicQueue.NicQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue replicateNicQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueueReplica(String name, jprime.NicQueue.INicQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return The queue for this nic
	  */
	public jprime.util.ChildList<jprime.NicQueue.INicQueue> getNic_queue() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.FluidQueue.FluidQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.FluidQueue.FluidQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.FluidQueue.FluidQueue.
	  */
	public void addFluidQueue(jprime.FluidQueue.FluidQueue kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueueReplica(jprime.FluidQueue.IFluidQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.FluidQueue.FluidQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue replicateFluidQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueueReplica(String name, jprime.FluidQueue.IFluidQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.DropTailQueue.DropTailQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.DropTailQueue.DropTailQueue.
	  */
	public void addDropTailQueue(jprime.DropTailQueue.DropTailQueue kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueueReplica(jprime.DropTailQueue.IDropTailQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.DropTailQueue.DropTailQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue replicateDropTailQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueueReplica(String name, jprime.DropTailQueue.IDropTailQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.RedQueue.RedQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.RedQueue.RedQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.RedQueue.RedQueue.
	  */
	public void addRedQueue(jprime.RedQueue.RedQueue kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueueReplica(jprime.RedQueue.IRedQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.RedQueue.RedQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue replicateRedQueue(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueueReplica(String name, jprime.RedQueue.IRedQueue to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocol and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.EmulationProtocol.EmulationProtocol, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocol and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.EmulationProtocol.EmulationProtocol.
	  */
	public void addEmulationProtocol(jprime.EmulationProtocol.EmulationProtocol kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocolReplica(jprime.EmulationProtocol.IEmulationProtocol to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol replicateEmulationProtocol(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocolReplica(String name, jprime.EmulationProtocol.IEmulationProtocol to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * return If this interface is emulated then this protcol must be set
	  */
	public jprime.util.ChildList<jprime.EmulationProtocol.IEmulationProtocol> getEmu_proto() {
		throw new RuntimeException("Aliases do not have children!");
	}

	/**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortal and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.TrafficPortal.TrafficPortal, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortal and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.TrafficPortal.TrafficPortal.
	  */
	public void addTrafficPortal(jprime.TrafficPortal.TrafficPortal kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortalReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortalReplica(jprime.TrafficPortal.ITrafficPortal to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.TrafficPortal.TrafficPortalReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal replicateTrafficPortal(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortalReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortalReplica(String name, jprime.TrafficPortal.ITrafficPortal to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulation and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.TAPEmulation.TAPEmulation, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulation and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.TAPEmulation.TAPEmulation.
	  */
	public void addTAPEmulation(jprime.TAPEmulation.TAPEmulation kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulationReplica(jprime.TAPEmulation.ITAPEmulation to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.TAPEmulation.TAPEmulationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation replicateTAPEmulation(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulationReplica(String name, jprime.TAPEmulation.ITAPEmulation to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation() {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create a a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation(String name) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Add a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation.
	  */
	public void addOpenVPNEmulation(jprime.OpenVPNEmulation.OpenVPNEmulation kid) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulationReplica(jprime.OpenVPNEmulation.IOpenVPNEmulation to_replicate) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	/**
	  * jython method to create replica new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation replicateOpenVPNEmulation(PyObject[] v, String[] n) {
		throw new RuntimeException("Cannot add children to aliases!");
	}

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulationReplica(String name, jprime.OpenVPNEmulation.IOpenVPNEmulation to_replicate) {
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
