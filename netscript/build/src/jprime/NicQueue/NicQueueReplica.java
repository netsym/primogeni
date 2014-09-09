
package jprime.NicQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class NicQueueReplica extends jprime.gen.NicQueueReplica implements jprime.NicQueue.INicQueue {
	public NicQueueReplica(ModelNodeRecord rec){ super(rec); }
	public NicQueueReplica(PyObject[] v, String[] s){super(v,s);}
	public NicQueueReplica(String name, IModelNode parent, jprime.NicQueue.INicQueue referencedNode) {
		super(name, parent,(jprime.NicQueue.INicQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.NicQueueReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
