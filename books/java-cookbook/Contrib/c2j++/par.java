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
 * 
 * Purpose: encapsulates an object which only characteristic is
 * its name
 * @version 1.0
 * @author Ilya Tilevich
 * Note: This class was based on the original code written in C++ by Chris Laffra
 */

class SomethingWithAName {
/**
 * 
 * Purpose: Constructor
 * @param strName	the name for the object
 */
public  SomethingWithAName(String strName) {
        m_strName = strName;
}

/**
 * 
 * Purpose: sets name
 * @param strNewName	the name to be set
 */
public void SetName(String strNewName) {
        m_strName = strNewName;
}

/**
 * 
 * Purpose: returns the name of the object
 */

public String GetName()  { return m_strName; }

 protected  String m_strName;
}

/** 
 * 
 * Purpose:  reads, holds, and prints  a list of function
 * parameters
 * @author Ilya Tilevich
 * @version 1.0 December 96
 * Note: This class was based on the original code written in C++ by Chris Laffra
 */

class ParmList
{

private SomethingWithAName m_Name;
private SomethingWithAName m_Type;
private ParmList m_next=null;  //self-referencing data-structure

/**
 * Purpose: Constructor
 * @param strType	the parameter's type
 * @param strName	the parameter's name
 * @param next_		points to the next parameter on the list
 * 
 */

public ParmList(String strType, String strName, ParmList next_){
       m_Name = new SomethingWithAName(strName);
       m_Type = new SomethingWithAName(strType);
       m_next = next_ ;
}

/**
 * Purpose: creates a valid copy of this object
 */
public Object clone(){
	ParmList list = new ParmList(m_Type.GetName(), m_Name.GetName(), m_next);
	return list;
}

/**
 * Returns the parameter's name
 * @return	SomethingWithAName
 *
 */
public SomethingWithAName GetName() {return m_Name; }

/**
 * Returns the parameter's type
 * @return SomethingWithAName
 */
public SomethingWithAName GetType() { return m_Type;}

/**
 * Returns the next parameter on the list
 * @return ParmList
 */

public  ParmList GetNext() { return m_next; }

/**
 * Returns the lenght of the parameters list
 * @return int
 *
 */
public  int length() { return (m_next!=null)? m_next.length() + 1 : 1; }
  
/**
 * Prints the list of parameters to stdio
 */
void printNames(){ 
	if(m_Name.GetName().compareTo("") == 0 || m_Name.GetName().compareTo("void") == 0) return;
	if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		m_next.printNames();
	if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		System.out.print(", ");
	if(m_Name.GetName().compareTo("0") !=0 )
		System.out.println(m_Type.GetName() + " " + m_Name.GetName());
}
 
/**
 * Prints the list of parameters to StringArray object
 * @param lines  the StringArray object to print the parameters to
 *
 */

void printNames(StringArray lines){
if(m_Name.GetName().compareTo("") == 0 || m_Name.GetName().compareTo("void") == 0) return;
	if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		m_next.printNames(lines);
	if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		lines.appendToLastString(", ");
	if(m_Name.GetName().compareTo("0") !=0 && m_Type.GetName().length() > 0 &&
		m_Name.GetName().length() > 0)
		lines.appendToLastString(m_Type.GetName() + " " + m_Name.GetName());
}


/**
 * Prints the list of parameters to Buffer object
 * @param lines  the Buffer object to print the parameters to
 *
 */

void printNames(Buffer lines){
if(m_Name.GetName().compareTo("") == 0 || m_Name.GetName().compareTo("void") == 0) return;
	if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		m_next.printNames(lines);
	if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		lines.Concatenate(", ");
	if(m_Name.GetName().compareTo("0") !=0 && m_Type.GetName().length() > 0 &&
		m_Name.GetName().length() > 0)
		lines.Concatenate(m_Type.GetName() + " " + m_Name.GetName());
}




/**
 * Prints the list of parameters for javadoc utility to DataOutputStream object
 * @param DataOutputStream	the stream to print the list to
 *
 */


