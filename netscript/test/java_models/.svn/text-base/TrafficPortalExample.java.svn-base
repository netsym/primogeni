import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

public class TrafficPortalExample extends ModelInterface{
	public TrafficPortalExample(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		//create the top most network
		INet topnet   = exp.createTopNet("top");
		topnet.createShortestPath();
		
		
		IRouter r1 = topnet.createRouter("r1");
		IInterface r1_if1 = r1.createInterface("portal");
		IInterface r1_if2 = r1.createInterface("if1");
		r1_if1.createTrafficPortal();


		IRouter r2 = topnet.createRouter("r2");
		IInterface r2_if1 = r2.createInterface("if0");
		IInterface r2_if2 = r2.createInterface("if1");

		IRouter r3 = topnet.createRouter("r3");
		IInterface r3_if1 = r3.createInterface("if0");
		IInterface r3_if2 = r3.createInterface("if1");
		IInterface r3_if3 = r3.createInterface("if2");
		
		IHost h = topnet.createHost("h1");
		IInterface i = h.createInterface("if0");
		
		IRouter r4 = topnet.createRouter("r4");
		IInterface r4_if1 = r4.createInterface("if0");
		IInterface r4_if2 = r4.createInterface("if1");

		IRouter r5 = topnet.createRouter("r5");
		IInterface r5_if1 = r5.createInterface("if0");
		IInterface r5_if2 = r5.createInterface("portal");
		r5_if2.createTrafficPortal();
		
		ILink l1 = topnet.createLink();
		l1.createInterface(r1_if2);
		l1.createInterface(r2_if1);

		ILink l2 = topnet.createLink();
		l2.createInterface(r2_if2);
		l2.createInterface(r3_if1);

		ILink l3 = topnet.createLink();
		l3.createInterface(r3_if2);
		l3.createInterface(r4_if1);
		
		ILink l4 = topnet.createLink();
		l4.createInterface(r4_if2);
		l4.createInterface(r5_if1);

		ILink l5 = topnet.createLink();
		l5.createInterface(r3_if3);
		l5.createInterface(i);

		
		//add network reachability
		r1_if1.addReachableNetwork("12.1.0.0/16");
		r1_if1.setIpAddress("12.1.0.100");
		r5_if2.addReachableNetwork("12.2.0.0/16");
		r5_if2.setIpAddress("12.2.0.100");
				
		return topnet;
		
	}
}
