
package jprime.ApplicationSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ApplicationSessionReplica extends jprime.gen.ApplicationSessionReplica implements jprime.ApplicationSession.IApplicationSession {
	public ApplicationSessionReplica(ModelNodeRecord rec){ super(rec); }
	public ApplicationSessionReplica(PyObject[] v, String[] s){super(v,s);}
	public ApplicationSessionReplica(String name, IModelNode parent, jprime.ApplicationSession.IApplicationSession referencedNode) {
		super(name, parent,(jprime.ApplicationSession.IApplicationSession)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ApplicationSessionReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
