
package jprime.ProtocolGraph;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolGraphAlias extends jprime.gen.ProtocolGraphAlias implements jprime.ProtocolGraph.IProtocolGraphAlias {
	public ProtocolGraphAlias(ModelNodeRecord rec){ super(rec); }
	public ProtocolGraphAlias(PyObject[] v, String[] s){super(v,s);}
	public ProtocolGraphAlias(IModelNode parent){
		super(parent);
	}
	public ProtocolGraphAlias(IModelNode parent, jprime.ProtocolGraph.IProtocolGraph referencedNode) {
		super(parent,(jprime.ProtocolGraph.IProtocolGraph)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolGraphAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
