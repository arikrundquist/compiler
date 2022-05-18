%{

#include <stdio.h>
#include "type.h"

extern int yylex();
void yyerror(const char *s);

YYSTYPE parseresult;

%}

%token string integer floating

%%

start : any { parseresult = $1; }
      | any start
;

any : string | integer | floating;

%%

int main(void) {
    int res = yyparse();
    if(res != 0) {
      printf("yyparse result = %8d\n", res);
    }
}

void yyerror(const char *s) {
  fprintf(stderr, "%s\n", s);
}
