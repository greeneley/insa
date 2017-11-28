# Thanh LUU STI 2019

from scipy import stats
from statsmodels.graphics.tsaplots import plot_acf
import random

# =======================================
#               FUNCTIONS
# =======================================


# =======================================
#               EXERCICE 1
# =======================================
def exo1(X0, a, c, m, size, affiche=False):
	"""Methode Congruentielle Lineaire
	"""

	# ====== VARIABLES ====== #
	X       = [X0]
	Ri      = -1
	periode = -1
	trouve  = False

	# ====== ALGORITHME ====== #
	for i in range(1, size):
		X.append(0.0) # C'est la valeur Xi
		X[i] = (a*X[i-1] + c) % m
		Ri = X[i] / m

		if( (X[i] == X0) and not(trouve) ):
			periode = i
			trouve  = True
		#END_IF

		if(affiche):
			print(Ri)
		#END_IF
	#END_FOR

	# ====== OUTPUT ====== #
	print("Periode(%d, %d, %d, %d) = %d" % (X[0], a, c, m, periode))
	return X
#END_DEF


# =======================================
#               EXERCICE 2
# =======================================
def exo2(m1, affiche, *generateurs):
	"""Methode de Congruences lineaires combinees
	"""

	# ====== VARIABLES ====== #
	X       = [0.0]
	nb_gen  = len(generateurs)
	nb_Xi   = len(generateurs[0])
	Xi      = -1
	Ri      = -1
	periode = -1
	trouve  = False

	# ====== ALGORITHME ====== #
	for i in range(nb_Xi):
		for j in range(1, nb_gen):
			X[i] += ( ( (-1)**(j-1) ) * generateurs[j][i] )
		#END_FOR

		X[i] %= (m1 - 1)

		if(X[i] > 0):
			Ri = X[i] / m1
		else:
			Ri = (m1 - 1) / m1
		#END_IF

		if( (X[i] == X[0]) and not(trouve) and i!=0 ):
			periode = i
			trouve  = True
		#END_IF

		if(affiche):
			print(Ri)
		#END_IF

		X.append(0.0)
	#END_FOR_i

	# ====== OUTPUT ====== #
	print("Periode(Xk, m1 = %d) = %d" % (m1, periode))
	return 0
#END_DEF


# =======================================
#               EXERCICE 3
# =======================================

# =======================================
#                  MAIN
# =======================================

if(__name__ == '__main__'):
	print("====== EXERCICE 1 ======")
	exo1(99, 798, 394, 5967, 543)
	exo1(1, 3, 0, 7, 543)
	print("")

	print("====== EXERCICE 2 ======")
	X1 = exo1(2, 3, 0, 13, 100)
	X2 = exo1(5, 5, 0, 11, 100)
	print("")
	exo2(13, False, X1, X2)

	print("====== EXERCICE 3 ======")
	print("====== EXERCICE 4 ======")
	print("====== EXERCICE 5 ======")
#END_IF