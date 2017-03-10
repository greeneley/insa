// ------------------------------------------------------------------
// exemple-siginterrupt.c
// Fichier d'exemple du livre "Developpement Systeme sous Linux"
// (C) 2000-2010 - Christophe BLAESS -Christophe.Blaess@Logilin.fr
// http://www.logilin.fr
// ------------------------------------------------------------------
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <signal.h>

void gestionnaire (int numero)
{
	fprintf(stdout, "\ngestionnaire de signal %d\n", numero); 
}

int main (int argc, char *argv[])
{
	int i;

	if ((argc != 2) || (sscanf(argv[1], "%d", & i) != 1)) {
		fprintf (stderr, "Syntaxe : %s {0|1}\n", argv[0]);
		exit(EXIT_FAILURE);
	}
	
	signal(SIGINT, gestionnaire);
	siginterrupt(SIGINT, i);

	while (1) {
		fprintf(stdout, "appel read()\n");
		if (read(STDIN_FILENO, &i, sizeof (int)) < 0)
			{if (errno == EINTR)
				fprintf(stdout, "je suis sorti du read EINTR \n");}
				else
				fprintf(stdout, "Valeur de i : %i\n",i);
	}
	return EXIT_SUCCESS;	
}
