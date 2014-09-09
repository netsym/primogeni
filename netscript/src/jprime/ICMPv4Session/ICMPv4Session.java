
package jprime.ICMPv4Session;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ICMPv4Session extends jprime.gen.ICMPv4Session implements jprime.ICMPv4Session.IICMPv4Session {
	public ICMPv4Session(PyObject[] v, String[] s){super(v,s);}
	public ICMPv4Session(ModelNodeRecord rec){ super(rec); }
	public ICMPv4Session(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ICMPv4Session;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
