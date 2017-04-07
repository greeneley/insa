/***********************************************************************\
   exemple_ordonnancement
 
   Programme d'exemple du livre "Developpement systeme en C sous Linux"
   
   (c) 2000,2005 - Christophe Blaess
 
\***********************************************************************/

	#include <errno.h>
	#include <sched.h>
	#include <stdio.h>
	#include <stdlib.h>
	#include <unistd.h>
	#include <sys/time.h>
	#include <sys/resource.h>

	void
syntaxe (char * nom)
{
	fprintf(stderr, "Syntaxe %s Pid \n", nom);
	exit(EXIT_FAILURE);
}

	int
main (int argc, char * argv [])
{
	int                 pid;
	int                 ordonnancement;
	int                 prior;
	struct sched_param  param;
	struct timespec     intervalle;

	if ((argc != 2) || (sscanf (argv[1], "%d", & pid) != 1))
		syntaxe(argv[0]);
	if (pid == 0)
		pid = (int) getpid();

	if ((ordonnancement = sched_getscheduler(pid)) < 0) {
		perror("sched_getscheduler");
		exit(EXIT_FAILURE);
	}
	if (sched_getparam(pid, & param) < 0) {
		perror("sched_getparam");
		exit(EXIT_FAILURE);
	}
	if (ordonnancement == SCHED_RR)
		if (sched_rr_get_interval(pid, & intervalle) < 0) {
			perror("sched_rr_get_interval");
			exit(EXIT_FAILURE);
		}
	if (ordonnancement == SCHED_OTHER) {
		errno = 0;
		if (((prior = getpriority(PRIO_PROCESS, pid)) == -1)
		 && (errno != 0)) {
			perror("getpriority");
			exit(EXIT_FAILURE);
		}
	}
	switch (ordonnancement) {
		case SCHED_RR :
			printf("RR : Priorité = %d, intervalle = %ld.%09ld s. \n",
				param.sched_priority,
				intervalle.tv_sec,
				intervalle.tv_nsec);
			break;
		case SCHED_FIFO :
			printf("FIFO : Priorité = %d \n",
				param.sched_priority);
			break;
		case SCHED_OTHER :
			printf("OTHER : Priorité statique = %d dynamique = %d \n",
				param.sched_priority,
				prior);
			break;
		default :  
			fprintf(stdout, "???\n");
			break;
	}
	return EXIT_SUCCESS;
}
