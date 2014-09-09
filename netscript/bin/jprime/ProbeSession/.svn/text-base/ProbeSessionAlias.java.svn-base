
package jprime.ProbeSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProbeSessionAlias extends jprime.gen.ProbeSessionAlias implements jprime.ProbeSession.IProbeSessionAlias {
	public ProbeSessionAlias(ModelNodeRecord rec){ super(rec); }
	public ProbeSessionAlias(PyObject[] v, String[] s){super(v,s);}
	public ProbeSessionAlias(IModelNode parent){
		super(parent);
	}
	public ProbeSessionAlias(IModelNode parent, jprime.ProbeSession.IProbeSession referencedNode) {
		super(parent,(jprime.ProbeSession.IProbeSession)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProbeSessionAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
