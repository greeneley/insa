	#include <stdio.h>
	#include <stdlib.h>
	#include <signal.h>
	#include <pthread.h>

	static void * thread_compteur    (void * inutile);
	static void * thread_signaux     (void * inutile);

	static int               compteur       = 0;
	// On deux threads
	static pthread_mutex_t   mutex_compteur = PTHREAD_MUTEX_INITIALIZER;
	static pthread_cond_t    cond_compteur  = PTHREAD_COND_INITIALIZER;

	static pthread_t	thr_signaux;
	static pthread_t	thr_compteur;
	int
main (void)
{
	sigset_t masque;
	sigfillset (& masque);
	// On bloque tous les signaux (sauf les inblocables et les temps reels)
	pthread_sigmask (SIG_BLOCK, & masque, NULL);
	pthread_create (& thr_signaux, NULL, thread_signaux, NULL);
	pthread_create (& thr_compteur,  NULL, thread_compteur,  NULL);
	pthread_exit (NULL);
}

	static void *
thread_compteur (void * inutile)
{
	// Compteur est le nombre de signaux recus
	while (compteur < 5 )  {
		pthread_mutex_lock (& mutex_compteur);
		pthread_cleanup_push (pthread_mutex_unlock,
				      (void *) & mutex_compteur);
		// arg2 est le mutex a liberer par la fonction
		// la fonction va ensuite attendre la condition donnee en arg1
		// la fonction va ensuite refermer le mutex
		// Les mutex sont de la responsabilite du programmeur
		// La fonction ne peut pas savoir sur quelle mutex s'appliquer.
		pthread_cond_wait (& cond_compteur,
				   & mutex_compteur);
		fprintf (stdout, "Compteur : %d \n", compteur);
		pthread_cleanup_pop (1); /* mutex_unlock */
	}
	pthread_cancel (thr_signaux);
	return (NULL);
}

	static void *
thread_signaux (void * inutile)
{
	sigset_t masque;
	int      numero;
	
	sigemptyset (& masque);
	// On met a 1 les bits qui correspondent a SIGINT et SIGQUIT
	sigaddset (& masque, SIGINT);
	sigaddset (& masque, SIGQUIT);
	while (1) {
		// Attend les signaux du masqus et on recupere dans &numero le signal recu
		sigwait (& masque, & numero);
		// Verouillage du thrad qui s'occupe du compteur
		// car on a l'intention de modifier le contenu du compteur
		// Rappel : c'est bloquant tant que l'operation de fermeture de verrou n'a pas ete fait
		pthread_mutex_lock (& mutex_compteur);	
		switch (numero) {
			case SIGINT :
				compteur ++;
				break;
			case SIGQUIT :
				compteur --;
				break;
		}
		// On envoie le signal pour dire que la condition sur le compteur est arrive
		// On informe donc ici aux autres que le compteur a change d'etat
		// A qui ? A ceux qui se sont abonnees
		pthread_cond_signal (& cond_compteur);
		pthread_mutex_unlock (& mutex_compteur);
	}
	return (NULL);
}
