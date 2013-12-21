package com.practicalHadoop.oozie.ftp;

import java.io.PrintWriter;
import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;

public class PrintCommandListener implements ProtocolCommandListener
{
    private PrintWriter __writer;

    public PrintCommandListener(PrintWriter writer)
    {
        __writer = writer;
    }

    public void protocolCommandSent(ProtocolCommandEvent event)
    {
        __writer.print(event.getMessage());
        __writer.flush();
    }

    public void protocolReplyReceived(ProtocolCommandEvent event)
    {
        __writer.print(event.getMessage());
        __writer.flush();
    }
}
