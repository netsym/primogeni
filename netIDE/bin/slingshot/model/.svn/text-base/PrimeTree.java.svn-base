package slingshot.model;

import java.util.ArrayList;

import jprime.IModelNode;
import jprime.Link.ILink;
import jprime.Net.INet;
import jprime.variable.Dataset;
import jprime.variable.ModelNodeVariable;
import monitor.core.IController;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.experiment.PyExperiment;
import slingshot.visualization.WrappedNet;

/**
 * 
 * When a model node is selected we can display it's attributes a hierarchy.
 * This is a tree of attributes.
 * 
 * @author Nathanael Van Vorst
 * 
 */
public class PrimeTree {
	public final PyExperiment pyexp;
	public final Tree tree;
	public IModelNode node;
	public ArrayList<PrimeTreeItem> kids = new ArrayList<PrimeTreeItem>();
	private PrimeTreeItem sel;
	private final SelectionListener slistener = new SelectionListener() {
		private PrimeTreeItem sel = null;
		public void widgetSelected(SelectionEvent e) {
			if(e.item!=null && e.item.getData() != null) {
				sel = handleSelection((PrimeTreeItem)e.item.getData(),sel);
			}
		}
		public void widgetDefaultSelected(SelectionEvent e) {
			if(e.item!=null && e.item.getData() != null)
				sel = handleSelection((PrimeTreeItem)e.item.getData(),sel);
		}
	};
	private final MouseListener mlistener = new MouseListener() {
		public void mouseUp(MouseEvent e) {
		}
		public void mouseDown(MouseEvent e) {
		}
		public void mouseDoubleClick(MouseEvent e) {
			if(e.widget != null && e.widget instanceof Tree) {
				Tree t = (Tree)e.widget;
				TreeItem[] i = t.getSelection();
				if(i != null && i.length>0) {
					final PrimeTreeItem pi = (PrimeTreeItem)i[0].getData();
					if(pi.node != null && !(pi.node instanceof WrappedNet)) {
						if(i[0].getText()!=null
							&& (
									(
											i[0].getText().compareTo("name")==0 &&
											i[0].getText(1)!=null && i[0].getText(1).compareTo(pi.node.getName())==0
									)
									||
									(
											i[0].getText().compareTo("parent")==0 &&
											i[0].getText(1)!=null && i[0].getText(1).compareTo(pi.node.getName())==0
									)
									||
									i[0].getText().compareTo(pi.node.getName())==0
									|| 
									i[0].getText().compareTo(pi.node.deference().getName()+"("+pi.node.getName()+")")==0
								)) {
							/*
							try {
								pyexp.selectNode(pi.node);
							} catch(Exception ee) {
								ee.printStackTrace();
							}
							*/
						}
					}
				}
			}
		}
	};
	public PrimeTree(Composite parent, int style, IModelNode node, PyExperiment pyexp) {
		this.pyexp=pyexp;
		this.tree = new Tree(parent, style);
		this.node=node;
		this.sel = null;
		tree.addSelectionListener(slistener);
		tree.addMouseListener(mlistener);
	}
	public PrimeTreeItem handleSelection(final PrimeTreeItem item, final PrimeTreeItem prev_item) {
		if(this.sel != item && prev_item == item && item.var!=null && ModelNodeVariable.userModifiable(item.var.id)) {
			this.sel=item;
			if(item.isSymbol) {
				this.sel=null;
				return item;
			}
			// Create the editor and set its attributes
			final TreeEditor editor = new TreeEditor(tree);
			editor.horizontalAlignment = SWT.LEFT;
			editor.grabHorizontal = true;
			editor.setColumn(1);

			// Create a text field to do the editing
			final Text text = new Text(tree, SWT.NONE);
			text.setText(item.treeItem.getText(1));
			text.selectAll();
			text.setFocus();

			// If the text field loses focus, set its text into the tree
			// and end the editing session
			text.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent event) {
					setValue(item,text.getText());
					text.dispose();
					unselect();
				}
			});

			// If they hit Enter, set the text into the tree and end the editing
			// session. If they hit Escape, ignore the text and end the editing
			// session
			text.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent event) {
					switch (event.keyCode) {
					case SWT.CR:
						// Enter hit--set the text into the tree and drop through
						setValue(item,text.getText());
					case SWT.ESC:
						// End editing session
						text.dispose();
						unselect();
						break;
					}
				}
			});

			// Set the text field into the editor
			editor.setEditor(text, item.treeItem);

		}
		else {
			this.sel=null;
		}
		return item;
	}
	protected void setValue(final PrimeTreeItem item, final String value) {
		if(!value.equals(item.treeItem.getText(1))) {
			try {
				final IController c = pyexp.getController();
				if(c != null) {
					item.treeItem.setText(1, value);
					c.setRuntimeAttribute(item.node, item.var.id, value);
				}
				else {
					if(!item.isSymbol) {
						item.treeItem.setText(1, value);
						item.node.setAttribute(item.var.id, value);
					}
					else {
						item.treeItem.setText(1, item.node.getAttributeValueByName(item.var.id));
					}
				}
			} catch(Exception e) {
				Util.dialog(Type.ERROR, "Error setting attribute:", Util.getStackTraceAsString(e));
			}
		}
	}
	
	protected void unselect() {
		this.sel=null;
	}
	public void reset(IModelNode node) {
		this.node=node;
		try {
			tree.removeAll();
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.kids.clear();
	}
	public void updateValues(Dataset ds) {
		for(PrimeTreeItem c : kids){
			c.updateValues(ds);
		}
	}
	public void setHeaderVisible(boolean show) {
		tree.setHeaderVisible(show);
	}
	public void setLinesVisible(boolean show) {
		tree.setLinesVisible(show);
	}
	public void setLayoutData(Object layoutData) {
		tree.setLayoutData(layoutData);
	}
	public boolean hasUID(long uid){
		if(this.node !=null) {
			if(this.node.getUID() == uid) {
				return true;
			}
			if(this.node instanceof ILink || this.node instanceof INet) {
				for(PrimeTreeItem c : kids){
					if(c.hasUID(uid)) return true;
				}
			}
		}
		return false;
	}
}