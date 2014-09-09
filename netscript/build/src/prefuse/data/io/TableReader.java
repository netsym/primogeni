package prefuse.data.io;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import prefuse.data.ITable;
import prefuse.data.ITable;

/**
 * Interface for classes that read in ITable data from a particular file format.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface TableReader {

    /**
     * Read in a table from the file at the given location. Though
     * not required by this interface, the String is typically resolved
     * using the {@link prefuse.util.io.IOLib#streamFromString(String)} method,
     * allowing URLs, classpath references, and files on the file system
     * to be accessed.
     * @param location the location to read the table from
     * @return the loaded ITable
     * @throws DataIOException
     * @see prefuse.util.io.IOLib#streamFromString(String)
     */
    public ITable readITable(String location) throws DataIOException;
    
    /**
     * Read in a table from the given URL.
     * @param url the url to read the graph from
     * @return the loaded ITable
     * @throws DataIOException
     */
    public ITable readITable(URL url) throws DataIOException;
    
    /**
     * Read in a table from the given File.
     * @param f the file to read the table from
     * @return the loaded ITable
     * @throws DataIOException
     */
    public ITable readITable(File f) throws DataIOException;
    
    /**
     * Read in a table from the given InputStream.
     * @param is the InputStream to read the table from
     * @return the loaded ITable
     * @throws DataIOException
     */
    public ITable readITable(InputStream is) throws DataIOException;
    
} // end of interface TableReader
