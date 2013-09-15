import java.util.Iterator;

/** Interface of a generic stack */
public interface IStack<E> extends Iterable<E> {
  void push(E element);           // Add the element to the top of the stack
  E pop();                        // Remove the element at the top of the stack.
  E peek();                       // Get the element at the top of the stack.
  int size();                     // No. of elements on the stack.
  boolean isEmpty();              // Determine is the stack is empty.
  boolean isMember(E element);    // Determine if the element is in the stack.
  Iterator<E> iterator();         // Get an iterator for the stack
  E[] toArray(E[] toArray);       // Copy elements from stack to array
  String toString();              // Return suitable string representation of
                                  // elements on the stack: (e1, e2, ..., en)
}