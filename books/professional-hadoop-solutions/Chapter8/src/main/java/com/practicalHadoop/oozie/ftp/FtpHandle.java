package com.practicalHadoop.oozie.ftp;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public abstract class FtpHandle
{
	String remote = "";
	String local = "";
	FTPClient ftpClient;
	boolean error, binaryTransfer, storeFile;

	protected FtpHandle(String remote, String local)
	{
		this.remote = remote;
		this.local = local;
	}
	
	protected void init() throws IOException
	{
		ftpClient = new FTPClient();
		binaryTransfer = true;
		error = false;
		ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
	}
	
	/**
	 * 
	 * @return: -1 if error happens, otherwise the number of files being ftp-ed 
	 */
	public abstract FtpCounters doFtpTransaction(String serverIP, int port, String userName, String password, String targetFileName) throws IOException;

	
	protected void tryConnect(String server, int port)
	{
        System.out.println("FtpHandle.tryConnect(): on entry: " + server + " : " + port);
		try
		{
			if (port == -1)
				ftpClient.connect(server);
			else
				ftpClient.connect(server, port);
			
	        System.out.println("... getting reply code");
	        
	        int reply = ftpClient.getReplyCode();
	        System.out.println("...reply code - " + reply);
	        
	        if(!FTPReply.isPositiveCompletion(reply))
	        {
                ftpClient.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
	        }
	        
	        System.out.println("... seems connected");
		}
		catch(IOException e)
		{
            try
            {
            	System.out.println("FtpHandle.tryConnect(): fail with: " + e);
                ftpClient.disconnect();
            }
            catch (IOException f)
            {
                // do nothing
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            System.exit(1);
		}
        System.out.println("FtpHandle.tryConnect(): on exit");
	}
	
	//============== static section =======================
	
	static String composeFullFileName(String remoteDir, String shortFileName)
	{
		if(remoteDir == null || shortFileName == null)
			throw new IllegalArgumentException("FtpHandle.composeRemoteFilename() - null arg");
		
		boolean endOnSlash = (remoteDir.endsWith("/") ? true : false );
		
		String fullFileName = remoteDir;
		if(! endOnSlash)
			fullFileName += "/";
		
		return fullFileName + shortFileName;
	}
	
	public static class FtpCounters
	{
		int ftpOK; {ftpOK = 0;}  // for fun - instance initializer
		int ftpFAIL = 0;
		
		public FtpCounters() {}
		public String report()
		{
			return "\n\t\tOK : FAILED ratio " + "\n\t\t " + ftpOK + " : " + ftpFAIL;
		}
		
		void incrementSussessCount() {ftpOK++;}
		void incrementFailureCount() {ftpFAIL++;}
		
		int getSussessCount() { return ftpOK; }
		int getFailureCount() { return ftpFAIL; }
	}
}
