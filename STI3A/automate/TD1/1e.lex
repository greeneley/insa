%{
	#include <stdio.h>
	#include <stdlib.h>	
	int nb=0;
%}

%%
[(] {nb++;}
[)] {nb--;}
%%

main()
{
	yylex();
	if(nb==0)
	{
		printf("\nLe parenthesage est correct\n");
	}
}