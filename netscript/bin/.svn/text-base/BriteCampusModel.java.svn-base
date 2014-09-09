import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.TrafficFactory;
import jprime.Host.IHost;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.models.BaselineCampus;
import jprime.util.XMLModelBuilder;

public class BriteCampusModel extends BaselineCampus{
	private static final int NUM_FLOWS = Integer.parseInt(System.getProperty("NUM_FLOWS", "100")); //number of flows in total
	private static final long FLOW_SIZE = Long.parseLong(System.getProperty("FLOW_SIZE", "10000000")); 
	private static final double START_TIME = Double.valueOf(System.getProperty("START_TIME", "0")); //start time for the first flow
	private static final double NUM_AS = Double.valueOf(System.getProperty("NUM_AS", "3")); //number of ASes that you want to attach campus networks
	private static final double FLOW_RATE = Double.valueOf(System.getProperty("FLOW_RATE", "1")); //arrival rate of flows
	private static final boolean EXPONENTIAL_ARRIVAL=Boolean.parseBoolean(System.getProperty("EXPONENTIAL_ARRIVAL","true"));
	
	public BriteCampusModel(Database db, Experiment exp) {
		super(db, exp, RoutingType.SHORTEST_PATH_L123);
	}
	
	public BriteCampusModel(Database db, String expName) {
		super(db, expName, RoutingType.SHORTEST_PATH_L123);
	}
	
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		String xml_file = System.getProperty("XML", "/export/home/tingli/workspace/primex/netscript/test/xml_models/iccc-cnf.xml");
		int sub_size = Integer.parseInt(System.getProperty("Campuses_Per_AS", "1")); //number of campus attached per AS
		ArrayList<IHost> clients = new ArrayList<IHost>();
		ArrayList<IHost> servers = new ArrayList<IHost>();
		ArrayList<String> client_name = new ArrayList<String>();
		ArrayList<String> server_name = new ArrayList<String>();

		if(xml_file == null) {
			throw new RuntimeException("must set the xml file to use via -DXML=some_file.xml");
		}
		if(sub_size <=0) {
			throw new RuntimeException("must set the subnet size via -DSUB_SIZE=some_size and the size must be >0.");
		}
		
		XMLModelBuilder xml = new XMLModelBuilder(exp,xml_file);
		xml.createModel();
		INet topnet = xml.getExp().getRootNode();
		Random rand = new Random(System.currentTimeMillis());
		//Random rand = new Random(0);

		// we assume that it is a two level structure
		if(topnet.getStaticRoutingProtocol() == null)
			topnet.createShortestPath();
		ArrayList<INet> subs = new ArrayList<INet>();
		for(IModelNode c : topnet.getAllChildren()) {
			if(c instanceof INet) {
				INet as = (INet)c;
				if(as.getStaticRoutingProtocol() == null)
					as.createShortestPath();
				subs.add(as);
			}
		}
		Collections.shuffle(subs, rand);
		for(int k = 0; k<NUM_AS;k++) {
			ArrayList<IRouter> rs = new ArrayList<IRouter>();
			for(IModelNode sc : subs.get(k).getAllChildren()) {
				if(sc instanceof IRouter) {
					rs.add((IRouter)sc);
				}
			}
			Collections.shuffle(rs, rand);		
			for(int i = 0; i<sub_size;i++) {
				IRouter r = rs.get(i);
				INet campus = subs.get(k).createNet();
				IRouter net0_r0=createBaselineCampus(campus);	
				ILink l1 = subs.get(k).createLink();
				l1.createInterface(r.createInterface());
				l1.createInterface(net0_r0.createInterface());
				//l1.createInterface((IInterface)net0_r0.getChildByName("if7"));
				l1.setBandwidth(1000000000);
				//collect servers
				INet net1 = (INet)campus.getChildByName("net01").getChildByName("net1"); 
				for(IModelNode c : net1.getAllChildren()) {
					if(c instanceof IHost && !(c instanceof IRouter)) {
						IHost server = (IHost)c;
						servers.add(server);
						server_name.add(server.getUniqueName().toString());
					}
				}
				//collect clients
				INet net2 = (INet)campus.getChildByName("net2"); 
				for(IModelNode c : net2.getAllChildren()) {
					if(c instanceof INet) {
						for(IModelNode d : c.getAllChildren()) {
							if(d instanceof IHost && !(d instanceof IRouter)) {
								IHost client = (IHost)d;
								clients.add(client);
								client_name.add(client.getUniqueName().toString());
							}
						}
					}
				}
				INet net3 = (INet)campus.getChildByName("net3"); 
				for(IModelNode c1 : net3.getAllChildren()) {
					if(c1 instanceof INet) {
						for(IModelNode d : c1.getAllChildren()) {
							if(d instanceof IHost && !(d instanceof IRouter)) {
								IHost client = (IHost)d;
								clients.add(client);
								client_name.add(client.getUniqueName().toString());
							}
						}
					}
				}
			}
		}
		Collections.shuffle(clients, rand);
		Collections.shuffle(servers, rand);
		
		TrafficFactory trafficFactory = new TrafficFactory(topnet);
		double start=START_TIME;

		for(int i=0;i<NUM_FLOWS;i++) {			
			System.out.println("client="+clients.get(i%clients.size()).getUniqueName()+", server="+servers.get(i%servers.size()).getUniqueName());
			trafficFactory.createSimulatedTCP(start, FLOW_SIZE, clients.get(i%clients.size()), servers.get(i%servers.size()));
			if(EXPONENTIAL_ARRIVAL){
				start += exponential(FLOW_RATE, rand); 
			}else{
				start += 1/FLOW_RATE;
			}
		}		
		
		return topnet;
	}
	
	public double exponential(double rate, Random rand) {
		if(rate <= 0) {
		  throw new RuntimeException("the rate must be >0.");
		}
		return -1.0/(rate*Math.log(1.0-rand.nextDouble()));
	}
}
