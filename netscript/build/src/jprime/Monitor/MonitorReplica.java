
package jprime.Monitor;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class MonitorReplica extends jprime.gen.MonitorReplica implements jprime.Monitor.IMonitor {
	public MonitorReplica(ModelNodeRecord rec){ super(rec); }
	public MonitorReplica(PyObject[] v, String[] s){super(v,s);}
	public MonitorReplica(String name, IModelNode parent, jprime.Monitor.IMonitor referencedNode) {
		super(name, parent,(jprime.Monitor.IMonitor)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.MonitorReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
