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

import java.util.Vector;
import java.io.*;
import java.util.StringTokenizer;


/**
 * Purpose: Encapsulates a pair of String objects
 * @version 1.0 December 96
 * @author Ilya Tilevich
 */

class StringPair
{

  protected String m_strOne;
  protected String m_strTwo;
  
  /**
   * Purpose: Constructor
   */
  public StringPair()
  {
  	  m_strOne = new String("");
	  m_strTwo = new String("");
  }
  
  
  /**
   * Purpose: Constructor
   * @param strOne - first String
   * @param strTwo - second String
   */
  
  public StringPair(String strOne, String strTwo)
  {
	  m_strOne = new String(strOne);
	  m_strTwo = new String(strTwo);
  }

  /**
   * Purpose: compares two StringPairs
   * @param StringPair
   */
  public boolean equal(StringPair stPair){
	  return (m_strOne == stPair.m_strOne && m_strTwo == stPair.m_strTwo);
  }
  
  /**
   * Purpose: returns the first string of the pair
   * @return String
   */
  public String GetFirstString() { return m_strOne; }
  
  /**
   * Purpose: returns the second string of the pair
   * @return String
   */
   public String GetSecondString() { return m_strTwo; }

  /**
  * Purpose: Sets the value for the first String
  * @param strOne
  */
  public void SetFirstString(String strOne) { m_strOne = strOne; }
  
  /**
  * Purpose: Sets the value for the second String
  * @param strTwo
  */
  public void SetSecondString(String strTwo) { m_strTwo = strTwo; }

 
}



/** 
 * Purpose: Read a text file and replace given patterns
 * Note: Doesn't support recursive replacement yet.
 * @version 1.0 December 96
 * @author Ilya Tilevich
 */ 
class Replacer
{
   	private Vector m_vect;
	private int m_iTotal;
	String m_strFileIn;
	private FileInputStream fisFileIn = null;
	private BufferedInputStream bisBufferedInput = null;
	private DataInputStream disDataIn = null;

	private FileOutputStream fosFileOut = null;
	private DataOutputStream dosDataOut = null;
	private boolean m_bDebug = false;

