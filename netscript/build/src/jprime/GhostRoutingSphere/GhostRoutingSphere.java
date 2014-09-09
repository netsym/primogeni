
package jprime.GhostRoutingSphere;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostRoutingSphere extends jprime.gen.GhostRoutingSphere implements jprime.GhostRoutingSphere.IGhostRoutingSphere {
	public GhostRoutingSphere(PyObject[] v, String[] s){super(v,s);}
	public GhostRoutingSphere(ModelNodeRecord rec){ super(rec); }
	public GhostRoutingSphere(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostRoutingSphere;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
