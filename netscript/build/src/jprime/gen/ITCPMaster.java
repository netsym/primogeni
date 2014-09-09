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
public interface ITCPMaster extends jprime.ProtocolSession.IProtocolSession {

	/**
	 * @return Congestion control algorithm.
	 */
	public jprime.variable.StringVariable getTcpCA();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setTcpCA(String value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setTcpCA(jprime.variable.SymbolVariable value);

	/**
	 * @return maximum segment size in bytes.
	 */
	public jprime.variable.IntegerVariable getMss();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMss(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setMss(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setMss(jprime.variable.SymbolVariable value);

	/**
	 * @return maximum sending window size of TCP protocol in bytes.
	 */
	public jprime.variable.IntegerVariable getSndWndSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndWndSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndWndSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSndWndSize(jprime.variable.SymbolVariable value);

	/**
	 * @return maximum size of the buffer for sending bytes.
	 */
	public jprime.variable.IntegerVariable getSndBufSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndBufSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSndBufSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSndBufSize(jprime.variable.SymbolVariable value);

	/**
	 * @return maximum receiving window size of TCP protocol in bytes.
	 */
	public jprime.variable.IntegerVariable getRcvWndSize();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRcvWndSize(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setRcvWndSize(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setRcvWndSize(jprime.variable.SymbolVariable value);

	/**
	 * @return Interval between consecutive sampling of TCP variables.
	 */
	public jprime.variable.FloatingPointNumberVariable getSamplingInterval();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSamplingInterval(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setSamplingInterval(float value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setSamplingInterval(jprime.variable.SymbolVariable value);

	/**
	 * @return Initial sequence number of TCP protocol.
	 */
	public jprime.variable.IntegerVariable getIss();

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIss(String value);

	/**
	 * Set the attribute to the static value 'value'.
	 * @param value the value
	 */
	public void setIss(long value);

	/**
	 * Have the attribute be bound to the value of the symbol at model instantiation.
	 * @param value the value
	 */
	public void setIss(jprime.variable.SymbolVariable value);

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds();
}
