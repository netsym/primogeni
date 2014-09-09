
package jprime.SwingServer;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingServerAlias extends jprime.gen.SwingServerAlias implements jprime.SwingServer.ISwingServerAlias {
	public SwingServerAlias(ModelNodeRecord rec){ super(rec); }
	public SwingServerAlias(PyObject[] v, String[] s){super(v,s);}
	public SwingServerAlias(IModelNode parent){
		super(parent);
	}
	public SwingServerAlias(IModelNode parent, jprime.SwingServer.ISwingServer referencedNode) {
		super(parent,(jprime.SwingServer.ISwingServer)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingServerAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
