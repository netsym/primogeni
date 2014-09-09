
package jprime.CNFApplication;

import java.util.HashMap;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFApplicationAliasReplica extends jprime.gen.CNFApplicationAliasReplica implements jprime.CNFApplication.ICNFApplicationAlias {
	public CNFApplicationAliasReplica(ModelNodeRecord rec){ super(rec); }
	public CNFApplicationAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public CNFApplicationAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFApplicationAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	public void addContentId(int cid, int size) {
		((ICNFApplication)deference()).addContentId(cid, size);
	}

	public HashMap<Integer,Integer> getContentIds() {
		return ((ICNFApplication)deference()).getContentIds();
	}
}
