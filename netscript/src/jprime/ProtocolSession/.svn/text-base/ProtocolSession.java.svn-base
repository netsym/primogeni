
package jprime.ProtocolSession;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolSession extends jprime.gen.ProtocolSession implements jprime.ProtocolSession.IProtocolSession {
	public ProtocolSession(PyObject[] v, String[] s){super(v,s);}
	public ProtocolSession(ModelNodeRecord rec){ super(rec); }
	public ProtocolSession(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolSession;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
