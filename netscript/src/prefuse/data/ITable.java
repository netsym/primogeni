package prefuse.data;

import java.util.Date;
import java.util.Iterator;

import prefuse.data.column.Column;
import prefuse.data.column.ColumnMetadata;
import prefuse.data.event.ColumnListener;
import prefuse.data.event.TableListener;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Predicate;
import prefuse.data.tuple.TupleManager;
import prefuse.data.tuple.TupleSet;
import prefuse.data.util.TableIterator;
import prefuse.data.util.Index;
import prefuse.data.util.Sort;
import prefuse.util.collections.IntIterator;

public interface ITable extends ColumnListener, TupleSet {

	/**
	 * Add a column with the given name and data type to this table.
	 * @param name the data field name for the column
	 * @param type the data type, as a Java Class, for the column
	 * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.Class)
	 */
	public abstract void addColumn(String name, Class type);

	/**
	 * Add a column with the given name and data type to this table.
	 * @param name the data field name for the column
	 * @param type the data type, as a Java Class, for the column
	 * @param defaultValue the default value for column data values
	 * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.Class, java.lang.Object)
	 */
	public abstract void addColumn(String name, Class type, Object defaultValue);
	
	/* (non-Javadoc)
	 * @see prefuse.data.Table#addColumn(java.lang.String, prefuse.data.column.Column)
	 */
	public abstract void addColumn(String name, Column col);
	
	/**
	 * Add a derived column to this table, using an Expression instance to
	 * dynamically calculate the column data values.
	 * @param name the data field name for the column
	 * @param expr the Expression that will determine the column values
	 * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String, prefuse.data.expression.Expression)
	 */
	public abstract void addColumn(String name, Expression expr);

	/**
	 * Add a derived column to this table, using an Expression instance to
	 * dynamically calculate the column data values.
	 * @param name the data field name for the column
	 * @param expr a String expression in the prefuse expression language, to
	 * be parsed into an {@link prefuse.data.expression.Expression} instance.
	 * The string is parsed by the
	 * {@link prefuse.data.expression.parser.ExpressionParser}. If an error
	 * occurs during parsing, an exception will be thrown. 
	 * @see prefuse.data.tuple.TupleSet#addColumn(java.lang.String, java.lang.String)
	 */
	public abstract void addColumn(String name, String expr);

	/**
	 * Add a constant column to this table, which returns one constant value
	 * for all column rows.
	 * @param name the data field name for the column
	 * @param type the data type, as a Java Class, for the column
	 * @param dflt the default value for column data values
	 */
	public abstract void addConstantColumn(String name, Class type, Object dflt);

	/**
	 * Add a row to this table. All data columns will be notified and will
	 * take on the appropriate default values for the added row.
	 * @return the row number of the newly added row
	 */
	public abstract int addRow();

	/**
	 * Add a given number of rows to this table. All data columns will be
	 * notified and will take on the appropriate default values for the
	 * added rows.
	 * @param nrows the number of rows to add.
	 */
	public abstract void addRows(int nrows);

	/**
	 * Add a table listener to this table.
	 * @param listnr the listener to add
	 */
	public abstract void addTableListener(TableListener listnr);

	/**
	 * Add a Tuple to this table. If the Tuple is already a member of this
	 * table, nothing is done and null is returned. If the Tuple is not
	 * a member of this ITable but has a compatible data schema, as
	 * determined by {@link Schema#isAssignableFrom(Schema)}, a new row
	 * is created, the Tuple's values are copied, and the new Tuple that
	 * is a member of this ITable is returned. If the data schemas are not
	 * compatible, nothing is done and null is returned.
	 * @param t the Tuple to "add" to this table
	 * @return the actual Tuple instance added to this table, or null if
	 * no new Tuple has been added
	 * @see prefuse.data.tuple.TupleSet#addTuple(prefuse.data.Tuple)
	 */
	public abstract Tuple addTuple(Tuple t);
	
	/**
	 * Check if the <code>get</code> method for the given data field returns
	 * values that are compatible with a given target type.
	 * @param field the data field to check
	 * @param type a Class instance to check for compatibility with the
	 * data field values.
	 * @return true if the data field is compatible with provided type,
	 * false otherwise. If the value is true, objects returned by
	 * the {@link #get(int, String)} can be cast to the given type.
	 * @see #get(int, String)
	 */
	public abstract boolean canGet(String field, Class type);

