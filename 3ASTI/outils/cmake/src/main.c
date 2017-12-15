#include <stdio.h>
#include <stdlib.h>

#include "vehicule.h"
#include "pile.h"

int main(int argc, char const *argv[])
{
    vehicule *v0, *v1, *v2;
    v0 = init_vehicule();
    v1 = init_vehicule();
    init_pile();

    //Empilement de v0
    afficher_vehicule(v0);
    empiler(v0);

    //Empilement de v1
    afficher_vehicule(v1);
    empiler(v1);

    //On depile
    v2 = depiler();
    afficher_vehicule(v2);

    free(v0);
    free(v1);
    
    return 0;
}
