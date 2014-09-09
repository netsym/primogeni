
package jprime.SwingTCPTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingTCPTrafficReplica extends jprime.gen.SwingTCPTrafficReplica implements jprime.SwingTCPTraffic.ISwingTCPTraffic {
	public SwingTCPTrafficReplica(ModelNodeRecord rec){ super(rec); }
	public SwingTCPTrafficReplica(PyObject[] v, String[] s){super(v,s);}
	public SwingTCPTrafficReplica(String name, IModelNode parent, jprime.SwingTCPTraffic.ISwingTCPTraffic referencedNode) {
		super(name, parent,(jprime.SwingTCPTraffic.ISwingTCPTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingTCPTrafficReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
