
public class ReadingCharsFromString {
  public static void main(String[] args) {
    int[] frequencyData = new int [Character.MAX_VALUE];    // (1)
    String str = "You cannot change me!";                   // (2)

    // Count the frequency of each character in the string.
    for (int i = 0; i < str.length(); i++)                  // (3)
      try {
        frequencyData[str.charAt(i)]++;                     // (4)
      } catch(StringIndexOutOfBoundsException e) {
        System.out.println("Index error detected: "+ i +" not in range.");
      }
      
      // Print the character frequency.
      System.out.println("Character frequency for string: \"" + str + "\"");
      for (int i = 0; i < frequencyData.length; i++)
        if (frequencyData[i] != 0)
          System.out.println((char)i + " (code "+ i +"): " +
              frequencyData[i]);

      System.out.println("Copying into a char array:");
      char[] destination = new char [str.length()];
      str.getChars( 0,            7, destination, 0);       // (5) "You can"
      str.getChars(10, str.length(), destination, 7);       // (6) " change me!"
      
      // Print the character array.
      for (int i = 0; i < 7 + (str.length() - 10); i++)
        System.out.print(destination[i]);
      System.out.println();
  }
}