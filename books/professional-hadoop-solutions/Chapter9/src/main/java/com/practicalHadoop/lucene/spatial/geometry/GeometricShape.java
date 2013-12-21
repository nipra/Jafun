package com.practicalHadoop.lucene.spatial.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public abstract class GeometricShape {

	protected static final int _precision = 1000000;
	protected static final int _maxLat = 90 * _precision;
	protected static final int _minLat = -90 * _precision;
	protected static final int _maxLon = 180 * _precision;
	protected static final int _minLon = -180 * _precision;


	abstract public boolean contains(GeometricShape other)throws Exception;

	abstract public String serialize();

	abstract public String getTypeString();

	abstract public GeometryType getType();

	public static GeometricShape fromString(GeometryType t, String s) throws UnsupportedOperationException {

		StringTokenizer tn = new StringTokenizer(s, ";");
		List<String> points = new ArrayList<String>();
		while(tn.hasMoreTokens()){
			points.add(tn.nextToken());
		}
		switch(t){
		case POINT:
			return Point.fromString(points);
		case BOUNDINGBOX:
			return BoundingBox.fromString(points);
		case POLYGON:
		case CIRCLE:
		default:
			throw new UnsupportedOperationException();

		}
	}
}
