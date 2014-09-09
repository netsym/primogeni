
package jprime.CNFTraffic;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTraffic extends jprime.gen.CNFTraffic implements jprime.CNFTraffic.ICNFTraffic {
	public CNFTraffic(PyObject[] v, String[] s){super(v,s);}
	public CNFTraffic(ModelNodeRecord rec){ super(rec); }
	public CNFTraffic(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTraffic;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
