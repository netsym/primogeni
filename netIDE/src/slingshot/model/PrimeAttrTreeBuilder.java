package slingshot.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jprime.IModelNode;
import jprime.ModelNode;
import jprime.State;
import jprime.BaseInterface.IBaseInterfaceAlias;
import jprime.Host.IHost;
import jprime.gen.ModelNodeVariable;
import jprime.gen.ModelNodeVariable.ClsDesc;
import jprime.util.ComputeNode;
import jprime.variable.Dataset;
import jprime.variable.ModelNodeVariable.VizAttr;
import slingshot.experiment.ProjectLoader.ProjectState;
import slingshot.experiment.PyExperiment;
import slingshot.visualization.PortalNetwork;

/**
 * @author Nathanael Van Vorst
 *
 */
public class PrimeAttrTreeBuilder implements Runnable{
	private final PyExperiment exp;
	private final PrimeTree attrTree;
	
	public PrimeAttrTreeBuilder(ProjectState e){
		this.exp = e==null?null:e.exp;
		this.attrTree = e==null?null:e.view.getAttrTree();
	}
	
	@Override
	public void run() {
		makeTree();
	}

	public void makeTree() {
		if(exp == null  || attrTree == null || exp.selected==null)
			return;
		if(attrTree.node == null || attrTree.node != exp.selected) {
			final Dataset dataset=exp.getDataset();
			attrTree.reset(exp.selected);
			final Map<String, VizAttr> attrs = exp.selected.getVizAttributes(dataset);

			/*if(exp.selected.getParent()!=null) {
				final PrimeTreeItem parentItem = new PrimeTreeItem(attrTree, 0, exp.selected.getParent(), null);
				parentItem.setText(new String[] {"parent", exp.selected.getParent().getName()});
			}*/
			
			if(! (exp.selected instanceof PortalNetwork)) {
				final PrimeTreeItem nameItem = new PrimeTreeItem(attrTree, 0, exp.selected, null,false);
				nameItem.setText(new String[] {"name", exp.selected.getName()});
			}
			
			if(exp.selected instanceof IHost) {
				final IHost h = (IHost)exp.selected;
				if(h.hasEmulationProtocol()) {
					final PrimeTreeItem cn = new PrimeTreeItem(attrTree, 0, exp.selected, null,false);
					String cnstr=null;
					if(exp.getState().gte(State.COMPILED)) {
						HashMap<Integer, ArrayList<IHost>> emap = exp.getEmuNodeMap();
						List<ComputeNode> cnodes = exp.getComputeNodes();
						if(emap != null && cnodes != null) {
							for(Entry<Integer, ArrayList<IHost>> e : emap.entrySet()) {
								for(IHost hh : e.getValue()) {
									if(hh.getUID() == h.getUID()) {
										for(ComputeNode cc : cnodes) {
											if(cc.getPartitionId() == e.getKey()) {
												cnstr = cc.getControl_ip();
												break;
											}
										}
										if(cnstr != null)break;
									}
									if(cnstr != null)break;
								}
								if(cnstr != null)break;
							}
						}
					}
					if(cnstr == null) cnstr="--";
					cn.setText(new String[] {"compute node", cnstr});
				}
			}
			if(exp.selected instanceof PortalNetwork) {
				for(Entry<String, String> nn : exp.selected.getAttributes().entrySet()) {
					final PrimeTreeItem n = new PrimeTreeItem(attrTree, 0, exp.selected, null,false);
					n.setText(new String[] {nn.getKey(), nn.getValue()});
				};
			}
			else {
				PrimeTreeItem props1 = new PrimeTreeItem(attrTree, 0, exp.selected, null,false);
				props1.setText(new String[] {"properties", ""});
				
				PrimeTreeItem state1 = null;
	
				for(final Map.Entry<String, VizAttr> attr : attrs.entrySet()){
					if(!attr.getValue().desc.is_stat) {
						final PrimeTreeItem treeItem = new PrimeTreeItem(props1, 0, exp.selected, attr.getValue().desc,attr.getValue().isSym);
						treeItem.setText(new String[] {attr.getKey(), attr.getValue().value});
					}
					else {
						if(state1==null) {
							state1 = new PrimeTreeItem(attrTree, 0, exp.selected, null,false);
							state1.setText(new String[] {"runtime state", ""});
						}
						final PrimeTreeItem treeItem = new PrimeTreeItem(state1, 0, exp.selected, attr.getValue().desc,attr.getValue().isSym);
						treeItem.setText(new String[] {attr.getKey(), attr.getValue().value});
					}
				}
				try {
					final List<ModelNode> children = exp.selected.getAllChildren();
					for(IModelNode child : children){
						ClsDesc cd = ModelNodeVariable.getClsDesc((ModelNode)child);
						if(cd.vars.size()==0) continue;
						PrimeTreeItem childItem = new PrimeTreeItem(attrTree, 0, child, null,false);
						String type = child.getClass().getName().substring(child.deference().getClass().getName().lastIndexOf('.')+1).replaceAll("Replica", "");
						if(child instanceof IBaseInterfaceAlias) {
							childItem.setText(new String[] {child.deference().getParent().getName()+"."+child.deference().getName(), type });
							child=child.deference();
						}
						else {
							childItem.setText(new String[] {child.getName(), type });
						}
						PrimeTreeItem props2 = new PrimeTreeItem(childItem, 0, exp.selected, null,false);
						props2.setText(new String[] {"properties", ""});
						PrimeTreeItem state2=null;
						Map<String, VizAttr> childAttrs = child.getVizAttributes(dataset);
						for(Map.Entry<String, VizAttr> attr : childAttrs.entrySet()){
							if(!attr.getValue().desc.is_stat) {
								PrimeTreeItem childAttrItem = new PrimeTreeItem(props2, 0, child, attr.getValue().desc,attr.getValue().isSym);
								childAttrItem.setText(new String[] {attr.getKey(), attr.getValue().value});
							}
							else {
								if(state2 == null) {
									state2 = new PrimeTreeItem(childItem, 0, exp.selected, null,false);
									state2.setText(new String[] {"runtime state", ""});
								}
								PrimeTreeItem childAttrItem = new PrimeTreeItem(state2, 0, child, attr.getValue().desc,attr.getValue().isSym);
								childAttrItem.setText(new String[] {attr.getKey(), attr.getValue().value});
							}
						}
						props2.treeItem.setExpanded(true);
						if(state2 != null)state2.treeItem.setExpanded(true);
					}
					props1.treeItem.setExpanded(true);
					if(state1 != null)state1.treeItem.setExpanded(true);
				}catch(UnsupportedOperationException e) {}
			}
		}
		else {
			attrTree.updateValues(exp.getDataset());
		}
	}


}