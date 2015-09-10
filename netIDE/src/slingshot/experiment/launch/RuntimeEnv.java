package slingshot.experiment.launch;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import slingshot.Util;
import slingshot.Util.Type;
import slingshot.environment.EnvType;
import slingshot.environment.EnvironmentFileModel;
import slingshot.environment.configuration.EnvironmentConfiguration;
import slingshot.environment.configuration.LocalConfiguration;
import slingshot.environment.configuration.LocalEmulatedConfiguration; //zzz
import slingshot.environment.configuration.ProtoGENIConfiguration;
import slingshot.environment.configuration.RemoteClusterConfiguration;

public class RuntimeEnv {
	public final EnvType type;
	public final String name;
	public final IFile file;
	public EnvironmentConfiguration env;
	public RuntimeEnv(EnvType type, IFile file) {
		super();
		this.type = type;
		this.file = file;
		if(null != file) {
			String f = file.getName();
			if(f.endsWith(".env")) {
				f=f.substring(0,f.length()-4);
			}
			name="["+type.str+"]"+f;
		}
		else {
			name=type.str;
		}
		env=null;
	}
	private void init() {
		try {
			EnvironmentFileModel model = null;
			switch(type) {
			case PROTO_GENI:
				model = EnvironmentFileModel.fromFile(file);
				env = new ProtoGENIConfiguration(model.name, model);
				env.runtime=null;
				env.numProcs=1;
				env.numMachines=((ProtoGENIConfiguration)env).getSlaves().size();
				break;
			case REMOTE_CLUSTER:
				model = EnvironmentFileModel.fromFile(file);
				env = new RemoteClusterConfiguration(model.name, model);
				env.runtime=null;
				env.numProcs=1;
				env.numMachines=((RemoteClusterConfiguration)env).getSlaves().size();
				break;
			case LOCAL:
				env = new LocalConfiguration(null, null, null);
				break;
			case LOCAL_EMULATED:   ////zzz_LOCAL_EMULATED
				env = new LocalEmulatedConfiguration(null, null, 1.0);
				break;
			default:
				throw new RuntimeException("Unknown enviornment!");
			}
		} catch (CoreException e) {
			Util.dialog(Type.ERROR, "Error Loading runtime environment.", Util.getStackTraceAsString(e));
		} catch (IOException e) {
			Util.dialog(Type.ERROR, "Error Loading runtime environment.", Util.getStackTraceAsString(e));
		}
	}
	public int getPortalCount() {
		if(env == null)
			init();
		if(env == null) return -1;
		return env.portalCount();
	}
	public EnvironmentConfiguration getEnv() {
		if(env == null)init();
		return env;
	}
}