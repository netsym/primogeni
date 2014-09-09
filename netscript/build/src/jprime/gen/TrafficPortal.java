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
public abstract class TrafficPortal extends jprime.EmulationProtocol.EmulationProtocol implements jprime.gen.ITrafficPortal {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public TrafficPortal(PyObject[] v, String[] s){super(v,s);}
	public TrafficPortal(ModelNodeRecord rec){ super(rec); }
	public TrafficPortal(IModelNode parent){ super(parent); }
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.TrafficPortal.ITrafficPortal.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.TrafficPortal.TrafficPortalReplica c = new jprime.TrafficPortal.TrafficPortalReplica(this.getName(),(IModelNode)parent,(jprime.TrafficPortal.ITrafficPortal)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1002: //TrafficPortal
			case 1058: //TrafficPortalAlias
			case 1114: //TrafficPortalReplica
			case 1170: //TrafficPortalAliasReplica
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
		attrIds.add(82);
	}

	/**
	 * @return list of ip prefixes that are reachable via this portal
	 */
	public jprime.variable.OpaqueVariable getNetworks() {
		return (jprime.variable.OpaqueVariable)getAttributeByName(ModelNodeVariable.networks());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNetworks(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.networks());
		if(temp==null){
			temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.networks(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.OpaqueVariable)){
				temp=new jprime.variable.OpaqueVariable(jprime.gen.ModelNodeVariable.networks(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.OpaqueVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNetworks(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.networks());
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
