package com.practicalHadoop.oozieext;

import java.io.IOException;

import org.apache.oozie.ErrorCode;
import org.apache.oozie.action.ActionExecutor;
import org.apache.oozie.action.ActionExecutorException;
import org.apache.oozie.action.ActionExecutorException.ErrorType;
import org.apache.oozie.client.WorkflowAction;
import org.apache.oozie.util.XmlUtils;
import org.jdom.Element;
import org.jdom.Namespace;

import com.practicalHadoop.oozie.ftp.FtpHandle;
import com.practicalHadoop.oozie.ftp.FtpHandleFactory;


public class FtpExecutor extends ActionExecutor
{
    private final static String SERVER_IP = "serverIP";
    private final static String PORT = "port";
    private final static String USER = "user";
    private final static String PASSWORD = "password";
    private final static String TARGET_FILE = "targetFile";
    private final static String REMOTE_DIR = "remoteDir";
    private final static String LOCALE_DIR = "localeDir";

    private static final String NODENAME = "ftp";
    private static final String SUCCEEDED = "OK";
    private static final String FAILED = "FAIL";
    private static final String KILLED = "KILLED";
    
    FtpExecutor()
    {
        super(NODENAME);
    }
    
    @Override
    public void initActionType() 
    {
        super.initActionType();
    }
    
    @Override
    public void check(Context arg0, WorkflowAction arg1) throws ActionExecutorException
    {
     // Should not be called for synch operation
        throw new UnsupportedOperationException();
    }

    @Override
    public void end(Context context, WorkflowAction action) throws ActionExecutorException
    {
        String externalStatus = action.getExternalStatus();
        WorkflowAction.Status status = externalStatus.equals(SUCCEEDED) ?
               WorkflowAction.Status.OK : WorkflowAction.Status.ERROR;
        context.setEndData(status, getActionSignal(status));    
    }
    

    @Override
    public boolean isCompleted(String arg0)
    {
        return true;
    }

    @Override
    public void kill(Context context, WorkflowAction action) throws ActionExecutorException
    {
        context.setExternalStatus(KILLED);
        context.setExecutionData(KILLED, null);
    }

    @Override
    public void start(Context context, WorkflowAction action) throws ActionExecutorException
    {
        try
        {
            Element actionXml = XmlUtils.parseXml(action.getConf());
            validateAndFtp(context, actionXml);
            context.setExecutionData("OK", null);
        }
        catch(Exception ex)
        {
            context.setExecutionData(FAILED, null);
            throw new ActionExecutorException(ErrorType.FAILED, 
                    ErrorCode.E0000.toString(), ex.getMessage());
        }
    }

    private void validateAndFtp(Context context, Element actionXml) throws ActionExecutorException
    {
        Namespace ns = Namespace.getNamespace("uri:oozie:ftp-action:0.1");
        
        String serverIP = getCheckNotNull(actionXml, SERVER_IP, "FTP001", "No server IP was specified.");
        int port;
        String sPort = getCheckNotNull(actionXml, PORT, "FTP002", "No port was specified.");
        try {
            port = Integer.parseInt(sPort);
        }
        catch(NumberFormatException nfe) {
            throw new ActionExecutorException(ErrorType.ERROR, "FTP003", "Invalid value for port was specified.");
        }
        
        String userName = getCheckNotNull(actionXml, USER, "FTP004", "No user was specified.");
        String password = getCheckNotNull(actionXml, PASSWORD, "FTP005", "No password was specified.");
        
        // Optional
        String localFileName;
        try {
            localFileName = actionXml.getChildTextTrim(TARGET_FILE, ns);; 
        } 
        catch(Exception ex) {
            localFileName = null;
        }
        
        String remoteDir = getCheckNotNull(actionXml, REMOTE_DIR, "FTP006", "No remote directory was specified.");
        String localeDir = getCheckNotNull(actionXml, LOCALE_DIR, "FTP007", "No locale directory was specified.");
        
        try {
            doFtp(serverIP, port, userName, password, localFileName, remoteDir, localeDir, localFileName);
        }
        catch(IOException ioe)
        {
            throw new ActionExecutorException(ErrorType.ERROR, "FTP008", "FTP failse. " + ioe);
        }
        
    }
    
    private String getCheckNotNull(Element actionXml, String tagName, String actionErrID, String actionErrMsg) throws ActionExecutorException
    {
        Namespace ns = Namespace.getNamespace("uri:oozie:ftp-action:0.1");
        String tmp = actionXml.getChildTextTrim(tagName, ns);
        if(tmp == null)
            throw new ActionExecutorException(ErrorType.ERROR, actionErrID, actionErrMsg);
        
        return tmp;            
    }
    
    private void doFtp(String serverIP, int port, String userName, String password,
            String targetFileName, String remoteDir, String localeDir, String localFileName) throws IOException
    {
        FtpHandle ftpHandle = FtpHandleFactory.create(remoteDir, localeDir);
        ftpHandle.doFtpTransaction(serverIP, port, userName, password, targetFileName);
    }
}
