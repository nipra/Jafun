package com.practicalHadoop.hbase.customFilter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * {@link Point} encapsulates coordinates on the earths surface.<br>
 * Coordinate projections might end up using this class...
 */
public class Point implements Serializable, Comparable<Point>{
	
	private static final long serialVersionUID = 1L;

	private double longitude;
	private double latitude;
	
	
	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		if (Math.abs(latitude) > 90 || Math.abs(longitude) > 180) {
			throw new IllegalArgumentException("The supplied coordinates " + this + " are out of range.");
		}
	}

	public Point(Point other) {
		this(other.latitude, other.longitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double l) {
		latitude = l;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double l) {
		longitude = l;
	}

	@Override
	public String toString() {
		return "(" + latitude + "," + longitude + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point other = (Point) obj;
			return latitude == other.latitude && longitude == other.longitude;
		}
		return false;
	}
	
	public double pointDistance(Point other){
		
        double dlat = latitude - other.latitude;
        double dlon = longitude - other.longitude;
    	return Math.sqrt(dlat*dlat + dlon*dlon);
	}

	@Override
	public int hashCode() {
		int result = 42;
		long latBits = Double.doubleToLongBits(latitude);
		long lonBits = Double.doubleToLongBits(longitude);
		result = 31 * result + (int) (latBits ^ (latBits >>> 32));
		result = 31 * result + (int) (lonBits ^ (lonBits >>> 32));
		return result;
	}
	
	public byte[] toBytes()throws Exception{
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream ous = new ObjectOutputStream(bos);
		ous.writeObject(this);
		ous.close();
		return bos.toByteArray();
	}
	
	public static Point fromBytes(byte[] bytes)throws Exception{
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (Point)ois.readObject();
	}

	@Override
	public int compareTo(Point other) {

		// Check latitude first
		if (latitude > other.latitude) 
			return 1;
	    if (latitude < other.latitude) 
	    	return (-1);
	    // and test longitude second
	    if (longitude > other.longitude) 
	    	return 1;
	    if (longitude < other.longitude) 
	    	return (-1);
	    // when you exclude all other possibilities, what remains is...
	    return 0;  // they are the same point 
	}
}