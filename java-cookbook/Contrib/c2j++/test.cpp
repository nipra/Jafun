/* Try modifying this code to see how C2J++
goes about translating different C++ features into Java
Note1: When modifying the code, make sure it is 
syntactically correct

Note2: If you check "Design Only" box
C2J++ will generate a Java file which will consist of
class  definitions, instance variables,
and member method headers without implementation*/


//Rectangle.cpp


#include <iostream.h>
#include "rectangle.h"

void Rectangle::move(int x_, int y_){
   //This is the first comment
   //This is the second comment
   x = x_;
   y = y_;
}

void Rectangle::resize(unsigned w_, unsigned h_) {
   w = w_;
   h = h_;
} 

void Rectangle::print(){
  cout << "Rectangle " << " " << y <<
		" " << w << " " << h << endl;
}

void main(){
  Rectangle * rect = new Rectangle(10,10,100,100);
  rect->print();
}
