
package jprime.VizAggregate;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class VizAggregateAlias extends jprime.gen.VizAggregateAlias implements jprime.VizAggregate.IVizAggregateAlias {
	public VizAggregateAlias(ModelNodeRecord rec){ super(rec); }
	public VizAggregateAlias(PyObject[] v, String[] s){super(v,s);}
	public VizAggregateAlias(IModelNode parent){
		super(parent);
	}
	public VizAggregateAlias(IModelNode parent, jprime.VizAggregate.IVizAggregate referencedNode) {
		super(parent,(jprime.VizAggregate.IVizAggregate)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.VizAggregateAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
