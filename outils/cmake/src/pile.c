#include <stdio.h>
#include <stdlib.h>

#include "vehicule.h"
#include "pile.h"

#define NOMBRE 20

PileVehicule pv; //pile de pointeurs de vehicules.

vehicule* depiler()
{
    if(!(pile_vide()))
    {
        pv.nbVehicule--;
        return pv.pile[pv.nbVehicule];
    }
    else
        printf("\nPile deja vide !");
        exit(EXIT_FAILURE);
}

void init_pile()
{
    if(!(pv.pile = (vehicule **)calloc(NOMBRE, sizeof(vehicule*))))
    {
        printf("\nErreur calloc");
        exit(EXIT_FAILURE);
    }
    else
    {
        int i;
        for(i=0; i<NOMBRE; i++)
            pv.pile[i] = (vehicule *)malloc(sizeof(vehicule));
        pv.nbVehicule = 0;
    }
}

void empiler(vehicule *v)
{
    if(!pile_pleine())
    {
        pv.pile[pv.nbVehicule] = v;
        pv.nbVehicule++;
    }
    else
        printf("\nPile deja pleine !");
}

int pile_pleine()
{
    return (pv.nbVehicule == NOMBRE);
}

int pile_vide()
{
    return (pv.nbVehicule == 0);
}
