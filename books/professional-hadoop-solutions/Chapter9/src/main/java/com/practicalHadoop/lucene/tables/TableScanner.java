package com.practicalHadoop.lucene.tables;

import java.util.Iterator;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

public class TableScanner {
	
	private HTableInterface _table;
	private ResultScanner _scanner;
	private TableManager _tManager;
	private Iterator<Result> _iterator;
	
	public TableScanner(TableManager tManager, HTableInterface table, ResultScanner scanner){
		
		_table = table;
		_scanner = scanner;
		_iterator = scanner.iterator();
		_tManager = tManager;
		
	}

	public Iterator<Result> getScanner() {
		return _iterator;
	}

	public void close(){
		
		_scanner.close();
		_tManager.releaseTable(_table);
	}
}