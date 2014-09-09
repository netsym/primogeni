import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.TrafficFactory;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.util.ModelInterface;
/**
 * @author TingLi
 */
public class SimpleModel extends ModelInterface{
	
	/**
	 * @param db
	 * @param exp
	 */
	public SimpleModel(Database db, Experiment exp) {
		super(db,exp, new ArrayList<ModelParam>());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public SimpleModel(Database db, String expName) {
		super(db,expName, new ArrayList<ModelParam>());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		Net topnet = exp.createTopNet("topnet");
		topnet.createShortestPath();
		
		IInterface[] ir = new IInterface[12];
		IHost hosts[] = new IHost[30];
		IRouter routers[] = new IRouter[3];

		for(int i=0; i<routers.length;i++) {
			routers[i] = topnet.createRouter("router_"+i);
			for(int j=0; j<ir.length;j++) {
				ir[j] = routers[i].createInterface("if_"+j);
				ir[j].setBitRate("1000000000");
				ir[j].setLatency(0);
			}
		}
		
		for(int i=0; i<hosts.length;i++) {
			hosts[i] = topnet.createHost("host"+i);
			IInterface h_if0= hosts[i].createInterface("if0");
			h_if0.setBitRate("1500000000");
			h_if0.setLatency(0);
		}
		
		//connect the hosts and routers
		for(int i=0; i<routers.length;i++) {
			for(int j=0; j<10;j++) {
				int k=j+i*10;
				ILink l_h = topnet.createLink("ll"+i+"_"+k);
				l_h.createInterface((IInterface)hosts[k].getChildByName("if0"));
				l_h.createInterface((IInterface)routers[i].getChildByName("if_"+j));
				l_h.setDelay("0.00000001");
			}
		}
		
		//Connect routers
		ILink l= topnet.createLink("l_1_0");
		l.createInterface((IInterface)routers[0].getChildByName("if_10"));
		l.createInterface((IInterface)routers[1].getChildByName("if_10"));
		l.setDelay("0.1");
		
		l= topnet.createLink("l_1_2");
		l.createInterface((IInterface)routers[2].getChildByName("if_10"));
		l.createInterface((IInterface)routers[1].getChildByName("if_11"));
		l.setDelay("0.1");
		
		TrafficFactory trafficFactory = new TrafficFactory(topnet);
		
		trafficFactory.createSimulatedTCP(0, 10000000, hosts[0], hosts[11]);
		trafficFactory.createSimulatedTCP(1, 10000000, hosts[1], hosts[21]);	
		trafficFactory.createSimulatedTCP(2, 10000000, hosts[12], hosts[2]);		
		trafficFactory.createSimulatedTCP(3, 10000000, hosts[13], hosts[22]);
		trafficFactory.createSimulatedTCP(4, 10000000, hosts[23], hosts[13]);
		trafficFactory.createSimulatedTCP(5, 10000000, hosts[23], hosts[3]);
		
		return topnet;
	}
}