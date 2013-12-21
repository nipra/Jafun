package com.practicalHadoop.lucene.cache;

import java.util.List;

public interface TermsDocumentsSet {
	
	public TermDocuments getTermDocuments(boolean create, List<Long> tileIds); 
	public TermDocuments getTermDocuments(boolean create, long tileId); 
	public void removeTermDocuments(long tileId); 
}