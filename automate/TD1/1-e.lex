%{

	/* Verifie si le parenthesage est correct

	L'algorithme etait simple mais il y avait un piege.
	Simplement incrementer sur des ( et inversement sur des ) est faux.
	Ceci voudrait dire que ))))(((( est un parenthesage correct.

	Donc, si le nombre de parentheses ouvrantes est negatif a tout moment de la lecture,
	alors le parenthesage est immediatement faux.
	*/

	#include <stdio.h>
	#include <stdlib.h>	

	int nb    = 0;
	int auxNb = 0;
%}

%%
[(] {
		if(nb<0) {printf("%s", yytext); return;}
		else {printf("%s", yytext); nb++;}
	}

[)] {
		if(nb<0) {printf("%s", yytext); return;}
		else {printf("%s", yytext); nb--;}
	}
%%

main()
{
	yylex();
	if(nb==0)
	{
		printf("\nLe parenthesage est correct\n");
	}
	else
	{
		printf("\nLe parenthesage est incorrect\n");
	}
}