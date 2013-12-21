package com.practicalHadoop.s3client;

import java.util.List;

import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;

/*
 * Runnable class for processing multi-parts upload request
 */
public class PartUploadRunnable implements Runnable
{
	
	private byte[] data;
	private int part;
	private int size;
	private GenericS3ClientImpl invoker;
	private InitiateMultipartUploadResult initResponse;
	private List<PartETag> partETags;
	private String sourceFileFullPath;
	private FileLoadResult uploadResult;
	
	public PartUploadRunnable(byte[] data, int part, int size, GenericS3ClientImpl invoker, InitiateMultipartUploadResult initResponse,
			List<PartETag> partETags, String sourceFileFullPath, FileLoadResult uploadResult)
	{
		this.size = size;
		this.part = part;
		this.data = data;
		this.invoker = invoker;
		this.initResponse = initResponse;
		this.partETags = partETags;
		this.sourceFileFullPath = sourceFileFullPath;
		this.uploadResult = uploadResult;
	}
	
	@Override
	public void run()
	{
		try
		{
			invoker.partUpload(data, size, part, initResponse, partETags);
		}
		catch (Throwable e)
		{
			uploadResult.addFile(sourceFileFullPath);
			System.out.println("Unexpected error in a part upload " + initResponse.getKey());
			e.printStackTrace(System.out);
		}
	}
}
