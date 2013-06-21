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
import java.util.Enumeration;
import java.lang.Character;


class Buffer extends Vector
{
	Buffer(int iCapacity){
		super(iCapacity);
	}
	
	public void Add(int iByte){
		addElement(new Character((char)iByte));
	}

	public void Add(char c){
		addElement(new Character(c));
	}
	
	public char At(int iLoc){
		
				
		try{
		    			
			return ((Character) elementAt(iLoc)).charValue();
		 }
		 
		 catch (Throwable e) {System.out.println(e);}
		
			return '$';
	}	

	void Concatenate(Buffer bufOther){
		int iSize = bufOther.size();
		for(int i = 0; i < iSize; i ++)
			addElement(new Character(bufOther.At(i)) );

	}	

	void Concatenate(String strBuf){
		int iLen = strBuf.length();
		for(int i = 0; i < iLen; i++)
		    Add(strBuf.charAt(i));
	}

	boolean StartsWithComment(){
		int iSize = size(), i = 0;
		char ch;
		if(iSize == 0) return false;
		
		do{
			ch = At(i++);
		}while(i < iSize && Character.isSpace(ch));

		if(ch == '/'){
			for(int j = i; j < i+2; j++)
				if(At(j)!='*')
				  return false;
			return true;
		}

		else return false;
	}

	public 	synchronized void WriteOut(){  //to stdio
		
		Enumeration enum = elements();
		
		for(;enum.hasMoreElements();){
			 Character Char = (Character)(enum.nextElement());
			 System.out.print(Char.charValue());
        }
	 
	}

	/**
	 * Purpose: Writes this object to DataOutputStream
	 */
	public 	synchronized void WriteOut(DataOutputStream  dos){
		
		int iSize = size();
		
		
		try{
		
			for(int i = 0; i < iSize; i ++){
				
				char ch = At(i);
				dos.writeByte(At(i));
				
			}
			
		} catch(IOException e){System.out.println("IO error in Buffer WriteOut(dos)");}

	   	 
	}
	   	


}


/**
 * Class Scan
 * Purpose: Scans a source files ignoring white space characters
 * @version 1.1	 January 97
 * @author Ilya Tilevich
 * Note: This class is based on the original code written in C++ by Chris Laffra
 */
class Scanner
{
    private boolean  m_bPutBack = false;		// see getByte()
	private byte m_btPutbackByte = 0;		// see getByte()
	private int  m_iLineNo  = 0;		// the current line number
	protected TokenStack m_tkStack = null;
	 
	protected FileOutputStream m_fosFileOut = null; 
	protected DataOutputStream m_dosDataOut = null;	
	
	private Buffer m_Buffer;

	
	public Buffer GetBuffer() { return m_Buffer; }
	
	protected FileInputStream m_fisFileIn = null;  //source file
	protected BufferedInputStream m_bisBufferedInput = null;
	private DataInputStream m_disDataIn = null;

	/**
	 * m_bEOF indicates if the end of file is reached
	 */
	public boolean m_bEOF = false;

	/**
	 * Scan
	 * Purpose: Constructor
	 * @exception IOException if there are problems with opening a temp file   
	 */
	
	public Scanner(){
	
		m_bPutBack = false;		// see getByte()
	    m_btPutbackByte = 0;		// see getByte()
	    m_iLineNo  = 0;		// the current line number
	    m_tkStack = new TokenStack();
	 	
		try{
			m_fosFileOut = new  FileOutputStream("__temp__");
			m_dosDataOut = new DataOutputStream(m_fosFileOut);	
		}catch(IOException e){};

			
	}

	/**
	 * OpenInputFile
	 * Purpose: opens a source file for scanning and reinitializes member variables
	 * @param strInFile		the name for the source file
	 * @exception FileNotFoundException		if strInFile doesn't exist
	 * @exception Throwable		if there are problems with opening the file
	 */
	 	
	public void OpenInputFile(String strInFile){
	
	   try{
	       
		   m_fisFileIn = new FileInputStream(strInFile);
		   m_bisBufferedInput = new BufferedInputStream(m_fisFileIn);
		   m_disDataIn  = new DataInputStream(m_bisBufferedInput);

		   
				
	   }
		catch(FileNotFoundException e){
		    System.out.println("File Not Found");
		    return;
	    }
		catch(Throwable e) {
	        System.out.println("Error in opening file");
	        return;
	    }

		

		//When open a new file for reading, reset all member variables;
		m_tkStack = new TokenStack();
		m_Buffer = new Buffer(1024);

		m_bPutBack = false;		
	    m_btPutbackByte = 0;		
	    m_iLineNo  = 0;		
		m_bEOF = false;
		

	}

	public void CloseAllFiles(){
		   
		try{	
		   m_bisBufferedInput.close();   
		   m_disDataIn.close();   
		   m_fisFileIn.close();
		   
		   
	   
		   m_dosDataOut.close();
		   m_fosFileOut.close();
		}catch(IOException e){System.out.println(e);}
	}
	
	public void FlushBuffer(){
		m_Buffer.setSize(0);
	}
	
	
	public void WriteBuffer(){
		int iSize = m_Buffer.size();
		try{

		for(int i = 0; i < iSize; i ++){
			m_dosDataOut.writeByte(m_Buffer.At(i));
			///System.out.print(m_Buffer.At(i));
		}
		
		}catch(IOException e){System.out.println("Error in WriteBuffer()");}
		FlushBuffer();	
	}
	
	public boolean BufferStartsWithComment(){
		return m_Buffer.StartsWithComment();
	}

	/**
	 * getByte()
	 * Purpose: gets a byte from the input stream or a m_btPutbackByte
	 * Ignores '\r' 
	 * @return byte read
	 * @exception IOException if  there are problems with reading the file
	 * @exception Throwable		if there are problems writing to the temp file
	 */
	
