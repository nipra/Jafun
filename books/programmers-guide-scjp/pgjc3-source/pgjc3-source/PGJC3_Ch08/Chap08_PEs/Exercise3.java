//Filename: Exercise3.java
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

public class Exercise3 {
  /* Inner class that applies the function, prints the value, and
       returns the result. */
  static class PrintFunc extends Print {
    PrintFunc(Function f) {
      func = f;
    }
    Function func;
    public int evaluate(int arg) {
      return super.evaluate(func.evaluate(arg));
    }
  }

  // Inner class that just returns the argument unchanged.
  /* Use this when you want a PrintFunc object to print
       the argument as-is. */
  static class NoOpFunc implements Function {
    public int evaluate(int arg) {
      return arg;
    }
  }

  public static void main(String[] args) {
    // Create array with values 1 .. 10
    int[] myArr = new int[10];
    for (int i=0; i<10;) myArr[i] = ++i;
    // Print array without modification
    applyFunctionToArray(myArr, new PrintFunc(new NoOpFunc()));
    // Print halved values
    applyFunctionToArray(myArr, new PrintFunc(new Half()));
  }

  public static int[] applyFunctionToArray(int[] arrIn, Function func) {
    int length = arrIn.length;
    int[] arrOut = new int[length];
    for (int i=0; i< length; i++)
      arrOut[i] = func.evaluate(arrIn[i]);
    return arrOut;
  }
}