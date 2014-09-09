import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.CNFApplication.ICNFApplication;
import jprime.CNFTraffic.ICNFTraffic;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Ting Li
 */
public class CNFBaseline extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public CNFBaseline(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelInterface.ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public CNFBaseline(Database db, String expName) {
		super(db, expName, new ArrayList<ModelInterface.ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		
		//create the top most network
		INet topnet = exp.createTopNet("top");
		topnet.createShortestPath();
		
		//INet net1 = topnet.createNet("net1");
		//net1.createShortestPath();

		//Create host h1	
		IHost h1 = topnet.createHost("h1");
		IInterface h1_r1 = h1.createInterface("h1_r1");
		
		//Create host h2	
		IHost h2 = topnet.createHost("h2");
		IInterface h2_r1 = h2.createInterface("h2_r1");
		
		//create router r1 and set as a cnf_router
		IRouter r1 = topnet.createRouter("r1");
		r1.createCNFTransport();
		IInterface r1_h1 = r1.createInterface("r1_h1");
		IInterface r1_h2 = r1.createInterface("r1_h2");
		IInterface r1_r2 = r1.createInterface("r1_r2");
		
		//connect r1 and host1
		ILink l_r1_h1 = topnet.createLink();
		l_r1_h1.createInterface(h1_r1);
		l_r1_h1.createInterface(r1_h1);
		
		//connect r1 and host2
		ILink l_r1_h2 = topnet.createLink();
		l_r1_h2.createInterface(h2_r1);
		l_r1_h2.createInterface(r1_h2);
		
		//INet net2 = topnet.createNet("net2"); 
		//net2.createShortestPath();

		//Create host h3	
		IHost h3 = topnet.createHost("h3");
		IInterface h3_r5 = h3.createInterface("h3_r5");
		
		//Create host h4	
		IHost h4 = topnet.createHost("h4");
		IInterface h4_r5 = h4.createInterface("h4_r5");
		
		//create router r5 and set as a cnf_router
		IRouter r5 = topnet.createRouter("r5");
		r5.createCNFTransport();
		IInterface r5_h3 = r5.createInterface("r5_h3");
		IInterface r5_h4 = r5.createInterface("r5_h4");
		IInterface r5_r3 = r5.createInterface("r5_r3");
		
		//connect r5 and host3
		ILink l_r5_h3 = topnet.createLink();
		l_r5_h3.createInterface(h3_r5);
		l_r5_h3.createInterface(r5_h3);
		
		//connect r5 and host4
		ILink l_r5_h4 = topnet.createLink();
		l_r5_h4.createInterface(h4_r5);
		l_r5_h4.createInterface(r5_h4);
		
		//INet net3 = topnet.createNet("net3"); 
		//net3.createShortestPath();

		//Create host h5	
		IHost h5 = topnet.createHost("h5");
		IInterface h5_r6 = h5.createInterface("h5_r6");
		
		//Create host h6	
		IHost h6 = topnet.createHost("h6");
		IInterface h6_r6 = h6.createInterface("h6_r6");
		
		//create router r6 and set as a cnf_router
		IRouter r6 = topnet.createRouter("r6");
		r6.createCNFTransport();
		IInterface r6_h5 = r6.createInterface("r6_h5");
		IInterface r6_h6 = r6.createInterface("r6_h6");
		IInterface r6_r4 = r6.createInterface("r6_r4");
		
		//connect r6 and host5
		ILink l_r6_h5 = topnet.createLink();
		l_r6_h5.createInterface(h5_r6);
		l_r6_h5.createInterface(r6_h5);
		
		//connect r6 and host6
		ILink l_r6_h6 = topnet.createLink();
		l_r6_h6.createInterface(h6_r6);
		l_r6_h6.createInterface(r6_h6);
		
	
		//put content of cid = 1 on h1
		ICNFApplication cnf1 = h1.createCNFApplication("cnf1");
		cnf1.addContentId(1,10000);
		
		//put content of cid = 2 on h2
		ICNFApplication cnf2 = h2.createCNFApplication("cnf2");
		cnf2.addContentId(2,10000);
		
		//put content of cid = 3 on h3
		ICNFApplication cnf3 = h3.createCNFApplication("cnf3");
		cnf3.addContentId(3,10000);
		
		//put content of cid = 4 on h4
		ICNFApplication cnf4 = h4.createCNFApplication("cnf4");
		cnf4.addContentId(4,10000);
		
		//put content of cid = 5 on h5
		ICNFApplication cnf5 = h5.createCNFApplication("cnf5");
		cnf5.addContentId(5,10000);
		
		//put content of cid = 6 on h6
		ICNFApplication cnf6 = h6.createCNFApplication("cnf6");
		cnf6.addContentId(6,10000);
		

		//create router r2 and set as a controller
		IRouter r2 = topnet.createRouter("r2");
		r2.setAsCNFController();
		IInterface r2_r1 = r2.createInterface("r2_r1");
		IInterface r2_r3 = r2.createInterface("r2_r3");
		IInterface r2_r4 = r2.createInterface("r2_r4");
		
		IRouter r3 = topnet.createRouter("r3");
		r3.createCNFTransport();
		IInterface r3_r5 = r3.createInterface("r3_r5");
		IInterface r3_r2 = r3.createInterface("r3_r2");
		IInterface r3_r4 = r3.createInterface("r3_r4");
		
		IRouter r4 = topnet.createRouter("r4");
		r4.createCNFTransport();
		IInterface r4_r6 = r4.createInterface("r4_r6");
		IInterface r4_r2 = r4.createInterface("r4_r2");
		IInterface r4_r3 = r4.createInterface("r4_r3");
		
		//connect r2 and r3
		ILink l_r2_r3 = topnet.createLink();
		l_r2_r3.createInterface(r2_r3);
		l_r2_r3.createInterface(r3_r2);
		
		//connect r3 and r4
		ILink l_r3_r4 = topnet.createLink();
		l_r3_r4.createInterface(r3_r4);
		l_r3_r4.createInterface(r4_r3);
		
		//connect r2 and r4
		ILink l_r2_r4 = topnet.createLink();
		l_r2_r4.createInterface(r2_r4);
		l_r2_r4.createInterface(r4_r2);
		
		//connect r2 and net1
		ILink l_r2_net1 = topnet.createLink();
		l_r2_net1.createInterface(r2_r1);
		l_r2_net1.createInterface(r1_r2);
		
		//connect r3 and net2
		ILink l_r3_net2 = topnet.createLink();
		l_r3_net2.createInterface(r3_r5);
		l_r3_net2.createInterface(r5_r3);
		
		//connect r4 and net3
		ILink l_r4_net3 = topnet.createLink();
		l_r4_net3.createInterface(r4_r6);
		l_r4_net3.createInterface(r6_r4);
		
		String six="6";
		String threeh="300";
		//create cnf traffic
		ITraffic t = topnet.createTraffic();
		ICNFTraffic c1 = t.createCNFTraffic();
		c1.setContentId("1");
		c1.setDstPort("1000");
		c1.setSrcs("{.:h1,.:h2,.:h3,.:h4,.:h5,.:h6}");
		c1.setDsts("{.:r2}");
		c1.setStartTime("0");
		c1.setNumberOfContents(six);
		c1.setNumberOfRequests(threeh);
		
		ICNFTraffic c2 = t.createCNFTraffic();
		c2.setContentId("2");
		c2.setDstPort("1000");
		//c2.setSrcs("{:top:net1//?Host}");
		c2.setSrcs("{.:h1,.:h2,.:h3,.:h4,.:h5,.:h6}");
		c2.setDsts("{.:r2}");
		c2.setStartTime("0");
		c2.setNumberOfContents(six);
		c2.setNumberOfRequests(threeh);
		
		ICNFTraffic c3 = t.createCNFTraffic();
		c3.setContentId("3");
		c3.setDstPort("1000");
		c3.setSrcs("{.:h1,.:h2,.:h3,.:h4,.:h5,.:h6}");
		c3.setDsts("{.:r2}");
		c3.setStartTime("0");
		c3.setNumberOfContents(six);
		c3.setNumberOfRequests(threeh);
		
		ICNFTraffic c4 = t.createCNFTraffic();
		c4.setContentId("4");
		c4.setDstPort("1000");
		//c.setSrcs("{:top:net1//?Host}");
		c4.setSrcs("{.:h1,.:h2,.:h3,.:h4,.:h5,.:h6}");
		c4.setDsts("{.:r2}");
		c4.setStartTime("0");
		c4.setNumberOfContents(six);
		c4.setNumberOfRequests(threeh);
		
		ICNFTraffic c5 = t.createCNFTraffic();
		c5.setContentId("5");
		c5.setDstPort("1000");
		//c5.setSrcs("{:top:net1//?Host}");
		c5.setSrcs("{.:h1,.:h2,.:h3,.:h4,.:h5,.:h6}");
		c5.setDsts("{.:r2}");
		c5.setStartTime("0");
		c5.setNumberOfContents(six);
		c5.setNumberOfRequests(threeh);
		
		ICNFTraffic c6 = t.createCNFTraffic();
		c6.setContentId(six);
		c6.setDstPort("1000");
		//c6.setSrcs("{:top:net1//?Host}");
		c6.setSrcs("{.:h1,.:h2,.:h3,.:h4,.:h5,.:h6}");
		c6.setDsts("{.:r2}");
		c6.setStartTime("0");
		c6.setNumberOfContents(six);
		c6.setNumberOfRequests(threeh);
		
		/*IPingTraffic p3 = t.createPingTraffic();
		p3.setCount(2);
		p3.setStartTime("0.5");
		p3.setSrcs("{.:h1}");
		p3.setDsts("{.:h3}");
		p3.setIntervalExponential("true");
		p3.setMapping("one2one");*/
		return topnet;
	}
	
}