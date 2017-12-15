#include <stdio.h>
#include <string.h>


int main(int argc, char **argv, char **env)
{
    char text[16];
    int a = 728;
    int b = -65432;
    int c = 1234;
    int d = 0xe;

    if (argc < 2)
	return -1;

    strncpy(text, argv[1], sizeof(text) - 1);
    
    printf("Elements de la fonction :\n-------------------------\n");

    printf("main       @ %p\n\n", &main);

    printf("env        @ %p\n", env);
    printf("argv1      @ %p\n", &argv[1]);
    printf("argv0      @ %p\n", &argv[0]);
    printf("argv       @ %p\n", argv);
    printf("argc       @ %p\n", &argc);

    printf("\nVariables :\n-----------\n");

    printf("a\t%d\t0x%08x\t@ %p\n", a, a, &a);
    printf("b\t%d\t0x%08x\t@ %p\n", b, b, &b);
    printf("c\t%d\t0x%08x\t@ %p\n", c, c, &c);
    printf("d\t%d\t0x%08x\t@ %p\n", d, d, &d);
    printf("Valeurs  : a = %d ; b = %d ; c = %d ; d = %d\nAdresses : a @ %p ; b @ %p ; c @ %p ; d @ %p\n", a, b, c, d, &a, &b, &c, &d);

    printf("\nExecution :\n-----------\n");

    printf("Affichage de la valeur de 'text' :\n");
    printf(text);
    
    printf("\n\nObservation :\n-------------\n");

    printf("Valeurs  : a = %d ; b = %d ; c = %d ; d = %d\n", a, b, c, d);
    printf("Adresses : a @ %p ; b @ %p ; c @ %p ; d @ %p\n", &a, &b, &c, &d);

    return(0);
}
