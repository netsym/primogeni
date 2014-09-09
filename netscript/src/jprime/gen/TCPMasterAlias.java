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
public abstract class TCPMasterAlias extends jprime.ProtocolSession.ProtocolSessionAlias implements jprime.gen.ITCPMasterAlias {
	public TCPMasterAlias(IModelNode parent, jprime.TCPMaster.ITCPMaster referencedNode) {
		super(parent,(jprime.TCPMaster.ITCPMaster)referencedNode);
	}
	public TCPMasterAlias(ModelNodeRecord rec){ super(rec); }
	public TCPMasterAlias(PyObject[] v, String[] s){super(v,s);}
	public TCPMasterAlias(IModelNode parent){
		super(parent);
	}
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.TCPMaster.ITCPMaster.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.TCPMaster.TCPMasterAliasReplica c = new jprime.TCPMaster.TCPMasterAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1100: //TCPMasterAlias
			case 1212: //TCPMasterAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return Congestion control algorithm.
	 */
	public jprime.variable.StringVariable getTcpCA() {
		return (jprime.variable.StringVariable)((ITCPMaster)deference()).getTcpCA();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTcpCA(String value) {
		((ITCPMaster)deference()).setTcpCA(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTcpCA(jprime.variable.SymbolVariable value) {
		((ITCPMaster)deference()).setTcpCA(value);
	}

	/**
	 * @return maximum segment size in bytes.
	 */
	public jprime.variable.IntegerVariable getMss() {
		return (jprime.variable.IntegerVariable)((ITCPMaster)deference()).getMss();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMss(String value) {
		((ITCPMaster)deference()).setMss(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMss(long value) {
		((ITCPMaster)deference()).setMss(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMss(jprime.variable.SymbolVariable value) {
		((ITCPMaster)deference()).setMss(value);
	}

	/**
	 * @return maximum sending window size of TCP protocol in bytes.
	 */
	public jprime.variable.IntegerVariable getSndWndSize() {
		return (jprime.variable.IntegerVariable)((ITCPMaster)deference()).getSndWndSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndWndSize(String value) {
		((ITCPMaster)deference()).setSndWndSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndWndSize(long value) {
		((ITCPMaster)deference()).setSndWndSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSndWndSize(jprime.variable.SymbolVariable value) {
		((ITCPMaster)deference()).setSndWndSize(value);
	}

	/**
	 * @return maximum size of the buffer for sending bytes.
	 */
	public jprime.variable.IntegerVariable getSndBufSize() {
		return (jprime.variable.IntegerVariable)((ITCPMaster)deference()).getSndBufSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndBufSize(String value) {
		((ITCPMaster)deference()).setSndBufSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndBufSize(long value) {
		((ITCPMaster)deference()).setSndBufSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSndBufSize(jprime.variable.SymbolVariable value) {
		((ITCPMaster)deference()).setSndBufSize(value);
	}

	/**
	 * @return maximum receiving window size of TCP protocol in bytes.
	 */
	public jprime.variable.IntegerVariable getRcvWndSize() {
		return (jprime.variable.IntegerVariable)((ITCPMaster)deference()).getRcvWndSize();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRcvWndSize(String value) {
		((ITCPMaster)deference()).setRcvWndSize(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRcvWndSize(long value) {
		((ITCPMaster)deference()).setRcvWndSize(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRcvWndSize(jprime.variable.SymbolVariable value) {
		((ITCPMaster)deference()).setRcvWndSize(value);
	}

	/**
	 * @return Interval between consecutive sampling of TCP variables.
	 */
	public jprime.variable.FloatingPointNumberVariable getSamplingInterval() {
		return (jprime.variable.FloatingPointNumberVariable)((ITCPMaster)deference()).getSamplingInterval();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSamplingInterval(String value) {
		((ITCPMaster)deference()).setSamplingInterval(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSamplingInterval(float value) {
		((ITCPMaster)deference()).setSamplingInterval(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSamplingInterval(jprime.variable.SymbolVariable value) {
		((ITCPMaster)deference()).setSamplingInterval(value);
	}

	/**
	 * @return Initial sequence number of TCP protocol.
	 */
	public jprime.variable.IntegerVariable getIss() {
		return (jprime.variable.IntegerVariable)((ITCPMaster)deference()).getIss();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIss(String value) {
		((ITCPMaster)deference()).setIss(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIss(long value) {
		((ITCPMaster)deference()).setIss(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIss(jprime.variable.SymbolVariable value) {
		((ITCPMaster)deference()).setIss(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.TCPMaster.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
