package prefuse.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.event.TableModelEvent;

import prefuse.data.column.Column;
import prefuse.data.column.ColumnFactory;
import prefuse.data.column.ColumnMetadata;
import prefuse.data.event.EventConstants;
import prefuse.data.event.TableListener;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.tuple.AbstractTupleSet;
import prefuse.data.tuple.TableTuple;
import prefuse.data.tuple.TupleManager;
import prefuse.data.util.FilterIteratorFactory;
import prefuse.data.util.Index;
import prefuse.data.util.RowManager;
import prefuse.data.util.Sort;
import prefuse.data.util.TableIterator;
import prefuse.data.util.TreeIndex;
import prefuse.util.TypeLib;
import prefuse.util.collections.CopyOnWriteArrayList;
import prefuse.util.collections.IncompatibleComparatorException;
import prefuse.util.collections.IntIterator;


/**
 * <p>A Table organizes a collection of data into rows and columns, each row 
 * containing a data record, and each column containing data values for a
 * named data field with a specific data type. Table data can be accessed
 * directly using the row number and column name, or rows can be treated
 * in an object-oriented fashion using {@link prefuse.data.
 * uple}
 * instances that represent a single row of data in the table. As such,
 * tables implement the {@link prefuse.data.tuple.TupleSet} interface.</p>
 * 
 * <p>Table rows can be inserted or deleted. In any case, none of the other
 * existing table rows are effected by an insertion or deletion. A deleted
 * row simply becomes invalid--any subsequent attempts to access the row
 * either directly or through a pre-existing Tuple instance will result
 * in an exception. However, if news rows are later added to the table,
 * the row number for previously deleted nodes can be reused. In fact, the
 * lower row number currently unused is assigned to the new row. This results
 * in an efficient reuse of the table rows, but carries an important side
 * effect -- rows do not necessarily maintain the order in which they were
 * added once deletions have occurred on the table. If not deletions
 * occur, the ordering of table rows will reflect the order in which
 * rows were added to the table.</p>
 * 
 * <p>Collections of table rows can be accessed using both iterators over
 * the actual row numbers and iterators over the Tuple instances that
 * encapsulate access to that row. Both types of iteration can also be
 * filtered by providing a {@link prefuse.data.expression.Predicate},
 * allowing tables to be queried for specific values.</p>
 * 
 * <p>Columns (alternativele referred to as data fields) can be added to
 * the Table using {@link #addColumn(String, Class)} and a host of
 * similar methods. This method will automatically determine the right
 * kind of backing column instance to use. Furthermore, Table columns
 * can be specified using a {@link Schema} instance, which describes
 * the column names, data types, and default values. The Table class
 * also maintains its own internal Schema, which be accessed (in a
 * read-only way) using the {@link #getSchema()} method.</p>
 * 
 * <p>Tables also support additional structures. The {@link ColumnMetadata}
 * class returned by the {@link #getMetadata(String)} method supports
 * calculation of different statistics for a column, including minimum
 * and maximum values, and the number of unique data values in the column.
 * {@link prefuse.data.util.Index} instances can be created and retrieved
 * using the {@link #index(String)} method and retrieved without triggering
 * creation using {@link #getIndex(String)} method. An index keeps a
 * sorted collection of all data values in a column, accelerating the creation
 * of filtered iterators by optimizing query calculations and also providing
 * faster computation of many of the {@link ColumnMetadata} methods. If
 * you will be issuing a number of queries (i.e., requesting filtered
 * iterators) dependent on the values of a given column, indexing that column
 * may result in a significant performance increase, though at the cost
 * of storing and maintaining the backing index structure.</p>  
 * 
 * @author <a href="http://jheer.org">jeffrey heer</a>
 */
public class Table extends AbstractTupleSet implements ITable {
    
    /** Listeners for changes to this table */
    protected CopyOnWriteArrayList m_listeners;
    
    /** Locally stored data columns */
    protected ArrayList m_columns;
    /** Column names for locally store data columns */
    protected ArrayList m_names;
    
    /** Mapping between column names and column entries
     * containing column, metadata, and index references */ 
    protected HashMap m_entries;
    
    /** Manager for valid row indices */
    protected RowManager m_rows;
    
    /** manager for tuples, which are object representations for rows */
    protected TupleManager m_tuples;
    
    /** Tracks the number of edits of this table */
    protected int m_modCount = 0;
    
