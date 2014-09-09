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
public abstract class TCPMasterReplica extends jprime.ProtocolSession.ProtocolSessionReplica implements jprime.gen.ITCPMaster {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public TCPMasterReplica(String name, IModelNode parent, jprime.TCPMaster.ITCPMaster referencedNode) {
		super(name,parent,referencedNode);
	}
	public TCPMasterReplica(ModelNodeRecord rec){ super(rec); }
	public TCPMasterReplica(PyObject[] v, String[] s){super(v,s);}
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
		doing_deep_copy=true;
		jprime.TCPMaster.TCPMasterReplica c = new jprime.TCPMaster.TCPMasterReplica(this.getName(), (IModelNode)parent,(jprime.TCPMaster.ITCPMaster)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1156: //TCPMasterReplica
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
		jprime.variable.StringVariable temp = (jprime.variable.StringVariable)getAttributeByName(ModelNodeVariable.tcpCA());
		if(null!=temp) return temp;
		return (jprime.variable.StringVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.tcpCA());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTcpCA(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.tcpCA());
		if(temp==null){
			temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.tcpCA(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.StringVariable)){
				temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.tcpCA(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.StringVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTcpCA(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.tcpCA());
		addAttr(value);
	}

	/**
	 * @return maximum segment size in bytes.
	 */
	public jprime.variable.IntegerVariable getMss() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.mss());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.mss());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMss(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.mss());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mss(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mss(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMss(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.mss());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mss(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.mss(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMss(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.mss());
		addAttr(value);
	}

	/**
	 * @return maximum sending window size of TCP protocol in bytes.
	 */
	public jprime.variable.IntegerVariable getSndWndSize() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.sndWndSize());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.sndWndSize());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndWndSize(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.sndWndSize());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndWndSize(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndWndSize(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndWndSize(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.sndWndSize());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndWndSize(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndWndSize(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSndWndSize(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.sndWndSize());
		addAttr(value);
	}

	/**
	 * @return maximum size of the buffer for sending bytes.
	 */
	public jprime.variable.IntegerVariable getSndBufSize() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.sndBufSize());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.sndBufSize());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndBufSize(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.sndBufSize());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndBufSize(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndBufSize(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndBufSize(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.sndBufSize());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndBufSize(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.sndBufSize(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSndBufSize(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.sndBufSize());
		addAttr(value);
	}

	/**
	 * @return maximum receiving window size of TCP protocol in bytes.
	 */
	public jprime.variable.IntegerVariable getRcvWndSize() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.rcvWndSize());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.rcvWndSize());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRcvWndSize(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.rcvWndSize());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.rcvWndSize(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.rcvWndSize(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRcvWndSize(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.rcvWndSize());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.rcvWndSize(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.rcvWndSize(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRcvWndSize(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.rcvWndSize());
		addAttr(value);
	}

	/**
	 * @return Interval between consecutive sampling of TCP variables.
	 */
	public jprime.variable.FloatingPointNumberVariable getSamplingInterval() {
		jprime.variable.FloatingPointNumberVariable temp = (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.samplingInterval());
		if(null!=temp) return temp;
		return (jprime.variable.FloatingPointNumberVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.samplingInterval());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSamplingInterval(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.samplingInterval());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.samplingInterval(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.samplingInterval(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSamplingInterval(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.samplingInterval());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.samplingInterval(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.samplingInterval(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSamplingInterval(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.samplingInterval());
		addAttr(value);
	}

	/**
	 * @return Initial sequence number of TCP protocol.
	 */
	public jprime.variable.IntegerVariable getIss() {
		jprime.variable.IntegerVariable temp = (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.iss());
		if(null!=temp) return temp;
		return (jprime.variable.IntegerVariable)this.getReplicatedNode().getAttributeByName(ModelNodeVariable.iss());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIss(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.iss());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.iss(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.iss(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIss(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.iss());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.iss(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.iss(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIss(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.iss());
		addAttr(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.TCPMaster.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
