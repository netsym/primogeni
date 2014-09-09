
package jprime.GhostRoutingSphere;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostRoutingSphereAliasReplica extends jprime.gen.GhostRoutingSphereAliasReplica implements jprime.GhostRoutingSphere.IGhostRoutingSphereAlias {
	public GhostRoutingSphereAliasReplica(ModelNodeRecord rec){ super(rec); }
	public GhostRoutingSphereAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public GhostRoutingSphereAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostRoutingSphereAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