	/**
	 * Check if the given data field can return primitive <code>boolean</code>
	 * values.
	 * @param field the data field to check
	 * @return true if the data field can return primitive <code>boolean</code>
	 * values, false otherwise. If true, the {@link #getBoolean(int, String)}
	 * method can be used safely.
	 */
	public abstract boolean canGetBoolean(String field);

	
	/**
	 * Check if the given data field can return primitive <code>Date</code>
	 * values.
	 * @param field the data field to check
	 * @return true if the data field can return primitive <code>Date</code>
	 * values, false otherwise. If true, the {@link #getDate(int, String)}
	 * method can be used safely.
	 */
	public abstract boolean canGetDate(String field);

	/**
	 * Check if the given data field can return primitive <code>double</code>
	 * values.
	 * @param field the data field to check
	 * @return true if the data field can return primitive <code>double</code>
	 * values, false otherwise. If true, the {@link #getDouble(int, String)}
	 * method can be used safely.
	 */
	public abstract boolean canGetDouble(String field);

	/**
	 * Check if the given data field can return primitive <code>float</code>
	 * values.
	 * @param field the data field to check
	 * @return true if the data field can return primitive <code>float</code>
	 * values, false otherwise. If true, the {@link #getFloat(int, String)}
	 * method can be used safely.
	 */
	public abstract boolean canGetFloat(String field);

	/**
	 * Check if the given data field can return primitive <code>int</code>
	 * values.
	 * @param field the data field to check
	 * @return true if the data field can return primitive <code>int</code>
	 * values, false otherwise. If true, the {@link #getInt(int, String)}
	 * method can be used safely.
	 */
	public abstract boolean canGetInt(String field);

	/**
	 * Check if the given data field can return primitive <code>long</code>
	 * values.
	 * @param field the data field to check
	 * @return true if the data field can return primitive <code>long</code>
	 * values, false otherwise. If true, the {@link #getLong(int, String)}
	 * method can be used safely.
	 */
	public abstract boolean canGetLong(String field);

	/**
	 * Check if the given data field can return primitive <code>String</code>
	 * values.
	 * @param field the data field to check
	 * @return true if the data field can return primitive <code>String</code>
	 * values, false otherwise. If true, the {@link #getString(int, String)}
	 * method can be used safely.
	 */
	public abstract boolean canGetString(String field);

	/**
	 * Check if the <code>set</code> method for the given data field can
	 * accept values of a given target type.
	 * @param field the data field to check
	 * @param type a Class instance to check for compatibility with the
	 * data field values.
	 * @return true if the data field is compatible with provided type,
	 * false otherwise. If the value is true, objects of the given type
	 * can be used as parameters of the {@link #set(int, String, Object)}
	 * method.
	 * @see #set(int, String, Object)
	 */
	public abstract boolean canSet(String field, Class type);

	/**
	 * Check if the <code>setBoolean</code> method can safely be used for the
	 * given data field.
	 * @param field the data field to check
	 * @return true if the {@link #setBoolean(int, String, boolean)} method can
	 * safely be used for the given field, false otherwise.
	 */
	public abstract boolean canSetBoolean(String field);

	/**
	 * Check if the <code>setDate</code> method can safely be used for the
	 * given data field.
	 * @param field the data field to check
	 * @return true if the {@link #setDate(int, String, Date)} method can
	 * safely be used for the given field, false otherwise.
	 */
	public abstract boolean canSetDate(String field);

	/**
	 * Check if the <code>setDouble</code> method can safely be used for the
	 * given data field.
	 * @param field the data field to check
	 * @return true if the {@link #setDouble(int, String, double)} method can
	 * safely be used for the given field, false otherwise.
	 */
	public abstract boolean canSetDouble(String field);

	/**
	 * Check if the <code>setFloat</code> method can safely be used for the
	 * given data field.
	 * @param field the data field to check
	 * @return true if the {@link #setFloat(int, String, float)} method can
	 * safely be used for the given field, false otherwise.
	 */
	public abstract boolean canSetFloat(String field);

	/**
	 * Check if the <code>setInt</code> method can safely be used for the
	 * given data field.
	 * @param field the data field to check
	 * @return true if the {@link #setInt(int, String, int)} method can safely
	 * be used for the given field, false otherwise.
	 */
	public abstract boolean canSetInt(String field);

	/**
	 * Check if the <code>setLong</code> method can safely be used for the
	 * given data field.
	 * @param field the data field to check
	 * @return true if the {@link #setLong(int, String, long)} method can
	 * safely be used for the given field, false otherwise.
	 */
	public abstract boolean canSetLong(String field);

