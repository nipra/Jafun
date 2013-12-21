package com.practicalHadoop.lucene.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.practicalHadoop.lucene.cache.TermDocuments;
import com.practicalHadoop.lucene.document.AVRODataConverter;
import com.practicalHadoop.lucene.document.TermDocument;

public class IndexTableSupport {
	
	private static TableManager _tManager = null;
		
	private IndexTableSupport(){}
	
	public static void init() throws NotInitializedException{	
		_tManager = TableManager.getInstance();
	}

	public static void addDocument(IndexKey key, String docID, TermDocument td)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface index = _tManager.getIndexTable(key.getLevel());
		if(index == null)
			throw new Exception("no Table");
		byte[] bkey = Bytes.toBytes(new MultiTableIndexKey(key).getKey());
		Put put = new Put(bkey);
		try {
			put.add(TableManager.getIndexTableDocumentsFamily(), Bytes.toBytes(docID), 
					AVRODataConverter.toBytes(td));
			index.put(put);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}

	public static void addDocuments(IndexKey key, Map<String, TermDocument> docs)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface index = _tManager.getIndexTable(key.getLevel());
		if(index == null)
			throw new Exception("no Table");
		byte[] bkey = Bytes.toBytes(new MultiTableIndexKey(key).getKey());
		
		Put put = new Put(bkey);
		try {
			for(Map.Entry<String, TermDocument> entry : docs.entrySet()){
				String docID = entry.getKey();
				TermDocument td = entry.getValue();
				put.add(TableManager.getIndexTableDocumentsFamily(), Bytes.toBytes(docID), 
						AVRODataConverter.toBytes(td));
			}
			index.put(put);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}
	
