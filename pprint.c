
#include "pprint.h"

char *tokentypes[END_TOKEN_TYPES] = {"NULL", "NUMERIC", "STRING", "OPERATOR", "RESERVED", "IDENTIFIER"};
char *datatypes[END_DATA_TYPES] = {"NULL", "INTEGER", "FLOATING", "CHARACTER", "BOOLEAN", "OPTIONAL", "OBJECT"};
char *operators[END_OPERATORS] = {"NULL", "="};
char *reserveds[END_RESERVED] = {"NULL", "IF", "FOR", "WHILE", "CLASS"};

void fpprint(FILE *f, TOKEN t) {
    fprintf(f, "%s", tokentypes[t->type]);
    if(t->dtype != NONEDTYPE) {
        fprintf(f, "\t%s", datatypes[t->dtype]);
    }
    if(t->type == NUMERIC) {
        if(t->dtype == INTEGER) {
            fprintf(f, "\t%d", t->intval);
        }
        if(t->dtype == FLOATING) {
            fprintf(f, "\t%lf", t->realval);
        }
    }
    if(t->type == STRING || t->type == IDENTIFIER) {
        fprintf(f, "\t%s", t->stringval);
    }
    if(t->type == OPERATOR) {
        fprintf(f, "\t%s", operators[t->operatorval]);
    }
    if(t->type == RESERVED) {
        fprintf(f, "\t%s", reserveds[t->reservedval]);
    }
    fprintf(f, "\n");
}