	/**
	 * Check if the <code>setString</code> method can safely be used for the
	 * given data field.
	 * @param field the data field to check
	 * @return true if the {@link #setString(int, String, String)} method can
	 * safely be used for the given field, false otherwise.
	 */
	public abstract boolean canSetString(String field);

	/**
	 * Clear this table, removing all rows.
	 * @see prefuse.data.tuple.TupleSet#clear()
	 */
	public abstract void clear();

	/**
	 * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column, int, boolean)
	 */
	public abstract void columnChanged(Column src, int idx, boolean prev);

	/**
	 * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column, int, double)
	 */
	public abstract void columnChanged(Column src, int idx, double prev);

	/**
	 * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column, int, float)
	 */
	public abstract void columnChanged(Column src, int idx, float prev);

    /**
	 * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column, int, int)
	 */
	public abstract void columnChanged(Column src, int idx, int prev);

    /**
	 * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column, int, int, int)
	 */
	public abstract void columnChanged(Column src, int type, int start, int end);

    /**
	 * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column, int, long)
	 */
	public abstract void columnChanged(Column src, int idx, long prev);
   
	/**
	 * @see prefuse.data.event.ColumnListener#columnChanged(prefuse.data.column.Column, int, java.lang.Object)
	 */
	public abstract void columnChanged(Column src, int idx, Object prev);

	/**
	 * Indicates if this table contains the given Tuple instance.
	 * @param t the Tuple to check for containment
	 * @return true if the Tuple represents a row of this table, false if
	 * it does not
	 * @see prefuse.data.tuple.TupleSet#containsTuple(prefuse.data.Tuple)
	 */
	public abstract boolean containsTuple(Tuple t);

	/* (non-Javadoc)
	 * @see prefuse.data.Table#fireTableEvent(int, int, int, int)
	 */
	public abstract void fireTableEvent(int row0, int row1, int col, int type);

	/* (non-Javadoc)
	 * @see prefuse.data.tuple.AbstractTupleSet#fireTupleEvent(prefuse.data.ITable, int, int, int)
	 */
	public abstract void fireTupleEvent(ITable t, int start, int end, int type);

	/* (non-Javadoc)
	 * @see prefuse.data.tuple.AbstractTupleSet#fireTupleEvent(prefuse.data.Tuple, int)
	 */
	public abstract void fireTupleEvent(Tuple t, int type);

	/* (non-Javadoc)
	 * @see prefuse.data.tuple.AbstractTupleSet#fireTupleEvent(prefuse.data.Tuple[], prefuse.data.Tuple[])
	 */
	public abstract void fireTupleEvent(Tuple[] added, Tuple[] removed);

	/**
	 * Get the data value at the given row and column numbers as an Object.
	 * @param row the row number
	 * @param col the column number
	 * @return the data value as an Object. The concrete type of this
	 * Object is dependent on the underlying data column used.
	 * @see #canGet(String, Class)
	 * @see #getColumnType(int)
	 */
	public abstract Object get(int row, int col);

	/**
	 * Get the data value at the given row and field as an Object.
	 * @param row the table row to get
	 * @param field the data field to retrieve
	 * @return the data value as an Object. The concrete type of this
	 * Object is dependent on the underlying data column used.
	 * @see #canGet(String, Class)
	 * @see #getColumnType(String)
	 */
	public abstract Object get(int row, String field);

	/**
	 * Get the data value at the given row and field as a
	 * <code>boolean</code>.
	 * @param row the table row to retrieve
	 * @param col the column number of the data field to get
	 * @see #canGetBoolean(String)
	 */
	public abstract boolean getBoolean(int row, int col);

	/**
	 * Get the data value at the given row and field as a
	 * <code>boolean</code>.
	 * @param row the table row to retrieve
	 * @param field the data field to retrieve
	 * @see #canGetBoolean(String)
	 */
	public abstract boolean getBoolean(int row, String field);

	/**
	 * Get the column at the given column number.
	 * @param col the column number
	 * @return the Column instance
	 */
	public abstract Column getColumn(int col);

	/**
	 * Get the column with the given data field name
	 * @param field the data field name of the column
	 * @return the Column instance
	 */
	public abstract Column getColumn(String field);

	/**
	 * Get the number of columns / data fields in this table.
	 * @return the number of columns 
	 */
	public abstract int getColumnCount();

	/**
	 * Get the data field name of the column at the given column number.
	 * @param col the column number
	 * @return the data field name of the column
	 */
	public abstract String getColumnName(int col);

