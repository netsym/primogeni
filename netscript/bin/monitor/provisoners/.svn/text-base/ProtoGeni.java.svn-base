package monitor.provisoners;

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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import jprime.Host.IHost;
import jprime.partitioning.Partitioning;
import jprime.util.ComputeNode;
import jprime.util.Portal;
import monitor.commands.StateExchangeCmd;
import monitor.core.IController;
import monitor.core.IExpListenter;
import monitor.core.Provisioner;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Miguel Erazo
 *
 */
public class ProtoGeni extends Provisioner {
	private final String sliceName;
	private final String passphrase;
	private final String privateKey;
	private final int numNodes;
	private final String primoPath;
	private static String statPrimoPath;
	private final IExpListenter listener;
	private static IExpListenter statListener;
	private List<ComputeNode> computeNodes;
	private boolean haveSlice;
	private ProtoGENICaller caller;
	
	private static final int MAX_POLLING_ATTEMPS = 150;
	private static final int POLLING_PERIOD = 5;
	
	public ProtoGeni(IExpListenter listener, String primoPath, String expname, String slicename, String passphrase, String privateKey, int numNodes) throws IOException 
	{
		super(numNodes);
		this.primoPath= primoPath;
		statPrimoPath = primoPath;
		this.listener = listener;
		statListener  = listener;
		
		//Calendar cal = Calendar.getInstance();
		this.sliceName = slicename;
		
		this.passphrase = passphrase;
		this.privateKey = privateKey;
		this.numNodes = numNodes;
		listener.println("--------expname=" + expname+", slicename=" + slicename + " passphrase=" + this.passphrase + " privatekey=" +
				this.privateKey + " primoPath=" + this.primoPath + " home=" + System.getProperty("user.home"));
		this.computeNodes = new ArrayList<ComputeNode>();
		this.haveSlice=false;
		this.caller = new ProtoGENICaller(listener,passphrase, privateKey, primoPath, slicename);
	}
	
	public static boolean deleteSlice(String slice_name, String passphrase, String certificate) throws IOException
	{
		System.out.println("****XXX Should delete the slice now!");
		if(slice_name==null) {
			throw new RuntimeException("slice name was null");
		}
		if(passphrase==null) {
			throw new RuntimeException("passphrase was null");
		}
		if(certificate==null) {
			throw new RuntimeException("certificate was null");
		}
		
		ProtoGENICaller.deleteSlice(slice_name, passphrase, certificate, statPrimoPath, statListener);
		return true;
	}
	