	/**
	* Purpose: Constructor
	* @param strFileIn - name of the source file
	* @param vect - Vector of StringPairs (key, replacement) 
	* @exception IOException if an error opening strFileIn occurs
	*/
	public Replacer(String strFileIn, Vector vect){ 
		 
		m_iTotal = 0;
		m_strFileIn = new String(strFileIn);
		
		try{
	           fisFileIn = new FileInputStream(strFileIn);
		    //Creates an instance of the class BufferedInputStream
		    //named bufferedInput
		   //bufferedInput receives the stream from the fileInputStream
		   //fileName as it is read
		   bisBufferedInput = new BufferedInputStream(fisFileIn);
		   disDataIn  = new DataInputStream(bisBufferedInput);

		   fosFileOut  = new FileOutputStream("__temp__");
		   dosDataOut = new DataOutputStream(fosFileOut);

		}
		catch(FileNotFoundException e){
		    System.out.println("File Not Found");
		    return;
	    }
		catch(Throwable e) {
	        System.out.println("Error in opening file");
	        return;
	    }

		   
		m_vect = vect;   
		
	}


/**
 * Purpose: reads the source file, matched given strings, and replaces them as
 * specified
 * @exception IOException
 */
public int MatchAndReplace() throws IOException
{
   	String str, strReplacement = new String("/** @c2j++ Replacement from ");
	int iCount = 0;
	boolean bSkip = false;
	while((str = disDataIn.readLine()) != null){
	    if(str.trim().startsWith("//")){
			dosDataOut.writeBytes(str+"\n");
			continue;
		}
		
		if(str.indexOf("/*")!=-1)
			bSkip = true;
		
		if(!bSkip){		  
			
			if(str.trim().startsWith("cout")){
				String strTemp = new String("");
				while(!str.trim().endsWith(";") && strTemp != null){
					strTemp = disDataIn.readLine();
				    str += strTemp;
				}
				
				dosDataOut.writeBytes(strReplacement + str.trim());
				str = CoutToSystemOut(str);
				dosDataOut.writeBytes(str + "*/ \n");
			}
			else if(str.trim().startsWith("cerr")){
				String strTemp = new String("");
				while(!str.trim().endsWith(";") && strTemp != null){
					strTemp = disDataIn.readLine();
				    str += strTemp;
				}
				
				dosDataOut.writeBytes(strReplacement + str.trim());
				str = CerrToSystemErr(str);
				str = CoutToSystemOut(str);
				dosDataOut.writeBytes(" */\n");
			}
			else if(str.trim().startsWith("strrev")){
				dosDataOut.writeBytes(strReplacement + str.trim()); 
				str = StrRevToReverse(str);
				str = CoutToSystemOut(str);
				dosDataOut.writeBytes(" */\n");
			} 
			else if(str.trim().startsWith("delete")){
				dosDataOut.writeBytes(strReplacement + str.trim());
				str = DeleteToNull(str);
				str = CoutToSystemOut(str);
				dosDataOut.writeBytes(" */\n");
			}
			else 
			   str = Replace(str);	
		}
		
		dosDataOut.writeBytes(str); 
		dosDataOut.writeByte('\n');
		
		if(str.indexOf("*/")!= -1)
			bSkip = false;
			
	}

	if(m_bDebug)
		System.out.println(String.valueOf(m_iTotal) + " instances replaced");
	
	
	   
   disDataIn.close();
   bisBufferedInput.close();
   fisFileIn.close();
   
   dosDataOut.close();
   fosFileOut.close();
   
   
   CopyAFile copier = new CopyAFile("__temp__", m_strFileIn);
   copier.DoCopy();
	
     	
   
	return m_iTotal;
}

private String Replace(String strLine) throws IOException
{
	
	String strToReplace = new String();
	String strReplacement = new String();
	String strStart = new String();
	String strEnd = new String();
	String strRep = new String();
	//////////////////
	int iCount = 0, iIndex = 0;
	while(iCount != m_vect.size()){
	    StringPair pair = (StringPair)(m_vect.elementAt(iCount));  
		strToReplace = pair.GetFirstString();
		strReplacement = pair.GetSecondString();
		iCount++;

		do{
			
			iIndex = strLine.indexOf(strToReplace);

				
			if(iIndex!=-1){
				strRep = strReplacement;
				
				strStart = (iIndex != 0) ? (strLine.substring(0, iIndex)) : "" ;
				
				int i =	iIndex + strToReplace.length();
				strEnd = strLine.substring(i);
				strStart+=strReplacement;
				strStart+=strEnd;
				m_iTotal ++;
				if(strReplacement.compareTo(" ")==0)
					strRep = "\" \"";
				dosDataOut.writeBytes("/* @c2j++: \"" + strLine.trim() + "\" replacement: " + strToReplace + 
					" to " + strRep + " */");
				dosDataOut.writeByte('\n');
				strLine = strStart;
				
			}
		}while(iIndex != -1);
	
	
    } //end of while
	return strLine;
}
	
