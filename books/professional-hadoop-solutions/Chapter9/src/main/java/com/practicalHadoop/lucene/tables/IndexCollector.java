package com.practicalHadoop.lucene.tables;

import java.util.HashMap;
import java.util.Map;

import com.practicalHadoop.lucene.document.TermDocument;
import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;

public class IndexCollector {
	
	
	private Map<Integer, Map<MultiTableIndexKey, Map<String, TermDocument>>> _rows = null;
	
	public IndexCollector(){
		
		int capacity = TableManager.getMaxLevel() - TableManager.getMinLevel() + 2;
		_rows = new HashMap<Integer, Map<MultiTableIndexKey,Map<String,TermDocument>>>(capacity);
		_rows.put(1, new HashMap<MultiTableIndexKey, Map<String,TermDocument>>(LuceneHBaseInitializer.getCollectorIndexSize()));
		for(int i = TableManager.getMinLevel(); i <= TableManager.getMaxLevel(); i++)
			_rows.put(i, new HashMap<MultiTableIndexKey, Map<String,TermDocument>>(LuceneHBaseInitializer.getCollectorIndexSize()));
	}
		
	public void addDocument(IndexKey key, String docID, TermDocument td){
		
		Map<MultiTableIndexKey,Map<String,TermDocument>> row = _rows.get(key.getLevel());
		if(row == null)
			return;
		MultiTableIndexKey mtkey = new MultiTableIndexKey(key);
		Map<String, TermDocument> index = row.get(mtkey);
		if(index == null){
			index = new HashMap<String, TermDocument>();
			row.put(mtkey, index);
		}
		index.put(docID, td);
		if(row.size() >= LuceneHBaseInitializer.getCollectorIndexSize())
			writeToTable(row, key.getLevel());
	}
	
	public void addDocuments(IndexKey key, Map<String, TermDocument> docs){
		
		for(Map.Entry<String, TermDocument> entry : docs.entrySet())
			addDocument(key, entry.getKey(), entry.getValue());
	}	
		
	private void writeToTable(Map<MultiTableIndexKey,Map<String,TermDocument>> row, int level){
		
		IndexTableSupport.addMultiDocuments(row, level);
		row.clear();
	}
	
	public void finalize(){

		for(Map.Entry<Integer, Map<MultiTableIndexKey, Map<String, TermDocument>>> rowEntry : _rows.entrySet()){
			Map<MultiTableIndexKey,Map<String,TermDocument>> row = rowEntry.getValue();
			if(row.size() > 0)
				writeToTable(row, rowEntry.getKey());
		}
	}	
}