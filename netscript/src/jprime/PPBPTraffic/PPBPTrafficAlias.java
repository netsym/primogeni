
package jprime.PPBPTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class PPBPTrafficAlias extends jprime.gen.PPBPTrafficAlias implements jprime.PPBPTraffic.IPPBPTrafficAlias {
	public PPBPTrafficAlias(ModelNodeRecord rec){ super(rec); }
	public PPBPTrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public PPBPTrafficAlias(IModelNode parent){
		super(parent);
	}
	public PPBPTrafficAlias(IModelNode parent, jprime.PPBPTraffic.IPPBPTraffic referencedNode) {
		super(parent,(jprime.PPBPTraffic.IPPBPTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.PPBPTrafficAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
