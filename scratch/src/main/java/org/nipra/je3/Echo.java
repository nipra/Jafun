package org.nipra.je3;

public class Echo {
	public static void main(String[] args) {
		int i = 0;
		while (i < args.length) {
			System.out.print(args[i] + " ");
			i++;
		}

		System.out.println();
	}
}
