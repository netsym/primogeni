
package jprime.Router;

import jprime.IModelNode;
import jprime.ModelNodeRecord;
import jprime.CNFTransport.ICNFTransport;
import jprime.Interface.IInterface;
import jprime.gen.ModelNodeVariable;
import jprime.visitors.IVizVisitor;

import org.python.core.PyObject;
public class Router extends jprime.gen.Router implements jprime.Router.IRouter {
	public Router(PyObject[] v, String[] n){super(v,n);}
	public Router(ModelNodeRecord rec){ super(rec); }
	public Router(jprime.IModelNode parent){
		super(parent);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.Router; }
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
    //Insert your user-specifIc code here (if any)
	/* (non-Javadoc)
	 * @see jprime.IModelNode#accept(jprime.routing.VizVisitor)
	 */
	public void accept(IVizVisitor visitor){
		visitor.visit(this);
	}
		
	public void setAsCNFController() {
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
	
	public boolean isCNFRouter() {
		for(IModelNode c : getAllChildren()) {
			if(c instanceof ICNFTransport) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isCNFController() {
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
	
	public boolean isTrafficPortal() {
		for(IModelNode c : getAllChildren()) {
			if(c instanceof IInterface)
				if(((IInterface)c).isTrafficPortal())
					return true;
		}
		return false;
	}

	public static boolean isRoutable(IRouter r) {
		return r.isTrafficPortal() || r.hasEmulationProtocol() || r.isCNFRouter();
	}
}
