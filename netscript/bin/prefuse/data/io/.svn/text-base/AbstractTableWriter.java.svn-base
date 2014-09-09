package prefuse.data.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import prefuse.data.ITable;
import prefuse.data.ITable;

/**
 * Abstract base class implementation of the TableWriter interface. Provides
 * implementations for all but the
 * {@link prefuse.data.io.TableWriter#writeITable(ITable, java.io.OutputStream)}
 * method.
 *  
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractTableWriter implements TableWriter {

    /**
     * @see prefuse.data.io.TableWriter#writeITable(prefuse.data.ITable, java.lang.String)
     */
    public void writeITable(ITable table, String filename) throws DataIOException
    {
        writeITable(table, new File(filename));
    }

    /**
     * @see prefuse.data.io.TableWriter#writeITable(prefuse.data.ITable, java.io.File)
     */
    public void writeITable(ITable table, File f) throws DataIOException {
        try {
            writeITable(table, new FileOutputStream(f));
        } catch ( FileNotFoundException e ) {
            throw new DataIOException(e);
        }
    }

} // end of abstract class AbstractTableReader
