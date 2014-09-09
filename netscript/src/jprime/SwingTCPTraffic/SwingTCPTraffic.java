
package jprime.SwingTCPTraffic;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingTCPTraffic extends jprime.gen.SwingTCPTraffic implements jprime.SwingTCPTraffic.ISwingTCPTraffic {
	public SwingTCPTraffic(PyObject[] v, String[] s){super(v,s);}
	public SwingTCPTraffic(ModelNodeRecord rec){ super(rec); }
	public SwingTCPTraffic(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingTCPTraffic;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
