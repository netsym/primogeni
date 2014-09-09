
package jprime.TAPEmulation;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TAPEmulation extends jprime.gen.TAPEmulation implements jprime.TAPEmulation.ITAPEmulation {
	public TAPEmulation(PyObject[] v, String[] s){super(v,s);}
	public TAPEmulation(ModelNodeRecord rec){ super(rec); }
	public TAPEmulation(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TAPEmulation;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
