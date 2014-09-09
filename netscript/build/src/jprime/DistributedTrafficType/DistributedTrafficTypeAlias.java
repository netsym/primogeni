
package jprime.DistributedTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DistributedTrafficTypeAlias extends jprime.gen.DistributedTrafficTypeAlias implements jprime.DistributedTrafficType.IDistributedTrafficTypeAlias {
	public DistributedTrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public DistributedTrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public DistributedTrafficTypeAlias(IModelNode parent){
		super(parent);
	}
	public DistributedTrafficTypeAlias(IModelNode parent, jprime.DistributedTrafficType.IDistributedTrafficType referencedNode) {
		super(parent,(jprime.DistributedTrafficType.IDistributedTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DistributedTrafficTypeAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