	/**
     * Internal method that returns an iterator over column names
     * @return an iterator over column name
     */
    public abstract Iterator getColumnNames();

	/**
	 * Get the column number for the given Column instance.
	 * @param col the Column instance to lookup
	 * @return the column number of the column, or -1 if the name is not found
	 */
	public abstract int getColumnNumber(Column col);

	/**
	 * Get the column number for a given data field name.
	 * @param field the name of the column to lookup
	 * @return the column number of the column, or -1 if the name is not found
	 */
	public abstract int getColumnNumber(String field);

	/**
	 * Get the row value for accessing an underlying Column instance,
	 * corresponding to the given table cell. For basic tables this just
	 * returns the input row value. However, for tables that inherit
	 * data columns from a parent table and present a filtered view on
	 * this data, a mapping between the row numbers of the table and
	 * the row numbers of the backing data column is needed. In those cases,
	 * this method returns the result of that mapping. The method
	 * {@link #getTableRow(int, int)} accesses this map in the reverse
	 * direction.
	 * @param row the table row to lookup
	 * @param col the table column to lookup
	 * @return the column row number for accessing the desired table cell
	 */
	public abstract int getColumnRow(int row, int col);

	/**
     * Internal method that returns an iterator over columns
     * @return an iterator over columns
     */
    public abstract Iterator getColumns();

	/**
	 * Get the data type of the column at the given column index.
	 * @param col the column index
	 * @return the data type (as a Java Class) of the column
	 */
	public abstract Class getColumnType(int col);

	/**
	 * Get the data type of the column with the given data field name.
	 * @param field the column / data field name
	 * @return the data type (as a Java Class) of the column
	 */
	public abstract Class getColumnType(String field);

	/**
	 * Get the data value at the given row and field as a
	 * <code>Date</code>.
	 * @param row the table row to retrieve
	 * @param col the column number of the data field to retrieve
	 * @see #canGetDate(String)
	 */
	public abstract Date getDate(int row, int col);

	/**
	 * Get the data value at the given row and field as a
	 * <code>Date</code>.
	 * @param row the table row to retrieve
	 * @param field the data field to retrieve
	 * @see #canGetDate(String)
	 */
	public abstract Date getDate(int row, String field);

	/**
	 * Get the default value for the given data field.
	 * @param field the data field
	 * @return the default value, as an Object, used to populate rows
	 * of the data field.
	 */
	public abstract Object getDefault(String field);

	/**
	 * Get the data value at the given row and field as a
	 * <code>double</code>.
	 * @param row the table row to retrieve
	 * @param col the column number of the data field to get
	 * @see #canGetDouble(String)
	 */
	public abstract double getDouble(int row, int col);

	/**
	 * Get the data value at the given row and field as a
	 * <code>double</code>.
	 * @param row the table row to retrieve
	 * @param field the data field to retrieve
	 * @see #canGetDouble(String)
	 */
	public abstract double getDouble(int row, String field);

	/**
	 * Get the data value at the given row and field as a
	 * <code>float</code>.
	 * @param row the table row to retrieve
	 * @param col the column number of the data field to get
	 * @see #canGetFloat(String)
	 */
	public abstract float getFloat(int row, int col);

	/**
	 * Get the data value at the given row and field as a
	 * <code>float</code>.
	 * @param row the table row to retrieve
	 * @param field the data field to retrieve
	 * @see #canGetFloat(String)
	 */
	public abstract float getFloat(int row, String field);

	/**
	 * Retrieve, without creating, an index for the given data field.
	 * @param field the data field name of the column
	 * @return the stored index for the column, or null if no index has
	 * been created
	 */
	public abstract Index getIndex(String field);

	/* (non-Javadoc)
	 * @see prefuse.data.Table#getIndex(java.lang.String, java.lang.Class, boolean)
	 */
	public abstract Index getIndex(String field, Class expType, boolean create);

	/**
	 * Get the data value at the given row and field as an
	 * <code>int</code>.
	 * @param row the table row to retrieve
	 * @param col the column number of the data field to retrieve
	 * @see #canGetInt(String)
	 */
	public abstract int getInt(int row, int col);

	/**
	 * Get the data value at the given row and field as an
	 * <code>int</code>.
	 * @param row the table row to retrieve
	 * @param field the data field to retrieve
	 * @see #canGetInt(String)
	 */
	public abstract int getInt(int row, String field);

