//Source Filename: CharStack.java
public class CharStack {         // Class name
  // Class Declarations:

  // Fields:                                                           (1)
  private char[] stackArray;     // The array implementing the stack.
  private int    topOfStack;     // The top of the stack.

  // Constructor:                                                      (2)
  public CharStack(int capacity) {
    stackArray = new char[capacity];
    topOfStack = -1;
  }

  // Methods:                                                          (3)
  public void push(char element) { stackArray[++topOfStack] = element; }
  public char pop()              { return stackArray[topOfStack--]; }
  public char peek()             { return stackArray[topOfStack]; }
  public boolean isEmpty()       { return topOfStack < 0; }
  public boolean isFull()        { return topOfStack == stackArray.length - 1; }
}