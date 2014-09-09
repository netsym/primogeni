package monitor.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jprime.variable.ModelNodeVariable;
import jprime.visitors.TLVVisitor.TLVType;
import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.StateExchangeCmd;
import monitor.commands.VarUpdate;
import monitor.commands.VarUpdate.DoubleUpdate;

public class IncompleteAggValue {
	public final Set<Integer> have;
	double min,max,sample_count,sum;
	IncompleteAggValue() {
		have=new HashSet<Integer>();
		min=max=sample_count=sum=-1;
	}
	public StateExchangeCmd processUpdate(PrimeStateExchangeCmd update, int com_id, Set<Integer> target) {
		return processUpdate(update.getUpdates(), update.getTime(), update.getUid(), update.getCmdType(), com_id, target);
	}
	
	public StateExchangeCmd processUpdate(StateExchangeCmd update, Set<Integer> target) {
		return processUpdate(update.getUpdates(), update.getTime(), update.getUid(), update.getCmdType(), update.getCommunity_id(), target);
	}

	public StateExchangeCmd processUpdate(List<VarUpdate> updates, long time, long uid, int cmd_type, int com_id, Set<Integer> target) {
		if(have.size()==0) {
			//first
			for(VarUpdate v : updates) {
				if(v.var_id == ModelNodeVariable.min()) {
					min=((DoubleUpdate)v).value;
				}
				else if(v.var_id == ModelNodeVariable.max()) {
					max=((DoubleUpdate)v).value;
				}
				else if(v.var_id == ModelNodeVariable.sum()) {
					sum=((DoubleUpdate)v).value;
				}
				else if(v.var_id == ModelNodeVariable.sample_count()) {
					sample_count=((DoubleUpdate)v).value;
				}
			}
		}
		else {
			if(have.contains(com_id)) {
				//its incomplete but some data is better than none
				//XXX
				ArrayList<VarUpdate> vupdates = new ArrayList<VarUpdate>(5);
				vupdates.add(new DoubleUpdate(ModelNodeVariable.min(), TLVType.FLOAT, min));
				vupdates.add(new DoubleUpdate(ModelNodeVariable.max(), TLVType.FLOAT, max));
				vupdates.add(new DoubleUpdate(ModelNodeVariable.sum(), TLVType.FLOAT, sum));
				vupdates.add(new DoubleUpdate(ModelNodeVariable.sample_count(), TLVType.FLOAT, sample_count));
				have.clear();
				min=max=sample_count=sum=-1;
				for(VarUpdate v : updates) {
					if(v.var_id == ModelNodeVariable.min()) {
						min=((DoubleUpdate)v).value;
					}
					else if(v.var_id == ModelNodeVariable.max()) {
						max=((DoubleUpdate)v).value;
					}
					else if(v.var_id == ModelNodeVariable.sum()) {
						sum=((DoubleUpdate)v).value;
					}
					else if(v.var_id == ModelNodeVariable.sample_count()) {
						sample_count=((DoubleUpdate)v).value;
					}
				}
				have.add(com_id);
				return new StateExchangeCmd(time, uid, true, false, cmd_type, -1, vupdates);
			}
			else {
				for(VarUpdate v : updates) {
					if(v.var_id == ModelNodeVariable.min()) {
						if(min > ((DoubleUpdate)v).value)
							min=((DoubleUpdate)v).value;
					}
					else if(v.var_id == ModelNodeVariable.max()) {
						if(max < ((DoubleUpdate)v).value)
							max=((DoubleUpdate)v).value;
					}
					else if(v.var_id == ModelNodeVariable.sum()) {
						sum+=((DoubleUpdate)v).value;
					}
					else if(v.var_id == ModelNodeVariable.sample_count()) {
						sample_count+=((DoubleUpdate)v).value;
					}
				}					
			}
		}
		have.add(com_id);
		
		if(have.containsAll(target)) {
			ArrayList<VarUpdate> vupdates = new ArrayList<VarUpdate>(5);
			vupdates.add(new DoubleUpdate(ModelNodeVariable.min(), TLVType.FLOAT, min));
			vupdates.add(new DoubleUpdate(ModelNodeVariable.max(), TLVType.FLOAT, max));
			vupdates.add(new DoubleUpdate(ModelNodeVariable.sum(), TLVType.FLOAT, sum));
			vupdates.add(new DoubleUpdate(ModelNodeVariable.sample_count(), TLVType.FLOAT, sample_count));
			have.clear();
			min=max=sample_count=sum=-1;
			return new StateExchangeCmd(time, uid, true, false, cmd_type, -1, vupdates);
		}
		return null;
	}
}