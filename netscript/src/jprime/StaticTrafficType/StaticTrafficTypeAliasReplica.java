
package jprime.StaticTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class StaticTrafficTypeAliasReplica extends jprime.gen.StaticTrafficTypeAliasReplica implements jprime.StaticTrafficType.IStaticTrafficTypeAlias {
	public StaticTrafficTypeAliasReplica(ModelNodeRecord rec){ super(rec); }
	public StaticTrafficTypeAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public StaticTrafficTypeAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.StaticTrafficTypeAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
