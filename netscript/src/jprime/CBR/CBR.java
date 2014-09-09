
package jprime.CBR;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CBR extends jprime.gen.CBR implements jprime.CBR.ICBR {
	public CBR(PyObject[] v, String[] s){super(v,s);}
	public CBR(ModelNodeRecord rec){ super(rec); }
	public CBR(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CBR;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
