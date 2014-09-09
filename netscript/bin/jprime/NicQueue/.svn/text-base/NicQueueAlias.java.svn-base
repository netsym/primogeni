
package jprime.NicQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class NicQueueAlias extends jprime.gen.NicQueueAlias implements jprime.NicQueue.INicQueueAlias {
	public NicQueueAlias(ModelNodeRecord rec){ super(rec); }
	public NicQueueAlias(PyObject[] v, String[] s){super(v,s);}
	public NicQueueAlias(IModelNode parent){
		super(parent);
	}
	public NicQueueAlias(IModelNode parent, jprime.NicQueue.INicQueue referencedNode) {
		super(parent,(jprime.NicQueue.INicQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.NicQueueAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
