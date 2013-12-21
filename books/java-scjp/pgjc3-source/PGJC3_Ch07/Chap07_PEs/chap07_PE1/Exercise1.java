//Filename: Exercise1.java
package chap07_PE1;

interface Function {
  public int evaluate(int arg);
}

class Half implements Function {
  public int evaluate(int arg) {
    return arg/2;
  }
}

public class Exercise1 {

  public static int[] applyFunctionToArray(int[] arrIn) {
    int length = arrIn.length;
    int[] arrOut = new int[length];
    Function func = new Half();
    for (int i = 0; i < length; i++)
      arrOut[i] = func.evaluate(arrIn[i]);
    return arrOut;
  }

  public static void main(String[] args) {
    // Create array with values 1..10
    int length = 10;
    int[] myArr = new int[length];
    for (int i = 0; i < length;) myArr[i] = ++i;
    // Print array
    for (int value : myArr) System.out.println(value);
    // Half values
    myArr = applyFunctionToArray(myArr);
    // Print array again
    for (int value : myArr) System.out.println(value);
  }
}