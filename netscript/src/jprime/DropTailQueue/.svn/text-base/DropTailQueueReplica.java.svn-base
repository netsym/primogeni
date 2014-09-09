
package jprime.DropTailQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DropTailQueueReplica extends jprime.gen.DropTailQueueReplica implements jprime.DropTailQueue.IDropTailQueue {
	public DropTailQueueReplica(ModelNodeRecord rec){ super(rec); }
	public DropTailQueueReplica(PyObject[] v, String[] s){super(v,s);}
	public DropTailQueueReplica(String name, IModelNode parent, jprime.DropTailQueue.IDropTailQueue referencedNode) {
		super(name, parent,(jprime.DropTailQueue.IDropTailQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DropTailQueueReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
