
/*
 * class Rectangle
 * 
 * This code has been generated using C2J++
 * C2J++ is based on Chris Laffra's C2J (laffra@ms.com)
 * Read general disclaimer distributed with C2J++ before using this code
 * For information about C2J++, send mail to Ilya_Tilevich@ibi.com
 */

class Rectangle
{
static final int FALSE = 0;
static final int TRUE = 1;
static final int MAYBE = 2;

/**
* Rectangle
* @param x_
* @param y_
* @param w_
* @param h_
*/
public
/* @c2j++: "Rectangle(int x_, int y_, unsigned w_, unsigned h_)" replacement: unsigned  to int  */
/* @c2j++: "Rectangle(int x_, int y_, int w_, unsigned h_)" replacement: unsigned  to int  */
Rectangle(int x_, int y_, int w_, int h_)
{
 x = x_;
	y = y_;
	w = w_;
	h = h_;
	}

/**
* move
* @param x_
* @param y_
*/
public
void move(int x_, int y_)
{
   //This is the first comment
   //This is the second comment
   x = x_;
   y = y_;
}

/**
* resize
* @param w_
* @param h_
*/
public
/* @c2j++: "void resize(unsigned w_, unsigned h_)" replacement: unsigned  to int  */
/* @c2j++: "void resize(int w_, unsigned h_)" replacement: unsigned  to int  */
void resize(int w_, int h_)
{
   w = w_;
   h = h_;
}

/**
* print
*/
public
void print()
{
/** @c2j++ Replacement from cout << "Rectangle " << " " << y <<		" " << w << " " << h << endl; System.out.println("Rectangle " + " " + String.valueOf(y) + " " + String.valueOf(w) + " " + String.valueOf(h));*/ 
 System.out.println("Rectangle " + " " + String.valueOf(y) + " " + String.valueOf(w) + " " + String.valueOf(h));
}
int x;
int y;
/* @c2j++: "unsigned w;" replacement: unsigned  to int  */
int w;
/* @c2j++: "unsigned h;" replacement: unsigned  to int  */
int h;

/**
*/
static void main()
{
/* @c2j++: "Rectangle * rect = new Rectangle(10,10,100,100);" replacement: * to " " */
  Rectangle   rect = new Rectangle(10,10,100,100);
/* @c2j++: "rect->print();" replacement: -> to . */
  rect.print();
}
}
