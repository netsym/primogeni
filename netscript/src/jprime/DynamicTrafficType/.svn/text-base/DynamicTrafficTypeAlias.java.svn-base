
package jprime.DynamicTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DynamicTrafficTypeAlias extends jprime.gen.DynamicTrafficTypeAlias implements jprime.DynamicTrafficType.IDynamicTrafficTypeAlias {
	public DynamicTrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public DynamicTrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public DynamicTrafficTypeAlias(IModelNode parent){
		super(parent);
	}
	public DynamicTrafficTypeAlias(IModelNode parent, jprime.DynamicTrafficType.IDynamicTrafficType referencedNode) {
		super(parent,(jprime.DynamicTrafficType.IDynamicTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DynamicTrafficTypeAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
