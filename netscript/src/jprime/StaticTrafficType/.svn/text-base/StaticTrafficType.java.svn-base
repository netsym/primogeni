
package jprime.StaticTrafficType;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class StaticTrafficType extends jprime.gen.StaticTrafficType implements jprime.StaticTrafficType.IStaticTrafficType {
	public StaticTrafficType(PyObject[] v, String[] s){super(v,s);}
	public StaticTrafficType(ModelNodeRecord rec){ super(rec); }
	public StaticTrafficType(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.StaticTrafficType;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
