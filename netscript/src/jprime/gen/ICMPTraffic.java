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
public abstract class ICMPTraffic extends jprime.StaticTrafficType.StaticTrafficType implements jprime.gen.IICMPTraffic {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public ICMPTraffic(PyObject[] v, String[] s){super(v,s);}
	public ICMPTraffic(ModelNodeRecord rec){ super(rec); }
	public ICMPTraffic(IModelNode parent){ super(parent); }
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.ICMPTraffic.IICMPTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.ICMPTraffic.ICMPTrafficReplica c = new jprime.ICMPTraffic.ICMPTrafficReplica(this.getName(),(IModelNode)parent,(jprime.ICMPTraffic.IICMPTraffic)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1029: //ICMPTraffic
			case 1030: //PingTraffic
			case 1085: //ICMPTrafficAlias
			case 1086: //PingTrafficAlias
			case 1141: //ICMPTrafficReplica
			case 1142: //PingTrafficReplica
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
		attrIds.add(168);
	}

	/**
	 * @return The version of ICMP protocol.
	 */
	public jprime.variable.StringVariable getVersion() {
		return (jprime.variable.StringVariable)getAttributeByName(ModelNodeVariable.version());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setVersion(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.version());
		if(temp==null){
			temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.version(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.StringVariable)){
				temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.version(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.StringVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setVersion(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.version());
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
