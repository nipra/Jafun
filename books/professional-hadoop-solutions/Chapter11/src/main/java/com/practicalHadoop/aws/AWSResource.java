package com.practicalHadoop.aws;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.PropertiesCredentials;

public final class AWSResource{
	
	private AWSResource(){}
	
	public static AWSCredentials getAWSCredentials() throws IOException
	{
		InputStream is = AWSResource.class.getClassLoader().getResourceAsStream("AwsCredentials.properties");
		if(is != null)
			return new PropertiesCredentials(is);
		return new InstanceProfileCredentialsProvider().getCredentials();	
	}
	
	public static AWSCredentials getAWSCredentials(File credentialsPropFile) throws IOException
	{
		return new PropertiesCredentials(credentialsPropFile);
	}
}
