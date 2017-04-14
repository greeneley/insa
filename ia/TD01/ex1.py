#/!usr/bin/python3.4

import numpy
import random

#Data set : Multivariate == x1,x2,x3,x4,...,x5 (plusieurs variables)
#Instances == echantillon
#Attribute characteristics : domaine mathematque
#Associated tasks : problematique

#Probleme a 2 classes : celle qui nous interesse, et les autres
#Format : [0.0, 0.0, 0.0, 0.0, "str"]

#50% d'apprentissage : input == donnees d'apprentissage, output == le modele : y = WX + w0
#50% de tests (ou l'on va calculer l'erreur) : input == donnes de test et le modele, output : la classe

#arrayFile = numpy.loadtxt("iris.data", delimiter=',', usecols=(0,1,2,3))

#Parsing du fichier


arrayX = []
arrayT = []
with open("iris.data", "r") as fd:
	for ligne in fd:
		arrayX.append([])
		arrayT.append([])
		split = ligne.split(',')
		if(split[-1]=="Iris-setosa\n"):
			arrayT[-1].append(1.0)
		else:
			arrayT[-1].append(-1.0)
		for val in split[:-2]:
			arrayX[-1].append(float(val))

arrayX = numpy.array(arrayX)
arrayT = numpy.array(arrayT)

#print(arrayX)
#print(arrayT)
#Algorithme 1

omega = [1]
w     = numpy.array([[random.random()]]*4)
dW    = 0
N     = 75
eta   = 0.1
tau   = 0

while(omega!=[]):
	omega = []
	for i in range(1, N+1):
		A = arrayT[i][0]*numpy.transpose(w)
		somme = 0
		for k in range(4):
			somme += A[0][k] * arrayX[i][k]
		if(somme <= 0):
			omega.append(arrayX[i])
			dW += (-arrayT[i][0]*arrayX[i])
		#EndIf
	#EndFor
	w = w - eta*dW
	tau+=1
#EndWhile

print(w)