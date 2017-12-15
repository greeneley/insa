#ifndef COPIE_H
#define COPIE_H

#define FICHIER_CLEF "copie.h"

#define PROJ_FTOK 'a'

#define TYPE_FIC1 1
#define TYPE_FIC2 2
#define TYPE_END1 3
#define TYPE_END2 4

#define SIZE_BUF 256

typedef struct data_s data_t;
struct data_s {
	long type;
	int taille;
	char buf[SIZE_BUF];
};
#endif
