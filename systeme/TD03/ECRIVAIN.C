#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <sys/ipc.h>
#include <sys/sem.h>
#include <sys/shm.h>

#include "commun.h"

int     shmid,
        semid;

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
   int clef_mem,
        clef_sem;
    if ((clef_mem= ftok(NOM,PROJ_FTOK_MEM))==-1)
    erreur(" Erreur lors du ftok m‚moire partag‚e");
    if  ((clef_sem= ftok(NOM,PROJ_FTOK_SEM))==-1)
     erreur(" Erreur lors du ftok s‚maphore");

     if ((shmid = shmget(clef_mem,TAILLE,IPC_CREAT|IPC_EXCL|0644))==-1)
     erreur(" Erreur lors du shmget");
     if ((semid = semget(clef_sem,2,IPC_CREAT|IPC_EXCL|0666))==-1)
     erreur(" Erreur lors du semget");
     if(semctl(semid,2,SETALL,init_sem)==-1)
     erreur("erreur lors du semctl");
     }
void ecrivain()
{
char buf[20];
char *addr;
int     i,
        j;
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
    sleep(5);
    if (semctl (semid,0,IPC_RMID,0)==-1)
    erreur("erreur lors du semctl");

    if (shmctl(shmid,IPC_RMID,NULL)==-1)
    erreur("erreur lors du shmctl");
    exit(0);
    }








