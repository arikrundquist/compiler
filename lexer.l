
%{


#include <stdlib.h>
#include "type.h"
#include "pprint.h"
#include "parser.tab.h"

int nested_comment = 0;

TOKEN talloc() {
   TOKEN t = (TOKEN) calloc(1, sizeof(token));
}
TOKEN mktoken(tokentype type, datatype dtype) {
   TOKEN t = talloc();
   t->type = type;
   t->dtype = dtype;
   return t;
}

TOKEN makeint() {
   int inum;
   sscanf(yytext, "%d", &inum);
   TOKEN t = mktoken(NUMERIC, INTEGER);
   t->intval = inum;
   return t;
}
TOKEN makereal() {
   double fnum;
   sscanf(yytext, "%lf", &fnum);
   TOKEN t = mktoken(NUMERIC, FLOATING);
   t->realval = fnum;
   return t;
}
TOKEN makeid() {
   int len = 0;
   for(; yytext[len++] != 0;);
   char *str = (char*) malloc(len);
   for(int i = 0; i < len; str[i] = yytext[i], i++);
   TOKEN t = mktoken(IDENTIFIER, 0);
   t->stringval = str;
   return t;
}
TOKEN makestring() {
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
   TOKEN t = mktoken(STRING, OBJECT);
   t->stringval = str;
   return t;
}
TOKEN makeop(int op) {
   TOKEN t = mktoken(OPERATOR, 0);
   t->operatorval = op;
   return t;
}
TOKEN makeres(int res) {
   TOKEN t = mktoken(RESERVED, 0);
   t->reservedval = res;
   return t;
}


#define DEBUG 1
FILE *logfile = NULL;
TOKEN log_token(TOKEN t) {
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
{id}        { handle(_identifier, makeid()); }


"="         { op(_assign, ASSIGN); }


{bad}       { /* todo: handle this */ }

%%

int yywrap() { return(1); }
