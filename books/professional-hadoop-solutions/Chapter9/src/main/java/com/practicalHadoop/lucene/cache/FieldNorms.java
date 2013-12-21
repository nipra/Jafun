package com.practicalHadoop.lucene.cache;

import java.util.List;
import java.util.Map;

import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;

public class FieldNorms {

	private long _tl;
	private long _tlu;
	private Map<String, Byte> _norms;
	
	public FieldNorms(Map<String, Byte> norms){
		
		_tl = _tlu = System.currentTimeMillis();
		_norms = norms;
	}

	public Map<String, Byte> getNorms() {
		_tlu = System.currentTimeMillis();
		return _norms;
	}
	
	public void setNorms(Map<String, Byte> norms) {
		_tl = System.currentTimeMillis();
		_norms = norms;
	}

	public boolean isStale(){
		
		return ((System.currentTimeMillis() - _tl) > LuceneHBaseInitializer.getTTL());
	}	

	public boolean isInUse(){
		
		return ((System.currentTimeMillis() - _tlu) < LuceneHBaseInitializer.getTTNA());
	}
	
	public Byte getNorm(String id){
		_tlu = System.currentTimeMillis();
		return _norms.get(id);
	}

	public void addNorm(String id, byte norm){
		_norms.put(id, norm);
	}

	public void addNorms(Map<String,Byte> norms){
		for(Map.Entry<String, Byte> entry : norms.entrySet())
			_norms.put(entry.getKey(), entry.getValue());
	}

	public void deleteNorm(String id){
		_norms.remove(id);
	}

	public void deleteNorms(List<String> IDs){
		for(String ID : IDs)
			_norms.remove(ID);
	}	
}
