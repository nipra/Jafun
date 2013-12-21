//Filename: ForPrimes.java
public class ForPrimes {
  final static int MAX = 100;
  public static void main( String[] args ) {
    numbers:
      for (int num = 1; num < MAX; num++) {
        int divLim = (int) Math.sqrt( num );
        for (int div = 2; div <= divLim; div++)
          if ((num % div) == 0) continue numbers;
        System.out.println( num );
      }
  }
}