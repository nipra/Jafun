//----------------------------------------------------------------------------
//
// Module:      SimpleTextInputStream.java
//
// Description: InputStream wrapper for CommonValue objects
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
import java.io.*;

public class SimpleTextInputStream
    extends        InputStream
{

    //--------------------------------------------------------------------
    // Constructor
    // Perform any necessary initialization
    //--------------------------------------------------------------------

    public SimpleTextInputStream(
        CommonValue value,
        int type,
        int length)
    {
        this.value = value;
        this.type = type;
        this.length = length;
    }

    //--------------------------------------------------------------------
    // read
    // Read a single byte of data.  Returns the byte read, or -1 when the
    // end of the stream is reached
    //--------------------------------------------------------------------

    public int read()
        throws IOException
    {
        int  singleByte;
        byte buffer[];

        // Allocate a single byte buffer

        buffer = new byte[1];

        // Read a single byte of data

        singleByte = read(buffer);

        // If not end of data, return the byte read

        if (singleByte != -1) {
            singleByte = buffer[0];
        }

        return singleByte;
    }

    //--------------------------------------------------------------------
    // read
    // Reads data into an array of bytes.  Returns the number of bytes
    // read, or -1 when end of data is reached
    //--------------------------------------------------------------------

    public synchronized int read(
        byte b[])
        throws IOException
    {
        int toRead = b.length;

        // If the value given is null, return end of data

        if (value == null) {
            return -1;
        }

        if (value.isNull()) {
            return -1;
        }

        // Calculate the number of bytes to return

        int copyBytes = length - currentOffset;

        // Is there more data to get?

        if (copyBytes <= 0) {
            return -1;
        }

        // Only copy up to the size of the given buffer

        if (copyBytes > toRead) {
            copyBytes = toRead;
        }

        // Get the data as a byte array

        byte data[];

        try {
            data = value.getBytes();
        }
        catch (SQLException ex) {
            return -1;
        }

        // Copy the data.  The type of InputStream should be checked.
        // We'll assume binary data for the SimpleText driver

        System.arraycopy(data, currentOffset, b, 0, copyBytes);

        // Increment our offset

        currentOffset += copyBytes;

        // Return the number of bytes copied

        return copyBytes;
    }

    //--------------------------------------------------------------------
    // available
    // Returns the number of bytes that can be read without blocking.
    //--------------------------------------------------------------------

    public int available()
        throws IOException
    {
        int bytes = 0;

        if (length > 0) {
            bytes = length - currentOffset;
        }
        return bytes;
    }


    // CommonValue object containing the data

    protected CommonValue value;

    // InputStream type

    protected int type;

        public final static int STREAM_TYPE_ASCII    = 1;
        public final static int STREAM_TYPE_UNICODE    = 2;
        public final static int STREAM_TYPE_BINARY    = 3;

    // Total length of data, or -1 if not known

    protected int length;

    // Current offset in the value

    protected int currentOffset;
}

