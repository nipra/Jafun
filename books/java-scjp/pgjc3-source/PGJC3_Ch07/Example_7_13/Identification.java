
public class Identification {
  public static void main(String[] args) {
    Object obj = new Object();
    StackImpl stack = new StackImpl(10);
    SafeStackImpl safeStack = new SafeStackImpl(5);
    IStack iStack;

    System.out.println("(1): " +
        (null instanceof Object));     // Always false.
    System.out.println("(2): " +
        (null instanceof IStack));     // Always false.

    System.out.println("(3): " +
        (stack instanceof Object));    // true: instance of subclass of Object.
    System.out.println("(4): " +
        (obj instanceof StackImpl));   // false: Object not subtype of StackImpl.
    System.out.println("(5): " +
        (stack instanceof StackImpl)); // true: instance of StackImpl.

    System.out.println("(6): " +
        (obj instanceof IStack));      // false: Object does not implement IStack.
    System.out.println("(7): " +
        (safeStack instanceof IStack));// true: SafeStackImpl implements IStack.

    obj = stack;                       // Assigning subclass to superclass.
    System.out.println("(8): " +
        (obj instanceof StackImpl));   // true: instance of StackImpl.
    System.out.println("(9): " +
        (obj instanceof IStack));      // true: StackImpl implements IStack.
    System.out.println("(10): " +
        (obj instanceof String));      // false: No relationship.

    iStack = (IStack) obj;        // Cast required: superclass assigned subclass.
    System.out.println("(11): " +
        (iStack instanceof Object));     // true: instance of subclass of Object.
    System.out.println("(12): " +
        (iStack instanceof StackImpl));  // true: instance of StackImpl.

    String[] strArray = new String[10];
    //  System.out.println("(13): " +
    //      (strArray instanceof String);// Compile-time error, no relationship.
    System.out.println("(14): " +
        (strArray instanceof Object));   // true: array subclass of Object.
    System.out.println("(15): " +
        (strArray instanceof Object[])); // true: array subclass of Object[].
    System.out.println("(16): " +
        (strArray[0] instanceof Object));// false: strArray[0] is null.
    System.out.println("(17): " +
        (strArray instanceof String[])); // true: array of String.

    strArray[0] = "Amoeba strip";
    System.out.println("(18): " +
        (strArray[0] instanceof String));// true: instance of String.
  }
}