import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import jprime.Experiment;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.Net.Net;
import jprime.Router.IRouter;
import jprime.SwingTCPTraffic.ISwingTCPTraffic;
import jprime.TCPMaster.ITCPMaster;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.util.ModelInterface;


public class SwingTest extends ModelInterface{
	private static final ArrayList<ModelParam> getParams() {
		ArrayList<ModelParam> rv = new ArrayList<ModelInterface.ModelParam>();
		rv.add(new ModelParam(ModelParamType.FILE, "clientFile","CLIENT_FILE","CLIENT_FILE", ""));
		rv.add(new ModelParam(ModelParamType.FILE, "serverFile","SERVER_FILE","SERVER_FILE", ""));
		return rv;
	}
	static final String CLIENT = "c";
	static final String SERVER = "s";
	static final String INTERFACE = "if";
	/**
	 * @param db
	 * @param exp
	 */
	public SwingTest(Database db, Experiment exp) 
	{
		super(db, exp, getParams());
	}

	/**
	 * @param db
	 * @param expName
	 */
	public SwingTest(Database db, String expName) 
	{
		super(db, expName, getParams());
	}

	/* (non-Javadoc)
	 * @see jprime.ModelInterface#buildModel()
	 */

	@Override
	public INet buildModel(Map<String, ModelParamValue> parameters)
	{
		final String clientFile = parameters.get("CLIENT_FILE").asString();
		if(clientFile.length()==0) {
			System.err.print("You must use -DCLIENT_FILE=<file> when calling the driver!");
			System.exit(100);
		}

		final String serverFile = parameters.get("SERVER_FILE").asString();
		if(serverFile.length()==0) {
			System.err.print("You must use -DSERVER_FILE=<file> when calling the driver!");
			System.exit(100);
		}

		Net top = exp.createTopNet("topnet");
		top.createShortestPath();
		//top.createAlgorithmicRouting();

		INet net0 = top.createNet("net0"); 
		INet net1 = top.createNet("net1"); 

		//create two routers connect with each other
		IRouter r0 = net0.createRouter("router0");
		IInterface r0_r1= r0.createInterface("if_r0");

		IRouter r1 = net1.createRouter("router1");
		IInterface r1_r0= r1.createInterface("if_r1");

		ILink l_r0_r1= top.createLink("r0_r1");
		l_r0_r1.createInterface(r0_r1);
		l_r0_r1.createInterface(r1_r0);
		//XXX, set delay and bitrate here ?


		try {
			File client = new File(clientFile);
			if (!client.exists()){
				System.out.println("client file not exist\n");
			}
			FileInputStream fsClient= new FileInputStream(clientFile);
			BufferedReader brClient = new BufferedReader(new InputStreamReader(fsClient));

			File server = new File(serverFile);
			if (!server.exists()){
				System.out.println("server file not exist\n");
			}
			FileInputStream fsServer= new FileInputStream(serverFile);
			BufferedReader brServer = new BufferedReader(new InputStreamReader(fsServer));

			String data;	
			String [] strarray = null;	
			String hostid, routerid, delay, bandwidth, mss;

			//List<String> nodelist = new ArrayList<String>();

			IHost host;
			IInterface interface1, interface2;
			ILink link;

			while((data = brClient.readLine()) != null) {
				data.trim();
				if(data.length()==0) continue;
				strarray = data.split(" ");
				//System.out.println("1strarry.length="+strarray.length);
				//read information (each link connects a host and a router)
				hostid = strarray[0];
				routerid = "0";
				delay = String.valueOf(Float.valueOf(strarray[1]).floatValue()/1000);
				bandwidth = String.valueOf(Float.valueOf(strarray[2]).floatValue()*1000000);
				mss = strarray[4];
				System.out.println("delay="+delay+", bandwidth="+bandwidth+", MSS="+mss);

				//create host
				host = net0.createHost(CLIENT + hostid);
				interface1 = host.createInterface("if0");

				ITCPMaster tcp1 = host.createTCPMaster();
				tcp1.setMss(mss);

				interface2 = r0.createInterface(INTERFACE + "_" + CLIENT + hostid);

				link = top.createLink("r" + routerid + "_" + CLIENT + hostid);
				link.createInterface(interface1);
				link.createInterface(interface2);

				//set delay and bandwidth
				link.setDelay(delay);
				System.out.println(link.getUniqueName()+".delay="+link.getDelay().toString());
				link.setBandwidth(bandwidth);     
			}

			while((data = brServer.readLine()) != null) {
				data.trim();
				if(data.length()==0) continue;
				strarray = data.split(" ");

				//read information (each link connects a host and a router)
				hostid = strarray[0];
				routerid = "1";
				delay = String.valueOf(Float.valueOf(strarray[1]).floatValue()/1000);
				bandwidth = String.valueOf(Float.valueOf(strarray[2]).floatValue()*1000000);
				mss = strarray[4];
				System.out.println("delay="+delay+", bandwidth="+bandwidth+", MSS="+mss);

				//create host
				host = net1.createHost(SERVER + hostid);
				interface1 = host.createInterface("if0");
				host.createSwingServer();

				ITCPMaster tcp2 = host.createTCPMaster();
				tcp2.setMss(mss);

				interface2 = r1.createInterface(INTERFACE + "_" + SERVER + hostid);

				link = top.createLink("r" + routerid + "_" + SERVER + hostid);
				link.createInterface(interface1);
				link.createInterface(interface2);

				//set delay and bandwidth
				link.setDelay(delay);
				System.out.println(link.getUniqueName()+".delay="+link.getDelay().toString());
				link.setBandwidth(bandwidth);     
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		ITraffic t = top.createTraffic();
		//setup swing traffic
		ISwingTCPTraffic s=t.createSwingTCPTraffic();
		s.setSrcs(".:net0");
		s.setDsts(".:net1");
		//s.setTraceDescription("/labspace/kinsman/tingli/workspace/swingdata/traffic_summary");
		s.setTraceDescription("/Users/tingli/Documents/workspace/swingdata/traffic_summary");

		return top;
	}

}