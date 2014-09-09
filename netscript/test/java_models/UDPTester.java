
import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.Traffic.ITraffic;
import jprime.UDPMaster.IUDPMaster;
import jprime.UDPTraffic.IUDPTraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author merazo
 *
 */
public class UDPTester extends ModelInterface {

	/**
	 * @param db
	 * @param exp
	 */
	public UDPTester(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public UDPTester(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		//top.createAlgorithmicRouting();
		
		IRouter r1 = top.createRouter("router1");
		IInterface r1_h1= r1.createInterface("ifr11");
		IInterface r1_r2= r1.createInterface("ifr12");
//		IInterface r1_h3= r1.createInterface("ifr13");
		r1_r2.setBufferSize("64000");//bytes
        //r0_h1.setBufferSize("18480");	

		IRouter r2 = top.createRouter("router2");
		IInterface r2_r1 = r2.createInterface("ifr21");
		IInterface r2_h2 = r2.createInterface("ifr22");
		r2_r1.setBufferSize("64000");//bytes
        //r0_h1.setBufferSize("18480");

		/*IRouter h0 = top.createRouter("host0");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h0_if0 = h0.createInterface("if0");
		ITCPMaster tcp0 = h0.createTCPMaster();
        tcp0.setTcpCA("reno");*/ 
		
		IRouter h1 = top.createRouter("host1");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h1_if0 = h1.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		IUDPMaster udp1 = h1.createUDPMaster();
		udp1.setMaxDatagramSize(192);
		h1.createCBR();		
		//cbr1.setDataSize(191); //bytes
			
		IRouter h2 = top.createRouter("host2");
		//IHost h2 = top.createHost("host2");
		//IRouter h2 = top.createRouter("host2");
		IInterface h2_if0= h2.createInterface("if0");
		//h2_if0.createOpenVPNEmulation();
		h2.createUDPMaster();		
		h2.createCBR();
		
/*        IRouter h3 = top.createRouter("host3");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h3_if0 = h3.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		IUDPMaster udp3 = h3.createUDPMaster();
		udp3.setMaxDatagramSize(192);
		ICBR cbr3 = h3.createCBR();		
		//cbr1.setDataSize(191); //bytes			*/
        
		ILink link_h1_r0= top.createLink("h1_router");
		link_h1_r0.setDelay("0.0");
		link_h1_r0.setBandwidth("1000000000");
	    //l_h1.setBandwidth("10000");
		link_h1_r0.createInterface(h1_if0);
		link_h1_r0.createInterface(r1_h1);	

		ILink link_r0_r1= top.createLink("router_router");
		link_r0_r1.setDelay((float)0.064);
		link_r0_r1.setBandwidth("1000000");
		link_r0_r1.createInterface(r1_r2);
		link_r0_r1.createInterface(r2_r1);
		
		ILink link_r1_h2= top.createLink("router_h2");
		link_r1_h2.setDelay("0.0");
		link_r1_h2.setBandwidth("1000000000");
		link_r1_h2.createInterface(r2_h2);
		link_r1_h2.createInterface(h2_if0);		
		
/*		ILink link_h3_r0= top.createLink("h3_router");
		link_h3_r0.setDelay("0.0");
		link_h3_r0.setBandwidth("1000000000");
	    //l_h1.setBandwidth("10000");
		link_h3_r0.createInterface(h3_if0);
		link_h3_r0.createInterface(r1_h3);				*/
		
		ITraffic t1 = top.createTraffic();
		/*IHTTPTraffic p1 = t1.createHTTPTraffic();
		p1.setDstPort(80);
		p1.setSrcs("[75]");
		p1.setDsts("[121]");*/
		
		IUDPTraffic p2 = t1.createUDPTraffic();
		p2.setInterval(1);
		p2.setBytesToSendEachInterval("1500");
		p2.setToEmulated("false");
		p2.setCount("2");
		p2.setSrcs("{.:host2}");
		p2.setDsts("{.:host1}");
		
/*		IUDPTraffic p3 = t1.createUDPTraffic();
		p3.setInterval(1);
		p3.setBytesToSendEachInterval("1500");
		p3.setToEmulated("false");
		p3.setCount("2");
		p3.setSrcs("{.:host2}");
		p3.setDsts("{.:host1}");
		
		IUDPTraffic p4 = t1.createUDPTraffic();
		p4.setInterval(1);
		p4.setBytesToSendEachInterval("1500");
		p4.setToEmulated("false");
		p4.setCount("2");
		p4.setSrcs("{.:host3}");
		p4.setDsts("{.:host1}");
		
		IUDPTraffic p5 = t1.createUDPTraffic();
		p5.setInterval(1);
		p5.setBytesToSendEachInterval("1500");
		p5.setToEmulated("false");
		p5.setCount("2");
		p5.setSrcs("{.:host3}");
		p5.setDsts("{.:host1}");				*/
			
		//XXX setup ping more...
		
		return top;
	}

}




