package slingshot.wizards.configuration;

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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import jprime.gen.ModelNodeVariable.ClsDesc;
import jprime.gen.ModelNodeVariable.VarDesc;
import jprime.variable.ModelNodeVariable;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

/**
 * This class defines the Wizard Page that asks for the necessary configuration parameters in the Slingshot Configuration Wizard.
 *
 * @author Nathanael Van Vorst
 *
 */
public class EditDefaultsPage extends WizardPage implements Listener {
	public static final class Cls {
		final String name,super_name;
		final TreeMap<String,Attr> attrs = new TreeMap<String, EditDefaultsPage.Attr>();
		final TreeMap<String,Cls> subs = new TreeMap<String, EditDefaultsPage.Cls>();
		public Cls(String name, String super_name) {
			this.name = name;
			this.super_name=super_name;
		}
		public Attr addAttr(String attr, String default_value, String doc){
			Attr a = new Attr(this,attr,default_value,doc);
			attrs.put(attr, a);
			return a;
		}
		public void addClass(Cls cls) {
			subs.put(cls.name, cls);
		}
	}
	public static final class Attr {
		//final Cls cls;
		final String name,default_value,doc;
		String _value;
		public Attr(Cls cls, String name, String default_value, String doc) {
			//this.cls = cls;
			this.name = name;
			this.default_value = default_value;
			this.doc=doc;
			this._value = null;
		}
		void setValue(String v) {
			if(v != null && v.compareTo(default_value)!=0)
				_value=v;
			else
				_value=null;
		}
		String getValue() {
			if(null != _value) return _value;
			return default_value;
		}
	}
	private final File configfile;
	private Text default_value,doc;
	private Attr selected=null;
	private final LinkedList<Cls> values = new LinkedList<EditDefaultsPage.Cls>();
	private Tree tree;
	
	protected EditDefaultsPage(String pageName, File configfile) {
		super(pageName);
		setTitle("View/Edit Default Values");
		setDescription("You can view and edit the default values for attributes within this experiment.");
		this.configfile = configfile;
		loadDefaultValues(this.configfile, values);
		tree=null;
	}
	
	protected static HashMap<String,Cls> loadDefaultValues(File f, LinkedList<Cls> roots) {
		Properties props = new Properties();
		if(f.exists() && f.isFile()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				props.load(fis);
				fis.close();
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
		}
		HashMap<String,Cls> t = new HashMap<String, EditDefaultsPage.Cls>();
		for(ClsDesc c : ModelNodeVariable.getAllClsDesc()) {
			Cls cls = new Cls(c.name,c.super_desc==null?null:c.super_desc.name);
			for(VarDesc a : c.vars.values()) {
				if(a.is_stat || !ModelNodeVariable.userModifiable(a.id))
					continue;
				Attr attr = cls.addAttr(a.name,a.default_value,a.doc_string);
				attr._value = props.getProperty(cls.name+"."+a.name, null);
			}
			t.put(cls.name,cls);
		}
		LinkedList<Cls> v = new LinkedList<EditDefaultsPage.Cls>();
		for(Cls c : t.values()) {
			if(c.super_name==null)v.add(c);
			else t.get(c.super_name).addClass(c);
		}
		for(Cls c : v) {
			cleanup(c,new HashSet<String>());
			if(roots != null) roots.add(c);
		}
		
		return t;
	}

	
	private static void cleanup(Cls c, HashSet<String> attrs) {
		for(String a : attrs) {
			c.attrs.remove(a);
		}
		HashSet<String> attrs1 = new HashSet<String>();
		attrs1.addAll(c.attrs.keySet());
		attrs1.addAll(attrs);
		for(Cls s : c.subs.values())
			cleanup(s,attrs1);
	}
	
