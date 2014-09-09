
package jprime.IPv4Session;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class IPv4SessionAlias extends jprime.gen.IPv4SessionAlias implements jprime.IPv4Session.IIPv4SessionAlias {
	public IPv4SessionAlias(ModelNodeRecord rec){ super(rec); }
	public IPv4SessionAlias(PyObject[] v, String[] s){super(v,s);}
	public IPv4SessionAlias(IModelNode parent){
		super(parent);
	}
	public IPv4SessionAlias(IModelNode parent, jprime.IPv4Session.IIPv4Session referencedNode) {
		super(parent,(jprime.IPv4Session.IIPv4Session)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.IPv4SessionAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
