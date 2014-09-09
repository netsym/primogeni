
package jprime.StaticTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class StaticTrafficTypeAlias extends jprime.gen.StaticTrafficTypeAlias implements jprime.StaticTrafficType.IStaticTrafficTypeAlias {
	public StaticTrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public StaticTrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public StaticTrafficTypeAlias(IModelNode parent){
		super(parent);
	}
	public StaticTrafficTypeAlias(IModelNode parent, jprime.StaticTrafficType.IStaticTrafficType referencedNode) {
		super(parent,(jprime.StaticTrafficType.IStaticTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.StaticTrafficTypeAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
