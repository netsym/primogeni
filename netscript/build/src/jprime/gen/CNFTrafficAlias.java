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
public abstract class CNFTrafficAlias extends jprime.StaticTrafficType.StaticTrafficTypeAlias implements jprime.gen.ICNFTrafficAlias {
	public CNFTrafficAlias(IModelNode parent, jprime.CNFTraffic.ICNFTraffic referencedNode) {
		super(parent,(jprime.CNFTraffic.ICNFTraffic)referencedNode);
	}
	public CNFTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public CNFTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public CNFTrafficAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.CNFTraffic.ICNFTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.CNFTraffic.CNFTrafficAliasReplica c = new jprime.CNFTraffic.CNFTrafficAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1081: //CNFTrafficAlias
			case 1193: //CNFTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The destination port for an CNF connection.
	 */
	public jprime.variable.IntegerVariable getDstPort() {
		return (jprime.variable.IntegerVariable)((ICNFTraffic)deference()).getDstPort();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(String value) {
		((ICNFTraffic)deference()).setDstPort(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(long value) {
		((ICNFTraffic)deference()).setDstPort(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstPort(jprime.variable.SymbolVariable value) {
		((ICNFTraffic)deference()).setDstPort(value);
	}

	/**
	 * @return The contenet id request to the controller.
	 */
	public jprime.variable.IntegerVariable getContentId() {
		return (jprime.variable.IntegerVariable)((ICNFTraffic)deference()).getContentId();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setContentId(String value) {
		((ICNFTraffic)deference()).setContentId(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setContentId(long value) {
		((ICNFTraffic)deference()).setContentId(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setContentId(jprime.variable.SymbolVariable value) {
		((ICNFTraffic)deference()).setContentId(value);
	}

	/**
	 * @return The total number of contents.
	 */
	public jprime.variable.IntegerVariable getNumberOfContents() {
		return (jprime.variable.IntegerVariable)((ICNFTraffic)deference()).getNumberOfContents();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfContents(String value) {
		((ICNFTraffic)deference()).setNumberOfContents(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfContents(long value) {
		((ICNFTraffic)deference()).setNumberOfContents(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumberOfContents(jprime.variable.SymbolVariable value) {
		((ICNFTraffic)deference()).setNumberOfContents(value);
	}

	/**
	 * @return The total number of requests.
	 */
	public jprime.variable.IntegerVariable getNumberOfRequests() {
		return (jprime.variable.IntegerVariable)((ICNFTraffic)deference()).getNumberOfRequests();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfRequests(String value) {
		((ICNFTraffic)deference()).setNumberOfRequests(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNumberOfRequests(long value) {
		((ICNFTraffic)deference()).setNumberOfRequests(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNumberOfRequests(jprime.variable.SymbolVariable value) {
		((ICNFTraffic)deference()).setNumberOfRequests(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.CNFTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
