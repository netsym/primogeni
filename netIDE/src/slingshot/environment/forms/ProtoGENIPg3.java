package slingshot.environment.forms;

import java.util.ArrayList;
import java.util.LinkedList;


import jprime.util.ComputeNode;
import jprime.util.Portal;
import slingshot.environment.EnvType;
import slingshot.environment.EnvironmentFileModel;

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



/**
 * 
 * 
 * @author Nathanael Van Vorst
 *
 */
public class ProtoGENIPg3 extends RemoteClusterPg2 {
	
	public ProtoGENIPg3() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see slingshot.environment.forms.BaseForm#saveDataToModel(slingshot.environment.EnvironmentFileModel, java.lang.String)
	 */
	public void saveDataToModel(EnvironmentFileModel model, String name) {
		if(name != null) model.name = name;
		model.environment = EnvType.PROTO_GENI;
		model.nodes = new LinkedList<ComputeNode>();
		
		//Modifying master and slave before saving to model (Modifying from XEN/Exo Vm form to our format.)
		String master_control_ip=txtMaster_control_ip.getText();
		if (master_control_ip.contains(":"))
		{
			String[] parts_master=master_control_ip.split(":");
			master_control_ip=parts_master[0]+":"+parts_master[1];
		}
		System.out.println("Adding Master: ControlIP="+master_control_ip);
		model.nodes.add(new ComputeNode(master_control_ip, txtMaster_data_ip.getText(), new ArrayList<Portal>()));
		
		//slaves are in a hash set of compute nodes they need to be modified other way
		if (slaves.toString().contains(":")) //slaves are of port format(not raw-pc:22), we need to delete the first two aprts of the slave name  
		{
 			for (ComputeNode a_slave : slaves) {
				// out2.println(c3.getString("\t"));
				// System.out.println("----------------------------------------:"+geni_node_counter+++":-----------------------------");//xxxxxxxx
				// System.out.println(c3.nics);
		        String[] parts_slave_cip=a_slave.getControl_ip().toString().split(":");
		        String slave_cip= parts_slave_cip[2].toString();
		        //System.out.println("This is what we will call slave as:"+ slave_cip);
		        a_slave.setControl_ip(slave_cip);
			}
			// All operation successful
		}
		System.out.println("Adding Slaves:"+slaves);
		model.nodes.addAll(slaves);
		
		model.linked_env_type=linked_env_type;
		model.linked_env_name=linked_env_name;
		model.slice_name=slice_name;
	}
}
