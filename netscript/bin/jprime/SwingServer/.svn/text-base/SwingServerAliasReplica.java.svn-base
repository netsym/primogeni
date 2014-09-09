
package jprime.SwingServer;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingServerAliasReplica extends jprime.gen.SwingServerAliasReplica implements jprime.SwingServer.ISwingServerAlias {
	public SwingServerAliasReplica(ModelNodeRecord rec){ super(rec); }
	public SwingServerAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public SwingServerAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingServerAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
