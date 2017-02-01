#!/usr/bin/python3.4

#Exercice 6

import socket
from os import chdir
from tkinter import *

#################################
##### Variables constantes ######
#################################
MODULE = 1
ELEVE = 0
#################################


#################################
####### Variables globales ######
#################################
DICT_MODULE = {}
LISTE_ENTRY = []
INCR        = 0
#################################


#################################
######### Fonction main #########
#################################

def main():
	TCP_IP = "127.0.0.1"
	TCP_PORT = 1337
	BUFFER_SIZE = 2048

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	print(s)
	s.bind((TCP_IP, TCP_PORT))
	s.listen(1)

	conn, addr = s.accept()
	print("Connection address :", addr)
	while(True):
		data = conn.recv(BUFFER_SIZE)
		if(not data):
			break

		tmp = data.split(None, 4)
		try:
			cmd = tmp[0]
			cmd = cmd.upper()
		except IndexError:
			cmd = ""

		try:
			arg1 = tmp[1].decode("utf-8")
			arg1 = arg1.upper()
		except IndexError:
			arg1 = ""

		try:
			arg2 = tmp[2].decode("utf-8")
			arg2 = arg2.upper()
		except IndexError:
			arg2 = ""

		if(cmd==b"DEL"):
			delete(arg1, arg2, conn)
		elif(cmd==b"EXPORT"):
			export(arg1, conn)
		elif(cmd==b"HELP"):
			conn.send(bytearray(arg1, "utf8"))
		elif(cmd==b"MAX"):
			max(arg1, conn)
		elif(cmd==b"MIN"):
			min(arg1, conn)
		elif(cmd==b"MOYENNE"):
			moyenne(arg1, conn)
		elif(cmd==b"RESET"):
			reset(arg1, conn)
		elif(cmd==b"SHOW"):
			printDico(conn)
		elif(cmd==b"UPLOAD"):
			if(arg1.isdecimal() and arg2!=""):
				upload(int(arg1), arg2, 1)
				conn.send(b"\nUpload effectue !")
			elif(arg1!=""):
				upload(0, arg1, 0)
				conn.send(b"\nUpload effectue !")
			else:
				conn.send(b"Precisez un nom de module ou d'eleve !")
				conn.send(b"\nUpload echoue !")
		elif(cmd==b"EXIT"):
			conn.send(b"\nFermeture de la connexion...")
			conn.close()
		else:
			conn.send(b"\nCommande non reconnue !")
	pass


#################################
##### Fonctions principales #####
#################################

def export(arg1, conn):
	"""

	EXPORT
	EXPORT monfichier.txt
	"""
	global DICT_MODULE

	chdir("/tmp")

	if(arg1 == ""):
		f=open("dict_td8_thanh_luu.txt", "w+")
	else:
		f=open(arg1, "w+")

	for module in DICT_MODULE:
		f.write("%s : " % (module))
		for eleve in DICT_MODULE[module]:
			f.write("%s;%d" % (eleve, DICT_MODULE[module][eleve]))
	f.close()
	pass

def delete(arg1, arg2, conn):
	"""

	DEL module
	DEL eleve
	DEL eleve module
	"""
	global DICT_MODULE

	print(DICT_MODULE)
	if(arg1 in DICT_MODULE):
		DICT_MODULE.pop(arg1)
		conn.send(bytearray("\nSuppression du module "+arg1+" effectuee !", "utf8"))
	elif(arg2==""):
		for module in DICT_MODULE:
			try:
				DICT_MODULE[module].pop(arg1)
			except:
				pass
		conn.send(bytearray("\nToutes les notes de "+arg1+" ont ete supprimees !", "utf8"))
	elif(arg2 in DICT_MODULE):
		try:
			DICT_MODULE[arg2].pop(arg1)
			conn.send(bytearray("\nSuppression de la note de "+arg1+" dans "+arg2+" effectuee !", "utf8"))
		except:
			pass
	pass

