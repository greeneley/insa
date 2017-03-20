#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <errno.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#include "copie.h"

static void usage (char *pgr)
{
printf (" Exemple d'application utilisant les files de messages\n");
printf (" Ce programme est un client \n");
printf (" %s 1|2 fichiercopie \n\n", pgr);
exit(-1);
}

static void erreur (char *msg, char *appel, int no_client)
{
fprintf (stderr, " Erreur du client%d %s :%d \n",no_client,msg,errno);
perror(appel);
exit(-1);
}

static void deconnexion ( int id_file, int no_client)
{
data_t deconnexion;

if (no_client == 1)
	deconnexion.type = TYPE_END1;
else
	deconnexion.type = TYPE_END2;

msgsnd (id_file, &deconnexion, sizeof (deconnexion),0);
}

static void close_all (int id_file, int fd, int no_client)
{
deconnexion (id_file, no_client);
msgctl (id_file, IPC_RMID, NULL);
close(fd);
}

int main (int argc, char **argv)
{
int fd,id_file,ret,no_client;
key_t clef;
data_t data;
int	type_client;
if (argc != 3)
	usage (argv[0]);

no_client = atoi (argv[1]);

if (( no_client != 1) && (no_client != 2)  )
	usage (argv[0]);

if (no_client == 1)
	type_client = TYPE_FIC1;
else
	type_client = TYPE_FIC2;

 if ((clef = ftok (FICHIER_CLEF, PROJ_FTOK)) == -1)
        erreur (" Erreur lors du ftok client","ftok",no_client);
if (( id_file = msgget (clef,  IPC_EXCL)) == -1) 
             erreur (" Erreur lors du msgget client","msgget",no_client);

 printf ("Le client %d a obtenu la file de message %d\n", no_client,id_file);

   if (( fd = open (argv[2], O_WRONLY|O_CREAT, 0666)) == -1)
              erreur (" Erreur lors du open client","open",no_client);
 while (1) {
	 
           if (msgrcv (id_file,(data_t *) &data, sizeof (data.buf)+sizeof(int), type_client,0) == -1 )
                                 erreur (" Erreur pendant réception client","msgrcv",no_client);
           if (data.taille == 0) break;
           ret= write(fd,data.buf, data.taille);
            if (ret == -1) {
                                 close_all (id_file,fd, no_client);
                                  erreur (" Erreur de l'écriture client","write",no_client);
                                 		} 
                                  }
      close (fd);
      printf ("Client %d : deconnexion \n",no_client);
      deconnexion (id_file,no_client);
}

