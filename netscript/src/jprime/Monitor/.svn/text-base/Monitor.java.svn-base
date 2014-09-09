
package jprime.Monitor;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class Monitor extends jprime.gen.Monitor implements jprime.Monitor.IMonitor {
	public Monitor(PyObject[] v, String[] s){super(v,s);}
	public Monitor(ModelNodeRecord rec){ super(rec); }
	public Monitor(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.Monitor;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
