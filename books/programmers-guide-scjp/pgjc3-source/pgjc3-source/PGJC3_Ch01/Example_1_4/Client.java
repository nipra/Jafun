//Filename: Client.java
public class Client {

  public static void main(String[] args) {

    // Create a stack.
    CharStack stack = new CharStack(40);

    // Create a string to push on the stack:
    String str = "!no tis ot nuf era skcatS";
    int length = str.length();
    System.out.println("Original string: " + str);

    // Push the string char by char onto the stack:
    for (int i = 0; i < length; i++) {
      stack.push(str.charAt(i));
    }

    System.out.print("Reversed string: ");
    // Pop and print each char from the stack:
    while (!stack.isEmpty()) { // Check if the stack is not empty.
      System.out.print(stack.pop());
    }
    System.out.println();
  }
}