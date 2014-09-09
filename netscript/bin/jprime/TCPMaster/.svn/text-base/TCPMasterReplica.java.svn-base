
package jprime.TCPMaster;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TCPMasterReplica extends jprime.gen.TCPMasterReplica implements jprime.TCPMaster.ITCPMaster {
	public TCPMasterReplica(ModelNodeRecord rec){ super(rec); }
	public TCPMasterReplica(PyObject[] v, String[] s){super(v,s);}
	public TCPMasterReplica(String name, IModelNode parent, jprime.TCPMaster.ITCPMaster referencedNode) {
		super(name, parent,(jprime.TCPMaster.ITCPMaster)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TCPMasterReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
