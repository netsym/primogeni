
package jprime.Aggregate;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class AggregateAliasReplica extends jprime.gen.AggregateAliasReplica implements jprime.Aggregate.IAggregateAlias {
	public AggregateAliasReplica(ModelNodeRecord rec){ super(rec); }
	public AggregateAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public AggregateAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.AggregateAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
