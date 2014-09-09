
package jprime.CentralizedTrafficType;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CentralizedTrafficTypeAlias extends jprime.gen.CentralizedTrafficTypeAlias implements jprime.CentralizedTrafficType.ICentralizedTrafficTypeAlias {
	public CentralizedTrafficTypeAlias(ModelNodeRecord rec){ super(rec); }
	public CentralizedTrafficTypeAlias(PyObject[] v, String[] s){super(v,s);}
	public CentralizedTrafficTypeAlias(IModelNode parent){
		super(parent);
	}
	public CentralizedTrafficTypeAlias(IModelNode parent, jprime.CentralizedTrafficType.ICentralizedTrafficType referencedNode) {
		super(parent,(jprime.CentralizedTrafficType.ICentralizedTrafficType)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CentralizedTrafficTypeAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
