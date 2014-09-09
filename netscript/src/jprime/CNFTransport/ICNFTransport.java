
package jprime.CNFTransport;
import java.util.ArrayList;

public interface ICNFTransport extends jprime.gen.ICNFTransport {
	
	/**
	 * Add an rid within this network to this controller
	 * @param rid
	 */
	public void addCNFRouter(long rid); 
	
	
	/**
	 * @return
	 */
	public ArrayList<Long> getCNFRouterIds();
}
