##############################################################################
#
#  c2j          96/04/04   Chris Laffra
#
#  Copyright (c) 1995-1996 Morgan Stanley & Co., Inc. All Rights Reserved.
#
#  Permission to use, copy, modify, and distribute this software
#  and its documentation for NON-COMMERCIAL purposes and without
#  fee is hereby granted provided that this copyright notice
#  appears in all copies. Please contact email: laffra@ms.com
#  for further copyright and licensing information.
#
#  MORGAN STANLEY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY
#  OF THIS SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
#  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
#  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. MORGAN STANLEY SHALL NOT BE LIABLE
#  FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
#  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
#
#  Please refer to the README file.
#
##############################################################################
#
# tested with Lucid (SunOS C++ compiler: lcc) and VisualC++2.0 (cl) only, 
# should work with any other though...
#
##############################################################################

parser: parser.cpp				# uses Lucid
	lcc -o parser parser.cpp
	c2j parser

parser.exe: parser.cpp				# uses Visual C++
	cl -o parser.exe parser.cpp
	c2j parser

clean:
	rm -rf __c2j_java__ c2j.zip parser parser.java
