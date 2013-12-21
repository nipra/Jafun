package com.practicalHadoop.s3client;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Generic AWS S3 Client to encapsulate uploading/downloading data.
 *
 * WARNING: If you kill a job, you must clean-up the associated bucket.
 *
 * @author Lei Wang
 * @author Boris Lublinsky
 * @author Michael Spicuzza
 *
 */
public interface GenericS3Client
{
	/**
	 * Get the total file size for a given S3 path.
	 * 
	 * @param s3Path S3 path to count file size. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @return long total size of the files in s3Path.
	 */
	public long getFileSize(String s3Path);
	
	/**
	 * 
	 * @param s3Path S3 path to list. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param includeFileNames if true includes the filenames, otherwise just returns a count of
	 *            files.
	 * 
	 * @return FolderListing
	 */
	public FolderListing list(String s3Path, boolean includeFileNames);

	/**
	 * Gets the latest S3 path including the prefix specified in the request. The returned path is
	 * similar to a file system directory. When multiple paths match the given prefix, only the
	 * latest modified path will be returned.
	 * 
	 * <pre>
	 * For example, consider a bucket that contains the following keys: 
	 * 		"twn-1357150796/log" 
	 * 		"twn-1357150867/log" 
	 * where twn-1357150867 was created later than twn-1357150796
	 * </pre>
	 * 
	 * If calling getS3PathByMostCurrentDate with the s3PathPrefix = "s3://bucket/twn" (assume that
	 * the bucket name = "bucket"), the returned S3 path is "s3://bucket/twn-1357150867".
	 * 
	 * @param s3PathPrefix. The prefix of S3 path. e.g., s3://bucketname/keyname.
	 * @return S3 path
	 */
	public String getS3PathByMostCurrentDate(String s3PathPrefix);

	/**
	 * Check whether the S3 path exist or not.
	 * 
	 * For example, if a S3 path = s3://bucket/abc/xyz, calling doesS3PathExist with the following
	 * parameters will return True:
	 * 
	 * <pre>
	 * 		"s3://bucket" 
	 * 		"s3://bucket/abc" 
	 * 		"s3://bucket/abc/xyz"
	 * </pre>
	 * 
	 * @param s3Path S3 path to check. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @return true if the S3 path exists. Otherwise, return false.
	 */
	public boolean doesS3PathExist(String s3Path);
	
	/**
	 * Copies objects from one S3 path to another. If multiple keys exist under the given key, then
	 * they are all copied (ie copy an entire 'directory') to the destination path. The source path
	 * structure is not preserved.
	 * 
	 * @param sourcePath source S3 path to copy. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param destPath destination S3 path. e.g., s3://bucketname, s3://bucketname/keyname.
	 */
	public void copy(String sourcePath, String destPath);

	/**
	 * Download data under s3Path. The S3 path must be a full path of the file. e.g.,
	 * s3://bucketname/keyname.
	 * 
	 * @param s3Path S3 download path.
	 * 
	 * @return byte[]
	 * 
	 * @throws IOException
	 */
	public byte[] getData(String s3Path) throws IOException;

	/**
	 * Download partial data under s3Path.
	 * 
	 * @param s3Path S3 download path. The S3 path must be a full path of the file.
	 *            e.g.,s3://bucketname/keyname.
	 * @param start position to start reading.
	 * @param len how many bytes to read.
	 * 
	 * @return byte[]
	 * 
	 * @throws IOException
	 */
	public byte[] getPartialData(String s3Path, long start, long len) throws IOException;

	/**
	 * Download files under s3Path. If the S3 path contains S3 key, all files starting with the same
	 * key will be downloaded. If s3Path contains bucket name only, all files on the bucket will be
	 * downloaded.
	 * 
	 * @param s3Path S3 download path. e.g., s3://bucketname, s3://bucketname/keyname.
	 * 
	 * @param localDirectory to download to. S3 directory structure is collapsed to one local
	 *            directory.
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult downloadData(String s3Path, String localDirectory) throws IOException;

	/**
	 * Download files under s3Path. If the S3 path contains S3 key, all files starting with the same
	 * key will be downloaded. If s3Path contains bucket name only, all files on the bucket will be
	 * downloaded.
	 * 
	 * @param s3Path S3 download path. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param localDirectory to download to (keeping S3 directory structure.)
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult downloadDataFullName(String s3Path, String localDirectory) throws IOException;

	/**
	 * Upload data with sourceFileName under s3Path. If s3Path contains bucket only, the data is
	 * saved to the bucket with sourceFileName.
	 * 
	 * @param s3Path S3 path to upload to. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param sourceFileName filename.
	 * @param data
	 * @param size
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult uploadData(String s3Path, String sourceFileName, InputStream data) throws IOException;

	/**
	 * Upload data with sourceFileName under s3Path. If s3Path contains bucket only, the data is
	 * saved to the bucket with sourceFileName.
	 * 
	 * @param s3Path S3 path to upload to. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param sourceFileName filename.
	 * @param data
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult uploadData(String s3Path, String sourceFileName, InputStream data, long size) throws IOException;

	/**
	 * Upload data with sourceFileName under s3Path. If s3Path contains bucket only, the data is
	 * saved to the bucket with sourceFileName.
	 * 
	 * @param s3Path S3 path to upload to. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param sourceFileName filename.
	 * @param data
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult uploadData(String s3Path, String sourceFileName, byte[] data) throws IOException;

	/**
	 * Upload, collapsing directory structure.
	 * 
	 * @param s3Path S3 path to upload to. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param sourceFile File directory or file to upload.
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult uploadData(String s3Path, File sourceFile) throws IOException;

	/**
	 * Upload, collapsing directory structure.
	 * 
	 * @param s3Path S3 path to upload to. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param sourceFiles List<File> directory or file to upload.
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult uploadData(String s3Path, List<File> sourceFiles) throws IOException;

	/**
	 * Upload, preserving directory structure.
	 * 
	 * @param s3Path S3 path to upload to. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param sourceFile
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult uploadDataFullName(String s3Path, File sourceFile) throws IOException;

	/**
	 * Upload, preserving source directory structure.
	 * 
	 * @param s3Path S3 path to upload to. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @param sourceFiles
	 * 
	 * @return FileLoadResult
	 * 
	 * @throws IOException
	 */
	public FileLoadResult uploadDataFullName(String s3Path, List<File> sourceFiles) throws IOException;
	
	/**
	 * Create Bucket if the bucket doesn't exist.
	 * 
	 * @param bucket S3 bucket name.
	 */
	public void createBucket(String bucket);
	
	/**
	 * Delete S3 files. Unless versioning has been turned on for your bucket, there is no way to
	 * undelete a file, so use caution when deleting files.
	 * 
	 * @param s3Path S3 path to delete. e.g., s3://bucketname, s3://bucketname/keyname.
	 * @return number of files successfully deleted
	 */
	public int deleteFile(String s3Path);
	
	/**
	 * Delete Bucket if the bucket is empty.
	 * 
	 * @param bucket S3 bucket name.
	 */
	public void deleteBucket(String bucket);
	
	/**
	 * Check whether the bucket is empty or not.
	 * 
	 * @param bucket S3 bucket name.
	 * @return true if the bucket is empty. Otherwise, return false.
	 */
	public boolean isBucketEmpty(String bucket);
}