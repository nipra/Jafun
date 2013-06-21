//----------------------------------------------------------------------------
//
// Module:      SimpleTextPreparedStatement.java
//
// Description: Implementation of the JDBC PreparedStatement interface
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
// A SQL statement is pre-compiled and stored in a
// PreparedStatement object. This object can then be used to
// efficiently execute this statement multiple times.
//
// Note:  The setXXX methods for setting IN parameter values
// must specify types that are compatible with the defined SQL type of
// the input parameter. For instance, if the IN parameter has SQL type
// Integer then setInt should be used.
//
// If arbitrary parameter type conversions are required then the
// setObject method should be used with a target SQL type.
//----------------------------------------------------------------------------
// NOTE - this is an implementation of the JDBC API version 1.20
//---------------------------------------------------------------------------

import java.sql.*;
import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class SimpleTextPreparedStatement
    extends        SimpleTextStatement
    implements    PreparedStatement
{
    //------------------------------------------------------------------------
    // initialize
    //------------------------------------------------------------------------

    public void initialize(
        SimpleTextIConnection con,
        String sql)
        throws SQLException
    {
        super.initialize(con);

        // Save the SQL statement

        sqlStatement = sql;

        // To prepare the statement, we must parse and validate.  The act
        // of preparing does nothing more than validate the statement; it
        // does not save an execution plan.  When the prepared statement is
        // executed, it is re-parsed and re-validated each time.

        parsedSQL = ownerConnection.parseSQL(sql);

        // Prepare the statement, indicating the we are only preparing

        prepare(true);

        // Create a new boundParams Hashtable

        boundParams = new Hashtable();
    }

    //-----------------------------------------------------------------------
    // executeQuery - JDBC API
    // A prepared SQL query is executed and its ResultSet is returned.
    //
    // Returns a ResultSet that contains the data produced by the query
    //-----------------------------------------------------------------------

    public ResultSet executeQuery()
        throws SQLException
    {
        if (traceOn()) {
            trace("@executeQuery()");
        }

        java.sql.ResultSet rs = null;

        // Execute the query.  If execute returns true, then a result set
        // exists

        if (execute()) {
            rs = getResultSet();
        }

        return rs;
    }

    // The overloaded executeQuery on the Statement object (which we
    // extend) is not valid for PreparedStatement or CallableStatement
    // objects.

    public ResultSet executeQuery(
        String sql)
        throws SQLException
    {
        throw new SQLException("Method is not valid");
    }

    //-----------------------------------------------------------------------
    // executeUpdate - JDBC API
    // Execute a SQL INSERT, UPDATE or DELETE statement. In addition,
    // SQL statements that return nothing such as SQL DDL statements
    // can be executed.
    //
    // Returns either the row count for INSERT, UPDATE or DELETE; or 0
    // for SQL statements that return nothing
    //-----------------------------------------------------------------------

    public int executeUpdate()
        throws SQLException
    {
        if (traceOn()) {
            trace("@executeUpdate()");
        }

        int count = -1;

        // Execute the query.  If execute returns false, then an update
        // count exists.

        if (execute() == false) {
            count = getUpdateCount();
        }

        return count;
    }

    // The overloaded executeUpdate on the Statement object (which we
    // extend) is not valid for PreparedStatement or CallableStatement
    // objects.

    public int executeUpdate(
        String sql)
        throws SQLException
    {
        throw new SQLException("Method is not valid");
    }

    //-----------------------------------------------------------------------
    // setNull - JDBC API
    // Set a parameter to SQL NULL.
    //
    // Note: You must specify the parameter's SQL type.
    //
    // @param parameterIndex the first parameter is 1, the second is 2, ...
    // @param sqlType SQL type code defined by java.sql.Types
    //-----------------------------------------------------------------------

    public void setNull(
        int parameterIndex,
        int sqlType)
        throws SQLException
    {
        // The SimpleText driver does not support null values

        throw DriverNotCapable();
    }

    //-----------------------------------------------------------------------
    // setBoolean - JDBC API
    // Set a parameter to a Java boolean value.  The driver converts this
    // to a SQL BIT value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setBoolean(
        int parameterIndex,
        boolean x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setByte - JDBC API
    // Set a parameter to a Java byte value.  The driver converts this
    // to a SQL TINYINT value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setByte(
        int parameterIndex,
        byte x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setShort - JDBC API
    // Set a parameter to a Java short value.  The driver converts this
    // to a SQL SMALLINT value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setShort(
        int parameterIndex,
        short x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setInt - JDBC API
    // Set a parameter to a Java int value.  The driver converts this
    // to a SQL INTEGER value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setInt(
        int parameterIndex,
        int x)
        throws SQLException
    {
        // Validate the parameter index

        verify(parameterIndex);

        // Put the parameter into the boundParams Hashtable.  Coerce the
        // data into a String

        boundParams.put(new Integer(parameterIndex),
                                    (new CommonValue(x)).getString());
    }

    //-----------------------------------------------------------------------
    // setLong - JDBC API
    // Set a parameter to a Java long value.  The driver converts this
    // to a SQL BIGINT value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setLong(
        int parameterIndex,
        long x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setFloat - JDBC API
    // Set a parameter to a Java float value.  The driver converts this
    // to a SQL FLOAT value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setFloat(
        int parameterIndex,
        float x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setDouble - JDBC API
    // Set a parameter to a Java double value.  The driver converts this
    // to a SQL DOUBLE value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setDouble(
        int parameterIndex,
        double x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setBigDecimal - JDBC API
    // Set a parameter to a java.math.BigDecimal value.  The driver converts this
    // to a SQL NUMERIC value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setBigDecimal(
        int parameterIndex,
        java.math.BigDecimal x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setString - JDBC API
    // Set a parameter to a Java String value.  The driver converts this
    // to a SQL VARCHAR or LONGVARCHAR value (depending on the arguments
    // size relative to the driver's limits on VARCHARs) when it sends
    // it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setString(
        int parameterIndex,
        String x)
        throws SQLException
    {
        // Validate the parameter index

        verify(parameterIndex);

        // Put the parameter into the boundParams Hashtable

        boundParams.put(new Integer(parameterIndex), x);
    }

    //-----------------------------------------------------------------------
    // setBytes - JDBC API
    // Set a parameter to a Java array of bytes.  The driver converts this
    // to a SQL VARBINARY or LONGVARBINARY (depending on the arguments
    // size relative to the driver's limits on VARBINARYs) when it sends
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setBytes(
        int parameterIndex,
        byte x[])
        throws SQLException
    {
        // Validate the parameter index

        verify(parameterIndex);

        // Put the parameter into the boundParams Hashtable.  Coerce the
        // data into a String

        boundParams.put(new Integer(parameterIndex),
                                    (new CommonValue(x)).getString());
    }

    //-----------------------------------------------------------------------
    // setDate - JDBC API
    // Set a parameter to a java.sql.Date value.  The driver converts this
    // to a SQL DATE value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setDate(
        int parameterIndex,
        java.sql.Date x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setTime - JDBC API
    // Set a parameter to a java.sql.Time value.  The driver converts this
    // to a SQL TIME value when it sends it to the database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setTime(
        int parameterIndex,
        java.sql.Time x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setTimestamp - JDBC API
    // Set a parameter to a java.sql.Timestamp value.  The driver
    // converts this to a SQL TIMESTAMP value when it sends it to the
    // database.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the parameter value
    //-----------------------------------------------------------------------

    public void setTimestamp(
        int parameterIndex,
        java.sql.Timestamp x)
        throws SQLException
    {
        // The SimpleText driver does not support this data type.  We could
        // coerce the data, but an exception is will be thrown for now.

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setAsciiStream - JDBC API
    // When a very large ASCII value is input to a LONGVARCHAR
    // parameter it may be more practical to send it via a
    // java.io.InputStream. JDBC will read the data from the stream
    // as needed, until it reaches end-of-file.  The JDBC driver will
    // do any necessary conversion from ASCII to the database char format.
    //
    // Note: this stream object can either be a standard
    // Java stream object, or your own subclass that implements the
    // standard interface.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the java input stream which contains the ASCII
    //                    parameter value
    //    length            the number of bytes in the stream
    //-----------------------------------------------------------------------

    public void setAsciiStream(
        int parameterIndex,
        java.io.InputStream x,
        int length)
        throws SQLException
    {
        // Only binary InputStreams are current supported

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setUnicodeStream - JDBC API
    // When a very large UNICODE value is input to a LONGVARCHAR
    // parameter it may be more practical to send it via a
    // java.io.InputStream. JDBC will read the data from the stream
    // as needed, until it reaches end-of-file.  The JDBC driver will
    // do any necessary conversion from UNICODE to the database char format.
    //
    // Note: this stream object can either be a standard
    // Java stream object, or your own subclass that implements the
    // standard interface.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the java input stream which contains the
    //                    UNICODE parameter value
    //    length            the number of bytes in the stream
    //-----------------------------------------------------------------------

    public void setUnicodeStream(
        int parameterIndex,
        java.io.InputStream x,
        int length)
        throws SQLException
    {
        // Only binary InputStreams are current supported

        throw DataTypeNotSupported();
    }

    //-----------------------------------------------------------------------
    // setBinaryStream - JDBC API
    // When a very large binary value is input to a LONGVARBINARY
    // parameter it may be more practical to send it via a
    // java.io.InputStream. JDBC will read the data from the stream
    // as needed, until it reaches end-of-file.
    //
    // Note: this stream object can either be a standard
    // Java stream object, or your own subclass that implements the
    // standard interface.
    //
    //     parameterIndex    the first parameter is 1, the second is 2, ...
    //     x                the java input stream which contains the binary
    //                    parameter value
    //     length            the number of bytes in the stream
    //-----------------------------------------------------------------------

    public void setBinaryStream(
        int parameterIndex,
        java.io.InputStream x,
        int length)
        throws SQLException
    {
        // Validate the parameter index

        verify(parameterIndex);

        // Read in the entire InputStream all at once.  A more optimal
        // way of handling this would be to defer the read until execute
        // time, and only read in chunks at a time.

        byte b[] = new byte[length];

        try {
            x.read(b);
        }
        catch (Exception ex) {
            throw new SQLException("Unable to read InputStream: " +
                                ex.getMessage());
        }

        // Set the data as a byte array

        setBytes(parameterIndex, b);
    }

    //-----------------------------------------------------------------------
    // clearParameters - JDBC API
    // In general parameter values remain in force for repeated use of a
    // Statement. Setting a parameter value automatically clears its
    // previous value.  However In some cases it is useful to immediately
    // release the resources used by the current parameter values; this can
    // be done by calling clearParameters.
    //-----------------------------------------------------------------------

    public void clearParameters()
        throws SQLException
    {
        // Clear all the bound parameters

        boundParams.clear();
    }

    //-----------------------------------------------------------------------
    // setObject - JDBC API
    // Set the value of a parameter using an object; use the
    // java.lang equivalent objects for integral values.
    //
    // The given Java object will be converted to the targetSqlType
    // before being sent to the database.
    //
    // Note that this method may be used to pass datatabase
    // specific abstract data types, by using a Driver specific Java
    // type and using a targetSqlType of java.sql.types.OTHER.
    //
    //     parameterIndex    The first parameter is 1, the second is 2, ...
    //     x                The object containing the input parameter value
    //     targetSqlType    The SQL type (as defined in java.sql.Types) to be
    //                    sent to the database. The scale argument may further
    //                    qualify this type.
    //     scale            For java.sql.Types.DECIMAL or java.sql.Types.NUMERIC
    //                    types this is the number of digits after the decimal.
    //                    For all other types this value will be ignored,
    //-----------------------------------------------------------------------

    public void setObject(
        int parameterIndex,
        Object x,
        int targetSqlType,
        int scale)
        throws SQLException
    {
//TODO
    }

    //-----------------------------------------------------------------------
    // setObject - JDBC API
     // This method is like setObject above, but assumes scale of zero.
     //-----------------------------------------------------------------------

    public void setObject(
        int parameterIndex,
        Object x,
        int targetSqlType)
        throws SQLException
    {
        setObject(parameterIndex, x, targetSqlType, 0);
    }

    //-----------------------------------------------------------------------
    // setObject - JDBC API
    // Set the value of a parameter using an object; use the
    // java.lang equivalent objects for integral values.
    //
    // The JDBC specification specifies a standard mapping from
    // Java Object types to SQL types.  The given argument java object
    // will be converted to the corresponding SQL type before being
    // sent to the database.
    //
    // Note that this method may be used to pass datatabase
    // specific abstract data types, by using a Driver specific Java
    // type.
    //
    //     parameterIndex    The first parameter is 1, the second is 2, ...
    //     x                The object containing the input parameter value
    //-----------------------------------------------------------------------

    public void setObject(
        int parameterIndex,
        Object x)
        throws SQLException
    {
        // We don't handle nulls

        if (x == null) {
            throw new SQLException("Null values are not supported");
        }

        // Determine the data type of the object.  We'll do this by attempting
        // to cast the given Object to the data types that are supported.

        int type = getObjectType(x);

        setObject(parameterIndex, x, type);
    }

    //-----------------------------------------------------------------------
    // execute - JDBC API
    // Some prepared statements return multiple results; the execute
    // method handles these complex statements as well as the simpler
    // form of statements handled by executeQuery and executeUpdate.
    //-----------------------------------------------------------------------

    public boolean execute()
        throws SQLException
    {
        resultSetColumns = null;

        // First, parse the sql statement into a String array

        parsedSQL = ownerConnection.parseSQL(sqlStatement);

        // Now validate the SQL statement and execute it.
        // Returns true if a result set exists.

        boolean rc = prepare(false);

        return rc;
    }

    // The overloaded execute on the Statement object (which we
    // extend) is not valid for PreparedStatement or CallableStatement
    // objects.

    public boolean execute(
        String sql)
        throws SQLException
    {
        throw new SQLException("Method is not valid");
    }

    //------------------------------------------------------------------------
    // verify
    // Verifies the parameter number given is valid, and resets the warnings.
    // If the parameter was previously set, clear it now.
    //------------------------------------------------------------------------

    protected void verify(
        int parameterIndex)
        throws SQLException
    {
        clearWarnings();

        // The paramCount was set when the statement was prepared

        if ((parameterIndex <= 0) ||
            (parameterIndex > paramCount)) {
            throw new SQLException("Invalid parameter number: " +
                                        parameterIndex);
        }

        // If the parameter has already been set, clear it

        if (boundParams.get(new Integer(parameterIndex)) != null) {
            boundParams.remove(new Integer(parameterIndex));
        }
    }

    //------------------------------------------------------------------------
    // getObjectType
    // Given an Object, return the corresponding SQL type.
    //------------------------------------------------------------------------

    protected int getObjectType(
        Object x)
        throws SQLException
    {

        // Determine the data type of the Object by attempting to cast
        // the object.  An exception will be thrown if an invalid casting
        // is attempted.

        try {
            if ((String) x != null) {
                return Types.VARCHAR;
            }
        }
        catch (Exception ex) {
        }

        try {
            if ((Integer) x != null) {
                return Types.INTEGER;
            }
        }
        catch (Exception ex) {
        }

        try {
            if ((byte[]) x != null) {
                return Types.VARBINARY;
            }
        }
        catch (Exception ex) {
        }

        throw new SQLException("Unknown object type");
    }


}
