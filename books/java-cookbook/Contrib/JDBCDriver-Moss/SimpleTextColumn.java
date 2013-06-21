//----------------------------------------------------------------------------
//
// Module:      SimpleTextColumn.java
//
// Description: Object that represents a single result set column
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

public class SimpleTextColumn
    extends        Object
{
    //------------------------------------------------------------------------
    // Constructor
    //------------------------------------------------------------------------

    public SimpleTextColumn(
        String name,
        int type,
        int precision)
    {
        this.name = name;
        this.type = type;
        this.precision = precision;
    }

    public SimpleTextColumn(
        String name,
        int type)
    {
        this.name = name;
        this.type = type;
        this.precision = 0;
    }

    public SimpleTextColumn(
        String name)
    {
        this.name = name;
        this.type = 0;
        this.precision = 0;
    }


    public String name;
    public int type;
    public int precision;
    public boolean searchable;
    public int colNo;
    public int displaySize;
    public String typeName;
}

