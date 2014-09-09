/*
 *	File: GraphWindow.java
 *
 *	5/29/96   Larry Barowski
*/



   package EDU.auburn.VGJ.gui;


   import java.awt.Frame;
   import java.awt.Label;
   import java.awt.Button;
   import java.awt.MenuBar;
   import java.awt.Menu;
   import java.awt.MenuItem;
   import java.awt.Event;
   import java.awt.Color;
   import java.awt.Choice;
   import java.awt.Panel;
   import java.awt.Scrollbar;
   import java.awt.GridBagLayout;
   import java.awt.Insets;
   import java.awt.BorderLayout;
   import java.awt.GridBagConstraints;
   import java.net.URL;
   import java.awt.Image;
   import java.awt.image.ImageProducer;
   import java.awt.Dimension;
   import java.awt.CheckboxMenuItem;
   import java.awt.FileDialog;
   import java.awt.Checkbox;
   import java.awt.CheckboxGroup;
   import java.awt.FlowLayout;


   import java.util.Hashtable;

   import java.io.StringBufferInputStream;
   import java.io.FileInputStream;
   import java.io.File;
   import java.io.FileNotFoundException;
   import java.io.IOException;
   import java.io.FileOutputStream;
   import java.io.PrintStream;
   import java.io.InputStream;


   import EDU.auburn.VGJ.util.DDimension;
   import EDU.auburn.VGJ.util.DPoint;
   import EDU.auburn.VGJ.util.DPoint3;
   import EDU.auburn.VGJ.algorithm.GraphAlgorithm;
   import EDU.auburn.VGJ.graph.Node;
   import EDU.auburn.VGJ.graph.Graph;
   import EDU.auburn.VGJ.graph.GMLlexer;
   import EDU.auburn.VGJ.graph.GMLobject;
   import EDU.auburn.VGJ.graph.ParseError;


   import java.lang.System;