	private byte getByte()
	{
	  if(m_bPutBack){
		  m_bPutBack = false;
		  return m_btPutbackByte;
	  }

	  int iCh = 0;
	  
	  try{
		  
		  if((iCh = m_disDataIn.read())!=-1){
			  //char ch = (char) iCh;
			  if(iCh == '\r'){ 
				  m_Buffer.Add(iCh);
				  return getByte();
			  }
			  if(iCh == '\n')  m_iLineNo++;

		  }

		  else
			  m_bEOF = true;
		  

		    //if(dosDataOut != null && iCh!= -1){
			//dosDataOut.writeByte(iCh);
			 
			// if(iCh == '\n') dosDataOut.writeBytes("  ");
		    //}

	  }
	  catch(IOException e) {
	          System.out.println("Error in reading file");
	  }


	  catch(Throwable e){
			System.out.println("Error in writing to file");
	  }
	
	    if(!m_bEOF)
			m_Buffer.Add(iCh);
	    return  (byte)iCh;
	  
	
	}

	/**
	 * ungetByte
	 * Purpose: put back a byte just read by getByte() method
	 * This method is used in this class and is called from Parse
	 * @param b the byte to put back	
	 */
	protected void ungetByte(byte b)
	{
		m_bPutBack = true;
		m_btPutbackByte = b;
	}
	
	/**
	 * isIdentifier
	 * Purpose: determines if the input param is a part of a legal C identifier
	 * @param b the byte to examine
	 * @return true if b can be a part of a C identifier, false otherwise
	 */
	
	public static boolean isIdentifier(byte b) { return Character.isLetterOrDigit((char)b) || b == '_'; }
	
	public  static  boolean isOperator(byte b){
		
		return (b == '!'||
				b == '^'||
				b == '&'||
				b == '*'||
				b == '-'||
				b == '+'||
				b == '='||
				b == '|'||
				b == '<'||
				b == '>'||
				b == '~');
	}
	
	
	/**
	 * skipSpaces
	 * Purpose: reads the source file until it encounters a byte other than space
	 */
	
	private void skipSpaces()
	{
		byte b;
		do{ 
			b = getByte(); 
			}while (b == ' '|| b == 10 || b == 9);
		ungetByte(b);
	}
	
	/**
	 * ByteArrayToString 
	 * Purpose: converts an array of bytes to a Java String
	 * @param	btArray[]	the array of bytes
	 * @param	iSize	the size of the btArray[]
	 */
	
	protected String ByteArrayToString(byte btArray[], int iSize)
	{
				String str = new String();
				for(int i = 0; i< iSize; ++i)
					str += (char)btArray[i];

		  		 return str;
	}

	/**
	 * getWord
	 * Purpose: Gets a word from the input stream and pushes it 
	 * into the ParseStack
	 * @param strBuf[]	a temporary array of bytes
	 * @param iBufSize	a maximum size of the word to be gotten
	 * @return the word read
	 */
	
	public String getWord(byte strBuf[], int iBufSize)
	{

	   skipSpaces();
	   int iN;
	   for(iN = 0; iN < iBufSize && !m_bEOF; ++iN){
		   if(iN < iBufSize)  strBuf[iN] = getByte();

		   if(strBuf[iN] == '/'){        //skip comments

			   if(iN < iBufSize-1) strBuf[++iN] = getByte();

			if(strBuf[iN] == '/') { 		  //the '//' form
				 do{
					 if(iN < iBufSize) strBuf[++iN] = getByte();
				 }while(iN < iBufSize && !m_bEOF && strBuf[iN]!='\n');

				 //try{
				//	WriteBuffer(); //don't throw away comments
				 //}catch(IOException e){}
				 
				 return getWord(strBuf, iBufSize);
			}
			  else if(strBuf[iN] == '*'){           //the '/*' form
				  boolean bWatchForSlash = false;
				  while(iN < iBufSize && !m_bEOF){
					 do{
						 if(iN < iBufSize) strBuf[iN] = getByte();
						 if(bWatchForSlash && strBuf[iN] == '/') break;
						 else bWatchForSlash = false;
					 }while(iN < iBufSize && !m_bEOF && strBuf[iN] != '*');
					 if(iN < iBufSize && strBuf[iN] != '/' && !m_bEOF) strBuf[iN] = getByte();
					 if(strBuf[iN] == '/'){
						 bWatchForSlash = false;
						 break;
					 }
					 else if(strBuf[iN] == '*') bWatchForSlash = true;					 
				  }
				 
				 //try{
				 //	WriteBuffer(); //don't throw away comments
				 //}catch (IOException e) {}
				 return getWord(strBuf, iBufSize);
			  }
			  else {
				  if(iN > 0) ungetByte(strBuf[iN--]);
			  }
		   }

			if(strBuf[iN] == '#'){           //skip preprocessor
			   byte b;
			   do{
				   b = getByte();
				   if(b == '\\') { b = getByte(); b = getByte(); }
			   }while(!m_bEOF && b != '\n');
			   //try{
			   //WriteBuffer(); //don't throw away preprocessor
			   //}catch (IOException e){}
			   return getWord(strBuf, iBufSize);
			}

			if(strBuf[iN] == '\'') {         //character constant
				do{
					if(iN < iBufSize) strBuf[++iN] = getByte();
					if(strBuf[iN] == '\\'){
						if(iN < iBufSize) strBuf[++iN] = getByte();
						if(iN < iBufSize) strBuf[++iN] = getByte();
					}
				}while(iN < iBufSize && m_bEOF && strBuf[iN]!='\'');

                return ByteArrayToString(strBuf, iN + 1);

	        }

	        if(strBuf[iN] == '"'){              //string constant
	            do{
	                if(iN < iBufSize-1) strBuf[++iN] = getByte();
	                if(strBuf[iN] == '\\'){
	                    if(iN < iBufSize-1) strBuf[++iN] = getByte();
	                    if(iN < iBufSize-1) strBuf[++iN] = getByte();
					}
				}while(iN < iBufSize && !m_bEOF &&  strBuf[iN] !='"');

	            return ByteArrayToString(strBuf, iN+1);
	        }

	        
							 
			if(!isIdentifier(strBuf[iN])) break;
	} //end of for


	if(iN > 0) ungetByte(strBuf[iN--]);

	String str = ByteArrayToString(strBuf, iN+1);
	m_tkStack.push(str);
	if(m_bEOF) return (str = "");
	return str;

}

public static void main(String[] args) throws IOException {
Scanner s = new Scanner();
s.OpenInputFile("t.1");

while(!s.m_bEOF)
    byte b = s.getByte();

   s.WriteBuffer();
}



}

/**
 * class Parser
 * Purpose: parse a C++ source file(s)
 * @version 1.0 December 96
 * @author Ilya Tilevich
 *  Note: This class is based on the original code written in C++ by Chris Laffra
 */
 
