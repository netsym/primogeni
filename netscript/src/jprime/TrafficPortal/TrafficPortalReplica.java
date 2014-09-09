
package jprime.TrafficPortal;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TrafficPortalReplica extends jprime.gen.TrafficPortalReplica implements jprime.TrafficPortal.ITrafficPortal {
	public TrafficPortalReplica(ModelNodeRecord rec){ super(rec); }
	public TrafficPortalReplica(PyObject[] v, String[] s){super(v,s);}
	public TrafficPortalReplica(String name, IModelNode parent, jprime.TrafficPortal.ITrafficPortal referencedNode) {
		super(name, parent,(jprime.TrafficPortal.ITrafficPortal)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TrafficPortalReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	//Insert your user-specific code here (if any)
}