 void printAsParms(DataOutputStream dos){
	  if(m_Name.GetName().compareTo("") == 0 || m_Name.GetName().compareTo("void") == 0) return;
	  if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		  m_next.printAsParms(dos);
	  try{
		  if(m_Name.GetName().compareTo("0") !=0 ){
			   dos.writeBytes(" * @param " + m_Name.GetName());
			   dos.writeBytes("\r\n");
		  }
	  }catch(IOException e){}	
  }
	  
 /**
  *	Prints the list of parameters for javadoc utility to StringArray object
  *
  */
 void printAsParms(StringArray lines){
      if(m_Name.GetName().compareTo("") == 0 || m_Name.GetName().compareTo("void") == 0) return;
	  if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		  m_next.printAsParms(lines);
	  
	  if(m_Name.GetName().compareTo("0") !=0 )
		   lines.addElement("* @param " + m_Name.GetName());
	  
  }

 
  /**
  *	Prints the list of parameters for javadoc utility to StringArray object
  *
  */
 void printAsParms(Buffer lines){
      if(m_Name.GetName().compareTo("") == 0 || m_Name.GetName().compareTo("void") == 0) return;
	  if(m_next!=null && m_next.GetType().GetName().compareTo("") != 0)
		  m_next.printAsParms(lines);
	  
	  if(m_Name.GetName().compareTo("0") !=0 ){
		   lines.Concatenate("* @param " + m_Name.GetName());
		   lines.Concatenate("\r\n");
	  }
	  
  }

 
  
  /**
  *	Appends the list of parameters to create a unique functions name to a String object
  *	@param strBuf	a string to appen
  * @return String  the resulting string
  */
 String printTypes(String strBuf){
		
		String strTemp = new String(strBuf);
	    
		if(m_Type.GetName().compareTo("") == 0) return strTemp;
		
		if(m_next!=null)
			strTemp = m_next.printTypes(strBuf);
		
		strTemp += "_";

  		strTemp += m_Type.GetName();
		
		return strTemp;
  }

 /**
  *	Prints the list of parameters to the DataOutputStream
  * @param dos DataOutputStream
  *
  */
 
 void printTypes(DataOutputStream dos){
		if(m_Type.GetName().compareTo("") == 0) return;
		
		if(m_next!=null)
			m_next.printTypes(dos);
		try{
		dos.writeBytes("_");

  		dos.writeBytes(m_Type.GetName());
		}
		catch (IOException e){} 
  }


 /**
  *	Prints the list of parameters to stdio
  *
  */
 void printTypes(){
		if(m_Type.GetName().compareTo("") == 0) return;
		
		if(m_next!=null)
			m_next.printTypes();
		//try{
		System.out.print("_");

  		System.out.print(m_Type.GetName());
		//}
		//catch (IOException e){} 
  }

  /**
   * Prints the list of parameters to the TextOutputStream
   * @param tos		the TextOutputStream to print to
   */
  void printTypes(TextOutputStream tos){
		if(m_Type.GetName().compareTo("") == 0) return;
		
		if(m_next!=null)
			m_next.printTypes(tos);
		try{
		tos.writeBytes("_");

  		tos.writeBytes(m_Type.GetName());
		}
		catch (IOException e){} 
  }


