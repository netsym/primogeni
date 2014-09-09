import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.StatusListener;
import jprime.CNFApplication.ICNFApplication;
import jprime.CNFTraffic.ICNFTraffic;
import jprime.Host.IHost;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Router.IRouter;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;
import jprime.util.XMLModelBuilder;

public class ICCC_CNF extends ModelInterface{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.FLOAT, "DENSITY","DENSITY","DENSITY", "0.1"));
		rv.add(new ModelParam(ModelParamType.INT, "SUB_SIZE","SUB_SIZE","SUB_SIZE", "1"));
		rv.add(new ModelParam(ModelParamType.INT, "CONTENT_SIZE","CONTENT_SIZE","CONTENT_SIZE", "10000000"));
		rv.add(new ModelParam(ModelParamType.INT, "NUM_REQUESTS","NUM_REQUESTS","NUM_REQUESTS", "10"));
		rv.add(new ModelParam(ModelParamType.STRING, "XML","XML","XML", "test/xml_models/iccc-cnf.xml"));
		return rv;
	}

	public ICCC_CNF(Database db, Experiment exp) {
		super(db, exp, getParams());
	}
	
	private IRouter buildSub(INet topnet, int sub_size, ArrayList<IHost> hosts) {
		INet sub = topnet.createNet();
		sub.createShortestPath();
		IRouter r = sub.createRouter();
		for(int i =0;i<sub_size;i++) {
			IHost h = sub.createHost();
			ILink l = sub.createLink();
			l.createInterface(h.createInterface());
			l.createInterface(r.createInterface());
			hosts.add(h);
		}
		return r;		
	}
	
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		String xml_file = parameters.get("XML").asString();
		double density = parameters.get("DENSITY").asDouble();
		int sub_size = parameters.get("SUB_SIZE").asInt();
		int content_size = parameters.get("CONTENT_SIZE").asInt();
		int num_requests = parameters.get("NUM_REQUESTS").asInt();
	
		if(xml_file == null) {
			throw new RuntimeException("must set the xml file to use via -DXML=some_file.xml");
		}
		if(density <=0 || density >1) {
			throw new RuntimeException("must set the cnf density via -DDENSITY=some_density and the density must be >0 and <=1.");
		}
		if(sub_size <=0) {
			throw new RuntimeException("must set the subnet size via -DSUB_SIZE=some_size and the size must be >0.");
		}
		
		XMLModelBuilder xml = new XMLModelBuilder(exp,xml_file);
		xml.createModel();
		INet topnet = xml.getExp().getRootNode();
		Random rand = new Random(System.currentTimeMillis());

		// we assume that it is a two level structure
		if(topnet.getStaticRoutingProtocol() == null)
			topnet.createShortestPath();
		ArrayList<INet> subs = new ArrayList<INet>();
		ArrayList<IRouter> all_routers = new ArrayList<IRouter>();
		ArrayList<IRouter> controllers = new ArrayList<IRouter>();
		ArrayList<IHost> hosts = new ArrayList<IHost>();
		for(IModelNode c : topnet.getAllChildren()) {
			if(c instanceof INet) {
				INet as = (INet)c;
				System.out.println("At as "+as.getUniqueName());
				if(as.getStaticRoutingProtocol() == null)
					as.createShortestPath();
				subs.add(as);
			}
		}
		for(INet as : subs) {
			ArrayList<IRouter> rs = new ArrayList<IRouter>();
			for(IModelNode sc : as.getAllChildren()) {
				if(sc instanceof IRouter) {
					rs.add((IRouter)sc);
				}
			}
			Collections.shuffle(rs, rand);
			IRouter controller = rs.remove(0);
			controller.setAsCNFController();
			controllers.add(controller);	
			
			for(IRouter r : rs) {
				all_routers.add(r);
				System.out.println("Attaching hosts to "+r.getUniqueName());
				IRouter rsub1 = buildSub(as, sub_size, hosts);
				IRouter rsub2 = buildSub(as, sub_size, hosts);
				all_routers.add(rsub1);
				all_routers.add(rsub2);
				ILink l1 = as.createLink();
				l1.createInterface(r.createInterface());
				l1.createInterface(rsub1.createInterface());
				ILink l2 = as.createLink();
				l2.createInterface(r.createInterface());
				l2.createInterface(rsub2.createInterface());
				ILink l3 = as.createLink();
				l3.createInterface(rsub1.createInterface());
				l3.createInterface(rsub2.createInterface());
			}
		}
		
		Collections.shuffle(all_routers, rand);
		Collections.shuffle(hosts, rand);
		int max = ((int)(density*all_routers.size()));
		System.out.println("making "+max+" routers run cnf out of "+all_routers.size());
		
		for(int i =0;i<max;i++) {
			all_routers.get(i).createCNFTransport();
		}
		
		
		HashMap<Integer,String> srcs=new HashMap<Integer, String>();
		String dsts=null;
		
		for(int j = 0; j<hosts.size();j++) {
			IHost toFilter=hosts.get(j);
			String t = null;
			for(IHost h : hosts) {
				if(h.getDBID()==toFilter.getDBID())continue;
				if(t==null)t="{";
				else t+=",";
				String uname = h.getUniqueName().toString();
				t+=".:"+uname.substring(topnet.getName().length()+1);
			}
			t+="}";
			srcs.put(j,t);
		}

		int i=1;		
		for(IHost h : hosts) {
			String uname = h.getUniqueName().toString();
			if(dsts==null)dsts="{"+".:"+uname.substring(topnet.getName().length()+1)+"}";
			ICNFApplication cnf = h.createCNFApplication();
			cnf.addContentId(i, content_size);
			i++;
		}
		ITraffic tt = topnet.createTraffic();
		for(i = 0; i<hosts.size();i++) {
			ICNFTraffic c = tt.createCNFTraffic();
			//c.setStartTime(""+((float)i/2.0));
			c.setStartTime(i+rand.nextInt(1));
			//c.setStartTime(rand.nextInt(hosts.size()));
			c.setSrcs(srcs.get(i));
			c.setDsts(dsts);
			c.setNumberOfContents(hosts.size());
		    c.setNumberOfRequests(hosts.size()*num_requests);
			c.setContentId((long)(i+1));
		}
		
		/*for(i = 0; i<hosts.size();i++) {
			IPingTraffic p = tt.createPingTraffic();
			p.setCount(1);
			//p.setStartTime("0");
			p.setSrcs(srcs);
			p.setDsts(srcs);
			p.setIntervalExponential("true");
			p.setMapping("all2all");
		}*/
	
		exp.compile(new StatusListener());
		
		String controller_uids = null;
		String host_uids = null;
		String cnf_router_uids = null;
		
		
		for(i =0;i<max;i++) {
			if(cnf_router_uids == null)cnf_router_uids="";
			else cnf_router_uids+=", ";
			cnf_router_uids+=all_routers.get(i).getUID();
		}
		for(IRouter r: controllers) {
			if(controller_uids == null)controller_uids="";
			else controller_uids+=", ";
			controller_uids+=r.getUID();
		}
		for(IHost h : hosts) {
			if(host_uids == null)host_uids="";
			else host_uids+=", ";
			host_uids+=h.getUID();
		}
		System.out.println("controller_uids=["+controller_uids+"]");
		System.out.println("cnf_router_uids=["+cnf_router_uids+"]");
		System.out.println("host_uids=["+host_uids+"]");
		
		return topnet;
	}
}
