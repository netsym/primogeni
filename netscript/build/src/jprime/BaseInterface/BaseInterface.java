
package jprime.BaseInterface;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class BaseInterface extends jprime.gen.BaseInterface implements jprime.BaseInterface.IBaseInterface {
	public BaseInterface(PyObject[] v, String[] s){super(v,s);}
	public BaseInterface(ModelNodeRecord rec){ super(rec); }
	public BaseInterface(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.BaseInterface;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
