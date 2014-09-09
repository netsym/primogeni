package prefuse.data.io;

import java.io.File;
import java.io.OutputStream;

import prefuse.data.ITable;

/**
 * Interface for classes that write ITable data to a particular file format.
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public interface TableWriter {

    /**
     * Write a table to the file with the given filename.
     * @param table the ITable to write
     * @param filename the file to write the table to
     * @throws DataWriteException
     */
    public void writeITable(ITable table, String filename) throws DataIOException;
    
    /**
     * Write a table to the given File.
     * @param table the ITable to write
     * @param f the file to write the table to
     * @throws DataWriteException
     */
    public void writeITable(ITable table, File f) throws DataIOException;
    
    /**
     * Write a table from the given OutputStream.
     * @param table the ITable to write
     * @param os the OutputStream to write the table to
     * @throws DataWriteException
     */
    public void writeITable(ITable table, OutputStream os) throws DataIOException;
    
} // end of interface TableWriter
