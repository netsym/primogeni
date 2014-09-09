
package jprime.ProtocolSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolSessionAlias extends jprime.gen.ProtocolSessionAlias implements jprime.ProtocolSession.IProtocolSessionAlias {
	public ProtocolSessionAlias(ModelNodeRecord rec){ super(rec); }
	public ProtocolSessionAlias(PyObject[] v, String[] s){super(v,s);}
	public ProtocolSessionAlias(IModelNode parent){
		super(parent);
	}
	public ProtocolSessionAlias(IModelNode parent, jprime.ProtocolSession.IProtocolSession referencedNode) {
		super(parent,(jprime.ProtocolSession.IProtocolSession)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolSessionAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
