%{
	#include <stdio.h>
	#include <stdlib.h>	
%}

lettre [ab]

%%
[ab]*[a]([ab]){3}$ {printf("L'expression %s appartient bien au langage L\n", yytext);}

([ab])*$ {printf("Je n'ai pas reconnu de mot du langage L\n");}
%%

main()
{
	yylex();
}