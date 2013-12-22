package com.packt.maven.dependency.projectFromAnt;

import it.sauronsoftware.ftp4j.FTPConnector;
import it.sauronsoftware.ftp4j.connectors.FTPProxyConnector;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA "Leda" 12 CE.
 * User: Jonathan LALOU
 * Time: 13:38
 */
public class AnyClass {
    public void anyMethod() {
        StringUtils.isAlphanumericSpace("ProximaCentauri");
        final GnuParser gnuParser = new GnuParser();
        final FTPConnector ftpConnector = new FTPProxyConnector("http://", 1234);
    }
}
