package slingshot.wizards.topology;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * This class define java file operations
 * @author Hao Jiang
 */
public class FileOperation 
{
	//copy folder
	public static void copyFolder(String oldPath, String newPath)
	{
		try 
		{
			(new File(newPath)).mkdirs(); 
			File a=new File(oldPath);
			String[] file=a.list();
			File temp=null;
			for (int i = 0; i < file.length; i++) 
			{
				if(oldPath.endsWith(File.separator))
				{
					temp=new File(oldPath+file[i]);
				}
				else
				{
					temp=new File(oldPath+File.separator+file[i]);
				}

				if(temp.isFile())
				{
					FileInputStream input = new FileInputStream(temp);
					File outputfile = new File(newPath + "/" +
                                            (temp.getName()).toString());
					FileOutputStream output = new FileOutputStream(outputfile);

					byte[] b = new byte[1024 * 5];
					int len;
					while ( (len = input.read(b)) != -1)
					{
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
					outputfile.setExecutable(true);
				}
				if(temp.isDirectory())	
				{
					copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
				}
			}
		}
	    catch (Exception e) 
	    {
	    	System.out.println("copy folder fail");
	    	e.printStackTrace();
	    }
	 }
	
	//copy file
	public static void copyFile(String source, String target) 
	{
		try
		{
			File sourceFile = new File(source);
			File targetFile = new File(target);
			
			if (sourceFile.exists())
			{
		        FileInputStream input = new FileInputStream(sourceFile);
		        BufferedInputStream inBuff= new BufferedInputStream(input);
		
		        FileOutputStream output = new FileOutputStream(targetFile);
		        BufferedOutputStream outBuff = new BufferedOutputStream(output);
		       
		        byte[] b = new byte[1024 * 5];
		        int len;
		        while ((len =inBuff.read(b)) != -1) 
		        {
		            outBuff.write(b, 0, len);
		        }
		      
		        outBuff.flush();
		        
		        inBuff.close();
		        outBuff.close();
		        output.close();
		        input.close();
			}
		}
	    catch (Exception e) 
	    {
	    	System.out.println("copy folder fail");
	    	e.printStackTrace();
	    }
	} 
	
	//check if a folder or file exist
	public static boolean isExists(String path)
	{
		File file = new File(path);
		if (file.exists())
			return true;
		else 
			return false;
	}
	
}
