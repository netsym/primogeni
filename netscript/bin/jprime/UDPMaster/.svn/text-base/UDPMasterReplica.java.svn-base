
package jprime.UDPMaster;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class UDPMasterReplica extends jprime.gen.UDPMasterReplica implements jprime.UDPMaster.IUDPMaster {
	public UDPMasterReplica(ModelNodeRecord rec){ super(rec); }
	public UDPMasterReplica(PyObject[] v, String[] s){super(v,s);}
	public UDPMasterReplica(String name, IModelNode parent, jprime.UDPMaster.IUDPMaster referencedNode) {
		super(name, parent,(jprime.UDPMaster.IUDPMaster)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.UDPMasterReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
