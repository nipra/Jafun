
public class Utilities {

  // The key and the array element type can be any type.
  static boolean containsV1(Object key, Object[] array) { // (1) Non-generic
                                                          //     version
    for(Object element : array)
      if(key.equals(element)) return true;
    return false;
  }

  // The key and the array element type are the same.
  static <E> boolean containsV2(E key, E[] array) {       // (2) Generic version
    for(E element : array)
      if(key.equals(element)) return true;
    return false;
  }

  // The type of the key is a subtype of the array element type.
  static <E, K extends E> boolean containsV3(K key, E[] array) {
    for(E element : array)
      if(key.equals(element)) return true;
    return false;
  }

  public static void main(String[] args) {
    Integer[] intArray = {10, 20, 30};

    // (1) E is Integer.
    // Method signature:      containsV2(Integer, Integer[])
    // Method call signature: containsV2(Integer, Integer[])
    assert Utilities.<Integer>containsV2(20, intArray) == true;

    // (2) E is Number.
    // Method signature:      containsV2(Number, Number[])
    // Method call signature: containsV2(Double, Integer[])
    assert Utilities.<Number>containsV2(30.5, intArray) == false;

    // (3) E is Comparable<Integer>.
    // Method signature:      containsV2(Comparable<Integer>,
    //                                   Comparable<Integer>[])
    // Method call signature: containsV2(Integer, Integer[]).
    assert Utilities.<Comparable<Integer>> containsV2(20, intArray) == true;

    // (4) E is Integer.
    // Method signature:      containsV2(Integer, Integer[])
    // Method call signature: containsV2(Double,  Integer[])
//  assert Utilities.<Integer>containsV2(30.5, intArray) == false;  // Error!

//  assert <Integer>containsV2(20, intArray) == true;     // (5) Syntax error

    // (6) E is inferred to be Integer.
    // Method signature:      containsV2(Integer, Integer[])
    // Method call signature: containsV2(Integer, Integer[])
    assert Utilities.containsV2(20, intArray) == true;

    // (7) E is inferred to be ? extends Number.
    // Method signature:      containsV2(? extends Number, ? extends Number[])
    // Method call signature: containsV2(Double, Integer[])
    assert Utilities.containsV2(30.5, intArray) == false;

    // (8) E is inferred to be ? extends Object.
    // Method signature:      containsV2(? extends Object, ? extends Object[])
    // Method call signature: containsV2(String, Integer[])
    assert Utilities.containsV2("Hi", intArray) == false;

    // Method signature:      containsV2(Integer, Integer[])
    // Method call signature: containsV2(String, Integer[])
//  assert Utilities.<Integer>containsV2("Hi", intArray) == false;// (9) Error!

//  assert Utilities.containsV3("Hi", intArray) == false;         // (10) Error!
//  assert Utilities.containsV3(30.5, intArray) == false;         // (11) Error!

    // (12) E is Number. K is Number. The constraint (K extends E) is satisfied.
    // Method signature:      containsV3(Number, Number[])
    // Method call signature: containsV3(Double, Integer[])
    assert Utilities.<Number, Number>containsV3(30.0, intArray) == false;

    // (13) E is Integer. K is Number.
    // The constraint (K extends E) is not satisfied.
//  assert Utilities.<Integer, Number>
//  containsV3(30.5, intArray) == false;                  // Compile-time Error!

    // (14) E is Number. K is Integer. The constraint (K extends E) is satisfied.
    // Method signature:      containsV3(Integer, Number[])
    // Method call signature: containsV3(Double, Integer[])
//  assert Utilities.<Number, Integer>
//  containsV3(30.5, intArray) == false;                  // Compile-time Error!

    // (15) Incorrect no. of type parameters.
//  assert Utilities.<Number> containsV3(30.5, intArray) == false;     // Error!
  }
}