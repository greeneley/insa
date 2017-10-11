#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "crypt.h"
#include "encrypt.h"
#include <math.h>


/****************************************************************
 *                                                               *
 *  ------------------ VARIABLES GLOBALES ---------------------  *
 *                                                               *
 ****************************************************************/
#define A_TO_a 32
#define a_TO_A -32


/****************************************************************
 *                                                               *
 *  --------------------------- XOR ---------------------------  *
 *                                                               *
 ****************************************************************/


/**
 *  * chiffrement utilisant le ou exclusif
 *   */
void xor_crypt(char * key, char * texte, char* chiffre)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	char *cp;
	int c, i;

	/* ===== Initialisation variables ===== */
	i      = 0;

	if((cp = key))
	{
		printf("Cryptage XOR...\n");
		while(texte[i] != '\0')
		{
			c = texte[i];
			if(*cp == '\0')
			{
				cp = key;
			}
			c  ^= *(cp++);
			chiffre[i] = c;
			i++;
		}
	}
}

/**
 *  * dÈchiffrement utilisant le ou exclusif
 *   */
void xor_decrypt(char * key, char * texte, char* chiffre)
{
	xor_crypt(key, texte, chiffre);
}



/****************************************************************
 *                                                               *
 *  -------------------------- Cesar --------------------------  *
 *                                                               *
 ****************************************************************/


/**
 *  * chiffrement utilisant cesar
 *   */
