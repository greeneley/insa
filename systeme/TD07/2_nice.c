/***********************************************************************\
   exemple_nice
 
   Programme d'exemple du livre "Developpement systeme en C sous Linux"
   
   (c) 2000,2005 - Christophe Blaess
 
\***********************************************************************/

	#define _GNU_SOURCE
	#include <signal.h>
	#include <stdio.h>
	#include <stdlib.h>
	#include <sys/wait.h>
	#include <unistd.h>

	volatile long compteur = 0;
	static int    gentillesse;

	void
gestionnaire (int numero)
{
	if (compteur != 0) {
		fprintf(stdout, "Fils %ld (nice %2d) Compteur = %9ld\n",
                          (long) getpid(), gentillesse, compteur);
		exit(EXIT_SUCCESS);
	}
}

#define NB_FILS	5

	int
main (void)
{
	pid_t   pid;
	int	fils;
	
	/* Création d'un nouveau groupe de processus */
	setpgid(0, 0);
	
	signal(SIGUSR1, gestionnaire);
	for (fils = 0; fils < NB_FILS; fils ++) {
		if ((pid = fork()) < 0) {
			perror("fork");
			exit(EXIT_SUCCESS);
		}
		if (pid != 0)
			continue;
		gentillesse = fils * (20 / (NB_FILS - 1));
		if (nice(gentillesse) < 0) {
			perror("nice");
			exit(EXIT_SUCCESS);
		}
		/* attente signal de démarrage */
		pause();
		/* comptage */
		while (1)
			compteur ++;
	}
	/* processus père */
	signal(SIGUSR1, SIG_IGN);
	sleep(1);
	kill(-getpgid(0), SIGUSR1);
	sleep(5);
	kill(-getpgid(0), SIGUSR1);
	while (wait(NULL) > 0)
		;
	exit(EXIT_SUCCESS);
}

