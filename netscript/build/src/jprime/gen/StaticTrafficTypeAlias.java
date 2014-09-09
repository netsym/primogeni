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
public abstract class StaticTrafficTypeAlias extends jprime.TrafficType.TrafficTypeAlias implements jprime.gen.IStaticTrafficTypeAlias {
	public StaticTrafficTypeAlias(IModelNode parent, jprime.StaticTrafficType.IStaticTrafficType referencedNode) {
		super(parent,(jprime.StaticTrafficType.IStaticTrafficType)referencedNode);
	}
	public StaticTrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public StaticTrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public StaticTrafficTypeAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.StaticTrafficType.StaticTrafficTypeAliasReplica c = new jprime.StaticTrafficType.StaticTrafficTypeAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1080: //StaticTrafficTypeAlias
			case 1081: //CNFTrafficAlias
			case 1082: //UDPTrafficAlias
			case 1083: //PPBPTrafficAlias
			case 1084: //TCPTrafficAlias
			case 1085: //ICMPTrafficAlias
			case 1086: //PingTrafficAlias
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
		return (ResourceIdentifierVariable)((IStaticTrafficType)deference()).getSrcs();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(String value) {
		((IStaticTrafficType)deference()).setSrcs(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(jprime.ResourceIdentifier.ResourceID value) {
		((IStaticTrafficType)deference()).setSrcs(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSrcs(jprime.variable.SymbolVariable value) {
		((IStaticTrafficType)deference()).setSrcs(value);
	}

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getDsts() {
		return (ResourceIdentifierVariable)((IStaticTrafficType)deference()).getDsts();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(String value) {
		((IStaticTrafficType)deference()).setDsts(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(jprime.ResourceIdentifier.ResourceID value) {
		((IStaticTrafficType)deference()).setDsts(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDsts(jprime.variable.SymbolVariable value) {
		((IStaticTrafficType)deference()).setDsts(value);
	}

	/**
	 * @return a list of emu/real nodes ips
	 */
	public jprime.variable.OpaqueVariable getDstIps() {
		return (jprime.variable.OpaqueVariable)((IStaticTrafficType)deference()).getDstIps();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstIps(String value) {
		((IStaticTrafficType)deference()).setDstIps(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstIps(jprime.variable.SymbolVariable value) {
		((IStaticTrafficType)deference()).setDstIps(value);
	}

	/**
	 * @return Time before the traffic starts (default: 0).
	 */
	public jprime.variable.FloatingPointNumberVariable getStartTime() {
		return (jprime.variable.FloatingPointNumberVariable)((IStaticTrafficType)deference()).getStartTime();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStartTime(String value) {
		((IStaticTrafficType)deference()).setStartTime(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStartTime(float value) {
		((IStaticTrafficType)deference()).setStartTime(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStartTime(jprime.variable.SymbolVariable value) {
		((IStaticTrafficType)deference()).setStartTime(value);
	}

	/**
	 * @return The time between sending each traffic (default: 0.1). 
	 */
	public jprime.variable.FloatingPointNumberVariable getInterval() {
		return (jprime.variable.FloatingPointNumberVariable)((IStaticTrafficType)deference()).getInterval();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setInterval(String value) {
		((IStaticTrafficType)deference()).setInterval(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setInterval(float value) {
		((IStaticTrafficType)deference()).setInterval(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setInterval(jprime.variable.SymbolVariable value) {
		((IStaticTrafficType)deference()).setInterval(value);
	}

	/**
	 * @return Whether to use exponential interval (default: false).
	 */
	public jprime.variable.BooleanVariable getIntervalExponential() {
		return (jprime.variable.BooleanVariable)((IStaticTrafficType)deference()).getIntervalExponential();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIntervalExponential(String value) {
		((IStaticTrafficType)deference()).setIntervalExponential(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIntervalExponential(boolean value) {
		((IStaticTrafficType)deference()).setIntervalExponential(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIntervalExponential(jprime.variable.SymbolVariable value) {
		((IStaticTrafficType)deference()).setIntervalExponential(value);
	}

	/**
	 * @return The method to map the source and destination based on the src and dst lists
	 */
	public jprime.variable.StringVariable getMapping() {
		return (jprime.variable.StringVariable)((IStaticTrafficType)deference()).getMapping();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMapping(String value) {
		((IStaticTrafficType)deference()).setMapping(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMapping(jprime.variable.SymbolVariable value) {
		((IStaticTrafficType)deference()).setMapping(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.StaticTrafficType.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