   /**
   * Prints the list of parameters to the StringArray
   * @param lines the StringArray object to print to
   */
  void printTypes(StringArray lines){
	  if(m_Type.GetName().compareTo("") == 0) return;
		
		if(m_next!=null)
			m_next.printTypes(lines);
		
		lines.addElement("_" +  m_Type.GetName());
  }
  
}


 /**
  *	 Encapsulates C++ types  
  *	 @author Ilya Tilevich
  *  @version 1.0 December 96
  */
  class Type{
      /**
	   * Purpose: Consturctor
	   */
	  public Type(){ m_iType = -1;} //default type
	  /**
	   * Purpose: Constructor
	   * @param iType	one of the static final members of this class
	   */
	  public Type(int  iType){	//send Type's static member and iType
            switch(iType){
                case Variable:
                       m_iType = Variable;
                       break;
                case Enum:
                        m_iType = Enum;
                        break;
                case Array:
                        m_iType = Array;
                        break;
                case Method:
                        m_iType = Method;
                        break;
                case InlinedMethod:
                         m_iType = InlinedMethod;
                         break;
                case Constructor:
                         m_iType = Constructor;
                         break;
                case InlinedConstructor:
                          m_iType = InlinedConstructor;
                          break;
                case Destructor:
                          m_iType = Destructor;
                          break;
                case InlinedDestructor:
                          m_iType = InlinedDestructor;
                          break;
                case StaticMethod:
                          m_iType = StaticMethod;
                          break;
                case StaticInlinedMethod:
                          m_iType = StaticInlinedMethod;
                          break;
                default:
                           m_iType = -1;
            }
    }
	/**
	 * Purpose:	 Converts the input parameter to a String
	 * @param iType  the type to convert to a String
	 * @return String	the type's name
	 */
   
	static String toString(int iType){ //might be usefull sometime
      String strReturn;

      switch(iType){
                case Variable:
                       strReturn = "Variable";
                       break;
                case Enum:
                        strReturn = "Enum";
                        break;
                case Array:
                        strReturn = "Array";
                        break;
                case Method:
                        strReturn = "Method";
                        break;
                case InlinedMethod:
                         strReturn = "InlinedMethod";
                         break;
                case Constructor:
                         strReturn = "Constructor";
                         break;
                case InlinedConstructor:
                          strReturn = "InlinedConstuctor";
                          break;
                case Destructor:
                          strReturn = "Destructor";
                          break;
                case InlinedDestructor:
                          strReturn = "InlinedDestructor";
                          break;
                case StaticMethod:
                          strReturn = "StaticMethod";
                          break;
                case StaticInlinedMethod:
                          strReturn = "StaticInlinedMethod";
                          break;
                default:
                           strReturn = "Garbage";
      }

            return strReturn;

    }
    
    static final int Variable  = 0;
    static final int Enum = 1;
    static final int Array = 2;
    static final int Method = 3;
    static final int InlinedMethod = 4;
    static final int Constructor = 5;
    static final int InlinedConstructor = 6;
    static final int Destructor = 7;
    static final int InlinedDestructor = 8;
    static final int StaticMethod = 9;
    static final int StaticInlinedMethod = 10;

    private int m_iType;
	/**
	 * Purpose: returns the type represented by this class
	 * @return int  the type
	 */
    public int GetType() { return m_iType; }
    /**
	 * Purpose: Sets the type for this class
	 * @param iType	the type to set
	 */
	public void SetType(int iType) {m_iType = iType; }
	/**
	 * Purpose: gets a name for the type this class represents which a human can read
	 * @return String	the Stirng representation of this class
	 */
	public String GetTypeAsString() { return toString(m_iType); }
  }






/** 
 * Purpose: Encapsulates C++ member-functions 
 * @version 1.0
 * @author Ilya Tilevich
 * Note: This class was based on the original code written in C++ by Chris Laffra
 */
class ClassMemberRepresentation{

   private SomethingWithAName m_Name;
   private SomethingWithAName m_Type;
   private Type m_ctType;
   private ClassMemberRepresentation m_cmemrepNext = null;
   private ParmList m_parameters;
   private String m_strAccessor;

   /**
    * Purpose: Constructor
	* @param strType	the member-function's return type
	* @param strName	the member-function's name
	* @param member_type_	the member-function's type
	* @param next_	the next ClassMemberRepresentation on the list
	* @param parameters_	the list of parameters
	* @accessor	 the level of accessability for the member
	*/
   
   public  ClassMemberRepresentation(String strType, String strName,
			Type member_type_, ClassMemberRepresentation next_,
			ParmList parameters_,
			String accessor_){
		    m_Name = new SomethingWithAName(strName);
            m_Type = new SomethingWithAName(strType);
	        m_ctType = new Type(member_type_.GetType());
	        m_cmemrepNext = next_;
	        m_parameters = (ParmList) parameters_.clone();
	        m_strAccessor = new String(accessor_);

  }
  
