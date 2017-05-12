// SymbolTable.java

import java.util.*;

enum Category {CLASS, FUNCTION, VARIABLE}

class Type { }

// IntegerType represents the type of integer variables in Tiny C++.

class IntegerType extends Type {

  public String toString () {
    return "int";
  }

}

// ArrayType represents array types in TinyC++.

class ArrayType extends Type {

  private int dimension;
  private Type baseType;

  public ArrayType (int dim, Type base) {
    dimension = dim;
    baseType = base;
  }

  public int dimension () { return dimension; }
  public Type baseType() { return baseType;}

  public String toString () {
    return baseType.toString();
  }

}

// ObjectType represents object types in TinyC++.

class ObjectType extends Type {

  private String className;
  private SymbolTableEntry objectType;

  public ObjectType (String className, SymbolTableEntry objectType) {
    this . className = className;
    this . objectType = objectType;
  }

  public String className () { return className; }

  public SymbolTableEntry objectType () { return objectType; }

  public String toString () {
    return className;
  }

}

// Declarator is a class which defines an identifier and its type.

class Declarator {

  private String id;
  private Type type;
  private boolean isArray;
  public Declarator (String id, Type type) {
    this . id = id;
    this . type = type;
    this . isArray = false;
  }
  public Declarator (String id, Type type, boolean isOfTypeArray) {
    this . id = id;
    this . type = type;
    this . isArray = isOfTypeArray;
  }

  public boolean isArray(){ return isArray; }
  public String id () { return id; }

  public Type type () { return type; }

}

// SymbolTableEntry is a class to represent the symbol table entries
// for TinyC++ programs.

class SymbolTableEntry {

  private Category category;
  private Type type;
  private SymbolTable localEnv;
  private String funcCode;
  public boolean isParam = false;
  public SymbolTableEntry (Category cat) { 
    category = cat;
  }

  public SymbolTableEntry (Category cat, Type typ) {
    category = cat;
    type = typ;
  }

  public SymbolTableEntry (Category cat, SymbolTable env) {
    category = cat;
    localEnv = env;
  }

  public SymbolTableEntry (Category cat, Type typ, SymbolTable env) {
    category = cat;
    type = typ;
    localEnv = env;
  }
  public SymbolTableEntry (Category cat, Type typ, boolean isParameter) {
    category = cat;
    type = typ;
    isParam = isParameter;
  }

  public Category category () { return category; }

  public Type type () { return type; }

  public SymbolTable localEnv () { return localEnv; }

  public String funcCode () { return funcCode; }

  public void setCode (String code) { funcCode = code; }

  public String toString () {
    String printString = "";

    if (category == Category . VARIABLE || category == Category . FUNCTION)
      printString = printString + " " + type;
    return printString;
  }

}
 
// SymbolTable is a class to represent the symbol table for TinyC++ programs.

public class SymbolTable {

  private static int maxlen = 2; // for header "Id"

  public static int maxLen () { return maxlen; }

  private SymbolTable parent;				// static parent

  private String intermediateCode = "";

  public int numberOfTemps = 1;

  public int currentLabel = 0;

  public int numberOfParams = 0;

  private TreeMap <String, SymbolTableEntry> table; 	// id table

  public List<Expression> paramList ;

  public SymbolTable (SymbolTable staticParent) { 
    parent = staticParent;
    table = new TreeMap <String, SymbolTableEntry> (); 
    paramList = new ArrayList<Expression>();
  }

  public SymbolTable parent () { return parent; }

  // The entry function returns the symbol table entry for the id, including
  // if the id appears in a parent's symbol table. If the id has not been 
  // declared, it prints an appropriate error message.

  public SymbolTableEntry entry (String id) {
    SymbolTableEntry idEntry = table . get (id);
    if (idEntry == null)
      if (parent == null)
        ErrorMessage . print ("Undeclared identifier: " + id);
      else				// not found in this scope
        idEntry = parent . entry (id); 	// check parent
    return idEntry;
  }

  // The enter function enters an id and its information into the symbol
  // table.

  public void enter (String id, SymbolTableEntry entry) {
    SymbolTableEntry idEntry = table . get (id);
    if (idEntry != null)
      ErrorMessage . print ("Identifier " + id + " already declared.");
    table . put (id, entry);
    if (id . length () > maxlen)
      maxlen = id . length ();
  }

  // The enterVar function enters a variable id into the symbol table.
 
