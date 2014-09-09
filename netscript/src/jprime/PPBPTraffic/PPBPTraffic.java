
package jprime.PPBPTraffic;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class PPBPTraffic extends jprime.gen.PPBPTraffic implements jprime.PPBPTraffic.IPPBPTraffic {
	public PPBPTraffic(PyObject[] v, String[] s){super(v,s);}
	public PPBPTraffic(ModelNodeRecord rec){ super(rec); }
	public PPBPTraffic(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.PPBPTraffic;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
