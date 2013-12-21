package com.practicalHadoop.outputformat.MultpleDirectories;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ExecutorMapper extends Mapper<LongWritable, Text, Text, IntWritable>{

	private MultipleOutputsDirectories mos;
	
	@Override
	protected void setup(Context context){
		mos = new MultipleOutputsDirectories(context);
	}

 	@Override
	public void map(LongWritable key, Text line, Context context) throws IOException, InterruptedException { 
 	
 		StringTokenizer tokenizer = new StringTokenizer(line.toString());
 		int number = 0;
 		while (tokenizer.hasMoreTokens()) {
 			if(number == 0){
 				mos.write("even", new Text(tokenizer.nextToken()), new IntWritable(number));
 			}
 			else{
 				mos.write("odd", new Text(tokenizer.nextToken()), new IntWritable(number));
 			}
 			number = 1 - number;
 		}
	}  
	@Override
	protected void cleanup(Context context){   
		try {
			mos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
