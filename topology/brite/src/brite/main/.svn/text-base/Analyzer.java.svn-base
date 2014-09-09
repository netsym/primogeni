/****************************************************************************/
/*  Author:     Mathias Golombek                                            */
/*  Title:      Analyzing Tool for generated Graphs			    */
/*  Date:	4/30/2002                       		            */
/****************************************************************************/

package brite.main;


import java.io.*;
import java.util.*;

import brite.export.*;
import brite.graph.*;
import brite.importer.*;
import brite.model.*;
import brite.topology.*;
import brite.util.*;

/*
 * This class analyzes an !!!undirected!!! graph in Brite's internal graph representation.
 * It provides computing :
 *	-degree distribution		(-> computeDegreeDistribution() 			creates <name>.degrees  )
 *	-cluster coefficient  		(-> computeClusterCoefficient() 			creates <name>.cluster  )
 *	-hop distribution     		(-> computeHopDistributionAndCharacteristicPathLength() creates <name>.hops     )
 *	-characteristic path length  	(-> computeHopDistributionAndCharacteristicPathLength() creates <name>.charPath )
 */
final public class Analyzer {

	private Graph graph; // the analyzed graph
	private String name; // the name of the output files

	/**
	 * @param g
	 *            The graph that shall be analyzed
	 * @param name
	 *            This String represents the beginning of all files produced
	 *            with this analyzer
	 */
	public Analyzer(Graph g, String name) {
		graph = g;
		this.name = name;
		System.out.println("Analyzing the generated Network");
	}

	/**
	 * This method computes the degree distribution and writes this distribution
	 * into a file <outFileName>.degree
	 */
	public void computeDegreeDistribution() {
		System.out.println("Computing the degree-distribution");
		Node[] nodes = graph.getNodesArray();
		int distribution[] = new int[nodes.length];
		int degree = 0; // temporary Variable
		for (int i = 0; i < nodes.length; i++) {
			degree = nodes[i].getInDegree();
			distribution[degree]++;
		}
		FileWriter outFile;
		try {
			outFile = new FileWriter(name + ".degree");
			for (int i = 0; i < nodes.length; i++) {
				if (distribution[i] > 0) {
					// System.out.println(i+":"+distribution[i]);
					outFile.write(i + ":" + distribution[i] + "\n");
				}
			}
			outFile.close();
		} catch (Exception e) {
			System.out.println("Analyzer.computeDegreeDistribution: Error while writing!");
		}
	}

	/*
	 * This method computes the cluster coefficient which means the average of
	 * the connectivity between the direct neighbors of all nodes.
	 * Attention: this method appends the cluster coefficient value to the file
	 * <name>.cluster for statistical analyzing of more graphs
	 */
	public void computeClusterCoefficient() {

		System.out.println("Computing the cluster coefficient");
		Calendar startTime = new GregorianCalendar(); // for measuring the computing time of the method
		Node[] nodes = graph.getNodesArray(); // saves all nodes of the Graph
		final int NUMBER_OF_NODES = nodes.length; // the number of nodes in the Graph. 
												  // It is saved to a constant because it is often used.
		Hashtable allNeighbors = new Hashtable(); // this hashmap saves the neighbors for each node
		double counter = 0;

		// 1: save the neighbors of all nodes to save time later
		for (int i = 0; i < NUMBER_OF_NODES; i++) {
			Node[] neighborsOfNodeI = graph.getNeighborsOf(nodes[i]);
			LinkedList listOfNeighbors = new LinkedList();
			for (int k = 0; k < neighborsOfNodeI.length; k++) {
				listOfNeighbors.add(neighborsOfNodeI[k]);
			}
			allNeighbors.put(nodes[i], listOfNeighbors);
		}

		// 2: for all nodes take the neighbors and count the links between them
		for (int i = 0; i < NUMBER_OF_NODES; i++) {
			Node actualNode = nodes[i];
			LinkedList actualNeighbors = (LinkedList) allNeighbors
					.get(actualNode);
			int countEdgesBetweenNeighbors = 0;

			for (int m = 0; m < actualNeighbors.size(); m++) {
				Node tmpNeighbor = (Node) actualNeighbors.get(m);
				LinkedList tmpList = (LinkedList) allNeighbors.get(tmpNeighbor);
				for (int n = 0; n < actualNeighbors.size(); n++) {
					if (tmpList.contains(actualNeighbors.get(n))
							&& (actualNeighbors.get(n) != actualNode)) {
						countEdgesBetweenNeighbors++;
					}
				}
			}

			/*
			 * computing the percentage of the possible amount of edges between
			 * the neighbors therefore I have to compute a factor which is the
			 * maximum number of edges between the neighbors (= k * (k-1))
			 */
			double factor = actualNeighbors.size()
					* (actualNeighbors.size() - 1);
			double percentageOfConnections;
			percentageOfConnections = countEdgesBetweenNeighbors / factor;

			// cumulating to the counter to get the average at last
			counter += percentageOfConnections;
		}

		// 3: dividing through the number of nodes to get the average
		counter = counter / NUMBER_OF_NODES;

		Calendar endTime = new GregorianCalendar();
		int seconds = (endTime.get(Calendar.SECOND) - startTime
				.get(Calendar.SECOND));
		int minutes = (endTime.get(Calendar.MINUTE) - startTime
				.get(Calendar.MINUTE));
		if (seconds < 0) {
			seconds = seconds + 60;
			minutes--;
		}
		System.out.println("Ready computing the cluster coefficient within "
				+ minutes + ":" + seconds + " minutes!");

		FileWriter outFile;
		try {
			outFile = new FileWriter(name + ".cluster", true);
			outFile.write((new Float(counter / NUMBER_OF_NODES)).toString()
					+ "\n");
			outFile.close();
		} catch (Exception e) {
			System.out
					.println("Analyzer.computeClusterCoefficient: Error while writing!");
		}

	}

