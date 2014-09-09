package jprime.util;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jprime.Experiment;
import jprime.EmulationProtocol.IEmulationProtocol;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.OpenVPNEmulation.IOpenVPNEmulation;

/**
 * create the vpn config file
 * @author Nathanael Van Vorst
 */
public class CreateVPNConfig {
	private static final String VPN_DATA_NAME = "vpn_info.txt";
	private static final String VPN_SCRIPT = "vpnscript.pl";
	protected final String results_dir;
	protected final String vpngateway;
	protected final String openvpn_dir;
	public CreateVPNConfig(String results_dir, String vpngateway, String openvpn_dir) {
		this.results_dir = results_dir;
		this.vpngateway = vpngateway;
		this.openvpn_dir=openvpn_dir;
	}

	public boolean createVPNConfig(final Experiment exp) throws IOException {
		HashMap<Long,List<IInterface>> nics = new HashMap<Long, List<IInterface>>();
		for(IEmulationProtocol e : exp.getEmuProtocols()) {
			if(e instanceof IOpenVPNEmulation) {
				final IInterface iface = (IInterface)e.getParent();
				final IHost h= (IHost)iface.getParent();
				if(!nics.containsKey(h.getUID())) {
					nics.put(h.getUID(),new LinkedList<IInterface>());
				}
				nics.get(h.getUID()).add(iface);
			}
		}
		return _createVPNConfig(exp,nics);
	}
	public boolean createVPNConfig(final Experiment exp, final Map<Long, IHost> hosts) throws IOException {
		HashMap<Long,List<IInterface>> nics = new HashMap<Long, List<IInterface>>();
		for(IHost h : hosts.values()) {
			for(IInterface iface : h.getNics()) {
				if(!nics.containsKey(h.getUID())) {
					nics.put(h.getUID(),new LinkedList<IInterface>());
				}
				nics.get(h.getUID()).add(iface);
			}
		}
		return _createVPNConfig(exp,nics);
	}
	private boolean _createVPNConfig(final Experiment exp, Map<Long,List<IInterface>> hosts) throws IOException {
		if(hosts.size()>0) {
			BufferedWriter out = new BufferedWriter(new FileWriter(results_dir+"/"+VPN_DATA_NAME));
			//hack for openvpn.... apparently it doesn't like 255.255.240.0 (which is  valid!)
			String[] ack = exp.getRootNode().getIpPrefix().toString().split("\\.");
			String subnet = ack[0]+"."+ack[1]+".0.0/16";
			out.write("subnet "+IPAddressUtil.int2IP(IPAddressUtil.cidr2Int(subnet))+" "+IPAddressUtil.cidr2Subnet(subnet)+"\n");
			out.write("server "+vpngateway+"\n");
			for( List<IInterface> c : hosts.values()) {
				out.write("client");
				for(IInterface i : c) {
					out.write(" "+i.getIpAddress());
				}
				out.write("\n");
			}
			out.close();
			return true;
		}
		return false;
	}