   private String CoutToSystemOut(String str){
   		String strReturn = new String();
		String strParam; // = new String();
		String strPlus = new String(" + ");
		String strValueOf = new String("String.valueOf(");
		if(!str.trim().startsWith("cout")) return str;

		if(str.trim().endsWith("endl;"))
			strReturn =  " System.out.println(";
		else strReturn = " System.out.print(";

		StringTokenizer strTok = new StringTokenizer(str, "<;", false);
		boolean bFirst = true;
	    if(strTok != null){
			strParam = new String(strTok.nextToken());
			strParam = strParam.trim();
			do{
			   	if(strParam.compareTo("cout") != 0 && strParam.compareTo(" ") != 0){
					if(strParam.endsWith("\"") && strParam.startsWith("\"")){
			   		  if(!bFirst)
						  strParam = strPlus + strParam; // + "string";
					   
				   }
				   else if(strParam.compareTo("endl;")!= 0 && strParam.compareTo(";")!=0
					   && strParam.compareTo("endl")!=0){
					  
					   strParam = strValueOf + strParam + ")";
					   if(!bFirst)
						   strParam = strPlus + strParam;
				   }
					
				   strReturn += strParam;
				   if(bFirst)
						bFirst = false;
			   }

			   if(!strTok.hasMoreTokens()) break;
			   strParam = new String(strTok.nextToken());
			   strParam = strParam.trim();
			}while(strParam.compareTo(";") !=0 && strParam.compareTo("endl;") !=0 &&
				strParam.compareTo("endl")!=0 );

			
		}
		  
   		m_iTotal++;
		
		strReturn+=");";
		
		return strReturn;
   
   }

	
   private String CerrToSystemErr(String str){
   		String strReturn = new String();
		String strParam; // = new String();
		String strPlus = new String(" + ");
		String strValueOf = new String("String.valueOf(");
		if(!str.trim().startsWith("cerr")) return str;

		if(str.trim().endsWith("endl;"))
			strReturn = "System.err.println(";
		else strReturn = "System.err.print(";

		StringTokenizer strTok = new StringTokenizer(str, "<;", false);
		boolean bFirst = true;
	    if(strTok != null){
			strParam = new String(strTok.nextToken());
			strParam = strParam.trim();
			do{
			   	if(strParam.compareTo("cerr") != 0 && strParam.compareTo(" ") != 0){
					if(strParam.endsWith("\"") && strParam.startsWith("\"")){
			   		  if(!bFirst)
						  strParam = strPlus + strParam; // + "string";
					   
				   }
				   else if(strParam.compareTo("endl;")!= 0 && strParam.compareTo(";")!=0
					   && strParam.compareTo("endl")!=0){
					  
					   strParam = strValueOf + strParam + ")";
					   if(!bFirst)
						   strParam = strPlus + strParam;
				   }
					
				   strReturn += strParam;
				   if(bFirst)
						bFirst = false;
			   }

			   if(!strTok.hasMoreTokens()) break;
			   strParam = new String(strTok.nextToken());
			   strParam = strParam.trim();
			}while(strParam.compareTo(";") !=0 && strParam.compareTo("endl;") !=0 &&
				strParam.compareTo("endl")!=0 );

			
		}
		  
   		m_iTotal++;
		
		strReturn+=");";
		
		return strReturn;
   
   }

	private String DeleteToNull(String str){
		
		String strReturn = new String();
		String strParam;
		StringTokenizer strTok = new StringTokenizer(str);
		boolean bFirst = true;
	    
		if(strTok != null && strTok.countTokens() >= 2){
			if(strTok.nextToken().trim().compareTo("delete") == 0){
			    strParam = new String(strTok.nextToken());
				if(strParam.compareTo("[]") == 0) 
					strParam = new String(strTok.nextToken());

				if(strParam.endsWith(";"))
					strParam = strParam.substring(0, strParam.length() -1);
				      strReturn = strParam + " = null;";
				m_iTotal++;
				return strReturn;
			}
		}
	
		return str;
	
	}
	   	  
   	private String StrRevToReverse(String str){
	
		String strReturn = new String();
		String strParam;
		if(!str.trim().startsWith("strrev")) return str;

		
		StringTokenizer strTok = new StringTokenizer(str, "()", false);
		
	    if(strTok != null){
			strParam = new String(strTok.nextToken()); //strrev
			strParam = new String(strTok.nextToken());
			strParam = strParam.trim();
			strReturn = new String(strParam + " = new String(new StringBuffer(" + strParam + ").reverse());");
		}
		return strReturn;
}
		//Where execution begins in a stand-alone excutable
    public static void main(String[] args) throws IOException
	{
		   
		StringPair stPair1 = new StringPair("char * ","String ");
		StringPair stPair2 = new StringPair("char* ","String ");
		StringPair stPair3 = new StringPair("const char * " ,"String ");
		StringPair stPair4 = new StringPair(" & ", " ");
		StringPair stPair5 = new StringPair("*", " ");
		StringPair stPair6 = new StringPair("unsigned int " ,"int ");
		StringPair stPair7 = new StringPair("unsigned long ","int ");		
		StringPair stPair8 = new StringPair("unsigned char ", "char ");
		StringPair stPair9 = new StringPair("unsigned ","int");
		StringPair stPair10 = new StringPair("const " , "static final ");
			
				
			
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




		Replacer rep = new Replacer("c:\\c2j\\java\\makepar.java", v);
		rep.MatchAndReplace();

	}

}
