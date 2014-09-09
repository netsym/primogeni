
package jprime.ProtocolGraph;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolGraphAliasReplica extends jprime.gen.ProtocolGraphAliasReplica implements jprime.ProtocolGraph.IProtocolGraphAlias {
	public ProtocolGraphAliasReplica(ModelNodeRecord rec){ super(rec); }
	public ProtocolGraphAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public ProtocolGraphAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolGraphAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
