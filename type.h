
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
    ASSIGN,
    END_OPERATORS
} operator;
typedef enum reserved {
    NORES,
    IF,
    FOR,
    WHILE,
    CLASS,
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
