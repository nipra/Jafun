package com.practicalHadoop.oozie.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

//import com.navteq.lcms.oozie.common.ResourceMngr;
//import com.navteq.lcms.oozie.common.util.LocalHelper;
//import com.navteq.lcms.oozie.common.util.PathChainFilter;
//import com.navteq.lcms.oozie.common.util.PathFilterFactory;

public class FtpExportFileHandle extends FtpHandle
{
	FtpExportFileHandle(String remoteDir, String localDir)
	{
		super(remoteDir, localDir);
	}

	public FtpCounters doFtpTransaction(String serverIP, int port, String userName, String password, String targetFileName) throws IOException
	{
		System.out.println("FtpExportFileHandle.doFtpTransaction(): on entry");
		init();
		tryConnect(serverIP, port);
		FtpCounters ftpCounters = new FtpCounters();

__codeFragment: 
		try
		{
			System.out.println("...loging in");
			if (!ftpClient.login(userName, password))
			{
				ftpClient.logout();
				error = true;
				break __codeFragment;
			}
			System.out.println("...logged in seesm ok");
			
			if (binaryTransfer)
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			ftpClient.enterLocalPassiveMode();
			
			String ftpWorkingDir = ftpClient.printWorkingDirectory();
			boolean bDirExists = ftpClient.changeWorkingDirectory(remote);
			if(bDirExists == false)
			{
				System.out.println("... remote dir does not exists: |" + remote + "|");
				error = true;
				break __codeFragment;
			}
			System.out.println("...remote dir seesm ok (no check on windows)");
			ftpClient.changeWorkingDirectory(ftpWorkingDir);

			Configuration conf = new Configuration();
			Path pPathLocal = new Path(local);
			final FileSystem fileSys = pPathLocal.getFileSystem(conf);

			PathChainFilter fileNotEmptyFltr = PathFilterFactory.create(
					PathFilterFactory.PathFilterEnum.FILE_NOT_EMPTY, 
					fileSys, null);
			
			PathChainFilter fileFltr = PathFilterFactory.create(
					PathFilterFactory.PathFilterEnum.FILE, 
					fileSys, fileNotEmptyFltr);
			
			List<Path> pathList = LocalHelper.calculateFtpPath(local, fileSys, fileFltr);
			if(pathList == null)
			{
				System.out.println("... local dir does not exists: |" + local + "|");
				error = true;
				break __codeFragment;
			}
			
			System.out.println("...pathList contains entries: " + pathList.size());
			for(Path path : pathList)
			{
				if(targetFileName == null)
					targetFileName = path.getName();
				
				String sRemoteDest = remote + "/" + targetFileName;
				System.out.println("... source-destination: " + path + " to " + sRemoteDest);
				
				InputStream input = LocalHelper.openFSInputStream(path);
				boolean bSussess = ftpClient.storeFile(sRemoteDest, input);
				
				if(bSussess)
					ftpCounters.incrementSussessCount();
				else
					ftpCounters.incrementFailureCount();
				
				String sFtpOperStatus = ftpClient.getStatus();
				System.out.println("... FTP status " + sFtpOperStatus);
					
				input.close();
			}
			ftpClient.logout();
		}
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("... server closed connection.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
        }
        finally
        {
            if (ftpClient.isConnected())
            {
                try
                {
                    ftpClient.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }
        if(error == false)
        	System.out.println("... FTP transfer done OK");
        else
        	System.out.println("... FTP transfer failed");
        
		System.out.println("FtpExportFileHandle.doFtpTransaction(): on exit");
        return ftpCounters;
	}
}
