package org.nipra.scratch;

import java.io.FileReader;

// http://www.java2s.com/Code/Java/File-Input-Output/UseaFileReadertodisplayatextfile.htm
public class DisplayFile {
	
	public static void main(String[] args) throws Exception {
		String inputFile = args[0];
		FileReader fr = new FileReader(inputFile);
		
		int ch;
		do {
			ch = fr.read();
			if (ch != -1) {
				System.out.println((char) ch);
			}
		} while (ch != -1);
		
		fr.close();
	}
}
