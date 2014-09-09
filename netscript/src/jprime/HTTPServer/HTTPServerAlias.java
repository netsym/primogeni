
package jprime.HTTPServer;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class HTTPServerAlias extends jprime.gen.HTTPServerAlias implements jprime.HTTPServer.IHTTPServerAlias {
	public HTTPServerAlias(ModelNodeRecord rec){ super(rec); }
	public HTTPServerAlias(PyObject[] v, String[] s){super(v,s);}
	public HTTPServerAlias(IModelNode parent){
		super(parent);
	}
	public HTTPServerAlias(IModelNode parent, jprime.HTTPServer.IHTTPServer referencedNode) {
		super(parent,(jprime.HTTPServer.IHTTPServer)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.HTTPServerAlias;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
