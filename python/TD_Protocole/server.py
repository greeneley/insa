#!/usr/bin/python3.4

#Exercice 6

import socket
from os import chdir
from tkinter import *

#################################
##### Variables constantes ######
#################################
MODULE = True
ELEVE = False
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
	"""
		Fonction main recuperant les differentes commandes du client pour les traiter.


			Argument null


		Cette fonction se charge de recuperer les donnees recues du client.
		L'idee est de split() la chaine de caractere envoyee par le client et de ne recuperer que les trois premieres chaines.
		Afin d'eviter les doublons, on utilise  
	"""

	TCP_IP = "127.0.0.1"
	TCP_PORT = 1337
	BUFFER_SIZE = 2048

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
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
			delete(conn, arg1, arg2)
		elif(cmd==b"EXPORT"):
			export(conn, arg1)
		elif(cmd==b"HELP"):
			conn.send(bytearray(arg1, "utf8"))
		elif(cmd==b"MAX"):
			max(conn, arg1)
		elif(cmd==b"MIN"):
			min(conn, arg1)
		elif(cmd==b"MOYENNE"):
			moyenne(conn, arg1)
		elif(cmd==b"RESET"):
			reset(conn, arg1)
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

def export(conn, nom=""):
	"""
		Exporte la base de donnees des notes dans un fichier texte dans le dossier courant.
	
	
			socket conn   : La socket stockant la connexion actuelle.
			str    nom    : [option] Dossier ou exporter le fichier texte.
	
	
		La fonction enregistre le fichier sous le nom 'dict_td8_thanh_luu.txt' si aucun nom n'est passe en argument.
		La fonction enregistre sous le nom entre en argument si ce dernier est present.
		Ne prend pas en charge d'eventuelles erreurs de place ou droit.
	
		[Utilisation cliente]
			EXPORT
			EXPORT monfichier.txt
	"""
	global DICT_MODULE

	chdir('.')

	try:	
		if(nom == ""):
			f=open("dict_td8_thanh_luu.txt", "w+")
			conn.send(b"Creation et criture dans le fichier 'dict_td8_thanh_luu.txt' reussite.")	
		else:
			f=open(nom, "w+")
			conn.send(bytearray("\nCreation et ecriture dans le fichier '"+arg1+"'' reussite.", "utf8"))
	except IOError as e:
		conn.send(bytearray(""+e, "utf8"))
		break

	for module in DICT_MODULE:
		f.write("%s : " % (module))
		for eleve in DICT_MODULE[module]:
			f.write("%s;%d\n" % (eleve, DICT_MODULE[module][eleve]))
	f.close()
	pass


def importer(conn, nom, chemin=""):
	"""
		Permet a l'utilisateur d'importer une base de donnees de notes.


			socket conn   : La socket de la connexion actuelle.
			str    nom    : Nom du fichier.
			str    chemin : [option] Chemin pour atteindre le dossier contenant le fichier.


		Si aucun 'chemin' n'est precise, alors importer va essayer d'ouvrir un fichier dans le dossier courant.
		Dans le cas contraire, on ouvre le fichier present dans le dossier indique.

		[Syntaxe cliente]
			IMPORT fichier
			IMPORT fichier chemin
	"""
	global DICT_MODULE

	if(chemin == ""):
		chdir(".")
	else:
		try:
			chdir(chemin)
		except OSError as e:
			conn.send(bytearray(""+e))
			break

	try:
		f=open(nom, "r")
	except IOError as e:
		conn.send(bytearray(""+e))
		break

	for ligne in f:
		if(ligne[-1] == ':'):
			DICT_MODULE[ligne] = {}
		else:
			# TODO : memo module + ecrire

	pass

def delete(conn, arg1, arg2=""):
	"""
		Fonction de suppression d'un module, eleve ou d'un eleve precis d'un module.
	
	
			socket conn : La socket stockant la connexion actuelle.
			str    arg1 : nom du module ou de l'eleve a cibler.
			str    arg2 : [option] nom du module a cible pour l'eleve entre en arg1.
	
	
		L'algorithme de la fonction va, en priorite, chercher a supprimer un module.
		La fonction va donc essayer de supprimer 'arg1' de la liste des modules.
		Si 'arg1' n'est pas reconnu comme un module, les notes de tous les eleves correspondant a 'arg1' seront supprimes.
		'arg2' peut etre precise afin de cibler un module particulier pour un eleve particulier.
	
	
		[Utilisation cliente]
			DEL module
			DEL eleve
			DEL eleve module
	"""
	global DICT_MODULE

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

