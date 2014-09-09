import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.TCPMaster.ITCPMaster;
import jprime.database.Database;
import jprime.util.ModelInterface;

public class TrafficPortalDumbbell1 extends ModelInterface{
	public TrafficPortalDumbbell1(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		//create the top most network
		INet topnet   = exp.createTopNet("top");
		topnet.createShortestPath();
		
		List<IInterface> ifaces=new LinkedList<IInterface>();

		IRouter r1 = topnet.createRouter("r1");
		IInterface r1_if1 = r1.createInterface("portal");
		IInterface r1_if2 = r1.createInterface("if1");
		r1_if1.createTrafficPortal();

		IRouter r2 = topnet.createRouter("r2");
		IInterface r2_if1 = r2.createInterface("if0");
		IInterface r2_if2 = r2.createInterface("if1");

		IHost h = topnet.createHost("h1");
		IInterface h_if1 = h.createInterface("if0");
		ITCPMaster tcpr2 = h.createTCPMaster();
        tcpr2.setMss("1440");
		h.createHTTPServer();

		ILink link1 = topnet.createLink();
		link1.createInterface(r1_if2);
		link1.createInterface(r2_if1);

		ILink link2 = topnet.createLink();
		link2.createInterface(h_if1);
		link2.createInterface(r2_if2);

		ifaces.add(r1_if1);
		ifaces.add(r1_if2);
		ifaces.add(r2_if1);
		ifaces.add(r2_if2);
		ifaces.add(h_if1);

		for(IInterface iface : ifaces) {
			iface.setBitRate("1000000000"); //1000Mbps
			iface.setLatency("0.0");
			iface.setBufferSize("65536"); //64K
		}
		link1.setDelay("0.0001"); //1 ms
		link2.setDelay("0.000001"); //1 ms
		int b = Integer.parseInt(System.getProperty("MBS","100"))*1000000;
		link1.setBandwidth(Integer.toString(b)); 
		link2.setBandwidth("1000000000"); //1G 

		
		//add network reachability
		r1_if1.addReachableNetwork("12.1.0.0/16");
		r1_if1.setIpAddress("12.1.0.100");
				
		return topnet;
		
	}
}
