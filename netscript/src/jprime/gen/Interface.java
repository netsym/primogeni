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
public abstract class Interface extends jprime.BaseInterface.BaseInterface implements jprime.gen.IInterface {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();
	getNic_queue().enforceChildConstraints();	getEmu_proto().enforceChildConstraints();
	}
	public Interface(PyObject[] v, String[] s){super(v,s);}
	public Interface(ModelNodeRecord rec){ super(rec); }
	public Interface(IModelNode parent){ super(parent); }
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
		jprime.Interface.InterfaceReplica c = new jprime.Interface.InterfaceReplica(this.getName(),(IModelNode)parent,(jprime.Interface.IInterface)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1007: //Interface
			case 1063: //InterfaceAlias
			case 1119: //InterfaceReplica
			case 1175: //InterfaceAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();
	public final static java.util.ArrayList<Integer> attrIds=new java.util.ArrayList<Integer>();
	static {
		attrIds.add(9);
		attrIds.add(62);
		attrIds.add(58);
		attrIds.add(31);
		attrIds.add(10);
		attrIds.add(79);
		attrIds.add(118);
		attrIds.add(55);
		attrIds.add(86);
		attrIds.add(85);
		attrIds.add(98);
		attrIds.add(13);
		attrIds.add(88);
		attrIds.add(87);
		attrIds.add(90);
		attrIds.add(89);
		attrIds.add(92);
		attrIds.add(91);
		attrIds.add(99);
		attrIds.add(14);
		attrIds.add(117);
	}

	/**
	 * @return transmit speed
	 */
	public jprime.variable.FloatingPointNumberVariable getBitRate() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.bit_rate());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBitRate(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bit_rate());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bit_rate(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bit_rate(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBitRate(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bit_rate());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bit_rate(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bit_rate(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBitRate(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.bit_rate());
		addAttr(value);
	}

	/**
	 * @return transmit latency
	 */
	public jprime.variable.FloatingPointNumberVariable getLatency() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.latency());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLatency(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.latency());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.latency(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.latency(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setLatency(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.latency());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.latency(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.latency(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setLatency(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.latency());
		addAttr(value);
	}

	/**
	 * @return jitter range
	 */
	public jprime.variable.FloatingPointNumberVariable getJitterRange() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.jitter_range());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setJitterRange(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.jitter_range());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.jitter_range(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.jitter_range(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setJitterRange(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.jitter_range());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.jitter_range(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.jitter_range(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setJitterRange(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.jitter_range());
		addAttr(value);
	}

	/**
	 * @return drop probability
	 */
	public jprime.variable.FloatingPointNumberVariable getDropProbability() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.drop_probability());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDropProbability(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.drop_probability());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.drop_probability(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.drop_probability(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDropProbability(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.drop_probability());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.drop_probability(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.drop_probability(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDropProbability(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.drop_probability());
		addAttr(value);
	}

	/**
	 * @return send buffer size
	 */
	public jprime.variable.IntegerVariable getBufferSize() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.buffer_size());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBufferSize(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.buffer_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.buffer_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.buffer_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBufferSize(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.buffer_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.buffer_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.buffer_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBufferSize(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.buffer_size());
		addAttr(value);
	}

	/**
	 * @return maximum transmission unit
	 */
	public jprime.variable.IntegerVariable getMtu() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.mtu());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMtu(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.mtu());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mtu(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mtu(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMtu(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.mtu());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mtu(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mtu(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMtu(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.mtu());
		addAttr(value);
	}

	/**
	 * @return The class to use for the queue.
	 */
	public jprime.variable.StringVariable getQueueType() {
		return (jprime.variable.StringVariable)getAttributeByName(ModelNodeVariable.queue_type());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueType(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.queue_type());
		if(temp==null){
			temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.queue_type(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.StringVariable)){
				temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.queue_type(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.StringVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQueueType(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.queue_type());
		addAttr(value);
	}

	/**
	 * @return is the interface on?
	 */
	public jprime.variable.BooleanVariable getIsOn() {
		return (jprime.variable.BooleanVariable)getAttributeByName(ModelNodeVariable.is_on());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsOn(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.is_on());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.is_on(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.is_on(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIsOn(boolean value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.is_on());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.is_on(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.is_on(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIsOn(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.is_on());
		addAttr(value);
	}

	/**
	 * @return number of packets received
	 */
	public jprime.variable.IntegerVariable getNumInPackets() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_in_packets());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInPackets(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInPackets(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInPackets(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_in_packets());
		addAttr(value);
	}

	/**
	 * @return number of bytes received
	 */
	public jprime.variable.IntegerVariable getNumInBytes() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_in_bytes());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInBytes(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInBytes(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInBytes(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_in_bytes());
		addAttr(value);
	}

	/**
	 * @return number of packets per second
	 */
	public jprime.variable.FloatingPointNumberVariable getPacketsInPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.packets_in_per_sec());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsInPerSec(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.packets_in_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_in_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_in_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsInPerSec(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.packets_in_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_in_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_in_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPacketsInPerSec(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.packets_in_per_sec());
		addAttr(value);
	}

	/**
	 * @return number of bytes per second
	 */
	public jprime.variable.FloatingPointNumberVariable getBytesInPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.bytes_in_per_sec());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesInPerSec(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bytes_in_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_in_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_in_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesInPerSec(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bytes_in_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_in_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_in_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesInPerSec(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.bytes_in_per_sec());
		addAttr(value);
	}

	/**
	 * @return number of unicast packets received
	 */
	public jprime.variable.IntegerVariable getNumInUcastPackets() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_in_ucast_packets());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastPackets(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_ucast_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastPackets(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_ucast_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInUcastPackets(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_in_ucast_packets());
		addAttr(value);
	}

	/**
	 * @return number of unicast bytes received
	 */
	public jprime.variable.IntegerVariable getNumInUcastBytes() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_in_ucast_bytes());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastBytes(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_ucast_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumInUcastBytes(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_in_ucast_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_in_ucast_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumInUcastBytes(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_in_ucast_bytes());
		addAttr(value);
	}

	/**
	 * @return number of packets sent
	 */
	public jprime.variable.IntegerVariable getNumOutPackets() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_out_packets());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutPackets(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutPackets(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutPackets(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_out_packets());
		addAttr(value);
	}

	/**
	 * @return number of bytes sent
	 */
	public jprime.variable.IntegerVariable getNumOutBytes() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_out_bytes());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutBytes(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutBytes(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutBytes(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_out_bytes());
		addAttr(value);
	}

	/**
	 * @return number of unicast packets sent
	 */
	public jprime.variable.IntegerVariable getNumOutUcastPackets() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_out_ucast_packets());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_ucast_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_ucast_packets());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_packets(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_packets(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutUcastPackets(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_out_ucast_packets());
		addAttr(value);
	}

	/**
	 * @return number of unicast bytes sent
	 */
	public jprime.variable.IntegerVariable getNumOutUcastBytes() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.num_out_ucast_bytes());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_ucast_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.num_out_ucast_bytes());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_bytes(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.num_out_ucast_bytes(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumOutUcastBytes(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.num_out_ucast_bytes());
		addAttr(value);
	}

	/**
	 * @return number of packets per second
	 */
	public jprime.variable.FloatingPointNumberVariable getPacketsOutPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.packets_out_per_sec());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.packets_out_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_out_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_out_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.packets_out_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_out_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.packets_out_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPacketsOutPerSec(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.packets_out_per_sec());
		addAttr(value);
	}

	/**
	 * @return number of bytes per second
	 */
	public jprime.variable.FloatingPointNumberVariable getBytesOutPerSec() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.bytes_out_per_sec());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesOutPerSec(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bytes_out_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_out_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_out_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesOutPerSec(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.bytes_out_per_sec());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_out_per_sec(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.bytes_out_per_sec(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesOutPerSec(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.bytes_out_per_sec());
		addAttr(value);
	}

	/**
	 * @return quesize
	 */
	public jprime.variable.IntegerVariable getQueueSize() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.queue_size());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueSize(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.queue_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.queue_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.queue_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setQueueSize(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.queue_size());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.queue_size(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.queue_size(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setQueueSize(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.queue_size());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return attrIds;
	}

	/**
	  * Create a new child of type jprime.NicQueue.NicQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue() {
		return createNicQueue(null);
	}

	/**
	  * jython method to create a a new child of type jprime.NicQueue.NicQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.NicQueue.NicQueueReplica temp = new jprime.NicQueue.NicQueueReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.NicQueue.NicQueue temp = new jprime.NicQueue.NicQueue(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueue(String name) {
		jprime.NicQueue.NicQueue temp = new jprime.NicQueue.NicQueue(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.NicQueue.NicQueue.
	  */
	public void addNicQueue(jprime.NicQueue.NicQueue kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueueReplica(jprime.NicQueue.INicQueue to_replicate) {
		jprime.NicQueue.NicQueueReplica temp = new jprime.NicQueue.NicQueueReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.NicQueue.NicQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue replicateNicQueue(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.NicQueue.NicQueueReplica temp = new jprime.NicQueue.NicQueueReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.NicQueue.NicQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.NicQueue.INicQueue createNicQueueReplica(String name, jprime.NicQueue.INicQueue to_replicate) {
		jprime.NicQueue.NicQueueReplica temp = new jprime.NicQueue.NicQueueReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return The queue for this nic
	  */
	public jprime.util.ChildList<jprime.NicQueue.INicQueue> getNic_queue() {
		return new jprime.util.ChildList<jprime.NicQueue.INicQueue>(this, 1010, 0, 1);
	}

	/**
	  * Create a new child of type jprime.FluidQueue.FluidQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue() {
		return createFluidQueue(null);
	}

	/**
	  * jython method to create a a new child of type jprime.FluidQueue.FluidQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.FluidQueue.FluidQueueReplica temp = new jprime.FluidQueue.FluidQueueReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.FluidQueue.FluidQueue temp = new jprime.FluidQueue.FluidQueue(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueue(String name) {
		jprime.FluidQueue.FluidQueue temp = new jprime.FluidQueue.FluidQueue(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.FluidQueue.FluidQueue.
	  */
	public void addFluidQueue(jprime.FluidQueue.FluidQueue kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueueReplica(jprime.FluidQueue.IFluidQueue to_replicate) {
		jprime.FluidQueue.FluidQueueReplica temp = new jprime.FluidQueue.FluidQueueReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.FluidQueue.FluidQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue replicateFluidQueue(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.FluidQueue.FluidQueueReplica temp = new jprime.FluidQueue.FluidQueueReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.FluidQueue.FluidQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.FluidQueue.IFluidQueue createFluidQueueReplica(String name, jprime.FluidQueue.IFluidQueue to_replicate) {
		jprime.FluidQueue.FluidQueueReplica temp = new jprime.FluidQueue.FluidQueueReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue() {
		return createDropTailQueue(null);
	}

	/**
	  * jython method to create a a new child of type jprime.DropTailQueue.DropTailQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.DropTailQueue.DropTailQueueReplica temp = new jprime.DropTailQueue.DropTailQueueReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.DropTailQueue.DropTailQueue temp = new jprime.DropTailQueue.DropTailQueue(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueue(String name) {
		jprime.DropTailQueue.DropTailQueue temp = new jprime.DropTailQueue.DropTailQueue(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.DropTailQueue.DropTailQueue.
	  */
	public void addDropTailQueue(jprime.DropTailQueue.DropTailQueue kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueueReplica(jprime.DropTailQueue.IDropTailQueue to_replicate) {
		jprime.DropTailQueue.DropTailQueueReplica temp = new jprime.DropTailQueue.DropTailQueueReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.DropTailQueue.DropTailQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue replicateDropTailQueue(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.DropTailQueue.DropTailQueueReplica temp = new jprime.DropTailQueue.DropTailQueueReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.DropTailQueue.DropTailQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.DropTailQueue.IDropTailQueue createDropTailQueueReplica(String name, jprime.DropTailQueue.IDropTailQueue to_replicate) {
		jprime.DropTailQueue.DropTailQueueReplica temp = new jprime.DropTailQueue.DropTailQueueReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.RedQueue.RedQueue and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue() {
		return createRedQueue(null);
	}

	/**
	  * jython method to create a a new child of type jprime.RedQueue.RedQueue, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RedQueue.RedQueueReplica temp = new jprime.RedQueue.RedQueueReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.RedQueue.RedQueue temp = new jprime.RedQueue.RedQueue(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueue and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueue(String name) {
		jprime.RedQueue.RedQueue temp = new jprime.RedQueue.RedQueue(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.RedQueue.RedQueue.
	  */
	public void addRedQueue(jprime.RedQueue.RedQueue kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueueReplica(jprime.RedQueue.IRedQueue to_replicate) {
		jprime.RedQueue.RedQueueReplica temp = new jprime.RedQueue.RedQueueReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.RedQueue.RedQueueReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue replicateRedQueue(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.RedQueue.RedQueueReplica temp = new jprime.RedQueue.RedQueueReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.RedQueue.RedQueueReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.RedQueue.IRedQueue createRedQueueReplica(String name, jprime.RedQueue.IRedQueue to_replicate) {
		jprime.RedQueue.RedQueueReplica temp = new jprime.RedQueue.RedQueueReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocol and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol() {
		return createEmulationProtocol(null);
	}

	/**
	  * jython method to create a a new child of type jprime.EmulationProtocol.EmulationProtocol, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.EmulationProtocol.EmulationProtocolReplica temp = new jprime.EmulationProtocol.EmulationProtocolReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.EmulationProtocol.EmulationProtocol temp = new jprime.EmulationProtocol.EmulationProtocol(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocol and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocol(String name) {
		jprime.EmulationProtocol.EmulationProtocol temp = new jprime.EmulationProtocol.EmulationProtocol(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.EmulationProtocol.EmulationProtocol.
	  */
	public void addEmulationProtocol(jprime.EmulationProtocol.EmulationProtocol kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocolReplica(jprime.EmulationProtocol.IEmulationProtocol to_replicate) {
		jprime.EmulationProtocol.EmulationProtocolReplica temp = new jprime.EmulationProtocol.EmulationProtocolReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol replicateEmulationProtocol(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.EmulationProtocol.EmulationProtocolReplica temp = new jprime.EmulationProtocol.EmulationProtocolReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.EmulationProtocol.EmulationProtocolReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.EmulationProtocol.IEmulationProtocol createEmulationProtocolReplica(String name, jprime.EmulationProtocol.IEmulationProtocol to_replicate) {
		jprime.EmulationProtocol.EmulationProtocolReplica temp = new jprime.EmulationProtocol.EmulationProtocolReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * return If this interface is emulated then this protcol must be set
	  */
	public jprime.util.ChildList<jprime.EmulationProtocol.IEmulationProtocol> getEmu_proto() {
		return new jprime.util.ChildList<jprime.EmulationProtocol.IEmulationProtocol>(this, 1001, 0, 1);
	}

	/**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortal and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal() {
		return createTrafficPortal(null);
	}

	/**
	  * jython method to create a a new child of type jprime.TrafficPortal.TrafficPortal, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TrafficPortal.TrafficPortalReplica temp = new jprime.TrafficPortal.TrafficPortalReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TrafficPortal.TrafficPortal temp = new jprime.TrafficPortal.TrafficPortal(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortal and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortal(String name) {
		jprime.TrafficPortal.TrafficPortal temp = new jprime.TrafficPortal.TrafficPortal(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.TrafficPortal.TrafficPortal.
	  */
	public void addTrafficPortal(jprime.TrafficPortal.TrafficPortal kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortalReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortalReplica(jprime.TrafficPortal.ITrafficPortal to_replicate) {
		jprime.TrafficPortal.TrafficPortalReplica temp = new jprime.TrafficPortal.TrafficPortalReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.TrafficPortal.TrafficPortalReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal replicateTrafficPortal(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.TrafficPortal.TrafficPortalReplica temp = new jprime.TrafficPortal.TrafficPortalReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.TrafficPortal.TrafficPortalReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TrafficPortal.ITrafficPortal createTrafficPortalReplica(String name, jprime.TrafficPortal.ITrafficPortal to_replicate) {
		jprime.TrafficPortal.TrafficPortalReplica temp = new jprime.TrafficPortal.TrafficPortalReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulation and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation() {
		return createTAPEmulation(null);
	}

	/**
	  * jython method to create a a new child of type jprime.TAPEmulation.TAPEmulation, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TAPEmulation.TAPEmulationReplica temp = new jprime.TAPEmulation.TAPEmulationReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.TAPEmulation.TAPEmulation temp = new jprime.TAPEmulation.TAPEmulation(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulation and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulation(String name) {
		jprime.TAPEmulation.TAPEmulation temp = new jprime.TAPEmulation.TAPEmulation(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.TAPEmulation.TAPEmulation.
	  */
	public void addTAPEmulation(jprime.TAPEmulation.TAPEmulation kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulationReplica(jprime.TAPEmulation.ITAPEmulation to_replicate) {
		jprime.TAPEmulation.TAPEmulationReplica temp = new jprime.TAPEmulation.TAPEmulationReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.TAPEmulation.TAPEmulationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation replicateTAPEmulation(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.TAPEmulation.TAPEmulationReplica temp = new jprime.TAPEmulation.TAPEmulationReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.TAPEmulation.TAPEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.TAPEmulation.ITAPEmulation createTAPEmulationReplica(String name, jprime.TAPEmulation.ITAPEmulation to_replicate) {
		jprime.TAPEmulation.TAPEmulationReplica temp = new jprime.TAPEmulation.TAPEmulationReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation() {
		return createOpenVPNEmulation(null);
	}

	/**
	  * jython method to create a a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation, and add it as a child to this node.
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation(PyObject[] v, String[] n) {
		if(jprime.ModelNode.__hasReplicate(v,n,false)) {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.OpenVPNEmulation.OpenVPNEmulationReplica temp = new jprime.OpenVPNEmulation.OpenVPNEmulationReplica(v1,n);
			addChild(temp);
			return temp;
		} else {
			PyObject[] v1 = new PyObject[v.length+1];
			for(int i=0;i<v.length;i++)v1[i+1]=v[i];
			v1[0]=Py.java2py(this);
			jprime.OpenVPNEmulation.OpenVPNEmulation temp = new jprime.OpenVPNEmulation.OpenVPNEmulation(v1,n);
			addChild(temp);
			return temp;
		}
	}

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulation(String name) {
		jprime.OpenVPNEmulation.OpenVPNEmulation temp = new jprime.OpenVPNEmulation.OpenVPNEmulation(this);
		temp.setName(name);
		addChild(temp);
		return temp;
	}

	 /**
	  * Add a new child of type jprime.OpenVPNEmulation.OpenVPNEmulation.
	  */
	public void addOpenVPNEmulation(jprime.OpenVPNEmulation.OpenVPNEmulation kid) {
		addChild(kid);
	}

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulationReplica(jprime.OpenVPNEmulation.IOpenVPNEmulation to_replicate) {
		jprime.OpenVPNEmulation.OpenVPNEmulationReplica temp = new jprime.OpenVPNEmulation.OpenVPNEmulationReplica(null, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	  * jython method to create replica new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which points to to_alias, and add it as a child to this node.
	  * @param to_alias the node to point to
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation replicateOpenVPNEmulation(PyObject[] v, String[] n) {
		PyObject[] v1 = new PyObject[v.length+1];
		for(int i=0;i<v.length;i++)v1[i+1]=v[i];
		v1[0]=Py.java2py(this);
		jprime.OpenVPNEmulation.OpenVPNEmulationReplica temp = new jprime.OpenVPNEmulation.OpenVPNEmulationReplica(v1,n);
		addChild(temp);
		return temp;
	}

	 /**
	  * Create a new child of type jprime.OpenVPNEmulation.OpenVPNEmulationReplica, which is a deep-lightweight copy of to_replicate, and add it as a child to this node.
	  * @param name the name to assign the new node
	  * @param to_replicate the node which is to be deep copied
	  * @return the new child
	  */
	public jprime.OpenVPNEmulation.IOpenVPNEmulation createOpenVPNEmulationReplica(String name, jprime.OpenVPNEmulation.IOpenVPNEmulation to_replicate) {
		jprime.OpenVPNEmulation.OpenVPNEmulationReplica temp = new jprime.OpenVPNEmulation.OpenVPNEmulationReplica(name, this,to_replicate);
		addChild(temp);
		return temp;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
