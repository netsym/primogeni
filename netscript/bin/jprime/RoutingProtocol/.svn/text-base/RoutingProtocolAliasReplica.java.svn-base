
package jprime.RoutingProtocol;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RoutingProtocolAliasReplica extends jprime.gen.RoutingProtocolAliasReplica implements jprime.RoutingProtocol.IRoutingProtocolAlias {
	public RoutingProtocolAliasReplica(ModelNodeRecord rec){ super(rec); }
	public RoutingProtocolAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public RoutingProtocolAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RoutingProtocolAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
