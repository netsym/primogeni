package monitor.commands;

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

import java.util.ArrayList;

import monitor.commands.codecs.AreaOfInterestUpdateDecoder;
import monitor.commands.codecs.AreaOfInterestUpdateEncoder;
import monitor.commands.codecs.BlockingCmdResultDecoder;
import monitor.commands.codecs.BlockingCmdResultEncoder;
import monitor.commands.codecs.ComPartAdvertDecoder;
import monitor.commands.codecs.ComPartAdvertEncoder;
import monitor.commands.codecs.ConnectToSlavesDecoder;
import monitor.commands.codecs.ConnectToSlavesEncoder;
import monitor.commands.codecs.CreateDynamicModelNodeCmdDecoder;
import monitor.commands.codecs.CreateDynamicModelNodeCmdEncoder;
import monitor.commands.codecs.HostCmdDecoder;
import monitor.commands.codecs.HostCmdEncoder;
import monitor.commands.codecs.MonitorCmdDecoder;
import monitor.commands.codecs.MonitorCmdEncoder;
import monitor.commands.codecs.NonBlockingCmdResultDecoder;
import monitor.commands.codecs.NonBlockingCmdResultEncoder;
import monitor.commands.codecs.PrimeAreaOfInterestUpdateEncoder;
import monitor.commands.codecs.PrimeCreateNodeEncoder;
import monitor.commands.codecs.PrimeStateUpdateDecoder;
import monitor.commands.codecs.PrimeStateUpdateEncoder;
import monitor.commands.codecs.PrimeUpdateVizExportRateEncoder;
import monitor.commands.codecs.SetupContainerDecoder;
import monitor.commands.codecs.SetupContainerEncoder;
import monitor.commands.codecs.SetupExperimentDecoder;
import monitor.commands.codecs.SetupExperimentEncoder;
import monitor.commands.codecs.SetupSlaveDecoder;
import monitor.commands.codecs.SetupSlaveEncoder;
import monitor.commands.codecs.SetupSlavesDecoder;
import monitor.commands.codecs.SetupSlavesEncoder;
import monitor.commands.codecs.ShutdownDecoder;
import monitor.commands.codecs.ShutdownEncoder;
import monitor.commands.codecs.StartExperimentDecoder;
import monitor.commands.codecs.StartExperimentEncoder;
import monitor.commands.codecs.StartGatewayDecoder;
import monitor.commands.codecs.StartGatewayEncoder;
import monitor.commands.codecs.StateExchangeDecoder;
import monitor.commands.codecs.StateExchangeEncoder;
import monitor.commands.codecs.UpdateBytesFromAppDecoder;
import monitor.commands.codecs.UpdateBytesFromAppEncoder;
import monitor.commands.codecs.UpdatePhysicalSystemDecoder;
import monitor.commands.codecs.UpdatePhysicalSystemEncoder;
import monitor.commands.codecs.UpdateVizExportRateDecoder;
import monitor.commands.codecs.UpdateVizExportRateEncoder;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * @author Nathanael Van Vorst
 * 
 */
public class CodecFactory extends DemuxingProtocolCodecFactory {
	public static class CommandType {

		public final Class<? extends AbstractCmd> cmdClass;
		public final Class<? extends MessageDecoder> decoder;
		@SuppressWarnings("rawtypes")
		public final Class<? extends MessageEncoder> encoder;

		@SuppressWarnings("rawtypes")
		public CommandType(Class<? extends AbstractCmd> cmdClass,
				Class<? extends MessageDecoder> decoder,
				Class<? extends MessageEncoder> encoder) {
			super();
			this.cmdClass = cmdClass;
			this.decoder = decoder;
			this.encoder = encoder;
		}
	}

	private static final ArrayList<CommandType> cmdTypes = new ArrayList<CommandType>();
	static {
		CodecFactory.cmdTypes.add(
				new CommandType(
						BlockingCmdResult.class,
						BlockingCmdResultDecoder.class,
						BlockingCmdResultEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						NonBlockingCmdResult.class,
						NonBlockingCmdResultDecoder.class,
						NonBlockingCmdResultEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						ConnectToSlavesCmd.class,
						ConnectToSlavesDecoder.class,
						ConnectToSlavesEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						SetupSlaveCmd.class,
						SetupSlaveDecoder.class,
						SetupSlaveEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						SetupSlavesCmd.class,
						SetupSlavesDecoder.class,
						SetupSlavesEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						SetupContainerCmd.class,
						SetupContainerDecoder.class,
						SetupContainerEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						SetupExperimentCmd.class,
						SetupExperimentDecoder.class,
						SetupExperimentEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						StartExperimentCmd.class,
						StartExperimentDecoder.class,
						StartExperimentEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						StartGatewayCmd.class,
						StartGatewayDecoder.class,
						StartGatewayEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						MonitorCmd.class,
						MonitorCmdDecoder.class,
						MonitorCmdEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						ShutdownCmd.class,
						ShutdownDecoder.class,
						ShutdownEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						HostCmd.class,
						HostCmdDecoder.class,
						HostCmdEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						StateExchangeCmd.class,
						StateExchangeDecoder.class,
						StateExchangeEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						ComPartAdvertCmd.class,
						ComPartAdvertDecoder.class,
						ComPartAdvertEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						AreaOfInterestUpdate.class, 
						AreaOfInterestUpdateDecoder.class,
						AreaOfInterestUpdateEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						UpdateVizExportRate.class, 
						UpdateVizExportRateDecoder.class,
						UpdateVizExportRateEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						CreateDynamicModelNode.class,
						CreateDynamicModelNodeCmdDecoder.class,
						CreateDynamicModelNodeCmdEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						UpdateBytesFromAppCmd.class,
						UpdateBytesFromAppDecoder.class,
						UpdateBytesFromAppEncoder.class));
		CodecFactory.cmdTypes.add(
				new CommandType(
						UpdatePhysicalSystemCmd.class,
						UpdatePhysicalSystemDecoder.class,
						UpdatePhysicalSystemEncoder.class));
	}

	public CodecFactory() {
		this(false);
	}
	
	public CodecFactory(boolean usePrimeCodec) {
		if(usePrimeCodec) {
			super.addMessageDecoder(PrimeStateUpdateDecoder.class);
			super.addMessageEncoder(PrimeStateExchangeCmd.class, PrimeStateUpdateEncoder.class);
			super.addMessageEncoder(CreateDynamicModelNode.class, PrimeCreateNodeEncoder.class);
			super.addMessageEncoder(PrimeAreaOfInterestUpdate.class, PrimeAreaOfInterestUpdateEncoder.class);
			super.addMessageEncoder(PrimeUpdateVizExportRate.class,PrimeUpdateVizExportRateEncoder.class);
		}
		else {
			for (CommandType c : cmdTypes) {
				super.addMessageDecoder(c.decoder);
				super.addMessageEncoder(c.cmdClass, c.encoder);
			}
		}
	}
}

