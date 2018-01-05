#!/usr/bin/python3.5

# ==========================================
#                 IMPORTS
# ==========================================
import random

from math import sqrt, exp, log
from math import isclose

# ==========================================
#                CONSTANTES
# ==========================================
NOMBRE_ECHANTILLON = 1000

# === TRIANGULAIRE
TOLERANCE_TRIANGLE = 1e-15

# === EXPONENTIELLE
TOLERANCE_EXPONENTIELLE = 1e-15


# ==========================================
#                  TESTS
# ==========================================

class Test(object):
	"""Classe personnelle qui permet de faire les tests de chaque exo
	"""

	#bern(0.3)
	#geo(n=10,p=0.8)

	def exo1_tri(self, a=0, b=5, c=4):
		# ===== VARIABLES ===== #
		Triangulaire  = LoiTriangulaire(a, b, c)

		# ===== ALGORITHME ===== #
		print("===== TRIANGULAIRE(%d, %d, %d) =====" % (a, b, c))
		tab_tri = []
		for i in range(NOMBRE_ECHANTILLON):
			u     = random.uniform(0, 1)
			x     = Triangulaire.fri(u)
			check = Triangulaire.fdr(x)
			data  = (u, x, check, isclose(u, check, rel_tol=TOLERANCE_TRIANGLE))
			tab_tri.append(data)
		#END_FOR

		# ===== OUTPUT ===== #
		for tab in tab_tri:
			print(tab)
		#END_FOR	
	#END_DEF

	def exo1_exp(self, var_lambda=3):
		# ===== VARIABLES ===== #
		Exponentielle = LoiExponentielle(var_lambda)

		# ===== ALGORITHME ===== #
		print("===== EXPONENTIELLE(%d) =====" % (var_lambda))
		tab_tri = []
		for i in range(NOMBRE_ECHANTILLON):
			u     = random.uniform(0, 1)
			x     = Exponentielle.fri(u)
			check = Exponentielle.fdr(x)
			data  = (u, x, check, isclose(u, check, rel_tol=TOLERANCE_EXPONENTIELLE))
			tab_tri.append(data)
		#END_FOR

		# ===== OUPUT ===== #
		for tab in tab_tri:
			print(tab)
		#END_FOR
	#END_DEF
#END_CLASS


# ==========================================
# >>>>>>>>>>>>>> EXERCICE 1 <<<<<<<<<<<<<<<<
# ==========================================

# ==========================================
#            TRIANGULAIRE(a, b, c)
# ==========================================

