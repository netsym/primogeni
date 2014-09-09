package jprime.util;

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


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import jprime.IModelNode;
import jprime.Host.IHost;
import jprime.Interface.IInterface;
import jprime.Net.INet;

/**
 * @author Nathanael Van Vorst
 *
 */
public class GraphOverlay {
	/** synced from prefuse! */
    public static final int SHAPE_RECTANGLE      = 0;
    public static final int SHAPE_ELLIPSE        = 1;
    public static final int SHAPE_DIAMOND        = 2;
    public static final int SHAPE_CROSS          = 3;
    public static final int SHAPE_STAR           = 4;
    public static final int SHAPE_TRIANGLE_UP    = 5;
    public static final int SHAPE_TRIANGLE_DOWN  = 6;
    public static final int SHAPE_TRIANGLE_LEFT  = 7;
    public static final int SHAPE_TRIANGLE_RIGHT = 8;
    public static final int SHAPE_HEXAGON        = 9;
    
	public static class OverlayInfo {
		public final Integer fill_color, stroke_color, shape;
		public final Double size;
		public final IModelNode node;
		public OverlayInfo(IModelNode node, Integer fill_color, Integer stroke_color,
				Integer shape, Double size) {
			super();
			this.node=node;
			this.fill_color = fill_color;
			this.stroke_color = stroke_color;
			this.shape = shape;
			this.size = size;
		}
		public String toString() {
			String rv = "["+node.getUniqueName();
			if(fill_color != null) {
				rv+=", fill_color="+fill_color;
			}
			if(stroke_color != null) {
				rv+=", stroke_color="+stroke_color;
			}
			if(shape != null) {
				rv+=", shape="+shape;
			}
			if(size != null) {
				rv+=", size="+size;
			}
			rv+="]";
			return rv;
		}
	}

	public static HashMap<Long,OverlayInfo> parse(final INet topnet, final File file) throws IOException {
		final HashMap<Long,OverlayInfo> overlay = new HashMap<Long, GraphOverlay.OverlayInfo>();
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String str;
		while ((str = br.readLine()) != null)   {
			parseLine(overlay,topnet,str);
		}
		in.close();
		return overlay;
	}
	
	public static HashMap<Long,OverlayInfo> parse(final INet topnet, final String file) {
		final HashMap<Long,OverlayInfo> overlay = new HashMap<Long, GraphOverlay.OverlayInfo>();
		String[] s = file.trim().split("\\n");
		for(String l : s) parseLine(overlay,topnet,l);
		return overlay;
	}

