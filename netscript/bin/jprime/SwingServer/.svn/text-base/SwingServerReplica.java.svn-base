
package jprime.SwingServer;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingServerReplica extends jprime.gen.SwingServerReplica implements jprime.SwingServer.ISwingServer {
	public SwingServerReplica(ModelNodeRecord rec){ super(rec); }
	public SwingServerReplica(PyObject[] v, String[] s){super(v,s);}
	public SwingServerReplica(String name, IModelNode parent, jprime.SwingServer.ISwingServer referencedNode) {
		super(name, parent,(jprime.SwingServer.ISwingServer)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingServerReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
