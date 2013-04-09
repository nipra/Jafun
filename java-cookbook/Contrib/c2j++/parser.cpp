/*
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
##############################################################################
*/

////////////////////////////////   See README file  //////////////////////////

#include <fstream.h>
#include <iostream.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdio.h>

#include "parser.hpp"

const char *PACKAGE= "SomePackage";	// change this to your package name
const char *AUTHOR=  "SomeBody";	// change this to your name
const char *COMPANY= "SomeCompany";	// change this to your company

int debug = 0;

char *PUBLIC = "public "; 
char *PRIVATE = "private "; 
char *PROTECTED = ""; 
char *accessor = PROTECTED;

int  putback = 0;		// see getChar()
char putback_char = 0;		// see getChar()
int  lineno  = 0;		// the current line number
int parse_methods = 0;		// program called with '-C' ?
FILE *fout;			// our output
FILE *statics;			// temporary file for storing global vars
char initializer[2048];		// constructor initialize list (long enough?)


// faster than strcmp(s1,s2):
const char *_t1, *_t2;
#define streq(s1, s2)  (_t1=s1,_t2=s2,_t1[0]==_t2[0] ? !strcmp(_t1+1,_t2+1) : 0)
#define strneq(s1, s2) (!(streq(s1, s2)))

char getChar()
{  
  if (putback) {
      putback = 0; 
      return putback_char;
  } 
  else if (!cin.eof()) {
    char c;
    {
      cin.unsetf(ios::skipws);
      cin >> c;
      if (c == '\r') return getChar();
      if ((int)c < 0 || cin.eof()) { return EOF; }
      if (c == '\n') lineno++;
    }
    if (fout != NULL) {
      fprintf(fout,"%c", c);
      if (c == '\n') fprintf(fout, "    ");
    }
    return c;
  } else {
    return EOF;
  }
}

void ungetChar(char c)
{
  putback = 1; 
  putback_char = c;
}

int isident(int c) { return isalnum(c) || c=='_'; }

void skipSpaces()
{
  char c;
  do c = getChar(); while (isspace(c)) ;
  ungetChar(c);
}

const int BUFSIZE = 1024;
const int MAXWORDSTACKSIZE = 32;
char wordStack[MAXWORDSTACKSIZE][BUFSIZE];
void initWordStack()
{
  for (int i=0; i<MAXWORDSTACKSIZE; i++) wordStack[i][0] = '\0';
}

void printStack(int depth)
{
  for (int n=depth; n>0; n--) 
    fprintf(stderr,"\t%d%s%s%s", n, "\t", wordStack[MAXWORDSTACKSIZE-n], "\n");
}

void getClassNameFromStack(char *class_name,  int depth)
{
  strcpy(class_name,  "");
  for (int n=depth; n<MAXWORDSTACKSIZE-depth-2; n+=3) {
      if (strneq(wordStack[MAXWORDSTACKSIZE-(n+1)], ":") ||
			strneq(wordStack[MAXWORDSTACKSIZE-(n+2)], ":")) 
	break;
  }
  if (debug) printStack(n);
  for (n; n>depth; n-=3)
      strcat(class_name,  wordStack[MAXWORDSTACKSIZE-n]);
  strcat(class_name,  wordStack[MAXWORDSTACKSIZE-depth]);
}

