
package jprime.UDPMaster;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class UDPMasterAliasReplica extends jprime.gen.UDPMasterAliasReplica implements jprime.UDPMaster.IUDPMasterAlias {
	public UDPMasterAliasReplica(ModelNodeRecord rec){ super(rec); }
	public UDPMasterAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public UDPMasterAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.UDPMasterAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
