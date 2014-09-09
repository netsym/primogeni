
package jprime.NicQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class NicQueueAliasReplica extends jprime.gen.NicQueueAliasReplica implements jprime.NicQueue.INicQueueAlias {
	public NicQueueAliasReplica(ModelNodeRecord rec){ super(rec); }
	public NicQueueAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public NicQueueAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.NicQueueAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
