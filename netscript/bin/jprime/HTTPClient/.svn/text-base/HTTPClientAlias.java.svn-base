
package jprime.HTTPClient;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class HTTPClientAlias extends jprime.gen.HTTPClientAlias implements jprime.HTTPClient.IHTTPClientAlias {
	public HTTPClientAlias(ModelNodeRecord rec){ super(rec); }
	public HTTPClientAlias(PyObject[] v, String[] s){super(v,s);}
	public HTTPClientAlias(IModelNode parent){
		super(parent);
	}
	public HTTPClientAlias(IModelNode parent, jprime.HTTPClient.IHTTPClient referencedNode) {
		super(parent,(jprime.HTTPClient.IHTTPClient)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.HTTPClientAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
