
package jprime.FluidQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class FluidQueueReplica extends jprime.gen.FluidQueueReplica implements jprime.FluidQueue.IFluidQueue {
	public FluidQueueReplica(ModelNodeRecord rec){ super(rec); }
	public FluidQueueReplica(PyObject[] v, String[] s){super(v,s);}
	public FluidQueueReplica(String name, IModelNode parent, jprime.FluidQueue.IFluidQueue referencedNode) {
		super(name, parent,(jprime.FluidQueue.IFluidQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.FluidQueueReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
