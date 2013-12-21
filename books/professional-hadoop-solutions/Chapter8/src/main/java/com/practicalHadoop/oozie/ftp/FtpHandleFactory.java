package com.practicalHadoop.oozie.ftp;

public class FtpHandleFactory
{
	public static FtpHandle create(String remote, String local)
	{
		return new FtpExportFileHandle(remote, local);
	}
}
