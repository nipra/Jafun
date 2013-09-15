class BankrupcyException
      extends RuntimeException {}            // (1) Unchecked Exception
class TooManyHotelsException
      extends Exception {}                   // (2) Checked Exception

class Hotel {
  // Static Members
  private static boolean bankrupt   = true;
  private static int     noOfHotels = 11;
  private static Hotel[] hotelPool;

  static {                                    // (3) Static block
    try {                                     // (4) Handles checked exception
      if (noOfHotels > 10)
        throw new TooManyHotelsException();
    } catch (TooManyHotelsException e) {
      noOfHotels = 10;
      System.out.println("No. of hotels adjusted to " + noOfHotels);
    }
    hotelPool = new Hotel[noOfHotels];
  }

  static {                                    // (5) Static block
    if (bankrupt)
      throw new BankrupcyException();         // (6) Throws unchecked exception
  }
  // ...
}

public class ExceptionInStaticInitBlocks {
  public static void main(String[] args) {
    new Hotel();
  }
}