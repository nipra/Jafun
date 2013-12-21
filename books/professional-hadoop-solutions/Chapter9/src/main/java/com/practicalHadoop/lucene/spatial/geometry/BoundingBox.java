package com.practicalHadoop.lucene.spatial.geometry;

import java.util.List;


/**
 * A Lat/Long rectangle. 
 */

public class BoundingBox extends GeometricShape{
    
	private Point _lowerLeft, _upperRight;

   /**
     * Constructor.
     * 
     */
    public BoundingBox(double lat, double lon) throws UnsupportedOperationException {
        this(new Point(lat, lon), new Point(lat, lon));
    }

    /**
     * Constructor.
     * 
     * @param minLat
     * @param minLon
     * @param maxLat
     * @param maxLon
     */
    public BoundingBox(double minLat, double minLon, double maxLat,
            double maxLon) throws UnsupportedOperationException {
        this(new Point(minLat, minLon), new Point(maxLat, maxLon));
    }

    /**
     * Constructor.
     * 
     * @param lowerLeft
     * @param upperRight
     */
    public BoundingBox(Point lowerLeft, Point upperRight) {
        this._lowerLeft = new Point(lowerLeft);
        this._upperRight = new Point(upperRight);
    }

    /**
     * @return the lower left point.
     */
    public Point getLowerLeft() {
        return _lowerLeft;
    }

    /**
     * 
     * @return the upper right point.
     */
    public Point getUpperRight() {
        return _upperRight;
    }

    public boolean contains(Point p) {
        return _lowerLeft.getLon() <= p.getLon() && p.getLon() <= _upperRight.getLon()
                && _lowerLeft.getLat() <= p.getLat() && p.getLat() <= _upperRight.getLat();
    }    
    
	public boolean contains(BoundingBox other) {
		return (other.getLowerLeft().getLon() > _lowerLeft.getLon()
				&& other.getUpperRight().getLon() < _upperRight.getLon()
				&& other.getLowerLeft().getLat() > _lowerLeft.getLat()
				&& other.getUpperRight().getLat() < _upperRight.getLat());
	}

	public boolean intersects(BoundingBox other) {
		return !(other.getLowerLeft().getLon() > _upperRight.getLon() 
				|| other.getUpperRight().getLon() < _lowerLeft.getLon() 
				|| other.getLowerLeft().getLat() > _upperRight.getLat() 
				|| other.getUpperRight().getLat() < _lowerLeft.getLat());
	}
	
	public Point getCenterPoint() {
		int centerLatitude = (_lowerLeft.getLat() + _upperRight.getLat()) / 2;
		int centerLongitude = (_lowerLeft.getLon() + _upperRight.getLon()) / 2;
		return new Point(centerLatitude, centerLongitude);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof BoundingBox) {
			BoundingBox b = (BoundingBox)o;
			return ((_lowerLeft == b.getLowerLeft()) && (_upperRight == b.getUpperRight()));
		}
		return false;
    }

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * _lowerLeft.hashCode();
		result = 37 * result + _upperRight.hashCode();
		return result;
	}
	
    @Override
    public String toString() {
        return new StringBuilder().append(_lowerLeft.toString()).append(";")
                .append(_upperRight.toString()).toString();
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

	@Override
	public String serialize() {

		return toString();
	}

	@Override
	public String getTypeString() {

		return GeometryType.BOUNDINGBOX.toString();
	}

	@Override
	public GeometryType getType() {

		return GeometryType.BOUNDINGBOX;
	}

	public static BoundingBox fromString(List<String> s) throws UnsupportedOperationException {
		
		Point ll = Point.fromString(s.get(0));
		Point ur = Point.fromString(s.get(1));
		return new BoundingBox(ll, ur);
	}
}