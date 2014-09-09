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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import slingshot.configuration.ConfigurationHandler;

/**
 * This class defines the Wizard Page that asks user to input attributes for the generator.
 * @author Hao Jiang
 */
public class GenAttriPage extends WizardPage
{
        public static final String pagename="GenAttriPage";
    
        private class TableTreeNode extends TreeItem
        {
                private Element elemnode;
                private Combo combo;
                private Text text;
                
                public TableTreeNode(TreeItem parent, int style, Element elem)
                {
                        super(parent, style);
                        this.elemnode = elem;
                        CreateNode();
                }
                
                public TableTreeNode(Tree tree, int style, Element elem)
                {
                        super(tree, style);
                        this.elemnode = elem;
                        CreateNode();
                }
                
                public void CreateNode()
                {
                        if (elemnode == null)
                                return;//throw new RuntimeException("Element node can not be null");
                        
                        if (elemnode.getNodeName().equals(XmlFunction.ATTRIBUTE))
                        {
                                GeneratorAttri attri = new GeneratorAttri(elemnode);
                                AttriConstraint constraint = new AttriConstraint(attri.getAttriConstraint());
                                
                                //Enum type attribute
                                this.setText(0, attri.getAttriName());
                                if (constraint.getType() == 1)
                                {
                                        java.util.List<EnumType> enumlist = constraint.getEnumList();
                                        setText(1, enumlist.get(0).getEnumName());
                                }
                                else
                                        setText(1, attri.getDefaultValue());
                                
                                setText(2, attri.getAttriDescri());
                                setText(3, attri.getAttriConstraint());
                                setText(4, attri.getAttriType());
                        }
                        else if (elemnode.getNodeName().equals(XmlFunction.ATTRIBUTESGRP))
                                        this.setText(elemnode.getAttribute(XmlFunction.NAME));
                }
                
                private void expand()
                {
                        setExpanded(true);
                        
                        TreeItem [] childrenodes = this.getItems();
                        for (int i = 0; i < childrenodes.length; i++)
                        {
                                TableTreeNode node = (TableTreeNode)childrenodes[i];
                                node.expand();
                        }
                }
                
                private void collapse()
                {
                        setExpanded(false);
                        
                        if(combo != null)
                                combo.dispose();
                        if (text != null)
                                text.dispose();
                        
                        TreeItem [] childrenodes = getItems();
                        for (int i = 0; i < childrenodes.length; i++)
                        {
                                TableTreeNode node = (TableTreeNode)childrenodes[i];
                                node.collapse();
                        }
                }
                
                public void dispose()
                {
                        if (this.combo != null)
                                combo.dispose();
                        if (this.text != null)
                                text.dispose();
                        
                        
                        TreeItem [] childrenodes = this.getItems();
                        for (int i = 0; i < childrenodes.length; i++)
                        {
                                TableTreeNode node = (TableTreeNode)childrenodes[i];
                                node.dispose();
                        }
                        
                        super.dispose();
                }
                
                //have to override this function or there will be a subclassing not allowed exception
                @Override
                protected void checkSubclass() 
                {
                        // Disable the check that prevents subclassing of SWT components
                }
        }
        
        //Model choice tree node class
        private class SelTreeNode extends TreeItem
        {
                //xml element node in xml profile
                private Element elemnode;
                private Combo combo;
                
                public SelTreeNode(TreeItem parentItem, int style, 
                                           Element elem)
                {
                        super(parentItem, style);
                        this.elemnode = elem;
                        combo = null;
                }
                
                public SelTreeNode(Tree tree, int style, 
                                           Element elem)
                {
                        super(tree, style);
                        this.elemnode = elem;
                        combo = null;
                }
                
                public TreeItem[] getSiblings()
                {
                        SelTreeNode parent = (SelTreeNode)this.getParentItem();
                        if (parent != null)
                                return parent.getItems();
                        else
                        {
                                Tree tree = this.getParent();
                                return tree.getItems();
                        }
                }
                
                public boolean isRoot()
                {
                        SelTreeNode parent = (SelTreeNode)this.getParentItem();
                        if (parent != null)
                                return false;
                        else
                                return true;
                }
                
                public SelTreeNode getRoot()
                {
                        if (this.isRoot())
                                return this;
                        SelTreeNode parent = (SelTreeNode)this.getParentItem();
                        return parent.getRoot();
                }
                                
                
                private List<Element> getSelChildnode(Element elem)
                {
                        List<Element> elemlist = new ArrayList<Element>();
                        Element tmp;
                        
                        NodeList list = elem.getElementsByTagName(XmlFunction.ATTRIBUTES);
                        for (int i = 0; i < list.getLength(); i++)
                        {
                                tmp = (Element)list.item(i);
                                if (XmlFunction.isSelXmlNode(tmp))
                                elemlist.add(tmp);
                        }
                        
                        list = this.elemnode.getElementsByTagName(XmlFunction.ATTRIBUTESGRP);
                        for (int i = 0; i < list.getLength(); i++)
                        {
                                tmp = (Element)list.item(i);
                                if (XmlFunction.isSelXmlNode(tmp))
                                elemlist.add(tmp);
                        }
                        
                        return elemlist;
                }
                