int getWord(char *buf_, int bufsize_)
{
  skipSpaces();

  for (int n=0; n<bufsize_ && !cin.eof(); n++) {
    if (n<bufsize_) buf_[n] = getChar();

    if (buf_[n] == '/') { 				// skip comments
      if (n<bufsize_) buf_[++n] = getChar();
      if (buf_[n] == '/') {				// the '//' form
        do {
          if (n<bufsize_) buf_[++n] = getChar();
        } while (n<bufsize_ && !cin.eof() && buf_[n]!='\n');
	return getWord(buf_, bufsize_);
      }
      else 
      if (buf_[n] == '*') {				// the '/*' form
	while (n<bufsize_ && !cin.eof()) {
          do {
            if (n<bufsize_) buf_[n] = getChar();
          } while (n<bufsize_ && !cin.eof() && buf_[n]!='*');
          if (n<bufsize_) buf_[n] = getChar();
	  if (buf_[n] == '/') break;
	}
	return getWord(buf_, bufsize_);
      }
      else {
        if (n>0) ungetChar(buf_[n--]); 
      }
    }
    if (buf_[n] == '#') { 				// skip preprocessor
      char c;
      do {
	c = getChar(); 
	if (c == '\\') { c = getChar(); c = getChar(); }
      } while (!cin.eof() && c!='\n');
      return getWord(buf_, bufsize_);
    }

    if (buf_[n] == '\'') { 				// character constants
      do {
        if (n<bufsize_) buf_[++n] = getChar();
	if (buf_[n] == '\\') {
          if (n<bufsize_) buf_[++n] = getChar();
          if (n<bufsize_) buf_[++n] = getChar();
 	}
      } while (n<bufsize_ && !cin.eof() && buf_[n]!='\'');
      buf_[n+1] = '\0';
      return n+1;
    }

    if (buf_[n] == '"') { 				// string constants
      do {
        if (n<bufsize_) buf_[++n] = getChar();
	if (buf_[n] == '\\') {
          if (n<bufsize_) buf_[++n] = getChar();
          if (n<bufsize_) buf_[++n] = getChar();
 	}
      } while (n<bufsize_ && !cin.eof() && buf_[n]!='"');
      buf_[n+1] = '\0';
      return n+1;
    }

    if (!isident(buf_[n])) break;			// end of token
  }
  if (n>0) ungetChar(buf_[n--]);
  buf_[n+1] = '\0';
  for (int i=0; i<MAXWORDSTACKSIZE-1; i++)
	strcpy(wordStack[i], wordStack[i+1]);
  strcpy(wordStack[i], buf_);
  return cin.eof() ? EOF : n+1;
}

int skipWord()
{
  static char buf_[BUFSIZE];
  return getWord(buf_, BUFSIZE);
}

int findWord(char *word_to_find)
{
  if (debug) cerr << "findWord(" << word_to_find << ")\n";
  char buf[256];
  do {
    if (getWord(buf, 256) == EOF) return 0;
  } while (strneq(word_to_find, buf));
  return 1;
}  

int findAndDoNotSkipWord(char *word_to_find, char *dont_skip)
{
  if (debug) cerr << "findAndDoNotSkipWord(" << word_to_find << 
					"," << dont_skip << ")\n";
  char buf[256];
  do {
    if (getWord(buf, 256) == EOF) return 0;
    if (debug) cerr << "getWord() -> \"" << buf << "\"" << ")\n";
    if (streq(buf, dont_skip)) return 0; 
  } while (strneq(word_to_find, buf));
  return 1;
}  

int skipMatching(char *word_to_skip, char *word_to_find)
{
  if (debug) cerr << "skipMatching(" << word_to_skip << 
					"," << word_to_find << ") {\n";
  char buf[256];
  do {
    if (getWord(buf, 256) == EOF) return 0;
    if (debug) cerr << "      " << buf << "\n";
    if (streq(word_to_skip, buf))
	skipMatching(word_to_skip, word_to_find);
  } while (strneq(word_to_find, buf));
  if (debug) cerr << "}" << "\n";
  return 1;
}  
char *MemberTypeNames[] = {
  "Variable", "Enum", "Array", "Method", "InlinedMethod", 
  "Constructor", "InlinedConstructor", 
  "Destructor", "InlinedDestructor", 
  "StaticMethod", "StaticInlinedMethod"
};

ParmList* readParmList();

int isStaticMethod(const char *class_name, const char *method_name);

ClassRepresentation *class_info = 0;

//////////////////////////////////////////////////////////////////////
//  PRINTING
//////////////////////////////////////////////////////////////////////

