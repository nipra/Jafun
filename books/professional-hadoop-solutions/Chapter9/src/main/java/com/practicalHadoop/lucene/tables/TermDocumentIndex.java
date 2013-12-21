package com.practicalHadoop.lucene.tables;

import com.practicalHadoop.lucene.cache.TermDocuments;

public class TermDocumentIndex {
	
	private IndexKey _index;
	private TermDocuments _td;
	
	public TermDocumentIndex(IndexKey index, TermDocuments td){
		
		_index = index;
		_td = td;
	}

	public IndexKey getIndex() {
		return _index;
	}

	public TermDocuments getTd() {
		return _td;
	}
}
