
package jprime.Aggregate;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class AggregateAlias extends jprime.gen.AggregateAlias implements jprime.Aggregate.IAggregateAlias {
	public AggregateAlias(ModelNodeRecord rec){ super(rec); }
	public AggregateAlias(PyObject[] v, String[] s){super(v,s);}
	public AggregateAlias(IModelNode parent){
		super(parent);
	}
	public AggregateAlias(IModelNode parent, jprime.Aggregate.IAggregate referencedNode) {
		super(parent,(jprime.Aggregate.IAggregate)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.AggregateAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
