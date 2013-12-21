package com.practicalHadoop.photo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatedPhoto {

	private static String[] _month = {"jan", "feb", "mar", "apr", "may", "jun", 
									 "jul", "aug", "sep", "oct", "nov", "dec" };
	
	long _date;
	byte[] _picture;
	
	public DatedPhoto(){
		_picture = null;
		_date = - 1;
	}
	public DatedPhoto(long date, byte[] picture){
		_date = date;
		_picture = picture;
	}
	public String getDate() {
		return timeToString(_date);
	}
	public long getLongDate() {
		return _date;
	}
	public void setDate(long date) {
		_date = date;
	}
	public byte[] getPicture() {
		return _picture;
	}
	public void setPicture(byte[] picture) {
		_picture = picture;
	}
	public static String timeToString(long time){
		Date d = new Date(time);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(d);
		return Integer.toString(c.get(Calendar.YEAR)) + _month[c.get(Calendar.MONTH)] + 
			   Integer.toString(c.get(Calendar.DAY_OF_MONTH)) +Integer.toString(c.get(Calendar.HOUR_OF_DAY)) + 
			   Integer.toString(c.get(Calendar.MINUTE)) + Integer.toString(c.get(Calendar.SECOND));
	}
}