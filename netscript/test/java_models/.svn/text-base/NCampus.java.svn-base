import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.PingTraffic.IPingTraffic;
import jprime.Router.IRouter;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.models.BaseCampus;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class NCampus extends BaseCampus {
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.INT, "NUM_CAMPUS","NUM_CAMPUS","NUM_CAMPUS", "8"));
		rv.add(new ModelParam(ModelParamType.INT, "NUM_LEVELS","NUM_LEVELS","NUM_LEVELS", "1"));
		rv.add(new ModelParam(ModelParamType.INT, "TRAFFIC_TYPE","TRAFFIC_TYPE","TRAFFIC_TYPE", "-1"));
		rv.add(new ModelParam(ModelParamType.INT, "REP_LEVEL","REP_LEVEL","REP_LEVEL", "100"));
		rv.add(new ModelParam(ModelParamType.BOOLEAN, "INTRA_CAMPUS_TRAFFIC","INTRA_CAMPUS_TRAFFIC","INTRA_CAMPUS_TRAFFIC", "false"));
		rv.add(new ModelParam(ModelParamType.BOOLEAN, "INTER_CAMPUS_TRAFFIC","INTER_CAMPUS_TRAFFIC","INTER_CAMPUS_TRAFFIC", "false"));
		return rv;
	}

	
	public static enum todo {
		COPY,
		REPLICATE,
	};
	
	public static todo whattodo(int cid, int NUM_CAMPUS, int REP_LEVEL) {
		if(cid==0)throw new RuntimeException("wtf?");
		final float l = (((float)cid)/NUM_CAMPUS)*100;
		if(l <= REP_LEVEL) return todo.REPLICATE;
		return todo.COPY;
	}
	private static class Level {
		final int level;
		final Net topnet,base;
		final int remaining;
		public Level(int level, Net topnet, Net base, int remaining) {
			this.level = level;
			this.topnet=topnet;
			this.base = base;
			this.remaining = remaining;
		}
		public String toString() {
			return "<level "+level+" topnet="+topnet.getUniqueName()+" base="+base.getUniqueName()+" remaining="+remaining+">";
		}

	}

	/**
	 * @param db
	 * @param exp
	 */
	public NCampus(Database db, Experiment exp) {
		super(db, exp, getParams());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public NCampus(Database db, String expName) {
		super(db, expName, getParams());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final int NUM_CAMPUS=parameters.get("NUM_CAMPUS").asInt();
		final int NUM_LEVELS=parameters.get("NUM_LEVELS").asInt();
		final boolean INTRA_CAMPUS_TRAFFIC=parameters.get("INTRA_CAMPUS_TRAFFIC").asBoolean();
		final boolean INTER_CAMPUS_TRAFFIC=parameters.get("INTER_CAMPUS_TRAFFIC").asBoolean();
		final int TRAFFIC_TYPE=parameters.get("TRAFFIC_TYPE").asInt();
		final int REP_LEVEL=parameters.get("REP_LEVEL").asInt();
		
		if(NUM_LEVELS < 1)
			throw new RuntimeException("Need 1 or more levels (-DNUM_LEVELS=...)");
		if(NUM_CAMPUS < 2)
			throw new RuntimeException("Need 2 or more campuses (-DNUM_CAMPUS=...)");

		final Net TOPNET = exp.createTopNet("topnet");

		LinkedList<Level> levels = new LinkedList<NCampus.Level>();
		while(levels.size()<NUM_LEVELS) {
			System.out.println("levels.size()["+levels.size()+"]<NUM_LEVELS["+NUM_LEVELS+"]");
			if(NUM_LEVELS-levels.size()==1) {
				if(NUM_LEVELS==1) {
					//just one level here....
					System.out.println("\tCreating ONLY level");
					Net b = buildFirstLevel(TOPNET, NUM_CAMPUS, NUM_LEVELS,INTRA_CAMPUS_TRAFFIC, INTER_CAMPUS_TRAFFIC, TRAFFIC_TYPE, REP_LEVEL);
					levels.add(new Level(0,TOPNET,b,0));
					System.out.println("\tCreated ONLY level:: "+levels.getLast().toString());
				}
				else {
					System.out.println("\tCreating bottom level");
					final Level prev = levels.getLast();
					final Net topnet = (Net)prev.base.createNet("net_0");
					buildFirstLevel(topnet, NUM_CAMPUS, NUM_LEVELS,INTRA_CAMPUS_TRAFFIC, INTER_CAMPUS_TRAFFIC, TRAFFIC_TYPE, REP_LEVEL);
					levels.add(new Level(prev.level+1, prev.base,topnet, NUM_CAMPUS-1));
					System.out.println("\tCreated bottom level:: "+levels.getLast().toString());
				}
			}
			else {
				if(levels.size()==0) {
					System.out.println("\tCreating top level:");
					final Net base = (Net)TOPNET.createNet("net_0");
					base.createShortestPath();
					TOPNET.createShortestPath();
					levels.add(new Level(0, TOPNET,base, NUM_CAMPUS-1));
					System.out.println("\tCreated top level:: "+levels.getLast().toString());
				}
				else {
					System.out.println("\tCreating itermediate level");
					final Level prev = levels.getLast();
					final Net topnet = (Net)prev.base;//.createNet("net_0");
					final Net base = (Net)topnet.createNet("net_0");
					base.createShortestPath();
					levels.add(new Level(prev.level+1, topnet,base, NUM_CAMPUS-1));
					System.out.println("\tCreated itermediate level:: "+levels.getLast().toString());
				}	
			}
		}
		while(levels.size()>0) {
			buildLevel(levels.removeLast(),NUM_CAMPUS,REP_LEVEL);
		}
		return TOPNET;
	}

	private void buildLevel(Level l, int NUM_CAMPUS, int REP_LEVEL) {
		System.out.println("Buidling level "+l.toString());
		INet prev_sub = l.base;
		for(int i=0;i<l.remaining;i++) {
			String name = "net_"+(i+1);
			System.out.println("\tCreating "+name+" in "+l.topnet.getUniqueName()+" from "+l.base.getUniqueName());
			INet sub = null;
			switch(whattodo(i+1,NUM_CAMPUS,REP_LEVEL)) {
			case COPY:
				sub = (INet)l.base.copy(name, l.topnet);
				System.out.println("\t\tCreated net "+sub.getUniqueName()+"[copy]");
				break;
			case REPLICATE:
				sub = l.topnet.createNetReplica(name, l.base);
				System.out.println("\t\tCreated net "+sub.getUniqueName()+"[replica]");
				break;
			}

			
			IInterface prev=null,next=null;
			try {
				prev=(IInterface)prev_sub.get("main_router.next_level");
				next=(IInterface)sub.get("main_router.prev_level");
			} catch(RuntimeException e) {
				System.out.println("[base]"+l.base.getUniqueName()+":");
				for(IModelNode c : l.base.getAllChildren()) {
					System.out.println("\t"+c.getName());
				}
				System.out.println("[prev]"+prev_sub.getUniqueName()+":");
				for(IModelNode c : prev_sub.getAllChildren()) {
					System.out.println("\t"+c.getName());
				}
				System.out.println("[next]"+sub.getUniqueName()+":");
				for(IModelNode c : sub.getAllChildren()) {
					System.out.println("\t"+c.getName());
				}
				throw e;
			}
			ILink link = l.topnet.createLink("link__"+prev_sub.getName()+"__"+sub.getName());
			link.setBandwidth("1000000000");
			link.setDelay("0.01");
			link.attachInterface(prev);
			link.attachInterface(next);
			System.out.println("\t\tCreated link "+link.getUniqueName()+" which links "+prev_sub.getUniqueName()+" and "+sub.getUniqueName());
			System.out.println("\tCreated "+sub.getUniqueName());
			prev_sub=sub;
		}
		//link n-1 and 0
		if(l.remaining>0){
			System.out.println("\tCreating main router");
			IRouter main_router = l.topnet.createRouter("main_router");
			if(l.level>0) {
				main_router.createInterface("prev_level");
				main_router.createInterface("next_level");
			}
			IInterface prev=null;
			try {
				prev=(IInterface)prev_sub.get("main_router.next_level");
			} catch(Exception e) {
				prev=(IInterface)prev_sub.get("campus_0.net01.net0.r0.next_level");
			}
			ILink link = l.topnet.createLink("link__"+prev_sub.getName()+"__"+main_router.getName());
			link.setBandwidth("1000000000");
			link.setDelay("0.01");
			link.attachInterface(prev);
			link.attachInterface(main_router.createInterface("prev_local_level"));
			System.out.println("\t\tCreated link "+link.getUniqueName()+" which links "+prev_sub.getUniqueName()+" and "+main_router.getUniqueName());


			IInterface next=null;
			try {
				next=(IInterface)l.base.get("main_router.prev_level");
			} catch(Exception e) {
				next=(IInterface)l.base.get("campus_0.net01.net0.r0.prev_level");
			}
			link = l.topnet.createLink("link__"+l.base.getName()+"__"+main_router.getName());
			link.setBandwidth("1000000000");
			link.setDelay("0.01");
			link.attachInterface(next);
			link.attachInterface(main_router.createInterface("next_local_level"));

			System.out.println("\t\tCreated link "+link.getUniqueName()+" which links "+main_router.getUniqueName()+" and "+l.base.getUniqueName());
			System.out.println("\tCreated main router:: "+main_router.getUniqueName());
		}

		System.out.println("Done level "+l.toString());
		//for(IModelNode c :  l.topnet.getAllChildren())
		//	System.out.println("\t"+c.getUniqueName());

	}


	private Net buildFirstLevel(final Net top, final int NUM_CAMPUS, final int NUM_LEVELS, final boolean INTRA_CAMPUS_TRAFFIC, final boolean INTER_CAMPUS_TRAFFIC, final int TRAFFIC_TYPE, final int REP_LEVEL) {
		if(top.getStaticRoutingProtocol() == null)
			top.createShortestPath();
		ITraffic tt = top.createTraffic();
		INet nets[] = new INet[NUM_CAMPUS];
		nets[0] = top.createNet("campus_0");
		IRouter mr = null;
		if(NUM_LEVELS>1) {
			mr = top.createRouter("main_router");
			System.out.println("\t\tCreated main router::"+mr.getUniqueName());
		}
		System.out.println("\t\tCreated net "+nets[0].getUniqueName());

		createCampus(nets[0],RoutingType.SHORTEST_PATH_L123);
		{
			IRouter r = (IRouter)nets[0].getChildByName("net01").getChildByName("net0").getChildByName("r0"); 
			if(r == null) {
				throw new RuntimeException("ACK!");
			}
			IInterface ack = r.createInterface("prev_net");
			if(ack != (IInterface)nets[0].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("prev_net")) {
				throw new RuntimeException("ACK!");
			}
			ack = r.createInterface("next_net");
			if(ack != (IInterface)nets[0].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("next_net")) {
				throw new RuntimeException("ACK!");
			}
			if(NUM_LEVELS>1) {
				r.createInterface("up_level");
			}

			if(INTRA_CAMPUS_TRAFFIC) {
				ITraffic t = nets[0].createTraffic();
				for(int i=0;i<5;i++) {
					IPingTraffic p = t.createPingTraffic();
					p.setSrcs("{.:net3:net3_lan"+i+":?Host}");
					p.setDsts("{.:net01:net1:?Host}");
					p.setInterval("0.001");
					p.setMapping("one2one");
					
					IPingTraffic p1 = t.createPingTraffic();
					p1.setSrcs("{.:net01:net1:?Host}");
					p1.setDsts("{.:net3:net3_lan"+i+":?Host}");
					p1.setInterval("0.001");
					p1.setMapping("one2one");
				}
				for(int j=0;j<7;j++) {
					IPingTraffic p = t.createPingTraffic();
					p.setSrcs("{.:net01:net1:?Host}");
					p.setDsts("{.:net2:net2_lan"+j+":?Host}");
					p.setInterval("0.001");
					p.setMapping("one2one");
					
					IPingTraffic p1 = t.createPingTraffic();
					p1.setSrcs("{.:net2:net2_lan"+j+":?Host}");
					p1.setDsts("{.:net01:net1:?Host}");
					p1.setInterval("0.001");
					p1.setMapping("one2one");
				}
			}
		}
		for(int i=1;i<NUM_CAMPUS;i++) {
			switch(whattodo(i,NUM_CAMPUS,REP_LEVEL)) {
			case COPY:
				nets[i] =  (INet)nets[0].copy("campus_"+i, top);
				System.out.println("\t\tCreated net "+nets[i].getUniqueName()+"[copy from "+nets[0].getUniqueName()+"]");
				break;
			case REPLICATE:
				nets[i] = top.createNetReplica("campus_"+i,nets[0]);
				System.out.println("\t\tCreated net "+nets[i].getUniqueName()+"[replica from "+nets[0].getUniqueName()+"]");
				break;
			}
			IInterface prev = (IInterface)nets[0].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("next_net"); 
			IInterface cur =  (IInterface)nets[0].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("prev_net"); 
			if(prev == null || cur==null) {
				throw new RuntimeException("XXX");
			}
			prev = (IInterface)nets[i].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("next_net"); 
			cur =  (IInterface)nets[i].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("prev_net"); 
			if(prev == null || cur==null) {
				throw new RuntimeException("XXX");
			}
		}
		for(int i=0;i<NUM_CAMPUS;i++) {
			IInterface prev = (IInterface)nets[(i+1)%NUM_CAMPUS].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("next_net"); 
			IInterface cur = (IInterface)nets[i].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("prev_net"); 
			if(prev == null || cur==null) {
				throw new RuntimeException("XXX");
			}
			ILink l = top.createLink("l_"+(((i+1)%NUM_CAMPUS))+"_"+i);
			l.setBandwidth("1000000000");
			l.setDelay("0.01");
			l.createInterface(prev);
			l.createInterface(cur);
			System.out.println("\t\tCreated link "+l.getUniqueName()+" which links "+nets[((i+1)%NUM_CAMPUS)].getUniqueName()+" and "+nets[i].getUniqueName());
		}
		
		if(NUM_LEVELS>1) {
			for(int i=0;i<NUM_CAMPUS;i++) {
				ILink l = top.createLink("up_"+i);
				l.setBandwidth("1000000000");
				l.setDelay("0.01");
				l.createInterface(mr.createInterface("n_"+i));
				l.createInterface((IInterface)nets[i].getChildByName("net01").getChildByName("net0").getChildByName("r0").getChildByName("up_level"));
				System.out.println("\t\tCreated link "+l.getUniqueName()+" which links "+nets[i].getUniqueName()+" and "+mr.getUniqueName());
			}
			mr.createInterface("prev_level");
			mr.createInterface("next_level");
		}

		if(INTER_CAMPUS_TRAFFIC) {
			for(int i=0;i<NUM_CAMPUS;i++) {
				for(int j=0;j<7;j++) {
					IPingTraffic p = tt.createPingTraffic();
					p.setSrcs("{.:campus_"+i+":net2:net2_lan"+j+":?Host}");
					p.setDsts("{.:campus_"+((i+2)%NUM_CAMPUS)+":net01:net1:?Host}");
					p.setInterval("0.001");
					p.setMapping("one2one");
					
					IPingTraffic p1 = tt.createPingTraffic();
					p1.setSrcs("{.:campus_"+((i+2)%NUM_CAMPUS)+":net01:net1:?Host}");
					p1.setDsts("{.:campus_"+i+":net2:net2_lan"+j+":?Host}");
					p1.setInterval("0.001");
					p1.setMapping("one2one");
				}
				for(int j=0;j<5;j++) {
					IPingTraffic p = tt.createPingTraffic();
					p.setSrcs("{.:campus_"+i+":net3:net3_lan"+j+":?Host}");
					p.setDsts("{.:campus_"+((i+2)%NUM_CAMPUS)+":net01:net1:?Host}");
					p.setInterval("0.001");
					p.setMapping("one2one");
					
					IPingTraffic p1 = tt.createPingTraffic();
					p1.setSrcs("{.:campus_"+((i+2)%NUM_CAMPUS)+":net01:net1:?Host}");
					p1.setDsts("{.:campus_"+i+":net3:net3_lan"+j+":?Host}");
					p1.setInterval("0.001");
					p1.setMapping("one2one");
				}
			}
		}
		/*if(NUM_CAMPUS>=3) {
			if(TRAFFIC_TYPE == 1 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_"+(NUM_CAMPUS-1)+":net2:net2_lan0:h2");
				p.setDsts(".:campus_0:net3:net3_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 2 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_"+(NUM_CAMPUS-1)+":net2:net2_lan0:h2");
				p.setDsts(".:campus_0:net2:net2_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 3 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_"+(NUM_CAMPUS-1)+":net3:net3_lan0:h2");
				p.setDsts(".:campus_0:net3:net3_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 4 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_"+(NUM_CAMPUS-1)+":net01:net1:h2");
				p.setDsts(".:campus_0:net3:net3_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 5 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();			
				p.setSrcs(".:campus_0:net01:net1:h2");
				p.setDsts(".:campus_"+(NUM_CAMPUS-1)+":net01:net1:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 6 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_"+(NUM_CAMPUS-1)+":net01:net1:h2");
				p.setDsts(".:campus_0:net2:net2_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 7 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_0:net01:net1:h2");
				p.setDsts(".:campus_0:net2:net2_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 8 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_0:net01:net1:h2");
				p.setDsts(".:campus_0:net3:net3_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}
			if(TRAFFIC_TYPE == 9 || TRAFFIC_TYPE==100){
				IPingTraffic p = tt.createPingTraffic();
				p.setSrcs(".:campus_0:net2:net2_lan0:h2");
				p.setDsts(".:campus_0:net3:net3_lan0:h2");
				//p.setIntervalExponential("true");
				p.setMapping("one2one");
			}		
		}*/
		return top;
	}


}
