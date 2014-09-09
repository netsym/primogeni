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
public abstract class TrafficTypeAlias extends jprime.ModelNodeAlias implements jprime.gen.ITrafficTypeAlias {
	public TrafficTypeAlias(IModelNode parent, jprime.TrafficType.ITrafficType referencedNode) {
		super(parent,(IModelNode)referencedNode);
	}
	public TrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public TrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public TrafficTypeAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.TrafficType.ITrafficType.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.TrafficType.TrafficTypeAliasReplica c = new jprime.TrafficType.TrafficTypeAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1074: //TrafficTypeAlias
			case 1075: //FluidTrafficAlias
			case 1076: //DynamicTrafficTypeAlias
			case 1077: //DistributedTrafficTypeAlias
			case 1078: //CentralizedTrafficTypeAlias
			case 1079: //SwingTCPTrafficAlias
			case 1080: //StaticTrafficTypeAlias
			case 1081: //CNFTrafficAlias
			case 1082: //UDPTrafficAlias
			case 1083: //PPBPTrafficAlias
			case 1084: //TCPTrafficAlias
			case 1085: //ICMPTrafficAlias
			case 1086: //PingTrafficAlias
			case 1186: //TrafficTypeAliasReplica
			case 1187: //FluidTrafficAliasReplica
			case 1188: //DynamicTrafficTypeAliasReplica
			case 1189: //DistributedTrafficTypeAliasReplica
			case 1190: //CentralizedTrafficTypeAliasReplica
			case 1191: //SwingTCPTrafficAliasReplica
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
	 * @return traffic type's seed
	 */
	public jprime.variable.IntegerVariable getTrafficTypeSeed() {
		return (jprime.variable.IntegerVariable)((ITrafficType)deference()).getTrafficTypeSeed();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficTypeSeed(String value) {
		((ITrafficType)deference()).setTrafficTypeSeed(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficTypeSeed(long value) {
		((ITrafficType)deference()).setTrafficTypeSeed(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTrafficTypeSeed(jprime.variable.SymbolVariable value) {
		((ITrafficType)deference()).setTrafficTypeSeed(value);
	}

	/**
	 * @return A comma separated list of the ids of the communities in this partition
	 */
	public jprime.variable.OpaqueVariable getCommunityIds() {
		return (jprime.variable.OpaqueVariable)((ITrafficType)deference()).getCommunityIds();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCommunityIds(String value) {
		((ITrafficType)deference()).setCommunityIds(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCommunityIds(jprime.variable.SymbolVariable value) {
		((ITrafficType)deference()).setCommunityIds(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.TrafficType.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
