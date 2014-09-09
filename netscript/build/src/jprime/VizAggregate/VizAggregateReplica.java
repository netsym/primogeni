
package jprime.VizAggregate;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class VizAggregateReplica extends jprime.gen.VizAggregateReplica implements jprime.VizAggregate.IVizAggregate {
	public VizAggregateReplica(ModelNodeRecord rec){ super(rec); }
	public VizAggregateReplica(PyObject[] v, String[] s){super(v,s);}
	public VizAggregateReplica(String name, IModelNode parent, jprime.VizAggregate.IVizAggregate referencedNode) {
		super(name, parent,(jprime.VizAggregate.IVizAggregate)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.VizAggregateReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