	/**
	 * Get the data value at the given row and field as an
	 * <code>long</code>.
	 * @param row the table row to retrieve
	 * @param col the column number of the data field to retrieve
	 * @see #canGetLong(String)
	 */
	public abstract long getLong(int row, int col);

	/**
	 * Get the data value at the given row and field as a
	 * <code>long</code>.
	 * @param row the table row to retrieve
	 * @param field the data field to retrieve
	 * @see #canGetLong(String)
	 */
	public abstract long getLong(int row, String field);

	/**
	 * Get the maximum row index currently in use by this ITable.
	 * @return the maximum row index
	 */
	public abstract int getMaximumRow();

	/**
	 * Return a metadata instance providing summary information about a column.
	 * @param field the data field name of the column
	 * @return the columns' associated ColumnMetadata instance
	 */
	public abstract ColumnMetadata getMetadata(String field);

	/**
	 * Get the minimum row index currently in use by this ITable.
	 * @return the minimum row index
	 */
	public abstract int getMinimumRow();

	/**
	 * Get the number of times this ITable has been modified. Adding rows,
	 * deleting rows, and updating table cell values all contribute to
	 * this count.
	 * @return the number of modifications to this table
	 */
	public abstract int getModificationCount();

	/**
	 * Get the number of rows in the table.
	 * @return the number of rows
	 */
	public abstract int getRowCount();

	/**
	 * Returns this ITable's schema. The returned schema will be
	 * locked, which means that any attempts to edit the returned schema
	 * by adding additional columns will result in a runtime exception.
	 * 
	 * If this ITable subsequently has columns added or removed, this will not
	 * be reflected in the returned schema. Instead, this method will need to
	 * be called again to get a current schema. Accordingly, it is not
	 * recommended that Schema instances returned by this method be stored
	 * or reused across scopes unless that exact schema snapshot is
	 * desired.
	 * 
	 * @return a copy of this ITable's schema
	 */
	public abstract Schema getSchema();

	/**
	 * Get the data value at the given row and field as a
	 * <code>String</code>.
	 * @param row the table row to retrieve
	 * @param col the column number of the data field to retrieve
	 * @see #canGetString(String)
	 */
	public abstract String getString(int row, int col);

	/**
	 * Get the data value at the given row and field as a
	 * <code>String</code>.
	 * @param row the table row to retrieve
	 * @param field the data field to retrieve
	 * @see #canGetString(String)
	 */
	public abstract String getString(int row, String field);

	/**
	 * Get the row number for this table given a row number for a backing
	 * data column and the column number for the data column. For basic
	 * tables this just returns the column row value. However, for tables that
	 * inherit data columns from a parent table and present a filtered view on
	 * this data, a mapping between the row numbers of the table and
	 * the row numbers of the backing data column is needed. In those cases,
	 * this method returns the result of this mapping, in the direction of
	 * the backing column rows to the table rows of the cascaded table. The
	 * method {@link #getColumnRow(int, int)} accesses this map in the reverse
	 * direction.
	 * @param colrow the row of the backing data column
	 * @param col the table column to lookup.
	 * @return the table row number for accessing the desired table cell
	 */
	public abstract int getTableRow(int colrow, int col);

	/* (non-Javadoc)
	 * @see prefuse.data.Table#addColumn(java.lang.String, prefuse.data.column.Column)
	 */
	public abstract ITable getTableToListenTo();

	/**
	 * Get the Tuple instance providing object-oriented access to the given
	 * table row.
	 * @param row the table row
	 * @return the Tuple for the given table row
	 */
	public abstract Tuple getTuple(int row);

	/**
	 * Get the number of tuples in this table. This is the same as the
	 * value returned by {@link #getRowCount()}.
	 * @return the number of tuples, which is the same as the number of rows
	 * @see prefuse.data.tuple.TupleSet#getTupleCount()
	 */
	public abstract int getTupleCount();

	/* (non-Javadoc)
	 * @see prefuse.data.Table#handleColumnChanged(prefuse.data.column.Column, int, int)
	 */
	public abstract void handleColumnChanged(Column c, int start, int end);

	/* (non-Javadoc)
	 * @see prefuse.data.Table#hasColumn(java.lang.String)
	 */
	public abstract boolean hasColumn(String name);

	/**
	 * Create (if necessary) and return an index over the given data field.
	 * The first call to this method with a given field name will cause the
	 * index to be created and stored. Subsequent calls will simply return
	 * the stored index. To attempt to retrieve an index without triggering
	 * creation of a new index, use the {@link #getIndex(String)} method.
	 * @param field the data field name of the column to index
	 * @return the index over the specified data column
	 */
	public abstract Index index(String field);