void ClassMemberRepresentation::print(ClassRepresentation *c)
{
  if (next()) next()->print(c);
  if (member_type() == ClassMemberRepresentation::Variable) {
    printf("    %s%s%s%s", type(), " ", name(), ";");
    printf("\n");
  }
  else 
  if (member_type() == ClassMemberRepresentation::Enum) {
    printf("    %s%s%s%s", type(), " ", name(), ";");
    printf("\n");
  }
  else {
    printf("\n");
    printf("    /commentstart**%s", "\n");
    printf("     * ");
    printf("%s%s", ((name()[0] == '~') ? "finalize" : name()), "\n");
    
    printf("#include \"%s%s%s", c->name(), "__", name());
    if (_parameters) 
      _parameters->printTypes(stdout);
    else
      printf("_void");
    printf("\"%s", "\n");

    printf("    %s%s", accessor(), "\n");

    printf("#include \"%s%s%s", c->name(), "__", name());
    if (_parameters) 
      _parameters->printTypes(stdout);
    else
      printf("_void");
    printf("__body\"%s", "\n");
  }
}

void ClassRepresentation::print() { 
  if (_is_super) return;

  printf("\n");
  printf("/commentstart**\n");
  printf(" * class %s%s", name(), "\n");
  printf(" * \n");
  printf(" * This code has been generated using c2j.\n");
  printf(" * Courtesy of Morgan Stanley & Co., Inc.\n");
  printf(" * Read general disclaimer distributed with c2j before");
  printf(" using this code.\n");
  printf(" * For information about c2j, send mail to laffra@ms.com\n");
  printf(" * \n");
  printf(" * Copyright 1995/1996, %s%s" , COMPANY,  " All rights reserved\n");
  printf(" * @author %s%s", AUTHOR, "\n");
  printf(" *commentend/\n");

  if (!nested())
      printf("public ");
  printf("class %s%s", name(), " ");

  if (super())
      printf("extends %s", super()->name());
  if (super() && super()->next()) {
      printf(" implements %s", super()->next()->name());
      if (super()->next()->next()) 
	  for (SuperClassRepresentation *s = super()->next()->next(); s; 
							s = s->next()) {
	    printf("[%s%s", s->name(), "]");
	    if (s->next() && s->name())
	      printf(", ");
	  }
  }

  printf(" {\n");
  if (members()) members()->print(this); 
  printf("\n");
  printf("#include \"__statics\"\n");
  printf("};\n");
}


//////////////////////////////////////////////////////////////////////

void readClassBody(char *class_name);
  
ClassRepresentation *
findClass(const char *class_name, int is_super_class)
{
  static ClassRepresentation *c_cache = 0;
  static char cache_class_name[256];
  if (c_cache && streq(class_name, cache_class_name)) return c_cache;
  for (ClassRepresentation *c = class_info; c; c = c->next()) 
    if (streq(c->name(), class_name)) {
      c_cache = c;
      strcpy(cache_class_name, class_name);
      return c;
    }
  c = new ClassRepresentation(class_name, class_info, is_super_class);
  class_info = c; 
  return c;
}

void setSuperClass(const char *class_name_, const char *super_class_name_)
{
  if (streq(super_class_name_, ",")) return;
  if (debug) cerr << "setSuperClasses(" << class_name_ << ", "
					  << super_class_name_ << ")\n";
  ClassRepresentation *c1 = findClass(class_name_, 0);
  ClassRepresentation *c2 = findClass(super_class_name_, 1);
  c1->addSuperClass(c2);
}

int readSuperClasses(const char *class_name_)
{
  if (debug) cerr << "readSuperClasses(" << class_name_ << ")\n";
  char buf[256];
  while (1) {
    if (getWord(buf, 256) == EOF) return 0;
    if (debug) cerr << "getWord() -> \"" << buf << "\"" << ")\n";
    if (streq(buf,":"))    	continue;
    if (streq(buf,"public"))    continue;
    if (streq(buf,"private"))   continue;
    if (streq(buf,"protected")) continue;
    if (streq(buf, "{")) break;
    if (streq(buf, ";")) return 0; 
    else setSuperClass(class_name_, buf);
  }
  return 1;
}

