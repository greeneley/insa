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

class LoiTriangulaire(object):
	"""Retranscrit la loi de distribution Triangulaire(a, b, c)
	"""

	def __init__(self, a, b, c):
		"""Constructeur de l'objet LoiTriangulaire
			
			@param float a Borne inferieure de la loi.
			@param float b Borne superieure de la loi.
			@param float c Valeur critique de la borne tel que a < c < b
		"""
		self._a = a
		self._b = b
		self._c = c
	#END_DEF


	def fdr(self, x):
		"""Fonction de repartition de la Loi appliquee a une variable x

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""

		if(x < self._a):
			return 0
		elif(self._b < x):
			return 1
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


	def __fdr_inf(self, x):
		"""Fonction de repartition de la Loi en supposant a < x < c

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""
		num    = (x - self._a) * (x - self._a)
		denom  = (self._b - self._a) * (self._c - self._a)

		return num / denom
	#END_DEF


	def fri(self, u):
		"""Fonction de repartition inverse de la Loi appliquee a une variable x

			@param  float u      Valeur de [0,1] tel que u = fdr(x), ou x dans [a,b]
			
			@return float result La valeur x tel que u = fdr(x)
			@return None         Si u n'est pas compris dans [0, 1]
		"""

		if(u == 1):
			return 4.0
		elif( (u < 0) or (u > 1)):
			return None
		#END_IF

		result = self._a + sqrt(u * (self._b-self._a) * (self._c-self._a))
		if(u == self.__fdr_inf(result)):
			return result
		#END_IF

		result = self._b - sqrt((1-u) * (self._b-self._a) * (self._b-self._c))
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
			result = None

		return result
	#END_DEF


	def fri(self, x):
		"""Fonction de repartition inverse de la Loi appliquee a une variable x

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""
	#END_DEF
#END_CLASS


class LoiBernoulli(object):
	"""Retranscrit la loi de distribution Triangulaire(a, b, c)
	"""

	def __init__(self, a, b, c):
		"""Constructeur de l'objet LoiTriangulaire
			
			@param float a Borne inferieure de la loi.
			@param float b Borne superieure de la loi.
			@param float c Valeur critique de la borne tel que a < c < b
		"""
	#END_DEF


	def fdr(self, x):
		"""Fonction de repartition de la Loi appliquee a une variable x

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""
	#END_DEF


	def __fdr_inf(self, x):
		"""Fonction de repartition de la Loi en supposant a < x < c

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""
	#END_DEF


	def fri(self, u):
		"""Fonction de repartition inverse de la Loi appliquee a une variable x

			@param  float u      Valeur de [0,1] tel que u = fdr(x), ou x dans [a,b]
			
			@return float result La valeur x tel que u = fdr(x)
			@return None         Si u n'est pas compris dans [0, 1]
		"""
	#END_DEF
#END_CLASS

class LoiGeometrique(object):
	"""Retranscrit la loi de distribution Triangulaire(a, b, c)
	"""

	def __init__(self, a, b, c):
		"""Constructeur de l'objet LoiTriangulaire
			
			@param float a Borne inferieure de la loi.
			@param float b Borne superieure de la loi.
			@param float c Valeur critique de la borne tel que a < c < b
		"""
	#END_DEF


	def fdr(self, x):
		"""Fonction de repartition de la Loi appliquee a une variable x

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""
	#END_DEF


	def __fdr_inf(self, x):
		"""Fonction de repartition de la Loi en supposant a < x < c

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""
	#END_DEF


	def fri(self, u):
		"""Fonction de repartition inverse de la Loi appliquee a une variable x

			@param  float u      Valeur de [0,1] tel que u = fdr(x), ou x dans [a,b]
			
			@return float result La valeur x tel que u = fdr(x)
			@return None         Si u n'est pas compris dans [0, 1]
		"""
	#END_DEF
#END_CLASS
# ==========================================
#                  TESTS
# ==========================================



if __name__ == '__main__':

	# ===== VARIABLES ===== #
	Triangulaire = LoiTriangulaire(0, 5, 4)

	# ===== ALGORITHME ===== #
	print("===== TRIANGULAIRE(0, 5, 4) =====")
	tab_tri = []
	for i in range(NOMBRE_ECHANTILLON):
		x = random.uniform(0, 5)
		result = Triangulaire.fdr(x)
		data = (x,result, Triangulaire.fri(result))
		tab_tri.append(data)
	#END_FOR

	for val in tab_tri:
		print(val)
#END_IF