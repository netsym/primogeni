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
import org.python.core.PyObject;
public interface IFluidTraffic extends jprime.TrafficType.ITrafficType {

	/**
	 * @return The type of this fluid class.
	 */
	public jprime.variable.StringVariable getProtocolType();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setProtocolType(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setProtocolType(jprime.variable.SymbolVariable value);

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getSrcs();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSrcs(jprime.ResourceIdentifier.ResourceID value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSrcs(jprime.variable.SymbolVariable value);

	/**
	 * @return XXX
	 */
	public ResourceIdentifierVariable getDsts();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDsts(jprime.ResourceIdentifier.ResourceID value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDsts(jprime.variable.SymbolVariable value);

	/**
	 * @return 1/lamda, which is the expected value of the exponential distribution.
	 */
	public jprime.variable.FloatingPointNumberVariable getOffTime();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setOffTime(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setOffTime(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setOffTime(jprime.variable.SymbolVariable value);

	/**
	 * @return The method to map the source and destination based on the src and dst lists
	 */
	public jprime.variable.StringVariable getMapping();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMapping(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMapping(jprime.variable.SymbolVariable value);

	/**
	 * @return The average number of homogeneous sessions in this fluid flow.
	 */
	public jprime.variable.IntegerVariable getNflows();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNflows(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setNflows(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setNflows(jprime.variable.SymbolVariable value);

	/**
	 * @return Mean packet size in Bytes.
	 */
	public jprime.variable.FloatingPointNumberVariable getPktsiz();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPktsiz(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setPktsiz(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setPktsiz(jprime.variable.SymbolVariable value);

	/**
	 * @return The hurst parameter indicates the presence of LRD (Long-Range Dependence).
	 */
	public jprime.variable.FloatingPointNumberVariable getHurst();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setHurst(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setHurst(jprime.variable.SymbolVariable value);

	/**
	 * @return TCP congestion window multiplicative decrease factor.
	 */
	public jprime.variable.FloatingPointNumberVariable getMd();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMd(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMd(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMd(jprime.variable.SymbolVariable value);

	/**
	 * @return TCP send window maximum size in packets
	 */
	public jprime.variable.FloatingPointNumberVariable getWndmax();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWndmax(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setWndmax(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setWndmax(jprime.variable.SymbolVariable value);

	/**
	 * @return UDP send rate in Byte per second.
	 */
	public jprime.variable.FloatingPointNumberVariable getSendrate();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSendrate(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSendrate(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSendrate(jprime.variable.SymbolVariable value);

	/**
	 * @return The start time of the flow
	 */
	public jprime.variable.FloatingPointNumberVariable getStart();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStart(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStart(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStart(jprime.variable.SymbolVariable value);

	/**
	 * @return The stop time of the flow
	 */
	public jprime.variable.FloatingPointNumberVariable getStop();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setStop(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setStop(jprime.variable.SymbolVariable value);

	/**
	 * @return The name of monitor file.
	 */
	public jprime.variable.StringVariable getMonfn();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonfn(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMonfn(jprime.variable.SymbolVariable value);

	/**
	 * @return Recording interval
	 */
	public jprime.variable.FloatingPointNumberVariable getMonres();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonres(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMonres(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMonres(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
