
public class ReferenceConversion {

  public static void main(String[] args) {
    // Reference declarations:
    Object        objRef;
    StackImpl     stackRef;
    SafeStackImpl safeStackRef;
    IStack        iStackRef;
    ISafeStack    iSafeStackRef;

    // SourceType is a class type:
    safeStackRef  = new SafeStackImpl(10);
    objRef        = safeStackRef;    // (1) Always possible
    stackRef      = safeStackRef;    // (2) Subclass to superclass assignment
    iStackRef     = stackRef;        // (3) StackImpl implements IStack
    iSafeStackRef = safeStackRef;    // (4) SafeStackImpl implements ISafeStack

    // SourceType is an interface type:
    objRef    = iStackRef;           // (5) Always possible
    iStackRef = iSafeStackRef;       // (6) Sub- to super-interface assignment

    // SourceType is an array type:
    Object[]        objArray        = new Object[3];
    StackImpl[]     stackArray      = new StackImpl[3];
    SafeStackImpl[] safeStackArray  = new SafeStackImpl[5];
    ISafeStack[]    iSafeStackArray = new ISafeStack[5];
    int[]           intArray        = new int[10];

    // Reference value assignments:
    objRef     = objArray;           // (7) Always possible
    objRef     = stackArray;         // (8) Always possible
    objArray   = stackArray;         // (9) Always possible
    objArray   = iSafeStackArray;    // (10) Always possible
    objRef     = intArray;           // (11) Always possible
    //  objArray   = intArray;       // (12) Compile-time error
    stackArray = safeStackArray;     // (13) Subclass array to superclass array
    iSafeStackArray = safeStackArray;// (14) SafeStackImpl implements ISafeStack

    // Method Invocation Conversions:
    System.out.println("First call:");
    sendParams(stackRef, safeStackRef, iStackRef,
               safeStackArray, iSafeStackArray);                    // (15)
    //  Call Signature: sendParams(StackImpl, SafeStackImpl, IStack,
    //                             SafeStackImpl[], ISafeStack[]);

    System.out.println("Second call:");
    sendParams(iSafeStackArray, stackRef, iSafeStackRef,
               stackArray, safeStackArray);                         // (16)
    //  Call Signature: sendParams(ISafeStack[], StackImpl, ISafeStack,
    //                             StackImpl[], SafeStackImpl[]);
  }

  public static void sendParams(Object objRefParam, StackImpl stackRefParam,
      IStack iStackRefParam, StackImpl[] stackArrayParam,
      final IStack[] iStackArrayParam) {                            // (17)
    //  Signature: sendParams(Object, StackImpl, IStack, StackImpl[], IStack[])
    //  Print class name of object denoted by the reference at runtime.
    System.out.println(objRefParam.getClass());
    System.out.println(stackRefParam.getClass());
    System.out.println(iStackRefParam.getClass());
    System.out.println(stackArrayParam.getClass());
    System.out.println(iStackArrayParam.getClass());
  }
}