/**
 *	GraphWindow is a graph editing tool window.
 *	</p>Here is the <a href="../gui/GraphWindow.java">source</a>.
 *
 *@author	Larry Barowski
**/
   public class GraphWindow extends Frame
   {
   
   /**
   *	spacing of controls
   **/
      protected static int	spacing_ = 6;  // spacing between controls
   
      private Graph graph_;
      private GraphCanvas	graphCanvas_;
   
      private Panel controls_;
      private CheckboxMenuItem showControls_;

      private ViewportScroller portScroller_;
   
      private ScrolledPanel viewingPanel_;
   
      private Label scaleLabel_;
   
      private double scale_ = 1.0;
   
      private Menu algorithmMenu_;
   
      private Hashtable algHashTable_;
      private Hashtable algNameHashTable_;
      private Hashtable menuHashTable_;
   
      private CheckboxMenuItem cbmiDirected_;
   
      private String filename_ = null;
      private GMLobject GMLfile_ = null;
   
      private PSdialog psDialog_ = null;
      private AlgPropDialog algPropDialog_ = null;
      private FontPropDialog fontPropDialog_ = null;
      private GroupControl groupControl_ = null;
   
      private static URL context_ = null;
   
   
   
      public GraphWindow(boolean directed)
      {
         construct(new Graph(directed));
      }
   
   
      public GraphWindow(Graph graph_in)
      {
         construct(graph_in);
      }
   
      private void construct(Graph graph_in)
      {
         algHashTable_ = new Hashtable();
         algNameHashTable_ = new Hashtable();
         menuHashTable_ = new Hashtable();
      	// create menus
      
         boolean directed = graph_in.isDirected();
      
         MenuBar menubar = new MenuBar();
         Menu menu = new Menu("File");
      
         menu.add(new MenuItem("Open (GML)"));
         menu.add(new MenuItem("Save (GML)"));
         menu.add(new MenuItem("Save As (GML)"));
      
         menu.add(new MenuItem("-"));
         menu.add(new MenuItem("Open Example Graph"));
         menu.add(new MenuItem("-"));
         menu.add(new MenuItem("PostScript Output"));
      
         menu.add(new MenuItem("-"));
         menu.add(new MenuItem("Exit This Window"));
         menubar.add(menu);
      
         algorithmMenu_ = new Menu("Algorithms");
         menubar.add(algorithmMenu_);
      
         menu = new Menu("Edit");
         menu.add(new MenuItem("Edit Text Representation (GML)"));
         menu.add(new MenuItem("Delete Selected Items"));
         menu.add(new MenuItem("Select All"));
         menu.add(new MenuItem("Remove All Edge Bends"));
         menu.add(new MenuItem("Remove All Groups"));
         menu.add(new MenuItem("Group Control"));
         menubar.add(menu);
      
         menu = new Menu("Properties");
      
         showControls_ = new CheckboxMenuItem("Show Controls  [s]");
         showControls_.setState(true);
         menu.add(showControls_);

         CheckboxMenuItem cbmi = new CheckboxMenuItem("Directed");
         cbmiDirected_ = cbmi;
         if(directed)
            cbmi.setState(true);
         menu.add(cbmi);
      
         menu.add(new MenuItem("Set New Node Properties"));
         menu.add(new MenuItem("Set Node Spacing")); 
         menu.add(new MenuItem("Set Font")); 
      
         cbmi = new CheckboxMenuItem("Scale Node Size");
         cbmi.setState(true);
         menu.add(cbmi);
      
         cbmi = new CheckboxMenuItem("Use Node ID As Default Label");
         cbmi.setState(true);
         menu.add(cbmi);
      
         cbmi = new CheckboxMenuItem("High Quality Display");
         cbmi.setState(false);
         menu.add(cbmi);
      
         menubar.add(menu);
      
         setMenuBar(menubar);
      
      
         setLayout(new BorderLayout());
      
      
      	// panel for controls
      
         LPanel p = new LPanel();
      
         p.addLineLabel("Mouse Action:", 0);
         CheckboxGroup mode_group = new CheckboxGroup();
         p.constraints.insets.bottom = 0;
         p.addCheckbox("Create Nodes", mode_group, true, 1, -1, 1.0, 1.0, 0, 0);
         p.addCheckbox("Create Edges", mode_group, false, 0, -1, 1.0, 1.0, 0,
                       0);
         p.constraints.insets.top = p.constraints.insets.bottom = 0;
         p.addCheckbox("Select Nodes", mode_group, false, 1, -1, 1.0, 1.0, 0,
                       0);
         p.addCheckbox("Select Edges", mode_group, false, 0, -1, 1.0, 1.0, 0,
                       0);
         p.constraints.insets.top = p.constraints.insets.bottom = 0;
         p.addCheckbox("Select Nodes or Edges", mode_group, false, 0, -1, 1.0,
                       1.0, 0, 0);
      
         portScroller_ = new ViewportScroller(90, 90,
                                              500.0, 500.0, 400.0, 400.0, 0.0, 0.0);
         portScroller_.setBackground(Color.lightGray);
      
         p.constraints.insets.bottom = 0;
         p.addLabel("Viewing Offset", 0, 0, 1.0, 1.0, 0, 0);
         p.constraints.insets.top = p.constraints.insets.bottom = 0;
         p.addComponent(portScroller_, 0, 0, 1.0, 1.0, 0, 0);
         p.constraints.insets.top = p.constraints.insets.bottom = 0;
         p.addButton("Center", 0, 0, 1.0, 1.0, 0, 0);
      
         scaleLabel_ = p.addLineLabel("Scale: 1", 0);     
      
         LPanel sp = new LPanel();
         sp.spacing = 0;
         sp.constraints.insets.top = sp.constraints.insets.bottom = 0;
         sp.addButton("Scale / 2", 1, -1, 1.0, 1.0, 0, 0);
         sp.addButton("Scale = 1", 1, -1, 1.0, 1.0, 0, 0);
         sp.addButton("Scale * 2", 0, -1, 1.0, 1.0, 0, 0);
         sp.finish();
      
         p.addComponent(sp, 0, 0, 1.0, 1.0, 0, 0);
      
         p.constraints.insets.top = 0;
         AngleControlPanel angc = new AngleControlPanel(180, 76);
         p.addComponent(angc, 0, 0, 1.0, 1.0, 1, 0);
      
      
      	// super panel is there just to allow controls to be
      	// fixed to top instead of centered
         Panel controls_superpanel = new Panel();
      
         controls_superpanel.add("North", p);
         p.finish();
         add("West", controls_superpanel);
	 controls_ = controls_superpanel;
      
         graph_ = graph_in;
      
      
         Node.defaults.setBoundingBox(20, 20, 20);
      
      	// the graph viewing canvas
         graphCanvas_ = new GraphCanvas(graph_, this);
      
      	// the graph viewing panel (canvas and scrollbars)
         viewingPanel_ = new ScrolledPanel(graphCanvas_);
         add("Center", viewingPanel_);
         setBackground(Color.lightGray);
         validate();
      }
   
   
   
   
   
      public boolean action(Event event, Object what)
      {
         if(event.target instanceof Checkbox)
         {
            CheckboxGroup cbgrp = ((Checkbox)(event.target)).getCheckboxGroup();
            Checkbox selected = cbgrp.getCurrent();
            String label = selected.getLabel();
         
            int mode = GraphCanvas.CREATE_NODES;
            if(label.equals("Create Edges"))
               mode = GraphCanvas.CREATE_EDGES;
            else if(label.equals("Select Nodes"))
               mode = GraphCanvas.SELECT_NODES;
            else if(label.equals("Select Edges"))
               mode = GraphCanvas.SELECT_EDGES;
            else if(label.equals("Select Nodes or Edges"))
               mode = GraphCanvas.SELECT_BOTH;
         
            graphCanvas_.setMouseMode(mode);
         }
         else if(event.target instanceof CheckboxMenuItem)
         {
            String label = ((CheckboxMenuItem)(event.target)).getLabel();
            boolean state = ((CheckboxMenuItem)(event.target)).getState();
         
            if(label.equals("Scale Node Size"))
               graphCanvas_.scaleBounds(state);
            else if(label.equals("Show Controls  [s]")) {
               if(state)
		  controls_.show();
	       else
		  controls_.hide();
	       validate();
	    }
            else if(label.equals("Directed"))
               graphCanvas_.setDirected(state);
            else if(label.equals("Use Node ID As Default Label"))
            {
               Node.setDefaultLabel(state);
               graphCanvas_.update(false);
            }
            else if(label.equals("High Quality Display"))
               graphCanvas_.setQuality(state == false? 1 : 2);
         }
         else if(event.target instanceof MenuItem)
         {
            GraphAlgorithm alg;
         
            MenuItem menuitem = (MenuItem)event.target;
         
            alg = (GraphAlgorithm) algHashTable_.get(menuitem);
            if(alg != null)
            {
               applyAlgorithm_(alg);
            }
            else
            {
               String label = menuitem.getLabel();
            
               if(label.equals("Open Example Graph")) {
                  URL src = null;
                  try {
                     src = new URL(context_, "example.gml");
                     InputStream strm = src.openStream();
                     GMLlexer lexer = new GMLlexer(strm);
                     GMLfile_ = new GMLobject(lexer, null);
                     GMLobject gmlgraph = GMLfile_.getGMLSubObject("graph", GMLobject.GMLlist, false);
                  
                     Graph newgraph = null;
                     newgraph = new Graph(gmlgraph);
                     graph_.copy(newgraph);
                     graphCanvas_.update(true);
                  
                     filename_ = null;
                     setTitle_();
                  }
                     catch(Exception e) {
                        new MessageDialog(this, "Error",
                                          "Error loading example graph.", true);
                     };
               
               }
               else if(label.equals("Open (GML)")) {
                  String filename = null;
               
                     FileDialog fd;
                     try {
                        fd = new FileDialog(this, "Open VGJ File (GML)", FileDialog.LOAD);
                        fd.show(); }
                     catch(Throwable e) {
                        MessageDialog dg = new MessageDialog(this,
                           "Error", "It appears your VM does not allow file loading.", true);
                        return true;
                     }
                     filename = fd.getFile();
                     if(filename == null)
                        return true;
                     filename = fd.getDirectory() + filename;

                     loadFile(filename);
                     return true;
               }
               else if(label.equals("Save (GML)") || label.equals("Save As (GML)"))
               {
                  try
                  {
                     String filename;
                     if(label.equals("Save As (GML)") || filename_ == null)
                     {
			FileDialog fd;
			try {
                           fd = new FileDialog(this, "Save VGJ File (GML)", FileDialog.SAVE);
                           fd.show(); }
                        catch(Throwable e) {
                           MessageDialog dg = new MessageDialog(this,
                              "Error", "It appears your VM does not allow file saving.", true);
                           return true;
                        }

                        filename = fd.getFile();
                        if(filename == null)
                           return true;
                     
                        // Work around JDK Windows bug.
                        if(filename.endsWith(".*.*"))
                        {
                           String tmpstr = filename.substring(0, filename.length() - 4);
                           filename = tmpstr;
                        }
                        filename = fd.getDirectory() + filename;
                     }
                     else
                        filename = filename_;
                  
                     PrintStream ps = new PrintStream(new FileOutputStream(filename));
                     if(GMLfile_ == null)
                     {
                        GMLfile_ = new GMLobject(null, GMLobject.GMLfile);
                        GMLfile_.addObjectToEnd(new GMLobject("graph", GMLobject.GMLlist));
                     }
                     GMLobject gmlgraph = GMLfile_.getGMLSubObject("graph", GMLobject.GMLlist, false);
                     graph_.setGMLvalues(gmlgraph);
                     gmlgraph.prune();
                  
                     ps.println(GMLfile_.toString(0));
                     ps.close();
                  
                     filename_ = filename;
                     setTitle_();
                  }
                     catch (IOException e)
                     {
                        MessageDialog dg = new MessageDialog(this,
                                                            "Error", e.getMessage(), true);
                     }
               
               }
               else if(label.equals("Exit This Window"))
                  Destroy();
               else if(label.equals("Delete Selected Items"))
                  graphCanvas_.deleteSelected(true);
               else if(label.equals("Select All"))
                  graphCanvas_.selectAll();
               else if(label.equals("Remove All Edge Bends"))
                  graphCanvas_.removeEdgeBends();
               else if(label.equals("Remove All Groups"))
                  graphCanvas_.removeGroups();
               else if(label.equals("Group Control")) {
                  if(groupControl_ == null)
                     groupControl_ = new GroupControl(this, graphCanvas_);
                  else
                     groupControl_.showMe();
               }
               else if(label.equals("Set New Node Properties"))
                  graphCanvas_.setNodeProperties(true);
               else if(label.equals("Set Node Spacing"))
               {
                  if(algPropDialog_ == null)
                     algPropDialog_ = new AlgPropDialog(this, graphCanvas_);
                  else
                     algPropDialog_.showMe();
               }
               else if(label.equals("Set Font"))
               {
                  if(fontPropDialog_ == null)
                     fontPropDialog_ = new FontPropDialog(this, graphCanvas_);
                  else
                     fontPropDialog_.showMe();
               }
               else if(label.equals("Edit Text Representation (GML)"))
               {
                  graphCanvas_.unselectItems();
                  GraphEdit ge = new GraphEdit(graph_, graphCanvas_);
                  ge.pack();
                  ge.show();
               }
               else if(label.equals("PostScript Output"))
               {
                  if(psDialog_ == null)
                     psDialog_ = new PSdialog(this, graphCanvas_);
                  else {
                     psDialog_.pack();
                     psDialog_.show();
                  }
               }
            }
         } 
         else if(event.target instanceof Button)
         {
            if(((String)what).equals("Scale / 2"))
            {
               scale_ /= 2.0;
               graphCanvas_.setScale(scale_);
            
               scaleLabel_.setText("Scale: " + scale_);
            }
            else if(((String)what).equals("Scale * 2"))
            {
               scale_ *= 2.0;
               graphCanvas_.setScale(scale_);
            
               scaleLabel_.setText("Scale: " + scale_);
            }
            else if(((String)what).equals("Scale = 1"))
            {
               scale_ = 1.0;
               graphCanvas_.setScale(scale_);
            
               scaleLabel_.setText("Scale: " + scale_);
            }
            else if(((String)what).equals("Center"))
            {
               DDimension port_dim = viewingPanel_.getPortSize();
               DDimension cont_dim = viewingPanel_.getContentSize();
            
               double x = (cont_dim.width - port_dim.width) / 2.0;
               double y = (cont_dim.height - port_dim.height) / 2.0;
            
               viewingPanel_.scrollTo((int)x, (int)y);
               portScroller_.setOffset(x, y);
            }
         
         }
         return true;
      }
   
   
   
   
   
      public boolean handleEvent(Event event)
      {
         if(event.target instanceof ScrolledPanel)
         {
            if(event.id == ScrolledPanel.RESIZE)
            		// RESIZE event from panel.
            {
               DDimension tmp_dim;
            
               tmp_dim = viewingPanel_.getPortSize();
               portScroller_.setPortSize(tmp_dim.width, tmp_dim.height);
            
               tmp_dim = viewingPanel_.getContentSize();
               portScroller_.setContentSize(tmp_dim.width, tmp_dim.height);
            
               tmp_dim = viewingPanel_.getOffset();
               portScroller_.setOffset(tmp_dim.width, tmp_dim.height);
            
               return true;
            }
            else if(event.id == ScrolledPanel.OFFSET)
            {
               DDimension tmp_dim = viewingPanel_.getOffset();
               portScroller_.setOffset(tmp_dim.width, tmp_dim.height);
            
               return true;
            }
         }
         else if(event.target instanceof ViewportScroller)
         {
            if(event.id == ViewportScroller.SCROLL) {
               graphCanvas_.setWireframe(true);
               viewingPanel_.scrollTo((int)portScroller_.getOffsetX(),
                                         (int)portScroller_.getOffsetY());
            }
            if(event.id == ViewportScroller.DONE) {
               graphCanvas_.setWireframe(false);
               viewingPanel_.scrollTo((int)portScroller_.getOffsetX(),
                                         (int)portScroller_.getOffsetY());
            }
         }
         else if(event.target instanceof AngleControlPanel)
         {
            if(event.id == AngleControlPanel.ANGLE) {
               DPoint	angles = (DPoint)event.arg;
            
               graphCanvas_.setWireframe(true);
               graphCanvas_.setViewAngles(angles.x, angles.y);
            }
            if(event.id == AngleControlPanel.DONE) {
               DPoint	angles = (DPoint)event.arg;
            
               graphCanvas_.setWireframe(false);
               graphCanvas_.setViewAngles(angles.x, angles.y);
            }
         }
         else if(event.target instanceof GraphCanvas)
            // Graph has changed.
         {
            if(event.id == GraphCanvas.UPDATE)
            {
               cbmiDirected_.setState(graph_.isDirected());
            }
         }
         // quit from Window Manager menu
         else if(event.id == Event.WINDOW_DESTROY)
         {
            Destroy();
            return false;
         }
      
      	// call inherited handler
         return super.handleEvent(event);
      }
   
   
   
      public boolean keyDown(Event e, int key)
      {
         if(e.id == Event.KEY_PRESS) {
	    if(key == 's')
	       showControls_.setState(!showControls_.getState());
	    else
               graphCanvas_.keyDown(e, key);
               if(showControls_.getState())
		  controls_.show();
	       else
		  controls_.hide();
	       validate();
            }
      
         return false;
      }
   
   
   
   
   // Function is called on an exit menu choice or delete from the WM menu
      private void Destroy()
      {
         dispose();
      }
   
   
   
   
   
      public void addAlgorithm(GraphAlgorithm alg, String name)
      {
         MenuItem menuitem;
      
         menuitem = new MenuItem(name);
         algorithmMenu_.add(menuitem);
      
      	 // Algorithms keyed by menuitems.
         algHashTable_.put(menuitem, alg);
         algNameHashTable_.put(name, alg);
      }
   
   
   
      public void addAlgorithmMenu(String name)
      {
         Menu menu;
      
         menu = new Menu(name);
         algorithmMenu_.add(menu);
      
      	// Menus keyed by names.
         menuHashTable_.put(name, menu);
      }
   
   
   
   
      public void addAlgorithm(GraphAlgorithm alg, String menuname, String name)
      {
         Menu menu = (Menu) menuHashTable_.get(menuname);
         if(menu == null)
            return;
      
         MenuItem menuitem = new MenuItem(name);
         menu.add(menuitem);
      
         algHashTable_.put(menuitem, alg);
         algNameHashTable_.put(menuname + "." + name, alg);
      }
   
   
   
   
   
      private void setTitle_()
      {
         if(filename_ != null)
            setTitle("VGJ:   " + filename_);
      }
   
   
   
   
      public static void setContext(URL context)
      {
         context_ = context;
      }
   
   
   


      public void loadFile(String filename)
      {
              try {
                     File fname = new File(filename);
                     int size = (int)fname.length();
                     int bytes_read = 0;
                     FileInputStream infile = new FileInputStream(fname);
                     byte[] data = new byte[size];
                     while (bytes_read < size)
                        bytes_read += infile.read(data, bytes_read, size - bytes_read);
                  
                     StringBufferInputStream stream = new StringBufferInputStream(new String(data, 0));
                     GMLlexer lexer = new GMLlexer(stream);
                  
                  
                     try
                     {
                        GMLfile_ = new GMLobject(lexer, null);
                        GMLobject gmlgraph = GMLfile_.getGMLSubObject("graph", GMLobject.GMLlist, false);
                        if(gmlgraph == null)
                        {
                           GMLfile_ = null;
                           new MessageDialog(this, "Error",
                                             "File does not contain a graph.", true);
                           return;
                        }
                     
                        Graph newgraph = null;
                     
                        newgraph = new Graph(gmlgraph);
                        graph_.copy(newgraph);
                        graphCanvas_.update(true);
                     
                        filename_ = filename;
                        setTitle_();
                     }
                        catch(ParseError error)
                        {
                           new MessageDialog(this,
                                             "Error", error.getMessage() +
                                             " at line " + lexer.getLineNumber() +
                                             " at or near \""
                                             + lexer.getStringval() + "\".", true);
                           return;
                        }
                        catch(IOException error)
                        {
                           MessageDialog dg = new MessageDialog(this,
                                                               "Error", error.getMessage(), true);
                           return;
                        }
                  }
                     catch (FileNotFoundException e)
                     {
                        MessageDialog dg = new MessageDialog(this,
                            "Error", "Error loading file  \"" + filename + "\".", true);
                     }
                  
                     catch (IOException e)
                     {
                        MessageDialog dg = new MessageDialog(this,
                                                            "Error", e.getMessage(), true);
                     }


      }


      public void selectNode(int node_index) {
         graphCanvas_.selectNode(node_index);
      }



      private void applyAlgorithm_(GraphAlgorithm alg)
      {
               if(graph_.numberOfNodes() < 1)
                  new MessageDialog(this, "Error", "Graph is empty.", true);
               else
               {
                  graph_.removeGroups();  // Remove group nodes.
                  graph_.pack();  // Pack the indexes.
               
                  String msg;
                  graphCanvas_.setWireframe(true);
                  msg = alg.compute(graph_, graphCanvas_);
                  graphCanvas_.setWireframe(false);
               
                  if(msg != null)
                     new MessageDialog(this, "Message", msg, true);
               
                  graphCanvas_.update(true);
               }
      }



      public void applyAlgorithm(String name)
      {
          GraphAlgorithm alg = (GraphAlgorithm) algNameHashTable_.get(name);
          if(alg != null)
            {
               applyAlgorithm_(alg);
            }
      }



   }
