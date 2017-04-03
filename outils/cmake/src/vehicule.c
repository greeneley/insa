#include <stdio.h>
#include <stdlib.h>

#include "vehicule.h"

vehicule * init_vehicule()
{
    vehicule *v;
    if(!(v = (vehicule *)malloc(sizeof(vehicule))))
       {
           printf("Erreur malloc");
           exit(EXIT_FAILURE);
       }
    else
    {
        v->puissance = rand();
        v->vitesse_max = (float)(rand());

        return v;
    }
}


void afficher_vehicule(vehicule* v)
{
    printf("\nPuissance : %d chevaux.", v->puissance);
    printf("\nVitesse maximale : %f km/h", v->vitesse_max);
}
