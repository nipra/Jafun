
public class OperandEvaluationOrder {
  public static void main(String[] args) {
    // Evaluate: 4 + 5 * 6
    int i = operandEval(1, 4) + operandEval(2, 5) * operandEval(3, 6);  // (1)
    System.out.println();
    System.out.println("Value of i: " + i);
  }

  static int operandEval(int opNum, int operand) {                      // (2)
    System.out.print(opNum);
    return operand;
  }
}