	/**
     * Invalidates this table's cached schema. This method should be called
     * whenever columns are added or removed from this table.
     */
    public abstract void invalidateSchema();

	/**
	 * Returns true, as this table supports the addition of new data fields.
	 * @see prefuse.data.tuple.TupleSet#isAddColumnSupported()
	 */
	public abstract boolean isAddColumnSupported();

	/**
	 * Indicates if the value of the given table cell can be changed. 
	 * @param row the row number
	 * @param col the column number
	 * @return true if the value can be edited/changed, false otherwise
	 */
	public abstract boolean isCellEditable(int row, int col);

	/**
	 * Indicates if the given row number corresponds to a valid table row.
	 * @param row the row number to check for validity
	 * @return true if the row is valid, false if it is not
	 */
	public abstract boolean isValidRow(int row);

	/**
	 * Return a TableIterator over the rows of this table.
	 * @return a TableIterator over this table
	 */
	public abstract TableIterator iterator();

	/**
	 * Return a TableIterator over the given rows of this table.
	 * @param rows an iterator over the table rows to visit
	 * @return a TableIterator over this table
	 */
	public abstract TableIterator iterator(IntIterator rows);

	/**
	 * Return an iterator over a range of rwos in this table, determined
	 * by a bounded range for a given data field. A new index over the
	 * data field will be created if it doesn't already exist.
	 * @param field the data field for determining the bounded range
	 * @param lo the minimum range value
	 * @param hi the maximum range value
	 * @param indexType indicate the sort order and inclusivity/exclusivity
	 * of the range bounds, using the constants of the
	 * {@link prefuse.data.util.Index} class.
	 * @return an iterator over a range of table rows, determined by a 
	 * sorted bounded range of a data field
	 */
	public abstract IntIterator rangeSortedBy(String field, double lo,
			double hi, int indexType);

	/**
	 * Return an iterator over a range of rwos in this table, determined
	 * by a bounded range for a given data field. A new index over the
	 * data field will be created if it doesn't already exist.
	 * @param field the data field for determining the bounded range
	 * @param lo the minimum range value
	 * @param hi the maximum range value
	 * @param indexType indicate the sort order and inclusivity/exclusivity
	 * of the range bounds, using the constants of the
	 * {@link prefuse.data.util.Index} class.
	 * @return an iterator over a range of table rows, determined by a 
	 * sorted bounded range of a data field
	 */
	public abstract IntIterator rangeSortedBy(String field, float lo, float hi,
			int indexType);

	/**
	 * Return an iterator over a range of rwos in this table, determined
	 * by a bounded range for a given data field. A new index over the
	 * data field will be created if it doesn't already exist.
	 * @param field the data field for determining the bounded range
	 * @param lo the minimum range value
	 * @param hi the maximum range value
	 * @param indexType indicate the sort order and inclusivity/exclusivity
	 * of the range bounds, using the constants of the
	 * {@link prefuse.data.util.Index} class.
	 * @return an iterator over a range of table rows, determined by a 
	 * sorted bounded range of a data field
	 */
	public abstract IntIterator rangeSortedBy(String field, int lo, int hi,
			int indexType);

	/**
	 * Return an iterator over a range of rwos in this table, determined
	 * by a bounded range for a given data field. A new index over the
	 * data field will be created if it doesn't already exist. 
	 * @param field the data field for determining the bounded range
	 * @param lo the minimum range value
	 * @param hi the maximum range value
	 * @param indexType indicate the sort order and inclusivity/exclusivity
	 * of the range bounds, using the constants of the
	 * {@link prefuse.data.util.Index} class.
	 * @return an iterator over a range of table rows, determined by a 
	 * sorted bounded range of a data field
	 */
	public abstract IntIterator rangeSortedBy(String field, long lo, long hi,
			int indexType);

	/**
	 * Return an iterator over a range of rwos in this table, determined
	 * by a bounded range for a given data field. A new index over the
	 * data field will be created if it doesn't already exist.
	 * @param field the data field for determining the bounded range
	 * @param lo the minimum range value
	 * @param hi the maximum range value
	 * @param indexType indicate the sort order and inclusivity/exclusivity
	 * of the range bounds, using the constants of the
	 * {@link prefuse.data.util.Index} class.
	 * @return an iterator over a range of table rows, determined by a 
	 * sorted bounded range of a data field
	 */
	public abstract IntIterator rangeSortedBy(String field, Object lo,
			Object hi, int indexType);

