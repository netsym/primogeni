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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.OpenVPNEmulation.IOpenVPNEmulation;
import jprime.TrafficPortal.ITrafficPortal;
import jprime.partitioning.Alignment;
import jprime.util.PartTlvPair;


/**
 * @author Nathanael Van Vorst
 *
 */
public abstract class Deployer{
	protected final IExpListenter listener;
	protected final Provisioner provisioner;
	protected final PartTlvPair parting;
	protected final HashMap<Integer, ArrayList<IHost>> vmMap;
	protected final HashMap<Long, IHost> vpns;
	protected final LinkedList<String> portalRoutes;
	protected final String ipRange;
	protected final Experiment exp;
	protected Map<String,String> runtimeSymbols;

	public Deployer(final String ipRange, final IExpListenter listener, final Experiment exp, final PartTlvPair parting, final Provisioner provisioner, final Map<String,String> runtimeSymbols) {
		super();
		this.ipRange=ipRange;
		this.listener=listener;
		this.exp=exp;
		this.parting=parting;
		this.provisioner=provisioner;
		this.runtimeSymbols=runtimeSymbols;
		this.vmMap=new HashMap<Integer, ArrayList<IHost>>();
		this.vpns = new HashMap<Long,IHost>();
		this.portalRoutes=new LinkedList<String>();
		findEmulatedHosts(exp);
	}
	
	private void findEmulatedHosts(final Experiment exp) {
		//find the hosts...
		HashMap<Long, IHost> vms = new HashMap<Long,IHost>();
		for(IEmulationProtocol p : exp.getEmuProtocols()) {
			if(p instanceof ITrafficPortal) {
				IHost h = (IHost)p.getParent().getParent();
				for(IModelNode c : h.getAllChildren()) {
					try {
						if(c instanceof IInterface) {
							portalRoutes.addAll(((IInterface)c).getReachableNetworks());
						}
					} catch(Exception e) {}
				}
			}
			else if(p instanceof IOpenVPNEmulation) {
				IHost h = (IHost)p.getParent().getParent();
				vpns.put(h.getUID(),h);
			}
			else {
				IHost h = (IHost)p.getParent().getParent();
				vms.put(h.getUID(),h);
			}
		}
		//setup the machine map
		for(IHost h : vms.values()) {
			Set<Integer> pid = h.getAlignments(parting.partitioning);
			if (pid.size() != 1)
				throw new RuntimeException("Should never happen!");
			for(Integer i : pid) {
				Alignment a = parting.partitioning.findAlignment(i);
				if(a == null)
					throw new RuntimeException("Should never happen!");
				if(!vmMap.containsKey(a.getPartId())) {
					vmMap.put(a.getPartId(), new ArrayList<IHost>());
				}
				//if(Utils.DEBUG)System.out.println("\tAdding "+node.getUniqueName()+" to part "+a.getPartId());
				vmMap.get(a.getPartId()).add(h);
			}
		}
	}

	public IController deploy(String openvpndir)  throws GeneralSecurityException, IOException  {
		return deploy(null,5*1000L, openvpndir);
	}

	public IController deploy(final String keyStore, String openvpndir) throws GeneralSecurityException, IOException  {
		return deploy(keyStore,5*1000L, openvpndir);
	}

	abstract public IController deploy(final String keyStore, final long timeout, String openvpndir) throws GeneralSecurityException, IOException;

	abstract public void setupExperiment();

}
