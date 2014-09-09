
package jprime.RedQueue;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RedQueueAliasReplica extends jprime.gen.RedQueueAliasReplica implements jprime.RedQueue.IRedQueueAlias {
	public RedQueueAliasReplica(ModelNodeRecord rec){ super(rec); }
	public RedQueueAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public RedQueueAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RedQueueAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
