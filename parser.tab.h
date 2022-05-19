/* A Bison parser, made by GNU Bison 3.8.2.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2021 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <https://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_PARSER_TAB_H_INCLUDED
# define YY_YY_PARSER_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    _string = 258,                 /* _string  */
    _integer = 259,                /* _integer  */
    _floating = 260,               /* _floating  */
    _identifier = 261,             /* _identifier  */
    _character = 262,              /* _character  */
    _actor = 263,                  /* _actor  */
    _class = 264,                  /* _class  */
    _for = 265,                    /* _for  */
    _if = 266,                     /* _if  */
    _return = 267,                 /* _return  */
    _while = 268,                  /* _while  */
    _tilde = 269,                  /* _tilde  */
    _percent = 270,                /* _percent  */
    _caret = 271,                  /* _caret  */
    _and = 272,                    /* _and  */
    _mult = 273,                   /* _mult  */
    _sub = 274,                    /* _sub  */
    _add = 275,                    /* _add  */
    _assign = 276,                 /* _assign  */
    _div = 277,                    /* _div  */
    _pipe = 278,                   /* _pipe  */
    _exclamation = 279,            /* _exclamation  */
    _at = 280,                     /* _at  */
    _pound = 281,                  /* _pound  */
    _lparen = 282,                 /* _lparen  */
    _rparen = 283,                 /* _rparen  */
    _lsbracket = 284,              /* _lsbracket  */
    _rsbracket = 285,              /* _rsbracket  */
    _lcbracket = 286,              /* _lcbracket  */
    _rcbracket = 287,              /* _rcbracket  */
    _semicolon = 288,              /* _semicolon  */
    _colon = 289,                  /* _colon  */
    _comma = 290,                  /* _comma  */
    _dot = 291,                    /* _dot  */
    _labracket = 292,              /* _labracket  */
    _rabracket = 293,              /* _rabracket  */
    _question = 294,               /* _question  */
    _andand = 295,                 /* _andand  */
    _subsub = 296,                 /* _subsub  */
    _addadd = 297,                 /* _addadd  */
    _multmult = 298,               /* _multmult  */
    _pipepipe = 299,               /* _pipepipe  */
    _eqeq = 300,                   /* _eqeq  */
    _neq = 301,                    /* _neq  */
    _leq = 302,                    /* _leq  */
    _geq = 303,                    /* _geq  */
    _bad = 304                     /* _bad  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;


int yyparse (void);


#endif /* !YY_YY_PARSER_TAB_H_INCLUDED  */
