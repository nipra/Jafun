package com.practicalHadoop.lucene.spatial.geometry;

public enum GeometryType {

	POINT ("point"),
	BOUNDINGBOX ("box"),
	POLYGON ("polygon"),
	CIRCLE ("circle");

	/**
	 * The String field value.
	 */
	private final String _field;

    public static GeometryType toGeometryType(String shape) {
        for (GeometryType next : GeometryType.values()) {
            if (next.toString().equals(shape)) {
                return next;
            }
        }
        return null;
    }

	@Override
    public String toString() {
		return _field;
	}


	/**
	 * create a type base on the string value
	 *
	 * @param urlString the name of the element
	 */
	private GeometryType(String value) {
		_field = value;
	}
}