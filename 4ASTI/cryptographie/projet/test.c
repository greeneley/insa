/* CREDITS : JEREMY BRIFFAUT */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "md5.h"
#include "crypt.h"

/**
 *  *
 *   * Usage : ./test fichier_a_chiffrer
 *    *
 *     */
int main(int argc, char *argv[]){

	if(argc < 2)
	{
		printf("Erreur d'utilisation !\n***USAGE : ./test <fichier_a_tester>\n");
		return 1;
	}

	/* =========================
    		    VARIABLES
       ========================= */	
	FILE* pFile;
	long lsize;
	char* texte;
	char* chiffre;
	char* dechiffre;
	int size;
	struct md5_ctx ctx;


	/* =========================
          MANIPULATION FICHIERS
       ========================= */	
	pFile = fopen( argv[1] , "rb");
	if (pFile == NULL)
	{
		printf("Erreur !\nLe fichier est introuvable.\n");
		return 1;
	}

	fseek (pFile, 0 , SEEK_END);  // Obtient la taille du fichier
	lsize = ftell (pFile);
	rewind (pFile);

	texte = (char*) malloc (lsize+1);   //Alloue mémoire pour le tampon, de la taille du fichier
	if (texte ==  NULL) 
	{
		printf("Erreur !\n Impossible d'allouer de la memoire pour le texte.\n");
		return 2;
	}

	fread (texte, 1, lsize, pFile); // copie fichier vers tampon
	texte[lsize]='\0';
        
	/* =========================
    		    TESTS
       ========================= */	
	int TAILLE_TEXTE_SOURCE = strlen(texte);
	size = (strlen(texte)+7)/8;

	chiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	dechiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	printf("----------------   XOR   ----------------\n");
	xor_crypt("une cle", texte,chiffre, TAILLE_TEXTE_SOURCE);
	xor_decrypt("une cle", chiffre, dechiffre, TAILLE_TEXTE_SOURCE);
	printf("'%s'\n",chiffre);
	printf("'%s'\n",dechiffre);
	printf("%s\n", strcmp(texte, dechiffre)==0?"ok":"NON");

	chiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	dechiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	printf("----------------   CESAR   ----------------\n");
	cesar_crypt(2, texte,chiffre);
	printf("'%s'\n",chiffre);
	cesar_decrypt(2, chiffre, dechiffre);
	printf("'%s'\n",dechiffre);
	printf("%s\n", strcmp(texte, dechiffre)==0?"ok":"NON");

	chiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	dechiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	printf("----------------   VIGENERE   ----------------\n");
	viginere_crypt("abc", texte,chiffre);
	viginere_decrypt("abc", chiffre, dechiffre);
	printf("'%s'\n",chiffre);
	printf("'%s'\n",dechiffre);
	printf("%s\n", strcmp(texte, dechiffre)==0?"ok":"NON");


	chiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	dechiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	printf("----------------   DES EBC   ----------------\n");
	des_crypt("chabada", texte,chiffre,size);
	des_decrypt("chabada", chiffre, dechiffre, size);
	printf("'%s'\n",chiffre);
	printf("'%s'\n",dechiffre);
	printf("%s\n", strcmp(texte, dechiffre)==0?"ok":"NON");

	chiffre = (char *)malloc(8+strlen(texte) * sizeof(char));
	dechiffre = (char *)malloc(8+strlen(texte) * sizeof(char)); 
	printf("----------------   3DES EBC   ----------------\n");
	tripledes_crypt("unecle", "dechiffrement", texte,chiffre, size);
	tripledes_decrypt("unecle", "dechiffrement", chiffre, dechiffre, size);
	printf("'%s'\n",chiffre);
	printf("'%s'\n",dechiffre);
	printf("%s\n", strcmp(texte, dechiffre)==0?"ok":"NON");

	chiffre = (char *)malloc(5*strlen(texte) * sizeof(char));
	dechiffre = (char *)malloc(5*strlen(texte) * sizeof(char)); 
	printf("----------------   RSA   ----------------\n");
	rsa_crypt(7, 5141, texte, chiffre, TAILLE_TEXTE_SOURCE);
	rsa_decrypt(4279, 5141,  chiffre, dechiffre);
	printf("'%s'\n",chiffre);
	printf("'%s'\n",dechiffre);
	printf("%s\n", strcmp(texte, dechiffre)==0?"ok":"NON");

	printf("----------------   MD5   ----------------\n");
	printf("'%s'\n", texte);
	MDFile(pFile, &ctx);

	fclose (pFile);  // ferme le flux et
	free(texte); // libère espace tampon

	return 0;
}
