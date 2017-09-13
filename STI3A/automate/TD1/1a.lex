%{
	#include <stdio.h>
	#include <stdlib.h>	
%}

lettre [a-z]|[A-Z]

%%
[.] {printf("[point]");}
[@] {printf("[at]");}
[-] {printf("[tiret]");}

. {printf("%s", yytext);}
%%

main()
{
	yylex();
}