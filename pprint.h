
#ifndef PPRINT_H
#define PPRINT_H

#include <stdio.h>
#include "type.h"

extern const char *tokentypes[6];
extern const char *datatypes[7];

void fpprint(FILE *f, token t);

#endif
