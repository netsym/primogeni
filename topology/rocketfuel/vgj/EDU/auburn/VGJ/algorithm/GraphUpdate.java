/*
	File: GraphUpdate.java
	5/22/96    Larry Barowski
*/


   package EDU.auburn.VGJ.algorithm;

   import java.awt.Frame;
   import EDU.auburn.VGJ.util.DRect;
   import EDU.auburn.VGJ.graph.Node;



/**
 *	This interface represents an updatable graph display.
 *	</p>Here is the <a href="../algorithm/GraphUpdate.java">source</a>.
 *
 *@author	Larry Barowski
**/
   public interface GraphUpdate
   {
   
   
   
   /**
   *  Update the display. If adjust_bounds is set, the boundaries
   *  are recomputed (basically, the controls get updated as well
   *  as the display, and this will be slow for large graphs).
   **/
      abstract public void update(boolean adjust_bounds);
   
   
   
   /**
   *  Set the scale value for display, and update the display. scaleval is
   *  interpreted as follows: screen_distance = physical_distance * scaleval.
   **/
      abstract public void scale(double scaleval);
   
   
   
   /**
   *  Update the display and boundaries, and center the graph in the
   *  display window.
   **/
      abstract public void center();
   
   
   
   
   /**
   *  Get the position and dimensions of the display window.
   **/
      abstract public DRect windowRect();
   
   
   
      abstract public double getHSpacing();
      abstract public double getVSpacing();
   
   
   
   /**
   * Get an application Frame from which to pop up windows.
   **/
      abstract public Frame getFrame();
   
   
   
   /**
   * Get the index of the selected node. -1 is returned if no node is selected.
   **/
      abstract public Node getSelectedNode();
   }
