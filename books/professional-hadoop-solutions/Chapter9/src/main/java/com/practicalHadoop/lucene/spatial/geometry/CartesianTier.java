package com.practicalHadoop.lucene.spatial.geometry;

import java.util.ArrayList;
import java.util.List;

public class CartesianTier {

	public static final String DEFALT_FIELD_PREFIX = "_tier_";

	private final int _tierLevel;
	private int _tierLength;
	private final String _fieldPrefix;
	
	private final int _deltaLat;
	private final int _deltaLon;
	
	private boolean VERBOSE = false;

	public CartesianTier (int tierLevel) {
		this(tierLevel,DEFALT_FIELD_PREFIX);
	}
	
	public CartesianTier (int tierLevel,String fieldPrefix) {

		_tierLevel  = tierLevel;
		_tierLength = (int) Math.pow(2 , _tierLevel);
		_deltaLat = (GeometricShape._maxLat - GeometricShape._minLat)/_tierLength;
		_deltaLon = (GeometricShape._maxLon - GeometricShape._minLon)/_tierLength;
		_fieldPrefix = fieldPrefix;
		
	}
	
	public int getTierLevel(){
		return _tierLevel;
	}
	
	public String getFieldFrefix(){
		return _fieldPrefix;
	}
	
	public String getFieldName(){
		return _fieldPrefix + _tierLevel;
	}
	
	public List<Long> getTierValues(Point p){
		
		List<Long> result = new ArrayList<Long>();
		int latD = (p.getLat() - GeometricShape._minLat)/_deltaLat;
		int lonD = (p.getLon() - GeometricShape._minLon)/_deltaLon;
		long index = latD * _tierLength + lonD;
		if(VERBOSE){
			System.out.println("Point: " + latD + "," + lonD + " index " + index);
		}
		result.add(index);
		return result;
	}

	public List<Long> getTierValues(BoundingBox b){
		
		int lowLatD = (b.getLowerLeft().getLat() - GeometricShape._minLat)/_deltaLat;
		int upLatD = (b.getUpperRight().getLat() - GeometricShape._minLat)/_deltaLat;
		int lowLonD = (b.getLowerLeft().getLon() - GeometricShape._minLon)/_deltaLon;
		int upLonD = (b.getUpperRight().getLon() - GeometricShape._minLon)/_deltaLon;
		if(VERBOSE){
			System.out.println("BB:  Lat - " + lowLatD + "," + upLatD + " lon - " + lowLonD + "," + upLonD);
		}
		
		List<Long> result = new ArrayList<Long>();
		for(int i = lowLatD; i < upLatD + 1; i++){
			long raw = i * (long)_tierLength;
			for(int j = lowLonD; j < upLonD + 1; j++){
				result.add(raw + j);
			}
		}
		
		return result;
	}
	
	public List<Long> getTierValues(Polygon p){
		
		return getTierValues(p.getEnclosingBox());
	}
	
	public List<Long> getTierValues(Circle c){
		
		return getTierValues(c.getEnclosingBox());
	}
	
	@Override
	public int hashCode() {

		return _tierLevel;
	}
	
	public List<Long> getTierValues(GeometricShape g){
		
		switch(g.getType()){
		case POINT:
			return getTierValues((Point)g);
			case POLYGON:
				return getTierValues((Polygon)g);
			case CIRCLE:
				return getTierValues((Circle)g);
			case BOUNDINGBOX:
				return getTierValues((BoundingBox)g);
			default:
				return null;
		}
	}
}