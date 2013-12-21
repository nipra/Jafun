class Account {
  int balance;

  /** (1) Method makes a deposit into an account. */
  void deposit(final int amount) {

    /** (2) Local class to save the necessary data and to check
        that the transaction was valid. */
    class Auditor {

      /** (3) Stores the old balance. */
      private int balanceAtStartOfTransaction = balance;

      /** (4) Checks the postcondition. */
      void check() {
        assert balance - balanceAtStartOfTransaction == amount;
      }
    }

    Auditor auditor = new Auditor(); // (5) Save the data.
    balance += amount;               // (6) Do the transaction.
    auditor.check();                 // (7) Check the postcondition.
  }

  public static void main(String[] args) {
    Account ac = new Account();
    ac.deposit(250);
  }
}