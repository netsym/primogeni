/*
*
* Date		Author
* 12/4/96	Larry Barowski
*
* The code related to this algorithm is based
* heavily on code by  Fwu-Shan Shieh .
*
*/




   package EDU.auburn.VGJ.algorithm.cgd;


   import java.lang.Integer;
   import java.awt.Point;

   import EDU.auburn.VGJ.algorithm.GraphAlgorithm;
   import EDU.auburn.VGJ.algorithm.GraphUpdate;
   import EDU.auburn.VGJ.graph.Graph;
   import EDU.auburn.VGJ.graph.Node;
   import EDU.auburn.VGJ.graph.Set;
   import EDU.auburn.VGJ.util.DPoint;
   import EDU.auburn.VGJ.util.DPoint3;
   import EDU.auburn.VGJ.util.DRect;
   import EDU.auburn.VGJ.util.DDimension;
   import EDU.auburn.VGJ.gui.TextOutDialog;

/**
 * An algorithm for laying out a graph by Clan-based Graph Deconposition.
 * </p>Here is the <a href="../algorithm/cgd/CGDAlgorithm.java">source</a>.
 */
   public class CGDAlgorithm implements GraphAlgorithm
   {
      private Set childRelation_[];
      private Set descendentRelation_[];
      private Set parentRelation_[];
      private Set ancestorRelation_[];
      private int numNodes_, numNodesOriginal_;
      private Graph graph_;
   
      private Clan firstClan_;
   
      private Set ccNodes_[];
      private int ccComponents_;
   
      private int topOOrder_[];
   
      private int height_[];
      private ClanTree root_;
   
      private double vSpacing_, hSpacing_;
      private boolean showTree_;
      private int lastIndex_;
   
      private ClanTree[] treeLookup_;
   
      private final static int debug_ = 0;
   
   
      public CGDAlgorithm()
      {
         showTree_ = false;
      }
   
   
      public CGDAlgorithm(boolean show)
      {
         showTree_ = show;
      }
   
   
      public String compute(Graph graph, GraphUpdate update)
      {
         if(!graph.isDirected())
            return("This algrithm is for directed graphs only.");
      
         graph_ = graph;
         Graph oldgraph = (Graph)graph.clone();
      
         if(graph_.numberOfNodes() < 2) 
            return "Graph must have at least two nodes.";
      
         makeChildRelation_();  // numNodes_ is set here
         numNodesOriginal_ = numNodes_;
      
         int i, j;
         if(true /* long edge handling */)
         {
            // Add dummy nodes for long edges.
         
            Set old_child_relation[] = new Set[numNodes_];
            for(i = 0; i < numNodes_; i++)
               old_child_relation[i] = (Set)childRelation_[i].clone();
         
            transitiveReduction_();
         
            // Count the number of dummy nodes needed.
            int extras = 0;
            for(i = 0; i < numNodes_; i++)
               for(j = old_child_relation[i].first(); j != -1;
               j = old_child_relation[i].next())
                  if(!childRelation_[i].isElement(j))
                     extras += 1;
         
            // Create a new child relation with the new nodes.
            numNodes_ += extras;
            Set new_child_relation[] = new Set[numNodes_];
            for(i = 0; i < numNodes_; i++)
               new_child_relation[i] = new Set();
         
            // Fill in non-transitive edges.
            for(i = 0; i < numNodesOriginal_; i++)
               for(j = childRelation_[i].first(); j != -1;
               j = childRelation_[i].next())
                  new_child_relation[i].includeElement(j);
         
            // Add the dummy nodes.
            lastIndex_ = numNodesOriginal_;
            for(i = 0; i < numNodesOriginal_; i++)
               for(j = old_child_relation[i].first(); j != -1;
               j = old_child_relation[i].next())
                  if(!childRelation_[i].isElement(j))
                  {
                     int newindex;
                  
                     newindex = graph.insertNode(true);
                     graph.removeEdge(i, j);
                     graph.insertEdge(i, newindex);
                     graph.insertEdge(newindex, j);
                  
                     new_child_relation[i].includeElement(newindex);
                     new_child_relation[newindex].includeElement(j);
                  }
         
            childRelation_ = new_child_relation;
         }
      
      
      
         if(debug_ > 1)
         {
            System.out.println("\nchildren - originial");
            printRelation_(childRelation_);
         }
      
         Set child_relation_back[] = new Set[numNodes_];
         for(i = 0; i < numNodes_; i++)
            child_relation_back[i] = (Set)childRelation_[i].clone();
      
         transitiveClosure_();
         descendentRelation_ = new Set[numNodes_];
         for(i = 0; i < numNodes_; i++)
            descendentRelation_[i] = (Set)childRelation_[i].clone();
         childRelation_ = child_relation_back;
      
      
         for(i = 0; i < numNodes_; i++)
            if(descendentRelation_[i].isElement(i))
            {
               graph_.copy(oldgraph);
               return "Graph contains cycles.";
            }
      
         if(debug_ > 1)
         {
            System.out.println("\nchildren - Hasse");
            printRelation_(childRelation_);
         }
      
         if(debug_ > 1)
         {
            System.out.println("\ndescendents");
            printRelation_(descendentRelation_);
         }
      
         parentRelation_ = new Set[numNodes_];
         ancestorRelation_ = new Set[numNodes_];
         for(i = 0; i < numNodes_; i++)
         {
            parentRelation_[i] = new Set();
            ancestorRelation_[i] = new Set();
         }
         for(i = 0; i < numNodes_; i++)
         {
            for(j = childRelation_[i].first(); j != -1;
            j = childRelation_[i].next())
               parentRelation_[j].includeElement(i);
         
            for(j = descendentRelation_[i].first(); j != -1;
            j = descendentRelation_[i].next())
               ancestorRelation_[j].includeElement(i);
         }
      
         if(debug_ > 1)
         {
            System.out.println("\nparents");
            printRelation_(parentRelation_);
         
            System.out.println("\nancestors");
            printRelation_(ancestorRelation_);
         }
      
         assignHeights_();
      
         topOOrder_ = new int[numNodes_];
         boolean is_dag = fillTopOOrder_();
         if(!is_dag)
            return "Graph contains cycles.";
      
         Set allNodes = new Set();
         allNodes.fill(numNodes_);
      
      	// Initialize the parse tree with a parse of all nodes.
         root_ = null;
         parseSet_(allNodes);
      
      	// Scan the parse tree and break down any primitives.
      	// Each primitive clan will be converted into a linear clan
      	// containing a (pseudo) independent and one other clan.
      	// The independent will consist of all sub-clans of the
      	// primitive that contain its sources.
      	// The other clan will be a re-parse of the remaining nodes
      	// (original primitive clan nodes - nodes added to the pseudo
      	// independent).
         breakPrimitives_(root_);
      
      
         if(debug_> 1)
            System.out.println("\nParse tree (after primitive decomposition, before reduction):\n" +
               root_.toString());
      
      	// Reduce the tree, i.e., for any node whose type is the same
      	// as it's parent's type, replace the node by it's children.
         reduce_(root_);
      
         if(debug_ > 0)
            System.out.println("\nParse tree:\n" + root_.toString());
      
      	// Assign unique id numbers to each clan.
         id_ = numNodes_;
         setId_(root_);
         numClans_ = id_ - numNodes_;
      
      
         // Reorder independent children according to x position.
         setPositions_(root_);
         reOrder_(root_);
      
      
         vSpacing_ = update.getVSpacing();
         hSpacing_ = update.getHSpacing();
      
      
      	// Layout.
         graph_.removeEdgePaths();  // Remove old edge paths from the graph.
         attributeGraph_(); // Initial bounding-box based spacing.
         graph.dummysToEdgePaths();
      
      
         if(showTree_)
         {
            new TextOutDialog(update.getFrame(), "Parse Tree",
               root_.toString(graph_), false);
            graph_.copy(oldgraph);
         }
      
         return null;
      }
   
   
   
   
   
   // Pre-order recursive.
      private void breakPrimitives_(ClanTree node)
      {
         if(node.clan.clanType == Clan.PRIMITIVE)
         {
                // Re-use the existing tree node - just change its label
            node.clan.clanType = Clan.LINEAR;
         
                // Create a new tree for the independent - it will be
                // attached to the linear after adding all the source
                // sub-clans of the original primitive.
            ClanTree ind_tree = new ClanTree();
            ind_tree.clan = new Clan(Clan.PSEUDOINDEPENDENT, new Set(), node.clan.sources, new Set(),
               node.clan.order);
         
                // As sub-clans are added to the independent, their
                // nodes are subtracted from this set.  Start with all
         	// nodes from the primitive clan.
            Set remaining_nodes = (Set)(node.clan.nodes.clone());
         
            int curr_max = 0;
            int n;
            for(n = node.clan.sources.first(); n != -1; n = node.clan.sources.next())
               if(height_[n] > curr_max)
                  curr_max = height_[n];
         
            ClanTree subclan;
            while((subclan = node.firstChild) != null)
            {
               node.firstChild = node.firstChild.nextSibling;
               if(node.clan.sources.intersects(subclan.clan.sources))
               {
                  // This subclan contains some of the primitive's
                  // sources.  So add its nodes and structure to the
                  // new independent.
                  if(height_[subclan.clan.sources.first()] >= curr_max)
                  {
                     ind_tree.clan.nodes.union(subclan.clan.nodes);
                     ind_tree.clan.sinks.union(subclan.clan.sinks);
                     addChild_(ind_tree, subclan);
                  
                     // Remove the nodes from this source subclan from
                     // the set of all nodes of the primitive clan.
                     remaining_nodes.difference(subclan.clan.nodes);
                  }
                  else
                  {
                     if(!(parentRelation_[subclan.clan.sources.first()].isEmpty()))
                     {
                        ind_tree.clan.nodes.union(subclan.clan.nodes);
                        ind_tree.clan.sinks.union(subclan.clan.sinks);
                        addChild_(ind_tree, subclan);
                     				// Remove the nodes from this source subclan from
                     				// the set of all nodes of the primitive clan.
                        remaining_nodes.difference(subclan.clan.nodes);
                     }
                  			// Else, the subclan goes away.
                  }
               }
            		// Else, the subclan goes away.
            }
         
            addChild_(node, ind_tree);
            addChild_(node, parseSet_(remaining_nodes));
         }
      
         ClanTree tmpclan;
         for(tmpclan = node.firstChild; tmpclan != null; tmpclan = tmpclan.nextSibling)
            breakPrimitives_(tmpclan);
      }
   
   
   
   
   
   
   
   
      private ClanTree parseSet_(Set node_subset)
      {
         if(debug_ > 1)
            System.out.println("\n\n********************************\n" +
               "Parsing subset: " + node_subset.toShortString());
      
      	// Partition nodes into sets S(0), ..., S(n-1) and M(0), ..., M(m-1)
         Partition S = new Partition(Partition.SIBLING, childRelation_,
         parentRelation_, descendentRelation_, ancestorRelation_,
         numNodes_, node_subset);
         Partition M = new Partition(Partition.MATE, childRelation_,
         parentRelation_, descendentRelation_, ancestorRelation_,
         numNodes_, node_subset);
      
         if(debug_ > 1)
         {
            System.out.println("S: " + S.toString());
            System.out.println("M: " + M.toString());
         }
      
      	// Initialize the list of clans.
         firstClan_ = null;
      
      	// Find clans as legal connected components of nodes between all
      	// pairs from an S set and an M set.
         for (int i = 0; i < S.size(); i++)
            for (int j = 0 ; j < M.size(); j++)
            {
               Set F = (Set)(S.star(i).clone());
               F.intersect(M.star(j));
            
               if(F.numberOfElements() > 1)
               {
                  if(debug_ > 1)
                     System.out.println("Set F:  i = " + i + "  j = " + j +
                        " : " + F.toShortString());
               
               		// Break up F into its connected components.
                  makeConnectedComponents_(F);
               
                  if(debug_ > 1)
                  {
                     System.out.println("F connected components - size = " +
                        ccComponents_);
                     int k;
                     for(k = 0; k < ccComponents_; k++)
                        System.out.println(ccNodes_[k].toShortString());
                     System.out.println();
                  }
               
               		// If more than one connected component is legal, then
               		// the union of all legal components is an independent clan.
               		// So start a count of legal components and a set of nodes.
                  int legal_components = 0;
                  Set legal_nodes = new Set();
               
                  Clan candidates = null;
               
                  int c;
                  for(c = 0; c < ccComponents_; c++)
                  {
                     if(ccNodes_[c].numberOfElements() > 1)
                     {
                     			// Get the sources and sinks of this component
                        Set s = (Set)(S.members(i).clone());
                        s.intersect(ccNodes_[c]);
                        Set m = (Set)(M.members(j).clone());
                        m.intersect(ccNodes_[c]);
                     
                     			// Add relatives for D* and A* sets.
                        Set ds = (Set)s.clone();
                        Set am = (Set)m.clone();
                        ds.indexedUnion(descendentRelation_, s);
                        am.indexedUnion(ancestorRelation_, m);
                     
                     			// Combine for A* U D* sets.
                        Set ads = (Set)ds.clone();
                        Set adm = (Set)am.clone();
                        ads.indexedUnion(ancestorRelation_, s);
                        adm.indexedUnion(descendentRelation_, m);
                     
                     			// Restrict all sets to the current subset.
                        ds.intersect(node_subset);
                        am.intersect(node_subset);
                        ads.intersect(node_subset);
                        adm.intersect(node_subset);
                     
                     			// Legal component if D*(S) <= A*(M) U D*(M)
                     			//  and A*(M) <= A*(S) U D*(S)
                        if(adm.isSubset(ds) && ads.isSubset(am))
                        {
                           Clan clan = new Clan(Clan.UNKNOWN, ccNodes_[c], s, m,
                           nodeOrder_(ccNodes_[c]));
                           clan.next = candidates;
                           candidates = clan;
                           legal_components++;
                           legal_nodes.union(ccNodes_[c]);
                        }
                     }
                     else
                     {
                     			// The component is a single node, so it is
                     			// legal.  Add to the set for an independent
                     			// clan, but do not add to the candidates list.
                        legal_components++;
                        legal_nodes.union(ccNodes_[c]);
                     }
                  }
               		// If more than one component is legal, then the union of
               		// all legal clans is an independent clan.
                  if(legal_components > 1)
                  {
                     Set sources = (Set)legal_nodes.clone();
                     Set sinks = (Set)legal_nodes.clone();
                     sources.intersect(S.members(i));
                     sinks.intersect(M.members(j));
                  
                     Clan clan = new Clan(Clan.INDEPENDENT, legal_nodes, sources,
                     sinks, nodeOrder_(sources));
                     clan.next = candidates;
                     candidates = clan;
                  }
               
                  Clan tmp_clan;
               
                  if(debug_ > 1)
                  {
                     System.out.println("Legal Components:");
                     for(tmp_clan = candidates; tmp_clan != null; tmp_clan =
                     tmp_clan.next)
                        System.out.println(tmp_clan.toString());
                  }
               
               		// 'Merge' the candidates into the clan list.
                  while(candidates != null)
                  {
                     Clan cand = candidates;
                     candidates = candidates.next;
                  
                     Set cand_nodes = cand.nodes;
                  
                  		// Scan the clan list, from smallest to largest.
                     Clan clan, prev_clan = null;
                     for (clan = firstClan_; clan != null; prev_clan = clan, clan = clan.listnext)
                     {
                        Set clan_nodes = clan.nodes;
                        if(cand_nodes.equals(clan_nodes))
                        {
                        			// Candidate matches an existing clan.
                        			// Update the existing type and throw away
                        			// the candidate.
                           if(debug_ > 1)
                              System.out.println("\nMatch on " +
                                 cand_nodes.toShortString());
                           if(cand.clanType != Clan.UNKNOWN)
                              clan.clanType = cand.clanType;
                           cand = null;
                           break;
                        }
                        if(cand_nodes.intersects(clan_nodes) &&
                        !(clan_nodes.isSubset(cand_nodes) ||
                        cand_nodes.isSubset(clan_nodes)))
                        {
                        			// Candidate has nodes in common with existing
                        			// clan, but neither is contained in the other.
                        			// I.e. {1, 2} & {2, 3} => {1, 2, 3} is linear.
                           if(debug_ > 1)
                              System.out.println("\nIntersection " +
                                 cand_nodes.toShortString() + ", " +
                                 clan_nodes.toShortString());
                        
                           cand_nodes.union(clan_nodes);
                           cand.size = cand_nodes.numberOfElements();
                           cand.clanType = Clan.LINEAR;
                        
                        			// Remove from list.
                           if(prev_clan != null)
                              prev_clan.listnext = clan.listnext;
                           else
                              firstClan_ = clan.listnext;
                        }
                     }
                     if(cand != null)
                     {
                     	    		// If the current candidate clan didn't get labeled
                     	    		// yet, mark it as a primitive clan.
                        if (cand.clanType == Clan.UNKNOWN)
                           cand.clanType = Clan.PRIMITIVE;
                     
                     	    		// Transfer the candidate onto the clans list.
                        addToClanList_(cand);
                     }
                  }
                  if(debug_ > 1)
                  {
                     System.out.println("\nUpdated clans list:");
                     Clan tmpclan;
                     for(tmpclan = firstClan_; tmpclan != null; tmpclan = tmpclan.listnext)
                        System.out.println(tmpclan.toString());
                     System.out.println("----------------------");
                  }
               }
            }
      
      	// Now build the parse tree.
      	// Reverse the clan list using the "next" field;
         Clan largest;
         firstClan_.next = null;
         for(largest = firstClan_; largest.listnext != null; largest = largest.listnext)
            largest.listnext.next = largest;
      
      	// The root of the tree is the largest clan found.
      	// (This should always be the entire nodeSubset passed in.)
         ClanTree root = new ClanTree();
         if(root_ == null)
            root_ = root;
         root.clan = largest;
         largest = largest.next;
      
         Clan f;
         while((f = largest) != null)
         {
            largest = largest.next;
            addClan_(root, f);
         }
      
         if(debug_ > 1)
            System.out.println("\nParse tree (before leafs, linear corrections):\n" + root.toString());
      
      	// Add singleton clans.
         int i;
         for(i = node_subset.first(); i != -1; i = node_subset.next())
         {
            Set singleset = new Set();
            singleset.includeElement(i);
            Clan clan = new Clan(Clan.SINGLETON, singleset, singleset, singleset,
            topOOrder_[i]);
            addClan_(root, clan);
         }
      
         if(debug_ > 1)
            System.out.println("\nParse tree (after leafs, before linear corrections):\n" + root.toString());
      
      	// Correctly label linear clans.
         root.fixLinear(node_subset, childRelation_, parentRelation_);
      
         if(debug_ > 1)
            System.out.println("\nParse tree for subset " + node_subset.toShortString() + "  :\n" +
               root.toString());
      
         return root;
      }
   
   
   
   
   
   
   
   
      private void addToClanList_(Clan clan)
      {
         if(firstClan_ == null)
         {
            firstClan_ = clan;
            return;
         }
         if(firstClan_.size > clan.size)
         {
            clan.listnext = firstClan_;
            firstClan_ = clan;
            return;
         }
      
         Clan tmpclan = firstClan_;
         while(tmpclan.listnext != null && tmpclan.listnext.size < clan.size)
            tmpclan = tmpclan.listnext;
      
         clan.listnext = tmpclan.listnext;
         tmpclan.listnext = clan;
      }
   
   
   
   
      private void transitiveClosure_()
      {
         int i, j;
         for(i = 0; i < numNodes_; i++)
            for(j = 0; j < numNodes_; j++)
            		// If edge from j to i, then every child of i
            		// becomes a child of j.
               if(childRelation_[j].isElement(i))
                  childRelation_[j].union(childRelation_[i]);
      }
   
   
   
   
      private void transitiveReduction_()
      {
         transitiveClosure_();
      
         int i, j;
         for(i = 0; i < numNodes_; i++)
            for(j = 0; j < numNodes_; j++)
            		// If edge from j to i, then every child of i
            		// should not be a child of j.
               if(childRelation_[j].isElement(i))
                  childRelation_[j].difference(childRelation_[i]);
      }
   
   
   
   
      private void makeChildRelation_()
      {
      	/* This function should be built in to Graph. */
      
         Node tmpnode;
         numNodes_ = 0;
         for(tmpnode = graph_.firstNode(); tmpnode != null;
         tmpnode = graph_.nextNode(tmpnode))
            if(tmpnode.getIndex() > numNodes_ - 1)
               numNodes_ = tmpnode.getIndex() + 1;
      
         childRelation_ = new Set[numNodes_];
         int i;
         for(i = 0; i < numNodes_; i++)
            childRelation_[i] = new Set();
      
         int child;
         for(tmpnode = graph_.firstNode(); tmpnode != null;
         tmpnode = graph_.nextNode(tmpnode))
            for(child = tmpnode.firstChild(); child != -1;
            child = tmpnode.nextChild())
               childRelation_[tmpnode.getIndex()].includeElement(child);
      }
   
   
   
   
   
   
      void makeConnectedComponents_(Set f)
      {
         int	n;
      
         ccComponents_ = 0;
      
         ccNodes_ = new Set[f.numberOfElements()];
         while((n = f.first()) != -1)
         {
            ccNodes_[ccComponents_] = new Set();
            Set c = ccNodes_[ccComponents_];
         
            c.includeElement(n);
            Set tmpset = (Set)(ancestorRelation_[n].clone());
            tmpset.union(descendentRelation_[n]);
            tmpset.intersect(f);
            c.union(tmpset);
            int last_size = -1;
            while(c.numberOfElements() != last_size)
            {
               last_size = c.numberOfElements();
               tmpset = new Set();
               tmpset.indexedUnion(ancestorRelation_, c);
               tmpset.indexedUnion(descendentRelation_, c);
               tmpset.intersect(f);
               c.union(tmpset);
            }
            f.difference(c);
            ccComponents_++;
         }
      }
   
   
   
   
   
   // Set topological order for the graph, if it is a DAG.
   // Return value indicates whether or not it is a DAG.
      private boolean fillTopOOrder_()
      {
         int count[] = new int[numNodes_];
      
      // Order according to height (distance from a source).      
         for(int i = 0; i < numNodes_; i++)
            topOOrder_[i] = height_[i];
      
         return true;
      }
   
   
   
   
   
   
      private int nodeOrder_(Set node_set)
      {
         int order = numNodes_;
         int n;
         for(n = node_set.first(); n != -1; n = node_set.next())
            if(topOOrder_[n] < order)
               order = topOOrder_[n];
         return order;
      }
   
   
   
   
   
   
   
      private void addClan_(ClanTree node, Clan clan)
      {
         ClanTree newnode = new ClanTree();
         newnode.clan = clan;
         addChild_(node, newnode);
      }
   
      private void addChild_(ClanTree node, ClanTree newnode)
      {
         Clan clan = newnode.clan;
         int order = clan.order;
      
         while(true)
         {
            if(node.firstChild == null)
            {
               node.firstChild = newnode;
               newnode.parent = node;
               newnode.nextSibling = null;
               return;
            }
            ClanTree child = node.firstChild;
            while(child != null)
            {
               if(child.clan.nodes.isSubset(clan.nodes))
               {
                  node = child;
                  break;
               }
               child = child.nextSibling;
            }
            if(child == null)
            		// Add as a new child
            {
               if(node.firstChild.clan.order > order)
               {
                  newnode.nextSibling = node.firstChild;
                  node.firstChild = newnode;
                  newnode.parent = node;
                  return;
               }
            
               ClanTree tmpnode = node.firstChild;
               while(tmpnode.nextSibling != null && tmpnode.nextSibling.clan.order
               <= order)
                  tmpnode = tmpnode.nextSibling;
            
               newnode.nextSibling = tmpnode.nextSibling;
               tmpnode.nextSibling = newnode;
               newnode.parent = node;
               return;
            }
         }
      }
   
   
   
   
   
   
      private void moveChild_(ClanTree node, ClanTree newnode)
      {
         Clan clan = newnode.clan;
         int order = clan.order;
      
         if(node.firstChild == null)
         {
            node.firstChild = newnode;
            newnode.parent = node;
            newnode.nextSibling = null;
            return;
         }
      
         if(node.firstChild.clan.order > order)
         {
            newnode.nextSibling = node.firstChild;
            node.firstChild = newnode;
            newnode.parent = node;
            return;
         }
      
         ClanTree tmpnode = node.firstChild;
         while(tmpnode.nextSibling != null && tmpnode.nextSibling.clan.order
         <= order)
            tmpnode = tmpnode.nextSibling;
      
         newnode.nextSibling = tmpnode.nextSibling;
         tmpnode.nextSibling = newnode;
         newnode.parent = node;
      }
   
   
   
   
   
      private void assignHeights_()
      {
         int i;
      
         // Find the sources.
         Set sources_ = new Set();
         for(i = 0; i < numNodes_; i++)
            if(parentRelation_[i].isEmpty())
               sources_.includeElement(i);
      
         // Assign heights
         height_ = new int[numNodes_];
         for(i = 0; i < numNodes_; i++)
            height_[i] = -1;
      
         for(i = sources_.first(); i != -1; i = sources_.next())
            assignHeights_(i, 0);
      }
   
   
      private void assignHeights_(int node, int height)
      {
         if(height > height_[node])
         {
            height_[node] = height;
            Set children = childRelation_[node];
         
            for(int i = children.first(); i != -1; i = children.next())
               assignHeights_(i, height + 1);
         }
      }
   
   
   
   
   
   
   
   
   
   // Post-order recursive.
      private void reduce_(ClanTree node)
      {
         ClanTree children[];
         ClanTree child;
      
         int num_children = 0;
         for(child = node.firstChild; child != null; child = child.nextSibling)
            num_children++;
         children = new ClanTree[num_children];
      
         int i;
         for(child = node.firstChild, i = 0; child != null; child = child.nextSibling, i++)
            children[i] = child;
      
         for(i = 0; i < num_children; i++)
            reduce_(children[i]);
      
         int ptype = (node.parent != null? node.parent.clan.clanType : 0);
         int ntype = node.clan.clanType;
      
         if(node.parent != null &&
         (ptype == ntype ||
         (ptype == Clan.PSEUDOINDEPENDENT && ntype == Clan.INDEPENDENT) ||
         (ptype == Clan.INDEPENDENT && ntype == Clan.PSEUDOINDEPENDENT)))
         {
            if(ntype == Clan.PSEUDOINDEPENDENT)
               node.parent.clan.clanType = Clan.PSEUDOINDEPENDENT;
            while(node.firstChild != null)
            {
               ClanTree tmpnode = node.firstChild;
               node.firstChild = node.firstChild.nextSibling;
               moveChild_(node.parent, tmpnode);
            }
            if(node.parent.firstChild == node)
               node.parent.firstChild = node.nextSibling;
            else
            {
               ClanTree tmpnode;
               for(tmpnode = node.parent.firstChild; tmpnode.nextSibling != node;
               tmpnode = tmpnode.nextSibling)
                  ;
               tmpnode.nextSibling = node.nextSibling;
            }
         }
      }
   
   
   
   
   
   
   /*** Temporary, for degugging. */
      private void printRelation_(Set relation[])
      {
         int i, j;
      
         System.out.println();
         for(i = 0; i < numNodes_; i++)
         {
            for(j = 0; j < numNodes_; j++)
               if(relation[i].isElement(j))
                  System.out.print("1 ");
               else
                  System.out.print("0 ");
            System.out.println();
         }
      }
   
   
   
      private int id_;
      private int numClans_;
   
   
   
   
   
   // Pre-order recursive
      private void setId_(ClanTree node)
      {
         if(node.clan.clanType == Clan.SINGLETON)
            node.clan.id = node.clan.nodes.first();
         else
            node.clan.id = id_++;
         ClanTree tmpnode;
         for(tmpnode = node.firstChild; tmpnode != null;
         tmpnode = tmpnode.nextSibling)
            setId_(tmpnode);
      }
   
   
   
   
   
      void attributeGraph_()
      {
         bbSizeAttribute_(root_, false);
      
         /*System.out.println(root_.toString());*/
      
         fillLeftSiblings_(root_);
         bbCornerAttribute_(root_);
      
         treeLookup_ = new ClanTree[numNodes_];
         setLookup_(root_);
         setHeightInTree_(root_, 0);
         longEdgeHeuristic_();
         treeLookup_ = new ClanTree[numNodes_];
         setLookup_(root_);
         bbSizeAttribute_(root_, true);
         bbCornerAttribute_(root_);
         realSizes_(root_);
         angleFix_();
         // First angleFix may cause more sharp angles, so call it again.
         angleFix_();
      
         // Find a source node, the position of which will
         // not change.
         Node first_source = graph_.getNodeFromIndex(0);
         int i;
         for(i = 0; i < numNodes_; i++)
            if(parentRelation_[i].isEmpty())
            {
               first_source = graph_.getNodeFromIndex(i);
               break;
            }
         DPoint offset = first_source.getPosition();
      
         copyCorner_(root_);
         removeBends_();
      
         DPoint pos = first_source.getPosition();
         offset.x -= pos.x;
         offset.y -= pos.y;
      
         Node tmpnode;
         for(tmpnode = graph_.firstNode(); tmpnode != null;
         tmpnode = graph_.nextNode(tmpnode))
         {
            pos = tmpnode.getPosition();
            tmpnode.setPosition(pos.x + offset.x, pos.y + offset.y);
         }
      
      }
   
   
   
   
   
   // Pre-order recursive.
      private void copyCorner_(ClanTree node)
      {
         if(node == null)
            return;
      
         if(node.clan.clanType == Clan.SINGLETON)
         {
            DPoint center_position = new DPoint(node.position.x, node.position.y);
            center_position.x += node.size.width / 2.0;
            center_position.y -= node.size.height / 2.0;
            graph_.getNodeFromIndex(node.clan.id).
               setPosition(center_position);
         }
      
         copyCorner_(node.firstChild);
         copyCorner_(node.nextSibling);
      }
   
   
   
   
   
   // Pre-order recursive.
      private void bbCornerAttribute_(ClanTree node)
      {
         if(node == null)
            return;
      
         if(node.parent == null)
         {
            node.position.x = 0;
            node.position.y = 0;
         }
         else if(node.parent.clan.clanType == Clan.LINEAR)
         {
            node.position.x = node.parent.position.x +
               (node.parent.size.width - node.size.width)
               / 2.0;
            if(node.leftSibling != null)
               node.position.y = node.leftSibling.position.y -
                  node.leftSibling.size.height - node.parent.extraheight;
            else
               node.position.y = node.parent.position.y;
         
         }
         else // parent is independent
         {
            node.position.y = node.parent.position.y -
               (node.parent.size.height -
               node.size.height) / 2.0;
            if(node.leftSibling != null)
               node.position.x = node.leftSibling.position.x +
                  node.leftSibling.size.width;
            else
               node.position.x = node.parent.position.x;
         
         }
      
         bbCornerAttribute_(node.firstChild);
         bbCornerAttribute_(node.nextSibling);
      }
   
   
   
   
      // Pre-order recursive.
      private void realSizes_(ClanTree node)
      {
         if(node == null)
            return;
      
         if(node.parent != null)
            if(node.parent.clan.clanType == Clan.LINEAR)
            {
               node.size.width = node.parent.size.width;
               node.position.x = node.parent.position.x;
            }
            else // parent is independent
            {
               node.size.height = node.parent.size.height;
               node.position.y = node.parent.position.y;
            }
      
         realSizes_(node.firstChild);
         realSizes_(node.nextSibling);
      }
   
   
   
   
   
   
   // Pre-order recursive
      private void fillLeftSiblings_(ClanTree node)
      {
         ClanTree tmpnode, prevnode;
      
         for(tmpnode = node.firstChild, prevnode = null; tmpnode != null;
         prevnode = tmpnode, tmpnode = tmpnode.nextSibling)
            tmpnode.leftSibling = prevnode;
      
         for(tmpnode = node.firstChild; tmpnode != null;
         tmpnode = tmpnode.nextSibling)
            fillLeftSiblings_(tmpnode);
      }
   
   
   
   
   
   
      private void bbSizeAttribute_(ClanTree node, boolean repeat)
      {
         if(node == null)
            return;
         bbSizeAttribute_(node.firstChild, repeat);
         node.extraheight = 0.0;
         node.size = bbSize_(node, repeat);
         bbSizeAttribute_(node.nextSibling, repeat);
      }
   
   
   
      private DDimension bbSize_(ClanTree node, boolean repeat)
      {
         DDimension size = new DDimension(0, 0);
      
         if(node.clan.clanType == Clan.LINEAR)
         {
            size.width = childMax_(node, 1);
            size.height = childSum_(node, 2);
         }
         else if(node.clan.clanType == Clan.SINGLETON)
         {
            if(!repeat)
            {
               size = graph_.getNodeFromIndex(node.clan.id).
                  getBoundingBox();
               size.width += hSpacing_;
               size.height += vSpacing_;
            }
            else
            {
               size.width = node.size.width;
               size.height = node.size.height;
            }
         }
         else // independent
         {
            if(node.size.height < childMax_(node, 2))
            {
               size.height = childMax_(node, 2);
            }
            else
               size.height = node.size.height;
               // Adjust the vertical spacing of the children of linear
               // children, and the width of singleton children.
            setExtras_(node, size.height);
            size.width = childSum_(node, 1);
         }
      
         return size;
      }
   
   
   
      private double childMax_(ClanTree node, int axis)
      {
         double max = 0;
      
         ClanTree child;
         for(child = node.firstChild; child != null; child = child.nextSibling)
            if((axis == 1 && child.size.width > max) ||
            (axis == 2 && child.size.height > max))
               max = axis == 1? child.size.width : child.size.height;
      
         return max;
      }
   
   
   
      private double childSum_(ClanTree node, int axis)
      {
         double sum = 0;
      
         ClanTree child;
         for(child = node.firstChild; child != null; child = child.nextSibling)
            sum += axis == 1? child.size.width : child.size.height;
      
         return sum;
      }
   
   
   
   
   
      // Find extra spacing for the children of linear clans.
      private void setExtras_(ClanTree node, double height)
      {
         double sum = 0;
      
         ClanTree child;
         for(child = node.firstChild; child != null; child = child.nextSibling)
            if(child.clan.clanType == Clan.LINEAR && child.size.height < height)
            {
               int children = 0;
               for(ClanTree tmp = child.firstChild; tmp != null;
               tmp = tmp.nextSibling)
                  children++;
               if(children > 1)
               {
                  child.extraheight = (height - child.size.height) /
                     ((double)(children - 1));
                  child.size.height = height;
               }
            }
      }
   
   
   
   
   
      // Order according to x position   
      private void setPositions_(ClanTree node)
      {
         node.dummy = false;
      
         if(node.clan.clanType == Clan.SINGLETON)
         {
            int graphnode = node.clan.nodes.first();
            if(graphnode < numNodesOriginal_)
               node.minx = node.maxx = node.centerx =
                  graph_.getNodeFromIndex(graphnode).getPosition().x;
            else
               node.dummy = true;
         }
         
         else
         {
            ClanTree tmp;
            boolean firsttime = true;
            for(tmp = node.firstChild; tmp != null; tmp = tmp.nextSibling)
            {
               setPositions_(tmp);
               if(tmp.dummy == false)
               {
                  if(firsttime)
                  {
                     node.minx = tmp.minx;
                     node.maxx = tmp.maxx;
                  }
                  else
                  {
                     if(tmp.minx < node.minx)
                        node.minx = tmp.minx;
                     if(tmp.maxx > node.maxx)
                        node.maxx = tmp.maxx;
                  }
                  firsttime = false;
               }
            }
            if(firsttime)
               node.dummy = true;
            else
               node.centerx = (node.minx + node.maxx) / 2.0;
         }
      }   
   
   
   
      // Reorder the children of independent clans from left to right.
      // Dummy clans (those with no real nodes) will be placed in the center).
      private void reOrder_(ClanTree node)
      {
         if(node.clan.clanType == Clan.SINGLETON)
            return;
      
         int numchildren = 0, numdummies = 0;
         ClanTree tmp;
         for(tmp = node.firstChild; tmp != null; tmp = tmp.nextSibling)
         {
            reOrder_(tmp);
            numchildren++;
            if(tmp.dummy)
               numdummies++;
         }
      
         if(numchildren < 2 || !(node.clan.clanType == Clan.INDEPENDENT
         || node.clan.clanType == Clan.PSEUDOINDEPENDENT))
            return;
      
         ClanTree children[] = new ClanTree[numchildren];
         int i = 0;
         for(tmp = node.firstChild; tmp != null; tmp = tmp.nextSibling)
            children[i++] = tmp;
      
         for(i = 0; i < numchildren - 1; i++)
            for(int j = i + 1; j < numchildren; j++)
               if((!children[i].dummy && !children[j].dummy && children[j].centerx <
               children[i].centerx) || (!children[i].dummy && children[j].dummy))
               {
                  ClanTree tmpnode = children[i];
                  children[i] = children[j];
                  children[j] = tmpnode;
               }
      
         if(numdummies > 0)
            // Move the dummy nodes to the center.
         {
            int num_left = (numchildren - numdummies) / 2;
            for(i = 0; i < num_left; i++)
            {
               ClanTree tmpnode = children[i];
               children[i] = children[i + numdummies];
               children[i + numdummies] = tmpnode;
            }
         }
      
         node.firstChild = children[0];
         for(i = 0; i < numchildren - 1; i++)
            children[i].nextSibling = children[i + 1];
         children[i].nextSibling = null;
      
      }
   
   
   
   
     // Fill the lookup table that relates graph nodes to
     // clan tree nodes.
      private void setLookup_(ClanTree node)
      {
         if(node == null)
            return;
      
         if(node.clan.clanType == Clan.SINGLETON)
            treeLookup_[node.clan.id] = node;
         setLookup_(node.firstChild);
         setLookup_(node.nextSibling);
      }
   
   
   
     // Fill the heightInTree fields in the clan tree nodes.
      private void setHeightInTree_(ClanTree node, int height)
      {
         if(node == null)
            return;
      
         node.heightInTree = height;
         setHeightInTree_(node.firstChild, height + 1);
         setHeightInTree_(node.nextSibling, height);
      }
   
   
   
   
      private void longEdgeHeuristic_()
      {
         int i, j;
         int old_numnodes = numNodes_;
      
         for(i = 0; i < old_numnodes; i++)
         {
            Set children = (Set)graph_.children(i).clone();
            for(j = children.first(); j != -1; j = children.next())
            {
               ClanTree nodei, nodej;
               nodei = treeLookup_[i];
               nodej = treeLookup_[j];
            
               int topnode = i, bottomnode = j;
            
               ClanTree sparenti = nodei.parent;
               ClanTree sparentj = nodej.parent;
            
               while(sparenti != null &&
               sparenti.clan.clanType != Clan.LINEAR)
                  sparenti = sparenti.parent;
               while(sparentj != null &&
               sparentj.clan.clanType != Clan.LINEAR)
                  sparentj = sparentj.parent;
            
               if(sparenti != null && sparentj != null)
               {
                  // Find least common ancestor and left-right order.
                  ClanTree lca = nodei;
                  ClanTree lca2 = nodej;
                  while(lca.parent != lca2.parent)
                  {
                     if(lca.heightInTree >= lca2.heightInTree)
                        lca = lca.parent;
                     if(lca.heightInTree < lca2.heightInTree)
                        lca2 = lca2.parent;
                  }
               
               
                  ClanTree left_node, right_node;
                  for(; lca2 != null && lca2 != lca; lca2 = lca2.nextSibling)
                     ;
                  if(lca2 == null)  // Then lca2 was on the right.
                  {
                     left_node = nodei;
                     right_node = nodej;
                  }
                  else
                  {
                     left_node = nodej;
                     right_node = nodei;
                  }
                  lca = lca.parent;  
               
                  ClanTree tmpnode;
                  for(; left_node.parent != lca; left_node = left_node.parent)
                     if(left_node.parent.clan.clanType == Clan.LINEAR)
                        for(tmpnode = left_node.nextSibling; tmpnode != null;
                        tmpnode = tmpnode.nextSibling)
                           topnode = addDummy_(tmpnode, topnode, bottomnode,
                              nodei, nodej);
                  for(; right_node.parent != lca; right_node = right_node.parent)
                     if(right_node.parent.clan.clanType == Clan.LINEAR)
                        for(tmpnode = right_node.leftSibling; tmpnode != null;
                        tmpnode = tmpnode.leftSibling)
                           bottomnode = addDummy_(tmpnode, topnode, bottomnode,
                              nodei, nodej);
                  for(left_node = left_node.nextSibling; left_node != right_node;
                  left_node = left_node.nextSibling)
                     topnode = addDummy_(left_node, topnode, bottomnode,
                        nodei, nodej);
               }
            }
         }
      }
   
   
   
   
      // Add dummy nodes for edges that might intersect
      // nodes because of a steep angle.
      private void angleFix_()
      {
         int i, j, k;
      
         for(i = 0; i < numNodes_; i++)
         {
            ClanTree tnodei = treeLookup_[i];
            for(k = 0; k < 2; k++)
            {
               Set connections;
               if(k == 0)
                  connections = graph_.parents(i);
               else
                  connections = graph_.children(i);
               for(j = connections.first(); j != -1; j = connections.next())
               {
                  double x1, x2, y1, y2;
               
                  if(j < numNodes_)
                  {
                     ClanTree tnodej = treeLookup_[j];
                     x2 = tnodej.position.x + tnodej.size.width / 2.0;
                     y2 = tnodej.position.y - tnodej.size.height / 2.0;
                  
                  }
                  else
                  {
                     DPoint pos = graph_.getNodeFromIndex(j).getPosition();
                     x2 = pos.x;
                     y2 = pos.y;
                  }
               
                  x1 = tnodei.position.x + tnodei.size.width / 2.0;
                  y1 = tnodei.position.y - tnodei.size.height / 2.0;
               
                  double dx, dy;
                  dx = Math.abs(x1 - x2);
                  if(dx == 0)
                     continue;
                  dy = Math.abs(y1 - y2);
               
                  double dy2;
                  double dist;
                  dy2 = dy / dx * tnodei.size.width / 2.0;
                  dist = tnodei.size.height / 2.0;
                  dist -= dy2;
                  if(dist <= vSpacing_ / 2.0)
                     continue;
               
                  double offs = dy * dy / (dx * dx + dy * dy);
                  offs = Math.sqrt(offs);
                  offs *= tnodei.size.width / 2.0;
                  if(x2 > x1)
                     offs = tnodei.size.width - offs;
               
                  int newnodeindex = graph_.insertNode(true);
                  Node newnode = graph_.getNodeFromIndex(newnodeindex);
               
                  if(k == 0)
                  {
                     graph_.removeEdge(j, i);
                     graph_.insertEdge(j, newnodeindex);
                     graph_.insertEdge(newnodeindex, i);
                  
                     newnode.setPosition(tnodei.position.x + offs,
                        tnodei.position.y - vSpacing_ / 4.0);
                  }
                  else
                  {
                     graph_.removeEdge(i, j);
                     graph_.insertEdge(i, newnodeindex);
                     graph_.insertEdge(newnodeindex, j);
                  
                     newnode.setPosition(tnodei.position.x + offs,
                        tnodei.position.y - tnodei.size.height + vSpacing_ / 4.0);
                  }
               }
            }
         }
      }
   
   
   
   
   
      private void removeBends_()
      {
         Node tmpnode;
         boolean needed;
      
         for(tmpnode = graph_.firstNode(); tmpnode != null;
         tmpnode = graph_.nextNode(tmpnode))
            if(tmpnode.getIndex() >= numNodesOriginal_)
            {
               needed = false;
               int index = tmpnode.getIndex();
               Node node1 = graph_.getNodeFromIndex(graph_.parents(index).first());
               Node node2 = graph_.getNodeFromIndex(tmpnode.firstChild());
               DPoint p1 = node1.getPosition();
               DPoint p2 = node2.getPosition();
               double dxdy = (p2.x - p1.x) / (p2.y - p1.y);
            
               double minx = Math.min(p1.x, p2.x);
               double miny = Math.min(p1.y, p2.y);
               double maxx = Math.max(p1.x, p2.x);
               double maxy = Math.max(p1.y, p2.y);
            
               Node tmpnode2;
               for(tmpnode2 = graph_.firstNode(); tmpnode2 != null;
               tmpnode2 = graph_.nextNode(tmpnode2))
                  if(tmpnode2.getIndex() < numNodesOriginal_ && tmpnode2 != node1 &&
                  tmpnode2 != node2)
                  {
                     DPoint p = tmpnode2.getPosition();
                     DDimension bbx = tmpnode2.getBoundingBox();
                     double x1 = p.x - bbx.width / 2.0 - hSpacing_ / 10.0;
                     double x2 = p.x + bbx.width / 2.0 + hSpacing_ / 10.0;
                     double y1 = p.y - bbx.height / 2.0 - vSpacing_ / 10.0;
                     double y2 = p.y + bbx.height / 2.0 + vSpacing_ / 10.0;
                  
                     if(x1 <= minx && x2 <= minx || x1 >= maxx && x2 >= maxx)
                        continue;
                     if(y1 <= miny && y2 <= miny || y1 >= maxy && y2 >= maxy)
                        continue;
                  
                     double cross;
                     if(y1 > miny && y1 < maxy)
                     {
                        cross = p1.x + dxdy * (y1 - p1.y);
                        if(x1 <= cross && cross <= x2)
                        {
                           needed = true;
                           break;
                        }
                     }
                     if(y2 > miny && y2 < maxy)
                     {
                        cross = p1.x + dxdy * (y2 - p1.y);
                        if(x1 <= cross && cross <= x2)
                        {
                           needed = true;
                           break;
                        }
                     }
                     if(x1 > minx && x1 < maxx)
                     {
                        cross = p1.y + (x1 - p1.x) / dxdy;
                        if(y1 <= cross && cross <= y2)
                        {
                           needed = true;
                           break;
                        }
                     }
                     if(x2 > minx && x2 < maxx)
                     {
                        cross = p1.y + (x2 - p1.x) / dxdy;
                        if(y1 <= cross && cross <= y2)
                        {
                           needed = true;
                           break;
                        }
                     }
                  }
            
               if(!needed)
               {
                  graph_.removeEdge(node1.getIndex(), index);
                  graph_.removeEdge(index, node2.getIndex());
                  graph_.insertEdge(node1.getIndex(), node2.getIndex());
               }
            }
      }
   
   
   
   
   
      // Add a dummy graph node and tree node.
      public int addDummy_(ClanTree treenode, int top, int bottom,
      ClanTree edgesource, ClanTree edgesink)
      {
         // Insert the dummy node.
         numNodes_++;
         int newnodeindex = graph_.insertNode(true);
         graph_.removeEdge(top, bottom);
         graph_.insertEdge(top, newnodeindex);
         graph_.insertEdge(newnodeindex, bottom);
      
         // If node is a singleton, make it an independent with
         // a singleton child.
         if(treenode.clan.clanType == Clan.SINGLETON)
         {
            ClanTree copy = new ClanTree();
            copy.clan = treenode.clan;
            copy.size.setTo(treenode.size);
            copy.position.move(treenode.position);
            copy.parent = treenode;
         
            treenode.clan = new Clan(Clan.INDEPENDENT, new Set(), null, null, 0);
            treenode.firstChild = copy;
         }
      
        // Create the new tree node.
         ClanTree tnode = new ClanTree();
         tnode.clan = new Clan(Clan.SINGLETON, new Set(newnodeindex), null, null, 0);
         tnode.clan.id = newnodeindex;
         tnode.parent = treenode;
      
        // Find the desired position.
         double x1 = edgesource.position.x + edgesource.size.width / 2.0;
         double y1 = edgesource.position.y + edgesource.size.height / 2.0;
         double x2 = edgesink.position.x + edgesink.size.width / 2.0;
         double y2 = edgesink.position.y + edgesink.size.height / 2.0;
      
         double y = treenode.position.y + treenode.size.height / 2.0;
         double idealx = x2 + (x1 - x2) / (y1 - y2) * (y - y2);
      
         // Find out which bounding box border the desired position
         // is closest to.
         int closest = 0;
         int index = 1;
         double cdist = Math.abs(treenode.position.x - idealx);
         for(ClanTree tmpnode = treenode.firstChild; tmpnode != null;
         tmpnode = tmpnode.nextSibling)
         {
            if(Math.abs(tmpnode.position.x + tmpnode.size.width - idealx)
            <= cdist)
            {
               cdist = Math.abs(tmpnode.position.x + tmpnode.size.width - idealx);
               closest = index;
            }
            index++;
         }
      
         // Insert the new tree node at the proper position.
         if(closest == 0)
         {
            tnode.nextSibling = treenode.firstChild;
            treenode.firstChild = tnode;
         }
         else
         {
            ClanTree tmpnode = treenode.firstChild;
            for(index = 1; index < closest; index++)
               tmpnode = tmpnode.nextSibling;
         
            tnode.nextSibling = tmpnode.nextSibling;
            tmpnode.nextSibling = tnode;
            tnode.leftSibling = tmpnode;
         }
      
         if(tnode.nextSibling != null)
            tnode.nextSibling.leftSibling = tnode;     
      
         tnode.size.setTo(hSpacing_, vSpacing_);
         Node newnode = graph_.getNodeFromIndex(newnodeindex);
         newnode.setBoundingBox(hSpacing_, vSpacing_);
      
         return newnodeindex;
      }
   
   }

