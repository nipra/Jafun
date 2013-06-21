//----------------------------------------------------------------------------
//
// Module:      SimpleTextIConnection.java
//
// Description: Interface of the SimpleTextConnection class.  This class
//              resolves circular dependancies.
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

import java.util.Hashtable;

public interface SimpleTextIConnection
    extends        java.sql.Connection
{
    String[] parseSQL(String sql);
    Hashtable getTables(String directory, String table);
    Hashtable getColumns(String directory, String table);
    String getDirectory(String directory);
}

