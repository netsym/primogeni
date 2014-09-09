
package jprime.PlaceHolder;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class PlaceHolderAlias extends jprime.gen.PlaceHolderAlias implements jprime.PlaceHolder.IPlaceHolderAlias {
	public PlaceHolderAlias(ModelNodeRecord rec){ super(rec); }
	public PlaceHolderAlias(PyObject[] v, String[] s){super(v,s);}
	public PlaceHolderAlias(IModelNode parent){
		super(parent);
	}
	public PlaceHolderAlias(IModelNode parent, jprime.PlaceHolder.IPlaceHolder referencedNode) {
		super(parent,(jprime.PlaceHolder.IPlaceHolder)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.PlaceHolderAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
