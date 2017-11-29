#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "crypt.h"
#include "encrypt.h"
#include <math.h>


/*****************************************************************
 *                                                               *
 *  ------------------ VARIABLES GLOBALES ---------------------  *
 *                                                               *
 ****************************************************************/
#define A_TO_a 32
#define a_TO_A -32
#define TAILLE_BLOCK_DES 8


/*****************************************************************
 *                                                               *
 *  --------------------------- XOR ---------------------------  *
 *                                                               *
 ****************************************************************/


/**
 * \fn 		void xor_crypt(char * key, char * texte, char* chiffre, int taille)
 * \brief 	Chiffrement utilisant le ou exclusif (XOR) muni d'une cle.
 *
 * \param 	char* key     La cle de chiffrement.
 * \param 	char* texte   Le texte source a chiffrer.
 * \param   char* chiffre Le texte ou stocker le chiffrement.
 * \param   int   taille  La taille du texte d'origine.
 *
 * \return 	Void
*/
void xor_crypt(char * key, char * texte, char* chiffre, int taille)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.\n");
		return;
	}

	/* ===== Variables ===== */
	char *cp; // iterateur de cle
	int c;    // caractere a chiffrer
	int i;    // iterateur de texte

	/* ===== Algorithme ===== */
	i      = 0;

	/* Le but de l'algorithme est simple.

	   On va XOR chaque lettre avec une 
	      lettre de la cle de chiffrement.

	   Si l'on est a la fin de la cle,
	      on retourne au debut de la cle.
	*/
	if((cp = key))
	{
		for(i=0; i<taille; i++)
		{
			c = texte[i];
			if(*cp == '\0')
			{
				cp = key;
			}
			c  ^= *(cp++);
			chiffre[i] = c;
		}
	}
}

/**
 * \fn 		void xor_decrypt(char * key, char * texte, char* chiffre, int taille)
 * \brief 	Dechiffrement utilisant le ou exclusif (XOR) muni d'une cle.
 *
 * \param 	char* key     La cle de chiffrement.
 * \param 	char* texte   Le texte source a dechiffrer.
 * \param   char* chiffre Le texte ou stocker le dechiffrement.
 * \param   int   taille  La taille du texte d'origine.
 *
 * \return 	Void
*/
void xor_decrypt(char * key, char * texte, char* chiffre, int taille)
{
	xor_crypt(key, texte, chiffre, taille);
}


/****************************************************************
 *                                                               *
 *  -------------------------- Cesar --------------------------  *
 *                                                               *
 ****************************************************************/


/**
 * \fn 		cesar_crypt(int decallage, char * texte, char* chiffre)
 * \brief 	Chiffrement de Cesar.
 *
 * \param 	int   decallage La variation d'index dans l'alphabet.
 * \param 	char* texte     Le texte source a chiffrer.
 * \param   char* chiffre   Le texte ou stocker le chiffrement.
 *
 * \return 	Void
*/
void cesar_crypt(int decallage, char * texte, char* chiffre)
{
	if(texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.\n");
		return;
	}

	/* === Variabless === */
	int c;
	int i;
	int cle; // Le nombre de decallages

	/* On s'assure que les nombres negatifs deviennent positifs 
	 * Et que la cle soit dans [[0,25]]
	 */
	cle = ((decallage%26)+26)%26;

	/* Pour le chiffrement :
	 *    On regarde si la lettre est minuscule ou majuscule
	 *    On calcule la position de la lettre chifrre par raport a 'a' ou 'A'
	 *    On ajoute 'a' ou 'A' pour retourner dans l'alphabet
	 */
	i = 0;
	while(texte[i] != '\0')
	{
		c = texte[i];
		if('a' <= c && c <= 'z')
		{
			c = ((c-'a'+cle)%26)+'a';
		}
		else if('A' <= c && c <= 'Z')
		{
			c = ((c-'A'+cle)%26)+'A';
		}
		chiffre[i] = c;
		i++;
	}
}


