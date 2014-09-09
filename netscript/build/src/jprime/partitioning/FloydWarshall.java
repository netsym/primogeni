package jprime.partitioning;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * On small computers the practical maximum graph size with a 4-byte Node is 
 * about 23,000, at which point the data size of an instance begins to exceed 4 GB. 
 * 
 * This seems fine since jaguar, the largest/fastest super computer at this time,
 * only less than 17K nodes (each with 16 procs or something).
 * 
 * @author Nathanael Van Vorst
 */
public final class FloydWarshall {
	private final int [][] D;
    private final int[][] P;
    private boolean calced=false;

    /**
     * Create an instance of this class by describing the graph
     * upon which it will operate.
     * <p>
     * 
     * @param nodes Array of Node; must be completely populated
     * @param edges Array of Edge, completely populated; order is not important
     */
    public FloydWarshall ( int num_nodes)  {
        final int maxNodes = 23000;  // roughly 4 GB 
        assert num_nodes < maxNodes : "nodes.length cannot exceed "+ maxNodes
            +".\nSize of class data structures is at least (2*(node size)*nodes.length**2).";
        //this.nodes=nodes;
        this.D = new int[num_nodes][num_nodes];
        this.P = new int[num_nodes][num_nodes];
        for(int i=0; i<num_nodes; i++)  {
            Arrays.fill(this.D[i], Integer.MAX_VALUE );
            Arrays.fill(this.P[i], -1 );
        }
        for( int k=0; k<num_nodes; k++ )  {
        	this.P[k][k]=k;
        	this.D[k][k]=0;
        }
    }
    
    /**
     * 
     */
    public void calcShortestPaths() {
    	if(calced)
    		throw new RuntimeException("Already calcuated the shortest paths!");
    	calced=true;
        for( int k=0; k<P[0].length; k++ )  {
            for( int i=0; i<P[0].length; i++ )  {
                for( int j=0; j<P[0].length; j++ )  {
                    if( D[i][k] != Integer.MAX_VALUE 
                     && D[k][j] != Integer.MAX_VALUE 
                     && D[i][k]+D[k][j] < D[i][j] )
                    {
                        D[i][j] = D[i][k]+D[k][j];
                        P[i][j] = P[k][k];
                    }
                }
            }
        }
    }
    
    /**
     * @param left
     * @param right
     * @param weight
     */
    public void addEdge(int left, int right, int weight)  {
    	if(calced)
    		throw new RuntimeException("Already calcuated the shortest paths!");
    	if(D[left][right] == Integer.MAX_VALUE) {
        	D[left][right] = weight;
        	D[right][left] = weight;
    	}
    	else {
    		//only keep the min values
    		if(D[left][right] > weight)
    			D[left][right] =  weight;
    		if(D[right][left] > weight)
    			D[right][left] = weight;
    	}
    }
    

    /**
     * Determines the length of the shortest path from vertex A (source) 
     * to vertex B (target), calculated by summing the weights of the edges 
     * traversed.
     * <p>
     * Note that distance, like path, is not commutative.  That is, 
     * distance(A,B) is not necessarily equal to distance(B,A).
     * 
     * @param source Start Node
     * @param target End Node
     * @return The path length as the sum of the weights of the edges traversed, 
     * or <code>Integer.MAX_VALUE</code> if there is no path
     */
    public int getShortestDistance ( final int source, final int target )  {
    	if(!calced)
    		throw new RuntimeException("You have not calcuated the shortest paths!");
        return D[source][target];
    }

    /**
     * Describes the shortest path from vertex A (source) to vertex B (target) 
     * by returning a collection of the vertices traversed, in the order traversed. 
     * If there is no such path an empty collection is returned.
     * <p>
     * Note that because each Edge applies only to one direction of traverse,  
     * the path from A to B may not be the same as the path from B to A. 
     * 
     * @param source Start Node
     * @param target End Node
     * @return A List (ordered Collection) of Node, possibly empty
     */
    public List<Integer> getShortestPath ( final int source, final int target )  {
    	if(!calced)
    		throw new RuntimeException("You have not calcuated the shortest paths!");
        if(D[source][target] == Integer.MAX_VALUE){
            return new ArrayList<Integer>(); // no path
        }
        final List<Integer> path = getIntermediatePath( source, target );
        path.add( 0, source );
        path.add( target );
        return path;
    }
    
    /**
     * @param source
     * @param target
     * @return
     */
    public int getNextHop( final int source, final int target )  {
    	final int nexthop = getIntermediateNextHop(source,target);
    	return nexthop==-1?target:nexthop;
    }
    
    private int getIntermediateNextHop( final int source, final int target )  {
        if(P[source][target] == -1)  {
            return -1;
        }
        int nexthop = getNextHop(source, P[source][target]);
        if(nexthop != -1) return nexthop;
        nexthop=P[source][target];
        if(nexthop != -1) return nexthop;
        return getNextHop(nexthop, target);
    }
    
    private List<Integer> getIntermediatePath ( final int source, final int target )  {
        if(P[source][target] == -1)  {
            return new ArrayList<Integer>();
        }
        final List<Integer> path = new ArrayList<Integer>();
        path.addAll( getIntermediatePath(source, P[source][target]));
        path.add( P[source][target]);
        path.addAll( getIntermediatePath(P[source][target], target));
        return path;
    }

	/**
	 * @param argv
	 * @throws IOException
	 */
	public static void main(String argv[]) throws IOException {
		//create a network to test this on
		/*   
		 * n0-1-n1
		 *      |2
		 * n2-2-n3-3-n4
		 *      | 1/ |
		 *     1| /  |1
		 *     n5    n6
		 */
		FloydWarshall f = new FloydWarshall(7);
		
		f.addEdge(0, 1, 1);
		f.addEdge(1, 3, 2);
		f.addEdge(2, 3, 2);
		f.addEdge(3, 4, 3);
		f.addEdge(3, 5, 1);
		f.addEdge(4, 5, 1);
		f.addEdge(4, 6, 1);
		
		f.calcShortestPaths();
		for(int i =0;i<7;i++) {
			jprime.Console.out.println("n"+i);
			for(int j =0;j<7;j++) {
				if(i != j) {
					List<Integer> p = f.getShortestPath(i, j);
					jprime.Console.out.println("\t--> n"+j+"{path="+p+", cost="+f.getShortestDistance(i, j)+"},  next hop="+f.getNextHop(i, j));
				}
			}
		}
	}
}
