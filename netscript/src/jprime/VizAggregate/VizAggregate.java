
package jprime.VizAggregate;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class VizAggregate extends jprime.gen.VizAggregate implements jprime.VizAggregate.IVizAggregate {
	public VizAggregate(PyObject[] v, String[] s){super(v,s);}
	public VizAggregate(ModelNodeRecord rec){ super(rec); }
	public VizAggregate(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.VizAggregate;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
