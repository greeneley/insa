#include <sys/mman.h>
#include <stdio.h>
#include <string.h>


#define PAGE_SIZE 4096U

int main(int argc, char **argv){
  char buf1[48];
  char buf2[48];
 
  mprotect((void *)((unsigned int)buf1 & ~(PAGE_SIZE - 1)), 2 * PAGE_SIZE, PROT_READ | PROT_WRITE | PROT_EXEC);

 
  strcpy(buf1, "Ceci est un exemple de programme vulnerable.\n");
  strcpy(buf2 , "La vulnerabilite est de type buffer overflow.\n");
 
  printf("***\n%s%s***\n", buf1, buf2);
  
  if ( argc < 2 ) {
	  fprintf(stderr, "Ce programme doit comporter au moins un argument !\n");
	  return -1;
  }

  printf("\nDonnees utiles : pour strlen(argv[1]) = %d\n\tbuf1 : %p %d\n\tbuf2 : %p %d\n\n",
         strlen(argv[1]), &buf1, sizeof(buf1), &buf2, sizeof(buf2));

  printf("Copie du premier argument dans la variable buf2.\n\n");
  strcpy(buf2, argv[1]);

  printf("***\nbuf1 : %s\nbuf2 : %s\n***\n", buf1, buf2);

  return 0;
}
