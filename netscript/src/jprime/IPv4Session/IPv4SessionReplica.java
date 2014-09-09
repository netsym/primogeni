
package jprime.IPv4Session;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class IPv4SessionReplica extends jprime.gen.IPv4SessionReplica implements jprime.IPv4Session.IIPv4Session {
	public IPv4SessionReplica(ModelNodeRecord rec){ super(rec); }
	public IPv4SessionReplica(PyObject[] v, String[] s){super(v,s);}
	public IPv4SessionReplica(String name, IModelNode parent, jprime.IPv4Session.IIPv4Session referencedNode) {
		super(name, parent,(jprime.IPv4Session.IIPv4Session)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.IPv4SessionReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
