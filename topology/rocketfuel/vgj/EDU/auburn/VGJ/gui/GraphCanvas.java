/*
 * File: GraphCanvas.java
 *
 * 5/29/96   Larry Barowski
 *
*/


   package EDU.auburn.VGJ.gui;




   import java.awt.Image;
   import java.awt.Graphics;
   import java.awt.Event;
   import java.awt.Point;
   import java.awt.Dimension;
   import java.awt.Color;
   import java.applet.Applet;
   import java.lang.Math;
   import java.util.Enumeration;
   import java.awt.FontMetrics;
   import java.awt.Font;
   import java.awt.Frame;

   import EDU.auburn.VGJ.algorithm.GraphUpdate;
   import EDU.auburn.VGJ.util.DDimension;
   import EDU.auburn.VGJ.util.DDimension3;
   import EDU.auburn.VGJ.util.DPoint;
   import EDU.auburn.VGJ.util.DPoint3;
   import EDU.auburn.VGJ.util.DRect;
   import EDU.auburn.VGJ.util.Matrix44;
   import EDU.auburn.VGJ.graph.Set;
   import EDU.auburn.VGJ.graph.Graph;
   import EDU.auburn.VGJ.graph.Node;
   import EDU.auburn.VGJ.graph.Edge;
   import EDU.auburn.VGJ.graph.NodePropertiesDialog;
   import EDU.auburn.VGJ.graph.EdgePropertiesDialog;


   import java.awt.image.*;


