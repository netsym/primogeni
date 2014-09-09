
package jprime.DropTailQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class DropTailQueueAliasReplica extends jprime.gen.DropTailQueueAliasReplica implements jprime.DropTailQueue.IDropTailQueueAlias {
	public DropTailQueueAliasReplica(ModelNodeRecord rec){ super(rec); }
	public DropTailQueueAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public DropTailQueueAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.DropTailQueueAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
