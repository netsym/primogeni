
package jprime.SwingClient;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingClient extends jprime.gen.SwingClient implements jprime.SwingClient.ISwingClient {
	public SwingClient(PyObject[] v, String[] s){super(v,s);}
	public SwingClient(ModelNodeRecord rec){ super(rec); }
	public SwingClient(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingClient;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
