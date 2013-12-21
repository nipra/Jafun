package com.practicalHadoop.outputformat.MultpleDirectories;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class Executor extends Configured implements Tool{

	private static final String CONFIGFILE = "SandBox_Cloud_Config.xml";
	private static final String OUTPUTPATH = "output directory";
	private static final String INPUTPATH = "input file";

	@Override
	public int run(String[] arg0) throws Exception {
		Configuration.addDefaultResource(CONFIGFILE);
		Configuration conf = new Configuration();
		Job job = new Job(conf, "Test Multiple Outputs");
		job.setJarByClass(Executor.class);

		// Set up the input format class
		job.setInputFormatClass(TextInputFormat.class); 
		TextInputFormat.addInputPath(job, new Path(INPUTPATH));

		// Set up the Mapper
		job.setMapperClass(ExecutorMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class); 
		Path jobOutputPath = new Path(OUTPUTPATH);
		FileOutputFormat.setOutputPath(job, jobOutputPath);
		MultipleOutputsDirectories.addNamedOutput(job, "even",
			 	TextOutputFormat.class,
			 	Text.class,IntWritable.class);		
		MultipleOutputsDirectories.addNamedOutput(job, "odd",
				 TextOutputFormat.class,
				 Text.class,IntWritable.class);	

		// Set up the reducer
        job.setNumReduceTasks(0);
        
        // Set FileOutputCommitter
//        job.setOutputFormatClass(null);
                
		FileSystem fs = jobOutputPath.getFileSystem(conf);
		if(fs.exists(jobOutputPath))
			fs.delete(jobOutputPath, true);
		
		boolean res = job.waitForCompletion(true);
		if (res)
			return 0;
		else
			return -1; 
	}
	
	public static void main(String[] args) throws Exception {  
		int res = ToolRunner.run(new Executor(), args); 
		System.exit(res);
	}
}
