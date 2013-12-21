package com.practicalHadoop.lucene.indexing.support;

import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

import com.practicalHadoop.lucene.indexing.support.SpatialReaderFilter;
import com.practicalHadoop.lucene.cache.LuceneIndexCache;
import com.practicalHadoop.lucene.cache.TermDocuments;
import com.practicalHadoop.lucene.indexing.HBaseIndexReader;
import com.practicalHadoop.lucene.tables.IndexKey;


public class HBaseTermEnum extends TermEnum {

	private IndexKey _currentIndex;
	private TermDocuments _currentFieldTerms;
    private SpatialReaderFilter _filter;
	
	public HBaseTermEnum(HBaseIndexReader reader){
		
		_filter = reader.getReaderFilter();
		setDefaults();
	}
	
	private void setDefaults(){
		
		try {
			_currentIndex = LuceneIndexCache.getNextkey(null, _filter);
			_currentFieldTerms = LuceneIndexCache.getTermDocuments(_currentIndex.getIndex(), _filter);
		} catch (Exception e) {
			_currentIndex = null;
			_currentFieldTerms = null;
		}		
	} 
	
	public HBaseTermEnum(Term term, HBaseIndexReader reader){
		
		_filter = reader.getReaderFilter();
		if(term == null){
			setDefaults();
			return;
		}
			
		try {
			_currentIndex = new IndexKey(term);
			_currentFieldTerms = LuceneIndexCache.getTermDocuments(_currentIndex.getIndex(), _filter);
			if(_currentFieldTerms == null){
				_currentIndex = LuceneIndexCache.getNextkey(_currentIndex, _filter);
				_currentFieldTerms = LuceneIndexCache.getTermDocuments(_currentIndex.getIndex(), _filter);
			}
		} catch (Exception e) {
			_currentIndex = null;
			_currentFieldTerms = null;
		} 
	}
	
	@Override
	public void close() throws IOException {

	}

	@Override
	public int docFreq() {
		
		try{
			return LuceneIndexCache.getDocumentFrequency(_currentIndex.getIndex(), _filter);
		}
		catch(Exception e){
			return 0;
		}
	}

	@Override
	public boolean next() throws IOException {

		try {
			_currentIndex = LuceneIndexCache.getNextkey(_currentIndex, _filter);
			if(_currentIndex == null){
				_currentFieldTerms = null;
				return false;
			}
			_currentFieldTerms = LuceneIndexCache.getTermDocuments(_currentIndex.getIndex(), _filter);
			return true;

		} catch (Exception e) {
			_currentIndex = null;
			_currentFieldTerms = null;
			return false;
		} 
	}

	@Override
	public Term term() {
		
		if(_currentFieldTerms == null)
			return null;
		return new Term((_currentIndex == null) ? null : _currentIndex.getField(), (_currentIndex == null) ? null : _currentIndex.getTerm());
	}

	public String getCurrentField() {
		return (_currentIndex == null) ? null : _currentIndex.getField();
	}

	public String getCurrentTerm() {
		return (_currentIndex == null) ? null : _currentIndex.getTerm();
	}

	public TermDocuments getCurrentFieldTerms() {
		return _currentFieldTerms;
	}
}