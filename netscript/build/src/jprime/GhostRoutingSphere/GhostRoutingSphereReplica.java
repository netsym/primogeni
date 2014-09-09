
package jprime.GhostRoutingSphere;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostRoutingSphereReplica extends jprime.gen.GhostRoutingSphereReplica implements jprime.GhostRoutingSphere.IGhostRoutingSphere {
	public GhostRoutingSphereReplica(ModelNodeRecord rec){ super(rec); }
	public GhostRoutingSphereReplica(PyObject[] v, String[] s){super(v,s);}
	public GhostRoutingSphereReplica(String name, IModelNode parent, jprime.GhostRoutingSphere.IGhostRoutingSphere referencedNode) {
		super(name, parent,(jprime.GhostRoutingSphere.IGhostRoutingSphere)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostRoutingSphereReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