	protected void save() {
		this.configfile.delete();
		try {
			final LinkedList<String> f = new LinkedList<String>();
			for(Cls c : values) {
				save(c, f);
			}
			final BufferedWriter out = new BufferedWriter(new FileWriter(this.configfile));
			for(String s : f) {
				out.write(s);
			}
			out.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}
		
	protected static void save(final Cls cls, final LinkedList<String> f) {
		for(Attr a : cls.attrs.values()) {
			if(a._value != null) {
				f.add(cls.name+"."+a.name+"="+a._value+"\n");
			}
		}
		for(Cls c : cls.subs.values())
			save(c,f);
	}
	
	protected static void save(final Cls cls, final Map<String,String> rv) {
		for(Attr a : cls.attrs.values()) {
			if(a._value != null) {
				rv.put(cls.name+"::"+a.name,a._value);
			}
		}
		for(Cls c : cls.subs.values())
			save(c,rv);
	}

	
	/**
	 * Handles any event that occurs inside this Wizard Page
	 * Events may come from the text fields or the browse buttons
	 */
	@Override
	public void handleEvent(Event event) {
		setPageComplete(isPageComplete());
	}

	/**
	 * Creates the visual components for this Wizard Page
	 *
	 * @param parent				The parent element for this Wizard Page
	 */
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		ManagedForm mf = new ManagedForm(parent);
		FormToolkit toolkit = mf.getToolkit();
		ScrolledForm form = mf.getForm();
		Composite composite = form.getBody();
		composite.setLayout(new GridLayout(3,true));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());

        tree = toolkit.createTree(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan=3;
		tree.setLayoutData(gd);
        TreeColumn header = new TreeColumn(tree, SWT.LEFT);
        header.setText("Class");
        header.setResizable(true);
        header.setWidth(200);
        header = new TreeColumn(tree, SWT.LEFT);
        header.setText("Attribute");
        header.setResizable(true);
        header.setWidth(150);
        tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
        for(Cls c: values) {
        	for(Cls cc : c.subs.values()) {
        		create(null,cc);
        	}
        }
        tree.setSize(tree.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        toolkit.createLabel(composite,"Default Value:", SWT.NONE);
        default_value = toolkit.createText(composite, "");
        default_value.setEditable(true);
        gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan=2;
		default_value.setLayoutData(gd);
        toolkit.createLabel(composite,"Description:", SWT.NONE);
        doc = toolkit.createText(composite,"",SWT.MULTI|SWT.WRAP);
        doc.setEditable(false);
        gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan=2;
		gd.verticalSpan=4;
		doc.setLayoutData(gd);
        tree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(e.item != null && e.item.getData()!=null) {
					selected = (Attr)e.item.getData();
					default_value.setText(selected.getValue());
					doc.setText(selected.doc);
				}
				else {
					selected = null;
					default_value.setText("");
					doc.setText("");
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
        default_value.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if(selected != null) {
					selected.setValue(default_value.getText());
				}
			}
		});

		
        setControl(form);
        setErrorMessage(null);// should not initially have error message
	}
	
	private void create(TreeItem p, Cls c) {
		if(c.attrs.size()==0 && c.subs.size()==0)return;
    	final TreeItem ti = p==null?(new TreeItem(tree, 0)):new TreeItem(p, 0);
    	ti.setText(new String[] {c.name});
    	ti.setData(null);
    	if(c.attrs.size()>0) {
    		final TreeItem asi = new TreeItem(ti, 0);
	    	asi.setText(new String[] {"Attributes"});
	    	asi.setData(null);
	    	for(Attr a : c.attrs.values()) {
	    		final TreeItem ai = new TreeItem(asi, 0);
	    		ai.setText(new String[] {"",a.name});
	        	ai.setData(a);
	    	}
	    	asi.setExpanded(true);
    	}
    	if(c.subs.size()>0) {
    		final TreeItem asi = new TreeItem(ti, 0);
	    	asi.setText(new String[] {"Derived Classes"});
	    	asi.setData(null);
	    	for(Cls s : c.subs.values()) {
	    		create(asi,s);
	    	}
    	}
	}

	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage(){
		// no next page for this path through the wizard
		return false;
	}

	/**
	 * Determines whether this page has been completed
	 *
	 * @return boolean				TRUE if the page is complete, FALSE otherwise
	 */
	public boolean isPageComplete(){
		return true;
	}
}
