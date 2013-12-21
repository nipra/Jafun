package com.practicalHadoop.photo;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.ByteWritable;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;

public class PhotoWriter {
	
	private static String _root = "/photos";
	
	private PhotoWriter(){}
	
	public static void writePhotos(UUID user, List<DatedPhoto> photos, String tName, Configuration conf) throws IOException{
		
		// Determine file name and create a writer
		String uString = user.toString();
		
		Path rootPath = new Path(_root);
		FileSystem fs = rootPath.getFileSystem(conf);
		Path userPath = new Path(rootPath, uString);
		String fName = null;
		if(fs.getFileStatus(userPath).isDirectory()){
			FileStatus[] photofiles = fs.listStatus(userPath);
			fName = Integer.toString(photofiles.length);
		}
		SequenceFile.Writer fWriter = SequenceFile.createWriter(conf, 
					Writer.file(new Path(userPath, fName)), 
					Writer.keyClass(LongWritable.class), Writer.valueClass(BytesWritable.class));
		// Connect to the table
		HTable index = new HTable(conf, tName);
		// And now for every image
		PhotoLocation location = new PhotoLocation();
		location.setFile(fName);
		LongWritable sKey = new LongWritable();
		for(DatedPhoto photo : photos){
			long pos = fWriter.getLength();
			location.setPos(pos);
			location.setTime(photo.getLongDate());
			String key = uString + photo.getDate();
			sKey.set(photo.getLongDate());
			fWriter.append(sKey, new BytesWritable(photo.getPicture()));
			Put put = new Put(Bytes.toBytes(key));
			put.add(Bytes.toBytes("A"), Bytes.toBytes("B"), location.toBytes());
			index.put(put);
		}
		fWriter.close();
		index.close();
	}

	public static String getRoot() {
		return _root;
	}	
}