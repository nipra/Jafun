//Filename: WhilePrimes.java
public class WhilePrimes {
  final static int MAX = 100;
  public static void main( String[] args ) {
    int num = 1;
    numbers:
      while (num < MAX) {
        int number = num++;
        int divLim = (int) Math.sqrt( number );
        int div = 2;
        while (div <= divLim)
          if ((number % div++) == 0) continue numbers;
        System.out.println( number );
      }
  }
}