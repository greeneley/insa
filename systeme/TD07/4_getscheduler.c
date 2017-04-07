/***********************************************************************\
   exemple_getscheduler
 
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
	fprintf(stderr, "Syntaxe %s Pid \n", nom);
	exit(EXIT_FAILURE);
}

	int
main (int argc, char * argv[])
{
	int ordonnancement;
	int pid;

	if ((argc != 2) || (sscanf (argv[1], "%d", & pid) != 1))
		syntaxe(argv[0]);
	if ((ordonnancement = sched_getscheduler(pid)) < 0) {
		perror("getscheduler");
		exit(EXIT_FAILURE);
	}
	switch (ordonnancement) {
		case SCHED_RR :    fprintf(stdout, "RR\n");    break;
		case SCHED_FIFO :  fprintf(stdout, "FIFO\n");  break;
		case SCHED_OTHER : fprintf(stdout, "OTHER\n"); break;
		default :          fprintf(stdout, "???\n");   break;
	}
	return EXIT_SUCCESS;
}
 