void readMemberBody(char *class_name_, char *member_name_, char *type_,
		ParmList **parameters_) 
{
  char file_name[BUFSIZE];

  strcpy(file_name, "__c2j_java__/");
  strcat(file_name, class_name_);
  strcat(file_name, "__");
  strcat(file_name, member_name_);
  if (*parameters_) 
    (*parameters_)->printTypes(file_name); 

  fout = fopen(file_name,"w");	// in VisualC++ ofstream did not work!
  if (*parameters_) 
    (*parameters_)->printAsParms(fout);
  if (strneq(type_, "void") && 
		strneq(type_,":") &&
		strneq(type_,";") &&
		strneq(type_,"}") &&
					strcmp(class_name_, member_name_))
    fprintf(fout,"     * @return %s\n", type_);
  fprintf(fout,"     *commentend/\n");
  fclose(fout);

  strcat(file_name, "__body");
  fout = fopen(file_name, "w");
  if (strneq(type_, ":") && strneq(type_,";") && strneq(type_,"}"))
    fprintf(fout, "    %s ", type_);

  if (member_name_[0] == '~')
    fprintf(fout,  "void finalize() {");
  else {
    fprintf(fout,  "%s(", member_name_);
    if (*parameters_) 
      (*parameters_)->printNames(fout);
    fprintf(fout, ") {");
  }

  fprintf(fout, " %s\n", initializer);
  strcpy(initializer,"");

  skipMatching("{", "}"); 

  fclose(fout);
}

