package monitor.core;

/*
 * Copyright (c) 2011 Florida International University.
 *
 * Permission is hereby granted, free of charge, to any individual or
 * institution obtaining a copy of this software and associated
 * documentation files (the "software"), to use, copy, modify, and
 * distribute without restriction.
 *
 * The software is provided "as is", without warranty of any kind,
 * express or implied, including but not limited to the warranties of
 * merchantability, fitness for a particular purpose and
 * non-infringement.  In no event shall Florida International
 * University be liable for any claim, damages or other liability,
 * whether in an action of contract, tort or otherwise, arising from,
 * out of or in connection with the software or the use or other
 * dealings in the software.
 *
 * This software is developed and maintained by
 *
 *   Modeling and Networking Systems Research Group
 *   School of Computing and Information Sciences
 *   Florida International University
 *   Miami, Florida 33199, USA
 *
 * You can find our research at http://www.primessf.net/.
 */

import java.io.StringReader;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import monitor.commands.AbstractCmd;
import monitor.commands.CodecFactory;
import monitor.commands.UpdatePhysicalSystemCmd;
import monitor.core.SymbioSimulationPath.LinkStaticProperties;
import monitor.util.Utils;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Miguel Erazol Erazo
 *
 * This class allows the simulator system to collect updates,
 * process them and send model updates to the physical system
 */
public class SimEDSS extends SymbioEDSS implements SymbioEDSSIntf {
	private final SimEDSSSessionHandler handler;
	//latest results from the simulator
	private Hashtable<String, ArrayList<Double>> history;
	//info about the paths
	private ArrayList<SymbioSimulationPath> paths;
	//XXX useless for the final implementation
	private final String dummyNetIP;
	//XXX here we suppose only one bottleneck shared by all sessions
	private int currentCwnd;
	
	//XXX Later on we will convey info regarding the actual Emulab configuration
	// using possibly a file
	SimEDSS(String master_ip, String dummyNet_ip){
		super(master_ip);
		collector = new SimEDSSCollector(this);
		handler = new SimEDSSSessionHandler(this);
		connector = null;
		dummyNetIP = dummyNet_ip;
		//XXX '20' value has to be changed later to a generic value passed from the process
		//that will orchestrate the instantiation of an experiment ans has to reflect the actual
		//expected size of the data structure, check the java documentation for this
		history = new Hashtable<String, ArrayList<Double>>(20);
		currentCwnd = 0;
	}

