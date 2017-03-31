#ifndef COMMUN_H
#define COMMUN_H

#define NOM "commun.h"
#define PROJ_FTOK_SEM 'a' // Pour acceder aux semaphores
#define PROJ_FTOK_MEM '0' // Pour acceder a la memoire partagee

#define TAILLE 20 // taille en octet partagee, meme si le noyau a reserve 4 ko
#define N 5

// on va utiliser ce tableau pour initialiser les semaphores
// 1 et N indiquent que l'on va utiliser des semaphores : on les initialise
// 1 == ecrivain, N == lecteurs
ushort init_sem[]={1,N};
// premier argument == index compteur
// deuxieme argument, valeur d'incrementation
// SEM_UNDO == on sauvegarde les valeurs a l'instant t. Si besoin, on remet la valeur d'origine

// Si un ecrivain ecrit, on s'assure que tous les lecteurs soient bloques
struct sembuf demande_ecriture[]={{0,-1,SEM_UNDO},{1,-N,SEM_UNDO}};
// Si l'ecrivain a termine d'ecrire, on repermet aux lecteurs de lire
struct sembuf stoppe_ecriture[]={{0,+1,SEM_UNDO},{1,N,SEM_UNDO}};

// Les deux lignes concernent les lecteurs
// Notons que l'on ne prend pas en compte le compteur des ecrivains
struct sembuf demande_lecture={1,-1,SEM_UNDO};
struct sembuf stoppe_lecture={1,+1,SEM_UNDO};
#endif


