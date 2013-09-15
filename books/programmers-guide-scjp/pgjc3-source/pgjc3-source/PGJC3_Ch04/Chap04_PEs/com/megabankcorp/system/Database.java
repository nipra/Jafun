//Filename: Database.java
// Specify package
package com.megabankcorp.system;

//Refer to the Account class by using the simple name Account.
import com.megabankcorp.records.Account;

// Class must be abstract since it has abstract methods.
public abstract class Database {

  // Abstract and available from anywhere.
  public abstract void deposit(Account acc, double amount);

  // Abstract and available from anywhere.
  public abstract void withdraw(Account acc, double amount);

  // Abstract and only available from package and subclasses.
  protected abstract double amount(Account acc);

  // Unmodifiable and only available from package.
  final void transfer(Account from, Account to, double amount) {
    withdraw(from, amount);
    deposit(to, amount);
  }
}