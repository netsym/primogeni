import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.TCPTraffic.ITCPTraffic;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.models.BaseCampus;
import jprime.util.ModelInterface;
import jprime.util.XMLModelBuilder;


/*
 * @author Ting Li
 */

public class GPULargeModel extends BaseCampus{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.FILE, "XML","XML","XML", "/export/home/tingli/workspace/primex/netscript/test/xml_models/BriteForGPU.xml"));
		rv.add(new ModelParam(ModelParamType.INT, "NUM_CAMPUS","NUM_CAMPUS","NUM_CAMPUS", "20"));
		rv.add(new ModelParam(ModelParamType.FLOAT, "LOCALITY","LOCALITY","LOCALITY", "0.5"));
		rv.add(new ModelParam(ModelParamType.INT, "NUM_FLOWS","NUM_FLOWS","NUM_FLOWS", "10"));
		return rv;
	}

	public GPULargeModel(Database db, Experiment exp) {
		super(db, exp, getParams());
	}
	
	public GPULargeModel(Database db, String expName) {
		super(db, expName,getParams());
	}
	
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		//The backbone has 20 ASes, each with 20 routers. 
		String xml_file = parameters.get("XML").asString();
		int num_campus=parameters.get("NUM_CAMPUS").asInt();
		double locality = parameters.get("LOCALITY").asDouble();
		int num_flows = parameters.get("NUM_FLOWS").asInt(); 

		if(xml_file == null) {
			throw new RuntimeException("must set the xml file to use via -DXML=some_file.xml");
		}
		if(locality < 0 || locality > 1) {
			throw new RuntimeException("must set the cnf density via -DLOCALITY=some_locality and the density must be >=0 and <=1.");
		}
		
		XMLModelBuilder xml = new XMLModelBuilder(exp,xml_file);
		xml.createModel();
		INet topnet = xml.getExp().getRootNode();
		Random rand = new Random(System.currentTimeMillis());

		// we assume that it is a two level structure
		if(topnet.getStaticRoutingProtocol() == null)
			topnet.createShortestPath();
		ArrayList<INet> subs = new ArrayList<INet>();
		ArrayList<INet> campuses = new ArrayList<INet>();
	
		//create routing sphere for each subnet
		for(IModelNode c : topnet.getAllChildren()) {
			if(c instanceof INet) {
				INet as = (INet)c;
				System.out.println("At as "+as.getUniqueName());
				if(as.getStaticRoutingProtocol() == null)
					as.createShortestPath();
				subs.add(as);
			}
		}
		if(num_campus<subs.size()){
			throw new RuntimeException("The number of campuses should be greater or equal to the number of ASes!");
		}
		//create campus network
		//System.out.println("CREATE CAMPUS 0 ");
		INet campus0 = subs.get(0).createNet("Campus0");
		createCampus(campus0,RoutingType.SHORTEST_PATH_L123); //return net01_r4
		{
			IRouter r = (IRouter)campus0.getChildByName("net01").getChildByName("net0").getChildByName("r0"); 
			if(r == null) {
				throw new RuntimeException("Cannot find net01:net0:r0 in campus!");
			}
			IInterface ack = r.createInterface("attach_to_router");
			if(ack != (IInterface)campus0.getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("attach_to_router")) {
				throw new RuntimeException("Failed to create an interface on net01:net0:r0!");
			}
		}	
		
		//Intra traffic
		{
			//XXX, use fluid traffic
			ITraffic t = campus0.createTraffic();
			for(int i=0;i<5;i++) {
				ITCPTraffic p = t.createTCPTraffic();
				p.setSrcs("{.:net3:net3_lan"+i+":?Host}");
				p.setDsts("{.:net01:net1:?Host}");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			for(int j=0;j<5;j++) {
				ITCPTraffic p = t.createTCPTraffic();
				p.setSrcs("{.:net3:net3_lan"+j+":?Host}");
				p.setDsts("{.:net2:net2_lan0:?Host}");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
		}
		
		campuses.add(campus0);
		
		//replicas
		for(int i =1;i<num_campus;i++) {
			//System.out.println("CREATE CAMPUS  "+i);
			INet campus_replica = subs.get(i%subs.size()).createNetReplica("Campus"+i,campus0);
			//INet campus_replica = (INet) campus0.copy("Campus"+i, subs.get(i%subs.size()));
			campuses.add(campus_replica);
		}
		
		int num_campus_per_as = num_campus/subs.size();
		
		//Attach campus to router	
		for(int i=0; i<subs.size();i++) {
			INet as=subs.get(i);
			ArrayList<IRouter> rs = new ArrayList<IRouter>();
			for(IModelNode sc : as.getAllChildren()) {
				if(sc instanceof IRouter) {
					rs.add((IRouter)sc);
				}
			}
			Collections.shuffle(rs, rand);
			
			for(int j=0; j<num_campus_per_as; j++) {
				IRouter r=rs.get(j);
				System.out.println("Attaching campus to "+r.getUniqueName());
				ILink l1 = as.createLink();
				System.out.println("first interface ");
				l1.createInterface(r.createInterface());
				System.out.println("second interface ");
				l1.createInterface((IInterface)as.getChildByName("Campus"+(i+j*20)).
						getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("attach_to_router"));
			}
		}
		//Cal the popularity of the campus networks
		float[] popularity = Zipf(0, num_campus);
		
		//Make a pool of campus indexes according to the popularity for sampling the destination
		int sample_total = 1000000;
		List<Integer> campus_index_for_sampling = new ArrayList<Integer>();
		for(int i=0; i<num_campus;i++){
			int n= (int)Math.round(sample_total*popularity[i]);
			if(n==0) n=1;
			for(int j=0; j<n; j++){
				campus_index_for_sampling.add(i);
			}
		}
		Collections.shuffle(campus_index_for_sampling, rand);
			
		//Inter traffic
		{
			ITraffic tt = topnet.createTraffic();
			//XXX use fluid traffic
			for(int i=0;i<num_campus;i++) {
				int index = rand.nextInt(campus_index_for_sampling.size());
				while(index==i){
					index = rand.nextInt(campus_index_for_sampling.size());
				}
				for(int j=0;j<num_flows;j++) {
					ITCPTraffic p = tt.createTCPTraffic();
					p.setSrcs("{.:campus"+i+":net2:net2_lan"+j+":?Host}");
					p.setDsts("{.:campus"+index+":net2:net2_lan"+j+":?Host}");
					//p.setIntervalExponential("true");
					p.setMapping("one2one");
				}
				for(int j=0;j<num_flows;j++) {
					ITCPTraffic p = tt.createTCPTraffic();
					p.setSrcs("{.:campus"+i+":net3:net3_lan"+j+":?Host}");
					p.setDsts("{.:campus"+index+":net3:net3_lan"+j+":?Host}");
					//p.setIntervalExponential("true");
					p.setMapping("one2one");
				}
			}
		}
		
		return topnet;
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
