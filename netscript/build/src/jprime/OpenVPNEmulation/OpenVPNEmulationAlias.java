
package jprime.OpenVPNEmulation;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class OpenVPNEmulationAlias extends jprime.gen.OpenVPNEmulationAlias implements jprime.OpenVPNEmulation.IOpenVPNEmulationAlias {
	public OpenVPNEmulationAlias(ModelNodeRecord rec){ super(rec); }
	public OpenVPNEmulationAlias(PyObject[] v, String[] s){super(v,s);}
	public OpenVPNEmulationAlias(IModelNode parent){
		super(parent);
	}
	public OpenVPNEmulationAlias(IModelNode parent, jprime.OpenVPNEmulation.IOpenVPNEmulation referencedNode) {
		super(parent,(jprime.OpenVPNEmulation.IOpenVPNEmulation)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.OpenVPNEmulationAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
