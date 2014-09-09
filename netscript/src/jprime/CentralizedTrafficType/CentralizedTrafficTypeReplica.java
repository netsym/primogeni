
package jprime.CentralizedTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CentralizedTrafficTypeReplica extends jprime.gen.CentralizedTrafficTypeReplica implements jprime.CentralizedTrafficType.ICentralizedTrafficType {
	public CentralizedTrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public CentralizedTrafficTypeReplica(PyObject[] v, String[] s){super(v,s);}
	public CentralizedTrafficTypeReplica(String name, IModelNode parent, jprime.CentralizedTrafficType.ICentralizedTrafficType referencedNode) {
		super(name, parent,(jprime.CentralizedTrafficType.ICentralizedTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CentralizedTrafficTypeReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
