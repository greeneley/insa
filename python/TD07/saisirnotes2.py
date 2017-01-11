#!/usr/bin/python3.2

from os import chdir
from tkinter import *

chdir("/tmp")

f=open('test.txt', 'w+')


def show_entry_fields():
	for i in range(25):
		f.write("Nom : %s Note : %s\n" % (listeEntry[i][0].get(), listeEntry[i][1].get()))

master = Tk()
listeEntry = [None]*25

for i in range(25):
	Label(master, text="Nom").grid(row=i, column=0)
	Label(master, text="Note").grid(row=i, column=2)
	listeEntry[i] = (Entry(master), Entry(master))
	listeEntry[i][0].grid(row=i, column=1)
	listeEntry[i][1].grid(row=i, column=3)

Button(master, text="Quitter", command=master.quit).grid(row=26, column=0, sticky=W, pady=4)
Button(master, text="Ecrire dans fichier", command=show_entry_fields).grid(row=26, column=1, sticky=W, pady=4)

mainloop()

f.close()

f=open('test.txt', 'r+')
for ligne in f:
	print(ligne)

f.close()
