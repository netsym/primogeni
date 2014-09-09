
package jprime.RedQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RedQueueAlias extends jprime.gen.RedQueueAlias implements jprime.RedQueue.IRedQueueAlias {
	public RedQueueAlias(ModelNodeRecord rec){ super(rec); }
	public RedQueueAlias(PyObject[] v, String[] s){super(v,s);}
	public RedQueueAlias(IModelNode parent){
		super(parent);
	}
	public RedQueueAlias(IModelNode parent, jprime.RedQueue.IRedQueue referencedNode) {
		super(parent,(jprime.RedQueue.IRedQueue)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RedQueueAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
