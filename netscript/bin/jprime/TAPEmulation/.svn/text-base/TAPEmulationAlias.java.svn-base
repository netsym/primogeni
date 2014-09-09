
package jprime.TAPEmulation;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TAPEmulationAlias extends jprime.gen.TAPEmulationAlias implements jprime.TAPEmulation.ITAPEmulationAlias {
	public TAPEmulationAlias(ModelNodeRecord rec){ super(rec); }
	public TAPEmulationAlias(PyObject[] v, String[] s){super(v,s);}
	public TAPEmulationAlias(IModelNode parent){
		super(parent);
	}
	public TAPEmulationAlias(IModelNode parent, jprime.TAPEmulation.ITAPEmulation referencedNode) {
		super(parent,(jprime.TAPEmulation.ITAPEmulation)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TAPEmulationAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
