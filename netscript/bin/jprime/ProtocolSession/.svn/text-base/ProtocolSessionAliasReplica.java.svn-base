
package jprime.ProtocolSession;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolSessionAliasReplica extends jprime.gen.ProtocolSessionAliasReplica implements jprime.ProtocolSession.IProtocolSessionAlias {
	public ProtocolSessionAliasReplica(ModelNodeRecord rec){ super(rec); }
	public ProtocolSessionAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public ProtocolSessionAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolSessionAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
