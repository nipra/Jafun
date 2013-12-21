package com.practicalHadoop.s3client;

import java.io.File;
import java.io.FileInputStream;

import com.practicalHadoop.s3client.util.S3FileUtils;


/*
 * Runnable class for processing a single file
 */
public class FileUploadRunnable implements Runnable
{
	
	private File file;
	private GenericS3ClientImpl invoker;
	private String s3Path;
	private boolean keepSourceFileDirectory;
	private FileLoadResult uploadResult;
	
	public FileUploadRunnable(String s3Path, File file, GenericS3ClientImpl invoker, boolean keepSourceFileDirectory,
			FileLoadResult uploadResult)
	{
		this.s3Path = s3Path;
		this.file = file;
		this.invoker = invoker;
		this.keepSourceFileDirectory = keepSourceFileDirectory;
		this.uploadResult = uploadResult;
	}
	
	@Override
	public void run()
	{
		try
		{
			if (!keepSourceFileDirectory)
			{
				invoker.uploadDataStream(s3Path, invoker.convertFileName(file), file.getAbsolutePath(), new FileInputStream(file),
						file.length(), uploadResult);
			}
			else
			{
				invoker.uploadDataStream(S3FileUtils.appendFileNameToS3Path(s3Path, file.getParent()), file.getName(),
						file.getAbsolutePath(),
						new FileInputStream(file),
						file.length(), uploadResult);
			}
		}
		catch (Throwable e)
		{
			uploadResult.addFile(file.getAbsolutePath());
			System.out.println("Unexpected error in a file upload " + file.getName());
			e.printStackTrace(System.out);
		}
	}
}