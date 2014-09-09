
package jprime.EmulationProtocol;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class EmulationProtocolReplica extends jprime.gen.EmulationProtocolReplica implements jprime.EmulationProtocol.IEmulationProtocol {
	public EmulationProtocolReplica(ModelNodeRecord rec){ super(rec); }
	public EmulationProtocolReplica(PyObject[] v, String[] s){super(v,s);}
	public EmulationProtocolReplica(String name, IModelNode parent, jprime.EmulationProtocol.IEmulationProtocol referencedNode) {
		super(name, parent,(jprime.EmulationProtocol.IEmulationProtocol)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.EmulationProtocolReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
