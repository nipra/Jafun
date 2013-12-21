package com.practicalHadoop.oozie.ftp;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;


/**
 *local testing (from eclipse)
 *		-ftpSys=ms_places -ftpLocal=D:/tmp -ftpRemote=/users/microsoft/Places/TestOozieWF
 *		-ftpServer=ftp2.navteq.com -ftpUser=Microsoft -ftpPass=9eprEfeT -ftpLocal=D:/tmp -ftpRemote=/users/microsoft/Places/TestOozieWF
 */
public class FtpExportMngr
{
	static private String SHOW_USAGE = "usage:" +
			" -ftpSys=ftp_service_name -ftpLocal=local_dir -ftpRemote=remote_dir [-ftpFile=FL_NM] || \n" +
			" -ftpServer=server [-ftpPort=port] [-ftpBasedir=dir] -ftpUser=user -ftpPass=pass "+
			" -ftpLocal=local_dir -ftpRemote=remote_dir [-ftpFile=FL_NM]"; 
	
	static public void main(String [] args) throws IOException, ConfigurationException
	{
		long startTime = System.currentTimeMillis();
		try
		{
			if(args.length < 3)
			{
				System.out.println(SHOW_USAGE);
				throw new IllegalArgumentException("SHOW_USAGE");
			}
		}
		catch(Throwable thr)
		{
			System.out.println(thr);
			throw new RuntimeException(thr);
		}
		
		
		long endTime = System.currentTimeMillis();
		System.out.println("exec time: " + FormatMillis.millisAsString(endTime - startTime));
	}

	private FtpExportMngr(String [] args) throws ConfigurationException
	{
	}
}
