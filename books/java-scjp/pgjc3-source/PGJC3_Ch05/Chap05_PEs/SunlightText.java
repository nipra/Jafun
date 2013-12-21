//Filename: Sunlight.java
public class Sunlight {
  public static void main(String[] args) {
    // Distance from sun (150 million kilometers)
    int kmFromSun = 150000000;

    int lightSpeed = 299792458; // meters per second

    // Convert distance to meters.
    int mFromSun = kmFromSun * 1000;

    int seconds = mFromSun / lightSpeed;

    System.out.print("Light will use ");
    printTime(seconds);
    System.out.println(" to travel from the sun to the earth.");
  }

  public static void printTime(int sec) {
    int min = sec / 60;
    sec = sec - (min * 60);
    System.out.print(min + " minute(s) and " + sec + " second(s)");
  }
}