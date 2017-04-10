all:
	@echo Usage : make \[numero\]
	@echo 1. Compilation de 1-b.lex sous \'1-b\'
	@echo 2. Compilation de 1-c.lex sous \'1-c\'
	@echo 3. Compilation de 1-e.lex sous \'1-e\'
	@echo 4. Compilation de 2-a.lex et 2-a.yac sous \'2-a\'

1:
	lex 1-b.lex
	gcc -o 1-b lex.yy.c -lfl

2:
	lex 1-c.lex
	gcc -o 1-c lex.yy.c -lfl

3:
	lex 1-e.lex
	gcc -o 1-e lex.yy.c -lfl

4:
	yacc -d 2-a.yac
	lex 2-a.lex
	gcc -o 2-a lex.yy.c y.tab.c -lfl