package com.practicalHadoop.lucene.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.practicalHadoop.lucene.document.TermDocument;
import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;

public class TermDocuments {
	
	private long _tl;
	private long _tlu;
	private int _dCounter;
	private int _tOccurence;
	private Map<String, TermDocument> _documents;

	public TermDocuments(){
		
		_dCounter = 0;
		_tOccurence = 0;
		_documents = null;
		_tl = _tlu = System.currentTimeMillis();
	}

	public TermDocuments(Map<String, TermDocument> documents){
		
		_dCounter = documents.size();
		_tOccurence = 0;
		for(Map.Entry<String, TermDocument> entry : documents.entrySet())
			_tOccurence += entry.getValue().docFrequency;
		_documents = documents;
		_tl = _tlu = System.currentTimeMillis();
	}
	
	public TermDocuments(TermDocuments tds){
		
		_dCounter = tds.getCounter();
		_tOccurence = tds.getOccurence();
		_documents = tds.getDocuments();
		_tl = _tlu = System.currentTimeMillis();
		
	}

	public void update(TermDocuments tds){
		
		_dCounter = tds.getCounter();
		_tOccurence = tds.getOccurence();
		_documents = tds.getDocuments();
		_tl = _tlu = System.currentTimeMillis();
		
	}

	public int getCounter() {
		_tlu = System.currentTimeMillis();
		return _dCounter;
	}

	public int getOccurence() {
		_tlu = System.currentTimeMillis();
		return _tOccurence;
	}

	public Map<String, TermDocument> getDocuments() {
		_tlu = System.currentTimeMillis();
		return _documents;
	}

	public void setCounter(int dCounter) {
		_dCounter = dCounter;
		_tl = System.currentTimeMillis();
	}

	public void setOccurence(int tOccurence) {
		_tOccurence = tOccurence;
		_tl = System.currentTimeMillis();
	}

	public void setDocuments(Map<String, TermDocument> documents) {
		_documents = documents;
		_tl = System.currentTimeMillis();
	}

	public boolean isStale(){
		
		return ((System.currentTimeMillis() - _tl) > LuceneHBaseInitializer.getTTL());
	}	

	public boolean isInUse(){
		
		return ((System.currentTimeMillis() - _tlu) < LuceneHBaseInitializer.getTTNA());
	}
	
	public void addDocuments(TermDocuments docs){
		
		addDocuments(docs.getDocuments());
	}
	
	public void addDocuments(Map<String, TermDocument> docs){
		
		for(Map.Entry<String, TermDocument> doc : docs.entrySet()){
			TermDocument td = doc.getValue();
			_dCounter++;
			_tOccurence += td.docFrequency;
			if(_documents == null)
				_documents = Collections.synchronizedMap(new HashMap<String, TermDocument>(50, .7f));
			_documents.put(doc.getKey(), td);
		}
	}

	public void addDocument(String id, TermDocument td){

		_dCounter++;
		_tOccurence += td.docFrequency;
		if(_documents == null)
			_documents = Collections.synchronizedMap(new HashMap<String, TermDocument>(50, .7f));
		_documents.put(id, td);
	}

/*	public void removeDocument(String id){

		if(_documents == null)
			return;
		TermDocument td = _documents.get(id);
		if(td == null)
			return;
		_dCounter--;
		_tOccurence -= td.docFrequency;
		_documents.remove(id); 
	} */
	
	public int getTermFrequency(){

		_tlu = System.currentTimeMillis();
		return _dCounter;
	}
	
	public int getdocumetFrequency(String docID){
		
		_tlu = System.currentTimeMillis();
		TermDocument td = _documents.get(docID);
		return (td == null) ? 0 : td.docFrequency;
	}

	public Iterator<String> getDocumentIterator(){
		
		_tlu = System.currentTimeMillis();
		return ((_documents == null) || (_documents.isEmpty())) ? null : _documents.keySet().iterator();
	}
	
	public TermDocument getTermDocument(String key){
		
		_tlu = System.currentTimeMillis();
		return (_documents == null) ? null : _documents.get(key);
	}
}