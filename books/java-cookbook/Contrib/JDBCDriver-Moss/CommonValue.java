//----------------------------------------------------------------------------
//
// Module:      CommonValue.java
//
// Description: Object that represents a single data value.  All data
//              coercion is handled here
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

public class CommonValue
    extends         Object
{
    //------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------

    public CommonValue()
    {
        data = null;
    }

    public CommonValue(String s)
    {
        data = (Object) s;
        internalType = Types.VARCHAR;
    }

    public CommonValue(int i)
    {
        data = (Object) new Integer(i);
        internalType = Types.INTEGER;
    }

    public CommonValue(Integer i)
    {
        data = (Object) i;
        internalType = Types.INTEGER;
    }

    public CommonValue(byte b[])
    {
        data = (Object) b;
        internalType = Types.VARBINARY;
    }

    //------------------------------------------------------------------------
    // isNull
    // returns true if the value is null
    //------------------------------------------------------------------------

    public boolean isNull()
    {
        return (data == null);
    }

    //------------------------------------------------------------------------
    // getMethods
    //------------------------------------------------------------------------

    // Attempt to convert the data into a String.  All data types
    // should be able to be converted

    public String getString()
        throws SQLException
    {
        String s;

        // A null value always returns null

        if (data == null) {
            return null;
        }

        switch(internalType) {

        case Types.VARCHAR:
            s = (String) data;
            break;

        case Types.INTEGER:
            s = ((Integer) data).toString();
            break;

        case Types.VARBINARY:
            {
                // Convert a byte array into a String of hex digits

                byte b[] = (byte[]) data;
                int len = b.length;
                String digits = "0123456789ABCDEF";
                char c[] = new char[len * 2];

                for (int i = 0; i < len; i++) {
                    c[i * 2] = digits.charAt((b[i] >> 4) & 0x0F);
                    c[(i * 2) + 1] = digits.charAt(b[i] & 0x0F);
                }
                s = new String(c);
            }


            break;

        default:
            throw new SQLException("Unable to convert data type to String: " +
                                internalType);
        }

        return s;
    }

    // Attempt to convert the data into an int

    public int getInt()
        throws SQLException
    {
        int i = 0;

        // A null value always returns zero

        if (data == null) {
            return 0;
        }

        switch(internalType) {

        case Types.VARCHAR:
            i = (Integer.valueOf((String) data)).intValue();
            break;

        case Types.INTEGER:
            i = ((Integer) data).intValue();
            break;

        default:
            throw new SQLException("Unable to convert data type to String: " +
                                internalType);
        }

        return i;
    }

    // Attempt to convert the data into a byte array

    public byte[] getBytes()
        throws SQLException
    {
        byte b[] = null;

        // A null value always returns null

        if (data == null) {
            return null;
        }

        switch(internalType) {

        case Types.VARCHAR:
            {
                // Convert the String into a byte array.  The String must
                // contain an even number of hex digits

                String s = ((String) data).toUpperCase();
                String digits = "0123456789ABCDEF";
                int len = s.length();
                int index;

                if ((len % 2) != 0) {
                    throw new SQLException(
                            "Data must have a even number of hex digits");
                }

                b = new byte[len / 2];

                for (int i = 0; i < (len / 2); i++) {
                    index = digits.indexOf(s.charAt(i * 2));

                    if (index < 0) {
                        throw new SQLException("Invalid hex digit");
                    }

                    b[i] = (byte) (index << 4);

                    index = digits.indexOf(s.charAt((i * 2) + 1));

                    if (index < 0) {
                        throw new SQLException("Invalid hex digit");
                    }

                    b[i] += (byte) index;

                }
            }
            break;

        case Types.VARBINARY:
            b = (byte[]) data;
            break;

        default:
            throw new SQLException("Unable to convert data type to byte[]: " +
                                internalType);
        }

        return b;
    }

    protected Object data;
    protected int internalType;

}

