/*
 *  ATTENTION : Tous mes algorithmes codent les espaces
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "crypto.h"


/* A = 65, Z = 90 */
/* a = 97, z = 122 */

TableauInt* init_TableauInt(int taille)
{
	TableauInt* newTab;
	newTab = malloc(sizeof(TableauInt));

	newTab->taille = taille;
	newTab->pos    = 0;
	newTab->tab    = malloc(sizeof(int)*taille);

	return newTab;
}


void add_TableauInt(TableauInt* tableau, int valeur)
{
	if(tableau->pos == tableau->taille)
	{
		printf("Erreur : taille maximale atteinte\n");
	}
	else
	{
		tableau->tab[tableau->pos] = valeur;
		tableau->pos++;
	}
}

void free_TableauInt(TableauInt* toDestroy)
{
	free(toDestroy->tab);
	free(toDestroy);
}

/*
 *  Chiffrement utilisant vigenere
 */
void vigenere_crypt(char* key, char* texte, char* chiffre)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	/* Declaration variables */
	FILE *fi, *fo;
	int c, cleOk, decalage;
	char *cle;
	TableauInt* cleNormalise;

	/* Initialisation variables */
	cle = key;
	cleOk = 1;
	cleNormalise = init_TableauInt(strlen(key));

	/* Verification illigibilite de la cle */
	while(*cle != '\0')
	{
		if('A' <= *cle && *cle <= 'Z')
		{
			add_TableauInt(cleNormalise, *cle-'A');
		}
		else if('a' <= *cle && *cle <= 'z')
		{
			add_TableauInt(cleNormalise, *cle-'a');
		}
		else
		{
			printf("Erreur : la cle ne doit etre constituee que de lettres.\n");
			cleOk = 0;
			break;
		}
		cle++;
	}

	if(cleOk)
	{
		if((fi = fopen(texte, "rb")) != NULL)
		{
			if((fo = fopen(chiffre, "wb")) != NULL)
			{
				printf("Encryptage...\n");
				while((c=getc(fi)) != EOF)
				{
					/* Si on est en fin de cle, on recommence au debut */
					if(cleNormalise->pos == cleNormalise->taille)
					{
						cleNormalise->pos = 0;
					}
					decalage = cleNormalise->tab[cleNormalise->pos];

					/* Cryptage du caractere */
					if('a' <= c && c <= 'z')
					{
						c = ((c-'a'+decalage)%26)+'a';
					}
					else if('A' <= c && c <= 'Z')
					{
						c = ((c-'A'+decalage)%26)+'A';
					}
					else
					{
						/* Sinon caractere special : on encrypte naivement */
						c += decalage;
					}
					putc(c, fo);
					cleNormalise->pos++;
				}
				fclose(fo);
			}
			else
			{
				printf("Erreur d'ecriture du fichier output.\n");
			}
			fclose(fi);
		}
		else
		{
			printf("Erreur de lecture du fichier input.\n");
		}
	}
	else
	{
		printf("Erreur de lecture de la cle.\n");
	}

	printf("Fin du programme\n");
	free_TableauInt(cleNormalise);
}

/*
 *  Dechiffrement utilisant vigenere
 *  
 *  Decrypter revient a faire un deplacement vers la gauche.
 *  L'operateur % ne semble pas gerer les signes negatifs en C.
 *  On effectue donc une operation equivalent avec + et - pour se passer du %.
 */
void vigenere_decrypt(char* key, char* chiffre, char* clair)
{
	if(key == NULL || chiffre == NULL || clair == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	/* Declaration variables */
	FILE *fi, *fo;
	int c, cleOk, decalage;
	char *cle;
	TableauInt* cleNormalise;

	/* Initialisation variables */
	cle = key;
	cleOk = 1;
	cleNormalise = init_TableauInt(strlen(key));

	/* Verification illigibilite de la cle */

	while(*cle != '\0')
	{
		if('A' <= *cle && *cle <= 'Z')
		{
			add_TableauInt(cleNormalise, *cle-'A');
		}
		else if('a' <= *cle && *cle <= 'z')
		{
			add_TableauInt(cleNormalise, *cle-'a');
		}
		else
		{
			printf("Erreur : la cle ne doit etre constituee que de lettres.\n");
			cleOk = 0;
			break;
		}
		cle++;
	}

	if(cleOk)
	{
		if((fi = fopen(chiffre, "rb")) != NULL)
		{
			if((fo = fopen(clair, "wb")) != NULL)
			{
				printf("Decryptage...\n");
				while((c=getc(fi)) != EOF)
				{
					/* Si on est en fin de cle, on recommence au debut */
					if(cleNormalise->pos == cleNormalise->taille)
					{
						cleNormalise->pos = 0;
					}
					decalage = cleNormalise->tab[cleNormalise->pos];

					/* Decryptage du caractere 
					 * C'est ici que l'on effectue un % artisanal avec 26-decalage
					 */
					if('a' <= c && c <= 'z')
					{
						c = ((c-'a'+(26-decalage))%26)+'a';
					}
					else if('A' <= c && c <= 'Z')
					{
						c = ((c-'A'+(26-decalage))%26)+'A';
					}
					else
					{
						/* Sinon caractere special : on decrypte naivement */
						c -= decalage;
					}
					putc(c, fo);
					cleNormalise->pos++;
				}
				fclose(fo);
			}
			else
			{
				printf("Erreur d'ecriture du fichier output.\n");
			}
			fclose(fi);
		}
		else
		{
			printf("Erreur de lecture du fichier input.\n");
		}
	}
	else
	{
		printf("Erreur de lecture de la cle.\n");
	}

	printf("Fin du programme\n");
	free_TableauInt(cleNormalise);
}

int main(int argc, char const *argv[])
{
	
	vigenere_crypt("BaCheLIEr", "plaintext", "encrypted");
	vigenere_decrypt("BaCheLIEr", "encrypted", "uncrypted");

	return 0;
}