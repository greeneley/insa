%{
	#include <stdio.h>
	#include <stdlib.h>	
	int i=0;
%}

%%
^. {printf("%d. %s", i, yytext); i++;}
. {printf("%s", yytext);}
%%

main()
{
	yylex();
}