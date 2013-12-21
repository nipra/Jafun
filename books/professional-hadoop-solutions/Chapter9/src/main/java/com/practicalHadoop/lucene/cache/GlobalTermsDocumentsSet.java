package com.practicalHadoop.lucene.cache;

import java.util.List;

public class GlobalTermsDocumentsSet implements TermsDocumentsSet {
	
	TermDocuments _termDocuments = null;

	public GlobalTermsDocumentsSet(){
		
		_termDocuments = null;
	}
	
	public GlobalTermsDocumentsSet(TermDocuments termDocuments){
		
		_termDocuments = termDocuments;
	}
	
	public void setTermDocuments(TermDocuments termDocuments){
		
		_termDocuments = termDocuments;
	}
	
	@Override
	public TermDocuments getTermDocuments(boolean create, List<Long> tileIds) {

		if(create)
			if(_termDocuments == null)
				_termDocuments = new TermDocuments();
		return _termDocuments;
	}

	@Override
	public TermDocuments getTermDocuments(boolean create, long tileId) {
		if(create)
			if(_termDocuments == null)
				_termDocuments = new TermDocuments();
		return _termDocuments;
	}

	@Override
	public void removeTermDocuments(long tileId) {
		
		_termDocuments = null;
	}
}