                public SelTreeNode getNode(Element elem)
                {
                        if (this.elemnode.equals(elem))
                                return this;
                
                        TreeItem[] childs = this.getItems();
                        SelTreeNode node;
                        for (int i = 0; i < childs.length; i++)
                        {
                                node = (SelTreeNode)childs[i];
                                if (node.getNode(elem) != null)
                                        return node.getNode(elem);
                        }
                        
                        return null;
                }
                
                //check if the decision tree is complete
                public boolean Check()
                {       
                        if (this.getItemCount() > 0)
                        {
                                TreeItem[] childs = this.getItems();
                                SelTreeNode node;
                                for (int i = 0; i < childs.length; i++)
                                {
                                        node = (SelTreeNode)childs[i];
                                        if (!node.Check())
                                                return false;
                                }
                                
                                return true;
                        }
                        //leaf node     
                        else 
                                return (getSelChildnode(this.elemnode).size() == 0);
                                
                }
                
                public void collapseChildren()
                {
                        TreeItem[] childrens = this.getItems();
                        SelTreeNode node;
                        for (int i = 0; i < childrens.length; i++)
                        {
                                node = (SelTreeNode)childrens[i];
                                node.collapseChildren();
                                
                                if (node.combo != null)
                                        node.combo.dispose();
                                node.dispose();
                        }
                }
                
                private void addChild(Element elem)
                {
                        if (!elem.getNodeName().equals(XmlFunction.ATTRIBUTES) &&
                                !elem.getNodeName().equals(XmlFunction.ATTRIBUTESGRP))
                                return;
                        
                        Node node;
                        Element tmp;
                        int i;
                        
                        if (XmlFunction.isSelXmlNode(elem))
                        {       
                                SelTreeNode child = new SelTreeNode(this, SWT.NULL, elem);
                                this.setExpanded(true);
                                child.combo  = new Combo(this.getParent(), SWT.NONE | SWT.READ_ONLY);
                                
                                NodeList tmplist = elem.getChildNodes();
                                
                                for (i = 0; i < tmplist.getLength(); i++)
                                {
                                        node = tmplist.item(i);
                                        if (node.getNodeType() == Node.ELEMENT_NODE &&
                                                node.getNodeName().equals(XmlFunction.ATTRIBUTESGRP))
                                        {
                                                elem = (Element)node;
                                                child.combo.add(elem.getAttribute(XmlFunction.NAME));
                                        }       
                                }
                                
                                child.combo.select(0);
                                child.setText(child.combo.getText());
                                child.combo.addSelectionListener(new ComboSelected(child));
                    
                                myTreeEditor editor = new myTreeEditor(this.getParent());
                        editor.horizontalAlignment = SWT.LEFT;
                        editor.verticalAlignment = SWT.CENTER;
                        editor.grabHorizontal = true;
                    editor.setEditor(child.combo, child);
                    
                        }
                        else
                        {
                                NodeList list = elem.getChildNodes();
                                for (i = 0; i < list.getLength(); i++)
                                {
                                        node = list.item(i);
                                        if (node.getNodeType() == Node.ELEMENT_NODE)
                                        {
                                                tmp = (Element)node;
                                                addChild(tmp);
                                        }
                                }
                        }
        
                }
                
                public void SelNode()
                {
                        this.collapseChildren();
                        Element elem = null;
                        
                        NodeList list = this.elemnode.getElementsByTagName(XmlFunction.ATTRIBUTES);
                        if (list.getLength() != 1)
                                return;//throw new RuntimeException("Each generator must have an Attributes node");
                        elem = (Element)list.item(0);
                        
                        addChild(elem);
                }
                
                public void SelCombo()
                {
                        this.collapseChildren();

                        Element elem = null, selelem = null;
                        Node node = null;
                        int i;
                        
                        String selnodename;
                        //find the combo selected Attributes_Group node
                        if (this.combo != null)
                        {
                                if (!this.combo.isDisposed())
                                {
                                        selnodename = this.combo.getText();
                                        setText(this.combo.getText());
                                        NodeList nodelist = this.elemnode.getChildNodes();
                                        for (i = 0; i < nodelist.getLength(); i++)
                                        {
                                                node = nodelist.item(i);
                                                if (node.getNodeType() == Node.ELEMENT_NODE &&
                                                        node.getNodeName().equals(XmlFunction.ATTRIBUTESGRP))
                                                {
                                                        elem = (Element)node;
                                                        if (elem.getAttribute(XmlFunction.NAME).equals(selnodename))
                                                        {
                                                                selelem = elem;
                                                                break;
                                                        }
                                                }
                                        }
                                }
                                
                                if (selelem != null)
                                        addChild(selelem);
                        }
                }
                
                
                //have to override this function or there will be a subclassing not allowed exception
                @Override
                protected void checkSubclass() 
                {
                        // Disable the check that prevents subclassing of SWT components
                }
        }