/**
 * \fn 		cesar_decrypt(int decallage, char * texte, char* chiffre)
 * \brief 	Dechiffrement de Cesar.
 *
 * \param 	int   decallage La variation d'index dans l'alphabet.
 * \param 	char* texte     Le texte source a dehiffrer.
 * \param   char* chiffre   Le texte ou stocker le dechiffrement.
 *
 * \return 	Void
*/
void cesar_decrypt(int decallage, char * texte, char* chiffre)
{
	if(texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.\n");
		return;
	}

	/* Meme principe que le chiffrement.
	 * Cette fois neanmoins on chiffre vers l'arriere
	 *   pour retrouver le caractere d'origine.
	 */
	cesar_crypt(-decallage, texte, chiffre);
}


/****************************************************************
 *                                                               *
 *  ------------------------ VIGENERE -------------------------  *
 *                                                               *
 ****************************************************************/


/**
 * \fn 		void viginere_crypt(char * key, char * texte, char* chiffre)
 * \brief 	Chiffrement de Vigenere.
 *
 * \param 	char* key     La cle de chiffrement.
 * \param 	char* texte   Le texte source a chiffrer.
 * \param   char* chiffre Le texte ou stocker le chiffrement.
 *
 * \return 	Void
*/
void viginere_crypt(char * key, char * texte, char* chiffre)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.\n");
		return;
	}

	/* ===== Variables ===== */
	int c, i;
	int pos;              // Iterateur de cle
	int decalage, taille;
	char* test;
	test = key;

	/* ===== Verification eligibilite de la cle ===== */
	while(*test != '\0')
	{
		if ( !('A' <= *test && *test <= 'Z') 
			 && !('a' <= *test && *test <= 'z') )
		{
			printf("Erreur : la cle ne doit etre constituee que de lettres.\n");
			return;
		}
		test++;
	}

	/* Initialisation variables */
	taille = strlen(key);
	pos    = 0;
	i      = 0;

	/* ===== Algorithme ===== */
	while(texte[i] != '\0')
	{
		c = texte[i];

		/* Si on est en fin de cle, on recommence au debut */
		if(pos == taille)
		{
			pos = 0;
		}
		decalage = key[pos];

		/* ==============================================
		   ========= Chiffrement du caractere ===========
		   ==============================================

		 * c-'lettre' correspond a l'index de c dans l'alphabet
		 * decalage-'lettre' correspond a l'index par rapport a 'lettre'
		 * la somme des deux donne la lettre chiffree 
		*/
		if('a' <= c && c <= 'z')
		{
			if('A' <= decalage && decalage <= 'Z')
			{
				/* Conversion majuscule en minuscule */
				decalage += A_TO_a;
			}
			c = ((c-'a'+decalage-'a')%26)+'a';
		}
		else if('A' <= c && c <= 'Z')
		{
			if('a' <= decalage && decalage <= 'z')
			{
				/* Conversion minuscule en majuscule */
				decalage += a_TO_A;
			}
			c = ((c-'A'+decalage-'A')%26)+'A';
		}

		chiffre[i] = c;
		pos++;
		i++;
	}
}

/**
 * \fn 		void viginere_decrypt(char * key, char * texte, char* chiffre)
 * \brief 	Dechiffrement de Vigenere.
 *
 * \param 	char* key     La cle de dechiffrement.
 * \param 	char* texte   Le texte source a dechiffrer.
 * \param   char* chiffre Le texte ou stocker le dechiffrement.
 *
 * \return 	Void
*/
void viginere_decrypt(char * key, char * texte, char* chiffre)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.\n");
		return;
	}

	/* Declaration variables */
	int c, i, pos;
	int decalage, taille;

	/* On suppose que la cle est correcte pour dechiffrer */

	/* Initialisation variables */
	taille = strlen(key);
	pos    = 0;
	i      = 0;

	while(texte[i] != '\0')
	{
		c = texte[i];

		/* Si on est en fin de cle, on recommence au debut */
		if(pos == taille)
		{
			pos = 0;
		}
		decalage = key[pos];

		/* ==============================================
		   ======== Dechiffrement du caractere ==========
		   ==============================================

		 * Au lieu de faire +(decalage-'lettre')
		 * on fait -(decalage-'lettre')
		 * pour revenir vers la lettre originelle.
		 * On doit s'assurer que le resultat est positif
		 * donc +26 a l'operation precedente.
		 * Et enfin un %26 si la soustraction etait deja
		 * positive pour ne pas depasser 26.
		*/
		if('a' <= c && c <= 'z')
		{
			if('A' <= decalage && decalage <= 'Z')
			{
				decalage += A_TO_a;
			}
			c = ((c-decalage+26)%26) + 'a';
		}
		else if('A' <= c && c <= 'Z')
		{
			if('a' <= decalage && decalage <= 'z')
			{
				decalage += a_TO_A;
			}
			c = ((c-decalage+26)%26) + 'A';
		}

		chiffre[i] = c;
		pos++;
		i++;
	}
}