def max(conn, arg1=""):
	"""
		Affiche la note maximale generale, d'un module ou d'un eleve.


		socket conn : La socket stockant la connexion actuelle.
		str    arg1 : [option] Cible de suppression de la fonction.


		Par defaut si 'arg1' est vide, la fonction fonction renvoie la note maximale generale, tout module et eleve compris.
		Si 'arg1' possede une valeur, max() va en priorite, chercher la note dans un module.
		Si 'arg1' ne correspond pas a un module, alors on renvoie la meilleure note d'un eleve parmis ses modules.

		[Utilisation cliente]
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

def min(conn, arg1=""):
	"""
		Affiche la note minimale generale, d'un module ou d'un eleve.


		socket conn : La socket stockant la connexion actuelle.
		str    arg1 : [option] Cible de suppression de la fonction.


		Par defaut si 'arg1' est vide, la fonction fonction renvoie la note minimale generale, tout module et eleve compris.
		Si 'arg1' possede une valeur, min() va en priorite, chercher la note dans un module.
		Si 'arg1' ne correspond pas a un module, alors on renvoie la plus basse note d'un eleve parmis ses modules.


		[Utilisation cliente]
		MAX
		MAX module
		MAX eleve
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
	

def moyenne(conn, arg1=""):
	"""
		Affiche la moyenne generale, d'un module ou d'un eleve.


		socket conn : La socket stockant la connexion actuelle.
		str    arg1 : [option] Cible de suppression de la fonction.


		Par defaut si 'arg1' est vide, la fonction fonction renvoie la moyenne generale, tout module et eleve compris.
		Si 'arg1' possede une valeur, max() va en priorite, cibler un module.
		Si 'arg1' ne correspond pas a un module, alors on calcule la moyenne d'un eleve.

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
	"""	
		Affiche le contenue du dictionnaire de notes au client.

			socket conn : La socket stockant la connexion actuelle.

		[Syntaxe cliente]
			SHOW

	"""
	global DICT_MODULE

	conn.send(bytearray("\nModules et notes actuellement en memoire :", "utf8"))
	for module in DICT_MODULE:
		conn.send(bytearray("\n"+module+" :", "utf8"))
		for eleve in DICT_MODULE[module]:
			conn.send(bytearray("\n\t"+eleve+" : "+str(DICT_MODULE[module][eleve]), "utf8"))
	pass

def reset(conn, arg1=""):
	"""
	Permet de reinitialiser le dictionnaire, un module ou un eleve.


		socket conn : La socket stockant la connexion actuelle.
		str    arg1 : [option] Cible de la reinitialisation.


	Si 'arg1' vide, reinitialise completement le dictionnaire.
	Sinon, verifie si 'arg1' est un module et dans ce cas, reinitialise le module.
	Dans le cas contraire, reinitialise (en realite, supprime) les notes de l'eleve.

	[Syntaxe cliente]
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
	Permet d'envoyer des notes au serveur pour un module particulier ou un eleve via une GUI.


		int nombre    : le nombre de notes a entrer.
		str nom       : le nom du module ou de l'eleve.
		int mode      : indique si l'on entre des notes pour un module ou un eleve.


	[Syntaxe cliente]
		UPLOAD etudiant
		UPLOAD nombre module
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
	"""
	Fonction auxiliaire pour vider la liste globale d'entries.


		Arguments null


	[Syntaxe cliente]
		Aucun acces explicite depuis le client.
	"""
	global LISTE_ENTRY
	LISTE_ENTRY = []
	pass


def ecrireDictionnaire(nom, nombre, mode=MODULE):
	"""
	Fonction auxiliaire pour écrire dans le dictionnaire de notes.


		str     nom    : Nom de l'eleve ou du module a ecrire.
		str     nombre : Nombre de notes a entrer s'il s'agit d'un module.
		bool    mode   : Indique si l'on ecrit pour un eleve ou un module.


	La fonction verifie d'abord le mode d'ecriture et agit en consequence.
	Si l'on est dans le mode MODULE, alors elle verifie l'existence ou non du module. 
	En cas d'inexistence, elle cree un nouveau dictionnaire et y ajoute a l'interieur les notes entrees.
	Dans le cas contraire, on est dans le ELEVE. On ecrase alors simplement toutes les notes existentes et/ou ajoute les nouvelles.

	[Syntaxe cliente]
		Aucun acces explicite depuis le client.
	"""
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
	"""
	Fonction auxiliaire pour ajouter un champ d'entry.


		<type> master : Fenetre principale dont les nouveaux champs seront les fils.
		<type> boutonAjout : Bouton d'ajout du parent a deplacer.
		<type> boutonQuit : Bouton quitter du parent a deplacer.
		<type> boutonValider : Bouton valider du parent a deplacer.


	Ajoute deux nouveaux champs d'entry dans la GUI du client pour lui permettre de rentrer plus de donnees.
	Deplace alors les trois boutons en argument d'un cran vers le bas pour eviter que les nouveaux champs les chevauchent.

	[Syntaxe cliente]
		Aucun acces explicite depuis le client.
	"""
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
