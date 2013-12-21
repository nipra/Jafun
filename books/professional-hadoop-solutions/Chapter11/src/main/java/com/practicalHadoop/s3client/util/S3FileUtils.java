package com.practicalHadoop.s3client.util;

import java.util.StringTokenizer;

import com.amazonaws.services.elasticbeanstalk.model.S3Location;

public final class S3FileUtils
{
	private static final String S3PATH_PREFIX = "s3://";

	private S3FileUtils()
	{
	}

	// Build a S3 file path. If the file directory is specified, the path = directory + file name. Otherwise, the path = file name 
	public static String appendFileNameToS3Path(String s3Path, String fileName)
	{
		S3Location s3Location = getS3Location(s3Path);
		
		if (fileName != null && !fileName.isEmpty())
		{
			String appendFileName = removeInvalidChar(fileName);

			if (s3Location.getS3Key().isEmpty())
			{
				s3Location.setS3Key(appendFileName);
			}
			else if (!appendFileName.isEmpty())
			{
				s3Location.setS3Key(s3Location.getS3Key() + "/" + appendFileName);
			}
		}

		return getS3FullPath(s3Location);
	}

	// remove driver name (e.g., c:), leading '/' from the directory/file name
	public static String removeInvalidChar(String name)
	{
		int pos = name.indexOf(':');

		if (pos >= 0 || name.charAt(0) == '\\' || name.charAt(0) == '/')
		{
			for (int i = pos + 1; i < name.length(); i++)
			{
				if (name.charAt(i) != '/' && name.charAt(i) != '\\')
				{
					return name.substring(i).replace('\\', '/');
				}
			}
			return "";
		}
		
		return name.replace('\\', '/');
	}
	
	/*
	 * Extract the directory string from a given full path.
	 */
	public static String extractDirectory(String path)
	{
		int index = findSlash(path);
		
		if (index > 0)
		{
			return path.substring(0, index);
		}
		else
		{
			return null;
		}
	}

	private static int findSlash(String path)
	{
		int index = path.lastIndexOf('/');
		if (index <= 0)
		{
			index = path.lastIndexOf('\\');
		}
		return index;
	}

	/*
	 * Extract the file name from a given full path.
	 */
	public static String extractFileName(String path)
	{
		int index = findSlash(path);
		
		if (index > 0)
		{
			return path.substring(index + 1);
		}
		else
		{
			return path;
		}
	}
	
	// reverses file name, for example converts /foo/bar/blah into /blah/bar/foo
	// The limitation - we assume there is no extension on the file
	public static String reverseFileName(String path)
	{
		StringTokenizer st = new StringTokenizer(path, "/");
		String result = "";
		while (st.hasMoreTokens())
		{
			result = "/" + st.nextToken() + result;
		}
		return result;
	}

	// Cuts file extension 
	public static String cutExtension(String path)
	{
		StringTokenizer st = new StringTokenizer(path, ".");
		return st.nextToken();
	}
	
	/*
	 * Extract the file name matching the given prefix from a given full path.
	 */
	public static String extractDirectoryStartwith(String path, String prefix)
	{
		String name = "";
		
		int index = path.toLowerCase().indexOf(prefix.toLowerCase());
		
		if (index >= 0)
		{
			name = path.substring(index);
			
			index = name.indexOf('/', prefix.length());
			
			if (index >= 0)
			{
				name = name.substring(0, index);
			}
		}
		
		return name;
	}
	
	// TODO merge with PropertyUtils.getS3Path()
	public static S3Location getS3Location(String s3Path)
	{
		String s3Location = removeLeadingS3(s3Path).replace("\\", "/");
		if (s3Location.endsWith("/"))
		{
			s3Location = s3Location.substring(0, s3Location.length() - 1);
		}

		int slash = s3Location.indexOf('/');
		if (slash >= 0)
		{
			return new S3Location(s3Location.substring(0, slash), s3Location.substring(slash + 1));
		}
		else
		{
			// S3 path contains bucket name only
			return new S3Location(s3Location, "");
		}
	}
	
	public static String getS3FullPath(String bucket, String key)
	{
		if (!key.isEmpty())
		{
			return S3PATH_PREFIX + bucket + "/" + key;
		}
		else
		{
			return S3PATH_PREFIX + bucket;
		}
	}
	
	public static String getS3FullPath(S3Location s3Location)
	{
		return getS3FullPath(s3Location.getS3Bucket(), s3Location.getS3Key());
	}

	public static String getS3PathWithDirectoryOnly(String s3Path)
	{
		S3Location s3Location = S3FileUtils.getS3Location(s3Path);
		String directory = S3FileUtils.extractDirectory(s3Location.getS3Key());
		if (directory == null)
		{
			return S3FileUtils.getS3FullPath(s3Location.getS3Bucket(), "");
		}
		else
		{
			return S3FileUtils.getS3FullPath(s3Location.getS3Bucket(), directory);
		}
	}
	public static String removeLeadingS3(String propValue)
	{
		if (propValue.indexOf("s3://") == -1)
		{
			throw new IllegalArgumentException("S3 path property \"" + propValue + "\" doesn't start with \"s3://\"");
		}
		return propValue.substring(5);
	}

}
