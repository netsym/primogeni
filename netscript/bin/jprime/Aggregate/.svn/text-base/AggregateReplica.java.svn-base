
package jprime.Aggregate;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class AggregateReplica extends jprime.gen.AggregateReplica implements jprime.Aggregate.IAggregate {
	public AggregateReplica(ModelNodeRecord rec){ super(rec); }
	public AggregateReplica(PyObject[] v, String[] s){super(v,s);}
	public AggregateReplica(String name, IModelNode parent, jprime.Aggregate.IAggregate referencedNode) {
		super(name, parent,(jprime.Aggregate.IAggregate)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.AggregateReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)
}
