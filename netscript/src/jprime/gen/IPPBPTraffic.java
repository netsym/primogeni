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
public interface IPPBPTraffic extends jprime.StaticTrafficType.IStaticTrafficType {

	/**
	 * @return The destination port for an CBR connection.
	 */
	public jprime.variable.IntegerVariable getDstPort();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setDstPort(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setDstPort(jprime.variable.SymbolVariable value);

	/**
	 * @return The number of bytes to send each time interval.
	 */
	public jprime.variable.IntegerVariable getBytesToSendPerInterval();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendPerInterval(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setBytesToSendPerInterval(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setBytesToSendPerInterval(jprime.variable.SymbolVariable value);

	/**
	 * @return Average number of sessions (default: 0 means unset).
	 */
	public jprime.variable.IntegerVariable getAvgSessions();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAvgSessions(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setAvgSessions(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setAvgSessions(jprime.variable.SymbolVariable value);

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
	 * @return The traffic stops starting new traffic flows after stop time.
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
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
