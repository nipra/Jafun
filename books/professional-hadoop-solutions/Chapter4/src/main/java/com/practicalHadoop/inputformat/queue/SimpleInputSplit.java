package com.practicalHadoop.inputformat.queue;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

public class SimpleInputSplit extends InputSplit implements Writable{
	
    private long length;
    private String[] hosts;

    public SimpleInputSplit(){
    	
    	length = 0;
    	hosts = null;
    }
    public SimpleInputSplit(long l, String[] locations){

        length = l;
        hosts = locations;
    }
 	@Override
	public long getLength() throws IOException, InterruptedException {
		return 0;
	}

	@Override
	public String[] getLocations() throws IOException, InterruptedException {
		return hosts;
	}
    @Override
    public void write(DataOutput out) throws IOException {

            out.writeLong(length);
            out.writeInt(hosts.length);
            for(String s : hosts)
            	out.writeUTF(s);
    }

    @Override
    public void readFields(DataInput in) throws IOException {

        length = in.readLong();
        int hlen = in.readInt();
        hosts = new String[hlen];
        for(int i = 0; i < hlen; i++)
        	hosts[i] = in.readUTF();
    }
}
