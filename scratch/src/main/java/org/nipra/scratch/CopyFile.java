package org.nipra.scratch;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CopyFile {
	
	public static void main(String[] args) {
		try {
			String inputFile = args[0];
			String outFile = args[1];
			BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outFile));
			
			String inputLine;
			while ((inputLine = inputReader.readLine()) != null) {
				outputWriter.write(inputLine);
				outputWriter.newLine();
			}
			
			inputReader.close();
			outputWriter.close();
			System.out.println("Copy Complete!");
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
}
