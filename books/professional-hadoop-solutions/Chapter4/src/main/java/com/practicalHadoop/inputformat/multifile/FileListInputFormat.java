package com.practicalHadoop.inputformat.multifile;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.StringUtils;

public class FileListInputFormat extends FileInputFormat<Text, Text>{

	private static final String MAPCOUNT = "map.reduce.map.count";
	private static final String INPUTPATH = "mapred.input.dir";

	@Override
	public List<InputSplit> getSplits(JobContext context) throws IOException {
		Configuration conf = context.getConfiguration();
		String fileName = conf.get(INPUTPATH, "");
		String[] hosts = getActiveServersList(context);
		Path p = new Path(StringUtils.unEscapeString(fileName));
		List<InputSplit> splits = new LinkedList<InputSplit>();
	    FileSystem fs = p.getFileSystem(conf);
		int mappers = 0;
		try{
			mappers = Integer.parseInt(conf.get(MAPCOUNT));
		}
		catch(Exception e){}
		if(mappers == 0)
			throw new IOException("Number of mappers is not specified");
		FileStatus[] files = fs.globStatus(p);
		int nfiles = files.length;
		if(nfiles < mappers)
			mappers = nfiles;
		for(int i = 0; i < mappers; i++)
			splits.add(new MultiFIleSplit(0,hosts));
		Iterator<InputSplit> siter = splits.iterator();
		for(FileStatus f : files){
			if(!siter.hasNext())
				siter = splits.iterator();
			((MultiFIleSplit)(siter.next())).addFile(f.getPath().toUri().getPath());
		}
		return splits;
	}
	@Override
	public RecordReader<Text, Text> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException,
			InterruptedException{
		
		return null;
	}
	
	public static void setMapCount(Job job, int mappers){
	    Configuration conf = job.getConfiguration();
	    conf.set(MAPCOUNT, new Integer(mappers).toString());
	}
	
	public static String[] getActiveServersList(JobContext context) throws IOException{

		String [] servers = null;

        return servers;
	}
}
