package chap07_PE2;

//Filename: Exercise2.java
interface Function {
  public int evaluate(int arg);
}

class Half implements Function {
  public int evaluate(int arg) {
    return arg/2;
  }
}

class Print implements Function {
  public int evaluate(int arg) {
    System.out.println(arg);
    return arg;
  }
}

public class Exercise2 {

  public static int[] applyFunctionToArray(int[] arrIn, Function func) {
    int length = arrIn.length;
    int[] arrOut = new int[length];
    for (int i = 0; i < length; i++)
      arrOut[i] = func.evaluate(arrIn[i]);
    return arrOut;
  }

  public static void main(String[] args) {
    // Create array with values 1..10
    int length = 10;
    int[] myArr = new int[length];
    for (int i = 0; i < length;) myArr[i] = ++i;
    // Create a print function
    Function print = new Print();
    // Print array
    applyFunctionToArray(myArr, print);
    // Half values
    myArr = applyFunctionToArray(myArr, new Half());
    // Print array again
    applyFunctionToArray(myArr, print);
  }
}