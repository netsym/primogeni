package slingshot.visualization;

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

import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.Iterator;

import prefuse.data.ITable;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.column.Column;
import prefuse.data.column.ColumnMetadata;
import prefuse.data.event.TableListener;
import prefuse.data.event.TupleSetListener;
import prefuse.data.expression.Expression;
import prefuse.data.expression.Predicate;
import prefuse.data.tuple.TupleManager;
import prefuse.data.util.Index;
import prefuse.data.util.Sort;
import prefuse.data.util.TableIterator;
import prefuse.util.collections.IntIterator;

/**
 * @author Eduardo Tibau
 *
 */
public class TableAdapter implements ITable {

	private ITable wrapped_table;

	/**
	 * XXX
	 */
	public TableAdapter() {
		wrapped_table = new Table();
	}

	/**
	 * XXX
	 * 
	 * @param t
	 */
	public TableAdapter(ITable t) {
		wrapped_table = t;
	}

	/**
	 * @param name
	 * @param type
	 * @see prefuse.data.Table#addColumn(java.lang.String, java.lang.Class)
	 */
	public void addColumn(String name, @SuppressWarnings("rawtypes") Class type) {
		wrapped_table.addColumn(name, type);
	}

	/**
	 * @param name
	 * @param type
	 * @param defaultValue
	 * @see prefuse.data.Table#addColumn(java.lang.String, java.lang.Class,
	 *      java.lang.Object)
	 */
	public void addColumn(String name, @SuppressWarnings("rawtypes") Class type, Object defaultValue) {
		wrapped_table.addColumn(name, type, defaultValue);
	}

	/**
	 * @param name
	 * @param col
	 * @see prefuse.data.Table#addColumn(java.lang.String,
	 *      prefuse.data.column.Column)
	 */
	public void addColumn(String name, Column col) {
		wrapped_table.addColumn(name, col);
	}

	/**
	 * @param name
	 * @param expr
	 * @see prefuse.data.Table#addColumn(java.lang.String,
	 *      prefuse.data.expression.Expression)
	 */
	public void addColumn(String name, Expression expr) {
		wrapped_table.addColumn(name, expr);
	}

	/**
	 * @param name
	 * @param expr
	 * @see prefuse.data.Table#addColumn(java.lang.String, java.lang.String)
	 */
	public void addColumn(String name, String expr) {
		wrapped_table.addColumn(name, expr);
	}

	/**
	 * @param schema
	 * @see prefuse.data.tuple.AbstractTupleSet#addColumns(prefuse.data.Schema)
	 */
	public void addColumns(Schema schema) {
		wrapped_table.addColumns(schema);
	}

	/**
	 * @param name
	 * @param type
	 * @param dflt
	 * @see prefuse.data.Table#addConstantColumn(java.lang.String,
	 *      java.lang.Class, java.lang.Object)
	 */
	public void addConstantColumn(String name, @SuppressWarnings("rawtypes") Class type, Object dflt) {
		wrapped_table.addConstantColumn(name, type, dflt);
	}

	/**
	 * @param lstnr
	 * @see prefuse.data.tuple.AbstractTupleSet#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener lstnr) {
		wrapped_table.addPropertyChangeListener(lstnr);
	}

	/**
	 * @param key
	 * @param lstnr
	 * @see prefuse.data.tuple.AbstractTupleSet#addPropertyChangeListener(java.lang.String,
	 *      java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(String key,
			PropertyChangeListener lstnr) {
		wrapped_table.addPropertyChangeListener(key, lstnr);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#addRow()
	 */
	public int addRow() {
		return wrapped_table.addRow();
	}

	/**
	 * @param nrows
	 * @see prefuse.data.Table#addRows(int)
	 */
	public void addRows(int nrows) {
		wrapped_table.addRows(nrows);
	}

	/**
	 * @param listnr
	 * @see prefuse.data.Table#addTableListener(prefuse.data.event.TableListener)
	 */
	public void addTableListener(TableListener listnr) {
		wrapped_table.addTableListener(listnr);
	}

	/**
	 * @param t
	 * @return
	 * @see prefuse.data.Table#addTuple(prefuse.data.Tuple)
	 */
	public Tuple addTuple(Tuple t) {
		return wrapped_table.addTuple(t);
	}

