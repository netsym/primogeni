
package jprime.OpenVPNEmulation;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class OpenVPNEmulation extends jprime.gen.OpenVPNEmulation implements jprime.OpenVPNEmulation.IOpenVPNEmulation {
	public OpenVPNEmulation(PyObject[] v, String[] s){super(v,s);}
	public OpenVPNEmulation(ModelNodeRecord rec){ super(rec); }
	public OpenVPNEmulation(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.OpenVPNEmulation;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