        //xml profile location
        private String profile;
        private Element root;
        
        Tree modelselTree;
        Tree importableTree;
        Text descriText;
        List<String> geners;
        
        String inputpath;
        Button attachlan;
        
        TreeColumn column1;
        TreeColumn column2;
        
        StatusDlg statusdlg; 
        MessageBox msgdlg; 
        /**
         * Create the wizard.
         */
        public GenAttriPage(String profile, List<String> geners) 
        {
            super(pagename);
            setTitle("Generator Attributes Page");
            setDescription("Spcify attributes for each generator");
            
            this.profile = profile;
            this.root = null;
            this.inputpath = null;
            this.geners = geners;
            
            statusdlg = new StatusDlg();
            msgdlg = new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_QUESTION | SWT.OK);
            msgdlg.setText("Error!");
        }
        
        private void ParseXML(String profile)
        {
	        //parse the profile xml
	        File file = new File(profile);
	        if (!file.exists())
	                return;//return;//throw new RuntimeException("profile does not exist");
	
	        try
	        {
	                // Create a factory
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            // Use the factory to create a builder
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.parse(file);
	            doc.normalize();
	            
	            NodeList nodelist;
	            Element elem;
	            
	            nodelist = doc.getElementsByTagName(XmlFunction.GENERATORS);
	            if (nodelist.getLength() != 1)
	                    return;//throw new RuntimeException("profile does not have a root Generators node");
	            //get the root node of the xml profile
	            this.root = (Element)nodelist.item(0);
	            
	            //get the root node for each generator
	            nodelist = doc.getElementsByTagName(XmlFunction.GENERATOR);
	            SelTreeNode SelTreeNode;
	            for (int i = 0; i < nodelist.getLength(); i++)
	            {
	                    elem = (Element)nodelist.item(i);
	                    String genername = elem.getAttribute("name");
	                    
	                    if (geners.contains(genername))
	                    {
	                            SelTreeNode = new SelTreeNode(this.modelselTree, SWT.NONE, elem);
	                            SelTreeNode.setText(elem.getAttribute("name"));
	                    }
	            }
	        }
	        catch (Exception e)
	        {
	                e.printStackTrace();
	        }       
        }
        
        /**
         * Create contents of the wizard.
         * @param parent
         */
        public void createControl(Composite parent) 
        {
	        initializeDialogUnits(parent);
	        
	        //Create the container and set the Layout
	        Composite composite = new Composite(parent, SWT.NULL);
	        
	        setControl(composite);
	        
	        GridLayout gridlayout = new GridLayout();
	        composite.setLayout(gridlayout);
	        composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL));
	        composite.setSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	        composite.setFont(parent.getFont());
	             
	        SashForm sashForm = new SashForm(composite, SWT.VERTICAL);
	        sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
        
	        createOptionsGroup1(sashForm);
	        createOptionsGroup2(sashForm);
	        sashForm.setWeights(new int[] {3, 1});
	        
	        addListeners();
	        ParseXML(this.profile);
        }
                
        private void createOptionsGroup1(Composite parent) 
        {
        	// container specification group
	        Composite containerGroup = new Composite(parent, SWT.NONE);
	        GridLayout layout = new GridLayout();
	        layout.numColumns = 2;
	        layout.makeColumnsEqualWidth = true;
	        containerGroup.setLayout(layout);
	        containerGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
	        containerGroup.setFont(parent.getFont());

	        GridData data;
	        Label modelLabel = new Label(containerGroup, SWT.FILL);
	        data = new GridData(GridData.FILL_HORIZONTAL);
	        data.horizontalAlignment = GridData.CENTER;
	        modelLabel.setText("Select topology generator:");
	        modelLabel.setLayoutData(data);
	        modelLabel.setFont(parent.getFont());
        
	        Label attristableLabel = new Label(containerGroup, SWT.FILL);
	        data = new GridData(GridData.FILL_HORIZONTAL);
	        data.horizontalAlignment = GridData.CENTER;
	        attristableLabel.setText("Select attributes:");
	        attristableLabel.setLayoutData(data);
	        attristableLabel.setFont(parent.getFont());
        
	        modelselTree = new Tree(containerGroup, SWT.BORDER | SWT.SINGLE);
	        data = new GridData(GridData.FILL_BOTH);
	        modelselTree.setLayoutData(data);
              
	        //disable tree collapse
	        modelselTree.addListener(SWT.Collapse, new Listener() 
            {
                public void handleEvent(final Event event) 
                {
                    modelselTree.setRedraw(false);
                    Display.getDefault().asyncExec(new Runnable() 
                    {
                            public void run()
                            {
                                    ((SelTreeNode)event.item).collapseChildren();
                                    ClearTableTree();
                                    modelselTree.setRedraw(true);
                            }
                    });
                }
            }); 
                
	        modelselTree.addSelectionListener(new SelectionAdapter()
	        {
                public void widgetSelected(SelectionEvent e) 
                {
                        SelTreeNode node = (SelTreeNode) e.item;
                        if (node == null)
                                return;
                        
                        //disable select treeitem with combo
                        if (node.combo != null)
                        {
                                node.combo.setFocus();
                                node.getParent().select(node.getRoot());
                        }
                }
	        });
        
            importableTree = new Tree(containerGroup, SWT.BORDER | SWT.SINGLE);     
            data = new GridData(GridData.FILL_BOTH);
            importableTree.setLayoutData(data);
            importableTree.setLinesVisible(true);
            importableTree.setHeaderVisible(true);
            column1 = new TreeColumn(importableTree, SWT.LEFT);
            column1.setText("Name");
            column1.setWidth(180);
            //column1.pack();
            column2 = new TreeColumn(importableTree, SWT.LEFT);     
            column2.setText("Value");
            column2.setWidth(180);

            importableTree.addListener(SWT.Collapse, new Listener() 
            {
                public void handleEvent(final Event event) 
                {
                    importableTree.setRedraw(false);
                    Display.getDefault().asyncExec(new Runnable() 
                    {
                            public void run()
                            {
                                    TableTreeNode node = (TableTreeNode)event.item;
                                    if (node != null)
                                    {
                                            node.collapse();
                                            TreeColumn tc = node.getParent().getColumn(0);
                                            tc.pack();
                                            tc = node.getParent().getColumn(1);
                                            tc.pack();
                                    }
                                    
                                    importableTree.setRedraw(true);
                            }
                    });
                }
            }); 
                
            importableTree.addListener(SWT.Expand, new Listener() 
            {
                public void handleEvent(final Event event) 
                {
                    importableTree.setRedraw(false);
                    Display.getDefault().asyncExec(new Runnable() 
                    {
                            public void run()
                            {
                                    TableTreeNode node = (TableTreeNode)event.item;
                                    if (node != null)
                                    {
                                            node.expand();
                                            TreeColumn tc = node.getParent().getColumn(0);
                                            tc.pack();
                                            tc = node.getParent().getColumn(1);
                                            tc.pack();
                                    }
                                    importableTree.setRedraw(true);
                            }
                    });
                }
            }); 
                
            final myTreeEditor editor = new myTreeEditor(importableTree);
            // The editor must have the same size as the cell and must
            // not be any smaller than 50 pixels.
            editor.grabVertical = true;
	        editor.grabHorizontal = true;
	        editor.horizontalAlignment = SWT.LEFT;
	        
	        editor.layout();
        
            // editing the second column
            final int EDITABLECOLUMN = 1;
            importableTree.addSelectionListener(new SelectionAdapter()
            {
                public void widgetSelected(SelectionEvent e) 
                {
                    // Clean up any previous editor control
                    Control oldEditor = editor.getEditor();
                    if (oldEditor != null)
                            oldEditor.dispose();

                    // Identify the selected row
                    TableTreeNode node = (TableTreeNode) e.item;
                    if (node == null)
                            return;
                    else 
                    {
                        TreeItem [] childrenodes = node.getItems();
                        if (childrenodes.length == 0)
                        {
                            //add combo
                            GeneratorAttri attri = new GeneratorAttri(node.elemnode);
                            AttriConstraint constraint = new AttriConstraint(attri.getAttriConstraint());
                            
                            if (constraint.getType() == 1)
                            {
                                    node.combo = new Combo(importableTree, SWT.NONE | SWT.READ_ONLY);
                                    //node.combo.setBounds(0, 0, 30, 20);
                                    java.util.List<EnumType> enumlist = constraint.getEnumList();
                                    
                                    for (int j = 0; j < enumlist.size(); j++)
                                            node.combo.add(enumlist.get(j).getEnumName());
                                    node.combo.select(0);
                                    node.setText(1, node.combo.getText());
                                    
                                    node.combo.addSelectionListener(new ComboModified(node, node.combo));
                                    
                                    editor.setEditor(node.combo, node, EDITABLECOLUMN);
                            }
                            else
                            {
                                    // The control that will be the editor must be a child of the Table
                                    node.text = new Text(importableTree, SWT.NONE);
                                    node.text.setText(node.getText(EDITABLECOLUMN));
                                    
                                    node.text.addModifyListener(new TextModified(editor, 
                                                                                             node.elemnode.getAttribute(XmlFunction.DESCRIPTION)));
                                    node.text.selectAll();
                                    node.text.setFocus();
                                    
                                    editor.setEditor(node.text, node, EDITABLECOLUMN);
                            }
                        }
                    }
                }
            });
        }
        
        private void createOptionsGroup2(Composite parent)
        {
        	// container specification group
	        Composite containerGroup = new Composite(parent, SWT.NONE);
	        GridLayout layout = new GridLayout();
	        layout.numColumns = 1;
	        containerGroup.setLayout(layout);
	        containerGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
	        containerGroup.setFont(parent.getFont());

	        GridData data;
        
	        Label describeLabel = new Label(containerGroup, SWT.FILL);
	        data = new GridData(GridData.FILL_HORIZONTAL);
	        describeLabel.setText("Description:");
	        describeLabel.setLayoutData(data);
	        describeLabel.setFont(parent.getFont());
           
	        descriText = new Text(containerGroup, 
	                                                  SWT.WRAP | SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.V_SCROLL);
	        data = new GridData(GridData.FILL_BOTH);
	        data.minimumHeight = 50;
	        descriText.setLayoutData(data);
	        descriText.setFont(parent.getFont());
	        
	        attachlan = new Button(containerGroup, SWT.CHECK);
	        attachlan.setText("Check box if you want to attach hosts and subnets to the generated topology");
	        data = new GridData(GridData.FILL_HORIZONTAL);
	        data.verticalAlignment = SWT.CENTER;
	        data.horizontalSpan = 2;
	        attachlan.setLayoutData(data);
	        attachlan.setSelection(false);
	        attachlan.addSelectionListener(new SelectionListener()
	        {
                @Override
                public void widgetSelected(SelectionEvent e) 
                {
                        // TODO Auto-generated method stub
                        UpdateButton();
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                        // TODO Auto-generated method stub
                        
                }
                
	        });
        }
        
        private static void ExpandAll(TreeItem item)
        {
            item.setExpanded(true);
            
            TreeItem [] childrenodes = item.getItems();
            for (int i = 0; i < childrenodes.length; i++)
                    ExpandAll(childrenodes[i]);
        }
        
        //clear all the nodes of TableTree
        private void ClearTableTree()
        {
            this.root = null;
            
            TreeItem [] children = importableTree.getItems();
            TableTreeNode node;
            for (int i = 0; i < children.length; i++)
            {
                    node = (TableTreeNode)children[i];
                    node.dispose();
            }
            
            column1.pack();
            column2.pack();
            
            UpdateButton();
        }
        
        /**
         * Adds event listeners to the components that require them
         */
        private void addListeners()
        {
            this.modelselTree.addSelectionListener(new SelectedSelTree());
            this.importableTree.addSelectionListener(new SelectedTableTree());
            //importableTree.addListener(SWT.KeyDown, this);
        }
        
        /**
         * Functions to Handle event 
        */
        private final class SelectedSelTree extends SelectionAdapter
        {
            SelectedSelTree()
            {
                super();
            }
                
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                SelTreeNode node = (SelTreeNode)event.item;
                if (node == null) return;
                
                String descri = node.elemnode.getAttribute(XmlFunction.DESCRIPTION);
                descriText.setText(descri);
                
                //change generater clear table tree
                if (node.getParentItem() == null)
                        ClearTableTree();
                
                TreeItem [] siblingnodes = null;
                int i;
		        if (node.isRoot())
		        {
		                siblingnodes = node.getSiblings();
		                for (i = 0;i < siblingnodes.length; i++)
		                {
		                        SelTreeNode tmpnode = (SelTreeNode)siblingnodes[i];
		                        tmpnode.collapseChildren();
		                }
		                
		                if (node.Check())
		                        CreateImportTableTree(node);
		                else
		                node.SelNode();
		        }
            }
            
        }
        
        private final class ComboSelected extends SelectionAdapter
        {
            private SelTreeNode node;
            ComboSelected(SelTreeNode node)
            {
                this.node = node;
            }

            @Override
            public void widgetSelected(SelectionEvent e) 
            {
                // TODO Auto-generated method stub
                String descri = node.elemnode.getAttribute(XmlFunction.DESCRIPTION);
                descriText.setText(descri);
                
                ClearTableTree();
                
                node.SelCombo();
                
                if (node.getRoot().Check())
                    CreateImportTableTree(node.getRoot());
            }
        }
        
        private final class SelectedTableTree extends SelectionAdapter
        {
            SelectedTableTree()
            {
                    super();
            }
                
            @Override
            public void widgetSelected(SelectionEvent event)
            {
                TableTreeNode node = (TableTreeNode)event.item;
                if (node == null) return;
                
                String descri = node.elemnode.getAttribute(XmlFunction.DESCRIPTION);
                descriText.setText(descri);
            }
        }
        
        private static final class ComboModified implements SelectionListener
        {
            private TreeItem item;
            private Combo combo;
            ComboModified(TreeItem item, Combo combo)
            {
                this.item = item;
                this.combo = combo;
            }
            @Override
            public void widgetDefaultSelected(SelectionEvent e) 
            {
                // TODO Auto-generated method stub
                this.item.setText(1, this.combo.getText());
            }

            @Override
            public void widgetSelected(SelectionEvent e) 
            {
                // TODO Auto-generated method stub
                this.item.setText(1, this.combo.getText());
            }
        }
        
        private static final class TextModified implements ModifyListener
        {
            private myTreeEditor editor;
            private String constraint;
            
            public TextModified(myTreeEditor editor, String constraint)
            {
                this.editor = editor;
                this.constraint = constraint;
            }

            @Override
            public void modifyText(ModifyEvent e) 
            {
                // TODO Auto-generated method stub
                Text text = (Text) editor.getEditor();  
                if (this.constraint.equals(""))
                {
                    editor.getItem().setText(1, text.getText());
                    return;
                }
                else
                {
                    AttriConstraint attriconstraint = new AttriConstraint(this.constraint);
            
                    try
                    {
                            if (attriconstraint.Valiate(text.getText()))
                                    editor.getItem().setText(1, text.getText());
                    }
                    catch (Exception exception)
                    {
                            exception.printStackTrace();
                    }
                }
            }
        }
        
        
        private void CreateImportTableTree(SelTreeNode root)
        {
            this.root = root.elemnode;
            
            NodeList list = root.elemnode.getElementsByTagName(XmlFunction.ATTRIBUTES);
            if (list.getLength() != 1)
                    return;//throw new RuntimeException("Each generator much have an Attributes node");
            Element attriselem = (Element)list.item(0);
            
            AddAttrisIntoImportTable(root, attriselem, null);
            
            TreeItem [] nodes = importableTree.getItems();
            for (int i = 0; i < nodes.length; i++)
            {
                    ExpandAll(nodes[i]);
            }
            
            column1.pack();
            column2.pack();
            
            UpdateButton();
        }
        
        //add import attributes into import table tree
        private void AddAttrisIntoImportTable(SelTreeNode root, Element elemnode, TableTreeNode parentnode)
        {               
            NodeList nodelist = elemnode.getChildNodes();
            Node node;
            Element elem;
            TableTreeNode treenode = null;
            int i;
            List<Element> list = new ArrayList<Element>();
            
            //add children attributes element to table tree
            for (i = 0; i < nodelist.getLength(); i++)
            {
                node = nodelist.item(i);
                if(node.getNodeType() == Node.ELEMENT_NODE)
                {   
                    elem = (Element)node;
                    
                    if (node.getNodeName().equals(XmlFunction.ATTRIBUTE))
                    {
                        if (parentnode != null)
                            treenode = new TableTreeNode(parentnode, SWT.NONE, elem);
                        else 
                            treenode = new TableTreeNode(importableTree, SWT.NONE, elem);
                    }
                    else if (node.getNodeName().equals(XmlFunction.ATTRIBUTESGRP))
                            list.add((Element)node);
                }
            }
            
            if (XmlFunction.isSelXmlNode(elemnode))
            {
                SelTreeNode selnode = root.getNode(elemnode);
                
                for (i = 0; i < list.size(); i++)       
                {
                    elem = (Element)list.get(i);
                    if (elem.getElementsByTagName(XmlFunction.ATTRIBUTE).getLength() == 0)
                            continue;
                    
                    if(elem.getAttribute(XmlFunction.NAME).equals(selnode.getText()))
                    {
                        if (parentnode != null)
                                treenode = new TableTreeNode(parentnode, SWT.NONE, elem);
                        else 
                                treenode = new TableTreeNode(importableTree, SWT.NONE, elem);
                        
                        AddAttrisIntoImportTable(root, elem, treenode);
                        break;
                    }
                }
            }
            else
            {
                for (i = 0; i < list.size(); i++)
                {
                    elem = (Element)list.get(i);
                    if (elem.getElementsByTagName(XmlFunction.ATTRIBUTE).getLength() == 0)
                            continue;
                    
                    if (parentnode != null)
                            treenode = new TableTreeNode(parentnode, SWT.NONE, elem);
                    else 
                            treenode = new TableTreeNode(importableTree, SWT.NONE, elem);
                    
                    AddAttrisIntoImportTable(root, elem, treenode);
                }
            }
        }
        
        @Override
        public boolean isPageComplete()
        {
                
            if (this.importableTree.getItemCount() == 0) 
            {
                setErrorMessage("Please select a topology generator.");
                //button.setEnabled(false);
                
                return false;
            }
            else
            {
                setErrorMessage(null);
                //button.setEnabled(true);
                
                return true;
            }
        }
        
        public void UpdateButton()
        {
            this.getWizard().getContainer().updateButtons();
            setPageComplete(isPageComplete());
        }
        
        //write user input xml file
        private void WriteXML()
        {
            try
            {
                // Create a factory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                // Use the factory to create a builder
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();
                
                org.w3c.dom.Text textseg;
                
                NodeList nodelist;
                Element generpathnode = null, importfilenode = null, exportfilenode = null;

                String gener_path, import_file, export_file;
                
                //java temporary file path
                String temp_path = System.getProperty("java.io.tmpdir");
                if (!(temp_path.endsWith(File.separator)) )
                        temp_path = temp_path + File.separator;
                
                String primexDirectory = ConfigurationHandler.getPrimexDirectory();
                if (!primexDirectory.endsWith(File.separator))
                        primexDirectory += File.separator;
         
                    String path = primexDirectory + "topology";
                    
                //gernerators node
                Element geners = doc.createElement("Generators");
                doc.appendChild(geners);
                
                //generator node
                String genername = root.getAttribute(XmlFunction.NAME);
                Element gener = doc.createElement(XmlFunction.GENERATOR);
                gener.setAttribute("name", genername);
                geners.appendChild(gener);
               
                //generator path node
                nodelist = root.getElementsByTagName(XmlFunction.GENERATORPATH);
                if (nodelist.getLength() == 1)
                { 
                    generpathnode = (Element)nodelist.item(0);
                    if (generpathnode.getFirstChild() != null)
                        gener_path = path + generpathnode.getFirstChild().getNodeValue() + File.separator;
                    else 
                        return;//throw new RuntimeException("Generator_Path node has invaild value");
                }
                else 
                    return;//throw new RuntimeException("Each generator must have a Generator_Path node");
                
                Element generpath = doc.createElement(XmlFunction.GENERATORPATH);
                textseg = doc.createTextNode(gener_path);
                generpath.appendChild(textseg);
                gener.appendChild(generpath);
                
                //import file path
                nodelist = root.getElementsByTagName(XmlFunction.IMPORTFILE);
                if (nodelist.getLength() == 1)
                {
                    importfilenode = (Element)nodelist.item(0);
                    if (importfilenode.getFirstChild() != null)
                    {   
                        import_file = temp_path + importfilenode.getFirstChild().getNodeValue();        
                    }
                    else
                        import_file = null;
                }
                else 
                    return;//throw new RuntimeException("Each generator must have a Import_File node");
                
                Element importpath = doc.createElement(XmlFunction.IMPORTFILE);
                if (import_file != null)
                {
                    textseg = doc.createTextNode(import_file);
                    importpath.appendChild(textseg);
                }	
                gener.appendChild(importpath);
                
                //export file path
                nodelist = root.getElementsByTagName(XmlFunction.EXPORTFILE);
                if (nodelist.getLength() == 1)
                {
                    exportfilenode = (Element)nodelist.item(0);
                    if (exportfilenode.getFirstChild() !=null)
                    {
                    	export_file = temp_path + exportfilenode.getFirstChild().getNodeValue();
                    }
                    else
                		return;//throw new RuntimeException("Export_File node has invaild value");
                }
                else
                    return;//throw new RuntimeException("Each generator must have a Export_File node");
	                
                Element exportpath = doc.createElement(XmlFunction.EXPORTFILE);
                textseg = doc.createTextNode(export_file);
                exportpath.appendChild(textseg);
                gener.appendChild(exportpath);
            
            	//Attributes
            	Element attris = doc.createElement(XmlFunction.ATTRIBUTES);
            	gener.appendChild(attris);
            
            	WriteAttris(doc, attris);
            
            	this.inputpath = temp_path + "input.xml";
            	XmlFunction.doc2XmlFile(doc, this.inputpath);
            }
            catch (Exception e)
            {
                    e.printStackTrace();
            }
        }
                
        private void WriteAttris(Document doc, Element attris)
        {
            TreeItem [] treeitems = this.importableTree.getItems();
            for (int i = 0; i < treeitems.length; i++)
            {
                TableTreeNode node = (TableTreeNode)treeitems[i];
                WriteAttribute(doc, node, attris);
            }
        }
        
        private void WriteAttribute(Document doc, TableTreeNode treeitem, Element parent)
        {
            TreeItem [] childrenitems = treeitem.getItems();
            //attributes group node
            if (childrenitems.length > 0)
            {
                Element attrisgrp = doc.createElement(XmlFunction.ATTRIBUTESGRP);
                attrisgrp.setAttribute("name", treeitem.getText(0));
                parent.appendChild(attrisgrp);
                
                for (int i = 0; i < childrenitems.length; i++)
                {
                    TableTreeNode node = (TableTreeNode)childrenitems[i];
                    WriteAttribute(doc, node, attrisgrp);
                }
            }
            //attribute node
            else
            {
                if (!treeitem.getText(1).equals(""))
                {
                    Element attri = doc.createElement(XmlFunction.ATTRIBUTE);
                    //attribute name
                    attri.setAttribute("name", treeitem.getText(0));
                    //attribute value       
                    org.w3c.dom.Text textseg;
                    String value = treeitem.getText(1);
                    AttriConstraint constraint = new AttriConstraint(treeitem.elemnode.getAttribute(XmlFunction.CONSTRAINT));
                    if (constraint.getType() == 1)
                    {
                        java.util.List<EnumType> enumlist = constraint.getEnumList();
                        for (int i = 0; i < enumlist.size(); i++)
                        {
                            EnumType e = enumlist.get(i);
                            if (value.equals(e.getEnumName()))
                            {
                                    value = String.valueOf(e.getEnumValue());
                            }
                        }
                    }
                    textseg = doc.createTextNode(value);
                    attri.appendChild(textseg);
            
                    //attribute description
                    //attri.setAttribute("description", treeitem.getText(2));
                    //attribute constraint
                    //attri.setAttribute("constraint", treeitem.getText(3));
                    //attribute type
                    //attri.setAttribute("type", treeitem.getText(4));
                    
                    parent.appendChild(attri);
                }
            }
        }
        
        private XmlScript getXmlScript()
        {
            statusdlg.setVisible(true);
            
            //write input xml and call  generator to create topology
            WriteXML();
            XmlScript xmlscript = null;
            
            if (getGeneratorName().equals(BRITE.GENERNAME))
            {       
                BRITE brite = new BRITE(getInputFilePath(), statusdlg.getTextArea());
                   
                if (!brite.CreateTopology()) {
                    msgdlg.setMessage("BRITE create topology fail!");
                    msgdlg.open();
                }
                else
                    xmlscript = brite.xmlscript;
            }
            
            if (getGeneratorName().equals(RocketFuel.GENERNAME))
            {
                RocketFuel rocketfuel = new RocketFuel(getInputFilePath(),statusdlg.getTextArea());
          
                if (!rocketfuel.CreateTopology()) {
                    msgdlg.setMessage("rocketfuel create topology fail!");
                    msgdlg.open();
                }
                else
                    xmlscript = rocketfuel.xmlscript;
            }
            
            if (getGeneratorName().equals(Inet.GENERNAME))
            {
                Inet inet = new Inet(getInputFilePath(), statusdlg.getTextArea());
   
                if (!inet.CreateTopology()) {
                    msgdlg.setMessage("Inet create topology fail!");
                    msgdlg.open();
                }
                else
                    xmlscript = inet.xmlscript;

            }
            
            if (getGeneratorName().equals(Orbis.GENERNAME))
            {
                Orbis orbis = new Orbis(getInputFilePath(), statusdlg.getTextArea());
            
                if (!orbis.CreateTopology()) {
                	msgdlg.setMessage("Orbis create topology fail!");
                    msgdlg.open();
                }
                else
                    xmlscript = orbis.xmlscript;
            }
            
            if (getGeneratorName().equals(GT_ITM.GENERNAME))
            {
                GT_ITM gt_itm = new GT_ITM(getInputFilePath(), statusdlg.getTextArea());
  
                if (!gt_itm.CreateTopology()) {
                	msgdlg.setMessage("GT_ITM create topology fail!");
                    msgdlg.open();
                }
                else
                    xmlscript = gt_itm.xmlscript;

            }
    
            statusdlg.getTextArea().setText("");
            statusdlg.setVisible(false);
            
            return xmlscript;
        }
        
        public String getGeneratorName()
        {
            if (this.root != null)
            	return root.getAttribute(XmlFunction.NAME);
            return "";
        }
        
        public String getInputFilePath()
        {
        	return this.inputpath; 
        }
        
        @Override
        public boolean canFlipToNextPage()
        {       
        	return isPageComplete() && 
        			attachlan.getSelection();
        }
        
        /**
         * Obtains the next Page for this Wizard and initializes the data it requires.
         */
        public IWizardPage getNextPage()
        {
            ((TopoWizard)getWizard()).xmlsrcipt = getXmlScript();
            if (((TopoWizard)getWizard()).xmlsrcipt != null) 
            {
            	AttachLanPage page = ((TopoWizard)getWizard()).attachlanpage;
            	page.SetAttri(getGeneratorName(),
                              ((TopoWizard)getWizard()).xmlsrcipt);
                    
                return super.getNextPage();
            }
            else
              return null;
        }
        
        public void PerformFinish()
        {
            ((TopoWizard)getWizard()).xmlsrcipt = getXmlScript();
        }
}