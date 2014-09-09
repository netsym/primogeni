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
import jprime.TCPTraffic.ITCPTraffic;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class Dumbbell extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public Dumbbell(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelInterface.ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public Dumbbell(Database db, String expName) {
		super(db, expName, new ArrayList<ModelInterface.ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		//top.createAlgorithmicRouting();
		
		IRouter r0 = top.createRouter("router0");
		IInterface r0_h1= r0.createInterface("ifh1");
		IInterface r0_r1= r0.createInterface("ifr1");
		r0_r1.setBitRate("1000000000"); //1G
        r0_r1.setLatency("0.0");
        r0_r1.setBufferSize("65535");
        r0_h1.setBitRate("1000000000"); //1G
        r0_h1.setLatency("0.0");
        r0_h1.setBufferSize("65535");
		
		IRouter r1 = top.createRouter("router1");
		IInterface r1_r0= r1.createInterface("ifr0");
		IInterface r1_h2= r1.createInterface("ifh2");
		r1_r0.setBitRate("1000000000"); //1G
        r1_r0.setLatency("0.0");
        r1_r0.setBufferSize("65535");
        r1_h2.setBitRate("1000000000"); //1G
        r1_h2.setLatency("0.0");
        r1_h2.setBufferSize("65535");

        
        
		IHost h1 = top.createHost("host1");
		IInterface h1_if0= h1.createInterface("if0");
		h1_if0.setBitRate("10000000000"); //1G
        h1_if0.setLatency("0.0");
        h1_if0.setBufferSize("65535");
		ITCPMaster tcp1 = h1.createTCPMaster();
		tcp1.setMss("1400");
		//tcp1.setSamplingInterval("0.1");


		IHost h2 = top.createHost("host2");
		IInterface h2_if0= h2.createInterface("if0");
		h2_if0.setBitRate("10000000000"); //1G
        h2_if0.setLatency("0.0");
        h2_if0.setBufferSize("65535");
		ITCPMaster tcp2 = h2.createTCPMaster();
		tcp2.setMss("1400");
		//tcp2.setSamplingInterval("0.1");
			
		ILink l_h1= top.createLink("r0_h1");
		l_h1.createInterface(r0_h1);
		l_h1.createInterface(h1_if0);
		l_h1.setDelay((float)0.0000001); 
		l_h1.setBandwidth("10000000000");

		ILink l_h2= top.createLink("r1_h2");
		l_h2.createInterface(r1_h2);
		l_h2.createInterface(h2_if0);
		l_h2.setDelay((float)0.0000001); 
		l_h2.setBandwidth("10000000000");
		
		ILink l_r0_r1= top.createLink("l_r0_r1");
		l_r0_r1.createInterface(r1_r0);
		l_r0_r1.createInterface(r0_r1);
        l_r0_r1.setDelay((float)0.001); 
		int b = Integer.parseInt(System.getProperty("MBS","100"))*1000000;
		System.out.println("Bandwidth="+b+" Mb/s");
		l_r0_r1.setBandwidth(Integer.toString(b)); 
			
		ITraffic t = top.createTraffic();
		
		ITCPTraffic h = t.createTCPTraffic();
		h.setSessionTimeout("3000");
		h.setFileSize(10000000);
		h.setConnectionsPerSession("1");
		h.setNumberOfSessions(1);
		h.setStartTime("0");
		h.setSrcs("{.:host1}");
		h.setDsts("{.:host2}");
		
		return top;
	}

}
