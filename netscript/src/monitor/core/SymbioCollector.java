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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author Miguel Erazol Erazo
 *
 */
abstract public class SymbioCollector {
	protected class GetData extends Thread
	{
		SymbioEDSSIntf coordinator;
		Socket connectionSocket;
		int threadCounter;
		BufferedReader inFromClient;
		
		GetData(SymbioEDSSIntf coordinator, Socket connectionSocket, int threadCounter)
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
					if(clientSentence.contains("<uid>4<") || clientSentence.contains("<uid>37<")) {
					System.out.println("First Received: " + clientSentence + " in thread " + threadCounter + " from:" + 
							connectionSocket.getInetAddress().toString());
//XXX					this.coordinator.getBytesTransmitted(Integer.parseInt(clientSentence), connectionSocket.getInetAddress().toString());
					this.coordinator.getDataFromSystems(clientSentence, connectionSocket.getInetAddress().toString());
					}
				}
				
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
	    }
	}
	
	abstract boolean start() throws InterruptedException;
}