	public void getDataFromSystems(String update, String source) {
		//int bytes_from_app = Integer.parseInt(update);
		//String ip = source;
		if(update.contains("<uid>4<") || update.contains("<uid>37<")) {
			//System.out.println("\tReceived: " + update + " from IP: " + ip);
		}
		
		//Update History table with the new updates got from the simulation
		//XXX we are not using the source parameter since we are now dealing with simulation from one
		// machine only
		updateDataFromSimulation("<set>" + update + "</set>");
		
		//XXX Now we apply the algorithm to compute the AB, naive algorithm for now
		for(SymbioSimulationPath p : paths){
//			System.out.println("");
//			System.out.println("\n\na new path is being traversed");
			double min_AB = 0;
			double path_rtt = 0;
			double path_ploss = 0;//XXX remember to implement morgan here
			boolean min_AB_set = false;
			Collection<Integer> values = p.getUidTxratePropDelay().keySet();
			Iterator<Integer> iter = values.iterator();
			HashMap<Integer, LinkStaticProperties> links = p.getUidTxratePropDelay();
			while(iter.hasNext()){
				Integer uid = (Integer)iter.next();
				LinkStaticProperties link = links.get(uid);
				Double tx_rate = link.getTxRate();
				Double prop_delay = link.getPropdelay();
				ArrayList<Double> time_utilization = (history.get(uid.intValue() + ",utilization"));
				//XXX double time = time_value.get(0).doubleValue(); //XXX this is not used now
				double utilization_value = time_utilization.get(1).doubleValue();
				double available_bandwidth = (1 - utilization_value) * tx_rate;
	//			System.out.println("\tuid=" + uid + " tx_rate=" + tx_rate + " utilization_value="+
		//				utilization_value + " available_bandwidth=" + available_bandwidth + " prop_delay=" +
			//			prop_delay);
				path_rtt += prop_delay;
					
				ArrayList<Double> time_delay = (history.get(uid.intValue() + "," + "delay"));
				//double delay_value = time_delay.get(1).doubleValue();
				//System.out.println("\t\tuid=" + uid.intValue() + " delay=" + time_delay.get(1).doubleValue());
				
				path_rtt += time_delay.get(1).doubleValue();
				
				ArrayList<Double> time_ploss = (history.get(uid.intValue() + "," + "ploss"));
				//double ploss_value = time_ploss.get(1).doubleValue();
				//System.out.println("\t\tuid=" + uid.intValue() + " ploss=" + time_ploss.get(1).doubleValue());
				
				/*System.out.println("---check this---util=" + utilization + " delay=" + delay + " ploss=" + ploss +
    					" uid=" + uid + " delayhist=" + history.get(uid + "," + "delay").get(1) +
    					" plosshist=" + history.get(uid + "," + "loss").get(1));*/
				
				System.out.println("\t ploss_from_val=" + time_ploss.get(1).doubleValue());
				path_ploss += time_ploss.get(1).doubleValue();
					
				if(min_AB_set == false)
					min_AB = available_bandwidth;
				else {
					if(available_bandwidth < min_AB)
						min_AB = available_bandwidth;
				}
			}
			//Compute the long-term stable TCP throughput achievable
			//XXX we are not taking into account the slow start for now, only Padhye's formula
			
			double tcp_throughput = ((1448.0*8)/path_rtt)*Math.sqrt(0.75/(path_ploss));
			
			System.out.println("***available band for path" + p.getPathIndex() + "=" + min_AB + 
					" RTT_path=" + path_rtt + " ploss_path=" +  path_ploss + " tcp_throughput=" + 
					tcp_throughput + " Mbps" + " currentCwnd=" + currentCwnd + 
					" bw_utilized=" + (currentCwnd*1448*8)/path_rtt + "\n");
			
			//XXX send the command without any filtering
			//XXX index, throughput, rtt(mss)
			//sendUpdateToActuator(p.getPathIndex(), (int)tcp_throughput, (int)(path_rtt*1000)); //XXX passing the rtt in ms
			//XXX THIS IS STATIC!!!!!! FIX !!!!!!
			if(min_AB < 9500000){
				//this this is the congested link of a dumbbell topology
				sendUpdateToActuator(p.getPathIndex(), 10000000 - currentCwnd, (int)(path_rtt*1000));//in ms
			} else {
				sendUpdateToActuator(p.getPathIndex(), 10000000, (int)(path_rtt*1000));//in ms
			}
		}
		
		//Create an updateFromBytesCmd
		//XXX we will not use here the IP reported from the app, instead we will find out the private IP
		//	assigned in Emulab assuming only one application lives in this physical machine...will we use
		//	virtual machines in the future in the physical system?
/*		String emulab_private_ip_interface = null;
		
		for(String phy_ip: this.getInterfacesIPs()){
			if(phy_ip.split("\\.")[0].equals("10")){
				System.out.println("\tEmulab's interface:" + phy_ip);
				emulab_private_ip_interface = phy_ip;
			}
		}
		
		if(emulab_private_ip_interface == null)
			throw new RuntimeException("Could not get host's interface ip");
		
		UpdateBytesFromAppCmd update_cmd = new UpdateBytesFromAppCmd(bytes_from_app, emulab_private_ip_interface);
		
		//Send the new command to master using the mastersession
		sendCommand(update_cmd);*/
	}

	private void sendUpdateToActuator(int path_index, int min_AB, int tcp_throughput){
		UpdatePhysicalSystemCmd cmd = new UpdatePhysicalSystemCmd(path_index, min_AB, tcp_throughput);
		System.out.println("######### SENDING UPDATE ACTUATOR CMD ##########");
		masterSession.write(cmd);
	}
	
