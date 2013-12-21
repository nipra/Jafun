package com.practicalHadoop.lucene.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.practicalHadoop.lucene.document.TermDocument;
import com.practicalHadoop.lucene.indexing.support.SpatialReaderFilter;
import com.practicalHadoop.lucene.cache.TermDocuments;
import com.practicalHadoop.lucene.cache.TermsDocumentsSet;
import com.practicalHadoop.lucene.tables.IndexCollector;
import com.practicalHadoop.lucene.tables.IndexKey;
import com.practicalHadoop.lucene.tables.IndexTableSupport;
import com.practicalHadoop.lucene.tables.TermDocumentIndex;

public class LuceneIndexCache{
	
	private static final byte[] _lowestKey = new String("").getBytes();

	/**
	 * Data structure for memory indexes
	 */

	private static Map<Index, Map<Integer, TermsDocumentsSet>> _terms = 
		Collections.synchronizedMap(new HashMap<Index, Map<Integer, TermsDocumentsSet>>(100, 0.7f));

	private LuceneIndexCache(){}

	public static void addDocuments (IndexCollector collector, String field, String term, int level, long tileID, Map<String, TermDocument> docs)throws Exception{
		
		Index index = new Index(field, term);
		addDocuments(collector, index, level, tileID, docs);
	}
	
	public static void addDocuments (IndexCollector collector, Index index, int level, long tileID, Map<String, TermDocument> docs)throws Exception{
		
		collector.addDocuments(new IndexKey(index, level, tileID), docs);
	}
	
	public static void addDocument(IndexCollector collector,String field, String term, int level, long tileID, String id, TermDocument td)throws Exception{

		Index index = new Index(field, term);
		addDocument(collector, index, level, tileID, id, td);
	}
	
	public static void addDocument(IndexCollector collector,Index index, int level, long tileID, String id, TermDocument td)throws Exception{

		collector.addDocument(new IndexKey(index, level, tileID), id, td);
	}
	
	public static void addDocument(IndexCollector collector,Index index, int level, List<Long> tileIDs, String id, TermDocument td)throws Exception{

		if(tileIDs == null)
			addDocument(collector,index, level, 0, id, td);
		else{
			for(long tileID : tileIDs)
				addDocument(collector, index, level, tileID, id, td);
		}
	}
	
	private static boolean toBeRemoved(TermDocuments term, int level, long tile, Index index, TermsDocumentsSet termSet){
		
		if(term == null)
			return false;
		if(!term.isInUse()){
			if(term.isStale()){
				// update it now
				IndexKey key = new IndexKey(index, level, tile);
				try {
					TermDocuments update = IndexTableSupport.getIndex(key);
					term.update(update);
				} catch (Exception e) {
//					termSet.removeTermDocuments(tile);
				}
			}
		}
		else{
			if(term.isStale())
				return true;
		}
		return false;
	}
	
	public static void refresh(){

		List<IndexKey> removed = new ArrayList<IndexKey>();
		for(Map.Entry<Index, Map<Integer, TermsDocumentsSet>> entry : _terms.entrySet()){
			Index index = entry.getKey();
			for(Map.Entry<Integer, TermsDocumentsSet> levelEntry : entry.getValue().entrySet()){
				int level = levelEntry.getKey();
				TermsDocumentsSet termSet = levelEntry.getValue();
				if(level == 1){
					GlobalTermsDocumentsSet gTermSet = (GlobalTermsDocumentsSet)termSet;
					TermDocuments term = gTermSet.getTermDocuments(false, null);
					if(toBeRemoved(term, level, 0, index, gTermSet))
						removed.add(new IndexKey(index, 1, 0));
				}
				else{
					SpatialTermsDocumentsSet sTermSet = (SpatialTermsDocumentsSet)termSet;
					Set<Long> tileIDs = sTermSet.getTermDocumentsCells();
					for(long tileID : tileIDs){
						TermDocuments term = sTermSet.getTermDocuments(false, tileID);
						if(toBeRemoved(term, level, 0, index, sTermSet))
							removed.add(new IndexKey(index, level, tileID));
					}
				}
			}
		}

		for(IndexKey index : removed){
			Map<Integer, TermsDocumentsSet> setMap = _terms.get(index.getIndex());
			if(setMap == null)
				continue;
			TermsDocumentsSet set = setMap.get(index.getLevel());
			if(set == null)
				continue;
			set.removeTermDocuments(index.getCell());
		}
	}
	
	public static void deleteDocument(String field, String term, String docID)throws Exception{
		
		IndexKey key = new IndexKey(field, term);
		IndexTableSupport.deleteDocument(key, docID);

	}

	public static void deleteDocument(String field, String term, int level, List<Long> tileIds, String docID)throws Exception{
		
		for (long tileID : tileIds) {
			IndexKey key = new IndexKey(field, term, level, tileID);
			IndexTableSupport.deleteDocument(key, docID);
		}
	}

	public static Iterator<String> getDocIterator(String field, String term, SpatialReaderFilter filter) throws Exception{

		Index index = new Index(field, term);
		return getDocIterator(index, filter);
	}
	