/****************************************************************
 *                                                               *
 *  ------------------------ DES ECB---------------------------  *
 *                                                               *
 ****************************************************************/


/**
 * \fn 		void des_crypt(char * key, char * texte, char* chiffre, int size)
 * \brief 	Chiffrement DES ECB.
 *
 * \param 	char* key     La cle de chiffrement.
 * \param 	char* texte   Le texte source a chiffrer.
 * \param   char* chiffre Le texte ou stocker le chiffrement.
 * \param   int   size    Le nombre de blocs a chiffrer.
 *
 * \return 	Void
*/
void des_crypt(char * key, char * texte, char* chiffre, int size)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : l'une des chaines est NULL\n");
		return;
	}

	char* ptrTxt;
	char* ptrChiffre;
	int i;

	/* Initialisation des variables */
	ptrTxt     = texte;
	ptrChiffre = chiffre;

	/* Chiffrement DES ECB */
	for(i=0; i<size; i++)
	{
		des_encipher((unsigned char*)ptrTxt, (unsigned char*)ptrChiffre, (unsigned char*)key);
		ptrTxt     += sizeof(char)*TAILLE_BLOCK_DES;
		ptrChiffre += sizeof(char)*TAILLE_BLOCK_DES;
	}
}


/**
 * \fn 		void des_decrypt(char * key, char * texte, char* chiffre, int size)
 * \brief 	Dechiffrement DES ECB.
 *
 * \param 	char* key     La cle de dechiffrement.
 * \param 	char* texte   Le texte source a dechiffrer.
 * \param   char* chiffre Le texte ou stocker le dechiffrement. 
 * \param   int   size    Le nombre de blocs a dechiffrer.
 *
 * \return 	Void
*/
void des_decrypt(char * key, char * texte, char* chiffre, int size)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : l'une des chaines est NULL\n");
		return;
	}

	char* ptrChiffre;
	char* ptrDechiffre;
	int i;

	/* Initialisation des variables */
	ptrChiffre   = texte;
	ptrDechiffre = chiffre;

	/* Dechiffrement DES ECB */
	for(i=0; i<size; i++)
	{
		des_decipher((unsigned char*)ptrChiffre, (unsigned char*)ptrDechiffre, (unsigned char*)key);
		ptrChiffre   += sizeof(char)*TAILLE_BLOCK_DES;
		ptrDechiffre += sizeof(char)*TAILLE_BLOCK_DES;
	}
}


/****************************************************************
 *                                                               *
 *  --------------------------- 3DES --------------------------  *
 *                                                               *
 ****************************************************************/


