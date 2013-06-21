/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
package je3.thread;

/**
 * A growable array of int values, suitable for use with multiple threads.
 **/
public class ThreadSafeIntList {
    protected int[] data;    // This array holds the integers
    protected int size;      // This is how many it current holds

    // Static final values are constants.  This one is private.
    private static final int DEFAULT_CAPACITY = 8;

    // Create a ThreadSafeIntList with a default capacity
    public ThreadSafeIntList() {
	// We don't have to set size to zero because newly created objects
	// automatically have their fields set to zero, false, and null.
	data = new int[DEFAULT_CAPACITY];  // Allocate the array
    }

    // This constructor returns a copy of an existing ThreadSafeIntList.
    // Note that it synchronizes its access to the original list.
    public ThreadSafeIntList(ThreadSafeIntList original) {
	synchronized(original) {
	    this.data = (int[]) original.data.clone();
	    this.size = original.size;
	}
    }

    // Return the number of ints stored in the list
    public synchronized int size() { return size; }

    // Return the int stored at the specified index
    public synchronized int get(int index) {
	if (index < 0 || index >= size) // Check that argument is legitimate
	    throw new IndexOutOfBoundsException(String.valueOf(index));
	return data[index];
    }

    // Append a new value to the list, reallocating if necessary
    public synchronized void add(int value) { 
	if (size == data.length) setCapacity(size*2); // realloc if necessary
	data[size++] = value;                         // add value to list
    }

    // Remove all elements from the list
    public synchronized void clear() { size = 0; }

    // Copy the contents of the list into a new array and return that array
    public synchronized int[] toArray() { 
	int[] copy = new int[size];
	System.arraycopy(data, 0, copy, 0, size);
	return copy;
    }

    // Reallocate the data array to enlarge or shrink it.
    // Not synchronized because it is always called from synchronized methods.
    protected void setCapacity(int n) {
	if (n == data.length) return;                // Check size
	int[] newdata = new int[n];                  // Allocate the new array
	System.arraycopy(data, 0, newdata, 0, size); // Copy data into it
	data = newdata;                              // Replace old array
    }
}
