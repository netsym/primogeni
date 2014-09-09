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
public abstract class ICMPTrafficAlias extends jprime.StaticTrafficType.StaticTrafficTypeAlias implements jprime.gen.IICMPTrafficAlias {
	public ICMPTrafficAlias(IModelNode parent, jprime.ICMPTraffic.IICMPTraffic referencedNode) {
		super(parent,(jprime.ICMPTraffic.IICMPTraffic)referencedNode);
	}
	public ICMPTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public ICMPTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public ICMPTrafficAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.ICMPTraffic.ICMPTrafficAliasReplica c = new jprime.ICMPTraffic.ICMPTrafficAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1085: //ICMPTrafficAlias
			case 1086: //PingTrafficAlias
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
	 * @return The version of ICMP protocol.
	 */
	public jprime.variable.StringVariable getVersion() {
		return (jprime.variable.StringVariable)((IICMPTraffic)deference()).getVersion();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setVersion(String value) {
		((IICMPTraffic)deference()).setVersion(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setVersion(jprime.variable.SymbolVariable value) {
		((IICMPTraffic)deference()).setVersion(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.ICMPTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
