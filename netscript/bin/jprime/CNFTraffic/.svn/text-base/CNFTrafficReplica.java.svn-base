
package jprime.CNFTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTrafficReplica extends jprime.gen.CNFTrafficReplica implements jprime.CNFTraffic.ICNFTraffic {
	public CNFTrafficReplica(ModelNodeRecord rec){ super(rec); }
	public CNFTrafficReplica(PyObject[] v, String[] s){super(v,s);}
	public CNFTrafficReplica(String name, IModelNode parent, jprime.CNFTraffic.ICNFTraffic referencedNode) {
		super(name, parent,(jprime.CNFTraffic.ICNFTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTrafficReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
