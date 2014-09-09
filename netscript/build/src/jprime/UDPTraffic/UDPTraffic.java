
package jprime.UDPTraffic;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class UDPTraffic extends jprime.gen.UDPTraffic implements jprime.UDPTraffic.IUDPTraffic {
	public UDPTraffic(PyObject[] v, String[] s){super(v,s);}
	public UDPTraffic(ModelNodeRecord rec){ super(rec); }
	public UDPTraffic(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.UDPTraffic;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)

}
