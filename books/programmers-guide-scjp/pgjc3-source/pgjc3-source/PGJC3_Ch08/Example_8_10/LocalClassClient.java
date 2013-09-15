interface IDrawable {                   // (1)
  void draw();
}
//_____________________________________________________________________________
class Shape implements IDrawable {      // (2)
  public void draw() { System.out.println("Drawing a Shape."); }
}
//_____________________________________________________________________________
class Painter {                         // (3) Top-level Class
  public Shape createCircle(final double radius) { // (4) Non-static Method
    class Circle extends Shape {        // (5) Non-static local class
      public void draw() {
        System.out.println("Drawing a Circle of radius: " + radius);
      }
    }
    return new Circle();                // (6) Object of non-static local class
  }

  public static IDrawable createMap() { // (7) Static Method
    class Map implements IDrawable {    // (8) Static local class
      public void draw() { System.out.println("Drawing a Map."); }
    }
    return new Map();                   // (9) Object of static local class
  }
}
//_____________________________________________________________________________
public class LocalClassClient {
  public static void main(String[] args) {
    IDrawable[] drawables = {           // (10)
        new Painter().createCircle(5),  // (11) Object of non-static local class
        Painter.createMap(),            // (12) Object of static local class
        new Painter().createMap()       // (13) Object of static local class
    };
    for (int i = 0; i < drawables.length; i++)     // (14)
      drawables[i].draw();

    System.out.println("Local Class Names:");
    System.out.println(drawables[0].getClass());   // (15)
    System.out.println(drawables[1].getClass());   // (16)
  }
}