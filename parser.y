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
%token _string _integer _floating _identifier _character

// reserved
%token _actor _class _for _if _return _while

// operators
%token _tilde _percent _caret _and _mult _sub _add _assign _div _pipe
%token _exclamation _at _pound _lparen _rparen _lsbracket _rsbracket _lcbracket _rcbracket _semicolon _colon _comma _dot _labracket _rabracket _question
%token _andand _subsub _addadd _multmult _pipepipe _eqeq _neq _leq _geq

// this is bad
%token _bad

%%

start : program { parseresult = $1; }
;


statement : assign
;







program : actor
        | classlist actor
        | program classoractor
;
classoractor : class | actor
;
classlist : class | class classlist { $$ = cons($1, $2); }
;

class : _class _identifier optionalclassbody
;
actor : _actor _identifier optionalactorbody
;

optionalclassbody : _lcbracket _rcbracket
                  | _lcbracket classbody _rcbracket
;
optionalactorbody : _lcbracket _rcbracket
                  | _lcbracket actorbody _rcbracket
;

classbody : classitem
          | classitem classbody
;
actorbody : actoritem
          | actoritem actorbody
;
actoritem : classitem
          | actor
;

classitem : statement
          //| funcdef
          //| class
;

assign  : _identifier _assign expression
        | uop _identifier
        | _identifier uop
;
uop : _addadd
    | _subsub
;

literal : _string | _integer | _floating | _character
;
term : literal | _identifier
;
pexpression : _lparen expression _rparen
            | term
;
uexpression : _sub pexpression
            | _exclamation pexpression
            | pexpression _question
            | pexpression _exclamation
            | pexpression
;
eop : _multmult
;
eexpression : uexpression eop uexpression
            | uexpression
;
mdop  : _mult | _div | _percent
;
mdexpression  : eexpression mdop eexpression
              | eexpression
;
asop  : _add | _sub
;
asexpression  : mdexpression asop mdexpression
              | mdexpression
;
compop : _eqeq | _neq | _leq | _geq | _labracket | _rabracket
;
compexpression  : asexpression compop asexpression
                | asexpression
;
bitop : _and | _pipe | _caret | _andand | _pipepipe | _tilde
;
bitexpression : compexpression bitop compexpression
              | compexpression
;
expression  : bitexpression
            | assign
;

%%

int main(void) {
  int res = yyparse();
  if(res != 0) {
    printf("yyparse result = %8d\n", res);
  }
}

void yyerror(const char *s) { }
