package com.practicalHadoop.s3client;

import com.practicalHadoop.s3client.FolderListing.FileDescriptor;



/*
 * Runnable class for downloading a single file
 */
public class FileDownloadRunnable implements Runnable
{
	private FileDescriptor file;
	private GenericS3ClientImpl invoker;
	private String localDirectory;
	private boolean keepS3FileDirectory;
	private FileLoadResult downloadResult;
	private String s3Bucket;
	
	public FileDownloadRunnable(String s3Bucket, String localDirectory, FileDescriptor file, GenericS3ClientImpl invoker,
			boolean keepS3FileDirectory,
			FileLoadResult downloadResult)
	{
		this.s3Bucket = s3Bucket;
		this.localDirectory = localDirectory;
		this.file = file;
		this.invoker = invoker;
		this.keepS3FileDirectory = keepS3FileDirectory;
		this.downloadResult = downloadResult;
	}
	
	@Override
	public void run()
	{
		invoker.writeDataToFile(s3Bucket, localDirectory, file, keepS3FileDirectory, downloadResult);
	}
}