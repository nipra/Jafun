/* Try modifying this code to see how C2J++
goes about translating different C++ features into Java
Note1: When modifying the code, make sure it is 
syntactically correct

Note2: If you check "Design Only" box
C2J++ will generate a Java file which will consist of
class  definitions, instance variables,
and member method headers without implementation*/


//Rectangle.h


class Rectangle{
public:
  enum PBOOL {FALSE, TRUE, MAYBE};

  Rectangle(int x_, int y_, unsigned w_, unsigned h_):
	  x(x_), y(y_), w(w_), h(h_){}
  void move(int x_, int y_);
  void resize(unsigned w_, unsigned h_);
  void print();
protected:
  int x, y;
  unsigned w, h;
};