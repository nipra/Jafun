#include <iostream.h> 

#include "sample1.h"

sample1::sample1(int n) {
	num = n;
}

int
sample1::getNum() {
	return num;
}

int main() {
	sample1 obj(2112);
	cout << obj.getNum();
	return 0;
}
