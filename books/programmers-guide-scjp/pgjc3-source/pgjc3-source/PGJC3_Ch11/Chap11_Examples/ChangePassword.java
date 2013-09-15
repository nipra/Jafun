import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

/** Class to change the password of a user */
public class ChangePassword {

  // Map for storing login/password info.                            (1)
  private static Map<String, Integer> pwStore;

  public static void main (String[] args) throws IOException  {

    // Obtain the console:                                           (2)
    Console console = System.console();
    if (console == null) {
      System.err.println("No console available.");
      return;
    }

    // Read the login/password info from a file:                     (3)
    readPWStore();

    // Verify user:                                                  (4)
    String login;
    char[] oldPassword;
    do {
      login = console.readLine("Enter your login: ");
      oldPassword = console.readPassword("Enter your current password: ");
    } while (login.length() == 0 || oldPassword.length == 0 ||
             !verifyPassword(login, oldPassword));
    Arrays.fill(oldPassword, '0');

    // Changing the password:                                        (5)
    boolean noMatch = false;
    do {
      // Read the new password and its confirmation:
      char[] newPasswordSelected
             = console.readPassword("Enter your new password: ");
      char[] newPasswordConfirmed
             = console.readPassword("Confirm your new password: ");

      // Compare the supplied passwords:
      noMatch = newPasswordSelected.length == 0  ||
                newPasswordConfirmed.length == 0 ||
                !Arrays.equals(newPasswordSelected, newPasswordConfirmed);
      if (noMatch) {
        console.format("Passwords don't match. Please try again.%n");
      } else {
        changePassword(login, newPasswordSelected);
        console.format("Password changed for %s.%n", login);
      }
      // Zero-fill the password arrays:
      Arrays.fill(newPasswordSelected, '0');
      Arrays.fill(newPasswordConfirmed, '0');
    } while (noMatch);

    // Save the login/password info to a file:                       (6)
    writePWStore();
  }

  /** Verifies the password. */                                   // (7)
  private static boolean verifyPassword(String login, char[] password) {
    Integer suppliedPassword = String.copyValueOf(password).hashCode();
    Integer storedPassword = pwStore.get(login);
    return storedPassword != null && storedPassword.equals(suppliedPassword);
  }

  /** Changes the password for the user. */                       // (8)
  private static void changePassword(String login, char[] password) {
    Integer newPassword = String.copyValueOf(password).hashCode();
    pwStore.put(login, newPassword);
  }

  /** Reads login/password from a file */                         // (9)
  private static void readPWStore() throws IOException {
    pwStore = new TreeMap<String, Integer>();
    BufferedReader source = new BufferedReader(new FileReader("pws.txt"));
    while (true) {
      String txtLine = source.readLine();
      if (txtLine == null) break;             // EOF?
      Scanner scanner = new Scanner(txtLine);
      // Format: <login string> <password int hash value>
      String login = scanner.next();
      Integer password = scanner.nextInt();
      pwStore.put(login, password);
    }
    source.close();
  }

  /** Writes login/password to a file */                          // (10)
  private static void writePWStore() throws IOException {
    PrintWriter destination = new PrintWriter(new FileWriter("pws.txt"));
    Set<Map.Entry<String, Integer>> pwSet = pwStore.entrySet();
    for (Map.Entry<String, Integer> entry : pwSet) {
      // Format: <login string> <password int hash value>
      destination.printf("%s %s%n", entry.getKey(), entry.getValue());
    }
    destination.close();
  }
}