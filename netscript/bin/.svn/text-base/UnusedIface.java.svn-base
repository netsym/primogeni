
import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.PingTraffic.IPingTraffic;
import jprime.Router.IRouter;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class UnusedIface extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public UnusedIface(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public UnusedIface(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		
		//create router for topnet
		IRouter r0 = top.createRouter("r0");
		IInterface r0_if0= r0.createInterface("if0");
		IInterface r0_if1= r0.createInterface("if1");
		
		//subnet 1
		INet sub1 = top.createNet("sub1");
		sub1.createShortestPath();
		
		//create r1 for sub1
		IRouter r1 = sub1.createRouter("r1");
		IInterface r1_if0= r1.createInterface("if0"); //connect h1
		IInterface r1_if1= r1.createInterface("if1"); //connect h2
		IInterface r1_if2= r1.createInterface("if2"); //connect r2
		r1.createInterface("if3"); //unused interface
		
		//create r2 for sub1
		IRouter r2 = sub1.createRouter("r2");
		IInterface r2_if0= r2.createInterface("if0"); //connect h3
		IInterface r2_if1= r2.createInterface("if1"); //connect h4
		IInterface r2_if2= r2.createInterface("if2"); //connect r1
		IInterface r2_if3= r2.createInterface("if3"); //connect r0
		
		IHost h1 = sub1.createHost("h1");
		IInterface h1_if0 = h1.createInterface("if0");
		
		IHost h2 = sub1.createHost("h2");
		IInterface h2_if0 = h2.createInterface("if0");
		
		IHost h3 = sub1.createHost("h3");
		IInterface h3_if0 = h3.createInterface("if0");
		
		IHost h4 = sub1.createHost("h4");
		IInterface h4_if0 = h4.createInterface("if0");
		
		//connect the router and hosts in sub1
		ILink sub1_r1_h1 = sub1.createLink("r1_h1");
		sub1_r1_h1.createInterface(r1_if0);
		sub1_r1_h1.createInterface(h1_if0);
		
		ILink sub1_r1_h2 = sub1.createLink("r1_h2");
		sub1_r1_h2.createInterface(r1_if1);
		sub1_r1_h2.createInterface(h2_if0);
		
		ILink sub1_r2_h3 = sub1.createLink("r2_h3");
		sub1_r2_h3.createInterface(r2_if0);
		sub1_r2_h3.createInterface(h3_if0);
		
		ILink sub1_r2_h4 = sub1.createLink("r2_h4");
		sub1_r2_h4.createInterface(r2_if1);
		sub1_r2_h4.createInterface(h4_if0);
		
		//connect the routers in sub1
		ILink sub1_r1_r2 = sub1.createLink("l_r1_h2");
		sub1_r1_r2.createInterface(r1_if2);
		sub1_r1_r2.createInterface(r2_if2);
		
		
		//sub2 is a replica of sub1
		INet sub2 = top.createNetReplica("sub2", sub1);

		//connect router in subnet and router in topnet
		ILink r0_sub1 = top.createLink("l_r0_sub1");
		r0_sub1.createInterface(r0_if0);
		r0_sub1.createInterface(r2_if3);

		ILink r0_sub2 = top.createLink("l_r0_sub2");
		r0_sub2.createInterface(r0_if1);
		r0_sub2.createInterface((IInterface)sub2.getChildByName("r1").getChildByName("if3"));
		
		ITraffic t = top.createTraffic();
		
		IPingTraffic p = t.createPingTraffic();
		p.setSrcs("sub1:h1");
		p.setDsts("sub2:h1");

		return top;
	}
}
