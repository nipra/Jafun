package com.practicalHadoop.s3client; 

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.elasticbeanstalk.model.S3Location;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.MultiObjectDeleteException;
import com.amazonaws.services.s3.model.MultiObjectDeleteException.DeleteError;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.practicalHadoop.aws.AWSResource;
import com.practicalHadoop.s3client.FolderListing.FileDescriptor;
import com.practicalHadoop.s3client.util.S3FileUtils;

public class GenericS3ClientImpl implements GenericS3Client
{
	private static final int ONE_HALF_SECOND = 500;
	private static final int FIVE_SECONDS = 5000;
	private static final int MAX_NUM_KEYS_FOR_BULK_DELETE = 1000;
	private static final int PARTSIZE = 50 * 1024 * 1024;
	private static final int RETRY_TIMES = 3;

	private AmazonS3Client s3Client = null;
	private FileNameConverter nameConverter = null;
	private ExecutorService multiPartExecutor = null;
	private ExecutorService executor = null;


	//==o==o==o==o==o==o==| Constructors |==o==o==o==o==o==o==//

	// without executors

	public GenericS3ClientImpl(InputStream credentialsStream)
		throws IOException
	{
		this(credentialsStream, null, null);
	}

	public GenericS3ClientImpl(File credentialsPropFile)
		throws IOException
	{
		this(credentialsPropFile, null, null);
	}

	public GenericS3ClientImpl(AWSCredentials credentials)
	{
		this(credentials, null, null);
	}

	public GenericS3ClientImpl(AWSCredentialsProvider provider)
	{
		this(provider, null, null);
	}

	// with executors

	public GenericS3ClientImpl(InputStream credentialsStream, ExecutorService executor, ExecutorService multiPartExecutor)
		throws IOException
	{
		this(new PropertiesCredentials(credentialsStream), executor, multiPartExecutor);
	}

	public GenericS3ClientImpl(File credentialsPropFile, ExecutorService executor, ExecutorService multiPartExecutor)
		throws IOException
	{
		this(AWSResource.getAWSCredentials(credentialsPropFile), executor, multiPartExecutor);
	}

	public GenericS3ClientImpl(AWSCredentials credentials, ExecutorService executor, ExecutorService multiPartExecutor)
	{
		this(new StaticCredentialsProvider(checkNotNull(credentials)), executor, multiPartExecutor);
	}

	public GenericS3ClientImpl(AWSCredentialsProvider provider, ExecutorService executor, ExecutorService multiPartExecutor)
	{
		this.s3Client = new AmazonS3Client(checkNotNull(provider, "credentials provider is required"));
		this.executor = executor;
		this.multiPartExecutor = multiPartExecutor;
	}

	//==o==o==o==o==o==o==| bucket level methods |==o==o==o==o==o==o==//
	@Override
	public void createBucket(String bucket)
	{
		checkNotNull(bucket, "bucket name must be provided");

		if (s3Client.doesBucketExist(bucket))
		{
			return;
		}

		try
		{
			s3Client.createBucket(bucket);
			System.out.println("bucket " + bucket + " created\n");
		}
		catch (AmazonServiceException ase)
		{
			printAmazonServiceException(ase, "createBucket");
			throw new RuntimeException("Fail to create bucket " + bucket, ase);
		}
		catch (AmazonClientException ace)
		{
			printAmazonClientException(ace, "createBucket");
			throw new RuntimeException("Fail to create bucket " + bucket, ace);
		}
	}

