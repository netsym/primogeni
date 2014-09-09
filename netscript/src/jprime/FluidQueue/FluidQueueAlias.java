
package jprime.FluidQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class FluidQueueAlias extends jprime.gen.FluidQueueAlias implements jprime.FluidQueue.IFluidQueueAlias {
	public FluidQueueAlias(ModelNodeRecord rec){ super(rec); }
	public FluidQueueAlias(PyObject[] v, String[] s){super(v,s);}
	public FluidQueueAlias(IModelNode parent){
		super(parent);
	}
	public FluidQueueAlias(IModelNode parent, jprime.FluidQueue.IFluidQueue referencedNode) {
		super(parent,(jprime.FluidQueue.IFluidQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.FluidQueueAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
