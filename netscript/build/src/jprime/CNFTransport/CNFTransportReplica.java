
package jprime.CNFTransport;
import java.util.ArrayList;
import jprime.IModelNode;
import jprime.ModelNodeRecord;

import org.python.core.PyObject;
public class CNFTransportReplica extends jprime.gen.CNFTransportReplica implements jprime.CNFTransport.ICNFTransport {
	private ArrayList<Long> cnf_rids = null;
	
	public CNFTransportReplica(ModelNodeRecord rec){ super(rec); }
	public CNFTransportReplica(PyObject[] v, String[] s){super(v,s);}
	public CNFTransportReplica(String name, IModelNode parent, jprime.CNFTransport.ICNFTransport referencedNode) {
		super(name, parent,(jprime.CNFTransport.ICNFTransport)referencedNode);
	}

	/* (non-Javadoc)
	* @see jprime.IModelNode#getTypeId()
	 */
	public int getTypeId(){ return jprime.EntityFactory.CNFTransportReplica;}
	public void accept(jprime.visitors.IGenericVisitor visitor){visitor.visit(this);}
	public void addCNFRouter(long rid) {
		if(isReplica())
			convertToReal();
		if(cnf_rids == null) {
			if(this.getCnfRouters()== null)
			cnf_rids = new ArrayList<Long>();
			else
			cnf_rids = CNFTransport.decodeCNFRouterVector(this.getCnfRouters().toString());
		}
		cnf_rids.add(rid);
		try {
			this.setAttribute(jprime.gen.ModelNodeVariable.cnf_routers(), CNFTransport.encodeCNFRouterVector(cnf_rids));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	
			
		}
	
	public ArrayList<Long> getCNFRouterIds() {
		if(isReplica())
			convertToReal();
		if(cnf_rids == null) {
			if(this.getCnfRouters() == null){
				return null;
				}
			cnf_rids = CNFTransport.decodeCNFRouterVector(this.getCnfRouters().toString());
		}
		return cnf_rids;
	}
}
