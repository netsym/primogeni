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
public abstract class SymbioSimAppProt extends jprime.ProtocolSession.ProtocolSession implements jprime.gen.ISymbioSimAppProt {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public SymbioSimAppProt(PyObject[] v, String[] s){super(v,s);}
	public SymbioSimAppProt(ModelNodeRecord rec){ super(rec); }
	public SymbioSimAppProt(IModelNode parent){ super(parent); }
	/**
	 * @return the interface which this node implements
	 */
	public Class<?> getNodeType() {
		return jprime.SymbioSimAppProt.ISymbioSimAppProt.class;
	}
	/**
	 * @param used by replicas to do a deep copy of the node.
	 */
	public jprime.ModelNode deepCopy(jprime.ModelNode parent) {
		jprime.SymbioSimAppProt.SymbioSimAppProtReplica c = new jprime.SymbioSimAppProt.SymbioSimAppProtReplica(this.getName(),(IModelNode)parent,(jprime.SymbioSimAppProt.ISymbioSimAppProt)this);
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
			case 1043: //SymbioSimAppProt
			case 1099: //SymbioSimAppProtAlias
			case 1155: //SymbioSimAppProtReplica
			case 1211: //SymbioSimAppProtAliasReplica
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