int readMember(char *class_name_, 
		ClassMemberRepresentation::Type *type,
		char *name_, char *type_, int bufsize_,
		ParmList **parameters_) 
{
  static int comma_seen = 0;

  *parameters_ = 0;

  if (debug) cerr << "readMember(" << class_name_ << 
					"," << name_ << 
					"," << type_ << ")\n";
  char separator[BUFSIZE];
  int is_static = 0;
  name_[0] = '\0';
  static int inside_enum = 0;

  if (comma_seen) {
      if (debug) cerr << "comma seen, continue with type " << type_ << "\n";
  }
  else {
      if (getWord(type_, bufsize_) ==  EOF) return 0;
      if (debug) cerr << "first word=" << type_ << "\n";
  }

  if (streq(type_,"public"))    { 
    accessor = PUBLIC;
    skipWord(); return 1; 
  } 
  else
  if (streq(type_,"private"))   { 
    accessor = PRIVATE;
    skipWord(); return 1; 
  } 
  else
  if (streq(type_,"protected")) { 
    accessor = PROTECTED; 
    skipWord(); return 1; 
  } 
  else
  if (streq(type_,"enum")) {
    if (debug) cerr << "enum " << type_ << "\n";
    if (!findAndDoNotSkipWord("{", ";")) 
      return 0;
    if (getWord(type_, bufsize_) ==  EOF) return 0;
    inside_enum = 1;
  }
  if (inside_enum) {
    if (streq(type_, "}")) {	// matching {
      inside_enum = 0;
      return 1;
    }
    strcpy(name_, type_);
    if (getWord(separator, bufsize_) ==  EOF) return 0;
    if (streq(separator, "=")) {
      if (getWord(separator, bufsize_) ==  EOF) return 0;
      strcat(name_, " = ");
      strcat(name_, separator);
      if (getWord(separator, bufsize_) ==  EOF) return 0;
    }
    if (streq(separator, "}")) {	// matching {
      inside_enum = 0;
    }
    strcpy(type_, "static final int");
    if (debug) cerr << "enum " << name_ << "\n";
    *type = ClassMemberRepresentation::Variable;
    return 1;
  }

  if (streq(type_,"{")) { skipMatching(type_, "}"); return 1; }

  // missing { ?
  if (streq(type_,"}")) { ungetChar(type_[0]); comma_seen=0; return 0; } 

  if (streq(type_,";")) return 1;

  if (streq(type_,"class")) {
    char name[256];
    char nested_class_name[256];
    if (getWord(nested_class_name, 256) == EOF) return 1;
    if (debug) cerr << "nested_class_name=" << nested_class_name << "\n";
    if (readSuperClasses(name)) {
      strcpy(name, class_name_);
      strcat(name, nested_class_name);
      readClassBody(name);
      findClass(name,0)->nested(1);
      skipMatching("{", "}"); 
      if (getWord(separator, bufsize_) == EOF) return 0; 
      return 1;
    }
  }
  if (getWord(name_, bufsize_) ==  EOF) return 0;
  if (debug) cerr << "name=" << name_ << "\n";

  if (streq(name_,"(")) { 
    strcpy(name_, type_);

    if (debug) cerr << "before constructor " << "\n";
    *parameters_ = readParmList();

    if (findAndDoNotSkipWord("{", ";")) {
      if (streq(name_, class_name_)) {
        *type = ClassMemberRepresentation::InlinedConstructor;
	readMemberBody(name_, name_, "", parameters_); 
      }
      else {
	readMemberBody(name_, name_, "", parameters_); 
        return 1;
      }
    }
    else
      *type = ClassMemberRepresentation::Constructor;

    if (debug) cerr << "found constructor " << "\n";
    return 1;
  }


  if (streq(type_,"~")) { 
    strcat(type_, name_);
    strcpy(name_, type_);
    strcpy(type_, "");
    *parameters_ = readParmList();
    if (findAndDoNotSkipWord("{", ";")) {
      *type = ClassMemberRepresentation::InlinedDestructor;
      readMemberBody(name_+1, name_, "", parameters_); 
    }
    else
      *type = ClassMemberRepresentation::Destructor;

    if (debug) cerr << "found destructor " << "\n";
    return 1;
  }
  if (streq(name_,"{")) { skipMatching("{", "}"); return 1; }
  if (streq(name_,"}")) { ungetChar('}'); comma_seen=0; return 0; }
  if (streq(name_,";")) { ungetChar(';'); return 1; }

  *type = ClassMemberRepresentation::Variable;
  for (;;) {
    if (streq(type_,"static")) is_static = 1;
    if (getWord(separator, bufsize_) == EOF) return 0; 
    if (debug) cerr << "separator=" << separator << "\n";

    if (streq(separator,"}")) { ungetChar('}'); comma_seen=0; return 0; }
    if (streq(separator,"{")) {	// }
      skipMatching("{", "}"); 
    }
    if (streq(separator,"[")) {
      skipMatching("[", "]"); 
      *type = ClassMemberRepresentation::Array;
      break; 
    }
    if (streq(separator,"(")) { 		// read member parameters

      if (debug) cerr << "seen start of function" << "\n";
      if (debug) cerr << "before readParmList " << "\n";
      *parameters_ = readParmList();
      if (debug)
	cerr << "after readParmList " << endl << "found a method" << "\n";

      if (findAndDoNotSkipWord("{", ";")) {	// }
        if (streq(name_,"="))      is_static = 1;
        if (streq(name_,"!="))     is_static = 1;
        if (streq(name_,"=="))     is_static = 1;
        if (streq(name_,"new"))    is_static = 1;
        if (streq(name_,"delete")) is_static = 1;

        readMemberBody(class_name_, name_, type_, parameters_); 

        *type = (is_static) ? ClassMemberRepresentation::StaticInlinedMethod
			    : ClassMemberRepresentation::InlinedMethod;
      }
      else {
        *type = (is_static) ? ClassMemberRepresentation::StaticMethod
			    : ClassMemberRepresentation::Method;
      }
      if (debug) cerr << "end of function" << "\n";
      break; 
    }	
    if (streq(separator,";")) break;
    if (streq(separator,",")) {
        comma_seen = 1;
	return 1;
    }

    else if (streq(name_,"*")) ; //strcat(type_, name_);
    else if (streq(name_,"&")) ; //strcat(type_, name_);
    else strncpy(type_, name_, bufsize_);
    strncpy(name_, separator, bufsize_);
  }
  if (streq(type_,":")) strcpy(type_, "");
  return 1;
}

