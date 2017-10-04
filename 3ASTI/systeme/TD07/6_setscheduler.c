/***********************************************************************\
   exemple_setscheduler
 
   Programme d'exemple du livre "Developpement systeme en C sous Linux"
   
   (c) 2000,2005 - Christophe Blaess
 
\***********************************************************************/

	#include <sched.h>
	#include <stdio.h>
	#include <stdlib.h>
	#include <unistd.h>

	void
syntaxe (char * nom)
{
	fprintf(stderr, "Syntaxe %s priorité commande...\n", nom);
	exit(EXIT_FAILURE);
}

	int
main (int argc, char * argv[])
{
	int                priorite;
	struct sched_param param;

	if ((argc < 3) || (sscanf (argv [1], "%d", & priorite) != 1))
		syntaxe(argv[0]);
	param.sched_priority = priorite;
	if (sched_setscheduler (0, SCHED_RR, & param) < 0) {
		perror("setscheduler");
		exit(EXIT_FAILURE);
	}
	if (seteuid(getuid()) < 0) {
		perror("seteuid");
		exit(EXIT_FAILURE);
	}
	execvp(argv[2], argv + 2);
	perror("execvp");
	return(EXIT_FAILURE);
}
 
