// ------------------------------------------------------------------
// exemple-sigaction-1.c
// Fichier d'exemple du livre "Developpement Systeme sous Linux"
// (C) 2000-2010 - Christophe BLAESS -Christophe.Blaess@Logilin.fr
// http://www.logilin.fr
// ------------------------------------------------------------------

#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>

void gestionnaire (int numero)
{
	switch (numero) {
		case SIGQUIT :
			fprintf(stdout, "\nSIGQUIT recu\n");
			break;
		case SIGINT :
			fprintf(stdout, "\nSIGINT recu\n");
			break;
	}
}

int main (void)
{
	struct sigaction action;

	/* Fonction qui s'occupera de traiter de les signaux recus */
	action.sa_handler = gestionnaire;
	/* On met tous les signaux 'a 0' */
	sigemptyset(& (action.sa_mask));
	/* Signau non reentrant */
	action.sa_flags = 0;

	/* On met en place le gestionnaire de signal */
	if (sigaction(SIGQUIT, & action, NULL) != 0) {
		fprintf(stderr, "Erreur %d\n", errno);
		exit(EXIT_FAILURE);
	}

	/* Preferez les termes REENTRANT et ONESHOT pour la comprehension */
	action.sa_flags = SA_RESTART | SA_RESETHAND;

	if (sigaction(SIGINT, & action, NULL) != 0) {
		fprintf(stderr, "Erreur %d\n", errno);
		exit(EXIT_FAILURE);
	}

	/* Lecture continue, pour avoir un appel-systeme lent  bloque */
	while (1) {
		int i;
		fprintf(stdout, "appel read()\n");
		if (read(0, & i, sizeof(int)) < 0)
			if (errno == EINTR)
				fprintf(stdout, "EINTR \n");
	}
	return EXIT_SUCCESS;

}

