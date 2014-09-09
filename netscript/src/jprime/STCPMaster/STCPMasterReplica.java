
package jprime.STCPMaster;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class STCPMasterReplica extends jprime.gen.STCPMasterReplica implements jprime.STCPMaster.ISTCPMaster {
	public STCPMasterReplica(ModelNodeRecord rec){ super(rec); }
	public STCPMasterReplica(PyObject[] v, String[] s){super(v,s);}
	public STCPMasterReplica(String name, IModelNode parent, jprime.STCPMaster.ISTCPMaster referencedNode) {
		super(name, parent,(jprime.STCPMaster.ISTCPMaster)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.STCPMasterReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)
}
