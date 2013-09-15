/** Determine if a string is a palindrome. */
public class Palindrome {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Usage: java Palindrome <word>");
      return;
    }
    String word = args[0];
    StringBuilder reverseWord = new StringBuilder(word);
    reverseWord.reverse();
    boolean isPalindrome = word.equals(reverseWord.toString());
    System.out.println("The word " + word + " is " +
                       (isPalindrome ? "" : "not ") + "a palindrome");
  }
}