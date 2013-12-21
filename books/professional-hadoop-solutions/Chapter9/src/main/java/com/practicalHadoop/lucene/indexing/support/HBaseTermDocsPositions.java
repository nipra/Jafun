package com.practicalHadoop.lucene.indexing.support;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermPositions;

import com.practicalHadoop.lucene.document.TermDocument;
import com.practicalHadoop.lucene.indexing.HBaseIndexReader;


public class HBaseTermDocsPositions implements TermDocs, TermPositions {

	private static final Log LOG = LogFactory.getLog(HBaseTermDocsPositions.class);

	private HBaseTermEnum _termEnum;
	private Iterator<String> _docIterator;
	private String _currentDoc;
	private HBaseIndexReader _reader;
    private List<Integer> _termPositionArray;
    private int _termPosition;
	
	public HBaseTermDocsPositions(HBaseIndexReader reader){
		
		_reader = reader;
		_termEnum = new HBaseTermEnum(reader);
		if((_termEnum.getCurrentField() == null) || (_termEnum.getCurrentFieldTerms() == null)){
			_termEnum = null;
			return;
		}
		_docIterator = (_termEnum.getCurrentFieldTerms() == null) ? null : _termEnum.getCurrentFieldTerms().getDocumentIterator();
		_currentDoc = null;
		_termPositionArray = null;
		_termPosition = 0;
		
	}

	public HBaseTermDocsPositions(Term term, HBaseIndexReader reader){
		
		_reader = reader;
		_termEnum = new HBaseTermEnum(term,reader);
		if((_termEnum.getCurrentField() == null) || (_termEnum.getCurrentFieldTerms() == null)){
			_termEnum = null;
			return;
		}
		_docIterator = (_termEnum.getCurrentFieldTerms() == null) ? null : _termEnum.getCurrentFieldTerms().getDocumentIterator();
		_currentDoc = null;
		_termPositionArray = null;
		_termPosition = 0;
		
	}

	@Override
	public void close() throws IOException {

//		LOG.info("Closing TermDocumentPositions");
	}

	@Override
	public int doc() {

		if(_currentDoc == null)
			return 0;
		int doc = _reader.getDocNumber(_currentDoc);
//		LOG.info("Getiing Doc " +  doc);
		return doc;
	}

	@Override
	public int freq() {
		
		int freq = 0;
		if(_currentDoc != null)
			freq =  _termEnum.getCurrentFieldTerms().getTermDocument( _currentDoc).docFrequency;
//		LOG.info("Getting Frequency " +  freq);		
		return freq;
	}

	@Override
	public boolean next() throws IOException {

//		LOG.info("Getiing next doc ");		
		_termPosition = 0;
		while((_docIterator != null) && (_docIterator.hasNext())){
			_currentDoc = _docIterator.next();
			TermDocument td = _termEnum.getCurrentFieldTerms().getTermDocument( _currentDoc);
			_termPositionArray = td.docPositions;
			return true;
		}
		_currentDoc = null;
		_termPositionArray = null;
		return false;
	}

	@Override
	public int read(int[] docs, int[] freqs) throws IOException {

//		LOG.info("Reading docs, frequencies ");		
		int i = 0;
		if(_docIterator == null)
			return 0;
		if(_currentDoc == null)
			_currentDoc = (_docIterator.hasNext() ? _docIterator.next() : null);
		for(;(_currentDoc != null) && (i < docs.length) && (i < freqs.length); 
				_currentDoc = (_docIterator.hasNext() ? _docIterator.next() : null)){
			TermDocument td = _termEnum.getCurrentFieldTerms().getTermDocument( _currentDoc);
			docs[i] = _reader.getDocNumber(_currentDoc);
			freqs[i] = td.docFrequency;
			i++;
		}
		if(_currentDoc != null)
			_termPositionArray = _termEnum.getCurrentFieldTerms().
						getTermDocument( _currentDoc).docPositions;
		else
			_termPositionArray = null;

		return i;
	}

	@Override
	public void seek(Term term) throws IOException {

		_termEnum = new HBaseTermEnum(term, _reader);
		_docIterator = (_termEnum.getCurrentFieldTerms() == null) ? null : _termEnum.getCurrentFieldTerms().getDocumentIterator();
		_currentDoc = null;
		_termPositionArray = null;
		_termPosition = 0;
	}

	@Override
	public void seek(TermEnum termEnum) throws IOException {

		Term term = termEnum.term();
//		LOG.info("Seeking to the termEnum with term (" + term.field() + "," + term.text() + ")");		
		seek(term);
	}

	@Override
	public boolean skipTo(int target) throws IOException {

//		LOG.info("Skiping to the doc " + target);		
		 while (target > doc()) {
            if (!next())
                return false;
        }

        return true;
	}

	@Override
	public byte[] getPayload(byte[] data, int offset) throws IOException {

		return null;
	}

	@Override
	public int getPayloadLength() {

		return 0;
	}

	@Override
	public boolean isPayloadAvailable() {

		return false;
	}

	@Override
	public int nextPosition() throws IOException {

//		LOG.info("Getting next position ");		

		if((_termPositionArray == null) || (_termPosition >= _termPositionArray.size()))
            return -1;
        
        int pos = _termPositionArray.get(_termPosition++);

        return pos;
	}

	public TermEnum getTermEnum(){
		return _termEnum;
	}
}