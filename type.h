
#ifndef YYSTYPE

typedef enum tokentype {
    NONETYPE,
    NUMERIC,
    STRING,
    OPERATOR,
    RESERVED,
    IDENTIFIER
} tokentype;
typedef enum datatype {
    NONEDTYPE,
    INTEGER,
    FLOATING,
    CHARACTER,
    BOOLEAN,
    OPTIONAL,
    OBJECT
} datatype;

typedef struct tokn {
    tokentype type;
    datatype dtype;
    struct tokn *operands;
    struct tokn *link;
    union {
        char *stringval;
        int whichval;
        int intval;
        double realval;
    } tokenval;
} token;
typedef token *TOKEN;
#define intval tokenval.intval
#define whichval tokenval.whichval
#define realval tokenval.realval
#define stringval tokenval.stringval

TOKEN talloc();



#define YYSTYPE TOKEN
#endif