ClassMemberRepresentation *
findMember(const char *class_name, const char *method_name)
{
  ClassRepresentation *c = findClass(class_name, 0);
  if (c) 
    for (ClassMemberRepresentation *m = c->members(); m; m = m->next())
      if (streq(m->name(), method_name)) return m;
  return 0;
}

int isStaticMethod(const char *class_name, const char *method_name)
{
  if (streq(method_name,"="))      return 1;
  if (streq(method_name,"!="))     return 1;
  if (streq(method_name,"=="))     return 1;
  if (streq(method_name,"new"))    return 1;
  if (streq(method_name,"delete")) return 1;
  ClassMemberRepresentation *m = findMember(class_name, method_name);
  if (!m) {
    return 1;
  }
  return (m->member_type()==ClassMemberRepresentation::StaticMethod ||
          m->member_type()==ClassMemberRepresentation::StaticInlinedMethod);
}

int is_a_valid_name(char *name)
{
  return (name && name[0] && name[0]!=';');
}

void readClassBody(char *class_name)
{
  if (debug) cerr << "readClassBody " << class_name << "\n";

  ClassRepresentation *c = findClass(class_name, 0);
  char name[256];
  char type[256];
  ClassMemberRepresentation::Type member_type;
  ClassMemberRepresentation *mdouble = 0;
  ClassMemberRepresentation *m = 0;
  ParmList *parms;
  while (readMember(class_name, &member_type, name, type, 256, &parms))
    if (is_a_valid_name(name)) {
      m = new ClassMemberRepresentation(type, name, 
				member_type, m, parms, accessor);
      c->members(m); 
      if (m->member_type()==ClassMemberRepresentation::Method)
        for (mdouble = c->members(); mdouble; mdouble = mdouble->next())
          if ((m->member_type()==ClassMemberRepresentation::StaticMethod ||
               m->member_type()==ClassMemberRepresentation::Method) &&
              streq(m->name(), mdouble->name()) &&
              streq(m->type(), mdouble->type())) {
	    // cannot cope with overloaded functions yet.
	    m->member_type(ClassMemberRepresentation::StaticMethod);
	  }
    }
}

void ParmList::Delete()
{
  if (next()) next()->Delete();
  delete this;
}

void ParmList::printNames(FILE *fout)
{
  if (!name()[0] || streq(name(), "void")) return;
  if (next() && next()->type()[0]) {
    next()->printNames(fout);
  }
  if (next() && next()->type()[0])
    fprintf(fout, ", ");
  if (strneq(name(),"0"))
    fprintf(fout, "%s %s", type(), name());
}

void ParmList::printAsParms(FILE *fout)
{
  if (!name()[0] || streq(name(), "void")) return;
  if (next() && next()->type()[0]) {
    next()->printAsParms(fout);
  }
  if (strneq(name(),"0"))
    fprintf(fout, "     * @param %s\n", name());
}

void ParmList::printTypes(char *buf)
{
  if (!type()[0]) return;
  if (next())
    next()->printTypes(buf);
  strcat(buf, "_");
  if (streq(type(),"unsigned"))
    strcat(buf, "int");
  else
    strcat(buf, type());
}

void ParmList::printTypes(FILE *fout)
{
  if (!type()[0]) return;
  if (next())
    next()->printTypes(fout);
  fprintf(fout, "_");
  if (streq(type(),"unsigned"))
    fprintf(fout, "int");
  else
    fprintf(fout, "%s", type());
}

