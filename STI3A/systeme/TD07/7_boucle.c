/***********************************************************************\
   exemple_boucle
 
   Programme d'exemple du livre "Developpement systeme en C sous Linux"
   
   (c) 2000,2005 - Christophe Blaess
 
\***********************************************************************/

	#include <stdlib.h>
	#include <unistd.h>

	int
main (void)
{
	alarm(5);
	while (1)
		;
	return EXIT_SUCCESS;
}
