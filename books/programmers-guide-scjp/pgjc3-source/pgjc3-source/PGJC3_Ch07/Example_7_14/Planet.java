interface HeavenlyBody { String describe(); }

class Star implements HeavenlyBody {
  String starName;
  public String describe() { return "star " + starName; }
}
class Planet {
  String name;
  Star orbiting;
  public String describe() {
    return "planet " + name + " orbiting " + orbiting.describe();
  }
}