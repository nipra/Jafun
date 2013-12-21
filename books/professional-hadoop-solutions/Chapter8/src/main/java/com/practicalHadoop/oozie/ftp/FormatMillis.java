package com.practicalHadoop.oozie.ftp;

public class FormatMillis
{
	public static String millisAsString(long millis)
	{
		
		int totalSec = (int)(millis / 1000);
		int reportMillis = (int)(millis % 1000);
		int totalMin = totalSec / 60;
		int reportSec = totalSec % 60;
		int totaHours =  totalMin / 60;
		int reportMin =  totalMin % 60;
		
		return "" + totaHours + " : " + reportMin + " : " + reportSec + " : " + reportMillis;
	}
	
	static public void main(String [] args)
	{
		long sec = 1000;
		long min = sec * 60;
		long hour = min * 60;
		
		long millis = 200 + 3 * sec + 4 * min + 2 * hour;
		System.out.println("" + millis + " => " + FormatMillis.millisAsString(millis));
	}
}
