
package jprime.GhostInterface;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class GhostInterfaceAlias extends jprime.gen.GhostInterfaceAlias implements jprime.GhostInterface.IGhostInterfaceAlias {
	public GhostInterfaceAlias(ModelNodeRecord rec){ super(rec); }
	public GhostInterfaceAlias(PyObject[] v, String[] s){super(v,s);}
	public GhostInterfaceAlias(IModelNode parent){
		super(parent);
	}
	public GhostInterfaceAlias(IModelNode parent, jprime.GhostInterface.IGhostInterface referencedNode) {
		super(parent,(jprime.GhostInterface.IGhostInterface)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.GhostInterfaceAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
