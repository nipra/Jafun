package org.nipra.je3;

// (nprabhak@nprabhak-mn ~/Projects/Java/Jafun/scratch)$ java -cp target/classes org.nipra.je3.FizzBuzz
public class FizzBuzz {
	public static void main(String[] args) {
		for (int i = 1; i <= 100; i++) {
			if (((i % 5) == 0) && ((i % 7) == 0)) {
				System.out.print("fizzbuzz");
			} else if ((i % 5) == 0) {
				System.out.print("fizz");
			} else if ((i % 7) == 0) {
				System.out.print("buzz");
			} else {
				System.out.print(i);
			}

			System.out.print(" ");
		}

		System.out.println();
	}
}
