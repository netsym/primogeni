
package jprime.SymbioSimAppProt;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SymbioSimAppProt extends jprime.gen.SymbioSimAppProt implements jprime.SymbioSimAppProt.ISymbioSimAppProt {
	public SymbioSimAppProt(PyObject[] v, String[] s){super(v,s);}
	public SymbioSimAppProt(ModelNodeRecord rec){ super(rec); }
	public SymbioSimAppProt(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SymbioSimAppProt;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
