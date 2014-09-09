
package jprime.CBR;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CBRAlias extends jprime.gen.CBRAlias implements jprime.CBR.ICBRAlias {
	public CBRAlias(ModelNodeRecord rec){ super(rec); }
	public CBRAlias(PyObject[] v, String[] s){super(v,s);}
	public CBRAlias(IModelNode parent){
		super(parent);
	}
	public CBRAlias(IModelNode parent, jprime.CBR.ICBR referencedNode) {
		super(parent,(jprime.CBR.ICBR)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CBRAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
