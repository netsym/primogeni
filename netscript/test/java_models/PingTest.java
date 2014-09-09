import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.FluidTraffic.IFluidTraffic;
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
public class PingTest extends ModelInterface{

	/**
	 * @param db
	 * @param exp
	 */
	public PingTest(Database db, Experiment exp) {
		super(db, exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public PingTest(Database db, String expName) {
		super(db, expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		
		IRouter r0 = top.createRouter("router");
		r0.setHostSeed("1");
		IInterface r0_h1= r0.createInterface("ifh1");
		IInterface r0_h2= r0.createInterface("ifh2");
		IInterface r0_h3= r0.createInterface("ifh3");
		IInterface r0_h4= r0.createInterface("ifh4");
		r0_h1.createFluidQueue();
		r0_h2.createFluidQueue();
		r0_h3.createFluidQueue();
		r0_h4.createFluidQueue();
		
		IHost h1 = top.createHost("host1");
		h1.setHostSeed("1");
		IInterface h1_if0= h1.createInterface("if0");
		h1_if0.createFluidQueue();

		IHost h2 = top.createHost("host2");
		h2.setHostSeed("1");
		IInterface h2_if0= h2.createInterface("if0");
		h2_if0.createFluidQueue();
		
		IHost h3 = top.createHost("host3");
		h3.setHostSeed("1");
		IInterface h3_if0= h3.createInterface("if0");
		h3_if0.createFluidQueue();

		IHost h4 = top.createHost("host4");
		h4.setHostSeed("1");
		IInterface h4_if0= h4.createInterface("if0");
		h4_if0.createFluidQueue();
		
		ILink l_h1= top.createLink("router_h1");
		l_h1.createInterface(r0_h1);
		l_h1.createInterface(h1_if0);

		ILink l_h2= top.createLink("router_h2");
		l_h2.createInterface(r0_h2);
		l_h2.createInterface(h2_if0);
		
		ILink l_h3= top.createLink("router_h3");
		l_h3.createInterface(r0_h3);
		l_h3.createInterface(h3_if0);

		ILink l_h4= top.createLink("router_h4");
		l_h4.createInterface(r0_h4);
		l_h4.createInterface(h4_if0);
		
		ITraffic t = top.createTraffic();
		
		IFluidTraffic f1=t.createFluidTraffic();
		f1.setSrcs(".:host1");
		f1.setDsts(".:host2");	
		//f1.setNflows("20");
		f1.setStart("2");
		f1.setStop("8");
		//f1.setStop("300");
		//f1.setMapping("one2one");
		
		IPingTraffic p1 = t.createPingTraffic();
		p1.setCount(1);
		//p1.setStartTime("0.5");
		p1.setSrcs(".:host1");
		p1.setDsts(".:host1");
		p1.setIntervalExponential("true");
		//p1.setMapping("one2one");
		
		/*IPingTraffic p2 = t.createPingTraffic();
		p2.setCount(100);
		p2.setStartTime("0.5");
		p2.setSrcs(".:host3");
		p2.setDsts(".:host4");
		p2.setIntervalExponential("true");*/
		//p2.setMapping("one2one");
		
		if("true".equalsIgnoreCase(System.getProperty("COMPLEX", "false"))) {
			IPingTraffic p3 = t.createPingTraffic();
			p3.setCount(2);
			p3.setStartTime("0.5");
			p3.setSrcs("{.:host1,.:host2}");
			p3.setDsts("{.:host3,.:host4}");
			p3.setIntervalExponential("true");
			p3.setMapping("one2one");
			
			IFluidTraffic f2=t.createFluidTraffic();	
			f2.setSrcs("{.:host3,.:host4}");
			f2.setDsts("{.:host1,.:host2}");	
			f2.setNflows("10");
			f2.setStop("10");
		}
	
		return top;
	}

}
