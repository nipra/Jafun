//----------------------------------------------------------------------------
//
// Module:      SimpleTextObject.java
//
// Description: Common Object base for all SimpleSelect classes
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

public class SimpleTextObject
    extends        Object
{

    //--------------------------------------------------------------------
    // traceOn
    // Returns true if tracing (logging) is currently enabled
    //--------------------------------------------------------------------

    protected static boolean traceOn()
    {
        return (DriverManager.getLogStream() != null);
    }

    //--------------------------------------------------------------------
    // trace
    // Logs the given text to the logging stream provided by the
    // DriverManager
    //--------------------------------------------------------------------

    protected static void trace(
        String text)
    {
        if (traceOn()) {
            (DriverManager.getLogStream()).println(text);
        }
    }

    //--------------------------------------------------------------------
    // DriverNotCapable
    // Create a new SQLException 'Driver not capable'
    //--------------------------------------------------------------------

    public SQLException DriverNotCapable()
    {
        return new SQLException ("Driver not capable");
    }

    //--------------------------------------------------------------------
    // DataTypeNotSupported
    // Create a new SQLException 'Data type not supported'
    //--------------------------------------------------------------------

    public SQLException DataTypeNotSupported()
    {
        return new SQLException ("Data type not supported");
    }

    //--------------------------------------------------------------------
    // parseWord
    // Given a String, get the next word from the string.  Any leading blanks
    // will be returned
    //--------------------------------------------------------------------

    public String parseWord(
        String s)
    {
        if (s.length() == 0) {
            return "";
        }

        // Flags to determine if we are in quotes

        boolean inSingle = false;
        boolean inDouble = false;
        boolean found = false;

        // Current offset in string

        int offset = 0;

        char c;

        // Special characters.  If the string starts with any of these
        // characters, it will be considered a word.  Otherwise, if any
        // of the characters are found outside quoted strings, it will
        // signify the end of a word

        String special = "()*=,? <>";

        // Skip any leading blanks

        while (s.charAt(offset) == ' ') {
            offset++;

            // No more string left.

            if (offset > s.length()) {
                return "";
            }
        }

        int si;

        // Look for special characters
        c = s.charAt(offset);

        for (si = 0; si < special.length(); si++) {
            if (c == special.charAt(si)) {
                return s.substring(0, offset + 1);
            }
        }

        // Loop while more characters exist in the string

        while (offset < s.length()) {
            c = s.charAt(offset);

            // Look for single or double quotes

            if (c == '\'') {
                if (!inDouble) {
                    if (inSingle) {
                        offset++;
                        break;
                    }
                    inSingle = true;
                }
            }

            if (c == '"') {
                if (!inSingle) {
                    if (inDouble) {
                        offset++;
                        break;
                    }
                    inDouble = true;
                }
            }


            // If not inside a double or single quote, examine the next
            // character

            if ((!inDouble) && (!inSingle)) {

                // If a special character exists, it terminates this word

                for (si = 0; si < special.length(); si++) {
                    if (c == special.charAt(si)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }

            offset++;
        }

        return s.substring(0, offset);
    }

}

