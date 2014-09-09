//
// ********************************************************************
// **
// **	Author		: cartegw@humsci.auburn.edu	Gerald Carter
// **	Date Created	: 961021
// **
// **	filename	: BiconnectData.java
// **
// **	Description	
// **	-----------
// **	Data object for storing node information used in implementing 
// **	J.E. Hopcroft's depth-first traversal of a graph to determine 
// **	the Articulation Points of the graph.
// **
// **	The private members of the class are as follows...
// **	
// **	int	num; 		// the number of the node as determined 
// **				// by the DFS
// **	int	low;		// lowest numbered node reachable by n 
// **				// edges and at most on back edge
// **	boolean visited;	// has the node been previously visited?
// **	boolean articulationPt;	// is the node an articulation point?
// **
// **	------------
// **	Modification
// **	------------
// **
// ********************************************************************
//

package EDU.auburn.VGJ.algorithm.cartegw;

/**
 *  --- Description
        Data object for generic information field in Graph class.  Used by class 
        BiconnectGraph.
 * </p>Here is the <a href="../algorithm/cartegw/BiconnectData.java">source</a>.
 */

public class BiconnectData {
   private int		num;
   private int		low;
   private boolean	articulationPt;
   private boolean 	visited;
   private int		childCount;
   private int		node_index;

   // Default constructor
   public BiconnectData () {
      num = -1;
      low = -1;
      articulationPt = false;
      visited = false;
      childCount = 0;
      node_index = -1;
   }

   // Set and Get methods
   public boolean SetNumber ( int x ) { num = x; return ( true ); }
   public int GetNumber () { return ( num ); }
   public boolean SetLow ( int x ) { low = x; return ( true ); }
   public int GetLow () { return ( low ); }
   public boolean SetArticulationPoint ( boolean x ) 
      { articulationPt = x; return ( true ); }
   public boolean IsArticulationPoint () { return ( articulationPt ); }
   public boolean SetVisited ( boolean x ) { visited = x; return ( true ); }
   public boolean Visited () { return ( visited ); }
   public int GetChildCount () { return ( childCount ); }
   public int SetChildCount ( int x ) { childCount = x; return ( childCount ); }
   public int SetIndex ( int x ) { return ( node_index = x ); }
   public int GetIndex ( ) { return ( node_index ); }
}

// ******* end of BiconnectData.java **********************************
// ********************************************************************
