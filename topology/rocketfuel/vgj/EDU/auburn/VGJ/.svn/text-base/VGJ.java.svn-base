
/*
 *File: VGJ.java
 *
 * Date      Author
 * 2/16/97   Larry Barowski
 *
 */


   package EDU.auburn.VGJ;



   import java.awt.GridLayout;
   import java.awt.Event;
   import java.awt.Button;
   import java.awt.Graphics;
   import java.awt.Color;
   import java.awt.Frame;
   import java.applet.Applet;

   import EDU.auburn.VGJ.graph.Node;

   import EDU.auburn.VGJ.gui.GraphWindow;
   import EDU.auburn.VGJ.examplealg.ExampleAlg2;

   import EDU.auburn.VGJ.algorithm.tree.TreeAlgorithm;
   import EDU.auburn.VGJ.algorithm.cgd.CGDAlgorithm;
   import EDU.auburn.VGJ.algorithm.shawn.Spring;
   import EDU.auburn.VGJ.algorithm.cartegw.BiconnectGraph;




   import EDU.auburn.VGJ.graph.*;
   import java.lang.System;
   import java.io.StringBufferInputStream;



/**
 *	The VGJ applet. It is a big button that pops up VGJ graph
 *  editor windows.
 *	</p>Here is the <a href="../VGJ.java">source</a>.
 *
 *@author	Larry Barowski
**/




   public class VGJ extends Applet
   {
      private int appCount_ = 0;
      private boolean isApp_ = false;
   

      public void init()
      {
         if(isApp_)
         {         
            setLayout(new GridLayout(2,1));
            add(new Button("Start a Graph Window"));
            add(new Button("Exit"));
         }
         else
         {
            setLayout(new GridLayout(1,1));
            add(new Button("Start a Graph Window"));
         }
      
	 Node.setToolkit(getToolkit());
	 if(!isApp_) {
	    Node.setContext(getCodeBase());
	    GraphWindow.setContext(getCodeBase());
	 }
	 validate();
         show();
      }
   
   
   
      public static void main(String args[])
      {
         Frame frame = new Frame("VGJ v1.03");
         VGJ vgj = new VGJ();
         vgj.isApp_ = true;
         vgj.init();
         vgj.start();
      
         frame.add("Center", vgj);
         frame.resize(200, 300);
	 frame.validate();
         frame.show();

         if(args.length > 0) {
            String filename = null;
            int selected = -1;
            String layout = null;

            for(int i = 0; i < args.length; i++) {
               if(args[i].equals("-f") && i < args.length)
                  filename = args[++i];
               else if(args[i].equals("-s") && i < args.length)
                  selected = Integer.parseInt(args[++i]);
               else if(args[i].equals("-l") && i < args.length)
                  layout = args[++i];
            }

            if(filename != null) {
               GraphWindow gw = vgj.buildWindow_();
               gw.loadFile(filename);
               if(selected != -1)
                  gw.selectNode(selected);
               if(layout != null)
                  gw.applyAlgorithm(layout);
            }
         }
      }
   
   
   
   
      public boolean action(Event event, Object what)
      {
         if(event.target instanceof Button)
            if(((String)what).equals("Exit"))
            {
               System.exit(0);
            }
            else
            {
                buildWindow_();
            } 
         return super.action(event, what);
      }
   




      private GraphWindow buildWindow_()
      {
            	// Bring up an undirected graph editor window.
            
            	// The parameter to GraphWindow() indicates directed
            	// or undirected.
               GraphWindow graph_editing_window = new GraphWindow(true);
            
            	// Here the algorithms are added.
               graph_editing_window.addAlgorithmMenu("Tree");
            
               ExampleAlg2 alg2 = new ExampleAlg2();
               graph_editing_window.addAlgorithm(alg2, "Random");
            
               TreeAlgorithm talg = new TreeAlgorithm('d');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Down");
            
               talg = new TreeAlgorithm('u');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Up");
            
               talg = new TreeAlgorithm('l');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Left");
            
               talg = new TreeAlgorithm('r');
               graph_editing_window.addAlgorithm(talg, "Tree", "Tree Right");
            
            
               graph_editing_window.addAlgorithmMenu("CGD");
            
               CGDAlgorithm calg = new CGDAlgorithm();
               graph_editing_window.addAlgorithm(calg, "CGD", "CGD");
            
               calg = new CGDAlgorithm(true);
               graph_editing_window.addAlgorithm(calg, "CGD",
                  "show CGD parse tree");
            
               Spring spring = new Spring();
               graph_editing_window.addAlgorithm(spring, "Spring");
            
               graph_editing_window.addAlgorithmMenu("Biconnectivity");
               BiconnectGraph make_biconnect = new BiconnectGraph(true);
               graph_editing_window.addAlgorithm (make_biconnect, 
                  "Biconnectivity", "Remove Articulation Points");
               BiconnectGraph check_biconnect = new BiconnectGraph(false);
               graph_editing_window.addAlgorithm (check_biconnect, 
                  "Biconnectivity", "Find Articulation Points");
            
               if (appCount_++ == 0)
                  graph_editing_window.setTitle("VGJ v1.03");
               else
                  graph_editing_window.setTitle("VGJ v1.03" + ": "
                     + appCount_);
            
               graph_editing_window.pack();
               graph_editing_window.show();

               return graph_editing_window;
      }


   }







