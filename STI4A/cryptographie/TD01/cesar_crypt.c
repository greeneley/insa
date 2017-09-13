#include <stdio.h>
#include <stdlib.h>

/**
 *
 *  Usage cesar_crypt key input_file output_file
 *
 */

int main(int argc, char const *argv[])
{
	FILE *fi, *fo;
	int c, cle;

	/* a = 97, z = 122 */
	/* A = 65, Z = 90 */

	/* Verification du nombre d'arguments */
	if(argc != 4)
	{
		printf("Erreur de syntaxe\nUsage : cesar_crypt key input_file output_file\n");
		printf("key         : int\ninput_file  : filename\noutput_file : filename\n");
		return 1;
	}

	if(argv[1] != NULL)
	{
		if((fi = fopen(argv[2], "rb")) != NULL)
		{
			if((fo = fopen(argv[3], "wb")) != NULL)
			{
				/* Meme si l'on rentre un integer en console, on recoit un char* */
				/* Il faut donc utiliser atoi() pour avoir la valeur numerique */
				/* On modifie la cle pour qu'elle ne soit pas superieure a 25 */
				cle = atoi(argv[1])%26;

				while((c=getc(fi)) != EOF)
				{
					/* Si minuscules */
					if('a' <= c && c <= 'z')
					{
						/* Explications
						 * c+cle est le chiffrage attendu
						 * %97 donne la position par rapport a 'a'
						 * %26 exprime une circulaire dans l'alphabet
						 */
						c = (((c+cle)%97)%26)+'a';
					}
					/* Sinon si majuscule */
					else if('A' <= c && c <= 'Z')
					{
						c = (((c+cle)%65)%26)+'A';
					}
					/* Sinon rien : on reecrit directement */
					putc(c, fo);
				}
				fclose(fo);
			}
			else
			{
				printf("Erreur de creation du fichier output\n");
			}
			fclose(fi);
		}
		else
		{
			printf("Erreur de lecture du fichier input\n");
		}
	}
	else
	{
		printf("Erreur de lecture de la clé\n");
	}

	return 1;
}
