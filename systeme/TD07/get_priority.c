/***********************************************************************\
   exemple_get_priority
 
   Programme d'exemple du livre "Developpement systeme en C sous Linux"
   
   (c) 2000,2005 - Christophe Blaess
 
\***********************************************************************/

	#include <sched.h>
	#include <stdio.h>
	#include <stdlib.h>
	#include <unistd.h>


int main (void)
{
	fprintf(stdout, "Ordonnancement FIFO :\n  %d <= priorité <= %d\n",
			sched_get_priority_min(SCHED_FIFO),
	 		sched_get_priority_max(SCHED_FIFO));
	fprintf(stdout, "Ordonnancement RR :\n  %d <= priorité <= %d\n",
			sched_get_priority_min(SCHED_RR),
	 		sched_get_priority_max(SCHED_RR));
	fprintf(stdout, "Ordonnancement OTHER :\n  %d <= priorité <= %d\n",
			sched_get_priority_min(SCHED_OTHER),
	 		sched_get_priority_max(SCHED_OTHER));
	return EXIT_SUCCESS;
}
 
