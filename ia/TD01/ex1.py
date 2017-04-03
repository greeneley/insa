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
		for val in split[:-1]:
			arrayX[-1].append(val)

arrayX = numpy.array(arrayX)
arrayT = numpy.array(arrayT)

print(arrayX)
print(arrayT)
#Algorithme 1

omega = [1]
w     = [[random.random()]]*4
dW    = 0
N     = 75

while(omega!=[]):
	omega = []
	for i in range(1, N+1):
		somme = numpy.sum((t[i][0]*numpy.dot(numpy.transpose(w), arrayX[i])))
		if(somme <= 0):
			omega.append(arrayX[i])
			dW += (-t[i][0]*x[i])
		#EndIf
	#EndFor
	#w
#EndWhile