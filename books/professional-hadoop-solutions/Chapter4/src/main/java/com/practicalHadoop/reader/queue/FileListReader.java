package com.practicalHadoop.reader.queue;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.practicalHadoop.queue.HdQueue;


public class FileListReader extends RecordReader<Text, Text> {

	public static final String QUEUE = "mapreduce.input.queue";
	
	private HdQueue _queue;
	private Configuration _conf;
	private Text key = new Text("key");
	private Text value = new Text();

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context)
	throws IOException, InterruptedException {
		_conf = context.getConfiguration();
		_queue = HdQueue.getQueue(_conf.get(QUEUE));
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		return getNextFile();
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return 0;
	}

	@Override
	public void close() throws IOException {
		_queue.close();

	}
	private boolean getNextFile(){

		byte[] f = null;
		try {
			f = _queue.dequeue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(f == null)
			return true;
		value.set(new String(f));
		return false;
	}
}