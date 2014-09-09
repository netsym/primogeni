package monitor.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import monitor.commands.PrimeStateExchangeCmd;
import monitor.commands.VarUpdate;
import monitor.util.Utils;

public class DataStorageThread extends Thread {
	private final ArrayBlockingQueue<PrimeStateExchangeCmd> work;
	private boolean stop;
	private final File file;
	private final BufferedWriter out;
	
	public DataStorageThread(final String filename) {
		super("data_storage_thread");
		this.work = new ArrayBlockingQueue<PrimeStateExchangeCmd>(Utils.STORAGE_QUEUE_SIZE);
		this.stop=false;
		this.file=new File(filename);
		this.out = getFile(file);
	}
	private BufferedWriter getFile(File f) {
		try {
			f.createNewFile();
			return new BufferedWriter(new FileWriter(f), 32768);
		} catch (IOException e) {
		}
		System.err.println("unable to open "+f.getAbsolutePath());
		return null;
	}
	public void stopThread() {
		this.stop=true;
	}
	public void addWork(PrimeStateExchangeCmd cmd) {
		while(true) {
			try {
				work.put(cmd);
				return;
			} catch (InterruptedException e) {
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		ArrayList<PrimeStateExchangeCmd> todo = new ArrayList<PrimeStateExchangeCmd>(1024);
		try {
			out.write("#time, is_agg, uid, var_id, value\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		while(!stop) {
			todo.clear();
			final int s = work.size();
			if(s==0) {
				while(true)  {
					try {
						todo.add(work.take());
						break;
					} catch (InterruptedException e) {
					}
				}
			}
			else {
				work.drainTo(todo, s>1024?1024:s);
			}
			for(int i=0;i<todo.size();i++) {
				try {
					final String pre=todo.get(i).getTime()+","+(todo.get(i).isAggregate()?1:0)+","+todo.get(i).getUid()+",";
					for(VarUpdate v : todo.get(i).getUpdates()) {
						out.write(pre);
						out.write(v.var_id+","+v.asString()+"\n");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			todo.clear();
		}
		try {
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
