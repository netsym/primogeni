
package jprime.SwingClient;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingClientAlias extends jprime.gen.SwingClientAlias implements jprime.SwingClient.ISwingClientAlias {
	public SwingClientAlias(ModelNodeRecord rec){ super(rec); }
	public SwingClientAlias(PyObject[] v, String[] s){super(v,s);}
	public SwingClientAlias(IModelNode parent){
		super(parent);
	}
	public SwingClientAlias(IModelNode parent, jprime.SwingClient.ISwingClient referencedNode) {
		super(parent,(jprime.SwingClient.ISwingClient)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingClientAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
