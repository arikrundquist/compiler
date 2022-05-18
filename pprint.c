
#include "pprint.h"

char *tokentypes[6] = {"NULL", "NUMERIC", "STRING", "OPERATOR", "RESERVED", "IDENTIFIER"};
char *datatypes[7] = {"NULL", "INTEGER", "FLOATING", "CHARACTER", "BOOLEAN", "OPTIONAL", "OBJECT"};

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
    if(t->type == STRING) {
        fprintf(f, "\t%s", t->stringval);
    }
    fprintf(f, "\n");
}
