class Hotel {
  private int noOfRooms        = 12;                                 // (1)
  private int maxNoOfGuests    = initMaxGuests();                    // (2) Bug
  private int occupancyPerRoom = 2;                                  // (3)

  public int initMaxGuests() {                                       // (4)
    System.out.println("occupancyPerRoom: " + occupancyPerRoom);
    System.out.println("maxNoOfGuests:    " + noOfRooms * occupancyPerRoom);
    return noOfRooms * occupancyPerRoom;
  }

  public int getMaxGuests() { return maxNoOfGuests; }                // (5)

  public int getOccupancy() { return occupancyPerRoom; }             // (6)
}
//________________________________________________________________________
public class TestOrder {
  public static void main(String[] args) {
    Hotel hotel = new Hotel();                                       // (7)
    System.out.println("After object creation: ");
    System.out.println("occupancyPerRoom: " + hotel.getOccupancy()); // (8)
    System.out.println("maxNoOfGuests:    " + hotel.getMaxGuests()); // (9)
  }
}