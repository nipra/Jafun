
public class QuizGrader {

  /** Enum type to represent the result of answering a question. */
  enum Result { CORRECT, WRONG, UNANSWERED }

  public static final int PASS_MARK = 5;

  public static void main(String[] args) {

    String[] correctAnswers = { "C", "A", "B", "D", "B", "C", "C", "A" };

    System.out.println("Question  Submitted Ans. Correct Ans.  Result");

    // Counters for misc. statistics:
    int numOfCorrectAnswers = 0;
    int numOfWrongAnswers = 0;
    int numOfUnanswered = 0;

    // Loop through submitted answers and correct answers:
    for (int i = 0; i < args.length; i++) {
      String submittedAnswer = args[i];
      String correctAnswer = correctAnswers[i];
      Result result = determineResult(submittedAnswer, correctAnswer);

      // Print report for current question.
      System.out.printf("%5d%10s%15s%15s%n",
                         i+1, submittedAnswer, correctAnswer, result);
      // Accumulate statistics.
      switch(result) {
        case CORRECT: numOfCorrectAnswers++; break;
        case WRONG: numOfWrongAnswers++; break;
        case UNANSWERED: numOfUnanswered++; break;
      }
    }
    // Print summary of statistics.
    System.out.println("No. of correct answers:      " + numOfCorrectAnswers);
    System.out.println("No. of wrong answers:        " + numOfWrongAnswers);
    System.out.println("No. of questions unanswered: " + numOfUnanswered);
    System.out.println("The candidate " +
                    (numOfCorrectAnswers >= PASS_MARK ? "PASSED." : "FAILED."));
  }

  /** Determines the result of answer to a question. */
  public static Result determineResult(String submittedAnswer,
                                       String correctAnswer) {
    Result result = null;
    if (submittedAnswer.equals(correctAnswer))
      result = Result.CORRECT;
    else if (submittedAnswer.equals("X"))
      result = Result.UNANSWERED;
    else
      result = Result.WRONG;
    return result;
  }
}