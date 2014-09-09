import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.FluidTraffic.IFluidTraffic;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author TingLi
 */
public class FluidTest extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public FluidTest(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public FluidTest(Database db, String expName) {
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
		
		IRouter r0 = top.createRouter("router0");
		IInterface r0_h1= r0.createInterface("ifh1");
		r0_h1.createFluidQueue();
		IInterface r0_r1= r0.createInterface("ifr1");
		r0_r1.setBitRate("10000000"); //10Mbps
        r0_r1.setLatency("0.0");
        r0_r1.setBufferSize("125000");
		r0_r1.createFluidQueue();
		
		IRouter r1 = top.createRouter("router1");
		IInterface r1_r0= r1.createInterface("ifr0");
		IInterface r1_h2= r1.createInterface("ifh2");
		r1_h2.createFluidQueue();
		r1_r0.setBitRate("10000000"); //10Mbps
        r1_r0.setLatency("0.0");
        r1_r0.setBufferSize("125000");
        r1_r0.createFluidQueue();
		
		IHost h1 = top.createHost("host1");
		IInterface h1_if0= h1.createInterface("if0");
		h1_if0.setBitRate("1000000000000"); //10Mbps
		h1_if0.createFluidQueue();
	
		
		IHost h2 = top.createHost("host2");
		IInterface h2_if0= h2.createInterface("if0");
		h2_if0.setBitRate("1000000000000"); //10Mbps
		h2_if0.createFluidQueue();
			
		ILink l_h1= top.createLink("r0_h1");
		l_h1.createInterface(r0_h1);
		l_h1.createInterface(h1_if0);
		l_h1.setDelay((float)0.0000001); 

		ILink l_h2= top.createLink("r1_h2");
		l_h2.createInterface(r1_h2);
		l_h2.createInterface(h2_if0);
		l_h2.setDelay((float)0.0000001); 
		
		ILink l_r0_r1= top.createLink("l_r0_r1");
		l_r0_r1.createInterface(r1_r0);
		l_r0_r1.createInterface(r0_r1);
        l_r0_r1.setDelay((float)0.001); 
        l_r0_r1.setBandwidth("10000000"); //10Mbps
			
		ITraffic t = top.createTraffic();
		IFluidTraffic f1=t.createFluidTraffic();	
		f1.setSrcs("{.:host2}");
		f1.setDsts("{.:host1}");
		f1.setNflows("5");
		f1.setHurst("0.8");
		f1.setStart("0");
		f1.setOffTime("0.045");
		f1.setTrafficTypeSeed(System.getProperty("BGSEED","0"));
		f1.setMonfn("monitor");
		
		return top;
	}
}
