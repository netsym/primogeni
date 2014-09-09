
package jprime.ApplicationSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ApplicationSessionAlias extends jprime.gen.ApplicationSessionAlias implements jprime.ApplicationSession.IApplicationSessionAlias {
	public ApplicationSessionAlias(ModelNodeRecord rec){ super(rec); }
	public ApplicationSessionAlias(PyObject[] v, String[] s){super(v,s);}
	public ApplicationSessionAlias(IModelNode parent){
		super(parent);
	}
	public ApplicationSessionAlias(IModelNode parent, jprime.ApplicationSession.IApplicationSession referencedNode) {
		super(parent,(jprime.ApplicationSession.IApplicationSession)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ApplicationSessionAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
