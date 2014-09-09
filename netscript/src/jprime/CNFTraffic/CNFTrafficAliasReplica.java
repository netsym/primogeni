
package jprime.CNFTraffic;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTrafficAliasReplica extends jprime.gen.CNFTrafficAliasReplica implements jprime.CNFTraffic.ICNFTrafficAlias {
	public CNFTrafficAliasReplica(ModelNodeRecord rec){ super(rec); }
	public CNFTrafficAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public CNFTrafficAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTrafficAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
