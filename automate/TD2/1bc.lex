%{
#include "y.tab.h"
%}

real ([0-9]*("."[0-9])*)

%{
#include <stdio.h>
#include <stdlib.h>
#include "y.tab.h"

  int yylex();

%}


%%
[ \t]+	;
{real}    { sscanf(yytext, "%lf", &yylval.valDble);
             return REAL;}

.    {return yytext[0];}
%%
