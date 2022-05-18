
%{


#include <stdlib.h>
#include "type.h"
#include "pprint.h"
#include "parser.tab.h"

int nested_comment = 0;

TOKEN talloc() {
   TOKEN t = (TOKEN) calloc(1, sizeof(token));
}


TOKEN makeint() {
   int inum;
   sscanf(yytext, "%d", &inum);
   TOKEN t = talloc();
   t->type = NUMERIC;
   t->dtype = INTEGER;
   t->intval = inum;
   return t;
}
TOKEN makereal() {
   double fnum;
   sscanf(yytext, "%lf", &fnum);
   TOKEN t = talloc();
   t->type = NUMERIC;
   t->dtype = FLOATING;
   t->realval = fnum;
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
   TOKEN t = talloc();
   t->type = STRING;
   t->dtype = OBJECT;
   t->stringval = str;
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
%}

ws       [ \t\n]+
alpha    [_$A-Za-z]
digit    [0-9]
id       {alpha}({alpha}|{digit})*
fnumber  {digit}+(\.{digit}+)?([eE][+\-]?{digit}+)?
inumber  {digit}+
escape   \\[\\'"trn]
char     \'([^'\\]|{escape})\'
string   \"([^"\\]|{escape})*\"

   /* comments may appear in two forms: */
   /* 1. (\{[^}]*\}), a {, followed by any non-} characters, and then a closing } */
   /* 2. (\(\*([^*]|\* /[^)]])*\*\)), a (*, then a non-* or an * that is followed by a non-), and a final *) */
startcomment   [/][*]
endcomment     [*][/]
insidecomment  .
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

{inumber}   { handle(integer, makeint()); }
{fnumber}   { handle(floating, makereal()); }
{string}    { handle(string, makestring()); }

%%

int yywrap() { return(1); }
