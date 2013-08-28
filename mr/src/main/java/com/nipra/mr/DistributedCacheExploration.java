package com.nipra.mr;

import java.io.*;

import java.util.*;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/mr)$ hadoop jar target/mr-0.0.1-SNAPSHOT.jar com.nipra.mr.DistributedCacheExploration -files hdfs://localhost:9000/data/hello-world.txt /gutenberg /gutenberg-output

public class DistributedCacheExploration extends Configured implements Tool {

  public static class DistributedCacheMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    private String dfcc;

    static String readFile(String path) 
      throws IOException {
      FileInputStream fis = new FileInputStream(new File(path));
      InputStreamReader isr = new InputStreamReader(fis);
      StringBuffer sb = new StringBuffer();
      char[] b = new char[8192];
      int n;
      // Read a block. If it gets any chars, append them.
      while ((n = isr.read(b)) > 0) {
        sb.append(b, 0, n);
      }
      // Only construct the String object once, here.
      return sb.toString();

    }

    @Override
    protected void setup(Context context)
      throws IOException, InterruptedException {
      dfcc = readFile("hello-world.txt");
    }
         
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
      String line = value.toString();
      StringTokenizer tokenizer = new StringTokenizer(line);
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken() + dfcc;
        word.set(token);
        context.write(word, one);
      }
    }
  } 
         
  public static class DistributedCacheReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
 
    public void reduce(Text key, Iterable<IntWritable> values, Context context) 
      throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      context.write(key, new IntWritable(sum));
    }
  }
  
  @Override
  public int run(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.printf("Usage: %s [generic options] <input> <output>\n",
                        getClass().getSimpleName());
      ToolRunner.printGenericCommandUsage(System.err);
      return -1;
    }

    Job job = new Job(getConf(), "Distributed Cache Exploration");
    job.setJarByClass(getClass());

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    
    job.setMapperClass(DistributedCacheMapper.class);
    job.setCombinerClass(DistributedCacheReducer.class);
    job.setReducerClass(DistributedCacheReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);

    
    return job.waitForCompletion(true) ? 0 : 1;

  }

  public static void main(String[] args) throws Exception {
    int exitCode = ToolRunner.run(new DistributedCacheExploration(), args);
    System.exit(exitCode);
  }

}
