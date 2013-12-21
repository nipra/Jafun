import java.util.*;
class Fruit {
    protected String name;
    protected int size;
    public Fruit (String name, int size) {
	this.name = name; this.size = size;
    }
    public boolean equals (Object o) {
	if (o instanceof Fruit) {
	    Fruit that = (Fruit)o;
	    return this.name == that.name && this.size == that.size;
	} else return false;
    }
    public String toString () {
	return this.name + "(" + size + ")";
    }
}
class Orange extends Fruit implements Comparable<Orange> {
    public Orange (int size) {
	super("Orange", size);
    }
    public int compareTo (Orange that) {
	return
	    this.size < that.size ? - 1 :
	    this.size == that.size ? 0 : 1 ;
    }
}
class Apple extends Fruit implements Comparable<Apple> {
    public Apple (int size) {
	super("Apple", size);
    }
    public int compareTo (Apple that) {
	return
	    this.size < that.size ? - 1 :
	    this.size == that.size ? 0 : 1 ;
    }
}
class Test {
    public static void main (String... args) {
	List<Apple> apples  = Arrays.<Apple>asList(new Apple(1), new Apple(10));
	List<Orange> oranges  = Arrays.<Orange>asList(new Orange(1), new Orange(10));
	List<Fruit> fruits  = Arrays.<Fruit>asList(new Apple(1), new Orange(10));
	assert Collections.max(apples).equals(new Apple(10));
	assert Collections.max(oranges).equals(new Orange(10));
	// assert Collections.max(fruits).equals(new Orange(10));  // compile-time error
	System.out.println(Collections.max(apples));
	System.out.println(Collections.max(oranges));
    }
}
