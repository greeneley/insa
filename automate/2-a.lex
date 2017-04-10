%{
	#include "y.tab.h"

	/*
	On supprime d'abord les espaces inutiles en debut de lignes.
	On determine ensuite les expressions comme etant des ensembles
	de lettres {a,b} espaces d'espaces.
	*/
%}

%%
[a]  {return yytext[0];}
[b]  {return yytext[0];}
[a]$ {return FIN_A;}
[b]$ {return FIN_B;}
^[ ]+ ;
^\t+  ;
\0    ;
[ ]+  {return EXPRESSION;}
\t+   {return EXPRESSION;}
%%