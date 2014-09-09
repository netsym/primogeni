
package jprime.FluidTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class FluidTrafficAlias extends jprime.gen.FluidTrafficAlias implements jprime.FluidTraffic.IFluidTrafficAlias {
	public FluidTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public FluidTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public FluidTrafficAlias(IModelNode parent){
		super(parent);
	}
	public FluidTrafficAlias(IModelNode parent, jprime.FluidTraffic.IFluidTraffic referencedNode) {
		super(parent,(jprime.FluidTraffic.IFluidTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.FluidTrafficAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
