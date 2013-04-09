//----------------------------------------------------------------------------
//
// Module:      SimpleTextTable.java
//
// Description: Object that represents a single table
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

public class SimpleTextTable
    extends        Object
{
    //------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------

    public SimpleTextTable(
        String dir,
        String file)
    {
        this.dir = dir;
        this.file = file;

        // If the filename has the .SDF extension, get rid of it

        if (file.endsWith(SimpleTextDefine.DATA_FILE_EXT)) {
            name = file.substring(0, file.length() -
                        SimpleTextDefine.DATA_FILE_EXT.length());
        }
        else {
            name = file;
        }
    }

    public String dir;
    public String file;
    public String name;
}

