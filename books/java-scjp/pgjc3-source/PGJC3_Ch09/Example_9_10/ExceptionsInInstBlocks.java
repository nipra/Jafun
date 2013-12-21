class RoomOccupancyTooHighException
      extends Exception {}                    // (1) Checked exception
class BankrupcyException
      extends RuntimeException {}             // (2) Unchecked exception
//_______________________________________________________________________________
class Hotel {
  // Instance Members
  private boolean bankrupt         = true;
  private int     noOfRooms        = 215;
  private int     occupancyPerRoom = 5;
  private int     maxNoOfGuests;

  {                                           // (3) Instance initializer block
    try {                                     // (4) Handles checked exception
      if (occupancyPerRoom > 4)
        throw new RoomOccupancyTooHighException();
    } catch (RoomOccupancyTooHighException exception) {
      System.out.println("ROOM OCCUPANCY TOO HIGH: " + occupancyPerRoom);
      occupancyPerRoom = 4;
    }
    maxNoOfGuests = noOfRooms * occupancyPerRoom;
  }

  {                                           // (5) Instance initializer block
    if (bankrupt)
      throw new BankrupcyException();         // (6) Throws unchecked exception
  }    // ...
}
//_______________________________________________________________________________
public class ExceptionsInInstBlocks {
  public static void main(String[] args) {
    new Hotel();
  }
}