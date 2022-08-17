
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
typedef enum operatortype {
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
} operatortype;
typedef enum reservedtype {
    NORES,
    ACTOR,
    CLASS,
    FOR,
    IF,
    RETURN,
    WHILE,
    END_RESERVED
} reservedtype;

typedef struct tokn {
    tokentype type;
    datatype dtype;
    union {
        char *stringval;
        reservedtype reservedval;
        operatortype operatorval;
        int intval;
        double realval;
    } tokenval;
} token;
#define intval tokenval.intval
#define reservedval tokenval.reservedval
#define operatorval tokenval.operatorval
#define realval tokenval.realval
#define stringval tokenval.stringval

#define YYSTYPE token
#endif
