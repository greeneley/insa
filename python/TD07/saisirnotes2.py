#!/usr/bin/python3.2

from os import chdir
chdir("/tmp")

f=open('test.txt', 'w')

reponse=""

print("Pour quitter, entrer exit")

while(reponse!="exit"):
  nom = input("Entrer le nom de l'eleve : ")
  note = input("Note de l'eleve "+nom+" : ")
  f.write(nom+";"+note+"\n")
  reponse = input("Taper 'exit' pour arreter, sinon continuer. ")

f.close()

f=open('test.txt', 'r')
for ligne in f:
	print(ligne)

f.close()
