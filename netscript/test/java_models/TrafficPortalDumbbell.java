import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jprime.Experiment;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

public class TrafficPortalDumbbell extends ModelInterface{
	public TrafficPortalDumbbell(Database db, Experiment exp) {
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
		IInterface r2_if2 = r2.createInterface("portal");
		r2_if2.createTrafficPortal();
		
		ILink link = topnet.createLink();
		link.createInterface(r1_if2);
		link.createInterface(r2_if1);
		
		ifaces.add(r1_if1);
		ifaces.add(r1_if2);
		ifaces.add(r2_if1);
		ifaces.add(r2_if2);

		for(IInterface iface : ifaces) {
			iface.setBitRate("1000000000"); //1000Mbps
			iface.setLatency("0.0");
			iface.setBufferSize("65536"); //64K
		}
		link.setDelay("0.0001"); //1 ms
		int b = Integer.parseInt(System.getProperty("MBS","100"))*1000000;
		link.setBandwidth(Integer.toString(b)); 

		
		//add network reachability
		r1_if1.addReachableNetwork("12.1.0.0/16");
		r1_if1.addReachableNetwork("13.1.0.0/16");
		r1_if1.addReachableNetwork("14.1.0.0/16");
		r1_if1.addReachableNetwork("15.1.0.0/16");
		r2_if2.addReachableNetwork("12.2.0.0/16");

		
		
		return topnet;
		
	}
}
