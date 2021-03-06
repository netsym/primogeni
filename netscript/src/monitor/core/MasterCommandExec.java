package monitor.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
//import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Set;

import org.apache.derby.impl.store.replication.net.SlaveAddress;

import jprime.util.NetAggPair;
import jprime.util.Portal;
import monitor.commands.AbstractCmd;
import monitor.commands.AreaOfInterestUpdate;
import monitor.commands.BlockingCmdResult;
import monitor.commands.ConnectToSlavesCmd;
import monitor.commands.CreateDynamicModelNode;
import monitor.commands.MonitorCmd;
import monitor.commands.NonBlockingCmdResult;
import monitor.commands.SetupExperimentCmd;
import monitor.commands.SetupExperimentCmd.TLVFile;
import monitor.commands.SetupSlaveCmd;
import monitor.commands.SetupSlavesCmd;
import monitor.commands.StartExperimentCmd;
import monitor.commands.StartGatewayCmd;
import monitor.commands.StateExchangeCmd;
import monitor.commands.UpdateVizExportRate;
import monitor.core.Monitor.AggData;
import monitor.core.Monitor.MetaCmd;
import monitor.util.Utils;
import monitor.util.Utils.CmdRv;

/**
 * @author Nathanael Van Vorst, Mohammad Abu Obadia
 * 
 */
public class MasterCommandExec extends Thread {
	private final Monitor monitor;
	private final ArrayBlockingQueue<AbstractCmd> cmd_queue = new ArrayBlockingQueue<AbstractCmd>(Utils.MAX_OUTSTANDING_CMDS);
	private final HashMap<String,ArrayList<SetupExperimentCmd.TLVFile>> exps = new HashMap<String, ArrayList<SetupExperimentCmd.TLVFile>>();
	private String mpiMachineFile=null;
	private String pvfsConfig=null;
	private String debug_messageMaster=null;
	int master_coutner=1;
	
	public MasterCommandExec(Monitor monitor) {
		this.monitor = monitor;
	}

	public void processCmd(AbstractCmd cmd) {
		while(true) {
			try {
				cmd_queue.put(cmd);
				return;
			} catch (InterruptedException e) {
			}
		}
	}

