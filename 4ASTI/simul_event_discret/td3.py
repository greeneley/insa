#!/usr/bin/python3.5

# ==========================================
#                  IMPORTS
# ==========================================
import random

from math import sqrt

# ==========================================
#                 CONSTANTES
# ==========================================
NOMBRE_ECHANTILLON = 1000

# === TRIANGULAIRE
CONST_A = 0
CONST_B = 5
CONST_C = 4

# ==========================================
#                  EXERCICE 1
# ==========================================

def uniforme(x):
	pass
#END_DEF

class LoiTriangulaire(object):
	"""Retranscrit la loi de distribution Triangulaire(a, b, c)

		param

	LONGUE
	"""

	def __init__(self, a, b, c):
		"""Constructeur de l'objet MaClasse"""
		self._a = a
		self._b = b
		self._c = c
	#END_DEF

	def fdr(self, x):
		"""Fonction de repartition de la Loi appliquee a une variable x

			@param  int   x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""

		if(not( (self._a < x) and (x < self._b) )):
			return 0
		#END_IF

		if( (self._a < x) and (x < self._c) ):
			num    = (x - self._a) * (x - self._a)
			denom  = (self._b - self._a) * (self._c - self._a)
			result = num / denom
		else: #( c < x < b )
			num = (self._b - x) * (self._b - x)
			denom = (self._b - self._a) * (self._b - self._c)
			result = 1.0 - (num / denom)
		#END_IF

		return result
	#END_DEF


	def fri(self, x):
		"""Fonction de repartition inverse de la Loi appliquee a une variable x

			@param  int   x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""

		u = self.fdr(x)
		result = 1 - sqrt(1 - u)
		return result
	#END_DEF
#END_CLASS


class LoiExponentielle(object):
	"""Retranscrit la loi de distribution Triangulaire(a, b, c)

		@param float lambda Parametre de la fonction exponentielle
	"""

	def __init__(self, var_lambda):
		"""Constructeur de l'objet MaClasse"""
		self._lambda = var_lambda
	#END_DEF

	def fdr(self, x):
		"""Fonction de repartition de la Loi appliquee a une variable x

			@param  int   x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""

		if(x >= 0):
			result = 1 - 

		return result
	#END_DEF


	def fri(self, x):
		"""Fonction de repartition inverse de la Loi appliquee a une variable x

			@param  int   x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""

		"""
		   ATTENTION : erreur avec la FRI
		   Il faut la calculer de maniere generale
		   C'est un cas particulier ici
		"""
		u = self.fdr(x)
		#result = 1 - sqrt(1 - u)
		return result
	#END_DEF
#END_CLASS



if __name__ == '__main__':

	# ===== VARIABLES ===== #
	Triangulaire = LoiTriangulaire(0, 5, 4)

	# ===== ALGORITHME ===== #
	print("===== TRIANGULAIRE(0, 5, 4) =====")
	tab_tri = []
	for i in range(NOMBRE_ECHANTILLON):
		x = random.uniform(CONST_A, CONST_B)
		tab_tri.append(Triangulaire.fri(x))		
	#END_FOR

#END_IF