  /**
   * Purpose: Returns member's name
   * @return String
   */
  public String GetName() {return m_Name.GetName();}
  
  /**
   * Purpose: Sets the member's name
   * @param strName
   */
  public void SetName(String strName){m_Name.SetName(strName);}
  
  /**
   * Purpose: Returns the next ClassMemberRepresentation on the list
   * @return ClassMemberRepresentation
   */
  public ClassMemberRepresentation GetNext() { return m_cmemrepNext; }
  
  /**
   * Purpose: Returns the length of the list which starts from the current member
   * @return int
   */
  public int length() { return (m_cmemrepNext != null) ? m_cmemrepNext.length() + 1 : 1; }
  
  /**
   * Purpose: Sets the type for this member
   *
   */
  public void SetMemberType(Type t_) { m_ctType = t_; };
  
  /**
   * Purpose: returns the type for this member
   * @return Type
   */
  public Type GetMemberType() { return m_ctType; };
  
  /**
   * Purpose: Print all the member-functions on the list starting from the current one
   * @param c	the class name to which this member-function belongs
   *
   */
  public void print(ClassRepresentation c){
	  if(m_cmemrepNext != null) m_cmemrepNext.print(c);
	  if(m_ctType.GetType() == Type.Variable || m_ctType.GetType() == Type.Enum){
		  System.out.println(m_Type.GetName() + " " + m_Name.GetName() + ";");
      }
	  else{
		  System.out.println("/**");
		  System.out.print("* ");
		  if(m_Name.GetName().charAt(0) == '~')
			  	  System.out.println("finalize");
		  else
			 System.out.println(GetName());

		  System.out.print("#include " + c.GetName() + "__" + GetName());
		  if(m_Name.GetName().charAt(0) != '~'){
			  if(m_parameters!= null)
				  m_parameters.printTypes();
			  else
				  System.out.println("_void");

			  System.out.println();
			  System.out.println(m_strAccessor);
		  }
		  else System.out.println();

		  System.out.print("#include " + c.GetName() + "__" + GetName());
		  if(m_parameters!= null)
			  m_parameters.printTypes();
		  else
			  System.out.println("void");
		  System.out.println("__body");
	  }
  }	  
  
  
  /**
   * Purpose: Print all the member-functions on the list starting from the current one
   * @param c	the class name to which this member-function belongs
   * @param tos	 TextOutputStream where the member-function is printed to
   */
   public void print(ClassRepresentation c, TextOutputStream tos) throws IOException{
	  if(m_cmemrepNext != null) m_cmemrepNext.print(c, tos);
	  if(m_ctType.GetType() == Type.Variable || m_ctType.GetType() == Type.Enum){
		  tos.writeLine(m_Type.GetName() + " " + m_Name.GetName() + ";");
      }
	  else{
		  tos.writeLine("");
		  tos.writeLine("/**");
		  tos.writeBytes("* ");
		  if(m_Name.GetName().charAt(0) == '~')
			  	  tos.writeLine("finalize");
		  else
			 tos.writeLine(GetName());

		  tos.writeBytes("#include " + c.GetName() + "__" + GetName());
		  
		  if(m_Name.GetName().charAt(0) != '~'){
			  if(m_parameters!= null)
				  m_parameters.printTypes(tos);
			  else
				  tos.writeLine("_void");

			  tos.writeLine("");
			  tos.writeLine(m_strAccessor);
		  }
		  else tos.writeLine("");
		  
		  tos.writeBytes("#include " + c.GetName() + "__" + GetName());
		  if(m_parameters!= null)
			  m_parameters.printTypes(tos);
		  else
			  tos.writeLine("void");
		  tos.writeLine("__body");
	  }
  }	  
  
  
  /**
   * Purpose: sets the level of accessobility for this member-function 
   * @param strAccessor	 public, private, or protected
   */
  public void SetAccessor(String strAccessor_) { m_strAccessor =strAccessor_; };
  
  /**
   * Purpose: Returns the level of accessobility for this member-function
   * @return String
   */
  public String  GetAccessor() { return m_strAccessor; };


}


