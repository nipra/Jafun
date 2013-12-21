package com.practicalHadoop.reader.xml;

public class KeyString {
	
	private String _string;
	private boolean _firstOnly;
	
	public KeyString(String string, boolean firstOnly){
		
		_string = string;
		_firstOnly = firstOnly;
	}

	public String getString() {
		return _string;
	}

	public boolean isFirstOnly() {
		return _firstOnly;
	}
}
