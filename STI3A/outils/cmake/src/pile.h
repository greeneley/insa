#ifndef _PILE_H
#define _PILE_H

#include "vehicule.h"

typedef struct
{
	int nbVehicule;
	vehicule **pile;
} PileVehicule;

vehicule* depiler();

void empiler(vehicule *v);
void init_pile();

int pile_pleine();
int pile_vide();

#endif // _PILE_H
