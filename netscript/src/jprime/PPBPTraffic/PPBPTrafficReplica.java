
package jprime.PPBPTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class PPBPTrafficReplica extends jprime.gen.PPBPTrafficReplica implements jprime.PPBPTraffic.IPPBPTraffic {
	public PPBPTrafficReplica(ModelNodeRecord rec){ super(rec); }
	public PPBPTrafficReplica(PyObject[] v, String[] s){super(v,s);}
	public PPBPTrafficReplica(String name, IModelNode parent, jprime.PPBPTraffic.IPPBPTraffic referencedNode) {
		super(name, parent,(jprime.PPBPTraffic.IPPBPTraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.PPBPTrafficReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
