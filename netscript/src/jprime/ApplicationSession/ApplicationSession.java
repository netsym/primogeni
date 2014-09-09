
package jprime.ApplicationSession;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ApplicationSession extends jprime.gen.ApplicationSession implements jprime.ApplicationSession.IApplicationSession {
	public ApplicationSession(PyObject[] v, String[] s){super(v,s);}
	public ApplicationSession(ModelNodeRecord rec){ super(rec); }
	public ApplicationSession(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	 * @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ApplicationSession;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
