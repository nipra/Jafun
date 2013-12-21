package com.practicalHadoop.lucene.tables;

import java.util.HashMap;
import java.util.Map;

import com.practicalHadoop.lucene.cache.DocumentInfo;
import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;

public class DocumentCollector {
		
	private Map<String, DocumentInfo> _rows = null;
	
	public DocumentCollector(){
		_rows = new HashMap<String, DocumentInfo>(LuceneHBaseInitializer.getCollectorDocumentSize());
	}
	
	public Map<String, DocumentInfo> getRows(){
		return _rows;
	}
	
	public void addDocument(String docID, DocumentInfo dInfo){		
		_rows.put(docID, dInfo);
		if(_rows.size() >= LuceneHBaseInitializer.getCollectorDocumentSize()){
			writeToTable();
		}
	}
		
	public void finalize(){
		if(_rows.size() > 0)
			writeToTable();
	}
	
	private void writeToTable(){

		DocumentsTableSupport.addMultiDocuments(this);
		_rows.clear();
	}	
}