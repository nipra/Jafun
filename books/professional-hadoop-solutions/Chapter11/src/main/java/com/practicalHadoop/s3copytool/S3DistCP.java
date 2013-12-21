package com.practicalHadoop.s3copytool;

import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.practicalHadoop.inputformat.queue.FileListQueueInputFormat;
import com.practicalHadoop.queue.HdQueue;

public class S3DistCP extends Configured implements Tool {
	private static final String usage = "Parameters are "
		+ " SOURCE [source HDFS directory]" 
		+ " Destination [destination AWS directory Bucket/dir/dir]" 
		+ " NumberMappers [How many Mappers to use (Default 50)]" 
		+ " NumberThreads [How many Threads per Mapper to use (Default 20)]" 
		+ " Queue [HInternal distribution (Optinal)]";

	@Override
   	public int run(String[] args) throws Exception {
		if(args.length < 2){
			System.out.println(usage);
			System.exit(2);
		}
		Configuration conf = new Configuration();
		
		// Populate params
		conf.set(Constants.S3_PATH_PROPERTY, args[1]);
		int nmappers = 50;
		if(args.length > 2){
			try{
				nmappers = Integer.parseInt(args[2]);
			}
			catch(Exception e){
				System.out.println("Error processing number of mappers, using default - 50");
			}
		}
		int nthreads = 20;
		conf.set(Constants.THREADS_PROPERTY, Integer.toString(nthreads));
		if(args.length > 3){
			try{
				nthreads = Integer.parseInt(args[3]);
			}
			catch(Exception e){
				System.out.println("Error processing number of threads, using default - 20");
			}
		}
		String queue = null;
		if(args.length > 4)
			queue = args[4];
		else
			queue = UUID.randomUUID().toString();
		conf.set(Constants.QUEUE_PROPERTY, queue);
		
		// Create a queue
		
		HdQueue lbQueue = HdQueue.createQueue(queue);

		Job job = new Job(conf, "Publish Feature Tiles");
	    job.setJarByClass(S3DistCP.class);

	    @SuppressWarnings({ "rawtypes", "unchecked" })
   		Class<? extends InputFormat> inputFormat = (Class<? extends InputFormat>) Class.forName("com.nokia.ec.hadoopjob.format.FileListInputFormat");
   		job.setInputFormatClass(inputFormat);
		
		conf.setInt("map.reduce.map.maxmappers", nmappers);
		FileListQueueInputFormat.setInputQueue(job, queue);
		FileListQueueInputFormat.setMapCount(job, nmappers);
		FileListQueueInputFormat.setInputPaths(job, new Path(args[0]));;
		
		job.setMapperClass(S3CopyMapper.class);
		job.setNumReduceTasks(0);
		job.setOutputFormatClass(NullOutputFormat.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(NullWritable.class);

		//Execute
		boolean result = job.waitForCompletion(true);
		
		//Cleanup
		lbQueue.close();
		lbQueue.destroy();
		
		if (result)
			return 0;
		else
			return -1; 


    }

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new S3DistCP(), args); 
		System.exit(res);
	}
}
