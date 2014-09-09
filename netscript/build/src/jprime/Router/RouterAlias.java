
package jprime.Router;

import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class RouterAlias extends jprime.gen.RouterAlias implements jprime.Router.IRouterAlias {
	public RouterAlias(PyObject[] v, String[] n){super(v,n);}
	public RouterAlias(ModelNodeRecord rec){ super(rec); }
	public RouterAlias(IModelNode parent){ super(parent); }
	public RouterAlias(IModelNode parent, jprime.Router.IRouter referencedNode) {
		super(parent,(jprime.Router.IRouter)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RouterAlias; }
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
