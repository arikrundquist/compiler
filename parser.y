%{

#include <stdio.h>
#include <stdarg.h>
#include "type.h"

extern "C" {
  extern int yylex();
  void yyerror(const char *s);
}

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

start : _bad { parseresult = $1; }
;

%%

int main(void) {
  int res = yyparse();
  if(res != 0) {
    printf("yyparse result = %8d\n", res);
  }
}

void yyerror(const char *s) { }