	public static void addMultiDocuments(Map<MultiTableIndexKey,Map<String,TermDocument>> rows, int level){
		
		HTableInterface index = null;
		
		List<Put> puts = new ArrayList<Put>(rows.size());
		try {
			for (Map.Entry<MultiTableIndexKey, Map<String, TermDocument>> row : rows.entrySet()) {
				byte[] bkey = Bytes.toBytes(row.getKey().getKey());
				Put put = new Put(bkey);
				put.setWriteToWAL(false);
				for (Map.Entry<String, TermDocument> entry : row.getValue().entrySet()) {
					String docID = entry.getKey();
					TermDocument td = entry.getValue();
					put.add(TableManager.getIndexTableDocumentsFamily(),
							Bytes.toBytes(docID), AVRODataConverter.toBytes(td));
				}
				puts.add(put);
			}
			for(int i = 0; i < 2; i++){
				try {
					index = _tManager.getIndexTable(level);
					index.put(puts);
					break;
				} catch (Exception e) {
					System.out.println("Index Table support. Reseting pool due to the multiput exception ");
					e.printStackTrace();
					_tManager.resetTPool();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			_tManager.releaseTable(index);
		}
	}
	
	public static void deleteDocument(IndexKey key, String docID)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface index = _tManager.getIndexTable(key.getLevel());
		if(index == null)
			throw new Exception("no Table");
		byte[] bkey = Bytes.toBytes(new MultiTableIndexKey(key).getKey());
		byte[] bID = Bytes.toBytes(docID);		
		Delete delete = new Delete(bkey);
		delete.deleteColumn(TableManager.getIndexTableDocumentsFamily(), bID);
		try {
			index.delete(delete);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}

	public static void deleteIndex(IndexKey key)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface index = _tManager.getIndexTable(key.getLevel());
		if(index == null)
			throw new Exception("no Table");
		byte[] bkey = Bytes.toBytes(new MultiTableIndexKey(key).getKey());
		Delete delete = new Delete(bkey);
		try {
			index.delete(delete);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}
	
	public static TermDocuments getIndex(IndexKey key)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface index = _tManager.getIndexTable(key.getLevel());
		if(index == null)
			throw new Exception("no Table");
		byte[] bkey = Bytes.toBytes(new MultiTableIndexKey(key).getKey());
		try {
			Get get = new Get(bkey);
			Result result = index.get(get);	
			return processResult(result);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}
	
	public static List<TermDocuments> getIndexes(List<IndexKey> keys)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		List<Get> gets = new ArrayList<Get>(keys.size());
		int level = 0;
		for(IndexKey key : keys){
			byte[] bkey = Bytes.toBytes(new MultiTableIndexKey(key).getKey());
			gets.add(new Get(bkey));
			level = key.getLevel();
		}
		HTableInterface index = _tManager.getIndexTable(level);
		if(index == null)
			throw new Exception("no Table");
		List<TermDocuments> results = new ArrayList<TermDocuments>(keys.size());
		try {
			Result[] result = index.get(gets);
			if((result == null) || (result.length < 1)){
				for(int i = 0; i < keys.size(); i++)
					results.add(null);
			}
			else{
				for(Result r : result)
					results.add(processResult(r));
			}
			return results;
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}
	
	private static TermDocuments processResult(Result result)throws Exception{

		if((result == null) || (result.isEmpty())){
			return null;
		}

		Map<String, TermDocument> docs = null;
		NavigableMap<byte[], byte[]> documents = result.getFamilyMap(TableManager.getIndexTableDocumentsFamily());
		if((documents != null) && (!documents.isEmpty())){
			docs = Collections.synchronizedMap(new HashMap<String, TermDocument>(50, .7f));
			for(Map.Entry<byte[], byte[]> entry : documents.entrySet()){
				TermDocument std = null;
				try {
					std = AVRODataConverter.unmarshallTermDocument(entry.getValue());
				} catch (Exception e) {
					System.out.println("Barfed in Avro conversion");
					e.printStackTrace();
					throw new Exception(e);
				}
				docs.put(Bytes.toString(entry.getKey()), std);
			}
		}
		return docs == null ? null : new TermDocuments(docs);
	}

	public static TermDocumentIndex getIndexNextKey(IndexKey lowKey)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface index = _tManager.getIndexTable(lowKey.getLevel());
		if(index == null)
			throw new Exception("no Table");
		
		byte[] startkey = Bytes.toBytes(new MultiTableIndexKey(lowKey).getKey());
		Filter filter = new PageFilter(1);
		try {
			Scan scan = new Scan(startkey, filter);
			ResultScanner scanner = index.getScanner(scan);  

			for (Result result : scanner) {
				IndexKey key = new IndexKey(result.getRow());
				Map<String, TermDocument> docs = null;
				NavigableMap<byte[], byte[]> documents = result.getFamilyMap(TableManager.getIndexTableDocumentsFamily());
				if((documents == null) || (documents.isEmpty()))
					continue;
				docs = Collections.synchronizedMap(new HashMap<String, TermDocument>(50, .7f));
				for(Map.Entry<byte[], byte[]> entry : documents.entrySet()){
					TermDocument std = AVRODataConverter.unmarshallTermDocument(entry.getValue());
					docs.put(Bytes.toString(entry.getKey()), std);
				}

				_tManager.releaseTable(index);
				TermDocuments td = new TermDocuments(docs);
				return 	new TermDocumentIndex(key, td);	
			}
			return null;
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}
	
	public static List<String> getFieldDocuments(String field)throws Exception{
		
		List<String>  documents = new ArrayList<String>();
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface index = _tManager.getIndexTable(1);
		
		byte[] startkey = Bytes.toBytes(field); 
		byte[] endkey = Bytes.toBytes(field);
		int lastbyte = endkey.length - 1;
		endkey[lastbyte]++;
		try {
			Scan scan = new Scan(startkey, endkey);
			scan.addFamily(TableManager.getIndexTableDocumentsFamily());
			ResultScanner scanner = index.getScanner(scan);  

			for (Result result : scanner) {
				NavigableMap<byte[], byte[]> data = result.getFamilyMap(TableManager.getIndexTableDocumentsFamily());
				for(byte[] docId : data.keySet())
					documents.add(Bytes.toString(docId));
			}

			return documents;
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(index);
		}
	}
	
	public static List<String> getFieldNames()throws Exception{
		
		return null;
	}
}