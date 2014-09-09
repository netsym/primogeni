
package jprime.RedQueue;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RedQueue extends jprime.gen.RedQueue implements jprime.RedQueue.IRedQueue {
	public RedQueue(PyObject[] v, String[] s){super(v,s);}
	public RedQueue(ModelNodeRecord rec){ super(rec); }
	public RedQueue(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RedQueue;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
