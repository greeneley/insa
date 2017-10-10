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
 *  Chiffrement utilisant le ou exclusif
 */
void xor_crypt(char* key, char* texte, char* chiffre)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	FILE *fi, *fo;
	char *cp;
	int c;

	if((cp = key))
	{
		if((fi = fopen(texte, "rb")) != NULL)
		{
			if((fo = fopen(chiffre, "wb")) != NULL)
			{
				printf("Cryptage XOR...\n");
				while((c=getc(fi)) != EOF)
				{
					if(*cp == '\0')
					{
						cp = key;
					}
					c  ^= *(cp++);
					putc(c, fo);
				}
				fclose(fo);
			}
			fclose(fi);
		}
	}
}

/*
 *  Dechiffrement utilisant le ou exclusif
 */
void xor_decrypt(char* key, char* chiffre, char* clair)
{
	xor_crypt(key, chiffre, clair);
}


/*
 *  Chiffrement utilisant cesar
 */
void cesar_crypt(int decalage, char* texte, char* chiffre)
{
	if(texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	/* Declaration variables */
	FILE *fi, *fo;
	int c, cle;

	/* Initialisation variables */
	/* On s'assure que les nombres negatifs deviennent positifs */
	cle = ((decalage%26)+26)%26;

	if((fi = fopen(texte, "rb")) != NULL)
	{
		if((fo = fopen(chiffre, "wb")) != NULL)
		{
			printf("Encryptage Cesar...\n");
			while((c=getc(fi)) != EOF)
			{
				/* Cryptage du caractere */
				if('a' <= c && c <= 'z')
				{
					c = ((c-'a'+cle)%26)+'a';
				}
				else if('A' <= c && c <= 'Z')
				{
					c = ((c-'A'+cle)%26)+'A';
				}
				/* Finalement, ca cree des problemes de decryptage !
				else
				{
					// Sinon caractere special : on encrypte naivement
					c += decalage;
				}
				*/
				putc(c, fo);
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
	printf("Fin du programme\n");
}

/*
 *  Dechiffrement utilisant cesar
 */
void cesar_decrypt(int decalage, char* chiffre, char* clair)
{
	if(chiffre == NULL || clair == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	cesar_crypt(-decalage, chiffre, clair);
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
	int c, pos;
	int cleOk, decalage, taille;
	char *cle;
	//TableauInt* cleNormalise;

	/* Initialisation variables */
	cle = key;
	cleOk = 1;
	//cleNormalise = init_TableauInt(strlen(key));

	/* Verification illigibilite de la cle */
	while(*cle != '\0')
	{
		if ( !('A' <= *cle && *cle <= 'Z') 
			 || !('a' <= *cle && *cle <= 'z') )
		{
			printf("Erreur : la cle ne doit etre constituee que de lettres.\n");
			cleOk = 0;
			break;
		}
		cle++;
	}

	taille = strlen(texte);
	pos    = 0;

	if(cleOk)
	{
		if((fi = fopen(texte, "rb")) != NULL)
		{
			if((fo = fopen(chiffre, "wb")) != NULL)
			{
				printf("Encryptage Vigenere...\n");
				while((c=getc(fi)) != EOF)
				{
					/* Si on est en fin de cle, on recommence au debut */
					if(pos == taille)
					{
						pos = 0;
					}
					decalage = texte[pos];

					/* Cryptage du caractere */
					if('a' <= c && c <= 'z')
					{
						c = ((c-'a'+decalage)%26)+'a';
					}
					else if('A' <= c && c <= 'Z')
					{
						c = ((c-'A'+decalage)%26)+'A';
					}
					/* Finalement non car on cree des problemes de decyptage sinon
					else
					{
						// Sinon caractere special : on encrypte naivement
						c += decalage;
					}
					*/
					putc(c, fo);
					pos++;
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
				printf("Decryptage Vigenere...\n");
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
					/* Finalement, ca cree des problemes de decodage !
					else
					{
						// Sinon caractere special : on decrypte naivement
						c -= decalage;
					}
					*/
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
	//xor_crypt("djzdu", "plaintext", "encrypted");
	//xor_decrypt("djzdu", "encrypted", "uncrypted");
	//cesar_crypt(67, "plaintext", "encrypted");
	//cesar_decrypt(67, "encrypted", "uncrypted");
	//vigenere_crypt("BaCheLIEr", "plaintext", "encrypted");
	//vigenere_decrypt("BaCheLIEr", "encrypted", "uncrypted");

	char* test;
	test = malloc(sizeof(char)*10);
	*test = 'l';
	*(test+1) = 'b';
	printf("%s", test);
	return 0;
}