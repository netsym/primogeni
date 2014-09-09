
package jprime.Traffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TrafficAlias extends jprime.gen.TrafficAlias implements jprime.Traffic.ITrafficAlias {
	public TrafficAlias(ModelNodeRecord rec){ super(rec); }
	public TrafficAlias(PyObject[] v, String[] s){super(v,s);}
	public TrafficAlias(IModelNode parent){
		super(parent);
	}
	public TrafficAlias(IModelNode parent, jprime.Traffic.ITraffic referencedNode) {
		super(parent,(jprime.Traffic.ITraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TrafficAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/* (non-Javadoc)
	 * @see jprime.Traffic.ITraffic#getNextDynamicUID()
	 */
	public Long getNextDynamicUID() {
		return ((ITrafficAlias)deference()).getNextDynamicUID();
	}
}