	/**
	 * Removes all table rows that meet the input predicate filter.
	 * @param filter a predicate specifying which rows to remove from
	 * the table.
	 */
	public abstract void remove(Predicate filter);

	/**
	 * Removes all table listeners from this table.
	 */
	public abstract void removeAllTableListeners();

	/**
	 * Remove a column from this table
	 * @param c the column instance to remove
	 */
	public abstract void removeColumn(Column c);

	/* (non-Javadoc)
	 * @see prefuse.data.Table#removeColumn(int)
	 */
	public abstract Column removeColumn(int idx);

	/**
	 * Remove a data field from this table
	 * @param field the name of the data field / column to remove
	 * @return the removed Column instance
	 */
	public abstract Column removeColumn(String field);

	/**
	 * Remove the Index associated with the given data field / column name.
	 * @param field the name of the column for which to remove the index
	 * @return true if an index was successfully removed, false if no
	 * such index was found
	 */
	public abstract boolean removeIndex(String field);

	/**
	 * Removes a row from this table.
	 * @param row the row to delete
	 * @return true if the row was successfully deleted, false if the
	 * row was already invalid
	 */
	public abstract boolean removeRow(int row);

	/**
	 * Remove a table listener from this table.
	 * @param listnr the listener to remove
	 */
	public abstract void removeTableListener(TableListener listnr);

	/**
	 * Remove a tuple from this table. If the Tuple is a member of this table,
	 * its row is deleted from the table. Otherwise, nothing is done.
	 * @param t the Tuple to remove from the table
	 * @return true if the Tuple row was successfully deleted, false if the
	 * Tuple is invalid or not a member of this table
	 * @see prefuse.data.tuple.TupleSet#removeTuple(prefuse.data.Tuple)
	 */
	public abstract boolean removeTuple(Tuple t);

	/* (non-Javadoc)
	 * @see prefuse.data.Table#renumberColumns()
	 */
	public abstract void renumberColumns();

	/**
	 * Revert this tuple's value for the given field to the default value
	 * for the field.
	 * @param field the data field
	 * @see #getDefault(String)
	 */
	public abstract void revertToDefault(int row, String field);

	/**
	 * Get an interator over the row numbers of this table.
	 * @return an iterator over the rows of this table
	 */
	public abstract IntIterator rows();

	/**
	 * Get an interator over the row numbers of this table.
	 * @param reverse true to iterate in rever order, false for normal order
	 * @return an iterator over the rows of this table
	 */
	public abstract IntIterator rows(boolean reverse);

	/**
	 * Get a filtered iterator over the row numbers of this table, returning
	 * only the rows whose tuples match the given filter predicate.
	 * @param filter the filter predicate to apply
	 * @return a filtered iterator over the rows of this table
	 */
	public abstract IntIterator rows(Predicate filter);

	/**
	 * Get an iterator over the rows of this table, sorted by the given data
	 * field. This method will create an index over the field if one does
	 * not yet exist.
	 * @param field the data field to sort by
	 * @param ascend true if the iteration should proceed in an ascending
	 * (lowest to highest) sort order, false for a descending order
	 * @return the sorted iterator over rows of this table
	 */
	public abstract IntIterator rowsSortedBy(String field, boolean ascend);

	/**
	 * Query this table for a filtered, sorted subset of this table. This
	 * operation creates an entirely new table independent of this table.
	 * If a filtered view of this same table is preferred, use the
	 * {@link CascadedTable} class.
	 * @param filter the predicate filter determining which rows to include
	 * in the new table. If this value is null, all rows will be included.
	 * @param sort the sorting criteria determining the order in which
	 * rows are added to the new table. If this value is null, the rows
	 * will not be sorted.
	 * @return a new table meeting the query specification
	 */
	public abstract ITable select(Predicate filter, Sort sort);

	/**
	 * Set the value of at the given row and column numbers.
	 * @param row the row number
	 * @param col the column number
	 * @param val the value for the field. If the concrete type of this
	 * Object is not compatible with the underlying data model, an
	 * Exception will be thrown. Use the {@link #canSet(String, Class)}
	 * method to check the type-safety ahead of time.
	 * @see #canSet(String, Class)
	 * @see #getColumnType(String)
	 */
	public abstract void set(int row, int col, Object val);

