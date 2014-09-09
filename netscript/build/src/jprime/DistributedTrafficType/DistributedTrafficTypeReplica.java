
package jprime.DistributedTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DistributedTrafficTypeReplica extends jprime.gen.DistributedTrafficTypeReplica implements jprime.DistributedTrafficType.IDistributedTrafficType {
	public DistributedTrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public DistributedTrafficTypeReplica(PyObject[] v, String[] s){super(v,s);}
	public DistributedTrafficTypeReplica(String name, IModelNode parent, jprime.DistributedTrafficType.IDistributedTrafficType referencedNode) {
		super(name, parent,(jprime.DistributedTrafficType.IDistributedTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DistributedTrafficTypeReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
