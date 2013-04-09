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
import java.util.Enumeration;
import java.util.Stack;
import java.io.*;

/**
 * Purpose: A vector of Strings
 * @version 1.0	 December 96
 * @author Ilya Tilevich
 *
 */
class StringArray
{
	 	
      private Vector m_Vector;
	  
	  /**
	   * Purpose: Constructor
	   * @param iCapacity the initial capacity
	   * @param iIncr	the increment
	   */
	  public StringArray (int iCapacity, int iIncr){
               m_Vector = new Vector(iCapacity, iIncr);
      }

	   /**
	   * Purpose: Constructor
	   * @param iCapacity the initial capacity
	   */
	  public StringArray (int iCapacity){
                m_Vector = new Vector(iCapacity);
       }

      
	   /**
	   * Purpose: Constructor
	   */
	  public StringArray (){
		m_Vector = new Vector();
       }

	/**
	 * Purpose: adds an element to the array
	 * @param obj the Stirng to add
	 */
	public synchronized void addElement(String obj){
              m_Vector.addElement(obj);
 	}

    /**
	 * Purpose: appends strToAppend to the last String added to the array
	 * if no string were added works like addElement()
	 * @param strToAppend
	 */
	public synchronized void appendToLastString(String strToAppend){
		if(size() <= 0)
			addElement(strToAppend);
		else{
			String str = new String (elementAt(size()-1));
			str += strToAppend;
			setElementAt(str, m_Vector.size()-1);
		}
	}
	
	/**
	 * Purpose: Returns the size for the array
	 * @return int
	 */
	public int size() { return m_Vector.size(); }

	/**
	 * Purpose: clones the array
	 */
	public synchronized Object clone(){
              return m_Vector.clone();
	}

	/**
	 * Purpose: searches the array for a given String
	 * @param elem
	 */
	public boolean contains(String elem){
              return m_Vector.contains((Object) elem);
	}

	/**
	 * Purpose: Returns the String at a given index
	 * @param iIndex
	 */
    public String elementAt(int iIndex){

		return (String) m_Vector.elementAt(iIndex);
	}

    /**
	 * Purpose: sets an element of the array at a given index to a given value
	 * @param obj
	 * @param iIndex
	 */
	public void  setElementAt(String obj, int iIndex){
		   m_Vector.setElementAt((Object) obj, iIndex);
	}

	/**
	 * Purpose: Copies this object into a given strArray[]
	 * @param strArray the String array to copy the current object to
	 */
	protected synchronized void copyInto(String strArray[]){
		strArray = new String[size()];
		for(int i = 0; i < size(); i++)
		     strArray[i] = elementAt(i);
	}


	/**
	 * Purpose: Returns the enumeration object for this object
	 * @return Enumeration
	 */
	public synchronized Enumeration elements(){
		return m_Vector.elements();
	}

	/**
	 * Purpose: Removes the specified element
	 * @param obj 
	 */
	
	public synchronized boolean removeElement(String obj){
		return m_Vector.removeElement((Object) obj);
	}

	/**
	 * Purpose: Converts this object to a String
	 * @return String
	 */
	public synchronized String toString(){
 		return m_Vector.toString();
	}

	
	/**
	 * Purpose: Writes this object to DataOutputStream
	 */
	public 	synchronized void WriteOut(DataOutputStream  dos){
		
		Enumeration enum = elements();
		
		try{
			for(;enum.hasMoreElements();){
				 String str = (String)(enum.nextElement());
				 dos.writeBytes(str);
				 dos.writeByte('\n');
			}
		}
		catch(IOException e){System.out.println("IO error in StringArray WriteOut"); }
	
	}

	 /**
	  *purpose: Writes this object to stdio
	  */
	public 	synchronized void WriteOut(){  //to stdio
		
		Enumeration enum = elements();
		
		for(;enum.hasMoreElements();){
			 String str = (String)(enum.nextElement());
			 System.out.println(str);
        }
	 
	}




}


/**
 *Purpose: Encapsulates the function body
 * @version 1.1 January 97
 * @author Ilya Tilevich
 */
class MemberBody{
	
	//StringArray m_strArrayBody;
	Buffer m_Buffer;
	String m_strMemberName;

	/**
	 *Purpose: Constructor
	 *@param strName member name
	 *@param strArrayBody
	 */
	public MemberBody(String strName, Buffer buffer)
	{
		m_strMemberName = strName;
		//m_strArrayBody = strArrayBody;
		m_Buffer = (Buffer)buffer.clone();
	}

	/**
	 * Purpose: Returns member's name
	 * @return String
	 */
	public String getName() { return m_strMemberName; }
	
	/**
	 * Purpose: Sets member's name
	 * @param strName
	 */
	public void setName(String strName) { m_strMemberName = strName; }
	
	/**
	 * Purpose: Returns everything between {} 
	 * @return StringArray
	 */
	public Buffer getBody() { return m_Buffer; }
	
	/**
	 * Purpose: Sets body for this member body
	 * @param StringArray
	 */
	public void setBody(Buffer buffer) { m_Buffer = buffer; }
	
	/**
	 * Purpose: writes this fuction to DataOutputStream
	 * @exception IOException if problems occur during writing
	 */
	public void WriteOutBody(DataOutputStream  dos) throws IOException
	{
		m_Buffer.WriteOut(dos);	
	
	}

