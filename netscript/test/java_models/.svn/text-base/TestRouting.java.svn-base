import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.IModelNode;
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
public class TestRouting extends ModelInterface{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.INT, "CxE matrix","E","E", "3"));
		rv.add(new ModelParam(ModelParamType.INT, "CxE matrix","C","C", "3"));
		rv.add(new ModelParam(ModelParamType.INT, "H routing spheres","H","H", "3"));
		rv.add(new ModelParam(ModelParamType.INT, "S chains","S","S", "3"));
		rv.add(new ModelParam(ModelParamType.INT, "#of pings","P","P", "3"));
		rv.add(new ModelParam(ModelParamType.INT, "cache size","CS","CS", "1"));
		rv.add(new ModelParam(ModelParamType.INT, "# parts","NUM_PARTS","NUM_PARTS", "1"));
		return rv;
	}

	
	private static class RV {
		public final ArrayList<IRouter> prev, next;
		public final ArrayList<IHost> hosts;
		public final IHost src;
		public RV(ArrayList<IRouter> prev, ArrayList<IRouter> next, IHost btmhost, ArrayList<IHost> hosts) {
			super();
			this.prev = prev;
			this.next = next;
			this.hosts=hosts;
			this.src = btmhost;
		}
	}
	/**
	 * @param db
	 * @param exp
	 */
	public TestRouting(Database db, Experiment exp) {
		super(db,exp,getParams());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public TestRouting(Database db, String expName) {
		super(db,expName,getParams());
	}
	
	private static ILink createLink(INet net, String name, IInterface if1, IInterface if2) {
		if1.setLatency("0");
		if1.setBitRate("1000000000");
		if1.setBufferSize("150000");
		if2.setLatency("0");
		if2.setBitRate("1000000000");
		if2.setBufferSize("150000");
		ILink l = net.createLink(name);
		l.setDelay("0.1");
		l.setBandwidth("1000000000");
		l.createInterface(if1);
		l.createInterface(if2);
		return l;

	}

	private static void linkSubnets(final INet upper, final INet lower, final int E, final int C) {
		System.out.println("\t\t\tLinking subnets\n\t\t\t\t"+upper.getUniqueName()+"\n\t\t\t\t" +lower.getUniqueName());
		for(int i=0;i<E;i++) {
			IModelNode down = upper.getChildByName("r_"+(C-1)+"_"+i);
			IModelNode up = lower.getChildByName("r_0_"+i);
			//YYY IModelNode down = upper.getChildByName("subnet").getChildByName("r_"+(C-1)+"_"+i);
			//YYY IModelNode up = lower.getChildByName("subnet").getChildByName("r_0_"+i);
			if(down == null || !(down instanceof IRouter)) {
				throw new RuntimeException("Should not happen!");
			}
			if(up == null || !(up instanceof IRouter)) {
				throw new RuntimeException("Should not happen!");
			}
			IInterface d = (IInterface)((IRouter)down).getChildByName("edge");
			IInterface u = (IInterface)((IRouter)up).getChildByName("edge");
			if(d == null || !(d instanceof IInterface)) {
				throw new RuntimeException("Should not happen! d="+d+", (d instanceof IInterface)="+(d instanceof IInterface));
			}
			if(u == null || !(u instanceof IInterface)) {
				throw new RuntimeException("Should not happen! u="+u+", (u instanceof IInterface)="+(u instanceof IInterface));
			}
			System.out.println("\t\t\t\t\t"+u.getUniqueName()+"\n\t\t\t\t\t\t" +d.getUniqueName());
			createLink(upper, "le_"+lower.getName()+"_"+i, d, u);
		}
	}

	private static RV createSubnet(final INet net, final int E, final int C, boolean addHostsToAll, boolean istopsubnet) {
		System.out.println("\t\tCreating subnet "+net.getUniqueName());
		IRouter r[][] = new IRouter[C][E];
		ArrayList<IHost> hosts=new ArrayList<IHost>();
		int lc=0;
		for(int i=0;i<C;i++) {
			for(int j=0;j<E;j++) {
				r[i][j]=net.createRouter("r_"+i+"_"+j);
				if(i>0) {
					IInterface d = r[i-1][j].createInterface("down");
					IInterface u = r[i][j].createInterface("up");
					createLink(net, "l_i"+i+"_i"+(i-1)+"_j"+j, d, u);
					lc++;
					if(i+1==C) {
						r[i][j].createInterface("edge");
					}
				}
				else {
					r[i][j].createInterface("edge");
				}
				if(j>0) {
					IInterface n = r[i][j-1].createInterface("next");
					IInterface p = r[i][j].createInterface("prev");
					createLink(net, "l_i"+i+"_j"+(j-1)+"_j"+j, n, p);
					lc++;
				}
				if(addHostsToAll) {
					IHost h = net.createHost("h_"+i+"_"+j);
					IInterface ri = r[i][j].createInterface("host");
					IInterface hi = h.createInterface("if0");
					createLink(net, "lh_"+i+"_"+j, ri, hi);
					hosts.add(h);
					lc++;
				}

			}
		}
		IHost h = null;
		if(!addHostsToAll) {
			h = net.createHost("host");
			IInterface ri = r[C-1][E-1].createInterface("host");
			IInterface hi = h.createInterface("if0");
			createLink(net, "l_host", ri, hi);
			lc++;
		}
		System.out.println("\t\t\tCreated "+lc+" links in subnet "+net.getUniqueName());
		//ZZZ r[0][0].createInterface("prev_slice");
		//ZZZZ r[C-1][E-1].createInterface("next_slice");
		ArrayList<IRouter> prev = new ArrayList<IRouter>();
		ArrayList<IRouter> next = new ArrayList<IRouter>();
		if(istopsubnet) {
			for(int i=0;i<C;i++) {
				r[i][0].createInterface("prev_slice");
				r[i][E-1].createInterface("next_slice");
				prev.add(r[i][0]);
				next.add(r[i][E-1]);
			}
		}
		return new RV(prev,next,h,hosts);
	}
	
	private static RV createSlice(final INet top, final int E, final int C, int H, int CS, boolean addHostsToAll) {
		System.out.println("\tCreating slice "+top.getUniqueName());
		RV first=null, last=null;
		INet parent=null;
		//YYY INet firstsub=null;
		boolean firstsub=true;
		while(H>0) {
			INet net = (parent==null)?top:(parent.createNet("owner"+H));
			//XXX another routing bug
			System.out.println("\t[WARN]I am forced to make all owning nets routing spheres or we get a null-pointer!");
			net.createShortestPath();
			net.getRoutingSphere().setLocalDstCacheSize(CS);
			INet sub = net;
			/*YYY
			INet sub = null;
			if(addHostsToAll && H==1) {
				sub = net.createNet("subnet");
				sub.createShortestPath();
				sub.getRoutingSphere().setLocalDstCacheSize(CS);
			}
			else if(firstsub==null) {
				sub = net.createNet("subnet");
				sub.createShortestPath();
				sub.getRoutingSphere().setLocalDstCacheSize(CS);
				firstsub=sub;
			}
			else {
				//XXX sub = net.createNetReplica("subnet",firstsub);
				//XXX
				System.out.println("\t[WARN] I cannot replicate subnet because it causes a routing error!");
				sub = net.createNet("subnet");
				sub.createShortestPath();
				sub.getRoutingSphere().setLocalDstCacheSize(CS);
			}*/
			RV rv = createSubnet(sub,E,C,(addHostsToAll && H==1),firstsub);
			if(parent != null) {
				linkSubnets(parent, net, E, C);
			}
			if(first == null) {
				first=rv;
				last=rv;
			}
			else {
				last=rv;
			}
			parent=net;
			H--;
			firstsub=false;
		}
		return new RV(first.prev, first.next, first.src, last.hosts);
	}
	
	private static RV cloneSlice(INet slice, RV rvi) {
		System.out.println("\tCloningSlice");
		IModelNode sub= slice;
		ArrayList<IRouter> prev = new ArrayList<IRouter>();
		ArrayList<IRouter> next = new ArrayList<IRouter>();
		//YYY IModelNode sub= slice.getChildByName("subnet");
		if(sub == null || !(sub instanceof INet))
			throw new RuntimeException("WHAT HAPPENED?");
		for(IRouter r: rvi.prev) {
			IModelNode p = sub.getChildByName(r.getName());
			if(p == null || !(p instanceof IRouter))
				throw new RuntimeException("WHAT HAPPENED?");
			prev.add((IRouter)p);
		}
		for(IRouter r: rvi.next) {
			IModelNode n = sub.getChildByName(r.getName());
			if(n== null || !(n instanceof IRouter))
				throw new RuntimeException("WHAT HAPPENED?");
			next.add((IRouter)n);
		}
		return new RV(prev,next,null,null);
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final int E=parameters.get("E").asInt(),
		C=parameters.get("C").asInt(),
		H=parameters.get("H").asInt(),
		S=parameters.get("S").asInt(),
		P=parameters.get("P").asInt(),
		CS=parameters.get("CS").asInt(),
		num_parts=parameters.get("NUM_PARTS").asInt();
		Net topnet = exp.createTopNet("topnet");
		topnet.createShortestPath();
		
		INet slices[] = new INet[S];
		RV rv[]  = new RV[S];
		
		for(int i=0;i<S;i++) {
			if(i==0)System.out.println("*************************************");
			System.out.println("slice "+i);
			System.out.println("*************************************");
			if(i!=0 && i+1!=S) {
				//we are in the middle
				if(num_parts==1) {
					slices[i] = topnet.createNetReplica("slice"+i,slices[0]);
					rv[i]=cloneSlice(slices[i], rv[0]);
				}
				else {
					System.out.println("\tWe currently have a hack here --- no replications for multiple partitions!");
					slices[i] = topnet.createNet("slice"+i);
					rv[i]=createSlice(slices[i],E,C,H,CS,i+1==S);
				}
			}
			else {
				slices[i] = topnet.createNet("slice"+i);
				rv[i]=createSlice(slices[i],E,C,H,CS,i+1==S);
			}
			if(i>0) {
				for(int j=0;j<rv[i-1].prev.size();j++) {
					IInterface n = (IInterface)rv[i-1].next.get(j).getChildByName("next_slice");
					IInterface p = (IInterface)rv[i].prev.get(j).getChildByName("prev_slice");
					if(n == null || !(n instanceof IInterface))
						throw new RuntimeException("WHAT HAPPENED?");
					if(p == null || !(p instanceof IInterface))
						throw new RuntimeException("WHAT HAPPENED?");
					System.out.println("\tLinking slices\n\t\t"+n.getUniqueName()+"\n\t\t" +p.getUniqueName());
					createLink(topnet, "l_"+(i-1)+"_"+i+"_"+j, n, p);
				}
			}
			System.out.println("*************************************");
		}
		ITraffic traffic =topnet.createTraffic();
		final String src="."+rv[0].src.getUniqueName().toString().substring(topnet.getName().toString().length());
		System.out.println("*************************************");
		System.out.println("\tsrc="+src);
		for(IHost h:rv[S-1].hosts) {
			final String dst="."+h.getUniqueName().toString().substring(topnet.getName().toString().length());
			System.out.println("\t\tdst="+dst);
			IPingTraffic ping = traffic.createPingTraffic();
			ping.setCount(P);
			ping.setStartTime("0");
			ping.setInterval("10");
			ping.setSrcs("{"+src+"}");
			ping.setDsts("{"+dst+"}");
			ping.setIntervalExponential("true");
			ping.setInterval(".5");
			ping.setMapping("one2one");
		}
		System.out.println("*************************************");

		return topnet;
	}
	
}
