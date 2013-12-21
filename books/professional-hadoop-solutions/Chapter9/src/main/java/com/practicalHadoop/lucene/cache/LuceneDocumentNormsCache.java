package com.practicalHadoop.lucene.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.practicalHadoop.lucene.tables.NormsTableSupport;

public class LuceneDocumentNormsCache {

	private static final Log LOG = LogFactory.getLog(LuceneDocumentNormsCache.class);
	
	/**
	 * Data structure for memory indexes
	 */

	private static Map<String, FieldNorms> _norms = 
		Collections.synchronizedMap(new HashMap<String, FieldNorms>(100, .7f));

	
	private LuceneDocumentNormsCache(){}
	
	public static void refresh(){

		List<String> removed = new ArrayList<String>();
		for(Map.Entry<String, FieldNorms> entry : _norms.entrySet()){
			String field = entry.getKey();
			FieldNorms norm = entry.getValue();
			if(!norm.isInUse()){
				removed.add(field);
				continue;
			}
			if(!norm.isStale()){
				try {
					FieldNorms update = NormsTableSupport.getNorms(field);
					norm.setNorms(update.getNorms());
				} catch (Exception e) {}
			}
		}
		
		for(String field : removed)
			_norms.remove(field);
	}
	public static Byte getNorm(String field, String ID) throws Exception{
		
		FieldNorms idNorms = _norms.get(field);
		if((idNorms == null) || (!idNorms.isInUse())){
			idNorms = NormsTableSupport.getNorms(field);
			if(idNorms == null)
				return null;
			_norms.put(field, idNorms);
		}
		return idNorms.getNorm(ID);
	}
	/**
         * storeNorm : Add a pair of ID and norm to the _norm Map
         * @param field
         * @param ID
         * @param norm
         * @throws Exception
         */
	public static void storeNorm(String field, String ID, byte norm) throws Exception{
		
		NormsTableSupport.addNorm(field, ID, norm);
                // Cache code ignore.. we just write to hbase table... cache updated elsewhere.
		//FieldNorms idNorms = _norms.get(field);
                /*
                 * Note: If idNorms is null that is there isn't a fieldNorm to
                 * match the field value, you can't add the norm to that id.
                 * You need to create it first.
                 */

                    // Need to create object and then add it
                    // Create a new fieldNorm which is a map of string, byte
                    // Add to _norms using field as the key
                // Can't add something to a null object!!!
		//if((idNorms == null) || (!idNorms.isInUse())){
			//idNorms.addNorm(ID, norm);
		//}
	}
		
	public static void storeNorms(String field, Map<String,Byte> norms) throws Exception{
		
		NormsTableSupport.addNorms(field, norms);
                // Cache code ignore.. we just write to hbase table... cache updated elsewhere.
		FieldNorms idNorms = _norms.get(field);
		if((idNorms == null) || (!idNorms.isInUse())){
			idNorms.addNorms(norms);
		}
	}
			
	public static void deleteNorm(String field, String ID) throws Exception{
		
		NormsTableSupport.deleteNorm(field, ID);
		FieldNorms idNorms = _norms.get(field);
		if((idNorms != null) && (idNorms.isInUse())){
			idNorms.deleteNorm(ID);
		}
	}
		
	public static void deleteNorms(String field, List<String> IDs) throws Exception{
		
		NormsTableSupport.deleteNorms(field, IDs);
		FieldNorms idNorms = _norms.get(field);
		if((idNorms == null) || (!idNorms.isInUse())){
			idNorms.deleteNorms(IDs);
		}
	}			
}