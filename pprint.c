
#include "pprint.h"

const char *tokentypes[END_TOKEN_TYPES] = {"NULL", "NUMERIC", "STRING", "OPERATOR", "RESERVED", "IDENTIFIER"};
const char *datatypes[END_DATA_TYPES] = {"NULL", "INTEGER", "FLOATING", "CHARACTER", "BOOLEAN", "OPTIONAL", "OBJECT"};
const char *operators[END_OPERATORS] = {"NULL", "~", "%", "^", "&", "*", "-", "+", "=", "/", "|", "!", "@", "#", "(", ")", "[", "]", "{", "}", ";", ":", ",", ".", "<", ">", "?", "&&", "--", "++", "**", "||", "==", "!=", "<=", ">="};
const char *reserveds[END_RESERVED] = {"NULL", "actor", "class", "for", "if", "return", "while"};

void fpprint(FILE *f, token t) {
    fprintf(f, "%s", tokentypes[t.type]);
    if(t.dtype != NONEDTYPE) {
        fprintf(f, "\t%s", datatypes[t.dtype]);
    }
    if(t.type == NUMERIC) {
        if(t.dtype == INTEGER) {
            fprintf(f, "\t%d", t.intval);
        }
        if(t.dtype == FLOATING) {
            fprintf(f, "\t%lf", t.realval);
        }
        if(t.dtype == CHARACTER) {
            fprintf(f, "\t%c", (char) t.realval);
        }
    }
    if(t.type == STRING || t.type == IDENTIFIER) {
        fprintf(f, "\t%s", t.stringval);
    }
    if(t.type == OPERATOR) {
        fprintf(f, "\t%s", operators[t.operatorval]);
    }
    if(t.type == RESERVED) {
        fprintf(f, "\t%s", reserveds[t.reservedval]);
    }
    fprintf(f, "\n");
}