	/*
	 * This method computes the distribution of the average of reachable Hosts
	 * within k Hops and the characteristic path length of the graph. At the hop
	 * distribution you can also see the diameter: it is the last entry in the
	 * file (here in average all other hops can be seen) Attention: this method
	 * appends the characteristic path length value to the file <name>.charPath
	 * for statistical analyzing of more graphs This method needs unfortunately
	 * much time because the problem finding all shortest paths between all
	 * pairs needs O(n^3) steps !!!!
	 */
	public void computeHopDistributionAndCharacteristicPathLength() {

		System.out.println("Computing the hop distribution and the characteristic path length");
		Calendar startTime = new GregorianCalendar(); // for measuring the computing time of the method
		Node[] nodes = graph.getNodesArray(); // saves all nodes of the Graph
		final int NUMBER_OF_NODES = nodes.length; // the number of nodes in the Graph. 
													// It is saved to a constant because it is often used.
		int[] seenHosts = new int[NUMBER_OF_NODES]; // saves the number of seen hosts of all nodes
		int[] countsSeenHosts2 = new int[NUMBER_OF_NODES]; // saves the cumulative number of seen Hosts
															// within k hops(sum of all nodes)
		Hashtable allNeighbors = new Hashtable(); // this hashmap saves the neighbors for each node
		boolean[] newHosts = new boolean[NUMBER_OF_NODES]; // this matrix saves for each node and each step which
															// nodes are "new" seen (this matrix is initialized
															// newly in each step)
		boolean[] copyOfRowI = new boolean[NUMBER_OF_NODES]; // this matrix saves one row out of the adjacency-matrix
																// so no modification are made on the adj-matrix
		boolean[] visited = new boolean[NUMBER_OF_NODES]; // this matrix saves the already visited nodes for
															// each node (this matrix is initialized newly
															// for each node)

		// 1: search all neighbors and take them into one big adjacency-list
		for (int i = 0; i < NUMBER_OF_NODES; i++) {
			Node[] neighborsOfNodeI = graph.getNeighborsOf(nodes[i]);
			LinkedList listOfNeighbors = new LinkedList();
			for (int k = 0; k < neighborsOfNodeI.length; k++) {
				listOfNeighbors.add(neighborsOfNodeI[k]);
			}
			allNeighbors.put(nodes[i], listOfNeighbors);
		}

		// 2: Create a adjacency-matrix for a faster computing out of the adjacenceList
		boolean[][] adjMatrix = new boolean[NUMBER_OF_NODES][NUMBER_OF_NODES];
		for (int i = 0; i < NUMBER_OF_NODES; i++) {
			seenHosts[i] = 1; // every node sees itself, of course
			// for each node search mark all neighbors with true
			for (int j = 0; j < NUMBER_OF_NODES; j++) {
				if (((LinkedList) allNeighbors.get(nodes[i]))
						.contains(nodes[j])) { // so node i is connected with node j
					adjMatrix[i][j] = true;
					seenHosts[i]++; // increment the seen Hosts for each neighbor
				} else {
					adjMatrix[i][j] = false;
				}
			}
			countsSeenHosts2[1] += seenHosts[i] - 1; // in 1 Hop you can see all the directly connected neighbors!
		}
		countsSeenHosts2[0] = NUMBER_OF_NODES; // in 0 steps you see always only yourself ! 
												// So the cumulative sum will be NumberOfNodes*1

		/*
		 * 3: Compute the average distribution using the adjacency-matrix i
		 * means the actual considered node. We search from there all nodes. In
		 * each step we see more hosts, still we have found all hosts (all but
		 * itself)
		 */
		for (int i = 0; i < NUMBER_OF_NODES; i++) {

			// make a copy of the i-th row so we can change the entries in this copy
			copyOfRowI = new boolean[NUMBER_OF_NODES];
			for (int m = 0; m < NUMBER_OF_NODES; m++) {
				copyOfRowI[m] = adjMatrix[i][m];
			}

			// save which nodes are already visited
			visited = new boolean[NUMBER_OF_NODES]; // the matrix is here initialized with false-entries !
			visited[i] = true; // never visit node itself
			int step = 2; // start with step 2 because step 1 is already considered (directly neighbors)
			int b, k, j; // variables are defined outside the loop because this is faster

			// we will be ready with Node i if we see all possible hosts !
			while (seenHosts[i] < NUMBER_OF_NODES) {
				newHosts = new boolean[NUMBER_OF_NODES]; // init: at the start of the loop, all entries are false.
				// If a new host is seen, this entry is turned to true
				// this loop represents one step in walking through the
				// neighbors
				for (j = 0; j < NUMBER_OF_NODES; j++) {
					if ((newHosts[j] == false) && (copyOfRowI[j] == true)
							&& (visited[j] == false)) { // then we have a new node to visit 
														// which was found in the last step!
						for (k = 0; k < NUMBER_OF_NODES; k++) {
							if ((copyOfRowI[k] == false)
									&& (adjMatrix[j][k] == true) && (k != i)) { 
								// then we have a new entry in row i (a new seen node)																			
								newHosts[k] = true; // the k-th node was found
								visited[j] = true; // node j is now visited and needn't be visited any more
								copyOfRowI[k] = true;
								seenHosts[i]++; // another new host is seen from node i
								countsSeenHosts2[step]++; // another new host is seen in this step
								// System.out.println("new seen host: "+k+" from host: "+j);
							}
						}
					}
				}
				step++; // we have then looked at all not visited nodes, which
						// are seen from node i in step <step> so we can go to
						// step <step+1>
			}
			System.out.print("."); // indicating the method is still running !!!
									// One point stands for computing one node
			// System.out.println("Ready with node "+ (i+1)
			// +" of "+NUMBER_OF_NODES);
		}
		System.out.println();

		// ----------------Hop Distribution---------------------------------

		// we have to modify the countseenHosts-array because before in each
		// step (since step 2) were only considered the new seen Hosts,
		// but we need the cumulative sum
		int[] cumulative = new int[NUMBER_OF_NODES];
		cumulative[0] = NUMBER_OF_NODES;
		for (int i = 1; i < NUMBER_OF_NODES; i++) {
			cumulative[i] = countsSeenHosts2[i] + cumulative[i - 1];
		}
		float[] distribution1 = new float[NUMBER_OF_NODES]; // saves the average of seen Hosts within k hops

		FileWriter outFile1;
		try {
			outFile1 = new FileWriter(name + ".hops");
			for (int i = 0; i < distribution1.length; i++) {
				distribution1[i] = ((float) cumulative[i])
						/ ((float) NUMBER_OF_NODES);
				outFile1.write(i + ":" + distribution1[i] + "\n");
				System.out.println("Average within " + i + " Hops: "
						+ distribution1[i]);

				// if in i steps in average all Nodes are seen, then also for
				// higher steps so these steps needn't to be stored!
				if (distribution1[i] == NUMBER_OF_NODES) {
					break;
				}
			}
			outFile1.close();
		} catch (Exception e) {
			System.out.println("Analyzer.computeHopDistributionAndCharacteristicPathLength: Error while writing!");
		}

		// ------------------------Characteristic Path Length---------------------------------

		float sum = 0;
		// the entries in the vector mean the number of seen hosts within k steps
		// so you have to multiply the entries with k to get the sum of the length of all shortest paths
		for (int i = 1; i < NUMBER_OF_NODES; i++) {
			sum += i * countsSeenHosts2[i];
		}
		// we must take the average of the paths (in the sum N*(N-1) paths are considered!!!)
		sum = sum / (NUMBER_OF_NODES * (NUMBER_OF_NODES - 1));											
		System.out.println("Characteristic Path Length = " + sum);

		FileWriter outFile2;
		try {
			outFile2 = new FileWriter(name + ".charPath", true);
			outFile2.write(new Float(sum).toString() + "\n");
			outFile2.close();
		} catch (Exception e) {
			System.out.println("Analyzer.computeHopDistributionAndCharacteristicPathLength: Error while writing!");
		}

		// ---------------------------------------------------------------------------------
		Calendar time1 = new GregorianCalendar();
		int seconds = (time1.get(Calendar.SECOND) - startTime
				.get(Calendar.SECOND));
		int minutes = (time1.get(Calendar.MINUTE) - startTime
				.get(Calendar.MINUTE));
		int hours = (time1.get(Calendar.HOUR) - startTime.get(Calendar.HOUR));

		if (seconds < 0) {
			seconds = seconds + 60;
			minutes--;
		}
		if (minutes < 0) {
			minutes = minutes + 60;
			hours--;
		}
		System.out.println("Ready computing the hop distribution and the characteristic path length within "
						+ hours + ":" + minutes + ":" + seconds + " hours!");
		// -----------------------------------------------------------------------------------

		/*
		 * Alternative: this algorithm for the hop distribution uses lists
		 * instead of a matrix. It is much more easy to read and understand but
		 * it is slower!!! For each node query the adjacency-list. You can get
		 * the seen hosts within k Hops, if you copy all the neighbors of the
		 * seen Hosts in your seenHostsWithin_k_Steps-List. This list contains
		 * at the beginning only the directed neighbors. In each of the k steps
		 * you need only to look for new added hosts in this list and to add its
		 * neighbors.
		 */

		/*
		 * int[] countsSeenHosts = new int[NUMBER_OF_NODES]; //saves the
		 * cumulative number of seen Hosts within k hops countsSeenHosts[0] =
		 * NUMBER_OF_NODES; //in 0 steps you see always only yourself ! So the
		 * cumulative sum will be NumberOfNodes*1 for (int i=0;
		 * i<NUMBER_OF_NODES; i++){ LinkedList visitedHosts = new LinkedList();
		 * LinkedList seenHosts_Within_k_Steps = (LinkedList) ((LinkedList)
		 * allNeighbors.get(nodes[i])).clone(); // case k=1 is considered here
		 * 
		 * visitedHosts.add(nodes[i]); // put the observed node in the list so
		 * it is not visited in the loop seenHosts_Within_k_Steps.add(nodes[i]);
		 * 
		 * countsSeenHosts[1] += (seenHosts_Within_k_Steps.size());
		 * 
		 * /* the first step is already done because it means only the directly
		 * connected Nodes which are already in the list !
		 */
		/*
		 * int steps; for (steps=2; steps <= NUMBER_OF_NODES-1; steps++){
		 * LinkedList copyOfSeenHosts_Within_k_Steps = (LinkedList)
		 * seenHosts_Within_k_Steps.clone(); //so no comodification is possible!
		 * Iterator iter = copyOfSeenHosts_Within_k_Steps.listIterator(); while
		 * (iter.hasNext()){ Node tmp1 = (Node) iter.next(); if
		 * (!visitedHosts.contains(tmp1)){ //only consider the new seen Hosts
		 * visitedHosts.add(tmp1); LinkedList neighborsOfTmp1 = (LinkedList)
		 * allNeighbors.get(tmp1); Iterator iterNeighbors =
		 * neighborsOfTmp1.listIterator(); while (iterNeighbors.hasNext()){ Node
		 * tmp2 = (Node) iterNeighbors.next(); if
		 * (!seenHosts_Within_k_Steps.contains(tmp2)){
		 * seenHosts_Within_k_Steps.add(tmp2); } } } }
		 * 
		 * 
		 * //System.out.println("SeenHosts from Node "+i+" in "+steps+" steps: "+
		 * seenHosts_Within_k_Steps.size()); countsSeenHosts[steps] +=
		 * (seenHosts_Within_k_Steps.size());
		 * 
		 * // if all Hosts are in the list no more steps are needed !!! if
		 * (seenHosts_Within_k_Steps.size() == NUMBER_OF_NODES){ break; } } //
		 * fill the number with the amount that is needed because not all steps
		 * were done ! for (int j=steps+1; j < countsSeenHosts.length; j++){
		 * countsSeenHosts[j] += NUMBER_OF_NODES; //from the last step all hosts
		 * were seen ! }
		 * 
		 * 
		 * } float[] distribution = new float[NUMBER_OF_NODES]; //saves the
		 * average of seen Hosts within k hops
		 * 
		 * FileWriter outFile; try { outFile = new
		 * FileWriter("lastAverageHopDistribution2.txt"); for (int i=0;
		 * i<distribution.length; i++){ distribution[i] =
		 * ((float)countsSeenHosts[i]) / ((float) NUMBER_OF_NODES);
		 * outFile.write(i+":"+distribution[i]+"\n");
		 * System.out.println("Average within "+i+" Hops: "+distribution[i]);
		 * 
		 * //if in i steps in average all Nodes are seen, then also for higher
		 * steps so these steps needn't to be stored! if (distribution[i] ==
		 * NUMBER_OF_NODES){ break; } } outFile.close(); } catch (Exception e){
		 * System
		 * .out.println("Analyzer.computeHopDistribution: Error while writing!"
		 * ); }
		 */
	}
}
