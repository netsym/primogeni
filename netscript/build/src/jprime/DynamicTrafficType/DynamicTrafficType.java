
package jprime.DynamicTrafficType;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DynamicTrafficType extends jprime.gen.DynamicTrafficType implements jprime.DynamicTrafficType.IDynamicTrafficType {
	public DynamicTrafficType(PyObject[] v, String[] s){super(v,s);}
	public DynamicTrafficType(ModelNodeRecord rec){ super(rec); }
	public DynamicTrafficType(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DynamicTrafficType;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
