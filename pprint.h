
#ifndef PPRINT_H
#define PPRINT_H

#include <stdio.h>
#include "type.h"

extern char *tokentypes[6];
extern char *datatypes[7];

void fpprint(FILE *f, TOKEN t);

#endif
