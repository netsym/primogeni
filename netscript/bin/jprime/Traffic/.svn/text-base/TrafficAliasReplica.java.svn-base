
package jprime.Traffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TrafficAliasReplica extends jprime.gen.TrafficAliasReplica implements jprime.Traffic.ITrafficAlias {
	public TrafficAliasReplica(ModelNodeRecord rec){ super(rec); }
	public TrafficAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public TrafficAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TrafficAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/* (non-Javadoc)
	 * @see jprime.Traffic.ITraffic#getNextDynamicUID()
	 */
	public Long getNextDynamicUID() {
		return ((ITrafficAlias)deference()).getNextDynamicUID();
	}
}
