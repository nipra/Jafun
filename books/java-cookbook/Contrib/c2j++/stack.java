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

/**
 * A stack which can hold only m_iMaxSize elements
 * Purpose: Stack used by Parser
 *	 
 */
class TokenStack
{

	private int m_iInsertLoc;
    private int m_iSize;
    private int m_iMask; 

	final static int m_iMaxSize = 32;
	private static boolean m_bDebug = false;
/** 
 * Purpose: 
 * @return int
 */
	
	private int GetStartPos(){ return (m_iInsertLoc -1) & m_iMask; }
/** 
 * Purpose: 
 * @return int
 * @param iCurrent
 */
	private int GetNext(int iCurrent) { return (iCurrent & m_iMask); }
	Vector 	m_Vector;

	/**
	 * Purpose: Constructor
	 */
	public TokenStack(){ 
		int iTest = m_iMaxSize;
	
	while((iTest & 1) != 1)	
		iTest = iTest >> 1;
		
	
		if((iTest>>1)!=0){ 
			System.out.println("m_iMaxSize should be a power of two");
		
			System.exit(1);
		}
	
		
		m_Vector = new Vector(m_iMaxSize);
		
	
		m_iSize = 0;
	    m_iInsertLoc = 0;
	    m_iMask = m_iMaxSize -1;
		initialize();
	
	}
/** 
 * Purpose: 
 */

		 
	private	void initialize(){
		for(int i = 0; i < m_iMaxSize; i++)
			m_Vector.addElement("");
	
	}
	
	
	/**
	 * Purpose: Returns the constant size of the stack 
	 * @return int
	 */
	
	public int GetSize(){ return  m_iMaxSize; }
	/**
	 * Purpose: pushes the stack
	 * Note: if there m_iMaxSize elements on the stack, the bottom most element is
	 * removed
	 * 
	 */
	public void push(String obj) {
		m_Vector.setElementAt(obj, m_iInsertLoc);
		m_iSize ++;
		m_iInsertLoc = m_iSize % m_iMaxSize;
		if(m_iSize == m_iMaxSize)
		   m_iSize = 0;
	}
	
	
	/**
	 * Purpose: Gets a token from the top of the stack
	 * Note: This function was written to maintain the compatability
	 * with ParseStack class that uses GetSize() to call TokenAt()
	 * Later, Parser should be modified to call ElementFromTopOfTheStack()
	 */
	
	public String TokenAt(int iIndex) { 
	    
		if(iIndex < 0 || iIndex > m_iMaxSize-1) 
			return "";
		
		iIndex = m_iMaxSize-iIndex-1;
		return 	ElementFromTopOfTheStack(iIndex);
	}
	
	
	/**
	 * Purpose: gets an element from the top of the stack 
	 * using zero-based iIndex
	 * @param iIndex
	 * @return String
	 */
	public String ElementFromTopOfTheStack(int iIndex){
	
		
		if(iIndex < 0 || iIndex > (GetSize()-1) )
			return "";
		iIndex = (GetStartPos() - iIndex) & m_iMask;
		return (String)m_Vector.elementAt(iIndex);
	}
	

	/**
	 * Purpose: prints the contents of the stack up the iDepth
	 * @param iDepth
	 */
	public void printStack(int iDepth){
		
		int iSize = GetSize();
		if( iDepth > iSize -1 )	System.out.println("Unable to print stack to " + String.valueOf(iDepth) + " depth");

		else{
			
			System.out.println("Printing the TokenStack: ");
			
			for(int n = 0; n < iDepth; n++){
				System.out.print(n + "th element from the top of the stack -  ");
				System.out.println(ElementFromTopOfTheStack(n));
			}
		}

	
	}					

	/**
	 * Purpose: searches stack up to iDepth for a class name
	 * @param iDepth
	 * @return String
	 */
	public String getClassNameFromStack(int iDepth)
	{
		String strClassName = new String();
		int iSize = GetSize(), n;
		for(n = iDepth; n < iSize - iDepth -2; n+=3){
			if( TokenAt(iSize-(n+1)).compareTo(":") != 0  ||
			    TokenAt(iSize-(n+2)).compareTo(":")!= 0 )
				break;
		}
	
		if(m_bDebug)printStack(n);
	
		for(; n > iDepth; n-=3)
			strClassName += TokenAt(iSize-n);
		strClassName+=TokenAt(iSize-iDepth);
		
		return strClassName;

	}
/** 
 * Purpose: 
 */
	
	



 public static void main(String[] args){
	 TokenStack stack = new TokenStack();
	 stack.push("A");
	 stack.push("B");
	 stack.push("C");
	 stack.push("D");
	 stack.push("E");
	 stack.push("F");
	 stack.push("G");
	 stack.push("H");
	 stack.push("I");
	 stack.push("J");
	 stack.push("K");
	 stack.push("L");

	 stack.printStack(5);
 	 System.out.println(stack.TokenAt(stack.GetSize() - 1));
	 System.out.println(stack.TokenAt(stack.GetSize() -2));
	 System.out.println(stack.TokenAt(stack.GetSize() -3));
 	 System.out.println(stack.ElementFromTopOfTheStack(0));
	 System.out.println(stack.ElementFromTopOfTheStack(1));
	 System.out.println(stack.ElementFromTopOfTheStack(2));

 
 
 
 }



}