/**
 * \fn 		void tripledes_crypt(char * key1, char * key2, char * texte, char* chiffre,int size)
 * \brief 	Chiffrement 3DES ECB.
 *
 * \param 	char* key1    La 1ere cle de chiffrement. 
 * \param 	char* key2    La 2nde cle de chiffrement.
 * \param 	char* texte   Le texte source a chiffrer.
 * \param   char* chiffre Le texte ou stocker le chiffrement. 
 * \param   int   size    Le nombre de blocs a chiffrer.
 *
 * \return 	Void
*/
void tripledes_crypt(char * key1, char * key2, char * texte, char* chiffre,int size)
{
	if(key1==NULL || key2==NULL || texte==NULL || chiffre==NULL)
	{
		printf("Erreur : l'une des chaines est NULL\n");
		return;
	}

	/* ===== Variables ===== */
	char* tmp_DES1;
	char* tmp_DES2;
	char* ptrTxt;
	char* ptrChiffre;
	int i;

	/* ===== Initialisation ===== */
	tmp_DES1   = malloc(TAILLE_BLOCK_DES*sizeof(char));
	tmp_DES2   = malloc(TAILLE_BLOCK_DES*sizeof(char));

	ptrTxt     = texte;
	ptrChiffre = chiffre;

	// Chiffrement 3DES ECB
	for(i=0; i<size; i++)
	{
		des_encipher((unsigned char*)ptrTxt, (unsigned char*)tmp_DES1, (unsigned char*)key1);
		des_decipher((unsigned char*)tmp_DES1, (unsigned char*)tmp_DES2, (unsigned char*)key2);
		des_encipher((unsigned char*)tmp_DES2, (unsigned char*)ptrChiffre, (unsigned char*)key1);
		ptrTxt     += sizeof(char)*TAILLE_BLOCK_DES;
		ptrChiffre += sizeof(char)*TAILLE_BLOCK_DES;
	}

	free(tmp_DES1);
	free(tmp_DES2);
}


/**
 * \fn 		void tripledes_decrypt(char* key1, char* key2, char* texte, char* chiffre, int size)
 * \brief 	Dechiffrement 3DES ECB.
 *
 * \param 	char* key     La 1ere cle de dechiffrement.
 * \param 	char* key     La 2nde cle de dechiffrement.
 * \param 	char* texte   Le texte source a dechiffrer.
 * \param   char* chiffre Le texte ou stocker le dechiffrement. 
 * \param   int   size    Le nombre de blocs a dechiffrer.
 *
 * \return 	Void
*/
void tripledes_decrypt(char* key1, char* key2, char* texte, char* chiffre, int size)
{
	if(key1==NULL || key2==NULL || texte==NULL || chiffre==NULL)
	{
		printf("Erreur : l'une des chaines est NULL\n");
		return;
	}

	/* ===== Variables ===== */
	char* tmp_DES1;
	char* tmp_DES2;
	char* ptrTxt;
	char* ptrChiffre;
	int i;

	/* ===== Initialisation ===== */
	tmp_DES1   = malloc(TAILLE_BLOCK_DES*sizeof(char));
	tmp_DES2   = malloc(TAILLE_BLOCK_DES*sizeof(char));

	ptrTxt     = texte;
	ptrChiffre = chiffre;

	// Dehiffrement 3DES ECB
	for(i=0; i<size; i++)
	{
		des_decipher((unsigned char*)ptrTxt, (unsigned char*)tmp_DES1, (unsigned char*)key1);
		des_encipher((unsigned char*)tmp_DES1, (unsigned char*)tmp_DES2, (unsigned char*)key2);
		des_decipher((unsigned char*)tmp_DES2, (unsigned char*)ptrChiffre, (unsigned char*)key1);
		ptrTxt     += sizeof(char)*TAILLE_BLOCK_DES;
		ptrChiffre += sizeof(char)*TAILLE_BLOCK_DES;
	}

	free(tmp_DES1);
	free(tmp_DES2);
}


/****************************************************************
 *  CREDITS JEREMY BRIFFAUT                                      *
 *  -------------------------- modexp -------------------------  *
 *                                                               *
 ****************************************************************/

static Huge modexp(Huge a, Huge b, Huge n) {
	
	Huge               y;
	
	/****************************************************************
	 *                                                               *
	 *  Calcule (pow(a, b) % n) avec la méthode du carré binaire     *
	 *  et de la multiplication.                                     *
	 *                                                               *
	 ****************************************************************/
	
	y = 1;
	
	while (b != 0) {
		
		/*************************************************************
		 *                                                            *
		 *  Pour chaque 1 de b, on accumule dans y.                   *
		 *                                                            *
		 *************************************************************/
		
		if (b & 1)
			y = (y * a) % n;
		
		/*************************************************************
		 *                                                            *
		 *  Élévation de a au carré pour chaque bit de b.             *
		 *                                                            *
		 *************************************************************/
		
		a = (a * a) % n;
		
		/*************************************************************
		 *                                                            *
		 *  On se prépare pour le prochain bit de b.                  *
		 *                                                            *
		 *************************************************************/
		
		b = b >> 1;
		
	}
	
	return y;
	
}

