package com.petehouston.jsoup;

public class LogUtils {

	public static final boolean LOG_MODE = true;
	
	public static void d(String clsName, String methodName, String content) {
		if(LOG_MODE) {
			System.out.println(String.format("[%s:%s] %s", clsName, methodName, content));
		}
	}
}