	@Override
	public boolean isBucketEmpty(String bucket)
	{
		checkNotNull(bucket, "bucket name must be provided");
		ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucket));
		return (objectListing.getObjectSummaries().size() == 0);
	}
	
	@Override
	public void deleteBucket(String bucket)
	{
		checkNotNull(bucket, "bucket name must be provided");

		if (!isBucketEmpty(bucket))
		{
			throw new IllegalArgumentException(
					"Bucket is not empty. Please delete all objects from your bucket before you try to delete the bucket.");
		}

		try
		{
			s3Client.deleteBucket(bucket);
		}
		catch (AmazonServiceException ase)
		{
			printAmazonServiceException(ase, "deleteBucket");
			throw new RuntimeException("Fail to delete bucket " + bucket, ase);
		}
		catch (AmazonClientException ace)
		{
			printAmazonClientException(ace, "deleteBucket");
			throw new RuntimeException("Fail to delete bucket " + bucket, ace);
		}
	}

	//==o==o==o==o==o==o==| delete methods |==o==o==o==o==o==o==//

	@Override
	public int deleteFile(String s3Path)
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3Path, "S3 path must be provided"));

		List<KeyVersion> keys = new ArrayList<KeyVersion>();

		int fileCount = 0;
		
		for (FileDescriptor file : list(s3Path, true).getFiles())
		{
			if (!file.getName().endsWith("/"))
			{
				keys.add(new KeyVersion(file.getName()));
				if (keys.size() == MAX_NUM_KEYS_FOR_BULK_DELETE)
				{
					fileCount += deleteMultiObjects(s3Location.getS3Bucket(), keys);
					keys.clear();
				}
			}
		}

		if (keys.size() > 0)
		{
			fileCount += deleteMultiObjects(s3Location.getS3Bucket(), keys);
			keys.clear();
		}

		return fileCount;
	}

	private int deleteMultiObjects(String bucket, List<KeyVersion> keys)
	{
		DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucket);

		multiObjectDeleteRequest.setKeys(keys);

		try
		{
			DeleteObjectsResult delObjRes = s3Client.deleteObjects(multiObjectDeleteRequest);
			System.out.format("Successfully deleted all the %s items.\n", delObjRes.getDeletedObjects().size());
			return delObjRes.getDeletedObjects().size();
		}
		catch (MultiObjectDeleteException e)
		{
			System.out.format("%s \n", e.getMessage());
			System.out.format("No. of objects successfully deleted = %s\n", e.getDeletedObjects().size());
			System.out.format("No. of objects failed to delete = %s\n", e.getErrors().size());
			System.out.format("Printing error data...\n");
			for (DeleteError deleteError : e.getErrors())
			{
				System.out.format("Object Key: %s\t%s\t%s\n", deleteError.getKey(), deleteError.getCode(), deleteError.getMessage());
			}
		}

		return 0;
	}

	//==o==o==o==o==o==o==| directory info methods |==o==o==o==o==o==o==//

	/**
	 * Get the total file size for a given S3 bucket and directory
	 * 
	 * @param s3Path S3 file path
	 * @return long the total file size
	 */
	@Override
	public long getFileSize(String s3Path)
	{
		FolderListing list = list(s3Path, true);
		
		long size = 0L;
		
		for (FileDescriptor file : list.getFiles())
		{
			if (!file.getName().endsWith("/"))
			{
				size += file.getSize();
			} 
		}
		
		return size;
	}

	@Override
	public boolean doesS3PathExist(String s3Path)
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3Path, "S3 path must be provided"));
		
		// input path contains bucket only
		if (s3Location.getS3Key().isEmpty())
		{
			return s3Client.doesBucketExist(s3Location.getS3Bucket());
		}
		
		try
		{
			ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(s3Location.getS3Bucket())
					.withPrefix(s3Location.getS3Key()));
			return (!objectListing.getObjectSummaries().isEmpty());
		}
		catch (AmazonServiceException ase)
		{
			if (ase.getErrorCode().equalsIgnoreCase("NoSuchBucket") || ase.getErrorCode().equalsIgnoreCase("NoSuchKey"))
			{
				return false;
			}
			throw new RuntimeException(ase);
		}
	}

	/**
	 * Get the object summary for a given S3 bucket and directory
	 * 
	 * @param s3Path S3 file path
	 * @param includeFileNames indicate whether to include the file name in the result list or not
	 * @return FolderListing
	 */
	@Override
	public FolderListing list(String s3Path, boolean includeFileNames)
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3Path, "S3 path must be provided"));

		FolderListing result = new FolderListing();
		ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(s3Location.getS3Bucket()).withPrefix(
				s3Location.getS3Key()));
        boolean looping = true;
        while (looping) 
        {
        	looping = objectListing.isTruncated();
        	result.addFileCount(objectListing.getObjectSummaries().size());
			if (includeFileNames)
        	{
        		for (S3ObjectSummary os : objectListing.getObjectSummaries())
        		{
        			result.addFile(new FileDescriptor(os.getKey(), os.getSize(), os.getLastModified()));
        		}
        	}
			String marker = objectListing.getNextMarker();
			if (looping)
			{
				objectListing = s3Client.listObjects(new ListObjectsRequest().withBucketName(s3Location.getS3Bucket())
						.withPrefix(s3Location.getS3Key())
						.withMarker(marker));
			}
        }
		return result;
	}

	@Override
	public String getS3PathByMostCurrentDate(String s3PathPrefix)
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3PathPrefix, "S3 path must be provided"));
		
		if (s3Location.getS3Key().isEmpty())
		{
			throw new IllegalArgumentException("Input S3 path must include the prefix of S3 key");
		}
		
		FolderListing list = list(s3PathPrefix, true);
		
		if (list.getNfiles() == 0)
		{
			throw new IllegalArgumentException("S3 path with prefix " + s3PathPrefix + " doesn't exist");
		}

		String fileName = null;
		Date fileModifiedDate = null;
		
		for (FileDescriptor file : list.getFiles())
		{
			String name = S3FileUtils.extractDirectoryStartwith(file.getName(), s3Location.getS3Key());

			if (!name.isEmpty() && (fileName == null || (!fileName.equals(name) && fileModifiedDate.before(file.getModified()))))
			{
				fileName = name;
				fileModifiedDate = file.getModified();
			}
		}

		return S3FileUtils.getS3FullPath(s3Location.getS3Bucket(), fileName);
	}

	//==o==o==o==o==o==o==| get data methods |==o==o==o==o==o==o==//

	/**
	 * Download data from S3 for a given S3 full path.
	 * 
	 * @param s3Path S3 file path
	 * @return data stream
	 * @throws IOException
	 */
	@Override
	public byte[] getData(String s3Path) throws IOException
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3Path, "S3 path must be provided"));
		return getS3ObjectContent(s3Location, new GetObjectRequest(s3Location.getS3Bucket(), s3Location.getS3Key()));
	}

	@Override
	public byte[] getPartialData(String s3Path, long start, long len) throws IOException
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3Path, "S3 path must be provided"));
		return getS3ObjectContent(s3Location,
				new GetObjectRequest(s3Location.getS3Bucket(), s3Location.getS3Key()).withRange(start, start + len - 1));
	}
	
	private byte[] getS3ObjectContent(S3Location s3Location, GetObjectRequest getObjectRequest) throws IOException
	{
		if (s3Location.getS3Key().isEmpty())
		{
			throw new IllegalArgumentException("S3 file must be provided");
		}

		byte[] bytes = null;
		S3Object object = null;
		
		try
		{
			object = s3Client.getObject(getObjectRequest);
		}
		catch (AmazonServiceException ase)
		{
			if (ase.getErrorCode().equalsIgnoreCase("NoSuchBucket"))
			{
				throw new IllegalArgumentException("Bucket " + s3Location.getS3Bucket() + " doesn't exist");
			}
			if (ase.getErrorCode().equalsIgnoreCase("NoSuchKey"))
			{
				throw new IllegalArgumentException(S3FileUtils.getS3FullPath(s3Location) + " is not a valid S3 file path");
			}
			throw new RuntimeException(ase);
		}

		if (object != null)
		{
			InputStream ois = object.getObjectContent();
			
			try
			{
				bytes = IOUtils.toByteArray(ois);
			}
			finally
			{
				if (ois != null)
				{
					ois.close();
				}
			}
		}

		return bytes;
	}

	/**
	 * Download data from S3 for a given S3 path. The download files are stored in the specified
	 * local directory regardless the file directory structure in S3.
	 * 
	 * @param s3Path S3 file path
	 * @param localDirectory local directory where the download files stored.
	 * @return download file count
	 * @throws IOException
	 */
	@Override
	public FileLoadResult downloadData(String s3Path, String localDirectory) throws IOException
	{
		return downloadData(s3Path, localDirectory, false, fileLoadResultInit(0));
	}
	
	/**
	 * Download data from S3 for a given bucket and directory. The download files are stored under
	 * the specified local directory with the same directory structure as in S3.
	 * 
	 * @param s3Path S3 file path
	 * @param localDirectory local directory where the download files stored underneath.
	 * @return download file count
	 * @throws IOException
	 */
	@Override
	public FileLoadResult downloadDataFullName(String s3Path, String localDirectory) throws IOException
	{
		return downloadData(s3Path, localDirectory, true, fileLoadResultInit(0));
	}
	
	private FileLoadResult downloadData(String s3Path, String localDirectory, boolean keepS3FileDir, FileLoadResult loadResult)
			throws IOException
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3Path, "S3 path must be provided"));
		checkNotNull(localDirectory, "local path must be provided");
		
		FolderListing list = list(s3Path, true);

		List<Future> completed = null;
		if (executor != null)
		{
			completed = new ArrayList<Future>(list.getFiles().size());
		}

		makeDirectory(localDirectory);

		if (keepS3FileDir)
		{
			for (FileDescriptor file : list.getFiles())
			{
				if (!file.getName().endsWith("/"))
				{
					String dir = S3FileUtils.extractDirectory(file.getName());
					if (dir != null)
					{
						makeDirectory(localDirectory + "/" + dir);
					}
				}
			}
		}

		for (FileDescriptor file : list.getFiles())
		{ 
			// skip directory
			if (file.getName().endsWith("/"))
			{
				continue;
			}
			
			loadResult.addNumberFilesRequested(1);

			if (executor == null)
			{
				writeDataToFile(s3Location.getS3Bucket(), localDirectory, file, keepS3FileDir, loadResult);
			}
			else
			{
				FileDownloadRunnable l = new FileDownloadRunnable(s3Location.getS3Bucket(), localDirectory, file, this, keepS3FileDir,
						loadResult);
				completed.add(submit(executor, l));
			}
		}
		
		if (executor != null)
		{
			waitForCompletion(completed);
		}
		
		loadResult.setEndTime(System.currentTimeMillis());

		return loadResult;
	}

	private void makeDirectory(String directory) throws IOException
	{
		File file = new File(directory);
		
		if (!file.exists())
		{
			if (!file.mkdirs()) {
				throw new IOException("Could not create directory: " + directory);
			}
		}
	}

	/*
	 * Copy a file to a given local directory
	 */
	public void writeDataToFile(String bucket, String localDirectory, FileDescriptor file, boolean keepS3FileDir,
			FileLoadResult downloadResult)
	{
		checkNotNull(bucket);
		checkNotNull(localDirectory);

		FileOutputStream fos = null;
		
		try
		{
			String outputPath = localDirectory + "/";
			if (keepS3FileDir)
			{
				outputPath += file.getName();
			}
			else
			{
				outputPath += S3FileUtils.extractFileName(file.getName());
			}
			
			fos = new FileOutputStream(outputPath);

			byte[] buffer = new byte[PARTSIZE];
			
			if (file.getSize() > PARTSIZE)
			{
				long startPos = 0;
				long endPos = PARTSIZE - 1;
				
				while (startPos < endPos && endPos < file.getSize())
				{
					S3Object object = s3Client.getObject(new GetObjectRequest(bucket, file.getName()).withRange(startPos, endPos));
					write(object, fos, buffer);
					
					startPos = endPos + 1;
					if ((file.getSize() - endPos) > PARTSIZE)
					{
						endPos += PARTSIZE;
					}
					else
					{
						endPos = file.getSize() - 1;
					}
				}
			}
			else if (file.getSize() > 0)
			{
				S3Object object = s3Client.getObject(new GetObjectRequest(bucket, file.getName()));
				write(object, fos, buffer);
			}
			
			downloadResult.addNumberFilesLoaded(1);
		}
		catch (Throwable e)
		{
			downloadResult.addFile(file.getName());
			System.out.println("Unexpected error in a file download " + file.getName());
			e.printStackTrace(System.out);
		}
		
		if (fos != null)
		{
			try
			{
				fos.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void write(S3Object object, FileOutputStream fos, byte[] buffer) throws IOException
	{
		if (object == null)
		{
			return;
		}

		InputStream ois = object.getObjectContent();
		int bytesRead;

		try
		{
			while ((bytesRead = ois.read(buffer, 0, PARTSIZE)) > 0)
			{
				fos.write(buffer, 0, bytesRead);
			}
		}
		finally
		{
			ois.close();
		} 
	}

	//==o==o==o==o==o==o==| put data methods |==o==o==o==o==o==o==//
	
	/**
	 * Upload data to S3.
	 * 
	 * @param s3Path S3 file path. If the S3 path doesn't exist, the path will be created.
	 * @param sourceFileName source file name
	 * @param data source data stream
	 * @param size source data size
	 * @return True if the file is successfully uploaded. Otherwise, return false.
	 * @throws IOException
	 */
	@Override
	public FileLoadResult uploadData(String s3Path, String sourceFileName, InputStream data, long size)
			throws IOException
	{
		FileLoadResult uploadResult = uploaderInit(s3Path, 1);
		uploadDataStream(s3Path, sourceFileName, sourceFileName, data, size, uploadResult);
		uploadResult.setEndTime(System.currentTimeMillis());
		return uploadResult;
	}
	
	@Override
	public FileLoadResult uploadData(String s3Path, String sourceFileName, InputStream data) throws IOException
	{
		FileLoadResult uploadResult = uploaderInit(s3Path, 1);
		uploadDataStream(s3Path, sourceFileName, sourceFileName, data, PARTSIZE + 1, uploadResult);
		uploadResult.setEndTime(System.currentTimeMillis());
		return uploadResult;
	}

	/**
	 * Upload data to S3.
	 * 
	 * @param s3Path S3 file path
	 * @param sourceFileName source file name
	 * @param data source data stream
	 * @return True if the file is successfully uploaded. Otherwise, return false.
	 * @throws IOException
	 */
	@Override
	public FileLoadResult uploadData(String s3Path, String sourceFileName, byte[] data) throws IOException
	{
		FileLoadResult uploadResult = uploaderInit(s3Path, 1);
		uploadDataStream(s3Path, sourceFileName, sourceFileName, new ByteArrayInputStream(data),
				data.length, uploadResult);
		uploadResult.setEndTime(System.currentTimeMillis());
		return uploadResult;
	}
	
	/**
	 * Upload file(s) to S3. All files will be put into the same S3 directory regardless the source
	 * file structure.
	 * 
	 * @param s3Path S3 file path.
	 * @param sourceFile source file, if the source file is a directory, all files underneath will
	 *            be uploaded to S3.
	 * @return True if all files are successfully uploaded. Otherwise, return false.
	 * @throws IOException
	 */
	@Override
	public FileLoadResult uploadData(String s3Path, File sourceFile) throws IOException
	{
		return uploadData(s3Path, Arrays.asList(sourceFile));
	}

	/**
	 * Upload a list of files to S3. All files will be put into the same S3 directory regardless the
	 * source file structure.
	 * 
	 * @param s3Path S3 file path.
	 * @param sourceFiles source files, if the source file is a directory, all files underneath will
	 *            be uploaded to S3.
	 * @return True if all files are successfully uploaded. Otherwise, return false.
	 * @throws IOException
	 */
	@Override
	public FileLoadResult uploadData(String s3Path, List<File> sourceFiles) throws IOException
	{
		FileLoadResult uploadResult = uploaderInit(s3Path, 0);
		for (File file : sourceFiles)
		{
			if (file.isDirectory())
			{
				// input is a directory
				uploadFolderData(s3Path, file, false, uploadResult);
			}
			else
			{
				// input is a file
				uploadResult.addNumberFilesRequested(1);
				uploadDataStream(s3Path, convertFileName(file), file.getAbsolutePath(), new FileInputStream(file), file.length(),
						uploadResult);
			}
		}
		
		uploadResult.setEndTime(System.currentTimeMillis());

		return uploadResult;
	}

	/**
	 * Upload file(s) to S3. The uploaded files will be stored with the same directory structure as
	 * the source files.
	 * 
	 * @param sourceFile source file.
	 * @param s3Path S3 file path.
	 * @return True if all files are successfully uploaded. Otherwise, return false.
	 * @throws IOException
	 */
	@Override
	public FileLoadResult uploadDataFullName(String s3Path, File sourceFile) throws IOException
	{
		return uploadDataFullName(s3Path, Arrays.asList(sourceFile));
	}
	
	@Override
	public FileLoadResult uploadDataFullName(String s3Path, List<File> sourceFiles) throws IOException
	{
		FileLoadResult uploadResult = uploaderInit(s3Path, 0);
		for (File file : sourceFiles)
		{
			if (file.isDirectory())
			{
				uploadFolderData(s3Path, file, true, uploadResult);
			}
			else
			{
				uploadResult.setNumberFilesRequested(1);
				uploadDataStream(S3FileUtils.appendFileNameToS3Path(s3Path, file.getParent()), file.getName(),
						file.getAbsolutePath(),
						new FileInputStream(file), file.length(), uploadResult);
			}
		}
		
		uploadResult.setEndTime(System.currentTimeMillis());

		return uploadResult;
	}

	/*
	 * Upload data stream to S3. If the file size is larger than 50MB, upload the file via
	 * multi-parts request.
	 */
	void uploadDataStream(String s3Path, String sourceFileName, String sourceFileFullPath, InputStream data, long size,
			FileLoadResult uploadResult)
	{
		String s3FullPath = S3FileUtils.appendFileNameToS3Path(s3Path, sourceFileName);
		S3Location s3Location = S3FileUtils.getS3Location(s3FullPath);
		
		if (size > PARTSIZE)
		{
			multiPartUploader(sourceFileFullPath, s3Location, data, uploadResult);
		}
		else
		{
			singlePartUploader(sourceFileFullPath, s3Location, data, size, uploadResult);
		}
		
		System.out.println("Upload file: "
				+ S3FileUtils.getS3FullPath(s3Location)
				+ ", number files loaded: "
				+ uploadResult.getNumberFilesLoaded());
	}
	
	/**
	 * For a given file directory, get all files underneath and upload them to S3.
	 * 
	 * @param s3Path S3 file path.
	 * @param sourceFile source file directory.
	 * @param keepSourceFileDirectory indicates whether to retain the source file directory or not
	 *            in S3.
	 * @return int file count
	 * @throws IOException
	 */
	private int uploadFolderData(String s3Path, File sourceFile, boolean keepSourceFileDirectory, FileLoadResult uploadResult)
			throws IOException
	{
		Collection<File> sourceFileList = FileUtils.listFiles(sourceFile, null, true);
		uploadResult.addNumberFilesRequested(sourceFileList.size());
		
		List<Future> completed = null;
		if (executor != null)
		{
			completed = new ArrayList<Future>(sourceFileList.size());
		}
		 
		for (File file : sourceFileList)
		{
			if (executor == null)
			{
				if (!keepSourceFileDirectory)
				{
					uploadDataStream(s3Path, convertFileName(file), file.getAbsolutePath(), new FileInputStream(file), file.length(),
							uploadResult);
				}
				else
				{
					uploadDataStream(S3FileUtils.appendFileNameToS3Path(s3Path, file.getParent()), file.getName(),
							file.getAbsolutePath(), new FileInputStream(file), file.length(),
							uploadResult);
				}
			}
			else
			{
				FileUploadRunnable l = new FileUploadRunnable(s3Path, file, this, keepSourceFileDirectory, uploadResult);
				completed.add(submit(executor, l));
			}
		}

		if (executor != null)
		{
			waitForCompletion(completed);
		}
		
		return sourceFileList.size();
	}

	//==o==o==o==o==o==o==| copy data methods |==o==o==o==o==o==o==//
	@Override
	public void copy(String sourcePath, String destPath)
	{
		final S3Location s3SourceLocation = S3FileUtils.getS3Location(checkNotNull(sourcePath, "S3 source path must be provided"));
		final S3Location s3DestLocation = S3FileUtils.getS3Location(checkNotNull(destPath, "S3 destination path must be provided"));

		if (s3SourceLocation.getS3Bucket().equals(s3DestLocation.getS3Bucket())
				&& s3SourceLocation.getS3Key().equals(s3DestLocation.getS3Key()))
		{
			throw new IllegalArgumentException("Source and destination are identical.");
		}

		for (final FileDescriptor file : list(sourcePath, true).getFiles())
		{
			// skip directories
			if (file.getName().endsWith("/"))
			{
				continue;
			}

			final String destKey = (s3DestLocation.getS3Key().isEmpty() ? S3FileUtils.extractFileName(file.getName()) : (s3DestLocation
					.getS3Key() + "/" + S3FileUtils.extractFileName(file.getName())));

			if (executor != null)
			{
				submit(executor, (new Runnable()
				{
					public @Override void run() {
						copyFile(s3SourceLocation.getS3Bucket(), file.getName(), s3DestLocation.getS3Bucket(), destKey);
					}
				}));
			}
			else
			{
				copyFile(s3SourceLocation.getS3Bucket(), file.getName(), s3DestLocation.getS3Bucket(), destKey);
			}
		}
	}

	private void copyFile(String sourceBucket, String sourceKey, String destBucket, String destKey)
	{
		CopyObjectRequest request = new CopyObjectRequest(sourceBucket, sourceKey, destBucket, destKey);

		try
		{
			s3Client.copyObject(request);
		}
		catch (AmazonClientException ex)
		{
			System.err.println("error while copying a file in S3");
			ex.printStackTrace();
		}
	}

	private Future submit(ExecutorService executorService, Runnable runnable)
	{
		while (true)
		{
			try
			{
				return executorService.submit(runnable);
			}
			catch (RejectedExecutionException e)
			{
				try
				{
					Thread.sleep(FIVE_SECONDS);
				}
				catch (InterruptedException e1)
				{
					throw new RuntimeException("Failed in submit request", e);
				}
			}
		}
	}

	/**
	 * Reset the count and list.
	 */
	private FileLoadResult fileLoadResultInit(int fileCount)
	{
		FileLoadResult fileLoadResult = new FileLoadResult();
		fileLoadResult.clear();
		fileLoadResult.setNumberFilesRequested(fileCount);
		fileLoadResult.setStartTime(System.currentTimeMillis());
		return fileLoadResult;
	}
	
	private FileLoadResult uploaderInit(String s3Path, int fileCount)
	{
		S3Location s3Location = S3FileUtils.getS3Location(checkNotNull(s3Path, "S3 path must be provided"));
		createBucket(s3Location.getS3Bucket());
		return fileLoadResultInit(fileCount);
	}

	/**
	 * Convert file name using custom name converter.
	 * 
	 * @param file
	 * @return String file name
	 */
	String convertFileName(File file)
	{
		if (nameConverter != null)
		{
			return nameConverter.convertName(file.getName());
		}
		else
		{
			return file.getName();
		}
	}

	private void multiPartUploader(String sourceFileFullPath, S3Location s3Location, InputStream is, FileLoadResult uploadResult)
	{
		List<PartETag> partETags = new ArrayList<PartETag>();
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(s3Location.getS3Bucket(), s3Location.getS3Key());
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
		List<Future> completed = null;
		if (multiPartExecutor != null)
		{
			completed = new ArrayList<Future>();
		}
		
		int uploadPart = 1;
		
		try
		{
			while (true)
			{
				byte[] data = new byte[PARTSIZE];
				
				int read = is.read(data);
				
				if (read > 0)
				{
					if (multiPartExecutor == null)
					{
						partUpload(data, read, uploadPart++, initResponse, partETags);
					}
					else
					{
						PartUploadRunnable pl = new PartUploadRunnable(data, uploadPart++, read, this, initResponse, partETags, sourceFileFullPath,
								uploadResult);
						completed.add(submit(multiPartExecutor, pl));
					}
				}
				else
				{
					break;
				}
			}
		}
		catch (Throwable e)
		{
			uploadResult.addFile(sourceFileFullPath);
			System.out.println("Unexpected error in multi part upload " + initResponse.getKey());
			e.printStackTrace(System.out);
		}

		if (multiPartExecutor != null)
		{
			waitForCompletion(completed);
		}

		// If any part is failed, call abortMultipartUploadRequest to free resources
		if (uploadResult.getFailedFileAbsolutePathList().contains(sourceFileFullPath))
		{
			abortMultipartUploadRequest(initResponse, s3Location.getS3Key());
		}
		else
		{
			completeMultipartUpload(initResponse, partETags, s3Location.getS3Key(), sourceFileFullPath, uploadResult);
		}
		
		try
		{
			is.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void completeMultipartUpload(InitiateMultipartUploadResult initResponse, List<PartETag> partETags, String file,
			String sourceFileFullPath,
			FileLoadResult uploadResult)
	{
		CompleteMultipartUploadRequest compRequest = new
		CompleteMultipartUploadRequest(initResponse.getBucketName(),
				initResponse.getKey(),initResponse.getUploadId(),partETags);
		
		// re-try 3 times when uploading fails
		for (int i = 1; i <= RETRY_TIMES; i++)
		{
			try
			{
				s3Client.completeMultipartUpload(compRequest);
				uploadResult.addNumberFilesLoaded(1);
				break;
			}
			catch (AmazonServiceException ase)
			{
				if (i >= RETRY_TIMES)
				{
					amazonServiceException("completeMultipartUpload", ase, file, sourceFileFullPath, uploadResult);
					abortMultipartUploadRequest(initResponse, file);
				}
			}
			catch (AmazonClientException ace)
			{
				if (i >= RETRY_TIMES)
				{
					amazonClientException("completeMultipartUpload", ace, file, sourceFileFullPath, uploadResult);
					abortMultipartUploadRequest(initResponse, file);
				}
			}
		}
	}

	private void abortMultipartUploadRequest(InitiateMultipartUploadResult result, String fname)
	{
		AbortMultipartUploadRequest abortRequest = new AbortMultipartUploadRequest(result.getBucketName(), result.getKey(),
				result.getUploadId());
		
		for (int i = 1; i <= RETRY_TIMES; i++)
		{
			try
			{
				s3Client.abortMultipartUpload(abortRequest);
				break;
			}
			catch (AmazonServiceException ase)
			{
				if (i >= RETRY_TIMES)
				{
					amazonServiceException("abortMultipartUpload", ase, fname, null, null);
				}
			}
			catch (AmazonClientException ace)
			{
				if (i >= RETRY_TIMES)
				{
					amazonClientException("abortMultipartUpload", ace, fname, null, null);
				}
			}
		}
	}

	private void singlePartUploader(String sourceFileFullPath, S3Location s3Location, InputStream data, long size,
			FileLoadResult uploadResult)
	{
		ObjectMetadata om = new ObjectMetadata();
		om.setContentLength(size);
		
		PutObjectRequest request = new PutObjectRequest(s3Location.getS3Bucket(), s3Location.getS3Key(), data, om);
		
		// re-try 3 times when uploading fails
		for (int i = 1; i <= RETRY_TIMES; i++)
		{
			try
			{
				s3Client.putObject(request);
				uploadResult.addNumberFilesLoaded(1);
				break;
			}
			catch (AmazonServiceException ase)
			{
				if (i >= RETRY_TIMES)
				{
					amazonServiceException("putObject", ase, s3Location.getS3Key(), sourceFileFullPath, uploadResult);
				}
			}
			catch (AmazonClientException ace)
			{
				if (i >= RETRY_TIMES)
				{
					amazonClientException("putObject", ace, s3Location.getS3Key(), sourceFileFullPath, uploadResult);
				}
			}
		}
		
		try
		{
			data.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void amazonServiceException(String awsMethodName, AmazonServiceException ase, String fileName, String sourceFileFullPath,
			FileLoadResult uploadResult)
	{
		if (uploadResult != null && sourceFileFullPath != null)
		{
			uploadResult.addFile(sourceFileFullPath);
		}

		printAmazonServiceException(ase, awsMethodName);
		System.out.println("Upload file " + fileName + " failed.");
	}
	
	private void amazonClientException(String awsMethodName, AmazonClientException ace, String fileName, String sourceFileFullPath,
			FileLoadResult uploadResult)
	{
		if (uploadResult != null && sourceFileFullPath != null)
		{
			uploadResult.addFile(sourceFileFullPath);
		}
		
		printAmazonClientException(ace, awsMethodName);
		
		System.out.println("Upload file " + fileName + " failed.");
	}
	
	private void printAmazonServiceException(AmazonServiceException ase, String awsMethodName)
	{
		System.out.println("Caught an AmazonServiceException while calling "
				+ awsMethodName
				+ ", which means your request made it "
				+ "to Amazon S3, but was rejected with an error.");
		System.out.println("Error Message: " + ase.getMessage());
		System.out.println("HTTP Status Code: " + ase.getStatusCode());
		System.out.println("AWS Error Code: " + ase.getErrorCode());
		System.out.println("Error Type: " + ase.getErrorType());
		System.out.println("Request ID: " + ase.getRequestId());
	}
	
	private void printAmazonClientException(AmazonClientException ace, String awsMethodName)
	{
		System.out.println("Caught an AmazonClientException while calling "
				+ awsMethodName
				+ ", which "
				+ "means the client encountered "
				+ "an internal error while trying to communicate with S3");
		System.out.println("Error Message: " + ace.getMessage());
	}

	private void waitForCompletion(List<Future> completed){
		boolean done = false; 
				 
		while (!done)
		{
			try 
			{
				Thread.sleep(ONE_HALF_SECOND);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			
			int cthreads = 0;
			for (Future f : completed)
			{
				try 
				{
					if (f.get(1, TimeUnit.MILLISECONDS) == null)
					{
						cthreads ++;
					}
					else
					{
						break;
					}
				}
				catch (Exception e)
				{ 
					break;
				}
			}
			done = (cthreads == completed.size()); 
		} 
	}
	
	void partUpload(byte[] data, int size, int part,
			InitiateMultipartUploadResult initResponse, List<PartETag> partETags){
		UploadPartRequest uploadRequest = new UploadPartRequest()
		.withBucketName(initResponse.getBucketName()).withKey(initResponse.getKey())
		.withUploadId(initResponse.getUploadId()).withPartNumber(part)
		.withInputStream(new ByteArrayInputStream(data))
		.withPartSize(size);
		// Upload part and add response to our list.
		partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());
	}

	//---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---o---//
	
	public ExecutorService getMultiPartExecutor()
	{
		return multiPartExecutor;
	}

	public void setMultiPartExecutor(ExecutorService multiPartExecutor)
	{
		this.multiPartExecutor = multiPartExecutor;
	}
	
	public ExecutorService getExecutor()
	{
		return executor;
	}
	
	public void setExecutor(ExecutorService executor)
	{
		this.executor = executor;
	}
	
	public FileNameConverter getNameConverter()
	{
		return nameConverter;
	}
	
	public void setNameConverter(FileNameConverter nameConverter)
	{
		this.nameConverter = nameConverter;
	}
}