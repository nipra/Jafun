package com.practicalHadoop.reader.xml;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class XMLIngester extends Mapper<Text, Text, NullWritable, NullWritable> {

	
	@Override
	protected void setup(Context context){
		
		// Any Setup goes here

	}
		
	@Override
	public void map(Text key, Text value, Context context){

		String tag = key.toString();
		String fragment = value.toString();
		
		// Processing goes here
	}
	
	
	@Override
	protected void cleanup(Context context){

		// Cleanup here
	}

}