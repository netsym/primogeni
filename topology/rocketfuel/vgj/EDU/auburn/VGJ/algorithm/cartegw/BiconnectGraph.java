//
// ********************************************************************
// **
// **	Author 		: cartegw@humsci.auburn.edu	Gerald Carter
// **	Date Created	: 961021
// **
// **	filename	: BiconnectGraph.java
// **
// **	Description	
// **	-----------
// ** 	This file implements a BiconnectedGraph class that is created 
// **	from a Graph object.  The Graph object is modified is neccessary
// **	so that it is a biconnected graph.
// **
// **	Implementation Notes
// **	--------------------
// **	The mapping between nodes in the actual graph and the generated
// **	DFS tree is kind of muddled.  The linking is done by holding a
// **	value in the BiconnectData Object ( data ) that holds the Node
// **	index to the respective graph.
// **
// **	I know the code can be borribly confusing to look at sometimes,
// **	but without pointers this is what you get.
// **
// **	------------
// **	Modification
// **	------------
// **	970124	cartegw
// **		Added the "implements GraphAlgorithm" modifier to class
// **		from the EDU.auburn.VGJ.algorithm package.
// **	970125 	cartegw
// **		Rewrote the class to make use of the Graph object passed
// **		to the compute () method rather than making a local
// **		copy and then copying it back to the original graph.
// **   970420  cartegw
// **		Fixed a bug in the edge crossing code.  The addition of 
// **		edges should now be minimum.  Problem was caused by an
// **		an error in the mapping of DFS tree nodes to the 
// **		nodes in the graph corresponding.
// **
// **	970428	cartegw
// **		Setup constructor to accept a boolean arguement to
// **		determine whether or not edges are added when we run
// **		the FindArticulationPoints() method
// **
// ********************************************************************
//

// PACKAGE to which we belong
package EDU.auburn.VGJ.algorithm.cartegw;

// IMPORT statements from the VGJ classes
import EDU.auburn.VGJ.graph.Graph;
import EDU.auburn.VGJ.graph.Node;
import EDU.auburn.VGJ.graph.Set;
import EDU.auburn.VGJ.algorithm.GraphAlgorithm;
import EDU.auburn.VGJ.algorithm.GraphUpdate;
import EDU.auburn.VGJ.algorithm.cartegw.BiconnectData;
import EDU.auburn.VGJ.gui.TextOutDialog;

// IMPORT statements from java
import java.lang.Math;
import java.lang.String;


/**
 *  Class to transform the given graph to a biconnected graph.
 * </p>Here is the <a href="../algorithm/cartegw/BiconnectGraph.java">source</a>.
 */


// --------------------------------------------------------------------
//
// BiconnectedGraph class
//
public class BiconnectGraph extends Graph implements GraphAlgorithm {

   // class variables
   boolean 	addEdges;

   // ----------------------------------------------------------------
   public BiconnectGraph ( boolean state ) {
      addEdges = state;
   }

   public BiconnectGraph ( ) {
      addEdges = true;
   }

   // ----------------------------------------------------------------
   public boolean FindArticulationPoints ( Graph G, Graph dfsTree ) {

      // local variables
      Node 		v;
      int		nodeNum, count;
      boolean		foundArtPnt = false;
      Node 		dfsRoot = new Node ( ),
		 	tree_node = new Node ( );

      // Start the FindArticulationPoints algorithm
      v = G.firstNode ();
      if ( v != null ) {

         // This portion of the code initializes the generic data object
	 // field in the Node object
         while ( v != null ) {

            v.data = new BiconnectData ();
            v = G.nextNode ( v );

         }

         // Now we procede with the DFS of the graph and determining 
	 // articulation points
         count = 0;
         v = G.firstNode ();

         // Save the DFS tree in the member variable dfsTree.
         // What this code section does is to basically set up a bi-directional
         // link between the graph node and the DFS tree node.
         //
	 // THERE HAS GOT TO BE A EASIER WAY!!!!!!!!!
         ((BiconnectData)v.data).SetIndex ( dfsTree.insertNode () );
         tree_node = dfsTree.getNodeFromIndex ( ((BiconnectData)v.data).GetIndex() ); 
         tree_node.data = new BiconnectData ();
         ((BiconnectData)tree_node.data).SetIndex ( G.getIndexFromNode( v ) ); 
         dfsRoot = v;

         // This method actually performs the DFS
         foundArtPnt = FAP ( G, dfsTree, dfsRoot, v, null, count, foundArtPnt );

         // This section of the code deals with the first part of the
	 // theorem where v if the root of the spanning tree T
         if ( ((BiconnectData)v.data).GetChildCount () >= 2 ) {

            // The node is an articulation point
            ((BiconnectData)v.data).SetArticulationPoint ( true );
            foundArtPnt = true;
            
            // Add edges to connect all the children of v
            // This is for minimal edge additions
            if ( addEdges ) {
               int		child1 = -1,
   				child2 = -1;
               Node		root_of_tree = new Node (),
                                child_node = new Node ();

               root_of_tree = dfsTree.firstNode ();
               child_node = dfsTree.getNodeFromIndex(root_of_tree.firstChild ());
               while ( child_node != null ) {

                  child1 = ((BiconnectData)child_node.data).GetIndex ();
  
                  child_node = dfsTree.getNodeFromIndex(root_of_tree.nextChild ());

                  if ( child_node != null ) {
                     child2 = ((BiconnectData)child_node.data).GetIndex ();
                     G.insertEdge ( child1, child2 );
                  }

                  child1 = child2;

               }
               
            }

         }

      }

      // successful completion
      return ( foundArtPnt );
   }

