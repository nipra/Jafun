
public class Light {
  // Properties:
  private int     noOfWatts;       // wattage
  private String  location;        // placement
  private boolean indicator;       // on or off

  // Setters
  public void setNoOfWatts(int noOfWatts)     { this.noOfWatts = noOfWatts; }
  public void setLocation(String location)    { this.location = location; }
  public void setIndicator(boolean indicator) { this.indicator = indicator; }

  // Getters
  public int     getNoOfWatts() { return noOfWatts; }
  public String  getLocation()  { return location; }
  public boolean isIndicator()  { return indicator; }
}