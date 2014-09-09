
package jprime.Router;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.CNFTransport.ICNFTransport;
import jprime.Interface.IInterface;
import jprime.variable.ModelNodeVariable;

import org.python.core.PyObject;
public class RouterReplica extends jprime.gen.RouterReplica implements jprime.Router.IRouter {
	public RouterReplica(PyObject[] v, String[] n){super(v,n);}
	public RouterReplica(ModelNodeRecord rec){ super(rec); }
	public RouterReplica(String name, IModelNode parent, jprime.Router.IRouter referencedNode) {
		super(name, parent,(jprime.Router.IRouter)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.RouterReplica; }
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}

	public void setAsCNFController() {
		if(isReplica()) {
			((IRouter)getReplicatedNode()).setAsCNFController();
		}
		else {
			ICNFTransport cnf = null;
			for(IModelNode c : getAllChildren()) {
				if(c instanceof ICNFTransport) {
					cnf = (ICNFTransport)c;
					break;
				}
			}
			if(cnf == null)
				cnf = this.createCNFTransport();
			try {
				cnf.setAttribute(ModelNodeVariable.is_controller(), "true");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	public boolean isCNFRouter() {
		if(isReplica()) {
			return ((IRouter)getReplicatedNode()).isCNFRouter();
		}
		for(IModelNode c : getAllChildren()) {
			if(c instanceof ICNFTransport) {
				return true;
			}
		}
		return false;
	}

	public boolean isCNFController() {
		if(isReplica()) {
			return ((IRouter)getReplicatedNode()).isCNFController();
		}
		else {
			ICNFTransport cnf = null;
			for(IModelNode c : getAllChildren()) {
				if(c instanceof ICNFTransport) {
					cnf = (ICNFTransport)c;
					break;
				}
			}
			if(cnf == null || cnf.getIsController() == null)
				return false;
			return cnf.getIsController().getValue();
		}
	}
	
	public boolean isTrafficPortal() {
		if(isReplica())
			return ((IRouter)getReplicatedNode()).isTrafficPortal();
		for(IModelNode c : getAllChildren()) {
			if(c instanceof IInterface)
				if(((IInterface)c).isTrafficPortal())
					return true;
		}
		return false;
	}

}
