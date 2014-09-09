import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.TrafficFactory;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

public class MyThirdJavaModel extends ModelInterface{
	public MyThirdJavaModel(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		//create the top most network
		INet topnet   = exp.createTopNet("top");
		topnet.createShortestPath();
		
		//Create the left network
		INet left_net = topnet.createNet("left");
		left_net.createShortestPath();
		
		IHost h1 = left_net.createHost("h1");
		IInterface if1 = h1.createInterface("if0");

		IHost h2 = left_net.createHost("h2");
		IInterface if2 = h2.createInterface("if0");

		IHost h3 = left_net.createHost("h3");
		IInterface if3 = h3.createInterface("if0");
		//make h3 in both the left and right nets emulated
		h3.enableEmulation();

		IHost h4 = left_net.createHost("h4");
		IInterface if4 = h4.createInterface("if0");

		IRouter r = left_net.createRouter("r");

		ILink l1 = left_net.createLink();
		l1.createInterface(if1);
		l1.createInterface(r.createInterface("if1"));

		ILink l2 = left_net.createLink();
		l2.createInterface(if2);
		l2.createInterface(r.createInterface("if2"));

		ILink l3 = left_net.createLink();
		l3.createInterface(if3);
		l3.createInterface(r.createInterface("if3"));

		ILink l4 = left_net.createLink();
		l4.createInterface(if4);
		l4.createInterface(r.createInterface("if4"));
		
		//create the right network
		INet right_net = (INet)left_net.copy("right_net",topnet);

		//link the left and right networks
		ILink toplink = topnet.createLink("toplink");
		toplink.createInterface(((IRouter)left_net.get("r")).createInterface("if0"));
		toplink.createInterface(((IRouter)right_net.get("r")).createInterface("if0"));		
		
		//add simulated traffic
		IHost right_h2 = (IHost)right_net.get("h1");
		TrafficFactory trafficFactory = new TrafficFactory(topnet);
		trafficFactory.createSimulatedTCP(10, 1000000000, h1, right_h2);
		trafficFactory.createSimulatedTCP(12, 2, 1000000000, h2, right_h2);
	

		//add emulated traffic
		IHost right_h3 = (IHost)right_net.get("h3");
		trafficFactory.createEmulatedTCP(5, 100000000, right_h3, h3);
		
		return topnet;
		
	}
}
