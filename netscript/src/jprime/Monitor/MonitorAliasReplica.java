
package jprime.Monitor;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class MonitorAliasReplica extends jprime.gen.MonitorAliasReplica implements jprime.Monitor.IMonitorAlias {
	public MonitorAliasReplica(ModelNodeRecord rec){ super(rec); }
	public MonitorAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public MonitorAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.MonitorAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
