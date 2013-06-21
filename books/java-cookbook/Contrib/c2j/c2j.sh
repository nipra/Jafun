#!/bin/sh
############################################################################## 
# 
#  c2j        	96/04/04   Chris Laffra
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
# This shell script requires: 
# 
#    dirname, sed, and cc (were only using the C pre-processor)
# 
# Alex Epshteyn <infolink@panix.com>, gave advise for the Win32/MKS version.
# 
############################################################################## 

BASEDIR=`dirname $0`

usage() {
cat <<EOF

  usage: c2j classname

  make sure that file <classname>.[H/h/hpp] and <classname>.[C/c/cpp] exist 
  in this directory.  The header file should contain the class declaration 
  of the classes you want to translate. Furthermore, all method bodies 
  should be listed in either the header and/or the other file.

EOF
}

if test $# -eq 0
then
    usage
fi
if [ ! -d __c2j_java__ ]
then
    mkdir __c2j_java__
fi

for i do
    rm -f __c2j_java__/*
    h=$i.H
    if test -f $h; then :
    else
	h=$i.h
    fi
    if test -f $h; then :
    else
	h=$i.hpp
    fi
    if test -f $h
    then 
      echo ""
      echo "c2j, Version 1.0a, 96/01/25"
      echo "Copyright (c) 1995-1996 Morgan Stanley & Co., Inc."
      echo "All Rights Reserved. "

      echo ""
      echo "This software comes with a disclaimer."
      echo -n "Did you read the disclaimer and can you accept it? (y/n) "
      read x
      case $x in y*) ;; *)  exit 1; esac

      c=$i.C
      if test -f $c; then :
      else
	  c=$i.c
      fi
      if test -f $c; then :
      else
	  c=$i.cpp
      fi

      echo ""
      echo "parsing files \"$h\" and \"$c\""
      echo ""

      if [ ! -f $BASEDIR/parser ]
      then
	  echo 
	  echo "ERROR!!!!"
	  echo 
	  echo parser not found.
	  echo First compile parser.C with any C++ compiler you can find.
	  echo Exiting.
	  echo 
	  exit 0
      fi

      #
      # the first pass will run a dangerous sed script, as it translate
      # all occurrences of "unsigned" into "int" for instance, independent
      # of the location. This is a quick hack, should integrate into parser.
      #
      # pass one will insert #include statements for each method declared
      # in the java class. Each inlined C++ method found in the .H file
      # is saved in the __c2j_java__ directory.
      #
      sed -f $BASEDIR/sedin0 $h | $BASEDIR/parser  > __c2j_java__/$i.j

      #
      # pass two will detect each C++ method in the .C file, and save it
      # in the __c2j_java__ directory.
      #
      sed -f $BASEDIR/sedin0 $h | $BASEDIR/parser -C > /dev/null
      sed -f $BASEDIR/sedin0 $c | $BASEDIR/parser -C > /dev/null

      #
      # the C preprocessor (-E) will resolve all the #includes and
      # finally weave everything together.
      # May generate errors like "cannot find include file ...."
      #
      # sedin1 contains some extra rules for indenting the body of the
      # member functions, for instance.
      # 
      cc -E __c2j_java__/$i.j 2>__c2j_java__/cc.out | \
				sed -f $BASEDIR/sedin1 > $i.java 

      sed -f $BASEDIR/sedin2 __c2j_java__/cc.out

      echo
      echo "c2j: Informational: created file" $i.java
      echo
    else
      usage
      exit
    fi
done

