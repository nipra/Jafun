/*
 * Copyright (c) 2004 David Flanagan.  All rights reserved.
 * This code is from the book Java Examples in a Nutshell, 3nd Edition.
 * It is provided AS-IS, WITHOUT ANY WARRANTY either expressed or implied.
 * You may study, use, and modify it for any non-commercial purpose,
 * including teaching and use in open-source projects.
 * You may distribute it non-commercially as long as you retain this notice.
 * For a commercial use license, or to purchase the book, 
 * please visit http://www.davidflanagan.com/javaexamples3.
 */
package je3.basics;

/**
 * This program prints out all its command-line arguments.
 **/
public class Echo {
    public static void main(String[] args) {
        int i = 0;                           // Initialize the loop variable
        while(i < args.length) {             // Loop until the end of array
            System.out.print(args[i] + " "); // Print each argument out
            i++;                             // Increment the loop variable
        }
        System.out.println();                // Terminate the line
    }
}
