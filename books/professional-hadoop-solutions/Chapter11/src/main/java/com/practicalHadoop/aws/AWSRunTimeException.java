package com.practicalHadoop.aws;

public class AWSRunTimeException extends RuntimeException
{
	
	private static final long serialVersionUID = 1L;
	
	public AWSRunTimeException()
	{
	}
	
	public AWSRunTimeException(String message)
	{
		super(message);
	}
	
	public AWSRunTimeException(Throwable cause)
	{
		super(cause);
	}
	
	public AWSRunTimeException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
}
