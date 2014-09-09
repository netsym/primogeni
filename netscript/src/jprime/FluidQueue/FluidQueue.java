
package jprime.FluidQueue;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class FluidQueue extends jprime.gen.FluidQueue implements jprime.FluidQueue.IFluidQueue {
	public FluidQueue(PyObject[] v, String[] s){super(v,s);}
	public FluidQueue(ModelNodeRecord rec){ super(rec); }
	public FluidQueue(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.FluidQueue;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
