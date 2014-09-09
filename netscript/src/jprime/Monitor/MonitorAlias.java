
package jprime.Monitor;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class MonitorAlias extends jprime.gen.MonitorAlias implements jprime.Monitor.IMonitorAlias {
	public MonitorAlias(ModelNodeRecord rec){ super(rec); }
	public MonitorAlias(PyObject[] v, String[] s){super(v,s);}
	public MonitorAlias(IModelNode parent){
		super(parent);
	}
	public MonitorAlias(IModelNode parent, jprime.Monitor.IMonitor referencedNode) {
		super(parent,(jprime.Monitor.IMonitor)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.MonitorAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
