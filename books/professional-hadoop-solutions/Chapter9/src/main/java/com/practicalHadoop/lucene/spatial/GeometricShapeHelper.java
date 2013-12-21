package com.practicalHadoop.lucene.spatial;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;

import com.practicalHadoop.lucene.indexing.support.SpatialReaderFilter;
import com.practicalHadoop.lucene.spatial.geometry.CartesianTier;
import com.practicalHadoop.lucene.spatial.geometry.GeometricShape;
import com.practicalHadoop.lucene.spatial.geometry.GeometryType;

public class GeometricShapeHelper {
	
	public static final String GEOMETRY_FIELD = "_geometry";
	public static final String GEOMETRY_TYPE_FIELD = "_geometryType";
	public static final List<String> points = Arrays.asList(GeometryType.POINT.toString());
	public static final List<String> bbs = Arrays.asList(GeometryType.BOUNDINGBOX.toString());
	public static final List<String> polygons = Arrays.asList(GeometryType.POLYGON.toString());
	private static int MAX_TILES = 5;

	private GeometricShapeHelper(){}
	
	
	public static void addGeometry(Document doc, GeometricShape shape, int sTier, int endTier){
		
		for (int i = sTier; i <= endTier; i++) {
			//Store boxes
			CartesianTier tier = new CartesianTier(i);
			List<Long> boxes = tier.getTierValues(shape);
			for (Long b : boxes) {
				doc.add(new Field(tier.getFieldName(), b.toString(),
						Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			}
		}
		// Store type
	      doc.add(new Field(GEOMETRY_TYPE_FIELD, 
	              shape.getTypeString(),
	              Field.Store.YES, 
	              Field.Index.NOT_ANALYZED_NO_NORMS));

			// Store geometry
	      doc.add(new Field(GEOMETRY_FIELD, 
	              shape.serialize(),
	              Field.Store.YES, 
	              Field.Index.NO));

		return;
	}

	public static void addGeometry(Document doc, Map<CartesianTier, List<Long>> tierBoxes, GeometricShape shape){
		
		for (CartesianTier tier : tierBoxes.keySet()) {
			//Store boxes
			List<Long> boxes = tierBoxes.get(tier);
			for (Long b : boxes) {
				doc.add(new Field(tier.getFieldName(), b.toString(),
						Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			}
		}
		// Store type
	      doc.add(new Field(GEOMETRY_TYPE_FIELD, 
	              shape.getTypeString(),
	              Field.Store.YES, 
	              Field.Index.NOT_ANALYZED_NO_NORMS));

			// Store geometry
	      doc.add(new Field(GEOMETRY_FIELD, 
	              shape.serialize(),
	              Field.Store.YES, 
	              Field.Index.NO));

		return;
	}

	private static TierBoxes getTier(GeometricShape shape, int sTier, int endTier){
		
		List<Long> boxes = null;
		CartesianTier tier = null;
		for (int i = endTier; i >= sTier; i--) {
			tier = new CartesianTier(i);
			boxes = tier.getTierValues(shape);
			if(boxes.size() > MAX_TILES)
				continue;
			break;
		}
		return new TierBoxes(tier, boxes);
	}

	
	public static Query buildGeometricQuery(GeometricShape shape, int sTier, int endTier, List<String> types){

		// Chose the tier first
		TierBoxes tb = getTier(shape, sTier, endTier);
		List<Long> boxes = tb.getBoxes();
		CartesianTier tier = tb.getTier();
        BooleanQuery query = new BooleanQuery();
		for(Long b : boxes){
			TermQuery tq = new TermQuery(new Term(tier.getFieldName(), b.toString()));
			query.add(tq, Occur.SHOULD);
		}
		if(types != null){
			BooleanQuery tQuery = new BooleanQuery();
			for(String type : types){
				TermQuery tq = new TermQuery(new Term(GEOMETRY_TYPE_FIELD, type));
				query.add(tq, Occur.SHOULD);
			}
			query.add(tQuery, Occur.MUST);
		}
        return query;
	}

	public static SpatialReaderFilter buildReaderFilter(GeometricShape shape, int sTier, int endTier){

		List<String> types = new LinkedList<String>();
		for(GeometryType t : GeometryType.values())
			types.add(t.toString());
		return buildReaderFilter(shape, sTier, endTier, types);
	}
	
	public static SpatialReaderFilter buildReaderFilter(GeometricShape shape, int sTier, int endTier, List<String> types){

		// Chose the tier first
		TierBoxes tb = getTier(shape, sTier, endTier);
		List<Long> boxes = tb.getBoxes();
		CartesianTier tier = tb.getTier();
		return new SpatialReaderFilter(types, tier.getTierLevel(), boxes);
	}
	
	public static GeometricShape getGeometry(Document doc){
		
		Field t = doc.getField(GEOMETRY_TYPE_FIELD);
		GeometryType type = GeometryType.toGeometryType(t.stringValue());
		Field g = doc.getField(GEOMETRY_FIELD);
		if(g == null)
			return null;
		try {
			return GeometricShape.fromString(type, g.stringValue());
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setMAX_TILES(int tiles){
		MAX_TILES = tiles;
	}
	
	private static class TierBoxes{
	
		private List<Long> _boxes;
		private CartesianTier _tier;
		
		public TierBoxes(CartesianTier tier, List<Long> boxes){
			
			_tier = tier;
			_boxes = boxes;
		}

		public List<Long> getBoxes() {
			return _boxes;
		}

		public CartesianTier getTier() {
			return _tier;
		}
	}
}