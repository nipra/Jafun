//----------------------------------------------------------------------------
//
// Module:      SimpleTextDriver.java
//
// Description: Implementation of the JDBC Driver interface
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
// The Java SQL framework allows for multiple database drivers.
//
// Each driver should supply a driver class that implements
// the Driver interface.
//
// The DriverManager will try to load as many drivers as it can
// find and then for any given connection request it will ask each
// driver in turn to try to connect to the target URL.
//
// It is strongly recommended that each Driver class should be
// small and standalone so that the Driver class can be loaded and
// queried without bringing in vast quantities of supporting code.
//
// When a Driver object is instantiated it should register itself
// with the SQL framework by calling DriverManager.registerDriver
//
// Note: Each driver must support a null constructor so it can be
// instantiated by doing:
//
//    java.sql.Driver d = Class.forName("foo.bah.Driver").newInstance();
//----------------------------------------------------------------------------
// NOTE - this is an implementation of the JDBC API version 1.20
//---------------------------------------------------------------------------

import java.sql.*;

public class SimpleTextDriver
    extends       SimpleTextObject
    implements    SimpleTextIDriver
{

    //------------------------------------------------------------------------
    // SimpleTextDriver
    // Constructor.  Attempt to register the JDBC driver
    //------------------------------------------------------------------------

    public SimpleTextDriver()
        throws SQLException
    {
        // Attempt to register this driver with the JDBC DriverManager.
        // If it fails, an exception will be thrown.

        DriverManager.registerDriver (this);
    }

    //------------------------------------------------------------------------
    // connect - JDBC API
    //
    // Try to make a database connection to the given URL.
    // The driver should return "null" if it realizes it is the wrong kind
    // of driver to connect to the given URL.  This will be common, as when
    // the JDBC driver manager is asked to connect to a given URL it passes
    // the URL to each loaded driver in turn.
    //
    // The driver should raise a SQLException if it is the right
    // driver to connect to the given URL, but has trouble connecting to
    // the database.
    //
    // The java.util.Properties argument can be used to passed arbitrary
    // string tag/value pairs as connection arguments.
    // Normally at least a "user" and "password" properties should be
    // included in the Properties.
    //
    //    url        The URL of the database to connect to
    //
    //    info    a list of arbitrary string tag/value pairs as
    //            connection arguments; normally at least a "user" and
    //            "password" property should be included
    //
    // Returns a Connection to the URL
    //------------------------------------------------------------------------

    public Connection connect(
        String url,
        java.util.Properties info)
        throws SQLException
    {
        if (traceOn()) {
            trace("@connect (url=" + url + ")");
        }

        // Ensure that we can understand the given url

        if (!acceptsURL(url)) {
            return null;
        }

        // Set the url for the driver

        driverURL = url;

        // For typical JDBC drivers, it would be appropriate to check
        // for a secure environment before connecting, and deny access
        // to the driver if it is deemed to be unsecure.  For the
        // SimpleText driver, if the environment is not secure we will
        // turn into a read-only driver.


        // Create a new SimpleTextConnection object

        SimpleTextConnection con = new SimpleTextConnection();


        // Initialize the new object

        con.initialize (this, info);

        return con;
    }

    //------------------------------------------------------------------------
    // acceptsURL - JDBC API
    //
    // Returns true if the driver thinks that it can open a connection
    // to the given URL.  Typically drivers will return true if they
    // understand the subprotocol specified in the URL and false if
    // they don't.
    //
    //    url        The URL of the database.
    //
    // Returns true if this driver can connect to the given URL.
    //------------------------------------------------------------------------

    public boolean acceptsURL(
        String url)
        throws SQLException
    {
        if (traceOn()) {
            trace("@acceptsURL (url=" + url + ")");
        }

        boolean rc = false;

        // Get the subname from the url.  If the url is not valid for
        // this driver, a null will be returned.

        if (getSubname(url) != null) {
            rc = true;
        }

        if (traceOn()) {
            trace(" " + rc);
        }
        return rc;
    }


    //------------------------------------------------------------------------
    // getPropertyInfo - JDBC API
    //
    // The getPropertyInfo method is intended to allow a generic GUI tool to
    // discover what properties it should prompt a human for in order to get
    // enough information to connect to a database.  Note that depending on
    // the values the human has supplied so far, additional values may become
    // necessary, so it may be necessary to iterate though several calls
    // to getPropertyInfo.
    //
    //    url        The URL of the database to connect to.
    //
    //    info    A proposed list of tag/value pairs that will be sent on
    //            connect open.
    //
    // Returns an array of DriverPropertyInfo objects describing possible
    //            properties.  This array may be an empty array if no properties
    //            are required.
    //------------------------------------------------------------------------

    public DriverPropertyInfo[] getPropertyInfo(
        String url,
        java.util.Properties info)
        throws SQLException
    {
        DriverPropertyInfo prop[];

        // Only one property required for the SimpleText driver; the
        // directory.  Check the property list coming in.  If the
        // directory is specified, return an empty list.

        if (info.getProperty("Directory") == null) {

            // Setup the DriverPropertyInfo entry

            prop = new DriverPropertyInfo[1];
            prop[0] = new DriverPropertyInfo("Directory", null);
            prop[0].description = "Initial text file directory";
            prop[0].required = false;

        }
        else {

            // Create an empty list

            prop = new DriverPropertyInfo[0];
        }

        return prop;
    }


    //------------------------------------------------------------------------
    // getMajorVersion - JDBC API
    //
    // Get the driver's major version number. Initially this should be 1.
    //------------------------------------------------------------------------

    public int getMajorVersion()
    {
        return SimpleTextDefine.MAJOR_VERSION;
    }

    //------------------------------------------------------------------------
    // getMinorVersion - JDBC API
    //
    // Get the driver's minor version number. Initially this should be 0.
    //------------------------------------------------------------------------

    public int getMinorVersion()
    {
        return SimpleTextDefine.MINOR_VERSION;
    }


    //------------------------------------------------------------------------
    // jdbcCompliant - JDBC API
    //
    // Report whether the Driver is a genuine JDBC COMPLIANT (tm) driver.
    // A driver may only report "true" here if it passes the JDBC compliance
    // tests, otherwise it is required to return false.
    //
    // JDBC compliance requires full support for the JDBC API and full support
    // for SQL 92 Entry Level.  It is expected that JDBC compliant drivers will
    // be available for all the major commercial databases.
    //
    // This method is not intended to encourage the development of non-JDBC
    // compliant drivers, but is a recognition of the fact that some vendors
    // are interested in using the JDBC API and framework for lightweight
    // databases that do not support full database functionality, or for
    // special databases such as document information retrieval where a SQL
    // implementation may not be feasible.
    //------------------------------------------------------------------------

    public boolean jdbcCompliant()
    {

        // The SimpleText driver is not JDBC compliant

        return false;
    }

    //------------------------------------------------------------------------
    // getSubname
    // Given a URL, return the subname.  Returns null if the protocol is
    // not 'jdbc' or the subprotocol is not 'simpletext'
    //------------------------------------------------------------------------

    public String getSubname()
    {
        return getSubname(driverURL);
    }

    public String getSubname(
        String url)
    {
        String subname = null;
        String protocol = "JDBC";
        String subProtocol = "SIMPLETEXT";

        // Convert to upper case and trim all leading and trailing
        // blanks

        url = (url.toUpperCase()).trim();

        // Make sure the protocol is jdbc:

        if (url.startsWith(protocol)) {

            // Strip off the protocol

            url = url.substring (protocol.length());

            // Look for the colon

            if (url.startsWith(":")) {
                url = url.substring(1);

                // Check the subprotocol

                if (url.startsWith (subProtocol)) {

                    // Strip off the subprotocol, leaving the subname

                    url = url.substring(subProtocol.length());

                    // Look for the colon that separates the subname
                    // from the subprotocol (or the fact that there
                    // is no subprotocol at all)

                    if (url.startsWith(":")) {
                        subname = url.substring(1);
                    }
                    else if (url.length() == 0) {
                        subname = "";
                    }
                }
            }
        }
        return subname;
    }


    //------------------------------------------------------------------------
    // getURL
    //
    // Get the URL specified for the driver
    //------------------------------------------------------------------------

    public String getURL()
    {
        return driverURL;
    }


    private String driverURL;
}

