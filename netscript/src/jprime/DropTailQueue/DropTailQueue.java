
package jprime.DropTailQueue;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DropTailQueue extends jprime.gen.DropTailQueue implements jprime.DropTailQueue.IDropTailQueue {
	public DropTailQueue(PyObject[] v, String[] s){super(v,s);}
	public DropTailQueue(ModelNodeRecord rec){ super(rec); }
	public DropTailQueue(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DropTailQueue;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
