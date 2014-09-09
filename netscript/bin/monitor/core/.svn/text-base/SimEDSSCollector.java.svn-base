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

public class SimEDSSCollector extends SymbioCollector{
	
	SymbioEDSSIntf coordinator;
	
	SimEDSSCollector(SymbioEDSSIntf coord){
		coordinator = coord;
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
	
	private void listen() throws IOException, InterruptedException
	{
		//String clientSentence;
        //String capitalizedSentence;
        ServerSocket socket;
        int threadCounter = 0;
        
		try {
			socket = new ServerSocket(10001);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not create server socket");
		}

        while(true)
        {
        	Socket connectionSocket;
        	System.out.println("listening...");
			try {
				//We will need threads for a distributed simulation
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
