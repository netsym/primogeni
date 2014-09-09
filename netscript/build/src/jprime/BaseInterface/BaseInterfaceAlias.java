
package jprime.BaseInterface;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class BaseInterfaceAlias extends jprime.gen.BaseInterfaceAlias implements jprime.BaseInterface.IBaseInterfaceAlias {
	public BaseInterfaceAlias(ModelNodeRecord rec){ super(rec); }
	public BaseInterfaceAlias(PyObject[] v, String[] s){super(v,s);}
	public BaseInterfaceAlias(IModelNode parent){
		super(parent);
	}
	public BaseInterfaceAlias(IModelNode parent, jprime.BaseInterface.IBaseInterface referencedNode) {
		super(parent,(jprime.BaseInterface.IBaseInterface)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.BaseInterfaceAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
