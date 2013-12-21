package com.practicalHadoop.lucene.spatial.geometry;

import java.util.List;

public class Polygon extends GeometricShape {

	private List<Point> _points;

	public List<Point> getPoints(){
		
		return _points;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof Polygon) {
			Polygon poly = (Polygon)o;
			int i = 0;
			for(Point p : poly.getPoints()){
				if(!p.equals(_points.get(i++)))
					return false;
			}
			return true;
		}
		return false;
    }

	@Override
	public int hashCode() {
		int result = 17;
		for(Point p : _points)
			result = 37 * result + p.hashCode();
		return result;
	}
	
    @Override
    public String toString() {
    	StringBuilder result = new StringBuilder();
    	boolean first = true;
		for(Point p : _points){
			if(first)
				first = false;
			else
				result.append(";");
			result.append(p.toString());
		}
		return result.toString();
    }

    @Override
	public boolean contains(GeometricShape other) throws Exception {

		throw new Exception("Unimplemented");
	}

	@Override
	public String serialize() {

		return toString();
	}

	@Override
	public String getTypeString() {

		return GeometryType.POLYGON.toString();
	}

	@Override
	public GeometryType getType() {

		return GeometryType.POLYGON;
	}
	
	public BoundingBox getEnclosingBox(){
		
		return null;
	}

/*	public static Polygon fromString(List<String> s) throws UnsupportedDataTypeException {
		
		Point ll = Point.fromString(s.get(0));
		Point ur = Point.fromString(s.get(1));
		return new BoundingBox(ll, ur);
	}
*/
}
