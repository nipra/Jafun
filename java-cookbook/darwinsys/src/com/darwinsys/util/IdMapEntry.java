package com.darwinsys.util;

/**
 * An IdMapEntry is one int-String pair, for example, an "id" or primary key
 * in a database and a name or description of the item in the named row.
 * IdMapEntry objects are immutable.
 * <p>Rather like a java.util.Map.Entry but without needing to convert.
 * @version $Id: IdMapEntry.java,v 1.6 2004/06/01 02:51:38 ian Exp $
 */
public class IdMapEntry {
	private final int id;
	private final String name;

	public IdMapEntry(final int i, final String n) {
		this.id = i;
		this.name = n;
	}

	public int getKey() {
		return id;
	}

	public String getValue() {
		return name;
	}
}
