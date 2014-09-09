
package jprime.UDPMaster;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class UDPMasterAlias extends jprime.gen.UDPMasterAlias implements jprime.UDPMaster.IUDPMasterAlias {
	public UDPMasterAlias(ModelNodeRecord rec){ super(rec); }
	public UDPMasterAlias(PyObject[] v, String[] s){super(v,s);}
	public UDPMasterAlias(IModelNode parent){
		super(parent);
	}
	public UDPMasterAlias(IModelNode parent, jprime.UDPMaster.IUDPMaster referencedNode) {
		super(parent,(jprime.UDPMaster.IUDPMaster)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.UDPMasterAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
