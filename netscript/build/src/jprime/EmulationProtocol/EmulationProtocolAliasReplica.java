
package jprime.EmulationProtocol;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class EmulationProtocolAliasReplica extends jprime.gen.EmulationProtocolAliasReplica implements jprime.EmulationProtocol.IEmulationProtocolAlias {
	public EmulationProtocolAliasReplica(ModelNodeRecord rec){ super(rec); }
	public EmulationProtocolAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public EmulationProtocolAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.EmulationProtocolAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
