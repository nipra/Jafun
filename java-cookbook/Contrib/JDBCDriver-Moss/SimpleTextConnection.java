//----------------------------------------------------------------------------
//
// Module:      SimpleTextConnection.java
//
// Description: Implementation of the JDBC Connection interface
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
// A Connection represents a session with a specific
// database. Within the context of a Connection, SQL statements are
// executed and results are returned.
//
// A Connection's database is able to provide information
// describing its tables, its supported SQL grammar, its stored
// procedures, the capabilities of this connection, etc. This
// information is obtained with the getMetaData method.
//
// Note: By default the Connection automatically commits
// changes after executing each statement. If auto commit has been
// disabled an explicit commit must be done or database changes will
// not be saved.
//----------------------------------------------------------------------------
// NOTE - this is an implementation of the JDBC API version 1.20
//---------------------------------------------------------------------------

import java.sql.*;
import java.io.*;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class SimpleTextConnection
    extends       SimpleTextObject
    implements    SimpleTextIConnection
{

    //------------------------------------------------------------------------
    // initialize
    // Initialize the Connection object
    //------------------------------------------------------------------------

    public void initialize (
        SimpleTextIDriver driver,
        java.util.Properties info)
        throws SQLException
    {

        // Save the owning driver object

        ownerDriver = driver;

        // Get the security manager and see if we can write to a file.
        // If no security manager is present, assume that we are a trusted
        // application and have read/write privileges.

        canWrite = false;

        SecurityManager securityManager = System.getSecurityManager ();

        if (securityManager != null) {
            try {
                // Use some arbitrary file to check for file write privileges

                securityManager.checkWrite ("SimpleText_Foo");

                // Flag is set if no exception is thrown

                canWrite = true;
            }

            // If we can't write, an exception is thrown.  We'll catch
            // it and do nothing

            catch (SecurityException ex) {
            }
        }
        else {
            canWrite = true;
        }

        // Set our initial read-only flag

        setReadOnly(!canWrite);

        // Get the directory.  It will either be supplied with the URL, in
        //  the property list, or we'll use our current default

        String s = ownerDriver.getSubname();
        int slen = 0;

        if (s != null) {
            slen = s.length();
        }

        if (slen == 0) {
            s = info.getProperty("Directory");
        }

        if (s == null) {
            s = System.getProperty("user.dir");
        }

        setCatalog(s);
    }

    //------------------------------------------------------------------------
    // createStatement - JDBC API
    //
    // SQL statements without parameters are normally
    // executed using Statement objects. If the same SQL statement
    // is executed many times it is more efficient to use a
    // PreparedStatement
    //
    // Returns a new Statement object
    //------------------------------------------------------------------------

    public Statement createStatement()
        throws SQLException
    {
        if (traceOn()) {
            trace("Creating new SimpleTextStatement");
        }

        // Create a new Statement object

        SimpleTextStatement stmt = new SimpleTextStatement();

        // Initialize the statement

        stmt.initialize (this);

        return stmt;
    }

    //------------------------------------------------------------------------
    // prepareStatement - JDBC API
    //
    // A SQL statement with or without IN parameters can be
    // pre-compiled and stored in a PreparedStatement object. This
    // object can then be used to efficiently execute this statement
    // multiple times.
    //
    // Note: This method is optimized for handling
    // parametric SQL statements that benefit from precompilation. If
    // the driver supports precompilation, prepareStatement will send
    // the statement to the database for precompilation. Some drivers
    // may not support precompilation. In this case, the statement may
    // not be sent to the database until the PreparedStatement is
    // executed.  This has no direct affect on users; however, it does
    // affect which method throws certain SQLExceptions.
    //
    //    sql        a SQL statement that may contain one or more '?' IN
    //            parameter placeholders
    //
    // Returns a new PreparedStatement object containing the
    //            pre-compiled statement
    //------------------------------------------------------------------------

    public PreparedStatement prepareStatement(
        String sql)
        throws SQLException
    {
        if (traceOn()) {
            trace("@prepareStatement (sql=" + sql + ")");
        }

        // Create a new PreparedStatement object

        SimpleTextPreparedStatement ps = new SimpleTextPreparedStatement();

        // Initialize the PreparedStatement

        ps.initialize(this, sql);

        return ps;
    }

    //------------------------------------------------------------------------
    // prepareCall - JDBC API
    //
    // A SQL stored procedure call statement is handled by creating a
    // CallableStatement for it. The CallableStatement provides
    // methods for setting up its IN and OUT parameters, and
    // methods for executing it.
    //
    // Note: This method is optimized for handling stored
    // procedure call statements. Some drivers may send the call
    // statement to the database when the prepareCall is done; others
    // may wait until the CallableStatement is executed. This has no
    // direct affect on users; however, it does affect which method
    // throws certain SQLExceptions.
    //
    //    sql        a SQL statement that may contain one or more '?'
    //            parameter placeholders
    //
    // Returns a new CallableStatement object containing the
    //            pre-compiled SQL statement
    //------------------------------------------------------------------------

    public CallableStatement prepareCall(
        String sql)
        throws SQLException
    {
        if (traceOn()) {
            trace("@prepareCall (sql=" + sql + ")");
        }

        // The SimpleText driver does not support callable statements

        throw new SQLException("Driver does not support this function");
    }

    //------------------------------------------------------------------------
    // nativeSQL - JDBC API
    //
    // A driver may convert the JDBC sql grammar into its system's
    // native SQL grammar prior to sending it; nativeSQL returns the
    // native form of the statement that the driver would have sent.
    //
    //    sql        a SQL statement that may contain one or more '?'
    //            parameter placeholders
    //
    // Returns the native form of this statement
    //------------------------------------------------------------------------

    public String nativeSQL(
        String sql)
        throws SQLException
    {

        // For the SimpleText driver, simply return the original
        // sql statement.  Other drivers will need to expand escape
        // sequences here.

        return sql;
    }

    //------------------------------------------------------------------------
    // setAutoCommit - JDBC API
    //
    // If a connection is in auto-commit mode, then all its SQL
    // statements will be executed and committed as individual
    // transactions.  Otherwise, its SQL statements are grouped into
    // transactions that are terminated by either commit() or
    // rollback().  By default, new connections are in auto-commit
    // mode.
    //
    //    autoCommit    true enables auto-commit; false disables
    //                auto-commit.
    //------------------------------------------------------------------------

    public void setAutoCommit(
        boolean autoCommit)
        throws SQLException
    {
        if (traceOn()) {
            trace("@setAutoCommit (autoCommit=" + autoCommit + ")");
        }

        // The SimpleText driver is always in auto-commit mode (it does
        // not support transactions).  Throw an exception if an attempt
        // is made to change the mode

        if (autoCommit == false) {
            throw DriverNotCapable();
        }
    }

    //------------------------------------------------------------------------
    // getAutoCommit - JDBC API
    //
    // Get the current auto-commit state.
    // Returns the current state of auto-commit mode.
    //------------------------------------------------------------------------

    public boolean getAutoCommit()
        throws SQLException
    {
        // The SimpleText driver is always in auto-commit mode (it does
        // not support transactions)

        return true;
    }

    //------------------------------------------------------------------------
    // commit - JDBC API
    //
    // Commit makes all changes made since the previous
    // commit/rollback permanent and releases any database locks
    // currently held by the Connection.
    //------------------------------------------------------------------------

    public void commit()
        throws SQLException
    {
        // No-op for the SimpleText driver
    }

    //------------------------------------------------------------------------
    // rollback - JDBC API
    //
    // Rollback drops all changes made since the previous
    // commit/rollback and releases any database locks currently held
    // by the Connection.
    //------------------------------------------------------------------------

    public void rollback()
        throws SQLException
    {
        // No-op for the SimpleText driver
    }

    //------------------------------------------------------------------------
    // close - JDBC API
    //
    // In some cases, it is desirable to immediately release a
    // Connection's database and JDBC resources instead of waiting for
    // them to be automatically released; the close method provides this
    // immediate release.
    //------------------------------------------------------------------------

    public void close()
        throws SQLException
    {
        connectionClosed = true;
    }

    //------------------------------------------------------------------------
    // isClosed - JDBC API
    //
    // Check if a Connection is closed
    //------------------------------------------------------------------------

    public boolean isClosed()
        throws SQLException
    {
        return connectionClosed;
    }

    //------------------------------------------------------------------------
    // getMetaData - JDBC API
    //
    // A Connection's database is able to provide information
    // describing its tables, its supported SQL grammar, its stored
    // procedures, the capabilities of this connection, etc. This
    // information is made available through a DatabaseMetaData
    // object.
    //
    // Returns a DatabaseMetaData object for this Connection
    //------------------------------------------------------------------------

    public DatabaseMetaData getMetaData()
        throws SQLException
    {
        SimpleTextDatabaseMetaData dbmd = new SimpleTextDatabaseMetaData ();

        dbmd.initialize(this);

        return dbmd;
    }

    //------------------------------------------------------------------------
    // setReadOnly - JDBC API
    //
    // You can put a connection in read-only mode as a hint to enable
    // database optimizations.
    //
    // Note: setReadOnly cannot be called while in the
    // middle of a transaction.
    //------------------------------------------------------------------------

    public void setReadOnly(
        boolean readOnly)
        throws SQLException
    {

        // If we are trying to set the connection not read only (allowing
        // writes), and this connection does not allow writes, throw
        // an exception

        if ((readOnly == false) &&
            (canWrite == false)) {
            throw DriverNotCapable();
        }

        // Set the readOnly attribute for the SimpleText driver.  If set,
        // the driver will not allow updates or deletes to any text file

        this.readOnly = readOnly;
    }

    //------------------------------------------------------------------------
    // isReadOnly - JDBC API
    //
    // Test if the connection is in read-only mode
    //------------------------------------------------------------------------

    public boolean isReadOnly()
        throws SQLException
    {
        return readOnly;
    }

    //------------------------------------------------------------------------
    // setCatalog - JDBC API
    //
    // A sub-space of this Connection's database may be selected by setting a
    // catalog name. If the driver does not support catalogs it will
    // silently ignore this request.
    //------------------------------------------------------------------------

    public void setCatalog(String catalog)
        throws SQLException
    {
        if (traceOn()) {
            trace("@setCatalog(" + catalog + ")");
        }

        // If the last character is a separator, remove it

        if (catalog.endsWith("/") ||
            catalog.endsWith("\\")) {
            catalog = catalog.substring(0, catalog.length());
        }

        // Make sure this is a directory

        File dir = new File(catalog);

        if (!dir.isDirectory()) {
            throw new SQLException("Invalid directory: " + catalog);
        }

        this.catalog = catalog;
    }

    //------------------------------------------------------------------------
    // getCatalog
    // Returns the Connection's current catalog name
    //------------------------------------------------------------------------

    public String getCatalog()
        throws SQLException
    {
        return catalog;
    }

    //------------------------------------------------------------------------
    // setTransactionIsolation - JDBC API
    //
    // You can call this method to try to change the transaction
    // isolation level on a newly opened connection, using one of the
    // TRANSACTION_* values.
    //
    //    level    one of the TRANSACTION_* isolation values with the
    //            exception of TRANSACTION_NONE; some databases may not support
    //            other values
    //------------------------------------------------------------------------

    public void setTransactionIsolation(
        int level)
        throws SQLException
    {
        if (traceOn()) {
            trace("@setTransactionIsolation (level=" + level + ")");
        }

        // Throw an exception if the transaction isolation is being
        // changed to something different

        if (level != TRANSACTION_NONE) {
            throw DriverNotCapable();
        }
    }

    //------------------------------------------------------------------------
    // getTransactionIsolation - JDBC API
    //
    // Get this Connection's current transaction isolation mode
    //------------------------------------------------------------------------

    public int getTransactionIsolation()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return TRANSACTION_NONE;
    }

    //------------------------------------------------------------------------
    // getWarnings - JDBC API
    //
    // The first warning reported by calls on this Connection is
    // returned.
    //
    // Note: Subsequent warnings will be chained to this SQLWarning.
    //------------------------------------------------------------------------

    public SQLWarning getWarnings()
        throws SQLException
    {
        // No warnings exist for the SimpleText driver.  Always return
        // null

        return null;
    }

    //------------------------------------------------------------------------
    // clearWarnings - JDBC API
    //
    // After this call getWarnings returns null until a new warning is
    // reported for this Connection.
    //------------------------------------------------------------------------

    public void clearWarnings()
        throws SQLException
    {
        // No-op
    }

    //------------------------------------------------------------------------
    // parseSQL
    // Given a sql statement, parse it and return a String array with each
    // keyword.  This is a VERY simple parser.
    //------------------------------------------------------------------------

    public String[] parseSQL(
        String sql)
    {
        String keywords[] = null;

        // Create a new Hashtable to keep our words in.  This way, we can
        // build the Hashtable as we go, then create a String array
        // once we know how may words are present

        java.util.Hashtable table = new java.util.Hashtable();
        int count = 0;

        // Current offset in the sql string

        int offset = 0;

        // Get the first word from the sql statement

        String word = parseWord(sql.substring(offset));

        // Loop while more words exist in the sql string

        while (word.length() > 0) {

            // Increment the offset pointer

            offset += word.length();

            // Trim all leading and trailing spaces

            word = word.trim();

            if (word.length() > 0) {

                // Put the word in our hashtable

                table.put(new Integer(count), word);
                count++;
            }

            // Get the next word

            word = parseWord(sql.substring(offset));
        }

        // Create our new String array with the proper number of elements

        keywords = new String[count];

        // Copy the words from the Hashtable to the String array

        for (int i = 0; i < count; i++) {
            keywords[i] = (String) table.get(new Integer(i));
        }
        return keywords;
    }

    //------------------------------------------------------------------------
    // getTables
    // Given a directory and table pattern, return a Hashtable containing
    // SimpleTextTable entries
    //------------------------------------------------------------------------

    public Hashtable getTables(
        String dir,
        String table)
    {
        Hashtable list = new Hashtable();

        // Create a FilenameFilter object.  This object will only allow
        // files with the .SDF extension to be seen

        FilenameFilter filter = new SimpleTextEndsWith(
                    SimpleTextDefine.DATA_FILE_EXT);


        File file = new File(dir);

        if (file.isDirectory()) {

            // List all of the files in the directory with the .SDF extension

            String entries[] = file.list(filter);
            SimpleTextTable tableEntry;

            // Create a SimpleTextTable entry for each, and put in
            // the Hashtable

            for (int i = 0; i < entries.length; i++) {

                // A complete driver needs to further filter the table
                // name here

                tableEntry = new SimpleTextTable(dir, entries[i]);
                list.put(new Integer(i), tableEntry);
            }
        }

        return list;
    }

    //------------------------------------------------------------------------
    // getColumns
    // Given a directory and table name, return a Hashtable containing
    // SimpleTextColumn entries.  Returns null if the table is not found
    //------------------------------------------------------------------------

    public Hashtable getColumns(
        String dir,
        String table)
    {
        Hashtable list = new Hashtable();

        // Create the full path to the table

        String fullPath = dir + "/" + table + SimpleTextDefine.DATA_FILE_EXT;

        File f = new File (fullPath);

        // If the file does not exist, return null

        if (!f.exists()) {
            if (traceOn()) {
                trace("File does not exist: " + fullPath);
            }
            return null;
        }

        String line = "";

        // Create a random access object and read the first line
        // Create the table

        try {
            RandomAccessFile raf = new RandomAccessFile(f, "r");

            // Read the first line, which is the column definitions

            line = raf.readLine();
			raf.close();

        }
        catch (IOException ex) {
            if (traceOn()) {
                trace("Unable to read file: " + fullPath);
            }
            return null;
        }

        // Now, parse the line.  First, check for the branding

        if (!line.startsWith(SimpleTextDefine.DATA_FILE_EXT)) {
            if (traceOn()) {
                trace("Invalid file format: " + fullPath);
            }
            return null;
        }

        line = line.substring(SimpleTextDefine.DATA_FILE_EXT.length());

        // Now we can use the StringTokenizer, since we know that the
        // column names can't contain data within quotes (this is why
        // we can't use the StringTokenizer with SQL statements)

        StringTokenizer st = new StringTokenizer(line, ",");

        String columnName;
        int columnType;
        int precision;
        SimpleTextColumn column;
        int count = 0;
        boolean searchable;
        int displaySize;
        String typeName;

        // Loop while more tokens exist

        while (st.hasMoreTokens()) {
            columnName = (st.nextToken()).trim();

            if (columnName.length() == 0) {
                continue;
            }

            if (columnName.startsWith(SimpleTextDefine.COL_TYPE_NUMBER)) {
                columnType = Types.INTEGER;
                precision = SimpleTextDefine.MAX_INTEGER_LEN;
                columnName = columnName.substring(
                            SimpleTextDefine.COL_TYPE_NUMBER.length());
                displaySize = precision;
                typeName = "VARCHAR";
                searchable = true;
            }
            else if (columnName.startsWith(SimpleTextDefine.COL_TYPE_BINARY)) {
                columnType = Types.VARBINARY;
                precision = SimpleTextDefine.MAX_VARBINARY_LEN;
                columnName = columnName.substring(
                            SimpleTextDefine.COL_TYPE_BINARY.length());
                displaySize = precision * 2;
                typeName = "BINARY";
                searchable = false;
            } else {
                columnType = Types.VARCHAR;
                precision = SimpleTextDefine.MAX_VARCHAR_LEN;
                searchable = true;
                displaySize = precision;
                typeName = "NUMBER";
            }

            // Create a new column object and add to the Hashtable

            column = new SimpleTextColumn(columnName, columnType, precision);
            column.searchable = searchable;
            column.displaySize = displaySize;
            column.typeName = typeName;

            // The column number will be 1-based

            count++;

            // Save the absolute column number

            column.colNo = count;

            list.put(new Integer(count), column);
        }

        return list;
    }

    //------------------------------------------------------------------------
    // getDirectory
    // Given a directory filter (which may be null), format the directory
    // to use in a search.  The default connection directory may be returned
    //------------------------------------------------------------------------

    public String getDirectory(
        String directory)
    {
        String dir;

        if (directory == null) {
            dir = catalog;
        }
        else if (directory.length() == 0) {
            dir = catalog;
        }
        else {
            dir = directory;
            if (dir.endsWith("/") ||
                dir.endsWith("\\")) {
                dir = dir.substring(0, dir.length());
            }
        }

        return dir;
    }


    protected SimpleTextIDriver ownerDriver;// Pointer to the owning
                                            // Driver object

    protected boolean connectionClosed;        // true if the connection is
                                            // currently closed

    protected boolean readOnly;                // true if the connection is
                                            // read-only

    protected boolean canWrite;                // true if we are able to write
                                            // to files

    protected String catalog;                // Current catalog (qualifier)
                                            // for text files

}

//----------------------------------------------------------------------------
// This class is a simple FilenameFilter.  It defines the required accept()
// method to determine whether a specified file should be listed.  A file
// will be listed if its name ends with the specified extension.
//----------------------------------------------------------------------------

class SimpleTextEndsWith
    implements FilenameFilter
{
    public SimpleTextEndsWith(
        String extension)
    {
        ext = extension;
    }

    public boolean accept(
        File dir,
        String name)
    {
        if (name.endsWith(ext)) {
            return true;
        }
        return false;
    }

    protected String ext;
}