ParmList* readParmList()
{
  if (debug) cerr << "enter readParmList()" << "\n";

  char token[256];
  ParmList *list = new ParmList("","",(ParmList*)0);
  char *type;
  char *name;
  do {
    if (getWord(token, 256) == EOF) return 0;

    if (streq(token, ",") || streq(token, ")")) {
      type = wordStack[MAXWORDSTACKSIZE-3];
      name = wordStack[MAXWORDSTACKSIZE-2];

      if (streq(type,",") || streq(type,"(")) {
          type = wordStack[MAXWORDSTACKSIZE-2];
          name = "";
      }

      if (streq(type,":")) {
          type = wordStack[MAXWORDSTACKSIZE-2];
          name = "";
      }

      if (streq(type,"*")) {
          type = wordStack[MAXWORDSTACKSIZE-4];
          name = wordStack[MAXWORDSTACKSIZE-2];
      }

      if (streq(type,"&")) {
          type = wordStack[MAXWORDSTACKSIZE-4];
          name = wordStack[MAXWORDSTACKSIZE-2];
      }

      if (streq(type,"=")) {
          type = wordStack[MAXWORDSTACKSIZE-5];
          name = wordStack[MAXWORDSTACKSIZE-4];
      }

      if (streq(name, "&")) 
	strcpy(name, "dummy_");
      if (streq(name, "*")) 
	strcpy(name, "dummy_");

      if (streq("unsigned", wordStack[MAXWORDSTACKSIZE-4]) &&
			(streq(type, "long") || streq(type, "short") ||
			 streq(type, "int") || streq(type, "char"))) {
        type = wordStack[MAXWORDSTACKSIZE-4];
	strcat(type, "_");
	strcat(type, wordStack[MAXWORDSTACKSIZE-3]);
      }

      if (streq(type, "unsigned") && 
			(streq(name, "long") || streq(name, "short") ||
			 streq(name, "int") || streq(name, "char"))) {
	strcat(type, "_");
	strcat(type, name);
	strcpy(name, "");
        list = new ParmList(type, "", list);
      }
      else
      if (streq(name, "void")) 
        list = new ParmList(name, "", list);
      else
      if (isident(name[0]))
        list = new ParmList(type, name, list);
      else
        if (name[0] == '(') 
          list = new ParmList("", "", list);
	else
          list = new ParmList(type, "0", list);
    }
  } while (!streq(token, "{") && !streq(token, ";") && !streq(token, ")"));

  if (debug) { 
    cerr << "\ttype = \"" << type << "\"" ; 
    cerr << "\tname = \"" << name << "\"" << "\n"; 
  }

  if (streq(token, ")")) {
    if (getWord(token, 256) == EOF) return 0;
    if (streq(token, "const")) {
      if (getWord(token, 256) == EOF) return 0;
    }
  }

  //
  // read initializer list
  //
  if (streq(token, ":")) {
    if (getWord(token, 256) == EOF) return 0;
    strcpy(initializer, "\n\t");
    int superConstructorCall = 0;
    while (!streq(token, "{") && !streq(token, ";")) {
      if (token[0]>='A'&&token[0]<='Z') {
          strcat(initializer, "super");
          superConstructorCall = 1;
      }
      else
      if (superConstructorCall) {
          if (streq(token, "(")) strcat(initializer, "(");
          else if (streq(token, ")")) {
             strcat(initializer, ");\n\t");
             superConstructorCall = 1;
          }
          else if (streq(token, ",")) strcat(initializer, ",");
          else strcat(initializer, token);
      }
      else {
          if (streq(token, "(")) strcat(initializer, "=");
          else if (streq(token, ")")) strcat(initializer, ";\n\t");
          else if (streq(token, ",")) strcat(initializer, "");
          else strcat(initializer, token);
      }
      if (getWord(token, 256) == EOF) return 0;
    }
  }

  ungetChar(token[0]);
  if (debug) cerr << "leave readParmList()" << "\n";
  return list;
}

