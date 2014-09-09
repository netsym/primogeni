package jprime.models;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import jprime.Experiment;
import jprime.IModelNode;
import jprime.StatusListener;
import jprime.TCPTraffic.ITCPTraffic;
import jprime.Host.IHost;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.PingTraffic.IPingTraffic;
import jprime.Router.IRouter;
import jprime.Traffic.ITraffic;
import jprime.database.Database;
import jprime.partitioning.Partitioning;
import jprime.util.ComputeNode;
import jprime.util.Portal;
import jprime.util.XMLModelBuilder;
import jprime.visitors.TLVVisitor;

/**
 * @author Nathanael Van Vorst
 *
 */
public class BriteTest {
	/**
	 * 
	 */
	private static void usage() {
		System.err.println("Usage: BriteTest <xml file> <expname> [ping | http]");
		jprime.Console.halt(1);
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws GeneralSecurityException 
	 */
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		if(args.length != 3) usage();
		boolean doping = false;
		if(args[2].compareToIgnoreCase("ping")==0) {
			doping=true;
		}
		else if(args[2].compareToIgnoreCase("http")==0) {
			doping=false;
		}
		else {
			System.out.println("Invalid traffic type! "+args[2]);
		}
		System.out.println("File="+args[0]+", name="+args[1]+", traffic="+args[2]);
		Database db = Database.createDatabase();
		XMLModelBuilder xml = new XMLModelBuilder(db.createExperiment(args[1],true),args[0]);
		xml.createModel();
		Experiment exp = xml.getExp();
		

		int router_count=0, net_count=0, routing_sphere_count=0, link_count=0;
		ArrayList<IModelNode> nodes = new ArrayList<IModelNode>();
		ArrayList<IHost> hosts_orig = new ArrayList<IHost>();
		INet top = exp.getRootNode();
		nodes.add(top);
		//find all hosts
		while(nodes.size()>0) {
			IModelNode n = nodes.remove(0);
			if(n instanceof INet) {
				nodes.addAll(n.getAllChildren());
				if(((INet) n).getStaticRoutingProtocol() != null)
					routing_sphere_count++;
				net_count++;
			}
			else if(n instanceof IRouter) {
				router_count++;
			}
			else if (n instanceof IHost) {
				hosts_orig.add((IHost)n);
			}
			else if(n instanceof ILink) {
				link_count++;
			}
		}
		ITraffic traffic = top.createTraffic();
		System.out.println("Hosts="+hosts_orig.size()
				+", routers="+router_count
				+", nets="+net_count
				+", routing spheres="+routing_sphere_count
				+", link_count="+link_count);
		
		System.out.println("Adding traffic");
		
		int max_flows=doping?hosts_orig.size():hosts_orig.size()/2;
		int t=0;
		
		while(max_flows>0) {
			ArrayList<IHost> hosts = new ArrayList<IHost>();
			hosts.addAll(hosts_orig);
			Collections.shuffle(hosts);
			while(hosts.size()>0 && max_flows>0) {
				IHost src = hosts.remove(0);
				String srcstr=".:"+src.getUniqueName().toString().substring(top.getUniqueName().toString().length()+1);
				IHost dst = hosts.remove(0);
				String dststr=".:"+dst.getUniqueName().toString().substring(top.getUniqueName().toString().length()+1);
				if(doping) {
					IPingTraffic ping = traffic.createPingTraffic();
					ping.setCount(1);
					ping.setStartTime(t+1);
					ping.setInterval("1");
					ping.setSrcs(srcstr);
					ping.setDsts(dststr);
					ping.setIntervalExponential("false");
				}
				else {
					ITCPTraffic http = traffic.createTCPTraffic();
					http.setStartTime("0.1");
					http.setIntervalExponential("false");
					http.setFileSize("1000000");
					http.setConnectionsPerSession(1);
					http.setNumberOfSessions(1);
					http.setSrcs(srcstr);
					http.setDsts(dststr);
				}
				t++;
				max_flows--;
				//System.out.println("["+srcs.size()+"]["+max_flows+"]Create traffic between "+srcstr+" and "+dststr);
			}
		}
		System.out.println("AAA Created "+t+" pings/flows");
		
		System.out.println("Compiling");
		exp.compile(new StatusListener());
		Partitioning partitioning = exp.partition("1::1:1", new HashSet<Portal>(), new ArrayList<ComputeNode>());
		
		System.out.println("\t***************************************");
		System.out.println("\tCreating TLV");
		System.out.println("\t***************************************");
		new TLVVisitor(exp.getMetadata(), partitioning, "./", exp.getName(),new HashMap<Integer, String>(),true);
	}
}
