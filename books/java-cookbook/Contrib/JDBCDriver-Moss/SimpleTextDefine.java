//----------------------------------------------------------------------------
//
// Module:      SimpleTextDefine.java
//
// Description: Static defines for all SimpleSelect classes
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

import java.sql.*;

public class SimpleTextDefine
{
    //------------------------------------------------------------------------
    // Version numbers
    //------------------------------------------------------------------------

    public final static int MAJOR_VERSION = 1;
    public final static int MINOR_VERSION = 0200;

    //------------------------------------------------------------------------
    // Maximums
    //------------------------------------------------------------------------


    public final static int MAX_COLUMN_NAME_LEN        = 18;
    public final static int MAX_COLUMNS_IN_TABLE    = 80;
    public final static int MAX_CATALOG_NAME_LEN    = 128;
    public final static int MAX_TABLE_NAME_LEN        = 40;
    public final static int MAX_VARCHAR_LEN            = 5120;
    public final static int MAX_INTEGER_LEN            = 9;
    public final static int MAX_VARBINARY_LEN        = 1048576;

    //------------------------------------------------------------------------
    // SQL statement types
    //------------------------------------------------------------------------

    public final static int SQL_SELECT    = 1;
    public final static int SQL_INSERT    = 2;
    public final static int SQL_CREATE    = 3;
    public final static int SQL_DROP    = 4;

    //------------------------------------------------------------------------
    // Column name prefixes indicating type
    //------------------------------------------------------------------------

    public final static String COL_TYPE_NUMBER = "#";
    public final static String COL_TYPE_BINARY = "@";

    //------------------------------------------------------------------------
    // File extensions
    //------------------------------------------------------------------------

    public final static String DATA_FILE_EXT    = ".SDF";
    public final static String BINARY_FILE_EXT    = ".SBF";
}