   // ----------------------------------------------------------------
   public boolean FAP ( Graph G, Graph dfsTree, Node dfsRoot, Node v, 
			Node u, int count, boolean foundArtPnt ) {

      // local variables
      Node		w = new Node ();
      int		nodeEdge;
      int 		low;
      Node		tree_node = new Node ();

      // Initial settings for the node upon entry
      count = count + 1;
      ((BiconnectData)v.data).SetNumber ( count );
      ((BiconnectData)v.data).SetLow ( count );
      ((BiconnectData)v.data).SetVisited ( true );

      // Next section
      nodeEdge = v.firstChild ();
      while ( nodeEdge != -1 ) {

         w = G.getNodeFromIndex ( nodeEdge );

         if ( !((BiconnectData)w.data).Visited () ) {

            ((BiconnectData)w.data).SetIndex ( dfsTree.insertNode () );

            // Again...set up the bidirectional link between the node in the DFS tree
	    // and the Graph node.
            dfsTree.insertEdge ( ((BiconnectData)v.data).GetIndex (), 
                                 ((BiconnectData)w.data).GetIndex () );
            tree_node = dfsTree.getNodeFromIndex ( ((BiconnectData)w.data).GetIndex() ); 
            tree_node.data = new BiconnectData ();
            ((BiconnectData)tree_node.data).SetIndex ( G.getIndexFromNode( w ) ); 

            foundArtPnt = FAP ( G, dfsTree, dfsRoot, w, v, count, foundArtPnt );    
            low = Math.min ( ((BiconnectData)v.data).GetLow(), 
                             ((BiconnectData)w.data).GetLow() );
            ((BiconnectData)v.data).SetLow ( low );

            if ( ( ((BiconnectData)w.data).GetLow() >= ((BiconnectData)v.data).GetNumber() ) 
                    && ( u != null ) ) {

               // The node is an acticulation point
               ((BiconnectData)v.data).SetArticulationPoint ( true );
               foundArtPnt = true;

               // Remove the articulation point.  At this point the dfsTree is complete
               if ( addEdges ) {
                  AddEdgesFromChildToParent ( G, dfsTree, dfsRoot, w );
               }

               // Let's recalculate the previous lows...
               low = Math.min ( ((BiconnectData)v.data).GetLow(), 
                                ((BiconnectData)w.data).GetLow() );
               ((BiconnectData)v.data).SetLow ( low );

            }

            // Set the child count
            ((BiconnectData)v.data).SetChildCount ( ((BiconnectData)v.data).GetChildCount() + 1 );

         }

         else if ( w != u ) {

            // if w is not the parent of v ( node u ), then set the low of v to be the
            // minimum of v's count and w's low
            low = Math.min ( ((BiconnectData)v.data).GetLow(), ((BiconnectData)w.data).GetNumber() );
            ((BiconnectData)v.data).SetLow ( low );

         }

         nodeEdge = v.nextChild ();
      }

      // successful completion
      return ( foundArtPnt );

   }