	public boolean start() throws InterruptedException {
		//Fill tables that will represent the paths and the interfaces (UIDs)
		// that compose them
		InitializeDataStructures();
		//Connect to master
		connect();
		//Start the collector
		collector.start();
		//Start the sender
		//XXX activate this later:		sender.start();
		return false;
	}

	private synchronized boolean updateDataFromSimulation(String update){
		//System.out.println("\n\n****** in updateDataFromSimulation, update=" + update + "\n");
		//Parse this xml to update the history table
		try {
    		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder doc_builder = docBuilderFactory.newDocumentBuilder();
    		InputSource input_source = new InputSource();
    		input_source.setCharacterStream(new StringReader(update));
    		
    		Document doc = doc_builder.parse(input_source);
    		NodeList list_of_updates = doc.getElementsByTagName("up");
    		int total_updates = list_of_updates.getLength();
    		//System.out.println("Total no of updates in this set: " + total_updates);
    		
    		for(int i=0; i<total_updates; i++){
    			//System.out.println("update " + i + " of the set");
    			Element element = (Element)list_of_updates.item(i);
    			NodeList list = (NodeList)element.getElementsByTagName("time");
    			Element elem = (Element)list.item(0);
    			//System.out.println("\ttime value=" + getCharacterDataFromElement(elem));
    			double time = Double.parseDouble(getCharacterDataFromElement(elem));
    			
    			list = element.getElementsByTagName("uid");
    			elem = (Element)list.item(0);
    			//System.out.println("\tuid value=" + getCharacterDataFromElement(elem));
    			int uid = Integer.parseInt(getCharacterDataFromElement(elem));
    			
    /*			list = element.getElementsByTagName("utilization");
    			elem = (Element)list.item(0);
    			//System.out.println("\tname value=" + getCharacterDataFromElement(elem));
    			String attr_name = getCharacterDataFromElement(elem);						*/
    			
    			list = element.getElementsByTagName("utilization");
    			elem = (Element)list.item(0);
    			//System.out.println("\t value=" + getCharacterDataFromElement(elem));
    			double utilization = Double.parseDouble(getCharacterDataFromElement(elem));
    			
    			list = element.getElementsByTagName("delay");
    			elem = (Element)list.item(0);
    			//System.out.println("\t value=" + getCharacterDataFromElement(elem));
    			double delay = Double.parseDouble(getCharacterDataFromElement(elem));
    			
    			list = element.getElementsByTagName("ploss");
    			elem = (Element)list.item(0);
    			//System.out.println("\t value=" + getCharacterDataFromElement(elem));
    			double ploss = Double.parseDouble(getCharacterDataFromElement(elem));
    			
    			list = element.getElementsByTagName("cwnd");
    			elem = (Element)list.item(0);
    			currentCwnd = Integer.parseInt(getCharacterDataFromElement(elem));
    			System.out.println("\t currentCwnd=" + currentCwnd);
    			
    			
    			//Now that we have the individual fields we update the history hash table
    			ArrayList<Double> time_utilization = new ArrayList<Double>();
    			time_utilization.add(0, new Double(time));
    			time_utilization.add(1, new Double(utilization));
    			history.put(uid + "," + "utilization", time_utilization);
    			
    			ArrayList<Double> time_delay = new ArrayList<Double>();
    			time_delay.add(0, new Double(time));
    			time_delay.add(1, new Double(delay));
    			history.put(uid + "," + "delay", time_delay);
    			
    			ArrayList<Double> time_ploss = new ArrayList<Double>();
    			time_ploss.add(0, new Double(time));
    			time_ploss.add(1, new Double(ploss));
    			history.put(uid + "," + "ploss", time_ploss);
    			
//    			System.out.println("---check this---util=" + utilization + " delay=" + delay + " ploss=" + ploss +
  //  					" uid=" + uid + " delayhist=" + history.get(uid + "," + "delay").get(1) +
    //					" plosshist=" + history.get(uid + "," + "loss").get(1));
    			   
    			//System.out.println("history updated with: attr=" + uid + "," + attr_name +  
    				//	" time=" + history.get(uid + "," + attr_name).get(0)
    				//	+ " value=" + history.get(uid + "," + attr_name).get(1));
    			//System.out.println("history updated with: attr=" + uid + "," + attr_name +  
    				//	" time=" + history.get(uid + "," + attr_name).get(0)
    				//	+ " value=" + history.get(uid + "," + attr_name).get(1));
    		}
    	} catch (SAXParseException err) {
    		//listener.println ("** Parsing error" + ", line "
			//	+ err.getLineNumber () + ", uri " + err.getSystemId ());
			//listener.println(" " + err.getMessage ());
			return false;
		} catch (SAXException e) {
			//Exception x = e.getException ();
	        //((x == null) ? e : x).printStackTrace ();
			return false;
		} catch (Throwable t) {
	        //t.printStackTrace ();
	        return false;
		}
		return false;
	}
	
