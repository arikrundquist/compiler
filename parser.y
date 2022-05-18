%{

#include <stdio.h>
#include <stdarg.h>
#include "type.h"

extern int yylex();
void yyerror(const char *s);


TOKEN freed = NULL;
TOKEN cons(TOKEN head, TOKEN rest) {
  head->link = rest;
  return head;
}
void _ignore(int start, ...) {
  va_list tokens;
  va_start(tokens, start);
  for(TOKEN t; (t = va_arg(tokens, TOKEN)) != NULL;) {
    freed = cons(t, freed);
  }
  va_end(tokens);
}
#define ignore(...) _ignore(0, __VA_ARGS__, NULL)
TOKEN new() {
  TOKEN t = freed;
  if(t == NULL) {
    t = talloc();
  }else {
    freed = freed->link;
  }
  t->link = NULL;
  return t;
}
TOKEN copy(TOKEN t) {
  TOKEN other = new();
  *other = *t;
  return other;
}
TOKEN _mkop(TOKEN overwrite, ...) {
  va_list tokens;
  va_start(tokens, overwrite);
  TOKEN tok = va_arg(tokens, TOKEN);
  overwrite->operands = tok;
  do {
    tok = (tok->link = va_arg(tokens, TOKEN));
  }while(tok != NULL);
  va_end(tokens);
  return overwrite;
}
#define mkop(overwrite, ...) _mkop(overwrite, __VA_ARGS__, NULL)

YYSTYPE parseresult;

%}

// literals and identifiers
%token _string _integer _floating _identifier

// operators
%token _assign

%%

start : statementlist { parseresult = $1; }
;

statementlist : statement
              | statement statementlist { $$ = cons($1, $2); }
;

statement : assign
;

assign : identifier _assign literal { $$ = mkop($2, $1, $3); }
;

identifier : _identifier
;

literal : _string | _integer | _floating
;

%%

int main(void) {
  int res = yyparse();
  if(res != 0) {
    printf("yyparse result = %8d\n", res);
  }
}

void yyerror(const char *s) { }
