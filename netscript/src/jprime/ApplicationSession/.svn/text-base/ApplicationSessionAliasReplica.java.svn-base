
package jprime.ApplicationSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ApplicationSessionAliasReplica extends jprime.gen.ApplicationSessionAliasReplica implements jprime.ApplicationSession.IApplicationSessionAlias {
	public ApplicationSessionAliasReplica(ModelNodeRecord rec){ super(rec); }
	public ApplicationSessionAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public ApplicationSessionAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ApplicationSessionAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
