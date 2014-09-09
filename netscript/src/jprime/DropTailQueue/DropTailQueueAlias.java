
package jprime.DropTailQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DropTailQueueAlias extends jprime.gen.DropTailQueueAlias implements jprime.DropTailQueue.IDropTailQueueAlias {
	public DropTailQueueAlias(ModelNodeRecord rec){ super(rec); }
	public DropTailQueueAlias(PyObject[] v, String[] s){super(v,s);}
	public DropTailQueueAlias(IModelNode parent){
		super(parent);
	}
	public DropTailQueueAlias(IModelNode parent, jprime.DropTailQueue.IDropTailQueue referencedNode) {
		super(parent,(jprime.DropTailQueue.IDropTailQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DropTailQueueAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
