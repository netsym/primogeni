
package jprime.CNFTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTrafficAlias extends jprime.gen.CNFTrafficAlias implements jprime.CNFTraffic.ICNFTrafficAlias {
	public CNFTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public CNFTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public CNFTrafficAlias(IModelNode parent){
		super(parent);
	}
	public CNFTrafficAlias(IModelNode parent, jprime.CNFTraffic.ICNFTraffic referencedNode) {
		super(parent,(jprime.CNFTraffic.ICNFTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTrafficAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
