
package jprime.HTTPServer;

import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class HTTPServer extends jprime.gen.HTTPServer implements jprime.HTTPServer.IHTTPServer {
	public HTTPServer(PyObject[] v, String[] s){super(v,s);}
	public HTTPServer(ModelNodeRecord rec){ super(rec); }
	public HTTPServer(jprime.IModelNode parent){ super(parent); }

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.HTTPServer;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
