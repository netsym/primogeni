
package jprime.HTTPServer;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class HTTPServerReplica extends jprime.gen.HTTPServerReplica implements jprime.HTTPServer.IHTTPServer {
	public HTTPServerReplica(ModelNodeRecord rec){ super(rec); }
	public HTTPServerReplica(PyObject[] v, String[] s){super(v,s);}
	public HTTPServerReplica(String name, IModelNode parent, jprime.HTTPServer.IHTTPServer referencedNode) {
		super(name, parent,(jprime.HTTPServer.IHTTPServer)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.HTTPServerReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
