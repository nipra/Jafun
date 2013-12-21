package com.practicalHadoop.hbase.customFilter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;
import org.apache.hadoop.hbase.util.Bytes;

public class BoundingBoxFilter extends WritableByteArrayComparable{


	private BoundingBox _bb;

	/**
	 * Nullary constructor, for Writable
	 */
	
	public BoundingBoxFilter(){}

	/**
	 * Constructor.
	 * @param value the value to compare against
	 *      */
	public BoundingBoxFilter(byte [] value) throws Exception{
		
		this(Bytes.toString(value));
	}
		/**
		 * Constructor.
		 * @param value the value to compare against
		 *      */
		
	public BoundingBoxFilter(String v) throws Exception{
			
		System.out.println(" Invoking constractor with the string " + v);
		_bb = stringToBB(v);
	}
		
	private BoundingBox stringToBB(String v)throws Exception{

		System.out.println(" Invoking bytesToBB method, String " + v);		

		StringTokenizer tk = new StringTokenizer(v, ",");
		if(tk.countTokens() != 4)
			throw new Exception("Wrong input");
		
		double lowerLeftLat = 0.;
		double lowerLeftLon = 0.;
		double upperRightLat = 0.;
		double upperRightLon = 0.;
		for(int index = 0; index < 4; index++){
			String vString = tk.nextToken();
			Double vDouble = Double.parseDouble(vString);
			switch (index) {
			case 0:
				lowerLeftLat = vDouble;
				break;
			case 1:
				lowerLeftLon = vDouble;
				break;
			case 2:
				upperRightLat = vDouble;
				break;
			case 3:
				upperRightLon = vDouble;
				break;
			}
		}
		BoundingBox bb = new BoundingBox(lowerLeftLat, upperRightLat, lowerLeftLon, upperRightLon);
		System.out.println(" Bounding box is "
				+ "(" + bb.getLowerLeft().getLatitude() + "," + bb.getLowerLeft().getLatitude() + ")"
				+ "(" + bb.getUpperRight().getLatitude() + "," + bb.getUpperRight().getLatitude() + ")");	
		return bb;
	}


	@Override
	public void readFields(DataInput in) throws IOException {

		System.out.println(" Invoking readFields method ");	
		String data = new String(Bytes.readByteArray(in));
		System.out.println(" Data is : " + data);	
		try {
			_bb = stringToBB(data);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	private Point bytesToPoint(byte[] bytes){
		
		String v = new String(bytes);
		System.out.println(" Invoking bytesToPoint method, String " + v);		
		StringTokenizer tk = new StringTokenizer(v, ",");
		double lat = 0.;
		double lon = 0.;
		for(int index = 0; index < 2; index++){
			String vString = tk.nextToken();
			Double vDouble = Double.parseDouble(vString);
			switch (index) {
			case 0:
				lat = vDouble;
				break;
			case 1:
				lon = vDouble;
				break;
			}
		}
		Point p = new Point(lat, lon);
		System.out.println(" Point is " + "(" + p.getLatitude() + "," + p.getLatitude() + ")");	
		return p;
	}

	@Override
	public void write(DataOutput out) throws IOException {

		String value = null;
		if(_bb != null)
			value = _bb.getLowerLeft().getLatitude() + ","  + _bb.getLowerLeft().getLongitude() + 
			"," + _bb.getUpperRight().getLatitude() + ","  + _bb.getUpperRight().getLongitude();
		else
			value = "no bb";
		System.out.println(" Invoking write method, String " + value);		
		Bytes.writeByteArray(out, value.getBytes());
	}

	@Override
	public int compareTo(byte[] bytes) {

		System.out.println(" Invoking compare method " + _bb);		
		Point point = bytesToPoint(bytes);
		return _bb.contains(point) ? 0 : 1;
	}
}