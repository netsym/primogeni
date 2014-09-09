
package jprime.HTTPClient;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class HTTPClientReplica extends jprime.gen.HTTPClientReplica implements jprime.HTTPClient.IHTTPClient {
	public HTTPClientReplica(ModelNodeRecord rec){ super(rec); }
	public HTTPClientReplica(PyObject[] v, String[] s){super(v,s);}
	public HTTPClientReplica(String name, IModelNode parent, jprime.HTTPClient.IHTTPClient referencedNode) {
		super(name, parent,(jprime.HTTPClient.IHTTPClient)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.HTTPClientReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
