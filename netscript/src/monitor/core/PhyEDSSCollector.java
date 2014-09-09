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
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Miguel Erazol Erazo
 *
 */
public class PhyEDSSCollector extends SymbioCollector
{
	/*private class GetTrafficData extends Thread
	{
		PhyEDSS coordinator;
		Socket connectionSocket;
		int threadCounter;
		BufferedReader inFromClient;
		
		GetTrafficData(PhyEDSS coordinator, Socket connectionSocket, int threadCounter)
		{
			this.coordinator = coordinator;
			this.connectionSocket = connectionSocket;
			this.threadCounter = threadCounter;
			try {
				this.inFromClient = new BufferedReader(new InputStreamReader(this.connectionSocket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public void run() {
	        while (true){
	        	String clientSentence = null;
				try {
					clientSentence = inFromClient.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(!(clientSentence==null)) {
					System.out.println("Received: " + clientSentence + " in thread " + threadCounter + " from:" + 
							connectionSocket.getInetAddress().toString());
					this.coordinator.getBytesTransmitted(Integer.parseInt(clientSentence), connectionSocket.getInetAddress().toString());
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    }
	}*/

	PhyEDSS coordinator;
	
	PhyEDSSCollector(PhyEDSS coordinator)
	{
		this.coordinator = coordinator;
	}
	
	public boolean start() throws InterruptedException
	{	
		try {
			listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	// Start listening for traffic-related data coming from apps
	private void listen() throws IOException, InterruptedException
	{
		//String clientSentence;
        //String capitalizedSentence;
        ServerSocket socket;
        int threadCounter = 0;
        
		try {
			socket = new ServerSocket(6789);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not create server socket");
		}

        while(true)
        {
        	Socket connectionSocket;
        	System.out.println("listening...");
			try {
				connectionSocket = socket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not accept connection");
			}
			GetData listener = new GetData(this.coordinator, connectionSocket, threadCounter++);
			listener.start();
        }
	}
}
