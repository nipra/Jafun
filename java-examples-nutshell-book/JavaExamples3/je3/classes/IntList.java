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
package je3.classes;

/**
 * A growable array of primitive int values.  It is more efficient than
 * ArrayList or Vector because Integer objects are not used.
 **/
public class IntList implements Comparable {
    // These are the fields of this class.
    // They are protected so that subclasses can access them directly.
    protected int[] data;    // This array holds the integers
    protected int size;      // This is how many it current holds

    // Static final values are constants.  This one is private.
    private static final int DEFAULT_CAPACITY = 8;

    // This no-argument constructor creates an IntList with a default capacity
    public IntList() { this(DEFAULT_CAPACITY); }

    // This constructor allows us to specify the initial size.  Useful when
    // we have an approximate idea of how big the list will need to be.
    public IntList(int initialCapacity) {
	// We don't have to set size to zero because newly created objects
	// automatically have their fields set to zero, false, and null.
	data = new int[initialCapacity];  // Allocate the array
    }

    // This constructor returns a copy of an existing IntList.
    public IntList(IntList original) {
	// All arrays are Cloneable, and their clone() method is an easy way
	// to copy them. Cloneable is ill-defined and hard to implement 
	// correctly, however, so it is not usually worth making your classes
	// cloneable.  A copy constructor like this one is a good alternative.
	this.data = (int[]) original.data.clone();
	this.size = original.size;
    }

    // Return the number of ints stored in the list
    public int size() { return size; }

    // Return the int stored at the specified index
    public int get(int index) {
	if (index < 0 || index >= size) // Check that argument is legitimate
	    throw new IndexOutOfBoundsException(String.valueOf(index));
	return data[index];
    }

    // Append a new value to the list, reallocating if necessary
    public void add(int value) { 
	if (size == data.length) setCapacity(size*2); // realloc if necessary
	data[size++] = value;                         // add value to list
    }

    // Set the value at the specified index
    public void set(int index, int value) {
	if (index < 0 || index >= size)
	    throw new IndexOutOfBoundsException(String.valueOf(index));
	data[index] = value;
    }

    // Shrink the list so that its capacity is the same as its size.
    // This is useful to free up unneeded memory when we know the list
    // will not have any new values added to it.
    public void trim() { setCapacity(size); }

    // Remove all elements from the list
    public void clear() { size = 0; }

    // Copy the contents of the list into a new array and return that array
    public int[] toArray() { 
	int[] copy = new int[size];
	System.arraycopy(data, 0, copy, 0, size);
	return copy;
    }

    // This is a very useful method, especially for logging and debugging.
    // Consider overriding it in every class you write.
    public String toString() {
	// Repetitive string contatenation with the + operator is inefficient.
	// It is much better to use a StringBuffer here.  In Java 1.5, use
	// StringBuilder instead.
	StringBuffer b = new StringBuffer(size*7); // Guess string length
	b.append('[');                          // Start the array
	for(int i = 0; i < size; i++) {         // Loop through list elements 
	    if (i > 0) {                        // If not the first element
		b.append(", ");                 // put a comma before it
		if (i%8 == 0) b.append('\n');   // newline every 8 elements
	    }
	    b.append(data[i]);                  // append a number
	}
	b.append(']');                          // end the array.
	return b.toString();                    // Return as a string
    }


    // Does this object contain the same values as the object o?
    // This is an Object method that we override.  Note that we must also
    // override hashCode() to match this.
    public boolean equals(Object o) {
	// If o is the same object as this, then they are equal
	if (o == this) return true;

	// If o is null or of the wrong type, it is not equal
	if (!(o instanceof IntList)) return false;

	// It is an IntList, so we can cast it, and compare this to that.
	IntList that = (IntList) o;

	// If the lists have different sizes, they are not equal
	// Note that equal lists may have different array lengths.
	if (this.size != that.size) return false;

	// If any of their elements differ then they are not equal
	for(int i = 0; i < this.size; i++)
	    if (this.data[i] != that.data[i]) return false;

	// If we get here, then the two lists contain exactly the same values
	// in the same positions, so they are equal
	return true;
    }

    // Map this object to an integer hash code used for hashtable data
    // structures.  Objects that are equal() must have the same hashCode(), so
    // we always override this method when we override equals().
    // Note that non-equal objects may have the same hashCode(), but we strive
    // to minimize that possibility.
    public int hashCode() {
	// It would be legal to just add the values up and return that as
	// the hash code, but then the list [1,2,3] would have the same hash
	// code as [3,2,1] and [3,3], which is not desired behavior.  Using
	// multiplication and addition here makes the result dependent on
	// order, and spreads codes out acros the full range of ints.
	int code = 1; // non-zero to hash [0] and [] to distinct values
	for(int i = 0; i < size; i++)
	    code = code*997 + data[i];  // ignore overflow
	return code;
    }

    /**
     * The compareTo() method is defined by the Comparable interface.
     * It defines an ordering for IntList objects and allows them to be sorted.
     *
     * Return a negative value if this object is "less than" the argument.
     * Return a postive value if this object is "greater than" the argument.
     * Return zero if the two objects are equal.
     * The first list value that differs is used to determine ordering.
     * If one list is a prefix of the other, then the longer list is greater.
     */
    public int compareTo(Object o) {
	// Cast the argument to an IntList. This will correctly throw
	// CastClassException if the wrong type is passed
	IntList that = (IntList) o;

	int n = Math.min(this.size, that.size);  // get length of shorter list
	// Compare elements of the two lists, looking for one that differs
	for(int i = 0; i < n; i++) {
	    if (this.data[i] < that.data[i]) return -1;
	    if (this.data[i] > that.data[i]) return 1;
	}

	// If we get here, then the lists are equal, or one is a prefix
	// of the other.
	return this.size - that.size;
    }

    // Reallocate the data array to enlarge or shrink it.
    // This is a non-public method used internally to grow or trim() the array.
    protected void setCapacity(int n) {
	// We use a Java 1.4 assertion to make sure the class is calling
	// this private method correctly.  Compile with "-source 1.4" to make
	// the compiler recognize this statement and run with "-ea" to make
	// the VM actually test the assertion.
	// Syntax: assert <condition> : <error message>
	assert (n >= size) : (n + "<" +size);

	if (n == data.length) return;                // Check size
	int[] newdata = new int[n];                  // Allocate the new array
	System.arraycopy(data, 0, newdata, 0, size); // Copy data into it
	data = newdata;                              // Replace old array
    }
}
