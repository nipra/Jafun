package je3.basics;

public class FizzBuzz {
	public static void main(String[] args) {
		for (int i = 1; i <= 100; i++) {
			if (((i % 5) == 0) && ((i % 7) == 0)) 
				System.out.print("fizzbuzz");
			else if ((i % 5) == 0)
				System.out.print("fizz");
			else if ((i % 7) == 0)
				System.out.print("buzz");
			else 
				System.out.print(i);
			System.out.print(" ");
		}
		System.out.println();
	}
}

// (nprabhak@unmac ~/Projects/Java/Jafun/java-examples/src)$ javac je3/basics/FizzBuzz.java 
// (nprabhak@unmac ~/Projects/Java/Jafun/java-examples/src)$ ls je3/basics/
// FizzBuzz.class FizzBuzz.java
// (nprabhak@unmac ~/Projects/Java/Jafun/java-examples/src)$ java je3.basics.FizzBuzz 10

// (nprabhak@unmac ~/Projects/Java/Jafun/java-examples)$ java -cp '/Users/nprabhak/Projects/Java/Jafun/java-examples/dist/java-examples.jar' je3.basics.FizzBuzz 10