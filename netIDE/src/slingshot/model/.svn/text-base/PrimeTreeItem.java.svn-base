package slingshot.model;

import java.util.ArrayList;

import jprime.IModelNode;
import jprime.gen.ModelNodeVariable.VarDesc;
import jprime.variable.Dataset;
import jprime.variable.Dataset.SimpleDatum;
import jprime.variable.ModelNodeVariable;

import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 * When a model node is selected we can display it's attributes a hierarchy.
 * Each attribute is wrapped in a PrimeTreeItem.
 *
 * @author Nathanael Van Vorst
 * 
 */
public class PrimeTreeItem {
	public final TreeItem treeItem;
	public final VarDesc var;
	public final IModelNode node;
	public ArrayList<PrimeTreeItem> kids = new ArrayList<PrimeTreeItem>();
	public final boolean isSymbol;
	public PrimeTreeItem(PrimeTree parent, int style, IModelNode node, VarDesc var, boolean isSymbol) {
		this.treeItem = new TreeItem(parent.tree, style);
		this.var=var;
		this.node=node;
		parent.kids.add(this);
		this.treeItem.setData(this);
		this.isSymbol=isSymbol;
	}

	public PrimeTreeItem(PrimeTreeItem parentItem, int style, IModelNode node, VarDesc var, boolean isSymbol) {
		this.treeItem = new TreeItem(parentItem.treeItem, style);
		parentItem.kids.add(this);
		this.var=var;
		this.node=node;
		this.treeItem.setData(this);
		this.isSymbol=isSymbol;
	}
	public void updateValues(Dataset ds) {
		if(this.var != null) {
			if(this.var.id == ModelNodeVariable.uid()) {
				try{
					this.treeItem.setText(new String[] {"uid", ""+this.node.getUID()});
				} catch(Exception e) {
					this.treeItem.setText(new String[] {"uid", "-"});
				}
			}
			else {
				if(var.is_stat) {
					SimpleDatum v = this.node.getRuntimeValueByName(this.var.id,ds);
					if(v == null)
						this.treeItem.setText(new String[] {this.var.name, this.var.default_value});
					else
						this.treeItem.setText(new String[] {this.var.name, v.value});
				}
				else {
					ModelNodeVariable v = this.node.getAttributeByName(this.var.id);
					if(v == null)
						this.treeItem.setText(new String[] {this.var.name, this.var.default_value});
					else
						this.treeItem.setText(new String[] {this.var.name, v.toString()});
				}
			}
		}
		for(PrimeTreeItem c : kids){
			c.updateValues(ds);
		}
	}

	public void setText(String[] strings) {
		treeItem.setText(strings);
	}

	public void setText(int index, String string) {
		treeItem.setText(index, string);
	}

	public void setText(String string) {
		treeItem.setText(string);
	}
	public boolean hasUID(long uid){
		if(this.node !=null) {
			if(this.node.getUID() == uid) {
				return true;
			}
		}
		for(PrimeTreeItem c : kids){
			if(c.hasUID(uid)) return true;
		}
		return false;
	}
}