	/**
	 * Purpose: writes this fuction to stdio
	 * @exception IOException if problems occur during writing
	 */
	public void WriteOutBody()throws IOException
	{
		m_Buffer.WriteOut();
	}

}


/**
 * Purpose: Stores and handles an array of functions
 * @version 1.0 December 96
 * @author Ilya Tilevich
 *
 */

class MembersArray
{
	 	
      private Vector m_Vector;

	  /**
	  * Purpose: Constructor
	  * @param iCapacit the initial capacity
	  * @param iIncr the increment
	  *
	  */
	  public MembersArray (int iCapacity, int iIncr){
               m_Vector = new Vector(iCapacity, iIncr);
      }

	  /**
	  * Purpose: Constructor
	  * @param iCapacit the initial capacity
	  *
	  */
	   public MembersArray (int iCapacity){
                m_Vector = new Vector(iCapacity);
       }

       /**
	  * Purpose: Constructor
	  */
	   public MembersArray (){
		m_Vector = new Vector();
       }

	 /**
	  * Purpose: adds an element to the array
	  * @param obj the MemberArray to add
	  */
		
	   public synchronized void addElement(MemberBody obj){
              m_Vector.addElement(obj);
 	    }

	   /**
	    * Purpose: returns the size of the array
		* @return int
	    */
	   
	   public int size() { return m_Vector.size(); }

	   /**
	    * Purpose: clones the object
		* @return Object
		*/
		public synchronized Object clone(){
              return m_Vector.clone();
		}

		/**
		 * Purpose: Searches the array for a given element
		 * @return boolean
		 */
		public boolean contains(MemberBody elem){
              return m_Vector.contains((Object) elem);
		}

		/**
		 * Purpose: Returns the element at a given index
		 *
		 */
		public MemberBody elementAt(int iIndex){

			return (MemberBody) m_Vector.elementAt(iIndex);
		}

		/**
		 * Purpose: Sets an element at a given index
		 * @param obj
		 * @param iIndex
		 */
		
		public void  setElementAt(MemberBody obj, int iIndex){
		   m_Vector.setElementAt((Object) obj, iIndex);
		}

	
		/**
		 * Purpose: Returns Enumeration for this object
		 * @return Enumeration
		 *
		 */
		public synchronized Enumeration elements(){
	 	     return m_Vector.elements();
		}

		/**
		 * Purpose: Removes the given MemberBody
		 * @param obj 
		 * @return boolean true if the obj was found, false otherwise  
		 *
		 */
		public synchronized boolean removeElement(MemberBody obj){
			return m_Vector.removeElement((Object) obj);
		}

		/**
		 * Purpose: converts this object to a String
		 * @return String
		 */
		public synchronized String toString(){
 			return m_Vector.toString();
		}

		/**
		 * Purpose: write this object to DataOutputStream
		 * @param dos
		 * @exception IOException
		 */
		public 	synchronized void WriteOut(DataOutputStream  dos){
		
			Enumeration enum = elements();
		
		 try{
			for(;enum.hasMoreElements();){
				 MemberBody member = (MemberBody)(enum.nextElement());
				 dos.writeChars(member.getName());
				 dos.writeByte('\n');
				 member.getBody().WriteOut(dos);
			}
		 }
		 catch(IOException e){System.out.println("IO error in WriteOut"); }
	
		}


		/**
		 * Purpose: write this object to stdio
		 */
		
		public 	synchronized void WriteOut(){
		
			Enumeration enum = elements();
		
			for(;enum.hasMoreElements();){
				 MemberBody member = (MemberBody)(enum.nextElement());
				 System.out.println("Member Name:");
				 System.out.println(member.getName());
				 System.out.println("Function Body:");
				 member.getBody().WriteOut();
			}

		}

		
		/**
		 * Purpose: Find a given member body in the array and write it out to DataOutputStream
		 * @param dos	DataOutputStream
		 * @param strName  String
		 * @return boolean true if the method was found, false otherwise
		 * @exception IOException if error happened during writting
		 */
		
		public synchronized boolean FindAndWriteOut(DataOutputStream  dos, String strName){
		
			Enumeration enum = elements();
		
		// try{
			for(;enum.hasMoreElements();){
				 MemberBody member = (MemberBody)(enum.nextElement());
				 String str = new String(member.getName());
				 if(str.compareTo(strName)	== 0){
					 member.getBody().WriteOut(dos);
					 return true;
				 }	 
					 
			}
		 //}
		 //catch(IOException e){System.out.println(e+"IO error in FindAndWriteOut"); }
	
		 return false;
		
		}


		/**
		 * Purpose: Find a given member body in the array and write it out to stdio
		 * @param strName  String
		 * @return boolean true if the method was found, false otherwise
		 * @exception IOException if error happened during writting
		 */
		public synchronized boolean FindAndWriteOut(String strName){
		
			Enumeration enum = elements();
		
		 
			for(;enum.hasMoreElements();){
				 MemberBody member = (MemberBody)(enum.nextElement());
				 if(member.getName().compareTo(strName)	== 0){
					 member.getBody().WriteOut();
					 return true;
				 }	 
					 
			}
		 
		 return false;
		
		}



}


