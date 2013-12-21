package com.practicalHadoop.photo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class PhotoReader {
	
	private PhotoReader(){}
	
	public static List<DatedPhoto> getPictures(UUID user, long startTime, long endTime, String tName, Configuration conf) throws IOException{
		
		List<DatedPhoto> result = new LinkedList<DatedPhoto>();
		// Connect to the table
		HTable index = new HTable(conf, tName);
		String uString = user.toString();
		byte[] family = Bytes.toBytes("A");
		PhotoLocation location = new PhotoLocation();
		if(endTime < 0)
			endTime = startTime + 1;
		Map<String, PhotoDataReader> readers = new HashMap<String, PhotoDataReader>();
		Scan scan = new Scan(Bytes.toBytes(uString + DatedPhoto.timeToString(startTime)), 
				Bytes.toBytes(uString + DatedPhoto.timeToString(endTime)));
		scan.addColumn(family, family);
		Iterator<Result> rIterator = index.getScanner(scan).iterator();
		while(rIterator.hasNext()){
			Result r = rIterator.next();
			location.fromBytes(r.getBytes().get());
			PhotoDataReader dr = readers.get(location.getFile());
			if(dr == null){
				dr = new PhotoDataReader(location.getFile(), user, conf);
				readers.put(location.getFile(), dr);
			}
			DatedPhoto df = new DatedPhoto(location.getTime(), dr.getPicture(location.getPos()));
			result.add(df);
		}
		for(PhotoDataReader dr : readers.values())
			dr.close();
		return result;
	}
	public static void deletePicture(UUID user, long startTime, String tName, Configuration conf) throws IOException{
		
		Delete delete = new Delete(Bytes.toBytes(user.toString() + DatedPhoto.timeToString(startTime)));
		HTable index = new HTable(conf, tName);
		index.delete(delete);
	}
}
