
package jprime.DynamicTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DynamicTrafficTypeReplica extends jprime.gen.DynamicTrafficTypeReplica implements jprime.DynamicTrafficType.IDynamicTrafficType {
	public DynamicTrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public DynamicTrafficTypeReplica(PyObject[] v, String[] s){super(v,s);}
	public DynamicTrafficTypeReplica(String name, IModelNode parent, jprime.DynamicTrafficType.IDynamicTrafficType referencedNode) {
		super(name, parent,(jprime.DynamicTrafficType.IDynamicTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DynamicTrafficTypeReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
