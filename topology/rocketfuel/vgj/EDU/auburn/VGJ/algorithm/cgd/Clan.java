/*
*
* Date		Author
* 12/4/96	Larry Barowski
*
*/


   package EDU.auburn.VGJ.algorithm.cgd;


   import EDU.auburn.VGJ.graph.Set;
   import EDU.auburn.VGJ.graph.Graph;
   import EDU.auburn.VGJ.graph.Node;
   import java.lang.String;


/**
 * A "Clan" class for CGD.
 * </p>Here is the <a href="../algorithm/cgd/Clan.java">source</a>.
 */
   public class Clan
   {
      public final static int	UNKNOWN = 0;
      public final static int	INDEPENDENT = 1;
      public final static int	LINEAR = 2;
      public final static int	PRIMITIVE = 3;
      public final static int	PSEUDOINDEPENDENT = 4;
      public final static int	SINGLETON = 5;
   
      public int clanType;
      public int id;
      public Set nodes;
      public Set sources;
      public Set sinks;
      public int size;	// number of nodes in the clan
      public int order;	// ordering for clan
   			//  is just the topo. no. from any node
   
      public Clan next;	// For use in stack of clans.
      public Clan listnext;	// For use in list of clans.
   
   
   
      public Clan(int type, Set nodes_in, Set sources_in, Set sinks_in,
      int order_in)
      {
         clanType = type;
         nodes = nodes_in;
         size = nodes.numberOfElements();
         sources = sources_in;
         sinks = sinks_in;
         order = order_in;
         next = listnext = null;
      }
   
   
   
      public String toString()
      {
         String string = new String();
      
         string += new String(" ILPiS").charAt(clanType);
         string += ": ";
         string += nodes.toShortString();
      
         return string;
      }
   
   
   
   
   
   // Translate indexes to ids for graph.
      public String toString(Graph graph)
      {
         String string = new String();
      
         string += new String(" ILPiS").charAt(clanType);
         string += ": ";
      
         string += "(";
         int index;
         boolean first = true;
         int numnodes = graph.numberOfNodes();
         for(index = nodes.first(); index != -1; index = nodes.next())
         {
            if(!first)
               string += ", ";
            else
               first = false;
         
            if(index < numnodes)
               string = string + graph.getNodeFromIndex(index).getId();
            else
               string += "d_" + index;
         
            if(nodes.isElement(index + 1) && nodes.isElement(index + 2))
            {
               string += "-";
               while(nodes.isElement(index + 1))
                  index = nodes.next();
               if(index < numnodes)
                  string += graph.getNodeFromIndex(index).getId();
               else
                  string += "dummy_" + index;
            }
         }
         string = string + ")";
      
         return string;
      }
   }
