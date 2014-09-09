
package jprime.SwingClient;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingClientAliasReplica extends jprime.gen.SwingClientAliasReplica implements jprime.SwingClient.ISwingClientAlias {
	public SwingClientAliasReplica(ModelNodeRecord rec){ super(rec); }
	public SwingClientAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public SwingClientAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingClientAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
