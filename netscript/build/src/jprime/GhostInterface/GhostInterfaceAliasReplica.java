
package jprime.GhostInterface;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostInterfaceAliasReplica extends jprime.gen.GhostInterfaceAliasReplica implements jprime.GhostInterface.IGhostInterfaceAlias {
	public GhostInterfaceAliasReplica(ModelNodeRecord rec){ super(rec); }
	public GhostInterfaceAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public GhostInterfaceAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostInterfaceAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
