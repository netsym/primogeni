
package jprime.TAPEmulation;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class TAPEmulationAliasReplica extends jprime.gen.TAPEmulationAliasReplica implements jprime.TAPEmulation.ITAPEmulationAlias {
	public TAPEmulationAliasReplica(ModelNodeRecord rec){ super(rec); }
	public TAPEmulationAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public TAPEmulationAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.TAPEmulationAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
