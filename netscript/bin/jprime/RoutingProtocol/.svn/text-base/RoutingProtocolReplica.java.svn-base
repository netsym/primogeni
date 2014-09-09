
package jprime.RoutingProtocol;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RoutingProtocolReplica extends jprime.gen.RoutingProtocolReplica implements jprime.RoutingProtocol.IRoutingProtocol {
	public RoutingProtocolReplica(ModelNodeRecord rec){ super(rec); }
	public RoutingProtocolReplica(PyObject[] v, String[] s){super(v,s);}
	public RoutingProtocolReplica(String name, IModelNode parent, jprime.RoutingProtocol.IRoutingProtocol referencedNode) {
		super(name, parent,(jprime.RoutingProtocol.IRoutingProtocol)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RoutingProtocolReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
