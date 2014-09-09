
package jprime.ICMPv4Session;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ICMPv4SessionReplica extends jprime.gen.ICMPv4SessionReplica implements jprime.ICMPv4Session.IICMPv4Session {
	public ICMPv4SessionReplica(ModelNodeRecord rec){ super(rec); }
	public ICMPv4SessionReplica(PyObject[] v, String[] s){super(v,s);}
	public ICMPv4SessionReplica(String name, IModelNode parent, jprime.ICMPv4Session.IICMPv4Session referencedNode) {
		super(name, parent,(jprime.ICMPv4Session.IICMPv4Session)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ICMPv4SessionReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
