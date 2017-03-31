#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

#include "commun.h"

int     shmid, // fd pour une memoire partagee
        semid; // fd pour un ensemble de semaphores

static void usage ()
{
printf("Exemple d'application et des s‚maphores et m‚moire partag‚e \n");
printf(" ---> lecteurs/Ecrivains \n");
printf(" Ecrivain \n\n");
exit (-1);
}
static void erreur(char *msg)
{
fprintf (stderr, "Erreur  Ecrivain :%s : %d \n",msg,errno);
exit(-1);
}
void ecriture(char *source, char *destination, int nb_octets)
{
int i;
semop(semid, demande_ecriture,2);
printf(" D‚but d'‚criture dans la zone partag‚e de %d octets par le processus %d\n"
        ,nb_octets,getpid());
for(i=0; i<nb_octets;i++)
     destination[i]=source[i];
     sleep(1);
printf(" Fin d'‚criture dans la zone partag‚e par le processus  %d\n",getpid());
semop(semid,stoppe_ecriture,2);
}

void init_ecrivain()
{
   int clef_mem,  // cle pour acceder a la memoire partgee
        clef_sem; // cle pour l'ensemble des semaphores
    if ((clef_mem= ftok(NOM,PROJ_FTOK_MEM))==-1)
    erreur(" Erreur lors du ftok m‚moire partag‚e");
    if  ((clef_sem= ftok(NOM,PROJ_FTOK_SEM))==-1)
     erreur(" Erreur lors du ftok s‚maphore");

     // on cree la memoire partagee
     // on ne peut pas lancer deux ecrivains en meme temps a cause du _CREAT
     if ((shmid = shmget(clef_mem,TAILLE,IPC_CREAT|IPC_EXCL|0644))==-1)
     erreur(" Erreur lors du shmget");

     // meme chose avec les semaphores
     // le 2 indique que l'on traite un ensemble de semaphores
     if ((semid = semget(clef_sem,2,IPC_CREAT|IPC_EXCL|0666))==-1)
     erreur(" Erreur lors du semget");

     // controle des semaphores
     // 2 == nombre d'initialiations a faire
     // on initialise les valeurs suivant ce qu'il 'y a dans init_sem

     // en POSIX, on aurait des probleme car il faudrait faire un verrou
     if(semctl(semid,2,SETALL,init_sem)==-1)
     erreur("erreur lors du semctl");
     }
void ecrivain()
{
char buf[20];
char *addr;
int     i,
        j;

// on attache le cadre de page de la memoire partage a la memoire virtuelle du processus
 if ((addr = shmat(shmid,NULL,0))==(char *)-1)
 erreur ("Erreur lors du shmat");
 for (j=0;j<4;j++)
 {
for (i=0;i<20;i++)
        buf[i]++;
        ecriture (buf,addr,20);
        }
        }
void main (int argc,char *argv[])
{
int     i,
        nb_process;
if ((argc !=1)){
    usage();
    exit(1);
    }
    init_ecrivain();
    ecrivain();
    sleep(20);

    // le programme demande au noyau de detruire la memoire
    // cependant il ne previens pas les divers lecteurs  de la destruction
    // et ne rend pas la memoire au noyau
    if (semctl (semid,0,IPC_RMID,0)==-1)
    erreur("erreur lors du semctl");

    if (shmctl(shmid,IPC_RMID,NULL)==-1)
    erreur("erreur lors du shmctl");
    exit(0);
    }








