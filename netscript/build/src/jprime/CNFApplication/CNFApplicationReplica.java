
package jprime.CNFApplication;

import java.util.HashMap;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.variable.ModelNodeVariable;

import org.python.core.PyObject;
public class CNFApplicationReplica extends jprime.gen.CNFApplicationReplica implements jprime.CNFApplication.ICNFApplication {
	//transient
	private HashMap<Integer, Integer> contents = null;
	
	public CNFApplicationReplica(ModelNodeRecord rec){ super(rec); }
	public CNFApplicationReplica(PyObject[] v, String[] s){super(v,s);}
	public CNFApplicationReplica(String name, IModelNode parent, jprime.CNFApplication.ICNFApplication referencedNode) {
		super(name, parent,(jprime.CNFApplication.ICNFApplication)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFApplicationReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	

	public void addContentId(int cid, int size) {
		if(isReplica())
			convertToReal();
		if(contents == null) {
			if (this.getCnfContentSizes() == null)
				contents = new HashMap<Integer, Integer>();
			else
				contents = CNFApplication.decodeContentMap(this.getCnfContentSizes().toString());
		}
		contents.put(cid, size);
		try {
			this.setAttribute(ModelNodeVariable.cnf_content_sizes(), CNFApplication.encodeContentMap(contents));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public HashMap<Integer,Integer> getContentIds() {
		if(isReplica())
			convertToReal();
		if(contents == null) {
			if(this.getCnfContentSizes() == null)
				return null;
			contents = CNFApplication.decodeContentMap(this.getCnfContentSizes().toString());
		}
		return contents;
	}


}
