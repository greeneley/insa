/* Allez voir dans le répertoire /dev/shm le résultat */
#include <fcntl.h>
#include <sys/stat.h>
#include <semaphore.h>
#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>

int main() {
sem_t *smutex;
/* creation d’un semaphore mutex initialisé à 1 */
// le dernier argument est la valeur du compteur par defaut
if ((smutex = sem_open("monsem", O_CREAT |O_EXCL | O_RDWR , 0666, 1)) == SEM_FAILED) {
if (errno != EEXIST) {
perror("sem_open"); exit(1);
}
/* Semaphore deja créé, ouvrir sans O_CREAT */
smutex = sem_open("monsem", O_RDWR);
}
/* P sur smutex */
// Operateur P == decrementer
// Si la valeur du semaphore est 0, il va rester bloque car il ne peut pas decrementer
sem_wait(smutex);
sleep(30);
//for(;;);
/* V sur smutex */
// Operateur V == incrementer le semaphore
sem_post(smutex);
/* Fermer le semaphore */
sem_close(smutex);
/* Detruire le semaphore */
// Dans le descripteur de fichier, on fait disparaitre l'element que l'on vient de creer
sem_unlink("monsem");
return 0;
}
