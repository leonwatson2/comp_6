import java_cup.runtime.SymbolFactory;
%%

%class    TinyCPPLexer
%unicode
%line
%column
%cupsym Symbol
%cup
%eofval{
  return symbolFactory . newSymbol ("EOF", Symbol . EOF);
%eofval}

%{
  private SymbolFactory symbolFactory;

  public TinyCPPLexer (java . io . InputStream input, SymbolFactory sf) {
    this (input);
    symbolFactory = sf;
  }

  private void echo () { System . out . print (yytext ()); }

  public int position () { return yycolumn; }
%}

Identifier = [:letter:]("_"?([:letter:] | [:digit:]))*
Integer    = [:digit:] [:digit:]*
Comment    = "//".*

%%
[ \t\n]		{ echo (); }
{Comment}	{ echo (); }
":"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(punctuation, :)", Symbol . COLON); }
";"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(punctuation, ;)", Symbol . SEMICOLON); }
"."		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(punctuation, .)", Symbol . DOT); }
","		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(punctuation, ,)", Symbol . COMMA); }
"{"             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(punctuation, {)", Symbol . LBRACE); }
"}"             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(punctuation, })", Symbol . RBRACE); }
"["             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, [)", Symbol . LBRACK); }
"]"             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, ])", Symbol . RBRACK); }
"("             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, ()", Symbol . LPAREN); }
")"             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, ))", Symbol . RPAREN); }
"!"             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, !)", Symbol . NOT); }
"&&"            { echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, &&)", Symbol . AND); }
"||"            { echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, ||)", Symbol . OR); }
"<"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, <)", Symbol . LT); }
"<="		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, <=)", Symbol . LE); }
">"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, >)", Symbol . GT); }
">="		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, >=)", Symbol . GE); }
"=="		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, ==)", Symbol . EQ); }
"!="		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, !=)", Symbol . NE); }
"+"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, +)", Symbol . PLUS); }
"-"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, -)", Symbol . MINUS); }
"*"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, *)", Symbol . TIMES); }
"/"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, /)", Symbol . SLASH); }
"="		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, =)", Symbol . ASSIGN); }
">>"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, >>)", Symbol . INPUT); }
"<<"		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(operator, <<)", Symbol . OUTPUT); }
cin             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, cin)", Symbol . CIN); }
class           { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, class)", Symbol . CLASS); }
cout            { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, cout)", Symbol . COUT); }
else            { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, else)", Symbol . ELSE); }
if		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, if)", Symbol . IF); }
"#include"	{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, #include)", Symbol . INCLUDE); }
int             { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, int)", Symbol . INT); }
"<iostream>"    { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, <iostream>)", Symbol . IOSTREAM); }
namespace       { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, namespace)", Symbol . NAMESPACE); }
public          { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, public)", Symbol . PUBLIC); }
return          { echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, return)", Symbol . RETURN); }
std		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, std)", Symbol . STD); }
using		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, using)", Symbol . USING); }
while		{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(keyword, while)", Symbol . WHILE); }
{Integer}	{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(integer, " + yytext () + ")",
                      Symbol . INTEGER, yytext ()); }
{Identifier}	{ echo (); 
                  return symbolFactory . 
                    newSymbol ("(identifier, " + yytext () + ")", 
                      Symbol . ID, yytext ()); }
.		{ echo (); ErrorMessage . print (yychar, "Illegal character"); }
