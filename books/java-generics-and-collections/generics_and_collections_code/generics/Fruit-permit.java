import java.util.*;
class Fruit implements Comparable<Fruit> {
    String name;
    int size;
    public Fruit(String name, int size) {
	this.name = name; this.size = size;
    }
    public int compareTo(Fruit that) {
	return
	    this.size < that.size ? - 1 :
	    this.size == that.size ? 0 : 1 ;
    }
    public String toString() {
	return this.name + "(" + size + ")";
    }
    public boolean equals(Object o) {
	return (o instanceof Fruit &&
		name.equals(((Fruit)o).name) &&
		size == ((Fruit)o).size) ;
    }
}
class Apple extends Fruit {
    public Apple (int size) {
	super("Apple", size);
    }
}
class Orange extends Fruit {
    public Orange (int size) {
	super("Orange", size);
    }
}
class Test {
    public static void main (String... args) {
	List<Apple> apples = Arrays.asList(new Apple(1), new Apple(10));
	List<Orange> oranges = Arrays.asList(new Orange(1), new Orange(10));
	List<Fruit> fruits = Arrays.<Fruit>asList(new Apple(1), new Orange(10));
	assert Collections.max(apples).equals(new Apple(10));
	assert Collections.max(oranges).equals(new Orange(10));
	assert Collections.max(fruits).equals(new Orange(10));  // ok
	System.out.println(Collections.max(apples));
	System.out.println(Collections.max(oranges));
	System.out.println(Collections.max(fruits));
	Apple weeApple = new Apple(1);
	Apple bigApple = new Apple(2);
	apples = Arrays.asList(weeApple, bigApple);
	System.out.println(weeApple.compareTo(bigApple));
	System.out.println(Collections.max(apples) == bigApple);
    }
}
