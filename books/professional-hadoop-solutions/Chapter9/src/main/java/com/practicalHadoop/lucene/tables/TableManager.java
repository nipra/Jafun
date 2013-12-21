package com.practicalHadoop.lucene.tables;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.util.Bytes;

public class TableManager {
	
	// Levels
	private static final int _minLevel = 10;
	private static final int _maxLevel = 24;
	
	// Purposes tables
	private static final String _indexTablePurpose = "index";
	private static String[] _indexLevelTablePurpose = null;
	private static final String _documentsTablePurpose = "documents";
	private static final String _normsTablePurpose = "norms";

	// Purposes families
	private static final String _indexTableDocumentsPurpose = "documents";
	private static final String _documentsTableFieldsPurpose = "fields";
	private static final String _documentsTableTermsPurpose = "terms";
	private static final String _normsTableNormsPurpose = "norms";

	// Tables
	private static byte[] _indexTable;
	private static byte[][] _indexLevelTable;
	private static byte[] _documentsTable;
	private static byte[] _normsTable;

	//Families
	private static byte[] _indexTableDocuments;
	private static byte[] _documentsTableFields;
	private static byte[] _documentsTableTerms;
	private static byte[] _normsTableNorms;
	
	// Pool
	private HTablePool _tPool = null;
	private Configuration _config = null;
	private int _poolSize = Integer.MAX_VALUE;
	
	// Instance
	private static TableManager _instance = null;
	
	// Static initializer
	static{
		int nlevels = _maxLevel - _minLevel + 1;
		_indexLevelTable = new byte[nlevels][];
		_indexLevelTablePurpose = new String[nlevels];
		for(int i = 0; i < nlevels; i ++)
			_indexLevelTablePurpose[i] = _indexTablePurpose + (i+_minLevel);
			
	}
	
	public static int getMinLevel() {
		return _minLevel;
	}

	public static int getMaxLevel() {
		return _maxLevel;
	}

	public static String getIndexTablePurpose() {
		return _indexTablePurpose;
	}

	public static String[] getIndexLevelTablePurpose() {
		return _indexLevelTablePurpose;
	}

	public static String getDocumentsTablePurpose() {
		return _documentsTablePurpose;
	}

	public static String getNormsTablePurpose() {
		return _normsTablePurpose;
	}

	public static String getIndexTableDocumentsPurpose() {
		return _indexTableDocumentsPurpose;
	}

	public static String getDocumentsTableFieldsPurpose() {
		return _documentsTableFieldsPurpose;
	}

	public static String getDocumentsTableTermsPurpose() {
		return _documentsTableTermsPurpose;
	}

	public static String getNormsTableNormsPurpose() {
		return _normsTableNormsPurpose;
	}

	public static void setIndexTable(String indexTable, int level) {
		if(level < 0)
			_indexTable = Bytes.toBytes(indexTable);
		else{
			if((level >= _minLevel) && (level <= _maxLevel)){
				int i = level - _minLevel;
				_indexLevelTable[i] = Bytes.toBytes(indexTable);
			}
		}
	}

	public static void setDocumentsTable(String documentsTable) {
		_documentsTable = Bytes.toBytes(documentsTable);
	}

	public static void setNormsTable(String normsTable) {
		_normsTable = Bytes.toBytes(normsTable);
	}

	public static void setIndexTableDocumentsFamily(String indexTableDocuments) {
		_indexTableDocuments = Bytes.toBytes(indexTableDocuments);
	}

	public static void setDocumentsTableFields(String documentsTableFields) {
		_documentsTableFields = Bytes.toBytes(documentsTableFields);
	}

	public static void setDocumentsTableTermsFamily(String documentsTableTerms) {
		_documentsTableTerms = Bytes.toBytes(documentsTableTerms);
	}

	public static void setNormsTableNormsFamily(String normsTableNorms) {
		_normsTableNorms = Bytes.toBytes(normsTableNorms);
	}

	public static byte[] getIndexTableDocumentsFamily() {
		return _indexTableDocuments;
	}

	public static byte[] getDocumentsTableFieldsFamily() {
		return _documentsTableFields;
	}

	public static byte[] getDocumentsTableTermsFamily() {
		return _documentsTableTerms;
	}

	public static byte[] getNormsTableNormsFamily() {
		return _normsTableNorms;
	}
	
	public static void initPool(Configuration config, int mSize){
		
		_instance = new TableManager(config, mSize);
	}
	
	// Disallow creation
	private TableManager(){}
	
	public static synchronized TableManager getInstance() throws NotInitializedException{
		
		if(_instance == null)
			throw new NotInitializedException();
		return _instance;
	}


	private TableManager(Configuration config, int poolSize){
		
		if(poolSize > 0)
			_poolSize = poolSize;
		_config = config;
		_tPool = new HTablePool(_config, _poolSize);
	}
	
	public void resetTPool(){
		
		_tPool = new HTablePool(_config, _poolSize);
	}
	
	public HTableInterface getIndexTable(int level) {
		
		if(level == 1)
			return _tPool.getTable(_indexTable);
		else
			return _tPool.getTable(_indexLevelTable[level-_minLevel]);
	}

	public HTableInterface getDocumentsTable(){
		
		return _tPool.getTable(_documentsTable);
	}

	public HTableInterface getNormsTable(){
		
		return _tPool.getTable(_normsTable);
	}

	public void releaseTable(HTableInterface t){
		
		try {
			t.close();
		} catch (IOException e) {}
	} 
}