class Parser
{
    
		
	static final int m_iBufSize = 516;
	
	static final boolean  m_bDebug = false;

	static final String PUBLIC = "public ";
	static final String PRIVATE = "private ";
	static final String PROTECTED = "protected";
	String m_strAccessor = "protected";
	MembersArray m_membersArray; //array of all member functions
	Scanner m_scan;
	private boolean m_bParseMethods; //if true parse Cpp file, H file otherwise
	FileOutputStream m_fosFileOut; 

	static TextOutputStream m_tos; //the output file
	//protected StringArray m_linesGlobal;	//contains all global C functions
	protected Buffer m_linesGlobal;	//contains all global C functions
	
	boolean m_bDefnOnly; //don't write function bodies
	/**
	 * Parser
	 * Purpose: Constructor
	 * @param bCppFile	specifies which parsing method to choose
	 * @exception	if temp file cannot be open
	 */
	
	public Parser(boolean bCppFile){
		  
		 m_bParseMethods = bCppFile; //if true be ready to parse C++ file
		  
		  m_membersArray = new MembersArray();
	      
		  m_scan = new Scanner();
		  
		  m_linesGlobal = new Buffer(1024);
		  
		  m_bCommaSeen = false;
        
		  m_bInsideEnum = false;

	 }

	/**
	 * SetParserMethod
	 * Purpose: unstructs the parser to parse a header or an implementation file
	 * @param bCpp	If false - parser a header, it true parse a cpp file
	 */
	
	public void SetParseMethod(boolean bCpp){
		m_bParseMethods = bCpp;
			
	}
	
	/**
	 * OpenOutputFile
	 * Purpose: opens the output file for input and resets all parse member 
	 * variables
	 * @param	strInFile the source file name
	 */
	
	public void OpenInputFile(String strInFile){
		
		String m_strAccessor = "protected";

	    ClassRepresentation cpCache = null;		   
        
		strCacheClassName = new String();	
        
		class_info = null;
		
		m_strInitializer = null;
		
		m_bCommaSeen = false;
        
		m_bInsideEnum = false;
		
		strReadName = ""; 
        
		strReadType = ""; 
        
		paramList = null;
	 	
		m_strAccessor = new String();
		
		strCacheClassName = new String(); 
		
		class_info = null;
		
		m_bCommaSeen = false;
        
		m_bInsideEnum = false;


		m_scan.OpenInputFile(strInFile);
	
	}	
	
	public void CloseAllFiles(){
		m_scan.CloseAllFiles();
	}
	
	/**
	 * isCKeyword
	 * Purpose: checks out if the input parameter is a valid C keyword
	 * @param str  a word to check out
	 * @return true is str is a valid C keyword, false otherwise
	 */
	