	/**
	 * Set the value of a given row and data field.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value for the field. If the concrete type of this
	 * Object is not compatible with the underlying data model, an
	 * Exception will be thrown. Use the {@link #canSet(String, Class)}
	 * method to check the type-safety ahead of time.
	 * @see #canSet(String, Class)
	 * @see #getColumnType(String)
	 */
	public abstract void set(int row, String field, Object val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>boolean</code>.
	 * @param row the table row to set
	 * @param col the column number of the data field to set
	 * @param val the value to set
	 * @see #canSetBoolean(String)
	 */
	public abstract void setBoolean(int row, int col, boolean val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>boolean</code>.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value to set
	 * @see #canSetBoolean(String)
	 */
	public abstract void setBoolean(int row, String field, boolean val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>Date</code>.
	 * @param row the table row to set
	 * @param col the column number of the data field to set
	 * @param val the value to set
	 * @see #canSetDate(String)
	 */
	public abstract void setDate(int row, int col, Date val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>Date</code>.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value to set
	 * @see #canSetDate(String)
	 */
	public abstract void setDate(int row, String field, Date val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>double</code>.
	 * @param row the table row to set
	 * @param col the column number of the data field to set
	 * @param val the value to set
	 * @see #canSetDouble(String)
	 */
	public abstract void setDouble(int row, int col, double val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>double</code>.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value to set
	 * @see #canSetDouble(String)
	 */
	public abstract void setDouble(int row, String field, double val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>float</code>.
	 * @param row the table row to set
	 * @param col the column number of the data field to set
	 * @param val the value to set
	 * @see #canSetFloat(String)
	 */
	public abstract void setFloat(int row, int col, float val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>float</code>.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value to set
	 * @see #canSetFloat(String)
	 */
	public abstract void setFloat(int row, String field, float val);

	/**
	 * Set the data value of the given row and field as an
	 * <code>int</code>.
	 * @param row the table row to set
	 * @param col the column number of the data field to set
	 * @param val the value to set
	 * @see #canSetInt(String)
	 */
	public abstract void setInt(int row, int col, int val);

	/**
	 * Set the data value of the given row and field as an
	 * <code>int</code>.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value to set
	 * @see #canSetInt(String)
	 */
	public abstract void setInt(int row, String field, int val);

	/**
	 * Set the data value of the given row and field as an
	 * <code>long</code>.
	 * @param row the table row to set
	 * @param col the column number of the data field to set
	 * @param val the value to set
	 * @see #canSetLong(String)
	 */
	public abstract void setLong(int row, int col, long val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>long</code>.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value to set
	 * @see #canSetLong(String)
	 */
	public abstract void setLong(int row, String field, long val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>String</code>.
	 * @param row the table row to set
	 * @param col the column number of the data field to set
	 * @param val the value to set
	 * @see #canSetString(String)
	 */
	public abstract void setString(int row, int col, String val);

	/**
	 * Set the data value of the given row and field as a
	 * <code>String</code>.
	 * @param row the table row to set
	 * @param field the data field to set
	 * @param val the value to set
	 * @see #canSetString(String)
	 */
	public abstract void setString(int row, String field, String val);

	/**
	 * Clears the contents of this table and then attempts to add the given
	 * Tuple instance.
	 * @param t the Tuple to make the sole tuple in thie table
	 * @return the actual Tuple instance added to this table, or null if
	 * no new Tuple has been added
	 * @see prefuse.data.tuple.TupleSet#setTuple(prefuse.data.Tuple)
	 */
	public abstract Tuple setTuple(Tuple t);

	/**
	 * Sets the TupleManager used by this ITable. Use this method
	 * carefully, as it will cause all existing Tuples retrieved
	 * from this ITable to be invalidated.
	 * @param tm the TupleManager to use
	 */
	public abstract void setTupleManager(TupleManager tm);

	/**
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

	/**
	 * Get an iterator over the tuples in this table.
	 * @return an iterator over the table tuples
	 * @see prefuse.data.tuple.TupleSet#tuples()
	 */
	public abstract Iterator tuples();

	/**
	 * Get an iterator over the tuples for the given rows in this table.
	 * @param rows an iterator over the table rows to visit
	 * @return an iterator over the selected table tuples
	 */
	public abstract Iterator tuples(IntIterator rows);

	/**
	 * Get an iterator over the tuples in this table in reverse order.
	 * @return an iterator over the table tuples in reverse order
	 */
	public abstract Iterator tuplesReversed();

	/* (non-Javadoc)
	 * @see prefuse.data.Table#updateRowCount()
	 */
	public abstract void updateRowCount();

}