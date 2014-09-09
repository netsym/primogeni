
package jprime.NicQueue;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class NicQueue extends jprime.gen.NicQueue implements jprime.NicQueue.INicQueue {
	public NicQueue(PyObject[] v, String[] s){super(v,s);}
	public NicQueue(ModelNodeRecord rec){ super(rec); }
	public NicQueue(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.NicQueue;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
