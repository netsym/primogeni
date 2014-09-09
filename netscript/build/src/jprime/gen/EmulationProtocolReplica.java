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
public abstract class EmulationProtocolReplica extends jprime.ModelNodeReplica implements jprime.gen.IEmulationProtocol {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public EmulationProtocolReplica(String name, IModelNode parent, jprime.EmulationProtocol.IEmulationProtocol referencedNode) {
		super(name,parent,referencedNode);
	}
	public EmulationProtocolReplica(ModelNodeRecord rec){ super(rec); }
	public EmulationProtocolReplica(PyObject[] v, String[] s){super(v,s);}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.EmulationProtocol.IEmulationProtocol.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		doing_deep_copy=true;
		jprime.EmulationProtocol.EmulationProtocolReplica c = new jprime.EmulationProtocol.EmulationProtocolReplica(this.getName(), (IModelNode)parent,(jprime.EmulationProtocol.IEmulationProtocol)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1113: //EmulationProtocolReplica
			case 1114: //TrafficPortalReplica
			case 1115: //TAPEmulationReplica
			case 1116: //OpenVPNEmulationReplica
			case 1169: //EmulationProtocolAliasReplica
			case 1170: //TrafficPortalAliasReplica
			case 1171: //TAPEmulationAliasReplica
			case 1172: //OpenVPNEmulationAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return If ip_forwarding is set to true the emulated interface will intercept all pkts it sees. If it is set to false it will only capture pkts targeted for it.
	 */
	public jprime.variable.BooleanVariable getIpForwarding() {
		jprime.variable.BooleanVariable temp = (jprime.variable.BooleanVariable)getAttributeByName(ModelNodeVariable.ip_forwarding());
		if(null!=temp) return temp;
		return (jprime.variable.BooleanVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.ip_forwarding());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpForwarding(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.ip_forwarding());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.ip_forwarding(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.ip_forwarding(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIpForwarding(boolean value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.ip_forwarding());
		if(temp==null){
			temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.ip_forwarding(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.BooleanVariable)){
				temp=new jprime.variable.BooleanVariable(jprime.gen.ModelNodeVariable.ip_forwarding(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.BooleanVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIpForwarding(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.ip_forwarding());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.EmulationProtocol.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
