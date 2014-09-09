
package jprime.HTTPServer;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class HTTPServerAliasReplica extends jprime.gen.HTTPServerAliasReplica implements jprime.HTTPServer.IHTTPServerAlias {
	public HTTPServerAliasReplica(ModelNodeRecord rec){ super(rec); }
	public HTTPServerAliasReplica(PyObject[] v, String[] s){super(v,s);}
	public HTTPServerAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.HTTPServerAliasReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
//Insert your user-specific code here (if any)
}