  public void enterVar (String id, Type type, boolean isParam) { 
    enter (id, new SymbolTableEntry (Category . VARIABLE, type, isParam));
  }

  // The enterFunc function enters a function id, its return type and its local 
  // symbol table into the symbol table. When this function is called, the local
  // symbol table contains only the formal parameters. This entry is needed to
  // allow type checking of recursive calls.

  public void enterFunc (String id, Type type, SymbolTable env) {
    enter (id, new SymbolTableEntry (Category . FUNCTION, type, env));
  }

  // The enterFuncCode function enters the code for function id into the
  // symbol table. The entry for id has already been added by enterFunc.

  public void enterFuncCode (String id, String code) {
    SymbolTableEntry idEntry = table . get (id);
    idEntry . setCode (code);
  }

  // The enterClass function enters a class id and its local symbol table 
  // into the symbol table.

  public void enterClass (String id, SymbolTable env) {
    enter (id, new SymbolTableEntry (Category . CLASS, env));
  }

  public void addParam(Expression e){
    paramList.add(e);
  }
  
  //Adds the lineOfCode to the intermediate code.
  public void emit(String lineOfCode){
    intermediateCode += "   " + lineOfCode + ";\n";
  }

  /*
  * Adds the header file string passed in to the intermediate code.
  * i.e. #include <stdio.h>
  */
  public void emitHeader(String header){
    intermediateCode += header + "\n";
  }

  /*
  * Adds an assignment statement to the intermediate code.
  * @param e, The expression/variable being assigned.
  * @param e2, The expression that e is assign 2 assigned.
  */
  public void emitAssign(Expression e, Expression e2){
    String assignmentStatement;
    if(e.isArray)
      assignmentStatement = e.addr +"["+ e.exp.addr +"]"+ " = " + e2.addr;
    else if(e.params == null)
      assignmentStatement = e.addr + " = " + e2.addr;
    else{
      assignmentStatement = e.addr + " = " + e2.addr + "(_actual)";
    }
    emit(assignmentStatement);
  }

  /*
  * Adds a print statement to the intermediate code.
  */
  public void emitPrintStatement(Expression e){
    String printStatement = "printf(\"%d\", "+ e.addr +")";

    emit(printStatement);
  }

  /*
  * Adds a scanf statement to the intermediate code.
  */
  public void emitScanStatement(Expression e){
    String scanStatement = "scanf(\"%d\", &"+ e.addr +")";
    emit(scanStatement);

  }

  /*
  * Adds an array assignment statement to the intermediate code.
  */
  public Expression emitArrayAssign(Expression e){
    String arrayAssignment = tempVariable(numberOfTemps) + " = " + e.addr +"["+ e.exp.addr +"]";
    emit(arrayAssignment);
    return new Expression(tempVariable(numberOfTemps), true);
  }

  /*
  * Adds a three address statement to the intermediate code.
  */
  public Expression emitThreeAddress(Expression e, Expression e2, String oper){
    String threeAd = tempVariable(numberOfTemps) + " = "+ e.addr + oper + e2.addr;
    emit(threeAd);
    return new Expression(tempVariable(numberOfTemps), false);
  }

  /*
  * Adds an if statement to the intermediate code.
  */
  public void emitIfStatement(Expression e, Expression e2, String oper){
    String ifStatement = "if(" + e.addr + oper + e2.addr + ") goto " + getLabel(currentLabel);
    emit(ifStatement);
    emitGoto(currentLabel + 1);
    emitLabel();
  }
  
  /*
  * Adds a most recent label to the intermediate code.
  */
  public void emitLabel(){
    intermediateCode += makeLabel(currentLabel) + "\n";
  }

  /*
  * Adds a goto with the label number to the intermediate code.
  */
  public void emitGoto(int number){
    intermediateCode += "goto " + getLabel(number) + ";\n";
  }

  public String emitParamsListStatement(String id){
    for(Expression e : paramList){
      if(e.isArray)
        emit("_actual[" + paramList.indexOf(e) + "] = (int)" + e.addr);
      else
        emit("_actual[" + paramList.indexOf(e) + "] = " + e.addr);
    }
    emit(tempVariable(numberOfTemps) + " = " + id + "(_actual)");
    return tempVariable(numberOfTemps);
  }
  public void emitFunctionParamStatement(Declarator decl){
    String functionParamStatement = decl.id() + " = ";
    if(decl.isArray())
      functionParamStatement += "("+ decl.type() +"*) _formal["+ (numberOfParams++) +"]";
    else 
      functionParamStatement += "_formal["+ (numberOfParams++) +"]";
    emit(functionParamStatement);
  }

