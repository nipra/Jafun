interface IDrawable {
  void draw();
}

class Shape implements IDrawable {
  public void draw() { System.out.println("Drawing a Shape."); }
}

class Circle extends Shape {
  public void draw() { System.out.println("Drawing a Circle."); }
}

class Rectangle extends Shape {
  public void draw() { System.out.println("Drawing a Rectangle."); }
}

class Square extends Rectangle {
  public void draw() { System.out.println("Drawing a Square."); }
}

class Map implements IDrawable {
  public void draw() { System.out.println("Drawing a Map."); }
}

public class PolymorphRefs {
  public static void main(String[] args) {
    Shape[] shapes = {new Circle(), new Rectangle(), new Square()};     // (1)
    IDrawable[] drawables = {new Shape(), new Rectangle(), new Map()};  // (2)

    System.out.println("Draw shapes:");
    for (Shape shape : shapes)                                          // (3)
      shape.draw();

    System.out.println("Draw drawables:");
    for (IDrawable drawable : drawables)                                // (4)
      drawable.draw();
  }
}