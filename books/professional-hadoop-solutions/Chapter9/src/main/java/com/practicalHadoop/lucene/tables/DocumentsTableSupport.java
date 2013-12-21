package com.practicalHadoop.lucene.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import com.practicalHadoop.lucene.cache.DocumentInfo;
import com.practicalHadoop.lucene.document.AVRODataConverter;
import com.practicalHadoop.lucene.document.FieldsData;
import com.practicalHadoop.lucene.document.TermDocumentFrequency;

public class DocumentsTableSupport {
	
	private static TableManager _tManager = null;
		
	private DocumentsTableSupport(){}
	
	public static void init() throws NotInitializedException{	
		_tManager = TableManager.getInstance();
	}

	public static void addDocument(String docID, DocumentInfo dInfo)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface documents = _tManager.getDocumentsTable();
		byte[] bkey = Bytes.toBytes(docID);
		
		try {
			Put put = new Put(bkey);
			for(Map.Entry<String, FieldsData> field : dInfo.getFields().entrySet())
				put.add(TableManager.getDocumentsTableFieldsFamily(), Bytes.toBytes(field.getKey()), AVRODataConverter.toBytes(field.getValue()));
			for(Map.Entry<IndexKey, TermDocumentFrequency> term : dInfo.getTerms().entrySet())
				put.add(TableManager.getDocumentsTableTermsFamily(), Bytes.toBytes(term.getKey().getKey()), AVRODataConverter.toBytes(term.getValue()));

			documents.put(put);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(documents);
		}
	}
	
	public static void addMultiDocuments(DocumentCollector collector){
		
		HTableInterface documents = null;
		Map<String, DocumentInfo> rows = collector.getRows();
		List<Put> puts = new ArrayList<Put>(rows.size());
		try {
			for(Map.Entry<String, DocumentInfo> row : rows.entrySet()){
				byte[] bkey = Bytes.toBytes(row.getKey());
				Put put = new Put(bkey);
				put.setWriteToWAL(false);
				for(Map.Entry<String, FieldsData> field : row.getValue().getFields().entrySet())
					put.add(TableManager.getDocumentsTableFieldsFamily(), Bytes.toBytes(field.getKey()), AVRODataConverter.toBytes(field.getValue()));
				for(Map.Entry<IndexKey, TermDocumentFrequency> term : row.getValue().getTerms().entrySet())
					put.add(TableManager.getDocumentsTableTermsFamily(), Bytes.toBytes(term.getKey().getKey()), AVRODataConverter.toBytes(term.getValue()));
				puts.add(put);
			}
			for(int i = 0; i < 2; i++){
				try {
					documents = _tManager.getDocumentsTable();
					documents.put(puts);
					break;
				} catch (Exception e) {
					System.out.println("Documents Table support. Reseting pool due to the multiput exception ");
					e.printStackTrace();
					_tManager.resetTPool();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			_tManager.releaseTable(documents);
		}
	}
	
	public static List<IndexKey> deleteDocument(String docID)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface documents = _tManager.getDocumentsTable();
		byte[] bkey = Bytes.toBytes(docID);

		List<IndexKey> terms;
		try {
			Get get = new Get(bkey);
			Result result = documents.get(get);
			if(result == null){	// Does not exist
				_tManager.releaseTable(documents);
				return null;
			}
			NavigableMap<byte[], byte[]> data = result.getFamilyMap(TableManager.getDocumentsTableTermsFamily());
			terms = null;
			if((data != null) && (!data.isEmpty())){
				terms = new LinkedList<IndexKey>();
				for(Map.Entry<byte[], byte[]> term : data.entrySet())
					terms.add(new IndexKey(term.getKey()));
			}
			Delete delete = new Delete(bkey);
			documents.delete(delete);
			return terms;
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(documents);
		}
	}
	
	public static DocumentInfo getDocument(String docID)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface documents = _tManager.getDocumentsTable();
		try {
			Get get = new Get(Bytes.toBytes(docID));
			Result result = documents.get(get);
			return processResult(result);
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(documents);
		}
	}

	public static List<DocumentInfo> getDocuments(List<String> docIDs)throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface documents = _tManager.getDocumentsTable();
		List<Get> gets = new ArrayList<Get>(docIDs.size());
		for(String docID : docIDs)
			gets.add(new Get(Bytes.toBytes(docID)));
		List<DocumentInfo> results = new ArrayList<DocumentInfo>(docIDs.size());
		try {
			Result[] result = documents.get(gets);
			if((result == null) || (result.length < 1)){
				for(int i = 0; i < docIDs.size(); i++)
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
			_tManager.releaseTable(documents);
		}
	}

	private static DocumentInfo processResult(Result result) throws Exception{

		if((result == null) || (result.isEmpty())){
			return null;
		}
		Map<String, FieldsData> fields = null;
		Map<IndexKey, TermDocumentFrequency> terms = null;
		NavigableMap<byte[], byte[]> data = result.getFamilyMap(TableManager.getDocumentsTableFieldsFamily());
		if((data != null) && (!data.isEmpty())){
			fields = Collections.synchronizedMap(new HashMap<String, FieldsData>(50, .7f));
			for(Map.Entry<byte[], byte[]> field : data.entrySet()){
				fields.put(Bytes.toString(field.getKey()), AVRODataConverter.unmarshallFieldData(field.getValue()));
			}
		}
		data = result.getFamilyMap(TableManager.getDocumentsTableTermsFamily());
		if((data != null) && (!data.isEmpty())){
			terms = Collections.synchronizedMap(new HashMap<IndexKey, TermDocumentFrequency>());
			for(Map.Entry<byte[], byte[]> term : data.entrySet()){
				IndexKey key = new IndexKey(term.getKey());
				byte[] value = term.getValue();
				TermDocumentFrequency tdf = AVRODataConverter.unmarshallTermDocumentFrequency(value);
				terms.put(key, tdf);
			}
		}
		return new DocumentInfo(terms, fields);
	}

	public static TableScanner getTableScanner()throws Exception{
		
		if(_tManager == null)
			throw new NotInitializedException();
		HTableInterface documents = _tManager.getDocumentsTable();
		try {
			Scan scan = new Scan(TableManager.getDocumentsTableFieldsFamily());
			return new TableScanner(_tManager, documents, documents.getScanner(scan));
		} catch (Exception e) {
			throw new Exception(e);
		}
		finally{
			_tManager.releaseTable(documents);
		}
	}
}