/**
 * Purpose: Encapsulates a  class representaton
 * @version 1.0 December 96
 * @author Ilya Tilevich
 * Note: This class was based on the original code written in C++ by Chris Laffra
 */
class SuperClassRepresentation extends SomethingWithAName {

  protected  SuperClassRepresentation m_next;  //self-referencing data-structure
  
  /**
   * Purpose: Constructor
   * @param strName the class's name
   * @param next_ the next SuperClassRepresentation on the list
   */
  public SuperClassRepresentation(String strName, SuperClassRepresentation next_){
     super(strName);
     m_next = next_;
  }

  /**
   * Purpose: Returns the next SuperClassRepresentation on the list
   *
   */
  SuperClassRepresentation GetNext() { return m_next; };

}


/** 
 * Purpose: Encapsulates C++ class
 * @version 1.0 December 96
 * @author Ilya Tilevich
 * Note: This class was based on the original code written in C++ by Chris Laffra
 */
class ClassRepresentation extends SomethingWithAName
{

	/**
	 * Purpose: Constructor
	 * @param strName	the class's name
	 * @param next_     the next class on the list
	 * @param bIsSuperClass	the class's place in the hierarchy
	 */
	public ClassRepresentation(String strName, ClassRepresentation next_,
							boolean bIsSuperClass) {
	        super(strName);
	        m_next = next_;
			m_iNumMembers = 0;
			m_SuperClassRep = null;
			m_bNested = false;
			m_bIsSuper = bIsSuperClass;

	}
 
	/**
	 * Purpose: Returns the number of members for this class
	 * @return int
	 */
	public int GetNumberOfMembers() { return m_iNumMembers; }
  
	/**
	 * Purpose: Returns nth member for this class
	 * @param the index of member to get
	 * @return ClassMemberRepresentation if found, null otherwise
	 */
	ClassMemberRepresentation GetMember(int n) { //get n th member
		ClassMemberRepresentation m = m_members;
		for ( ; m!=null && n>0; n--) m = m.GetNext();
		return (m);
    }
  
	/**
	 * Purpose: Sets the members for this class
	 * @param members_	
	 */
	void SetMembers(ClassMemberRepresentation members_) {
		m_members = members_;
		m_iNumMembers = (m_members != null) ? m_members.length() : 0;
	}
  
	/**
	 * Purpose: Sets a super class for this class
	 * @param super_  ClassRepresentation of the super class
	 */
	void addSuperClass(ClassRepresentation super_) {
		m_SuperClassRep = new SuperClassRepresentation(super_.m_strName, m_SuperClassRep);
	}
  
	/**
	 * Purpose: Returns member-functions for this class
	 * @return ClassMemberRepresentation
	 *
	 */
	public ClassMemberRepresentation GetMembers() { return m_members; }
  
	/**
	 * Purpose: Gets the next class on the list
	 *
	 */
	public ClassRepresentation GetNext() { return m_next; }
  
	
	public void SetNext(ClassRepresentation nxtClass) { m_next = nxtClass; }
	/**
	 * Purpose: Gets the super class for this class
	 * @return SuperClassRepresentation
	 *
	 */
	public SuperClassRepresentation GetSuper() { return m_SuperClassRep; };
  
	/**
	 * Purpose: Prints this class to Stdio
	 *
	 */
	public void print(){
		if(m_bIsSuper) return;

		System.out.println("");
		System.out.println("/*");
		System.out.println(" * class " + GetName());
		System.out.println(" * ");
		System.out.println(" * This code has been generated using C2J++");
		System.out.println(" * C2J++ is a Java version of C2J (Courtesy of Morgan Stanley & Co., Inc. and Chris Laffra)");
        System.out.println(" * Read general disclaimer distributed with C2J++ before using this code");
        System.out.println(" * For information about C2J++, send mail to Ilya_Tilevich@ibi.com");
 		System.out.println(" */");
		System.out.println("");
		
		if(m_bNested)
			System.out.print("private ");
		System.out.println("class " + GetName());

		if(m_SuperClassRep!=null && m_SuperClassRep.GetNext()!=null){
			System.out.println(" implements " + m_SuperClassRep.GetNext().GetName());
			if(m_SuperClassRep.GetNext().GetNext()!=null)
				for(SuperClassRepresentation s = m_SuperClassRep.GetNext().GetNext(); s!=null; s=s.GetNext()){
				    System.out.println("[ " + s.GetName() + " ]");
					if(s.GetNext()!= null && s.GetName()!= null)
						System.out.println(", ");
				}
	
		}
	  
  		System.out.println(" {");
		if(m_members!=null)
			m_members.print(this);
		System.out.println("#include  __globals");
		System.out.println("}");
	}

