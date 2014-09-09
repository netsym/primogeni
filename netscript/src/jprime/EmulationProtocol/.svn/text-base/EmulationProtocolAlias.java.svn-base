
package jprime.EmulationProtocol;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class EmulationProtocolAlias extends jprime.gen.EmulationProtocolAlias implements jprime.EmulationProtocol.IEmulationProtocolAlias {
	public EmulationProtocolAlias(ModelNodeRecord rec){ super(rec); }
	public EmulationProtocolAlias(PyObject[] v, String[] s){super(v,s);}
	public EmulationProtocolAlias(IModelNode parent){
		super(parent);
	}
	public EmulationProtocolAlias(IModelNode parent, jprime.EmulationProtocol.IEmulationProtocol referencedNode) {
		super(parent,(jprime.EmulationProtocol.IEmulationProtocol)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.EmulationProtocolAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