class LoiTriangulaire(object):
	"""Retranscrit la loi de distribution Triangulaire(a, b, c)
	"""

	# =====================
	#     CONSTRUCTORS
	# =====================

	def __init__(self, a=0, b=1, c=0.5):
		"""Constructeur de l'objet LoiTriangulaire
			
			@param float a Borne inferieure de la loi.
			@param float b Borne superieure de la loi.
			@param float c Valeur critique de la borne tel que a < c < b
		"""
		self.__a = a
		self.__b = b
		self.__c = c
	#END_DEF

	# =====================
	#      PARAMETERS
	# =====================

	# ===================== self.__a
	@property
	def a(self):
		return self.__a

	@a.setter
	def a(self, value):
		if(value > self.__b):
			print("Erreur de valeur : a > b")
		else:
			self.__a = value


	# ===================== self.__b
	@property
	def b(self):
		return self.__b

	@b.setter
	def b(self, value):
		if(value < self.__a):
			print("Erreur de valeur : b < a")
		else:
			self.__b = value


	# ===================== self.__c
	@property
	def c(self):
		return self.__c

	@c.setter
	def c(self, value):
		if( (value < self.__a) or (value > self.__b)):
			print("Erreur de valeur ! Il faut a < c < b")
		else:
			self.__c = value


	# =====================
	#       METHODS
	# =====================

	def fdr(self, x):
		"""Fonction de repartition de la Loi appliquee a une variable x

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""

		if(x < self.a):
			return 0
		elif(self.b < x):
			return 1
		#END_IF

		if( (self.a < x) and (x < self.c) ):
			num    = (x - self.a) * (x - self.a)
			denom  = (self.b - self.a) * (self.c - self.a)
			result = num / denom
		else: #( c < x < b )
			num = (self.b - x) * (self.b - x)
			denom = (self.b - self.a) * (self.b - self.c)
			result = 1.0 - (num / denom)
		#END_IF

		return result
	#END_DEF


	def __fdr_inf(self, x):
		"""Fonction de repartition de la Loi en supposant a < x < c

			@param  float x      Valeur a laquelle appliquer la fonction. a < x < b
			
			@return float result Resultat de la fonction.
		"""
		num    = (x - self.a) * (x - self.a)
		denom  = (self.b - self.a) * (self.c - self.a)
		result = num / denom

		return result
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

		result = self.a + sqrt(u * (self.b-self.a) * (self.c-self.a))

		if(isclose(u, self.fdr(result), rel_tol=TOLERANCE_TRIANGLE)):
			return result
		#END_IF
		result = self.b - sqrt((1.0-u) * (self.b-self.a) * (self.b-self.c))

		return result
	#END_DEF
#END_CLASS


# ==========================================
#           EXPONENTIELLE(lambda)
# ==========================================

class LoiExponentielle(object):
	"""Retranscrit la loi de distribution Exponentielle(lambda)
	"""

	# =====================
	#     CONSTRUCTORS
	# =====================
	def __init__(self, var_lambda):
		"""
			@param float lambda Parametre de la fonction exponentielle
		"""
		self.__l = var_lambda
	#END_DEF


	# =====================
	#      PROPERTIES
	# =====================
	@property
	def l(self):
		return self.__l

	@l.setter
	def l(self, value):
		if(value <= 0):
			print("Erreur de valeur. Condition : lambda > 0")
		else:
			self.__l = value


	# =====================
	#       METHODS
	# =====================

	def fdr(self, x):
		"""Fonction de repartition de la Loi appliquee a une variable x

			@param  float x      Valeur a laquelle appliquer la fonction. x > 0
			
			@return float result Resultat de la fonction.
		"""

		if(x < 0):
			return 0.0
		result = 1 - exp( (-self.l) * x )

		return result
	#END_DEF


	def fri(self, u):
		"""Fonction de repartition inverse de la Loi appliquee a une variable u.

			@param  float u      Valeur a laquelle appliquer la fonction. 0 <= u <= 1
			
			@return float result Resultat de la fonction.
			@return None         Si les donnees entrees ne valident pas les conditions d'execution.
		"""

		if( (u < 0) or (u > 1) ):
			print("Erreur de valeur. Condition : 0 <= u <= 1")
			return None

		result = log(1 - u) / (-self.l)
		return result
	#END_DEF


	def get_esperance():
		"""Renvoie l'esperance de la loi exponentielle.

			@return float value La valeur de l'esperance.
		"""
		return 1.0 / self.l
	#END_DEF


	def get_variance():
		"""Renvoie la variance de la loi exponentielle.

			@return float value La valeur de la variance.
		"""
		return 1.0 / (self.l * self.l)
	#END_DEF
#END_CLASS


# ==========================================
#              BERNOULLI(p)
# ==========================================

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


# ==========================================
#            GEOMETRIQUE(n, p)
# ==========================================

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
#              UNIFORME(a, b)
# ==========================================

class LoiUniforme(object):
	"""Retranscrit la loi de distribution Uniforme(a, b)
	"""

	# =====================
	#     CONSTRUCTORS
	# =====================
	def __init__(self, a, b):
		"""
			@param float a Borne inferieure de la fonction exponentielle
			@param float b Borne superieure de la fonction exponentielle
		"""

		if(a > b):
			printf("Erreur de definition. Condition : a <= b")
			raise ValueError
		else:
			self.__a = a
			self.__b = b
		#END_IF
	#END_DEF


	# =====================
	#      PROPERTIES
	# =====================

	# ===================== self.__a
	@property
	def a(self):
		return self.__a

	@a.setter
	def a(self, value):
		if(value > self.__b):
			print("Erreur de definition. Condition : a <= b")
		else:
			self.__a = value

	# ===================== self.__b
	@property
	def b(self):
		return self.__b

	@b.setter
	def b(self, value):
		if(value < self.__a):
			print("Erreur de definition. Condition : a <= b")
		else:
			self.__b = value


	# =====================
	#       METHODS
	# =====================

	def get_esperance():
		"""Renvoie la valeur de l'esperance de la loi uniforme continue(a,b).

			@return float value La valeur de l'esperance.
		"""
		return (self.a + self.b) / 2.0
	#END_DEF


	def get_variance():
		"""Renvoie la valeur de la variance de la loi uniforme continue(a,b).

			@return float value La valeur de la variance.
		"""
		return (self.b - self.a) * (self.b - self.a) / 12.0
	#END_DEF
#END_CLASS


# ==========================================
#              NORMALE(mu, sigsqr)
# ==========================================

class LoiNormale(object):
	"""Retranscrit la loi de distribution Normale(mu, sigsqr)
	"""

	# =====================
	#     CONSTRUCTORS
	# =====================
	def __init__(self, moyenne, variance):
		"""
			@param float moyenne  Parametre 'moyenne' de la loi normale.
			@param float variance Parametre 'variance' de la loi normale.
		"""
		self.__mu     = moyenne
		self.__sigsqr = variance
	#END_DEF


	# =====================
	#      PROPERTIES
	# =====================

	# ===================== self.__mu
	@property
	def mu(self):
		return self.__mu

	@mu.setter
	def mu(self, value):
		self.__mu = value

	# ===================== self.__sigsqr
	@property
	def sigsqr(self):
		return self.__sigsqr

	@sigsqr.setter
	def sigsqr(self, value):
		self.__sigsqr = value


	# =====================
	#       METHODS
	# =====================

	def get_esperance():
		"""Renvoie l'esperance de la loi normale.

			@return float value La valeur de l'esperance.
		"""
		return self.mu
	#END_DEF


	def get_variance():
		"""Renvoie la variance de la loi normale.

			@return float value La valeur de la variance.
		"""
		return self.sigsqr
	#END_DEF
#END_CLASS

# ==========================================
#                  MAIN
# ==========================================

if(__name__ == '__main__'):
	#test = Test()
	#test.exo1_tri(0, 5, 4)
	#test.exo1_exp(3)
	pass
#END_IF