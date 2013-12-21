package com.practicalHadoop.inputformat.multifile;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

public class MultiFIleSplit extends InputSplit implements Writable{
	
    private long length;
    private String[] hosts;
	private List<String> files = null;

    public MultiFIleSplit(){
    	
    	length = 0;
    	hosts = null;
    	files = new ArrayList<String>();
    }
    public MultiFIleSplit(long l, String[] locations){

        length = l;
        hosts = locations;
       	files = new ArrayList<String>();
    }
    public MultiFIleSplit(long l, String[] locations, List<String> files){

        length = l;
        hosts = locations;
       	this.files = new ArrayList<String>(files.size());
       	this.files.addAll(files);
    }
    public void addFile(String file){
    	files.add(file);
    }
    public List<String> getFile(){
    	return files;
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
            out.writeInt(files.size());
            for(String s : files)
            	out.writeUTF(s);
    }

    @Override
    public void readFields(DataInput in) throws IOException {

        length = in.readLong();
        int hlen = in.readInt();
        hosts = new String[hlen];
        for(int i = 0; i < hlen; i++)
        	hosts[i] = in.readUTF();
        int flen = in.readInt();
        files = new ArrayList<String>(flen);
        for(int i = 0; i < hlen; i++)
        	files.add(in.readUTF());
    }
}
