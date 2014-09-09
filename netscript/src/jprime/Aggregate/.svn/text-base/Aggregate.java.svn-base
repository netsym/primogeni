
package jprime.Aggregate;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class Aggregate extends jprime.gen.Aggregate implements jprime.Aggregate.IAggregate {
	public Aggregate(PyObject[] v, String[] s){super(v,s);}
	public Aggregate(ModelNodeRecord rec){ super(rec); }
	public Aggregate(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.Aggregate;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)
}
