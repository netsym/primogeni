
package jprime.ProtocolGraph;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class ProtocolGraph extends jprime.gen.ProtocolGraph implements jprime.ProtocolGraph.IProtocolGraph {
	public ProtocolGraph(PyObject[] v, String[] s){super(v,s);}
	public ProtocolGraph(ModelNodeRecord rec){ super(rec); }
	public ProtocolGraph(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.ProtocolGraph;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