def max(arg1, conn):
	"""

	MAX
	MAX module
	MAX eleve
	"""
	global DICT_MODULE

	maxNote   = 0
	nom       = []
	nom2      = ""
	if(arg1==""):
		for module in DICT_MODULE:
			for eleve in DICT_MODULE[module]:
				if(DICT_MODULE[module][eleve] > maxNote):
					maxNote = DICT_MODULE[module][eleve]
					nom     = [eleve]
					nom2    = module
				elif(DICT_MODULE[module][eleve] == maxNote):
					nom.append(eleve)
		conn.send(bytearray("\nLa meilleure note est : "+str(maxNote), "utf8"))
		conn.send(bytearray("\nElle a ete obtenue dans le module : "+nom2, "utf8"))
		conn.send(b"\nLa liste des eleves ayant cette note est :")
		for eleve in nom:
			conn.send(bytearray(eleve, "utf8"))

	elif(arg1 in DICT_MODULE):
		for eleve in DICT_MODULE[arg1]:
			if(DICT_MODULE[arg1][eleve] > maxNote):
				nom     = [eleve]
				maxNote = DICT_MODULE[arg1][eleve]
			elif(DICT_MODULE[module][eleve] == maxNote):
				nom.append(eleve)
		conn.send(bytearray("\nLa meilleure note du module "+arg1+" est : "+str(maxNote), "utf8"))
		conn.send(b"\nLa liste des eleves ayant cette note est :")
		for eleve in nom:
			conn.send(bytearray(eleve+"; ", "utf8"))

	else:
		for module in DICT_MODULE:
			try:
				if(DICT_MODULE[module][arg1] > maxNote):
					maxNote = DICT_MODULE[module][arg1]
					nom     = [module]
				elif(DICT_MODULE[module][eleve] == maxNote):
					nom.append(module)
			except:
				pass
		conn.send(bytearray("\nLa meilleure note de "+arg1+" est : "+str(maxNote), "utf8"))
		conn.send(b"\nLa liste des modules ayant cette note est :")
		for module in nom:
			conn.send(bytearray(module+"; ", "utf8"))
	pass

def min(arg1, conn):
	"""

	MIN
	MIN module
	MIN eleve
	"""
	global DICT_MODULE

	minNote   = 20
	nom       = []
	nom2      = ""
	if(arg1==""):
		for module in DICT_MODULE:
			for eleve in DICT_MODULE[module]:
				if(DICT_MODULE[module][eleve] < minNote):
					minNote = DICT_MODULE[module][eleve]
					nom     = [eleve]
					nom2    = module
				elif(DICT_MODULE[module][eleve] == minNote):
					nom.append(eleve)
		conn.send(bytearray("\nLa note minimale est : "+str(minNote), "utf8"))
		conn.send(bytearray("\nElle a ete obtenue dans le module : "+nom2, "utf8"))
		conn.send(b"\nLa liste des eleves ayant cette note est :")
		for eleve in nom:
			conn.send(bytearray(eleve, "utf8"))

	elif(arg1 in DICT_MODULE):
		for eleve in DICT_MODULE[arg1]:
			if(DICT_MODULE[arg1][eleve] < minNote):
				nom     = [eleve]
				minNote = DICT_MODULE[arg1][eleve]
			elif(DICT_MODULE[module][eleve] == minNote):
				nom.append(eleve)
		conn.send(bytearray("\nLa note minimale du module "+arg1+" est : "+str(minNote), "utf8"))
		conn.send(b"\nLa liste des eleves ayant cette note est :")
		for eleve in nom:
			conn.send(bytearray(eleve+"; ", "utf8"))

	else:
		for module in DICT_MODULE:
			try:
				if(DICT_MODULE[module][arg1] < minNote):
					minNote = DICT_MODULE[module][arg1]
					nom     = [module]
				elif(DICT_MODULE[module][eleve] == minNote):
					nom.append(module)
			except:
				pass
		conn.send(bytearray("\nLa plus mauvaise note de "+arg1+" est : "+str(minNote), "utf8"))
		conn.send(b"\nLa liste des modules ayant cette note est :")
		for module in nom:
			conn.send(bytearray(module+"; ", "utf8"))
	pass
	

def moyenne(arg1, conn):
	"""
		int nombre, str module

		moyenne module
		moyenne eleve
	"""
	global DICT_MODULE

	arg1    = arg1.upper()
	somme   = 0
	nb      = 0
	moyenne = 0
	for module in DICT_MODULE:
		if(module==arg1):
			for eleve in DICT_MODULE[module]:
				somme += DICT_MODULE[module][eleve]
				nb    += 1
			if(nb>0):
				moyenne = somme / nb
				conn.send(bytearray("\nLa moyenne du module "+arg1+" est "+str(moyenne), "utf8"))
				return
			conn.send(b"\nImpossible de calculer la moyenne du module!\nIl n'y a pas de notes !")
			return

	for module in DICT_MODULE:
		try:
			somme += DICT_MODULE[module][eleve]
			nb    += 1
		except:
			pass
	if(nb>0):
		moyenne = somme / nb
		conn.send(bytearray("\nLa moyenne de l'eleve "+arg1+" dans "+str(moyenne), "utf8"))
	else:
		conn.send(b"\nImpossible de calculer la moyenen de l'eleve !\nIl n'y a pas de notes !")
	pass

def printDico(conn):
	global DICT_MODULE

	conn.send(bytearray("\nModules et notes actuellement en memoire :", "utf8"))
	for module in DICT_MODULE:
		conn.send(bytearray("\n"+module+" :", "utf8"))
		for eleve in DICT_MODULE[module]:
			conn.send(bytearray("\n\t"+eleve+" : "+str(DICT_MODULE[module][eleve]), "utf8"))
	pass

