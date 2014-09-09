
package jprime.Router;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RouterAliasReplica extends jprime.gen.RouterAliasReplica implements jprime.Router.IRouterAlias {
	public RouterAliasReplica(PyObject[] v, String[] n){super(v,n);}
	public RouterAliasReplica(ModelNodeRecord rec){ super(rec); }
	public RouterAliasReplica(String name, IModelNode parent, jprime.ModelNodeAlias referencedNode) {
		super(name, parent,referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RouterAliasReplica; }
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	
	public void setAsCNFController() {
		((IRouter)deference()).setAsCNFController();
	}
	
	public boolean isCNFController() {
		return ((IRouter)deference()).isCNFController();
	}
	
	public boolean isCNFRouter() {
		return ((IRouter)deference()).isCNFRouter();
	}

	public boolean isTrafficPortal() {
		return ((IRouter)deference()).isTrafficPortal();
	}

}
