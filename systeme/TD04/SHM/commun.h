#ifndef COMMUN_H
#define COMMUN_H

#define NOM "commun.h"
#define PROJ_FTOK_SEM 'a' // Pour acceder aux semaphores
#define PROJ_FTOK_MEM '0' // Pour acceder a la memoire partagee

#define TAILLE 20 // taille en octet partagee
#define N 5

// on va utiliser ce tableau pour initialiser les semaphores
ushort init_sem[]={1,N};
struct sembuf demande_ecriture[]={{0,-1,SEM_UNDO},{1,-N,SEM_UNDO}};
struct sembuf stoppe_ecriture[]={{0,+1,SEM_UNDO},{1,N,SEM_UNDO}};

struct sembuf demande_lecture={1,-1,SEM_UNDO};
struct sembuf stoppe_lecture={1,+1,SEM_UNDO};
#endif


