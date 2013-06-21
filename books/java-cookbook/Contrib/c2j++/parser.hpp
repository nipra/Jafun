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

class SomethingWithAName {
public:
  SomethingWithAName(const char *name_) {
    _name = new char[strlen(name_)+1]; 
    strcpy(_name, name_);
  }
  ~SomethingWithAName() {
    delete _name;
  }
  void name(const char *name_) {
    delete _name;
    _name = new char[strlen(name_)+1]; 
    strcpy(_name, name_);
  }
  const char *name(void) const { return _name; }
    
protected:
  char *_name;
};

class SomethingWithAType {
public:
  SomethingWithAType(const char *type_) {
    _type = new char[strlen(type_)+1]; 
    strcpy(_type, type_);
  }
  ~SomethingWithAType() {
    delete _type;
  }
  void type(const char *type_) {
    delete _type;
    _type = new char[strlen(type_)+1]; 
    strcpy(_type, type_);
  }
  const char *type(void) const { return _type; }
    
protected:
  char *_type;
};

class ParmList : public SomethingWithAName, public SomethingWithAType 
{
public:
  ParmList(const char *type_, const char *name_, ParmList *next_) :
        SomethingWithAName(name_), SomethingWithAType(type_), _next(next_) { }
  ~ParmList() { }
  ParmList *next(void) { return _next; }
  int length() { return (_next) ? _next->length() + 1 : 1; }
  void Delete();
  void printNames(FILE*);
  void printAsParms(FILE*);
  void printTypes(FILE*);
  void printTypes(char *buf);
private:
  ParmList *_next;
};

class ClassRepresentation;


class ClassMemberRepresentation : public SomethingWithAName, 
				  public SomethingWithAType 
{
public:
  enum Type { Variable, Enum, Array, Method, InlinedMethod, 
			        Constructor, InlinedConstructor, 
			        Destructor, InlinedDestructor, 
				StaticMethod, StaticInlinedMethod };
  ClassMemberRepresentation(const char *type_, const char *name_,
			Type member_type_, ClassMemberRepresentation *next_,
			ParmList *parameters_,
			const char *accessor_) :
        SomethingWithAName(name_), SomethingWithAType(type_), 
	_type(member_type_), _next(next_), 
	_parameters(parameters_), _accessor(accessor_) { }
  ~ClassMemberRepresentation() { }
  ClassMemberRepresentation *next(void) { return _next; }
  int length() { return (_next) ? _next->length() + 1 : 1; }
  void member_type(Type t_) { _type = t_; };
  Type member_type() { return _type; };
  void print(ClassRepresentation *c);
  void accessor(const char *accessor_) { _accessor = accessor_; };
  const char *accessor() { return _accessor; };

protected:
  ClassMemberRepresentation *_next;
  Type _type;
  ParmList *_parameters;
  const char *_accessor;
};


class SuperClassRepresentation : public SomethingWithAName {
public:
  SuperClassRepresentation(const char *name_, SuperClassRepresentation *next_) 
  	: SomethingWithAName(name_), _next(next_) { }
  SuperClassRepresentation *next() { return _next; };
protected:
  SuperClassRepresentation* _next;
};


class ClassRepresentation : public SomethingWithAName 
{
public:
  ClassRepresentation(const char *name_, ClassRepresentation *next_,
							int is_super_class) :
	SomethingWithAName(name_), _next(next_), 
			_n_members(0), _super(0), 
			_nested(0), 
			_is_super(is_super_class) { }
  ~ClassRepresentation() { }
  int numberOfMembers(void) { return _n_members; }
  ClassMemberRepresentation *member(int n) {
    ClassMemberRepresentation *m;
    for (m = _members; m && n>0; n--) m = m->next();
    return (m);
  }
  void members(ClassMemberRepresentation *members_) {
    _members = members_;
    _n_members = _members ? _members->length() : 0;
  }
  void addSuperClass(ClassRepresentation *super_) {
    _super = new SuperClassRepresentation(super_->name(), _super);
  }
  ClassMemberRepresentation *members(void) { return _members; }
  ClassRepresentation *next(void) { return _next; }
  SuperClassRepresentation *super() { return _super; };
  void print();
  int nested(void) { return _nested; }
  void nested(int nested_) { _nested = nested_; }
    
protected:
  ClassMemberRepresentation *_members;
  int _n_members;
  int _is_super;
  int _nested;
  ClassRepresentation *_next;
  SuperClassRepresentation *_super;
};
