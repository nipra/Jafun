package com.practicalHadoop.lucene.spatial.geometry.filters;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import com.practicalHadoop.lucene.spatial.GeometricShapeHelper;
import com.practicalHadoop.lucene.spatial.geometry.BoundingBox;
import com.practicalHadoop.lucene.spatial.geometry.GeometricShape;
import com.practicalHadoop.lucene.spatial.geometry.GeometryType;
import com.practicalHadoop.lucene.spatial.geometry.Point;

public class BoundingBoxPointFilter extends Filter {

	private static final long serialVersionUID = 1L;
	private BoundingBox _box = null;
	
	public BoundingBoxPointFilter(BoundingBox b){
		_box = b;
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		int numDocs = reader.maxDoc();
		final OpenBitSet set = new OpenBitSet(numDocs);		
		for (int index = 0;index < numDocs; index++) {
			Document doc = reader.document(index);
			if(doc == null)
				continue;
			GeometricShape shape = GeometricShapeHelper.getGeometry(doc);
			if((shape == null) || (!shape.getType().equals(GeometryType.POINT)))
				continue;
			if(_box.contains((Point)shape))
				set.set(index);
		}
		return set.isEmpty() ? null : set;
	}
}
