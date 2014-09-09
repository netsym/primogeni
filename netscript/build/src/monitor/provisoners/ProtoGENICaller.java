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


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

import monitor.core.IExpListenter;

/**
 * @author Miguel Erazo
 *
 */
public class ProtoGENICaller {
	private final IExpListenter listener;
	//private static IExpListenter statListener = null;
	private String myPassphrase = "";
	private String privateKey = "";
	private String primoPath = "";
	private String myCredential = "";
	private static String pythonPath = "";
	private String sliceName = "";
	private static String sliceURN = "";
	private String myXMLRPCServer = "";
	private String myXMLRPCServerDomain = "";
	
	ProtoGENICaller(IExpListenter listener, String pass_phrase, String private_key, String primo_path, String slice_name) throws IOException
	{
		this.listener = listener;
		//statListener = listener;
		myPassphrase = pass_phrase;
		privateKey = private_key;
		primoPath = primo_path;
		myCredential = "";
		sliceName = slice_name;
		getPythonPath();
		getServerDomain();
		//getCredential();
	}
	
	private void getServerDomain() throws IOException
	{
		String command = "";
		String cmd_output = "";
		String home_dir = System.getProperty("user.home");
		
		command = pythonPath + " " + primoPath + "get_issuer.py -c " + privateKey + " -o " + primoPath + "out.txt";
		System.out.println("monitor.provisioners/protoGENICaller.java: Command = "+command);
		this.listener.println("GETSERVER CMD:" + command);
		cmd_output = execCmd(command, this.listener);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists()){
	    		this.listener.println("Could not get the server that issued this certificate, trying ~/.protogeni-config file...:\n\t" + cmd_output);
	    		//this.listener.println("home dir=" + home_dir);
	    		String xmlrpc_server = ProtoGeni.readFromFile(home_dir + "/.protogeni-config.py");
	    		//this.listener.println("xmlrpc server=" + xmlrpc_server);
	    		String[] ref = xmlrpc_server.split(":");
	    		//this.listener.println(ref[1].split("\"")[1]);
	    		//this.listener.println(ref[1].split("\"")[1].indexOf("."));
	    		//this.listener.println(ref[1].split("\"")[1].substring(ref[1].split("\"")[1].indexOf(".") + 1));
	    		myXMLRPCServer = ref[1].split("\"")[1].substring(ref[1].split("\"")[1].indexOf(".") + 1);
	    		this.listener.println("domain from proto:" + myXMLRPCServer);
	    	} else {
	    		myXMLRPCServer = ProtoGeni.readFromFile(primoPath + "out.txt");
	    		this.listener.println("GOT server:" + myXMLRPCServer);
	    		//Write this server tp file so that the script can use it
	    		if(myXMLRPCServer.equals("boss.emulab.net")){
	    			//Solve Emulab's host name mismatch errors
	    			ProtoGeni.writeToFile(home_dir + "/.protogeni-config.py", "XMLRPC_SERVER = { \"default\" : \"www.emulab.net\"}");
	    		} else {
	    			ProtoGeni.writeToFile(home_dir + "/.protogeni-config.py", "XMLRPC_SERVER = { \"default\" : \"" + myXMLRPCServer + "\"}");
	    		}
	    	}
	    	myXMLRPCServerDomain = myXMLRPCServer.substring(myXMLRPCServer.indexOf(".") + 1);
    		this.listener.println("GOT domain:" + myXMLRPCServerDomain);
		}
		if(myXMLRPCServer.equals("") || myXMLRPCServerDomain.equals("")) {
			this.listener.println("Cannot extract the XMLRPC Server from your certificate.\n" +
					"Please download your certificate again and configure the ~/.protogeni-config file correctly");
			throw new RuntimeException("Cannot extract the XMLRPC Server from your certificate");
		}
	}
	
	private void getPythonPath() throws IOException
	{
		int return_code = 0;
		StringBuilder python_path = new StringBuilder();
		
		//Check where python is installed
		this.listener.println("getting python installation path");
		return_code = executeProcess("which", "python", "", python_path, this.listener);
		if(return_code != 0)
			this.listener.println("\tProblem with python:" + python_path + "\nretrun code=" + return_code);
		else {
			this.listener.println("\tcmd output:" + python_path);
			pythonPath = python_path.toString();
		}
	}
	
	public static String deleteSlice(String slice_name, String passphrase, String certificate, 
			String primo_path, IExpListenter listener) throws IOException
	{
		int return_code = 0;
		StringBuilder python_path = new StringBuilder();
		String python_p = "";
		
		//Check where python is installed
		listener.println("getting python installation path");
		return_code = executeProcess("which", "python", "", python_path, listener);
		if(return_code != 0)
			listener.println("\tProblem with python:" + python_path + "\nretrun code=" + return_code);
		else {
			listener.println("\tcmd output:" + python_path);
			python_p = python_path.toString();
		}
		//listener.println("\n**ERASE**\n python path:" + python_p + "\n**ERASE**\n");
		//**************************
		
		System.out.println("slice=" + slice_name + " passphrase=" + passphrase + " certificate" + certificate);
		listener.println("slice=" + slice_name + " passphrase=" + passphrase + " certificate" + certificate);
		//**********
		//Get server domain
		String command = "";
		String cmd_output = "";
		String home_dir = System.getProperty("user.home");
		String my_XMLRPC_Server = "";
		String my_XMLRPC_Server_Domain = "";
		
		command = python_p + " " + primo_path + "get_issuer.py -c " + certificate + " -o " + primo_path + "out.txt";
		listener.println("GETSERVER CMD:" + command);
		System.out.println("GETSERVER CMD:" + command);
		cmd_output = execCmd(command, listener);
		{
	    	//Check if the output is available
	    	File f = new File(primo_path + "out.txt");
	    	if(!f.exists()){
	    		listener.println("Could not get the server that issued this certificate, trying ~/.protogeni-config file...:\n\t" + cmd_output);
	    		//this.listener.println("home dir=" + home_dir);
	    		String xmlrpc_server = ProtoGeni.readFromFile(home_dir + "/.protogeni-config.py");
	    		//this.listener.println("xmlrpc server=" + xmlrpc_server);
	    		String[] ref = xmlrpc_server.split(":");
	    		//this.listener.println(ref[1].split("\"")[1]);
	    		//this.listener.println(ref[1].split("\"")[1].indexOf("."));
	    		//this.listener.println(ref[1].split("\"")[1].substring(ref[1].split("\"")[1].indexOf(".") + 1));
	    		my_XMLRPC_Server = ref[1].split("\"")[1].substring(ref[1].split("\"")[1].indexOf(".") + 1);
	    		listener.println("domain from proto:" + my_XMLRPC_Server);
	    	} else {
	    		my_XMLRPC_Server = ProtoGeni.readFromFile(primo_path + "out.txt");
	    		listener.println("server to delete the slice from:" + my_XMLRPC_Server);
	    		//Write this server tp file so that the script can use it
	    		if(my_XMLRPC_Server.equals("boss.emulab.net")){
	    			//Solve Emulab's host name mismatch errors
	    			ProtoGeni.writeToFile(home_dir + "/.protogeni-config.py", "XMLRPC_SERVER = { \"default\" : \"www.emulab.net\"}");
	    		} else {
	    			ProtoGeni.writeToFile(home_dir + "/.protogeni-config.py", "XMLRPC_SERVER = { \"default\" : \"" + my_XMLRPC_Server + "\"}");
	    		}
	    	}
	    	my_XMLRPC_Server_Domain = my_XMLRPC_Server.substring(my_XMLRPC_Server.indexOf(".") + 1);
	    	listener.println("GOT domain:" + my_XMLRPC_Server_Domain);
		}
		if(my_XMLRPC_Server.equals("") || my_XMLRPC_Server_Domain.equals("")) {
			listener.println("Cannot extract the XMLRPC Server from your certificate.\n" +
					"Please download your certificate again and configure the ~/.protogeni-config file correctly");
		}
		
		//***********
		int rval = 0;
		String slice_URN = "urn:publicid:IDN+" + my_XMLRPC_Server_Domain + "+slice+" + slice_name;
		
		command = python_p + " " + primo_path + "make_call.py -u cm -o DeleteSlice -n " + slice_name + " -b 1 -l " + slice_URN +
		" -f " +  certificate + " -w " + passphrase + " -i " + primo_path + "out.txt -e " + primo_path + "credential.txt";
		//listener.println("\n**ERASE**\n cmd to execute:" + command + "\n**ERASE**\n");
		//System.out.println("\n**ERASE**\n cmd to execute:" + command + "\n**ERASE**\n");
		cmd_output = execCmd(command, listener);
		StringBuilder output = new StringBuilder();
		{
			//Check if the output is available
			File f = new File(primo_path + "out.txt");
			if(!f.exists()){
				listener.println("\n---> Problem deleting slice");
				System.out.println("\n---> Problem deleting slice");
				return cmd_output;
			} else {
				rval = parseProtoOutput(primo_path + "out.txt", output);
			}
		}
		listener.println("rval=" + rval);
		if(rval == 0){
			listener.println("Slice " + slice_name + " obtained successfully");
		} else  {
			listener.println("\n---> Problem deleting slice");
			System.out.println("\n---> Problem deleting slice");
			return cmd_output + "\nReponse from Protogeni:\n" + output;
		}
		
		//**********
		return "";
	}
	
	//Get the credential to later make xmlrpc-calls
	public String getCredential() throws IOException
	{
		//Get credential
		try {
			int rval = 0;
			//Erase previous history
			String command = "rm " + primoPath + "out.txt";
			String cmd_output = execCmd(command, this.listener);
		    this.listener.println("erasing cmd:" + cmd_output);
			
			command = pythonPath + " " + primoPath + "make_call.py -u sa -o GetCredential" +
	        		" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt";
	        cmd_output = execCmd(command, this.listener);
	        StringBuilder crd = new StringBuilder();
	        {
	        	//Check if the output is available
	        	File f = new File(primoPath + "out.txt");
	        	if(!f.exists()){
	        		this.listener.println("\n---> Problem getting credential, please check your certificate and passphrase");
	        		System.out.println("\n---> Problem getting credential, please check your certificate and passphrase");
	        		return cmd_output;
	        	} else {
	        		rval = parseProtoOutput(primoPath + "out.txt", crd);
	        	}
	        }
	        this.listener.println("rval=" + rval);
	        if(rval == 0){
	        	this.listener.println("Credential obtained successfully");
	        	myCredential = crd.toString();
	        	ProtoGeni.writeToFile(primoPath + "credential.txt", myCredential);
	        	this.listener.println("myCredential=" + myCredential);
	        } else  {
	        	this.listener.println("\n---> Problem getting credential, please check your certificate and passphrase");
        		System.out.println("\n---> Problem getting credential, please check your certificate and passphrase");
	        	return cmd_output + "\nReponse from Protogeni:\n" + crd;
	        }
	        
            listener.println("\n---> Got credential");
            System.out.println("\n---> Got credential");
        } catch(Exception e) {
        	System.err.println(e.toString());
        	throw new RuntimeException();
        }
        return "";
	}
	
	public String registerSlice() throws IOException
	{
		//int return_code = 0;
		int rval = 0;
		StringBuilder response = new StringBuilder();
		String valid_time = "";
		String cmd_output = "";
		String command = "";
		
		if(myCredential.equals("")){
			this.listener.println("Invalid credential...doing one more attempt...");
			getCredential();
			if(myCredential.equals("")){
				this.listener.println("Could not get credential, check your certificate");
				//Runtime.getRuntime().halt(1);
			}
		}
		//Check if there is a slice with the same name
		command = pythonPath + " " + primoPath + "make_call.py -u sa -o Resolve -t Slice -n " + sliceName +
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -e " + primoPath + "credential.txt";
		cmd_output = execCmd(command, this.listener);
		{
        	//Check if the output is available
        	File f = new File(primoPath + "out.txt");
        	if(!f.exists()){
        		this.listener.println("\n---> Problem registering slice");
        		System.out.println("\n---> Problem registering slice");
        		return cmd_output;
        	} else
        		rval = parseProtoOutput(primoPath + "out.txt", response);
        }
		
		if(rval == 0){
        	this.listener.println("\n---> This slice already exists, please choose another name for your experiment");
    		System.out.println("\n---> This slice already exists, please choose another name for your experiment");
    		return cmd_output + "\nProtoGENI's return node: " + rval + "\nResponse from Protogeni:\n" + response;
        } else  {
        	this.listener.println("\n---> This slice does not exist, registering the slice...");
        	System.out.println("\n---> This slice does not exist, registering the slice " + sliceName + "...");
        }
        
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH);
        System.out.println("before: year=" + year + " month=" + month);
        if(month==12){
        	month = 1;
        	year += 1;
        	valid_time = year + "0" + month + "18T12:45:11";
        } else {
        	month += 2;
        	if(month<10){
        		valid_time = year + "0" + month + "18T12:45:11";
        	} else {
        		valid_time = year + "" + month + "18T12:45:11";
        	}
        }
        System.out.println("after: year=" + year + " month=" + month);
        //valid_time = "20110618T12:45:11";
        System.out.println("************");
        System.out.println("******Expiration date:" + valid_time);
        System.out.println("************");
        
        command = pythonPath + " " + primoPath + "make_call.py -u sa -o Register -t Slice -n " + sliceName + " -v " + valid_time +
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -e " + primoPath + "credential.txt";
		cmd_output = execCmd(command, this.listener);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists()){
	    		this.listener.println("Problem registering slice:\n\t" + cmd_output);
	    		//Runtime.getRuntime().halt(1);
	    	} else
	    		rval = parseProtoOutput(primoPath + "out.txt", response);
	    }
		
		if(rval == 0){
			System.out.println("\n---> The slice has been registered sucessfully");
	    	this.listener.println("\n---> The slice has been registered sucessfully");
	    } else  {
	    	this.listener.println("\n---> A problem has occured registering slice");
    		System.out.println("\n---> A problem has occured registering slice");
    		return cmd_output + "\nProtoGENI's return node: " + rval + "\nResponse from Protogeni:\n" + response;
	    }
		return "";
	}
	
	public String getTicket(String rspec_file) throws IOException
	{
		//Get the domain from the protogeni-config file
		String command = "";
		String cmd_output = "";
		int rval = 0;
		StringBuilder my_slice = new StringBuilder();
		StringBuilder my_slice_credential = new StringBuilder();
		
		String slice_urn = "urn:publicid:IDN+" + this.myXMLRPCServerDomain + "+slice+" + sliceName;
		sliceURN = slice_urn;
		this.listener.println("sliceurn=" + slice_urn);
		
		//Lookup slice
		command = pythonPath + " " + primoPath + "make_call.py -u sa -o Resolve -t Slice -l " + slice_urn +
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -e " + primoPath + "credential.txt";
		cmd_output = execCmd(command, this.listener);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists()){
	    		this.listener.println("\n---> Problem resolving slice credential");
        		System.out.println("\n---> Problem resolving slice credential");
        		return cmd_output;
	    	} else {
	    		rval = parseProtoOutput(primoPath + "out.txt", my_slice);
	    		//this.listener.println("---Slice was found sucessfully:\n\t" + my_slice.toString());
	    	}
	    }
		
		//Get slice credential and getticket
		//XXX possibly the slice_urn is passed wrongly here
		command = pythonPath + " " + primoPath + "make_call.py -u sa -o GetCredential -t Slice -l " + slice_urn +
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -e " + primoPath + "credential.txt -q 1" +
			" -g " + rspec_file;
		cmd_output = execCmd(command, this.listener);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists()){
	    		this.listener.println("\n---> Problem resolving slice credential");
        		System.out.println("\n---> Problem resolving slice credential");
        		return cmd_output;
	    	} else {
	    		rval = parseProtoOutput(primoPath + "out.txt", my_slice_credential);
	    		//this.listener.println("---Slice credential obtained successfully:\n\t" + my_slice.toString());
	    	}
	    }
		if(rval == 0){
			//this.listener.println("---Slice credential obtained successfully:\n\t" + my_slice_credential.toString());
			ProtoGeni.writeToFile(primoPath + "slicecredential.txt", my_slice_credential.toString());
			System.out.println("\n---> Got ticket successfully");
	    	this.listener.println("\n---> Got ticket successfully");
        } else  {
        	this.listener.println("\n---> A problem has occured getting ticket");
    		System.out.println("\n---> A problem has occured getting ticket");
    		return cmd_output + "\nProtoGENI's return node: " + rval + "\nResponse from Protogeni:\n" + my_slice_credential;
        }
		
		//Get the ticket
		command = pythonPath + " " + primoPath + "make_call.py -u cm -o GetTicket -k " + slice_urn + " -g " + rspec_file + " -b 0" + 
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -j " + primoPath + "slicecredential.txt";
		//this.listener.println("LAST COMMAND=" + command);
