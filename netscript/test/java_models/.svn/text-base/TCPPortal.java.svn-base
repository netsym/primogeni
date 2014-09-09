import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.TCPMaster.ITCPMaster;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author merazo
 *
 */
public class TCPPortal extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public TCPPortal(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public TCPPortal(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		
		IRouter r1 = top.createRouter("router1");
		IInterface r1_h1= r1.createInterface("eth1");
		r1_h1.createTrafficPortal();
		IInterface r1_r2= r1.createInterface("ifr12");
		r1_r2.setBufferSize("64000");//bytes
        //r0_h1.setBufferSize("18480");	

		IRouter r2 = top.createRouter("router2");
		IInterface r2_r1 = r2.createInterface("ifr21");
		IInterface r2_h2 = r2.createInterface("ifr22");
		r2_r1.setBufferSize("64000");//bytes
        //r0_h1.setBufferSize("18480");

		IHost h2 = top.createHost("host2");
		IInterface h2_if0= h2.createInterface("if0");
		ITCPMaster tcp2 = h2.createTCPMaster();
		tcp2.setMss("1440");
	    tcp2.setTcpCA("cubic");
	    tcp2.setSamplingInterval("0.1");
	    h2.createHTTPServer();
		
		ILink link_r0_r1= top.createLink("router_router");
		link_r0_r1.setDelay((float)0.064);
		link_r0_r1.setBandwidth("5000000");
		link_r0_r1.createInterface(r1_r2);
		link_r0_r1.createInterface(r2_r1);
		
		ILink link_r1_h2= top.createLink("router_h2");
		link_r1_h2.setDelay("0.0");
		link_r1_h2.setBandwidth("100000000");
		link_r1_h2.createInterface(r2_h2);
		link_r1_h2.createInterface(h2_if0);		
		
		//add network reachability
		r1_h1.addReachableNetwork("10.10.0.0/16");
		r1_h1.setIpAddress("10.10.1.2");
		
		return top;
	}

}
