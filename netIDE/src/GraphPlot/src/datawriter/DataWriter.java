package datawriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataWriter {
	
	public static DataStreamWriter createDataWriter(String filePath, boolean overwrite) throws FileAlreadyExists{
		if (null == filePath)
			throw new NullPointerException("Cannot create a DataStreamWriter for a null file");
		
		return createDataWriter(new File(filePath), overwrite);
	}
	
	private static DataStreamWriter createDataWriter(File file, boolean overwrite) throws FileAlreadyExists{
		if (null == file)
			throw new NullPointerException("Cannot create a DataStreamWriter for a null file");
			
		if (overwrite && file.exists())
			file.delete();
		
		if (!file.exists()) {		
			try {
				FileOutputStream outs = new FileOutputStream(file);
				DataStreamWriter writer = new DataStreamWriter(outs);
				
				return writer;
			} catch (IOException e) { }
		}
		
		throw new FileAlreadyExists("File Exists.");
	}
	
}

@SuppressWarnings("serial")
class FileAlreadyExists extends Exception{
	FileAlreadyExists() {}
	FileAlreadyExists(String message){ super(message); }
}
