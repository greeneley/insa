#include <stdio.h>

/**
 *
 *  Usage xor_crypt key input_file output_file
 *
 */

int main(int argc, char const *argv[])
{
	FILE *fi, *fo;
	char *cp;
	int c;
	int cle;

	cle = 27;
	cle %= 26;

	/* a = 97, z = 122 */
	/* A = 65, Z = 90 */
	/* Ca fonctionne pour les minuscules */
	printf("%c\n", (('a'+cle)%97)+'a');

	/* 
	 *  Il faut maintenant bouclier pour encoder chaque caractere
	 *  + verifier que le caractere est bien une lettre codable
	 *  + verifier si c'est une minuscule ou une majuscule
	 */

	printf("%d\n", 'A');
	printf("%d\n", 'Z');
	printf("%d\n", 'z');

	return 1;
}