    /** Memoize the index of the last column operated on,
     * used to expedite handling of column updates. */
    protected int m_lastCol = -1;
    
    /** A cached schema instance, loaded lazily */
    protected Schema m_schema;
    
    // ------------------------------------------------------------------------
    // Constructors
    
    /**
     * Create a new, empty Table. Rows can be added to the table using
     * the {@link #addRow()} method.
     */
    public Table() {
        this(0, 0);
    }
    
    /**
     * Create a new Table with a given number of rows, and the starting
     * capacity for a given number of columns.
     * @param nrows the starting number of table rows
     * @param ncols the starting capacity for columns 
     */
    public Table(int nrows, int ncols) {
        this(nrows, ncols, TableTuple.class);
    }
    
        
    /**
     * Create a new Table.
     * @param nrows the starting number of table rows
     * @param ncols the starting capacity for columns 
     * @param tupleType the class of the Tuple instances to use
     */
    protected Table(int nrows, int ncols, Class tupleType) {
        m_listeners = new CopyOnWriteArrayList();
        m_columns = new ArrayList(ncols);
        m_names = new ArrayList(ncols);
        m_rows = new RowManager(this);
        m_entries = new HashMap(ncols+5);        
        m_tuples = new TupleManager(this, null, tupleType);

        if ( nrows > 0 )
            addRows(nrows);
    }
    
    /* (non-Javadoc)
     * @see prefuse.data.ITable#getTableToListenTo()
     */
    public ITable getTableToListenTo() {
    	return this;
    }
    