	/**
	 * @param tsl
	 * @see prefuse.data.tuple.AbstractTupleSet#addTupleSetListener(prefuse.data.event.TupleSetListener)
	 */
	public void addTupleSetListener(TupleSetListener tsl) {
		wrapped_table.addTupleSetListener(tsl);
	}

	/**
	 * @param field
	 * @param type
	 * @return
	 * @see prefuse.data.Table#canGet(java.lang.String, java.lang.Class)
	 */
	public boolean canGet(String field, @SuppressWarnings("rawtypes") Class type) {
		return wrapped_table.canGet(field, type);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canGetBoolean(java.lang.String)
	 */
	public final boolean canGetBoolean(String field) {
		return wrapped_table.canGetBoolean(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canGetDate(java.lang.String)
	 */
	public final boolean canGetDate(String field) {
		return wrapped_table.canGetDate(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canGetDouble(java.lang.String)
	 */
	public final boolean canGetDouble(String field) {
		return wrapped_table.canGetDouble(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canGetFloat(java.lang.String)
	 */
	public final boolean canGetFloat(String field) {
		return wrapped_table.canGetFloat(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canGetInt(java.lang.String)
	 */
	public final boolean canGetInt(String field) {
		return wrapped_table.canGetInt(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canGetLong(java.lang.String)
	 */
	public final boolean canGetLong(String field) {
		return wrapped_table.canGetLong(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canGetString(java.lang.String)
	 */
	public final boolean canGetString(String field) {
		return wrapped_table.canGetString(field);
	}

	/**
	 * @param field
	 * @param type
	 * @return
	 * @see prefuse.data.Table#canSet(java.lang.String, java.lang.Class)
	 */
	public boolean canSet(String field, @SuppressWarnings("rawtypes") Class type) {
		return wrapped_table.canSet(field, type);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canSetBoolean(java.lang.String)
	 */
	public final boolean canSetBoolean(String field) {
		return wrapped_table.canSetBoolean(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canSetDate(java.lang.String)
	 */
	public final boolean canSetDate(String field) {
		return wrapped_table.canSetDate(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canSetDouble(java.lang.String)
	 */
	public final boolean canSetDouble(String field) {
		return wrapped_table.canSetDouble(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canSetFloat(java.lang.String)
	 */
	public final boolean canSetFloat(String field) {
		return wrapped_table.canSetFloat(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canSetInt(java.lang.String)
	 */
	public final boolean canSetInt(String field) {
		return wrapped_table.canSetInt(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canSetLong(java.lang.String)
	 */
	public final boolean canSetLong(String field) {
		return wrapped_table.canSetLong(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#canSetString(java.lang.String)
	 */
	public final boolean canSetString(String field) {
		return wrapped_table.canSetString(field);
	}

	/**
	 * 
	 * @see prefuse.data.Table#clear()
	 */
	public void clear() {
		wrapped_table.clear();
	}

	/**
	 * @param src
	 * @param idx
	 * @param prev
	 * @see prefuse.data.Table#columnChanged(prefuse.data.column.Column, int,
	 *      boolean)
	 */
	public void columnChanged(Column src, int idx, boolean prev) {
		wrapped_table.columnChanged(src, idx, prev);
	}

	/**
	 * @param src
	 * @param idx
	 * @param prev
	 * @see prefuse.data.Table#columnChanged(prefuse.data.column.Column, int,
	 *      double)
	 */
	public void columnChanged(Column src, int idx, double prev) {
		wrapped_table.columnChanged(src, idx, prev);
	}

	/**
	 * @param src
	 * @param idx
	 * @param prev
	 * @see prefuse.data.Table#columnChanged(prefuse.data.column.Column, int,
	 *      float)
	 */
	public void columnChanged(Column src, int idx, float prev) {
		wrapped_table.columnChanged(src, idx, prev);
	}

	/**
	 * @param src
	 * @param idx
	 * @param prev
	 * @see prefuse.data.Table#columnChanged(prefuse.data.column.Column, int,
	 *      int)
	 */
	public void columnChanged(Column src, int idx, int prev) {
		wrapped_table.columnChanged(src, idx, prev);
	}

	/**
	 * @param src
	 * @param type
	 * @param start
	 * @param end
	 * @see prefuse.data.Table#columnChanged(prefuse.data.column.Column, int,
	 *      int, int)
	 */
	public void columnChanged(Column src, int type, int start, int end) {
		wrapped_table.columnChanged(src, type, start, end);
	}

	/**
	 * @param src
	 * @param idx
	 * @param prev
	 * @see prefuse.data.Table#columnChanged(prefuse.data.column.Column, int,
	 *      long)
	 */
	public void columnChanged(Column src, int idx, long prev) {
		wrapped_table.columnChanged(src, idx, prev);
	}

	/**
	 * @param src
	 * @param idx
	 * @param prev
	 * @see prefuse.data.Table#columnChanged(prefuse.data.column.Column, int,
	 *      java.lang.Object)
	 */
	public void columnChanged(Column src, int idx, Object prev) {
		wrapped_table.columnChanged(src, idx, prev);
	}

	/**
	 * @param t
	 * @return
	 * @see prefuse.data.Table#containsTuple(prefuse.data.Tuple)
	 */
	public boolean containsTuple(Tuple t) {
		return wrapped_table.containsTuple(t);
	}

	/**
	 * @param obj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return wrapped_table.equals(obj);
	}

	/**
	 * @param row0
	 * @param row1
	 * @param col
	 * @param type
	 * @see prefuse.data.Table#fireTableEvent(int, int, int, int)
	 */
	public void fireTableEvent(int row0, int row1, int col, int type) {
		wrapped_table.fireTableEvent(row0, row1, col, type);
	}

	/**
	 * @param t
	 * @param start
	 * @param end
	 * @param type
	 * @see prefuse.data.tuple.AbstractTupleSet#fireTupleEvent(prefuse.data.ITable,
	 *      int, int, int)
	 */
	public void fireTupleEvent(ITable t, int start, int end, int type) {
		wrapped_table.fireTupleEvent(t, start, end, type);
	}

	/**
	 * @param t
	 * @param type
	 * @see prefuse.data.tuple.AbstractTupleSet#fireTupleEvent(prefuse.data.Tuple,
	 *      int)
	 */
	public void fireTupleEvent(Tuple t, int type) {
		wrapped_table.fireTupleEvent(t, type);
	}

	/**
	 * @param added
	 * @param removed
	 * @see prefuse.data.tuple.AbstractTupleSet#fireTupleEvent(prefuse.data.Tuple[],
	 *      prefuse.data.Tuple[])
	 */
	public void fireTupleEvent(Tuple[] added, Tuple[] removed) {
		wrapped_table.fireTupleEvent(added, removed);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#get(int, int)
	 */
	public Object get(int row, int col) {
		return wrapped_table.get(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#get(int, java.lang.String)
	 */
	public Object get(int row, String field) {
		return wrapped_table.get(row, field);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getBoolean(int, int)
	 */
	public final boolean getBoolean(int row, int col) {
		return wrapped_table.getBoolean(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getBoolean(int, java.lang.String)
	 */
	public final boolean getBoolean(int row, String field) {
		return wrapped_table.getBoolean(row, field);
	}

	/**
	 * @param key
	 * @return
	 * @see prefuse.data.tuple.AbstractTupleSet#getClientProperty(java.lang.String)
	 */
	public Object getClientProperty(String key) {
		return wrapped_table.getClientProperty(key);
	}

	/**
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getColumn(int)
	 */
	public Column getColumn(int col) {
		return wrapped_table.getColumn(col);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getColumn(java.lang.String)
	 */
	public Column getColumn(String field) {
		return wrapped_table.getColumn(field);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getColumnCount()
	 */
	public int getColumnCount() {
		return wrapped_table.getColumnCount();
	}

	/**
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getColumnName(int)
	 */
	public String getColumnName(int col) {
		return wrapped_table.getColumnName(col);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getColumnNames()
	 */
	@SuppressWarnings("rawtypes")
	public Iterator getColumnNames() {
		return wrapped_table.getColumnNames();
	}

	/**
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getColumnNumber(prefuse.data.column.Column)
	 */
	public int getColumnNumber(Column col) {
		return wrapped_table.getColumnNumber(col);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getColumnNumber(java.lang.String)
	 */
	public int getColumnNumber(String field) {
		return wrapped_table.getColumnNumber(field);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getColumnRow(int, int)
	 */
	public int getColumnRow(int row, int col) {
		return wrapped_table.getColumnRow(row, col);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getColumns()
	 */
	@SuppressWarnings("rawtypes")
	public Iterator getColumns() {
		return wrapped_table.getColumns();
	}

	/**
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getColumnType(int)
	 */
	@SuppressWarnings("rawtypes")
	public Class getColumnType(int col) {
		return wrapped_table.getColumnType(col);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getColumnType(java.lang.String)
	 */
	@SuppressWarnings("rawtypes")
	public Class getColumnType(String field) {
		return wrapped_table.getColumnType(field);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getDate(int, int)
	 */
	public final Date getDate(int row, int col) {
		return wrapped_table.getDate(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getDate(int, java.lang.String)
	 */
	public final Date getDate(int row, String field) {
		return wrapped_table.getDate(row, field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getDefault(java.lang.String)
	 */
	public Object getDefault(String field) {
		return wrapped_table.getDefault(field);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getDouble(int, int)
	 */
	public final double getDouble(int row, int col) {
		return wrapped_table.getDouble(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getDouble(int, java.lang.String)
	 */
	public final double getDouble(int row, String field) {
		return wrapped_table.getDouble(row, field);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getFloat(int, int)
	 */
	public final float getFloat(int row, int col) {
		return wrapped_table.getFloat(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getFloat(int, java.lang.String)
	 */
	public final float getFloat(int row, String field) {
		return wrapped_table.getFloat(row, field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getIndex(java.lang.String)
	 */
	public Index getIndex(String field) {
		return wrapped_table.getIndex(field);
	}

	/**
	 * @param field
	 * @param expType
	 * @param create
	 * @return
	 * @see prefuse.data.Table#getIndex(java.lang.String, java.lang.Class,
	 *      boolean)
	 */
	@SuppressWarnings("rawtypes")
	public Index getIndex(String field, Class expType, boolean create) {
		return wrapped_table.getIndex(field, expType, create);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getInt(int, int)
	 */
	public final int getInt(int row, int col) {
		return wrapped_table.getInt(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getInt(int, java.lang.String)
	 */
	public final int getInt(int row, String field) {
		return wrapped_table.getInt(row, field);
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getLong(int, int)
	 */
	public final long getLong(int row, int col) {
		return wrapped_table.getLong(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getLong(int, java.lang.String)
	 */
	public final long getLong(int row, String field) {
		return wrapped_table.getLong(row, field);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getMaximumRow()
	 */
	public int getMaximumRow() {
		return wrapped_table.getMaximumRow();
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getMetadata(java.lang.String)
	 */
	public ColumnMetadata getMetadata(String field) {
		return wrapped_table.getMetadata(field);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getMinimumRow()
	 */
	public int getMinimumRow() {
		return wrapped_table.getMinimumRow();
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getModificationCount()
	 */
	public int getModificationCount() {
		return wrapped_table.getModificationCount();
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getRowCount()
	 */
	public int getRowCount() {
		return wrapped_table.getRowCount();
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getSchema()
	 */
	public Schema getSchema() {
		return wrapped_table.getSchema();
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getString(int, int)
	 */
	public final String getString(int row, int col) {
		return wrapped_table.getString(row, col);
	}

	/**
	 * @param row
	 * @param field
	 * @return
	 * @see prefuse.data.Table#getString(int, java.lang.String)
	 */
	public final String getString(int row, String field) {
		return wrapped_table.getString(row, field);
	}

	/**
	 * @param colrow
	 * @param col
	 * @return
	 * @see prefuse.data.Table#getTableRow(int, int)
	 */
	public int getTableRow(int colrow, int col) {
		return wrapped_table.getTableRow(colrow, col);
	}

	/*
	 * @see prefuse.data.ITable#getTableToListenTo()
	 */
	public ITable getTableToListenTo() {
		return wrapped_table;
	}

	/**
	 * @param row
	 * @return
	 * @see prefuse.data.Table#getTuple(int)
	 */
	public Tuple getTuple(int row) {
		return wrapped_table.getTuple(row);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#getTupleCount()
	 */
	public int getTupleCount() {
		return wrapped_table.getTupleCount();
	}

	/**
	 * @param c
	 * @param start
	 * @param end
	 * @see prefuse.data.Table#handleColumnChanged(prefuse.data.column.Column,
	 *      int, int)
	 */
	public void handleColumnChanged(Column c, int start, int end) {
		wrapped_table.handleColumnChanged(c, start, end);
	}

	/**
	 * @param name
	 * @return
	 * @see prefuse.data.Table#hasColumn(java.lang.String)
	 */
	public boolean hasColumn(String name) {
		return wrapped_table.hasColumn(name);
	}

	/**
	 * @return
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return wrapped_table.hashCode();
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#index(java.lang.String)
	 */
	public Index index(String field) {
		return wrapped_table.index(field);
	}

	/**
	 * 
	 * @see prefuse.data.Table#invalidateSchema()
	 */
	public void invalidateSchema() {
		wrapped_table.invalidateSchema();
	}

	/**
	 * @return
	 * @see prefuse.data.Table#isAddColumnSupported()
	 */
	public boolean isAddColumnSupported() {
		return wrapped_table.isAddColumnSupported();
	}

	/**
	 * @param row
	 * @param col
	 * @return
	 * @see prefuse.data.Table#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int col) {
		return wrapped_table.isCellEditable(row, col);
	}

	/**
	 * @param row
	 * @return
	 * @see prefuse.data.Table#isValidRow(int)
	 */
	public boolean isValidRow(int row) {
		return wrapped_table.isValidRow(row);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#iterator()
	 */
	public TableIterator iterator() {
		return wrapped_table.iterator();
	}

	/**
	 * @param rows
	 * @return
	 * @see prefuse.data.Table#iterator(prefuse.util.collections.IntIterator)
	 */
	public TableIterator iterator(IntIterator rows) {
		return wrapped_table.iterator(rows);
	}

	/**
	 * @param key
	 * @param value
	 * @see prefuse.data.tuple.AbstractTupleSet#putClientProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public void putClientProperty(String key, Object value) {
		wrapped_table.putClientProperty(key, value);
	}

	/**
	 * @param field
	 * @param lo
	 * @param hi
	 * @param indexType
	 * @return
	 * @see prefuse.data.Table#rangeSortedBy(java.lang.String, double, double,
	 *      int)
	 */
	public IntIterator rangeSortedBy(String field, double lo, double hi,
			int indexType) {
		return wrapped_table.rangeSortedBy(field, lo, hi, indexType);
	}

	/**
	 * @param field
	 * @param lo
	 * @param hi
	 * @param indexType
	 * @return
	 * @see prefuse.data.Table#rangeSortedBy(java.lang.String, float, float,
	 *      int)
	 */
	public IntIterator rangeSortedBy(String field, float lo, float hi,
			int indexType) {
		return wrapped_table.rangeSortedBy(field, lo, hi, indexType);
	}

	/**
	 * @param field
	 * @param lo
	 * @param hi
	 * @param indexType
	 * @return
	 * @see prefuse.data.Table#rangeSortedBy(java.lang.String, int, int, int)
	 */
	public IntIterator rangeSortedBy(String field, int lo, int hi, int indexType) {
		return wrapped_table.rangeSortedBy(field, lo, hi, indexType);
	}

	/**
	 * @param field
	 * @param lo
	 * @param hi
	 * @param indexType
	 * @return
	 * @see prefuse.data.Table#rangeSortedBy(java.lang.String, long, long, int)
	 */
	public IntIterator rangeSortedBy(String field, long lo, long hi,
			int indexType) {
		return wrapped_table.rangeSortedBy(field, lo, hi, indexType);
	}

	/**
	 * @param field
	 * @param lo
	 * @param hi
	 * @param indexType
	 * @return
	 * @see prefuse.data.Table#rangeSortedBy(java.lang.String, java.lang.Object,
	 *      java.lang.Object, int)
	 */
	public IntIterator rangeSortedBy(String field, Object lo, Object hi,
			int indexType) {
		return wrapped_table.rangeSortedBy(field, lo, hi, indexType);
	}

	/**
	 * @param filter
	 * @see prefuse.data.Table#remove(prefuse.data.expression.Predicate)
	 */
	public void remove(Predicate filter) {
		wrapped_table.remove(filter);
	}

	/**
	 * 
	 * @see prefuse.data.Table#removeAllTableListeners()
	 */
	public void removeAllTableListeners() {
		wrapped_table.removeAllTableListeners();
	}

	/**
	 * @param c
	 * @see prefuse.data.Table#removeColumn(prefuse.data.column.Column)
	 */
	public void removeColumn(Column c) {
		wrapped_table.removeColumn(c);
	}

	/**
	 * @param idx
	 * @return
	 * @see prefuse.data.Table#removeColumn(int)
	 */
	public Column removeColumn(int idx) {
		return wrapped_table.removeColumn(idx);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#removeColumn(java.lang.String)
	 */
	public Column removeColumn(String field) {
		return wrapped_table.removeColumn(field);
	}

	/**
	 * @param field
	 * @return
	 * @see prefuse.data.Table#removeIndex(java.lang.String)
	 */
	public boolean removeIndex(String field) {
		return wrapped_table.removeIndex(field);
	}

	/**
	 * @param lstnr
	 * @see prefuse.data.tuple.AbstractTupleSet#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener lstnr) {
		wrapped_table.removePropertyChangeListener(lstnr);
	}

	/**
	 * @param key
	 * @param lstnr
	 * @see prefuse.data.tuple.AbstractTupleSet#removePropertyChangeListener(java.lang.String,
	 *      java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(String key,
			PropertyChangeListener lstnr) {
		wrapped_table.removePropertyChangeListener(key, lstnr);
	}

	/**
	 * @param row
	 * @return
	 * @see prefuse.data.Table#removeRow(int)
	 */
	public boolean removeRow(int row) {
		return wrapped_table.removeRow(row);
	}

	/**
	 * @param listnr
	 * @see prefuse.data.Table#removeTableListener(prefuse.data.event.TableListener)
	 */
	public void removeTableListener(TableListener listnr) {
		wrapped_table.removeTableListener(listnr);
	}

	/**
	 * @param t
	 * @return
	 * @see prefuse.data.Table#removeTuple(prefuse.data.Tuple)
	 */
	public boolean removeTuple(Tuple t) {
		return wrapped_table.removeTuple(t);
	}

	/**
	 * @param tsl
	 * @see prefuse.data.tuple.AbstractTupleSet#removeTupleSetListener(prefuse.data.event.TupleSetListener)
	 */
	public void removeTupleSetListener(TupleSetListener tsl) {
		wrapped_table.removeTupleSetListener(tsl);
	}

	/**
	 * 
	 * @see prefuse.data.Table#renumberColumns()
	 */
	public void renumberColumns() {
		wrapped_table.renumberColumns();
	}

	/**
	 * @param row
	 * @param field
	 * @see prefuse.data.Table#revertToDefault(int, java.lang.String)
	 */
	public void revertToDefault(int row, String field) {
		wrapped_table.revertToDefault(row, field);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#rows()
	 */
	public IntIterator rows() {
		return wrapped_table.rows();
	}

	/**
	 * @param reverse
	 * @return
	 * @see prefuse.data.Table#rows(boolean)
	 */
	public IntIterator rows(boolean reverse) {
		return wrapped_table.rows(reverse);
	}

	/**
	 * @param filter
	 * @return
	 * @see prefuse.data.Table#rows(prefuse.data.expression.Predicate)
	 */
	public IntIterator rows(Predicate filter) {
		return wrapped_table.rows(filter);
	}

	/**
	 * @param field
	 * @param ascend
	 * @return
	 * @see prefuse.data.Table#rowsSortedBy(java.lang.String, boolean)
	 */
	public IntIterator rowsSortedBy(String field, boolean ascend) {
		return wrapped_table.rowsSortedBy(field, ascend);
	}

	/**
	 * @param filter
	 * @param sort
	 * @return
	 * @see prefuse.data.Table#select(prefuse.data.expression.Predicate,
	 *      prefuse.data.util.Sort)
	 */
	public ITable select(Predicate filter, Sort sort) {
		return wrapped_table.select(filter, sort);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#set(int, int, java.lang.Object)
	 */
	public void set(int row, int col, Object val) {
		wrapped_table.set(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#set(int, java.lang.String, java.lang.Object)
	 */
	public void set(int row, String field, Object val) {
		wrapped_table.set(row, field, val);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#setBoolean(int, int, boolean)
	 */
	public final void setBoolean(int row, int col, boolean val) {
		wrapped_table.setBoolean(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#setBoolean(int, java.lang.String, boolean)
	 */
	public final void setBoolean(int row, String field, boolean val) {
		wrapped_table.setBoolean(row, field, val);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#setDate(int, int, java.util.Date)
	 */
	public final void setDate(int row, int col, Date val) {
		wrapped_table.setDate(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#setDate(int, java.lang.String, java.util.Date)
	 */
	public final void setDate(int row, String field, Date val) {
		wrapped_table.setDate(row, field, val);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#setDouble(int, int, double)
	 */
	public final void setDouble(int row, int col, double val) {
		wrapped_table.setDouble(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#setDouble(int, java.lang.String, double)
	 */
	public final void setDouble(int row, String field, double val) {
		wrapped_table.setDouble(row, field, val);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#setFloat(int, int, float)
	 */
	public final void setFloat(int row, int col, float val) {
		wrapped_table.setFloat(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#setFloat(int, java.lang.String, float)
	 */
	public final void setFloat(int row, String field, float val) {
		wrapped_table.setFloat(row, field, val);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#setInt(int, int, int)
	 */
	public final void setInt(int row, int col, int val) {
		wrapped_table.setInt(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#setInt(int, java.lang.String, int)
	 */
	public final void setInt(int row, String field, int val) {
		wrapped_table.setInt(row, field, val);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#setLong(int, int, long)
	 */
	public final void setLong(int row, int col, long val) {
		wrapped_table.setLong(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#setLong(int, java.lang.String, long)
	 */
	public final void setLong(int row, String field, long val) {
		wrapped_table.setLong(row, field, val);
	}

	/**
	 * @param row
	 * @param col
	 * @param val
	 * @see prefuse.data.Table#setString(int, int, java.lang.String)
	 */
	public final void setString(int row, int col, String val) {
		wrapped_table.setString(row, col, val);
	}

	/**
	 * @param row
	 * @param field
	 * @param val
	 * @see prefuse.data.Table#setString(int, java.lang.String,
	 *      java.lang.String)
	 */
	public final void setString(int row, String field, String val) {
		wrapped_table.setString(row, field, val);
	}

	/**
	 * @param t
	 * @return
	 * @see prefuse.data.Table#setTuple(prefuse.data.Tuple)
	 */
	public Tuple setTuple(Tuple t) {
		return wrapped_table.setTuple(t);
	}

	/**
	 * @param tm
	 * @see prefuse.data.Table#setTupleManager(prefuse.data.tuple.TupleManager)
	 */
	public void setTupleManager(TupleManager tm) {
		wrapped_table.setTupleManager(tm);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#toString()
	 */
	public String toString() {
		return wrapped_table.toString();
	}

	/**
	 * @return
	 * @see prefuse.data.Table#tuples()
	 */
	@SuppressWarnings("rawtypes")
	public Iterator tuples() {
		return wrapped_table.tuples();
	}

	/**
	 * @param rows
	 * @return
	 * @see prefuse.data.Table#tuples(prefuse.util.collections.IntIterator)
	 */
	@SuppressWarnings("rawtypes")
	public Iterator tuples(IntIterator rows) {
		return wrapped_table.tuples(rows);
	}

	/**
	 * @param filter
	 * @return
	 * @see prefuse.data.tuple.AbstractTupleSet#tuples(prefuse.data.expression.Predicate)
	 */
	@SuppressWarnings("rawtypes")
	public Iterator tuples(Predicate filter) {
		return wrapped_table.tuples(filter);
	}

	/**
	 * @param filter
	 * @param sort
	 * @return
	 * @see prefuse.data.tuple.AbstractTupleSet#tuples(prefuse.data.expression.Predicate,
	 *      prefuse.data.util.Sort)
	 */
	@SuppressWarnings("rawtypes")
	public Iterator tuples(Predicate filter, Sort sort) {
		return wrapped_table.tuples(filter, sort);
	}

	/**
	 * @return
	 * @see prefuse.data.Table#tuplesReversed()
	 */
	@SuppressWarnings("rawtypes")
	public Iterator tuplesReversed() {
		return wrapped_table.tuplesReversed();
	}

	/**
	 * 
	 * @see prefuse.data.Table#updateRowCount()
	 */
	public void updateRowCount() {
		wrapped_table.updateRowCount();
	}
}