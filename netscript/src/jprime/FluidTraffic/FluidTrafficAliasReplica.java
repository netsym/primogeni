
package jprime.FluidTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class FluidTrafficAliasReplica extends jprime.gen.FluidTrafficAliasReplica implements jprime.FluidTraffic.IFluidTrafficAlias {
	public FluidTrafficAliasReplica(ModelNodeRecord rec){ super(rec); }
	public FluidTrafficAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public FluidTrafficAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.FluidTrafficAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
