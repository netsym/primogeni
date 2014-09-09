
package jprime.FluidQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class FluidQueueAliasReplica extends jprime.gen.FluidQueueAliasReplica implements jprime.FluidQueue.IFluidQueueAlias {
	public FluidQueueAliasReplica(ModelNodeRecord rec){ super(rec); }
	public FluidQueueAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public FluidQueueAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.FluidQueueAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
