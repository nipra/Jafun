package com.practicalHadoop.lucene.tables;

public class NotInitializedException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotInitializedException(){
		super();
	}
	public NotInitializedException(String reason){
		super(reason);
	}
	public NotInitializedException(Throwable cause){
		super(cause);
	}
}
