
package jprime.SwingTCPTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class SwingTCPTrafficAliasReplica extends jprime.gen.SwingTCPTrafficAliasReplica implements jprime.SwingTCPTraffic.ISwingTCPTrafficAlias {
	public SwingTCPTrafficAliasReplica(ModelNodeRecord rec){ super(rec); }
	public SwingTCPTrafficAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public SwingTCPTrafficAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.SwingTCPTrafficAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