  /*
  * Adds a return statement to the intermediate code.
  */
  public void emitReturnStatement(Expression e){
    emit("return " + e.addr);
  }

  //Return the string form of the temp variable of the number passed in.
  public String tempVariable(int number){
    return "_T" + number;
  }

  public String makeLabel(int number){
    return "_L" + number + ":";
  }

  public String getLabel(int number){
    return "_L" + number;
  }
 
  //Increments the number of temps.
  public int newTemp(){
    return numberOfTemps++;
  }
  
  //Increments the current label.
  public int newLabel(){
    return currentLabel++;
  }


  public void print1(String blockName){
    System.out.println();
    System . out . println ("Intermediate Code for " + blockName);
    System . out . print ("---------------------");
    System.out.println();

  }

  // The print function prints the entire symbol table, including local
  // symbol tables for classes and functions.

  public void print (String blockName) {
    print1(blockName);

    System.out.println(intermediateCode);
    Iterator <Map . Entry <String, SymbolTableEntry>> envIterator = 
      table . entrySet () . iterator ();
   
    while (envIterator . hasNext ()) {
      Map . Entry <String, SymbolTableEntry> entry = envIterator . next ();
      String id = entry . getKey ();
      SymbolTableEntry idEntry = entry . getValue ();
      if (idEntry . category () == Category . VARIABLE){
        printDeclaration(idEntry, id);
      }    
      if (idEntry . category () == Category . FUNCTION){
       idEntry . localEnv () . printFunction (idEntry, id); 
      }
      if(idEntry . category () == Category . CLASS){
       idEntry . localEnv () . printClass (idEntry, id); 
      }
    } 
  }//print

  public void printFunction(SymbolTableEntry funcOrClass, String funcName){
    printFunctionStart(funcOrClass, funcName);

    printDeclarations(false);

    printTempVariables();
    printFunctionCallVariable();
    System.out.println(intermediateCode);

    System.out.println("}\n");

  }//printFunction

  public void printFunctionStart(SymbolTableEntry table, String funcName){
     if(funcName.equals("main"))
      System.out.print(table.type() + " " + funcName + "(){\n");
    else
      System.out.print(table.type() + " " + funcName + "(int* _formal){\n");

  }

  public void printClass(SymbolTableEntry funcOrClass, String className){
    System.out.println("struct {");
      printDeclarations(true);

    System.out.println("\n\n} "+className);
  }

  /*
  * Prints all the declartions in the table.
  */
  public void printDeclarations(boolean isClass){
    Iterator <Map . Entry <String, SymbolTableEntry>> envIterator = 
      table . entrySet () . iterator ();
   
    while (envIterator . hasNext ()) {
      Map . Entry <String, SymbolTableEntry> entry = envIterator . next ();
      String id = entry . getKey ();
      SymbolTableEntry idEntry = entry . getValue ();
      if (idEntry . category () != Category . VARIABLE && isClass){
        printDeclaration(idEntry, id);
      }
    }
  }

  /*
  * Prints declaration of a variable from a SymbolTableEntry
  * and the id of that variable.
  * Formatted:
  * int* num;
  * int num[100];
  * int num;
  */
  public void printDeclaration(SymbolTableEntry t, String id){
    System . out . print("   ");

    if(t.type().getClass() == ArrayType.class){
      ArrayType aType = (ArrayType) t.type();
      if(t.isParam)
        System.out.println(aType + "* " + id +";");
      else
        System.out.println(aType + " " + id + "["+aType.dimension()+"];");
     }else{
       System.out.println(t + " " + id + ";");
     }
  }

  public void printTempVariables(){
    int i = 1;
    while(i < numberOfTemps){
      System.out.println("   int "+ tempVariable(i++) + ";");
    }
  }

  public void printFunctionCallVariable(){
    if(paramList.size() > 0)
      System.out.println("   int _actual[" + paramList.size() +"];");
  }

}

class Expression{
  public String addr;
  public Boolean isArray;
  public Expression exp;
  public List<Expression> params;
  Expression(String address, boolean t){
    addr = address;
    isArray = t;

  }

  Expression(String address, boolean t, List paramsList){
    addr = address;
    isArray = t;
    params = paramsList;
  }
  public String toString(){
    return addr; 
  }
}
