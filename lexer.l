
%{

#include <stdlib.h>
#include "type.h"
#include "pprint.h"
#include "parser.tab.h"

extern "C" int yylex();

int nested_comment = 0;

token mktoken(tokentype type, datatype dtype) {
   token t;
   t.type = type;
   t.dtype = dtype;
   return t;
}

token makeint() {
   int inum;
   sscanf(yytext, "%d", &inum);
   token t = mktoken(NUMERIC, INTEGER);
   t.intval = inum;
   return t;
}
token makereal() {
   double fnum;
   sscanf(yytext, "%lf", &fnum);
   token t = mktoken(NUMERIC, FLOATING);
   t.realval = fnum;
   return t;
}
token makeid() {
   int len = 0;
   for(; yytext[len++] != 0;);
   char *str = (char*) malloc(len);
   for(int i = 0; i < len; str[i] = yytext[i], i++);
   token t = mktoken(IDENTIFIER, NONEDTYPE);
   t.stringval = str;
   return t;
}
token makechar() {
   char c = yytext[1];
   if(c == '\\') {
      c = yytext[2];
      switch(c) {
      case('t'):
         c = '\t';
         break;
      case('r'):
         c = '\r';
         break;
      case('n'):
         c = '\n';
         break;
      default:
         break;
      }
   }
   token t = mktoken(NUMERIC, CHARACTER);
   t.intval = c;
   return t;
}
token makestring() {
   int len = -2;
   char c;
   for(int i = 0; (c = yytext[i]) != 0; len++, i++) {
      if(c == '\\') {
         i++; continue;
      }
   }
   char *str = (char*) malloc(len+1);
   for(int i1 = 0, i2 = 1; i1 < len; i1++, i2++) {
      c = yytext[i2];
      if(c != '\\') {
         str[i1] = c;
         continue;
      }
      c = yytext[++i2];
      switch(c) {
      case('t'):
         c = '\t';
         break;
      case('r'):
         c = '\r';
         break;
      case('n'):
         c = '\n';
         break;
      default:
         break;
      }
      str[i1] = c;
   }
   str[len] = 0;
   token t = mktoken(STRING, OBJECT);
   t.stringval = str;
   return t;
}
token makeop(operatortype op) {
   token t = mktoken(OPERATOR, NONEDTYPE);
   t.operatorval = op;
   return t;
}
token makeres(reservedtype res) {
   token t = mktoken(RESERVED, NONEDTYPE);
   t.reservedval = res;
   return t;
}


#define DEBUG 1
FILE *logfile = NULL;
token log_token(token t) {
   if(!DEBUG) {
      return t;
   }
   if(logfile == NULL) {
      logfile = fopen("lexer.log", "w");
   }
   fpprint(logfile, t);
   return t;
}
#define handle(type, handler) yylval = log_token(handler); return type
#define op(type, op) handle(type, makeop(op))
#define res(type, res) handle(type, makeres(res))
%}

bad      .
ws       [ \t\n]+
alpha    [_$A-Za-z]
digit    [0-9]
id       {alpha}({alpha}|{digit})*
fnumber  {digit}+(\.{digit}+)?([eE][+\-]?{digit}+)?
inumber  {digit}+
escape   \\[\\'"trn]
char     \'([^'\\]|{escape})\'
string   \"([^"\\]|{escape})*\"
startcomment   [/][*]
endcomment     [*][/]
insidecomment  ([.]|[^.])
linecomment    [/][/][^\n]*

%x INSIDE_COMMENT

%%

{ws}              { /* nothing */ }
{linecomment}     { /* nothing */ }
<INITIAL,INSIDE_COMMENT>{
{startcomment}    { nested_comment++; BEGIN(INSIDE_COMMENT); }
}
<INSIDE_COMMENT>{
{insidecomment}   { /* nothing */ }
{endcomment}      { if(--nested_comment == 0) { BEGIN(INITIAL); }}
}

{inumber}   { handle(_integer, makeint()); }
{fnumber}   { handle(_floating, makereal()); }
{string}    { handle(_string, makestring()); }
{char}      { handle(_character, makechar()); }

"actor"     { res(_actor, ACTOR); }
"class"     { res(_class, CLASS); }
"for"       { res(_for, FOR); }
"if"        { res(_if, IF); }
"return"    { res(_return, RETURN); }
"while"     { res(_while, WHILE); }

"~"         { op(_tilde, TILDE); }
"%"         { op(_percent, PERCENT); }
"^"         { op(_caret, CARET); }
"&"         { op(_and, AND); }
"*"         { op(_mult, MULT); }
"-"         { op(_sub, SUB); }
"+"         { op(_add, ADD); }
"="         { op(_assign, ASSIGN); }
"/"         { op(_div, DIV); }
"|"         { op(_pipe, PIPE); }
"!"         { op(_exclamation, EXCLAMATION); }
"@"         { op(_at, AT); }
"#"         { op(_pound, POUND); }
"("         { op(_lparen, LPAREN); }
")"         { op(_rparen, RPAREN); }
"["         { op(_lsbracket, LSBRACKET); }
"]"         { op(_rsbracket, RSBRACKET); }
"{"         { op(_lcbracket, LCBRACKET); }
"}"         { op(_rcbracket, RCBRACKET); }
";"         { op(_semicolon, SEMICOLON); }
":"         { op(_colon, COLON); }
","         { op(_comma, COMMA); }
"."         { op(_dot, DOT); }
"<"         { op(_labracket, LABRACKET); }
">"         { op(_rabracket, RABRACKET); }
"?"         { op(_question, QUESTION); }
"=="        { op(_eqeq, EQEQ); }
"!="        { op(_neq, NEQ); }
"<="        { op(_leq, LEQ); }
">="        { op(_geq, GEQ); }
"&&"        { op(_andand, ANDAND); }
"--"        { op(_subsub, SUBSUB); }
"++"        { op(_addadd, ADDADD); }
"**"        { op(_multmult, MULTMULT); }
"||"        { op(_pipepipe, PIPEPIPE); }


{id}        { handle(_identifier, makeid()); }


{bad}       { handle(_bad, mktoken(NONETYPE, NONEDTYPE)); }

%%

int yywrap() { return(1); }
