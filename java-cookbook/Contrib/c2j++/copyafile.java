/*
 * Copyright (C) 1996,1997 Morgan Stanley & Co., Inc. 
 * Copyright (C) 1997 Ilya Tilevich
 *
 * This file is part of C2J++.
 * C2J++ is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by the Free Software Foundation; 
 * either version 2, or (at your option) any later version.
 * C2J++ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with C2J; 
 * see the file COPYING.  If not, write to
 * the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

import java.io.*;

/**
 * Purpose: copies one file into another 
 * @version 1.0 December 96
 * @author Ilya Tilevich
 */

 final class CopyAFile{
	private static boolean m_bDebug = false;
	private FileInputStream fileName = null;
	//Assigns the variable bufferedInput to the class
 	//BufferedInputStream
	private BufferedInputStream bufferedInput = null;
	private DataInputStream dataIn = null;
	
	private FileOutputStream writeOut = null;
	private DataOutputStream dataWrite = null;

	/**
	 * Purpose: Constructor
	 * @param inFile - input file name
	 * @param outFile - output file name
	 * @exception IOException if there are errors during fiels opening 
	 */

	public  CopyAFile(String inFile, String outFile){
		try{
	            fileName = new FileInputStream(inFile);
		    //Creates an instance of the class BufferedInputStream
		    //named bufferedInput
		   //bufferedInput receives the stream from the fileInputStream
		   //fileName ad it is read
		   bufferedInput = new BufferedInputStream(fileName);
		   dataIn = new DataInputStream(bufferedInput);
		   
		   writeOut = new FileOutputStream(outFile);
		   dataWrite = new DataOutputStream(writeOut);
			
		}
		catch(FileNotFoundException e){
		    System.out.println("File Not Found");
		    return;
	    }
		catch(Throwable e) {
	        System.out.println("Error in opening file");
	        return;
	    }


	}

	/**
	 * Purpose: Copies a file
	 * @exception IOException if error happens
	 */
	public void DoCopy(){

		int iCh;
		try{
	        
			while((iCh = dataIn.read())!=-1){
				dataWrite.writeByte(iCh);
				if(m_bDebug)
				   System.out.println(iCh);
							
			}
				
			fileName.close();
	        dataWrite.close();
	    }
						
		catch(IOException e) {
	          System.out.println("Error in reading file");
	    }
	
				
		
		catch(Throwable e){
			System.out.println("Error in writing to file");
		}
		
		
			
	
    }
   
 }