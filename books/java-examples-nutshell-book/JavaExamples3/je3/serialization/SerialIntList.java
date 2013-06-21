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
package je3.serialization;
import java.io.*;

/**
 * A simple class that implements a growable array of ints, and knows
 * how to serialize itself as efficiently as a non-growable array.
 **/
public class SerialIntList implements Serializable {
    // These are the fields of this class.  By default the serialization
    // mechanism would just write them out.  But we've declared size to be
    // transient, which means it will not be serialized.  And we've
    // provided writeObject() and readObject() methods below to customize
    // the serialization process.
    protected int[] data = new int[8]; // An array to store the numbers.
    protected transient int size = 0;  // Index of next unused element of array
    
    /** Return an element of the array */
    public int get(int index) {
        if (index >= size) throw new ArrayIndexOutOfBoundsException(index);
        else return data[index];
    }
    
    /** Add an int to the array, growing the array if necessary */
    public void add(int x) {
        if (data.length==size) resize(data.length*2);  // Grow array if needed.
        data[size++] = x;                              // Store the int in it.
    }
    
    /** An internal method to change the allocated size of the array */
    protected void resize(int newsize) {
	int[] newdata = new int[newsize];            // Create a new array
        System.arraycopy(data, 0, newdata, 0, size); // Copy array elements.
	data = newdata;                              // Replace old array
    }

    /**
     * Get rid of unused array elements before serializing the array.  This
     * may reduce the number of array elements to serialize.  It also makes
     * data.length == size, so there is no need to safe the (transient) size
     * field. The serialization mechanism will automatically call this method
     * when serializing an object of this class.  Note that this must be 
     * declared private.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        if (data.length > size) resize(size);  // Compact the array.
        out.defaultWriteObject();              // Then write it out normally.
    }
    
    /**
     * Restore the transient size field after deserializing the array.
     * The serialization mechanism automatically calls this method.
     */
    private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();                // Read the array normally.
        size = data.length;                    // Restore the transient field.
    }

    /**
     * Does this object contain the same values as the object o?
     * We override this Object method so we can test the class.
     **/
    public boolean equals(Object o) {
	if (!(o instanceof SerialIntList)) return false;
	SerialIntList that = (SerialIntList) o;
	if (this.size != that.size) return false;
	for(int i = 0; i < this.size; i++)
	    if (this.data[i] != that.data[i]) return false;
	return true;
    }

    /** We must override this method when we override equals(). */
    public int hashCode() {
	int code = 1; // non-zero to hash [0] and [] to distinct values
	for(int i = 0; i < size; i++)
	    code = code*997 + data[i];  // ignore overflow
	return code;
    }

    /** A main() method to prove that it works */
    public static void main(String[] args) throws Exception {
	SerialIntList list = new SerialIntList();
	for(int i = 0; i < 100; i++) list.add((int)(Math.random()*40000));
	SerialIntList copy = (SerialIntList)Serializer.deepclone(list);
	if (list.equals(copy)) System.out.println("equal copies");
	Serializer.store(list, new File("intlist.ser"));
    }
}
