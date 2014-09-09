
package jprime.ProtocolGraph;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolGraphReplica extends jprime.gen.ProtocolGraphReplica implements jprime.ProtocolGraph.IProtocolGraph {
	public ProtocolGraphReplica(ModelNodeRecord rec){ super(rec); }
	public ProtocolGraphReplica(PyObject[] v, String[] s){super(v,s);}
	public ProtocolGraphReplica(String name, IModelNode parent, jprime.ProtocolGraph.IProtocolGraph referencedNode) {
		super(name, parent,(jprime.ProtocolGraph.IProtocolGraph)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolGraphReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
