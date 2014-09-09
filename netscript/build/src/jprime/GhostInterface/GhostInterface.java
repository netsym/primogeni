
package jprime.GhostInterface;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostInterface extends jprime.gen.GhostInterface implements jprime.GhostInterface.IGhostInterface {
	public GhostInterface(PyObject[] v, String[] s){super(v,s);}
	public GhostInterface(ModelNodeRecord rec){ super(rec); }
	public GhostInterface(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostInterface;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
