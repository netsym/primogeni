
package jprime.IPv4Session;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class IPv4SessionAliasReplica extends jprime.gen.IPv4SessionAliasReplica implements jprime.IPv4Session.IIPv4SessionAlias {
	public IPv4SessionAliasReplica(ModelNodeRecord rec){ super(rec); }
	public IPv4SessionAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public IPv4SessionAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.IPv4SessionAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
