
package jprime.StaticTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class StaticTrafficTypeReplica extends jprime.gen.StaticTrafficTypeReplica implements jprime.StaticTrafficType.IStaticTrafficType {
	public StaticTrafficTypeReplica(ModelNodeRecord rec){ super(rec); }
	public StaticTrafficTypeReplica(PyObject[] v, String[] s){super(v,s);}
	public StaticTrafficTypeReplica(String name, IModelNode parent, jprime.StaticTrafficType.IStaticTrafficType referencedNode) {
		super(name, parent,(jprime.StaticTrafficType.IStaticTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.StaticTrafficTypeReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
