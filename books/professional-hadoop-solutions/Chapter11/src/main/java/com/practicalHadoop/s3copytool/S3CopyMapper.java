package com.practicalHadoop.s3copytool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.practicalHadoop.aws.AWSResource;
import com.practicalHadoop.queue.HdQueue;
import com.practicalHadoop.s3client.GenericS3Client;
import com.practicalHadoop.s3client.GenericS3ClientImpl;



public class S3CopyMapper extends Mapper<Text, Text, NullWritable, NullWritable> {

	protected HashMap<String, String> lookup = new HashMap<String, String>();
	private GenericS3Client _S3Client;
	private FileSystem fs;
	private String AWSPath;
	protected CacheShutdownHook sHook = null;
	protected List<String> files;
	private HdQueue queue;
	
	@Override
	protected void setup(Context context) throws IOException, InterruptedException {

		Configuration configuration = context.getConfiguration();
		AWSPath = configuration.get(Constants.S3_PATH_PROPERTY, "");
		
		fs = FileSystem.get(configuration);
		int threads = Integer.parseInt(configuration.get(Constants.THREADS_PROPERTY));
		ExecutorService executor = new ThreadPoolExecutor(threads, threads,
				100l, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(threads));		
		_S3Client = new GenericS3ClientImpl(AWSResource.getAWSCredentials(), null, executor);
		//attach the shutdown hook for clean exit and attempt retries from the mapper
		queue = HdQueue.getQueue(configuration.get(Constants.QUEUE_PROPERTY));
		files = new ArrayList<String>();
		sHook = new CacheShutdownHook(queue, files);
		sHook.attachShutDownHook();
	}

	@Override
	public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
		String fname = value.toString();
		Path p = new Path(fname);
		FSDataInputStream in = fs.open(p);
		if (in != null) {
			if (files != null)
				files.add(fname);
			_S3Client.uploadData(AWSPath, p.getName(), in, fs.getFileStatus(p).getLen());
			if (files != null)
				files.remove(fname);
			context.setStatus("Copy to S3 Completed for file: "+key);
		}
	}
	
	@Override
	protected void cleanup(Context context) throws IOException, InterruptedException {
		if (sHook != null) {
			files.clear(); // normal exit, don't roll-back any input key.
		}
	}

}
