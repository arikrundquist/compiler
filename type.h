
#ifndef YYSTYPE

typedef enum tokentype {
    NONETYPE,
    NUMERIC,
    STRING,
    OPERATOR,
    RESERVED,
    IDENTIFIER,
    END_TOKEN_TYPES
} tokentype;
typedef enum datatype {
    NONEDTYPE,
    INTEGER,
    FLOATING,
    CHARACTER,
    BOOLEAN,
    OPTIONAL,
    OBJECT,
    END_DATA_TYPES
} datatype;
typedef enum operator {
    NOOP,
    TILDE,
    PERCENT,
    CARET,
    AND,
    MULT,
    SUB,
    ADD,
    ASSIGN,
    DIV,
    PIPE,
    EXCLAMATION,
    AT,
    POUND,
    LPAREN,
    RPAREN,
    LSBRACKET,
    RSBRACKET,
    LCBRACKET,
    RCBRACKET,
    SEMICOLON,
    COLON,
    COMMA,
    DOT,
    LABRACKET,
    RABRACKET,
    QUESTION,
    ANDAND,
    SUBSUB,
    ADDADD,
    MULTMULT,
    PIPEPIPE,
    EQEQ,
    NEQ,
    LEQ,
    GEQ,
    END_OPERATORS
} operator;
typedef enum reserved {
    NORES,
    ACTOR,
    CLASS,
    FOR,
    IF,
    RETURN,
    WHILE,
    END_RESERVED
} reserved;

typedef struct tokn {
    tokentype type;
    datatype dtype;
    struct tokn *operands;
    struct tokn *link;
    union {
        char *stringval;
        reserved reservedval;
        operator operatorval;
        int intval;
        double realval;
    } tokenval;
} token;
typedef token *TOKEN;
#define intval tokenval.intval
#define reservedval tokenval.reservedval
#define operatorval tokenval.operatorval
#define realval tokenval.realval
#define stringval tokenval.stringval

TOKEN talloc();



#define YYSTYPE TOKEN
#endif
