
package jprime.SwingServer;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingServer extends jprime.gen.SwingServer implements jprime.SwingServer.ISwingServer {
	public SwingServer(PyObject[] v, String[] s){super(v,s);}
	public SwingServer(ModelNodeRecord rec){ super(rec); }
	public SwingServer(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingServer;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
