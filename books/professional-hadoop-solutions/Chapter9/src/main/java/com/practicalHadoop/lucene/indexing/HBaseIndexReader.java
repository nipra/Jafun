package com.practicalHadoop.lucene.indexing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FieldSelector;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.FieldInfos;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositions;
import org.apache.lucene.index.TermVectorMapper;
import org.apache.lucene.search.DefaultSimilarity;

import com.practicalHadoop.lucene.cache.LuceneDocumentCache;
import com.practicalHadoop.lucene.cache.LuceneIndexCache;
import com.practicalHadoop.lucene.indexing.support.DocIDManager;
import com.practicalHadoop.lucene.indexing.support.HBaseAllTermDocsPositions;
import com.practicalHadoop.lucene.indexing.support.HBaseFrequencyVector;
import com.practicalHadoop.lucene.indexing.support.HBaseTermDocsPositions;
import com.practicalHadoop.lucene.indexing.support.HBaseTermEnum;
import com.practicalHadoop.lucene.indexing.support.SpatialReaderFilter;


public class HBaseIndexReader extends IndexReader {

	private static final Log LOG = LogFactory.getLog(HBaseIndexReader.class);
	private  static final byte DEFAULT_NORM = DefaultSimilarity.encodeNorm(1.0f);

    private DocIDManager _docIdManager = null;
    private SpatialReaderFilter _readerFilter = null;
        
    public HBaseIndexReader(){
    	
    	_readerFilter = null;
    	_docIdManager = new DocIDManager();
    }
    
    public IndexReader reopen() throws CorruptIndexException, IOException {

    	_readerFilter = null;
    	_docIdManager = new DocIDManager();
        return this;
    }

    public int getDocNumber(String ID){
    	
    	return _docIdManager.getDocNum(ID);
    }
    
    public String getDocID(int id){

    	return _docIdManager.getDocID(id);
    }
    
    public void setReaderFilter(SpatialReaderFilter f){
    	
    	_readerFilter = f;
    }
    
    public SpatialReaderFilter getReaderFilter(){
    	
    	return _readerFilter;
    }
    
	@Override
	protected void doClose() throws IOException {

		LOG.info("Closing reader");
		return;
	}

	@Override
	protected void doCommit(Map<String, String> arg0) throws IOException {

		LOG.info("Committing reader");
		return;
	}

	@Override
	protected void doDelete(int arg0) throws CorruptIndexException, IOException {
		// not applicable
		return;
	}

	@Override
	protected void doSetNorm(int arg0, String arg1, byte arg2) throws CorruptIndexException, IOException {
		// not applicable
		return;
	}

	@Override
	protected void doUndeleteAll() throws CorruptIndexException, IOException {
		// not applicable
		return;
	}

	@Override
	public int docFreq(Term term) throws IOException {
		
//		LOG.info("Getting Frequency for term (" + term.field() + "," + term.text() +")");
		try {
			return LuceneIndexCache.getDocumentFrequency(term.field(), term.text(), _readerFilter);
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Document document(int docNumber, FieldSelector arg1)
			throws CorruptIndexException, IOException {

//		LOG.info("Getting document by ID " + docNumber);
		String ID = getDocID(docNumber);
		try {
			return LuceneDocumentCache.getDocument(ID);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	public List<Document> documents(List<Integer> docNumbers) throws CorruptIndexException, IOException {

		List<String> ids = new ArrayList<String>(docNumbers.size());
		for(int dn : docNumbers)
			ids.add(getDocID(dn));
		try {
			return LuceneDocumentCache.getDocuments(ids);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

/*
	@Override
	public Collection<String> getFieldNames(FieldOption arg0) {

		// For now do as Lucandra
		return Arrays.asList(new String[] {});
	}*/
	@Override
	public FieldInfos getFieldInfos() {
		return null;
	}

	@Override
	public TermFreqVector getTermFreqVector(int docNumber, String field)
			throws IOException {

		String ID = getDocID(docNumber);
		try {
			return new HBaseFrequencyVector(ID, field);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	@Override
	public void getTermFreqVector(int arg0, TermVectorMapper arg1)
			throws IOException {
        throw new RuntimeException();

	}

	@Override
	public void getTermFreqVector(int docNumber, String field, TermVectorMapper arg2)
			throws IOException {
		throw new RuntimeException();
	}

	@Override
	public TermFreqVector[] getTermFreqVectors(int docNumber) throws IOException {

//		LOG.info("Getting TermFreqVector for document");
//		Collection<String> fields = getFieldNames(null);  
		Collection<String> fields = Arrays.asList(new String[] {});  
		TermFreqVector[] vectors = new TermFreqVector[fields.size()];
		int i = 0;
		for(String field : fields)
			vectors[i++] = getTermFreqVector(docNumber, field);
		return vectors;
	}

	@Override
	public boolean hasDeletions() {

		return false;
	}

	@Override
	public boolean isDeleted(int docNum) {

		return false;
	}

	@Override
	public int maxDoc() {

		int numdocs = LuceneDocumentCache.getNumDocs();
//		LOG.info("Getting Max Docs " + numdocs);
		return numdocs;
	}

	@Override
	public byte[] norms(String field) throws IOException {
		
	    byte[] result = new byte[maxDoc() + 1];
	    Arrays.fill(result, DEFAULT_NORM);

		return result;
	}

	@Override
	public void norms(String field, byte[] bytes, int offset) throws IOException {

		return;
	}

	@Override
	public int numDocs() {

		int numdocs = LuceneDocumentCache.getNumDocs();
//		LOG.info("Getting Number Docs " + numdocs);
		return numdocs;
	}

	@Override
	public TermDocs termDocs() throws IOException {

//		LOG.info("Getting TermDocs");
		return new HBaseTermDocsPositions(this);
	}

	@Override
	public TermPositions termPositions() throws IOException {
		
//		LOG.info("Getting TermPositions");
		return new HBaseTermDocsPositions(this);
	}

	@Override
	public TermEnum terms() throws IOException {

//		LOG.info("Getting Term Enumeration");
		return new HBaseTermEnum(this);
	}

	@Override
	public TermEnum terms(Term term) throws IOException {

//		LOG.info("Getting Term Enumeration for Terms : (" + term.field() + "," + term.text() +")");
		return new HBaseTermEnum(term, this);
	}

	@Override
	public TermDocs termDocs(Term term) throws IOException {
		
		if(term == null)
			try {
				return new HBaseAllTermDocsPositions(this);
			} catch (Exception e) {
				return null;
			}
		HBaseTermDocsPositions termDocs = new HBaseTermDocsPositions(term, this);
		if(termDocs.getTermEnum() == null)
			return null;
		return termDocs;
	}
}
