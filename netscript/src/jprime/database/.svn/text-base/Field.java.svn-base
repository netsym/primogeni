package jprime.database;

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

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jprime.PersistableObject;

/**
 * @author Nathanael Van Vorst
 *
 */
public abstract class Field {
	
	public static enum ConstraintType {
		AUTO_INCREMENT,
		UNIQUE,
		NOT_NULL,
		INDEX
	};
	public static enum ColumnType {
		BIGINT("BIGINT"),
		BLOB("LONGBLOB","BLOB(10M)"),
		CHAR("CHAR"),
		CHAR_FOR_BIT_DATA("CHAR FOR BIT DATA"),
		CLOB("LONGTEXT","CLOB(10M)"),
		DATE("DATE"),
		DECIMAL("DECIMAL"),
		DOUBLE("DOUBLE"),
		DOUBLE_PRECISION("DOUBLE PRECISION"),
		FLOAT("FLOAT"),
		INTEGER("INTEGER"),
		LONG_VARCHAR("LONG_VARCHAR"),
		LONG_VARCHAR_FOR_BIT_DATA("LONG VARCHAR FOR BIT DATA"),
		NUMERIC("NUMERIC"),
		REAL("REAL"),
		SMALLINT("SMALLINT"),
		TIME("TIME"),
		TIMESTAMP("TIMESTAMP"),
		VARCHAR_256("VARCHAR(256)"),
		VARCHAR_2048("VARCHAR(2048)"),
		VARCHAR_FOR_BIT_DATA("VARCHAR FOR BIT DATA"),
		XML("XML");

		public final String mysql_str;
		public final String derby_str;
		ColumnType(String mysql_str, String derby_str){
			this.mysql_str=mysql_str;
			this.derby_str=derby_str;
		}
		ColumnType(String mysql_str){
			this.mysql_str=mysql_str;
			this.derby_str=mysql_str;
		}
		public String getString(DBType dbtype) {
			if(dbtype==DBType.MYSQL) {
				return mysql_str;
			}
			return derby_str;
		}
	}

	public class BoundValue {
		public final Object value;
		public BoundValue(Object value) {
			super();
			this.value = value;
		}
		public void setValue(int idx, PreparedStatement stmt) throws SQLException {
			switch(type) {
			case CLOB:
				stmt.setObject(idx, value.toString());
				break;
			case BLOB:
				stmt.setBinaryStream(idx,(ByteArrayInputStream)value);
				break;
			case BIGINT:
			case DOUBLE:
			case DOUBLE_PRECISION:
			case SMALLINT:
			case FLOAT:
			case INTEGER:
			case VARCHAR_256:
			case VARCHAR_2048:
			case CHAR:
			case CHAR_FOR_BIT_DATA:
			case DATE:
			case DECIMAL:
			case LONG_VARCHAR:
			case LONG_VARCHAR_FOR_BIT_DATA:
			case NUMERIC:
			case REAL:
			case TIME:
			case TIMESTAMP:
			case VARCHAR_FOR_BIT_DATA:
			case XML:
				try {
					stmt.setObject(idx, value);
				} catch(Exception e) {
					jprime.Console.err.println("field "+name+", val="+value);
					jprime.Console.err.printStackTrace(e);
					jprime.Console.halt(100);
					//throw new RuntimeException(e);
				}
			}
		}
		public String toString() {
			if(value==null) return "";
			return value.toString();
		}
	}
	public final Table table;
	public final String name;
	public final ColumnType type;
	public final ConstraintType[] constraints;
	public final String defaultValue;
	public final boolean isGenerated,isMetadataId;
	public boolean isPKEY=false;

	public Field(Table table, String name, ColumnType type, String defaultValue, ConstraintType[] ct, boolean isMetadataId) {
		super();
		this.table = table;
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
		this.constraints = ct==null?new ConstraintType[]{}:ct;
		boolean t = false;
		for(ConstraintType c : constraints) {
			if(ConstraintType.AUTO_INCREMENT == c) {
				t=true;
				break;
			}
		}
		isGenerated=t;
		this.isMetadataId=isMetadataId;
	}

	public abstract BoundValue getValue(PersistableObject obj);


	public BoundValue getPkeyVal(long l) {
		return new BoundValue(l);
	}
	public BoundValue getPkeyVal(String s) {
		return new BoundValue(s);
	}

}
