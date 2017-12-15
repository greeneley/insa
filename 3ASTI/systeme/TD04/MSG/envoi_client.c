#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <string.h>

#include "copie.h"

int main(int argc, char const *argv[])
{
	int id_file;
	key_t clef;
	clef = ftok (FICHIER_CLEF, PROJ_FTOK);
	id_file = msgget (clef,  IPC_EXCL);
	printf ("file de message %d\n",id_file);

	data_t aEnvoyer;
	char* monMsg = "Ceci est mon message";
	strcpy(aEnvoyer.buf, monMsg);
	aEnvoyer.type   = TYPE_FIC1;
	aEnvoyer.taille = 21;

	msgsnd(id_file, (struct msgbuf *) &aEnvoyer, sizeof(aEnvoyer.buf) + sizeof (int), IPC_NOWAIT);

	printf("Message envoye\n");
	return 0;
}