void cesar_crypt(int decallage, char * texte, char* chiffre)
{
	if(texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	/* Declaration variables */
	int c, i, cle;

	/* Initialisation variables */
	i = 0;

	/* On s'assure que les nombres negatifs deviennent positifs */
	cle = ((decallage%26)+26)%26;

	printf("Encryptage Cesar...\n");
	while(texte[i] != '\0')
	{
		c = texte[i];
		/* Cryptage du caractere */
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
 *  * dÈchiffrement utilisant  cesar
 *   */
void cesar_decrypt(int decallage, char * texte, char* chiffre)
{
	if(texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	cesar_crypt(-decallage, texte, chiffre);
}


/****************************************************************
 *                                                               *
 *  ------------------------ VIGENERE -------------------------  *
 *                                                               *
 ****************************************************************/


/**
 *  * chiffrement utilisant viginere
 *   */
void viginere_crypt(char * key, char * texte, char* chiffre)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
		return;
	}

	/* Declaration variables */
	int c, i, pos;
	int decalage, taille;
	char* test;
	test = key;

	/* Verification eligibilite de la cle */
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

	printf("Encryptage Vigenere...\n");
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
		   =========== Cryptage du caractere ============
		   ==============================================

		 * c-'lettre' correspond a l'index de c dans l'alphabet
		 * decalage-'lettre' correspond a l'index par rapport a 'lettre'
		 * la somme des deux donne la lettre chiffree 
		*/
		if('a' <= c && c <= 'z')
		{
			if('A' <= decalage && decalage <= 'Z')
			{
				decalage += A_TO_a;
			}
			c = ((c-'a'+decalage-'a')%26)+'a';
		}
		else if('A' <= c && c <= 'Z')
		{
			if('a' <= decalage && decalage <= 'z')
			{
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
 *  * dÈchiffrement utilisant viginere
 *   */
void viginere_decrypt(char * key, char * texte, char* chiffre)
{
	if(key == NULL || texte == NULL || chiffre == NULL)
	{
		printf("Erreur : un des arguments est NULL.");
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

	printf("Decryptage Vigenere...\n");
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
		   =========== Cryptage du caractere ============
		   ==============================================

		 * Au lieu de faire +(decalage-'lettre')
		 * on fait -(decalage-'lettre')
		 * pour revenir vers la lettre originelle.
		 * On doit s'assurer que le resultat est positif
		 * donc +26 a l'operation precedente.
		 * Et enfin un %26 si la soustrction etait deja
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
	printf("Fin du programme\n");
}


/****************************************************************
 *                                                               *
 *  --------------------------- DES ---------------------------  *
 *                                                               *
 ****************************************************************/


/**
 *  * chiffrement utilisant des
 *   */
void des_crypt(char * key, char * texte, char* chiffre, int size)
{

}


/**
 *  * dÈchiffrement utilisant des
 *   */
void des_decrypt(char * key, char * texte, char* chiffre, int size)
{

}


/****************************************************************
 *                                                               *
 *  --------------------------- 3DES --------------------------  *
 *                                                               *
 ****************************************************************/


/**
 *  * chiffrement utilisant 3des
 *   */
void tripledes_crypt(char * key1, char * key2, char * texte, char* chiffre,int size)
{

}


/**
 *  * dÈchiffrement utilisant 3des
 *   */
void tripledes_decrypt(char* key1, char* key2, char* texte, char* chiffre, int size)
{

}


/****************************************************************
 *                                                               *
 *  -------------------------- modexp -------------------------  *
 *                                                               *
 ****************************************************************/

static Huge modexp(Huge a, Huge b, Huge n) {
	
	Huge               y;
	
	/****************************************************************
	 *                                                               *
	 *  Calcule (pow(a, b) % n) avec la mÈthode du carrÈ binaire     *
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
		 *  …lÈvation de a au carrÈ pour chaque bit de b.             *
		 *                                                            *
		 *************************************************************/
		
		a = (a * a) % n;
		
		/*************************************************************
		 *                                                            *
		 *  On se prÈpare pour le prochain bit de b.                  *
		 *                                                            *
		 *************************************************************/
		
		b = b >> 1;
		
	}
	
	return y;
	
}


/**
 * Transforme une chaine de caractère en chaine d'entier
 */
void texttoint(char * texte, char* chiffre, int size){
	*chiffre='\0';
	int tmp;
	int i;
	for(i=0;i<size;i++){		
	    // on ajoute 10 pour éviter le problème de disparition du 0 devnt les entiers entre 1 et 9 (01 a 09)
		// ceci évite de découper le texte en bloc de taille < n et de les normaliser ensuite
		tmp=(*(texte+i)-'a'+10);
		sprintf(chiffre+strlen(chiffre),"%d%c",tmp,'\0');
	}
}

/**
 * Transforme une chaine d'entier en chaine de caractère
 */ 
void inttotext(char * texte, char* chiffre){
	*chiffre='\0';
	int tmp=0;
	while((*texte) != '\0'){	
	    // lettre de l'alphabet (0..25 correspond pour nous à 10..35)	
		if(10*tmp+(*(texte)-'0') > 36){
		    // on déduit donc 10 pour obtenir la bonne lettre dans l'alphabet
			sprintf(chiffre+strlen(chiffre),"%c%c",tmp+'a'-10, '\0');
			tmp=0;
		}
		tmp=10*tmp+(*(texte)-'0');
		texte++;
	}
}

/**
 * Chiffrement RSA
 */
void rsa_crypt(int e, int n, char * texte, char* chiffre, int size)
{
    int tmp;
	Huge buf=0;
	char* pt;
	char* btmp = (char *)malloc(strlen(texte) * sizeof(char)); 
	
	texttoint(texte,btmp,size);
	pt = btmp;
	*chiffre='\0';
	while((*pt) != '\0'){
		tmp=*pt-'0';
		if(10*buf + tmp >= n){
		    // on utilise le $ comme séparateur de bloc
			sprintf(chiffre+strlen(chiffre),"%ld$%c", modexp(buf, e, n),'\0');
			buf=0;
		}
		buf=10*buf+tmp;
		pt++;
	}
	sprintf(chiffre+strlen(chiffre),"%ld$%c", modexp(buf, e, n),'#');
	printf("\n");
}

/**
 * Déchiffrement RSA
 */
void rsa_decrypt(int d, int n, char * texte, char* chiffre)
{
	int tmp;
	char* pt=texte;
	char* tmpc= (char *)malloc(strlen(texte) * sizeof(char)); 
	Huge buf=0;
	
	*tmpc='\0';
	while((*pt) != '#'){
		// on utilise le $ comme séparateur de bloc
	    if((*pt) == '$'){
			sprintf(tmpc+strlen(tmpc),"%ld", modexp(buf, d, n));
			buf=0;
		}else{
			tmp=*pt-'0';
			buf=10*buf+tmp;
		}
		pt++;
	}
	sprintf(tmpc+strlen(tmpc),"%ld", modexp(buf, d, n));
	
	inttotext(tmpc,chiffre);
}
