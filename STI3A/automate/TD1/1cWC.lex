%{
	#include <stdio.h>
	#include <stdlib.h>	
	int i;
	int total = 0;
%}

%%
^. {i=0;}
[ ] {i++;}
\n {i++;}
(\t)+ {i++;}

.$ {i++; total+=i; printf("\nJ'ai compte %d mots dans cette ligne\n", i);}
<<EOF>> {i++; total+=i; printf("J'ai compte %d mots depuis le debut\n", total);}
%%

main()
{
	yylex();
}