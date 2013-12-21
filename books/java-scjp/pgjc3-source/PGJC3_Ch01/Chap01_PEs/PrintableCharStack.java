//Filename: PrintableCharStack.java
public class PrintableCharStack extends CharStack {                 // (1)

  // Instance method
  public void printStackElements() {                                // (2)
    for (int i = 0; i <= topOfStack; i++)
      System.out.print(stackArray[i]); // print each char on terminal
    System.out.println();
  }

  // Constructor calls the constructor of the superclass explicitly.
  PrintableCharStack(int capacity) { super(capacity); }             // (3)
}