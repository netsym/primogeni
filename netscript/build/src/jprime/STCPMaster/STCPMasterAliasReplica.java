
package jprime.STCPMaster;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class STCPMasterAliasReplica extends jprime.gen.STCPMasterAliasReplica implements jprime.STCPMaster.ISTCPMasterAlias {
	public STCPMasterAliasReplica(ModelNodeRecord rec){ super(rec); }
	public STCPMasterAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public STCPMasterAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.STCPMasterAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
