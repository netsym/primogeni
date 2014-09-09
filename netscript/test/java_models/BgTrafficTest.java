import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.FluidTraffic.IFluidTraffic;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.PPBPTraffic.IPPBPTraffic;
import jprime.Router.IRouter;
import jprime.TCPMaster.ITCPMaster;
import jprime.TCPTraffic.ITCPTraffic;
import jprime.Traffic.ITraffic;
import jprime.UDPMaster.IUDPMaster;
import jprime.UDPTraffic.IUDPTraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author ting
 *
 */
public class BgTrafficTest extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public BgTrafficTest(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public BgTrafficTest(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		
		IRouter r0 = top.createRouter("r0");
		IInterface r0_iface[] = new IInterface[10];
		for(int i=1; i<10;i++){
			r0_iface[i] = r0.createInterface("ifr0_h"+i);
			r0_iface[i].setBitRate("1000000000"); //1e9
			r0_iface[i].setLatency("0.0");
			if(i%9 != 0){
				r0_iface[i].createFluidQueue();
			}
		}
		IInterface r0_if0 = r0.createInterface("r0_if0");
		r0_if0.setBitRate("10000000"); //1e7, slow
		r0_if0.setLatency("0.0");
		r0_if0.createFluidQueue();
		
		IRouter r1 = top.createRouter("r1");
		IInterface r1_iface[] = new IInterface[10];
		for(int i=1; i<10;i++){
			r1_iface[i] = r1.createInterface("ifr1_h"+i);
			r1_iface[i].setBitRate("1000000000"); //1e9
			r1_iface[i].setLatency("0.0");
			if(i%9 != 0){
				r1_iface[i].createFluidQueue();
			}
		}
		
		IInterface r1_if0 = r1.createInterface("r1_if0");
		r1_if0.setBitRate("10000000"); //1e7, slow
		r1_if0.setLatency("0.0");
		r1_if0.createFluidQueue();
		
		IHost h[] = new IHost[19];
		IInterface iface[] = new IInterface[19];
		ITCPMaster t_master[] = new ITCPMaster[2];
		IUDPMaster u_master[] = new IUDPMaster[2];
		
		for(int i=1 ; i<19 ; i++){
			h[i] = top.createHost("h"+i);
			iface[i] = h[i].createInterface("if0");	
			iface[i].setBitRate("1000000000"); //1e9
			iface[i].setLatency("0.0");
			if(i%9 != 0){
				if(System.getProperty("BGTYPE", "-1").equals("1")){
					iface[i].createFluidQueue();
				}
				if(System.getProperty("BGTYPE", "-1").equals("2")){
					h[i].createUDPMaster();
					h[i].createCBR();
				}
			}else if(i==9){
				if(System.getProperty("FGTYPE", "-1").equals("1")){
					t_master[0] = h[i].createTCPMaster();
					h[i].createHTTPServer();
				}
				if(System.getProperty("FGTYPE", "-1").equals("2")){
					u_master[0] = h[i].createUDPMaster();
					h[i].createCBR();	
				}
			}else{
				if(System.getProperty("FGTYPE", "-1").equals("1")){
					t_master[1] = h[i].createTCPMaster();
					h[i].createHTTPClient();
				}
				if(System.getProperty("FGTYPE", "-1").equals("2")){
					u_master[1] = h[i].createUDPMaster();
					h[i].createCBR();	
				}
			}
		}
		
		for(int i=0; i<2; i++){
			if(System.getProperty("FGTYPE", "-1").equals("1")){
				t_master[i].setMss("960"); //maximum segment size
				t_master[i].setRcvWndSize("320000"); //receive buffer size
				t_master[i].setSndWndSize("320000"); //maximum send window size
			}
			if(System.getProperty("FGTYPE", "-1").equals("2")){
				u_master[i].setMaxDatagramSize("1024"); //max UDP datagram size (payload bytes)
			}
		}
		
		//link between routers
		ILink l_r0_r1= top.createLink("r0_r1");
		l_r0_r1.setDelay("0.005");
		l_r0_r1.createInterface(r0_if0);
		l_r0_r1.createInterface(r1_if0);		
		
		//links between router and host
		ILink l_r0[] = new ILink[10];
		
		for(int i=1 ; i<10 ; i++){
			l_r0[i] = top.createLink("l_r0_h"+i);
			l_r0[i].createInterface((IInterface)r0.getChildByName("ifr0_h"+i));
			l_r0[i].createInterface((IInterface)h[i].getChildByName("if0"));
			l_r0[i].setDelay((float)0.005); 
		}
		
		ILink l_r1[] = new ILink[10];
		
		for(int i=1 ; i<10 ; i++){
			int j=i+9;
			l_r1[i] = top.createLink("l_r1_h"+j);
			l_r1[i].createInterface((IInterface)r1.getChildByName("ifr1_h"+i));
			l_r1[i].createInterface((IInterface)h[j].getChildByName("if0"));
			l_r1[i].setDelay((float)0.005); 
		}
			
		ITraffic t = top.createTraffic();
		
		//set background traffic
		if(System.getProperty("BGTYPE", "-1").equals("1")){
			IFluidTraffic f1=t.createFluidTraffic();	
			f1.setSrcs("{.:h1}");
			f1.setDsts("{.:h10}");
			f1.setNflows("5");
			f1.setHurst("0.8");
			f1.setStart("0");
			f1.setOffTime("0.045");
			f1.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IFluidTraffic f2=t.createFluidTraffic();	
			f2.setSrcs("{.:h2}");
			f2.setDsts("{.:h11}");
			f2.setNflows("5");
			f2.setHurst("0.8");
			f2.setStart("1");
			f2.setOffTime("0.045");
			f2.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IFluidTraffic f3=t.createFluidTraffic();	
			f3.setSrcs("{.:h3}");
			f3.setDsts("{.:h12}");
			f3.setNflows("5");
			f3.setHurst("0.8");
			f3.setStart("2");
			f3.setOffTime("0.045");
			f3.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IFluidTraffic f4=t.createFluidTraffic();	
			f4.setSrcs("{.:h4}");
			f4.setDsts("{.:h13}");
			f4.setNflows("5");
			f4.setHurst("0.8");
			f4.setStart("3");
			f4.setOffTime("0.045");
			f4.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IFluidTraffic f5=t.createFluidTraffic();	
			f5.setSrcs("{.:h14}");
			f5.setDsts("{.:h5}");
			f5.setNflows("1");
			f5.setHurst("0.8");
			f5.setStart("0.5");
			f5.setOffTime("0.045");
			f5.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IFluidTraffic f6=t.createFluidTraffic();	
			f6.setSrcs("{.:h15}");
			f6.setDsts("{.:h6}");
			f6.setNflows("1");
			f6.setHurst("0.8");
			f6.setStart("1.5");
			f6.setOffTime("0.045");
			f6.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IFluidTraffic f7=t.createFluidTraffic();	
			f7.setSrcs("{.:h16}");
			f7.setDsts("{.:h7}");
			f7.setNflows("1");
			f7.setHurst("0.8");
			f7.setStart("2.5");
			f7.setOffTime("0.045");
			f7.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IFluidTraffic f8=t.createFluidTraffic();	
			f8.setSrcs("{.:h17}");
			f8.setDsts("{.:h8}");
			f8.setNflows("1");
			f8.setHurst("0.8");
			f8.setStart("3.5");
			f8.setOffTime("0.045");
			f8.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
		}
		if(System.getProperty("BGTYPE", "-1").equals("2")){
			IPPBPTraffic p1=t.createPPBPTraffic();	
			p1.setSrcs("{.:h1}");
			p1.setDsts("{.:h10}");
			p1.setAvgSessions("5");
			p1.setHurst("0.8");
			p1.setStartTime("0");
			p1.setIntervalExponential("True");
			p1.setInterval("0.045");
			p1.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IPPBPTraffic p2=t.createPPBPTraffic();	
			p2.setSrcs("{.:h2}");
			p2.setDsts("{.:h11}");
			p2.setAvgSessions("5");
			p2.setHurst("0.8");
			p2.setStartTime("1");
			p2.setIntervalExponential("True");
			p2.setInterval("0.045");
			p2.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IPPBPTraffic p3=t.createPPBPTraffic();	
			p3.setSrcs("{.:h3}");
			p3.setDsts("{.:h12}");
			p3.setAvgSessions("5");
			p3.setHurst("0.8");
			p3.setStartTime("2");
			p3.setIntervalExponential("True");
			p3.setInterval("0.045");
			p3.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IPPBPTraffic p4=t.createPPBPTraffic();	
			p4.setSrcs("{.:h4}");
			p4.setDsts("{.:h13}");
			p4.setAvgSessions("5");
			p4.setHurst("0.8");
			p4.setStartTime("3");
			p4.setIntervalExponential("True");
			p4.setInterval("0.045");
			p4.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IPPBPTraffic p5=t.createPPBPTraffic();	
			p5.setSrcs("{.:h14}");
			p5.setDsts("{.:h5}");
			p5.setAvgSessions("10");
			p5.setHurst("0.8");
			p5.setStartTime("0.5");
			p5.setIntervalExponential("True");
			p5.setInterval("0.045");
			p5.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IPPBPTraffic p6=t.createPPBPTraffic();	
			p6.setSrcs("{.:h15}");
			p6.setDsts("{.:h6}");
			p6.setAvgSessions("10");
			p6.setHurst("0.8");
			p6.setStartTime("1.5");
			p6.setIntervalExponential("True");
			p6.setInterval("0.045");
			p6.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IPPBPTraffic p7=t.createPPBPTraffic();	
			p7.setSrcs("{.:h16}");
			p7.setDsts("{.:h7}");
			p7.setAvgSessions("10");
			p7.setHurst("0.8");
			p7.setStartTime("2.5");
			p7.setIntervalExponential("True");
			p7.setInterval("0.045");
			p7.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
			
			IPPBPTraffic p8=t.createPPBPTraffic();	
			p8.setSrcs("{.:h17}");
			p8.setDsts("{.:h8}");
			p8.setAvgSessions("10");
			p8.setHurst("0.8");
			p8.setStartTime("3.5");
			p8.setIntervalExponential("True");
			p8.setInterval("0.045");
			p8.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
		}
		
		if(System.getProperty("FGTYPE", "-1").equals("1")){
			//application, http
			ITCPTraffic http = t.createTCPTraffic();
			http.setIntervalExponential("True");
			http.setInterval("0.045");
			http.setDstPort(80);
			http.setFileSize(3000000);
			http.setConnectionsPerSession("1");
			http.setNumberOfSessions(1);
			http.setStartTime("10");
			http.setSrcs("{.:h18}");
			http.setDsts("{.:h9}");
			http.setTrafficTypeSeed("100");
		}
		
		if(System.getProperty("FGTYPE", "-1").equals("2")){
			//application, cbr
			IUDPTraffic cbr = t.createUDPTraffic();
			cbr.setInterval("0.001");
			cbr.setBytesToSendEachInterval("1024");
			cbr.setStartTime("10");
			cbr.setCount("3000");
			cbr.setSrcs("{.:h9}");
			cbr.setDsts("{.:h18}");
		}
		
		return top;
	}

}
