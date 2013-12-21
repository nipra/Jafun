package com.practicalHadoop.lucene.cache;

import java.util.StringTokenizer;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.lucene.index.Term;

public class Index implements Comparable<Index>{
	
	private static final String _delimiter = "|";

	private String _field = null;
	private String _term = null;
	
	public Index(String field, String term){
		_field = field;
		_term = term;
	}

	public Index(Term term){
		_field = term.field();
		_term = term.text();
	}

	public Index(byte[] value){
		
		StringTokenizer tokenizer = new StringTokenizer(Bytes.toString(value), _delimiter);
		int current = 0;
		while(tokenizer.hasMoreTokens()){			
			if(current++ == 0){
				_field = tokenizer.nextToken();
				continue;
			}
			else{
				_term = tokenizer.nextToken();
				break;
			}
		}
	}

	public String getField() {
		return _field;
	}

	public String getTerm() {
		return _term;
	}
	
	public String getKey() {
		return (_field + _delimiter + _term);
	}

	@Override
	public int compareTo(Index ind) {

		if(ind == null)
			return -1;
		if(_field.equals(ind.getField()))
			return (_term.compareTo(ind.getTerm()));
		return _field.compareTo(ind.getField());
	}
	
	@Override
	public int hashCode() {
		
		return _field.hashCode() * 31 + _term.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Index) {
			Index other = (Index) obj;
			return _field.equals(other.getField()) && _term.equals(other.getTerm());
		}
		return false;
	}
}