/**
 * \fn 		void rsa_crypt(int e, int n, char * texte, char* chiffre, int size)
 * \brief 	Chiffrement RSA. Base sur l'algorithme de JEREMY BRIFFAUT.
 *
 * \param 	int   e       Exposant de chiffrement. 
 * \param 	int   n       Module de chiffrement.
 * \param 	char* texte   Le texte source a dechiffrer.
 * \param   char* chiffre Le texte ou stocker le dechiffrement. 
 * \param   int   size    Taille du texte d'origine avant chiffrement.
 *
 * \return 	Void
*/
void rsa_crypt(int e, int n, char * texte, char* chiffre, int size)
{
	/* ===== Variables ===== */
	int i;
	char c;
	Huge buf=0; // Buffer pour la fonction modexp
	*chiffre='\0';

	/* ===== Algorithmes ===== */
	/* Pour l'algo :
	 * On va se deplacer caractere par caractere dans le texte source
	 *    puis on va calculer le chiffrement RSA de ce caractere.
	 * On concatene ce caractere avec le pointeur sur la destination (chiffre).
	 * On fait donc un RSA sur chaque caractere, dans leur format decimal.
	 */ 
	for(i=0;i<(size);i++)
	{
		c   = *(texte+i);
		buf = (Huge)c;
		/* Le caractere '$' sert de separateur pour le dechiffrement */
		sprintf(chiffre+strlen(chiffre),"%ld$%c",modexp(buf, e, n),'\0');
	}
}

/**
 * \fn 		void rsa_decrypt(int d, int n, char * texte, char* chiffre)
 * \brief 	Dehiffrement RSA. Base sur l'algorithme de JEREMY BRIFFAUT.
 *
 * \param 	int   e       Exposant de dechiffrement. 
 * \param 	int   n       Module de chiffrement.
 * \param 	char* texte   Le texte source a dechiffrer.
 * \param   char* chiffre Le texte ou stocker le dechiffrement. 
 * \param   int   size    Taille du texte d'origine avant chiffrement.
 *
 * \return 	Void
*/
void rsa_decrypt(int d, int n, char * texte, char* chiffre)
{

	/* ===== Variables ====== */
	int puiss;  // Puissance de dix
	int tmp;    // Variable pour la conversion caractere->numerique
	int i;
	int nombre; // Variable pour stocker un nombre sous forme %c
	char* ptr_fin;
	char* ptr_debut;

	/* ===== Initialisation ===== */
	ptr_fin = texte;
	ptr_debut = texte;
	*chiffre = '\0';

	/* ===== Algorithmes ===== */
	/* Pour l'algorithmes :
	 * On va chercher a parcourir toute la chaine.
	 * Quand on rencontre '$', on regarde tout ce qui est avant,
	 *   pour convertire les char numeriques en nombre numeriques.
	 * On applique ensuite le dechiffrement sur ce nombre.
	 */
	while((*ptr_fin) != '\0')
	{
		if((*ptr_fin) == '$')
		{
			puiss = 1;
			tmp   = 0;
			for(i = 0; i<(ptr_fin-ptr_debut); i++)
			{
				/* On converti les caracteres numeriques en valeur numerique
				[[0,9]] == [[48,57]] */
				nombre = *(ptr_fin - i - 1) - 48;

				tmp = tmp + ( nombre * puiss );
				puiss *= 10;
			}
			sprintf(chiffre+strlen(chiffre),"%c%c", (int)modexp(tmp, d, n),'\0');
			ptr_debut = ptr_fin+1;
		}
		ptr_fin++;
	}
}



