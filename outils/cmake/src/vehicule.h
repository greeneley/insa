#ifndef _VEHICULE_H
#define _VEHICULE_H

struct vehicule
{
    char nom_modele[20];
    int puissance;
    float vitesse_max;
};
typedef struct vehicule vehicule;

vehicule * init_vehicule();
void afficher_vehicule(vehicule* v);

#endif // _VEHICULE_H
