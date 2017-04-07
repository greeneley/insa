/*
I) Intérêt de ce programme
Dans le livre de Christophe Blaess "programmation système en C sous Linux[Eyrolles],
 on peut lire les lignes suivantes :
"Nous avons réussi à optimiser la communication sur plusieurs canaux
simultanément grâce au multiplexage de select( ), et à laisser le programme se dérouler
normalement en réitérant de temps à autre les tentatives d'entrée-sortie.

Toutefois ces mécanismes ne sont pas suffisants dans le cas où on désire vraiment
recevoir ou émettre des données de manière suffisamment fiable, avec un mode opératoire
totalement asynchrone par rapport au reste du programme. En effet, lorsque select( ) –
ou un signal programmé par fcntl( ) – nous indique que des données sont disponibles
en lecture, tout ce que nous savons c'est que le descripteur est prêt à nous délivrer un
octet. Si nous désirons en lire plusieurs, l'appel read( ) peut être bloquant. Si on
bascule en lecture non bloquante, il faut de surcroît gérer un buffer interne pour recevoir
assez d'informations avant de les traiter.

...

Heureusement, on peut employer des procédures d'entrées-sorties totalement asynchrones
nous épargnant la gestion d'un buffer. On programme une opération de lecture ou
d'écriture, le noyau la démarre, et lorsqu'elle est terminée le processus est averti par
exemple par l'arrivée d'un signal. Durant le temps de l'opération d'entrée-sortie, le
programme est libre de faire ce que bon lui semble, utiliser le processeur, faire des appels-
système, dormir... "

II) Principe du programme

Une fois compilé, on lance le programme en passant en paramètre le chemin et le nom d'un fichier existant.
Le noyau va lire un bloc de 256 octets dans ce fichier et avertit le processus lorsque les 256 octets sont disponibles.



*/
	#include <aio.h> // Permet en particulier de disposer d'une structure aiocb permettant 
			 // d'informer le noyau sur le comportement attendu lors des accès io
			//asynchrones
	#include <errno.h>
	#include <stdio.h>
	#include <stdlib.h>
	#include <signal.h>
	#include <unistd.h>
	#include <sys/types.h>
	#include <sys/stat.h>
	#include <fcntl.h>
	#define SIGNAL_IO	(SIGRTMIN + 3)

	void
gestionnaire (int signum, siginfo_t * info, void * vide)
{
	struct aiocb * cb;
	ssize_t        nb_octets;
	if (info -> si_code == SI_ASYNCIO) {
		cb = info -> si_value . sival_ptr;
		if (aio_error (cb) == EINPROGRESS)
			return;
		nb_octets = aio_return (cb);
		fprintf (stdout, "Lecture 1 : %d octets lus \n", nb_octets);
	}
}

	void
thread (sigval_t valeur)
{
	struct aiocb * cb;
	ssize_t       nb_octets;
	cb = valeur . sival_ptr;
	if (aio_error (cb) == EINPROGRESS)
		return;
	nb_octets = aio_return (cb);
	fprintf (stdout, "Lecture 2 : %d octets lus \n", nb_octets);
}

	int
