
package jprime.RoutingProtocol;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RoutingProtocolAlias extends jprime.gen.RoutingProtocolAlias implements jprime.RoutingProtocol.IRoutingProtocolAlias {
	public RoutingProtocolAlias(ModelNodeRecord rec){ super(rec); }
	public RoutingProtocolAlias(PyObject[] v, String[] s){super(v,s);}
	public RoutingProtocolAlias(IModelNode parent){
		super(parent);
	}
	public RoutingProtocolAlias(IModelNode parent, jprime.RoutingProtocol.IRoutingProtocol referencedNode) {
		super(parent,(jprime.RoutingProtocol.IRoutingProtocol)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RoutingProtocolAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
