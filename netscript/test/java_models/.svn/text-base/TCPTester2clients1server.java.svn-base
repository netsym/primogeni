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
public class TCPTester2clients1server extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public TCPTester2clients1server(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public TCPTester2clients1server(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		
		top.createAlgorithmicRouting();
		//top.createShortestPath();
		
		IRouter r1 = top.createRouter("router1");
		IInterface r1_r2= r1.createInterface("ifr1r2");
		IInterface r1_h1= r1.createInterface("ifr1h1");
		IInterface r1_h2= r1.createInterface("ifr1h2");		
		IInterface r1_h3= r1.createInterface("ifr1h3");
		IInterface r1_h4= r1.createInterface("ifr1h4");
		IInterface r1_h5= r1.createInterface("ifr1h5");
	/*	IInterface r1_h6= r1.createInterface("ifr1h6");
		IInterface r1_h7= r1.createInterface("ifr1h7");
		IInterface r1_h8= r1.createInterface("ifr1h8");
		IInterface r1_h9= r1.createInterface("ifr1h9");
		IInterface r1_h10= r1.createInterface("ifr1h10");*/
		r1_r2.setBufferSize("67800");//bytes

		IRouter r2 = top.createRouter("router2");
		IInterface r2_r1 = r2.createInterface("ifr2r1");
		IInterface r2_h11 = r2.createInterface("ifr2h11");
		IInterface r2_h12= r2.createInterface("ifr2h12");
		IInterface r2_h13= r2.createInterface("ifr2h13");		
		IInterface r2_h14= r2.createInterface("ifr2h14");
		IInterface r2_h15= r2.createInterface("ifr2h15");
		/*IInterface r2_h16= r1.createInterface("ifr2h16");
		IInterface r2_h17= r1.createInterface("ifr2h17");
		IInterface r2_h18= r1.createInterface("ifr2h18");
		IInterface r2_h19= r1.createInterface("ifr2h19");
		IInterface r2_h20= r1.createInterface("ifr2h20");*/		
		r2_r1.setBufferSize("67800");//bytes
		
		IHost h1 = top.createHost("host1");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h1_if0 = h1.createInterface("if0");
		h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp1 = h1.createTCPMaster();
		tcp1.setSamplingInterval("0.1");
        tcp1.setTcpCA("reno");
        tcp1.setMss("1448");
        tcp1.setSndWndSize("1000");
        h1.createHTTPClient();
        //httpclient1.setEmulation("1");
	    
        IHost h2 = top.createHost("host2");
		//IHost h2 = top.createHost("host2");
		//IRouter h2 = top.createRouter("host2");
		IInterface h2_if0= h2.createInterface("if0");
		//h2_if0.createOpenVPNEmulation();
		ITCPMaster tcp2 = h2.createTCPMaster();
		tcp2.setMss("1448");
	    tcp2.setTcpCA("reno");
	    tcp2.setSamplingInterval("0.1");
//	    IHTTPClient http2 = h2.createHTTPClient();
//	    IHTTPServer http2 = h2.createHTTPServer();
	    h2.createSymbioSimAppProt();
        //http2.setPageSize("2024000");
	
        IHost h3 = top.createHost("host3");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h3_if0 = h3.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
/*		ITCPMaster tcp3 = h3.createTCPMaster();
		tcp3.setSamplingInterval("0.1");
        tcp3.setTcpCA("reno");
        tcp3.setMss("1000");
        IHTTPClient httpclient3 = h3.createHTTPClient();*/
        //httpclient3.setEmulation("1");
//		h3_if0.createOpenVPNEmulation();
        
        IHost h4 = top.createHost("host4");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h4_if0 = h4.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp4 = h4.createTCPMaster();
		tcp4.setSamplingInterval("0.1");
        tcp4.setTcpCA("reno");
        tcp4.setMss("1000");
        h4.createHTTPClient();
        //httpclient4.setEmulation("1");
        
        IHost h5 = top.createHost("host5");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h5_if0 = h5.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp5 = h5.createTCPMaster();
		tcp5.setSamplingInterval("0.1");
		tcp5.setTcpCA("reno");
		tcp5.setMss("1000");
        h5.createHTTPClient();
        //httpclient5.setEmulation("1");
        
  /*      IRouter h6 = top.createRouter("host6");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h6_if0 = h6.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp6 = h6.createTCPMaster();
		tcp6.setSamplingInterval("0.1");
		tcp6.setTcpCA("reno");
		tcp6.setMss("1000");
        IHTTPClient httpclient6 = h6.createHTTPClient();
        //httpclient6.setEmulation("1");
        
        IRouter h7 = top.createRouter("host7");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h7_if0 = h7.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp7 = h7.createTCPMaster();
		tcp7.setSamplingInterval("0.1");
		tcp7.setTcpCA("reno");
		tcp7.setMss("1000");
		IHTTPClient httpclient7 = h7.createHTTPClient();
        //httpclient7.setEmulation("1");
        
        IRouter h8 = top.createRouter("host8");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h8_if0 = h8.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp8 = h8.createTCPMaster();
		tcp8.setSamplingInterval("0.1");
		tcp8.setTcpCA("reno");
		tcp8.setMss("1000");
        IHTTPClient httpclient8 = h8.createHTTPClient();
        //httpclient8.setEmulation("1");
        
        IRouter h9 = top.createRouter("host9");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h9_if0 = h9.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp9 = h9.createTCPMaster();
		tcp9.setSamplingInterval("0.1");
		tcp9.setTcpCA("reno");
		tcp9.setMss("1000");
        IHTTPClient httpclient9 = h9.createHTTPClient();
        //httpclient9.setEmulation("1");
        
        IRouter h10 = top.createRouter("host10");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h10_if0 = h10.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp10 = h10.createTCPMaster();
		tcp10.setSamplingInterval("0.1");
		tcp10.setTcpCA("reno");
		tcp10.setMss("1000");
        IHTTPClient httpclient10 = h10.createHTTPClient();
        //httpclient10.setEmulation("1");						*/
                
        IHost h11 = top.createHost("host11");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h11_if0 = h11.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp11 = h11.createTCPMaster();
		tcp11.setSamplingInterval("0.1");
		tcp11.setTcpCA("reno");
		tcp11.setMss("1448");
        h11.createHTTPServer();
        //httpclient10.setEmulation("1");
        
        IHost h12 = top.createHost("host12");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h12_if0 = h12.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp12 = h12.createTCPMaster();
		tcp12.setSamplingInterval("0.1");
		tcp12.setTcpCA("reno");
		tcp12.setMss("1448");
		tcp12.setSndWndSize("1000");
//		IHTTPServer httpclient12 = h12.createHTTPServer();
        //httpclient10.setEmulation("1");						
//        IHTTPClient httpclient12 = h12.createHTTPClient();
		h12.createSymbioSimAppProt();
        //httpclient1.setEmulation("1");
		
        
        IHost h13 = top.createHost("host13");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h13_if0 = h13.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp13 = h13.createTCPMaster();
		tcp13.setSamplingInterval("0.1");
		tcp13.setTcpCA("reno");
		tcp13.setMss("1448");
		h13.createHTTPServer();
        //httpclient10.setEmulation("1");
        
        IHost h14 = top.createHost("host14");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h14_if0 = h14.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp14 = h14.createTCPMaster();
		tcp14.setSamplingInterval("0.1");
		tcp14.setTcpCA("reno");
		tcp14.setMss("1000");
		h14.createHTTPServer();
        //httpclient10.setEmulation("1");
        
        IHost h15 = top.createHost("host15");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h15_if0 = h15.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp15 = h15.createTCPMaster();
		tcp15.setSamplingInterval("0.1");
		tcp15.setTcpCA("reno");
		tcp15.setMss("1000");
		h15.createHTTPServer();
        //httpclient10.setEmulation("1");
  /*      
        IRouter h16 = top.createRouter("host16");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h16_if0 = h16.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp16 = h16.createTCPMaster();
		tcp16.setSamplingInterval("0.1");
		tcp16.setTcpCA("reno");
		tcp16.setMss("1000");
		IHTTPServer httpclient16 = h16.createHTTPServer();
        //httpclient10.setEmulation("1");
        
        IRouter h17 = top.createRouter("host17");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h17_if0 = h17.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp17 = h17.createTCPMaster();
		tcp17.setSamplingInterval("0.1");
		tcp17.setTcpCA("reno");
		tcp17.setMss("1000");
		IHTTPServer httpclient17 = h17.createHTTPServer();
        //httpclient10.setEmulation("1");
        
        IRouter h18 = top.createRouter("host18");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h18_if0 = h18.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp18 = h18.createTCPMaster();
		tcp18.setSamplingInterval("0.1");
		tcp18.setTcpCA("reno");
		tcp18.setMss("1000");
		IHTTPServer httpclient18 = h18.createHTTPServer();
        //httpclient10.setEmulation("1");
        
        IRouter h19 = top.createRouter("host19");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h19_if0 = h19.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp19 = h19.createTCPMaster();
		tcp19.setSamplingInterval("0.1");
		tcp19.setTcpCA("reno");
		tcp19.setMss("1000");
		IHTTPServer httpclient19 = h19.createHTTPServer();
        //httpclient10.setEmulation("1");
        
        IRouter h20 = top.createRouter("host20");
		//IHost h1 = top.createHost("host1");
		//IRouter h1 = top.createRouter("host1");
		IInterface h20_if0 = h20.createInterface("if0");
		//h1_if0.createOpenVPNEmulation();		
		ITCPMaster tcp20 = h20.createTCPMaster();
		tcp20.setSamplingInterval("0.1");
		tcp20.setTcpCA("reno");
		tcp20.setMss("lk;;");
		IHTTPServer httpclient20 = h20.createHTTPServer();
        //httpclient10.setEmulation("1");						*/
        
		ILink link_h1_r0= top.createLink("h1_router");
		link_h1_r0.setDelay("0.0");
		link_h1_r0.setBandwidth("1000000000");
		link_h1_r0.createInterface(h1_if0);
		link_h1_r0.createInterface(r1_h1);	
		
		ILink link_r1_h2= top.createLink("h2_router");
		link_r1_h2.setDelay("0.0");
		link_r1_h2.setBandwidth("1000000000");
		link_h1_r0.createInterface(h2_if0);
		link_h1_r0.createInterface(r1_h2);	
		
		ILink link_h3_r0= top.createLink("h3_router");
		link_h3_r0.setDelay("0.0");
		link_h3_r0.setBandwidth("1000000000");
		link_h3_r0.createInterface(h3_if0);
		link_h3_r0.createInterface(r1_h3);
		
		ILink link_h4_r0= top.createLink("h4_router");
		link_h4_r0.setDelay("0.0");
		link_h4_r0.setBandwidth("1000000000");
		link_h4_r0.createInterface(h4_if0);
		link_h4_r0.createInterface(r1_h4);
		
		ILink link_h5_r0= top.createLink("h5_router");
		link_h5_r0.setDelay("0.0");
		link_h5_r0.setBandwidth("1000000000");
		link_h5_r0.createInterface(h5_if0);
		link_h5_r0.createInterface(r1_h5);
		
	/*	ILink link_h6_r0= top.createLink("h6_router");
		link_h6_r0.setDelay("0.0");
		link_h6_r0.setBandwidth("1000000000");
		link_h6_r0.createInterface(h6_if0);
		link_h6_r0.createInterface(r1_h6);
		
		ILink link_h7_r0= top.createLink("h7_router");
		link_h7_r0.setDelay("0.0");
		link_h7_r0.setBandwidth("1000000000");
		link_h7_r0.createInterface(h7_if0);
		link_h7_r0.createInterface(r1_h7);
		
		ILink link_h8_r0= top.createLink("h8_router");
		link_h8_r0.setDelay("0.0");
		link_h8_r0.setBandwidth("1000000000");
		link_h8_r0.createInterface(h8_if0);
		link_h8_r0.createInterface(r1_h8);
		
		ILink link_h9_r0= top.createLink("h9_router");
		link_h9_r0.setDelay("0.0");
		link_h9_r0.setBandwidth("1000000000");
		link_h9_r0.createInterface(h9_if0);
		link_h9_r0.createInterface(r1_h9);
		
		ILink link_h10_r0= top.createLink("h10_router");
		link_h10_r0.setDelay("0.0");
		link_h10_r0.setBandwidth("1000000000");
		link_h10_r0.createInterface(h10_if0);
		link_h10_r0.createInterface(r1_h10);*/

		ILink link_h11_r2= top.createLink("h11_router");
		link_h11_r2.setDelay("0.0");
		link_h11_r2.setBandwidth("1000000000");
		link_h11_r2.createInterface(h11_if0);
		link_h11_r2.createInterface(r2_h11);
		
		ILink link_h12_r0= top.createLink("h12_router");
		link_h12_r0.setDelay("0.0");
		link_h12_r0.setBandwidth("1000000000");
		link_h12_r0.createInterface(h12_if0);
		link_h12_r0.createInterface(r2_h12);
		
		ILink link_h13_r0= top.createLink("h13_router");
		link_h13_r0.setDelay("0.0");
		link_h13_r0.setBandwidth("1000000000");
		link_h13_r0.createInterface(h13_if0);
		link_h13_r0.createInterface(r2_h13);
		
		ILink link_h14_r0= top.createLink("h14_router");
		link_h14_r0.setDelay("0.0");
		link_h14_r0.setBandwidth("1000000000");
		link_h14_r0.createInterface(h14_if0);
		link_h14_r0.createInterface(r2_h14);
		
		ILink link_h15_r0= top.createLink("h15_router");
		link_h15_r0.setDelay("0.0");
		link_h15_r0.setBandwidth("1000000000");
		link_h15_r0.createInterface(h15_if0);
		link_h15_r0.createInterface(r2_h15);
	/*	
		ILink link_h16_r0= top.createLink("h16_router");
		link_h16_r0.setDelay("0.0");
		link_h16_r0.setBandwidth("1000000000");
		link_h16_r0.createInterface(h16_if0);
		link_h16_r0.createInterface(r2_h16);
		
		ILink link_h17_r0= top.createLink("h17_router");
		link_h17_r0.setDelay("0.0");
		link_h17_r0.setBandwidth("1000000000");
		link_h17_r0.createInterface(h17_if0);
		link_h17_r0.createInterface(r2_h17);
		
		ILink link_h18_r0= top.createLink("h18_router");
		link_h18_r0.setDelay("0.0");
		link_h18_r0.setBandwidth("1000000000");
		link_h18_r0.createInterface(h8_if0);
		link_h18_r0.createInterface(r2_h18);
		
		ILink link_h19_r0= top.createLink("h19_router");
		link_h19_r0.setDelay("0.0");
		link_h19_r0.setBandwidth("1000000000");
		link_h19_r0.createInterface(h19_if0);
		link_h19_r0.createInterface(r2_h19);
		
		ILink link_h20_r0= top.createLink("h20_router");
		link_h20_r0.setDelay("0.0");
		link_h20_r0.setBandwidth("1000000000");
		link_h20_r0.createInterface(h20_if0);
		link_h20_r0.createInterface(r2_h20);*/
				
		ILink link_r1_r2= top.createLink("router_router");
		link_r1_r2.setDelay((float)0.010);
		link_r1_r2.setBandwidth("10000000");
		link_r1_r2.createInterface(r1_r2);
		link_r1_r2.createInterface(r2_r1);
		
/*		ITraffic t1 = top.createTraffic();				
		IHTTPTraffic p10 = t1.createHTTPTraffic();
		p10.setIntervalExponential("True");
		//p10.setFilePareto("True");
		p10.setSessionTimeout("3000");
		p10.setDstPort(80);
		p10.setFileSize(100000000);
		p10.setConnectionsPerSession("1");
		p10.setNumberOfSessions(1);
		p10.setStartTime("0");
		//p10.setSessionTimeout(5);
		//p10.setToEmulated("true");
		//p10.setSrcs("[91]");
		//p10.setSrcs("{.:host1,.:host2}");//, .:host3, .:host4, .:host5}");
		//p10.setDsts("{.:host11,.:host12}");//, .:host13, .:host14, .:host15}");
		p10.setSrcs("{.:host1}");//, .:host3, .:host4, .:host5}");
	 	p10.setDsts("{.:host11}");//, .:host13, .:host14, .:host15}");						
		
/*		IHTTPTraffic p11 = t1.createHTTPTraffic();
		p11.setIntervalExponential("True");
		//p10.setFilePareto("True");
		p11.setSessionTimeout("3000");
		p11.setDstPort(80);
		p11.setFileSize(100000000);
		p11.setConnectionsPerSession("1");
		p11.setNumberOfSessions(1);
		p11.setStartTime("0");
		//p10.setSessionTimeout(5);
		//p10.setToEmulated("true");
		//p10.setSrcs("[91]");
		p11.setSrcs("{.:host12}");//, .:host3, .:host4, .:host5}");
		p11.setDsts("{.:host2}");//, .:host13, .:host14, .:host15}");
	*/	
		/*IHTTPTraffic p12 = t1.createHTTPTraffic();
		p12.setIntervalExponential("True");
		//p10.setFilePareto("True");
		p12.setSessionTimeout("500");
		p12.setDstPort(80);
		p12.setFileSize(5040000);
		p12.setConnectionsPerSession("1");
		p12.setNumberOfSessions(1000);
		p12.setStartTime("0");
		//p10.setSessionTimeout(5);
		//p10.setToEmulated("true");
		//p10.setSrcs("[91]");
		p12.setSrcs("{.:host3,.:host4}");//, .:host3, .:host4, .:host5}");
		p12.setDsts("{.:host13,.:host14}");//, .:host13, .:host14, .:host15}");*/
		
		return top;
	}

}
