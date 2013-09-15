interface IDrawable {                           // (1)
  void draw();
}
//_____________________________________________________________________________
class Shape implements IDrawable {              // (2)
  public void draw() { System.out.println("Drawing a Shape."); }
}
//_____________________________________________________________________________
class Painter {                                 // (3) Top-level Class

  public Shape createShape() {                  // (4) Non-static Method
    return new Shape(){                         // (5) Extends superclass at (2)
      public void draw() { System.out.println("Drawing a new Shape."); }
    };
  }
  public static IDrawable createIDrawable() {   // (7) Static Method
    return new IDrawable(){                     // (8) Implements interface at (1)
      public void draw() {
        System.out.println("Drawing a new IDrawable.");
      }
    };
  }
}
//_____________________________________________________________________________
public class AnonClassClient {
  public static void main(String[] args) {      // (9)
    IDrawable[] drawables = {                   // (10)
        new Painter().createShape(),            // (11) non-static anonymous class
        Painter.createIDrawable(),              // (12) static anonymous class
        new Painter().createIDrawable()         // (13) static anonymous class
    };
    for (int i = 0; i < drawables.length; i++)  // (14)
      drawables[i].draw();

    System.out.println("Anonymous Class Names:");
    System.out.println(drawables[0].getClass());// (15)
    System.out.println(drawables[1].getClass());// (16)
  }
}