
package jprime.PlaceHolder;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class PlaceHolder extends jprime.gen.PlaceHolder implements jprime.PlaceHolder.IPlaceHolder {
	public PlaceHolder(PyObject[] v, String[] s){super(v,s);}
	public PlaceHolder(ModelNodeRecord rec){ super(rec); }
	public PlaceHolder(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.PlaceHolder;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
