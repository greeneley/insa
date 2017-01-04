#!/usr/bin/python3.2

try:
	fh = open("test.txt", "w")
	fh.write("coucou")
	print("Ecriture ok")
except IOError:
	print("Erreur: IO")
else:
	fh.close()

try:
	fh = open("test.txt", "r")
	fh.write("coucou")
	print("Ecriture ok")
except IOError:
	print("Erreur: IO")
else:
	fh.close()
