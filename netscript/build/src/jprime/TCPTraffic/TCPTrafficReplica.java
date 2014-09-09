
package jprime.TCPTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TCPTrafficReplica extends jprime.gen.TCPTrafficReplica implements jprime.TCPTraffic.ITCPTraffic {
	public TCPTrafficReplica(ModelNodeRecord rec){ super(rec); }
	public TCPTrafficReplica(PyObject[] v, String[] s){super(v,s);}
	public TCPTrafficReplica(String name, IModelNode parent, jprime.TCPTraffic.ITCPTraffic referencedNode) {
		super(name, parent,(jprime.TCPTraffic.ITCPTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TCPTrafficReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