def reset(arg1, conn):
	"""
		int nombre, str module

		RESET
		RESET module
		RESET eleve
	"""
	global DICT_MODULE

	if(arg1==""):
		DICT_MODULE.clear()
		conn.send(b"\nReset total effectue !")
	elif(arg1 in DICT_MODULE):
		DICT_MODULE[arg1].clear()
		conn.send(bytearray("\nReset du module "+arg1+" effectue !", "utf8"))
	else:
		for module in DICT_MODULE:
			try:
				DICT_MODULE[module].pop(arg1)
			except:
				pass
		conn.send(bytearray("\nLes notes de l'eleve "+arg1+" ont bien reinitialisees !", "utf8"))
	pass

def upload(nombre, nom, mode):
	"""
	Permet d'envoyer des notes au serveur pour un module particulier ou un eleve via une GUI

		int nombre    : le nombre de notes a entrer
		str nom       : le nom du module ou de l'eleve
		int mode      : indique si l'on entre des notes pour un module ou un eleve

	[Syntaxe client]
		UPLOAD nombre module
		UPLOAD etudiant
	"""
	global INCR

	master = Tk()
	resetEntry()

	if(mode):
		master.title("Notes pour le module : "+nom)

		for i in range(nombre):
			Label(master, text="Nom").grid(row=i, column=0)
			Label(master, text="Note").grid(row=i, column=2)
			LISTE_ENTRY.append((Entry(master), Entry(master)))
			LISTE_ENTRY[i][0].grid(row=i, column=1)
			LISTE_ENTRY[i][1].grid(row=i, column=3)

		Button(master, text="Quitter", command=master.destroy).grid(row=(nombre+1), column=0, sticky=W, pady=4)
		Button(master, text="Valider", command=lambda:ecrireDictionnaire(nom, nombre, MODULE)).grid(row=(nombre+1), column=1, sticky=E, pady=4)

	else: # C'est un eleve
		print(nom)
		master.title("Notes pour l'eleve : "+nom)
		INCR = 0

		for module in DICT_MODULE:
			Label(master, text=module).grid(row=INCR, column=0)
			LISTE_ENTRY.append((module, Entry(master)))
			LISTE_ENTRY[INCR][1].grid(row=INCR, column=1)
			INCR+=1

		boutonQuit = Button(master, text="Quitter", command=master.destroy)
		boutonValider = Button(master, text="Valider", command=lambda:ecrireDictionnaire(nom, INCR, ELEVE))
		boutonAjout = Button(master, text="Ajouter un module", command=lambda: ajoutEntry(master, boutonAjout, boutonQuit, boutonValider))
		
		boutonQuit.grid(row=(INCR+2), column=0, sticky=W, pady=4)
		boutonAjout.grid(row=INCR, column=0)
		boutonValider.grid(row=(INCR+2), column=1, sticky=E, pady=4)

	master.mainloop()
	pass


#################################
##### Fonctions utilitaires #####
#################################

def resetEntry():
	global LISTE_ENTRY
	LISTE_ENTRY = []
	pass


def ecrireDictionnaire(nom, nombre, mode=MODULE):
	global DICT_MODULE

	if(mode):
		boolPresent = False
		for module in DICT_MODULE:
			if(module == nom):
				for i in range(nombre):
					DICT_MODULE[module][LISTE_ENTRY[i][0].get().upper()] = int(LISTE_ENTRY[i][1].get())
				boolPresent = True
				break
		if(not boolPresent):
			DICT_MODULE[nom] = {}
			for i in range(nombre):
				DICT_MODULE[nom][LISTE_ENTRY[i][0].get().upper()] = int(LISTE_ENTRY[i][1].get())
	else:
		for i in range(nombre):
			if(type(LISTE_ENTRY[i][0])==str):
				DICT_MODULE[LISTE_ENTRY[i][0]][nom] = int(LISTE_ENTRY[i][1].get())
			else:
				nomModule = LISTE_ENTRY[i][0].get().upper()
				DICT_MODULE[nomModule] = {}
				DICT_MODULE[nomModule][nom] = int(LISTE_ENTRY[i][1].get())
	pass

def ajoutEntry(master, boutonAjout, boutonQuit, boutonValider):
	global INCR

	INCR+=1
	boutonAjout.grid(row=INCR, column=0)
	boutonQuit.grid(row=(INCR+2), column=0, sticky=W, pady=4)
	boutonValider.grid(row=(INCR+2), column=1, sticky=E, pady=4)

	INCR-=1
	Label(master, text="Module").grid(row=INCR, column=0)
	Label(master, text="Note").grid(row=INCR, column=2)
	LISTE_ENTRY.append((Entry(master), Entry(master)))
	LISTE_ENTRY[INCR][0].grid(row=INCR, column=1)
	LISTE_ENTRY[INCR][1].grid(row=INCR, column=3)

	INCR+=1
	pass


if(__name__ == "__main__"):
	main()
