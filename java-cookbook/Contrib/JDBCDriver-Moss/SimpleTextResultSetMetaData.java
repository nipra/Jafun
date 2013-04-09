//----------------------------------------------------------------------------
//
// Module:      SimpleTextResultSetMetaData.java
//
// Description: Implementation of the JDBC ResultSetMetaData interface
//
// Author:      Karl Moss
//
// Copyright:   (C) 1996,1997 Karl Moss.  All rights reserved.
//              You may study, use, modify and distribute this example
//              for any purpose, provided that this copyright notice
//              appears in all copies.  This example is provided WITHOUT
//              WARRANTY either expressed or implied.
//----------------------------------------------------------------------------

package jdbc.SimpleText;


//----------------------------------------------------------------------------
// A ResultSetMetaData object can be used to find out about the types
// and properties of the columns in a ResultSet.
//----------------------------------------------------------------------------
// NOTE - this is an implementation of the JDBC API version 1.20
//---------------------------------------------------------------------------

import java.sql.*;
import java.util.Hashtable;

public class SimpleTextResultSetMetaData
    extends        SimpleTextObject
    implements    ResultSetMetaData
{
    //------------------------------------------------------------------------
    // initialize
    //------------------------------------------------------------------------

    public void initialize(
        Hashtable columns,
        boolean readOnly)
    {
        inMemoryColumns = columns;
    }

    //------------------------------------------------------------------------
    // getColumnCount - JDBC API
    // What's the number of columns in the ResultSet?
    //
    // Returns the number
    //------------------------------------------------------------------------

    public int getColumnCount()
        throws SQLException
    {
        return inMemoryColumns.size();
    }

    //------------------------------------------------------------------------
    // isAutoIncrement - JDBC API
    // Is the column automatically numbered, thus read-only?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isAutoIncrement(
        int column)
        throws SQLException
    {
        // The SimpleText driver does not support auto increment columns

        return false;
    }

    //------------------------------------------------------------------------
    // isCaseSensitive - JDBC API
    // Does a column's case matter?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isCaseSensitive(
        int column)
        throws SQLException
    {
        // Case never matters

        return false;
    }

    //------------------------------------------------------------------------
    // isSearchable - JDBC API
    // Can the column be used in a where clause?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isSearchable(
        int column)
        throws SQLException
    {
        // The SimpleText driver does not support searching

        return false;
    }

    //------------------------------------------------------------------------
    // isCurrency - JDBC API
    // Is the column a cash value?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isCurrency(
        int column)
        throws SQLException
    {
        // The SimpleText driver does not support any currency data types

        return false;
    }

    //------------------------------------------------------------------------
    // isNullable - JDBC API
    // Can you put a NULL in this column?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns columnNoNulls, columnNullable or columnNullableUnknown
    //------------------------------------------------------------------------

    public int isNullable(
        int column)
        throws SQLException
    {
        // The SimpleText driver does not support nullable columns

        return columnNoNulls;
    }

    //------------------------------------------------------------------------
    // isSigned - JDBC API
    // Is the column a signed number?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isSigned(
        int column)
        throws SQLException
    {
        // The SimpleText driver does not support signed columns

        return false;
    }

    //------------------------------------------------------------------------
    // getColumnDisplaySize
    // What's the column's normal max width in chars?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns max width
    //------------------------------------------------------------------------

    public int getColumnDisplaySize(
        int column)
        throws SQLException
    {
        return (getColumn(column)).displaySize;
    }

    //------------------------------------------------------------------------
    // getColumnLabel - JDBC API
    // What's the suggested column title for use in printouts and
    // displays?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns the column label
    //------------------------------------------------------------------------

    public String getColumnLabel(
        int column)
        throws SQLException
    {
        // Use the column name

        return getColumnName(column);
    }

    //------------------------------------------------------------------------
    // getColumnName - JDBC API
    // What's a column's name?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns column name
    //------------------------------------------------------------------------

    public String getColumnName(
        int column)
        throws SQLException
    {
        return (getColumn(column)).name;
    }

    //------------------------------------------------------------------------
    // getSchemaName - JDBC API
    // What's a column's table's schema?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns schema name or "" if not applicable
    //------------------------------------------------------------------------

    public String getSchemaName(
        int column)
        throws SQLException
    {
        // The SimpleText driver does not support schemas

        return "";
    }

    //------------------------------------------------------------------------
    // getPrecision - JDBC API
    // What's a column's number of decimal digits?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns precision
    //------------------------------------------------------------------------

    public int getPrecision(
        int column)
        throws SQLException
    {
        return (getColumn(column)).precision;
    }

    //------------------------------------------------------------------------
    // getScale - JDBC API
    // What's a column's number of digits to right of decimal?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns scale
    //------------------------------------------------------------------------

    public int getScale(
        int column)
        throws SQLException
    {
        // The SimpleText driver does not support any data types with scale

        return 0;
    }

    //------------------------------------------------------------------------
    // getTableName - JDBC API
    // What's a column's table name?
    //
    // Returns table name or "" if not applicable
    //------------------------------------------------------------------------

    public String getTableName(
        int column)
        throws SQLException
    {
        return "";
    }

    //------------------------------------------------------------------------
    // getCatalogName - JDBC API
    // What's a column's table's catalog name?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns column name or "" if not applicable.
    //------------------------------------------------------------------------

    public String getCatalogName(
        int column)
        throws SQLException
    {
        return "";
    }

    //------------------------------------------------------------------------
    // getColumnType - JDBC API
    // What's a column's SQL type?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns SQL type
    //------------------------------------------------------------------------

    public int getColumnType(
        int column)
        throws SQLException
    {
        return (getColumn(column)).type;
    }

    //------------------------------------------------------------------------
    // getColumnTypeName - JDBC API
    // What's a column's data source specific type name?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns type name
    //------------------------------------------------------------------------

    public String getColumnTypeName(
        int column)
        throws SQLException
    {
        return (getColumn(column)).typeName;
    }

    //------------------------------------------------------------------------
    // isReadOnly - JDBC API
    // Is a column definitely not writable?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isReadOnly(
        int column)
        throws SQLException
    {
        return readOnly;
    }

    //------------------------------------------------------------------------
    // isWritable - JDBC API
    // Is it possible for a write on the column to succeed?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isWritable(
        int column)
        throws SQLException
    {
        return !isReadOnly(column);
    }

    //------------------------------------------------------------------------
    // isDefinitelyWritable - JDBC API
    // Will a write on the column definitely succeed?
    //
    //    column    the first column is 1, the second is 2, ...
    //
    // Returns true if so
    //------------------------------------------------------------------------

    public boolean isDefinitelyWritable(
        int column)
        throws SQLException
    {
        return !isReadOnly(column);
    }

    //------------------------------------------------------------------------
    // getColumn
    // Returns the SimpleTextColumn object for the given column number.
    // If not found, an exception is thrown
    //------------------------------------------------------------------------

    protected SimpleTextColumn getColumn(
        int col)
        throws SQLException
    {
        SimpleTextColumn column = (SimpleTextColumn)
                        inMemoryColumns.get(new Integer(col));

        if (column == null) {
            throw new SQLException("Invalid column number: " + col);
        }

        return column;
    }

    // All column information is kept in a Hashtable.  This hashtable
    // is keyed by the column number, and contains a SimpleTextColumn
    // object

    protected Hashtable inMemoryColumns;

    // Flag indicating whether the connection is read-only

    protected boolean readOnly;
}
