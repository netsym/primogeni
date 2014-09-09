package slingshot.spyconsole;


import java.awt.Color;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;


/**
 * SPyConsole Application
 * Developed by Tom Maxwell, maxwell@cbl.umces.edu
 * University of Maryland Institute for Ecological Economics
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * @author Tom Maxwell <maxwell@cbl.umces.edu>
 * @version 1.0
 */
public class Styles extends StyleContext {
	private static final long serialVersionUID = 2573755506321071607L;


	public Style addBase( String name, int tabsize, int fontSize, String fontFamily ) {
		Style parent = getStyle(DEFAULT_STYLE);
		Style style = addStyle(name, parent);
		StyleConstants.setFontSize(style,fontSize);
		StyleConstants.setFontFamily(style,fontFamily);

		int charWidth=StyleConstants.getFontSize(style);
		TabStop[] tabs = new TabStop[20];
		for( int i=0; i<20; i++ ) {
			tabs[i] = new TabStop(i*tabsize*charWidth);
		}
		StyleConstants.setTabSet(style, new TabSet(tabs));

		return style;
	}


	public Style addDerived( String name, Style parent, Color c ) {
		Style style = addStyle(name, parent);
		StyleConstants.setForeground(style,c);
		return style;
	}

	
	
}