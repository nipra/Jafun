package com.practicalHadoop.lucene.indexing.support;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;

import com.practicalHadoop.lucene.indexing.HBaseIndexReader;
import com.practicalHadoop.lucene.tables.DocumentsTableSupport;
import com.practicalHadoop.lucene.tables.TableScanner;


public class HBaseAllTermDocsPositions implements TermDocs{

	private static final Log LOG = LogFactory.getLog(HBaseAllTermDocsPositions.class);

	private TableScanner _docIterator;
	private HBaseIndexReader _reader;
	private String _doc;

	public HBaseAllTermDocsPositions(HBaseIndexReader reader)throws Exception{

		_reader = reader;
		_docIterator = DocumentsTableSupport.getTableScanner();
	}

	public void seek(Term term) throws IOException {
		if (term == null) {
			_docIterator.close();
			try {
				_docIterator = DocumentsTableSupport.getTableScanner();
			} catch (Exception e) {
				throw new IOException(e);
			}
		} else {
			throw new UnsupportedOperationException();
		}
	}

	public void seek(TermEnum termEnum) throws IOException {
		throw new UnsupportedOperationException();
	}

	public int doc() {
		return _reader.getDocNumber(_doc);
	}

	public int freq() {
		return 1;
	}

	public boolean next() throws IOException {
		
		if(_docIterator.getScanner().hasNext()){
			_doc = _docIterator.getScanner().next().raw().toString();
			return true;
		}
		_docIterator.close();
		return false;
	}

	public int read(int[] docs, int[] freqs) throws IOException {
		final int length = docs.length;
		int i = 0;
		while (i < length && _docIterator.getScanner().hasNext()) {
			docs[i] = _reader.getDocNumber(_docIterator.getScanner().next().raw().toString());
			freqs[i] = 1;
		}
		if(!_docIterator.getScanner().hasNext())
			_docIterator.close();
		return i;
	}

	public boolean skipTo(int target) throws IOException {

		while(_reader.getDocNumber(_doc) < target){
			if(!_docIterator.getScanner().hasNext()){
				_docIterator.close();
				return false;
			}
			_doc = _docIterator.getScanner().next().raw().toString();
		}
			
		return true;
	}

	public void close() throws IOException {
	}
}