   // ----------------------------------------------------------------
   // This code has been a problem from the word go.  What we want to 
   // do is first to minmize the number of edge additions for the tree.
   // Then we need to know which edges to add.
   //
   // Seems like the best way to do this is to add an edge from <v>
   // v's leafmost descendents in dfsTree to dfsRoot only if
   // their current values for the leaf make v an Articulation Point.
   private int AddEdgesFromChildToParent ( Graph G, Graph dfsTree, 
					   Node dfsRoot, Node v ) {
   
      // local variables
      int		child_of_v = 0,
                        low;
      Node		tree_node_of_v,
                        child_node_of_v,
                        graph_child;

      // The general idea is to traverse to all leaf nodes in the dfsTree
      // beginning with node v and add edges from the leaves to u if they
      // do not already exist.

      // First get the node from the dfsTree
      tree_node_of_v = dfsTree.getNodeFromIndex (((BiconnectData)v.data).GetIndex());

      // Now we can traverse the dfsTree
      child_of_v = tree_node_of_v.firstChild ();
      if ( child_of_v != -1 ) {		// not a leaf node

         while ( child_of_v != -1 ) {

            // Use of recursion keeps us from having to keep up with a stack 
            // for the DFS.
            child_node_of_v = dfsTree.getNodeFromIndex( child_of_v );
            graph_child = G.getNodeFromIndex ( ((BiconnectData)child_node_of_v.data).GetIndex() );
            AddEdgesFromChildToParent ( G, dfsTree, dfsRoot, graph_child );

            // At this point "graph_child" has the new low value.  If we get a low value in 
	    // graph_child lower than one we currently have, then stop the addition process.
            // This means that only one edge should be added per articulation point.
            // The propogation of low values up the dfsTree means than we may possibly
            // avoid some other articulation points later on by these edge additions.
            //
            // The problem with this is that we can no longer tell what the original
            // articulation points in the graph were.
            if ( ((BiconnectData)v.data).GetLow() > ((BiconnectData)graph_child.data).GetLow() ) {
               ((BiconnectData)v.data).SetLow ( ((BiconnectData)graph_child.data).GetLow() );
               return ( 0 );
            }

            // go to the next child
            child_of_v = tree_node_of_v.nextChild ();

         }

      }

      else {				// leaf node

         if ( ((BiconnectData)v.data).GetLow () > ((BiconnectData)dfsRoot.data).GetNumber () ) {

            // root is the root node of the DFS tree
            // descendent is the corrsponding node to v in the dfsTree
            Node		root,
				descendent;
 
            root = dfsTree.getNodeFromIndex ( ((BiconnectData)dfsRoot.data).GetIndex () );
            descendent = dfsTree.getNodeFromIndex ( ((BiconnectData)v.data).GetIndex () );

            G.insertEdge ( ((BiconnectData)root.data).GetIndex() , 
                           ((BiconnectData)descendent.data).GetIndex() );

            ((BiconnectData)v.data).SetLow ( ((BiconnectData)dfsRoot.data).GetNumber () );

         }

      }

      // successful completion
      return ( 0 );

   }

   // ----------------------------------------------------------------
   public String ArticulationPoints2String ( Graph G ) {

      // method to place the listed articulation points in a string.

      // local variables
      Node		v;
      int		node_num;
      String		retString = new String ();

      retString += "Articulation Points of the graph are...\n";

      // traverse all the nodes in the graph
      v = G.firstNode ();
      while ( v != null ) {

         // Print out the information to standard out
         // System.out.println ( ((BiconnectData)v.data).Visited () );
         if ( ((BiconnectData)v.data).IsArticulationPoint () ) {
            node_num = getIndexFromNode ( v );
            retString += "\tNode ";
            retString += node_num;
            retString += "\n";
         }
        
         // Go on to the next node in the graph
         v = G.nextNode ( v );

      }

      // successful completion
      return ( retString );

   }

   // ----------------------------------------------------------------
   public String compute ( Graph graph, GraphUpdate update ) {

      // The reason that we use a local copy of BiconnectGraph here
      // is so that we don't have to worry about static members of
      // the class.  Everytime we want to generate a biconnected graph
      // from the one we pass in to compute(), we simply create a new 
      // one. 

      // local variables

      // Our DFS tree
      Graph			dfsTree = new Graph ( );

      // If our FindArticulationPoints() method returns true then
      // we found an articulation point and the graph is not 
      // biconnected.
      boolean			not_biconnected = false;


      // Check to make sure the graph is not directed
      if ( graph.isDirected() ) {
         return ( "This algorithm should not be performed on a directed graph." );
      }

      // The DFS tree we keep should be directed
      dfsTree.setDirected ( true );

      // All we have to do is to call the FindArticulationPoints method
      not_biconnected = FindArticulationPoints ( graph, dfsTree );

      // If we found any articulation points, post them in a text dialog
      // box for the user to see.  The listed nodes will not be articulation
      // points any more because we have added edges to make the graph
      // biconnected.
      if ( !addEdges ) {

         if ( not_biconnected ) {
         
            new TextOutDialog ( update.getFrame(), "Articulation Points",
                                ArticulationPoints2String( graph ), false );
            return ( null );
         }
         else 
            return ( "Graph is already biconnected." );

      }

      else

            return ( null );

   }
}

// ******* end of BiconnectGraph.java *********************************
// ********************************************************************