	public void allocate()
	{
		if(!haveSlice) {
			String rspec = "";
			String output = "";
	
			//Get credential
			try {
				output = caller.getCredential();
				if(!output.equals("")){
					if(this.abortInstantiation(output)){
						computeNodes = null;
						return;
					} else {
						//let's try again...
						System.out.println("\tXMLRPC server is busy, let's try again...");
						listener.println("\tXMLRPC server is busy, let's try again...");
						Thread.sleep(1000);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Register slice
			try {
				while(true) {
					output = caller.registerSlice();
					if(!output.equals("")){
						//There was a problem...
						if(this.abortInstantiation(output)){
							computeNodes = null;
							return;
						} else {
							//let's try again...
							System.out.println("\tXMLRPC server is busy, let's try again...");
							listener.println("\tXMLRPC server is busy, let's try again...");
							Thread.sleep(1000);
						}
					} else {
						//No problem, let's continue
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Generate an rspec and get the ticket
			String request_rspec = "";
	        RSpec new_rspec = new RSpec(numNodes, caller.getXMLRPCServerDomain());
	        request_rspec = new_rspec.GenerateRSpec();
	        listener.println("\n------RSPEC-------\n" + request_rspec + "\n----------\n");
	        writeToFile(primoPath + sliceName + ".rspec", request_rspec);
			try {
				while(true) {
					output = caller.getTicket(primoPath + sliceName + ".rspec");
					if(!output.equals("")){
						//There was a problem...
						if(this.abortInstantiation(output)){
							computeNodes = null;
							return;
						} else {
							//let's try again...
							System.out.println("\tXMLRPC server is busy, let's try again...");
							listener.println("\tXMLRPC server is busy, let's try again...");
							Thread.sleep(1000);
						}
					} else {
						//No problem, let's continue
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Redeem Ticket
			StringBuilder manifest = new StringBuilder();
			try {
				while(true) {
					output = caller.redeemTicket(manifest);
					if(!output.equals("")){
						//There was a problem...
						if(this.abortInstantiation(output)){
							computeNodes = null;
							return;
						} else {
							//let's try again...
							System.out.println("\tXMLRPC server is busy, let's try again...");
							listener.println("\tXMLRPC server is busy, let's try again...");
							Thread.sleep(1000);
						}
					} else {
						//No problem, let's continue
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			listener.println("MANIFEST:\n" + manifest.toString() + "\n");
			System.out.println("MANIFEST:\n" + manifest.toString() + "\n");
			//Get the rspec from the manifest
			listener.println("RSPEC=" + manifest.substring(manifest.indexOf("<rspec"), manifest.length() -2) + "---");
			System.out.println("RSPEC=" + manifest.substring(manifest.indexOf("<rspec"), manifest.length() -2) + "---");
			rspec = manifest.substring(manifest.indexOf("<rspec"), manifest.length() -2);
			writeToFile(primoPath + sliceName + "_rspec.xml", rspec);
			//Get the IPs from the rspec
			String[][] manifest_data_container = new String[this.numNodes][4];
        	boolean parsed = parseXML(primoPath + sliceName + "_rspec.xml", manifest_data_container);
        	System.out.println("We parsed the following data:");
        	for(int i=0; i<this.numNodes; i++){
        		System.out.println("\t---virt_id=" + manifest_data_container[i][0] + " hostname=" + 
        				manifest_data_container[i][1] + " control_ip=" + manifest_data_container[i][2] +
						" data_ip=" +  manifest_data_container[i][3]);
        	}
        	if(parsed==false){
        		throw new RuntimeException("Could not parse the manifest returned by Emulab:\n");
        	}
        	
        	for(int i=0; i<this.numNodes; i++){
        		//virtual id, hostname, control_ip, data_ip
        		display("GOT ip=" + manifest_data_container[i][2] + " pcNames=" + manifest_data_container[i][3]);
        		System.out.println("before adding compute nodes, server domain:" + caller.getXMLRPCServerDomain());
        		if(!caller.getXMLRPCServerDomain().equals("cis.fiu.edu")){
        			this.computeNodes.add(new ComputeNode(manifest_data_container[i][2], manifest_data_container[i][3], new ArrayList<Portal>()));
        		} else {
        			this.computeNodes.add(new ComputeNode(manifest_data_container[i][2], manifest_data_container[i][2], new ArrayList<Portal>()));
        		}
        	}
			
        	try {
        		while(true) {
					output = caller.StartSliver();
					if(!output.equals("")){
						//There was a problem...
						if(this.abortInstantiation(output)){
							computeNodes = null;
							return;
						} else {
							//let's try again...
							System.out.println("\tXMLRPC server is busy, let's try again...");
							listener.println("\tXMLRPC server is busy, let's try again...");
							Thread.sleep(1000);
						}
					} else {
						//No problem, let's continue
						break;
					}
        		}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		
			pollSlivers();
			haveSlice = true;
			System.out.println("\n---> Successful slice allocation"); System.err.flush();
			listener.println("\n---> Successful slice allocation"); System.err.flush();
		} else {
			if(computeNodes.size() >= this.numNodes) {
				return;
			} else {
				throw new RuntimeException("Already have the slice!");
			}
		}
	}
	
	public void pollSlivers() {
    	//After a period of time elapses, poll Emulab for status and configure those machines which are up
    	int attempts_ = 1;
    	String status_output = "";
    	//Poll untill all machines have booted and registered successfully               
    	display("---> Polling for status of sliver...");
           
    	while(true) {
    		if(attempts_ > MAX_POLLING_ATTEMPS) {
    			//Let's delete the slice
    			try {
					Runtime.getRuntime().exec("python " + primoPath + "deleteslice.py -n " + this.sliceName +
					        " -p " + primoPath);
				} catch (IOException e) {
					e.printStackTrace();
				}
				int minutes = MAX_POLLING_ATTEMPS / 60;
    			display("waited for " + minutes + " minutes and the sliver is not UP. Deleting slice" +
    					"....please try again");
    			computeNodes = null;
    			return;
    		}
    		
    		try{                           
    			//listener.println("date=" + (new java.util.Date()).toString());
    			Thread.sleep(POLLING_PERIOD * 1000);
    		}
    		catch(Exception e){
    			listener.println(e.toString());
    		}
           
    		try {
				status_output = caller.pollStatus();
				listener.println("\tStatus of slice: " + status_output);
				System.out.println("\tStatus of slice:" + status_output);System.err.flush();
			} catch (IOException e) {
				//e.printStackTrace();
				if(status_output.indexOf("is busy") > 0) {
					listener.println("ups...XML server is busy, trying again...");
					System.out.println("ups...XML server is busy, trying again...");
				} else {
					listener.println("ups...unknown error occured polling the status of the slice, trying again...");
					System.out.println("ups...unknown error occured polling the status of the slice, trying again...");
				}
			}
    		
    		try{
    			//Process p = Runtime.getRuntime().exec(cmd.toString());
    			//String status_report = printProcessOutput(p, 5, null);
    			StringBuilder status = new StringBuilder();
    			StringBuilder state = new StringBuilder();
    			parseStatus(status_output, status, state);
    			//display("    status=" + status.toString() + " state=" +
    				//	state.toString());
    			System.out.println("    status=" + status.toString() + " state=" + state.toString()); System.err.flush();
    			//System.out.println("IPs got from allocation=" + this.ips.toString()); System.err.flush();
    			//if(status.toString().equals("ready") && state.toString().equals("started")){
    			if(status.toString().equals("ready")){	
    				display("---> All machines in the sliver are up");
    				System.out.println("---> All machines in the sliver are up");System.err.flush();
    				break;        				
    			} else if (state.toString().equals("stopped")) {
    				//start the sliver again
    				StringBuilder command4 = new StringBuilder("");

    	        	//command4.append("python "+PATH+"sliveraction.py -n " + this.expName + " start");
    	        	command4.append("python " + primoPath + "sliveraction.py -n " + this.sliceName +
    	        			" -p " + primoPath + " " + " start");
    	        	//Let's try to start the sliver again...	
    		        try{
    	                Process p_ = Runtime.getRuntime().exec(command4.toString());
    	                printProcessOutput(listener,p_, 10, null);
    		        } catch (Exception e) {
    	                System.err.println(e.toString());
    		        }
    			}
    		} catch (Exception e) {
    			listener.println(e.toString());
    		}
    		attempts_++;
    	}
    	//Pause for 30 seconds until Monitor is running on all machines
    	try {
    		display("Waiting for our monitor to start in compute nodes ...");
    		System.out.println("Waiting for our monitor to start in compute nodes ...");System.err.flush();
    		
    		Thread.sleep(150000);
			display("Woke up...");
			System.out.println("Woke up");System.err.flush();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("post sleeping");System.err.flush();
    }
	
	private void parseStatus(String report, StringBuilder status_, StringBuilder state_)
    {
    	int counter = 0;
    	int status_value_start_index=0, status_value_finish_index = 0;
    	int state_value_start_index=0, state_value_finish_index = 0;
    	
    	//Get index of status
    	int status_index = report.indexOf("status"); 
    	boolean start=false;
    	
    	//listener.println("status_index:" + status_index + " report:" + report);
    	
    	for(int i=1; i<30; i++){
    		if(Character.toString(report.charAt(status_index + i)).equals("\'")){
    			//listener.println("status_index:" + i + "counter=" + counter);
    			counter++;
    		}
    		if(counter == 2 && start == false){
    			status_value_start_index = status_index + i + 1;
    			//listener.println("\tstatus_index=" + status_index + " i=" + i + " status_value_start_index:" + status_value_start_index);
    			start = true;
    		} else if(counter == 3){
    			status_value_finish_index = status_index + i - 1;
    			//listener.println("\tstatus_index=" + status_index + " i=" + i + " status_value_finish_index:" + status_value_finish_index);
    			break;
    		}
    	}
    	status_.append(report.substring(status_value_start_index, status_value_finish_index+1));
    	//listener.println("status:" + status_.toString());
    	
    	//Get index of state
    	int state_index = report.indexOf("state");
    	counter = 0; start=false;
    	
    	for(int i=1; i<30; i++){
    		if(Character.toString(report.charAt(state_index + i)).equals("'")){
    			counter++;
    		}
    		if(counter == 2 && start==false){
    			state_value_start_index = state_index + i + 1;
    			start=true;
    		} else if(counter == 3){
    			state_value_finish_index = state_index + i -1;
    			break;
    		}
    	}
    	state_.append(report.substring(state_value_start_index, state_value_finish_index+1));
    	//listener.println("state:" + state_.toString());
    }
	
	public List<ComputeNode> getComputeNodes() {
		if(!haveSlice) {
			allocate();
		}
		this.computeNodes.get(0).setPartitionId(null);
		for(int i =1;i<this.computeNodes.size();i++) {
			this.computeNodes.get(0).setPartitionId(i);
		}
		return computeNodes;
	}
	
	public String toString() {
		return "[ProtoGeni "
		+" privateKey='"+ privateKey
		+"', passphrase='"+ passphrase
		+"', expName='"+sliceName
		+"', primoPath='" +primoPath
		+"' ]";
	}
	
	private void display(String str){
    	listener.println(str);
    }
	
	public static String printProcessOutput(IExpListenter listener, Process p, int offset_, int[] error_)
    {
    	String s = null;
    	StringBuilder output = new StringBuilder("");
    	int lineCounter_ = 0;
    	int startWritting = 0;
           
    	try{
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
            		InputStreamReader(p.getErrorStream()));

            //read the output from the command
            //printLineToConsole("Here is the standard output of the command:\n");

            while ((s = stdInput.readLine()) != null) {
            	if(offset_ == 1000){
            		//This is the manifest
            		if(error_ != null)
            			error_[0] = 0;
            		//printLineToConsole(s);
            		
            		if(s.subSequence(0, 6).equals("<rspec")) {
            			startWritting = 1;
            		}
            		if(startWritting == 1){
            			output.append(s + "\n");
            		}
            		
            	} else {
            		if(error_ != null)
            			error_[0] = 0;
            		//printLineToConsole(s);

            		lineCounter_ ++;
            		//to prevent useless info to be in the manifest
            		if(lineCounter_ > offset_) {
                    	output.append(s + "\n");
            		}
            	}
            }
   
            //read any errors from the attempted command        
            while ((s = stdError.readLine()) != null) {
            	if(error_ != null)
            		error_[0] = 1;

            	if(!s.equals("Error: 1")) {               	
            		//listener.println(s);
            	}

            	output.append(s + "\n");
            }
    	} catch (Exception e){
    		if(listener != null) {
    			listener.println(e.toString());
    		}
    		else {
    			System.out.println(e.toString());
    		}
    	}
    	return output.toString();
    }
	
	public static void writeToFile(String file_name, String content)
    {
    	FileOutputStream out; // declare a file output object
    	PrintStream p; // declare a print stream object
   
    	try {
            // Create a new file output stream
            // connected to "myfile.txt"
            out = new FileOutputStream(file_name);

            // Connect print stream to the output stream
            p = new PrintStream(out);              
            p.println (content);
            out.flush();
            p.close();
    	} catch (Exception e) {
            System.err.println ("Error writing to file");
    	}
    }
	
	public static String readFromFile(String file_to_read){
		File file = new File(file_to_read);
	    FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    DataInputStream dis = null;
	    String out = "";

	    try {
	    	fis = new FileInputStream(file);

	    	// Here BufferedInputStream is added for fast reading.
	    	bis = new BufferedInputStream(fis);
	    	dis = new DataInputStream(bis);

	    	// dis.available() returns 0 if the file does not have more lines.
	    	while (dis.available() != 0) {
	    		// this statement reads the line from the file and print it to
	    		// the console.
	    		@SuppressWarnings("deprecation")
				String line = dis.readLine();
	    		//System.out.println("-------line=" + line);
	    		out += line;
	    	}

	    	// dispose all the resources after using them.
	    	fis.close();
	    	bis.close();
	    	dis.close();
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	    return out;
	}
	
	private boolean parseXML(String doc_name, String[][] data_container)
    {
    	int hostIndex_ = 0;
    	int totalNodes = 0;
    	//java.sql.Statement stm = null;
//    	String[][] data_container = null;//virtual id, hostname, control_ip, data_ip 
           
    	try {
    		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    		Document doc = docBuilder.parse (new File(doc_name));

    		// normalize text representation
    		doc.getDocumentElement().normalize();
    		//display ("Root element of the doc is " +
    			//	doc.getDocumentElement().getNodeName());
       
    		NodeList listOfNodes = doc.getElementsByTagName("node");
    		totalNodes = listOfNodes.getLength();
//    		data_container = new String [totalNodes][4];//virtual id, hostname, control_ip, data_ip
    		display("Total no of nodes : " + totalNodes);
    		
    		for(int s=0; s<listOfNodes.getLength() ; s++){
    			Node firstNode = listOfNodes.item(s);
    			if(firstNode.getNodeType() == Node.ELEMENT_NODE)
    			{
    				Element firstNodeElement = (Element)firstNode;                    
    				//NodeList hostName = firstNodeElement.getElementsByTagName("hostname");
    				//Element firstHostNameElement = (Element)hostName.item(0);
    				//NodeList textFNList = firstHostNameElement.getChildNodes();
    				
    				//Let's find the virtual_id of this node
    				data_container[hostIndex_][0] = firstNodeElement.getAttribute("virtual_id");
    				
//    				pc_names[hostIndex_] = firstNodeElement.getAttribute("hostname");
    				
    				//virtual id, hostname, control_ip, data_ip
    				data_container[hostIndex_][1] = firstNodeElement.getAttribute("hostname");
    				
    				//Split the output                    
    				String delims = "[.]+";
               
    				//pcNames_[hostIndex_] = ((Node)textFNList.item(0)).getNodeValue().trim();
//    				listener.println("    ---> PrimoGENI: pc:" + pc_names[hostIndex_] + "-");
    				listener.println("    ---> PrimoGENI: pc:" + data_container[hostIndex_][1] + "-");
    				System.out.println("    ---> PrimoGENI: pc:" + data_container[hostIndex_][1] + "-");
    				//listener.println("    ---> PrimoGENI: pc" + s + "=" + pcNames_[hostIndex_]);
//    				java.net.InetAddress inetAdd = 
//    					java.net.InetAddress.getByName(pc_names[hostIndex_]);
    				java.net.InetAddress inetAdd = 
    					java.net.InetAddress.getByName(data_container[hostIndex_][1]);    				
//    				IPs[hostIndex_] = inetAdd.getHostAddress();
    				//virtual id, hostname, control_ip, data_ip
    				data_container[hostIndex_][2] = inetAdd.getHostAddress();
    				listener.println("    ---> PrimoGENI: IP:" + data_container[hostIndex_][2] + "-");
    				System.out.println("    ---> PrimoGENI: IP:" + data_container[hostIndex_][2] + "-");
    				
    				String[] tokens = data_container[hostIndex_][1].split(delims);
    				data_container[hostIndex_][1] = tokens[0];
    				listener.println("    ---> PrimoGENI: pc trimmed:" + data_container[hostIndex_][1] + "-");
    				System.out.println("    ---> PrimoGENI: pc trimmed:" + data_container[hostIndex_][1] + "-"); System.err.flush();
    				
    				System.out.println("\t virt_id=" + data_container[hostIndex_][0] + " hostname=" + 
    						data_container[hostIndex_][1] + " control_ip=" + data_container[hostIndex_][2]);
    				
    				hostIndex_ ++;    			
    			}
    		}
    		
    		//System.out.println("--0--");
    		NodeList list_of_links = doc.getElementsByTagName("link");
    		
    		if(list_of_links.getLength() > 0){
	    		Node the_link = list_of_links.item(0);
	    		//System.out.println("--1--");
	    		if(the_link.getNodeType() == Node.ELEMENT_NODE)
				{
	    			//System.out.println("--2--");
					Element link_element = (Element)the_link;
					NodeList data_interfaces_list = link_element.getElementsByTagName("interface_ref");
					//System.out.println("--3--");
					for(int i=0; i<data_interfaces_list.getLength(); i++){
						//System.out.println("--4--");
						Node data_intf = data_interfaces_list.item(i);
						if(data_intf.getNodeType() == Node.ELEMENT_NODE){
							//System.out.println("--5--");
							Element data_intf_elem = (Element)data_intf;
							String node_id = data_intf_elem.getAttribute("virtual_node_id");
							String data_ip = data_intf_elem.getAttribute("IP");
							System.out.println("data_ip for " + node_id + " is " + data_ip);
							//match virtual_node_ids
							System.out.println("numnodes=" + totalNodes);
							for(int j=0; j<totalNodes; j++){
								System.out.println("---" + j + "---=" + data_container[j][0]);
								if(data_container[j][0].equals(node_id) ){
									System.out.println("\t---" + j + "---=" + data_container[j][0]);
									//virtual id, hostname, control_ip, data_ip
									System.out.println("---1---");
									data_container[j][3] = data_ip;
				    				System.out.println("\t---virt_id=" + data_container[j][0] + " hostname=" + 
				    						data_container[j][1] + " control_ip=" + data_container[j][2] +
				    						" data_ip=" +  data_container[j][3]);
									break;
								}
							}
						}
					}
				}
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
    	return true;
    }
	
	public boolean abortInstantiation(String output)
	{
		if(output.indexOf("is busy") > 0){
			//try again
			return false;
		} else {
			//print the error and abort
			System.out.println("*********************");
			this.listener.println("*********************");
			System.out.println("Output from console:");
			this.listener.println("Output from console:");
			System.out.println(output);
			this.listener.println(output);
			System.out.println("*********************");
			this.listener.println("*********************");
			return true;
		}
	}
	
	public static void main(String[] args) {
    	System.out.println("Calling main function...");
    	String exp_name = args[0];
    	String slice_name = args[1];
    	String passphrase = args[2];
    	String private_key = args[3];
    	String num_nodes = args[4];
    	String scripts_dir = "/Users/vanvorst/Documents/workspace/primex/netscript/src/provisioner_scripts";
    	
    	System.out.println("expname=" + exp_name + " passphrase=" + passphrase + " private_key=" + private_key +
    			"numNodes=" + num_nodes);
    	IExpListenter foo = new IExpListenter() {
    		public IController controller=null;
			public void println(String str) {
				System.out.println(str);
			}
			public void handleStateUpdate(StateExchangeCmd update) {
			}
			public void finishedExperiment(boolean failed) {
			}
			public IController getController() {
				return controller;
			}
			public void setController(IController controller) {
				this.controller = controller;
			}
			public void setCompileInfo(Partitioning parting,
					List<ComputeNode> computeNodes) {
				// TODO Auto-generated method stub
				
			}
			public void setEmuHosts(HashMap<Integer, ArrayList<IHost>> emuNodes) {
				// TODO Auto-generated method stub
				
			}
		};
    	ProtoGeni conf;
		try {
			conf = new ProtoGeni(foo,scripts_dir,exp_name, slice_name, passphrase, private_key, Integer.parseInt(num_nodes));
	    	conf.allocate();
	    	System.out.println("Successful slice allocation");
	    	System.out.println("IPs got from allocation=" + conf.getComputeNodes().toString());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