/**
 *	A window class for editing and displaying Graphs.
 *	</p>Here is the <a href="../gui/GraphCanvas.java">source</a>.
 *
 *@author	Larry Barowski
**/




   public class GraphCanvas extends OffsetCanvas
   implements GraphUpdate
   {
      public static final int		MOUSEMOVE =	32451;	// event id for mouse movement
   
      public static final int		CREATE_NODES = 0;
      public static final int		CREATE_EDGES = 1;
      public static final int		SELECT_NODES = 2;
      public static final int		SELECT_EDGES = 3;
      public static final int		SELECT_BOTH = 4;
   
   // Event indicating that the graph has changed.
      public static final int		UPDATE = 38792;
   
      private static final int aaDivs_ = 4;  // Number of pixel divisions for
                                             // anti-aliasing.
      static private Color[] aaShades_;
   
      private Frame frame_;
   
      private double	width_,		// drawable width
      height_;	// drawable height
   
   
      private Dimension	windowSize_ =
      new Dimension(-1, -1);	// window viewable size, -1 values
   				// will force a change_size at start
   
   // the scroll offsets - screen position + offset = physical position
      private double		offsetx_ = 0, offsety_ = 0;
   
      private double		minx_, miny_, maxx_, maxy_;
   
      private double		scale_ = 1.0;
   
      private Graph	graph_;
   
      private	DPoint	offset_ = new DPoint(0, 0);
   
      private Node	newEdgeNode_;
      private Node	movingNode_;
      private Node	selectedNode_;
      private double	movingZ_, movingX_, movingY_;
      private double	selectedRatio_;
      private Point	selectedEdge_;
   
      private final static int	NONE_ = 0;
      private final static int	CENTER_ = 1;
      private final static int	CORNER_ = 2;
      private final static int	BOTTOM_ = 3;
      private final static int	LEFT_ = 4;
   
      private int	selected_ = NONE_;
   
      private int	mouseMode_ = CREATE_NODES;
   
      private boolean	scaleBounds_ = true;
   
      private Matrix44	viewTransform_, moveTransform_;
      private Matrix44	scaleMatrix_, shiftMatrix_;
      private Matrix44	rotxMatrix_, rotzMatrix_;
   
      private boolean xyPlane_ = true;
   
      private boolean _3d_ = false;
      private Image backImage_ = null;
      private Font font_;
   
      private int currentMouseAction_ = 0;
   
      private DPoint3 lastEdgePoint_ = null;
      private int pathLength_, pathArraySize_;
      private DPoint3[] pathArray_;
   
      private int multiSelectX_, multiSelectY_, multiSelectX2_,
      multiSelectY2_;
   
      private double moveX_, moveY_;
   
      public double hSpacing = 30, vSpacing = 40;
   
      private DragFix dragFix_;
   
      private int qualityCB_ = 1;
      private int quality_ = 1;
   
      static private NodePropertiesDialog propDialog_ = null;
      static private EdgePropertiesDialog edgePropDialog_ = null;
   
   
   
   
   
      public GraphCanvas(Graph graph_in, Frame frame_in)
      {
         graph_ = graph_in;
         frame_ = frame_in;
      
         setBackground(Color.white);
         font_ = new Font("Helvetica", Font.PLAIN, 12);
      
         computeBounds_();
      
         scaleMatrix_ = new Matrix44();
      
         scaleMatrix_.matrix[0][0] = scaleMatrix_.matrix[1][1] =
                              scaleMatrix_.matrix[2][2] = scaleMatrix_.matrix[3][3] = 1.0;
      
         shiftMatrix_ = new Matrix44(scaleMatrix_);
         rotxMatrix_ = new Matrix44(scaleMatrix_);
         rotxMatrix_.matrix[1][1] = rotxMatrix_.matrix[2][2] = -1.0;
         rotxMatrix_.matrix[2][1] = -(rotxMatrix_.matrix[1][2] = 0.0);
         rotzMatrix_ = new Matrix44(scaleMatrix_);
      
         updateViewTransform_();
      
         dragFix_ = new DragFix(this);
      }
   
   
   
   
   
   // This will give the initial window size.
      public Dimension preferredSize()
      {
         return new Dimension(400, 400);
      }
   
   
   
   
   
      public synchronized void paint(Graphics graphics)
      {
         graphics.dispose();
         paintOver();
      }
   
   
   
   
   
   
      public synchronized void paintOver()
      {
         Dimension tmpdim = size();
         if(tmpdim.width != windowSize_.width || tmpdim.height != windowSize_.height)
         {
         	// Maintain the center point.
            if(windowSize_.width > 0) // not first time
            {
               setOffsets_(offset_.x + (tmpdim.width - windowSize_.width) / 2,
                           offset_.y + (tmpdim.height - windowSize_.height) / 2);
            }
            else // initialize to centered
            {
               setOffsets_(.5 * (tmpdim.width - (minx_ + maxx_) * scale_),
                           .5 * (tmpdim.height - (miny_ + maxy_) * scale_));
            }
         
            windowSize_.width = tmpdim.width;
            windowSize_.height = tmpdim.height;
         
         
            // post an event indicating a size change
            getParent().postEvent(new Event((Object)this, RESIZE, (Object)this));
         
            // Force recreation of back buffer.
            backImage_ = null;
         }
      
         if(backImage_ == null)
            backImage_ = createImage(windowSize_.width, windowSize_.height);
      
      
         Graphics back_graphics = getBackGraphics_();
         back_graphics.setColor(Color.white);
         back_graphics.setPaintMode();
         back_graphics.clearRect(0, 0, windowSize_.width, windowSize_.height);
      
         back_graphics.setColor(Color.black);
         drawAxes_(back_graphics);
         drawObjects_(false, back_graphics, 1);
         back_graphics.dispose();
      
         Graphics graphics = getGraphics();
         graphics.setPaintMode();
         graphics.setColor(Color.black);
         graphics.drawImage(backImage_, 0, 0, null);
      
         // Draw selected objects directly to screen.
         graphics.setFont(font_);
         drawObjects_(true, graphics, 0);
      
         graphics.dispose();
      }
   
   
   
   
   
     // Get the graphics with the font set.
      private Graphics getGraphicsInternal_()
      {
         Graphics graphics = getGraphics();
         graphics.setFont(font_);
         return graphics;
      }
   
   
   
      private Graphics getBackGraphics_()
      {
         Graphics graphics = backImage_.getGraphics();
         graphics.setFont(font_);
         return graphics;
      }
   
   
   
     // Draw selected or unselected objects.
      public synchronized void drawObjects_(boolean selected, Graphics graphics,
                                          int which_gr)
      {
         Node tmpnode;
      
      // Draw nodes.
         for(tmpnode = graph_.firstNode(); tmpnode != null;
             tmpnode = graph_.nextNode(tmpnode))
            if(tmpnode.getSelected() == selected)
               tmpnode.draw(this, graphics, viewTransform_, quality_);
      
         // Draw edges.
         Enumeration edges = graph_.getEdges();
         boolean directed = graph_.isDirected();
         while(edges.hasMoreElements())
         {
            Edge edge = (Edge)(edges.nextElement());
            Node head = edge.head(), tail = edge.tail();
         
            if((head.getVisibleGroupRoot() == tail.getVisibleGroupRoot() &&
                  head != tail) || (head == tail && !head.isVisible()))
               // Inter-group edge.
               continue;
         
            boolean arrow_only = false;
            if(directed)
               arrow_only = drawBackEdge_(tail.getIndex(), head.getIndex());
         
            boolean sel = edge.selected;
            sel |= head.getVisibleGroupRoot().getSelected();
            sel |= tail.getVisibleGroupRoot().getSelected();
            if(sel == selected)
               if(directed || tail.getIndex() <= head.getIndex()) {
                  edge.draw(graphics, viewTransform_, xyPlane_,
                            directed, arrow_only, quality_, this, which_gr);
                  graphics.dispose();
                  if(which_gr == 0)
                     graphics = getGraphicsInternal_();
                  else
                     graphics = getBackGraphics_();
               }
         }
      }
   
   
   
   
   
   
      public boolean mouseDown(Event e, int x_in, int y_in)
      {
         if(currentMouseAction_ == 2 && newEdgeNode_ != null)
         {
            if((e.modifiers & Event.SHIFT_MASK) == 0) {
               Node tmpnode = findNearestNode_(x_in, y_in, false);
            
               if(tmpnode != null) {  // Finish edge.
                  if(pathLength_ == 0 && newEdgeNode_ != tmpnode)
                     graph_.insertEdge(graph_.getIndexFromNode(newEdgeNode_),
                                       graph_.getIndexFromNode(tmpnode));
                  else {
		     DPoint3[] tmp_array;
                     if(pathLength_ == 0) { // Must be self-edge with no intermediate points
			// Add some intermediate points.
			tmp_array = new DPoint3[2];
			DPoint3 pos = tmpnode.getPosition3();
			pos.transform(viewTransform_);
			DPoint3 pos2 = new DPoint3(pos);
			DDimension3 size = tmpnode.getBoundingBox3();
			double w = size.width / 2.0 * scale_;
			if(w < 10)  w = 10;
			double h = size.height / 2.0 * scale_ + w * 1.5;
			pos.translate(-w, -h, 0);
			pos2.translate(w, -h, 0);
			pos.transform(moveTransform_);
			pos2.transform(moveTransform_);
			tmp_array[0] = pos;
			tmp_array[1] = pos2;
		     }
		     else {
                        tmp_array = new DPoint3[pathLength_];
                        System.arraycopy(pathArray_, 0, tmp_array, 0, pathLength_);
		     }
                     graph_.insertEdge(graph_.getIndexFromNode(newEdgeNode_),
                                       graph_.getIndexFromNode(tmpnode), tmp_array);
                  }
                  currentMouseAction_ = 0;
                  paintOver();
               }
            }
            else  // New edge path point.
            {
               DPoint3 pos = new DPoint3(x_in, y_in, 0.0);
               pos.transform(moveTransform_);
            
               DPoint3 p2;
               if(lastEdgePoint_ != null)
                  p2 = new DPoint3(lastEdgePoint_);
               else
                  p2 = newEdgeNode_.intersectWithLineTo(pos, xyPlane_,
                                                        quality_);
               p2.transform(viewTransform_);
            
               Graphics bgraphics = getBackGraphics_();
               bgraphics.setColor(Color.black);
            
               bgraphics.drawLine((int)x_in, (int)y_in, (int)p2.x, (int)p2.y);
               bgraphics.dispose();
               Graphics graphics = getGraphics();
               graphics.drawImage(backImage_, 0, 0, null);
               graphics.dispose();
            
               lastEdgePoint_ = pos;
            
               if(pathLength_ >= pathArraySize_)
               {
                  pathArraySize_ = pathLength_ * 2;
                  DPoint3[] new_array = new DPoint3[pathArraySize_];
                  System.arraycopy(pathArray_, 0, new_array, 0, pathLength_);
                  pathArray_ = new_array;
               }
               pathArray_[pathLength_] = lastEdgePoint_;
               pathLength_++;
            
               return false;
            }
         
            return false;
         }
      
      	// Avoid overlapping mouse events.
         if(currentMouseAction_ != 0)
            return false;
      
         DPoint3 pos = new DPoint3(x_in, y_in, 0.0);
         pos.transform(moveTransform_);
      
         movingZ_ = 0.0;
      
      // Determine which action to take.
         if((e.modifiers & (Event.META_MASK | Event.ALT_MASK | Event.CTRL_MASK)) == 0)  // button 1
         {
            currentMouseAction_ = 1;
            if(mouseMode_ == CREATE_EDGES)
               currentMouseAction_ = 2;
            else if(mouseMode_ == SELECT_NODES || mouseMode_ == SELECT_EDGES ||
                    mouseMode_ == SELECT_BOTH)
               currentMouseAction_ = 3;
         }
         else if((e.modifiers & (Event.ALT_MASK | Event.CTRL_MASK)) != 0)  // button 2
            currentMouseAction_ = 2;
         else
            currentMouseAction_ = 3;
      
         if(currentMouseAction_ == 1)
         {
            if(selectedNode_ != null || selectedEdge_ != null)
            {
               unselectItems();
               paintOver();
            }
         
            int index = graph_.insertNode();
         
            movingNode_ = graph_.getNodeFromIndex(index);
         
            movingNode_.setPosition(pos);
         
         
            DDimension bbox = movingNode_.getBoundingBox();
         
            if(scaleBounds_)
            {
               bbox.width /= scale_;
               bbox.height /= scale_;
            }
         
            movingNode_.setBoundingBox(bbox.width, bbox.height);
         
            Graphics graphics = getGraphicsInternal_();
            graphics.setColor(Color.black);
         
            movingNode_.draw(this, graphics, viewTransform_, quality_);
            graphics.dispose();
         }
         else if(currentMouseAction_ == 2)
         {
            newEdgeNode_ = null;
            lastEdgePoint_ = null;
         
            if(selectedNode_ != null || selectedEdge_ != null)
            {
               unselectItems();
               paintOver();
            }
         
            if((newEdgeNode_ = findNearestNode_(x_in, y_in, false)) != null)
            {
               pathLength_ = 0;
               pathArraySize_ = 10;  // Arbitrary value.
               pathArray_ = new DPoint3[pathArraySize_];
            }
         }
         else if(currentMouseAction_ == 3) // Select object.
         {
            selected_ = NONE_;
         
            if(selectedEdge_ != null && e.clickCount == 2)
            {
               setEdgeProperties(false);
               return false;
            }
         
         
            if(selectedNode_ != null)
            {
               if(e.clickCount == 2)
               {
                  setNodeProperties(false);
                  return false;
               }
            
               for(Node tmpnode = graph_.firstNode(); tmpnode != null && selected_ == NONE_;
                   tmpnode = graph_.nextNode(tmpnode))
                  if(tmpnode.getSelected() && tmpnode.isVisible())
                  {
                     selectedNode_ = tmpnode;
                  
                     DPoint3 posc = tmpnode.getPosition3();
                     DDimension bbox = tmpnode.getBoundingBox();
                  
                     posc.transform(viewTransform_);
                  
                     DPoint tr = new DPoint(posc.x + bbox.width / 2.0 * scale_ + 1,
                                            posc.y - bbox.height / 2.0 * scale_ - 1);
                     DPoint bl = new DPoint(posc.x - bbox.width / 2.0 * scale_ - 1,
                                            posc.y + bbox.height / 2.0 * scale_ + 1);
                  
                     double dist = 16.0, newdist;
                     newdist = (posc.x - x_in) * (posc.x - x_in) + (posc.y - y_in) * (posc.y - y_in);
                     if(newdist < dist)
                     {
                        selected_ = CENTER_;
                        dist = newdist;
                     }
                     newdist = (tr.x - x_in) * (tr.x - x_in) + (tr.y - y_in) * (tr.y - y_in);
                     if(newdist < dist)
                     {
                        selected_ = CORNER_;
                        if(bbox.height == 0.0 && bbox.width == 0.0)
                           selectedRatio_ = 1;
                        else
                           selectedRatio_ = bbox.height / bbox.width;
                        dist = newdist;
                     }
                     newdist = (posc.x - x_in) * (posc.x - x_in) + (bl.y - y_in) * (bl.y - y_in);
                     if(newdist < dist)
                     {
                        selected_ = BOTTOM_;
                        dist = newdist;
                     }
                     newdist = (bl.x - x_in) * (bl.x - x_in) + (posc.y - y_in) * (posc.y - y_in);
                     if(newdist < dist)
                        selected_ = LEFT_;
                  
                  }
            
            
            
               if(selected_ != NONE_)
               {
                  DPoint3 tmppos = selectedNode_.getPosition3();
                  tmppos.transform(viewTransform_);
                  movingZ_ = tmppos.z;
                  movingX_ = tmppos.x;
                  movingY_ = tmppos.y;
               
                  for(Node tmpnode = graph_.firstNode(); tmpnode != null;
                      tmpnode = graph_.nextNode(tmpnode))
                     tmpnode.saveState();
                  Enumeration edges = graph_.getEdges();
                  while(edges.hasMoreElements())
                     ((Edge)(edges.nextElement())).saveState();
                  moveX_ = (double)x_in;
                  moveY_ = (double)y_in;
               }
            }
            if(selected_ == NONE_)
            {
               if((e.modifiers & Event.SHIFT_MASK) == 0)
                  unselectItems();
            
               Node tmpnode = findNearestNode_(x_in, y_in, true);
               Point tmpedge;
               if(mouseMode_ != SELECT_EDGES && tmpnode != null)
               {
                  selectedNode_ = tmpnode;
                  selectedNode_.setSelected(true);
                  selected_ = NONE_;
               }
               else if(mouseMode_ != SELECT_NODES &&
                          (tmpedge = findNearestEdge_(x_in, y_in)) != null)
               {
                  selectedEdge_ = tmpedge;
                  setEdgeSelected_(selectedEdge_.x, selectedEdge_.y, true);
               }
               else
               {
                  currentMouseAction_ = 4;  // Multiple select.
                  multiSelectX_ = x_in;
                  multiSelectY_ = y_in;
                  multiSelectX2_ = -1;  // First time flag.
               }
            }
            //paintOver(currentMouseAction_ == 4);
         }
      
         String string = getLabel_(pos.x, pos.y, pos.z, true);
      
         getParent().postEvent(new Event((Object)this, OffsetCanvas.LABEL, string));
      
         return false;
      }
   
   
   
   
   
      public boolean mouseDrag(Event e, int x_in, int y_in)
      {
         DPoint3 pos = new DPoint3(x_in, y_in, movingZ_);
         DPoint3 vpos = new DPoint3(pos);
         pos.transform(moveTransform_);
      
         if(currentMouseAction_ == 1)
         {
            Graphics graphics = getGraphicsInternal_();
         
            graphics.setColor(Color.black);
            movingNode_.setPosition(pos);
            graphics.drawImage(backImage_, 0, 0, null);
            movingNode_.draw(this, graphics, viewTransform_, quality_);
            graphics.dispose();
         }
         
         else if(currentMouseAction_ == 2 && newEdgeNode_ != null)
         {
            DPoint3 p2;
            if(lastEdgePoint_ != null)
               p2 = new DPoint3(lastEdgePoint_);
            else
               p2 = newEdgeNode_.intersectWithLineTo(pos, xyPlane_,
                                                     quality_);
         
            p2.transform(viewTransform_);
         
            Graphics graphics = getGraphics();
            graphics.setColor(Color.black);
         
            graphics.drawImage(backImage_, 0, 0, null);
            graphics.drawLine((int)x_in, (int)y_in, (int)p2.x, (int)p2.y);
            graphics.dispose();
         }
         
         else if(currentMouseAction_ == 3 && selectedNode_ != null)
         {
            /* If a node is dragged far enough, start moving it. */
            if(selected_ == NONE_) {
               DPoint3 tmppos = selectedNode_.getPosition3();
               tmppos.transform(viewTransform_);

               moveX_ = tmppos.x;
               moveY_ = tmppos.y;
               int xoffs = x_in - (int)moveX_;
               int yoffs = y_in - (int)moveY_;
	       if(xoffs * xoffs + yoffs * yoffs > 9 /* Three pixels */) {
                  selected_ = CENTER_;

                  movingZ_ = tmppos.z;
                  movingX_ = tmppos.x;
                  movingY_ = tmppos.y;
               
                  for(Node tmpnode = graph_.firstNode(); tmpnode != null;
                      tmpnode = graph_.nextNode(tmpnode))
                     tmpnode.saveState();
                  Enumeration edges = graph_.getEdges();
                  while(edges.hasMoreElements())
                     ((Edge)(edges.nextElement())).saveState();
		  paintOver();
		  }
            }

            if(selected_ == CENTER_)
            {
               int xoffs = x_in - (int)moveX_;
               int yoffs = y_in - (int)moveY_;

               for(Node tmpnode = graph_.firstNode(); tmpnode != null;
                   tmpnode = graph_.nextNode(tmpnode))
                  if(tmpnode.getSelected())
                     tmpnode.slide(moveTransform_, viewTransform_, xoffs, yoffs);
            
               Enumeration edges = graph_.getEdges();
               while(edges.hasMoreElements())
               {
                  Edge edge = (Edge)(edges.nextElement());
                  if(edge.tail().getSelected() && edge.head().getSelected())
                     edge.slide(moveTransform_, viewTransform_, xoffs, yoffs);
               }
            
            }
            else if(selected_ != NONE_)
            {
               double ratiox = 1.0, ratioy = 1.0, ratioz = 1.0;
               if(selected_ == CORNER_)
                  ratiox = ratioy = ratioz = Math.max(
                                                     Math.abs(x_in - movingX_) / Math.abs(moveX_ - movingX_),
                                                     Math.abs(y_in - movingY_) / Math.abs(moveY_ - movingY_));
               else if(selected_ == BOTTOM_)
                  ratioy = Math.abs(y_in - movingY_) / Math.abs(moveY_ - movingY_);
               else if(selected_ == LEFT_)
                  ratiox = Math.abs(x_in - movingX_) / Math.abs(moveX_ - movingX_);
               for(Node tmpnode = graph_.firstNode(); tmpnode != null;
                   tmpnode = graph_.nextNode(tmpnode))
                  if(tmpnode.getSelected())
                     tmpnode.scale(ratiox, ratioy, ratioz);
            }
         
            drawSelectedNodes_();
         }
         else if(currentMouseAction_ == 4)
         {
            multiSelectX2_ = x_in;
            multiSelectY2_ = y_in;
            drawSelectRect_();
         }
      
         String string = getLabel_(pos.x, pos.y, pos.z, true);
      
         getParent().postEvent(new Event((Object)this, OffsetCanvas.LABEL, string));
      
         return false;
      }
   
   
   
   
   
      public boolean mouseMove(Event event, int x_in, int y_in)
      {
         mouseDrag(event, x_in, y_in);
         return false;
      }
   
   
   
   
   
      public boolean mouseUp(Event e, int x_in, int y_in)
      {
         DPoint3 pos = new DPoint3(x_in, y_in, movingZ_);
         pos.transform(moveTransform_);
      
         if(currentMouseAction_ == 1)  // button 1
         {
            computeBounds_();
            getParent().postEvent(new Event((Object)this, RESIZE, (Object)this));
            paintOver();
         }
         else if(currentMouseAction_ == 2 && newEdgeNode_ != null)
            // Creating an edge, do nothing.
         {
            return false;
         }
         else if(currentMouseAction_ == 3)
         {
            computeBounds_();
            getParent().postEvent(new Event((Object)this, RESIZE, (Object)this));
            paintOver();
         }
         else if(currentMouseAction_ == 4)
         {
            multiSelect_();
         }
      
         currentMouseAction_ = 0;
      
         String string = getLabel_(pos.x, pos.y, pos.z, true);
      
         getParent().postEvent(new Event((Object)this, OffsetCanvas.LABEL, string));
      
         return false;
      }
   
   
   
   
      public boolean mouseExit(Event event, int x_in, int y_in)
      {
         String string = getLabel_(0, 0, 0, false);
      
         getParent().postEvent(new Event((Object)this, OffsetCanvas.LABEL, string));
      
         return false;
      }
   
   
   
   
   
   
      public DDimension contentsSize()
      {
         double w = width_ * scale_ + (double)windowSize_.width * 2.0;
         double h = height_ * scale_ + (double)windowSize_.height * 2.0;
      
         return new DDimension(w, h);
      }
   
   
      public void setOffsets(double xoffset, double yoffset, boolean redraw)
      {
         offsetx_ = xoffset / scale_;
         offsety_ = yoffset / scale_;
         computeDrawOffset_();
         if(redraw)
            paintOver();
      }
   
   
   
   
      private void computeDrawOffset_()
      {
         setOffsets_((-offsetx_ - minx_) * scale_ + windowSize_.width,
                        (-offsety_ - miny_) * scale_ + windowSize_.height);
      }
   
   
   
   
      private void setOffsets_(double offx, double offy)
      {
         offset_.x = offx;
         offset_.y = offy;
      
         shiftMatrix_.matrix[0][3] = offx;
         shiftMatrix_.matrix[1][3] = offy;
      
         updateViewTransform_();
      }
   
   
   
      public DPoint getOffset()
      {
         DPoint val = new DPoint(0, 0);
      
         val.x = -(offset_.x - (double)windowSize_.width) - minx_ * scale_;
         val.y = -(offset_.y - (double)windowSize_.height) - miny_ * scale_;
      
         return val;
      }
   
   
   
      private void computeBounds_()
      {
         double	oldminx = minx_;
         double	oldminy = miny_;
      
         minx_ = miny_ = maxx_ = maxy_ = 0;
      
         Node tmpnode = graph_.firstNode();
         DPoint tmppoint;
         DDimension tmpdim;
      
         if(tmpnode != null)
         {
            tmppoint = tmpnode.getPosition();
            tmpdim = tmpnode.getBoundingBox();
         
            minx_ = tmppoint.x - tmpdim.width / 2.0;
            maxx_ = tmppoint.x + tmpdim.width / 2.0;
            miny_ = tmppoint.y - tmpdim.height / 2.0;
            maxy_ = tmppoint.y + tmpdim.height / 2.0;
         
            tmpnode = graph_.nextNode(tmpnode);
            while(tmpnode != null)
            {
               tmppoint = tmpnode.getPosition();
               tmpdim = tmpnode.getBoundingBox();
            
               double w = tmpdim.width / 2.0;
               double h = tmpdim.height / 2.0;
            
               if(tmppoint.x - w < minx_)  
                  minx_ = tmppoint.x - w;
               if(tmppoint.x + w > maxx_)  
                  maxx_ = tmppoint.x + w;
               if(tmppoint.y - h < miny_)  
                  miny_ = tmppoint.y - h;
               if(tmppoint.y + h > maxy_)  
                  maxy_ = tmppoint.y + h;
               tmpnode = graph_.nextNode(tmpnode);
            }
         }
      
      	// 3D approach.
         double maxdim = Math.abs(maxy_);
         if(Math.abs(miny_) > maxdim)
            maxdim = Math.abs(miny_);
         if(Math.abs(minx_) > maxdim)
            maxdim = Math.abs(minx_);
         if(Math.abs(maxx_) > maxdim)
            maxdim = Math.abs(maxx_);
      
         maxx_ = maxy_ = maxdim;
         minx_ = miny_ = -maxdim;
         width_ = height_ = 2.0 * maxdim;
      }
   
   
   
   
   
      private Node findNearestNode_(double x, double y, boolean group_nodes)
      {
         Node node = null;
      
         Node tmpnode;
         DPoint3 pos;
         DDimension bbox;
      
         double closest = (width_ + height_) * scale_;
      
         for(tmpnode = graph_.firstNode(); tmpnode != null; tmpnode = graph_.nextNode(tmpnode))
         {
            if(!tmpnode.isVisible() || (!group_nodes && tmpnode.isGroup()))
               continue;
            pos = tmpnode.getPosition3();
            bbox = tmpnode.getBoundingBox();
         
            pos.transform(viewTransform_);
         
            pos.x = Math.abs(pos.x - x);
            pos.y = Math.abs(pos.y - y);
         
         
            if(pos.x < bbox.width / 2.0 * scale_ + 1 && pos.y < bbox.height / 2.0 * scale_ + 1)
            {
               if(pos.x + pos.y < closest)  // rough estimate of closeness
               {
                  closest = pos.x + pos.y;
                  node = tmpnode;
               }
            }
         }
         return node;
      }
   
   
   
   
   
   
   
   
      private Point findNearestEdge_(double x, double y)
      {
         Point edge = null;
      
         Node tmpnode;
         DPoint3 pos = new DPoint3(), pos2 = new DPoint3();
         DDimension bbox;
      
         double closest = width_ + height_;
      
         double xd, yd, dist;
      
         for(tmpnode = graph_.firstNode(); tmpnode != null; tmpnode = graph_.nextNode(tmpnode))
         {
            if(tmpnode.isGroup())
               continue;
            for(int child = tmpnode.firstChild(); child != -1; child = tmpnode.nextChild())
            {
               Node childnode = graph_.getNodeFromIndex(child);
               if(childnode.isGroup())
                  continue;
            
               int npoints = 0;
               DPoint3[] path = graph_.getEdgePathPoints(tmpnode.getIndex(), child);
               if(path != null)
                  npoints = path.length;
            
               for(int pointindex = 0; pointindex <= npoints; pointindex++)
               {
                  if(pointindex == 0)
                     pos = tmpnode.getPosition3();
                  else
                     pos.move(path[pointindex - 1]);
               
                  if(pointindex == npoints)
                     pos2 = childnode.getPosition3();
                  else
                     pos2.move(path[pointindex]);
               
                  pos.transform(viewTransform_);
                  pos2.transform(viewTransform_);
               
                  if((x >= pos.x - 1 && x <= pos2.x + 1 || x >= pos2.x - 1 &&
                        x <= pos.x + 1) &&
                        (y >= pos.y - 1 && y <= pos2.y + 1 ||
                         y >= pos2.y - 1 && y <= pos.y + 1))
                  {
                     double dx = pos2.x - pos.x;
                     double dy = pos2.y - pos.y;
                  
                     if(dx == 0.0 || dy == 0.0)
                        dist = 0.0;
                     else
                     {
                     // x distance to line
                        xd = Math.abs((dx / dy) * (y - pos.y) + pos.x - x);
                     
                     // y distance to line
                        yd = Math.abs((dy / dx) * (x - pos.x) + pos.y - y);
                     
                        dist = Math.min(xd, yd);
                     }
                  
                     if(dist < 3.0 && dist < closest)
                     {
                        closest = dist;
                        edge = new Point(tmpnode.getIndex(), child);
                     }
                  
                  }
               }
            }
         }
         return edge;
      }
   
   
   
   
   
   
   
   
   
      private void drawSelectedNodes_()
      {
         if(selectedNode_ == null)
            return;
      
         Graphics graphics = getGraphicsInternal_();
      
         graphics.setColor(Color.black);
         graphics.setPaintMode();
         graphics.drawImage(backImage_, 0, 0, null);
         setWireframe(true);
         drawObjects_(true, graphics, 0);
         setWireframe(false);
         graphics.dispose();
      }
   
   
   
   
   
   
   
   
      private String getLabel_(double x, double y, double z, boolean mousein)
      {
         String string = new String();
      
         if(mousein)
            string = string.concat("x: " + x + "  y: " + y + "  z: " + z);
      
         if(selectedNode_ != null)
         {
            DPoint3 pos = selectedNode_.getPosition3();
            DDimension3 bbox = selectedNode_.getBoundingBox3();
            string = string.concat("   Node " + selectedNode_.getIndex() +
                                   "   x: " + pos.x + "  y: " + pos.y + "  z: " + pos.z +
                                   "   w: " + bbox.width + "  h: " + bbox.height + "  d: " +
                                   bbox.depth);
         }
      
         if(selectedEdge_ != null)
         {
            string = string.concat("   Edge (" + selectedEdge_.x + "," +
                                   selectedEdge_.y + ")");
         }
      
         return string;
      }
   
   
   
   
      public void setScale(double new_scale)
      {
      	// Scale about the center - compute new offset to keep centered
         setOffsets_(windowSize_.width / 2.0 - (new_scale / scale_) * (windowSize_.width / 2.0 - offset_.x),
                     windowSize_.height / 2.0 - (new_scale / scale_) * (windowSize_.height / 2.0 - offset_.y));
      
         scale_ = new_scale;
      
         scaleMatrix_.matrix[0][0] = scaleMatrix_.matrix[1][1] =
                              scaleMatrix_.matrix[2][2] = scale_;
      
         updateViewTransform_();
      
         getParent().postEvent(new Event((Object)this, RESIZE, (Object)this));
      
         paintOver();
      }
   
   
   
   
   
      public void setViewAngles(double theta, double phi)
      {
         rotxMatrix_.matrix[1][1] = rotxMatrix_.matrix[2][2] = -Math.cos(-phi + Math.PI / 2.0);
         rotxMatrix_.matrix[2][1] = -(rotxMatrix_.matrix[1][2] = -Math.sin(-phi + Math.PI / 2.0));
      
         rotzMatrix_.matrix[0][0] = rotzMatrix_.matrix[1][1] = Math.cos(-theta);
         rotzMatrix_.matrix[1][0] = -(rotzMatrix_.matrix[0][1] = -Math.sin(-theta));
      
         updateViewTransform_();
      
         xyPlane_ = (theta == 0.0 && phi == Math.PI / 2.0);
      
         paintOver();
      }
   
   
   
   
      public DPoint3 getCenter()
      {
         DPoint3 retval = new DPoint3(windowSize_.width / 2.0,
                                      windowSize_.height / 2.0, 0);
         retval.transform(moveTransform_);
      
         return retval;
      }
   
   
   
   
      public void update(boolean adjust_bounds)
      {
         unselectItems();
         currentMouseAction_ = 0;
      
         if(adjust_bounds)
         {
            computeBounds_();
            getParent().postEvent(new Event((Object)this, RESIZE, (Object)this));
         
            String string = getLabel_(0, 0, 0, false);
            getParent().postEvent(new Event((Object)this, OffsetCanvas.LABEL, string));
         
         }
         paintOver();
      
         getParent().postEvent(new Event((Object)this, UPDATE, null));
      }
   
   
   
      public void scale(double scaleval)
      {
         setScale(scaleval);
      }
   
   
   
      public void center()
      {
         computeBounds_();
      
         setOffsets_(.5 * (windowSize_.width - (minx_ + maxx_) * scale_),
                     .5 * (windowSize_.height - (miny_ + maxy_) * scale_));
         getParent().postEvent(new Event((Object)this, RESIZE, (Object)this));
      
         paintOver();
      }
   
   
   
   
      public Frame getFrame()
      {
         return frame_;
      }
   
   
      public Graph getGraph()
      {
         return graph_;
      }
   
   
   
      public void deleteSelected(boolean group_warning)
      {
         if(selectedNode_ != null || selectedEdge_ != null)
         {
            if(selectedNode_ != null)
            {
            
               if(group_warning) {
                  boolean group = false;
                  for(Node tmpnode = graph_.firstNode(); tmpnode != null;
                      tmpnode = graph_.nextNode(tmpnode))
                     if(tmpnode.getSelected() && tmpnode.isGroup()) {
                        group = true;
                        break; }
               
                  if(group) {
                     new GroupWarningDialog(frame_, this);
                     return;
                  }
               }
            
            
               for(Node tmpnode = graph_.firstNode(); tmpnode != null;
                   tmpnode = graph_.nextNode(tmpnode))
                  if(tmpnode.getSelected())
                     graph_.removeNode(tmpnode);
               selectedNode_ = null;
               computeBounds_();
               getParent().postEvent(new Event((Object)this, RESIZE, (Object)this));
            }
            if(selectedEdge_ != null)
            {
               Enumeration edges = graph_.getEdges();
               while(edges.hasMoreElements())
               {
                  Edge edge = (Edge)(edges.nextElement());
                  if(edge.selected)
                     graph_.removeEdge(edge);
               }
            }
            paintOver();
         }
         else if(currentMouseAction_ == 2)  // Drawing edge.
         {
            currentMouseAction_ = 0;
            paintOver();
         }
      }
   
   
   
   
      public Node getSelectedNode()
      {
         return selectedNode_;
      }
   
   
   
      public void unselectItems()
      {
         if(selectedNode_ == null && selectedEdge_ == null)
            return;
         for(Node tmpnode = graph_.firstNode(); tmpnode != null;
             tmpnode = graph_.nextNode(tmpnode))
            tmpnode.setSelected(false);
         selectedNode_ = null;
      
         Enumeration edges = graph_.getEdges();
         while(edges.hasMoreElements())
            ((Edge)(edges.nextElement())).selected = false;
         selectedEdge_ = null;
      
         paintOver();
      }
   
   
   
   
      public void scaleBounds(boolean sb)
      {
         scaleBounds_ = sb;
      }
   
   
   
   
      public void setMouseMode(int mode)
      {
         mouseMode_ = mode;
      }
   
   
   
   
      public DRect windowRect()
      {
         return new DRect(-offset_.x / scale_, -offset_.y / scale_, windowSize_.width / scale_, windowSize_.height / scale_);
      }
   
   
   
      public void setDirected(boolean directed)
      {
         graph_.setDirected(directed);
         paintOver();
      
         getParent().postEvent(new Event((Object)this, UPDATE, null));
      }
   
   
   
      private synchronized void updateViewTransform_()
      {
         viewTransform_ = new Matrix44(shiftMatrix_);
         viewTransform_.mult(scaleMatrix_);
         viewTransform_.mult(rotxMatrix_);
         viewTransform_.mult(rotzMatrix_);
      
      
      
         moveTransform_ = new Matrix44(rotzMatrix_);
         moveTransform_.matrix[1][0] = -moveTransform_.matrix[1][0];
         moveTransform_.matrix[0][1] = -moveTransform_.matrix[0][1];
      
         Matrix44 tmp_matrix = new Matrix44(rotxMatrix_);
         tmp_matrix.matrix[2][1] = -tmp_matrix.matrix[2][1];
         tmp_matrix.matrix[1][2] = -tmp_matrix.matrix[1][2];
         moveTransform_.mult(tmp_matrix);
      
         tmp_matrix.setTo(scaleMatrix_);
         tmp_matrix.matrix[0][0] = 1.0 / tmp_matrix.matrix[0][0];
         tmp_matrix.matrix[1][1] = 1.0 / tmp_matrix.matrix[1][1];
         tmp_matrix.matrix[2][2] = 1.0 / tmp_matrix.matrix[2][2];
         moveTransform_.mult(tmp_matrix);
      
         tmp_matrix.setTo(shiftMatrix_);
         tmp_matrix.matrix[0][3] = - tmp_matrix.matrix[0][3];
         tmp_matrix.matrix[1][3] = - tmp_matrix.matrix[1][3];
         moveTransform_.mult(tmp_matrix);
      
         viewTransform_.scale = scale_;
      }
   
   
   
      private synchronized void drawAxes_(Graphics graphics)
      {
         String letter;
         double lx, ly;
         FontMetrics fm = graphics.getFontMetrics();
 
         graphics.setColor(Color.black);
      
         DPoint3 p2 = new DPoint3();
         p2.move(25, 0, 0);
         p2.transform(rotzMatrix_);
         p2.transform(rotxMatrix_);
         graphics.drawLine(40, 40, 40 + (int)p2.x, 40 + (int)p2.y);
      
         p2.move(32, 0, 0);
         p2.transform(rotzMatrix_);
         p2.transform(rotxMatrix_);
         letter = new String("X");
         lx = p2.x - fm.stringWidth(letter) / 2.0;
         ly = p2.y + fm.getAscent() / 2.0;
         graphics.drawString(letter, 40 + (int)lx, 40 + (int)ly);
      
      
         p2.move(0, 25, 0);
         p2.transform(rotzMatrix_);
         p2.transform(rotxMatrix_);
         graphics.drawLine(40, 40, 40 + (int)p2.x, 40 + (int)p2.y);
      
         p2.move(0, 32, 0);
         p2.transform(rotzMatrix_);
         p2.transform(rotxMatrix_);
         letter = new String("Y");
         lx = p2.x - fm.stringWidth(letter) / 2.0;
         ly = p2.y + fm.getAscent() / 2.0;
         graphics.drawString(letter, 40 + (int)lx, 40 + (int)ly);
      
      
         p2.move(0, 0, 25);
         p2.transform(rotzMatrix_);
         p2.transform(rotxMatrix_);
         graphics.drawLine(40, 40, 40 + (int)p2.x, 40 + (int)p2.y);
      
         p2.move(0, 0, 32);
         p2.transform(rotzMatrix_);
         p2.transform(rotxMatrix_);
         letter = new String("Z");
         lx = p2.x - fm.stringWidth(letter) / 2.0;
         ly = p2.y + fm.getAscent() / 2.0;
         graphics.drawString(letter, 40 + (int)lx, 40 + (int)ly);
      }
   
   
   
      // See if an edge has an identical back edge and head index > tail index.
      private boolean drawBackEdge_(int n1, int n2)
      {
         if(n1 <= n2 || graph_.getEdge(n2, n1) == null)
            return false;
      
         DPoint3[] path1 = graph_.getEdgePathPoints(n1, n2);
         DPoint3[] path2 = graph_.getEdgePathPoints(n2, n1);
         if(path1.length != path2.length)
            return false;
         for(int pt = 0; pt < path1.length; pt++)
            if(!path1[pt].equals(path2[path1.length - 1 - pt]))
               return false;
         return true;
      }
   
   
   
   
   
   
   
   
   
      // Construct a PostScript file for the graph
      public String toPS(double width, double height, double pagewidth,
                         double pageheight, double fontsize, double margin,
                         double overlap, boolean landscape)
      {
         String result = new String();
      
         if(landscape)
         {
            double tmp = pagewidth;
            pagewidth= pageheight;
            pageheight = tmp;
         }
      
         // Get graph drawn boundaries.
         DPoint graphwidth = new DPoint(), graphheight = new DPoint();
         getDrawBounds_(graphwidth, graphheight);
      
         int pages_wide = (int)Math.ceil(width / pagewidth);
         int pages_high = (int)Math.ceil(height / pageheight);
      
         // Adjust for margins and overlap.
         width -= 2.0 * margin + ((double)pages_wide - 1.0) * overlap;
         height -= 2.0 * margin + ((double)pages_high - 1.0) * overlap;
      
         // Convert to points.
         width *= 72.0;
         height *= 72.0;
         margin *= 72.0;
         overlap *= 72.0;
         pagewidth *= 72.0;
         pageheight *= 72.0;
      
         height -= fontsize; // Room for a label at the bottom.
      
         // Adjust width or height according to graph bounds.
         double ratio = (graphwidth.y - graphwidth.x) /
            (graphheight.y - graphheight.x);
         if(ratio > width / height)
            height = width / ratio;
         else
            width = height * ratio;
      
         // One of theses may change if the whole bounds were not needed.
         pages_wide = (int)Math.ceil((width - overlap + 2.0 * margin) /
                                        (pagewidth - overlap));
         pages_high = (int)Math.ceil((height + fontsize - overlap + 2.0
                                        * margin) / (pageheight - overlap));
      
      
         double scale = width / (graphwidth.y - graphwidth.x);
      
      
         result += "%!PS-Adobe-3.0\n\n%%BoundingBox: 0 0 612 792\n" +
                   "%% Pages: " + (pages_wide * pages_high) + "\n%% EndComments\n\n";
         result += "/ellipse\n  {\n  gsave\n  newpath\n  /h exch def\n" +
                   "  /w exch def\n" +
                   "  translate\n  1 h w div scale\n  0 0 w 2 div 0 360 arc\n" +
                   "  1 w h div scale\n  stroke\n  grestore\n" +
                   "  }\ndef\n\n";
      
         result += "/rectangle\n  {\n  newpath\n  /h exch def\n  /w exch def\n" +
                   "  moveto\n  w 2 div h 2 div rmoveto\n" +
                   "  w neg 0 rlineto\n  0 h neg rlineto\n  w 0 rlineto\n  closepath\n" +
                   "  stroke\n}\ndef\n\n";
      
         result += "/label\n  {\n  gsave\n  newpath\n" +
                   "  /type exch def\n  type 2 eq\n" +
                   "  {  /h exch def  /w exch def  } if" +
                   "  translate\n  1 -1 scale\n  dup dup length /rows exch def\n" +
                   "  /sw 0 def\n  {\n    stringwidth pop /csw exch def\n" +
                   "    csw sw gt\n    { /sw csw def }\n    if" +
                   "  } forall\n" +
                   "  type 0 eq\n  { sw 2 div neg fontsize neg moveto } if\n" +
                   "  type 1 eq\n  { sw 2 div neg fontsize 2 div neg moveto } if\n" +
                   "  type 2 eq\n  { w 8 scl div sub sw div h 8 scl div sub fontsize rows mul div scale\n" +
                   "    sw 2 div neg rows 2 sub fontsize mul 2 div fontsize descent mul add moveto } if\n" +
                   "  {\n    currentpoint 3 2 roll\n" +
                   "    show\n    fontsize sub moveto\n  } forall\n" +
                   "  stroke\n  grestore\n}\ndef\n\n";
      
         result += "/inlabel\n  {\n  gsave\n  newpath\n" +
                   "  /h exch def\n  /w exch def\n" +
                   "  /y exch def\n  /x exch def\n" +
                   "  x y h 2 div sub translate\n  1 -1 scale\n" +
                   "  dup stringwidth pop\n  /sw exch def\n" +
                   "  w 8 scl div sub sw div h 8 scl div sub fontsize div scale\n" +
                   "  sw 2 div neg fontsize 2 div neg moveto\n  show\n" +
                   "  stroke\n  grestore\n}\ndef\n\n";
      
         result += "/arrow\n  {\n  newpath\n  /dy exch arrowsize mul def\n" +
                   "  /dx exch arrowsize mul def\n" +
                   "  /y exch def\n  /x exch def\n" +
                   "  /dy2 .7 dy mul def\n  /dx2 .7 dx mul def\n  x y moveto\n  dx dy rmoveto\n" +
                   "  dy2 dx2 neg rmoveto\n  x y lineto\n  dx dy rmoveto\n  dy2 neg dx2 rmoveto\n" +
                   "  x y lineto\n  stroke\n}\ndef\n\n";
      
         result += "/slantlabel\n  {\n  gsave\n  newpath\n  /angle exch def\n" +
                   "  translate\n  1 -1 scale\n  angle rotate\n" +
                   "  dup stringwidth pop 2 div neg fontsize 3 div moveto\n" +
                   "  show\n  stroke\n  grestore\n}\ndef\n\n";
      
         result += "/vgjimage\n  {\n  gsave\n  /ih exch def\n" +
                   "  /iw exch def\n  /imagedata exch def\n  /h exch def\n" +
                   "  /w exch def\n  translate\n  w h scale\n" +
                   "  iw ih 8 [iw 0 0 ih iw 2 div ih 2 div]" +
                   " { imagedata } image\n  grestore\n}\ndef\n\n";
      
      // Output the images - with no repeats.
         Node tmpnode;
         Image tmpimage;
         int nnodes = graph_.numberOfNodes();
         Image[] images = new Image[nnodes];
         int image_count = 0;
         for(tmpnode = graph_.firstNode(); tmpnode != null;
             tmpnode = graph_.nextNode(tmpnode)) {
            if((tmpimage = tmpnode.getImage()) != null) {
               int i;
               for(i = 0; i < image_count; i++)
                  if(images[i] == tmpimage)
                     break;
               if(i == image_count)
                  images[image_count++] = tmpimage;
            }
         }
      
         int i;
         for(i = 0; i < image_count; i++) {
            result += "/image" + i + "  {\n\n" + Node.imagePS(images[i]) +
                      "}\ndef\n\n";
         }
      
         result += "/graph\n{\n";
         result += "0 396 translate\n1 1 neg scale\n0 neg 396 neg translate\n\n";
      
         result += "/scl " + scale + " def\nscl scl scale\n.6 scl div setlinewidth\n" +
                   "/fontsize " + fontsize + " scl div def\n" +
                   "/Courier findfont fontsize scalefont setfont\n" +
                   "/Courier findfont\nbegin\nFontType 1 eq\n" +
                   "{ FontBBox }\n{ -2 -2 8 8 } ifelse\nend\n" +
                   "/y2 exch def pop /y1 exch def pop\n" +
                   "y1 neg y2 y1 sub div /descent exch def\n" +
                   "/arrowsize 5 scl div def\n";
         result += PSnum_(-graphwidth.x + margin / scale) +
                   PSnum_(-graphheight.x + margin / scale) + "translate\n\n";
      
      // First output the node images.
         for(tmpnode = graph_.firstNode(); tmpnode != null;
             tmpnode = graph_.nextNode(tmpnode))
            if((tmpimage = tmpnode.getImage()) != null)
               for(i = 0; i < image_count; i++)
                  if(images[i] == tmpimage)
                     result += tmpnode.toPSimage(i, viewTransform_);
      
         for(tmpnode = graph_.firstNode(); tmpnode != null;
             tmpnode = graph_.nextNode(tmpnode))
            result += tmpnode.toPS(viewTransform_);
      
         Enumeration edges = graph_.getEdges();
         boolean directed = graph_.isDirected();
         while(edges.hasMoreElements())
         {
            Edge edge = (Edge)(edges.nextElement());
            Node head = edge.head(), tail = edge.tail();
         
            if((head.getVisibleGroupRoot() == tail.getVisibleGroupRoot() &&
                  head != tail) || (head == tail && !head.isVisible()))
               // Inter-group edge.
               continue;
         
            if(directed || tail.getIndex() <= head.getIndex())
               result += edge.toPS(viewTransform_, xyPlane_, directed);
         }
      
      
         //drawAxes_(back_graphics);
      
         result += "}\ndef\n\n";
      
         int page = 1;
         for(int w = 0; w < pages_wide; w++)
            for(int h = 0; h < pages_high; h++)
            {
               result += "%%Page:" + page + " " + page + "\n";
               if(landscape)
                  result += "792 0 translate\n90 rotate\n";
            
            
               result += PSnum_(((double)w) * (-pagewidth + overlap)) +
                         PSnum_(((double)h) * (pageheight - overlap)) + "translate\n";
               result += "graph\n";
               result += "showpage\n\n";
            
               page++;
            }
      
         return result;
      }
   
   
   
   
   
   
   
      private String PSnum_(double num)
      {
         if(num > 0.0)
            return String.valueOf(num) + " ";
         else if(num < 0.0)
            return String.valueOf(-num) + " neg ";
         else 
            return "0 ";
      }
   
   
   
   
   
         // Get bounds of drawing with current transform.
      public void getDrawBounds_(DPoint width, DPoint height)
      {
         boolean first = true;
         DPoint tmpwidth = new DPoint(), tmpheight = new DPoint();
         DPoint3 pos = new DPoint3();
         Node tmpnode;
         for(tmpnode = graph_.firstNode(); tmpnode != null;
             tmpnode = graph_.nextNode(tmpnode))
         {
            tmpnode.getDrawBounds_(scale_, viewTransform_,
                                   tmpwidth, tmpheight);
            if(first)
            {
               first = false;
               width.move(tmpwidth);
               height.move(tmpheight);
            }
            else
            {
               width.x = Math.min(width.x, tmpwidth.x);
               width.y = Math.max(width.y, tmpwidth.y);
               height.x = Math.min(height.x, tmpheight.x);
               height.y = Math.max(height.y, tmpheight.y);
            }
         
            // Check edge path points.
            int tmpnode_index = tmpnode.getIndex();
            for(int child = tmpnode.firstChild(); child != -1; child = tmpnode.nextChild())
            {
               if(graph_.isDirected() || tmpnode_index <= child)
               {
                  DPoint3[] path = graph_.getEdgePathPoints(tmpnode_index,
                                                      child);
                  if(path != null && path.length > 0)
                     for(int i = 0; i < path.length; i++)
                     {
                        pos.move(path[i]);
                        pos.transform(viewTransform_);
                        if(first)
                        {
                           first = false;
                           width.move(pos.x, pos.x);
                           height.move(pos.y, pos.y);
                        }
                        else
                        {
                           width.x = Math.min(width.x, pos.x);
                           width.y = Math.max(width.y, pos.x);
                           height.x = Math.min(height.x, pos.y);
                           height.y = Math.max(height.y, pos.y);
                        }
                     }
               }
            }
         
         }
      
         if(first)
         {
            width.move(0.0, 1.0);
            height.move(0.0, 1.0);
         }
      
      }
   
   
   
   
      public void setNodeProperties(boolean always_default)
      {
         Node which = selectedNode_;
         if(always_default)
            which = null;
      
         if(propDialog_ == null)
            propDialog_ = new NodePropertiesDialog(frame_, which);
         else
            propDialog_.setNode(which);
      
         propDialog_.pack();
         propDialog_.show();
      
         if(!always_default)
            update(true);
      }
   
   
   
   
      public void setEdgeProperties(boolean always_default)
      {
         Edge which = graph_.getEdge(selectedEdge_.x, selectedEdge_.y);
         if(always_default)
            which = null;
      
         if(edgePropDialog_ == null)
            edgePropDialog_ = new EdgePropertiesDialog(frame_, which, graph_);
         else
            edgePropDialog_.setEdge(which, graph_);
      
         edgePropDialog_.pack();
         edgePropDialog_.show();
      
         if(!always_default)
            update(true);
      }
   
   
   
   
      private void drawSelectRect_()
      {
         Graphics graphics = getGraphics();
         graphics.setColor(Color.black);
      
         int sx = Math.min(multiSelectX_, multiSelectX2_);
         int sy = Math.min(multiSelectY_, multiSelectY2_);
         int sw = Math.abs(multiSelectX_ - multiSelectX2_);
         int sh = Math.abs(multiSelectY_ - multiSelectY2_);
         if(multiSelectX2_ != -1)
         {
            graphics.drawImage(backImage_, 0, 0, null);
            graphics.drawRect(sx, sy, sw, sh);
         }
         graphics.dispose();
      }
   
   
   
   
      private void multiSelect_()
      {
         if(multiSelectX2_ == -1)
            return;
      
         double x1 = (double)Math.min(multiSelectX_, multiSelectX2_);
         double y1 = (double)Math.min(multiSelectY_, multiSelectY2_);
         double x2 = (double)Math.max(multiSelectX_, multiSelectX2_);
         double y2 = (double)Math.max(multiSelectY_, multiSelectY2_);
      
         if(mouseMode_ != SELECT_EDGES)
         {
            Node tmpnode;
            for(tmpnode = graph_.firstNode(); tmpnode != null; tmpnode = graph_.nextNode(tmpnode))
            {
               DPoint3 pos = tmpnode.getPosition3();
               pos.transform(viewTransform_);
            
               if(pos.x >= x1 && pos.x <= x2 && pos.y >= y1 && pos.y <= y2)
               {
                  tmpnode.setSelected(true);
                  selectedNode_ = tmpnode;
               }
            }
         }
      
      
         if(mouseMode_ == SELECT_EDGES)
         {
            Enumeration edges = graph_.getEdges();
            boolean directed = graph_.isDirected();
            while(edges.hasMoreElements())
            {
               Edge edge = (Edge)(edges.nextElement());
               if(directed || edge.tail().getIndex() <= edge.head().getIndex())
                  // Check the edge.
               {
                  DPoint3 p1 = edge.tail().getPosition3();
                  p1.transform(viewTransform_);
                  DPoint3 p2 = edge.head().getPosition3();
                  p2.transform(viewTransform_);
                  if(p1.x >= x1 && p1.x <= x2 && p1.y >= y1 && p1.y <= y2 &&
                     p2.x >= x1 && p2.x <= x2 && p2.y >= y1 && p2.y <= y2)
                  {
                     edge.selected = true;
                     selectedEdge_ = new Point(edge.tail().getIndex(),
                                               edge.head().getIndex());
                  }
               }
            }
         }
      
         paintOver();
      }
   
   
   
      private void setEdgeSelected_(int n1, int n2, boolean state)
      { 
         if(!graph_.isDirected()) {
            Edge edge = graph_.getEdge(Math.min(n1, n2), Math.max(n1, n2));
            edge.selected = state;
         }
         else {
            Edge edge = graph_.getEdge(n1, n2);
            edge.selected = state;
         }
      }
   
   
   
   
      public void selectAll()
      {
         for(Node tmpnode = graph_.firstNode(); tmpnode != null;
             tmpnode = graph_.nextNode(tmpnode))
            tmpnode.setSelected(true);
         selectedNode_ = graph_.firstNode();
         paintOver();
      }
   
   
   
   
      public double getHSpacing()
      {
         return 1/scale_ * hSpacing;
      }
   
      public double getVSpacing()
      {
         return 1/scale_ * vSpacing;
      }
   
   
      public boolean handleEvent(Event e)
      {
         if(e.id == DragFix.QUEUED)
         {
            super.handleEvent((Event)e.arg);
            getParent().postEvent((Event)e.arg);
            return true;
         }
         dragFix_.queueEvent(e);
         return true;
      }
   
   
      public boolean keyDown(Event e, int key)
      {
         if(e.id == Event.KEY_PRESS) {
            if(key == 127)
            // Delete key.
               deleteSelected(true);
            else if((key == 'g' || key == 'u' || key == 'd' || key == 'c')) {
               groupControl(key);
            }
         }
      
         return true;
      }
   
   
   
   
      public synchronized void removeNotify()
      {
         dragFix_.killThread();
         super.removeNotify();
      }
   
   
   
      public Font getFont()
      { 
         return font_;
      }
   
   
   
      public void setFont(Font font)
      {
         font_ = font;
         paintOver();
      }
   
   
   
      public void setWireframe(boolean wireframe)
      {
         if(wireframe)
            quality_ = 0;
         else
            quality_ = qualityCB_;
      }
   
   
      public void setQuality(int quality)
      {
         if(qualityCB_ == quality_)
            quality_ = quality;
         qualityCB_ = quality;
      }
   
   
      public void removeEdgeBends()
      {
         graph_.removeEdgePaths();
         paintOver();
      }
   
   
      public void removeGroups()
      {
         graph_.removeGroups();
         paintOver();
      }
   
   
   
      public void groupControl(int key)
      {
         if((key == 'g' || key == 'u' || key == 'd')) {
            Node tmpnode;
            for(tmpnode = graph_.firstNode(); tmpnode != null;
                tmpnode = graph_.nextNode(tmpnode)) {
               if(tmpnode.getSelected() && tmpnode.isVisible())
                  if(key == 'g' || key == 'u')  // Group or ungroup.
                     graph_.group(tmpnode, key == 'g');
                  else if(key == 'd')  // Delete group.
                     graph_.killGroup(tmpnode);
            
            }
         
         }
         else if(key == 'c') {  // Create group.
            int groupnode_id = graph_.insertNode();
            Node groupnode = graph_.nodeFromIndex(groupnode_id);
            groupnode.setGroup();
         
            Node tmpnode, one_member = null;
            for(tmpnode = graph_.firstNode(); tmpnode != null;
                tmpnode = graph_.nextNode(tmpnode))
               if(tmpnode.getSelected() && tmpnode.isVisible()) {
                  graph_.setNodeGroup(tmpnode, groupnode);
                  one_member = tmpnode;
               }
            if(one_member != null)
               graph_.group(one_member, true);
         }
         unselectItems();
         paintOver();
      
      }
   
   
   
   
   
      // Create anti-aliasing grey shades.
      static {
         aaShades_ = new Color[aaDivs_ * aaDivs_ * 2];
      
         for(int i = 0; i < aaDivs_ * aaDivs_ * 2; i++) {
            int shade = 255 - i * 512 / (aaDivs_ * aaDivs_);
            if(shade < 0)
               shade = 0;
            aaShades_[i] = new Color(shade, shade, shade);
         }
      }
   
      public void drawRotatedText(String string, double theta, int cx, int cy,
                                  Graphics graphics_in, int which_gr)
      {
         FontMetrics fm = graphics_in.getFontMetrics();
         int label_w = fm.stringWidth(string);
         int label_h = fm.getHeight();
      
         double cos_theta = Math.cos(theta);
         if(cos_theta < 0.0) {
            theta += Math.PI;
            cos_theta = -cos_theta;
         }
         double sin_theta = Math.sin(theta);
         cx += -sin_theta * label_h / 2.0;
         cy += -cos_theta * label_h / 2.0;
      
         graphics_in.dispose();
      
         Image tmp_image = createImage(label_w, label_h);
         Graphics graphics = tmp_image.getGraphics();
         graphics.setFont(font_);
         graphics.setColor(new Color(0));
         graphics.drawString(string, 0, fm.getAscent());
         graphics.dispose();
      
         int[] pixels = Node.getImagePixels(tmp_image, label_w, label_h);
         if(pixels == null)
            return;
      
         int x, y;
         int image_size = (int)Math.ceil(Math.sqrt((double)(label_w * label_w) +
                                                      (double)(label_h * label_h))) + 2;
      
         int[] result = new int[image_size * image_size];
      
         rotImage_(theta, pixels, label_w, label_h, result, image_size);
      
         cx -= image_size / 2;
         cy -= image_size / 2;
      
         if(which_gr == 0)
            graphics_in = getGraphicsInternal_();
         else
            graphics_in = getBackGraphics_();
         for(y = 0; y < image_size; y++) {
            for(x = 0; x < image_size; x++) {
               if(result[y * image_size + x] > 0) {
                  graphics_in.setColor(aaShades_[result[y * image_size + x] / 2]);
                  graphics_in.drawLine(cx + x, cy + y, cx + x, cy + y);
               }
            
            }
         }
         graphics_in.dispose();
      }
   
   
   
   
   
   
   
      private void rotImage_(double theta, int[] pixels, int w, int h, int[] result,
                             int image_size)
      {
         double dxX = Math.cos(theta);
         double dyX = -Math.sin(theta);
         double dxY = -dyX;
         double dyY = dxX;
         double dxIx = dxX / (double)aaDivs_;
         double dyIx = dyX / (double)aaDivs_;
         double dxIy = dxY / (double)aaDivs_;
         double dyIy = dyY / (double)aaDivs_;
         double hdxIx = dxIx / 2.0, hdyIx = dyIx / 2.0;
         double hdxIy = dxIy / 2.0, hdyIy = dyIy / 2.0;
      
         double xX = image_size / 2.0 - dxX * w / 2.0 - dxY * h / 2.0;
         double yX = image_size / 2.0 - dyX * w / 2.0 - dyY * h / 2.0;
      
         double xY, yY, Ix, Iy, xIx, yIx, xIy, yIy;
         int x, y;
         for(x = 0; x < w; x++) {
            xY = xX; yY = yX;
            for(y = 0; y < h; y++) {
               xIx = xY + hdxIx; yIx = yY + hdyIx;
               if((pixels[y * w + x] & 0xFFFFFF) == 0) {
                  for(Ix = 0; Ix < aaDivs_; Ix++) {
                     xIy = xIx + hdxIy; yIy = yIx + hdyIy;
                     for(Iy = 0; Iy < aaDivs_; Iy++) {
                        if(((int)yIy * image_size + (int)xIy > 0) &&
                              ((int)yIy * image_size + (int)xIy < image_size * image_size))
                           result[(int)yIy * image_size + (int)xIy]++;
                     
                        xIy += dxIy;  yIy += dyIy;
                     }
                     xIx += dxIx;  yIx += dyIx;
                  }
               }
               xY += dxY;  yY += dyY;
            }
            xX += dxX;  yX += dyX;
         }
      }
   
   
      public void selectNode(int node_index) {
         Node node = graph_.getNodeFromId(node_index);

         selectedNode_ = node;
         node.setSelected(true);
         paintOver();
      }

   }