int readClass()
{
  if (debug) cerr << "readClass() {" << "\n";
  char token[256];
  char class_name[256];
  for (;;) {
    if (getWord(token, 256) == EOF) return 0;

    if (streq(token, "class")) {	// start of class definition
      if (getWord(class_name, 256) == EOF) return 0;
      if (readSuperClasses(class_name)) {

	readClassBody(class_name);	// will read unto }
        if (debug) cerr << "}" << "\n";
	return 1;
      }
      continue;
    }
    
    char type[256];
    char class_name[256];
    char method_name[256];
    method_name[0] = '\0';
    class_name[0] = '\0';
    if (streq(token, "(")) {		// beginning of formal parameter list?

      if (streq(wordStack[MAXWORDSTACKSIZE-3], ":") &&
          streq(wordStack[MAXWORDSTACKSIZE-4], ":")) {
        getClassNameFromStack(class_name,  5);
        strcpy(method_name, wordStack[MAXWORDSTACKSIZE-2]);
        strcpy(type, wordStack[MAXWORDSTACKSIZE-6]);
	if (streq(type,"*") || streq(type,"&"))
          strcpy(type, wordStack[MAXWORDSTACKSIZE-7]);
      }
      else 
      if (streq(wordStack[MAXWORDSTACKSIZE-3], "~") &&
          streq(wordStack[MAXWORDSTACKSIZE-4], ":") &&
          streq(wordStack[MAXWORDSTACKSIZE-5], ":")) {
        getClassNameFromStack(class_name,  6);
        strcpy(type, wordStack[MAXWORDSTACKSIZE-7]);
	if (streq(type,"*") || streq(type,"&"))
          strcpy(type, wordStack[MAXWORDSTACKSIZE-8]);
        strcpy(method_name, "~");
        strcat(method_name, wordStack[MAXWORDSTACKSIZE-2]);
      }

      if (class_name[0]) {
	//
	// start of a class method 
	//

        if (debug) cerr << "start of class method" << "\n";
        ParmList *p = readParmList();

	char token[128];
        if (getWord(token, 128) == EOF) return 0;
        
        if (p && streq(token,"{")) {	// found a body
	  if (parse_methods) {
	    readMemberBody(class_name, method_name, type, &p); 
	    if (getWord(token, 128) == EOF) return 0;
	  }
	  else 
            skipMatching("{", "}"); // look for }
        }
        if (debug) cerr << "end of class method" << "\n";
      }
      else {
	//
	// start of a global function 
	//
        if (debug) cerr << "start of global function" << "\n";
        strcpy(method_name, wordStack[MAXWORDSTACKSIZE-2]);
        ParmList *p = readParmList();
	char token[128];
        if (getWord(token, 128) == EOF) return 0;
        if (streq(token,"{")) {	// found a body
	  if (p) {
	    p->printNames(stdout);
	    p->Delete();
	  }
	  skipMatching("{", "}"); // look for }
	}
        else
	  if (getWord(token, 256) == EOF) return 0;
      }
    }
    if (parse_methods && streq(token, ";")) {		// end of a declaration
      if (debug) cerr << "start of static declaration" << "\n";
      if (debug) printStack(10);
      for (int i=0; i<MAXWORDSTACKSIZE; i++) {
        if (streq(wordStack[MAXWORDSTACKSIZE-i], "static")) {
	  fprintf(statics, "    final ");
          for (int j=i; j>=0; j--) {
	    fprintf(statics, "%s ", wordStack[MAXWORDSTACKSIZE-j] );
	    if (debug) cerr << wordStack[MAXWORDSTACKSIZE-j] << " ";
	  }
	  fprintf(statics,"\n");
	  if (debug) cerr << "\n";
	}
      }
      if (debug) cerr << "end of declaration" << "\n";
    }
  }
  if (debug) cerr << "}" << "\n";
  return 0;
}


int main(int argc,char **argv)
{
  if (argc == 2) {
    if (argv[1][0] == '-' && argv[1][1] == 'C')
       parse_methods = 1;
  } 
  else if (argc == 1) { } 
  else { 
    cerr << "usage: parser <filename>" << "\n"; 
    return 1; 
  }

  if (parse_methods) 
    statics = fopen("__c2j_java__/__statics", "w");
  else 
    printf("package %s %s", PACKAGE , ";\n");

  while (readClass()) ;

  if (!parse_methods)
    for (ClassRepresentation *c = class_info; c; c = c->next())
      c->print();
  return 0;
}