	public static String getCharacterDataFromElement(Element e) {
	   Node child = e.getFirstChild();
	   if (child instanceof CharacterData) {
	     CharacterData cd = (CharacterData) child;
	       return cd.getData();
	     }
	   return "?";
	 }
	
	//XXX this method has to be changed completely
	private void InitializeDataStructures() {
		//XXX this has to be donde automatically later
		// For now for a dumbbell topology we have two routes
		ArrayList<Double> al_util_1 = new ArrayList<Double>();
		al_util_1.add(0, new Double(0)); //time
		al_util_1.add(1, new Double(0)); //utilization
		history.put("70,utilization", al_util_1);
		ArrayList<Double> al_delay_1 = new ArrayList<Double>();
		al_delay_1.add(0, new Double(0)); //time
		al_delay_1.add(1, new Double(0)); //value
		history.put("70,delay", al_delay_1);
		ArrayList<Double> al_ploss_1 = new ArrayList<Double>();
		al_ploss_1.add(0, new Double(0)); //time
		al_ploss_1.add(1, new Double(0)); //value
		history.put("70,ploss", al_ploss_1);
	
		ArrayList<Double> al_util_2 = new ArrayList<Double>();
		al_util_2.add(0, new Double(0));
		al_util_2.add(1, new Double(0));
		history.put("6,utilization", al_util_2);
		ArrayList<Double> al_delay_2 = new ArrayList<Double>();
		al_delay_2.add(0, new Double(0)); //time
		al_delay_2.add(1, new Double(0)); //value
		history.put("6,delay", al_delay_2);
		ArrayList<Double> al_ploss_2 = new ArrayList<Double>();
		al_ploss_2.add(0, new Double(0)); //time
		al_ploss_2.add(1, new Double(0)); //value
		history.put("6,ploss", al_ploss_2);
		
		ArrayList<Double> al_util_3 = new ArrayList<Double>();
		al_util_3.add(0, new Double(0));
		al_util_3.add(1, new Double(0));
		history.put("4,utilization", al_util_3);
		ArrayList<Double> al_delay_3 = new ArrayList<Double>();
		al_delay_3.add(0, new Double(0)); //time
		al_delay_3.add(1, new Double(0)); //value
		history.put("4,delay", al_delay_3);
		ArrayList<Double> al_ploss_3 = new ArrayList<Double>();
		al_ploss_3.add(0, new Double(0)); //time
		al_ploss_3.add(1, new Double(0)); //value
		history.put("4,ploss", al_ploss_3);
		
		ArrayList<Double> al_util_4 = new ArrayList<Double>();
		al_util_4.add(0, new Double(0));
		al_util_4.add(1, new Double(0));
		history.put("37,utilization", al_util_4);
		ArrayList<Double> al_delay_4 = new ArrayList<Double>();
		al_delay_4.add(0, new Double(0)); //time
		al_delay_4.add(1, new Double(0)); //value
		history.put("37,delay", al_delay_4);
		ArrayList<Double> al_ploss_4 = new ArrayList<Double>();
		al_ploss_4.add(0, new Double(0)); //time
		al_ploss_4.add(1, new Double(0)); //value
		history.put("37,ploss", al_ploss_4);
		
		ArrayList<Double> al_util_5 = new ArrayList<Double>();
		al_util_5.add(0, new Double(0));
		al_util_5.add(1, new Double(0));
		history.put("39,utilization", al_util_5);
		ArrayList<Double> al_delay_5 = new ArrayList<Double>();
		al_delay_5.add(0, new Double(0)); //time
		al_delay_5.add(1, new Double(0)); //value
		history.put("39,delay", al_delay_5);
		ArrayList<Double> al_ploss_5 = new ArrayList<Double>();
		al_ploss_5.add(0, new Double(0)); //time
		al_ploss_5.add(1, new Double(0)); //value
		history.put("39,ploss", al_ploss_5);
		
		ArrayList<Double> al_util_6 = new ArrayList<Double>();
		al_util_6.add(0, new Double(0));
		al_util_6.add(1, new Double(0));
		history.put("185,utilization", al_util_6);
		ArrayList<Double> al_delay_6 = new ArrayList<Double>();
		al_delay_6.add(0, new Double(0)); //time
		al_delay_6.add(1, new Double(0)); //value
		history.put("185,delay", al_delay_6);
		ArrayList<Double> al_ploss_6 = new ArrayList<Double>();
		al_ploss_6.add(0, new Double(0)); //time
		al_ploss_6.add(1, new Double(0)); //value
		history.put("185,ploss", al_ploss_6);
			
		//System.out.println("39=" + history.get("39,traffic_intensity"));
		//history.put("39,traffic_intensity", new Double(11));
		//System.out.println("***39=" + history.get("39,traffic_intensity"));
	
		//XXX This has to be done automatically and dynamically
		paths = new ArrayList<SymbioSimulationPath>();
		//XXX add one path for the dumbbell model
		SymbioSimulationPath dumbbell_client_to_server = new SymbioSimulationPath(1, dummyNetIP);
		dumbbell_client_to_server.addLink(70, 1000000000, 0);
		dumbbell_client_to_server.addLink(4,  10000000, 0.064);
		dumbbell_client_to_server.addLink(39, 1000000000, 0);
		paths.add(dumbbell_client_to_server);
		SymbioSimulationPath dumbbell_server_to_client = new SymbioSimulationPath(2, dummyNetIP);
		dumbbell_server_to_client.addLink(185,1000000000, 0);
		dumbbell_server_to_client.addLink(37, 10000000, 0.064);
		dumbbell_server_to_client.addLink(6,  1000000000, 0);
		paths.add(dumbbell_server_to_client);
	}

