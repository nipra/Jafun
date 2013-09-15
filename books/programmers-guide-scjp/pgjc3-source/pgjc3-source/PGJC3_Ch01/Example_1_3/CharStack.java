//Filename CharStack.java
public class CharStack {
  // Instance variables:
  protected char[] stackArray;   // The array implementing the stack.
  protected int    topOfStack;   // The top of the stack.

  // Static variable
  private static int counter;                                      // (1)

  // Constructor now increments the counter for each object created.
  public CharStack(int capacity) {                                 // (2)
    stackArray = new char[capacity];
    topOfStack = -1;
    counter++;
  }

  // Instance methods:
  public void push(char element) { stackArray[++topOfStack] = element; }
  public char pop()              { return stackArray[topOfStack--]; }
  public char peek()             { return stackArray[topOfStack]; }
  public boolean isEmpty()       { return topOfStack < 0; }
  public boolean isFull()        { return topOfStack == stackArray.length - 1; }

  // Static method                                                    (3)
  public static int getInstanceCount() { return counter; }
}