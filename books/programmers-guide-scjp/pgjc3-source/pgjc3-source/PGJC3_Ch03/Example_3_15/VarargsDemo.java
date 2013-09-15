
public class VarargsDemo {

  public static void flexiPrint(Object... data) { // Object[]
    // Print the name of the Class object for the varargs parameter.
    System.out.print("\nType: " + data.getClass().getName());

    System.out.println("  No. of elements: " + data.length);

    for(int i = 0; i < data.length; i++)
      System.out.print(data[i] + " ");
    if (data.length != 0)
      System.out.println();
  }

  public static void main(String... args) {
    int    day   = 1;
    String month = "March";
    int    year  = 2009;

    // Passing primitives and non-array types.
    flexiPrint();                        // (1) new Object[] {}
    flexiPrint(day);                     // (2) new Object[] {new Integer(day)}
    flexiPrint(day, month);              // (3) new Object[] {new Integer(day),
                                         //                   month}
    flexiPrint(day, month, year);        // (4) new Object[] {new Integer(day),
                                         //                   month,
                                         //                   new Integer(year)}

    // Passing an array type.
    Object[] dateInfo = {day,            // (5) new Object[] {new Integer(day),
                         month,          //                   month,
                         year};          //                   new Integer(year)}
    flexiPrint(dateInfo);                // (6) Non-varargs call
    flexiPrint((Object) dateInfo);       // (7) new Object[] {(Object) dateInfo}
    flexiPrint(new Object[] {dateInfo}); // (8) Non-varargs call

    // Explicit varargs or non-varargs call.
    flexiPrint(args);                    // (9) Warning!
    flexiPrint((Object) args);           // (10) Explicit varargs call.
    flexiPrint((Object[]) args);         // (11) Explicit non-varargs call
  }
}