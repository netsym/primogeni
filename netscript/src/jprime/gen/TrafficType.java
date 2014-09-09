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
public abstract class TrafficType extends jprime.ModelNode implements jprime.gen.ITrafficType {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public TrafficType(PyObject[] v, String[] s){super(v,s);}
	public TrafficType(ModelNodeRecord rec){ super(rec); }
	public TrafficType(IModelNode parent){ super(parent); }
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
		jprime.TrafficType.TrafficTypeReplica c = new jprime.TrafficType.TrafficTypeReplica(this.getName(),(IModelNode)parent,(jprime.TrafficType.ITrafficType)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1018: //TrafficType
			case 1019: //FluidTraffic
			case 1020: //DynamicTrafficType
			case 1021: //DistributedTrafficType
			case 1022: //CentralizedTrafficType
			case 1023: //SwingTCPTraffic
			case 1024: //StaticTrafficType
			case 1025: //CNFTraffic
			case 1026: //UDPTraffic
			case 1027: //PPBPTraffic
			case 1028: //TCPTraffic
			case 1029: //ICMPTraffic
			case 1030: //PingTraffic
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
			case 1130: //TrafficTypeReplica
			case 1131: //FluidTrafficReplica
			case 1132: //DynamicTrafficTypeReplica
			case 1133: //DistributedTrafficTypeReplica
			case 1134: //CentralizedTrafficTypeReplica
			case 1135: //SwingTCPTrafficReplica
			case 1136: //StaticTrafficTypeReplica
			case 1137: //CNFTrafficReplica
			case 1138: //UDPTrafficReplica
			case 1139: //PPBPTrafficReplica
			case 1140: //TCPTrafficReplica
			case 1141: //ICMPTrafficReplica
			case 1142: //PingTrafficReplica
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
	public final static java.util.ArrayList<Integer> attrIds=new java.util.ArrayList<Integer>();
	static {
		attrIds.add(162);
		attrIds.add(23);
	}

	/**
	 * @return traffic type's seed
	 */
	public jprime.variable.IntegerVariable getTrafficTypeSeed() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.traffic_type_seed());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficTypeSeed(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.traffic_type_seed());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.traffic_type_seed(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.traffic_type_seed(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTrafficTypeSeed(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.traffic_type_seed());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.traffic_type_seed(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.traffic_type_seed(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTrafficTypeSeed(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.traffic_type_seed());
		addAttr(value);
	}

	/**
	 * @return A comma separated list of the ids of the communities in this partition
	 */
	public jprime.variable.OpaqueVariable getCommunityIds() {
		return (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.community_ids());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setCommunityIds(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.community_ids());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.community_ids(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.community_ids(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setCommunityIds(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.community_ids());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