	public boolean sendCommand(AbstractCmd cmd) {
		cmd.setSerialNumber(Utils.nextSerial());
		//XXX Insert this command to outstandingBlockingCommands structure? (check the 'controller' code)
		masterSession.write(cmd);
		return true;
	}
	
	public boolean connect() {
		//Connect to master and return the result of the attempt
		if(this.masterSession == null) {
			//Verify that masterIP is not null or empty
			if(masterIP == null || masterIP.length() == 0) {
				throw new RuntimeException("Invalid master ip!");
			}
			// Socket for sending to other nodes
			connector = new NioSocketConnector();
			
			// Configure the service
			connector.getFilterChain().addLast(
					"codec", new ProtocolCodecFilter(new CodecFactory()));
			
			// Set handler for the connector socket
			connector.setHandler(this.handler);
			
			// Connect to master
			System.out.println("Connecting to master at " + masterIP + ":" + Utils.MASTER_PORT);
			ConnectFuture future = connector.connect(new InetSocketAddress(
					masterIP, Utils.MASTER_PORT));
			future.awaitUninterruptibly();
			this.masterSession = future.getSession();
			System.out.println("SimEDSS Connected to master! connected=" + this.masterSession.isConnected());
		} else {
			throw new RuntimeException("Already connected to master!");
		}
		
		// Socket for sending to other nodes
		this.connector = new NioSocketConnector();
		
		return true;	
	}
	
	public static void main(String[] args)
	{
		System.out.println("master IP:" + args[0]);
		System.out.flush();
		String master_ip = args[0];
		String dummynet_ip = args[1];
		SimEDSS sim_edss = new SimEDSS(master_ip, dummynet_ip);
		try {
			sim_edss.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not start services in SimEDSS\n!");
		}
		return;
	}
}
