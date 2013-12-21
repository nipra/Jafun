package com.practicalHadoop.partitioner;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import org.apache.hadoop.io.RawComparator;

public class CompositeKeyComparator implements RawComparator<CompositeKey> {

	@Override
	public int compare(CompositeKey k1, CompositeKey k2) {
		return k1.compareTo(k2);
	}

	@Override
	public int compare(byte[] key1, int start1, int length1, byte[] key2, int start2, int length2) {
		DataInput in1 = new DataInputStream(new ByteArrayInputStream(key1));
		CompositeKey k1 = new CompositeKey();
		DataInput in2 = new DataInputStream(new ByteArrayInputStream(key2));
		CompositeKey k2 = new CompositeKey();
		try{
			k1.readFields(in1);
			k2.readFields(in2);
			return k1.compareTo(k2);
		}
		catch(Exception e){
			return -1;
		}
	}
}