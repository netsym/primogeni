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
public abstract class SymbioSimAppProtReplica extends jprime.ProtocolSession.ProtocolSessionReplica implements jprime.gen.ISymbioSimAppProt {

	/* used to enforce the minimum/maximum child requirement */

	public void enforceChildConstraints() {
		super.enforceChildConstraints();

	}
	public SymbioSimAppProtReplica(String name, IModelNode parent, jprime.SymbioSimAppProt.ISymbioSimAppProt referencedNode) {
		super(name,parent,referencedNode);
	}
	public SymbioSimAppProtReplica(ModelNodeRecord rec){ super(rec); }
	public SymbioSimAppProtReplica(PyObject[] v, String[] s){super(v,s);}
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
		doing_deep_copy=true;
		jprime.SymbioSimAppProt.SymbioSimAppProtReplica c = new jprime.SymbioSimAppProt.SymbioSimAppProtReplica(this.getName(), (IModelNode)parent,(jprime.SymbioSimAppProt.ISymbioSimAppProt)this);
		doing_deep_copy=false;
		return c;
	}
	public static boolean isSubType(IModelNode n) {
		return isSubType(n.getTypeId());
	}
	public static boolean isSubType(int id) {
		switch(id) {
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

	/**
	 * @return a list of ids of the possible type of attribute this model node type can have
	 */
	public java.util.ArrayList<Integer> getAttrIds() {
		return jprime.gen.SymbioSimAppProt.attrIds;
	}

	/**
	 * @param visitor a generic visitor
	 */
	public abstract void accept(jprime.visitors.IGenericVisitor visitor);
}
