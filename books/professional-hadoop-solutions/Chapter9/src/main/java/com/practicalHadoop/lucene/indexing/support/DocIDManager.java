package com.practicalHadoop.lucene.indexing.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class DocIDManager {

    private Map<String, Integer> _docIdToDocIndex = new HashMap<String, Integer>();
    private Map<Integer, String> _docIndexToDocId = new HashMap<Integer, String>();
    private int _uniqueId = 0;


    public int getDocNum(String ID){
    	Integer idn = _docIdToDocIndex.get(ID);
    	if(idn == null){
    		int dn = _uniqueId++;
    		_docIdToDocIndex.put(ID, dn);
    		_docIndexToDocId.put(dn, ID);
    		return dn;
    	}
    	return idn;
    }
    
    public String getDocID(int id){

    	return _docIndexToDocId.get(id);
    }

    public Set<Integer> getDocNumbers(){

    	return _docIndexToDocId.keySet();
    }
}
