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
public abstract class FluidTraffic extends jprime.TrafficType.TrafficType implements jprime.gen.IFluidTraffic {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public FluidTraffic(PyObject[] v, String[] s){super(v,s);}
	public FluidTraffic(ModelNodeRecord rec){ super(rec); }
	public FluidTraffic(IModelNode parent){ super(parent); }
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.FluidTraffic.IFluidTraffic.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.FluidTraffic.FluidTrafficReplica c = new jprime.FluidTraffic.FluidTrafficReplica(this.getName(),(IModelNode)parent,(jprime.FluidTraffic.IFluidTraffic)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1019: //FluidTraffic
			case 1075: //FluidTrafficAlias
			case 1131: //FluidTrafficReplica
			case 1187: //FluidTrafficAliasReplica
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
		attrIds.add(111);
		attrIds.add(139);
		attrIds.add(35);
		attrIds.add(96);
		attrIds.add(69);
		attrIds.add(83);
		attrIds.add(103);
		attrIds.add(42);
		attrIds.add(73);
		attrIds.add(172);
		attrIds.add(130);
		attrIds.add(140);
		attrIds.add(142);
		attrIds.add(76);
		attrIds.add(77);
	}

	/**
	 * @return The type of this fluid class.
	 */
	public jprime.variable.StringVariable getProtocolType() {
		return (jprime.variable.StringVariable)getAttributeByName(ModelNodeVariable.protocol_type());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setProtocolType(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.protocol_type());
		if(temp==null){
			temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.protocol_type(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.StringVariable)){
				temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.protocol_type(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.StringVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setProtocolType(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.protocol_type());
		addAttr(value);
	}

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getSrcs() {
		return (ResourceIdentifierVariable)getAttributeByName(ModelNodeVariable.srcs());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.srcs());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(jprime.ResourceIdentifier.ResourceID value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.srcs());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.srcs(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSrcs(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.srcs());
		addAttr(value);
	}

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getDsts() {
		return (ResourceIdentifierVariable)getAttributeByName(ModelNodeVariable.dsts());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.dsts());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(jprime.ResourceIdentifier.ResourceID value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.dsts());
		if(temp==null){
			temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof ResourceIdentifierVariable)){
				temp=new ResourceIdentifierVariable(jprime.gen.ModelNodeVariable.dsts(),value);
				addAttr(temp);
			}
			else { ((ResourceIdentifierVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDsts(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.dsts());
		addAttr(value);
	}

	/**
	 * @return 1/lamda, which is the expected value of the exponential distribution.
	 */
	public jprime.variable.FloatingPointNumberVariable getOffTime() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.off_time());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setOffTime(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.off_time());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.off_time(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.off_time(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setOffTime(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.off_time());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.off_time(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.off_time(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setOffTime(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.off_time());
		addAttr(value);
	}

	/**
	 * @return The method to map the source and destination based on the src and dst lists
	 */
	public jprime.variable.StringVariable getMapping() {
		return (jprime.variable.StringVariable)getAttributeByName(ModelNodeVariable.mapping());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMapping(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.mapping());
		if(temp==null){
			temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.mapping(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.StringVariable)){
				temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.mapping(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.StringVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMapping(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.mapping());
		addAttr(value);
	}

	/**
	 * @return The average number of homogeneous sessions in this fluid flow.
	 */
	public jprime.variable.IntegerVariable getNflows() {
		return (jprime.variable.IntegerVariable)getAttributeByName(ModelNodeVariable.nflows());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNflows(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.nflows());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nflows(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nflows(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNflows(long value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.nflows());
		if(temp==null){
			temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nflows(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.IntegerVariable)){
				temp=new jprime.variable.IntegerVariable(jprime.gen.ModelNodeVariable.nflows(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.IntegerVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNflows(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.nflows());
		addAttr(value);
	}

	/**
	 * @return Mean packet size in Bytes.
	 */
	public jprime.variable.FloatingPointNumberVariable getPktsiz() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.pktsiz());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPktsiz(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.pktsiz());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.pktsiz(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.pktsiz(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPktsiz(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.pktsiz());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.pktsiz(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.pktsiz(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPktsiz(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.pktsiz());
		addAttr(value);
	}

	/**
	 * @return The hurst parameter indicates the presence of LRD (Long-Range Dependence).
	 */
	public jprime.variable.FloatingPointNumberVariable getHurst() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.hurst());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.hurst());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.hurst(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.hurst(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.hurst());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.hurst(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.hurst(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setHurst(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.hurst());
		addAttr(value);
	}

	/**
	 * @return TCP congestion window multiplicative decrease factor.
	 */
	public jprime.variable.FloatingPointNumberVariable getMd() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.md());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMd(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.md());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.md(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.md(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMd(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.md());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.md(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.md(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMd(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.md());
		addAttr(value);
	}

	/**
	 * @return TCP send window maximum size in packets
	 */
	public jprime.variable.FloatingPointNumberVariable getWndmax() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.wndmax());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWndmax(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.wndmax());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.wndmax(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.wndmax(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWndmax(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.wndmax());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.wndmax(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.wndmax(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setWndmax(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.wndmax());
		addAttr(value);
	}

	/**
	 * @return UDP send rate in Byte per second.
	 */
	public jprime.variable.FloatingPointNumberVariable getSendrate() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.sendrate());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSendrate(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.sendrate());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.sendrate(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.sendrate(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSendrate(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.sendrate());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.sendrate(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.sendrate(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSendrate(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.sendrate());
		addAttr(value);
	}

	/**
	 * @return The start time of the flow
	 */
	public jprime.variable.FloatingPointNumberVariable getStart() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.start());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStart(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.start());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStart(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.start());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.start(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStart(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.start());
		addAttr(value);
	}

	/**
	 * @return The stop time of the flow
	 */
	public jprime.variable.FloatingPointNumberVariable getStop() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.stop());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.stop());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.stop(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.stop(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.stop());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.stop(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.stop(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStop(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.stop());
		addAttr(value);
	}

	/**
	 * @return The name of monitor file.
	 */
	public jprime.variable.StringVariable getMonfn() {
		return (jprime.variable.StringVariable)getAttributeByName(ModelNodeVariable.monfn());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonfn(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.monfn());
		if(temp==null){
			temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.monfn(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.StringVariable)){
				temp=new jprime.variable.StringVariable(jprime.gen.ModelNodeVariable.monfn(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.StringVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMonfn(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.monfn());
		addAttr(value);
	}

	/**
	 * @return Recording interval
	 */
	public jprime.variable.FloatingPointNumberVariable getMonres() {
		return (jprime.variable.FloatingPointNumberVariable)getAttributeByName(ModelNodeVariable.monres());
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonres(String value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.monres());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.monres(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.monres(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonres(float value) {
		jprime.variable.ModelNodeVariable temp = getAttributeByName(jprime.gen.ModelNodeVariable.monres());
		if(temp==null){
			temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.monres(),value);
			addAttr(temp);
		}
		else{
			if(! (temp instanceof jprime.variable.FloatingPointNumberVariable)){
				temp=new jprime.variable.FloatingPointNumberVariable(jprime.gen.ModelNodeVariable.monres(),value);
				addAttr(temp);
			}
			else { ((jprime.variable.FloatingPointNumberVariable)temp).setValue(value); }
		}
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMonres(jprime.variable.SymbolVariable value) {
		if(value==null)throw new RuntimeException("attr was null");
		if(value.getDBName() != -1) throw new RuntimeException("the attr was already attached to another model node!");
		value.attachToNode(this,jprime.gen.ModelNodeVariable.monres());
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
