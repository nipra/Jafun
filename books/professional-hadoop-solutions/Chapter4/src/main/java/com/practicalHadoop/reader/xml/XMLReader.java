package com.practicalHadoop.reader.xml;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class XMLReader extends RecordReader<Text, Text> {

	public static final String ELEMENT_NAME = "elementName";
	
	private InputStream _fileIn;
	private long _start;
	private long _end;
	private long _current;
	private boolean _eof = false;
	private boolean _firstOnly = false;
	
	private KeyString[] _keyString;
	private Text _key;
	private StringBuilder _value;
	
	private byte[][] _startTag; 
	private byte[][] _endTag; 
	
	private int [] _matchingStartTag;
	private int [] _matchingEndTag;

	private byte[] _buffer;
	private int _bufferPos;
	private int _bufferRead;
	
	@Override
	public void initialize(InputSplit inputSplit, TaskAttemptContext context) throws IOException, InterruptedException {

		FileSplit split = (FileSplit) inputSplit;
		Configuration configuration = context.getConfiguration();
		String key = configuration.get(ELEMENT_NAME);

		long s = split.getStart();
		long e = s + split.getLength() - 1;
		final Path file = split.getPath();
		CompressionCodecFactory compressionCodecs = new CompressionCodecFactory(configuration);
	    final CompressionCodec codec = compressionCodecs.getCodec(file);

		// open the file and seek to the start of the split
		final FileSystem fs = file.getFileSystem(configuration);
		FSDataInputStream fileIn = fs.open(file);
		if (codec != null) 
			init(0, Long.MAX_VALUE, key, codec.createInputStream(fileIn));
		else 
			init(s, e, key, fileIn);	
	}

	@Override
	public void close() throws IOException {
		
		if(_fileIn != null)
			_fileIn.close();
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		
		return _key;
	}

	@Override
	public Text getCurrentValue() throws IOException, InterruptedException {
		
		return new Text( _value.toString());
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {

		if (_start == _end)
			return 0.0f;
		else
			return Math.min(1.0f, (_current - _start) / (float)(_end - _start));
	}

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		
		if((_eof) || (_current >= _end))
			return false;
		_firstOnly = false;
		
		if (readUntilMatch(_startTag, _matchingStartTag, false)) 
			return true;
		
		if(_eof)
			return false;
		
		String endtag = "</" + _key.toString() + ">";
		if(_firstOnly){
			_value.append(endtag);;
			return true;
		}

		_endTag[0] = endtag.getBytes();
		readUntilMatch(_endTag, _matchingEndTag, true);
		if(_eof)
			return false;
		return true;
	}
	
	private int nextByte(){
		if(_bufferRead == -1){
			try {
				_bufferRead = _fileIn.read(_buffer);
				if(_bufferRead < 0){
					// end of file:
					return -1;
				}
			} catch (Exception e) {
				// end of file:
				_eof = true;
				return -1;
			}
			_bufferPos = 0;
		}
		int value = 0x00ff &_buffer[_bufferPos++];
		if(_bufferPos >= _bufferRead){
			_bufferRead = -1;
		}
		return value;
	}

	private boolean readUntilMatch(byte[][] match, int[] matchingTag,boolean withinBlock) throws IOException {

		char currChar = ' ', prevChar = ' ';
		boolean matched = false;
		for(int i = 0; i < matchingTag.length; i++)
			matchingTag[i] = 0;
		for (;;) {
//			try {
//				int rv = _fileIn.read();
				int rv = nextByte();
				_current++;
				if(rv < 0){
					// end of file:
					_eof = true;
					return false;
				}
				currChar = (char)rv;
//			} catch (Exception e) {
//				// end of file:
//				_eof = true;
//				return false;
//			}
			
			// Save the value
			if(withinBlock || matched)
                _value.append(currChar);
						
			if (!matched) {
				for (int j = 0; j < matchingTag.length; j++) {
					// check if we're matching:
					if (currChar == match[j][matchingTag[j]]) {
						matchingTag[j]++;
						if (matchingTag[j] >= match[j].length) {
							matched = true;
							if(withinBlock)
								return false;
							long st = _current - match[j].length;
							if(st >= _end){
								// end of block:
								_eof = true;
								return false;
							}
							_value.append(new String(match[j]));
							if(j >= _keyString.length)
								j -= _keyString.length;
							_key = new Text(_keyString[j].getString());
							_firstOnly = _keyString[j].isFirstOnly();
                            if ('>' == _value.charAt(_value.length() - 1))								return false;
								break;
						}
					} else
						matchingTag[j] = 0;
				}
				prevChar = currChar;
				continue;
			}
			
			// We are matched, go till the end of the tag
			if(currChar == '>'){
				if(!withinBlock && (prevChar == '/'))
					return true;
				return false;
			}
			prevChar = currChar;
		}
	}
	
	private void init(long s, long e, String keys, InputStream in) throws IOException {

		_start = s;
		_end = e;
		_current = _start;
		StringTokenizer st = new StringTokenizer(keys, ",");
		int kLen = st.countTokens();
		_keyString = new KeyString[kLen];
		_startTag = new byte [2*kLen][];
		_endTag = new byte [1][];
		for (int i = 0; i < kLen; i++) {
			String str = st.nextToken();
			boolean firstOnly = false;
			if(str.startsWith("+")){
				firstOnly = true;
				str = str.substring(1);
			}
			_keyString[i] = new KeyString(str, firstOnly);
			_startTag[i] = ("<" + _keyString[i].getString() + ">").getBytes();
			_startTag[kLen + i] = ("<" + _keyString[i].getString() + " ").getBytes();
		}
		_matchingStartTag = new int[_startTag.length];
		_matchingEndTag = new int[1];
		_fileIn = in;
		if (_start > 0) {
			if (_fileIn instanceof FSDataInputStream) {
				((FSDataInputStream) _fileIn).seek(_start);
			} else {
				_fileIn.skip(_start);
			}
		}
		
		_buffer = new byte[4096];
		_bufferPos = -1;
		_bufferRead = -1;
	}
	
	// This is a test method
	public static void main(String[] args) throws Exception {

		// Parameters
		// args[0] - file
		// args[1] - tag names; comma separated
		// args[2] - start
		// args[3] - end
		
		DataInputStream in = new DataInputStream(new FileInputStream(new File(args[0])));
		long start = Long.parseLong(args[2]);
		long end = Long.parseLong(args[3]);
		XMLReader reader = new XMLReader();
		reader.init(start, end, args[1], in);
		Map<String, Integer> occurences = new HashMap<String, Integer>();
		while(reader.nextKeyValue()){
			String key = reader.getCurrentKey().toString();
			System.out.println("key " + key);
//			System.out.println("value " + reader.getCurrentValue().toString());
			Integer count = occurences.get(key);
			if(count == null)
				count = 1;
			else
				count++;
			occurences.put(key, count);
		}
		for(Map.Entry<String, Integer> entry: occurences.entrySet())
			System.out.println("key " + entry.getKey() + " found " + entry.getValue() + " times");
			
	}
}