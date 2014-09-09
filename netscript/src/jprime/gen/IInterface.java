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
public interface IInterface extends jprime.BaseInterface.IBaseInterface {

	/**
	 * @return transmit speed
	 */
	public jprime.variable.FloatingPointNumberVariable getBitRate();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBitRate(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBitRate(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBitRate(jprime.variable.SymbolVariable value);

	/**
	 * @return transmit latency
	 */
	public jprime.variable.FloatingPointNumberVariable getLatency();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLatency(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLatency(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setLatency(jprime.variable.SymbolVariable value);

	/**
	 * @return jitter range
	 */
	public jprime.variable.FloatingPointNumberVariable getJitterRange();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setJitterRange(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setJitterRange(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setJitterRange(jprime.variable.SymbolVariable value);

	/**
	 * @return drop probability
	 */
	public jprime.variable.FloatingPointNumberVariable getDropProbability();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDropProbability(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDropProbability(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDropProbability(jprime.variable.SymbolVariable value);

	/**
	 * @return send buffer size
	 */
	public jprime.variable.IntegerVariable getBufferSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBufferSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBufferSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBufferSize(jprime.variable.SymbolVariable value);

	/**
	 * @return maximum transmission unit
	 */
	public jprime.variable.IntegerVariable getMtu();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMtu(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMtu(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMtu(jprime.variable.SymbolVariable value);

	/**
	 * @return The class to use for the queue.
	 */
	public jprime.variable.StringVariable getQueueType();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueType(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQueueType(jprime.variable.SymbolVariable value);

	/**
	 * @return is the interface on?
	 */
	public jprime.variable.BooleanVariable getIsOn();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsOn(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsOn(boolean value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIsOn(jprime.variable.SymbolVariable value);

	/**
	 * @return number of packets received
	 */
	public jprime.variable.IntegerVariable getNumInPackets();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInPackets(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInPackets(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInPackets(jprime.variable.SymbolVariable value);

	/**
	 * @return number of bytes received
	 */
	public jprime.variable.IntegerVariable getNumInBytes();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInBytes(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInBytes(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInBytes(jprime.variable.SymbolVariable value);

	/**
	 * @return number of packets per second
	 */
	public jprime.variable.FloatingPointNumberVariable getPacketsInPerSec();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsInPerSec(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsInPerSec(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPacketsInPerSec(jprime.variable.SymbolVariable value);

	/**
	 * @return number of bytes per second
	 */
	public jprime.variable.FloatingPointNumberVariable getBytesInPerSec();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesInPerSec(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesInPerSec(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesInPerSec(jprime.variable.SymbolVariable value);

	/**
	 * @return number of unicast packets received
	 */
	public jprime.variable.IntegerVariable getNumInUcastPackets();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastPackets(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastPackets(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInUcastPackets(jprime.variable.SymbolVariable value);

	/**
	 * @return number of unicast bytes received
	 */
	public jprime.variable.IntegerVariable getNumInUcastBytes();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastBytes(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastBytes(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInUcastBytes(jprime.variable.SymbolVariable value);

	/**
	 * @return number of packets sent
	 */
	public jprime.variable.IntegerVariable getNumOutPackets();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutPackets(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutPackets(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutPackets(jprime.variable.SymbolVariable value);

	/**
	 * @return number of bytes sent
	 */
	public jprime.variable.IntegerVariable getNumOutBytes();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutBytes(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutBytes(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutBytes(jprime.variable.SymbolVariable value);

	/**
	 * @return number of unicast packets sent
	 */
	public jprime.variable.IntegerVariable getNumOutUcastPackets();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(jprime.variable.SymbolVariable value);

	/**
	 * @return number of unicast bytes sent
	 */
	public jprime.variable.IntegerVariable getNumOutUcastBytes();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(jprime.variable.SymbolVariable value);

	/**
	 * @return number of packets per second
	 */
	public jprime.variable.FloatingPointNumberVariable getPacketsOutPerSec();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(jprime.variable.SymbolVariable value);

	/**
	 * @return number of bytes per second
	 */
	public jprime.variable.FloatingPointNumberVariable getBytesOutPerSec();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesOutPerSec(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesOutPerSec(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesOutPerSec(jprime.variable.SymbolVariable value);

	/**
	 * @return quesize
	 */
	public jprime.variable.IntegerVariable getQueueSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQueueSize(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();

	/**
	  * Create a new child of type jprime.NicQueue.NicQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue();

	/**
	  * jython method to create a a new child of type jprime.NicQueue.NicQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue(String name);

	 /**
	  * Add a new child of type jprime.NicQueue.NicQueue.
	  */
	public void addNicQueue(jprime.NicQueue.NicQueue kid);

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueueReplica(jprime.NicQueue.INicQueue to_replicate);

	/**
	  * jython method to create replica new child of type jprime.NicQueue.NicQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue replicateNicQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueueReplica(String name, jprime.NicQueue.INicQueue to_replicate);

	/**
	  * return The queue for this nic
	  */
	public jprime.util.ChildList<jprime.NicQueue.INicQueue> getNic_queue();

	/**
	  * Create a new child of type jprime.FluidQueue.FluidQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue();

	/**
	  * jython method to create a a new child of type jprime.FluidQueue.FluidQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue(String name);

	 /**
	  * Add a new child of type jprime.FluidQueue.FluidQueue.
	  */
	public void addFluidQueue(jprime.FluidQueue.FluidQueue kid);

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueueReplica(jprime.FluidQueue.IFluidQueue to_replicate);

	/**
	  * jython method to create replica new child of type jprime.FluidQueue.FluidQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue replicateFluidQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueueReplica(String name, jprime.FluidQueue.IFluidQueue to_replicate);

	/**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue();

	/**
	  * jython method to create a a new child of type jprime.DropTailQueue.DropTailQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue(String name);

	 /**
	  * Add a new child of type jprime.DropTailQueue.DropTailQueue.
	  */
	public void addDropTailQueue(jprime.DropTailQueue.DropTailQueue kid);

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueueReplica(jprime.DropTailQueue.IDropTailQueue to_replicate);

	/**
	  * jython method to create replica new child of type jprime.DropTailQueue.DropTailQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue replicateDropTailQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueueReplica(String name, jprime.DropTailQueue.IDropTailQueue to_replicate);

	/**
	  * Create a new child of type jprime.RedQueue.RedQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue();

	/**
	  * jython method to create a a new child of type jprime.RedQueue.RedQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue(String name);

	 /**
	  * Add a new child of type jprime.RedQueue.RedQueue.
	  */
	public void addRedQueue(jprime.RedQueue.RedQueue kid);

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueueReplica(jprime.RedQueue.IRedQueue to_replicate);

	/**
	  * jython method to create replica new child of type jprime.RedQueue.RedQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue replicateRedQueue(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueueReplica(String name, jprime.RedQueue.IRedQueue to_replicate);

	/**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocol and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol();

	/**
	  * jython method to create a a new child of type jprime.EmulationProtocol.EmulationProtocol, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocol and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol(String name);

	 /**
	  * Add a new child of type jprime.EmulationProtocol.EmulationProtocol.
	  */
	public void addEmulationProtocol(jprime.EmulationProtocol.EmulationProtocol kid);

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocolReplica(jprime.EmulationProtocol.IEmulationProtocol to_replicate);

	/**
	  * jython method to create replica new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol replicateEmulationProtocol(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocolReplica(String name, jprime.EmulationProtocol.IEmulationProtocol to_replicate);

	/**
	  * return If this interface is emulated then this protcol must be set
	  */
	public jprime.util.ChildList<jprime.EmulationProtocol.IEmulationProtocol> getEmu_proto();

	/**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortal and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal();

	/**
	  * jython method to create a a new child of type jprime.TrafficPortal.TrafficPortal, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortal and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal(String name);

	 /**
	  * Add a new child of type jprime.TrafficPortal.TrafficPortal.
	  */
	public void addTrafficPortal(jprime.TrafficPortal.TrafficPortal kid);

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortalReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortalReplica(jprime.TrafficPortal.ITrafficPortal to_replicate);

	/**
	  * jython method to create replica new child of type jprime.TrafficPortal.TrafficPortalReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal replicateTrafficPortal(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortalReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortalReplica(String name, jprime.TrafficPortal.ITrafficPortal to_replicate);

	/**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulation and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation();

	/**
	  * jython method to create a a new child of type jprime.TAPEmulation.TAPEmulation, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulation and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation(String name);

	 /**
	  * Add a new child of type jprime.TAPEmulation.TAPEmulation.
	  */
	public void addTAPEmulation(jprime.TAPEmulation.TAPEmulation kid);

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulationReplica(jprime.TAPEmulation.ITAPEmulation to_replicate);

	/**
	  * jython method to create replica new child of type jprime.TAPEmulation.TAPEmulationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation replicateTAPEmulation(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulationReplica(String name, jprime.TAPEmulation.ITAPEmulation to_replicate);

	/**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation();

	/**
	  * jython method to create a a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation(String name);

	 /**
	  * Add a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation.
	  */
	public void addOpenVPNEmulation(jprime.OpenVPNEmulation.OpenVPNEmulation kid);

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulationReplica(jprime.OpenVPNEmulation.IOpenVPNEmulation to_replicate);

	/**
	  * jython method to create replica new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation replicateOpenVPNEmulation(PyObject[] v, String[] n);

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulationReplica(String name, jprime.OpenVPNEmulation.IOpenVPNEmulation to_replicate);
}
