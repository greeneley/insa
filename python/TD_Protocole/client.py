#!/usr/bin/python3.4

import socket
import server


#################################
######### Fonction main #########
#################################

def main():
	TCP_IP = "127.0.0.1"
	TCP_PORT = 1337
	BUFFER_SIZE = 2048

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((TCP_IP, TCP_PORT))

	print("Connexion reussie !")
	printAide()
	while(True):
		cmd = input(">>> ")
		cmd = cmd.upper()

		if(cmd=="HELP"):
			printAide()
		elif(cmd[0:4]=="HELP"):
			s.send(bytearray(cmd, "utf8"))	
			data = s.recv(BUFFER_SIZE)
			aide(data)
			print(data.decode("utf-8"))
		elif(cmd=="EXIT"):
			s.close()
			break
		else:
			s.send(bytearray(cmd, "utf8"))	
			data = s.recv(BUFFER_SIZE)
			print(data.decode("utf-8"))
	pass


#################################
##### Fonctions principales #####
#################################

def printAide():
	print("\t=== Fichiers server.py et client.py par Thanh Luu 3ASTI ===")
	print("Pour avoir une aide detaillee des commandes :")
	print("\t\tHELP COMMANDE\n")
	print("Liste des commandes")
	print("DEL         - Supression d'une entree")
	print("EXPORT      - Enregistre les notes dans un fichier texte")
	print("IMPORT      - Importe des notes depuis un fichier texte")
	print("HELP        - Affiche une aide sur les commandes")
	print("MAX         - Obtenir la note maximale")
	print("MIN         - Obtenir la note minimale")
	print("MOYENNE     - Obtenir la moyenne")
	print("UPLOAD      - Permet l'envoie de notes au serveur")
	print("RESET       - Reinitialiser un champ")
	print("SHOW        - Affiche les notes entrees par module")
	print("EXIT        - Quitte le serveur")
	pass


def aide(cmd):
	if(cmd==b"DEL"):
		help("server.delete")
	elif(cmd==b"EXPORT"):
		help("server.export")
	elif(cmd==b"HELP"):
		help("client.aide")
	elif(cmd==b"MAX"):
		help("server.max")
	elif(cmd==b"MIN"):
		help("server.min")
	elif(cmd==b"MOYENNE"):
		help("server.moyenne")
	elif(cmd==b"UPLOAD"):
		help("server.upload")
	elif(cmd==b"RESET"):
		help("server.reset")
	elif(cmd==b"SHOW"):
		help("server.printDico")
	else:
		print("Commande non reconnue !")
	pass


if(__name__=="__main__"):
	main()
