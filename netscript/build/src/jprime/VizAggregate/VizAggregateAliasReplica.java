
package jprime.VizAggregate;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class VizAggregateAliasReplica extends jprime.gen.VizAggregateAliasReplica implements jprime.VizAggregate.IVizAggregateAlias {
	public VizAggregateAliasReplica(ModelNodeRecord rec){ super(rec); }
	public VizAggregateAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public VizAggregateAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.VizAggregateAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
