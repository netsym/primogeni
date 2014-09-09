
package jprime.ProbeSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProbeSessionReplica extends jprime.gen.ProbeSessionReplica implements jprime.ProbeSession.IProbeSession {
	public ProbeSessionReplica(ModelNodeRecord rec){ super(rec); }
	public ProbeSessionReplica(PyObject[] v, String[] s){super(v,s);}
	public ProbeSessionReplica(String name, IModelNode parent, jprime.ProbeSession.IProbeSession referencedNode) {
		super(name, parent,(jprime.ProbeSession.IProbeSession)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProbeSessionReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
