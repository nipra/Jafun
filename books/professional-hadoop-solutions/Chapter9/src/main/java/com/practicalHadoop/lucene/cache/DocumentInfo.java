package com.practicalHadoop.lucene.cache;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

//import com.navteq.lucene.hbase.document.FieldsData;
import com.practicalHadoop.lucene.document.FieldsData;
import com.practicalHadoop.lucene.document.TermDocumentFrequency;
import com.practicalHadoop.lucene.document.singleField;
import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;
import com.practicalHadoop.lucene.tables.IndexKey;

public class DocumentInfo {


	private long _tl;
	private long _tlu;
	private Map<IndexKey, TermDocumentFrequency> _terms;
	private Map<String, FieldsData> _fields;
	
	public DocumentInfo(){
		
		_tl = _tlu = System.currentTimeMillis();
		_fields = new HashMap<String, FieldsData>();
		_terms = new HashMap<IndexKey, TermDocumentFrequency>();
	}

	public DocumentInfo(Map<IndexKey, TermDocumentFrequency> terms, Map<String, FieldsData> fields){
		
		_tl = _tlu = System.currentTimeMillis();
		_fields = fields;
		_terms = terms;
	}

	public Map<IndexKey, TermDocumentFrequency> getTerms() {
		 _tlu = System.currentTimeMillis();
		return _terms;
	}

	public Map<String, FieldsData> getFields() {
		 _tlu = System.currentTimeMillis();
		return _fields;
	}
	
	public boolean isStale(){
		
		return ((System.currentTimeMillis() - _tl) > LuceneHBaseInitializer.getTTL());
	}	

	public boolean isInUse(){
		
		return ((System.currentTimeMillis() - _tlu) < LuceneHBaseInitializer.getTTNA());
	}
	
	public void addField(String name, singleField data){
		FieldsData field = _fields.get(name);
		if(field == null){
			field = new FieldsData();
			_fields.put(name, field);
			field.fieldsArray = new LinkedList<singleField>();
		}
		field.fieldsArray.add(data);
	}	

	public void addTermDocument(IndexKey index, TermDocumentFrequency termDocument){
		
		_terms.put(index, termDocument);
	}
	
	public void setFields(Map<String, FieldsData> fields){

		_fields = fields;
		_tl = System.currentTimeMillis();
	}

	public void setTermDocuments(Map<IndexKey, TermDocumentFrequency> terms){

		_terms = terms;
		_tl = System.currentTimeMillis();
	}	
}