/*This file generates RSPEC v0.2  for ProtoGENI emulab.net component Manager*/
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

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author Miguel Erazo
 *
 */
public class RSpec
{
	//Instance variables;
	private int numMachines = 0;
	private String heading = "";
	//private String node = "";
	private String nodeTail = "";
	private String rspecTail = "";
	private String nodeHeading = "";
	private String nodeCM = "";
	//private String virtualID = "";
	private String virtualizationType = "";
	//private String startupCommand = "";
	//private String tarFile;
	private String interfaceVirtualID = "";	
	private String imageName = "";
	private String serverDomain = "";
	
	//Constructors
	public RSpec(int number_machines)
	{
		//Default is FIU's ProtoGENI site
		this(number_machines, "cis.fiu.edu", "", "", "");
	}
	
	public RSpec(int number_machines, String server_domain)
	{
		this(number_machines, server_domain, "", "", "primoGENIv2-PG");
	}
	
	public RSpec(int num_machines, String domain, String command, String tar_file, String image_name)
	{
		/*
		 * RSpec specific fields
		 */
		heading = "<rspec xmlns=\"http://protogeni.net/resources/rspec/0.2\" type=\"request\">";		
		nodeHeading = "<node virtual_id=\"primogeni";
		nodeCM = "component_manager_uuid=\"urn:publicid:IDN+" + domain + "+authority+cm\"";
		virtualizationType = "virtualization_type=\"emulab-vnode\"";
		interfaceVirtualID = "<interface virtual_id=\"if0\"/>";
		imageName = "<disk_image name=\"urn:publicid:IDN+" + domain + "+image+GeniSlices//" + image_name + "\"/>";
		nodeTail = "</node>";
		rspecTail = "</rspec>";
		serverDomain = domain;
		
		//runtime parameters
		numMachines = num_machines;
	}
	
	private void writetoFile(String output_file_, String content_){
		try{
			// Create file 
			FileWriter fstream = new FileWriter(output_file_);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content_);
		    //Close the output stream
		    out.close();
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public String GenerateRSpec()
	{
		String rspec_ = "";
		
		if(numMachines <= 0){
			System.out.println("Cannot allocate a negative number of machines!");
			System.exit(1);
		}
				
		rspec_ = heading + "\n";
		for(int i=0; i < numMachines; i++) {
			rspec_ += nodeHeading + i + "\"\n";
			rspec_ += nodeCM + "\n";
			rspec_ += virtualizationType + "\n";
			
			/*if(i == numMachines) {
				//this is the last machine
				rspec_ += "startup_command=\"" + "\"" + ">\n";
			} else {*/
				//experimental machines
				rspec_ += "startup_command=\"" + "\"" + ">\n";
			//}
			rspec_ += interfaceVirtualID + "\n";
			if(!serverDomain.equals("cis.fiu.edu"))
				rspec_ += imageName + "\n";				
			rspec_ += nodeTail + "\n";			
		}
		
		if(!serverDomain.equals("cis.fiu.edu")){
			rspec_ += "<link virtual_id=\"center\">\n";
			for(int i=0; i < numMachines; i++) {
				rspec_ += "<interface_ref virtual_node_id=\"primogeni" + i + "\"\n";
				rspec_ += "virtual_interface_id=\"if0\" />\n";
			}
			rspec_ += "</link>\n";
		}
		
		rspec_ += rspecTail;
		
		System.out.println("RSPEC----**********-------\n" + rspec_ + "\n------******----\n");
		this.writetoFile("test3.rspec", rspec_);
		
		return rspec_;
	}	
}

