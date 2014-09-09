
package jprime.PlaceHolder;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class PlaceHolderReplica extends jprime.gen.PlaceHolderReplica implements jprime.PlaceHolder.IPlaceHolder {
	public PlaceHolderReplica(ModelNodeRecord rec){ super(rec); }
	public PlaceHolderReplica(PyObject[] v, String[] s){super(v,s);}
	public PlaceHolderReplica(String name, IModelNode parent, jprime.PlaceHolder.IPlaceHolder referencedNode) {
		super(name, parent,(jprime.PlaceHolder.IPlaceHolder)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.PlaceHolderReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
