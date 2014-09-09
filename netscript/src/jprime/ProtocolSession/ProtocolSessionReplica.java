
package jprime.ProtocolSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolSessionReplica extends jprime.gen.ProtocolSessionReplica implements jprime.ProtocolSession.IProtocolSession {
	public ProtocolSessionReplica(ModelNodeRecord rec){ super(rec); }
	public ProtocolSessionReplica(PyObject[] v, String[] s){super(v,s);}
	public ProtocolSessionReplica(String name, IModelNode parent, jprime.ProtocolSession.IProtocolSession referencedNode) {
		super(name, parent,(jprime.ProtocolSession.IProtocolSession)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolSessionReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
