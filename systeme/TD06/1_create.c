#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

#define NB_THREADS	5

void * fn_thread (void * numero);

static int compteur = 0;

int main (void)
{

	pthread_t thread [NB_THREADS];
	int       i;
	int       ret;

	for (i = 0; i < NB_THREADS; i ++)
		/*Premier parametre : recuperation du PID
			Comme on a plusieurs threads, on les stocj dans un tableau
		Deuxieme parametre : les attributs. Ici NULL == heritage du thread principal.
			Le soucis c'est qu'on ne sait pas comment le thread a ete cree (joignable ou pas)
			Cependant, un thread est joignable par defau
		Troisieme : la fonction qui demarre le thread
		Dernier : adresse memoire qui indique les donnees a passer a la fonction
			Ici on passe la valeur de i, on doit cast en void* car i est normalement un int
		*/
		if ((ret = pthread_create (& thread [i], 
                                    NULL, 
                                    fn_thread, 
                                    (void *) i)) != 0) {
			fprintf (stderr, "%s", strerror (ret));
			// S'il y a une erreur, le processus tue tout le monde
			exit (1);
		}

	while (compteur < 40) { // compteur est une var globale
		fprintf (stdout, "main : compteur = %d\n", compteur);
		sleep (1);
	}
	for (i = 0; i < NB_THREADS; i ++)
		//pthread_join attend que tous les threads finissent
		//la fonction est bloquante
		pthread_join (thread [i], NULL);

	return (0);
}

void * fn_thread (void * num)
{
	int numero = (int) num; //numero du thread par transtypage
	while (compteur < 40) {
		usleep ((numero+1) * 100000);
		compteur ++;
		fprintf (stdout, "Thread %d : compteur = %d\n",
                                 numero, compteur);
	}
	// informe les threads qui sont dans un pthread_join qu'ils ont termine
	pthread_exit (NULL);
}
