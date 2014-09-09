
import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author merazo
 *
 */
public class UDPTesterEmu extends ModelInterface {

	/**
	 * @param db
	 * @param exp
	 */
	public UDPTesterEmu(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public UDPTesterEmu(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		//top.createShortestPath();
		top.createAlgorithmicRouting();
		
		IRouter r1 = top.createRouter("router1");
		IInterface r1_h1= r1.createInterface("ifr11");
		IInterface r1_r2= r1.createInterface("ifr12");
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
		h1_if0.createOpenVPNEmulation();		
		//IUDPMaster udp1 = h1.createUDPMaster();
		//udp1.setMaxDatagramSize(192);
		//ICBR cbr1 = h1.createCBR();		
		//cbr1.setDataSize(191); //bytes
			
		IRouter h2 = top.createRouter("host2");
		//IHost h2 = top.createHost("host2");
		//IRouter h2 = top.createRouter("host2");
		IInterface h2_if0= h2.createInterface("if0");
		//h2_if0.createOpenVPNEmulation();
		//IUDPMaster udp2 = h2.createUDPMaster();		
        //ICBR cbr2 = h2.createCBR();
		
		ILink link_h1_r0= top.createLink("h1_router");
		link_h1_r0.setDelay("0.0");
		link_h1_r0.setBandwidth("1000000000");
	    //l_h1.setBandwidth("10000");
		link_h1_r0.createInterface(h1_if0);
		link_h1_r0.createInterface(r1_h1);	

		ILink link_r0_r1= top.createLink("router_router");
		link_r0_r1.setDelay((float)0.064);
		link_r0_r1.setBandwidth("1000000"); //bits
		link_r0_r1.createInterface(r1_r2);
		link_r0_r1.createInterface(r2_r1);
		
		ILink link_r1_h2= top.createLink("router_h2");
		link_r1_h2.setDelay("0.0");
		link_r1_h2.setBandwidth("1000000000");
		link_r1_h2.createInterface(r2_h2);
		link_r1_h2.createInterface(h2_if0);		
		
		//ITraffic t1 = top.createTraffic();
		/*IHTTPTraffic p1 = t1.createHTTPTraffic();
		p1.setDstPort(80);
		p1.setSrcs("[75]");
		p1.setDsts("[121]");*/
		
		//ICBRTraffic p2 = t1.createCBRTraffic();
		//p2.setInterval(1);
		//p2.setCount(20);
		//p2.setSrcs("[75]");
		//p2.setDsts("[98]");
		
		//XXX setup ping more...
		
		return top;
	}

}




