package com.practicalHadoop.lucene.tables;

import java.util.StringTokenizer;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.lucene.index.Term;

import com.practicalHadoop.lucene.cache.Index;

public class MultiTableIndexKey implements Comparable<MultiTableIndexKey>{
	
	private static final String _delimiter = "|";

	private String _field = null;
	private String _term = null;
	private long _cell = -1;
	
	public MultiTableIndexKey(String field, String term, long cell){
		_field = field;
		_term = term;
		_cell = cell;
	}

	public MultiTableIndexKey(Index index, long cell){
		_field = index.getField();
		_term = index.getTerm();
		_cell = cell;
	}

	public MultiTableIndexKey(Index index){
		_field = index.getField();
		_term = index.getTerm();
		_cell = -1;
	}

	public MultiTableIndexKey(String field, String term){
		_field = field;
		_term = term;
		_cell = -1;
	}

	public MultiTableIndexKey(Term term){
		_field = term.field();
		_term = term.text();
		_cell = -1;
	}

	public MultiTableIndexKey(IndexKey key){
		_field = key.getField();
		_term = key.getTerm();
		_cell = (key.getLevel() > 1) ? key.getCell() : -1;
	}

	public MultiTableIndexKey(byte[] value){
		
		StringTokenizer tokenizer = new StringTokenizer(Bytes.toString(value), _delimiter);
		int current = 0;
		int nTokens = tokenizer.countTokens();
		while(tokenizer.hasMoreTokens()){
			if (nTokens > 2) {
				switch (current) {
					case 0:
					try {
						_cell = Long.parseLong(tokenizer.nextToken());
					} catch (NumberFormatException e) {
						_cell = -1;
					}
						break;
					case 1:
						_field = tokenizer.nextToken();
						break;
					default:
						_term = tokenizer.nextToken();
						break;
				}
			}
			else{
				switch (current) {
					case 0:
						_field = tokenizer.nextToken();
						break;
					default:
						_term = tokenizer.nextToken();
						break;
				}
			}
			current++;
		}
	}

	public String getField() {
		return _field;
	}

	public String getTerm() {
		return _term;
	}
	
	public Index getIndex(){
		
		return new Index(_field, _term); 
	}
	

	public long getCell() {
		return _cell;
	}

	public String getKey() {
		if(_cell <= 0)
			return (_field + _delimiter + _term);
		return (_cell + _delimiter + _field + _delimiter + _term);
	}

	@Override
	public int compareTo(MultiTableIndexKey ind) {

		if(ind == null)
			return -1;
		if(_cell != ind.getCell()){
			if(_cell < ind.getCell())
				return -1;
			return 1;
		}
		if(_field.equals(ind.getField()))
			return (_term.compareTo(ind.getTerm()));
		return _field.compareTo(ind.getField());
	}
	
	@Override
	public int hashCode() {
		
		int hash = 0;
		if(_cell > 0)
			hash = 31 * hash + new Long(_cell).hashCode();
		if(_term != null)
			hash = 31 * hash + _term.hashCode();
		if(_field != null)
			hash = 31 * hash + _field.hashCode();
		return hash;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MultiTableIndexKey) {
			MultiTableIndexKey other = (MultiTableIndexKey) obj;
			return (_cell == other.getCell()) && _field.equals(other.getField()) && _term.equals(other.getTerm());
		}
		return false;
	}
	
	@Override
	public String toString(){
		return getKey();
	}
}