import java.util.ArrayList;
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
public class TestNixVector extends ModelInterface{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.INT, "N","N","N", "10"));
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
	public TestNixVector(Database db, Experiment exp) {
		super(db,exp,getParams());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public TestNixVector(Database db, String expName) {
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
	
	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */
	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters) {
		final int N = parameters.get("N").asInt();
		Net topnet = exp.createTopNet("topnet");
		topnet.createShortestPath();
		
		IHost[][] hosts = new IHost[3][N];
		IInterface[][][] interfaces = new IInterface[3][N][4];
		for(int i=0;i<3;i++) {
			for(int j=0;j<N;j++) {
				hosts[i][j] = topnet.createHost(i+"_"+j);
				switch(i) {
				case 0:
					interfaces[i][j][DOWN] = hosts[i][j].createInterface("d");
					break;
				case 1:
					interfaces[i][j][TOP] = hosts[i][j].createInterface("t");
					interfaces[i][j][DOWN] = hosts[i][j].createInterface("d");
					createLink(topnet, "l_i"+(i-1)+"_i"+i+"_j"+j, interfaces[i-1][j][DOWN], interfaces[i][j][TOP]);
					break;
				case 2:
					interfaces[i][j][TOP] = hosts[i][j].createInterface("t");
					createLink(topnet, "l_i"+(i-1)+"_i"+i+"_j"+j, interfaces[i-1][j][DOWN], interfaces[i][j][TOP]);
					break;
				}
				if(j==0) {
					interfaces[i][j][RIGHT] = hosts[i][j].createInterface("r");
				}
				else if (j==N-1) {
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
		ITraffic traffic =topnet.createTraffic();
		final String src="."+hosts[0][0].getUniqueName().toString().substring(topnet.getName().toString().length());
		int t=1;
		for(int i=0;i<3;i++) {
			for(int j=0;j<N;j++) {
				if(i==0 && j==0) continue;
				final String dst="."+hosts[i][j].getUniqueName().toString().substring(topnet.getName().toString().length());
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
