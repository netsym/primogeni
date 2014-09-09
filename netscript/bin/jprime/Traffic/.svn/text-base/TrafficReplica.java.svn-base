
package jprime.Traffic;

import java.util.List;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.ModelNodeRecord;
import jprime.State;

import org.python.core.PyObject;
public class TrafficReplica extends jprime.gen.TrafficReplica implements jprime.Traffic.ITraffic {
	//transient
	private Long next_uid = null;
	
	public TrafficReplica(ModelNodeRecord rec){ super(rec); }
	public TrafficReplica(PyObject[] v, String[] s){super(v,s);}
	public TrafficReplica(String name, IModelNode parent, jprime.Traffic.ITraffic referencedNode) {
		super(name, parent,(jprime.Traffic.ITraffic)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TrafficReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	/* (non-Javadoc)
	 * @see jprime.Traffic.ITraffic#getNextDynamicUID()
	 */
	public Long getNextDynamicUID() {
		if(next_uid == null) {
			if(getMetadata().getState().lt(State.COMPILED)) {
				throw new RuntimeException("Can only call this after the model has been compiled.");
			}
			final List<ModelNode> l = this.getAllChildren();
			if(l.size()==0) {
				this.next_uid=getMinUID();
			}
			else {
				this.next_uid=l.get(l.size()-1).getUID()+1;
			}
		}
		if(next_uid >= getUID())
			throw new RuntimeException("There are no more free UIDs to dynamically assign!");
		return next_uid++;
	}
}
