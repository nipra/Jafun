package com.practicalHadoop.lucene.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpatialTermsDocumentsSet implements TermsDocumentsSet {

	Map<Long, TermDocuments> _tileTermDocuments = new HashMap<Long, TermDocuments>();

	public void addTermDocumentsTile(long tile, TermDocuments tds){
		
		_tileTermDocuments.put(tile, tds);
	}
	
	
	@Override
	public TermDocuments getTermDocuments(boolean create, List<Long> tileIds){

		int nTiles = tileIds.size();
		if(nTiles == 1){
			return getTermDocuments(create, tileIds.get(0));
		}
		TermDocuments tds = new TermDocuments(_tileTermDocuments.get(tileIds.get(0)));			
		for(int i = 1; i < nTiles; i++){
			tds.addDocuments(_tileTermDocuments.get(tileIds.get(i)));
		}
		return tds;
	}

	@Override
	public TermDocuments getTermDocuments(boolean create, long tileId) {

		TermDocuments tds = _tileTermDocuments.get(tileId);
		if(create && (tds == null)){
			tds = new TermDocuments();
			_tileTermDocuments.put(tileId, tds);
		}
		return tds;
	}

	public Set<Long> getTermDocumentsCells() {
		return _tileTermDocuments.keySet();
	}

	@Override
	public void removeTermDocuments(long tileId) {
		
		_tileTermDocuments.remove(tileId);
	}
}