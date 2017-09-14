#include <stdio.h>
#include <stdlib.h>

/**
 *
 *  Usage cesar_crypt key input_file output_file [decrypt]
 *
 */

int main(int argc, char const *argv[])
{
	FILE *fi, *fo;
	int c, cle;

	/* a = 97, z = 122 */
	/* A = 65, Z = 90 */

	/* Verification du nombre d'arguments */
	if(argc != 4 && argc != 5)
	{
		printf("Erreur de syntaxe\nUsage : cesar_crypt key input_file output_file [decrypt]\n");
		printf("key         : integer\ninput_file  : filename\noutput_file : filename\n");
		printf("decrypt     : str [optional]\n");
		return 1;
	}

	if(argv[1] != NULL)
	{
		if((fi = fopen(argv[2], "rb")) != NULL)
		{
			if((fo = fopen(argv[3], "wb")) != NULL)
			{
				/* 
				 * Meme si l'on rentre un integer en console, on recoit un char*
				 * Il faut donc utiliser atoi() pour avoir la valeur numerique
				 * On modifie la cle pour qu'elle ne soit pas superieure a 25 
				 */

				if(argc == 4)
				{
					cle = atoi(argv[1])%26;
					printf("Encryptage...\n");
				}
				else
				{
					cle = -(atoi(argv[1])%26);
					printf("Decryptage...\n");
				}

				while((c=getc(fi)) != EOF)
				{
					if('a' <= c && c <= 'z')
					{
						/* Explications
						 * c+cle est le chiffrage/dechiffrage attendu
						 * %97 donne la position par rapport a 'a'
						 * %26 exprime une circulaire dans l'alphabet
						 */
						c = (((c+cle)%97)%26)+'a';
					}
					else if('A' <= c && c <= 'Z')
					{
						c = (((c+cle)%65)%26)+'A';
					}
					else if(c != ' ') /* Trop facile casser si on code les espaces */
					{
						/* Sinon caractere speciale : on encrypte naivement */
						c += cle;
					}
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

	printf("Fin du programme\n");
	return 1;
}