    // ------------------------------------------------------------------------
    // Table Metadata
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumnCount()
	 */
    public int getColumnCount() {
        return m_columns.size();
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumnType(int)
	 */
    public Class getColumnType(int col) {
        return getColumn(col).getColumnType();
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumnType(java.lang.String)
	 */
    public Class getColumnType(String field) {
        Column c = getColumn(field); 
        return (c==null ? null : c.getColumnType());
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getRowCount()
	 */
    public int getRowCount() {
        return m_rows.getRowCount();
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getMinimumRow()
	 */
    public int getMinimumRow() {
        return m_rows.getMinimumRow();
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getMaximumRow()
	 */
    public int getMaximumRow() {
        return m_rows.getMaximumRow();
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#isCellEditable(int, int)
	 */
    public boolean isCellEditable(int row, int col) {
        if ( !m_rows.isValidRow(row) ) {
            return false;
        } else {
            return getColumn(col).isCellEditable(row);
        }
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getModificationCount()
	 */
    public int getModificationCount() {
        return m_modCount;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setTupleManager(prefuse.data.tuple.TupleManager)
	 */
    public void setTupleManager(TupleManager tm) {
        m_tuples.invalidateAll();
        m_tuples = tm;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getSchema()
	 */
    public Schema getSchema() {
        if ( m_schema == null ) {
            Schema s = new Schema();
            for ( int i=0; i<getColumnCount(); ++i ) {
                s.addColumn(getColumnName(i), getColumnType(i), 
                            getColumn(i).getDefaultValue());
            }
            s.lockSchema();
            m_schema = s;
        }
        return m_schema;
    }
    
    /**
     * Invalidates this table's cached schema. This method should be called
     * whenever columns are added or removed from this table.
     */
    public void invalidateSchema() {
        m_schema = null;
    }
    
    // ------------------------------------------------------------------------
    // Row Operations
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumnRow(int, int)
	 */
    public int getColumnRow(int row, int col) {
        return m_rows.getColumnRow(row, col);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getTableRow(int, int)
	 */
    public int getTableRow(int colrow, int col) {
        return m_rows.getTableRow(colrow, col);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addRow()
	 */
    public int addRow() {
        int r = m_rows.addRow();
        updateRowCount();
        
        fireTableEvent(r, r, TableModelEvent.ALL_COLUMNS,
                       TableModelEvent.INSERT);        
        return r;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addRows(int)
	 */
    public void addRows(int nrows) {
        for ( int i=0; i<nrows; ++i ) {
            addRow();
        }
    }
    
    /**
     * Internal method that updates the row counts for local data columns.
     */
    public void updateRowCount() {
        int maxrow = m_rows.getMaximumRow() + 1;
        
        // update columns
        Iterator cols = getColumns();
        while ( cols.hasNext() ) {
            Column c = (Column)cols.next();
            c.setMaximumRow(maxrow);
        }
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#removeRow(int)
	 */
    public boolean removeRow(int row) {
        if ( m_rows.isValidRow(row) ) {
            // the order of operations here is extremely important
            // otherwise listeners may end up with corrupted state.
            // fire update *BEFORE* clearing values
            // allow listeners (e.g., indices) to perform clean-up
            fireTableEvent(row, row, TableModelEvent.ALL_COLUMNS, 
                           TableModelEvent.DELETE);
            // invalidate the tuple
            m_tuples.invalidate(row);
            // release row with row manager
            // do this before clearing column values, so that any
            // listeners can determine that the row is invalid
            m_rows.releaseRow(row);
            // now clear column values
            for ( Iterator cols = getColumns(); cols.hasNext(); ) {
                Column c = (Column)cols.next();
                c.revertToDefault(row);
            }
            return true;
        }
        return false;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#clear()
	 */
    public void clear() {
        IntIterator rows = rows(true);
        while ( rows.hasNext() ) {
            removeRow(rows.nextInt());
        }
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#isValidRow(int)
	 */
    public boolean isValidRow(int row) {
        return m_rows.isValidRow(row);
    }
    
    // ------------------------------------------------------------------------
    // Column Operations
    
    /**
     * Internal method indicating if the given data field is included as a
     * data column.
     */
    public boolean hasColumn(String name) {
        return getColumnNumber(name) != -1;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumnName(int)
	 */
    public String getColumnName(int col) {
        return (String)m_names.get(col);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumnNumber(java.lang.String)
	 */
    public int getColumnNumber(String field) {
        ColumnEntry e = (ColumnEntry)m_entries.get(field);
        return ( e==null ? -1 : e.colnum );
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumnNumber(prefuse.data.column.Column)
	 */
    public int getColumnNumber(Column col) {
        return m_columns.indexOf(col);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumn(int)
	 */
    public Column getColumn(int col) {
        m_lastCol = col;
        return (Column)m_columns.get(col);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getColumn(java.lang.String)
	 */
    public Column getColumn(String field) {
        ColumnEntry e = (ColumnEntry)m_entries.get(field);
        return ( e != null ? e.column : null );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addColumn(java.lang.String, java.lang.Class)
	 */
    public void addColumn(String name, Class type) {
        addColumn(name, type, null);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addColumn(java.lang.String, java.lang.Class, java.lang.Object)
	 */
    public void addColumn(String name, Class type, Object defaultValue) {
        Column col = ColumnFactory.getColumn(type, 
                        m_rows.getMaximumRow()+1, defaultValue);
        addColumn(name, col);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addColumn(java.lang.String, java.lang.String)
	 */
    public void addColumn(String name, String expr) {
        Expression ex = ExpressionParser.parse(expr);
        Throwable t = ExpressionParser.getError();
        if ( t != null ) {
            throw new RuntimeException(t);
        } else {
            addColumn(name, ex);
        }
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addColumn(java.lang.String, prefuse.data.expression.Expression)
	 */
    public void addColumn(String name, Expression expr) {
        addColumn(name, ColumnFactory.getColumn(this, expr));
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addConstantColumn(java.lang.String, java.lang.Class, java.lang.Object)
	 */
    public void addConstantColumn(String name, Class type, Object dflt) {
        addColumn(name, ColumnFactory.getConstantColumn(type, dflt));
    }
    
    /**
     * Internal method for adding a column.
     * @param name the name of the column
     * @param col the actual Column instance
     */
    public void addColumn(String name, Column col) {
        int idx = getColumnNumber(name);
        if ( idx >= 0 && idx < m_columns.size() ) {
            throw new IllegalArgumentException(
                "Table already has column with name \""+name+"\"");
        }
        
        // add the column
        m_columns.add(col);
        m_names.add(name);
        m_lastCol = m_columns.size()-1;
        ColumnEntry entry = new ColumnEntry(m_lastCol, col, 
                new ColumnMetadata(this, name));
        
        // add entry, dispose of an overridden entry if needed
        ColumnEntry oldEntry = (ColumnEntry)m_entries.put(name, entry);
        if ( oldEntry != null ) oldEntry.dispose();
        
        invalidateSchema();
        
        // listen to what the column has to say
        col.addColumnListener(this);
        
        // fire notification
        fireTableEvent(m_rows.getMinimumRow(), m_rows.getMaximumRow(), 
                m_lastCol, TableModelEvent.INSERT);
    }

    /**
     * Internal method for removing a column.
     * @param idx the column number of the column to remove
     * @return the removed Column instance
     */
    public Column removeColumn(int idx) {
        // make sure index is legal
        if ( idx < 0 || idx >= m_columns.size() ) {
            throw new IllegalArgumentException("Column index is not legal.");
        }
        
        String name = (String)m_names.get(idx);
        ((ColumnEntry)m_entries.get(name)).dispose();
        Column col = (Column)m_columns.remove(idx);
        m_entries.remove(name);
        m_names.remove(idx);
        renumberColumns();
        
        m_lastCol = -1;
        invalidateSchema();
        
        // ignore what the old column has to say
        col.removeColumnListener(this);
        
        // fire notification
        fireTableEvent(m_rows.getMinimumRow(), m_rows.getMaximumRow(), 
                       idx, TableModelEvent.DELETE);
        
        return col;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#removeColumn(java.lang.String)
	 */
    public Column removeColumn(String field) {
        int idx = m_names.indexOf(field);
        if ( idx < 0 ) {
            throw new IllegalArgumentException("No such column.");
        }
        return removeColumn(idx);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#removeColumn(prefuse.data.column.Column)
	 */
    public void removeColumn(Column c) {
        int idx = m_columns.indexOf(c);
        if ( idx < 0 ) {
            throw new IllegalArgumentException("No such column.");
        }
        removeColumn(idx);
    }
    
    /**
     * Internal method that re-numbers columns upon column removal.
     */
    public void renumberColumns() {
        Iterator iter = m_names.iterator();
        for ( int idx=0; iter.hasNext(); ++idx ) {
            String name = (String)iter.next();
            ColumnEntry e = (ColumnEntry)m_entries.get(name);
            e.colnum = idx;
        }
    }
    
    /**
     * Internal method that returns an iterator over columns
     * @return an iterator over columns
     */
    public Iterator getColumns() {
        return m_columns.iterator();
    }

    /**
     * Internal method that returns an iterator over column names
     * @return an iterator over column name
     */
    public Iterator getColumnNames() {
        return m_names.iterator();
    }
    
    // ------------------------------------------------------------------------
    // Column Metadata
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getMetadata(java.lang.String)
	 */
    public ColumnMetadata getMetadata(String field) {
        ColumnEntry e = (ColumnEntry)m_entries.get(field);
        if ( e == null ) {
            throw new IllegalArgumentException("Unknown column name: "+field);
        }
        return e.metadata;
    }
    
    // ------------------------------------------------------------------------
    // Index Methods
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#index(java.lang.String)
	 */
    public Index index(String field) {
        ColumnEntry e = (ColumnEntry)m_entries.get(field);
        if ( e == null ) {
            throw new IllegalArgumentException("Unknown column name: "+field);
        } else if ( e.index != null ) {
            return e.index; // already indexed
        }
        
        Column col = e.column;
        try {
            e.index = new TreeIndex(this, m_rows, col, null);
        } catch ( IncompatibleComparatorException ice ) { /* can't happen */ }
        
        return e.index;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getIndex(java.lang.String)
	 */
    public Index getIndex(String field) {
        ColumnEntry e = (ColumnEntry)m_entries.get(field);
        if ( e == null ) {
            throw new IllegalArgumentException("Unknown column name: "+field);
        }
        return e.index;
    }
    
    /**
     * Internal method for index creation and retrieval.
     * @param field the data field name of the column
     * @param expType the expected data type of the index
     * @param create indicates whether or not a new index should be created
     * if none currently exists for the given data field
     * @return the Index for the given data field
     */
    public Index getIndex(String field, Class expType, boolean create) {
        if ( !expType.equals(getColumnType(field)) ) {
            // TODO: need more nuanced type checking here?
            throw new IllegalArgumentException("Column type does not match.");
        }
        if ( getIndex(field)==null && create) {
            index(field);
        }
        return getIndex(field);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#removeIndex(java.lang.String)
	 */
    public boolean removeIndex(String field) {
        ColumnEntry e = (ColumnEntry)m_entries.get(field);
        if ( e == null ) {
            throw new IllegalArgumentException("Unknown column name: "+field);
        }
        if ( e.index == null ) {
            return false;
        } else {
            e.index.dispose();
            e.index = null;
            return true;
        }
    }
    
    // ------------------------------------------------------------------------
    // Tuple Methods
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getTuple(int)
	 */
    public Tuple getTuple(int row) {
        return m_tuples.getTuple(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addTuple(prefuse.data.Tuple)
	 */
    public Tuple addTuple(Tuple t) {
        if ( t.getTable() == this ) {
            return null;
        } else {
            Schema s = t.getSchema();
            if ( getSchema().isAssignableFrom(s) ) {
                int r = addRow();
                for ( int i=0; i<s.getColumnCount(); ++i ) {
                    String field = s.getColumnName(i);
                    this.set(r, field, t.get(i));
                }
                return getTuple(r);
            } else {
                return null;
            }
        }
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setTuple(prefuse.data.Tuple)
	 */
    public Tuple setTuple(Tuple t) {
        clear();
        return addTuple(t);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#removeTuple(prefuse.data.Tuple)
	 */
    public boolean removeTuple(Tuple t) {
        if ( containsTuple(t) ) {
            removeRow(t.getRow());
            return true;
        } else {
            return false;
        }
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#containsTuple(prefuse.data.Tuple)
	 */
    public boolean containsTuple(Tuple t) {
        return (t.getTable()==this && isValidRow(t.getRow())); 
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getTupleCount()
	 */
    public int getTupleCount() {
        return getRowCount();
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#isAddColumnSupported()
	 */
    public boolean isAddColumnSupported() {
        return true;
    }
    
    // ------------------------------------------------------------------------
    // Data Access Methods
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGet(java.lang.String, java.lang.Class)
	 */
    public boolean canGet(String field, Class type) {
        Column c = getColumn(field);
        return ( c==null ? false : c.canGet(type) );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSet(java.lang.String, java.lang.Class)
	 */
    public boolean canSet(String field, Class type) {
        Column c = getColumn(field);
        return ( c==null ? false : c.canSet(type) );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#get(int, java.lang.String)
	 */
    public Object get(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).get(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#set(int, java.lang.String, java.lang.Object)
	 */
    public void set(int row, String field, Object val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).set(val, row);
        
        // we don't fire a notification here, as we catch the
        // notification from the column itself and then dispatch
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#get(int, int)
	 */
    public Object get(int row, int col) {
        row = getColumnRow(row, col);
        return getColumn(col).get(row);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#set(int, int, java.lang.Object)
	 */
    public void set(int row, int col, Object val) {
        row = getColumnRow(row, col);
        getColumn(col).set(val, row);
        
        // we don't fire a notification here, as we catch the
        // notification from the column itself and then dispatch
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getDefault(java.lang.String)
	 */
    public Object getDefault(String field) {
        int col = getColumnNumber(field);
        return getColumn(col).getDefaultValue();
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#revertToDefault(int, java.lang.String)
	 */
    public void revertToDefault(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).revertToDefault(row);
    }

    // ------------------------------------------------------------------------
    // Convenience Data Access Methods
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGetInt(java.lang.String)
	 */
    public final boolean canGetInt(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canGetInt() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSetInt(java.lang.String)
	 */
    public final boolean canSetInt(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canSetInt() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getInt(int, java.lang.String)
	 */
    public final int getInt(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).getInt(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setInt(int, java.lang.String, int)
	 */
    public final void setInt(int row, String field, int val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).setInt(val, row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getInt(int, int)
	 */
    public final int getInt(int row, int col) {
        row = getColumnRow(row, col);
        return getColumn(col).getInt(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setInt(int, int, int)
	 */
    public final void setInt(int row, int col, int val) {
        row = getColumnRow(row, col);
        getColumn(col).setInt(val, row);
    }
    
    // --------------------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGetLong(java.lang.String)
	 */
    public final boolean canGetLong(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canGetLong() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSetLong(java.lang.String)
	 */
    public final boolean canSetLong(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canSetLong() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getLong(int, java.lang.String)
	 */
    public final long getLong(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).getLong(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setLong(int, java.lang.String, long)
	 */
    public final void setLong(int row, String field, long val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).setLong(val, row);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getLong(int, int)
	 */
    public final long getLong(int row, int col)  {
        row = getColumnRow(row, col);
        return getColumn(col).getLong(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setLong(int, int, long)
	 */
    public final void setLong(int row, int col, long val) {
        row = getColumnRow(row, col);
        getColumn(col).setLong(val, row);
    }
    
    // --------------------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGetFloat(java.lang.String)
	 */
    public final boolean canGetFloat(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canGetFloat() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSetFloat(java.lang.String)
	 */
    public final boolean canSetFloat(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canSetFloat() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getFloat(int, java.lang.String)
	 */
    public final float getFloat(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).getFloat(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setFloat(int, java.lang.String, float)
	 */
    public final void setFloat(int row, String field, float val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).setFloat(val, row);   
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getFloat(int, int)
	 */
    public final float getFloat(int row, int col) {
        row = getColumnRow(row, col);
        return getColumn(col).getFloat(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setFloat(int, int, float)
	 */
    public final void setFloat(int row, int col, float val) {
        row = getColumnRow(row, col);
        getColumn(col).setFloat(val, row);   
    }
    
    // --------------------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGetDouble(java.lang.String)
	 */
    public final boolean canGetDouble(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canGetDouble() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSetDouble(java.lang.String)
	 */
    public final boolean canSetDouble(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canSetDouble() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getDouble(int, java.lang.String)
	 */
    public final double getDouble(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).getDouble(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setDouble(int, java.lang.String, double)
	 */
    public final void setDouble(int row, String field, double val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).setDouble(val, row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getDouble(int, int)
	 */
    public final double getDouble(int row, int col) {
        row = getColumnRow(row, col);
        return getColumn(col).getDouble(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setDouble(int, int, double)
	 */
    public final void setDouble(int row, int col, double val) {
        row = getColumnRow(row, col);
        getColumn(col).setDouble(val, row);
    }

    // --------------------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGetBoolean(java.lang.String)
	 */
    public final boolean canGetBoolean(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canGetBoolean() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSetBoolean(java.lang.String)
	 */
    public final boolean canSetBoolean(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canSetBoolean() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getBoolean(int, java.lang.String)
	 */
    public final boolean getBoolean(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).getBoolean(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setBoolean(int, java.lang.String, boolean)
	 */
    public final void setBoolean(int row, String field, boolean val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).setBoolean(val, row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getBoolean(int, int)
	 */
    public final boolean getBoolean(int row, int col) {
        row = getColumnRow(row, col);
        return getColumn(col).getBoolean(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setBoolean(int, int, boolean)
	 */
    public final void setBoolean(int row, int col, boolean val) {
        row = getColumnRow(row, col);
        getColumn(col).setBoolean(val, row);
    }
    
    // --------------------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGetString(java.lang.String)
	 */
    public final boolean canGetString(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canGetString() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSetString(java.lang.String)
	 */
    public final boolean canSetString(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canSetString() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getString(int, java.lang.String)
	 */
    public final String getString(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).getString(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setString(int, java.lang.String, java.lang.String)
	 */
    public final void setString(int row, String field, String val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).setString(val, row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getString(int, int)
	 */
    public final String getString(int row, int col) {
        row = getColumnRow(row, col);
        return getColumn(col).getString(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setString(int, int, java.lang.String)
	 */
    public final void setString(int row, int col, String val) {
        row = getColumnRow(row, col);
        getColumn(col).setString(val, row);
    }
    
    // --------------------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canGetDate(java.lang.String)
	 */
    public final boolean canGetDate(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canGetDate() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#canSetDate(java.lang.String)
	 */
    public final boolean canSetDate(String field) {
        Column col = getColumn(field);
        return ( col==null ? false : col.canSetDate() );
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getDate(int, java.lang.String)
	 */
    public final Date getDate(int row, String field) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        return getColumn(col).getDate(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setDate(int, java.lang.String, java.util.Date)
	 */
    public final void setDate(int row, String field, Date val) {
        int col = getColumnNumber(field);
        row = getColumnRow(row, col);
        getColumn(col).setDate(val, row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#getDate(int, int)
	 */
    public final Date getDate(int row, int col) {
        row = getColumnRow(row, col);
        return getColumn(col).getDate(row);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#setDate(int, int, java.util.Date)
	 */
    public final void setDate(int row, int col, Date val) {
        row = getColumnRow(row, col);
        getColumn(col).setDate(val, row);
    }

    // ------------------------------------------------------------------------
    // Query Operations
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#select(prefuse.data.expression.Predicate, prefuse.data.util.Sort)
	 */
    public ITable select(Predicate filter, Sort sort) {
        ITable t = getSchema().instantiate();
        Iterator tuples = tuples(filter, sort);
        while ( tuples.hasNext() ) {
            t.addTuple((Tuple)tuples.next());
        }
        return t;
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#remove(prefuse.data.expression.Predicate)
	 */
    public void remove(Predicate filter) {
        for ( IntIterator ii = rows(filter); ii.hasNext(); )
            removeRow(ii.nextInt());
    }
    
    // ------------------------------------------------------------------------
    // Iterators
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#iterator()
	 */
    public TableIterator iterator() {
        return iterator(rows());
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#iterator(prefuse.util.collections.IntIterator)
	 */
    public TableIterator iterator(IntIterator rows) {
        return new TableIterator(this, rows);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#tuples()
	 */
    public Iterator tuples() {
        return m_tuples.iterator(rows());
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#tuplesReversed()
	 */
    public Iterator tuplesReversed() {
        return m_tuples.iterator(rows(true));
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#tuples(prefuse.util.collections.IntIterator)
	 */
    public Iterator tuples(IntIterator rows) {
        return m_tuples.iterator(rows);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rows()
	 */
    public IntIterator rows() {
        return m_rows.rows();
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rows(prefuse.data.expression.Predicate)
	 */
    public IntIterator rows(Predicate filter) {
        return FilterIteratorFactory.rows(this, filter);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rows(boolean)
	 */
    public IntIterator rows(boolean reverse) {
        return m_rows.rows(reverse);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rowsSortedBy(java.lang.String, boolean)
	 */
    public IntIterator rowsSortedBy(String field, boolean ascend) {
        Class type = getColumnType(field);
        Index index = getIndex(field, type, true);
        int t = ascend ? Index.TYPE_ASCENDING : Index.TYPE_DESCENDING;
        return index.allRows(t);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rangeSortedBy(java.lang.String, int, int, int)
	 */
    public IntIterator rangeSortedBy(String field, int lo, int hi, int indexType) {
        Index index = getIndex(field, int.class, true);
        return index.rows(lo, hi, indexType);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rangeSortedBy(java.lang.String, long, long, int)
	 */
    public IntIterator rangeSortedBy(String field, long lo, long hi, int indexType) {
        Index index = getIndex(field, long.class, true);
        return index.rows(lo, hi, indexType);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rangeSortedBy(java.lang.String, float, float, int)
	 */
    public IntIterator rangeSortedBy(String field, float lo, float hi, int indexType) {
        Index index = getIndex(field, float.class, true);
        return index.rows(lo, hi, indexType);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rangeSortedBy(java.lang.String, double, double, int)
	 */
    public IntIterator rangeSortedBy(String field, double lo, double hi, int indexType) {
        Index index = getIndex(field, double.class, true);
        return index.rows(lo, hi, indexType);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#rangeSortedBy(java.lang.String, java.lang.Object, java.lang.Object, int)
	 */
    public IntIterator rangeSortedBy(String field, Object lo, Object hi, int indexType) {
        Class type = TypeLib.getSharedType(lo, hi);
        // TODO: check this for correctness
        if ( type == null )
            throw new IllegalArgumentException("Incompatible arguments");
        Index index = getIndex(field, type, true);
        return index.rows(lo, hi, indexType);
    }
    
    // ------------------------------------------------------------------------
    // Listener Methods
    
    // -- ColumnListeners -----------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#columnChanged(prefuse.data.column.Column, int, boolean)
	 */
    public void columnChanged(Column src, int idx, boolean prev) {
        handleColumnChanged(src, idx, idx);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#columnChanged(prefuse.data.column.Column, int, double)
	 */
    public void columnChanged(Column src, int idx, double prev) {
        handleColumnChanged(src, idx, idx);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#columnChanged(prefuse.data.column.Column, int, float)
	 */
    public void columnChanged(Column src, int idx, float prev) {
        handleColumnChanged(src, idx, idx);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#columnChanged(prefuse.data.column.Column, int, int)
	 */
    public void columnChanged(Column src, int idx, int prev) {
        handleColumnChanged(src, idx, idx);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#columnChanged(prefuse.data.column.Column, int, long)
	 */
    public void columnChanged(Column src, int idx, long prev) {
        handleColumnChanged(src, idx, idx);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#columnChanged(prefuse.data.column.Column, int, java.lang.Object)
	 */
    public void columnChanged(Column src, int idx, Object prev) {
        handleColumnChanged(src, idx, idx);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#columnChanged(prefuse.data.column.Column, int, int, int)
	 */
    public void columnChanged(Column src, int type, int start, int end) {
        handleColumnChanged(src, start, end);
    }
    
    /**
     * Handle a column change event.
     * @param c the modified column
     * @param start the starting row of the modified range
     * @param end the ending row (inclusive) of the modified range
     */
    public void handleColumnChanged(Column c, int start, int end) {
        for ( ; !isValidRow(start) && start <= end; ++start );
        if ( start > end ) return; // bail if no valid rows
        
        // determine the index of the updated column
        int idx;
        if ( m_lastCol != -1 && c == getColumn(m_lastCol) ) {
            // constant time
            idx = m_lastCol;
        } else {
            // linear time
            idx = getColumnNumber(c);
        }
        
        // if we have a valid index, fire a notification
        if ( idx >= 0 ) {
            fireTableEvent(start, end, idx, TableModelEvent.UPDATE);
        }
    }
    
    // -- TableListeners ------------------------------------------------------
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#addTableListener(prefuse.data.event.TableListener)
	 */
    public void addTableListener(TableListener listnr) {
        if ( !m_listeners.contains(listnr) )
            m_listeners.add(listnr);
    }

    /* (non-Javadoc)
	 * @see prefuse.data.ITable#removeTableListener(prefuse.data.event.TableListener)
	 */
    public void removeTableListener(TableListener listnr) {
        m_listeners.remove(listnr);
    }
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#removeAllTableListeners()
	 */
    public void removeAllTableListeners() {
    	m_listeners.clear();
    }
    
    /**
     * Fire a table event to notify listeners.
     * @param row0 the starting row of the modified range
     * @param row1 the ending row (inclusive) of the modified range
     * @param col the number of the column modified, or
     * {@link prefuse.data.event.EventConstants#ALL_COLUMNS} for operations
     * effecting all columns.
     * @param type the table modification type, one of
     * {@link prefuse.data.event.EventConstants#INSERT},
     * {@link prefuse.data.event.EventConstants#DELETE}, or
     * {@link prefuse.data.event.EventConstants#UPDATE}.
     */
    public void fireTableEvent(int row0, int row1, int col, int type) {
        // increment the modification count
        ++m_modCount;
        
        if ( type != EventConstants.UPDATE && 
             col == EventConstants.ALL_COLUMNS )
        {
            // fire event to all tuple set listeners
            fireTupleEvent(this, row0, row1, type);
        }
        
        if ( !m_listeners.isEmpty() ) {
            // fire event to all table listeners
            Object[] lstnrs = m_listeners.getArray();
            for ( int i=0; i<lstnrs.length; ++i ) {
                ((TableListener)lstnrs[i]).tableChanged(
                        this, row0, row1, col, type);
            }
        }
    }
    
    // ------------------------------------------------------------------------
    // String Methods
    
    /* (non-Javadoc)
	 * @see prefuse.data.ITable#toString()
	 */
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("Table[");
        sbuf.append("rows=").append(getRowCount());
        sbuf.append(", cols=").append(getColumnCount());
        sbuf.append(", maxrow=").append(m_rows.getMaximumRow());
        sbuf.append("]");
        return sbuf.toString();
    }
    
    // ------------------------------------------------------------------------
    // ColumnEntry helper
    
    /**
     * Helper class that encapsulates a map entry for a column, including the
     * column itself and its metadata and index.
     * 
     * @author <a href="http://jheer.org">jeffrey heer</a>
     */
    protected static class ColumnEntry {

        /** The column number. */
        public int            colnum;
        /** The Column instance. */
        public Column         column;
        /** The column metadata instance. */
        public ColumnMetadata metadata;
        /** The column Index instance. */
        public Index          index;
        
        /**
         * Create a new ColumnEntry.
         * @param col the column number
         * @param column the Column instance
         * @param metadata the ColumnMetadata instance
         */
        public ColumnEntry(int col, Column column, ColumnMetadata metadata) {
            this.colnum = col;
            this.column = column;
            this.metadata = metadata;
            this.index = null;
        }
        
        /**
         * Dispose of this column entry, disposing of any allocated
         * metadata or index instances.
         */
        public void dispose() {
            if ( metadata != null )
                metadata.dispose();
            if ( index != null )
                index.dispose();
        }

    } // end of inner class ColumnEntry
    
} // end of class Table
