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
printf(" Lecteur < nb_lecteurs> \n\n");
exit (-1);
}
static void erreur(char *msg)
{
fprintf (stderr, "Erreur  lecteur :%s : %d \n",msg,errno);
exit(-1);
}
void lecture(char *source, char *destination, int nb_octets)
{
int i;
semop(semid, &demande_lecture,1);
printf(" D‚but de lecture dans la zone partag‚e de %d octets par le processus %d\n"
        ,nb_octets,getpid());
for(i=0; i<nb_octets;i++)
     destination[i]=source[i];
     sleep(2);
printf(" Fin de lecture dans la zone partag‚e par le processus  %d\n",getpid());
semop(semid,&stoppe_lecture,1);
sleep(1);
}
void init_lecteurs()
{
key_t   clef_mem,
        clef_sem;
if ((clef_mem= ftok(NOM,PROJ_FTOK_MEM))==-1)
erreur("erreur lors du ftok m‚moire partag‚e");
if ((clef_sem= ftok(NOM,PROJ_FTOK_SEM))==-1)
erreur("erreur lors du ftok s‚maphore");

// on ne voit pes flags comme CREAT car le lecteur n'est pas cense initialiser de semahores
// ou d'ensemble de semaphores
if ((shmid=shmget(clef_mem,TAILLE,0))==-1)
   erreur("erreur lors du shmget");
if ((semid=semget(clef_sem,2,0))==-1)
   erreur("erreur lors du semget");
   }
void lecteur ()
{
char buf[20];
char    *addr;
int i;

if ((addr=shmat(shmid,NULL,SHM_RDONLY))==(char *)-1)
erreur ("erreur lors du shmat");
 for(i=0;i<4;i++){
  sleep(1);
  lecture(addr,buf,20);
 }
 exit(0);
 }
void main (int argc, char *argv[])
{
int     i,
        nb_process;
   if ((argc<2) || (argc>2))
   {
   usage();
   exit(1);
   }
   init_lecteurs();
   nb_process= atoi (argv[1]);
    for (i=1;i<=nb_process;i++)
    {
    switch(fork()) {
    case 0:
           lecteur();
           break;
    case -1:
           erreur("erreur lors du fork");
   default:
        break;
        }
        }
   exit(0);
   }




