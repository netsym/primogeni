
package jprime.CNFApplication;

import java.util.HashMap;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFApplicationAlias extends jprime.gen.CNFApplicationAlias implements jprime.CNFApplication.ICNFApplicationAlias {
	public CNFApplicationAlias(ModelNodeRecord rec){ super(rec); }
	public CNFApplicationAlias(PyObject[] v, String[] s){super(v,s);}
	public CNFApplicationAlias(IModelNode parent){
		super(parent);
	}
	public CNFApplicationAlias(IModelNode parent, jprime.CNFApplication.ICNFApplication referencedNode) {
		super(parent,(jprime.CNFApplication.ICNFApplication)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFApplicationAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	public void addContentId(int cid, int size) {
		((ICNFApplication)deference()).addContentId(cid, size);
	}
	
	public HashMap<Integer,Integer> getContentIds() {
		return ((ICNFApplication)deference()).getContentIds();
	}
}
