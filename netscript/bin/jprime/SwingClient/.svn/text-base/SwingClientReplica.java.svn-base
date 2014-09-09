
package jprime.SwingClient;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingClientReplica extends jprime.gen.SwingClientReplica implements jprime.SwingClient.ISwingClient {
	public SwingClientReplica(ModelNodeRecord rec){ super(rec); }
	public SwingClientReplica(PyObject[] v, String[] s){super(v,s);}
	public SwingClientReplica(String name, IModelNode parent, jprime.SwingClient.ISwingClient referencedNode) {
		super(name, parent,(jprime.SwingClient.ISwingClient)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingClientReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