/*		cmd_output = execCmd(command);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists()){
	    		this.listener.println("Problem getting ticket:\n\t" + cmd_output);
	    		Runtime.getRuntime().halt(1);
	    	} else {
	    		rval = parseProtoOutput(primoPath + "out.txt", my_ticket);
	    		//this.listener.println("---Slice credential obtained successfully:\n\t" + my_slice.toString());
	    	}
	    }
		if(rval == 0){
			this.listener.println("---Ticket obtained successfully:\n\t" + my_ticket.toString());
	    } else  {
	    	this.listener.println("[code=" + rval + "]" + " Problem getting ticket:\n\t" + cmd_output);
	    	Runtime.getRuntime().halt(1);
	    }*/
		return "";
	}
	
	public String redeemTicket(StringBuilder manifest) throws IOException
	{
		String command = "";
		String cmd_output = "";
		int rval = 0;
		//StringBuilder manifest = new StringBuilder();
		
		command = pythonPath + " " + primoPath + "make_call.py -u sa -o Resolve -t Slice -n " + this.sliceName + " -x 1 -l " + sliceURN + 
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -e " + primoPath + "credential.txt &";
		this.listener.println("REDEEM CMD:" + command);
		this.listener.println("got out!:" + command);
		cmd_output = execCmd(command, this.listener);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists()){
	    		this.listener.println("\n---> Problem redeeming ticket");
        		System.out.println("\n---> Problem redeeming ticket");
        		return cmd_output;
	    	} else {
	    		rval = parseProtoOutput(primoPath + "out.txt", manifest);
	    		//this.listener.println("---Slice credential obtained successfully:\n\t" + my_slice.toString());
	    	}
		}
		if(rval == 0){
			this.listener.println("\n---> Ticket redeemed successfully");
			System.out.println("\n---> Ticket redeemed successfully");
			//ProtoGeni.writeToFile(primoPath + "slicecredential.txt", my_slice_credential.toString());
	    } else  {
	    	this.listener.println("\n---> Problem redeeming ticket");
    		System.out.println("\n---> Problem redeeming ticket");
    		return cmd_output + "\nProtoGENI's return node: " + rval + "\nResponse from Protogeni:\n" + manifest;
	    }
		//return manifest.toString();
		return "";
	}
	
	public String StartSliver() throws IOException
	{
		String command = "";
		String cmd_output = "";
		StringBuilder response = new StringBuilder();
		int rval = 0;
		
		command = pythonPath + " " + primoPath + "make_call.py -u sa -o Resolve -t Slice -n " + this.sliceName + " -y 1 -l " + sliceURN + 
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -e " + primoPath + "credential.txt";
		this.listener.println("START CMD:" + command);
		cmd_output = execCmd(command, this.listener);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists()){
	    		this.listener.println("\n---> Problem starting slice");
        		System.out.println("\n---> Problem starting slice");
        		return cmd_output;
	    	} else {
	    		//this.listener.println("---Slice credential obtained successfully:\n\t" + my_slice.toString());
	    		rval = parseProtoOutput(primoPath + "out.txt", response);
	    	}
		}
		if(rval == 0){
			this.listener.println("\n---> Slice started successfully");
			System.out.println("\n---> Slice started successfully");
			//ProtoGeni.writeToFile(primoPath + "slicecredential.txt", my_slice_credential.toString());
	    } else  {
	    	this.listener.println("\n---> Problem starting slice");
    		System.out.println("\n---> Problem starting slice");
    		return cmd_output + "\nProtoGENI's return node: " + rval + "\nResponse from Protogeni:\n" + response;
	    }
		return "";
	}
	
	public String pollStatus() throws IOException
	{
		String command = "";
		String cmd_output = "";
		StringBuilder sliver_status = new StringBuilder();
		int rval = 0;
		
		command = pythonPath + " " + primoPath + "make_call.py -u sa -o SliverStatus -t Slice -n " + this.sliceName + " -z 1 -l " + sliceURN + 
			" -f " +  privateKey + " -w " + myPassphrase + " -i " + primoPath + "out.txt -e " + primoPath + "credential.txt";
		//this.listener.println("POLL CMD:" + command);
		cmd_output = execCmd(command, this.listener);
		{
	    	//Check if the output is available
	    	File f = new File(primoPath + "out.txt");
	    	if(!f.exists())
	    	{
	    		if(cmd_output.indexOf("is busy") > 0){
	    			this.listener.println("\tXMLRPC server is busy, let's try again...");
	    		} else {
	    			this.listener.println("Problem querying slice status:\n\t" + cmd_output);
	    		}
	    	} else {
	    		rval = parseProtoOutput(primoPath + "out.txt", sliver_status);
	    	}
		}
		if(rval == 0){
			this.listener.println("---Slice status queried successfully");
			//ProtoGeni.writeToFile(primoPath + "slicecredential.txt", my_slice_credential.toString());
	    } else  {
	    	this.listener.println("[code=" + rval + "]" + " Problem querying slice status:\n\t" + cmd_output);
	    	//Runtime.getRuntime().halt(1);
	    }
		return sliver_status.toString();
	}
	
		
	private static int parseProtoOutput(String out_file, StringBuilder container)
	{
		//System.out.println("out_file=" + out_file); System.err.flush();
		
		String out = ProtoGeni.readFromFile(out_file);
		//this.listener.println("PARSING FILE 1 out=" + out_file + " content=" + out);
		String[] ans = out.split("END");
		container.append(ans[1]);
		
		return Integer.parseInt(ans[0]);
	}
	
	private static String execCmd(String cmd, IExpListenter listener) throws IOException
	{
		listener.println("executing:" + cmd);
		System.out.println("executing:" + cmd);
		Process p = Runtime.getRuntime().exec(cmd);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ProtoGeni.printProcessOutput(listener, p, 10, null);
	}
	
	private static int executeProcess(String cmd, String file, String params, StringBuilder output, IExpListenter listener) throws IOException
	{
		listener.println("EXECUTING:" + cmd + " " + file + " " + params);
		ProcessBuilder pb;
		//cmd = "sh";
		//params = "/primoGENI/newscripts/sh_caller.sh";
		//String params2 = "3 4";
		if(params.equals("")){
			//this.listener.println("	params null");
			pb = new ProcessBuilder(cmd, file);
		}
		else {
			//this.listener.println("	params NOT null");
			pb = new ProcessBuilder(cmd, file, params);
		}
		
		//pb.directory(new File(Utils.TMP_DIR));
		pb.redirectErrorStream(true);
		Process proc = pb.start();

		java.io.InputStream is = proc.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String line;
		int exit = -1;

		while ((line = br.readLine()) != null) {
		    // Outputs your process execution
			listener.println("-" + line + "-");
		    if(!line.trim().equalsIgnoreCase(""))
		    	output.append(line);
		    try {
		        exit = proc.exitValue();
		        if (exit == 0)  {
		            // Process finished
		        	listener.println("	SUCCESS!");
		        	return 0;
		        }
		        if(exit != 0) {
		        	listener.println("	ERROR!");
		        	return exit;
		        }
		    } catch (IllegalThreadStateException t) {
		        // The process has not yet finished. 
		        // Should we stop it?
		       // if (processMustStop())
		            // processMustStop can return true 
		            // after time out, for example.
		         //   proc.destroy();
		    }
		}
		return 0;
	}
	
	public String getXMLRPCServer(){
		return myXMLRPCServer;
	}
	
	public String getXMLRPCServerDomain(){
		return myXMLRPCServerDomain;
	}	
}
