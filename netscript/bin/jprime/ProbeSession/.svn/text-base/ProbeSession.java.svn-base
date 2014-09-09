
package jprime.ProbeSession;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProbeSession extends jprime.gen.ProbeSession implements jprime.ProbeSession.IProbeSession {
	public ProbeSession(PyObject[] v, String[] s){super(v,s);}
	public ProbeSession(ModelNodeRecord rec){ super(rec); }
	public ProbeSession(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProbeSession;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
