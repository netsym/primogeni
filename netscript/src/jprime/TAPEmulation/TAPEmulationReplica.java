
package jprime.TAPEmulation;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TAPEmulationReplica extends jprime.gen.TAPEmulationReplica implements jprime.TAPEmulation.ITAPEmulation {
	public TAPEmulationReplica(ModelNodeRecord rec){ super(rec); }
	public TAPEmulationReplica(PyObject[] v, String[] s){super(v,s);}
	public TAPEmulationReplica(String name, IModelNode parent, jprime.TAPEmulation.ITAPEmulation referencedNode) {
		super(name, parent,(jprime.TAPEmulation.ITAPEmulation)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TAPEmulationReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
