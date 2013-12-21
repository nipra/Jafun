package com.practicalHadoop.photo;

import java.io.Serializable;

public class PhotoLocation implements Serializable{
	
	private byte[] _buffer = new byte[128];

	private static final long serialVersionUID = 1L;
	
	private long _pos;
	private long _time;
	private String _file;
		
	public PhotoLocation(){}
	
	public PhotoLocation(long time, String file, long pos){
		_time = time;
		_file = file;
		_pos = pos;
	}
	
	public long getPos() {
		return _pos;
	}

	public void setPos(long pos) {
		_pos = pos;
	}

	public String getFile() {
		return _file;
	}

	public void setFile(String file) {
		_file = file;
	}
	
	public long getTime() {
		return _time;
	}

	public void setTime(long time) {
		_time = time;
	}

	public byte[] toBytes(){
		for(byte b : _buffer){
			b = 0;
		}
		System.arraycopy(_pos, 0, _buffer, 0, 8);
		System.arraycopy(_time, 0, _buffer, 8, 8);
		System.arraycopy(_file, 0, _buffer, 16, _file.length());
		return _buffer;
	}
	
	public void fromBytes(byte[] buffer){
		System.arraycopy(buffer, 0, _pos, 0, 8);
		System.arraycopy(buffer, 8, _time, 0, 8);
		System.arraycopy(buffer, 16, _file, 0, buffer.length - 8);
	}
}
