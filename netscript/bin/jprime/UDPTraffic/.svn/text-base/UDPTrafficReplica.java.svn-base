
package jprime.UDPTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class UDPTrafficReplica extends jprime.gen.UDPTrafficReplica implements jprime.UDPTraffic.IUDPTraffic {
	public UDPTrafficReplica(ModelNodeRecord rec){ super(rec); }
	public UDPTrafficReplica(PyObject[] v, String[] s){super(v,s);}
	public UDPTrafficReplica(String name, IModelNode parent, jprime.UDPTraffic.IUDPTraffic referencedNode) {
		super(name, parent,(jprime.UDPTraffic.IUDPTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.UDPTrafficReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