	public static TermDocuments getTermDocuments(Index index, SpatialReaderFilter filter) throws Exception{

		Map<Integer, TermsDocumentsSet> tDocSet = _terms.get(index);
		if(tDocSet == null){
			// see if a global one exists
			tDocSet = Collections.synchronizedMap(new HashMap<Integer, TermsDocumentsSet>(50, .7f));
			_terms.put(index, tDocSet);
		}
		int level = 1;
		if(filter != null)
			level = filter.getLevel();
		TermsDocumentsSet docSet = tDocSet.get(level);
		if(docSet == null){
			//try to load from HBase.
			if(level == 1){
				TermDocuments tds = IndexTableSupport.getIndex(new IndexKey(index));
				if(tds != null){
					docSet = new GlobalTermsDocumentsSet(tds);
					tDocSet.put(1, docSet);
				}
				else
					return null;
			}
			else{
				
				// Read all tiles in parallel
				List<IndexKey> keys = new ArrayList<IndexKey>(filter.getIndexes().size());
				for(long tile : filter.getIndexes())
					keys.add(new IndexKey(index, level, tile));
				List<TermDocuments> tdss = IndexTableSupport.getIndexes(keys);
				int i = 0;
				for (TermDocuments tds : tdss) {
					if(tds != null){
						if(docSet == null)
							docSet = new SpatialTermsDocumentsSet();
						((SpatialTermsDocumentsSet)docSet).addTermDocumentsTile(filter.getIndexes().get(i), tds);
						i++;
					}					
				}
				if(docSet != null)
					tDocSet.put(level, docSet);
				else
					return null;
			}
		}
			
		TermDocuments docs = null;
		
		if(filter == null){
			// Non spatial
			docs = docSet.getTermDocuments(false, null);
			if((docs == null) || (!docs.isInUse()))
				docs = IndexTableSupport.getIndex(new IndexKey(index));
		}
		else{
			//Spatial
			boolean first = true;
			// Read all missing tiles in parallel
			List<IndexKey> keys = new ArrayList<IndexKey>(filter.getIndexes().size());
			for(long tile : filter.getIndexes()){
				TermDocuments tds = docSet.getTermDocuments(false, tile);
				if((tds == null) || (!tds.isInUse()))					
					keys.add(new IndexKey(index, level, tile));
				else{
					if(first){
						docs = new TermDocuments(tds);
						first = false;
					}
					else{
						docs.addDocuments(tds);
					}
				}
			}
			if(keys.size() > 0){
				List<TermDocuments> tdss = IndexTableSupport.getIndexes(keys);
				int i = 0;
				for (TermDocuments tds : tdss) {
					if(first){
						if(tds == null)
							docs = docSet.getTermDocuments(true, keys.get(i).getCell());
						else
							docs = new TermDocuments(tds);
						first = false;
					}
					else{
						if(tds != null)
							docs.addDocuments(tds);
					}
					i++;
				}
			}
		}	
		return docs;
	}

	public static Iterator<String> getDocIterator(Index index, SpatialReaderFilter filter) throws Exception{
			
		TermDocuments docs = getTermDocuments(index, filter);
		return (docs == null) ? null : docs.getDocumentIterator();

	}

	public static int getDocumentFrequency(String field, String term, SpatialReaderFilter filter) throws Exception{
		
		return getDocumentFrequency(new Index(field, term), filter);
	}
	
	public static int getDocumentFrequency(Index index, SpatialReaderFilter filter) throws Exception{
		
		TermDocuments td = getTermDocuments(index, filter);
		return (td == null) ? 0 : td.getTermFrequency();
	}
	
	public static int getTermDocumentFrequency(String field, String term, String ID, SpatialReaderFilter filter) throws Exception{
		
		TermDocuments td = getTermDocuments(new Index(field, term), filter);;
		return (td == null) ? 0 : td.getdocumetFrequency(ID);

	}
	
	public static IndexKey getNextkey(IndexKey lowerKey, SpatialReaderFilter filter) throws Exception{
		
		if(lowerKey == null){
			if(filter == null)
				lowerKey = new IndexKey(_lowestKey);
			else{
				lowerKey = new IndexKey(new Index(_lowestKey), filter.getLevel(), filter.getIndexes().get(0));
			}
		}
		TermDocumentIndex tInd = IndexTableSupport.getIndexNextKey(lowerKey);
		if(tInd == null){
			if(filter == null)
				return null;
			long tile = lowerKey.getCell();
			boolean found = false;
			for(long fTile : filter.getIndexes()){
				if(found){
					lowerKey = new IndexKey(new Index(_lowestKey), filter.getLevel(), fTile);
					return getNextkey(lowerKey, filter);
				}
				if(tile == fTile)
					found = true;
			}
		}
		IndexKey key = tInd.getIndex();
		Map<Integer, TermsDocumentsSet> tDocSet = _terms.get(key.getIndex());
		if(tDocSet == null){
			//create a new one
			tDocSet = Collections.synchronizedMap(new HashMap<Integer, TermsDocumentsSet>(50, .7f));
			_terms.put(key.getIndex(), tDocSet);
		}
		int level = key.getLevel();
		TermsDocumentsSet docSet = tDocSet.get(level);
		if(docSet == null){
			// create it.
			if(level == 1){
				docSet = new GlobalTermsDocumentsSet(tInd.getTd());
				tDocSet.put(1, docSet);
			}
			else{
				docSet = new SpatialTermsDocumentsSet();
				((SpatialTermsDocumentsSet)docSet).addTermDocumentsTile(key.getCell(), tInd.getTd());
				tDocSet.put(level, docSet);
			}
		}
		else{
			if(level == 1)
				((GlobalTermsDocumentsSet)docSet).setTermDocuments(tInd.getTd());
			else
				((SpatialTermsDocumentsSet)docSet).addTermDocumentsTile(key.getCell(), tInd.getTd());
		}

		return tInd.getIndex();
	}
	
	public static void deleteIndex(IndexKey index) throws Exception{
		
		IndexTableSupport.deleteIndex(index);
	}
	
	public static List<String> getFieldNames() throws Exception{
		
		return IndexTableSupport.getFieldNames();
	}
	
	public static List<String> getFieldDocuments(String field)throws Exception{
	
		return IndexTableSupport.getFieldDocuments(field);
	}
}