	public File generateServerAndClientConfigs() {
		File temp = new File(results_dir+"/"+vpngateway+".tgz");
		if(temp.exists()) {
			jprime.Console.out.println("Using cached vpn configs!");
			//use the cached version
			return temp;
		}
		final File config = new File(results_dir+"/"+VPN_DATA_NAME);
		if(config.exists()) {
			jprime.Console.out.println("Generating new vpn configs!");
			if(getScript(this.results_dir)) {
				final File script = new File(results_dir+"/"+VPN_SCRIPT);
				if(!script.exists())
					throw new RuntimeException("wtf?");
				//we have the script and the config file....
				//1) generate the files we need
				//2) cleanup
				try {
					jprime.Console.out.print("Generating vpn into at "+results_dir);
					
					ProcessBuilder pb = new ProcessBuilder("perl", VPN_SCRIPT, "-d", openvpn_dir, VPN_DATA_NAME);
					pb.directory(new File(results_dir));
					pb.redirectErrorStream(true);
					Process p = pb.start();
					InputStream out = p.getInputStream();
					while(true) {
						try {
							p.exitValue();
							break;
						} catch (IllegalThreadStateException e) {
							byte b[] = new byte[out.available()];
							int r = out.read(b);
							if(r<b.length)
							b[r]='\0';
							jprime.Console.out.print(new String(b));
							try {
								Thread.sleep(50);
							} catch (InterruptedException e1) {
							}
						}
					}
					{
						byte b[] = new byte[out.available()];
						int r = out.read(b);
						if(r<b.length)
						b[r]='\0';
						jprime.Console.out.print(new String(b));
					}
					jprime.Console.out.print("\n");
					pb = new ProcessBuilder("perl", VPN_SCRIPT, "-c");
					pb.directory(new File(results_dir));
					p = pb.start();
					while(true) {
						try {
							p.waitFor();
							break;
						} catch (InterruptedException e) {
						}
					}
					script.delete();
					File server_tar = new File(results_dir+"/"+vpngateway+".tgz");
					if(server_tar.exists()) {
						return server_tar;
					}
					jprime.Console.out.println(server_tar.getAbsolutePath()+" does not exist!");
					throw new RuntimeException("Error generating vpn config files!");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			throw new RuntimeException("wtf?");
		}
		throw new RuntimeException(config.getAbsolutePath()+" does not exist!");
	}


	private boolean getScript(String results_dir) { 
		File script=null;
		final Class<?> c = CreateVPNConfig.class;
		final URL location = c.getProtectionDomain().getCodeSource().getLocation();
		// get the class object for this class, and get the location of it
		try {
			//jprime.Console.out.println("Looking for vpn script in jar at "+location.getPath());
			ZipFile zf = new ZipFile(location.getPath());
			final String lib="jprime/util/"+VPN_SCRIPT;
			// jars are just zip files, get the input stream for the lib
			//jprime.Console.out.println("Trying to load "+lib);
			ZipEntry entry = zf.getEntry(lib);
			if(entry != null) {
				//jprime.Console.out.println(lib+" is in the zip");
				InputStream in = zf.getInputStream(entry);

				// create a temp file and an input stream for it
				script = new File(results_dir+"/"+VPN_SCRIPT);
				FileOutputStream out = new FileOutputStream(script);
				// copy the lib to the temp file
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0)
					out.write(buf, 0, len);
				in.close();
				out.close();
			}
			else {
				throw new RuntimeException("can't find "+VPN_SCRIPT+"!");
			}
		} catch (IOException e) {
			File f = new File(location.getPath()+"/jprime/util/"+VPN_SCRIPT);
			//jprime.Console.out.println("Looking for vpn script in jar at "+f.getPath());
			if(f.exists()) {
				// create a temp file and an input stream for it
				script = new File(results_dir+"/"+VPN_SCRIPT);
				try {
					FileOutputStream out = new FileOutputStream(script);
					FileInputStream in = new FileInputStream(f);
					// copy the lib to the temp file
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0)
						out.write(buf, 0, len);
					in.close();
					out.close();
				} catch (FileNotFoundException e1) {
					script=null;
				} catch (IOException e1) {
					script=null;
				}
			}
		}
		if(script == null)
			throw new RuntimeException("can't find the "+VPN_SCRIPT+"!");
		return true;
	}

	public static void main(String[] args) {
		/*
		You need to make vpn_info.txt in the dir.....
		there is an example here:
		<-- start example --> 
		subnet 192.1.0.0 255.255.0.0
		server 10.10.1.1
		client 192.1.2.3 192.1.2.4
		client 192.1.5.4
		<-- end example -->
		
		the server in the file and the server in the command need to match
		 */
		CreateVPNConfig foo = new CreateVPNConfig(
				"/Users/vanvorst/Documents/workspace/primex/netscript/foo",
				"10.10.1.1",
				"/Users/vanvorst/Documents/workspace/primex/netsim/src/emuproxy/openvpn");
		foo.generateServerAndClientConfigs();
	}


}
