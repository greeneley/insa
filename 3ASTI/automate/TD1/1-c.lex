%{
	/* Cet algorithme permet de compte le nombre de mots a l'instar d'un wc

	J'ai decide d'adopter la definition d'un mot en me basant sur le man de wc :
	`Un mot est une séquence non vide de caractères délimités par des espaces.`

	Rq : J'ai eu un petit probleme de reconnaissance de `\t`.
	     Apres des tests, je n'ai pas l'impression que la tabulation est reconnue

	Un ligne est reconnue si :
		- elle ne contient rien ou que des tabulations
		- elle commence par un espace
		- elle commence par autre chose qu'un espacement

	Un mot est reconnu si :
		- il est precede d'un espace ou d'un tabulation
		- il commence la ligne

	Un caractere est reconnu si :
		- c'est un saut de ligne ou autre chose
	Deux caracteres sont reconnus si :
		- on reconnait un mot
		- une nouvelle ligne non vide
	*/

	#include <stdio.h>
	#include <stdlib.h>	

	int lignes = 0;
	int mots   = 0;
	int octets = 0;
%}

%%
\0 {lignes++; octets++;}

^\t+. {lignes++; mots++; octets++; octets++;}
^\t+$ {lignes++;}
^[ ] {lignes++; octets++;}
^[^\t ] {lignes++; mots++; octets++;}

[ ][^\n\t ] {mots++; octets++; octets++;}
\t[^\n\t ] {mots++; octets++; octets++;}

. {octets++;}
\n {octets++;}
%%

main()
{
	printf("Lecture...\n");
	yylex();
	printf("\nJ'ai compte %d lignes\n", lignes);
	printf("J'ai compte %d mots\n", mots);
	printf("J'ai compte %d octets\n", octets);
}