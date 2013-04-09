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
import java.util.Vector;

/**
 * Purpose: Encapsulates all the stages of translating C++ to Java
 * @version 1.0
 * @author Ilya Tilevich
 */
class C2J 
{

	private String m_strHeaderFile; 
	private String m_strCppFile; 
	private String m_strTempFile; 
	private String m_strOutputFile; 
	private TextOutputStream m_tos;
	private FileOutputStream m_fosFileOut; 
	private Parser m_Parser;
	static  boolean m_bDefnOnly = false;
	/**
	 * Purpose: Constructor
	 * @param strHeaderFile - an *.h file
	 * @param strCppFile - a *.cpp file
	 * @exception FileNotFoundException if either file is not found
	 */
	public C2J(String strHeaderFile, String strCppFile){
		
		   m_strHeaderFile = new String(strHeaderFile); 
	       m_strCppFile = new String(strCppFile); 
		   m_strTempFile = new String("__temp__"); 
		   m_strOutputFile = ConstructOutputFileName(strCppFile); 
		   
		   
		   try{
				m_fosFileOut  = new FileOutputStream(m_strTempFile);
				m_tos = new TextOutputStream(m_fosFileOut);
		   }
		   catch(FileNotFoundException e){
				System.out.println("File Not Found" + e);
				return;
	       }
			
		   catch(Throwable e) {
				System.out.println("Error in opening file" + e);
				return;
		   }

		   m_Parser = new Parser(false); //create a parser to parse a header file
   
	}
	
	private String ConstructOutputFileName(String strCppFile){
	   	
		String strOutputFile = new String(strCppFile); 
	    
	   	//System.out.println(strOutputFile);
		int iIndex = strOutputFile.indexOf(".");

	   
	   if(iIndex == -1){
   		  System.out.println("Invalid name for a cpp file : " + strCppFile);
		  System.exit(1);
	   }

	   strOutputFile = strOutputFile.substring(0, iIndex) + ".java";
	   return strOutputFile;

	}
	
	/**
 	 * Purpose: Returns the output file name
	 * @return String
	 */
	
	public String GetOutputFileName(){ return m_strOutputFile; }
		
	/**
	 * Purpose: parses a header file containing class definitions
	 * Note: Should be called first
	 * @exception IOException
	 */
	public void ParseHeaderFile() throws IOException{
	
	    boolean bFirstClass = true;
	    m_Parser.SetParseMethod(false);
	    m_Parser.OpenInputFile(m_strHeaderFile);

	    while(m_Parser.readClass(m_bDefnOnly));		
		
	    for(ClassRepresentation c = m_Parser.class_info; c!=null; c = c.GetNext()){
			c.print(m_tos, bFirstClass);      
	   		if(bFirstClass) bFirstClass = false; 
	    }		
	}

	/**
	 * Purpose: parses a cpp file containig the implementation of the class defined in
	 * header file
	 * @exception IOException 
	 */
	public void ParseCppFile() throws IOException{

	   m_Parser.OpenInputFile(m_strCppFile);
	   m_Parser.SetParseMethod(true);

	   while(m_Parser.readClass(m_bDefnOnly));

	   //m_Parser.m_membersArray.WriteOut();
	   m_Parser.addGlobals();
	   
	   m_Parser.CloseAllFiles();
	   
	   m_tos.close();
	   m_fosFileOut.close();
	 
		   
	   		   	
	}

	/**
	 * Purpose: Calls the preprocessor
	 */
	public void WeaveItTogether(){
	    //m_Parser.m_membersArray.WriteOut();
		PrimitivePreprocessor prep = new PrimitivePreprocessor(new String(m_strTempFile), new String(m_strOutputFile), 
		m_Parser.m_membersArray);
	    prep.ProcessFile();
	}


	/**
	 * Purpose: Reads an output file and replaces found strings as specified
	 * Note: similar to sed utility
	 */
	public void MatchAndReplace() throws IOException{
		StringPair stPair1 = new StringPair("char * ","String ");
		StringPair stPair2 = new StringPair("char* ","String ");
		StringPair stPair3 = new StringPair("const char * " ,"String ");
		StringPair stPair4 = new StringPair(" & ", " ");
		StringPair stPair5 = new StringPair("*", " ");
		StringPair stPair6 = new StringPair("->", ".");
		StringPair stPair7 = new StringPair("unsigned int " ,"int ");
		StringPair stPair8 = new StringPair("unsigned long ","int ");		
		StringPair stPair9 = new StringPair("unsigned char ", "char ");
		StringPair stPair10 = new StringPair("unsigned ","int ");
		StringPair stPair11 = new StringPair("const " , "static final ");
		StringPair stPair12 = new StringPair("BOOL ", "boolean ");	
        StringPair stPair13 = new StringPair("NULL", "null");
		
			
		Vector v = new Vector();
		v.addElement(stPair1);
		v.addElement(stPair2);
		v.addElement(stPair3);
		v.addElement(stPair4);
		v.addElement(stPair5);
		v.addElement(stPair6);
		v.addElement(stPair7);
		v.addElement(stPair8);
		v.addElement(stPair9);
		v.addElement(stPair10);
		v.addElement(stPair11);
		v.addElement(stPair12);
		v.addElement(stPair13);

		Replacer rep = new Replacer(m_strOutputFile, v);
		rep.MatchAndReplace();
		
		(new File(m_strTempFile)).delete();
	}




public static void main(String[] args) throws IOException {

	String strHeaderFile = new String();
	String strCppFile = new String();
	
	
	if(args.length >= 2){
	   strHeaderFile = new String(args[0]);
	   strCppFile = new String(args[1]);
	
	   if(args.length == 3 &&  args[2].equalsIgnoreCase("-DefnOnly"))
	      C2J.m_bDefnOnly = true;
		
	}
	else{
		System.out.println("Usage: Java C2J  Source.h  Source.cpp [-DefnOnly]");
		System.exit(1);
	}
   
	
	
	C2J trans = new C2J(strHeaderFile,  strCppFile);
	trans.ParseHeaderFile();
	trans.ParseCppFile();
	trans.WeaveItTogether();
	trans.MatchAndReplace();

	
			
			

	System.out.println(trans.GetOutputFileName() + " is created");
	System.out.println("Enjoy!");
    
	
	//Runtime run = Runtime.getRuntime(); //works only under Windows NT
	//run.exec("write " + trans.GetOutputFileName());
	
}

}