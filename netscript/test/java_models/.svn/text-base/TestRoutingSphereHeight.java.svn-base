import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.PingTraffic.IPingTraffic;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;

/**
 * @author Nathanael Van Vorst
 */
public class TestRoutingSphereHeight extends ModelInterface{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.INT, "P","P","P", "4"));
		rv.add(new ModelParam(ModelParamType.INT, "W","W","W", "4"));
		rv.add(new ModelParam(ModelParamType.BOOLEAN, "DO_TRANSIT","DO_TRANSIT","DO_TRANSIT", "true"));
		return rv;
	}

	
	private final int LEFT=0;
	private final int RIGHT=1;
	private final int TOP=2;
	private final int DOWN=3;
	/**
	 * @param db
	 * @param exp
	 */
	public TestRoutingSphereHeight(Database db, Experiment exp) {
		super(db,exp,getParams());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public TestRoutingSphereHeight(Database db, String expName) {
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
		//System.out.println("Linking "+if1.getUniqueName()+" and "+if2.getUniqueName()+" within net "+net.getUniqueName());
		return l;

	}
	
	public IHost[][] buildsphere(INet topnet, boolean do_up, boolean do_down, int P, int W, boolean do_transit) {
		
		IHost[][] hosts = new IHost[3][W];
		IInterface[][][] interfaces = new IInterface[3][W][4];
		for(int i=0;i<3;i++) {
			for(int j=0;j<W;j++) {
				hosts[i][j] = topnet.createHost(i+"_"+j);
				switch(i) {
				case 0:
					interfaces[i][j][DOWN] = hosts[i][j].createInterface("d");
					if(do_up)
						interfaces[i][j][TOP] = hosts[i][j].createInterface("t");
					break;
				case 1:
					interfaces[i][j][TOP] = hosts[i][j].createInterface("t");
					interfaces[i][j][DOWN] = hosts[i][j].createInterface("d");
					createLink(topnet, "l_i"+(i-1)+"_i"+i+"_j"+j, interfaces[i-1][j][DOWN], interfaces[i][j][TOP]);
					break;
				case 2:
					interfaces[i][j][TOP] = hosts[i][j].createInterface("t");
					if(do_down)
						interfaces[i][j][DOWN] = hosts[i][j].createInterface("d");
					createLink(topnet, "l_i"+(i-1)+"_i"+i+"_j"+j, interfaces[i-1][j][DOWN], interfaces[i][j][TOP]);
					break;
				}
				if(j==0) {
					interfaces[i][j][RIGHT] = hosts[i][j].createInterface("r");
				}
				else if (j==W-1) {
					interfaces[i][j][LEFT] = hosts[i][j].createInterface("l");
					createLink(topnet, "l_i"+i+"_j"+j+"_j"+(j-1), interfaces[i][j-1][RIGHT], interfaces[i][j][LEFT]);
				}
				else {
					interfaces[i][j][LEFT] = hosts[i][j].createInterface("l");
					interfaces[i][j][RIGHT] = hosts[i][j].createInterface("r");
					createLink(topnet, "l_i"+i+"_j"+j+"_j"+(j-1), interfaces[i][j-1][RIGHT], interfaces[i][j][LEFT]);
				}
			}
		}
		return hosts;
	}
	
	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final int P = parameters.get("P").asInt();
		final int W = parameters.get("W").asInt();
		final boolean do_transit=parameters.get("DO_TRANSIT").asBoolean();

		Net topnet = exp.createTopNet("topnet");
		topnet.createShortestPath();
		
		List<IHost[][]> nets = new ArrayList<IHost[][]>();
		INet curnet=topnet;
		nets.add(buildsphere(topnet,false,true,P,W,do_transit));
		for(int i=0;i<P;i++) {
			IHost[][] prev = nets.get(nets.size()-1);
			INet n = curnet.createNet("net"+i);
			n.createShortestPath();
			nets.add(buildsphere(n,true,i+1<P,P,W,do_transit));
			IHost[][] cur = nets.get(nets.size()-1);
			
			for(int j = 0;j<W;j++) {
				createLink(curnet, "down_i"+i+"_j"+j, (IInterface)cur[0][j].getChildByName("t"), (IInterface)prev[2][j].getChildByName("d"));
			}
			if(!do_transit) {
				//they will all be siblings otherwise....
				curnet=n;
			}
		}
		ITraffic traffic =topnet.createTraffic();
		
		final String src="."+nets.get(nets.size()-1)[2][W-1].getUniqueName().toString().substring(topnet.getName().toString().length());
		int t=1;
		IHost[][] dsts=nets.get(0);
		for(int i=0;i<3;i++) {
			for(int j=0;j<W;j++) {
				final String dst="."+dsts[i][j].getUniqueName().toString().substring(topnet.getName().toString().length());
				System.out.println("\t\tdst="+dst);
				IPingTraffic ping = traffic.createPingTraffic();
				ping.setCount("100");
				ping.setStartTime(t);
				ping.setInterval(".5");
				ping.setSrcs("{"+src+"}");
				ping.setDsts("{"+dst+"}");
				ping.setIntervalExponential("false");
				ping.setMapping("one2one");
				t+=50;
			}
		}
		System.out.println("*************************************");
		System.out.println("Last ping is scheduled for "+t);
		System.out.println("*************************************");
		return topnet;
	}	
}
