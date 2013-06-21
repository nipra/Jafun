//----------------------------------------------------------------------------
//
// Module:      SimpleTextDatabaseMetaData.java
//
// Description: Implementation of the JDBC DatabaseMetaData interface
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

//---------------------------------------------------------------------------
// This class provides information about the database as a whole.
//
// Many of the methods here return lists of information in ResultSets.
// You can use the normal ResultSet methods such as getString and getInt
// to retrieve the data from these ResultSets.  If a given form of
// metadata is not available, these methods show throw a SQLException.
//
// Some of these methods take arguments that are String patterns.  These
// methods all have names such as fooPattern.  Within a pattern String "%"
// means match any substring of 0 or more characters and "_" means match
// any one character.
//---------------------------------------------------------------------------
// NOTE - this is an implementation of the JDBC API version 1.20
//---------------------------------------------------------------------------

import java.sql.*;
import java.util.Hashtable;

public class SimpleTextDatabaseMetaData
    extends        SimpleTextObject
    implements    DatabaseMetaData
{

    //------------------------------------------------------------------------
    // initialize
    //------------------------------------------------------------------------

    public void initialize(
        SimpleTextIConnection con)
        throws SQLException
    {
        // Save the owning connection object

        ownerConnection = con;
    }

    //-----------------------------------------------------------------------
    // allProceduresAreCallable - JDBC API
    // Can all the procedures returned by getProcedures be called by the
    // current user?
    //-----------------------------------------------------------------------

    public boolean allProceduresAreCallable()
        throws SQLException
    {
        // The SimpleText driver does not support callable statements, so
        // none are callable

        return false;
    }

    //-----------------------------------------------------------------------
    // allTablesAreSelectable - JDBC API
    // Can all the tables returned by getTable be SELECTed by the
    // current user?
    //-----------------------------------------------------------------------

    public boolean allTablesAreSelectable()
        throws SQLException
    {
        // The SimpleText driver allows all tables returned by getTables
        // to be selected

        return true;
    }

    //-----------------------------------------------------------------------
    // getURL - JDBC API
    // What's the url for this database?
    // Return the url or null if it can't be generated
    //-----------------------------------------------------------------------

    public String getURL()
        throws SQLException
    {
        // Can't generate a URL

        return null;
    }

    //-----------------------------------------------------------------------
    // getUserName - JDBC API
    // What's our user name as known to the database?
    //-----------------------------------------------------------------------

    public String getUserName()
        throws SQLException
    {
        // The SimpleText driver does not support user names

        return "";
    }

    //-----------------------------------------------------------------------
    // isReadOnly - JDBC API
    // Is the database in read-only mode?
    //-----------------------------------------------------------------------

    public boolean isReadOnly()
        throws SQLException
    {
        return ownerConnection.isReadOnly();
    }

    //-----------------------------------------------------------------------
    // nullsAreSortedHigh - JDBC API
    // Are NULL values sorted high?
    //-----------------------------------------------------------------------

    public boolean nullsAreSortedHigh()
        throws SQLException
    {
        // The SimpleText driver does not support nulls (or sorting, for
        // that matter)

        return false;
    }

    //-----------------------------------------------------------------------
    // nullsAreSortedLow - JDBC API
    // Are NULL values sorted low?
    //-----------------------------------------------------------------------

    public boolean nullsAreSortedLow()
        throws SQLException
    {
        // The SimpleText driver does not support nulls (or sorting, for
        // that matter)

        return false;
    }

    //-----------------------------------------------------------------------
    // nullsAreSortedAtStart - JDBC API
    // Are NULL values sorted at the start regardless of sort order?
    //-----------------------------------------------------------------------

    public boolean nullsAreSortedAtStart()
        throws SQLException
    {
        // The SimpleText driver does not support nulls (or sorting, for
        // that matter)

        return false;
    }

    //-----------------------------------------------------------------------
    // nullsAreSortedAtEnd - JDBC API
    // Are NULL values sorted at the end regardless of sort order?
    //-----------------------------------------------------------------------

    public boolean nullsAreSortedAtEnd()
        throws SQLException
    {
        // The SimpleText driver does not support nulls (or sorting, for
        // that matter)

        return false;
    }

    //-----------------------------------------------------------------------
    // getDatabaseProductName - JDBC API
    // What's the name of this database product?
    //-----------------------------------------------------------------------

    public String getDatabaseProductName()
        throws SQLException
    {
        return "Simple Text JDBC Driver";
    }

    //-----------------------------------------------------------------------
    // getDatabaseProductVersion - JDBC API
    // What's the version of this database product?
    //-----------------------------------------------------------------------

    public String getDatabaseProductVersion()
        throws SQLException
    {
        return "1.00";
    }

    //-----------------------------------------------------------------------
    // getDriverName - JDBC API
    // What's the name of this JDBC driver?
    //-----------------------------------------------------------------------

    public String getDriverName()
        throws SQLException
    {
        return "SimpleText";
    }

    //-----------------------------------------------------------------------
    // getDriverVersion - JDBC API
    // What's the version of this JDBC driver?
    //-----------------------------------------------------------------------

    public String getDriverVersion()
        throws SQLException
    {
        String s = "";
        int minorVersion = getDriverMinorVersion();

        // Format the minor version to have 4 places, with leading 0's

        if (minorVersion < 1000) s += "0";
        if (minorVersion < 100) s += "0";
        if (minorVersion < 10) s += "0";
        s += "" + minorVersion;

        return "" + getDriverMajorVersion() + "." + s;
    }

    //-----------------------------------------------------------------------
    // getDriverMajorVersion - JDBC API
    // What's this JDBC driver's major version number?
    //-----------------------------------------------------------------------

    public int getDriverMajorVersion()
    {
        return SimpleTextDefine.MAJOR_VERSION;
    }

    //-----------------------------------------------------------------------
    // getDriverMinorVersion - JDBC API
    // What's this JDBC driver's minor version number?
    //-----------------------------------------------------------------------

    public int getDriverMinorVersion()
    {
        return SimpleTextDefine.MINOR_VERSION;
    }

    //-----------------------------------------------------------------------
    // usesLocalFiles - JDBC API
    // Does the database store tables in a local file?
    //-----------------------------------------------------------------------

    public boolean usesLocalFiles()
        throws SQLException
    {
        // The SimpleText driver stores all database data in files

        return true;
    }

    //-----------------------------------------------------------------------
    // usesLocalFilePerTable - JDBC API
    // Does the database use a file for each table?
    //-----------------------------------------------------------------------

    public boolean usesLocalFilePerTable()
        throws SQLException
    {
        // The SimpleText driver uses a file for each table

        return true;
    }

    //-----------------------------------------------------------------------
    // supportsMixedCaseIdentifiers - JDBC API
    // Does the database support mixed case unquoted SQL identifiers?
    //-----------------------------------------------------------------------

    public boolean supportsMixedCaseIdentifiers()
        throws SQLException
    {
        return true;
    }

    //-----------------------------------------------------------------------
    // storesUpperCaseIdentifiers - JDBC API
    // Does the database store mixed case unquoted SQL identifiers in
    // upper case?
    //-----------------------------------------------------------------------

    public boolean storesUpperCaseIdentifiers()
        throws SQLException
    {
        return true;
    }

    //-----------------------------------------------------------------------
    // storesLowerCaseIdentifiers - JDBC API
    // Does the database store mixed case unquoted SQL identifiers in
    // lower case?
    //-----------------------------------------------------------------------

    public boolean storesLowerCaseIdentifiers()
        throws SQLException
    {
        return false;
    }

    //-----------------------------------------------------------------------
    // storesMixedCaseIdentifiers - JDBC API
    // Does the database store mixed case unquoted SQL identifiers in
    // mixed case?
    //-----------------------------------------------------------------------

    public boolean storesMixedCaseIdentifiers()
        throws SQLException
    {
        return false;
    }

    //-----------------------------------------------------------------------
    // supportsMixedCaseQuotedIdentifiers - JDBC API
    // Does the database support mixed case quoted SQL identifiers?
    //
    // A JDBC compliant driver will always return true.
    //-----------------------------------------------------------------------

    public boolean supportsMixedCaseQuotedIdentifiers()
        throws SQLException
    {
        return true;
    }

    //-----------------------------------------------------------------------
    // storesUpperCaseQuotedIdentifiers - JDBC API
    // Does the database store mixed case quoted SQL identifiers in
    // upper case?
    //
    // A JDBC compliant driver will always return true.
    //-----------------------------------------------------------------------

    public boolean storesUpperCaseQuotedIdentifiers()
        throws SQLException
    {
        return true;
    }

    //-----------------------------------------------------------------------
    // storesLowerCaseQuotedIdentifiers - JDBC API
    // Does the database store mixed case quoted SQL identifiers in
    // lower case?
    //
    // A JDBC compliant driver will always return false.
    //-----------------------------------------------------------------------

    public boolean storesLowerCaseQuotedIdentifiers()
        throws SQLException
    {
        return false;
    }

    //-----------------------------------------------------------------------
    // storesMixedCaseQuotedIdentifiers - JDBC API
    // Does the database store mixed case quoted SQL identifiers in
    // mixed case?
    //
    // A JDBC compliant driver will always return false.
    //-----------------------------------------------------------------------

    public boolean storesMixedCaseQuotedIdentifiers()
        throws SQLException
    {
        return false;
    }

    //-----------------------------------------------------------------------
    // getIdentifierQuoteString - JDBC API
    // What's the string used to quote SQL identifiers?
    // This returns a space " " if identifier quoting isn't supported.
    //
    // A JDBC compliant driver always uses a double quote character.
    //-----------------------------------------------------------------------

    public String getIdentifierQuoteString()
        throws SQLException
    {
        // The SimpleText driver does not support quoting

        return " ";
    }

    //-----------------------------------------------------------------------
    // getSQLKeywords - JDBC API
    // Get a comma separated list of all a database's SQL keywords
    // that are NOT also SQL92 keywords.
    //-----------------------------------------------------------------------

    public String getSQLKeywords()
        throws SQLException
    {
        return "";
    }

    //-----------------------------------------------------------------------
    // getNumericFunctions - JDBC API
    // Get a comma separated list of math functions.
    //-----------------------------------------------------------------------

    public String getNumericFunctions()
        throws SQLException
    {
        // The SimpleText driver does not support any math functions

        return "";
    }

    //-----------------------------------------------------------------------
    // getStringFunctions - JDBC API
    // Get a comma separated list of string functions.
    //-----------------------------------------------------------------------

    public String getStringFunctions()
        throws SQLException
    {
        // The SimpleText driver does not support any String functions

        return "";
    }

    //-----------------------------------------------------------------------
    // getSystemFunctions - JDBC API
    // Get a comma separated list of system functions.
    //-----------------------------------------------------------------------

    public String getSystemFunctions()
        throws SQLException
    {
        // The SimpleText driver does not support any System functions

        return "";
    }

    //-----------------------------------------------------------------------
    // getTimeDateFunctions - JDBC API
    // Get a comma separated list of time and date functions.
    //-----------------------------------------------------------------------

    public String getTimeDateFunctions()
        throws SQLException
    {
        // The SimpleText driver does not support any Time or Date functions

        return "";
    }

    //-----------------------------------------------------------------------
    // getSearchStringEscape - JDBC API
    // This is the string that can be used to escape '_' or '%' in
    // the string pattern style catalog search parameters.
    //
    // The '_' character represents any single character.
    // The '%' character represents any sequence of zero or
    // more characters.
    //-----------------------------------------------------------------------

    public String getSearchStringEscape()
        throws SQLException
    {
        // The SimpleText driver does not support search patterns, so
        // return an empty string

        return "";
    }

    //-----------------------------------------------------------------------
    // getExtraNameCharacters - JDBC API
    // Get all the "extra" characters that can be used in unquoted
    // identifier names (those beyond a-z, 0-9 and _).
    //-----------------------------------------------------------------------

    public String getExtraNameCharacters()
        throws SQLException
    {
        // The SimpleText driver does not allow any special characters
        // in indentifier names

        return "";
    }


    //-----------------------------------------------------------------------
    // supportsAlterTableWithAddColumn - JDBC API
    // Is "ALTER TABLE" with add column supported?
    //-----------------------------------------------------------------------

    public boolean supportsAlterTableWithAddColumn()
        throws SQLException
    {
        // The SimpleText driver does not support ALTER TABLE at all

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsAlterTableWithDropColumn - JDBC API
    // Is "ALTER TABLE" with drop column supported?
    //-----------------------------------------------------------------------

    public boolean supportsAlterTableWithDropColumn()
        throws SQLException
    {
        // The SimpleText driver does not support ALTER TABLE at all

        return false;
    }


    //-----------------------------------------------------------------------
    // supportsColumnAliasing - JDBC API
    // Is column aliasing supported?
    //
    // If so, the SQL AS clause can be used to provide names for
    // computed columns or to provide alias names for columns as
    // required.
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsColumnAliasing()
        throws SQLException
    {
        // The SimpleText driver does not support column alias names

        return false;
    }

    //-----------------------------------------------------------------------
    // nullPlusNonNullIsNull - JDBC API
    // Are concatenations between NULL and non-NULL values NULL?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean nullPlusNonNullIsNull()
        throws SQLException
    {
        // The SimpleText driver does not support nulls

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsConvert - JDBC API
    // Is the CONVERT function between SQL types supported?
    //-----------------------------------------------------------------------

    public boolean supportsConvert()
        throws SQLException
    {
        // The SimpleText driver does not support the CONVERT function

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsConvert - JDBC API
    // Is CONVERT between the given SQL types supported?
    //
    //    fromType    the type to convert from
    //    param        toType the type to convert to
    //-----------------------------------------------------------------------

    public boolean supportsConvert(
        int fromType,
        int toType)
        throws SQLException
    {
        // The SimpleText driver does not support the CONVERT function

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsTableCorrelationNames - JDBC API
    // Are table correlation names supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsTableCorrelationNames()
        throws SQLException
    {
        // The SimpleText driver does not support table correlation names

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsDifferentTableCorrelationNames - JDBC API
    // If table correlation names are supported, are they restricted
    // to be different from the names of the tables?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsDifferentTableCorrelationNames()
        throws SQLException
    {
        // The SimpleText driver does not support table correlation names

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsExpressionsInOrderBy - JDBC API
    // Are expressions in "ORDER BY" lists supported?
    //-----------------------------------------------------------------------

    public boolean supportsExpressionsInOrderBy()
        throws SQLException
    {
        // The SimpleText driver does not support ORDER BY

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsOrderByUnrelated - JDBC API
    // Can an "ORDER BY" clause use columns not in the SELECT?
    //-----------------------------------------------------------------------

    public boolean supportsOrderByUnrelated()
        throws SQLException
    {
        // The SimpleText driver does not support ORDER BY

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsGroupBy - JDBC API
    // Is some form of "GROUP BY" clause supported?
    //-----------------------------------------------------------------------

    public boolean supportsGroupBy()
        throws SQLException
    {
        // The SimpleText driver does not support GROUP BY

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsGroupByUnrelated - JDBC API
    // Can a "GROUP BY" clause use columns not in the SELECT?
    //-----------------------------------------------------------------------

    public boolean supportsGroupByUnrelated()
        throws SQLException
    {
        // The SimpleText driver does not support GROUP BY

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsGroupByBeyondSelect - JDBC API
    // Can a "GROUP BY" clause add columns not in the SELECT
    // provided it specifies all the columns in the SELECT?
    //-----------------------------------------------------------------------

    public boolean supportsGroupByBeyondSelect()
        throws SQLException
    {
        // The SimpleText driver does not support GROUP BY

        return false;
    }


    //-----------------------------------------------------------------------
    // supportsLikeEscapeClause - JDBC API
    // Is the escape character in "LIKE" clauses supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsLikeEscapeClause()
        throws SQLException
    {
        // The SimpleText driver does not support the LIKE clause

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsMultipleResultSets - JDBC API
    // Are multiple ResultSets from a single execute supported?
    //-----------------------------------------------------------------------

    public boolean supportsMultipleResultSets()
        throws SQLException
    {
        // The SimpleText driver does not support multiple result sets

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsMultipleTransactions - JDBC API
    // Can we have multiple transactions open at once (on different
    // connections)?
    //-----------------------------------------------------------------------

    public boolean supportsMultipleTransactions()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsNonNullableColumns - JDBC API
    // Can columns be defined as non-nullable?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsNonNullableColumns()
        throws SQLException
    {
        // The SimpleText driver does not support nulls, so all columns by
        // default are non-nullable.  This, however, specifies whether the
        // column can be defined as NON NULL in the DDL (Data Definition
        // Language) statement, which is not supported.

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsMinimumSQLGrammar - JDBC API
    // Is the ODBC Minimum SQL grammar supported?
    //
    // All JDBC compliant drivers must return true.
    //-----------------------------------------------------------------------

    public boolean supportsMinimumSQLGrammar()
        throws SQLException
    {
        // The SimpleText driver does not even support the most minimum
        // SQL grammar

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsCoreSQLGrammar - JDBC API
    // Is the ODBC Core SQL grammar supported?
    //-----------------------------------------------------------------------

    public boolean supportsCoreSQLGrammar()
        throws SQLException
    {
        // The SimpleText driver does not even support the most minimum
        // SQL grammar

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsExtendedSQLGrammar - JDBC API
    // Is the ODBC Extended SQL grammar supported?
    //-----------------------------------------------------------------------

    public boolean supportsExtendedSQLGrammar()
        throws SQLException
    {
        // The SimpleText driver does not even support the most minimum
        // SQL grammar

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsANSI92EntryLevelSQL - JDBC API
    // Is the ANSI92 entry level SQL grammar supported?
    //
    // All JDBC compliant drivers must return true.
    //-----------------------------------------------------------------------

    public boolean supportsANSI92EntryLevelSQL()
        throws SQLException
    {
        // The SimpleText driver does not even support the most minimum
        // SQL grammar

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsANSI92IntermediateSQL - JDBC API
    // Is the ANSI92 intermediate SQL grammar supported?
    //-----------------------------------------------------------------------

    public boolean supportsANSI92IntermediateSQL()
        throws SQLException
    {
        // The SimpleText driver does not even support the most minimum
        // SQL grammar

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsANSI92FullSQL - JDBC API
    // Is the ANSI92 full SQL grammar supported?
    //-----------------------------------------------------------------------

    public boolean supportsANSI92FullSQL()
        throws SQLException
    {
        // The SimpleText driver does not even support the most minimum
        // SQL grammar

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsIntegrityEnhancementFacility - JDBC API
    // Is the SQL Integrity Enhancement Facility supported?
    //-----------------------------------------------------------------------

    public boolean supportsIntegrityEnhancementFacility()
        throws SQLException
    {
        // The SimpleText driver does support referential integrity

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsOuterJoins - JDBC API
    // Is some form of outer join supported?
    //-----------------------------------------------------------------------

    public boolean supportsOuterJoins()
        throws SQLException
    {
        // The SimpleText driver does not support outer joins

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsFullOuterJoins - JDBC API
    // Are full nested outer joins supported?
    //-----------------------------------------------------------------------

    public boolean supportsFullOuterJoins()
        throws SQLException
    {
        // The SimpleText driver does not support outer joins

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsLimitedOuterJoins - JDBC API
    // Is there limited support for outer joins?  (This will be true
    // if supportFullOuterJoins is true.)
    //-----------------------------------------------------------------------

    public boolean supportsLimitedOuterJoins()
        throws SQLException
    {
        // The SimpleText driver does not support outer joins

        return false;
    }

    //-----------------------------------------------------------------------
    // getSchemaTerm - JDBC API
    // What's the database vendor's preferred term for "schema"?
    //-----------------------------------------------------------------------

    public String getSchemaTerm()
        throws SQLException
    {
        return "SCHEMA";
    }

    //-----------------------------------------------------------------------
    // getProcedureTerm - JDBC API
    // What's the database vendor's preferred term for "procedure"?
    //-----------------------------------------------------------------------

    public String getProcedureTerm()
        throws SQLException
    {
        return "PROCEDURE";
    }

    //-----------------------------------------------------------------------
    // getCatalogTerm - JDBC API
    // What's the database vendor's preferred term for "catalog"?
    //-----------------------------------------------------------------------

    public String getCatalogTerm()
        throws SQLException
    {
        return "CATALOG";
    }

    //-----------------------------------------------------------------------
    // isCatalogAtStart - JDBC API
    // Does a catalog appear at the start of a qualified table name?
    // (Otherwise it appears at the end)
    //-----------------------------------------------------------------------

    public boolean isCatalogAtStart()
        throws SQLException
    {
        // The SimpleText driver supports specifying fully qualified
        // file names, so the catalog (directory) is specified first

        return true;
    }

    //-----------------------------------------------------------------------
    // getCatalogSeparator - JDBC API
    // What's the separator between catalog and table name?
    //-----------------------------------------------------------------------

    public String getCatalogSeparator()
        throws SQLException
    {
        // The SimpleText driver supports specifying fully qualified
        // file names, so the catalog separator is the directory
        // separator

        return "/";
    }

    //-----------------------------------------------------------------------
    // supportsSchemasInDataManipulation - JDBC API
    // Can a schema name be used in a data manipulation statement?
    //-----------------------------------------------------------------------

    public boolean supportsSchemasInDataManipulation()
        throws SQLException
    {
        // The SimpleText driver does not support schemas

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSchemasInProcedureCalls - JDBC API
    // Can a schema name be used in a procedure call statement?
    //-----------------------------------------------------------------------

    public boolean supportsSchemasInProcedureCalls()
        throws SQLException
    {
        // The SimpleText driver does not support schemas

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSchemasInTableDefinitions - JDBC API
    // Can a schema name be used in a table definition statement?
    //-----------------------------------------------------------------------

    public boolean supportsSchemasInTableDefinitions()
        throws SQLException
    {
        // The SimpleText driver does not support schemas

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSchemasInIndexDefinitions - JDBC API
    // Can a schema name be used in an index definition statement?
    //-----------------------------------------------------------------------

    public boolean supportsSchemasInIndexDefinitions()
        throws SQLException
    {
        // The SimpleText driver does not support schemas

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSchemasInPrivilegeDefinitions - JDBC API
    // Can a schema name be used in a privilege definition statement?
    //-----------------------------------------------------------------------

    public boolean supportsSchemasInPrivilegeDefinitions()
        throws SQLException
    {
        // The SimpleText driver does not support schemas

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsCatalogsInDataManipulation - JDBC API
    // Can a catalog name be used in a data manipulation statement?
    //-----------------------------------------------------------------------

    public boolean supportsCatalogsInDataManipulation()
        throws SQLException
    {
        // The SimpleText driver does support catalogs (path names)

        return true;
    }

    //-----------------------------------------------------------------------
    // supportsCatalogsInProcedureCalls - JDBC API
    // Can a catalog name be used in a procedure call statement?
    //-----------------------------------------------------------------------

    public boolean supportsCatalogsInProcedureCalls()
        throws SQLException
    {
        // The SimpleText driver does not support stored procedures

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsCatalogsInTableDefintions - JDBC API
    // Can a catalog name be used in a table definition statement?
    //-----------------------------------------------------------------------

    public boolean supportsCatalogsInTableDefinitions()
        throws SQLException
    {
        // The SimpleText driver does support catalogs (path names)

        return true;
    }

    //-----------------------------------------------------------------------
    // supportsCatalogsInIndexDefinitions - JDBC API
    // Can a catalog name be used in a index definition statement?
    //-----------------------------------------------------------------------

    public boolean supportsCatalogsInIndexDefinitions()
        throws SQLException
    {
        // The SimpleText driver does not support indexes

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsCatalogsInPrivilegeDefinitions - JDBC API
    // Can a catalog name be used in a privilege definition statement?
    //-----------------------------------------------------------------------

    public boolean supportsCatalogsInPrivilegeDefinitions()
        throws SQLException
    {
        // The SimpleText driver does not support privileges

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsPositionedDelete - JDBC API
    // Is positioned DELETE supported?
    //-----------------------------------------------------------------------

    public boolean supportsPositionedDelete()
        throws SQLException
    {
        // The SimpleText driver does not support positioned deletes

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsPositionedUpdate - JDBC API
    // Is positioned UPDATE supported?
    //-----------------------------------------------------------------------

    public boolean supportsPositionedUpdate()
        throws SQLException
    {
        // The SimpleText driver does not support positioned updates

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSelectForUpdate - JDBC API
    // Is SELECT for UPDATE supported?
    //-----------------------------------------------------------------------

    public boolean supportsSelectForUpdate()
        throws SQLException
    {
        // The SimpleText driver does not support the FOR UPDATE clause

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsStoredProcedures - JDBC API
    // Are stored procedure calls using the stored procedure escape
    // syntax supported?
    //-----------------------------------------------------------------------

    public boolean supportsStoredProcedures()
        throws SQLException
    {
        // The SimpleText driver does not support stored procedures

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSubqueriesInComparisons - JDBC API
    // Are subqueries in comparison expressions supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsSubqueriesInComparisons()
        throws SQLException
    {
        // The SimpleText driver does not support subqueries

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSubqueriesInExists - JDBC API
    // Are subqueries in exists expressions supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsSubqueriesInExists()
        throws SQLException
    {
        // The SimpleText driver does not support subqueries

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSubqueriesInIns - JDBC API
    // Are subqueries in "in" statements supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsSubqueriesInIns()
        throws SQLException
    {
        // The SimpleText driver does not support subqueries

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsSubqueriesInQuantifieds - JDBC API
    // Are subqueries in quantified expressions supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsSubqueriesInQuantifieds()
        throws SQLException
    {
        // The SimpleText driver does not support subqueries

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsCorrelatedSubqueries - JDBC API
    // Are correlated subqueries supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsCorrelatedSubqueries()
        throws SQLException
    {
        // The SimpleText driver does not support subqueries

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsUnion - JDBC API
    // Is SQL UNION supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsUnion()
        throws SQLException
    {
        // The SimpleText driver does not support unions

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsUnionAll - JDBC API
    // Is SQL UNION ALL supported?
    //
    // A JDBC compliant driver always returns true.
    //-----------------------------------------------------------------------

    public boolean supportsUnionAll()
        throws SQLException
    {
        // The SimpleText driver does not support unions

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsOpenCursorsAcrossCommit - JDBC API
    // Can cursors remain open across commits?
    //-----------------------------------------------------------------------

    public boolean supportsOpenCursorsAcrossCommit()
        throws SQLException
    {
        // The SimpleText driver does not support transactions; it is
        // always in auto-commit mode.  The cursor remains open after
        // a transaction is auto-committed

        return true;
    }

    //-----------------------------------------------------------------------
    // supportsOpenCursorsAcrossRollback - JDBC API
    // Can cursors remain open across rollbacks?
    //-----------------------------------------------------------------------

    public boolean supportsOpenCursorsAcrossRollback()
        throws SQLException
    {
        // The SimpleText driver does not support transactions; it is
        // always in auto-commit mode.  The cursor remains open after
        // a transaction is auto-committed.  A rollback has no effect.

        return true;
    }

    //-----------------------------------------------------------------------
    // supportsOpenStatementsAcrossCommit - JDBC API
    // Can statements remain open across commits?
    //-----------------------------------------------------------------------

    public boolean supportsOpenStatementsAcrossCommit()
        throws SQLException
    {
        // The SimpleText driver does not support transactions; it is
        // always in auto-commit mode.  The statement remains open after
        // a transaction is auto-committed

        return true;
    }


    //-----------------------------------------------------------------------
    // supportsOpenStatementsAcrossRollback - JDBC API
    // Can statements remain open across rollbacks?
    //-----------------------------------------------------------------------

    public boolean supportsOpenStatementsAcrossRollback()
        throws SQLException
    {
        // The SimpleText driver does not support transactions; it is
        // always in auto-commit mode.  The statement remains open after
        // a transaction is auto-committed.  A rollback has no effect.

        return true;
    }

    //-----------------------------------------------------------------------
    // getMaxBinaryLiteralLength - JDBC API
    // How many hex characters can you have in an inline binary literal?
    //-----------------------------------------------------------------------

    public int getMaxBinaryLiteralLength()
        throws SQLException
    {
        // The SimpleText driver does not have a limit.  0 indicates no
        // limit, or the limit is not known.

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxCharLiteralLength - JDBC API
    // What's the max length for a character literal?
    //-----------------------------------------------------------------------

    public int getMaxCharLiteralLength()
        throws SQLException
    {
        // The SimpleText driver does not have a limit.  0 indicates no
        // limit, or the limit is not known.

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxColumnNameLength - JDBC API
    // What's the limit on column name length?
    //-----------------------------------------------------------------------

    public int getMaxColumnNameLength()
        throws SQLException
    {
        return SimpleTextDefine.MAX_COLUMN_NAME_LEN;
    }

    //-----------------------------------------------------------------------
    // getMaxColumnsInGroupBy - JDBC API
    // What's the maximum number of columns in a "GROUP BY" clause?
    //-----------------------------------------------------------------------

    public int getMaxColumnsInGroupBy()
        throws SQLException
    {
        // The SimpleText driver does not support GROUP BY

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxColumnsInIndex - JDBC API
    // What's the maximum number of columns allowed in an index?
    //-----------------------------------------------------------------------

    public int getMaxColumnsInIndex()
        throws SQLException
    {
        // The SimpleText driver does not support indexes

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxColumnsInOrderBy - JDBC API
    // What's the maximum number of columns in an "ORDER BY" clause?
    //-----------------------------------------------------------------------

    public int getMaxColumnsInOrderBy()
        throws SQLException
    {
        // The SimpleText driver does not support ORDER BY

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxColumnsInSelect - JDBC API
    // What's the maximum number of columns in a "SELECT" list?
    //-----------------------------------------------------------------------

    public int getMaxColumnsInSelect()
        throws SQLException
    {
        // The SimpleText driver does not have a limit.  0 indicates no
        // limit, or the limit is not known.

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxColumnsInTable - JDBC API
    // What's maximum number of columns in a table?
    //-----------------------------------------------------------------------

    public int getMaxColumnsInTable()
        throws SQLException
    {
        return SimpleTextDefine.MAX_COLUMNS_IN_TABLE;
    }

    //-----------------------------------------------------------------------
    // getMaxConnections - JDBC API
    // How many active connections can we have at a time to this database?
    //-----------------------------------------------------------------------

    public int getMaxConnections()
        throws SQLException
    {
        // The SimpleText driver does not have a limit.  0 indicates no
        // limit, or the limit is not known.

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxCursorNameLength - JDBC API
    // What's the maximum cursor name length?
    //-----------------------------------------------------------------------

    public int getMaxCursorNameLength()
        throws SQLException
    {
        // The SimpleText driver does not support named cursors

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxIndexLength - JDBC API
    // What's the maximum length of an index (in bytes)?
    //-----------------------------------------------------------------------

    public int getMaxIndexLength()
        throws SQLException
    {
        // The SimpleText driver does not support indexes

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxSchemaNameLength - JDBC API
    // What's the maximum length allowed for a schema name?
    //-----------------------------------------------------------------------

    public int getMaxSchemaNameLength()
        throws SQLException
    {
        // The SimpleText driver does not support schemas

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxProcedureNameLength - JDBC API
    // What's the maximum length of a procedure name?
    //-----------------------------------------------------------------------

    public int getMaxProcedureNameLength()
        throws SQLException
    {
        // The SimpleText driver does not support stored procedures

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxCatalogNameLength - JDBC API
    // What's the maximum length of a catalog name?
    //-----------------------------------------------------------------------

    public int getMaxCatalogNameLength()
        throws SQLException
    {
        return SimpleTextDefine.MAX_CATALOG_NAME_LEN;
    }

    //-----------------------------------------------------------------------
    // getMaxRowSize - JDBC API
    // What's the maximum length of a single row?
    //-----------------------------------------------------------------------

    public int getMaxRowSize()
        throws SQLException
    {
        // The SimpleText driver does not have a limit.  0 indicates no
        // limit, or the limit is not known.

        return 0;
    }

    //-----------------------------------------------------------------------
    // doesMaxRowSizeIncludeBlobs - JDBC API
    // Did getMaxRowSize() include LONGVARCHAR and LONGVARBINARY
    // blobs?
    //-----------------------------------------------------------------------

    public boolean doesMaxRowSizeIncludeBlobs()
        throws SQLException
    {
        return false;
    }

    //-----------------------------------------------------------------------
    // getMaxStatementLength - JDBC API
    // What's the maximum length of a SQL statement?
    //-----------------------------------------------------------------------

    public int getMaxStatementLength()
        throws SQLException
    {
        // The SimpleText driver does not have a limit.  0 indicates no
        // limit, or the limit is not known.

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxStatements - JDBC API
    // How many active statements can we have open at one time to this
    // database?
    //-----------------------------------------------------------------------

    public int getMaxStatements()
        throws SQLException
    {
        // The SimpleText driver does not have a limit.  0 indicates no
        // limit, or the limit is not known.

        return 0;
    }

    //-----------------------------------------------------------------------
    // getMaxTableNameLength - JDBC API
    // What's the maximum length of a table name?
    //-----------------------------------------------------------------------

    public int getMaxTableNameLength()
        throws SQLException
    {
        return SimpleTextDefine.MAX_TABLE_NAME_LEN;
    }

    //-----------------------------------------------------------------------
    // getMaxTablesInSelect - JDBC API
    // What's the maximum number of tables in a SELECT?
    //-----------------------------------------------------------------------

    public int getMaxTablesInSelect()
        throws SQLException
    {
        // The SimpleText driver does not support joins, so only 1 table
        // is allowed to be specified in a SELECT statement

        return 1;
    }

    //-----------------------------------------------------------------------
    // getMaxUserNameLength - JDBC API
    // What's the maximum length of a user name?
    //-----------------------------------------------------------------------

    public int getMaxUserNameLength()
        throws SQLException
    {
        // The SimpleText driver does not support users

        return 0;
    }

    //-----------------------------------------------------------------------
    // getDefaultTransactionIsolation - JDBC API
    // What's the database's default transaction isolation level?  The
    // values are defined in java.sql.Connection.
    //-----------------------------------------------------------------------

    public int getDefaultTransactionIsolation()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return Connection.TRANSACTION_NONE;
    }

    //-----------------------------------------------------------------------
    // supportsTransactions - JDBC API
    // Are transactions supported? If not, commit is a noop and the
    // isolation level is TRANSACTION_NONE.
    //-----------------------------------------------------------------------

    public boolean supportsTransactions()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsTransactionIsolationLevel - JDBC API
    // Does the database support the given transaction isolation level?
    //
    //    level    the values are defined in java.sql.Connection
    //-----------------------------------------------------------------------

    public boolean supportsTransactionIsolationLevel(
        int level)
        throws SQLException
    {
        // The SimpleText driver does not support transaction.  Return
        // false for any level except for TRANSACTION_NONE

        boolean rc = false;
        if (level == Connection.TRANSACTION_NONE) {
            rc = true;
        }
        return rc;
    }

    //-----------------------------------------------------------------------
    // supportsDataDefinitionAndDataManipulationTransactions - JDBC API
    // Are both data definition and data manipulation statements
    // within a transaction supported?
    //-----------------------------------------------------------------------

    public boolean supportsDataDefinitionAndDataManipulationTransactions()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return false;
    }

    //-----------------------------------------------------------------------
    // supportsDataManipulationTransactionsOnly
    // Are only data manipulation statements within a transaction
    // supported?
    //-----------------------------------------------------------------------

    public boolean supportsDataManipulationTransactionsOnly()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return false;
    }

    //-----------------------------------------------------------------------
    // dataDefinitionsCausesTransactionCommit - JDBC API
    // Does a data definition statement within a transaction force the
    // transaction to commit?
    //-----------------------------------------------------------------------

    public boolean dataDefinitionCausesTransactionCommit()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return false;
    }

    //-----------------------------------------------------------------------
    // dataDefinitionIgnoredInTransactions - JDBC API
    // Is a data definition statement within a transaction ignored?
    //-----------------------------------------------------------------------

    public boolean dataDefinitionIgnoredInTransactions()
        throws SQLException
    {
        // The SimpleText driver does not support transactions

        return false;
    }


    //-----------------------------------------------------------------------
    // getProcedures - JDBC API
    // Get a description of stored procedures available in a
    // catalog.
    //
    // Only procedure descriptions matching the schema and
    // procedure name criteria are returned.  They are ordered by
    // PROCEDURE_SCHEM, and PROCEDURE_NAME.
    //
    // Each procedure description has the the following columns:
    //
    //    (1) PROCEDURE_CAT    String => procedure catalog (may be null)
    //    (2) PROCEDURE_SCHEM    String => procedure schema (may be null)
    //    (3) PROCEDURE_NAME    String => procedure name
    //    (4) REMARKS            String => explanatory comment on the procedure
    //    (5) PROCEDURE_TYPE    short => kind of procedure:
    //            procedureResultUnknown - May return a result
    //            procedureNoResult - Does not return a result
    //            procedureReturnsResult - Returns a result
    //
    //    catalog            a catalog name; "" retrieves those without a catalog
    //    schemaPattern    a schema name pattern; "" retrieves those
    //                    without a schema
    //    procedureNamePattern    a procedure name pattern
    //
    // Returns a ResultSet.  Each row is a procedure description
    //-----------------------------------------------------------------------

    public ResultSet getProcedures(
        String catalog,
        String schemaPattern,
        String procedureNamePattern)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getProcedures(" + catalog + ", " + schemaPattern + ", " +
                    procedureNamePattern + ")");
        }

        // The SimpleText driver does not support procedures.  Instead of
        // throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "PROCEDURE_CAT", Types.VARCHAR);
        add(columns, 2, "PROCEDURE_SCHEM", Types.VARCHAR);
        add(columns, 3, "PROCEDURE_NAME", Types.VARCHAR);
        add(columns, 4, "REMARKS", Types.VARCHAR);
        add(columns, 5, "PROCEDURE_TYPE", Types.SMALLINT);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getProcedureColumns - JDBC API
    // Get a description of a catalog's stored procedure parameters
    // and result columns.
    //
    // Only descriptions matching the schema, procedure and
    // parameter name criteria are returned.  They are ordered by
    // PROCEDURE_SCHEM and PROCEDURE_NAME. Within this, the return value,
    // if any, is first. Next are the parameter descriptions in call
    // order. The column descriptions follow in column number order.
    //
    // Each row in the ResultSet is a parameter desription or
    // column description with the following fields:
    //
    //    (1) PROCEDURE_CAT    String => procedure catalog (may be null)
    //    (2) PROCEDURE_SCHEM    String => procedure schema (may be null)
    //    (3) PROCEDURE_NAME    String => procedure name
    //    (4) COLUMN_NAME        String => column/parameter name
    //    (5) COLUMN_TYPE        Short => kind of column/parameter:
    //            procedureColumnUnknown - nobody knows
    //            procedureColumnIn - IN parameter
    //            procedureColumnInOut - INOUT parameter
    //            procedureColumnOut - OUT parameter
    //            procedureColumnReturn - procedure return value
    //            procedureColumnResult - result column in ResultSet
    //    (6) DATA_TYPE        short => SQL type from java.sql.Types
    //    (7) TYPE_NAME        String => SQL type name
    //  (8) PRECISION        int => precision
    //    (9) LENGTH            int => length in bytes of data
    //    (10) SCALE            short => scale
    //    (11) RADIX            short => radix
    //    (12) NULLABLE        short => can it contain NULL?
    //            procedureNoNulls - does not allow NULL values
    //            procedureNullable - allows NULL values
    //            procedureNullableUnknown - nullability unknown
    //    (13) REMARKS        String => comment describing parameter/column
    //
    // Note: Some databases may not return the column
    // descriptions for a procedure. Additional columns beyond
    // REMARKS can be defined by the database.
    //
    //    catalog            a catalog name; "" retrieves those without a catalog
    //    schemaPattern    a schema name pattern; "" retrieves those
    //                    without a schema
    //    procedureNamePattern    a procedure name pattern
    //    columnNamePattern        a column name pattern
    //
    // Returns a ResultSet.  Each row is a stored procedure parameter or
    // column description
    //-----------------------------------------------------------------------

    public ResultSet getProcedureColumns(
        String catalog,
        String schemaPattern,
        String procedureNamePattern,
        String columnNamePattern)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getProcedureColumns(" + catalog + ", " + schemaPattern +
                    ", " + procedureNamePattern + ", " +
                    columnNamePattern + ")");
        }

        // The SimpleText driver does not support procedures.  Instead of
        // throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "PROCEDURE_CAT", Types.VARCHAR);
        add(columns, 2, "PROCEDURE_SCHEM", Types.VARCHAR);
        add(columns, 3, "PROCEDURE_NAME", Types.VARCHAR);
        add(columns, 4, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "COLUMN_TYPE", Types.SMALLINT);
        add(columns, 6, "DATA_TYPE", Types.SMALLINT);
        add(columns, 7, "TYPE_NAME", Types.VARCHAR);
        add(columns, 8, "PRECISION", Types.INTEGER);
        add(columns, 9, "LENGTH", Types.INTEGER);
        add(columns, 10, "LENGTH", Types.SMALLINT);
        add(columns, 11, "RADIX", Types.SMALLINT);
        add(columns, 12, "NULLABLE", Types.SMALLINT);
        add(columns, 13, "REMARKS", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getTables - JDBC API
    // Get a description of tables available in a catalog.
    //
    // Only table descriptions matching the catalog, schema, table
    // name and type criteria are returned.  They are ordered by
    // TABLE_TYPE, TABLE_SCHEM and TABLE_NAME.
    //
    // Each table description has the following columns:
    //
    //    (1) TABLE_CAT    String => table catalog (may be null)
    //    (2) TABLE_SCHEM    String => table schema (may be null)
    //    (3) TABLE_NAME    String => table name
    //    (4) TABLE_TYPE    String => table type.
    //            Typical types are "TABLE", "VIEW", "SYSTEM TABLE",
    //            "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM"
    //    (5) REMARKS    String => explanatory comment on the table
    //
    // Note: Some databases may not return information for
    // all tables.
    //
    //    catalog            a catalog name; "" retrieves those without a catalog
    //    schemaPattern    a schema name pattern; "" retrieves those
    //                    without a schema
    //    tableNamePattern    a table name pattern
    //    types            a list of table types to include; null returns all
    //                    types
    //
    // Returns a ResultSet.  Each row is a table description
    //-----------------------------------------------------------------------

    public ResultSet getTables(
        String catalog,
        String schemaPattern,
        String tableNamePattern,
        String types[])
        throws SQLException
    {
        if (traceOn()) {
            trace("@getTables(" + catalog + ", " + schemaPattern +
                    ", " + tableNamePattern + ")");
        }

        // Create a statement object

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_CAT", Types.VARCHAR);
        add(columns, 2, "TABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "TABLE_NAME", Types.VARCHAR);
        add(columns, 4, "TABLE_TYPE", Types.VARCHAR);
        add(columns, 5, "REMARKS", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // If any of the parameters will return an empty result set, do so

        boolean willBeEmpty = false;

        // If table types are specified, make sure that 'TABLE' is
        // included.  If not, no rows will be returned

        if (types != null) {
            willBeEmpty = true;
            for (int ii = 0; ii < types.length; ii++) {
                if (types[ii].equalsIgnoreCase("TABLE")) {
                    willBeEmpty = false;
                    break;
                }
            }
        }

        if (!willBeEmpty) {

            // Get a Hashtable will all tables

            Hashtable tables = ownerConnection.getTables(
                    ownerConnection.getDirectory(catalog), tableNamePattern);

            Hashtable singleRow;
            SimpleTextTable table;

            // Create a row for each table in the Hashtable

            for (int i = 0; i < tables.size(); i++) {
                table = (SimpleTextTable) tables.get(new Integer(i));

                // Create a new Hashtable for a single row

                singleRow = new Hashtable();

                // Build the row
                singleRow.put(new Integer(1), new CommonValue(table.dir));
                singleRow.put(new Integer(3), new CommonValue(table.name));
                singleRow.put(new Integer(4), new CommonValue("TABLE"));

                // Add it to the row list
                rows.put(new Integer(i + 1), singleRow);
            }
        }

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getSchemas - JDBC API
    // Get the schema names available in this database.  The results
    // are ordered by schema name.
    //
    // The schema column is:
    //
    //    (1) TABLE_SCHEM        String => schema name
    //
    // Returns a ResultSet.  Each row has a single String column that is a
    // schema name
    //-----------------------------------------------------------------------

    public ResultSet getSchemas()
        throws SQLException
    {
        if (traceOn()) {
            trace("@getSchemas");
        }

        // The SimpleText driver does not support schemas.  Instead of
        // throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_SCHEM", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getCatalogs - JDBC API
    // Get the catalog names available in this database.  The results
    // are ordered by catalog name.
    //
    // The catalog column is:
    //
    //    (1) TABLE_CAT    String => catalog name
    //
    // Returns a ResultSet.  Each row has a single String column that is a
    // catalog name
    //-----------------------------------------------------------------------

    public ResultSet getCatalogs()
        throws SQLException
    {
        if (traceOn()) {
            trace("@getCatalogs");
        }

        // The SimpleText driver only supports one catalog - the current
        // directory for the connection

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_CAT", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create a Hashtable for a single row

        Hashtable singleRow = new Hashtable();

        // Set the value for column 1, which is the directory for the
        // connection

        singleRow.put (new Integer(1),
                new CommonValue(ownerConnection.getDirectory(null)));

        rows.put (new Integer(1), singleRow);

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getTableTypes - JDBC API
    // Get the table types available in this database.  The results
    // are ordered by table type.
    //
    // The table type is:
    //
    //    (1) TABLE_TYPE    String => table type.
    //            Typical types are "TABLE", "VIEW", "SYSTEM TABLE",
    //            "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
    //
    // Return a ResultSet.  Each row has a single String column that is a
    // table type
    //-----------------------------------------------------------------------

    public ResultSet getTableTypes()
        throws SQLException
    {
        if (traceOn()) {
            trace("@getTableTypes");
        }

        // The SimpleText driver only supports one table type - TABLE

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_TYPE", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create a Hashtable for a single row

        Hashtable singleRow = new Hashtable();

        // Set the value for column 1, which is the only table type - TABLE

        singleRow.put (new Integer(1), new CommonValue("TABLE"));

        rows.put (new Integer(1), singleRow);

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getColumns - JDBC API
    // Get a description of table columns available in a catalog.
    //
    // Only column descriptions matching the catalog, schema, table
    // and column name criteria are returned.  They are ordered by
    // TABLE_SCHEM, TABLE_NAME and ORDINAL_POSITION.
    //
    // Each column description has the following columns:
    //
    //    (1) TABLE_CAT        String => table catalog (may be null)
    //    (2) TABLE_SCHEM        String => table schema (may be null)
    //    (3) TABLE_NAME        String => table name
    //    (4) COLUMN_NAME        String => column name
    //    (5) DATA_TYPE        short => SQL type from java.sql.Types
    //    (6) TYPE_NAME        String => Data source dependent type name
    //    (7) COLUMN_SIZE        int => column size.  For char or date
    //            types this is the maximum number of characters, for numeric or
    //            decimal types this is precision.
    //    (8) BUFFER_LENGTH    int => is not used.
    //    (9) DECIMAL_DIGITS    int => the number of fractional digits
    //    (10) NUM_PREC_RADIX    int => Radix (typically either 10 or 2)
    //    (11) NULLABLE        int => is NULL allowed?
    //            columnNoNulls - might not allow NULL values
    //            columnNullable - definitely allows NULL values
    //            columnNullableUnknown - nullability unknown
    //    (12) REMARKS        String => comment describing column (may be null)
    //    (13) COLUMN_DEF        String => default value (may be null)
    //    (14) SQL_DATA_TYPE    int => unused
    //    (15) SQL_DATETIME_SUB    int => unused
    //    (16) CHAR_OCTET_LENGTH    int => for char types the
    //            maximum number of bytes in the column
    //    (17) ORDINAL_POSITION    int    => index of column in table
    //            (starting at 1)
    //    (18) IS_NULLABLE    String => "NO" means column definitely
    //            does not allow NULL values; "YES" means the column might
    //            allow NULL values.  An empty string means nobody knows.
    //
    //    catalog            a catalog name; "" retrieves those without a catalog
    //    schemaPattern    a schema name pattern; "" retrieves those
    //                    without a schema
    //    tableNamePattern    a table name pattern
    //    columnNamePattern    a column name pattern
    //
    // Returns a ResultSet.  Each row is a column description
    //-----------------------------------------------------------------------

    public ResultSet getColumns(
        String catalog,
        String schemaPattern,
        String tableNamePattern,
        String columnNamePattern)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getColumns(" + catalog + ", " + schemaPattern +
                    ", " + tableNamePattern + ", " + columnNamePattern + ")");
        }

        // Create a statement object

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_CAT", Types.VARCHAR);
        add(columns, 2, "TABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "TABLE_NAME", Types.VARCHAR);
        add(columns, 4, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "DATA_TYPE", Types.SMALLINT);
        add(columns, 6, "TYPE_NAME", Types.VARCHAR);
        add(columns, 7, "COLUMN_SIZE", Types.INTEGER);
        add(columns, 8, "BUFFER_LENGTH", Types.INTEGER);
        add(columns, 9, "DECIMAL_DIGITS", Types.INTEGER);
        add(columns, 10, "NUM_PREC_RADIX", Types.INTEGER);
        add(columns, 11, "NULLABLE", Types.INTEGER);
        add(columns, 12, "REMARKS", Types.VARCHAR);
        add(columns, 13, "COLUMN_DEF", Types.VARCHAR);
        add(columns, 14, "SQL_DATA_TYPE", Types.INTEGER);
        add(columns, 15, "SQL_DATETIME_SUB", Types.INTEGER);
        add(columns, 16, "CHAR_OCTET_LENGTH", Types.INTEGER);
        add(columns, 17, "ORDINAL_POSITION", Types.INTEGER);
        add(columns, 18, "IS_NULLABLE", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Get a Hashtable will all tables

        Hashtable tables = ownerConnection.getTables(
                    ownerConnection.getDirectory(catalog), tableNamePattern);

        Hashtable singleRow;
        Hashtable columnList;
        SimpleTextTable table;
        SimpleTextColumn column;

        int count = 0;

        // Create a row for each column in each table in the Hashtable

        for (int i = 0; i < tables.size(); i++) {
            table = (SimpleTextTable) tables.get(new Integer(i));

            // Get the columns

            columnList = ownerConnection.getColumns(table.dir, table.name);

            if (columnList == null) {
                continue;
            }

            for (int ii = 1; ii <= columnList.size(); ii++) {

                column = (SimpleTextColumn) columnList.get(new Integer(ii));

                // Create a new Hashtable for a single row

                singleRow = new Hashtable();

                // Build the row
                singleRow.put(new Integer(1), new CommonValue(table.dir));
                singleRow.put(new Integer(3), new CommonValue(table.name));
                singleRow.put(new Integer(4), new CommonValue(column.name));
                singleRow.put(new Integer(5), new CommonValue(column.type));
                singleRow.put(new Integer(6),
                            new CommonValue(typeToName(column.type)));
                singleRow.put(new Integer(7),
                                    new CommonValue(column.precision));

                // Add it to the row list (column numbers are 1-based)
                count++;
                rows.put(new Integer(count), singleRow);
            }

        }

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getColumnPriviledges - JDBC API
    // Get a description of the access rights for a table's columns.
    //
    // Only privileges matching the column name criteria are
    // returned.  They are ordered by COLUMN_NAME and PRIVILEGE.
    //
    // Each privilige description has the following columns:
    //
    //    (1) TABLE_CAT    String => table catalog (may be null)
    //    (2) TABLE_SCHEM    String => table schema (may be null)
    //    (3) TABLE_NAME    String => table name
    //    (4) COLUMN_NAME    String => column name
    //    (5) GRANTOR        String => grantor of access (may be null)
    //    (6) GRANTEE        String => grantee of access
    //    (7) PRIVILEGE    String => name of access (SELECT,
    //            INSERT, UPDATE, REFRENCES, ...)
    //    (8) IS_GRANTABLE    String => "YES" if grantee is permitted
    //            to grant to others; "NO" if not; null if unknown
    //
    //    catalog        a catalog name; "" retrieves those without a catalog
    //    schema        a schema name; "" retrieves those without a schema
    //    table        a table name
    //    columnNamePattern    a column name pattern
    //
    // Returns a ResultSet.  Each row is a column privilege description
    //-----------------------------------------------------------------------

    public ResultSet getColumnPrivileges(
        String catalog,
        String schema,
        String table,
        String columnNamePattern)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getColumnPrivileges(" + catalog + ", " + schema + ", " +
                            table + ", " + columnNamePattern + ")");
        }

        // The SimpleText driver does not support column privileges.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_CAT", Types.VARCHAR);
        add(columns, 2, "TABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "TABLE_NAME", Types.VARCHAR);
        add(columns, 4, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "GRANTOR", Types.VARCHAR);
        add(columns, 6, "GRANTEE", Types.VARCHAR);
        add(columns, 7, "PRIVILEGE", Types.VARCHAR);
        add(columns, 8, "IS_GRANTABLE", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getTablePrivileges - JDBC API
    // Get a description of the access rights for each table available
    // in a catalog.
    //
    // Only privileges matching the schema and table name
    // criteria are returned.  They are ordered by TABLE_SCHEM,
    // TABLE_NAME, and PRIVILEGE.
    //
    // Each privilige description has the following columns:
    //
    //    (1) TABLE_CAT    String => table catalog (may be null)
    //    (2) TABLE_SCHEM    String => table schema (may be null)
    //    (3) TABLE_NAME    String => table name
    //    (4) COLUMN_NAME    String => column name
    //    (5) GRANTOR        String => grantor of access (may be null)
    //    (6) GRANTEE        String => grantee of access
    //    (7) PRIVILEGE    String => name of access (SELECT,
    //            INSERT, UPDATE, REFRENCES, ...)
    //    (8) IS_GRANTABLE    String => "YES" if grantee is permitted
    //            to grant to others; "NO" if not; null if unknown
    //
    //    catalog            a catalog name; "" retrieves those without a catalog
    //    schemaPattern    a schema name pattern; "" retrieves those
    //                    without a schema
    //    tableNamePattern    a table name pattern
    //
    // Returns a ResultSet.  Each row is a table privilege description
    //-----------------------------------------------------------------------

    public ResultSet getTablePrivileges(
        String catalog,
        String schemaPattern,
        String tableNamePattern)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getTablePrivileges(" + catalog + ", " + schemaPattern +
                            ", " + tableNamePattern + ")");
        }

        // The SimpleText driver does not support table privileges.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_CAT", Types.VARCHAR);
        add(columns, 2, "TABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "TABLE_NAME", Types.VARCHAR);
        add(columns, 4, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "GRANTOR", Types.VARCHAR);
        add(columns, 6, "GRANTEE", Types.VARCHAR);
        add(columns, 7, "PRIVILEGE", Types.VARCHAR);
        add(columns, 8, "IS_GRANTABLE", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getBestRowIdentifier - JDBC API
    // Get a description of a table's optimal set of columns that
    // uniquely identifies a row. They are ordered by SCOPE.
    //
    // Each column description has the following columns:
    //
    //    (1) SCOPE            short => actual scope of result
    //            bestRowTemporary - very temporary, while using row
    //            bestRowTransaction - valid for remainder of current transaction
    //            bestRowSession - valid for remainder of current session
    //    (2) COLUMN_NAME        String => column name
    //    (3) DATA_TYPE        short => SQL data type from java.sql.Types
    //    (4) TYPE_NAME        String => Data source dependent type name
    //    (5) COLUMN_SIZE        int => precision
    //    (6) BUFFER_LENGTH    int => not used
    //    (7) DECIMAL_DIGITS    short => scale
    //    (8) PSEUDO_COLUMN    short => is this a pseudo column
    //                                 like an Oracle ROWID
    //            bestRowUnknown - may or may not be pseudo column
    //            bestRowNotPseudo - is NOT a pseudo column
    //            bestRowPseudo - is a pseudo column
    //
    //    catalog        a catalog name; "" retrieves those without a catalog
    //    schema        a schema name; "" retrieves those without a schema
    //    table        a table name
    //    scope        the scope of interest; use same values as SCOPE
    //    nullable    include columns that are nullable?
    //
    // Returns a ResultSet.  Each row is a column description
    //-----------------------------------------------------------------------

    public ResultSet getBestRowIdentifier(
        String catalog,
        String schema,
        String table,
        int scope,
        boolean nullable)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getBestRowIdentifier(" + catalog + ", " + schema + ", " +
                            table + ", " + scope + ", " + nullable + ")");
        }

        // The SimpleText driver does not support row identifiers.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "SCOPE", Types.SMALLINT);
        add(columns, 2, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 3, "DATA_TYPE", Types.SMALLINT);
        add(columns, 4, "TYPE_NAME", Types.VARCHAR);
        add(columns, 5, "COLUMN_SIZE", Types.INTEGER);
        add(columns, 6, "BUFFER_LENGTH", Types.INTEGER);
        add(columns, 7, "DECIMAL_DIGITS", Types.SMALLINT);
        add(columns, 8, "PSEUDO_COLUMN", Types.SMALLINT);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }


    //-----------------------------------------------------------------------
    // getVersionColumns - JDBC API
    // Get a description of a table's columns that are automatically
    // updated when any value in a row is updated.  They are
    // unordered.
    //
    // Each column description has the following columns:
    //
    //    (1) SCOPE            short => is not used
    //    (2) COLUMN_NAME        String => column name
    //    (3) DATA_TYPE        short => SQL data type from java.sql.Types
    //    (4) TYPE_NAME        String => Data source dependent type name
    //    (5) COLUMN_SIZE        int => precision
    //    (6) BUFFER_LENGTH    int => length of column value in bytes
    //    (7) DECIMAL_DIGITS    short => scale
    //    (8) PSEUDO_COLUMN    short => is this a pseudo column
    //                        like an Oracle ROWID
    //            versionColumnUnknown - may or may not be pseudo column
    //            versionColumnNotPseudo - is NOT a pseudo column
    //            versionColumnPseudo - is a pseudo column
    //
    //    catalog    a catalog name; "" retrieves those without a catalog
    //    schema    a schema name; "" retrieves those without a schema
    //    table    a table name
    //
    // Returns a ResultSet.  Each row is a column description
    //-----------------------------------------------------------------------

    public ResultSet getVersionColumns(
        String catalog,
        String schema,
        String table)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getVersionColumns(" + catalog + ", " + schema + ", " +
                            table + ")");
        }

        // The SimpleText driver does not support version columns.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "SCOPE", Types.SMALLINT);
        add(columns, 2, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 3, "DATA_TYPE", Types.SMALLINT);
        add(columns, 4, "TYPE_NAME", Types.VARCHAR);
        add(columns, 5, "COLUMN_SIZE", Types.INTEGER);
        add(columns, 6, "BUFFER_LENGTH", Types.INTEGER);
        add(columns, 7, "DECIMAL_DIGITS", Types.SMALLINT);
        add(columns, 8, "PSEUDO_COLUMN", Types.SMALLINT);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getPrimaryKeys - JDBC API
    // Get a description of a table's primary key columns.  They
    // are ordered by COLUMN_NAME.
    //
    // Each column description has the following columns:
    //
    //    (1) TABLE_CAT        String => table catalog (may be null)
    //    (2) TABLE_SCHEM        String => table schema (may be null)
    //    (3) TABLE_NAME        String => table name
    //    (4) COLUMN_NAME        String => column name
    //    (5) KEY_SEQ            short => sequence number within primary key
    //    (6) PK_NAME            String => primary key name (may be null)
    //
    //    catalog    a catalog name; "" retrieves those without a catalog
    //    schema    a schema name pattern; "" retrieves those
    //            without a schema
    //    table    a table name
    //
    // Returns a ResultSet.  Each row is a primary key column description
    //-----------------------------------------------------------------------

    public ResultSet getPrimaryKeys(
        String catalog,
        String schema,
        String table)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getPrimaryKeys(" + catalog + ", " + schema + ", " +
                            table + ")");
        }

        // The SimpleText driver does not support indexes.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_CAT", Types.VARCHAR);
        add(columns, 2, "TABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "TABLE_NAME", Types.VARCHAR);
        add(columns, 4, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "KEY_SEQ", Types.SMALLINT);
        add(columns, 6, "PK_NAME", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getImportedKeys - JDBC API
    // Get a description of the primary key columns that are
    // referenced by a table's foreign key columns (the primary keys
    // imported by a table).  They are ordered by PKTABLE_CAT,
    // PKTABLE_SCHEM, PKTABLE_NAME, and KEY_SEQ.
    //
    // Each primary key column description has the following columns:
    //
    //    (1) PKTABLE_CAT        String => primary key table catalog
    //                            being imported (may be null)
    //    (2) PKTABLE_SCHEM    String => primary key table schema
    //                            being imported (may be null)
    //    (3) PKTABLE_NAME    String => primary key table name
    //                            being imported
    //    (4) PKCOLUMN_NAME    String => primary key column name
    //                            being imported
    //    (5) FKTABLE_CAT        String => foreign key table catalog (may be null)
    //    (6) FKTABLE_SCHEM    String => foreign key table schema (may be null)
    //    (7) FKTABLE_NAME    String => foreign key table name
    //    (8) FKCOLUMN_NAME    String => foreign key column name
    //    (9) KEY_SEQ            short => sequence number within foreign key
    //    (10) UPDATE_RULE    short => What happens to
    //                            foreign key when primary is updated:
    //            importedKeyCascade - change imported key to agree
    //                 with primary key update
    //            importedKeyRestrict - do not allow update of primary
    //                key if it has been imported
    //            importedKeySetNull - change imported key to NULL if
    //                its primary key has been updated
    //    (11) DELETE_RULE    short => What happens to
    //                            the foreign key when primary is deleted.
    //            importedKeyCascade - delete rows that import a deleted key
    //            importedKeyRestrict - do not allow delete of primary
    //                key if it has been imported
    //            importedKeySetNull - change imported key to NULL if
    //                its primary key has been deleted
    //    (12) FK_NAME        String => foreign key name (may be null)
    //    (13) PK_NAME        String => primary key name (may be null)
    //
    //    catalog    a catalog name; "" retrieves those without a catalog
    //    schema    a schema name pattern; "" retrieves those without a schema
    //    table    a table name
    //
    // Returns a ResultSet.  Each row is a primary key column description
    //-----------------------------------------------------------------------

    public ResultSet getImportedKeys(
        String catalog,
        String schema,
        String table)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getImportedKeys(" + catalog + ", " + schema + ", " +
                            table + ")");
        }

        // The SimpleText driver does not support indexes.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "PKTABLE_CAT", Types.VARCHAR);
        add(columns, 2, "PKTABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "PKTABLE_NAME", Types.VARCHAR);
        add(columns, 4, "PKCOLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "FKTABLE_CAT", Types.VARCHAR);
        add(columns, 6, "FKTABLE_SCHEM", Types.VARCHAR);
        add(columns, 7, "FKTABLE_NAME", Types.VARCHAR);
        add(columns, 8, "FKCOLUMN_NAME", Types.VARCHAR);
        add(columns, 9, "KEY_SEQ", Types.SMALLINT);
        add(columns, 10, "UPDATE_RULE", Types.SMALLINT);
        add(columns, 11, "DELETE_RULE", Types.SMALLINT);
        add(columns, 12, "FK_NAME", Types.VARCHAR);
        add(columns, 13, "PK_NAME", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }


    //-----------------------------------------------------------------------
    // getExportedKeys - JDBC API
    // Get a description of a foreign key columns that reference a
    // table's primary key columns (the foreign keys exported by a
    // table).  They are ordered by FKTABLE_CAT, FKTABLE_SCHEM,
    // FKTABLE_NAME, and KEY_SEQ.
    //
    // Column definitions are the same as getImportedKeys.
    //
    // Returns a ResultSet.  Each row is a foreign key column description
    //-----------------------------------------------------------------------

    public ResultSet getExportedKeys(
        String catalog,
        String schema,
        String table)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getExportedKeys(" + catalog + ", " + schema + ", " +
                            table + ")");
        }

        // The SimpleText driver does not support indexes.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "PKTABLE_CAT", Types.VARCHAR);
        add(columns, 2, "PKTABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "PKTABLE_NAME", Types.VARCHAR);
        add(columns, 4, "PKCOLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "FKTABLE_CAT", Types.VARCHAR);
        add(columns, 6, "FKTABLE_SCHEM", Types.VARCHAR);
        add(columns, 7, "FKTABLE_NAME", Types.VARCHAR);
        add(columns, 8, "FKCOLUMN_NAME", Types.VARCHAR);
        add(columns, 9, "KEY_SEQ", Types.SMALLINT);
        add(columns, 10, "UPDATE_RULE", Types.SMALLINT);
        add(columns, 11, "DELETE_RULE", Types.SMALLINT);
        add(columns, 12, "FK_NAME", Types.VARCHAR);
        add(columns, 13, "PK_NAME", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getCrossReference - JDBC API
    // Get a description of the foreign key columns in the foreign key
    // table that reference the primary key columns of the primary key
    // table (describe how one table imports another's key.) This
    // should normally return a single foreign key/primary key pair
    // (most tables only import a foreign key from a table once.)  They
    // are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and
    // KEY_SEQ.
    //
    // Column definitions are the same as getImportedKeys.
    //
    // Returns a ResultSet.  Each row is a foreign key column description
    //-----------------------------------------------------------------------

    public ResultSet getCrossReference(
        String primaryCatalog,
        String primarySchema,
        String primaryTable,
        String foreignCatalog,
        String foreignSchema,
        String foreignTable)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getCrossReference(" + primaryCatalog + ", " +
                    primarySchema + ", " + primaryTable + ", " +
                    foreignCatalog + ", " +    foreignSchema + ", " +
                    foreignTable + ")");
        }

        // The SimpleText driver does not support indexes.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "PKTABLE_CAT", Types.VARCHAR);
        add(columns, 2, "PKTABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "PKTABLE_NAME", Types.VARCHAR);
        add(columns, 4, "PKCOLUMN_NAME", Types.VARCHAR);
        add(columns, 5, "FKTABLE_CAT", Types.VARCHAR);
        add(columns, 6, "FKTABLE_SCHEM", Types.VARCHAR);
        add(columns, 7, "FKTABLE_NAME", Types.VARCHAR);
        add(columns, 8, "FKCOLUMN_NAME", Types.VARCHAR);
        add(columns, 9, "KEY_SEQ", Types.SMALLINT);
        add(columns, 10, "UPDATE_RULE", Types.SMALLINT);
        add(columns, 11, "DELETE_RULE", Types.SMALLINT);
        add(columns, 12, "FK_NAME", Types.VARCHAR);
        add(columns, 13, "PK_NAME", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // getTypeInfo - JDBC API
    // Get a description of all the standard SQL types supported by
    // this database. They are ordered by DATA_TYPE and then by how
    // closely the data type maps to the corresponding JDBC SQL type.
    //
    //    Each type description has the following columns:
    //
    //    (1) TYPE_NAME        String => Type name
    //    (2) DATA_TYPE        short => SQL data type from java.sql.Types
    //    (3) PRECISION        int => maximum precision
    //    (4) LITERAL_PREFIX    String => prefix used to quote a literal
    //                            (may be null)
    //    (5) LITERAL_SUFFIX    String => suffix used to quote a literal
    //                            (may be null)
    //    (6) CREATE_PARAMS    String => parameters used in creating
    //                            the type (may be null)
    //    (7) NULLABLE        short => can you use NULL for this type?
    //            typeNoNulls - does not allow NULL values
    //            typeNullable - allows NULL values
    //            typeNullableUnknown - nullability unknown
    //    (8) CASE_SENSITIVE    boolean=> is it case sensitive?
    //    (9) SEARCHABLE        short => can you use "WHERE" based on this type:
    //            typePredNone - No support
    //            typePredChar - Only supported with WHERE .. LIKE
    //            typePredBasic - Supported except for WHERE .. LIKE
    //            typeSearchable - Supported for all WHERE ..
    //    (10) UNSIGNED_ATTRIBUTE    boolean => is it unsigned?
    //    (11) FIXED_PREC_SCALE    boolean => can it be a money value?
    //    (12) AUTO_INCREMENT        boolean => can it be used for an
    //                                auto-increment value?
    //    (13) LOCAL_TYPE_NAME    String => localized version of type name
    //                                (may be null)
    //    (14) MINIMUM_SCALE        short => minimum scale supported
    //    (15) MAXIMUM_SCALE        short => maximum scale supported
    //    (16) SQL_DATA_TYPE        int => unused
    //    (17) SQL_DATETIME_SUB    int => unused
    //    (18) NUM_PREC_RADIX        int => usually 2 or 10
    //
    // Returns a ResultSet.  Each row is a SQL type description
    //-----------------------------------------------------------------------

    public ResultSet getTypeInfo()
        throws SQLException
    {
        if (traceOn()) {
            trace("@getTableTypes");
        }

        // The SimpleText driver only supports the following data types:
        //
        //    VARCHAR
        //    INTEGER
        //    VARBINARY

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TYPE_NAME", Types.VARCHAR);
        add(columns, 2, "DATA_TYPE", Types.SMALLINT);
        add(columns, 3, "PRECISION", Types.INTEGER);
        add(columns, 4, "LITERAL_PREFIX", Types.VARCHAR);
        add(columns, 5, "LITERAL_SUFFIX", Types.VARCHAR);
        add(columns, 6, "CREATE_PARAMS", Types.VARCHAR);
        add(columns, 7, "NULLABLE", Types.SMALLINT);
        add(columns, 8, "CASE_SENSITIVE", Types.BIT);
        add(columns, 9, "SEARCHABLE", Types.SMALLINT);
        add(columns, 10, "UNSIGNED_ATTRIBUTE", Types.BIT);
        add(columns, 11, "FIXED_PREC_SCALE", Types.BIT);
        add(columns, 12, "AUTO_INCREMENT", Types.BIT);
        add(columns, 13, "LOCAL_TYPE_NAME", Types.VARCHAR);
        add(columns, 14, "MINIMUM_SCALE", Types.SMALLINT);
        add(columns, 15, "MAXIMUM_SCALE", Types.SMALLINT);
        add(columns, 16, "SQL_DATA_TYPE", Types.INTEGER);
        add(columns, 17, "SQL_DATETIME_SUB", Types.INTEGER);
        add(columns, 18, "NUM_PREC_RADIX", Types.INTEGER);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create a Hashtable for a single row

        Hashtable singleRow;

        // Create the VARCHAR entry

        singleRow = new Hashtable();
        singleRow.put(new Integer(1),
                    new CommonValue(typeToName(Types.VARCHAR)));
        singleRow.put(new Integer(2), new CommonValue(Types.VARCHAR));
        singleRow.put(new Integer(3),
                    new CommonValue(SimpleTextDefine.MAX_VARCHAR_LEN));
        singleRow.put(new Integer(4), new CommonValue("'"));
        singleRow.put(new Integer(5), new CommonValue("'"));
        singleRow.put(new Integer(7), new CommonValue(typeNoNulls));
        singleRow.put(new Integer(9), new CommonValue(typePredBasic));
        singleRow.put(new Integer(13),
                    new CommonValue(typeToName(Types.VARCHAR)));
        rows.put (new Integer(1), singleRow);

        // Create the INTEGER entry

        singleRow = new Hashtable();
        singleRow.put(new Integer(1),
                    new CommonValue(typeToName(Types.INTEGER)));
        singleRow.put(new Integer(2), new CommonValue(Types.INTEGER));
        singleRow.put(new Integer(3),
                    new CommonValue(SimpleTextDefine.MAX_INTEGER_LEN));
        singleRow.put(new Integer(7), new CommonValue(typeNoNulls));
        singleRow.put(new Integer(9), new CommonValue(typePredBasic));
        singleRow.put(new Integer(13),
                    new CommonValue(typeToName(Types.INTEGER)));
        rows.put (new Integer(2), singleRow);

        // Create the VARBINARY entry

        singleRow = new Hashtable();
        singleRow.put(new Integer(1),
                    new CommonValue(typeToName(Types.VARBINARY)));
        singleRow.put(new Integer(2), new CommonValue(Types.VARBINARY));
        singleRow.put(new Integer(3),
                    new CommonValue(SimpleTextDefine.MAX_VARBINARY_LEN));
        singleRow.put(new Integer(4), new CommonValue("'"));
        singleRow.put(new Integer(5), new CommonValue("'"));
        singleRow.put(new Integer(7), new CommonValue(typeNoNulls));
        singleRow.put(new Integer(9), new CommonValue(typePredNone));
        singleRow.put(new Integer(13),
                    new CommonValue(typeToName(Types.VARBINARY)));
        rows.put (new Integer(3), singleRow);

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // typeToName
    // Return the type name for the given type
    //-----------------------------------------------------------------------

    protected String typeToName(
        int type)
    {
        String s = "";

        switch(type) {
        case Types.VARCHAR:
            s = "VARCHAR";
            break;
        case Types.INTEGER:
            s = "INTEGER";
            break;
        case Types.VARBINARY:
            s = "BINARY";
            break;
        }
        return s;
    }


    //-----------------------------------------------------------------------
    // getIndexInfo - JDBC API
    // Get a description of a table's indices and statistics. They are
    // ordered by NON_UNIQUE, TYPE, INDEX_NAME, and ORDINAL_POSITION.
    //
    // Each index column description has the following columns:
    //
    //    (1) TABLE_CAT        String => table catalog (may be null)
    //    (2) TABLE_SCHEM        String => table schema (may be null)
    //    (3) TABLE_NAME        String => table name
    //    (4) NON_UNIQUE        boolean => Can index values be non-unique?
    //                            false when TYPE is tableIndexStatistic
    //    (5) INDEX_QUALIFIER    String => index catalog (may be null);
    //                            null when TYPE is tableIndexStatistic
    //    (6) INDEX_NAME        String => index name; null when TYPE is
    //                            tableIndexStatistic
    //    (7) TYPE            short => index type:
    //            tableIndexStatistic - this identifies table statistics that are
    //                returned in conjuction with a table's index descriptions
    //            tableIndexClustered - this is a clustered index
    //            tableIndexHashed - this is a hashed index
    //            tableIndexOther - this is some other style of index
    //    (8) ORDINAL_POSITION    short => column sequence number
    //                            within index; zero when TYPE is
    //                            tableIndexStatistic
    //    (9) COLUMN_NAME        String => column name; null when TYPE is
    //                            tableIndexStatistic
    //    (10) ASC_OR_DESC    String => column sort sequence, "A" => ascending,
    //                            "D" => descending, may be null if sort
    //                            sequence is not supported;  null when TYPE
    //                            is tableIndexStatistic
    //    (11) CARDINALITY    int => When TYPE is tableIndexStatisic then
    //                            this is the number of rows in the table;
    //                            otherwise it is the number of unique values
    //                            in the index.
    //    (12) PAGES            int => When TYPE is  tableIndexStatisic then
    //                            this is the number of pages used for the
    //                            table, otherwise it is the number of pages
    //                            used for the current index.
    //    (13) FILTER_CONDITION    String => Filter condition, if any.
    //                            (may be null)
    //
    //    catalog    a catalog name; "" retrieves those without a catalog
    //    schema    a schema name pattern; "" retrieves those without a schema
    //    table    a table name
    //    unique    when true, return only indices for unique values; when false,
    //            return indices regardless of whether unique or not
    //    approximate    when true, result is allowed to reflect approximate
    //            or out of data values; when false, results are requested
    //            to be accurate
    //
    // Returns a ResultSet.  Each row is an index column description
    //-----------------------------------------------------------------------

    public ResultSet getIndexInfo(
        String catalog,
        String schema,
        String table,
        boolean unique,
        boolean approximate)
        throws SQLException
    {
        if (traceOn()) {
            trace("@getIndexInfo(" + catalog + ", " + schema + ", " +
                    table + ", " + unique + ", " + approximate + ")");
        }

        // The SimpleText driver does not support indexes.  Instead
        // of throwing a 'Driver not capable' SQLException, we'll be
        // graceful and return an empty result set

        SimpleTextStatement stmt =
                (SimpleTextStatement) ownerConnection.createStatement();

        // Create a Hashtable for all of the columns

        Hashtable columns = new Hashtable();

        add(columns, 1, "TABLE_CAT", Types.VARCHAR);
        add(columns, 2, "TABLE_SCHEM", Types.VARCHAR);
        add(columns, 3, "TABLE_NAME", Types.VARCHAR);
        add(columns, 4, "NON_UNIQUE", Types.BIT);
        add(columns, 5, "INDEX_CAT", Types.VARCHAR);
        add(columns, 6, "INDEX_NAME", Types.VARCHAR);
        add(columns, 7, "TYPE", Types.SMALLINT);
        add(columns, 8, "ORDINAL_POSITION", Types.SMALLINT);
        add(columns, 9, "COLUMN_NAME", Types.VARCHAR);
        add(columns, 10, "ASC_OR_DESC", Types.VARCHAR);
        add(columns, 11, "CARDINALITY", Types.INTEGER);
        add(columns, 12, "PAGES", Types.INTEGER);
        add(columns, 13, "FILTER_CONDITION", Types.VARCHAR);

        // Create an empty Hashtable for the rows

        Hashtable rows = new Hashtable();

        // Create the ResultSet object and return it

        SimpleTextResultSet rs = new SimpleTextResultSet();

        rs.initialize(stmt, columns, rows);

        return rs;
    }

    //-----------------------------------------------------------------------
    // add
    // Helper function used to create an in-memory column Hashtable
    //-----------------------------------------------------------------------

    protected void add(
        Hashtable h,
        int col,
        String name,
        int type)
    {
        h.put(new Integer(col), new SimpleTextColumn(name,type));
    }

    // Owning connection object

    protected SimpleTextIConnection ownerConnection;

}

