package slingshot.experiment.launch;

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import jprime.SymbolTable;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import slingshot.experiment.PyExperiment;

/**
 * This class defines the Wizard Page that asks for the necessary configuration parameters in the Slingshot Configuration Wizard.
 *
 * @author Nathanael Van Vorst
 *
 */
public class SymbolsPage extends WizardPage implements Listener {
	private static class Sym {
		final String name;
		String value_;
		public Sym(String name) {
			super();
			this.name = name;
			this.value_ = null;
		}
		public String setValue(String v) {
			if(v == null || v.length()==0)
				value_=null;
			else value_=v;
			return getValue();
		}
		public String getValue() {
			if(value_ == null)
				return "<USE DEFAULT VALUE>";
			return value_;
		}
		public String toString() {
			return name+":"+getValue();
		}
	}
	private Table symbolTable=null;
	private PyExperiment exp;
	private HashSet<Sym> symbols = new HashSet<SymbolsPage.Sym>();
	protected SymbolsPage(String pageName, PyExperiment exp) {
		super(pageName);
		this.exp=exp;
		setTitle("Specify Symbols");
		setDescription("Specify values for symbols within the Experiment.");
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
		composite.setFont(parent.getFont());
		parent.setBackground(toolkit.getColors().getBackground());
		parent.getParent().setBackground(toolkit.getColors().getBackground());
		
		symbolTable = toolkit.createTable(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan=3;
		symbolTable.setLayoutData(gd);
		TableColumn header = new TableColumn(symbolTable, SWT.LEFT);
        header.setText("Symbol Name");
        header.setResizable(true);
        header.setWidth(200);
        header = new TableColumn(symbolTable, SWT.LEFT);
        header.setText("Value");
        header.setResizable(true);
        header.setWidth(150);
        symbolTable.setLinesVisible(true);
        symbolTable.setHeaderVisible(true);
        symbolTable.setParent(composite);

        final SymbolTable st = exp.getExperiment().getMetadata().getSymbolTable();
        for(String s : st.getSymbolMap().keySet()) {
        	Sym sym = new Sym(s);
        	symbols.add(sym);
            TableItem si = new TableItem(symbolTable, 0);
        	si.setText(new String[] {sym.name,sym.getValue()});
        	si.setData(sym);
        }
    	symbolTable.setSize(symbolTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    	final TableEditor editor = new TableEditor (symbolTable);
    	editor.horizontalAlignment = SWT.LEFT;
    	editor.grabHorizontal = true;
    	symbolTable.addListener (SWT.MouseDown, new Listener () {
    		public void handleEvent (Event event) {
    			Rectangle clientArea = symbolTable.getClientArea ();
    			Point pt = new Point (event.x, event.y);
    			int index = symbolTable.getTopIndex ();
    			while (index < symbolTable.getItemCount ()) {
    				boolean visible = false;
    				final TableItem item = symbolTable.getItem (index);
    				final Sym sym = (Sym)item.getData();
    				for (int i=1; i<symbolTable.getColumnCount (); i++) {
    					Rectangle rect = item.getBounds (i);
    					if (rect.contains (pt)) {
    						final int column = i;
    						final Text text = new Text (symbolTable, SWT.NONE);
    						Listener textListener = new Listener () {
    							public void handleEvent (final Event e) {
    								switch (e.type) {
    									case SWT.FocusOut:
    										sym.setValue( text.getText ());
											item.setText (column, sym.getValue());
    										text.dispose ();
    										break;
    									case SWT.Traverse:
    										switch (e.detail) {
    											case SWT.TRAVERSE_RETURN:
    	    										sym.setValue( text.getText ());
    												item.setText (column, sym.getValue());
    												//FALL THROUGH
    											case SWT.TRAVERSE_ESCAPE:
    												text.dispose ();
    												e.doit = false;
    										}
    										break;
    								}
    							}
    						};
    						text.addListener (SWT.FocusOut, textListener);
    						text.addListener (SWT.Traverse, textListener);
    						editor.setEditor (text, item, i);
    						text.setText (item.getText (i));
    						text.selectAll ();
    						text.setFocus ();
    						return;
    					}
    					if (!visible && rect.intersects (clientArea)) {
    						visible = true;
    					}
    				}
    				if (!visible) return;
    				index++;
    			}
    		}
    	});    	
        symbolTable.addListener(SWT.Selection,this);
		setControl(form);
        setErrorMessage(null);
        setPageComplete(false);
	}
	
	public Map<String,String> getSymbolMapping() {
		HashMap<String,String> rv = new HashMap<String,String>();
		for(Sym s : symbols) {
			if(s.value_ != null) {
				rv.put(s.name,s.value_);
			}
		}
		return rv;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		setPageComplete(isPageComplete());
	}
	
	/**
	 * Determines whether conditions are met to flip to the next Wizard's Page
	 *
	 * @return boolean				TRUE if the user can flip to the next page, FALSE otherwise
	 */
	public boolean canFlipToNextPage(){
		return isPageComplete();
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
