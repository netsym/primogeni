
package jprime.CBR;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CBRReplica extends jprime.gen.CBRReplica implements jprime.CBR.ICBR {
	public CBRReplica(ModelNodeRecord rec){ super(rec); }
	public CBRReplica(PyObject[] v, String[] s){super(v,s);}
	public CBRReplica(String name, IModelNode parent, jprime.CBR.ICBR referencedNode) {
		super(name, parent,(jprime.CBR.ICBR)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CBRReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
