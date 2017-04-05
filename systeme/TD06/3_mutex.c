#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>



static void *	routine_threads (void * argument);
static	int	aleatoire	(int maximum);

// Creation d'un mutex
// Les mutex sont globales car c'est un verrou (tous les processus doivent pouvoir y acceder)
pthread_mutex_t	mutex_stdout = PTHREAD_MUTEX_INITIALIZER; // constante symbolique pour initialiser le verrou qui sert ouvert par defaut

int main (void)
{
	int		i;
	pthread_t	thread;
	
	for (i = 0; i < 10; i ++)
		pthread_create (& thread, NULL, routine_threads, (void *) i);

	pthread_exit (NULL);
}

static void * routine_threads (void * argument)
{
	int numero = (int) argument;
	int nombre_iterations;
	int i;
	nombre_iterations = aleatoire (6);
	for (i = 0; i < nombre_iterations; i ++) {
		sleep (aleatoire (3));
		// Empeche que deux threads envoient un signal sur l'ecran en meme temps
		// Si le verrou est deja ferme, il reste bloque a cette ligne
		pthread_mutex_lock (& mutex_stdout);
		fprintf (stdout, "Le thread numéro %d tient le mutex\n", numero);
		fprintf (stdout, "Le thread numéro %d est libere\n", numero);
		pthread_mutex_unlock (& mutex_stdout);
	}
	return (NULL);
}

static int aleatoire (int maximum)
{
	double d;
	d = (double) maximum * rand ();
	d = d / (RAND_MAX + 1.0);
	return ((int) d);
}	
