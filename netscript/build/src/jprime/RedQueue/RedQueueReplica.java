
package jprime.RedQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RedQueueReplica extends jprime.gen.RedQueueReplica implements jprime.RedQueue.IRedQueue {
	public RedQueueReplica(ModelNodeRecord rec){ super(rec); }
	public RedQueueReplica(PyObject[] v, String[] s){super(v,s);}
	public RedQueueReplica(String name, IModelNode parent, jprime.RedQueue.IRedQueue referencedNode) {
		super(name, parent,(jprime.RedQueue.IRedQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RedQueueReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
