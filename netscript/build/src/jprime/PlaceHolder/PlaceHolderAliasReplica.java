
package jprime.PlaceHolder;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class PlaceHolderAliasReplica extends jprime.gen.PlaceHolderAliasReplica implements jprime.PlaceHolder.IPlaceHolderAlias {
	public PlaceHolderAliasReplica(ModelNodeRecord rec){ super(rec); }
	public PlaceHolderAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public PlaceHolderAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.PlaceHolderAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
