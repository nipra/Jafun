import java.util.*;
class ArrayList<E> extends AbstractList<E> implements RandomAccess {
    private E[] arr;
    private int size = 0;
    public ArrayList(int cap) {
	if (cap < 0)
	    throw new IllegalArgumentException("Illegal Capacity: "+cap);
	arr = (E[])new Object[cap];  // unchecked
    }
    public ArrayList() { this(10); }
    public ArrayList(Collection<? extends E> c) {
	this(c.size()); addAll(c);
    }
    public void ensureCapacity(int mincap) {
        int oldcap = arr.length;
        if (mincap > oldcap) {
            int newcap = Math.max(mincap, (oldcap*3)/2+1);
            E[] oldarr = arr;
            arr = (E[])new Object[newcap];  // unchecked cast
            System.arraycopy(oldarr,0,arr,0,size);
        }
    }
    public int size() { return size; }
    private void checkBounds(int i, int size) {
	if (i < 0 || i >= size)
	    throw new IndexOutOfBoundsException("Index: "+i+", Size: "+size);
    }
    public E get(int i) {
	checkBounds(i,size); return arr[i];
    }
    public E set(int i, E elt) {
	checkBounds(i,size); E old = arr[i]; arr[i] = elt; return old;
    }
    public void add(int i, E elt) {
	checkBounds(i,size+1);
	ensureCapacity(size+1);
	System.arraycopy(arr,i,arr,i+1,size-i);
	arr[i] = elt;  size++;
    }
    public E remove(int i) {
	checkBounds(i,size);
	E old = arr[i];  arr[i] = null;  size--;
	System.arraycopy(arr,i+1,arr,i,size-i);
	return old;
    }
    public <T> T[] toArray(T[] newarr) {
	if (newarr.length < size) {
	    newarr = GenericArray.newInstance(newarr, size);
	}
	System.arraycopy(arr,0,newarr,0,size);
	if (size < newarr.length) newarr[size] = null;
	return newarr;
    }
}
class ArrayListTest {
    public static void main(String... args) {
	List<String> l = new ArrayList<String>(Arrays.asList("this","is","a","test"));
	assert l.toString().equals("[this, is, a, test]");
	l.add(2, "only");
	assert l.toString().equals("[this, is, only, a, test]");
	l.add(0, "is");
	assert l.toString().equals("[is, this, is, only, a, test]");
	String s = l.remove(2);
	assert s.equals("is");
	assert l.toString().equals("[is, this, only, a, test]");
	l.set(2, "just");
	assert l.toString().equals("[is, this, just, a, test]");
	String[] a = l.toArray(new String[0]);
	assert Arrays.toString(a).equals("[is, this, just, a, test]");
	String[] b = { "x", "x", "x", "x", "x", "x", "x", "x" };
	l.toArray(b);
	assert Arrays.toString(b).equals("[is, this, just, a, test, null, x, x]");
        System.out.println(l);
    }
}
class GenericArray {
    public static <T> T[] newInstance(Class<T> c, int size) {
	return (T[])java.lang.reflect.Array.newInstance(c, size);  // unchecked
    }
    public static <T> Class<T> getComponentType(T[] a) {
	return (Class<T>)a.getClass().getComponentType();  // unchecked
    }
    public static <T> T[] newInstance(T[] arr, int size) {
	return newInstance(getComponentType(arr), size);
    }
}
