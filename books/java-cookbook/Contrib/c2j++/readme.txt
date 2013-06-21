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

C2J++ is a C++ to Java translator that translates C++ code to Java code.

C2J++ is based on Chris Laffras C2J (laffra@ms.com)
C2J is courtesy of Morgan Stanley & Co., Inc.
Read general disclaimer distributed with C2J++ before using this code

For information about C2J++, send mail to Ilya_Tilevich@ibi.com

The input must consist of:
   - one .H file, containing one or more C++ class definitions.
   - one .C file, containing all method bodies referenced in the .H file.


***************************************************************************** 
*  In order to use C2J++, copy .H and .CPP files that you want to translate *
* in  the directory where you keep C2J++.                                   *  
* Then type:                                                                * 
*        java C2J yourHeaderFile.h yourCppFile.cpp                          * 
* Or:                                                                       *
*	Java C2J  Source.h  Source.cpp -DefnOnly                            *
*                                                                           *
* In the latter case, C2J++ will generate a Java file which will consist of *
* class  definitions, instance variables, and member method headers without *
* implementation                                                            * 
*                                                                           *
***************************************************************************** 

Different problems that are tackled by C2J++:

   - C++ can have a header file with a class declaration and a member
     function given in another file (typically a .C or .cpp file),
     Java has all the methods "inlined" in the class definition. 

   - Data types are incompatible (unsigned versus int, for instance)

   - Pointer references (->) need to be translated into refs (.)

   - Access (public,protected,private) is done by region in C++, and
     is per method in Java.

   - Print statements to cerr and cout, which are to be translated into 
     calls to System.out.print, and System.err.print.

   - Statements with "delete object;" need to be translated into
     "object = null;" to render a similar effect. Note that these 
     assignments are not necessary at all in a destructor, as we will
     lose the reference to the object automatically.
    
   - C++ has multiple inheritance. Java has single inheritance and
     interfaces. In the case of multiple inheritance, we need to make
     a decision.

Still to be done:

   - Variable declarations on the stack are not recognized and handled.

   - Overloaded operators in the code. 

   - Templates.

Note:   
     All global variables and functions  are collected and put into the first class found
     in the .H file. If you have a lot of global data, I would suggest putting 
     an empty class definition at the beginning of your header file to force C2J++ 
     to put all the globals in that class. 

      

For further information about C2J++, send mail to Ilya_Tilevich@ibi.com
