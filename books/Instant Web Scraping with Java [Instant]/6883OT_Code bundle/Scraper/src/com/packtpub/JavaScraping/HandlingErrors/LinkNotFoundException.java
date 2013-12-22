package com.packtpub.JavaScraping.HandlingErrors;

public class LinkNotFoundException extends Exception{

    public LinkNotFoundException(String msg) 
	{
		super(msg);
    }

}