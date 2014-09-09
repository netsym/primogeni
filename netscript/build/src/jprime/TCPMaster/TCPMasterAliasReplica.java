
package jprime.TCPMaster;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TCPMasterAliasReplica extends jprime.gen.TCPMasterAliasReplica implements jprime.TCPMaster.ITCPMasterAlias {
	public TCPMasterAliasReplica(ModelNodeRecord rec){ super(rec); }
	public TCPMasterAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public TCPMasterAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TCPMasterAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