	/**
	 * Purpose: Prints this class to the TextOutputStream
	 * @param tos  the TextOutputStream to print to
	 * @param bIncludeGlobals	tells if global functions and variables have to 
	 * be included in this class
	 */
	public void print(TextOutputStream tos, boolean bIncludeGlobals) throws IOException{
		if(m_bIsSuper) return;

		tos.writeLine("");
		tos.writeLine("/*");
		tos.writeLine(" * class " + GetName());
		tos.writeLine(" * ");
		tos.writeLine(" * This code has been generated using C2J++");
		tos.writeLine(" * C2J++ is based on Chris Laffra's C2J (laffra@ms.com)");
		tos.writeLine(" * Read general disclaimer distributed with C2J++ before using this code");
            tos.writeLine(" * For information about C2J++, send mail to Ilya_Tilevich@ibi.com");
 		tos.writeLine(" */");
		tos.writeLine("");
		
		if(m_bNested)
			tos.writeBytes("private ");
		tos.writeBytes("class " + GetName());

		if(m_SuperClassRep!=null)
			tos.writeBytes(" extends " + m_SuperClassRep.GetName());
		if(m_SuperClassRep!=null && m_SuperClassRep.GetNext()!=null){
			tos.writeBytes(" implements " + m_SuperClassRep.GetNext().GetName());
			if(m_SuperClassRep.GetNext().GetNext()!=null)
				for(SuperClassRepresentation s = m_SuperClassRep.GetNext().GetNext(); s!=null; s=s.GetNext()){
				    tos.writeBytes(" [ " + s.GetName() + " ]");
					if(s.GetNext()!= null && s.GetName()!= null)
						tos.writeBytes(", ");
				}
	
		}
	  	tos.writeLine("");
  		tos.writeLine("{");
		if(m_members!=null)
			m_members.print(this, tos);
		if(bIncludeGlobals)
			tos.writeLine("#include  __globals");
		tos.writeLine("}");
	}

		
	/**
	 * Purpoee: Tells if this class is nested inside another class
	 * @return boolean
	 */
	
	public boolean isNested() { return m_bNested; }
  
	/**
	 * Purpose: Sets the nested attribute for this class
	 * @param nested_ 
	 */
	void SetNested(boolean nested_) { m_bNested = nested_; }


  
	protected ClassMemberRepresentation m_members;  //point to the first member
  
	protected int m_iNumMembers; //num of members
  
	protected boolean m_bIsSuper;
  
	protected boolean  m_bNested;
  
	protected ClassRepresentation m_next;  //self-referencing data-structure
  
	protected SuperClassRepresentation m_SuperClassRep;	 //parent
}


/**
 * Purpose:  Provides a convenient way to write a line of text to TextOutputStream
 * @version 1.0 December 96
 * @author Ilya Tilevich
 * Note: This class was based on the original code written in C++ by Chris Laffra
 */

class TextOutputStream extends DataOutputStream
{
   	/**
	 * Purpose: Constructor
	 * @param out	
	 */
	TextOutputStream(OutputStream  out){
		super(out);
	}

	/**
	 * Purpose: writes a String on a separate line
	 * @param str
	 */
	void writeLine(String str){
	   try{
		   writeBytes(str);
		   writeBytes("\r\n");
		   
	   }catch(IOException e){System.out.println(e);}
   }
}