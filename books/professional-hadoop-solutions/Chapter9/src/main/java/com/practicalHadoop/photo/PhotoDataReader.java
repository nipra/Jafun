package com.practicalHadoop.photo;

import java.io.IOException;
import java.util.UUID;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Reader;

public class PhotoDataReader {
	
	private String _user;
	private String _file;
	private long _position;
	private Configuration _conf;
	private SequenceFile.Reader _fReader;
	private BytesWritable _value;
	private LongWritable _header;
	
	public PhotoDataReader(String file, UUID user, Configuration conf) throws IOException{
		
		_file = file;
		_conf = conf;
		_user = user.toString();
		_value = new BytesWritable();
		Path rootPath = new Path(PhotoWriter.getRoot());
		FileSystem fs = rootPath.getFileSystem(_conf);
		Path userPath = new Path(rootPath, _user);
		_fReader = new Reader(fs, new Path(userPath, _file), _conf);
		_position = 0;
	}

	public byte[] getPicture(long pos) throws IOException{
		if(pos != _position)
			_fReader.seek(pos);
		boolean fresult = _fReader.next(_header, _value);
		if(!fresult)
			throw new IOException("EOF");
		_position = _fReader.getPosition();
		return _value.getBytes();
	}
	
	public void close() throws IOException{
		
		_fReader.close();
	}
}