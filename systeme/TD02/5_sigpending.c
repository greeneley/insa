// ------------------------------------------------------------------
// exemple-sigpending.c
// Fichier d'exemple du livre "Developpement Systeme sous Linux"
// (C) 2000-2010 - Christophe BLAESS -Christophe.Blaess@Logilin.fr
// http://www.logilin.fr
// ------------------------------------------------------------------
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <errno.h>
#include <unistd.h>

#ifdef _POSIX_REALTIME_SIGNALS
	#define NB_SIG_CLASSIQUES	SIGRTMIN
#else
	#define NB_SIG_CLASSIQUES	SIGRTMIN
#endif

void gestionnaire (int numero)
{
	fprintf(stdout, "%d (%s) recu\n", numero, sys_siglist[numero]);
}

int main (void)
{
	int i;
	struct sigaction action;
	sigset_t ensemble;

	action.sa_handler = gestionnaire;
	sigfillset (& (action . sa_mask));
	action.sa_flags = 0; /* Pas de SA_RESTART */
	for (i = 1; i < NSIG; i ++)
		if (sigaction(i, & action, NULL) != 0)
			fprintf(stderr, "%ld : %d pas capture\n",
					(long) getpid(), i);

	/* On bloque tout sauf SIGINT */
	sigfillset(& ensemble); /* On remplit tous les signaux de 1 */
	sigdelset(& ensemble, SIGINT);  /* On passe a 0 celui du SIGINT */
	/* On installe le masque de signaux avec l'action liee au premier arguùent */
	sigprocmask(SIG_BLOCK, & ensemble, NULL);

	/* un appel systeme lent bloque */
	read(0, & i, sizeof(int));
	
	/* Voyons maintenant qui est en attente */
	/* Recopie dans le masque de signaux, ceux dont les iterations sont arrivees */
	sigpending(& ensemble);
	for (i = 1; i < NSIG; i ++)
		if (sigismember(& ensemble, i))
			fprintf(stdout, "en attente %d (%s)\n", 
				i, sys_siglist[i]);

	/* On debloque tous les signaux pour les voir arriver */
	/* Le but est de savoir dans quel ordre les signaux seront lachés */
	sigemptyset(& ensemble);
	sigprocmask(SIG_SETMASK, & ensemble, NULL);
	
	return EXIT_SUCCESS;
}

