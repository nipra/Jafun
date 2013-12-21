package com.practicalHadoop.lucene.indexing.support;

import java.util.List;

import com.practicalHadoop.lucene.document.SpatialTermDocument;

public class SpatialReaderFilter {
	
	private List<String> _geoShapes;
	private int _level;
	private List<Long> _indexes;
	
	public SpatialReaderFilter(List<String> shapes, int l, List<Long> ind){
		
		_geoShapes = shapes;
		_level = l;
		_indexes = ind;
	}

	public List<String> getGeoShapes() {
		return _geoShapes;
	}

	public int getLevel() {
		return _level;
	}

	public List<Long> getIndexes() {
		return _indexes;
	}
	
	public boolean accept(SpatialTermDocument td){
		
		if(td == null)
			return false;
		String gt = td.getGeometryType();
		if(!_geoShapes.contains(gt))
			return false;
		List<Long> tdBoxes = td.getTierBoxes(_level);
		if(tdBoxes == null)
			return false;
		for(long b : tdBoxes){
			if(_indexes.contains(b))
				return true;
		}
		return false;
	}
}
