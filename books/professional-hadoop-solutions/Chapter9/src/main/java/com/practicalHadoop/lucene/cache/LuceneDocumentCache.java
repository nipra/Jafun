package com.practicalHadoop.lucene.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

import com.practicalHadoop.lucene.document.FieldsData;
import com.practicalHadoop.lucene.document.TermDocumentFrequency;
import com.practicalHadoop.lucene.document.singleField;
import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;
import com.practicalHadoop.lucene.tables.DocumentCollector;
import com.practicalHadoop.lucene.tables.DocumentsTableSupport;
import com.practicalHadoop.lucene.tables.IndexKey;

public class LuceneDocumentCache {

	/**
	 * Data structure for memory indexes
	 */

	private static Map<String, DocumentInfo> _documents = 
		Collections.synchronizedMap(new HashMap<String, DocumentInfo>(100, .7f));

	
	private LuceneDocumentCache(){}
	
	public static int getNumDocs() {
		return LuceneHBaseInitializer.getNumberDocuments();
	}

	private static DocumentInfo getDocumentInfo(String id) throws Exception{
		
		DocumentInfo struct = _documents.get(id);
		if(struct == null){
			struct = DocumentsTableSupport.getDocument(id);
			if(struct == null)
				return null;
			_documents.put(id, struct);
		}
		return struct;
	}
	
	public static List<Document> getDocuments(List<String> ids) throws Exception{
		
		List<Document> results = new ArrayList<Document>(ids.size());
		List<String> getids = new ArrayList<String>(ids.size());
		List<Integer> idPositions = new ArrayList<Integer>(ids.size());
		int i = 0;
		for(String id : ids){
			DocumentInfo struct = _documents.get(id);
			if(struct != null)
				results.add(i, fromDocumentInfo(struct));
			else{
				getids.add(id);
				idPositions.add(i);
			}
			i++;
		}
		if(getids.size() > 0){
			List<DocumentInfo> readResults = DocumentsTableSupport.getDocuments(getids);
			int j = 0;
			for(DocumentInfo struct : readResults){
				results.add(idPositions.get(j), fromDocumentInfo(struct));
				if(struct != null)
					_documents.put(idPositions.get(j).toString(), struct);
				j++;
			}
		}
		
		return results;
	}
	
	public static Document getDocument(String id) throws Exception{
		
		DocumentInfo struct = getDocumentInfo(id);
		if(struct == null){
			return null;
		}

		return fromDocumentInfo(struct);
	}
	
	private static Document fromDocumentInfo(DocumentInfo struct){
		Document document = new Document();
		for(Map.Entry<String,FieldsData> entry : struct.getFields().entrySet()){
			String name = entry.getKey();
			for(singleField fd : entry.getValue().fieldsArray){
			if(fd.binary)
				document.add(new Field(name, (byte[])fd.data, Store.YES));
			else
				document.add(new Field(name, fd.data.toString(), Store.YES, Index.NO));
		
			}
		}
		return document;
	}
	
	public static Map<IndexKey, TermDocumentFrequency> getDocumentTerms(String id) throws Exception{
		
		DocumentInfo struct = getDocumentInfo(id);
		if(struct == null){
			return null;
		}
		return struct.getTerms();
	}
	
	public static void deleteDocument(String id) throws Exception{
		
		List<IndexKey> indexes = DocumentsTableSupport.deleteDocument(id);
		if(indexes == null)
			return;
		for(IndexKey index : indexes){
			LuceneIndexCache.deleteDocument(index.getField(), index.getTerm(), id);
			LuceneDocumentNormsCache.deleteNorm(index.getField(), id);
		}
	}
	
	public static void addDocument(DocumentCollector collector, String docID, DocumentInfo dInfo)throws Exception{
		
		collector.addDocument(docID, dInfo);
	}
	
	public static void refresh(){
		
		List<String> removed = new ArrayList<String>();
		for(Map.Entry<String, DocumentInfo> entry : _documents.entrySet()){
			String docID = entry.getKey();
			DocumentInfo document = entry.getValue();
			if(!document.isInUse()){
				removed.add(docID);
				continue;
			}
			if(!document.isStale()){
				try {
					DocumentInfo update = DocumentsTableSupport.getDocument(docID);
					document.setFields(update.getFields());
					document.setTermDocuments(update.getTerms());
				} catch (Exception e) {}
			}
		}
		
		for(String docID : removed)
			_documents.remove(docID);
	}
	
	public static String stringValue(){
		
		StringBuffer rb = new StringBuffer();
		for(String id : _documents.keySet()){
			rb.append("Document " + id + ":\n\r");
			DocumentInfo struct = _documents.get(id);
			rb.append(struct.toString() + "\n\r");
		}
		return rb.toString();
	}
}