main (int argc, char * argv [])
{
	int              fd;
	struct aiocb     cb [3]; // Création d'un tableau de 3 variables type "struc aiocb" une par
				// type événement SIGEV attendu du noyau.
				// les différents cas :
				// a) SIGEV_NONE le noyau n'avertit pas de la disponibilité des données
				// b) SIGEV_SIGNAL le noyau avertit de la disponibilité des données par un signal (necessité d'installation d'une
				//    fonction gestionnaire de signal
				// c) Mise en route d'un Thread 
	char             buffer [256] [3];
	struct sigaction action;
	int              nb_octets;
	
	if (argc != 2) {
		fprintf (stderr, "Syntaxe : %s fichier\n", argv [0]);
		exit (1);
	}
	if ((fd = open (argv [1], O_RDONLY)) < 0) { // On ouvre le fichier à parcourir son nom et chemin sont dans "argv[1]
		perror ("open");
		exit (1);
	}
	action . sa_sigaction = gestionnaire;
	action . sa_flags     = SA_SIGINFO; // on active l'ajout d'informations complémentaires envoyées au gestionnaire
	sigemptyset (& action . sa_mask);
	if (sigaction (SIGNAL_IO, & action, NULL) < 0) { // on installe le gestionnaire "sigaction ()" déja vu en cours et en TD
							 // pour le signal "SIG_IO"
		perror ("sigaction");
		exit (1);
	}

	/* Lecture 0 :  Pas de notification */
	// Réglages cas numéro a) ci-dessus
	cb [0] . aio_fildes   = fd; // le file descripteur sur lequel on veut que le noyau fasse l'opération
	cb [0] . aio_offset   = 0; // A combien d'octets du début du fichier commence-t-il la lecture (ici au début du fichier)
	cb [0] . aio_buf      = buffer [0]; //Où il met les octets récupérés dans la zone d'adressage du processus
	cb [0] . aio_nbytes   = 256;   // Combien il doit récupérer d'octets si on n'arrive pas à la fin du fichier
	cb [0] . aio_reqprio  = 0; // avec quel niveau de priorité il le fait (seulement utile pour les threads)
	cb [0] . aio_sigevent . sigev_notify = SIGEV_NONE;// Comment il informe le processus qu'un bloc est disponible
								// ici "SIGEV_NONE", il informe pas c'est au processu de venir voir
								// quand il a le temps

	/* Lecture 1 : Notification par signal */
	// Réglages cas numéro b) ci-dessus
	cb [1] . aio_fildes   = fd;		// idem cas a) ci-dessus
	cb [1] . aio_offset   = 0;		// idem cas a) ci-dessus
	cb [1] . aio_buf      = buffer [1];	// idem cas a) ci-dessus
	cb [1] . aio_nbytes   = 256;		// idem cas a) ci-dessus
	cb [1] . aio_reqprio  = 0;		// idem cas a) ci-dessus
	cb [1] . aio_sigevent . sigev_notify = SIGEV_SIGNAL; //Comment il informe le processus qu'un bloc est disponible
								// ici "SIGEV_SIGNAL", le processus va recevoir un signal
	cb [1] . aio_sigevent . sigev_signo  = SIGNAL_IO;	// le signal que le noyau va envoyer "ici SIG_IO" pour lequel on a installé
								// le gestionnaire avec "sigaction()" ci-dessus
	cb [1] . aio_sigevent . sigev_value  . sival_ptr = & cb [1]; // on enverra au gestionnaire l'adresse mémoire de la structure aiocb qui a été utilisée
								// permet au gestionnaire de savoir si c'est bien le noyau  en utilisant "aiocb" qui a envoyé le signal
								// voir la ligne "if (info -> si_code == SI_ASYNCIO)" dans le gestionnaire ci-dessus
 
	
	/* Lecture 2 : Notification par thread */
	// Réglages cas numéro b) ci-dessus
	cb [2] . aio_fildes   = fd;
	cb [2] . aio_offset   = 0;
	cb [2] . aio_buf      = buffer [2];
	cb [2] . aio_nbytes   = 256;
	cb [2] . aio_reqprio  = 0; // valeur retirée à la priorité du thread s'il est SCHED_RR  ou SHCED_FIFO autrement dit quand il est temps réel
	cb [2] . aio_sigevent . sigev_notify = SIGEV_THREAD; //Comment le noyau informe le processus qu'un bloc est disponible
								// ici "SIGEV_THREAD", le noyau va lancer un "thread"
	cb [2] . aio_sigevent . sigev_notify_function   = thread; // Le nom de la fonction "thred à lancer"
	cb [2] . aio_sigevent . sigev_notify_attributes = NULL; // Atrributs du nouveau thread " Joignable vs détaché ; Scheduling : FIFO;RR;OTHER;" ici valeurs par défaut
	cb [2] . aio_sigevent . sigev_value  . sival_ptr = & cb [2]; // On passe au thread l'adresse de la structure que le noyau a utilisée

	/* Lancement des lectures */
	// On lance les lectures simultanément , rappel ces fonctions ne sont pas bloquantes !! 
	if ((aio_read (& cb  [0]) < 0) 
	 || (aio_read (& cb  [1]) < 0) 
	 || (aio_read (& cb  [2]) < 0)) {
		perror ("aio_read");
		exit (1);
	}
	fprintf (stdout, "Lectures lancées\n");// On confirme que le noyau travaille et que le processus continue son execution
	while ((aio_error (& cb [0]) == EINPROGRESS) // Toutes les secondes, on verifie que le noyau n'a produit aucune erreur sur chacune des lectures asynchrones
	    || (aio_error (& cb [1]) == EINPROGRESS)		
	    || (aio_error (& cb [2]) == EINPROGRESS))
		sleep (1);
	nb_octets = aio_return (& cb [0]); // on verifie que le bloc a bien été lu pour le cas a) car pas de notification
	fprintf (stdout, "Lecture O : %d octets lus \n", nb_octets);

usleep (500);	// Partie modifiée par mes soins, on remarque que la création du "thread" prend un peu de temps pour le noyau, donc je laisse un peu de temps avant le "return(0)" qui tue le processus
		// sinon le thread n'a pas le temps de s'executer;
		// faites des essais avec et sans le "usleep()" !! et concluez (rq : le comportement peut changer d'un calculateur à l'autre )

	return (0);
}
	