	public void run() {
		while (true) {
			AbstractCmd cmd;
			debug_messageMaster=null;
//			/int master_coutner=1;
			try {
				debug_messageMaster=null;
				cmd = cmd_queue.take();
				if(Utils.DEBUG)System.out.println("\nGot cmd:"+ cmd + " machine id=" + cmd.getMachineId());
				//Utils.appendMsgToFile("/tmp/pgc_debug_msg","\nMasterCommandExec:run() Got cmd:"+ cmd + " machine id=" + cmd.getMachineId());
				if(cmd.getMachineId()==-1 || cmd.getMachineId() == monitor.getMasterConfig().machineId) {
					//Utils.appendMsgToFile("/tmp/pgc_debug_msg","\nLocalCommand:"+cmd.toString());
					//by default we assume commands with no machine id are targeted to the master
					handleLocalCmd(cmd);
				}
				else {
					//Utils.appendMsgToFile("/tmp/pgc_debug_msg","\nSlaveCommand:"+cmd.toString());
					handleSlaveCommand(cmd);
				}
			} catch (InterruptedException e) {
				Utils.appendMsgToFile("/tmp/pgc_debug_msg","\n!!! Exception Caught in MasterCommandExec!");
				e.printStackTrace();
			}
			//String path=;
			//File file = new File("/tmp/pgc_debug_Messges");
			
	        FileWriter fileWriter = null;
			try {
				//String hostname=InetAddress.getLocalHost().getHostName();
				fileWriter = new FileWriter(new File("/tmp/pgc_debug_msg"),true);
				BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);//Hostname:"+hostname+"
				if (master_coutner<=100)
					 fileWriter.append("\n["+master_coutner++ +"] MasterCommandExec      Debug Messages:"+debug_messageMaster);  //DEBUGG
				else
					fileWriter.append(">");
		        bufferFileWriter.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
	private void handleSlaveCommand(AbstractCmd cmd) {
		if(Utils.DEBUG)System.out.println("Handling slave command. targeting partition/machine "+cmd.getMachineId());
		debug_messageMaster+="\n\nHandling slave command. targeting partition/machine "+cmd.getMachineId()+"cmd="+cmd.toString();	
		
		final NodeConfig nc = monitor.getSlaveConfigFromMachineID(cmd.getMachineId());
		if(nc == null) {
			switch (cmd.getCommandType()) {
			case STATE_EXCHANGE_CMD:{
				//debug_messageMaster=null;
				StateExchangeCmd se = (StateExchangeCmd)cmd;
				monitor.commandFinished(true,cmd.getSerialNumber(),100, "[master]Unable to process StateExchange for uid="+se.getUid()
						+". Couldn't find the slave with machine id "+se.getMachineId());//failure
			}
				break;
				default:
					monitor.commandFinished((cmd instanceof BlockingCmdResult)?true:false,cmd.getSerialNumber(),
									100,"Uknown slave, machine id="+cmd.getMachineId()); //failed
			}
			return;
		}
		switch (cmd.getCommandType()) {
		case SETUP_SLAVE:
		case SETUP_CONTAINER:
		{
			MetaCmd mc= new MetaCmd(cmd.getSerialNumber());
			monitor.sendCommand(cmd.getMachineId(),cmd, mc);
		}
			break;
		case MONITOR_CMD:
		case STATE_EXCHANGE_CMD:
		case HOST_CMD:
			nc.session.write(cmd);
			break;
		case START_GATEWAY:
		case SHUTOWN_CMD:
		case BLOCKING_CMD_RESULT:
		case NON_BLOCKING_CMD_RESULT:
		case CONNNECT_SLAVES:
		case SETUP_EXPERIMENT:
		case START_EXPERIMENT:
		case SETUP_SLAVES:
		case CREATE_DYNAMIC_MODEL_NODE:
		case AREA_OF_INTEREST_UPDATE:
		case VIZ_EXPORT_RATE_UPDATE:
			//should not happen
			monitor.commandFinished((cmd instanceof BlockingCmdResult)?true:false,cmd.getSerialNumber(), 100, "Slaves should not be directly sent these type of commands, type="+cmd.getClass().getSimpleName()); //failed
			break;
		default:
			if(Utils.DEBUG)System.out.println("Encountered an unknown command type! class="+cmd.getClass().getSimpleName());
			debug_messageMaster+="\nEncountered an unknown command type! class="+cmd.getClass().getSimpleName();
		}
	}
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
		private void connectToSlaves(ConnectToSlavesCmd slavecmd) {
			if(Utils.DEBUG)System.out.println("Executing connectToSlaves cmd");
			debug_messageMaster+="\n  Executing connectToSlaves() cmd";
			List<NodeConfig> slaves_list = new ArrayList<NodeConfig>();
			if(Utils.GEN_SSH_KEYS) {
				CmdRv rv = monitor.generateSSHKeys();
				if(rv.status != 0) {
					if(Utils.DEBUG)System.out.println("Failed to generate ssh keys: msg="+rv.msg);
					debug_messageMaster+="\n  !!!Failed to generate ssh keys: msg="+rv.msg;
					monitor.commandFinished(true,slavecmd.getSerialNumber(), rv.status, rv.msg);
					return;
				}
			}
			monitor.setMasterConfig(slavecmd.getMasterConfig());
			for (NodeConfig s : slavecmd.getSlaveConfigs()) {
				if(Utils.DEBUG)System.out.println("Connecting to slave " + s);
				debug_messageMaster+="\n  Connecting to slave =" + s;
				slaves_list.add(s);
				try {
					monitor.connect(s, Utils.SLAVE_PORT);
				} catch (Exception e) {
			        StringWriter sw = new StringWriter();
			        PrintWriter pw = new PrintWriter(sw, true);
			        e.printStackTrace(pw);
			        pw.flush();
			        sw.flush();
					monitor.commandFinished(true,slavecmd.getSerialNumber(), 100, "Unable to connect to slave "+s+":"+Utils.SLAVE_PORT+", st:\n"+sw.toString());
					return;
				}
				if(Utils.DEBUG)System.out.println("Connected to slave: " + s);
				debug_messageMaster+="\n  Connected to slave: " + s;
			}
			mpiMachineFile = monitor.generateMpiMachineFile(slaves_list);
			if(mpiMachineFile == null || mpiMachineFile.length()==0) {
				if(Utils.DEBUG)System.out.println("Failed to generate mpi machine file!");
				debug_messageMaster+="\n  !!!Failed to generate mpi machine file!";
				monitor.commandFinished(true,slavecmd.getSerialNumber(), 100, "Failed to generate mpi machine file!");
				return;
			}
			
			
			pvfsConfig = monitor.generatePVFSConfig();
			if(pvfsConfig == null || pvfsConfig.length()==0) {
				if(Utils.DEBUG)System.out.println("Failed to generate pvfs config file!");
				debug_messageMaster+="\n  !!!!Failed to generate pvfs config file!";
				monitor.commandFinished(true,slavecmd.getSerialNumber(), 100, "Failed to generate pvfs config file!");
				return;
			}
			monitor.commandFinished(true,slavecmd.getSerialNumber());
			if(Utils.DEBUG)System.out.println("Done Executing connectToSlaves cmd");
			debug_messageMaster+="\n  Done Executing connectToSlaves().\n";
		}
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------
	private void handleLocalCmd(AbstractCmd cmd) {
		if(Utils.DEBUG)System.out.println("Handling local command");
		//debug_messageMaster+="\n\nHandling local command, case:"+cmd.getCommandType()+ ", cmd= "+cmd.toString();
		debug_messageMaster+="\nHandling local command, case:"+cmd.getCommandType();
		
		
		switch (cmd.getCommandType()) {

		case BLOCKING_CMD_RESULT:
			if (cmd.getSerialNumber() < 0) {
				// cmd created at the master and sent to slave
				// if the master receives a cmd form the controller
				// that is complicated, it may need to issue sub-commands to
				// slaves ... the serial numbers of those commands are
				// negative....
				// the master must collate all of the results before
				// send a response to the controller that the original
				// complex command is
				// complete
				MetaCmd mc = monitor.getMetaCmd(cmd.getSerialNumber());
				if (mc != null) {
					// we are waiting for this...
					if (0 == mc.finished(cmd.getSerialNumber(),((BlockingCmdResult) cmd).getReturnCode(),((BlockingCmdResult) cmd).getMsg())) {
						// all the sub-commands have finished so we can
						// notify the controller
						monitor.commandFinished(true,mc.serialNumber, mc.returnCodeSum, mc.getMsgs());
					}
					// else there are still outstanding sub-commands
				} else {
					throw new RuntimeException("Should not happen");
				}
			} else {
				// forward to controller
				monitor.commandFinished(true,cmd.getSerialNumber(),((BlockingCmdResult) cmd).getReturnCode(),((BlockingCmdResult) cmd).getMsg());
			}
			break;
			
		case CREATE_DYNAMIC_MODEL_NODE:
		{
			//send command to partition with lowest ID in each machine
			HashMap<Integer, NodeConfig> macs = new HashMap<Integer, NodeConfig>();
			for(NodeConfig nc: monitor.getSlaveConfigs()) {
				if(!macs.containsKey(nc.machineId)) {
					macs.put(nc.machineId, nc);
				}
				else {
					if(macs.get(nc.machineId).partition_id < nc.partition_id)
						macs.put(nc.machineId, nc);
				}
			}
			for(Entry<Integer, NodeConfig> e  : macs.entrySet()) {
				if(Utils.DEBUG)System.out.println("Sending CREATE_DYNAMIC_MODEL_NODE command to part "+e.getValue().partition_id+" in machine "+e.getKey());
				debug_messageMaster+="\nSending CREATE_DYNAMIC_MODEL_NODE command to part "+e.getValue().partition_id+" in machine "+e.getKey();
				e.getValue().session.write(new CreateDynamicModelNode(e.getValue().partition_id, ((CreateDynamicModelNode)cmd).tlv));
			}
		}
			break;
			
		case STATE_EXCHANGE_CMD:
		{
			debug_messageMaster=" -> ";
			monitor.handleStateUpdate((StateExchangeCmd)cmd);
		}
			break;
			
		case NON_BLOCKING_CMD_RESULT:
			// forward to controller
			monitor.commandFinished(false,cmd.getSerialNumber(),
					((NonBlockingCmdResult) cmd).getReturnCode(),((NonBlockingCmdResult) cmd).getMsg());
			break;
		case CONNNECT_SLAVES:
			connectToSlaves((ConnectToSlavesCmd) cmd);
			break;
			
		case SETUP_SLAVES:
		{String temp_expt_name=null;
			if(Utils.DEBUG)System.out.println("Setting up slaves...mpiMachineFile=" + mpiMachineFile +" pvfsConfig=" + pvfsConfig);
			debug_messageMaster+="\nSetting up slaves...mpiMachineFile=" + mpiMachineFile +" pvfsConfig=" + pvfsConfig;
			if(mpiMachineFile==null) {
				monitor.commandFinished(true,cmd.getSerialNumber(), 100,"The mpiMachineFile is null"); //failed
			}
			if(pvfsConfig==null) {
				monitor.commandFinished(true,cmd.getSerialNumber(), 100, "The pvfsConfig is null"); //failed
			}
			else {
				MetaCmd mc= new MetaCmd(cmd.getSerialNumber());
				for(Integer sid : monitor.getSlaveIds()) {
					if(Utils.DEBUG)System.out.println("sending a SetupSlaveCmd for sid=" + sid);
					debug_messageMaster+="\nsending a SetupSlaveCmd for sid=" + sid ;
					//String temp_expt_name=((SetupSlavesCmd)cmd).getExpName();
					monitor.sendCommand(sid, new SetupSlaveCmd(sid, monitor.getSsh_publicKey(), monitor.getSsh_privateKey(), pvfsConfig, ((SetupSlavesCmd)cmd).getExpName()), mc);
					temp_expt_name=((SetupSlavesCmd)cmd).getExpName();
					debug_messageMaster+="\nEXPT NAME: "+temp_expt_name;
				}
			}
			
			
			CmdRv rv1 = null;
			String scmd_scp_tlv = "perl  "+Utils.PRIMOGENI_FOLDER+"/copyTlvFilesToExps.pl "+Utils.BASE_EXP_DIR+"/"+temp_expt_name;
			debug_messageMaster+="\n Copying file from master using SCP, command=: "+scmd_scp_tlv;
			
			rv1 = Utils.executeCommand(scmd_scp_tlv);
			//System.out.println("\tCopy tlv file command:" + scmd_scp_tlv);
			if(rv1.status != 0){
				if(Utils.DEBUG)System.out.println("Failed to copy tlv files");
				debug_messageMaster+="\n\t\t***Failed to copy TLV files";
				break;
			}			
			//perl  copyTlvFilesToExps.pl /primex/exps/AThirdJavaMode
			
			if(Utils.DEBUG)System.out.println("Done Setting up slaves");
			debug_messageMaster+="\nDone Setting up slaves";
		}
		
			break;
			
		case SETUP_EXPERIMENT:
		{String temp_expt_name_2=null; //obaida

			final SetupExperimentCmd scmd = ((SetupExperimentCmd)cmd);
			if(Utils.DEBUG)System.out.println("Setting up exp tlv count=" +scmd.getTlvs().size());
			debug_messageMaster+="\nSetting up exp tlv count=" +scmd.getTlvs().size();
			ArrayList<SetupExperimentCmd.TLVFile> temp = new ArrayList<SetupExperimentCmd.TLVFile>();
			
			temp.addAll(scmd.getTlvs());
			//getting tlvs here
			
			Collections.sort(temp);
			for(TLVFile t : temp) {
				if(Utils.DEBUG)System.out.println("\t[TLV]"+t.part_id+", name="+t.filename);
				debug_messageMaster+="\n\t[TLV]"+t.part_id+", name="+t.filename;
				
			}
			//Transfer files here
			for(TLVFile t : temp) {
				if(Utils.DEBUG)System.out.println("\t[TLV]"+t.part_id+", name="+t.filename);
				debug_messageMaster+="\n\t[TLV]"+t.part_id+", name="+t.filename;
				//get slave machine name and trasfer the file there.
				
				
			}
			exps.put(((SetupExperimentCmd)cmd).getName(), temp);
			String path = Utils.BASE_EXP_DIR+"/"+scmd.getName()+"/"+Utils.RUNTIME_VAR_NAME;
			debug_messageMaster+="\n MasterCommandExec: Writing ="+path;
			temp_expt_name_2=scmd.getName();//obaida
			
			
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(path));
				
				out.write(scmd.getRuntimeSymbols());
				
				out.close();
				monitor.commandFinished(true,cmd.getSerialNumber());// success
			} catch (IOException e) {
				e.printStackTrace();
				monitor.commandFinished(true,cmd.getSerialNumber(), 100,"Error creating '"+path+"' for '"+scmd.getName()+"'");// failure
			}
			final HashMap<Long,AggData> agg2coms = monitor.getAgg2coms();
			for(Entry<NetAggPair, Set<Integer>> e : ((SetupExperimentCmd)cmd).getAggMap().entrySet()) {
				for(long aid : e.getKey().agg_uids) {
					agg2coms.put(aid, new AggData(e.getValue()));
				}
			}
			
			
			//obaida
			CmdRv rv1 = null;
			String scmd_scp_tlv = "perl  "+Utils.PRIMOGENI_FOLDER+"/copyTlvFilesToExps.pl "+Utils.BASE_EXP_DIR+"/"+temp_expt_name_2;
			debug_messageMaster+="\n Copying file from master using SCP, 9Attempt2), command=: "+scmd_scp_tlv;
			
			rv1 = Utils.executeCommand(scmd_scp_tlv);
			//System.out.println("\tCopy tlv file command:" + scmd_scp_tlv);
			if(rv1.status != 0){
				if(Utils.DEBUG)System.out.println("Failed to copy tlv files");
				debug_messageMaster+="\n\t\t***Failed to copy TLV files";
				break;
			}			
			//perl  copyTlvFilesToExps.pl /primex/exps/AThirdJavaMode			
			
			
			
		}
			break;
			
		case START_EXPERIMENT: {
			
			debug_messageMaster+="     inside case";
			
			StartExperimentCmd scmd = (StartExperimentCmd)cmd;
			if(exps.containsKey(scmd.getName())) {
				ArrayList<String> portal_args = new ArrayList<String>();
				ArrayList<SetupExperimentCmd.TLVFile> tlvs = exps.get(scmd.getName());
				boolean error=false;
				for(int i = 0; i< tlvs.size(); i++) {
					SetupExperimentCmd.TLVFile tlv = tlvs.get(i);
					NodeConfig nc = monitor.getSlaveConfigFromPartitionID(tlv.part_id);
					if(nc == null) {
						monitor.commandFinished(true,cmd.getSerialNumber(), 100,"Could find the slave which owns partition "+tlv.part_id+" of experiemnt '"+scmd.getName()+"'");// failure
						error=true;
						break;
					}
					else {
						String pa = "";
						for(Portal p : nc.portals) {
							pa += " -tp "+p.getIP()+" "+p.getLinkedInterfaceUID();
						}
						portal_args.add(pa);
					}
				}
				if(!error) {
					scmd.setTlvs(tlvs);
					scmd.setPortalArgs(portal_args);
					monitor.getBackgroundCmdExecutor().schedule((StartExperimentCmd)cmd);
				}
			}
			else {
				monitor.commandFinished(true,cmd.getSerialNumber(), 100,"Unknown experiemnt '"+scmd.getName()+"'");// failure
			}
	
		}			
			break;
		case MONITOR_CMD: {
			monitor.getBackgroundCmdExecutor().schedule((MonitorCmd)cmd);
		}
		break;
		
		case START_GATEWAY: {
			monitor.getBackgroundCmdExecutor().schedule((StartGatewayCmd)cmd);
		}
		break;

		case AREA_OF_INTEREST_UPDATE: {
			//just broad cast this....
			if(Utils.DEBUG)System.out.println("Broadcast AOI to slaves! aoi="+cmd.toString());
			debug_messageMaster+="\nBroadcast AOI to slaves! aoi="+cmd.toString();
			for(NodeConfig s : this.monitor.getSlaveConfigs()) {
				s.session.write(((AreaOfInterestUpdate)cmd).dup());
			}
			monitor.commandFinished(true,cmd.getSerialNumber());
		}
		break;
		
		case VIZ_EXPORT_RATE_UPDATE: {
			//just broad cast this....
			if(Utils.DEBUG)System.out.println("Broadcast Viz rate udpate to slaves! aoi="+cmd.toString());
			debug_messageMaster+="\nBroadcast Viz rate udpate to slaves! aoi="+cmd.toString();
			for(NodeConfig s : this.monitor.getSlaveConfigs()) {
				s.session.write(((UpdateVizExportRate)cmd).dup());
			}
			monitor.commandFinished(true,cmd.getSerialNumber());
		}
		break;
		
		case SHUTOWN_CMD:
			monitor.shutdown();
			break;
			
		default:
			if(Utils.DEBUG)System.out.println("Encountered an unknown command type! class="+cmd.getClass().getSimpleName());
			//should not happen
			debug_messageMaster+="\nEncountered an unknown command type! class="+cmd.getClass().getSimpleName();
			monitor.commandFinished(true,cmd.getSerialNumber(), 100,"[Master]Unknown command type "+cmd.getClass().getSimpleName()); //failed
			break;
		case SETUP_CONTAINER:
			monitor.commandFinished(true,cmd.getSerialNumber(), 100,"\n***1** The master should not see commands of type "+
					cmd.getClass().getSimpleName()+". [machineid="+cmd.getMachineId()+", masterid="+monitor.getMasterConfig().machineId+"]");
		case SETUP_SLAVE:
		case HOST_CMD:
			//should not happen
			monitor.commandFinished(true,cmd.getSerialNumber(), 100,"\n***2** The master should not see commands of type "+
					cmd.getClass().getSimpleName()+". [machineid="+cmd.getMachineId()+", masterid="+monitor.getMasterConfig().machineId+"]"); //failed
			break;
		}
	}
//---------------------------------------------------------------------------------------------
//---------------------------------------------------------------------------------------------


}
