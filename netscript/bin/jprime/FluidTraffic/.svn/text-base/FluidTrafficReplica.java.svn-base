
package jprime.FluidTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class FluidTrafficReplica extends jprime.gen.FluidTrafficReplica implements jprime.FluidTraffic.IFluidTraffic {
	public FluidTrafficReplica(ModelNodeRecord rec){ super(rec); }
	public FluidTrafficReplica(PyObject[] v, String[] s){super(v,s);}
	public FluidTrafficReplica(String name, IModelNode parent, jprime.FluidTraffic.IFluidTraffic referencedNode) {
		super(name, parent,(jprime.FluidTraffic.IFluidTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.FluidTrafficReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
