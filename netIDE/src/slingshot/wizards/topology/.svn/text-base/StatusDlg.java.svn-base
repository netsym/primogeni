package slingshot.wizards.topology;

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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;


/**
 * This class creates a status dialog to show generator status while generating the topology.
 * @author Hao Jiang
 */
class StatusDlg extends JDialog
{       
    private static final long serialVersionUID = 1L;
    JTextArea statusText = new JTextArea();
    JScrollPane scrollPane1;
    LineBorder lineBorder1 = new LineBorder(java.awt.Color.black);
    
    public JTextArea getTextArea() { return statusText; }
    public JScrollPane getScroll() { return scrollPane1; }
    
    public StatusDlg() 
    {
                super.dialogInit();
                setResizable(false);
                
                getContentPane().setLayout(null);
                getContentPane().setBackground(UIManager.getColor("Label.background"));
                
                statusText.setBackground(SystemColor.text);
                statusText.setFont(new Font("SansSerif", Font.PLAIN, 10));
                statusText.setBounds(10,10,370,350);
                statusText.setLineWrap(true);
                statusText.setEditable(false);
                
                scrollPane1 = new JScrollPane(statusText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                getContentPane().add(scrollPane1);
                scrollPane1.setBounds(10, 10, 370, 350);
                //      scrollPane1.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);
                
                setTitle("Status Window");
                //setSize(getPreferredSize());
                setSize(400, 400);
    }
}