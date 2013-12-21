package com.practicalHadoop.lucene.tables;

import java.util.StringTokenizer;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.lucene.index.Term;

import com.practicalHadoop.lucene.cache.Index;

public class IndexKey implements Comparable<IndexKey>{
	
	private static final String _delimiter = "|";

	private String _field = null;
	private String _term = null;
	private int _level = 1;
	private long _cell = 0;
	
	public IndexKey(String field, String term, int level, long cell){
		_field = field;
		_term = term;
		_level = level;
		_cell = cell;
	}

	public IndexKey(Index index, int level, long cell){
		_field = index.getField();
		_term = index.getTerm();
		_level = level;
		_cell = cell;
	}

	public IndexKey(Index index){
		_field = index.getField();
		_term = index.getTerm();
		_level = 1;
		_cell = 0;
	}

	public IndexKey(String field, String term){
		_field = field;
		_term = term;
		_level = 1;
		_cell = 0;
	}

	public IndexKey(Term term){
		_field = term.field();
		_term = term.text();
		_level = 1;
		_cell = 0;
	}

	public IndexKey(byte[] value){
		
		StringTokenizer tokenizer = new StringTokenizer(Bytes.toString(value), _delimiter);
		int current = 0;
		int nTokens = tokenizer.countTokens();
		while(tokenizer.hasMoreTokens()){
			if (nTokens > 2) {
				switch (current) {
					case 0:
					try {
						_level = Integer.parseInt(tokenizer.nextToken());
					} catch (NumberFormatException e) {
						_level = 1;
					}
						break;
					case 1:
					try {
						_cell = Long.parseLong(tokenizer.nextToken());
					} catch (NumberFormatException e) {
						_cell = 0;
					}
						break;
					case 2:
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
	
	public int getLevel() {
		return _level;
	}

	public long getCell() {
		return _cell;
	}

	public String getKey() {
		if(_level <= 1)
			return (_field + _delimiter + _term);
		return (_level + _delimiter  + _cell + _delimiter + _field + _delimiter + _term);
	}

	@Override
	public int compareTo(IndexKey ind) {

		if(ind == null)
			return -1;
		if(_level != ind.getLevel()){
			if(_level < ind.getLevel())
				return -1;
			return 1;
		}
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
		
		int hash = new Integer(_level).hashCode();
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
		if (obj instanceof IndexKey) {
			IndexKey other = (IndexKey) obj;
			return (_level == other.getLevel()) && (_cell == other.getCell()) && _field.equals(other.getField()) && _term.equals(other.getTerm());
		}
		return false;
	}
	
	@Override
	public String toString(){
		return getKey();
	}
}