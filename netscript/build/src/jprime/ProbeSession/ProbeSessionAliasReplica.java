
package jprime.ProbeSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProbeSessionAliasReplica extends jprime.gen.ProbeSessionAliasReplica implements jprime.ProbeSession.IProbeSessionAlias {
	public ProbeSessionAliasReplica(ModelNodeRecord rec){ super(rec); }
	public ProbeSessionAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public ProbeSessionAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProbeSessionAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
