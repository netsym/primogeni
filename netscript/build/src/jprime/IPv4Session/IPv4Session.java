
package jprime.IPv4Session;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class IPv4Session extends jprime.gen.IPv4Session implements jprime.IPv4Session.IIPv4Session {
	public IPv4Session(PyObject[] v, String[] s){super(v,s);}
	public IPv4Session(ModelNodeRecord rec){ super(rec); }
	public IPv4Session(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.IPv4Session;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
