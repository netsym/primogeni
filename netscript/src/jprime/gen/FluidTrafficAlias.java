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
public abstract class FluidTrafficAlias extends jprime.TrafficType.TrafficTypeAlias implements jprime.gen.IFluidTrafficAlias {
	public FluidTrafficAlias(IModelNode parent, jprime.FluidTraffic.IFluidTraffic referencedNode) {
		super(parent,(jprime.FluidTraffic.IFluidTraffic)referencedNode);
	}
	public FluidTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public FluidTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public FluidTrafficAlias(IModelNode parent){
		super(parent);
	}
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
		jprime.FluidTraffic.FluidTrafficAliasReplica c = new jprime.FluidTraffic.FluidTrafficAliasReplica(this.getName(),(IModelNode)parent,this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1075: //FluidTrafficAlias
			case 1187: //FluidTrafficAliasReplica
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public abstract int getTypeId();

	/**
	 * @return The type of this fluid class.
	 */
	public jprime.variable.StringVariable getProtocolType() {
		return (jprime.variable.StringVariable)((IFluidTraffic)deference()).getProtocolType();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setProtocolType(String value) {
		((IFluidTraffic)deference()).setProtocolType(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setProtocolType(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setProtocolType(value);
	}

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getSrcs() {
		return (ResourceIdentifierVariable)((IFluidTraffic)deference()).getSrcs();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(String value) {
		((IFluidTraffic)deference()).setSrcs(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(jprime.ResourceIdentifier.ResourceID value) {
		((IFluidTraffic)deference()).setSrcs(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSrcs(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setSrcs(value);
	}

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getDsts() {
		return (ResourceIdentifierVariable)((IFluidTraffic)deference()).getDsts();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(String value) {
		((IFluidTraffic)deference()).setDsts(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(jprime.ResourceIdentifier.ResourceID value) {
		((IFluidTraffic)deference()).setDsts(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDsts(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setDsts(value);
	}

	/**
	 * @return 1/lamda, which is the expected value of the exponential distribution.
	 */
	public jprime.variable.FloatingPointNumberVariable getOffTime() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getOffTime();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setOffTime(String value) {
		((IFluidTraffic)deference()).setOffTime(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setOffTime(float value) {
		((IFluidTraffic)deference()).setOffTime(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setOffTime(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setOffTime(value);
	}

	/**
	 * @return The method to map the source and destination based on the src and dst lists
	 */
	public jprime.variable.StringVariable getMapping() {
		return (jprime.variable.StringVariable)((IFluidTraffic)deference()).getMapping();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMapping(String value) {
		((IFluidTraffic)deference()).setMapping(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMapping(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setMapping(value);
	}

	/**
	 * @return The average number of homogeneous sessions in this fluid flow.
	 */
	public jprime.variable.IntegerVariable getNflows() {
		return (jprime.variable.IntegerVariable)((IFluidTraffic)deference()).getNflows();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNflows(String value) {
		((IFluidTraffic)deference()).setNflows(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNflows(long value) {
		((IFluidTraffic)deference()).setNflows(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNflows(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setNflows(value);
	}

	/**
	 * @return Mean packet size in Bytes.
	 */
	public jprime.variable.FloatingPointNumberVariable getPktsiz() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getPktsiz();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPktsiz(String value) {
		((IFluidTraffic)deference()).setPktsiz(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPktsiz(float value) {
		((IFluidTraffic)deference()).setPktsiz(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPktsiz(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setPktsiz(value);
	}

	/**
	 * @return The hurst parameter indicates the presence of LRD (Long-Range Dependence).
	 */
	public jprime.variable.FloatingPointNumberVariable getHurst() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getHurst();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(String value) {
		((IFluidTraffic)deference()).setHurst(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(float value) {
		((IFluidTraffic)deference()).setHurst(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setHurst(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setHurst(value);
	}

	/**
	 * @return TCP congestion window multiplicative decrease factor.
	 */
	public jprime.variable.FloatingPointNumberVariable getMd() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getMd();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMd(String value) {
		((IFluidTraffic)deference()).setMd(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMd(float value) {
		((IFluidTraffic)deference()).setMd(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMd(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setMd(value);
	}

	/**
	 * @return TCP send window maximum size in packets
	 */
	public jprime.variable.FloatingPointNumberVariable getWndmax() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getWndmax();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWndmax(String value) {
		((IFluidTraffic)deference()).setWndmax(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWndmax(float value) {
		((IFluidTraffic)deference()).setWndmax(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setWndmax(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setWndmax(value);
	}

	/**
	 * @return UDP send rate in Byte per second.
	 */
	public jprime.variable.FloatingPointNumberVariable getSendrate() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getSendrate();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSendrate(String value) {
		((IFluidTraffic)deference()).setSendrate(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSendrate(float value) {
		((IFluidTraffic)deference()).setSendrate(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSendrate(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setSendrate(value);
	}

	/**
	 * @return The start time of the flow
	 */
	public jprime.variable.FloatingPointNumberVariable getStart() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getStart();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStart(String value) {
		((IFluidTraffic)deference()).setStart(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStart(float value) {
		((IFluidTraffic)deference()).setStart(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStart(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setStart(value);
	}

	/**
	 * @return The stop time of the flow
	 */
	public jprime.variable.FloatingPointNumberVariable getStop() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getStop();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(String value) {
		((IFluidTraffic)deference()).setStop(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(float value) {
		((IFluidTraffic)deference()).setStop(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStop(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setStop(value);
	}

	/**
	 * @return The name of monitor file.
	 */
	public jprime.variable.StringVariable getMonfn() {
		return (jprime.variable.StringVariable)((IFluidTraffic)deference()).getMonfn();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonfn(String value) {
		((IFluidTraffic)deference()).setMonfn(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMonfn(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setMonfn(value);
	}

	/**
	 * @return Recording interval
	 */
	public jprime.variable.FloatingPointNumberVariable getMonres() {
		return (jprime.variable.FloatingPointNumberVariable)((IFluidTraffic)deference()).getMonres();
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonres(String value) {
		((IFluidTraffic)deference()).setMonres(value);
	}

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonres(float value) {
		((IFluidTraffic)deference()).setMonres(value);
	}

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMonres(jprime.variable.SymbolVariable value) {
		((IFluidTraffic)deference()).setMonres(value);
	}

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.FluidTraffic.attrIds;
	}

	/**
	 * @param kid the child to add
	 */

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
