
package jprime.CBR;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CBRAliasReplica extends jprime.gen.CBRAliasReplica implements jprime.CBR.ICBRAlias {
	public CBRAliasReplica(ModelNodeRecord rec){ super(rec); }
	public CBRAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public CBRAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CBRAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
