package com.practicalHadoop.lucene.spatial.geometry;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.lucene.spatial.geometry.DistanceUnits;

public class Point extends GeometricShape{

    private int _lat;
    private int _lon;

    public Point(double lat, double lon) throws UnsupportedOperationException{
    	
        if((lat > 90.0) || (lat < -90.0))
        	throw new  UnsupportedOperationException("Latitude must be in [-90, 90]");
        if((lon > 180.0) || (lon < -180.0)) 
        	throw new  UnsupportedOperationException("Longitude must be in [-180, 180]");
        _lat = (int)(lat * _precision);
        _lon = (int)(lon * _precision);
    }

    public Point(int lat, int lon){
    	
        _lat = lat;
        _lon = lon;
    }

    public Point(Point p){
    	
        _lat = p.getLat();
        _lon = p.getLon();
    }

    public int getLat() {
        return _lat;
    }

    public int getLon() {
        return _lon;
    }
    
	public double pointDistance(Point p){
		
		return pointDistance(p, DistanceUnits.MILES);
	}
	
	public double pointDistance(Point p, DistanceUnits lUnits){
		
		if(this.equals(p))
			return 0;
		if(lUnits == null)
			lUnits = DistanceUnits.MILES;
	    // Get the m_dLongitude difference. Don't need to worry about
	    // crossing 180 since cos(x) = cos(-x)
	    double dLon = (_lon - p.getLon())/_precision;

	    double a = radians(90.0 - _lat/_precision);
	    double c = radians(90.0 - p.getLat()/_precision);
	    double cosB = (Math.cos(a) * Math.cos(c))
	        + (Math.sin(a) * Math.sin(c) * Math.cos(radians(dLon)));

	    double radius = (lUnits == DistanceUnits.MILES) ? 
	    		3963.205/* MILERADIUSOFEARTH */
	    		: 6378.160187/* KMRADIUSOFEARTH */;

	    // Find angle subtended (with some bounds checking) in radians and
	    // multiply by earth radius to find the arc distance
	    if (cosB < -1.0)
	      return 3.14159265358979323846/* PI */* radius;
	    else if (cosB >= 1.0)
	      return 0;
	    else
	      return Math.acos(cosB) * radius;
	  }

	  private double radians(double a) {
	    return a * 0.01745329251994;
	  }

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if(o instanceof Point){
			Point p = (Point)o;
			return ((_lat == p.getLat()) && (_lon == p.getLon()));
		}
		return false;
    }

	@Override
	public int hashCode() {
		int result = 42;
		long latBits = Double.doubleToLongBits(_lat);
		long lonBits = Double.doubleToLongBits(_lon);
		result = 31 * result + (int) (latBits ^ (latBits >>> 32));
		result = 31 * result + (int) (lonBits ^ (lonBits >>> 32));
		return result;
	}

	@Override
    public String toString() {
        return new StringBuilder().append('(').append(_lat).append(',').append(
                _lon).append(')').toString();
    }

	@Override
	public boolean contains(GeometricShape other)throws Exception {
		
		throw new Exception("Unimplemented");
	}

	@Override
	public String serialize() {
		
		return toString();
	}

	@Override
	public String getTypeString() {

		return GeometryType.POINT.toString();
	}
	
	@Override
	public GeometryType getType() {

		return GeometryType.POINT;
	}

	public static Point fromString(List<String> strings) throws UnsupportedOperationException {
		
		return fromString(strings.get(0));
	}
		
	public static Point fromString(String s) throws UnsupportedOperationException {
		
		int start = s.indexOf('(', 0);
		int end = s.indexOf(')', start);
		StringTokenizer tn = new StringTokenizer(s.substring(start + 1, end), ",");
		int lat = Integer.parseInt(tn.nextToken());
		int lon = Integer.parseInt(tn.nextToken());
		return new Point(lat, lon);
	}
}