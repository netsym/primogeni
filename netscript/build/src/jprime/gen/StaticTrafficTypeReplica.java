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
public abstract class StaticTrafficTypeReplica extends jprime.TrafficType.TrafficTypeReplica implements jprime.gen.IStaticTrafficType {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public StaticTrafficTypeReplica(String name, IModelNode parent, jprime.StaticTrafficType.IStaticTrafficType referencedNode) {
		super(name,parent,referencedNode);
	}
	public StaticTrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public StaticTrafficTypeReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.StaticTrafficType.IStaticTrafficType.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.StaticTrafficType.StaticTrafficTypeReplica c = new jprime.StaticTrafficType.StaticTrafficTypeReplica(this.getName(), (IModelNode)parent,(jprime.StaticTrafficType.IStaticTrafficType)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1136: //StaticTrafficTypeReplica
			case 1137: //CNFTrafficReplica
			case 1138: //UDPTrafficReplica
			case 1139: //PPBPTrafficReplica
			case 1140: //TCPTrafficReplica
			case 1141: //ICMPTrafficReplica
			case 1142: //PingTrafficReplica
			case 1192: //StaticTrafficTypeAliasReplica
			case 1193: //CNFTrafficAliasReplica
			case 1194: //UDPTrafficAliasReplica
			case 1195: //PPBPTrafficAliasReplica
			case 1196: //TCPTrafficAliasReplica
			case 1197: //ICMPTrafficAliasReplica
			case 1198: //PingTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getSrcs() {
		ResourceIdentifierVariable temp = (ResourceIdentifierVariable)getAttributeByName(ModelNodeVariable.srcs());
		if(null!=temp) return temp;
		return (ResourceIdentifierVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.srcs());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.srcs());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(jprime.ResourceIdentifier.ResourceID value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.srcs());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSrcs(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.srcs());
		addAttr(value);
	}

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getDsts() {
		ResourceIdentifierVariable temp = (ResourceIdentifierVariable)getAttributeByName(ModelNodeVariable.dsts());
		if(null!=temp) return temp;
		return (ResourceIdentifierVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.dsts());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.dsts());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(jprime.ResourceIdentifier.ResourceID value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.dsts());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDsts(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.dsts());
		addAttr(value);
	}

	/**
	 * @return a list of emu/real nodes ips
	 */
	public jprime.variable.OpaqueVariable getDstIps() {
		jprime.variable.OpaqueVariable temp = (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.dst_ips());
		if(null!=temp) return temp;
		return (jprime.variable.OpaqueVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.dst_ips());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstIps(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.dst_ips());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.dst_ips(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.dst_ips(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstIps(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.dst_ips());
		addAttr(value);
	}

	/**
	 * @return Time before the traffic starts (default: 0).
	 */
	public jprime.variable.FloatingPointNumberVariable getStartTime() {
		jprime.variable.FloatingPointNumberVariable temp = (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.start_time());
		if(null!=temp) return temp;
		return (jprime.variable.FloatingPointNumberVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.start_time());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStartTime(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.start_time());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start_time(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start_time(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStartTime(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.start_time());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start_time(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start_time(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStartTime(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.start_time());
		addAttr(value);
	}

	/**
	 * @return The time between sending each traffic (default: 0.1). 
	 */
	public jprime.variable.FloatingPointNumberVariable getInterval() {
		jprime.variable.FloatingPointNumberVariable temp = (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.interval());
		if(null!=temp) return temp;
		return (jprime.variable.FloatingPointNumberVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.interval());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setInterval(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.interval());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.interval(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.interval(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setInterval(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.interval());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.interval(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.interval(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setInterval(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.interval());
		addAttr(value);
	}

	/**
	 * @return Whether to use exponential interval (default: false).
	 */
	public jprime.variable.BooleanVariable getIntervalExponential() {
		jprime.variable.BooleanVariable temp = (jprime.variable.BooleanVariable)getAttributeByName(ModelNodeVariable.interval_exponential());
		if(null!=temp) return temp;
		return (jprime.variable.BooleanVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.interval_exponential());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIntervalExponential(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.interval_exponential());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.interval_exponential(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.interval_exponential(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIntervalExponential(boolean value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.interval_exponential());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.interval_exponential(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.interval_exponential(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIntervalExponential(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.interval_exponential());
		addAttr(value);
	}

	/**
	 * @return The method to map the source and destination based on the src and dst lists
	 */
	public jprime.variable.StringVariable getMapping() {
		jprime.variable.StringVariable temp = (jprime.variable.StringVariable)getAttributeByName(ModelNodeVariable.mapping());
		if(null!=temp) return temp;
		return (jprime.variable.StringVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.mapping());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMapping(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.mapping());
		if(temp==null){
			temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.mapping(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.StringVariable)){
				temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.mapping(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.StringVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMapping(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.mapping());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.StaticTrafficType.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
