import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.TrafficFactory;
import jprime.Host.IHost;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.database.Database;
import jprime.models.BaselineCampus;


/*
 * @author Ting Li
 */

public class CampusWithZipfTraffic extends BaselineCampus{
	private static final int NUM_FLOWS = Integer.parseInt(System.getProperty("NUM_FLOWS", "10")); 
	private static final long FLOW_SIZE = Long.parseLong(System.getProperty("FLOW_SIZE", "10000")); 
	private static final double START_TIME = Double.valueOf(System.getProperty("START_TIME", "0"));
	private static final double FLOW_RATE = Double.valueOf(System.getProperty("FLOW_RATE", "1"));
	private static final boolean EXPONENTIAL_ARRIVAL=Boolean.parseBoolean(System.getProperty("EXPONENTIAL_ARRIVAL","true"));
	
	public CampusWithZipfTraffic(Database db, Experiment exp) {
		super(db, exp, RoutingType.SHORTEST_PATH_L123);
	}
	
	public CampusWithZipfTraffic(Database db, String expName) {
		super(db, expName, RoutingType.SHORTEST_PATH_L123);
	}
	
	public INet buildModel(Map<String, ModelParamValue> values) {
		Random rand = new Random(System.currentTimeMillis());

		INet top = exp.createTopNet("topnet");
		createBaselineCampus(top);
	
		ArrayList<IHost> clients = new ArrayList<IHost>();
		ArrayList<IHost> servers = new ArrayList<IHost>();
	
		//collect servers
		INet net1 = (INet)top.getChildByName("net01").getChildByName("net1"); 
		for(IModelNode c : net1.getAllChildren()) {
			if(c instanceof IHost && !(c instanceof IRouter)) {
				IHost server = (IHost)c;
				System.out.println("At server "+server.getUniqueName());
				servers.add(server);
			}
		}
		//collect clients
		INet net2 = (INet)top.getChildByName("net2"); 
		for(IModelNode c : net2.getAllChildren()) {
			if(c instanceof INet) {
				for(IModelNode d : c.getAllChildren()) {
					if(d instanceof IHost && !(d instanceof IRouter)) {
						IHost client = (IHost)d;
						System.out.println("Net2, at client "+client.getUniqueName());
						clients.add(client);
					}
				}
			}
		}
		INet net3 = (INet)top.getChildByName("net3"); 
		for(IModelNode c1 : net3.getAllChildren()) {
			if(c1 instanceof INet) {
				for(IModelNode d : c1.getAllChildren()) {
					if(d instanceof IHost && !(d instanceof IRouter)) {
						IHost client = (IHost)d;
						System.out.println("Net3, at client "+client.getUniqueName());
						clients.add(client);
					}
				}
			}
		}
		
		System.out.println("client size="+clients.size()+", server size="+servers.size());
		
		//Cal the popularity of the clients
		float[] c_popularity = Zipf(0, clients.size());
		//Cal the popularity of the servers
		float[] s_popularity = Zipf(0, servers.size());
		
		//Make a pool of client indexes according to the popularity
		int sample_total = 1000000;
		List<Integer> client_index_for_sampling = new ArrayList<Integer>();
		for(int i=0; i<clients.size();i++){
			int n= (int)Math.round(sample_total*c_popularity[i]);
			if(n==0) n=1;
			for(int j=0; j<n; j++){
				client_index_for_sampling.add(i);
			}
		}
		Collections.shuffle(client_index_for_sampling, rand);
		
		
		//Make a pool of client indexes according to the popularity
		List<Integer> server_index_for_sampling = new ArrayList<Integer>();
		for(int i=0; i<servers.size();i++){
			int n= (int)Math.round(sample_total*s_popularity[i]);
			if(n==0) n=1;
			for(int j=0; j<n; j++){
				server_index_for_sampling.add(i);
			}
		}
		Collections.shuffle(client_index_for_sampling, rand);
		
		TrafficFactory trafficFactory = new TrafficFactory(top);
		double start=START_TIME;
		//trafficFactory.createSimulatedTCP(start, FLOW_SIZE, clients.get(0), servers.get(0));

		for(int i=0;i<NUM_FLOWS;i++) {
			int s_index = rand.nextInt(server_index_for_sampling.size());
			int c_index = rand.nextInt(client_index_for_sampling.size());

			s_index = server_index_for_sampling.get(s_index-1);
			c_index = client_index_for_sampling.get(c_index-1);
			
			System.out.println("client="+clients.get(c_index).getUniqueName()+", server="+servers.get(s_index).getUniqueName());
			trafficFactory.createSimulatedTCP(start, FLOW_SIZE, clients.get(c_index), servers.get(s_index));
			if(EXPONENTIAL_ARRIVAL){
				start += exponential(FLOW_RATE, rand); 
			}else{
				start += 1/FLOW_RATE;
			}
		}		
		return top;
	}
	
	public double exponential(double rate, Random rand) {
		if(rate <= 0) {
		  throw new RuntimeException("the rate must be >0.");
		}
		return -1.0/(rate*Math.log(1.0-rand.nextDouble()));
	}
	
	public float[] Zipf(float theta, int N){
		/*
		 * zipfian - p(i) = c / i ^^ (1 - theta)
		 * At theta = 1, uniform *
		 * at theta = 0, pure zipfian
		 */
	    float sum= 0;
	    float c= 0;
	    float expo;
	    int i;
	    float[] zdist = new float[N];
	    expo = 1 - theta;

	    for (i = 1; i <= N; i++) {
	        sum += 1.0 /(float) Math.pow((double) i, (double) (expo));
	    }
	    c = 1 / sum;

	    for (i = 0; i < N; i++) {
	         zdist[i] = c / (float) Math.pow((double) (i + 1), (double) (expo));
	    }

	    return zdist;
	}
}
