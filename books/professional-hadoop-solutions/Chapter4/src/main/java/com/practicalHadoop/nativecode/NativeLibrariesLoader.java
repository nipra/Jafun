package com.practicalHadoop.nativecode;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;

public class NativeLibrariesLoader {
	
	private static final String LIBRARIES = "mapreduce.shared.libraries";
	private static final String DELIM = ",";
	private NativeLibrariesLoader(){}
	
	public static void storeLibraries(Configuration conf, String baseDir, Collection<String> libraries) throws Exception{
        DistributedCache.createSymlink(conf);
        boolean first = true;
		StringBuffer sb = new StringBuffer();
		for(String library : libraries){
        	DistributedCache.addCacheFile(new URI(baseDir + library), conf);
        	if(first)
        		first = false;
        	else
        		sb.append(DELIM);
        	sb.append(library);
		}
		conf.set(LIBRARIES, sb.toString());
	}

	public static void loadLibraries(Configuration conf) throws IOException{
		Map<String, String> libPaths = new HashMap<String, String>();
		StringTokenizer st = new StringTokenizer(conf.get(LIBRARIES),DELIM);
		Path[] libraries = DistributedCache.getLocalCacheFiles(conf);
		for(Path library : libraries){
			libPaths.put(library.getName(), library.toString());
		}
		while(st.hasMoreTokens()){
			System.load(libPaths.get(st.nextToken()));
		}
	}
}
