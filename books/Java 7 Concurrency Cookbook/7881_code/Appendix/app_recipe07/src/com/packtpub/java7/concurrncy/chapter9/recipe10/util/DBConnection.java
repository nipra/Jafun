package com.packtpub.java7.concurrncy.chapter9.recipe10.util;

public class DBConnection {

	private static DBConnection connection;
	
	private DBConnection() {
		
	}
	
	public static DBConnection getConnection(){
		if (connection==null) {
			connection=new DBConnection();
		}
		return connection;
	}
}
