package com.practicalHadoop.hbase.customFilter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class BoundingBox implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Point lowerLeft;
	private Point upperRight;

	public BoundingBox(double lat1, double lat2, double lon1, double lon2) {
		
		double minLon = Math.min(lon1, lon2);
		double maxLon = Math.max(lon1, lon2);
		double minLat = Math.min(lat1, lat2);
		double maxLat = Math.max(lat1, lat2);
		lowerLeft = new Point(minLat, minLon);
		upperRight = new Point(maxLat, maxLon);
	}

	public BoundingBox(Point ll, Point ur) {
		
		lowerLeft = ll;
		upperRight = ur;
	}
	
	public BoundingBox(BoundingBox that) {
		this(that.lowerLeft, that.upperRight);
	}

	public Point getLowerLeft() {
		return lowerLeft;
	}

	public Point getUpperRight() {
		return upperRight;
	}

	public double getLatitudeSize() {

		return upperRight.getLatitude() - lowerLeft.getLatitude();
	}

	public double getLongitudeSize() {
		
		return upperRight.getLongitude() - lowerLeft.getLongitude();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof BoundingBox) {
			BoundingBox that = (BoundingBox) obj;
			return lowerLeft.equals(that.lowerLeft) && upperRight.equals(that.upperRight);
		} 
		return false;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * lowerLeft.hashCode();
		result = 37 * result + upperRight.hashCode();
		return result;
	}

	public boolean contains(Point point) {
		return (point.getLatitude() >= lowerLeft.getLatitude()) && (point.getLongitude() >= lowerLeft.getLongitude()) 
		&& (point.getLatitude() <= upperRight.getLatitude()) && (point.getLongitude() <= upperRight.getLongitude());
	}

	public boolean contains(BoundingBox other) {
		return (other.lowerLeft.getLongitude() > lowerLeft.getLongitude()
				&& other.upperRight.getLongitude() < upperRight.getLongitude()
				&& other.lowerLeft.getLatitude() > lowerLeft.getLatitude()
				&& other.upperRight.getLatitude() < upperRight.getLatitude());
	}

	public boolean intersects(BoundingBox other) {
		return !(other.lowerLeft.getLongitude() > upperRight.getLongitude() 
				|| other.upperRight.getLongitude() < lowerLeft.getLongitude() 
				|| other.lowerLeft.getLatitude() > upperRight.getLatitude() 
				|| other.upperRight.getLatitude() < lowerLeft.getLatitude());
	}

	@Override
	public String toString() {
		return "[" + lowerLeft + " -> " + upperRight + "]";
	}

	public Point getCenterPoint() {
		double centerLatitude = (lowerLeft.getLatitude() + upperRight.getLatitude()) / 2;
		double centerLongitude = (lowerLeft.getLongitude() + upperRight.getLongitude()) / 2;
		return new Point(centerLatitude, centerLongitude);
	}

	public void expandToInclude(BoundingBox other) {
		if (other.lowerLeft.getLongitude() < lowerLeft.getLongitude()) {
			lowerLeft.setLongitude(other.lowerLeft.getLongitude());
		}
		if (other.upperRight.getLongitude() > upperRight.getLongitude()) {
			upperRight.setLongitude(other.upperRight.getLongitude());
		}
		if (other.lowerLeft.getLatitude() < lowerLeft.getLatitude()) {
			lowerLeft.setLatitude(other.lowerLeft.getLatitude());
		}
		if (other.upperRight.getLatitude() > upperRight.getLatitude()) {
			upperRight.setLatitude(other.upperRight.getLatitude());
		}
	}

	public double getMinLon() {
		return lowerLeft.getLongitude();
	}

	public double getMinLat() {
		return lowerLeft.getLatitude();
	}

	public double getMaxLat() {
		return upperRight.getLatitude();
	}

	public double getMaxLon() {
		return upperRight.getLongitude();
	}
	
	public byte[] toBytes()throws Exception{
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream ous = new ObjectOutputStream(bos);
		ous.writeObject(this);
		ous.close();
		return bos.toByteArray();
	}
	
	public static BoundingBox fromBytes(byte[] bytes)throws Exception{
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bis);
		return (BoundingBox)ois.readObject();
	}
}
