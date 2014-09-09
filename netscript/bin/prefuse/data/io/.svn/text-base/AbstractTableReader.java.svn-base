package prefuse.data.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import prefuse.data.ITable;
import prefuse.data.ITable;
import prefuse.util.io.IOLib;


/**
 * Abstract base class implementation of the TableReader interface. Provides
 * implementations for all but the
 * {@link prefuse.data.io.TableReader#readITable(InputStream)} method. 
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public abstract class AbstractTableReader implements TableReader {

    /**
     * @see prefuse.data.io.TableReader#readITable(java.lang.String)
     */
    public ITable readITable(String location) throws DataIOException
    {
        try {
            InputStream is = IOLib.streamFromString(location);
            if ( is == null )
                throw new DataIOException("Couldn't find " + location
                    + ". Not a valid file, URL, or resource locator.");
            return readITable(is);
        } catch ( IOException e ) {
            throw new DataIOException(e);
        }
    }

    /**
     * @see prefuse.data.io.TableReader#readITable(java.net.URL)
     */
    public ITable readITable(URL url) throws DataIOException {
        try {
            return readITable(url.openStream());
        } catch ( IOException e ) {
            throw new DataIOException(e);
        }
    }

    /**
     * @see prefuse.data.io.TableReader#readITable(java.io.File)
     */
    public ITable readITable(File f) throws DataIOException {
        try {
            return readITable(new FileInputStream(f));
        } catch ( FileNotFoundException e ) {
            throw new DataIOException(e);
        }
    }

} // end of abstract class AbstractTableReader
