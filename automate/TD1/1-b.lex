%{
	/* Cet algorithme reconnait les mots de {a,b} ayant un 'a' en 4e position depuis la fin.

	Je propose une implémentation qui reconnait les mots de L suivis de :
		- 1+ espaces
		- 1+ tabulations
		- 1+ retours chariots
		- une fin de ligne

	Un petit probleme cependant : je n'arrive pas a reconnaitre un mot suivi de <<EOF>>
	[ab]*[a][ab]{3}<<EOF>> {trouve=1; printf(MESSAGE, yytext);}
	declenche une erreur de compilation chez flex
	*/

	#include <stdio.h>
	#include <stdlib.h>	

	#define MESSAGE_INLINE "L'expression %s appartient bien au langage L\n"
	#define MESSAGE        "L'expression %s appartient bien au langage L"

	int trouve = 0;
%}

%%
[ab]*[a][ab]{3}$ {trouve=1; printf(MESSAGE, yytext);}
[ab]*[a][ab]{3}[ ]+ {trouve=1; printf(MESSAGE_INLINE, yytext);}
[ab]*[a][ab]{3}[\t]+ {trouve=1; printf(MESSAGE_INLINE, yytext);}
[ab]*[a][ab]{3}[\n]+ {trouve=1; printf(MESSAGE, yytext);}
[ab]*[a][ab]{3} {trouve=1; printf(MESSAGE, yytext);}
%%

main()
{
	printf("Recherche de mots appartenant au langage L...\n");
	yylex();
	if(trouve)
	{
		printf("\nJ'ai reconnu tous les mots possibles du langage L\n");
	}
	else
	{
		printf("\nJe n'ai reconnu aucun mot du langage L\n");
	}
}