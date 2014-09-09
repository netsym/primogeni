
package jprime.GhostRoutingSphere;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostRoutingSphereAlias extends jprime.gen.GhostRoutingSphereAlias implements jprime.GhostRoutingSphere.IGhostRoutingSphereAlias {
	public GhostRoutingSphereAlias(ModelNodeRecord rec){ super(rec); }
	public GhostRoutingSphereAlias(PyObject[] v, String[] s){super(v,s);}
	public GhostRoutingSphereAlias(IModelNode parent){
		super(parent);
	}
	public GhostRoutingSphereAlias(IModelNode parent, jprime.GhostRoutingSphere.IGhostRoutingSphere referencedNode) {
		super(parent,(jprime.GhostRoutingSphere.IGhostRoutingSphere)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostRoutingSphereAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
