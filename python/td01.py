print("Ce script utilise Python3")

##### Exercice 1

def calculette():

	while(True):
		try:
			print("Entrez vos deux valeurs.")
			a = float(input("Valeur de a : "))
			b = float(input("Valeur de b : "))
			break

		except:
			print("Entrer des nombres !")

	while(True): #Oui c'est moche
		operateur = input("Entrer votre operateur : ")

		if(operateur == "+"):
			print(a+b)
			break
		elif(operateur == "-"):
			print(a-b)
			break
		elif(operateur == "%"):
			print(a%b)
			break
		elif(operateur == "/"):
			if(b==0):
				print("Erreur division par zero !")
			else:
				print(a/b)
			break
		elif(operateur == "//"):
			if(b==0):
				print("Erreur division par zero !")
			else:
				print(a//b)
			break
		elif(operateur == "*"):
			print(a*b)
			break
		elif(operateur == "^" or operateur == "**"):
			print(a**b)
			break
		else:
			print("Erreur : operateur non reconnue !")


##### Exercice 2

def racineCarre():
	while(True):
		try:
			x = abs(float(input("Entrer votre nombre : ")))
			break
		except:
			print("Entrer un nombre !")

	print(x**0.5)


##### Exercice 3

def evaluation():
	while(True):
		try:
			N = float(input("Entrer votre moyenne : "))
			break
		except:
			print("Entrer un nombre !")

	if(N >= 16):
		print("Mention Tres Bien.")
	elif(N >= 14):
		print("Mention Bien.")
	elif(N >= 12):
		print("Mention Assez Bien.")
	elif(N >= 10):
		print("Admis(e).")
	else:
		print("Ajourne(e).")

##### Exercice 4

def eqSecondDegre():
	while(True):
		try:
			print("Resolution de ax² + bx + c = 0")
			print("Entrer vos variables a, b et c")
			a = float(input("a = "))
			b = float(input("b = "))
			c = float(input("c = "))
			break
		except:
			print("Entrer un nombre !")

	if(a==0):
		if(b==0):
			print("Aucune solution !")
		else:
			x0 = -(c/b)
			print("La solution est x =", x)
	else:
		delta = (b**2) - (4*a*c)
		if(delta > 0):
			x1 = ((-b) - (delta**0.5)) / (2*a)
			x2 = ((-b) + (delta**0.5)) / (2*a)
			print("Les solutions sont :")
			print("x1 =", x1)
			print("x2 =", x2)
		elif(delta == 0):
			x0 = -(b/(2*a))
			print("La solution est x =", x0)
		else:
			delta = abs(delta)
			r1 = -(b)/(2*a)
			i1 = -(delta**0.5)/(2*a)
			r2 = -(b)/(2*a)
			i2 =  (delta**0.5)/(2*a)
			print("Les solutions sont :")
			print("x1 =", r1, "+ ("+str(i1)+")i")
			print("x2 =", r2, "+ ("+str(i2)+")i")


##### Exercice 5

def securise():
	ps = 2.3
	vs = 7.41

	while(True):
		try:
			p = float(input("Entrer la pression : "))
			v = float(input("Entrer le volume courant de l'enceintre : "))
			break
		except:
			print("Entrer un nombre !")	

	if(v>=ps and p>=ps):
		print("arret immediat")
	elif(p>=ps):
		print("Augmenter le volume de l'enceinte")
	elif(v>=vs):
		print("Diminuer le volume de l'enceintre")
	else:
		print("Tout va bien")

##### Test

demon = True
while(demon):
	print("\n============================")
	print("     Liste des scripts      ")
	print("============================\n")
	print("  1. Calculette")
	print("  2. Racine caree")
	print("  3. Evaluation")
	print("  4. Equation second degre")
	print("  5. Enceinte pressurisee")
	print("\n\n")
	choix = input("Choisir le script a executer : ")

	if(choix == "1"):
		calculette()
	elif(choix == "2"):
		racineCarre()
	elif(choix == "3"):
		evaluation()
	elif(choix == "4"):
		eqSecondDegre()
	elif(choix == "5"):
		securise()
	else:
		print("Choix non reconnu !")

	while(True):
		choix = input("Voulez vous recommencer ? [o/n] ")
		if(choix=="o" or choix=="O"):
			break
		elif(choix=="n" or choix=="N"):
			demon = False
			break
		else:
			print("Choix non reconnu !")
