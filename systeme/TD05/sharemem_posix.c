/* allez voir dans /dev/shm le résultat
et utiliser la commande xxd pour visualiser le contenu de la mémoire paratagée */

#include <fcntl.h>
#include <sys/stat.h>
#include <semaphore.h>
#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/mman.h>

int *sp;
int main() {
int fd;
/* Creer le segment monshm, ouverture en R/W */
if ((fd = shm_open("monshm", O_RDWR | O_CREAT, 0600)) == -1) {
perror("shm_open");
exit(1);}
/* Allouer au segment une taille pour stocker un entier */
if (ftruncate(fd, sizeof(int)) == -1) {
perror("ftruncate");
exit(1);}
/* “mapper” le segment en R/W partagé */
if ((sp = mmap(NULL, sizeof(int), PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0))== MAP_FAILED) {
perror("mmap");
exit(1);}
/* Accès au segment */
*sp = 10;
for(;;);
/* “detacher” le segment */
munmap(sp, sizeof(int));
/* detruire le segment */
shm_unlink("monshm");
return 0;
}

