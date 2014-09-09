
package jprime.OpenVPNEmulation;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class OpenVPNEmulationReplica extends jprime.gen.OpenVPNEmulationReplica implements jprime.OpenVPNEmulation.IOpenVPNEmulation {
	public OpenVPNEmulationReplica(ModelNodeRecord rec){ super(rec); }
	public OpenVPNEmulationReplica(PyObject[] v, String[] s){super(v,s);}
	public OpenVPNEmulationReplica(String name, IModelNode parent, jprime.OpenVPNEmulation.IOpenVPNEmulation referencedNode) {
		super(name, parent,(jprime.OpenVPNEmulation.IOpenVPNEmulation)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.OpenVPNEmulationReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
