package com.practicalHadoop.lucene.initializer;

import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;

import com.practicalHadoop.lucene.cache.CacheUpdater;
import com.practicalHadoop.lucene.spatial.GeometricShapeHelper;
import com.practicalHadoop.lucene.tables.DocumentsTableSupport;
import com.practicalHadoop.lucene.tables.IndexTableSupport;
import com.practicalHadoop.lucene.tables.NormsTableSupport;
import com.practicalHadoop.lucene.tables.TableCreator;
import com.practicalHadoop.lucene.tables.TableManager;
import com.practicalHadoop.lucene.tables.configuration.ColumnFamily;
import com.practicalHadoop.lucene.tables.configuration.TableType;
import com.practicalHadoop.lucene.tables.configuration.TablesType;

public class LuceneHBaseInitializer {
	
	private static final String CONFIGFILE = "SandBox_Cloud_Config.xml";
	private static final String TABLEJAXBPACKAGE = "com.practicalHadoop.lucene.tables.configuration";
	private static final String TABLEDEFFILE = "HBASETables.xml";
	// Updater parameters
	private static final long _nap = 5*60*1000; // 5 mins - update timeout
	public static final long _ttl = 2*60*1000;  // 2 mins - in use
	public static final long _ttna = 5*60*1000; // 5 mins - not used
	// Number of documents
	private static final int _numDocs = 50000;
	// Collector
	private static final int COLLECTORDOCUMENTERSIZE = 100;
	private static final int COLLECTORINDEXERSIZE = 500;
	// Geometry parameter for tiling
	private static final int MAX_TILES = 15;
	
	private static CacheUpdater updater = null;
	
	private LuceneHBaseInitializer(){}
		
	public static long getTTL() {
		return _ttl;
	}

	public static long getTTNA() {
		return _ttna;
	}

	public static long getUpdaterTimeout() {
		return _nap;
	}

	public static int getNumberDocuments() {
		return _numDocs;
	}

	public static int getCollectorDocumentSize() {
		return COLLECTORDOCUMENTERSIZE;
	}

	public static int getCollectorIndexSize() {
		return COLLECTORINDEXERSIZE;
	}

	public static Configuration init() throws Exception{
		
		Configuration.addDefaultResource(CONFIGFILE);
		Configuration configuration = HBaseConfiguration.create(new Configuration());

		// Get table definitions list
		JAXBContext jc = JAXBContext.newInstance(TABLEJAXBPACKAGE);
		Unmarshaller u = jc.createUnmarshaller();
		JAXBElement<TablesType> resultValue = 
			(JAXBElement<TablesType>)u.unmarshal(LuceneHBaseInitializer.class.getClassLoader().getResourceAsStream(TABLEDEFFILE));
		TablesType tableDefs = resultValue.getValue();

		// Set table names
		int tablesMatched = 0;
		for (TableType table : tableDefs.getTable()) {
			if(table.getPurpose().equals(TableManager.getIndexTablePurpose())){
				tablesMatched++;
				TableManager.setIndexTable(table.getName(), -1);
				boolean set = false;
				for(ColumnFamily familly : table.getColumnFamily()){
					if(familly.getPurpose().equals(TableManager.getIndexTableDocumentsPurpose())){
						TableManager.setIndexTableDocumentsFamily(familly.getName());
						set = true;
						break;
					}
				}
				if(!set)
					throw new Exception("Not all table families defined");				
				continue;
			}
			if(table.getPurpose().equals(TableManager.getDocumentsTablePurpose())){
				tablesMatched++;
				TableManager.setDocumentsTable(table.getName());
				int famMatched = 0;
				for(ColumnFamily familly : table.getColumnFamily()){
					if(familly.getPurpose().equals(TableManager.getDocumentsTableFieldsPurpose())){
						TableManager.setDocumentsTableFields(familly.getName());
						famMatched++;
						continue;
					}
					if(familly.getPurpose().equals(TableManager.getDocumentsTableTermsPurpose())){
						TableManager.setDocumentsTableTermsFamily(familly.getName());
						famMatched++;
						continue;
					}
				}
				if(famMatched < 2)
					throw new Exception("Not all table families defined");
				continue;
			}
			if(table.getPurpose().equals(TableManager.getNormsTablePurpose())){
				tablesMatched++;
				TableManager.setNormsTable(table.getName());
				boolean set = false;
				for(ColumnFamily familly : table.getColumnFamily()){
					if(familly.getPurpose().equals(TableManager.getNormsTableNormsPurpose())){
						TableManager.setNormsTableNormsFamily(familly.getName());
						set = true;
						break;
					}
				}
				if(!set)
					throw new Exception("Not all table families defined");
				continue;
			}
			String[] levelPurpose = TableManager.getIndexLevelTablePurpose();
			int level = TableManager.getMinLevel();
			for (String purpose : levelPurpose) {
				if (table.getPurpose().equals(purpose)) {
					tablesMatched++;
					TableManager.setIndexTable(table.getName(), level);
					boolean set = false;
					for (ColumnFamily familly : table.getColumnFamily()) {
						if (familly.getPurpose().equals(TableManager.getIndexTableDocumentsPurpose())) {
							TableManager.setIndexTableDocumentsFamily(familly.getName());
							set = true;
							break;
						}
					}
					if (!set)
						throw new Exception("Not all table families defined");
					break;
				}
				level++;
			}
		}
		if(tablesMatched < 3)
			throw new Exception("Not all tables defined");
		
		// Get tables list
		List<HTable> tables = TableCreator.getTables(tableDefs, configuration);
		TableManager.initPool(configuration, 1);
		IndexTableSupport.init();
		DocumentsTableSupport.init();
		NormsTableSupport.init();
		
		// Set geometry search parameter
		GeometricShapeHelper.setMAX_TILES(MAX_TILES);
		
		//Start cache Updater		
		updater = new CacheUpdater();
		Thread updaterThread = new Thread(updater);
		updaterThread.setPriority(Thread.MIN_PRIORITY);
		updaterThread.start();
		return configuration;
	}
	
	public static void finalizeLucene(){
		updater.complete();
	}
}
