
package jprime.BaseInterface;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class BaseInterfaceReplica extends jprime.gen.BaseInterfaceReplica implements jprime.BaseInterface.IBaseInterface {
	public BaseInterfaceReplica(ModelNodeRecord rec){ super(rec); }
	public BaseInterfaceReplica(PyObject[] v, String[] s){super(v,s);}
	public BaseInterfaceReplica(String name, IModelNode parent, jprime.BaseInterface.IBaseInterface referencedNode) {
		super(name, parent,(jprime.BaseInterface.IBaseInterface)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.BaseInterfaceReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
