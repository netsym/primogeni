
package jprime.GhostInterface;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostInterfaceReplica extends jprime.gen.GhostInterfaceReplica implements jprime.GhostInterface.IGhostInterface {
	public GhostInterfaceReplica(ModelNodeRecord rec){ super(rec); }
	public GhostInterfaceReplica(PyObject[] v, String[] s){super(v,s);}
	public GhostInterfaceReplica(String name, IModelNode parent, jprime.GhostInterface.IGhostInterface referencedNode) {
		super(name, parent,(jprime.GhostInterface.IGhostInterface)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostInterfaceReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
