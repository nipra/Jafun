//Filename: Client.java
public class Client {

  public static void main(String[] args) {

    // Create a printable character stack.
    PrintableCharStack stack = new PrintableCharStack(40);

    // Create a string to push on the stack:
    String str = "!no tis ot nuf era skcatS";
    int length = str.length();

    // Push the string char by char onto the stack:
    for (int i = 0; i < length; i++) {
      stack.push(str.charAt(i));
    }

    System.out.print("Stack contents: ");
    stack.printStackElements();

    // Pop and print each char from the stack:
    while (!stack.isEmpty()) {
      System.out.print(stack.pop());
    }
    System.out.println();

    System.out.print("Stack contents: ");
    stack.printStackElements();
  }
}