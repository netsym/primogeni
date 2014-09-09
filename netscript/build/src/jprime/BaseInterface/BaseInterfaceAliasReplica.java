
package jprime.BaseInterface;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class BaseInterfaceAliasReplica extends jprime.gen.BaseInterfaceAliasReplica implements jprime.BaseInterface.IBaseInterfaceAlias {
	public BaseInterfaceAliasReplica(ModelNodeRecord rec){ super(rec); }
	public BaseInterfaceAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public BaseInterfaceAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.BaseInterfaceAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
