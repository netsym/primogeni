package monitor.util;


//package slingshot.environment.forms;

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

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
* @author Nathanael Van Vorst, Abu Obaida
* 
*         a class to parse a protogeni rspec
* 
*         for now we only care about links and nodes. later we may support
*         other types of information.
* 
*/
public class ManifestParser {
	// ManifestParserInSlingshotForProtoGeniV2
	private static int manifest_version;

	// public static int counter_obaida;
	public static interface ManifestNode {
		abstract boolean errors(ArrayList<String> errs);

		abstract String getString(String tab);

		abstract String getURN();
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	HashMap<String, ArrayList<NIC>> nics = new HashMap<String, ArrayList<ManifestParser.NIC>>();
	private final ArrayList<NIC_ref> refs = new ArrayList<ManifestParser.NIC_ref>();
	private final ArrayList<ManifestNode> skipped = new ArrayList<ManifestParser.ManifestNode>();
	private final static ArrayList<GeniNode> compute_nodes = new ArrayList<ManifestParser.GeniNode>();
	private final static ArrayList<GeniNodeLink> compute_node_links = new ArrayList<ManifestParser.GeniNodeLink>();
	private final ArrayList<String> errs = new ArrayList<String>();

	public ManifestParser(List<File> files, int manifest_version_arg) {
		ManifestParser.manifest_version = manifest_version_arg;
		if (files.size() == 0) {
			throw new RuntimeException("There must be at least one manifest to parse. Found 0!");
		}
		for (File f : files) { // LIST OF FILES SUPPLIED TO PARSE
			System.out.println("ManifestParser is now parsing file: "+f);
			parseFile(f);
			//System.out.println("ManifestParser Completed Parsing: "+f);

		}
		//System.out.println("Statring to resolve");
		resolve();
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	public ManifestParser(File file, int manifest_version) {
		// ManifestParserInSlingshotForProtoGeniV2
		parseFile(file);
		resolve();
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	private void parseFile(File file) {
		Document doc = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file); // Inputting Manifest file(*.xml/*.rspec) to doc parser
			
			
			//GOTTA PRINT DOC
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("Manifest Parser version: Geni v" + manifest_version);
		NodeList nodeLst = doc.getChildNodes();
		ArrayList<Node> cs = new ArrayList<Node>();
		ArrayList<Node> ls = new ArrayList<Node>();
		//System.out.println("Before parsing CS:"+cs);
		for (int i = 0; i < nodeLst.getLength(); i++) {
			Node n = nodeLst.item(i);
			if (n == null || n.getNodeName() == null)
				continue;
			if (n.getNodeName().compareTo("rspec") == 0) { 
				// see if there is a node that starts like this: <rspec verifying its our valid file
				NodeList nodeLst2 = n.getChildNodes(); // taking nodes in a list
				for (int j = 0; j < nodeLst2.getLength(); j++) { // loop for number of items in nodeLst2
					Node nn = nodeLst2.item(j); // taking one by one item of the
												// node list- extracting nodes
					if (nn == null || nn.getNodeName() == null)
						continue;
					if (nn.getNodeName().compareTo("node") == 0) { // xml nodes that starts like <node
						cs.add(nn);
					}
					/*
					 * else
					 * if(nn.getNodeName().compareTo("another_xml_node_type"
					 * )==0) { cs.add(nn); //cs.add()- cs is the list where
					 * these entries are going to }
					 */
					else if (nn.getNodeName().compareTo("link") == 0) {
						ls.add(nn);
						// System.out.println("\t LINK1");//xxxxxx
					}
				}
			}
			if (n.getNodeName().compareTo("node") == 0) {
				// System.out.println("\n\n never beeen here");
				cs.add(n);
			} else if (n.getNodeName().compareTo("link") == 0) {
				ls.add(n);
			}
		} // THis far we took all the "node"s in a list and "link"s in another
			// list //OBAIDA //Take one by one "node" from list and send to
			// GeniNode(n) to retrieve data
		//compute_nodes=null;
		
		
		//compute_nodes.removeAll(cs);
		//yyy
		//printing all the compute_nodes
		//System.out.println("Existing compute nodes are:");
		//
		//
		if(!compute_nodes.isEmpty())//compute_nodes.size()>0
		{
			compute_nodes.clear();
		}
		for (Node n : cs) {
			GeniNode c = new GeniNode(n); // Converting a NODE of XML file to GeniNode data structure defined some place in this class
			ArrayList<String> errs1 = new ArrayList<String>();
			if (c.errors(errs1)) {
				errs.add("Errors GeniNode '" + c.getURN() + "'");
				for (String e : errs1) {
					errs.add("\t" + e);
				}
				skipped.add(c);
			} else {
				compute_nodes.add(c);
				// System.out.println("\n COMPUTE NODE Added. All of out NICS: "+c.nics);//xxxxxx

				for (NIC nic : c.nics) {
					// System.out.println("\n ProblematicMark01");//xxxxxxx
					String curn = nic.getURN();
					ArrayList<NIC> nl = null;
					// System.out.println("\n nic.getURN(curn):"+curn);//xxxxxx
					if (!nics.containsKey(curn)) {
						nl = new ArrayList<ManifestParser.NIC>();
						nics.put(curn, nl);
						// System.out.println("\n nics.put(curn,nl):"+curn);//nics.put(curn,nl));
						// //xxxxx vvi
					} else {
						nl = nics.get(curn);
					}
					nl.add(nic);
				}
			}
		}
		for (Node n : ls) {
			// System.out.println("\t LINK1-1");//xxxxxx
			GeniNodeLink l = new GeniNodeLink(n);
			ArrayList<String> errs1 = new ArrayList<String>();
			if (l.errors(errs1)) {
				// System.out.println("\n There has been an error"); //xxxxxx
				errs.add("Errors GeniNodeLink '" + l.getURN() + "'");
				for (String e : errs1) {
					errs.add("\t" + e);
				}
				skipped.add(l);
			} else {
				// System.out.println("\nAdded Link:"+l);//xxxxxx
				compute_node_links.add(l);
				refs.addAll(l.refs);

			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------

	public static class GeniNode implements ManifestNode {
		public final static String[] attr_names = { "virtual_id", // client_id
				"component_manager_uuid", // fill with random values
				"virtualization_type", // fill with random values
				"virtualization_subtype", "component_urn", // component_id
				"component_uuid", // fill with random values
				"component_manager_urn", 
				"hostname", 
				"sliver_uuid", // fill with random values values
				"sliver_urn", // sliver_id
				"disk_image" };
		public final HashMap<String, String> attrs;
		public final List<NIC> nics;
		public final List<User> users;
		public int obaida_total_attrs_in_this_node;

		/*
		 * // * Example node to parse <node client_id="node3-lan1"
		 * component_manager_uuid="28a10955-aa00-11dd-ad1f-001143e453fe"
		 * virtualization_type="emulab-vnode" startup_command="" exclusive="1"
		 * virtualization_subtype="raw"
		 * component_urn="urn:publicid:IDN+emulab.net+node+pc355"
		 * component_uuid="dea00a1d-773e-102b-8eb4-001143e453fe"
		 * component_manager_id="urn:publicid:IDN+emulab.net+authority+cm"
		 * hostname="pc355.emulab.net" sshdport="22"
		 * sliver_uuid="3804bfd7-d8cc-11e0-b47a-001143e453fe"
		 * sliver_id="urn:publicid:IDN+emulab.net+sliver+54384">
		 * 
		 * <interface .../> <interface .../> <interface .../> <interface .../>
		 * <disk_image
		 * name="urn:publicid:IDN+emulab.net+image+emulab-ops//UBUNTU10-64-STD"
		 * /> <services> <login authentication="ssh-keys"
		 * hostname="pc355.emulab.net" port="22" username="vanvorst"/>
		 * </services> </node>
		 */
		public GeniNode(Node n) {
			// System.out.println("Marker  1:"+n.getNodeName());
			if (n.getNodeName().compareTo("node") != 0) {
				throw new RuntimeException("invalid node type '"+ n.getNodeName() + "'. Expected 'node'.");
			}
			this.attrs = new HashMap<String, String>();
			this.nics = new ArrayList<ManifestParser.NIC>();
			this.users = new ArrayList<ManifestParser.User>();
			NamedNodeMap node_attrs = n.getAttributes(); // a variable taking the attributes of our node int total_attributes_in_this_node=node_attrs.getLength();
			if (null != node_attrs) {// counter_obaida++; //delete_later
				if(manifest_version==3)
				{
					attrs.put("virtualization_type", "NOT_FOUND"); // length 8-4-4-4-12 characters	
				}
				//Assuming that virt_type might be missing in RSPEC geniv3 
				
				//

				this.obaida_total_attrs_in_this_node = node_attrs.getLength();
				for (int i = 0; i < node_attrs.getLength(); i++) {
					// WE HAVE TO MAP all the variables
					Node a = node_attrs.item(i);

					if (a.getNodeName().compareTo("client_id") == 0) {
						attrs.put("virtual_id", a.getNodeValue());
						continue;
					}

					else if (a.getNodeName().compareTo("component_id") == 0) {
						attrs.put("component_urn", a.getNodeValue());
						continue;
					}

					else if (a.getNodeName().compareTo("sliver_id") == 0) {
						attrs.put("sliver_urn", a.getNodeValue());
						continue;
					}

					else if (a.getNodeName().compareTo("component_manager_id") == 0) {
						attrs.put("component_manager_urn", a.getNodeValue());
						continue;
					}

					// else if(a.getNodeName().compareTo("client_id")==0)
					// {attrs.put("sliver_urn",a.getNodeValue());continue;}

					else if (a.getNodeName().compareTo("exclusive") == 0) {
						if (a.getNodeValue().compareTo("true") == 0)
							attrs.put("exclusive", "1");
						else
							attrs.put("exclusive", "0");
					}

					// geniv3
					// Earlier version of Geni Rspec didnt have compontnt_name, and this version doesnot have virtualization_type. 
					//So I m mapping them here.
					else if (a.getNodeName().compareTo("component_name") == 0) {
						attrs.put("virtualization_type", a.getNodeValue());
						continue;
					}

					// Obaida cancelling useless TAGS
					else
						continue;
					// else if
					// (a.getNodeName().compareTo("xmlns:rs")==0||a.getNodeName().compareTo("xmlns:flack")==0)
					// continue;

					// attrs.put(a.getNodeName(),a.getNodeValue());
					// System.out.println("Attr_Name="+a.getNodeName()+" | Value="+a.getNodeValue()+"  obaida_total_attrs_in_this_node="+obaida_total_attrs_in_this_node--);
				} // delete_later upper line /// xxxx delete upper line
			}
			attrs.put("component_manager_uuid",
					"RANDOMuu-idfo-rCom-pone-ntManagerLab"); // length
																// 8-4-4-4-12
																// characters
			attrs.put("component_uuid", "RANDOMuu-idfo-rCom-pone-ntEmuLabNode"); // length 8-4-4-4-12 characters
			attrs.put("sliver_uuid", "RANDOMuu-idfo-rSli-verO-faSliceProto"); // length 8-4-4-4-12 characters

			if (manifest_version == 3) // geniV3
			{
				//attrs.put("hostname", "111.111.111.111"); 
			}
			String host_info1=null, host_info2port=null,host_info3=null,host_info4=null,host_info5=null;
			//What Expected: host_info1:host_info2:host_info3:host_info4:host_info5
			//What Expected(e.g.)   pc3.instageni.instageni.clemson.edu:31242:sliver1.slicemname.geni.clemson.edu:userName:10.10.1.1
			
			
			//Hname_port_name_info=null; //ts a local variable just to store the hostname temporarily for vms(that acts as hosts)
			//Hname is the name of the Internet reachable hostname that is the creator of VMs.
			
			NodeList nodeLst = n.getChildNodes();
			if (nodeLst.getLength() > 0) {
				for (int i = 0; i < nodeLst.getLength(); i++) {
					//n =  nodeLst.item(i);  
					Node nx= nodeLst.item(i);//node list item will be "sliver_tyupe", "vnode", "interface" etc.
					//System.out.println("XX= "+nx.getNodeName());
									
					if (nx == null || nx.getNodeName() == null)
						continue;
					// We are assuming first useful SUB-NODE is sliver_type

					// APPLICABLE FOR GENI RSPEC geniV2
					else if (nx.getNodeName().compareTo("vnode") == 0) { // OBAIDA_MARKER
						attrs.put("virtualization_type", "emulab-vnode");
						// For now, im asssigning fixed value here, need to to
						// combine with hostname to get proper value
						// System.out.println("   1-Working node: <"+nx.getNodeName()+">");
						// //xxxx
						NamedNodeMap sliver_type_attrs = nx.getAttributes();
						if (null != sliver_type_attrs) {// counter_obaida++;
														// //delete_later
							for (int m = 0; m < sliver_type_attrs.getLength(); m++) {
								if (sliver_type_attrs.item(m).getNodeName().compareTo("name") == 0) {
									// String
									// vnode_name=sliver_type_attrs.item(m).getNodeValue();
									//String hostname_temp = sliver_type_attrs+ ".emulab.net";
									//attrs.put("hostname", hostname_temp); 
									// hostname serious error MODIFYY xxxxxxx
									// System.out.println("Attr_name(virtualization_subtype) = "+sliver_type_attrs.item(m).getNodeName()+", val="+sliver_type_attrs.item(m).getNodeValue());
								} // xxxx delete upper line
								else
									continue;
							} // delete_later upper line -- SET
								// virtualization_subtype here
						}
					}

//				    <sliver_type xmlns:emulab="http://www.protogeni.net/resources/rspec/ext/emulab/1" name="emulab-xen">
//				      <emulab:xen xmlns="http://www.protogeni.net/resources/rspec/ext/emulab/1" cores="2" ram="2048" disk="8"/>
//				      <disk_image name="urn:publicid:IDN+instageni.rnoc.gatech.edu+image+ch-geni-net:pgcGatechIGXen"/>
//				      <disk_image2 name="urn:publicid:IDage+ch-geni-net:pgcGatechIGXen"/>
//				    </sliver_type>
					
					else if (nx.getNodeName().compareTo("sliver_type") == 0) { // OBAIDA_MARKER
						// System.out.println("   1-Working node: <"+nx.getNodeName()+">");
						// //xxxx
						NamedNodeMap sliver_type_attrs = nx.getAttributes();
						if (null != sliver_type_attrs) {
							// counter_obaida++; delete_later
							for (int m = 0; m < sliver_type_attrs.getLength(); m++) {
								if (sliver_type_attrs.item(m).getNodeName().compareTo("name") == 0
										&& sliver_type_attrs.item(m).getNodeValue().compareTo("raw-pc") == 0) 
									{attrs.put("virtualization_subtype", "raw");
									// System.out.println("Attr_name(virtualization_subtype) = "+sliver_type_attrs.item(m).getNodeName()+", val="+sliver_type_attrs.item(m).getNodeValue());
								} // xxxx delete upper line
								else  //geniv3 specially
									attrs.put("virtualization_subtype",sliver_type_attrs.item(m).getNodeValue());
							} // delete_later upper line -- SET
								// virtualization_subtype here
						}
						//int a_counter=0;
						NodeList nodeLst1x = nx.getChildNodes();
						// NamedNodeMap
						int ttl_child=nodeLst1x.getLength();
//						System.out.println("Total child of sliver_type: "+ttl_child);
						
						if (ttl_child>0){
							for (int lc =1; lc<ttl_child; lc++ ){
								
								//System.out.println("Child#"+lc+", Val: "+nodeLst1x.item(lc).getNodeName().toString());
								
								
								//INSTAGENI WORK FOR EXTENDED OPERATIONS 
								if ((nodeLst1x.item(lc).getNodeName().compareTo("disk_image") == 0)) {
									// System.out.println("   FINALLY DISK_IMAGE [1]"+nodeLst10.item(1).getNodeName());
									// extracting attributes of disk_image now.
									NamedNodeMap disk_image_attrs = nodeLst1x.item(lc) .getAttributes();
									if (null != disk_image_attrs) {
										for (int p = 0; p < disk_image_attrs.getLength(); p++) {
											if (disk_image_attrs.item(p).getNodeName().compareTo("name") == 0||disk_image_attrs.item(p).getNodeName().compareTo("url") == 0) {
												attrs.put("disk_image",disk_image_attrs.item(p).getNodeValue());
												// System.out.println("Attr_name(disk_image) = "+disk_image_attrs.item(p).getNodeName()+" | Value="+disk_image_attrs.item(p).getNodeValue());
											} // / xxxx delete upper line

											// geniV3
											else if (disk_image_attrs.item(p) .getNodeName().compareTo("version") == 0) // sha1sum image_file_name.xml
											{
												// String
												// TEMP_version="THIS is a unused value"+disk_image_attrs.item(p).getNodeValue();
											}
										}
									}
								}
								else  //
									continue;
							}
							
						}
						
					}

					else if (nx.getNodeName().compareTo("interface") == 0) { // xxx
						try {
							 //System.out.println("\n CHECK if NIC Interface is adding: " );//xxxxxx
							nics.add(new NIC(nx, this)); // problemproblem
							 //System.out.println("\t\t Node Interface Added");//xxxxxx
						} catch (Exception e) {
							System.out.println("\t Exception: NIC <interface> didn't insert properly : "+nx);
						}
					}

					// geniV2 only
					else if (nx.getNodeName().compareTo("host") == 0) {
						node_attrs = nx.getAttributes();
						if (null != node_attrs) {
							for (int j = 0; j < node_attrs.getLength(); j++) {
								//System.out.println("HOST TAG ATTRS=" +node_attrs.getLength());

								Node a = node_attrs.item(j);
								if (a.getNodeName().compareTo("name") == 0) { 
									// updating hostname?
									if(manifest_version==3)
									{
										//this is the real hostname we will use to treat slaves in a XEN VM/ or any host that doesnt have default ssh port address 22 (not a raw-pc) 
										host_info3=a.getNodeValue();
										//System.out.println("Host Name Initial:"+a.getNodeValue());
										// attrs.put("hostname",a.getNodeValue()); 								
									}
									else if(manifest_version==2)
									{
										// attrs.put("hostname",a.getNodeValue()); //UNUSED for now										
									}
								}
							}
						}
					}

					else if (nx.getNodeName().compareTo("services") == 0) { //hostnamexxx
						//System.out.println("Services");
						NodeList nodeLst3 = nx.getChildNodes();
						//NodeList nodeLstHostname =nodeLst3; int host_count_1=0;
						if (nodeLst3.getLength() > 0) {
							for (int j = 0; j < nodeLst3.getLength(); j++) {
								Node n1 = nodeLst3.item(j);
								if (n1 == null || n1.getNodeName() == null)
									continue;
								if (n1.getNodeName().compareTo("login") == 0) {
									//System.out.println("Hostname X="+host_count_1++);
									users.add(new User(n1));
									//RETRIEVE HOSTNAME or IP from here
									if (manifest_version == 3||manifest_version == 2) // geniV3 only
									{   
										//String hostname_temp_loop = null;
										NamedNodeMap node_attrs_hostname = n1.getAttributes();
        								//IF THERE IS MULTIPLE <LOGIN> NODE THAN IT KEEPS THE LAST ONE BECAUSE WE ARE OVERWRITING 
											if (null != node_attrs_hostname) {
												for (int l = 0; l < node_attrs_hostname.getLength(); l++) {
												//for (int l = 0; l < 2; l++) {
													Node b = node_attrs_hostname.item(l);
													if (b.getNodeName().equals("hostname"))
														{
																												
														host_info1 = b.getNodeValue();
													    //System.out.println("Name is: "+hostname_temp_loop);
														}
													else if(b.getNodeName().equals("port"))
													{
														host_info2port=b.getNodeValue();
														//This node is a VM created by a hypervisor e.g. Xen
													}
													else if(b.getNodeName().equals("username")&&!b.getNodeValue().equals("root"))
													{
														host_info4=b.getNodeValue();
													}
													else 
														continue;
												}
											}
									}									
								}
							}
						}
					}
					// cancelling Nodes
					else if (nx.getNodeName().compareTo("flack:node_info") == 0 || nx.getNodeName().compareTo("ns4:geni_sliver_info") == 0 || nx.getNodeName().compareTo("location") == 0 || nx.getNodeName().compareTo("rs:vnode") == 0)
					{
						// System.out.println("   X-Skipped node: <"+nx.getNodeName()+">");//xxxx
						continue;
					}
				}
			}

			if(host_info2port.compareTo("22")!=0)
				attrs.put("hostname",host_info1+":"+host_info2port+":"+host_info3+":"+host_info4); 
			else 
				attrs.put("hostname",host_info1);//exogeni case
			
			//attrs.put("hostname",host_info);
		}
		// ---------------------------------------------------------------------------------------------------------------------------
		public String getURN() {// System.out.println("\n I was here"); //xxxxx
			if (attrs.containsKey("component_urn"))
				return attrs.get("component_urn");
			return "[component_urn not found]";
		}
		// ---------------------------------------------------------------------------------------------------------------------------
		public boolean errors(ArrayList<String> all_errs) {
			boolean rv = false;
			boolean added_p = false;
			ArrayList<String> errs = new ArrayList<String>();
			for (String s : attr_names) {
				if (!attrs.containsKey(s)) {
					if (s.compareTo("disk_image") == 0) {
						errs.add("\tExpected child node 'disk_image' with attribute 'name' to be present!\n");
					} else {
						if (!added_p) {
							added_p = true;
						}
						errs.add("\tExpected attribute '" + s
								+ "' to be present!\n");
					}
					rv = true;
				}
			}

			for (NIC nic : nics) {
				ArrayList<String> errs1 = new ArrayList<String>();
				rv = rv || nic.errors(errs1);
				if (errs1.size() > 0) {
					errs.add("\tErrors NIC '" + nic.getURN() + "'\n");
					for (String s : errs1) {
						errs.add("\t\t" + s + "\n");
					}
				}
			}
			for (User user : users) {
				ArrayList<String> errs1 = new ArrayList<String>();
				rv = rv || user.errors(errs1);
				if (errs1.size() > 0) {
					errs.add("\tErrors User '" + user.getURN() + "'\n");
					for (String s : errs1) {
						errs.add("\t\t" + s + "\n");
					}
				}
			}
			if (errs.size() > 0) {
				all_errs.add("\tErrors Parsing node element!");
				all_errs.addAll(errs);
			}
			return rv;
		}

		public String toString() {
			return getString("");
		}

		public String getString(String tab) {
			String rv = tab + "<GeniNode";
			for (Entry<String, String> s : attrs.entrySet()) {
				rv += "\n" + tab + "\t " + s.getKey() + "=\"" + s.getValue()
						+ "\"";
			}
			rv += " >\n";
			for (NIC nic : nics) {
				rv += nic.getString(tab + "\t") + "\n";
			}
			for (User user : users) {
				rv += user.getString(tab + "\t") + "\n";
			}
			rv += tab + "</GeniNode>";
			return rv;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	public static class User implements ManifestNode {
		public final String username;
		public final String authentication;

		public User(Node nu) {
			if (nu.getNodeName().compareTo("login") != 0) {
				throw new RuntimeException("invalid node type '" + nu.getNodeName() + "'. Expected 'login'.");
			}
			String auth = null, user = null;
			NamedNodeMap node_attrs = nu.getAttributes();
			if (null != node_attrs) {
				for (int i = 0; i < node_attrs.getLength(); i++) {
					Node a = node_attrs.item(i);
					if (a.getNodeName().equals("username"))
						user = a.getNodeValue();
					else if (a.getNodeName().equals("authentication"))
						auth = a.getNodeValue();
				}
			}
			this.username = user == null ? "" : user;
			this.authentication = auth == null ? "" : auth;
		}

		public boolean errors(ArrayList<String> errs) {
			if (0 == username.length() || 0 == authentication.length()) {
				if (0 == username.length())
					errs.add("Expected attribute 'username' to be present and have a length greater than 0!");
				if (0 == authentication.length())
					errs.add("Expected attribute 'authentication' to be present and have a length greater than 0!");
				return true;
			}
			return false;
		}

		public String getURN() {
			return username;
		}

		public String toString() {
			return getString("");
		}

		public String getString(String tab) {
			return tab + "<User username=\"" + username
					+ "\" authentication=\" " + authentication + "\" />";
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	public static class NIC implements ManifestNode {
		public final String virtual_id;
		// public final String component_id; //this would store which "ethX" for
		// this node
		public final String component_id;
		public final String urn;
		public final String sliver_id;
		public final String mac_address;
		public final String address;
		public final String netmask;
		public final String type;

		int fault_nic_counter = 1;

		public NIC_ref ref = null; // will be set during resolution phase
		public GeniNode parent;

		public NIC(Node nodeNic, GeniNode parent) {
			if (nodeNic.getNodeName().compareTo("interface") != 0) {
				// System.out.println("\t\t DID nothing"); //xxxxxx
				throw new RuntimeException("invalid node type '" + nodeNic.getNodeName() + "'. Expected 'interface'.");
			}
			//System.out.println("\tCall transfered to NIC to add :"+nodeNic.getNodeName());
			// //xxxxxx
			this.parent = parent;
			String virtual_id_temp = null, component_urn_temp = null, sliver_id_temp = null, mac_addr = null;
			NamedNodeMap node_attrs = nodeNic.getAttributes();
			if (null != node_attrs) {
				// System.out.println("\t node_attrs.getlength():"+node_attrs.getLength());
				// //xxxxxx
				for (int i = 0; i < node_attrs.getLength(); i++) {
					Node a = node_attrs.item(i);
					if (a.getNodeName().equals("client_id"))
						{virtual_id_temp = a.getNodeValue();
						//System.out.println("Got Client ID: ss"+a.getNodeValue());
						}
					else if (a.getNodeName().equals("mac_address")) {
						if (manifest_version == 3) { // for geniV3 conversion
							mac_addr = a.getNodeValue().replaceAll(":", ""); 
							// truncating special signs from mac_address
						} else
							mac_addr = a.getNodeValue();
						
					}
					// geniV2 only
					else if (a.getNodeName().equals("component_id")&&manifest_version==2) {
						 System.out.println("HERE I AM PROCESSING NIC ::::"+a.getNodeValue());
						component_urn_temp = a.getNodeValue();
						// to SOLVE problem with compatiablity: that old
						// component id was just a ethernet number
						// I will take out the host part from the component_urn
						String[] split_parts = component_urn_temp.split(":");
						// String cid_1=split_parts[0];
						// String cid_2=split_parts[1];
						component_urn_temp = ""+ split_parts[split_parts.length - 1];
						 System.out.println("component_urn(expecting like eth0):"+component_urn_temp); //xxxx vvi
					} else if (a.getNodeName().equals("sliver_id")&&manifest_version==2)
						sliver_id_temp = a.getNodeValue();
					else
						continue; // System.out.println("FAILED! :"+a.getNodeName());
				}
			}
			String my_mac=null;
			if (manifest_version == 3) { // for geniV3 conversion
				sliver_id_temp = "urn:________:IDN+______.net+sliver+000000";
				//component_urn_temp = "NO_ETH"; // NEEED TO BE CHANGED
				String [] component_urn_temp_v3 =virtual_id_temp.split(":");
				component_urn_temp=component_urn_temp_v3[1];
				if (mac_addr==null)
					{
					//System.out.println("Mac_addr1:"+mac_addr);
					Random rand = new Random();
					int randomNum2Dgt = rand.nextInt((99 - 10) + 1) + 10;
					my_mac="00:00:00:00:00:"+randomNum2Dgt;
					mac_addr=my_mac;
					//System.out.println("Mac_addr2:"+mac_addr);

					}
			}

			this.component_id = component_urn_temp == null ? "": component_urn_temp;

			if (manifest_version == 2) // geniV2 only
			{
				this.urn = parent.getURN().replace("+node+", "+interface+")+ ":" + this.component_id;
				//System.out.println("geniv2");
			} else if (manifest_version == 3) // geniv3 only
				this.urn = virtual_id_temp;
			else
				this.urn = virtual_id_temp;
			//System.out.println("  link Component ID: "+component_urn); //xxxxxx
			// this.urn=virtual_id_temp;
			//System.out.println("\nHERE is the problem , I believe regarding(urn): "+this.urn);//xxxx vvi
			this.virtual_id = virtual_id_temp == null ? "" : virtual_id_temp;
			// System.out.println("  link Component ID: "+component_urn); //xxxxxx
			this.sliver_id = sliver_id_temp == null ? "" : sliver_id_temp;
			if (sliver_id_temp == null)
				throw new RuntimeException("invalid sid");
			// System.out.println("  link sliver ID: "+sliver_id); //xxxxxx
			// System.out.println("Mac_addra:"+mac_addr);
			this.mac_address = mac_addr == null ? my_mac : mac_addr;
			if (mac_addr == null)
				{
				//System.out.println("Mac_addrb:"+mac_addr);
				throw new RuntimeException("invalid mac");}
			// Need to find IP address from <ip> tag.
			//System.out.println("Finding IP ADDRESS in interface"); //xxxx
			NodeList nodeLst11 = nodeNic.getChildNodes();
			String ip_address = null, ip_netmask = null, ip_type = null;
			for (int s=0;s<nodeLst11.getLength();s++) {
				if (nodeLst11.item(s).getNodeName().compareTo("ip") == 0) {
					//System.out.println("       FINALLY IP: index["+s+"]:"+nodeLst11.item(s).getNodeName());//xxxxxxx
					// extracting attributes of IP.
					NamedNodeMap ip_attributes = nodeLst11.item(s)
							.getAttributes();
					if (null != ip_attributes) {
						for (int t = 0; t < ip_attributes.getLength(); t++) {
							if (ip_attributes.item(t).getNodeName().compareTo("address") == 0) {
								ip_address = ip_attributes.item(t).getNodeValue();
								
								//System.out.println("Attr_name(ip) = "+ip_attributes.item(t).getNodeName()+" | Value="+ip_attributes.item(t).getNodeValue());
							} // / xxxx delete upper line
							else if (ip_attributes.item(t).getNodeName().compareTo("netmask") == 0) {
								ip_netmask = ip_attributes.item(t).getNodeValue();
								//System.out.println("Attr_name(ip) = "+ip_attributes.item(t).getNodeName()+" | Value="+ip_attributes.item(t).getNodeValue());
							} 
							else if (ip_attributes.item(t).getNodeName().compareTo("type") == 0) {
								ip_type = ip_attributes.item(t).getNodeValue();
								//System.out.println("Attr_name(ip) = "+ip_attributes.item(t).getNodeName()+" | Value="+ip_attributes.item(t).getNodeValue());
							} 
						}
					}
					break;
				}
			}
			this.address = ip_address;
			this.netmask = ip_netmask;
			this.type = ip_type;
		}

		/*
		 * public String getURN() { //Previously they used to return urn, that
		 * is a string; since new RSPEC //doesn't have urn field we are changing
		 * it to component_urn //return "xxURN NOT FOUNDxx"; //return urn; }
		 */

		public String getURN() {// Previously they used to return urn, that is a string
								//  since new RSPEC doesn't have urn field we are changing it
								// to component_urn return "xxURN NOT FOUNDxx";
			return urn;
		}

		public String toString() {
			return getString("");
		}

		public String getString(String tab) {
			// we already have MAC and sliver id, we can concatenate that, we
			// need to concatenate ip_address from Child node though
			// String mac = (ref == null)?null:ref.attrs.get("MAC");
			// if(mac ==null)mac = "unknown";
			// String ip = (ref == null)?null:ref.attrs.get("IP");
			// if(ip ==null)ip = "unknown";
			return tab + "<NIC> virtual_id=\"" + virtual_id
					+ "\" component_urn=\"" + component_id + "\" sliver_id=\""
					+ sliver_id + "\" mac_address=\"" + mac_address
					+ "\" ip=\"" + address + "\" />";
		} // return
			// tab+"<NIC> client_id=\""+client_id+"\" component_urn=\""+component_urn+"\" "+" mac_address="+mac_address+"/>";
			// EXCEPTION and error handling

		public boolean errors(ArrayList<String> errs) {
			if (0 == virtual_id.length() || 0 == component_id.length()
					|| 0 == sliver_id.length() || 0 == mac_address.length()
					|| 0 == address.length()) {
				if (0 == virtual_id.length())
					errs.add("Expected attribute 'virtual_id' to be present and have a length greater than 0!");
				if (0 == component_id.length())
					errs.add("Expected attribute 'component_urn' to be present and have a length greater than 0!");
				if (0 == sliver_id.length())
					errs.add("Expected attribute 'sliver_id' to be present and have a length greater than 0!");
				if (0 == mac_address.length())
					errs.add("Expected attribute 'mac_address' to be present and have a length greater than 0!");
				if (0 == address.length())
					errs.add("Expected attribute 'ip' to be present and have a length greater than 0!");
				return true;
			}
			return false;
		}

		public String getValue() {
			// TODO Auto-generated method stub
			return address;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	public static class GeniNodeLink implements ManifestNode {
		public final static String[] attr_names = { "virtual_id",
				"sliver_uuid", "sliver_id", "vlantag" };
		public final HashMap<String, String> attrs;
		public final List<NIC_ref> refs;

		/*
		 * <link virtual_id="lan1-prime"
		 * sliver_uuid="3fc79e4a-d8cc-11e0-b47a-001143e453fe"
		 * sliver_id="urn:publicid:IDN+emulab.net+sliver+54396" vlantag="565">
		 * <interface_ref ... /> <interface_ref ... /> <interface_ref ... />
		 * </link>
		 */
		public GeniNodeLink(Node n) {
			if (n.getNodeName().compareTo("link") != 0) {
				throw new RuntimeException("invalid node type '"
						+ n.getNodeName() + "'. Expected 'link'.");
			}
			this.attrs = new HashMap<String, String>();
			this.refs = new ArrayList<ManifestParser.NIC_ref>();
			NamedNodeMap node_attrs = n.getAttributes();
			
			boolean vlantag_presence=false; 
			if (null != node_attrs) {
				for (int i = 0; i < node_attrs.getLength(); i++) {
					Node a = node_attrs.item(i);
					if (a.getNodeName().compareTo("client_id") == 0) {
						attrs.put("virtual_id", a.getNodeValue());
					} 
					else if (a.getNodeName().compareTo("sliver_id") == 0|| a.getNodeName().compareTo("vlantag") == 0) 
					{
						attrs.put(a.getNodeName(), a.getNodeValue());
						
					}
					else if (a.getNodeName().compareTo("vlantag") == 0) 
					{
						attrs.put(a.getNodeName(), a.getNodeValue());
						vlantag_presence=true;
					} 
					else
						continue;// if(a.getNodeName().compareTo("xmlns:flack")==0)
									// continue;
				}
			}
			attrs.put("sliver_uuid", "RANDOMuu-idfo-rSli-verO-faSliceLink1"); // length 8-4-4-4-12 characters
			if (vlantag_presence==false)
			{  //Giving random vlantag
				Random rand= new Random();
		        int pick = rand.nextInt(900) + 100;
		        
		        //String vlantag_value=""+pick;
				attrs.put("vlantag",""+pick);
			}
				
			NodeList nodeLst = n.getChildNodes();
			if (nodeLst.getLength() > 0) {
				for (int i = 0; i < nodeLst.getLength(); i++) 
				{
					n = nodeLst.item(i);
					if (n == null || n.getNodeName() == null)
						continue;
					if (n.getNodeName().compareTo("interface_ref") == 0) 
					{
						refs.add(new NIC_ref(n, this));
					}
				}
			}
		}

		public String getURN() {
			if (attrs.containsKey("virtual_id"))
				return attrs.get("virtual_id");
			return "[virtual_id not found]";
		}

		public boolean errors(ArrayList<String> errs) {
			boolean rv = false;
			for (String s : attr_names) {
				if (!attrs.containsKey(s)) {
					errs.add("Expected attribute '" + s + "' to be present!");
					rv = true;
				}
			}
			if (refs.size() == 0) {
				errs.add("Expected at least one network interface reference. Found 0!");
				rv = true;
			} else {
				for (NIC_ref ref : refs) {
					ArrayList<String> errs1 = new ArrayList<String>();
					rv = rv || ref.errors(errs1);
					if (errs1.size() > 0) {
						errs.add("Errors NIC_ref '" + ref.getURN() + "'");
						for (String s : errs1) {
							errs.add("\t" + s);
						}
					}
				}
			}
			return rv;
		}

		public String toString() {
			return getString("");
		}

		public String getString(String tab) {
			String rv = tab + "<GeniNodeLink";
			for (Entry<String, String> s : attrs.entrySet()) {
				rv += "\n" + tab + "\t" + s.getKey() + "=\"" + s.getValue()
						+ "\"";
			}
			rv += " >\n";
			for (NIC_ref nic : refs) {
				rv += nic.getString(tab + "\t") + "\n";
			}
			rv += tab + "</GeniNodeLink>";
			return rv;
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	public static class NIC_ref implements ManifestNode {
		public final static String[] attr_names = { "virtual_id",
				"virtual_interface_id", // TO DO- virtual_interface_id="geni1:if0" that is <interface client_id> under Geni_node
				"component_urn", "sliver_uuid", "sliver_id", "IP", // TODO from Geni Node NIC
				"MAC" // TODO from Geni Node NIC //SOLVE THIS MAC PROBLEM
		};
		public final HashMap<String, String> attrs;
		public final GeniNodeLink parent;
		public NIC nic = null;

		public NIC_ref(Node n, GeniNodeLink parent) {
			if (n.getNodeName().compareTo("interface_ref") != 0) {
				throw new RuntimeException("invalid node type '"
						+ n.getNodeName() + "'. Expected 'interface_ref'.");
			}
			this.parent = parent;
			this.attrs = new HashMap<String, String>();
			NamedNodeMap node_attrs = n.getAttributes();
			if (null != node_attrs) {
				for (int i = 0; i < node_attrs.getLength(); i++) {
					Node a = node_attrs.item(i);

					if (a.getNodeName().compareTo("client_id") == 0) {
						attrs.put("virtual_id", a.getNodeValue());
						// very important for geniv3 ony
						if (manifest_version == 3) {
							attrs.put("component_urn", a.getNodeValue());
							continue;
							// System.out.println("I am here:"+a.getNodeValue());
						}
						continue;
					}
					// geniv2 only
					else if (a.getNodeName().compareTo("component_id") == 0&&manifest_version==2) {
						attrs.put("component_urn", a.getNodeValue());

						continue;
					} else if (a.getNodeName().compareTo("sliver_id") == 0) {
						attrs.put("sliver_id", a.getNodeValue());
						continue;
					}

					// geni no vetrsion tags
					else
						continue;// attrs.put(a.getNodeName(),a.getNodeValue());
					// System.out.println("NIC_ref : name="+a.getNodeName()+" val="+a.getNodeValue());//xxxxxx

				}
			}
			attrs.put("sliver_uuid", "RANDOMuu-idfo-rSli-verO-faSliceLink1");  // length 8-4-4-4-12 characters
			attrs.put("virtual_interface_id","RANDOMuu-idfo-rSli-verO-faSliceLink1"); // length 8-4-4-4-12 characters
			attrs.put("IP", "10.10.10.10"); // length 8-4-4-4-12 characters
			attrs.put("MAC", "000Random000"); // length 8-4-4-4-12 characters
			// geniv3 only
			if (manifest_version == 3) {
				attrs.put("sliver_id", "sliver_id__________________________________empty");
				// attrs.put("component_urn", "NO_ETH");
			}
		}

		public boolean errors(ArrayList<String> errs) {
			boolean rv = false;
			for (String s : attr_names) {
				if (!attrs.containsKey(s)) {
					errs.add("Expected attribute '" + s + "' to be present!");
					rv = true;
				} else if (attrs.get(s).length() == 0) {
					errs.add("Expected attribute '"
							+ s
							+ "' to be present and have a length greater than 0!");
					rv = true;
				}
			}
			return rv;
		}

		public String getURN() {
			if (attrs.containsKey("component_urn"))
				return attrs.get("component_urn");
			return "[component_urn not found]";
		}

		public String get_NIC_ref__fake_IP() {
			if (attrs.containsKey("IP"))
				return attrs.get("IP");
			return "[IP not found]";
		}

		// TODO we need to get MAC and IP return functions
		public String get_NIC_ref__fake_MAC() {
			if (attrs.containsKey("MAC"))
				return attrs.get("MAC");
			return "[MAC not found]";
		}

		public String getNICrefInterfaceSliverID() {
			if (attrs.containsKey("sliver_id"))
				return attrs.get("sliver_id");
			return "[sliver_id not found]";
		}

		// geniv3 only
		public String getNICrefInterfaceClintIDorVirtualID() {
			if (attrs.containsKey("virtual_id")) // client_id in GENI RSPEC v3.0
				return attrs.get("virtual_id");
			return "[virtual_id not found]";
		}

		public boolean set_NIC_ref__original_IP(String ipv4_ip_temp) {
			// System.out.println("Trying to change existing fake IP:"+attrs.get("IP"));
			attrs.put("IP", ipv4_ip_temp);
			if (attrs.get("IP").compareTo(ipv4_ip_temp) == 0) {
				// System.out.println("Change Successful, new value of IP:"+attrs.get("IP"));
				return true;
			} else
				return false;
		}

		public boolean set_NIC_ref__original_MAC(String mac_address_temp) {
			//System.out.println("Trying to change existing fake MAC:"+attrs.get("MAC"));
			attrs.put("MAC", mac_address_temp);
			if (attrs.get("MAC").compareTo(mac_address_temp) == 0) {
				//System.out.println("Change Successful, new value of MAC:"+attrs.get("MAC"));
				return true;
			} else
				return false;
		}

		public String toString() {
			return getString("");
		}

		public String getString(String tab) {
			String rv = tab + "<NIC_ref ";
			for (Entry<String, String> s : attrs.entrySet()) {
				rv += " " + s.getKey() + "=\"" + s.getValue() + "\"";
			}
			String vid = parent.attrs.get("virtual_id");
			if (vid == null)
				vid = "<unknown>";
			rv += " link_vid=\"" + vid + "\"";
			rv += " resolved=" + (nic == null ? "false" : "true");
			System.out.println(rv + " />");
			// System.out.println("HERE: "+rv+" />"); //my version
			return rv + " />";
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------
	private void resolve() {// int counter=1;
		System.out.println("ManifestParser resolve-> Before Update");

		if (Updating_Data_Structure()) {
			System.out .println("All IP,MAC updates successful");
			System.out.println("Updating done ");

		} else
			System.out.println("!WARNING! Problem updating data structure");

		for (NIC_ref ref : refs) {
			
			//System.out.println("(M1)Searching for: ref.getURN()->  "+ref.getURN()+" THAT IS component_urn in PREVIOUS RSPEC"); //xxxx vvi
			//System.out.println("    (M2)Searching in: "+nics +"\n"); 
			//xxxx vvi ISSUE WITH I NEEED TO USE unique way for nics that is virtual_id or client_id in <LINK>
			if (nics.containsKey(ref.getURN())) { // xxxxxx
				// System.out.println("\nMARKER 3 "); //xxxxxxx vvi
				for (NIC nic : nics.get(ref.getURN())) {

					//System.out.println("\n\n\nMARKER 4 "+nics.get(ref.getURN())); //xxxxx vvi
					nic.ref = ref;
					ref.nic = nic;
				}
			} else {

				errs.add("Unable to find interface '" + ref.getURN() + "' referred to by:");
				errs.add(ref.getString("\t"));
			}
		}

		for (Entry<String, ArrayList<NIC>> e : nics.entrySet()) {
			if (e.getValue().size() == 1) {
				for (NIC nic : e.getValue()) {
					if (nic.ref == null) {
						errs.add("Could not find reference for interface '"
								+ nic.getURN() + "':");
						errs.add(nic.getString("\t"));
					}
				}
			} else {
				errs.add("The component URN " + e.getKey() + " mapped to " + e.getValue().size() + " interfaces! Interfaces:");
				for (NIC nic : e.getValue()) {
					errs.add(nic.getString("\t"));
				}
			}
		}
	}

	// ---------------------------------------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------------------------------------
	public ArrayList<String> getParseErrors() {
		return errs;
	}
//yyy
	public ArrayList<GeniNode> getNodes() {
		return compute_nodes;
	}

	public ArrayList<GeniNodeLink> getLinks() {
		return compute_node_links;
	}

	public void print(PrintStream out) {
		if (skipped.size() > 0) {
			out.println("Skipped:");
			for (ManifestNode n : skipped) {
				out.println(n.getString("\t"));
			}
		}
		out.println("Nodes:");
		for (GeniNode c : compute_nodes) {
			out.println(c.getString("\t"));
		}
		out.println("Links:");
		for (GeniNodeLink l : compute_node_links) {
			out.println(l.getString("\t"));
		}
	}

	// -------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	public static boolean Updating_Data_Structure() {
		// FUNCTION starts here
		// int geni_node_counter=1;
		for (GeniNode c3 : compute_nodes) {
			//out2.println(c3.getString("\t"));
			//yyy
			//System.out.println("Compute Node ="+c3.getString("\t"));
			// System.out.println("----------------------------------------:"+geni_node_counter+++":-----------------------------");//xxxxxxxx
			// System.out.println(c3.nics);
			List<NIC> nics3 = c3.nics;
			Iterator<NIC> iter = nics3.iterator();
			// int total_nics_in_a_node=nics3.size();
			// System.out.println("\t total_nics_in_a_node:"+total_nics_in_a_node);
			while (iter.hasNext()) {
				NIC entry = iter.next();
				String mac_address_temp = entry.mac_address;
				String ipv4_ip_temp = entry.address;
				
				//System.out.println("IP="+ipv4_ip_temp+" mac="+mac_address_temp);
				// String unique_component_urn=entry.component_urn;
				// //component_urn from a Interface of a GENI Node
				String comparable_value2 = null;
				 
				// component_urn from a Interface of a GENI Node

				// System.out.println("\n\t\t macAddressTemp:"+mac_address_temp+"\tipv4IpTemp:"+ipv4_ip_temp);
				// Now I have to add ip address and mac address addresses to LINk nic_refs
				// TODO
				// we are going to loop all nic_ref now and update the ones with the
				// Common field is component_id(comkponent_urn)
				// System.out.println("LINK_refs(infos to be replaced):");
				if (manifest_version == 2) 
				{
				  comparable_value2=entry.sliver_id;
				}
				else if(manifest_version == 3)
					comparable_value2=entry.virtual_id;
					
					
				for (GeniNodeLink lr3 : compute_node_links) {
					List<NIC_ref> nic_ref_entries = lr3.refs;
					Iterator<NIC_ref> iter2 = nic_ref_entries.iterator();
					while (iter2.hasNext()) {
						NIC_ref nic_ref_item = iter2.next();
						// System.out.println("\tNIC_ref_entries:"+nic_ref_entries);
						// System.out.println("\tlink_ref_temp getURN:"+nic_ref_item.getURN()+" ||get_NIC_ref__fake_IP:"+nic_ref_item.get_NIC_ref__fake_IP()+" || get_NIC_ref__fake_MAC:"+nic_ref_item.get_NIC_ref__fake_MAC());
						// System.out.println("\t1 = "+unique_interface_sliver_id+"\n\t2 = "+nic_ref_item.getNICrefInterfaceSliverID());
						String temp_nic_ref = null;
						if (manifest_version == 3) {
							//System.out.println("Updating V3 MACs for:"+nic_ref_item.getNICrefInterfaceClintIDorVirtualID());
							temp_nic_ref = nic_ref_item.getNICrefInterfaceClintIDorVirtualID();
						} else if (manifest_version == 2) {
							temp_nic_ref = nic_ref_item.getNICrefInterfaceSliverID();
							
						}
						//System.out.println("temp_nic_ref: "+temp_nic_ref+ " compare to="+comparable_value2);
						if (comparable_value2.compareTo(temp_nic_ref) == 0) {
							//System.out.println("FOUND a MATCH");
							// nic_ref_item.get_NIC_ref__fake_ip()
							// System.out.println("\t\tFOUND MATCH. MAKE structure changes here: (i.e. copy MAC and IP to link ArrayList)");
							// TODO
							// System.out.println("\tReplace "+nic_ref_item.get_NIC_ref__fake_MAC()+" with "+mac_address_temp);
							// System.out.println("\tReplace "+nic_ref_item.get_NIC_ref__fake_IP()+" with "+ipv4_ip_temp);
							// CHANGING IP
							if (nic_ref_item.set_NIC_ref__original_IP(ipv4_ip_temp)) {
								System.out.print("IP updated, ");
							} else {
								System.out.println("!WARNING!IP couldnt update");
								return false;
							}
							// CHANGING MAC
							if (nic_ref_item.set_NIC_ref__original_MAC(mac_address_temp)) {
								System.out.print("MAC updated for ="+ comparable_value2 + "\n");
							} else {
								System.out.println("!WARNING!MAC couldnt update");
								return false;
							}
						}
					}
				}
			}
		}
		// All operation successful
		return true;

	}// end of function

	// -------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------

	// -------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------

	public static final void main(String args[]) {

		// START OPTION1
		ArrayList<File> files = new ArrayList<File>();
		for (int i = 0; i < args.length; i++) {
			System.out.println("Adding '" + args[i] + "' to the parse set.");
			files.add(new File(args[i]));
		}
		int manifest_version = 2;
		ManifestParser p = new ManifestParser(files, manifest_version);

		if (p.getParseErrors().size() > 0) {
			System.out.println("WARNING: There were problems parsing 1 or more xml elements in the manifest(s). The errors follow.");
			for (String e : p.getParseErrors()) {
				System.out.println("\t" + e);
			}
		}
		p.print(System.out);

		// START OPTION2

	}// end of main function
}// end of class