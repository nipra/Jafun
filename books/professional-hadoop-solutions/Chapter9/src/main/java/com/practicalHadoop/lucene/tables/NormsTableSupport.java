package com.practicalHadoop.lucene.tables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.practicalHadoop.lucene.cache.FieldNorms;

public class NormsTableSupport {
	
	private static TableManager _tManager = null;
		
	private NormsTableSupport(){}
	
	public static void init() throws NotInitializedException{	
		_tManager = TableManager.getInstance();
	}

	public static void addNorm(String field, String docID, byte norm)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface norms = _tManager.getNormsTable();
		byte[] normsData = new byte[1];
		normsData[0] = norm;
		
		Put put = new Put(Bytes.toBytes(field));
		put.add(TableManager.getNormsTableNormsFamily(), Bytes.toBytes(docID), normsData);
		norms.put(put);
		norms.flushCommits();
		_tManager.releaseTable(norms);
	}
	
	public static void addNorms(String field, Map<String, Byte> n)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface norms = _tManager.getNormsTable();		
		byte[] bkey = Bytes.toBytes(field);
		Put put = new Put(bkey);
		for(Map.Entry<String, Byte> entry : n.entrySet()){ 
			byte[] normsData = new byte[1];
			normsData[0] = entry.getValue();
			put.add(TableManager.getNormsTableNormsFamily(), Bytes.toBytes(entry.getKey()), normsData);
		}
		norms.put(put);
		norms.flushCommits();
		_tManager.releaseTable(norms);
	}
	
	public static void deleteNorm(String field, String docID)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface norms = _tManager.getNormsTable();
		byte[] bkey = Bytes.toBytes(field);
		
		Delete delete = new Delete(bkey);
		delete.deleteColumn(TableManager.getNormsTableNormsFamily(), Bytes.toBytes(docID));
		norms.delete(delete);
		norms.flushCommits();
		_tManager.releaseTable(norms);
	}
	
	public static void deleteNorms(String field, List<String> docIDs)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface norms = _tManager.getNormsTable();
		byte[] bkey = Bytes.toBytes(field);
		
		Delete delete = new Delete(bkey);
		for(String docID : docIDs)
			delete.deleteColumn(TableManager.getNormsTableNormsFamily(), Bytes.toBytes(docID));
		norms.delete(delete);
		norms.flushCommits();
		_tManager.releaseTable(norms);
	}
	
	public static FieldNorms getNorms(String field)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface norms = _tManager.getNormsTable();
		Get get = new Get(Bytes.toBytes(field));
		Result result = norms.get(get);
		if(result == null){
			_tManager.releaseTable(norms);
			return null;
		}
		NavigableMap<byte[], byte[]> data = result.getFamilyMap(TableManager.getNormsTableNormsFamily());
		if((data == null) || (data.isEmpty())){
			_tManager.releaseTable(norms);
			return null;
		}
		Map<String,Byte> results = new HashMap<String,Byte>();
		for(Map.Entry<byte[], byte[]> norm : data.entrySet()){
			String name = Bytes.toString(norm.getKey());
			byte value = norm.getValue()[0];
			results.put(name, value);
		}
		_tManager.releaseTable(norms);
		return new FieldNorms(results);
	}
}