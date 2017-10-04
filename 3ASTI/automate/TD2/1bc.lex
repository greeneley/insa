%{
#include "y.tab.h"
%}

chiffres [0-9]
real ([0-9]*("."[0-9])+)

%{
#include <stdio.h>
#include <stdlib.h>
#include "y.tab.h"

  int yylex();

%}


%%
[ \t]+	;
{chiffres} { sscanf(yytext, "%d", &yylval.valInt);
             return INTEGER;}
{real}     { sscanf(yytext, "%lf", &yylval.valDble);
             return REAL;}

.    {return yytext[0];}
%%
