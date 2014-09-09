
package jprime.EmulationProtocol;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class EmulationProtocol extends jprime.gen.EmulationProtocol implements jprime.EmulationProtocol.IEmulationProtocol {
	public EmulationProtocol(PyObject[] v, String[] s){super(v,s);}
	public EmulationProtocol(ModelNodeRecord rec){ super(rec); }
	public EmulationProtocol(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.EmulationProtocol;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
