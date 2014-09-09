
package jprime.SymbioSimAppProt;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SymbioSimAppProtReplica extends jprime.gen.SymbioSimAppProtReplica implements jprime.SymbioSimAppProt.ISymbioSimAppProt {
	public SymbioSimAppProtReplica(ModelNodeRecord rec){ super(rec); }
	public SymbioSimAppProtReplica(PyObject[] v, String[] s){super(v,s);}
	public SymbioSimAppProtReplica(String name, IModelNode parent, jprime.SymbioSimAppProt.ISymbioSimAppProt referencedNode) {
		super(name, parent,(jprime.SymbioSimAppProt.ISymbioSimAppProt)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SymbioSimAppProtReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