	public static boolean isCKeyword(String str){
		if(str.compareTo("break") == 0 || 
		   str.compareTo("case") == 0 || 
		   str.compareTo("char") == 0 || 
		   str.compareTo("const") == 0 || 
		   str.compareTo("continue") == 0 || 
		   str.compareTo("default") == 0 || 
		   str.compareTo("delete") == 0 ||
		   str.compareTo("do") == 0 || 
		   str.compareTo("double") == 0 || 
		   str.compareTo("else") == 0 || 
		   str.compareTo("enum") == 0 || 
		   str.compareTo("extern") == 0 || 
		   str.compareTo("float") == 0 || 
		   str.compareTo("for") == 0 || 
		   str.compareTo("goto") == 0 || 
		   str.compareTo("if") == 0 || 
		   str.compareTo("int") == 0 || 
		   str.compareTo("long") == 0 || 
		   str.compareTo("new") == 0 ||
		   str.compareTo("register") == 0 || 
		   str.compareTo("return") == 0 || 
		   str.compareTo("short") == 0 || 
		   str.compareTo("signed") == 0 || 
		   str.compareTo("sizeof") == 0 || 
		   str.compareTo("static") == 0 || 
		   str.compareTo("struct") == 0 || 
		   str.compareTo("switch") == 0 || 
		   str.compareTo("typedef") == 0 || 
		   str.compareTo("union") == 0 || 
		   str.compareTo("unsigned") == 0 || 
		   str.compareTo("void") == 0 || 
		   str.compareTo("volatile") == 0 || 
		   str.compareTo("while") == 0)
		   		   return true;
		return false;
	}
		   

/**
 * isConstCharOrString
 * Purpose: finds out if the input parameter is a constant char of form 'x' or
 * a quoted string
 * @param str a word to check out
 * @return true if str is a const char or str, false otherwise
 */

protected boolean isConstCharOrString(String str){
	int iSize = str.length();
	return ((str.charAt(0) == '"'	&& str.charAt(iSize-1) == '"') || 
		    (str.charAt(0) == '\'' &&  str.charAt(iSize-1) ==  '\''));
}


/**
 * skipWord
 * Purpose: reads a word from the input stream
 * @return the word read right after the skipped one
 */

private String skipWord()
{
	byte btArray[] = new byte[m_iBufSize];
	return m_scan.getWord(btArray, m_iBufSize);
}

/**
 * findWord
 * Purpose: fins a word in the source file
 * @param strToFind		word to look for
 * @return true if the word was found, false if the end of file was reached
 */

private boolean findWord(String strToFind)
{
	String str;
	if(m_bDebug) System.out.println("strToFind is " + strToFind);
	byte btArray[] = new byte[m_iBufSize];
	do{
		str = m_scan.getWord(btArray, m_iBufSize);
		if(str.compareTo("") == 0) return false;
	}while(str.compareTo(strToFind) != 0);

	return true;
}

/**
 * findAndDoNotSkipWord
 * Purpose: searches for a word in the  source file until either 
 * the word to search is found or strDontSkip is encountered
 * @param	strWordToFind	the search word
 * @param	strDontSkip		the word not to be skipped
 * @return	true if the word was found, false if the strDontSkip
 * was encountered or end of file was reached
 */

boolean findAndDoNotSkipWord(String strWordToFind, String strDontSkip)
{
	if(m_bDebug) System.out.println("findAndDontSkipWord " + strWordToFind + " ," +
		strDontSkip);
	byte btArray[] = new byte[m_iBufSize];
	String str;
	do{
		
		str = m_scan.getWord(btArray, m_iBufSize);
		if(str.compareTo("") == 0) return false;
		if(m_bDebug) System.out.println("m_scan.getWord() -> \"" + str);
		if(str.compareTo(strDontSkip) == 0) return false;
	
	}while(str.compareTo(strWordToFind) != 0);

	return true;
}

/**
 * skipMatching
 * Purpose: read the function body between strWordToSkip and strWordtoFind
 * putting the words read into strArray.
 * ; character serves as a signal to start entering a new element into strArray
 * @param strWordToSkip		the word to be skipped
 * @param strWordToFind		the word to find
 * @param strArray	array  to hold the function's body
 * @return false if the end of the file was reached, true otherwise
 */


boolean skipMatching(String strWordToSkip, String strWordToFind, StringArray strArray)
{
  
	
	if(m_bDebug) System.out.println("skipMatching( " + strWordToSkip + " , "
	                   + strWordToFind);
   byte btArray[] = new byte[m_iBufSize];
   String str;
   boolean bNewLine = true;
   do{
	   str = m_scan.getWord(btArray, m_iBufSize);
	   if(str.compareTo("") == 0) return false;
	   if(str.compareTo("for") == 0){ //take care of for loop
	      
		  strArray.addElement(str);
		  str = m_scan.getWord(btArray, m_iBufSize);
	   	  if(str.compareTo("") == 0) return false;
		  else if(str.compareTo("(") == 0){
			  strArray.appendToLastString(str);
			  while(str.compareTo(")")!= 0 && str.compareTo("") !=0){
	   			 str = m_scan.getWord(btArray, m_iBufSize);
		      	 if(isCKeyword(str))
		             str += " ";
	   	  		 strArray.appendToLastString(str);
			  }	//while
	   	  bNewLine = true;
		  }//else if
	   	  continue;
	   }
		if(str.compareTo("}") == 0 || str.compareTo("{") == 0)
		   bNewLine = true;
	   else if(isCKeyword(str))
		   str += " ";
	   if(str.compareTo(";") == 0 || str.compareTo("';") == 0){
		   bNewLine = true;
	   	   strArray.appendToLastString(str);
	   }
	   if(!bNewLine)
		   strArray.appendToLastString(str);
	   else if(bNewLine && str.compareTo(";")!=0 && str.compareTo("';")!=0  ){
	       if(str.compareTo("{") != 0  &&  str.compareTo("}") != 0)
				strArray.addElement(" " + str);
	   	   else 
			   strArray.addElement(str);
		   bNewLine = false;
	   }
	   
	   if(str.endsWith("}") || str.endsWith("{"))
		   bNewLine = true;
	   if(m_bDebug) System.out.println("    " + str);
	   if(str.compareTo(strWordToSkip) == 0)
		   skipMatching(strWordToSkip, strWordToFind, strArray);
   }while(str.compareTo(strWordToFind) !=0 );

   return true;
}

boolean skipMatching(String strWordToSkip, String strWordToFind)
{
   	
   if(m_bDebug) System.out.println("skipMatching( " + strWordToSkip + " , "
	                   + strWordToFind);
   byte btArray[] = new byte[m_iBufSize];
   
   boolean bNewLine = true;
   
   String str;
   do{
	   str = m_scan.getWord(btArray, m_iBufSize);
	   if( str.compareTo("")==0 ) 
		   return false;
	   else if(str.compareTo(strWordToSkip) == 0)
		   skipMatching(strWordToSkip, strWordToFind);
   
   }while(str.compareTo(strWordToFind) !=0 );

   
   return true;
}




private ClassRepresentation cpCache = null;		   
private String strCacheClassName;	
protected ClassRepresentation class_info = null;

/**
 * findClass
 * Purpose: determines if class with a given name already has a ClassRepresentation
 * uses strCacheClassName to hold a name of the last found class
 * @param strClassName  the class name to find
 * @param bIsSuper	specifies if the class to find is a super class
 * @return	ClassRepresentation of the found class
 *
 */
 

ClassRepresentation findClass(String strClassName, boolean bIsSuper)
{
	
	ClassRepresentation c, d;
	if(cpCache!=null && strClassName.compareTo(strCacheClassName) == 0) return cpCache;
	for(c = class_info; c!=null;c = c.GetNext())
		if(c.GetName().compareTo(strClassName) == 0){
			cpCache = c;
			strCacheClassName = strClassName;
			return c;
		}
		/*c = new ClassRepresentation(strClassName, class_info, bIsSuper);
		class_info = c;
		*/
	c = new ClassRepresentation(strClassName, null, bIsSuper);
	
	if(class_info == null) 
			class_info = c;
	else {
		for(d = class_info; d.GetNext() != null; d = d.GetNext());
		d.SetNext(c); //add to the head of the list
	}		
	
	return c;
}


/**
 * setSuperClass
 * Purpose: sets a super class for a class named strClassName
 * @param	strClass the class name to have a super class set for
 * @param	strSuperClass	the name of the super class
 *
 */
 

void setSuperClass(String strClassName, String strSuperClassName)
{
	if(strSuperClassName.compareTo(",") == 0) return;
	if(m_bDebug) System.out.println("setSuperClass( " + strClassName + " , " +
									strSuperClassName);
	ClassRepresentation c1 = findClass(strClassName, false);
	ClassRepresentation c2 = findClass(strSuperClassName, true);
	c1.addSuperClass(c2);
}

/**
 * readSuperClasses
 * Purpose: determines if the class named strClassName is a child class
 * @param	strClassName	the class to be investigated
 * @return true if class is followed by a valid class definition,
 * false otherwise
 *
 */

boolean readSuperClasses(String strClassName)
{
	if(m_bDebug) System.out.println("readSuperClasses( " + strClassName);
	byte btArray[] = new byte[m_iBufSize];
	String str;
	while(true){
		str = m_scan.getWord(btArray, m_iBufSize);
		if(str.compareTo("") == 0) return false;
		if(m_bDebug) System.out.println("m_scan.getWord() -> " + str);
		if(str.compareTo(":") == 0) continue;
		if(str.compareTo("public") == 0) continue;
		if(str.compareTo("private") == 0) continue;
		if(str.compareTo("protected") == 0) continue;
		if(str.compareTo("{") == 0) break;
		if(str.compareTo(";") == 0) return false;
		else setSuperClass(strClassName, str);
	}

	return true;

}  

/**
 * readGlobalBody
 * Purpose: read a global function and store it's body in m_linesGlobal array
 * @param strMemberName		the function name
 * @param str	the function return type
 * @param parameters	the function's parameters list
 *
 */
void readGlobalBody(String strMemberName, String strType,  ParmList parameters){
						
 	m_linesGlobal.Concatenate("\r\n/**\r\n");

	if(parameters != null)
		parameters.printAsParms(m_linesGlobal);

	if(strType.compareTo("void") != 0 &&
		strType.compareTo(":") != 0 &&
	   strType.compareTo(";") != 0 &&
	   strType.compareTo("}") != 0)
	       m_linesGlobal .Concatenate("* @return " + strType + "\r\n");
	m_linesGlobal.Concatenate("*/\r\n");
	
	
	
	if(strType.compareTo(":") !=0 && strType.compareTo(";") != 0 && strType.compareTo("}") !=0 ){
		  
		m_linesGlobal.Concatenate("static " + strType + " ");
	}
		
		
	if(parameters != null){ 
			m_linesGlobal.Concatenate(strMemberName + "(");
			parameters.printNames(m_linesGlobal);
			m_linesGlobal.Concatenate(")\r\n{");
	}
		
	else
		   m_linesGlobal.Concatenate(strMemberName + "()\r\n{");
			
		
	
	//m_strInitializer = "";
	
	
	m_scan.FlushBuffer(); //clear the buffer 
	skipMatching("{", "}");
	
	if(!m_bDefnOnly)
		m_linesGlobal.Concatenate(m_scan.GetBuffer());
    else
		m_linesGlobal.Concatenate("\n}");
	
	
}	
/**
 * addGlobals
 * Purpose: adds the array of global functions and variables to the m_membersArray
 * Note: this function should be called when the parsing if over
 *
 */


public void addGlobals(){

	MemberBody memBodies = new MemberBody("__globals",  m_linesGlobal); //the actual body
	m_membersArray.addElement(memBodies); 
}	
//////////////
private String m_strInitializer; //constructor initialize list
/**
 * readMemberBody
 * Purpose: reads the class method's body; generates comments and adds both
 * the comments and the body to m_membersArray 
 * @param strClassName		the function's class name
 * @param strMemberName		the function's name
 * @strType		the function's return type
 * @parameters	the function's parameters list
 */

void readMemberBody(String strClassName, String strMemberName, String strType,
					ParmList parameters){
						
 	Buffer lines1 = new Buffer(1024);
	Buffer lines2;

	MemberBody memBody1, memBody2;
	String strMemName = new String(); //a unique name which represents a functions
	//in m_membersArray;
	strMemName = strClassName;
	strMemName += "__";
	strMemName += strMemberName;
	if(parameters != null)
		strMemName = parameters.printTypes(strMemName);

	if(parameters != null)
		parameters.printAsParms(lines1);

	if(strType.compareTo("void") != 0 &&
		strType.compareTo(":") != 0 &&
	   strType.compareTo(";") != 0 &&
	   strType.compareTo("}") != 0 &&
	   strClassName.compareTo(strMemberName) != 0)
	   	    lines1.Concatenate("* @return " + strType + "\r\n");
	lines1.Concatenate("*/");
	memBody1 = new MemberBody(strMemName, lines1); //contains only comments
	
	
	lines2 = new Buffer(1024);

	strMemName += "__body";

	if(strType.compareTo(":") !=0 && strType.compareTo(";") != 0 && strType.compareTo("}") !=0 ){
		if(strClassName.compareTo(strMemberName) != 0)
		    lines2.Concatenate(strType + " ");
	 	else  
			lines2.Concatenate(strType);
	}
		
	if(strMemberName.charAt(0) == '~')  //destructor
		lines2.Concatenate("void finalize() {");
		
	else{
		
		if(parameters != null){ 
			lines2.Concatenate(strMemberName + "(");
			parameters.printNames(lines2);
			lines2.Concatenate(")\r\n{");
		}
		
		else
			lines2.Concatenate(strMemberName + "()\r\n{");
			
		
		
	}
	
	if(m_strInitializer != null)
		lines2.Concatenate(m_strInitializer);
	m_strInitializer = "";
	
	m_scan.FlushBuffer(); //clear the buffer 
	skipMatching("{", "}");
	
	if(!m_bDefnOnly)
		lines2.Concatenate(m_scan.GetBuffer());
    else
		lines2.Concatenate("\n}");
	///concat to lines2
	memBody2 = new MemberBody(strMemName, lines2); //the actual body
	

	m_membersArray.addElement(memBody1); 
	m_membersArray.addElement(memBody2);

}	
 
private boolean m_bCommaSeen = false;
private boolean m_bInsideEnum = false;
private int		m_iEnumValue = 0;
/**
 * readMember
 * Purpose: reads a class member either a variable or a method
 * @param strClassName  the class name
 * @param type	type of a member (constructor, variable ...)
 * @return false if the end of class defintio of the end of file was reached
 * true otherwise
 *
 */


boolean readMember(String strClassName, Type type) {
	
	byte btArray[] = new byte[m_iBufSize];
	
	if(m_bDebug) System.out.println(" readMember(" + strClassName + " " + strReadName + " " + strReadType);
	
	String strSeparator = new String();
	
	boolean bIsStatic = false;
	
	strReadName = "";
	
	if(m_bCommaSeen){
		if(m_bDebug) System.out.println("comma seen, continue with type " + strReadType);
	}
	else{
		if((strReadType = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
		if(m_bDebug) System.out.println("first word " + strReadType);
	}

	if(strReadType.compareTo("public") == 0){
		m_strAccessor = "public";
		skipWord(); return true;
    }
	else if(strReadType.compareTo("private") == 0){
		m_strAccessor = "private";
		skipWord(); return true;
	}
	else if(strReadType.compareTo("protected") == 0){
		m_strAccessor = "protected";
	    skipWord(); return true;
	}
	else if(strReadType.compareTo("enum") == 0){
		if(m_bDebug) System.out.println("enum " + strReadType);
		if(!findAndDoNotSkipWord("{", ";"))
			return false;
		if((strReadType = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
		m_bInsideEnum = true;
	}
	if(m_bInsideEnum){
		if(strReadType.compareTo("}") == 0){      //matching {
			m_bInsideEnum = false;
			return true;
		}
		strReadName = strReadType;
		if((strSeparator = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
		if(strSeparator.compareTo("=") == 0) {
			if((strSeparator = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
			strReadName += " = ";
			m_iEnumValue = new Integer(strSeparator).intValue() + 1;
			strReadName += strSeparator;
			if((strSeparator = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
		}
		
		else if(!strSeparator.equals("=")){
			strReadName += " = ";
			strReadName += String.valueOf(m_iEnumValue++);
		}
		if(strSeparator.compareTo("}") == 0) { //matching {
			m_iEnumValue = 0;
			m_bInsideEnum = false;
		}
		
		
		strReadType = "static final int";
		if(m_bDebug) System.out.println("enum " +  strReadName);
		type.SetType(Type.Variable);
		return true;
	    }

	
	StringArray lines = new StringArray();
	if(strReadType.compareTo("{") == 0) {
		lines = new StringArray();
		skipMatching(strReadType, "}"); 
		return true;
	}

	if(strReadType.compareTo("}") == 0) {
		m_scan.ungetByte((byte)strReadType.charAt(0));
		m_bCommaSeen = false;
		return false;
	}
	if(strReadType.compareTo(";") == 0) return true;
	if(strReadType.compareTo("class") == 0 ||
		strReadType.compareTo("struct") == 0){
		String strName = new String();
		String strNestedClassName;
		if((strNestedClassName = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0)
			return true;
		if(m_bDebug) System.out.println("nested_class_name " + strNestedClassName);
		if(readSuperClasses(strName)){
			strName = strClassName;
			strName += strNestedClassName;
			readClassBody(strName);
			findClass(strName, false).SetNested(true);
			skipMatching("{", "}");
			if((strSeparator = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0)
				return false;
			return true;
		}
	}

	if((strReadName = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0)
		return false;
	if(m_bDebug) System.out.println("name = " + strReadName);

	if(strReadName.compareTo("(") == 0){
		strReadName = strReadType;
		if(m_bDebug) System.out.println("Before Constuructor");
		paramList = readParmList();

		if(findAndDoNotSkipWord("{", ";")){
			if(strReadName.compareTo(strClassName) == 0){
				type.SetType(Type.InlinedConstructor);
				readMemberBody(strReadName, strReadName, "", paramList);
			} 
			else{
				readMemberBody(strReadName, strReadName, "", paramList);
				return true;
			}
		}
		else
			type.SetType(Type.Constructor);
		if(m_bDebug)System.out.println("found constructor");
		return true;
	}
	

	if(strReadType.compareTo("~") == 0){
		strReadType += strReadName;
		strReadName = strReadType;
		strReadType = "";
		paramList = readParmList();
		if(findAndDoNotSkipWord("{", ";")){
			type.SetType(Type.InlinedDestructor);
			readMemberBody(strReadName.substring(1),strReadName, "", paramList);
		}
		else
			type.SetType(Type.Destructor);
			 if(m_bDebug) System.out.println("found destructor");
			 return true;
	}
	if(strReadName.compareTo("{") == 0) {skipMatching("{", "}"); return true;}
	if(strReadName.compareTo("}") == 0) {m_scan.ungetByte((byte)'}'); m_bCommaSeen = false; return false;}
	if(strReadName.compareTo(";") == 0) {m_scan.ungetByte((byte)';'); return true;}

	type.SetType(Type.Variable);

	for(;;){
		if(strReadType.compareTo("static") == 0) bIsStatic = true;
		if((strSeparator=m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
		if(m_bDebug) System.out.println("separator= " + strSeparator);

		if(strSeparator.compareTo("}") == 0) { m_scan.ungetByte((byte)'}'); m_bCommaSeen = false; return false;}
		if(strSeparator.compareTo("{") == 0){
			skipMatching("{", "}");
		}
		if(strSeparator.compareTo("[") == 0){
			skipMatching("[", "]");
			type.SetType(Type.Array);
			break;
		}
		if(strSeparator.compareTo("(") == 0){          //read member parameters
			if(m_bDebug){ System.out.println("Seen start of function");
			              System.out.println("before readParmList"); }
			paramList  = readParmList();
			if(m_bDebug)
			System.out.println("after read parmList found a method "); 

			if(findAndDoNotSkipWord("{", ";")) { //}
				if(strReadName.compareTo("=") == 0 ||
				   strReadName.compareTo("!=") == 0 || 
				   strReadName.compareTo("==") == 0 ||
				   strReadName.compareTo("new") == 0 ||
				   strReadName.compareTo("delete") == 0)
						bIsStatic = true;
				readMemberBody(strClassName, strReadName, strReadType, paramList);
				if(bIsStatic)type.SetType(Type.StaticInlinedMethod);
				else type.SetType(Type.InlinedMethod);
			}
			else{
				if(bIsStatic)
					type.SetType(Type.StaticMethod);
				else type.SetType(Type.Method);
			}

			if(m_bDebug)System.out.println("end of function");
			break;
		}

		if(strSeparator.compareTo(";") == 0){
			m_bCommaSeen = false;
			break;
		}
		if(strSeparator.compareTo(",") == 0){
			m_bCommaSeen = true;
			return true;
		}

		else if(strReadName.compareTo("*") == 0)  //Replacer will take care of it
			strReadType += " " + strReadName;
		else if(strReadName.compareTo("&") == 0) 
			strReadType += " " + strReadName;
		else  strReadType = strReadName;        //.substring(0, m_iBufSize -1);
		
		strReadName = strSeparator;  //strType.substring(0, m_iBufSize -1);
	
	} 

	if(strReadType.compareTo(":") == 0) strReadType = "";
	return true;


}	

/**
 * findMember
 * Purpose: finds a class member which was previously read
 * @param strClassName	the class name
 * @param strMethodName	the name of the member to be found
 * @return ClassMemberRepresentation of the found member, or null if 
 * the member was not found
 *
 */

ClassMemberRepresentation findMember(String strClassName, String strMethodName)
{

   ClassRepresentation c = findClass(strClassName, false);
   if(c!=null)
	  for(ClassMemberRepresentation m = c.GetMembers(); m!=null; m = m.GetNext())
	      if(m.GetName().compareTo(strMethodName) == 0) return m;
   return null;


}	

/**
 * isStaticMethod
 * not being used
 *
 */
boolean isStaticMethod(String strClassName, String strMethodName)
{
	if(strMethodName.compareTo("=") == 0 ||
	   strMethodName.compareTo("!=") == 0 ||
	   strMethodName.compareTo("==") == 0 ||
	   strMethodName.compareTo("new") == 0 ||
	   strMethodName.compareTo("delete") == 0)
	    return true;
	ClassMemberRepresentation m = findMember(strClassName, strMethodName);
    if(m == null)
		return true;

	return (m.GetMemberType().GetType() == Type.StaticMethod || 
		m.GetMemberType().GetType() == Type.StaticInlinedMethod);

}

/**
 * isAValidName
 * Purpose: determines if the strName is a valid token
 * @param strName the word to be checked
 * @return boolean
 *
 */

boolean isAValidName(String strName)
{

   return(strName!=null && strName.length() > 0 && strName.charAt(0)!=';');
}


String strReadName; //Since it is not possible to  pass Strings by reference for modifications
String strReadType; 
ParmList paramList;
/**
 * readClassBody
 * Purpose: parse the class definition
 * @param strClassName	the class name
 */

void readClassBody(String strClassName) 
{
	if(m_bDebug) System.out.println("readClassBody " + strClassName);
	ClassRepresentation c = findClass(strClassName, false);
	strReadName = new String();
	strReadType = new String();
	Type type = new Type();
	ClassMemberRepresentation mdouble = null;
	ClassMemberRepresentation m = null;
	paramList = new ParmList("", "" ,null);  //?????????? check it out
	while(readMember(strClassName, type))
		if(isAValidName(strReadName)){
			m = new ClassMemberRepresentation(strReadType, strReadName, type, m, paramList, m_strAccessor); 
			c.SetMembers(m);
			if(m.GetMemberType().GetType() == Type.Method)
				for(mdouble = c.GetMembers(); mdouble!=null; mdouble = mdouble.GetNext())
					if((m.GetMemberType().GetType() == Type.StaticMethod || 
					   m.GetMemberType().GetType()  == Type.Method) &&
					   m.GetName().compareTo(mdouble.GetName()) == 0 &&
					   m.GetMemberType().GetType() == mdouble.GetMemberType().GetType()){
					//cannot cope with overloaded functions yet.
					m.SetMemberType(new Type(Type.StaticMethod));
				}

	    }
}

   /**
    * readParmList
	* Purpose: read a list of function parameters and put them into ParmList
	* @return ParmList
	*
	*/

 
   ParmList readParmList()
   {
   
		if(m_bDebug)System.out.println("enter readParmList()");

		byte []btArray = new byte[m_iBufSize];
		String strToken;
		ParmList list = new ParmList("", "", null);
		String strType = new String();
		String strName = new String();
		do{
		   if((strToken = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return null;

		   if(strToken.compareTo(",") == 0 || strToken.compareTo(")") == 0){
				strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-3);
				strName = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2);
				if(strType.compareTo(",") == 0 || strType.compareTo("(") == 0 || strType.compareTo(":") == 0){
					strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2);
					strName = "";
				}

				if(strType.compareTo("*") == 0 || strType.compareTo("&") == 0){
					for(int i = 0; i < 30 ; i++){
						String str = new String(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-(4 +i)));
						strType = str + strType;
						if(str.compareTo("*") !=0 && str.compareTo("&") !=0)
							   break;
						
					}
					//strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-4) + strType;
					strName = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2);
				}

				if(strType.compareTo("=") == 0){
					strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-5);
					strName = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-4);
				}

				if(strName.compareTo("&") == 0){
					strName = "dummy_";
					strType += "&";
				}
				if(strName.compareTo("*") == 0){
					strName = "dummy_";
					strType += "*";
				}
				if( ((m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-4)).compareTo("unsigned") == 0) &&
					(strType.compareTo("long") == 0 || strType.compareTo("short") == 0 || strType.compareTo("int") == 0 ||
					strType.compareTo("char") == 0)){

					strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-4);
					strType += "_";
					strType += m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-3);
				}


				if( (strType.compareTo("unsigned") == 0) &&
					(strName.compareTo("long") == 0 || strName.compareTo("short") == 0 || strName.compareTo("int") == 0 ||
					strName.compareTo("char") == 0)){
					strType+="_";
					strType+=strName;
					strName = "";
					list = new ParmList(strType, "", list);
				}
				else if(strName.compareTo("void") == 0)
					list = new ParmList(strName, "", list);
				else if(strName.length() > 0 && Scanner.isIdentifier( (byte)(strName.charAt(0)) ) )
					list = new ParmList(strType, strName, list);
				else if(strName.length() > 0 && strName.charAt(0) == '(')
					list = new ParmList("", "", list);
				else
					list = new ParmList(strType, "0", list);
		   }
		}while(strToken.compareTo("{") != 0 && strToken.compareTo(";") != 0 && strToken.compareTo(")") != 0);
   
		if(m_bDebug){
			System.out.println("strType is " + strType);
			System.out.println("strName is " + strName);
		}

		if(strToken.compareTo(")") == 0){
			if((strToken = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return null;
			if(strToken.compareTo("const") == 0)
				if((strToken = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return null;
		}

		//read initializer list
		if(strToken.compareTo(":") == 0){
			if((strToken = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return null;
			  m_strInitializer = "\n ";
			  boolean bSuperConstructorCall = false;
			  while(strToken.compareTo("{") != 0 && strToken.compareTo(";") !=0){
				  if(strToken.charAt(0) >='A' && strToken.charAt(0) <= 'Z'){ //traditionally class names 
					   m_strInitializer += "super";		   //are capitalized
					   bSuperConstructorCall = true;
				  }
				  else if(bSuperConstructorCall){
					  if(strToken.compareTo("(") == 0)	 m_strInitializer += "(";
					  else if(strToken.compareTo(")") == 0){
						  m_strInitializer+=");\n\t";
						  bSuperConstructorCall = false;
					  }
					  else if(strToken.compareTo(",") == 0) m_strInitializer += ",";
					  else m_strInitializer+=strToken;
				  }
					  
				  else{ 
					   if(strToken.compareTo("(") == 0) m_strInitializer += " = ";
					   else if(strToken.compareTo(")") == 0) m_strInitializer += ";\n\t";
					   else if(strToken.compareTo("')") == 0)m_strInitializer += "';\n\t";  
					   else if(strToken.compareTo(",") == 0) m_strInitializer += "";
					   else m_strInitializer += strToken;
				  }

				  if((strToken = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return null;
			  }	//while

		}  //if

		 m_scan.ungetByte((byte)(strToken.charAt(0)));
		 if(m_bDebug) System.out.println("leave readParmList()");
		 return list;	 
}
   
/**
 * readClass
 * Purpose: read a class or not a class
 * @return boolean false if the end of file has been reached
 * true otherwise
 *
 */


boolean readClass(boolean bDefnOnly)	throws IOException
{
    m_bDefnOnly = bDefnOnly;
	if(m_bDebug)System.out.println("readClass() {");
	byte [] btArray = new byte[m_iBufSize];
	StringArray lines = new StringArray();
	String strToken = new String();
	String strClassName = new String();
	int iWordsRead = 0;
	String strConst = new String();

	for(;;){
		
		if((strToken=m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
		if(isConstCharOrString(strToken))
			strConst = new String(strToken);
		iWordsRead++;
		//boolean bTest = (strToken.compareTo("class") == 0);
		if(strToken.compareTo("class") == 0 ||
			strToken.compareTo("struct") == 0){	//start of class definition
			if((strClassName=m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
			if(readSuperClasses(strClassName)){
				readClassBody(strClassName); //will read unto }
				if(m_bDebug) System.out.println("}");
				m_scan.ungetByte((byte)' '); //clear scan buffer
				iWordsRead = 0;
				return true;
			}

			continue;
		}

		String strType = new String();
		strClassName = "";
	 	String strMethodName = new String();
		if(strToken.compareTo("(") == 0){   //beginning of formal parameter list?
			if(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-3).compareTo(":") == 0 &&
				m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-4).compareTo(":") == 0) {
				
				iWordsRead = 0;
				strClassName = m_scan.m_tkStack.getClassNameFromStack(5);
				strMethodName = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2);
				strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-6);
				if(strType.compareTo("*") == 0 || strType.compareTo("&") == 0){
					for(int i = 0; i < 32; i++){
						String str = new String(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-(7 + i))); 
						strType = str + strType;
						if(str.compareTo("*") !=0  &&  str.compareTo("&") != 0)
				    		  break;
							//strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-7);
					}
				}
			}

			else if(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-3).compareTo("~") == 0 &&
				m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-4).compareTo(":") == 0 &&
				m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-5).compareTo(":") == 0){
				strClassName = m_scan.m_tkStack.getClassNameFromStack(6);
				strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-7);
				if(strType.compareTo("*") == 0 || strType.compareTo("&") == 0)
					   strType = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-8);
				strMethodName = "~";
				strMethodName+=m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2);
			}

			if(strClassName.compareTo("") != 0) { //start of class method
				
				iWordsRead = 0;
				if(m_bDebug)System.out.println("start of class method");
				ParmList p = readParmList();
				//String strToken;
				if((strToken = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
				    iWordsRead ++;
				if(p!=null && strToken.compareTo("{") == 0){ //found a body
					if(m_bParseMethods){
						readMemberBody(strClassName, strMethodName, strType, p);
						if((strToken=m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
					}

					else{
						iWordsRead = 0;
						skipMatching("{", "}");
					}
				}
				if(m_bDebug)System.out.println("end of class method");
			}	
					
				else{ //start of global function
					if(m_bDebug)System.out.println("start of global function");
					strMethodName = m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2);
					for(int i = 0; i< 30; i++){
						  String str = new String(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize() - (3+i) ) );
						  strType = str+strType;
						  if(str.compareTo("*") !=0 && str.compareTo("&") != 0)
							 break;
					}
					    
					ParmList p = readParmList();
					//String strToken;
					if((strToken = m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
					    iWordsRead ++;
					if(strToken.compareTo("{") == 0){
						if(m_bDebug)
						 if(p!=null){
							p.printNames();
						 }
					
						if(m_bDebug){
							System.out.println("Before calling readGlobalBody, type is " + strType);
							m_scan.m_tkStack.printStack(7);
						}
						readGlobalBody(strMethodName, strType, p);
						iWordsRead = 0; 
						if((strToken=m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
				
					
					}

					else if(strToken.compareTo(";") == 0){
						iWordsRead = 0;   
						continue;
					}
					else if((strToken=m_scan.getWord(btArray, m_iBufSize)).compareTo("") == 0) return false;
				}	     iWordsRead++;
			}

			
			if(/*m_bParseMethods && */m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2).compareTo("}") !=0 &&
				strToken.compareTo(";") == 0){	 //end of declaration
				if(m_bDebug)System.out.println("start of static declaration");
				if(m_bDebug)m_scan.m_tkStack.printStack(10);
				m_linesGlobal.Concatenate("\n");
				m_linesGlobal.Concatenate("/*@c2j++ The following variable used to be declared global */");
				m_linesGlobal.Concatenate("\n");
				for(int i = m_scan.m_tkStack.GetSize() - iWordsRead; i < m_scan.m_tkStack.GetSize(); i++){
					if(m_scan.m_tkStack.TokenAt(i).compareTo("static") == 0)
						m_linesGlobal.Concatenate("final "); //?????
					else if(m_scan.m_tkStack.TokenAt(i).compareTo("const") == 0){
						m_linesGlobal.Concatenate("static final ");
					   continue;
					}
					
					if(i == m_scan.m_tkStack.GetSize() -1){
					  if(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-2).compareTo("=") == 0) 
						 m_linesGlobal.Concatenate(strConst);
					}
					if(i!= m_scan.m_tkStack.GetSize() - iWordsRead || m_scan.m_tkStack.TokenAt(i).compareTo(";") != 0)    
					   m_linesGlobal.Concatenate(m_scan.m_tkStack.TokenAt(i) + " ");
					/*
						   for(int j = i; j >=0; j--){
							m_tos.writeBytes(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-j));
							if(m_bDebug)System.out.println(m_scan.m_tkStack.TokenAt(m_scan.m_tkStack.GetSize()-j));
						} */
				}
				
				m_linesGlobal.Concatenate("\n");
				iWordsRead = 0;	
				
				if(m_bDebug)System.out.println("end of declaration");
			}  //end of declaration

		} //end of for
			
		 
 }
 
}