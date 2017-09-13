all:
	@echo ===== Instructions du Makefile =====
	@echo Pour compiler un exercice : make numero
	@echo 1: compilation de 1bc.yac et 1bc.lex sous 1bc
	@echo 2: compilation de 2abcd.yac et 2abcd.lex sous 2abcd

1:
	yacc -d 1bc.yac
	lex 1bc.lex
	gcc -o 1bc lex.yy.c y.tab.c -lfl
	rm lex.yy.c y.tab.c y.tab.h

2:
	yacc -d 2abcd.yac
	lex 2abcd.lex
	gcc -o 2abcd lex.yy.c y.tab.c -lfl -lm
	rm lex.yy.c y.tab.c y.tab.h
