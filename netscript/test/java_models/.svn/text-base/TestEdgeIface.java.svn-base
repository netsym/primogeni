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
public class TestEdgeIface extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public TestEdgeIface(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public TestEdgeIface(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		
		//subnet 1
		INet sub1 = top.createNet("sub1");
		sub1.createShortestPath();
		
		//create r1 for sub1
		IRouter r1 = sub1.createRouter("r1");
		IInterface r1_if0= r1.createInterface("if0"); //connect r1 in sub2
		IInterface r1_if1= r1.createInterface("if1"); //connect h1
		IInterface r1_if2= r1.createInterface("if2"); //connect h2
		
		IHost h1 = sub1.createHost("h1");
		IInterface h1_if0 = h1.createInterface("if0");
		
		IHost h2 = sub1.createHost("h2");
		IInterface h2_if0 = h2.createInterface("if0");
		
		//connect the router and hosts in sub1
		ILink r1_h1 = sub1.createLink("r1_h1");
		r1_h1.createInterface(r1_if1);
		r1_h1.createInterface(h1_if0);
		
		ILink r1_h2 = sub1.createLink("r1_h2");
		r1_h2.createInterface(r1_if2);
		r1_h2.createInterface(h2_if0);
		
		//sub2
		INet sub2 = top.createNetReplica("sub2", sub1);
		
		//connect the sub1 and sub2	
		ILink sub1_sub2 = top.createLink("sub1_sub2");
		sub1_sub2.createInterface(r1_if0);
		sub1_sub2.createInterface((IInterface)sub2.getChildByName("r1").getChildByName("if0"));
		
		
		//create subnets for subnet 1
		//INet subsub1 = sub1.createNet("subsub1");
		//subsub1.createShortestPath();
		
		//create r1 for subsub1
		/*IRouter r1 = subsub1.createRouter("r1");
		IInterface r1_if0= r1.createInterface("if0"); //connect r1 in sub2
		IInterface r1_if1= r1.createInterface("if1"); //connect h1
		IInterface r1_if2= r1.createInterface("if2"); //connect h2
		IInterface r1_if3= r1.createInterface("if3"); //connect r1 in subsub2
		
		IHost h1 = subsub1.createHost("h1");
		IInterface h1_if0 = h1.createInterface("if0");
		
		IHost h2 = subsub1.createHost("h2");
		IInterface h2_if0 = h2.createInterface("if0");*/
		
		//connect the router and hosts in subsub1
		/*ILink subsub1_r1_h1 = subsub1.createLink("r1_h1");
		subsub1_r1_h1.createInterface(r1_if1);
		subsub1_r1_h1.createInterface(h1_if0);
		
		ILink subsub1_r1_h2 = subsub1.createLink("r1_h2");
		subsub1_r1_h2.createInterface(r1_if2);
		subsub1_r1_h2.createInterface(h2_if0);*/
		
		//INet subsub2 = sub1.createNetReplica("subsub2", subsub1);
		
		//connect the subsub1 and subsub2	
		/*ILink subsub1_subsub2 = sub1.createLink("subsub1_subsub2");
		subsub1_subsub2.createInterface(r1_if0);
		subsub1_subsub2.createInterface((IInterface)subsub2.getChildByName("r1").getChildByName("if3"));*/
		
		//subnet2
		//INet sub2 = top.createNetReplica("sub2", subsub1);
		
		//connect the subsub1 and sub2	
		/*ILink sub1_sub2 = top.createLink("sub1_sub2");
		sub1_sub2.createInterface(r1_if3);
		sub1_sub2.createInterface((IInterface)sub2.getChildByName("r1").getChildByName("if0"));*/
		
		ITraffic t = top.createTraffic();
		
		IPingTraffic p = t.createPingTraffic();
		p.setSrcs(".:sub1:h1");
		p.setDsts(".:sub2:h1");

		return top;
	}
}
