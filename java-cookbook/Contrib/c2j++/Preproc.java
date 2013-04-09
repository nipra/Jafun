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
import java.util.StringTokenizer;

/**
* Purpose: Scan source file and substitute all instances of #include with
* function names
*/
class PrimitivePreprocessor{
    private FileInputStream fis = null;
    private DataInputStream dis = null;
    private FileOutputStream writeOut = null;
	private DataOutputStream dataWrite = null;
	private boolean m_bDebug = false;

	private MembersArray m_MembersVector;
    
	/**
	 * Purpose: Constructor
	 * @param strInFile - source file
	 * @param strOutFile - output files
	 * @param vec - vector of MemberBodies
	 * @exception IOException
	 */
	public PrimitivePreprocessor(String strInFile, String strOutFile, MembersArray vect){
            m_MembersVector = vect;
            
			try{

                fis = new FileInputStream(strInFile);
				writeOut = new FileOutputStream(strOutFile);

            }catch(FileNotFoundException eFnf){
		        System.out.println("Unable to open " + strInFile);
		        System.exit(1);
            }

			catch(Throwable e) {
				System.out.println("Error in opening file");
				return;
			}



              dis = new DataInputStream(fis);

    		  
		      dataWrite = new DataOutputStream(writeOut);
		
			  
		  	
	}

    /**
	 * Purpose: preprocesses the file
	 */
	
	public void ProcessFile(){
          int iBadData = 0;
          String strLine;
          String strTry;

		  try{
          
			  while((strLine = dis.readLine())!=null){
			  	  //System.out.println(strLine);
				  strTry = new String(strLine);
				  if(strLine.trim().startsWith("#include")){
					  ProcessInclude(strLine);
				  	  dataWrite.writeByte('\n');
				  }
				  else{
					  dataWrite.writeBytes(strLine);
				  	  dataWrite.writeByte('\n');
				  }
		      
				}
		  
		  
		  }catch(Exception e){
             System.out.println("Error occurred reading file");
             System.exit(1);
          }
          

		  try{
			  dis.close();
			  fis.close();
				  
			  dataWrite.close();
			  writeOut.close();
		  
		  }catch(IOException e){System.out.println(e);}	  	     
	}

	  private void ProcessInclude(String strLine){
		   
		  if(strLine.compareTo("") == 0) return;
		  String strMemName, strInclude;
		  
		  StringTokenizer strTok = new StringTokenizer(strLine);

		   if(strTok != null && strTok.countTokens() == 2){
					strInclude = new String(strTok.nextToken()); //who cares
					strMemName = new String(strTok.nextToken());
					if(!m_MembersVector.FindAndWriteOut(dataWrite, strMemName))
						if(m_bDebug)System.out.println(strMemName + " not found!");
 		   }
		   else
					System.out.println("Bad Line" + strTok);
					
		   
	}

	/*
    public static void main(String[] args) { 
	  MembersArray mArray = new MembersArray();

	  StringArray sArray1 = new StringArray();
	  StringArray sArray2 = new StringArray();
	  StringArray sArray3 = new StringArray();
	  
	  MemberBody memBody1;
	  MemberBody memBody2;
	  MemberBody memBody3;


	  String name1 = new String("function_1");
	  
	  String line11 = new String("{ 1 This is ");
	  String line12 = new String(" 1 a body of ");
	  String line13 = new String("1 function # 1 }");
	  
	  sArray1.addElement(line11); 
	  sArray1.addElement(line12); 
	  sArray1.addElement(line13); 

	  memBody1 = new MemberBody(name1, sArray1);
	  

	  String name2 = new String("function_2");
	  
	  String line21 = new String("{ 2 This is ");
	  String line22 = new String(" 2 a body of ");
	  String line23 = new String("2 function # 2 }");
	  
	  
	  sArray2.addElement(line21); 
	  sArray2.addElement(line22); 
	  sArray2.addElement(line23); 

	  memBody2 = new MemberBody(name2, sArray2);
	  
	  
	  String name3 = new String("function_3");
	  
	  String line31 = new String("{ 3 This is ");
	  String line32 = new String(" 3 a body of ");
	  String line33 = new String(" 3 function # 3 }");
	  
	  
	  sArray3.addElement(line31); 
	  sArray3.addElement(line32); 
	  sArray3.addElement(line33); 

	  memBody3 = new MemberBody(name3, sArray3);

	 mArray.addElement(memBody1); 
	 mArray.addElement(memBody2); 
 	 mArray.addElement(memBody3); 

	 PrimitivePreprocessor p =
	 new PrimitivePreprocessor("Prep.txt", "Prep.don", mArray);
	 p.ProcessFile();
	
	
	
	
	}

	 */

	  
}

      
