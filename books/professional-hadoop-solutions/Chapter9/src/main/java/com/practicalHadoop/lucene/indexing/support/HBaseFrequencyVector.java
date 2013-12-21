package com.practicalHadoop.lucene.indexing.support;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.TermFreqVector;
import org.apache.lucene.index.TermPositionVector;
import org.apache.lucene.index.TermVectorOffsetInfo;

import com.practicalHadoop.lucene.cache.LuceneDocumentCache;
import com.practicalHadoop.lucene.document.TermDocumentFrequency;
import com.practicalHadoop.lucene.document.TermsOffset;
import com.practicalHadoop.lucene.tables.IndexKey;


public class HBaseFrequencyVector implements TermFreqVector, TermPositionVector {
	
    private String _field;
    private String[] _terms = null;
    private int[] _freqVec = null;
    private TermVectorOffsetInfo[][] _offsetVec = null;
    private int[][] _posVec = null;

    public HBaseFrequencyVector(String ID, String field) throws Exception{
    	
    	_field = field;
    	Map<IndexKey, TermDocumentFrequency> terms = LuceneDocumentCache.getDocumentTerms(ID);
    	if(terms == null)
    		return;
    	List<String> tms = new LinkedList<String>();
    	List<Integer> freq = new LinkedList<Integer>();
    	List<TermVectorOffsetInfo[]> offsets = new LinkedList<TermVectorOffsetInfo[]>();
    	List<List<Integer>> pos = new LinkedList<List<Integer>>();
    	for(Map.Entry<IndexKey, TermDocumentFrequency> entry : terms.entrySet()){
    		if(field.equals(entry.getKey().getField())){
    			tms.add(entry.getKey().getTerm());
    			TermDocumentFrequency term = entry.getValue();
    			freq.add(term.docFrequency);
    			TermVectorOffsetInfo[] coff = new TermVectorOffsetInfo[term.docOffsets.size()];
    			int i = 0;
    			for(TermsOffset to : term.docOffsets){
    				coff[i++] = new TermVectorOffsetInfo(to.startOffset, to.endOffset);
    			}
    			offsets.add(coff);
    			pos.add(term.docPositions);
    		}
     	}
		_terms = new String[tms.size()];
		int i = 0;
		for(String t : tms)
			_terms[i++] = t;
		_freqVec = new int[freq.size()];
		i = 0;
		for(int f : freq)
			_freqVec[i++] = f;
		_posVec = new int[pos.size()][];
		i = 0;
		for(List<Integer> pl : pos){
			int[] p = new int[pl.size()];
			int j = 0;
			for(int v : pl)
				p[j++] = v;
			_posVec[i++] = p;
		}
		_offsetVec = new TermVectorOffsetInfo[offsets.size()][];
		i = 0;
		for(TermVectorOffsetInfo[] o : offsets)
			_offsetVec[i++] = o;
	}

	@Override
	public String getField() {
		
		return _field;
	}

	@Override
	public int[] getTermFrequencies() {

		return _freqVec;
	}

	@Override
	public String[] getTerms() {

		return _terms;
	}

	@Override
	public int indexOf(String term) {

		return Arrays.binarySearch(_terms, term);
	}

	@Override
	public int[] indexesOf(String[] terms, int start, int len) {

		int[] res = new int[terms.length];

		for(int i=0; i<terms.length; i++){
			res[i] = indexOf(terms[i]);
		}

		return res;
	}

	@Override
	public int size() {

		return _terms.length;
	}

	@Override
	public TermVectorOffsetInfo[] getOffsets(int index) {

		if((_offsetVec == null) || (_offsetVec.length == 0) || (index < _offsetVec.length))			
			return _offsetVec[index];
		return null;
	}

	@Override
	public int[] getTermPositions(int index) {
		if((_posVec == null) || (_posVec.length == 0) || (index < _posVec.length))			
			return _posVec[index];
		return null;
	}

}