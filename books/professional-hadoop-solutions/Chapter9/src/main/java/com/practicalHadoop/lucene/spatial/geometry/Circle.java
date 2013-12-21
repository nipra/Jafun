package com.practicalHadoop.lucene.spatial.geometry;

import javax.activation.UnsupportedDataTypeException;

import org.apache.lucene.spatial.geometry.DistanceUnits;
import org.apache.lucene.spatial.geometry.shape.DistanceApproximation;

public class Circle extends GeometricShape {

	public static final double KILOMETERS_PER_MILE  = 1.609347f;
	public static final double METERS_PER_KILOMETER  = 1000f;

	private Point _center;
	private int _radius; //meters

    public Circle(Point center, float radius, DistanceUnits unit) throws UnsupportedDataTypeException{

    	_center = new Point(center);
    	if(unit.equals(DistanceUnits.KILOMETERS))
    		_radius = (int)(radius * METERS_PER_KILOMETER);
    	else
    		_radius = (int)(radius * METERS_PER_KILOMETER * KILOMETERS_PER_MILE);
    }
    	

	public Point getCenter() {
		return _center;
	}


	public int getRadius() {
		return _radius;
	}

	@Override
	public boolean contains(GeometricShape other) throws Exception {

		switch(other.getType()){
		case POINT:
			return contains((Point)other);
		case POLYGON:
			return contains((BoundingBox)other);
		default:
			throw new Exception("Unimplemented");
		}
	}

    public boolean contains(Point p) {

    	return (_center.pointDistance(p, DistanceUnits.KILOMETERS) < (_radius/METERS_PER_KILOMETER));
    }
	
    public boolean contains(BoundingBox bb) {

    	return contains(bb.getCenterPoint()) && contains(bb.getUpperRight());
    }
	
//    public boolean intersects(BoundingBox bb) {
//
//    }
	
	@Override
	public GeometryType getType() {

		return GeometryType.CIRCLE;
	}

	@Override
	public String getTypeString() {

		return GeometryType.CIRCLE.toString();
	}

	@Override
	public String serialize() {

		return toString();
	}

    @Override
    public String toString() {
        return new StringBuilder().append(_center.toString()).append(";").append(_radius).toString();
    }

	public BoundingBox getEnclosingBox(){
		
		double rMiles = (_radius * METERS_PER_KILOMETER) / KILOMETERS_PER_MILE; 
		int dlat = (int)((rMiles/DistanceApproximation.getMilesPerLatDeg()) * _precision);
		int dlon = (int)((rMiles/DistanceApproximation.getMilesPerLngDeg(_center.getLat()/_precision)) * _precision);
		Point ll = new Point(_center.getLat() - dlat, _center.getLon() - dlon);
		Point ur = new Point(_center.getLat() + dlat, _center.getLon() + dlon);
		return new BoundingBox(ll, ur);
	}
}