	private static void parseLine(final HashMap<Long,OverlayInfo> overlay, final INet topnet, final String l) {
		String[] ss = l.trim().split(",");
		String type=null;
		IModelNode node=null;
		Integer fill_color=null, stroke_color=null, shape=null;
		Double size=null;
		for(String s : ss) {
			String[] tt = s.trim().split("=");
			if(tt.length != 2) {
				throw new RuntimeException("Invalid overlay line '"+l+"'. Bad syntax.");
			}
			tt[0]=tt[0].trim();
			tt[1]=tt[1].trim();
			if(tt[0].compareToIgnoreCase("ip")==0) {
				node = topnet.findInterfaceWithIp(tt[1]);
				if(node == null) {
					break;
				}
			}
			else if(tt[0].compareToIgnoreCase("uid")==0) {
				node = topnet.findNodeByUID(Long.parseLong(tt[1]));
				if(node == null) {
					jprime.Console.err.println("Unable to find node with uid "+tt[1]);
					break;
				}
			}
			else if(tt[0].compareToIgnoreCase("name")==0) {
				if(topnet.getName().equals(tt[1].substring(0, topnet.getName().length()))) {
					node = topnet.get(tt[1].substring(topnet.getName().length()+1).replace(":", "."));
					if(node == null) {
						jprime.Console.err.println("Unable to find node "+tt[1]+" in "+topnet.getName());
						break;
					}
				}
				else {
					jprime.Console.err.println("Unable to find node "+tt[1]+" in "+topnet.getName());
					break;
				}
			}
			else if(tt[0].compareToIgnoreCase("type")==0) {
				type = tt[1];
				if(type.compareTo("host") !=0 && type.compareTo("link")!=0 && type.compareTo("net")!=0 )
					throw new RuntimeException("Invalid type '"+tt[1]+"'. Valid type: host, net, link.");
			}
			else if(tt[0].compareToIgnoreCase("fill_color")==0) {
				fill_color = Integer.parseInt(tt[1]);
			}
			else if(tt[0].compareToIgnoreCase("stroke_color")==0) {
				stroke_color = Integer.parseInt(tt[1]);
			}
			else if(tt[0].compareToIgnoreCase("size")==0) {
				size = Double.parseDouble(tt[1]);
			}
			else if(tt[0].compareToIgnoreCase("shape")==0) {
				if(tt[1].compareToIgnoreCase("rectangle")==0) {
					shape = SHAPE_RECTANGLE;
				}
				else if(tt[1].compareToIgnoreCase("ellipse")==0) {
					shape = SHAPE_ELLIPSE;
				}
				else if(tt[1].compareToIgnoreCase("diamond")==0) {
					shape = SHAPE_DIAMOND;
				}
				else if(tt[1].compareToIgnoreCase("cross")==0) {
					shape = SHAPE_CROSS;
				}
				else if(tt[1].compareToIgnoreCase("star")==0) {
					shape = SHAPE_STAR;
				}
				else if(tt[1].compareToIgnoreCase("triangle_up")==0) {
					shape = SHAPE_TRIANGLE_UP;
				}
				else if(tt[1].compareToIgnoreCase("triangle_down")==0) {
					shape = SHAPE_TRIANGLE_DOWN;
				}
				else if(tt[1].compareToIgnoreCase("triangle_left")==0) {
					shape = SHAPE_TRIANGLE_LEFT;
				}
				else if(tt[1].compareToIgnoreCase("triangle_right")==0) {
					shape = SHAPE_TRIANGLE_RIGHT;
				}
				else if(tt[1].compareToIgnoreCase("hexagon")==0) {
					shape = SHAPE_HEXAGON;
				}
				else {
					throw new RuntimeException("Invalid shape '"+tt[1]+"'. Valid shapes: rectangle, ellipse, diamond, cross, star, triangle_up, triangle_down, triangle_left, triangle_right, hexagon.");
				}
			}
		}
		if(node == null) {
			jprime.Console.err.println("Invalid overlay line '"+l+"'. Must specify 'ip', 'uid', or 'name'.");
			return;
		}
		if(null == fill_color && null == stroke_color && null == shape && null ==  size) {
			jprime.Console.err.println("Invalid overlay line '"+l+"'. Must specify at least one of: 'fill_color', 'stroke_color', 'shape', or 'size'.");
			return;
		}
		if(type == null) {
			overlay.put(node.getUID(),new OverlayInfo(node,fill_color, stroke_color, shape, size));
		}
		else if(type.compareTo("net")==0) {
			IModelNode o = node;
			node=node.deference();
			while(node!=null && !(node instanceof INet)) node=node.getParent();
			if(node ==null) {
				jprime.Console.err.println("Couldn't find owning net of ."+o.getUniqueName()+"'["+o.deference().getUniqueName()+"].");
			}
			else {
				overlay.put(node.getUID(),new OverlayInfo(node,fill_color, stroke_color, shape, size));
			}
		}
		else if(type.compareTo("host")==0) {
			IModelNode o = node;
			node=node.deference();
			while(node!=null && !(node instanceof IHost)) node=node.getParent();
			if(node ==null) {
				jprime.Console.err.println("Couldn't find owning host of ."+o.getUniqueName()+"'["+o.deference().getUniqueName()+"].");
			}
			else {
				overlay.put(node.getUID(),new OverlayInfo(node,fill_color, stroke_color, shape, size));
			}
		}
		else if(type.compareTo("link")==0) {
			IModelNode o = node;
			if(node instanceof IInterface) {
				node=((IInterface)node.deference()).getAttachedLink();
				if(node == null) {
					jprime.Console.err.println("Couldn't find a link attached to ."+o.getUniqueName()+"'["+o.deference().getUniqueName()+"].");
				}
				else {
					overlay.put(node.getUID(),new OverlayInfo(node,fill_color, stroke_color, shape, size));
				}
			}
			else {
				jprime.Console.err.println("Couldn't find a link attached to ."+o.getUniqueName()+"'["+o.deference().getUniqueName()+"].");
			}
		}
		else {
			throw new RuntimeException("How did this happen?");
		}
	}


}
